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
import java.awt.geom.GeneralPath
import java.awt.geom.Rectangle2D
import java.awt.image.BufferedImage
import java.awt.image.BufferedImageOp
import java.net.URL

import javax.swing.JComponent

import scala.language.implicitConversions
import scala.swing.Graphics2D

import net.kogics.kojo.mathworld.MathWorld
import net.kogics.kojo.story.HandlerHolder
import net.kogics.kojo.turtle.TurtleWorldAPI
import net.kogics.kojo.util.Read
import net.kogics.kojo.util.UserCommand
import net.kogics.kojo.xscala.CodeCompletionUtils
import net.kogics.kojo.xscala.Help
import net.kogics.kojo.xscala.RepeatCommands
import core.Voice
import picture.DslImpl
import picture.PicDrawingDsl
import story.HandlerHolder
import story.IntHandlerHolder
import story.StringHandlerHolder
import story.VoidHandlerHolder
import util.Read
import util.Throttler
import util.Utils
import net.kogics.kojo.picture.PicCache

// a static instance is needed for the compiler prefix code 
object Builtins {
  @volatile var instance: Builtins = _
}

class Builtins(
  val TSCanvas:    DrawingCanvasAPI,
  val Tw:          TurtleWorldAPI,
  val Staging:     staging.API,
  val Mw:          MathWorld,
  storyTeller:     story.StoryTeller,
  mp3player:       music.KMp3,
  fuguePlayer:     music.FuguePlayer,
  val kojoCtx:     core.KojoCtx,
  scalaCodeRunner: core.CodeRunner
) extends CoreBuiltins with RepeatCommands { builtins =>
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

  def schedule(seconds: Double)(code: => Unit) = Utils.schedule(seconds)(code)

  @deprecated("Use Color instead", "2.7")
  def color(rgbHex: Int) = new Color(rgbHex)

  def clearOutput() = kojoCtx.clearOutput()

  def interpret(code: String) {
    scalaCodeRunner.runCode(code)
  }

  def resetInterpreter() {
    scalaCodeRunner.resetInterp()
  }

  def kojoInterp = AppMode.currentMode.kojoInterpreter(scalaCodeRunner)
  // for debugging only!
  //  def pcompiler = scalaCodeRunner.asInstanceOf[ScalaCodeRunner].pcompiler
  //  def compiler = scalaCodeRunner.asInstanceOf[ScalaCodeRunner].compiler

  def reimportBuiltins() {
    interpret("import TSCanvas._; import Tw._")
  }
  def reimportDefaults() = reimportBuiltins()

  import story.{HandlerHolder, IntHandlerHolder, StringHandlerHolder, VoidHandlerHolder}
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
  val picBatch = BatchPics
  @deprecated("Use picRowCentered instead of picRow2", "2.7.08")
  val picRow2 = HPics2
  @deprecated("Use picColCentered instead of picCol2", "2.7.08")
  val picCol2 = VPics2
  @deprecated("Use picStackCentered instead of picStack2", "2.7.08")
  val picStack2 = GPics2
  val picRowCentered = HPics2
  val picColCentered = VPics2
  val picStackCentered = GPics2

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
  val penThickness = picture.strokeWidth _

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
  def drawCentered(pic: Picture): Unit = {
    pic.invisible()
    pic.draw()
    center(pic)
    pic.visible()
  }
  def center(pic: Picture): Unit = {
    val cb = canvasBounds; val pb = pic.bounds
    val xDelta = cb.getMinX - pb.getMinX + (cb.width - pb.width) / 2
    val yDelta = cb.getMinY - pb.getMinY + (cb.height - pb.height) / 2
    pic.offset(xDelta, yDelta)
  }
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

  def applyFilter(p: Picture, filter: BufferedImageOp): Picture = {
    drawCentered(p)
    p.scale(1, -1)
    val img = p.toImage
    p.erase()
    Picture.image(applyFilter(img, filter))
  }

  def applyFilter(img: BufferedImage, filter: BufferedImageOp): BufferedImage = {
    filter.filter(img, null)
  }

  def setDrawingCanvasAspectRatio(r: Double): Unit = {
    val dch = Main.drawingCanvasHolder
    val dc = dch.dc
    val b = dc.getBounds()
    val newWidth = b.height * r
    import java.awt.Dimension
    dch.setResizeRequest(new Dimension(math.round(newWidth).toInt, b.height), true)
  }

  def setDrawingCanvasSize(width: Int, height: Int): Unit = {
    val dch = Main.drawingCanvasHolder
    import java.awt.Dimension
    dch.setResizeRequest(new Dimension(width, height), true)
  }

  def setDrawingCanvasToA4(): Unit = {
    setDrawingCanvasAspectRatio(A4.aspectRatio)
  }

  def setDrawingCanvasToA4Landscape(): Unit = {
    setDrawingCanvasAspectRatio(A4Landscape.aspectRatio)
  }

  def setDrawingCanvasToA3(): Unit = {
    setDrawingCanvasAspectRatio(A3.aspectRatio)
  }

  def setDrawingCanvasToA3Landscape(): Unit = {
    setDrawingCanvasAspectRatio(A3Landscape.aspectRatio)
  }

  val hueMod = Utils.hueMod _
  val satMod = Utils.satMod _
  val britMod = Utils.britMod _

  type Vector2D = util.Vector2D
  val Vector2D = util.Vector2D

  def playMp3(mp3File: String) {
    mp3player.playMp3(mp3File)
  }
  UserCommand("playMp3", List("fileName"), "Plays the specified MP3 file.")

  def playMp3Sound(mp3File: String) {
    mp3player.playMp3Sound(mp3File)
  }
  UserCommand("playMp3Sound", List("fileName"), "Plays the specified MP3 sound.")

  def playMp3Loop(mp3File: String) {
    mp3player.playMp3Loop(mp3File)
  }
  UserCommand("playMp3Loop", List("fileName"), "Plays the specified MP3 file in the background.")

  def canvasBounds = tCanvas.cbounds
  def setBackground(c: Paint) = tCanvas.setCanvasBackground(c)

  def isMp3Playing = mp3player.isMp3Playing
  def isMusicPlaying = fuguePlayer.isMusicPlaying
  def stopMp3() = mp3player.stopMp3()
  def stopMp3Loop() = mp3player.stopMp3Loop()
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

    val topCollides = p.collidesWith(stageTop)
    val leftCollides = p.collidesWith(stageLeft)
    val botCollides = p.collidesWith(stageBot)
    val rightCollides = p.collidesWith(stageRight)

    val c = v.magnitude / math.sqrt(2)
    if (topCollides && leftCollides)
      Vector2D(c, -c)
    else if (topCollides && rightCollides)
      Vector2D(-c, -c)
    else if (botCollides && leftCollides)
      Vector2D(c, c)
    else if (botCollides && rightCollides)
      Vector2D(-c, c)
    else if (topCollides)
      Vector2D(v.x, -v.y)
    else if (botCollides)
      Vector2D(v.x, -v.y)
    else if (leftCollides)
      Vector2D(-v.x, v.y)
    else if (rightCollides)
      Vector2D(-v.x, v.y)
    else
      v
  }
  def bouncePicVectorOffStage(p: Picture, v: Vector2D): Vector2D = bouncePicVectorOffPic(p, v, TSCanvas.stageBorder)
  def bouncePicVectorOffPic(pic: Picture, v: Vector2D, obstacle: Picture): Vector2D =
    picture.bouncePicVectorOffPic(pic, v, obstacle, Random)

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
  def isMousePressed: Boolean = staging.Inputs.mousePressedFlag
  def isMousePressed(button: Int): Boolean = {
    staging.Inputs.mousePressedFlag && mouseButton == button
  }
  def mouseButton: Int = staging.Inputs.mouseBtn
  def screenDPI = kojoCtx.screenDPI
  def setScreenDPI(dpi: Int) { kojoCtx.screenDPI = dpi }
  def screenSize = Toolkit.getDefaultToolkit.getScreenSize
  def hiDpiFontIncrease = kojoCtx.hiDpiFontIncrease
  def baseFontSize = kojoCtx.baseFontSize
  def isScratchPad = kojoCtx.subKojo
  def isTracing = false

  def TexturePaint(file: String, x: Double, y: Double) =
    cm.texture(file, x, y)

  val PShapes = Picture
  val PicShape = Picture
  //  implicit def p2ep(p: Picture) = PicShape.effectablePic(p)
  implicit class GeneralPathOps(path: GeneralPath) {
    // enable using setPosition instead of moveTo - to be consistent with turtle usage.
    def setPosition(x: Double, y: Double) = path.moveTo(x, y)
  }
  def url(url: String) = new URL(url)
  object Picture {
    def text(s0: Any, fontSize: Int = 15) = picture.text(s0, fontSize)
    def textu(s0: Any, fontSize: Int = 15, color: Color = red) = picture.textu(s0, fontSize, color)
    def rect(h: Double, w: Double) = picture.rect(h, w)
    def rectangle(w: Double, h: Double) = picture.rect2(w, h)
    // def rectangle(x: Double, y: Double, w: Double, h: Double) = picture.offset(x, y) -> picture.rect2(w, h)
    def vline(l: Double) = picture.vline(l)
    def hline(l: Double) = picture.hline(l)
    def line(x: Double, y: Double) = picture.line(x, y)
    // def line(x1: Double, y1: Double, x2: Double, y2: Double) = picture.offset(x1, y1) -> picture.line(x2 - x1, y2 - y1)
    def fromPath(fn: GeneralPath => Unit) = { val path = new GeneralPath(); fn(path); picture.fromPath(path) }
    def fromTurtle(fn: Turtle => Unit) = PictureT(fn)
    def fromJava2D(w: Int, h: Int)(fn: Graphics2D => Unit) = picture.fromJava2d(w, h, fn)
    def circle(r: Double) = picture.circle(r)
    // def circle(x: Double, y: Double, r: Double) = picture.offset(x, y) -> picture.circle(r)
    def ellipse(rx: Double, ry: Double) = picture.ellipse(rx, ry)
    // def ellipse(x: Double, y: Double, rx: Double, ry: Double) = picture.offset(x, y) -> picture.ellipse(rx, ry)
    def arc(r: Double, angle: Double) = picture.arc(r, angle)
    def image(fileName: String) = picture.image(fileName, None)
    def image(fileName: String, envelope: Picture) = picture.image(fileName, Some(envelope))
    def image(url: URL) = picture.image(url, None)
    def image(url: URL, envelope: Picture) = picture.image(url, Some(envelope))
    def image(image: Image) = picture.image(image, None)
    def image(image: Image, envelope: Picture) = picture.image(image, Some(envelope))
    def widget(component: JComponent) = picture.widget(component)
    def button(label: String)(fn: => Unit) = widget(Button(label)(fn))
    def effectablePic(pic: Picture) = picture.effectablePic(pic)
    def hgap(n: Double) = penColor(noColor) * penThickness(0.001) -> Picture.rectangle(n, 0.001)
    def vgap(n: Double) = penColor(noColor) * penThickness(0.001) -> Picture.rectangle(0.001, n)
    def showBounds(p: Picture, c: Color = cm.black) = Utils.runInSwingThread {
      val b = p.tnode.getGlobalFullBounds
      val pic = trans(b.x, b.y) * penColor(c) -> rectangle(b.width, b.height)
      draw(pic)
    }
    def showAxes(p: Picture) = {
      p.axesOn()
    }
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

  def showFps(color: Color = black, fontSize: Int = 15) {
    val cb = canvasBounds
    @volatile var frameCnt = 0
    val fpsLabel = Picture.textu("Fps: ", fontSize, color)
    fpsLabel.setPosition(cb.x + 10, cb.y + cb.height - 10)
    draw(fpsLabel)
    fpsLabel.forwardInputTo(TSCanvas.stageArea)

    TSCanvas.timer(1000) {
      fpsLabel.update(s"Fps: $frameCnt")
      frameCnt = 0
    }
    fpsLabel.react { self =>
      frameCnt += 1
    }
  }

  def drawCenteredMessage(message: String, color: Color = black, fontSize: Int = 15) {
    val cb = canvasBounds
    val te = textExtent(message, fontSize)
    val pic = penColor(color) *
      trans(cb.x + (cb.width - te.width) / 2, cb.y + (cb.height - te.height) / 2 + te.height) ->
      PicShape.text(message, fontSize)
    draw(pic)
  }

  def showGameTime(limitSecs: Int, endMsg: String, color: Color = black, fontSize: Int = 15) {
    val cb = canvasBounds
    @volatile var gameTime = 0
    val timeLabel = trans(cb.x + 10, cb.y + 50) -> PicShape.textu(gameTime, fontSize, color)
    draw(timeLabel)
    timeLabel.forwardInputTo(TSCanvas.stageArea)

    TSCanvas.timer(1000) {
      gameTime += 1
      timeLabel.update(gameTime)

      if (gameTime == limitSecs) {
        drawCenteredMessage(endMsg, color, fontSize * 2)
        stopAnimation()
      }
    }
  }

  type Shape = PicDrawingDsl
  object Shape {
    def clear() = TSCanvas.cleari()
    def rectangle(w: Double, h: Double): Shape = DslImpl(picture.rect(h, w))
    def square(l: Double): Shape = rectangle(l, l)
    def circle(r: Double): Shape = DslImpl(picture.circle(r)) translated (r, r)
    def gap(w: Double, h: Double) = rectangle(w, h) outlined (noColor)
    def vline(l: Double): Shape = DslImpl(picture.vline(l))
    def hline(l: Double): Shape = DslImpl(picture.hline(l))
    def text(string: Any, fontSize: Int = 15): Shape =
      DslImpl(picture.textu(string, fontSize, black)) translated (0, textExtent(string.toString, fontSize).height)
    def image(file: String) = DslImpl(picture.image(file, None))
    def turtleMade(fn: => Unit): Shape = DslImpl(Picture(fn))
    def stack(shapes: Shape*): Shape = DslImpl(picture.GPics2(shapes.map(s => PicCache.freshPic(s.pic)).toList))
    def row(shapes: Shape*): Shape = DslImpl(picture.HPics2(shapes.map(s => PicCache.freshPic(s.pic)).toList))
    def col(shapes: Shape*): Shape = DslImpl(picture.VPics2(shapes.map(s => PicCache.freshPic(s.pic)).toList))
    def stack2(shapes: Shape*): Shape = DslImpl(picture.GPics(shapes.map(s => PicCache.freshPic(s.pic)).toList))
    def row2(shapes: Shape*): Shape = DslImpl(picture.HPics(shapes.map(s => PicCache.freshPic(s.pic)).toList))
    def col2(shapes: Shape*): Shape = DslImpl(picture.VPics(shapes.map(s => PicCache.freshPic(s.pic)).toList))
    def draw2(shapes: Shape*) = shapes.foreach { _.draw() }
    def draw(shapes: Shape*): Unit = {
      def center(shape: Shape) = {
        val cb = canvasBounds; val sb = shape.pic.bounds
        val xDelta = cb.getMinX - sb.getMinX + (cb.width - sb.width) / 2
        val yDelta = cb.getMinY - sb.getMinY + (cb.height - sb.height) / 2
        shape.pic.offset(xDelta, yDelta)
      }
      if (shapes.size > 1) {
        val shapeStack = stack(shapes: _*)
        shapeStack.pic.invisible()
        shapeStack.draw()
        center(shapeStack)
        shapeStack.pic.visible()
      }
      else {
        val shape = shapes(0)
        shape.pic.invisible()
        shape.draw()
        center(shape)
        shape.pic.visible()
      }
    }
  }

  type TileWorld = tiles.TileWorld
  type SpriteSheet = tiles.SpriteSheet
  val SpriteSheet = tiles.SpriteSheet
  type TileXY = tiles.TileXY
  val TileXY = tiles.TileXY
}
