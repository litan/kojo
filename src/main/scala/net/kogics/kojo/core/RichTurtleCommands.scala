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

package net.kogics.kojo.core

trait RichTurtleCommands {
  def turn(angle: Double): Unit
  def forward(n: Double): Unit

  def left(angle: Double): Unit = turn(angle)
  def right(angle: Double) = turn(-angle)
  def left(): Unit = left(90)
  def right(): Unit = right(90)
  def back(n: Double) = forward(-n)
  def back(): Unit = back(25)

  def arc2(r: Double, a: Double): Unit = throw new UnsupportedOperationException("making arcs is not supported")
  def left(angle: Double, rad: Double): Unit = arc2(rad, angle)
  def right(angle: Double, rad: Double): Unit = { if (angle == 0) return; right(180); arc2(rad, -angle) }
  def turn(angle: Double, rad: Double): Unit = if (angle < 0) right(-angle, rad) else left(angle, rad)
  def arc(r: Double, a: Double): Unit = turn(a, r)
}
