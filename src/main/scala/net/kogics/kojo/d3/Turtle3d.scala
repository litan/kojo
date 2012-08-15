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
import java.awt.Color

class Turtle3d(val position : Vector3d = Vector3d(),
               val orientation : Quaternion4d = Quaternion4d(),
               val material : Material = Material(1d, 1d, 1d),
               val visible : Boolean = true,
               val trail : Boolean = true,
               val lineWidth : Double = Defaults.lineWidth) extends Mover[Turtle3d]{
  
  def setPosition(position : Vector3d) =
    new Turtle3d(position, orientation, material, visible, trail, lineWidth)
  def setOrientation(orientation : Quaternion4d) =
    new Turtle3d(position, orientation, material, visible, trail, lineWidth)
  def setVisible(visible : Boolean) =
    new Turtle3d(position, orientation, material, visible, trail, lineWidth)
  def setTrail(trail : Boolean) =
    new Turtle3d(position, orientation, material, visible, trail, lineWidth)
  def setLineWidth(lineWidth : Double) =
    new Turtle3d(position, orientation, material, visible, trail, lineWidth)
  def setColor(r : Int, g : Int, b : Int) =
    new Turtle3d(position, orientation, material.setColor(r, g, b), visible, trail, lineWidth)
  def setColor(r : Double, g : Double, b : Double) =
    new Turtle3d(position, orientation, material.setColor(r, g, b), visible, trail, lineWidth)
  def setColor(color : Color) =
    new Turtle3d(position, orientation, material.setColor(color), visible, trail, lineWidth)

  def avatar = List[Shape](
    new Sphere(absolutePosition(Vector3d(0d, 0d, 0d)),
      orientation,
      Material(0.05d, 0.3d, 0.05d),
      0.5 * Defaults.turtleSize),
    new Sphere(absolutePosition(Vector3d(0d, 0d, 0.55d)),
      orientation,
      Material(0.2d, 0.6d, 0.7d),
      0.25 * Defaults.turtleSize),
    new Sphere(absolutePosition(Vector3d(-0.25d, -0.25d, 0.25d)),
      orientation,
      Material(0.4d, 0.8d, 0.6d),
      0.25 * Defaults.turtleSize),
    new Sphere(absolutePosition(Vector3d(0.25d, -0.25d, 0.25d)),
      orientation,
      Material(0.4d, 0.8d, 0.6d),
      0.25 * Defaults.turtleSize),
    new Sphere(absolutePosition(Vector3d(-0.25d, -0.25d, -0.25d)),
      orientation,
      Material(0.5d, 0.4d, 0.2d),
      0.25 * Defaults.turtleSize),
    new Sphere(absolutePosition(Vector3d(0.25d, -0.25d, -0.25d)),
      orientation,
      Material(0.5d, 0.4d, 0.2d),
      0.25 * Defaults.turtleSize))
  
  def absolutePosition(local : Vector3d) = position + (-orientation).rotate(local*Defaults.turtleSize)
}