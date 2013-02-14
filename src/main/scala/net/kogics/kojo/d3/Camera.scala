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
import java.awt.image.BufferedImage

abstract class Camera(val position : Vector3d = Vector3d(0d, 0d, 10d),
             val orientation : Quaternion4d = Quaternion4d(),
             val width : Int = Defaults.cameraWidth,
             val height : Int = Defaults.cameraHeight,
             val axesVisible : Boolean = Defaults.axesVisible,
             val defaultLightsOn : Boolean = Defaults.defaultLightsOn,
             val frequency : Int = Defaults.frequency) extends Mover[Camera] {
  import language.postfixOps
  
  def setPosition(position : Vector3d) : Camera
  def setOrientation(orientation : Quaternion4d) : Camera
  def setPictureDimensions(width : Int, height : Int) : Camera
  def setAxesVisibility(axesVisible : Boolean) : Camera
  def setFrequency(frequency : Int) : Camera
  def setDefaultLights(defaultLightsOn : Boolean) : Camera
  
  def render(shapes : List[Shape], lights : List[Light], turtle : Turtle3d) : BufferedImage

  def clip(brightness: Double) = {
    (1.0 - exp(-brightness)) toFloat
  }
}
