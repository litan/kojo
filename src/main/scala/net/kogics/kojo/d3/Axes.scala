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

object Axes {

  def avatar = List[Shape](
        new Cylinder(Vector3d(0d, 0d, 0d),
            Quaternion4d.fromAxisAngle(Vector3d(0d, 1d, 0d), -90),
            Material(1d, 0d, 0d),
            Defaults.axisRadius,
            Defaults.axisLength),
        new Cylinder(Vector3d(0d, 0d, 0d),
            Quaternion4d.fromAxisAngle(Vector3d(1d, 0d, 0d), 90),
            Material(0d, 1d, 0d),
            Defaults.axisRadius,
            Defaults.axisLength),
        new Cylinder(Vector3d(0d, 0d, 0d),
            Quaternion4d(),
            Material(0d, 0d, 1d),
            Defaults.axisRadius,
            Defaults.axisLength))
  
  def avatarWithTicks = 
      ticks(0,
      Quaternion4d.fromAxisAngle(Vector3d(0d, 1d, 0d), -90),
      Material(1d, 0d, 0d)) :::
      ticks(0,
      Quaternion4d.fromAxisAngle(Vector3d(1d, 0d, 0d), 90),
      Material(0d, 1d, 0d)) :::
      ticks(0,
      Quaternion4d(),
      Material(0d, 0d, 1d)) ::: avatar
  
  def ticks(distance : Double, direction : Quaternion4d, material : Material) : List[Shape] = {
    if(distance > Defaults.axisLength) Nil
    else new Sphere((-direction).rotate(Vector3d(0d, 0d, distance)),
            direction,
            material,
            Defaults.axisTickSize) :: ticks(distance + Defaults.axisTickInterval, direction, material)
  }
}