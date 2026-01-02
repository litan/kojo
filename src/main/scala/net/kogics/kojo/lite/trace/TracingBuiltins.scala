package net.kogics.kojo
package lite
package trace

import java.awt.Paint

import scala.tools.nsc.interpreter.IMain

import edu.umd.cs.piccolo.activities.PActivity
import edu.umd.cs.piccolo.PCamera
import edu.umd.cs.piccolo.PLayer
import net.kogics.kojo.core.SCanvas
import net.kogics.kojo.lite.CoreBuiltins
import net.kogics.kojo.lite.NoOpSCanvas
import net.kogics.kojo.util.Utils
import net.kogics.kojo.xscala.RepeatCommands

object TracingBuiltins extends CoreBuiltins with RepeatCommands {

  val TSCanvas = new TracingTSCanvas

  class Costume {
    val car = "/media/costumes/car.png"
    val pencil = "/media/costumes/pencil.png"
    val bat1 = "/media/costumes/bat1-a.png"
    val bat2 = "/media/costumes/bat1-b.png"
    val womanWaving = "/media/costumes/womanwaving.png"
  }

  class Background {
    val trainTrack = "/media/backgrounds/train-tracks3.gif"
  }

  class Sound {
    val medieval1 = "/media/music-loops/Medieval1.mp3"
  }

  val Costume = new Costume
  val Background = new Background
  val Sound = new Sound

  def hueMod(c: Color, f: Double) = Utils.hueMod(c, f)
  def satMod(c: Color, f: Double) = Utils.satMod(c, f)
  def britMod(c: Color, f: Double) = Utils.britMod(c, f)

  def showScriptInOutput() = {}
  def hideScriptInOutput() = {}
  def showVerboseOutput() = {}
  def hideVerboseOutput() = {}
  def retainSingleLineCode() = {}
  def clearSingleLineCode() = {}
  def version = {}
  def switchToDefaultPerspective() = {}
  def switchToDefault2Perspective() = {}
  def switchToScriptEditingPerspective() = {}
  def switchToWorksheetPerspective() = {}
  def switchToStoryViewingPerspective() = {}
  def switchToHistoryBrowsingPerspective() = {}
  def switchToOutputStoryViewingPerspective() = {}
  def toggleFullScreenCanvas() = {}
  def toggleFullScreenOutput() = {}
  def setOutputBackground(color: Color) = {}
  def setOutputTextColor(color: Color) = {}
  def setOutputTextFontSize(size: Int) = {}

  def playMp3Loop(mp3File: String): Unit = {}

  def setBackground(c: Paint): Unit = {}
  def stopActivity() = {}
  def runInBackground(code: => Unit): Unit = { code }

  def isScratchPad = false
  def setEditorTabSize(n: Int): Unit = {}
  def setEditorFont(name: String): Unit = {}
  def clearOutput(): Unit = {}
  def readln(prompt: String): String = "Unsupported"
  def breakpoint(msg: Any): Unit = {}
  //  def readInt(prompt: String): Int = 0
  //  def readDouble(prompt: String): Double = 0
  def addCodeTemplates(lang: String, templates: Map[String, String]): Unit = {}
  def addHelpContent(lang: String, content: Map[String, String]): Unit = {}
  def isTracing = true
  def kojoInterp = new TracingInterp // get reqT to work with tracing
  def print(obj: Any): Unit = {}
  def println(obj: Any): Unit = print("%s\n".format(obj))

  def TexturePaint(file: String, x: Double, y: Double) = Color(247, 247, 247)

  implicit val picCanvas: TracingTSCanvas = TSCanvas
  def Picture(fn: => Unit) = new picture.Pic(t => fn)
  def PictureT(fn: Turtle => Unit) = new picture.Pic(fn)
  def picStack(pics: Picture*) = new GPics(pics.toList)
  def picRow(pics: Picture*) = new HPics(pics.toList)
  def picCol(pics: Picture*) = new VPics(pics.toList)
  object PicShape {
    def text(s0: Any, fontSize: Int = 15) = picture.text(s0, fontSize)
    def rect(h: Double, w: Double) = picture.rect(h, w)
    def vline(l: Double) = picture.vline(l)
    def hline(l: Double) = picture.hline(l)
    //    def circle(r: Double) = picture.circle(r)
    //    def arc(r: Double, angle: Int) = picture.arc(r, angle)
  }

  class TracingInterp {
    def interp: IMain = null
  }

  class TracingTurtle(canvas: SCanvas, costume: String, x: Double, y: Double)
      extends turtle.Turtle(canvas, costume, x, y) {

    override def act(fn: Turtle => Unit): Unit = {
      fn(this)
    }

    override def moveTo(x: Double, y: Double): Unit = {
      towards(x, y)
      val d = Utils.runInSwingThreadAndWait { distanceTo(x, y) }
      forward(d)
    }

    override def arc2(r: Double, a: Double): Unit = {
      // gets intercepted in the Kojo VM
    }

    override def toString() = s"Turtle with Id: ${System.identityHashCode(this)}"
  }

  class TracingTSCanvas extends NoOpSCanvas {
    override val turtle0: core.Turtle = newTurtle(0.0, 0.0)
    type TurtleLike <: core.Turtle

    def newTurtle(): Turtle = newTurtle(0, 0)
    def newTurtle(x: Double, y: Double): core.Turtle = {
      newTurtle(x, y, "/images/turtle32.png")
    }
    override def newTurtle(x: Double, y: Double, costume: String): core.Turtle = {
      _newTurtle(x, y, costume)
    }

    private def _newTurtle(x: Double, y: Double, costume: String): core.Turtle = {
      new TracingTurtle(this, costume, x, y)
    }

    private[kojo] override def newInvisibleTurtle(x: Double, y: Double) = {
      _newTurtle(x, y, "/images/turtle32.png").asInstanceOf[TurtleLike]
    }

    override def animateActivity(a: PActivity): Unit = {
      a.getDelegate().activityFinished(a)
    }

    override lazy val getCamera: PCamera = new PCamera
    override lazy val pictures: PLayer = new PLayer
    def cleari(): Unit = {}
  }
}
