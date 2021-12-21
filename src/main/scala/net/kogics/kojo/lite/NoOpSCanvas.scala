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
  def clear(): Unit = {}
  def clearStepDrawing(): Unit = {}
  def newTurtle(x: Double, y: Double, costume: String): Turtle = null
  def showAxes(): Unit = {}
  def hideAxes(): Unit = {}
  def showGrid(): Unit = {}
  def hideGrid(): Unit = {}
  def showProtractor(x: Double, y: Double) = null
  def hideProtractor(): Unit = {}
  def showScale(x: Double, y: Double) = null
  def hideScale(): Unit = {}
  def zoom(factor: Double): Unit = {}
  def zoom(factor: Double, cx: Double, cy: Double): Unit = {}
  def zoomXY(xfactor: Double, yfactor: Double, cx: Double, cy: Double): Unit = {}
  def scroll(x: Double, y: Double): Unit = {}
  def viewRotate(a: Double): Unit = {}
  def exportImage(filePrefix: String): java.io.File = null
  def exportImage(filePrefix: String, width: Int, height: Int): java.io.File = null
  def exportImageH(filePrefix: String, height: Int): java.io.File = null
  def exportImageW(filePrefix: String, width: Int): java.io.File = null
  def exportThumbnail(filePrefix: String, height: Int): java.io.File = null
  def onKeyPress(fn: Int => Unit): Unit = {}
  def onKeyRelease(fn: Int => Unit): Unit = {}
  def onMouseClick(fn: (Double, Double) => Unit): Unit = {}
  def onMouseDrag(fn: (Double, Double) => Unit): Unit = {}
  def onMouseMove(fn: (Double, Double) => Unit): Unit = {}
  def setUnitLength(ul: UnitLen): Unit = {}
  def clearWithUL(ul: UnitLen): Unit = {}
  def camScale: Double = 1.0
  def wipe(): Unit = {}
  def setBackgroundH(c1: Color, c2: Color): Unit = {}
  def setBackgroundV(c1: Color, c2: Color): Unit = {}
  def drawStage(fillc: Paint): Unit = {}
  def stage: Picture = null
  def stageLeft: Picture = null
  def stageTop: Picture = null
  def stageRight: Picture = null
  def stageBot: Picture = null
  def stageArea: Picture = null

  def turtle0: Turtle = null
  def activate(): Unit = {}
  def cbounds: PBounds = null
  def setCanvasBackground(c: Paint): Unit = {}
  def kojoCtx: KojoCtx = null
  def timer(rate: Long)(fn: => Unit): Future[PActivity] = null
  def timerWithState[S](rate: Long, init: S)(code: S => S): Future[PActivity] = null
  def animate(fn: => Unit): Future[PActivity] = null
  def animateWithState[S](init: S)(code: S => S): Future[PActivity] = null
  def animateActivity(a: PActivity): Unit = {}
  def stopAnimation(): Unit = {}
  def stopAnimationActivity(a: Future[PActivity]): Unit = {}
  def onAnimationStart(fn: => Unit): Unit = {}
  def onAnimationStop(fn: => Unit): Unit = {}
  // stuff for the pictures module
  def getCamera: PCamera = new PCamera
  def pictures: PLayer = null
  def pCanvas: PCanvas = null
  def unitLen = null
  type TurtleLike <: Turtle
  private[kojo] def newInvisibleTurtle(x: Double, y: Double) = null.asInstanceOf[TurtleLike]
  private[kojo] def setDefTurtle(t: TurtleLike): Unit = {}
  private[kojo] def restoreDefTurtle(): Unit = {}
  def resetPanAndZoom(): Unit = {}
  def disablePanAndZoom(): Unit = {}
}


