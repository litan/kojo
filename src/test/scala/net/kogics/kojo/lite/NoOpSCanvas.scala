package net.kogics.kojo
package lite

import java.awt.Color
import java.awt.Paint
import java.util.concurrent.Future

import net.kogics.kojo.core.Picture
import net.kogics.kojo.core.SCanvas
import net.kogics.kojo.core.Turtle
import net.kogics.kojo.core.UnitLen

import edu.umd.cs.piccolo.PCamera
import edu.umd.cs.piccolo.PCanvas
import edu.umd.cs.piccolo.PLayer
import edu.umd.cs.piccolo.activities.PActivity
import edu.umd.cs.piccolo.util.PBounds

class NoOpSCanvas extends SCanvas {
  def clear() {}
  def newTurtle(x: Double, y: Double, costume: String): Turtle = null
  def axesOn() {}
  def axesOff() {}
  def gridOn() {}
  def gridOff() {}
  def zoom(factor: Double) {}
  def zoom(factor: Double, cx: Double, cy: Double) {}
  def zoomXY(xfactor: Double, yfactor: Double, cx: Double, cy: Double) {}
  def exportImage(filePrefix: String): java.io.File = null
  def exportImage(filePrefix: String, width: Int, height: Int): java.io.File = null
  def exportThumbnail(filePrefix: String, height: Int): java.io.File = null
  def onKeyPress(fn: Int => Unit) {}
  def onKeyRelease(fn: Int => Unit) {}  
  def onMouseClick(fn: (Double, Double) => Unit) {}
  def setUnitLength(ul: UnitLen) {}
  def clearWithUL(ul: UnitLen) {}
  def camScale: Double = 1.0
  def wipe() {}
  def setBackgroundH(c1: Color, c2: Color) {}
  def setBackgroundV(c1: Color, c2: Color) {}
  def drawStage(fillc: Paint) {}
  def stage: Picture = null
  def stageLeft: Picture = null
  def stageTop: Picture = null
  def stageRight: Picture = null
  def stageBot: Picture = null
  
  def turtle0: Turtle = null
  def activate() {}
  def cbounds: PBounds = null
  def setCanvasBackground(c: Paint) {}
  def kojoCtx: KojoCtx = null
  def animate(fn: => Unit): Future[PActivity] = null
  def animateActivity(a: PActivity) {}
  def stopAnimation() {}
  // stuff for the pictures module
  def getCamera: PCamera = new PCamera
  def pictures: PLayer = null
  def pCanvas: PCanvas = null
  def unitLen = null
  type TurtleLike <: Turtle
  private[kojo] def newInvisibleTurtle(x: Double, y: Double) = null.asInstanceOf[TurtleLike]
  private[kojo] def setDefTurtle(t: TurtleLike) {}
  private[kojo] def restoreDefTurtle() {}
}


