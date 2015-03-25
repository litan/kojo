/*
 * Copyright (C) 2010 Lalit Pant <pant.lalit@gmail.com>
 *
 * The contents of this file are subject to the GNU General Public License
 * Version 3 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.gnu.org/copyleft/gpl.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 */

package net.kogics.kojo
package lite

import java.awt.Paint
import java.awt.TexturePaint
import java.awt.Toolkit
import java.awt.geom.Rectangle2D

import javax.swing.JComponent

import scala.language.implicitConversions

import net.kogics.kojo.mathworld.MathWorld
import net.kogics.kojo.story.HandlerHolder
import net.kogics.kojo.turtle.TurtleWorldAPI
import net.kogics.kojo.util.Read
import net.kogics.kojo.util.UserCommand
import net.kogics.kojo.xscala.CodeCompletionUtils
import net.kogics.kojo.xscala.Help
import net.kogics.kojo.xscala.RepeatCommands
import net.kogics.kojo.xscala.ScalaCodeRunner

import core.Voice
import story.HandlerHolder
import story.IntHandlerHolder
import story.StringHandlerHolder
import story.VoidHandlerHolder
import util.Read
import util.Throttler
import util.Utils

// a static instance is needed for the compiler prefix code 
object Builtins {
  @volatile var instance: Builtins = _
}

