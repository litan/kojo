package net.kogics.kojo
package lite
package trace

import java.awt.{ Color => JColor }
import java.awt.{ Font => JFont }
import java.awt.Paint
import net.kogics.kojo.core.RichTurtleCommands
import net.kogics.kojo.util.Utils
import net.kogics.kojo.core.TSCanvasFeatures
import net.kogics.kojo.turtle.TurtleWorldAPI
import net.kogics.kojo.core.UnitLen
import net.kogics.kojo.core.Picture

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

  def playMp3Loop(mp3File: String) {
  }

  def repeat(n: Int)(fn: => Unit) {
    var i = 0
    while (i < n) {
      fn
      i += 1
    }
  }

  def repeatWhile(condition: Boolean)(fn: => Unit) {
    while (condition) {
      fn
    }
  }

  def cleari() {}
  def setBackground(c: Paint) {}

  def stopActivity() = {}
  /* turtle creation */

  def runInBackground(code: => Unit) { code }

  def isScratchPad = false
  def setEditorTabSize(n: Int) {}
  def clearOutput() {}
  def readln(prompt: String): String = "Unsupported"
  def addCodeTemplates(lang: String, templates: Map[String, String]) {}
  def addHelpContent(lang: String, content: Map[String, String]) {}

  class TracingTSCanvas extends TSCanvasFeatures {
    val spriteCanvas = new NoOpSCanvas
    val turtle0: core.Turtle = newTurtle(0.0, 0.0)

    def clear() {}
    def newTurtle(): Turtle = newTurtle(0, 0)
    def newTurtle(x: Double, y: Double): core.Turtle = {
      newTurtle(x, y, "/images/turtle32.png")
    }
    def newTurtle(x: Double, y: Double, costume: String): core.Turtle = {
      new net.kogics.kojo.turtle.Turtle(spriteCanvas, costume, x, y) {
        override def act(fn: Turtle => Unit) {
          fn(this)
        }
      }
    }

    def axesOn() {}
    def axesOff() {}
    def gridOn() {}
    def gridOff() {}
    def zoom(factor: Double) {}
    def zoom(factor: Double, cx: Double, cy: Double) {}
    def zoomXY(xfactor: Double, yfactor: Double, cx: Double, cy: Double) {}
    def exportImage(filePrefix: String) = new java.io.File("noop")
    def exportImage(filePrefix: String, width: Int, height: Int) = new java.io.File("noop")
    def exportThumbnail(filePrefix: String, height: Int) = new java.io.File("noop")
    def onKeyPress(fn: Int => Unit) {}
    def onKeyRelease(fn: Int => Unit) {}
    def onMouseClick(fn: (Double, Double) => Unit) {}
    def setUnitLength(ul: UnitLen) {}
    def clearWithUL(ul: UnitLen) {}
    def camScale = 1
    def wipe() {}
    def setBackground(c: Paint) {}
    def setBackgroundH(c1: Color, c2: Color) {}
    def setBackgroundV(c1: Color, c2: Color) {}
    def drawStage(fillc: Paint) {}
    def stage: Picture = null
    def stageLeft: Picture = null
    def stageTop: Picture = null
    def stageRight: Picture = null
    def stageBot: Picture = null
  }

}