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

import java.awt.{ Color => JColor }
import java.awt.{ Font => JFont }
import java.awt.GradientPaint
import java.awt.Paint
import java.awt.Toolkit
import java.util.concurrent.CountDownLatch

import javax.swing.JComponent

import net.kogics.kojo.mathworld.MathWorld
import net.kogics.kojo.story.HandlerHolder
import net.kogics.kojo.turtle.TurtleWorldAPI
import net.kogics.kojo.util.Read
import net.kogics.kojo.util.UserCommand
import net.kogics.kojo.xscala.CodeCompletionUtils
import net.kogics.kojo.xscala.Help
import net.kogics.kojo.xscala.RepeatCommands
import net.kogics.kojo.xscala.ScalaCodeRunner

import core.Rectangle
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
  kojoCtx: core.KojoCtx,
  scalaCodeRunner: core.CodeRunner) extends RepeatCommands { builtins =>
  Builtins.instance = this
  import language.implicitConversions
  val tCanvas = TSCanvas.tCanvas

  type Turtle = core.Turtle
  type Color = java.awt.Color
  type Font = java.awt.Font
  type Point = core.Point
  val Point = core.Point
  type PolyLine = kgeom.PolyLine
  val PolyLine = kgeom.PolyLine
  type Point2D = java.awt.geom.Point2D.Double
  def Point2D(x: Double, y: Double) = new java.awt.geom.Point2D.Double(x, y)

  val Random = new java.util.Random

  val blue = JColor.blue
  val red = JColor.red
  val yellow = JColor.yellow
  val green = JColor.green
  val orange = JColor.orange
  val purple = new Color(0x740f73)
  val pink = JColor.pink
  val brown = new Color(0x583a0b)
  val black = JColor.black
  val white = JColor.white
  val gray = JColor.gray
  val lightGray = JColor.lightGray
  val darkGray = JColor.darkGray
  val magenta = JColor.magenta
  val cyan = JColor.cyan
  
  val BoldFont = JFont.BOLD
  val PlainFont = JFont.PLAIN
  val ItalicFont = JFont.ITALIC

  val C = staging.KColor
  //  val Color = staging.KColor
  val noColor = C.noColor

  val Kc = new staging.KeyCodes
  val Costume = new Tw.Costume
  val Background = new Tw.Background
  val Sound = new Tw.Sound

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

  def random(upperBound: Int) = Random.nextInt(upperBound)
  UserCommand("random", List("upperBound"), "Returns a random Integer between 0 (inclusive) and upperBound (exclusive).")

  def randomDouble(upperBound: Int) = Random.nextDouble * upperBound
  UserCommand("randomDouble", List("upperBound"), "Returns a random Double-precision Real between 0 (inclusive) and upperBound (exclusive).")

  def color(r: Int, g: Int, b: Int) = new Color(r, g, b)
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

  def textExtent(text: String, fontSize: Int) = Utils.runInSwingThreadAndWait {
    val tnode = Utils.textNode(text, 0, 0, tCanvas.camScale, fontSize)
    val b = tnode.getFullBounds
    new Rectangle(new Point(b.x, b.y), new Point(b.x + b.width, b.y + b.height))
  }
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

  private val urlHandler = new story.LinkListener(storyTeller)
  def stGotoUrl(url: String) = urlHandler.gotoUrl(new java.net.URL(url))

  def stOnStoryStop(story: Story)(fn: => Unit) {
    storyTeller.onStop(story, fn)
  }

  def stHelpFor(instr: String) = Help(instr)

  type Picture = core.Picture
  type Painter = picture.Painter
  type Pic = picture.Pic
  //  val Pic = picture.Pic
  type Pic0 = picture.Pic0
  //  val Pic0 = picture.Pic0
  type HPics = picture.HPics
  val HPics = picture.HPics
  val picRow = HPics
  type VPics = picture.VPics
  val VPics = picture.VPics
  val picCol = VPics
  type GPics = picture.GPics
  val GPics = picture.GPics
  val picStack = GPics

  val rot = picture.rot _
  val rotp = picture.rotp _
  def scale(f: Double) = picture.scale(f)
  def scale(x: Double, y: Double) = picture.scale(x, y)
  val opac = picture.opac _
  val hue = picture.hue _
  val sat = picture.sat _
  val brit = picture.brit _
  val trans = picture.trans _
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
  def draw(pictures: Picture*) = pictures.foreach { _ draw () }
  def drawAndHide(pictures: Picture*) = pictures.foreach { p => p.draw(); p.invisible() }
  def show(pictures: Picture*) {
    throw new UnsupportedOperationException("Use draw(pic/s) instead of show(pic/s)")
  }

  def setRefreshRate(fps: Int) {
    require(fps >= 10 && fps <= 100, "FPS needs to be in the range: 10 to 100")
    kojoCtx.fps = fps
  }
  def animate(fn: => Unit) = tCanvas.animate(fn)
  def stopAnimation() = stopActivity()
  def stopActivity() = kojoCtx.stopActivity()
  def isKeyPressed(key: Int) = staging.Inputs.isKeyPressed(key)
  def activateCanvas() = kojoCtx.activateDrawingCanvas()
  def activateEditor() = kojoCtx.activateScriptEditor()
  def Color(r: Int, g: Int, b: Int, a: Int = 255) = new Color(r, g, b, a)
  def ColorG(x1: Double, y1: Double, c1: Color, x2: Double, y2: Double, c2: Color, cyclic: Boolean = false) = {
    new GradientPaint(x1.toFloat, y1.toFloat, c1, x2.toFloat, y2.toFloat, c2, cyclic)
  }
  def ColorHSB(h: Double, s: Double, b: Double) = java.awt.Color.getHSBColor((h / 360).toFloat, (s / 100).toFloat, (b / 100).toFloat)
  val hueMod = Utils.hueMod _
  val satMod = Utils.satMod _
  val britMod = Utils.britMod _

  type Vector2D = util.Vector2D
  val Vector2D = util.Vector2D

  def Font(name: String, size: Int, style: Int = JFont.PLAIN) = new Font(name, style, size)

  def playMp3(mp3File: String) {
    mp3player.playMp3(mp3File)
  }
  UserCommand("playMp3", List("fileName"), "Plays the specified MP3 file.")

  def playMp3Loop(mp3File: String) {
    mp3player.playMp3Loop(mp3File)
  }
  UserCommand("playMp3Loop", List("fileName"), "Plays the specified MP3 file in the background.")

  def installDir = Utils.installDir
  def canvasBounds = tCanvas.cbounds
  def setBackground(c: Paint) = tCanvas.setCanvasBackground(c)

  def isMp3Playing = mp3player.isMusicPlaying
  def isMusicPlaying = fuguePlayer.isMusicPlaying
  def stopMp3() = mp3player.stopMp3()
  def stopMusic() = fuguePlayer.stopMusic()
  def newMp3Player = new music.KMp3(kojoCtx)
  def onAnimationStop(fn: => Unit) = Staging.onAnimationStop(fn)
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

  def switchToDefaultPerspective() = kojoCtx.switchToDefaultPerspective()
  def switchToScriptEditingPerspective() = kojoCtx.switchToScriptEditingPerspective()
  def switchToWorksheetPerspective() = kojoCtx.switchToWorksheetPerspective()
  def switchToStoryViewingPerspective() = kojoCtx.switchToStoryViewingPerspective()
  def switchToHistoryBrowsingPerspective() = kojoCtx.switchToHistoryBrowsingPerspective()
  def switchToCanvasPerspective() = kojoCtx.switchToCanvasPerspective()

  private val fullScreenAction = kojoCtx.fullScreenCanvasAction()
  def toggleFullScreenCanvas() = fullScreenAction.actionPerformed(null)

  private val fullScreenOutputAction = kojoCtx.fullScreenOutputAction()
  def toggleFullScreenOutput() = fullScreenOutputAction.actionPerformed(null)

  def setOutputBackground(color: Color) = kojoCtx.setOutputBackground(color)
  def setOutputTextColor(color: Color) = kojoCtx.setOutputForeground(color)
  def setOutputTextFontSize(size: Int) = kojoCtx.setOutputFontSize(size)

  def epochTimeMillis = System.currentTimeMillis()
  def epochTime = System.currentTimeMillis() / 1000.0
  def countDownLatch(n: Int) = new CountDownLatch(n)
  def homeDir = Utils.homeDir
  def currentDir = Utils.currentDir

  def setEditorTabSize(ts: Int) = kojoCtx.setEditorTabSize(ts)
  def pause(secs: Double) = Thread.sleep((secs * 1000).toLong)
  def mouseX = staging.Inputs.mousePos.x
  def mouseY = staging.Inputs.mousePos.x
  def mousePosition = staging.Inputs.mousePos
  def screenDPI = kojoCtx.screenDPI
  def setScreenDPI(dpi: Int) { kojoCtx.screenDPI = dpi }
  def screenSize = Toolkit.getDefaultToolkit.getScreenSize
  def isScratchPad = kojoCtx.subKojo

  val PShapes = PicShape
  object PicShape {
    def text(s0: Any, fontSize: Int = 15) = picture.text(s0, fontSize)
    def rect(h: Double, w: Double) = picture.rect(h, w)
    def vline(l: Double) = picture.vline(l)
    def hline(l: Double) = picture.hline(l)
    def circle(r: Double) = picture.circle(r)
    def arc(r: Double, angle: Int) = picture.arc(r, angle)
  }

  object Gaming {
    import PicShape._
    def gamePanel(
      onStart: => Unit,
      onPause: => Unit,
      onStop: => Unit,
      onLevelUp: => Unit,
      onLevelDown: => Unit) = {
      import Tw._
      val FontSize = 20

      val buttonBg = Color(0, 0, 255, 100)
      val buttonPressedBg = Color(0, 255, 0, 127)

      def button(label: String)(fn: => Unit) = {
        val btn = PictureT { t =>
          import t._
          setPenFontSize(FontSize)
          val te = textExtent(label, FontSize)
          setFillColor(buttonBg)
          setPenColor(Color(255, 255, 255, 200))
          Utils.trect(te.height.toInt + 10, te.width.toInt + 10, t)
          penUp()
          forward(te.height + 5)
          right()
          forward(5)
          left()
          penDown()
          write(label)
        }
        btn.onMouseClick { (x, y) =>
          btn.setFillColor(buttonPressedBg)
          schedule(0.25) {
            btn.setFillColor(buttonBg)
          }
          fn
        }
        btn
      }

      val startButton = button("Start")(onStart)
      val stopButton = button("Stop")(onStop)
      val pauseButton = button("Pause")(onPause)
      val levelUpButton = button(" \u25b2 ")(onLevelUp)
      val levelDownButton = button(" \u25bc ")(onLevelDown)
      val gPanel = GPics(
        fillColor(Color(255, 0, 0, 127)) -> rect(200, 400),
        trans(0, 30) -> VPics(
          trans(90, 0) -> HPics(
            startButton,
            pauseButton,
            stopButton
          ).withGap(10),
          trans(120, 0) -> HPics(
            levelDownButton,
            penColor(black) * trans(0, 20) -> text("Level"),
            levelUpButton
          ).withGap(10)
        ).withGap(30)
      )
      trans(-200, -100) -> gPanel
    }
  }
}
