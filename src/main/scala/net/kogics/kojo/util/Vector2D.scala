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

package net.kogics.kojo.util

import com.vividsolutions.jts.math.{Vector2D => Vec2D}

case class Vector2D(x: Double, y: Double) {
  import language.implicitConversions
  implicit def wrap(v: Vec2D): Vector2D = Vector2D(v.getX, v.getY)
  implicit def unwrap(v: Vector2D): Vec2D = new Vec2D(v.x, v.y)

  val vec = new Vec2D(x, y)
  
  def rotate(angle: Double): Vector2D = {
    vec.rotate(angle.toRadians)
  }
  def scale(factor: Double): Vector2D = {
    vec.multiply(factor)
  }
  def add(other: Vector2D): Vector2D = {
    vec.add(other)
  }
  
  def + (other: Vector2D): Vector2D = vec.add(other)
  def * (factor: Double): Vector2D = vec.multiply(factor)
  
  def heading = vec.angle.toDegrees
  def angle(v: Vector2D) = vec.angle(v).toDegrees
  def angleTo(v: Vector2D) = vec.angleTo(v).toDegrees
  override def toString = "Vector2D(%.2f , %.2f)" format(x, y)
}
