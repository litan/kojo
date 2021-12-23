package net.kogics.kojo
package lite

import java.awt.Color
import java.awt.Paint
import java.util.concurrent.Future

import net.kogics.kojo.core.SCanvas
import net.kogics.kojo.core.TSCanvasFeatures
import net.kogics.kojo.core.Turtle
import net.kogics.kojo.core.UnitLen
import net.kogics.kojo.util.UserCommand

import edu.umd.cs.piccolo.activities.PActivity

// Turtle and Staging Canvas
class DrawingCanvasAPI(val tCanvas: SCanvas) extends TSCanvasFeatures {
  def turtle0 = tCanvas.turtle0
  def clear() = tCanvas.clear()
  UserCommand("clear", Nil, "Clears the screen, and brings the turtle to the center of the window.")
  def cleari() = { clear(); turtle0.invisible() }
  UserCommand("cleari", Nil, "Clears the turtle canvas and makes the turtle invisible.")
  def clearStepDrawing() = tCanvas.clearStepDrawing()

  def zoom(factor: Double) = tCanvas.zoom(factor)
  def zoom(factor: Double, cx: Double, cy: Double) = tCanvas.zoom(factor, cx, cy)
  UserCommand("zoom", List("factor"), "Zooms in by the given factor, leaving the center point unchanged.")
  UserCommand.addSynopsisSeparator()
  
  def scroll(x: Double, y: Double) = tCanvas.scroll(x, y)

  def viewRotate(a: Double): Unit = tCanvas.viewRotate(a)

//  def showScale = tCanvas.
  def showGrid() = tCanvas.showGrid()
  UserCommand("gridOn", Nil, "Shows a grid on the canvas.")

  def hideGrid() = tCanvas.hideGrid()
  UserCommand("gridOff", Nil, "Hides the grid.")

  def showAxes() = tCanvas.showAxes()
  UserCommand("axesOn", Nil, "Shows the X and Y axes on the canvas.")

  def hideAxes() = tCanvas.hideAxes()
  UserCommand("axesOff", Nil, "Hides the X and Y axes.")

  def showProtractor() = tCanvas.showProtractor(-tCanvas.cbounds.getWidth/2, -tCanvas.cbounds.getHeight/2)
  def showProtractor(x: Double, y: Double) = tCanvas.showProtractor(x, y)
  def hideProtractor() = tCanvas.hideProtractor()
  def showScale() = tCanvas.showScale(-tCanvas.cbounds.getWidth/2, tCanvas.cbounds.getHeight/2)
  def showScale(x: Double, y: Double) = tCanvas.showScale(x, y)
  def hideScale() = tCanvas.hideScale()
  
  def newTurtle(): Turtle = newTurtle(0, 0)
  def newTurtle(x: Double = 0, y: Double = 0, costume: String = "/images/turtle32.png") = tCanvas.newTurtle(x, y, costume)

  def exportImage(filePrefix: String) = tCanvas.exportImage(filePrefix)
  def exportImage(filePrefix: String, width: Int, height: Int) = tCanvas.exportImage(filePrefix, width, height)
  def exportImageH(filePrefix: String, height: Int) = tCanvas.exportImageH(filePrefix, height)
  def exportImageW(filePrefix: String, width: Int) = tCanvas.exportImageW(filePrefix, width)
  def exportThumbnail(filePrefix: String, height: Int) = tCanvas.exportThumbnail(filePrefix, height)
  def zoomXY(xfactor: Double, yfactor: Double, cx: Double, cy: Double) =
    tCanvas.zoomXY(xfactor, yfactor, cx, cy)

  def onKeyPress(fn: Int => Unit) = tCanvas.onKeyPress(fn)
  def onKeyRelease(fn: Int => Unit) = tCanvas.onKeyRelease(fn)
  def onMouseClick(fn: (Double, Double) => Unit) = tCanvas.onMouseClick(fn)
  def onMouseDrag(fn: (Double, Double) => Unit) = tCanvas.onMouseDrag(fn)
  def onMouseMove(fn: (Double, Double) => Unit) = tCanvas.onMouseMove(fn)

  val Pixel = core.Pixel
  val Inch = core.Inch
  val Cm = core.Cm
  def setUnitLength(ul: UnitLen) = tCanvas.setUnitLength(ul)
  def clearWithUL(ul: UnitLen) = tCanvas.clearWithUL(ul)
  def camScale = tCanvas.camScale
  def setBackgroundH(c1: Color, c2: Color) = tCanvas.setBackgroundH(c1, c2)
  def setBackgroundV(c1: Color, c2: Color) = tCanvas.setBackgroundV(c1, c2)
  def wipe() = tCanvas.wipe()
  def drawStage(fillc: Paint) = tCanvas.drawStage(fillc)
  def stage = tCanvas.stage
  def stageLeft = tCanvas.stageLeft
  def stageTop = tCanvas.stageTop
  def stageRight = tCanvas.stageRight
  def stageBot = tCanvas.stageBot
  def stageArea = tCanvas.stageArea
  def stageBorder = tCanvas.stage
  def timer(milliSeconds: Long)(code: => Unit): Future[PActivity] = tCanvas.timer(milliSeconds)(code)
  def timerWithState[S](rate: Long, init: S)(code: S => S): Future[PActivity] =
    tCanvas.timerWithState(rate, init)(code)
  def animate(code: => Unit) = tCanvas.animate(code)
  def animateWithState[S](init: S)(code: S => S): Future[PActivity] =
    tCanvas.animateWithState(init)(code)
  //  def animateWithState[S](init: S, nextState: S => S)(code: S => Unit): Future[PActivity] = {
  //    tCanvas.animateWithState(init) { s =>
  //      code(s)
  //      nextState(s)
  //    }
  //  }
  def stopAnimationActivity(a: Future[PActivity]) = tCanvas.stopAnimationActivity(a)
  def onAnimationStart(fn: => Unit) = tCanvas.onAnimationStart(fn)
  def onAnimationStop(fn: => Unit) = tCanvas.onAnimationStop(fn)
  def resetPanAndZoom() = tCanvas.resetPanAndZoom()
  def disablePanAndZoom() = tCanvas.disablePanAndZoom()
}
