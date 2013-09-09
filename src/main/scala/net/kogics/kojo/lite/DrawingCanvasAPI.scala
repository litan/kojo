package net.kogics.kojo
package lite

import java.awt.Color
import java.awt.Paint

import net.kogics.kojo.core.SCanvas
import net.kogics.kojo.core.TSCanvasFeatures
import net.kogics.kojo.core.Turtle
import net.kogics.kojo.core.UnitLen
import net.kogics.kojo.util.UserCommand
import net.kogics.kojo.util.Utils

// Turtle and Staging Canvas
class DrawingCanvasAPI(val tCanvas: SCanvas) extends TSCanvasFeatures {
  def turtle0 = tCanvas.turtle0
  def clear() = tCanvas.clear()
  UserCommand("clear", Nil, "Clears the screen, and brings the turtle to the center of the window.")
  def cleari() = { clear(); turtle0.invisible() }
  UserCommand("cleari", Nil, "Clears the turtle canvas and makes the turtle invisible.")

  def zoom(factor: Double) = tCanvas.zoom(factor)
  def zoom(factor: Double, cx: Double, cy: Double) = tCanvas.zoom(factor, cx, cy)
  UserCommand("zoom", List("factor"), "Zooms in by the given factor, leaving the center point unchanged.")
  UserCommand.addSynopsisSeparator()

  def gridOn() = tCanvas.gridOn()
  UserCommand("gridOn", Nil, "Shows a grid on the canvas.")

  def gridOff() = tCanvas.gridOff()
  UserCommand("gridOff", Nil, "Hides the grid.")

  def axesOn() = tCanvas.axesOn()
  UserCommand("axesOn", Nil, "Shows the X and Y axes on the canvas.")

  def axesOff() = tCanvas.axesOff()
  UserCommand("axesOff", Nil, "Hides the X and Y axes.")

  def newTurtle(): Turtle = newTurtle(0, 0)
  def newTurtle(x: Double = 0, y: Double = 0, costume: String = "/images/turtle32.png") = tCanvas.newTurtle(x, y, costume)
  
  def exportImage(filePrefix: String) = tCanvas.exportImage(filePrefix)
  def exportImage(filePrefix: String, width: Int, height: Int) = tCanvas.exportImage(filePrefix, width, height)
  def exportThumbnail(filePrefix: String, height: Int) = tCanvas.exportThumbnail(filePrefix, height)
  def zoomXY(xfactor: Double, yfactor: Double, cx: Double, cy: Double) =
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
