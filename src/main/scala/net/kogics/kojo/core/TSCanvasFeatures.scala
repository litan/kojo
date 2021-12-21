/*
 * Copyright (C) 2011 Lalit Pant <pant.lalit@gmail.com>
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
package core

import java.awt.Color
import java.awt.Paint
import java.util.concurrent.Future

import edu.umd.cs.piccolo.activities.PActivity

trait TSCanvasFeatures {
  def turtle0: Turtle
  def clear(): Unit
  def clearStepDrawing(): Unit
  def newTurtle(x: Double, y: Double, costume: String): Turtle
  def showAxes(): Unit
  def hideAxes(): Unit
  def axesOn() = showAxes()
  def axesOff()= hideAxes()
  def showGrid(): Unit
  def hideGrid(): Unit
  def gridOn() = showGrid()
  def gridOff() = hideGrid()
  def showProtractor(x: Double, y: Double): Picture
  def hideProtractor(): Unit
  def showScale(x: Double, y: Double): Picture
  def hideScale(): Unit
  def zoom(factor: Double): Unit
  def zoom(factor: Double, cx: Double, cy: Double): Unit
  def zoomXY(xfactor: Double, yfactor: Double, cx: Double, cy: Double): Unit
  def scroll(x: Double, y: Double): Unit
  def viewScale(factor: Double): Unit = zoom(factor)
  def viewTranslate(x: Double, y: Double): Unit = scroll(-x, -y)
  def viewRotate(a: Double): Unit
  def exportImage(filePrefix: String): java.io.File
  def exportImage(filePrefix: String, width: Int, height: Int): java.io.File
  def exportImageH(filePrefix: String, height: Int): java.io.File
  def exportImageW(filePrefix: String, width: Int): java.io.File
  def exportThumbnail(filePrefix: String, height: Int): java.io.File
  def onKeyPress(fn: Int => Unit): Unit
  def onKeyRelease(fn: Int => Unit): Unit
  def onMouseClick(fn: (Double, Double) => Unit): Unit
  def onMouseDrag(fn: (Double, Double) => Unit): Unit
  def onMouseMove(fn: (Double, Double) => Unit): Unit
  def setUnitLength(ul: UnitLen): Unit
  def clearWithUL(ul: UnitLen): Unit
  def camScale: Double
  def wipe(): Unit
  def erasePictures() = wipe()
  def setBackgroundH(c1: Color, c2: Color): Unit
  def setBackgroundV(c1: Color, c2: Color): Unit
  def drawStage(fillc: Paint): Unit
  def stage: Picture
  def stageLeft: Picture
  def stageTop: Picture
  def stageRight: Picture
  def stageBot: Picture
  def stageArea: Picture
  def timer(rate: Long)(fn: => Unit): Future[PActivity]
  def timerWithState[S](rate: Long, init: S)(code: S => S): Future[PActivity]
  def animate(fn: => Unit): Future[PActivity]
  def animateWithState[S](init: S)(code: S => S): Future[PActivity]
  def stopAnimationActivity(a: Future[PActivity]): Unit
  def onAnimationStart(fn: => Unit): Unit
  def onAnimationStop(fn: => Unit): Unit
  def resetPanAndZoom(): Unit
  def disablePanAndZoom(): Unit
}
