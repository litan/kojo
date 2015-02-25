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

object Defaults {

  val lineWidth = 0.02
  val axisRadius = 0.01
  val axisLength = 3.0
  val axisTickInterval = 1.0
  val axisTickSize = 0.03
  val turtleSize = 0.25
  val cameraWidth = 320
  val cameraHeight = 240
  val frequency = 30
  val quality = 0
  val intermediateRendering = true
  val mouseControl = true
  val defaultLightsOn = true
  val axesVisible = true
  val mouseControlAngleRatio = 0.3		// pixels to degrees
  val mouseControlDistanceRatio = 0.03	// pixels to length units
}

object DefaultLights {
  
  val lights = List[Light](
        new PointLight(Vector3d(2, 2, 2), Vector3d(5, 5, 5)),
        new PointLight(Vector3d(-2, 2, 2), Vector3d(2, 2, 2)))
}