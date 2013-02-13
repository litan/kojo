package net.kogics.kojo
package lite

import java.awt.Color
import java.awt.Paint

import net.kogics.kojo.core.SCanvas
import net.kogics.kojo.core.TSCanvasFeatures
import net.kogics.kojo.core.Turtle
import net.kogics.kojo.core.UnitLen

// Turtle and Staging Canvas
class DrawingCanvasAPI(tCanvas: SCanvas) extends TSCanvasFeatures {
  lazy val turtle0 = tCanvas.turtle0
  override def clear() = tCanvas.clear()
  def cleari() = { clear(); turtle0.invisible() }
  override def zoom(factor: Double) = tCanvas.zoom(factor)
  override def zoom(factor: Double, cx: Double, cy: Double) = tCanvas.zoom(factor, cx, cy)

  override def gridOn() = tCanvas.gridOn()

  override def gridOff() = tCanvas.gridOff()

  override def axesOn() = tCanvas.axesOn()

  override def axesOff() = tCanvas.axesOff()

  def newTurtle(): Turtle = newTurtle(0, 0)
  override def newTurtle(x: Int, y: Int) = tCanvas.newTurtle(x, y)
  override def exportImage(filePrefix: String) = tCanvas.exportImage(filePrefix)
  override def exportThumbnail(filePrefix: String, height: Int) = tCanvas.exportThumbnail(filePrefix, height)
  override def zoomXY(xfactor: Double, yfactor: Double, cx: Double, cy: Double) =
    tCanvas.zoomXY(xfactor, yfactor, cx, cy)

  def onKeyPress(fn: Int => Unit) = tCanvas.onKeyPress(fn)
  def onKeyRelease(fn: Int => Unit) = tCanvas.onKeyRelease(fn)
  def onMouseClick(fn: (Double, Double) => Unit) = tCanvas.onMouseClick(fn)

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
}
