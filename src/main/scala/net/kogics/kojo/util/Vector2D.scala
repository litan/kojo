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

import com.vividsolutions.jts.math.{ Vector2D => Vec2D }

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
  def normalize: Vector2D = vec.normalize
  def magnitude = vec.length
  def magSquared = vec.lengthSquared
  def limit(m: Double): Vector2D = {
    if (magnitude < m) this
    else normalize * m
  }
  def +(other: Vector2D): Vector2D = vec.add(other)
  def -(other: Vector2D): Vector2D = vec.subtract(other)
  def *(factor: Double): Vector2D = vec.multiply(factor)
  def /(factor: Double): Vector2D = vec.divide(factor)

  def dot(other: Vector2D): Double = vec.dot(other)

  // project this vector onto other
  def project(other: Vector2D): Vector2D = other * (other.dot(this) / other.magSquared)

  def lerp(other: Vector2D, frac: Double): Vector2D = vec.weightedSum(other, frac)
  def distance(other: Vector2D): Double = vec.distance(other)

  def heading = vec.angle.toDegrees
  def angle(v: Vector2D) = vec.angle(v).toDegrees
  def angleTo(v: Vector2D) = vec.angleTo(v).toDegrees
  def unary_- : Vector2D = vec.negate()
  def bounceOff(other: Vector2D) = {
    val a = angleTo(other)
    rotate(-2 * (180 - a))
  }
  override def toString = "Vector2D(%.2f , %.2f)".format(x, y)
  override def equals(other: Any) = other match {
    case v: Vector2D =>
      Utils.doublesEqual(x, v.x, 1e-6) && Utils.doublesEqual(y, v.y, 1e-6)
    case _ => false
  }
}
