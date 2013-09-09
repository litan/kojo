package net.kogics.kojo
package lite
package trace

import java.awt.Paint

import scala.tools.nsc.interpreter.IMain

import net.kogics.kojo.core.SCanvas
import net.kogics.kojo.lite.CoreBuiltins
import net.kogics.kojo.lite.NoOpSCanvas
import net.kogics.kojo.util.Utils

import edu.umd.cs.piccolo.PCamera
import edu.umd.cs.piccolo.PLayer
import edu.umd.cs.piccolo.activities.PActivity
import util.Throttler

object TracingBuiltins extends CoreBuiltins {

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

  def Font(name: String, size: Int) = new Font(name, 0, size)

  /* those are basically still no-ops
   * 
   */
  def showScriptInOutput() = TSCanvas.kojoCtx.showScriptInOutput
  def hideScriptInOutput() = TSCanvas.kojoCtx.hideScriptInOutput()
  def showVerboseOutput() = TSCanvas.kojoCtx.showVerboseOutput()
  def hideVerboseOutput() = TSCanvas.kojoCtx.hideVerboseOutput()
  def retainSingleLineCode() = {}
  def clearSingleLineCode() = {}
  def version = println("Scala " + scala.tools.nsc.Properties.versionString)
  def switchToDefaultPerspective() = TSCanvas.kojoCtx.switchToDefaultPerspective()
  def switchToScriptEditingPerspective() = TSCanvas.kojoCtx.switchToScriptEditingPerspective()
  def switchToWorksheetPerspective() = TSCanvas.kojoCtx.switchToWorksheetPerspective()
  def switchToStoryViewingPerspective() = TSCanvas.kojoCtx.switchToStoryViewingPerspective()
  def switchToHistoryBrowsingPerspective() = TSCanvas.kojoCtx.switchToHistoryBrowsingPerspective()
  def switchToOutputStoryViewingPerspective() = TSCanvas.kojoCtx.switchToOutputStoryViewingPerspective()
  private val fullScreenAction = TSCanvas.kojoCtx.fullScreenCanvasAction()
  def toggleFullScreenCanvas() = fullScreenAction.actionPerformed(null)

  private val fullScreenOutputAction = TSCanvas.kojoCtx.fullScreenOutputAction()
  def toggleFullScreenOutput() = fullScreenOutputAction.actionPerformed(null)

  def setOutputBackground(color: Color) = TSCanvas.kojoCtx.setOutputBackground(color)
  def setOutputTextColor(color: Color) = TSCanvas.kojoCtx.setOutputForeground(color)
  def setOutputTextFontSize(size: Int) = TSCanvas.kojoCtx.setOutputFontSize(size)

  
  
  
  def playMp3Loop(mp3File: String) {
  }

  def repeat(n: Int)(fn: => Unit) {
    var i = 0
    while (i < n) {
      fn
      i += 1
    }
  }

  def repeatWhile(condition: => Boolean)(fn: => Unit) {
    while (condition) {
      fn
    }
  }

  def setBackground(c: Paint) {}
  def stopActivity() = {}
  def runInBackground(code: => Unit) { code }

  def isScratchPad = false
  def setEditorTabSize(n: Int) {}
  def clearOutput() {}
  def readln(prompt: String): String = TSCanvas.kojoCtx.readInput(prompt)
  def readInt(prompt: String): Int = readln(prompt).toInt
  def readDouble(prompt: String): Double = readln(prompt).toDouble
  def addCodeTemplates(lang: String, templates: Map[String, String]) {}
  def addHelpContent(lang: String, content: Map[String, String]) {}
  def isTracing = true
  def kojoInterp = new TracingInterp // get reqT to work with tracing
  def print(obj: Any) {}
  def println(obj: Any): Unit = print("%s\n" format (obj))

  implicit val picCanvas = TSCanvas
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
    def circle(r: Double) = picture.circle(r)
    def arc(r: Double, angle: Int) = picture.arc(r, angle)
  }

  class TracingInterp {
    def interp: IMain = null
  }

  class TracingTurtle(canvas: SCanvas, costume: String, x: Double, y: Double)
    extends turtle.Turtle(canvas, costume, x, y) {

    override def act(fn: Turtle => Unit) {
      fn(this)
    }

    override def arc(r: Double, a: Int) {
      def makeArc(lforward: Double => Unit, lturn: Double => Unit) {
        var i = 0
        val (lim, step, trn) = if (a > 0) (a, 2 * math.Pi * r / 360, 1) else (-a, 2 * math.Pi * r / 360, -1)
        while (i < lim) {
          lforward(step)
          lturn(trn)
          i += 1
        }
      }
      makeArc(forward _, turn _)
    }

    override def moveTo(x: Double, y: Double) {
      towards(x, y)
      val d = Utils.runInSwingThreadAndWait { distanceTo(x, y) }
      forward(d)
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

    override def animateActivity(a: PActivity) {
      a.getDelegate().activityFinished(a)
    }

    override lazy val getCamera: PCamera = new PCamera
    override lazy val pictures: PLayer = new PLayer
    def cleari() {}
  }
}