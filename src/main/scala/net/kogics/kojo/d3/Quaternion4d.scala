/*
 * Copyright (C) 2012 Jerzy Redlarski <5xinef@gmail.com>
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

package net.kogics.kojo.d3

import java.lang.Math._

case class Quaternion4d(val v: Vector3d = Vector3d(0d, 0d, 0d), val w: Double = 1d) {

  def unary_- = Quaternion4d(Vector3d(-v.x, -v.y, -v.z), w)
  def +(q : Quaternion4d) = Quaternion4d(v + q.v, w + q.w)
  def -(q : Quaternion4d) = Quaternion4d(v - q.v, w - q.w)
  def *(q : Quaternion4d) = Quaternion4d((q.v * w) + (v * q.w) + (v ^ q.v),
                                         w * q.w - v * q.v)
  def *(s : Double) = Quaternion4d(v * s, w * s)
  def /(s : Double) = Quaternion4d(v / s, w / s)
  def *(v : Vector3d) : Quaternion4d = this * Quaternion4d(v, 0d)
  def rotate(q : Quaternion4d) = this * q * -this
  def rotate(v : Vector3d) = (this * v * -this).v
  def angle = 2 * acos(w)
  def axis = v / v.magnitude
  def magnitude = sqrt(w * w + v.x * v.x + v.y * v.y + v.z * v.z)
  def normalized = {
    val magnitudeValue = magnitude
    if (magnitudeValue > 0.0)
      (this / magnitudeValue)
    else
      (this)
  }
}

object Quaternion4d {
  
  def fromAxisAngle(axis: Vector3d = Vector3d(0d, 0d, 0d), angle: Double = 0d) = {
    val angleRadiansHalved = angle * PI / 360d
    Quaternion4d(axis.normalized * sin(angleRadiansHalved), cos(angleRadiansHalved))
  }
}