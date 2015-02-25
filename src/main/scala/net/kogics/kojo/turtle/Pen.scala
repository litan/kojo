/*
 * Copyright (C) 2009 Lalit Pant <pant.lalit@gmail.com>
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
package turtle

import java.awt.Font
import java.awt.Paint

import core.Style

trait Pen {
  def init(): Unit
  def clear(): Unit
  def updatePosition(): Unit
  def startMove(x: Double, y: Double): Unit
  def move(x: Double, y: Double): Unit
  def endMove(x: Double, y: Double): Unit
  def setColor(color: Paint): Unit
  def setThickness(t: Double): Unit
  def setFillColor(color: Paint): Unit
  def setStyle(style: Style): Unit
  def getColor: Paint
  def getFillColor: Paint
  def getThickness: Double
  def setFontSize(n: Int)
  def setFont(f: Font)
  def getFontSize: Int
  def getFont: Font
  def write(text: String)
}
