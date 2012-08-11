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

case class Vector3d(val x: Double = 0d, val y: Double = 0d, val z: Double = 0d) {

  def unary_- = Vector3d(-x, -y, -z)
  def +(v: Vector3d) = Vector3d(x + v.x, y + v.y, z + v.z)
  def -(v: Vector3d) = Vector3d(x - v.x, y - v.y, z - v.z)
  def ^(v: Vector3d) = Vector3d(y * v.z - z * v.y, -x * v.z + z * v.x, x * v.y - y * v.x)
  def *(v: Vector3d) = x * v.x + y * v.y + z * v.z
  def **(v: Vector3d) = Vector3d(x * v.x, y * v.y, z * v.z)
  def *(s: Double) = Vector3d(x * s, y * s, z * s)
  def /(s: Double) = Vector3d(x / s, y / s, z / s)
  def magnitude = sqrt(x * x + y * y + z * z)
  def normalized = {
    val magnitudeValue = magnitude
    if (magnitudeValue > 0.0)
      (this / magnitudeValue)
    else
      (this)
  }
}
