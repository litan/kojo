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

import java.awt.Paint
import java.awt.Color

trait TSCanvasFeatures {
  def clear(): Unit
  def newTurtle(x: Double, y: Double, costume: String): Turtle
  def axesOn(): Unit
  def axesOff(): Unit
  def gridOn(): Unit
  def gridOff(): Unit
  def zoom(factor: Double): Unit
  def zoom(factor: Double, cx: Double, cy: Double): Unit
  def zoomXY(xfactor: Double, yfactor: Double, cx: Double, cy: Double): Unit
  def exportImage(filePrefix: String): java.io.File
  def exportImage(filePrefix: String, width: Int, height: Int): java.io.File
  def exportThumbnail(filePrefix: String, height: Int): java.io.File
  def onKeyPress(fn: Int => Unit): Unit
  def onKeyRelease(fn: Int => Unit): Unit  
  def onMouseClick(fn: (Double, Double) => Unit): Unit
  def setUnitLength(ul: UnitLen): Unit
  def clearWithUL(ul: UnitLen): Unit
  def camScale: Double
  def wipe(): Unit
  def setBackgroundH(c1: Color, c2: Color): Unit
  def setBackgroundV(c1: Color, c2: Color): Unit
  def drawStage(fillc: Paint): Unit
  def stage: Picture
  def stageLeft: Picture
  def stageTop: Picture
  def stageRight: Picture
  def stageBot: Picture
}
