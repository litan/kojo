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

trait Mover[T] {

  val position : Vector3d
  val orientation : Quaternion4d
  
  def setPosition(position : Vector3d) : T
  def setOrientation(orientation : Quaternion4d) : T
  
  def forward(distance : Double) = {
    setPosition(position + (-orientation).rotate(Vector3d(0.0d, 0.0d, distance)))
  }
  
  def back(distance : Double) = {
    forward(-distance)
  }
  
  def turn(angle : Double) = {
    val vector = (-orientation).rotate(Vector3d(0d, -1d, 0d))
    val rotation = Quaternion4d.fromAxisAngle(vector, angle)
    setOrientation(orientation * rotation)
  }
  
  def left(angle : Double) = {
    turn(angle)
  }
  
  def right(angle : Double) = {
    turn(-angle)
  }
  
  def pitch(angle : Double) = {
    val vector = (-orientation).rotate(Vector3d(-1d, 0d, 0d))
    val rotation = Quaternion4d.fromAxisAngle(vector, angle)
    setOrientation(orientation * rotation)
  }
  
  def roll(angle : Double) = {
    val vector = (-orientation).rotate(Vector3d(0d, 0d, -1d))
    val rotation = Quaternion4d.fromAxisAngle(vector, angle)
    setOrientation(orientation * rotation)
  }
  
  def moveTo(x : Double, y : Double, z : Double) = {
    setPosition(Vector3d(x, y, z))
  }
  
  def lookAt(x : Double, y : Double, z : Double) = {
    require(Vector(x, y, z) != position, "The lookAt command should not be told to look at the current position of the object to be rotated")
    val toTarget = (Vector3d(x, y, z) - position).normalized
    val currentForward = (-orientation).rotate(Vector3d(0d, 0d, 1d)).normalized
    val rotationAxis = toTarget ^ currentForward
    val angle = acos(toTarget * currentForward).toDegrees
    val rotation = Quaternion4d.fromAxisAngle(rotationAxis, angle)
    setOrientation(orientation * rotation)
  }
  
  def strafeUp(distance : Double) = {
    setPosition(position + (-orientation).rotate(Vector3d(0.0d, distance, 0.0d)))
  }
  
  def strafeDown(distance : Double) = {
    strafeUp(-distance)
  }
  
  def strafeLeft(distance : Double) = {
    setPosition(position + (-orientation).rotate(Vector3d(distance, 0.0d, 0.0d)))
  }
  
  def strafeRight(distance : Double) = {
    strafeLeft(-distance)
  }
}