class Builtins(
  val TSCanvas: DrawingCanvasAPI,
  val Tw: TurtleWorldAPI,
  val Staging: staging.API,
  val Mw: MathWorld,
  val D3: d3.API,
  storyTeller: story.StoryTeller,
  mp3player: music.KMp3,
  fuguePlayer: music.FuguePlayer,
  val kojoCtx: core.KojoCtx,
  scalaCodeRunner: core.CodeRunner) extends CoreBuiltins with RepeatCommands { builtins =>
  Builtins.instance = this
  import language.implicitConversions
  val tCanvas = TSCanvas.tCanvas

  val Costume = new Tw.Costume
  val Background = new Tw.Background
  val Sound = new Tw.Sound

  UserCommand.addCompletion("repeat", "(${n}) {\n    ${cursor}\n}")
  UserCommand.addSynopsis("repeat(n) {} - Repeats the commands within braces n number of times.")
  UserCommand.addCompletion("repeati", "(${n}) { i => \n    ${cursor}\n}")
  UserCommand.addSynopsis("repeati(n) {} - Repeats the commands within braces n number of times. The current repeat index is available within the braces.")
  UserCommand.addCompletion("repeatWhile", "(${condition}) {\n    ${cursor}\n}")
  UserCommand.addSynopsis("repeatWhile(cond) {} - Repeats the commands within braces while the given condition is true.")
  UserCommand.addCompletion("repeatUntil", "(${condition}) {\n    ${cursor}\n}")
  UserCommand.addSynopsis("repeatUntil(cond) {} - Repeats the commands within braces until the given condition is true.")

  def showScriptInOutput() = kojoCtx.showScriptInOutput()
  UserCommand("showScriptInOutput", Nil, "Enables the display of scripts in the output window when they run.")

  def hideScriptInOutput() = kojoCtx.hideScriptInOutput()
  UserCommand("hideScriptInOutput", Nil, "Stops the display of scripts in the output window.")

  def showVerboseOutput() = kojoCtx.showVerboseOutput()
  UserCommand("showVerboseOutput", Nil, "Enables the display of output from the Scala interpreter. By default, output from the interpreter is shown only for single line scripts.")

  def hideVerboseOutput() = kojoCtx.hideVerboseOutput()
  UserCommand("hideVerboseOutput", Nil, "Stops the display of output from the Scala interpreter.")
  UserCommand.addSynopsisSeparator()

  def retainSingleLineCode() = {}
  def clearSingleLineCode() = {}

  def version = println("Scala " + scala.tools.nsc.Properties.versionString)
  UserCommand.addSynopsis("version - Displays the version of Scala being used.")

  def print(obj: Any) {
    // Runs on Actor pool (interpreter) thread
    kojoCtx.kprintln("%s" format (obj))
    Throttler.throttleHard()
  }
  UserCommand.addCompletion("print", List("obj"))

  def println(obj: Any): Unit = print("%s\n" format (obj))
  UserCommand.addCompletion("println", List("obj"))
  UserCommand.addSynopsis("println(obj) or print(obj) - Displays the given object as a string in the output window.")

  def readln(prompt: String): String = kojoCtx.readInput(prompt)
  UserCommand("readln", List("promptString"), "Displays the given prompt in the output window and reads a line that the user enters.")

  def readInt(prompt: String): Int = readln(prompt).toInt
  UserCommand("readInt", List("promptString"), "Displays the given prompt in the output window and reads an Integer value that the user enters.")

  def readDouble(prompt: String): Double = readln(prompt).toDouble
  UserCommand("readDouble", List("promptString"), "Displays the given prompt in the output window and reads a Double-precision Real value that the user enters.")

  UserCommand("random", List("upperBound"), "Returns a random Integer between 0 (inclusive) and upperBound (exclusive).")
  UserCommand("randomDouble", List("upperBound"), "Returns a random Double-precision Real between 0 (inclusive) and upperBound (exclusive).")

  UserCommand("color", List("red", "green", "blue"), "Creates a new color based on the specified red, green, and blue levels.")

  def setAstStopPhase(phase: String): Unit = kojoCtx.setAstStopPhase(phase)
  def astStopPhase = kojoCtx.astStopPhase
  UserCommand.addSynopsis("astStopPhase - Gets the compiler phase value for AST printing.")
  UserCommand("setAstStopPhase", List("stopAfterPhase"), "Sets the compiler phase value for AST printing.")

  def stClear() {
    storyTeller.clear()
  }
  UserCommand.addSynopsisSeparator()
  UserCommand("stClear", Nil, "Clears the Story Teller Window.")

  type Para = story.Para
  val Para = story.Para
  type Page = story.Page
  val Page = story.Page
  type IncrPage = story.IncrPage
  val IncrPage = story.IncrPage
  type Story = story.Story
  val Story = story.Story
  type StoryPage = story.Viewable
  UserCommand.addCompletion("Story", List("pages"))

  def stPlayStory(st: story.Story) {
    storyTeller.playStory(st)
  }
  UserCommand("stPlayStory", List("story"), "Play the given story.")

  def stFormula(latex: String, size: Int = 18, cssColor: String = null) =
    <div style={ "text-align:center;margin:6px;" }>
      { if (cssColor != null) { <img src={ xml.Unparsed(story.CustomHtmlEditorKit.latexPrefix + latex) }
            style={ "color:%s" format(cssColor) }
            height={ "%d" format(size) } /> } else { <img src={ xml.Unparsed(story.CustomHtmlEditorKit.latexPrefix + latex) }
            height={ "%d" format(size) } /> } }
    </div>
  UserCommand("stFormula", List("latex"), "Converts the supplied latex string into html that can be displayed in the Story Teller Window.")

  def stPlayMp3(mp3File: String) {
    storyTeller.playMp3(mp3File)
  }
  UserCommand("stPlayMp3", List("fileName"), "Plays the specified MP3 file.")

  def stPlayMp3Loop(mp3File: String) {
    storyTeller.playMp3Loop(mp3File)
  }
  UserCommand("stPlayMp3Loop", List("fileName"), "Plays the specified MP3 file in the background.")

  def stAddButton(label: String)(fn: => Unit) {
    storyTeller.addButton(label)(fn)
  }
  UserCommand.addCompletion("stAddButton", " (${label}) {\n    ${cursor}\n}")
  UserCommand.addSynopsis("stAddButton(label) {code} - Adds a button with the given label to the Story Teller Window, and runs the supplied code when the button is clicked.")

  def stAddField(label: String, default: Any) {
    storyTeller.addField(label, default)
  }
  UserCommand("stAddField", List("label", "default"), "Adds an input field with the supplied label and default value to the Story Teller Window.")

  implicit val StringRead = util.Read.StringRead
  implicit val DoubleRead = util.Read.DoubleRead
  implicit val IntRead = util.Read.IntRead
  import util.Read

  def stFieldValue[T](label: String, default: T)(implicit reader: Read[T]): T = {
    storyTeller.fieldValue(label, default)
  }
  UserCommand("stFieldValue", List("label", "default"), "Gets the value of the specified field.")

  def stShowStatusMsg(msg: String) {
    storyTeller.showStatusMsg(msg)
  }
  UserCommand("stShowStatusMsg", List("msg"), "Shows the specified message in the Story Teller status bar.")

  def stSetScript(code: String) = kojoCtx.setScript(code)
  UserCommand("stSetScript", List("code"), "Copies the supplied code to the script editor.")

  def stRunCode(code: String) = interpret(code)
  UserCommand("stRunCode", List("code"), "Runs the supplied code (without copying it to the script editor).")

  def stClickRunButton() = kojoCtx.clickRun()
  UserCommand("stClickRunButton", Nil, "Simulates a click of the run button")

  def stClickRunInterpreterButton() = kojoCtx.clickInterpreterRun()
  UserCommand("stClickRunInterpreterButton", Nil, "Simulates a click of the run button")

  def stClickRunAsWorksheetButton() = kojoCtx.clickWorksheetRun()
  UserCommand("stClickRunAsWorksheetButton", Nil, "Simulates a click of the 'run as worksheet' button")

  def stShowStatusError(msg: String) {
    storyTeller.showStatusError(msg)
  }
  UserCommand("stShowStatusError", List("msg"), "Shows the specified error message in the Story Teller status bar.")

  def stNext() = storyTeller.nextPage()
  UserCommand("stNext", Nil, "Moves the story to the next page/view.")

  def stInsertCodeInline(code: String) = kojoCtx.insertCodeInline(code)
  def stInsertCodeBlock(code: String) = kojoCtx.insertCodeBlock(code)
  def stSetStorytellerWidth(width: Int) = kojoCtx.setStorytellerWidth(width)
  def stFrame = kojoCtx.frame
  def stSetUserControlsBg(color: Color) = storyTeller.setUserControlsBg(color)
  def stCanvasLocation = kojoCtx.canvasLocation
  def stDisableNextButton() = storyTeller.disableNextButton()
  def stEnableNextButton() = storyTeller.enableNextButton()

  UserCommand.addSynopsisSeparator()

  def help() = {
    println("""You can press Ctrl-Space in the script window at any time to see available commands and functions.

Here's a partial list of the available commands:
              """ + UserCommand.synopses)
  }

  type Melody = core.Melody
  val Melody = core.Melody

  type Rhythm = core.Rhythm
  val Rhythm = core.Rhythm

  val MusicScore = core.Score

  def playMusic(voice: Voice, n: Int = 1) {
    fuguePlayer.playMusic(voice, n)
  }
  UserCommand("playMusic", List("score"), "Plays the specified melody, rhythm, or score.")

  def playMusicUntilDone(voice: Voice, n: Int = 1) {
    fuguePlayer.playMusicUntilDone(voice, n)
  }
  UserCommand("playMusicUntilDone", List("score"), "Plays the specified melody, rhythm, or score, and waits till the music finishes.")

  def playMusicLoop(voice: Voice) {
    fuguePlayer.playMusicLoop(voice)
  }
  UserCommand("playMusicLoop", List("score"), "Plays the specified melody, rhythm, or score in the background - in a loop.")

  UserCommand("textExtent", List("text", "fontSize"), "Determines the size/extent of the given text fragment for the given font size.")

  def runInBackground(code: => Unit) = Utils.runAsyncMonitored(code)
  UserCommand.addSynopsis("runInBackground", List("code"), "Runs the given code in the background, concurrently with other code that follows right after this command.")

  def runInGuiThread(code: => Unit) = Utils.runInSwingThread(code)
  UserCommand.addSynopsis("runInGuiThread", List("code"), "Runs the given code in the the GUI Thread, concurrently with other code that follows right after this command.")

  def schedule(s: Double)(code: => Unit) = Utils.schedule(s)(code)

  // undocumented
  def color(rgbHex: Int) = new Color(rgbHex)
  def clearOutput() = kojoCtx.clearOutput()

  def interpret(code: String) {
    scalaCodeRunner.runCode(code)
  }

  def resetInterpreter() {
    scalaCodeRunner.resetInterp()
  }

  // for debugging only!
  def kojoInterp = scalaCodeRunner.asInstanceOf[ScalaCodeRunner].kojointerp
  def pcompiler = scalaCodeRunner.asInstanceOf[ScalaCodeRunner].pcompiler
  def compiler = scalaCodeRunner.asInstanceOf[ScalaCodeRunner].compiler

  def reimportBuiltins() {
    interpret("import TSCanvas._; import Tw._")
  }
  def reimportDefaults() = reimportBuiltins()

  import story.{ HandlerHolder, IntHandlerHolder, StringHandlerHolder, VoidHandlerHolder }
  implicit def toIhm(handler: Int => Unit): HandlerHolder[Int] = new IntHandlerHolder(handler)
  implicit def toShm(handler: String => Unit): HandlerHolder[String] = new StringHandlerHolder(handler)
  implicit def toVhm(handler: () => Unit): HandlerHolder[Unit] = new VoidHandlerHolder(handler)

  def stAddLinkHandler[T](name: String, story: Story)(implicit hm: HandlerHolder[T]) {
    storyTeller.addLinkHandler(name, story)(hm)
  }

  def stAddLinkEnterHandler[T](name: String, story: Story)(implicit hm: HandlerHolder[T]) {
    storyTeller.addLinkEnterHandler(name, story)(hm)
  }

  def stAddLinkExitHandler[T](name: String, story: Story)(implicit hm: HandlerHolder[T]) {
    storyTeller.addLinkExitHandler(name, story)(hm)
  }

  def stAddUiComponent(c: JComponent) {
    storyTeller.addUiComponent(c)
  }

  def stAddUiBigComponent(c: JComponent) {
    storyTeller.addUiBigComponent(c)
  }

  private val urlHandler = new story.LinkListener(storyTeller)
  def stGotoUrl(url: String) = urlHandler.gotoUrl(new java.net.URL(url))

  def stOnStoryStop(story: Story)(fn: => Unit) {
    storyTeller.onStop(story, fn)
  }

  def stHelpFor(instr: String) = Help(instr)

  type Painter = picture.Painter
  type Pic = picture.Pic
  type Pic0 = picture.Pic0
  val picRow = HPics
  val picCol = VPics
  val picStack = GPics

  val rotp = picture.rotp _
  val opac = picture.opac _
  val hue = picture.hue _
  val sat = picture.sat _
  val brit = picture.brit _
  val offset = picture.offset _
  val flip = picture.flipY
  val flipY = picture.flipY
  val flipX = picture.flipX
  val axes = picture.axesOn
  val fillColor = picture.fill _
  val penColor = picture.stroke _
  val penWidth = picture.strokeWidth _
  val deco = picture.deco _

  val spin = picture.spin _
  val reflect = picture.reflect _
  val row = picture.row _
  val col = picture.col _

  implicit val _picCanvas = tCanvas
  def pict(painter: Painter) = picture.Pic(painter)
  def PictureT(painter: Painter) = picture.Pic(painter)
  def Picture(fn: => Unit) = picture.Pic0 { t =>
    fn
  }
  def drawAndHide(pictures: Picture*) = pictures.foreach { p => p.draw(); p.invisible() }
  def show(pictures: Picture*) {
    throw new UnsupportedOperationException("Use draw(pic/s) instead of show(pic/s)")
  }

  def setRefreshRate(fps: Int) {
    require(fps >= 1 && fps <= 200, "FPS needs to be in the range: 1 to 200")
    kojoCtx.fps = fps
  }
  def stopAnimations() = kojoCtx.stopActivity()
  def stopAnimation() = kojoCtx.stopActivity()
  def isKeyPressed(key: Int) = staging.Inputs.isKeyPressed(key)
  def activateCanvas() = kojoCtx.activateDrawingCanvas()
  def activateEditor() = kojoCtx.activateScriptEditor()
  val hueMod = Utils.hueMod _
  val satMod = Utils.satMod _
  val britMod = Utils.britMod _

  type Vector2D = util.Vector2D
  val Vector2D = util.Vector2D

  def playMp3(mp3File: String) {
    mp3player.playMp3(mp3File)
  }
  UserCommand("playMp3", List("fileName"), "Plays the specified MP3 file.")

  def playMp3Loop(mp3File: String) {
    mp3player.playMp3Loop(mp3File)
  }
  UserCommand("playMp3Loop", List("fileName"), "Plays the specified MP3 file in the background.")

  def canvasBounds = tCanvas.cbounds
  def setBackground(c: Paint) = tCanvas.setCanvasBackground(c)

  def isMp3Playing = mp3player.isMusicPlaying
  def isMusicPlaying = fuguePlayer.isMusicPlaying
  def stopMp3() = mp3player.stopMp3()
  def stopMusic() = fuguePlayer.stopMusic()
  def newMp3Player = new music.KMp3(kojoCtx)
  def addCodeTemplates(lang: String, templates: Map[String, String]) {
    CodeCompletionUtils.addTemplates(lang, templates)
  }
  def addHelpContent(lang: String, content: Map[String, String]) {
    Help.addContent(lang, content)
  }

  def bounceVecOffStage(v: Vector2D, p: Picture): Vector2D = {
    import TSCanvas._
    val stageparts = List(stageTop, stageBot, stageLeft, stageRight)
    p.collision(stageparts).map {
      _ match {
        case p if p == stageTop   => Vector2D(v.x, -v.y)
        case p if p == stageBot   => Vector2D(v.x, -v.y)
        case p if p == stageLeft  => Vector2D(-v.x, v.y)
        case p if p == stageRight => Vector2D(-v.x, v.y)
        case _                    => v
      }
    }.get
  }

  def bouncePicOffStage(p: Picture, v: Vector2D): Vector2D = {
    import TSCanvas._
    bouncePicOffPic(p, v, stageArea)
  }

  def bouncePicOffPic(pic: Picture, v: Vector2D, obj: Picture): Vector2D = {
    def collisionVector(p: Picture, p2: Picture) = {
      val pt = p.intersection(p2)
      p.picGeom.getCoordinates.sliding(2).find { cs =>
        pt.getCoordinates.find { c =>
          val xcheck = if (cs(0).x > cs(1).x)
            cs(0).x >= c.x && c.x >= cs(1).x
          else
            cs(0).x <= c.x && c.x <= cs(1).x

          val ycheck = if (cs(0).y > cs(1).y)
            cs(0).y >= c.y && c.y >= cs(1).y
          else
            cs(0).y <= c.y && c.y <= cs(1).y
          xcheck && ycheck
        }.isDefined
      } match {
        case Some(cs) =>
          Vector2D(cs(0).x - cs(1).x, cs(0).y - cs(1).y).normalize * v.magnitude
        case None =>
          Vector2D(randomDouble(1), randomDouble(1)).normalize * v.magnitude
      }
    }
    val cv = collisionVector(obj, pic)
    val ret = v.bounceOff(cv)
    ret
  }

  def switchToDefaultPerspective() = kojoCtx.switchToDefaultPerspective()
  def switchToDefault2Perspective() = kojoCtx.switchToDefault2Perspective()
  def switchToScriptEditingPerspective() = kojoCtx.switchToScriptEditingPerspective()
  def switchToWorksheetPerspective() = kojoCtx.switchToWorksheetPerspective()
  def switchToStoryViewingPerspective() = kojoCtx.switchToStoryViewingPerspective()
  def switchToHistoryBrowsingPerspective() = kojoCtx.switchToHistoryBrowsingPerspective()
  def switchToOutputStoryViewingPerspective() = kojoCtx.switchToOutputStoryViewingPerspective()

  private val fullScreenAction = kojoCtx.fullScreenCanvasAction()
  def toggleFullScreenCanvas() = fullScreenAction.actionPerformed(null)

  private val fullScreenOutputAction = kojoCtx.fullScreenOutputAction()
  def toggleFullScreenOutput() = fullScreenOutputAction.actionPerformed(null)

  def setOutputBackground(color: Color) = kojoCtx.setOutputBackground(color)
  def setOutputTextColor(color: Color) = kojoCtx.setOutputForeground(color)
  def setOutputTextFontSize(size: Int) = kojoCtx.setOutputFontSize(size)

  def setEditorTabSize(ts: Int) = kojoCtx.setEditorTabSize(ts)
  def setEditorFont(name: String) = kojoCtx.setEditorFont(name)
  def mouseX = staging.Inputs.mousePos.x
  def mouseY = staging.Inputs.mousePos.y
  def mousePosition = staging.Inputs.mousePos
  def screenDPI = kojoCtx.screenDPI
  def setScreenDPI(dpi: Int) { kojoCtx.screenDPI = dpi }
  def screenSize = Toolkit.getDefaultToolkit.getScreenSize
  def isScratchPad = kojoCtx.subKojo
  def isTracing = false

  def TexturePaint(file: String, x: Double, y: Double) = {
    val img = Utils.loadBufImage(file)
    new TexturePaint(img, new Rectangle2D.Double(x, y, img.getWidth, -img.getHeight))
  }

  val PShapes = PicShape
  //  implicit def p2ep(p: Picture) = PicShape.effectablePic(p)
  object PicShape {
    def text(s0: Any, fontSize: Int = 15) = picture.text(s0, fontSize)
    def rect(h: Double, w: Double) = picture.rect(h, w)
    def vline(l: Double) = picture.vline(l)
    def hline(l: Double) = picture.hline(l)
    def circle(r: Double) = picture.circle(r)
    def arc(r: Double, angle: Double) = picture.arc(r, angle)
    def image(fileName: String) = picture.image(fileName)
    def image(image: Image) = picture.image(image)
    def widget(component: JComponent) = picture.widget(component)
    def button(label: String)(fn: => Unit) = widget(Button(label)(fn))
    def effectablePic(pic: Picture) = picture.effectablePic(pic)
  }
  type Widget = JComponent
  type TextField[A] = widget.TextField[A]
  type TextArea = widget.TextArea
  type Label = widget.Label
  type Button = widget.Button
  type ToggleButton = widget.ToggleButton
  type DropDown[A] = widget.DropDown[A]
  type Slider = widget.Slider
  type RowPanel = widget.RowPanel
  type ColPanel = widget.ColPanel
  val TextField = widget.TextField
  val TextArea = widget.TextArea
  val Label = widget.Label
  val Button = widget.Button
  val ToggleButton = widget.ToggleButton
  val DropDown = widget.DropDown
  val Slider = widget.Slider
  val RowPanel = widget.RowPanel
  val ColPanel = widget.ColPanel
}
