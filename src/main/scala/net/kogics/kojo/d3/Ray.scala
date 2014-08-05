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

class Ray(val origin : Vector3d, dir : Vector3d) {

  val direction = dir.normalized

  def trace(shapes : List[Shape], lights : List[Light], turtle : Turtle3d, axesVisible : Boolean, defaultLightsOn : Boolean) = {
    val allShapes = {
      val withTurtle = if (turtle.visible) {
        turtle.avatar ::: shapes
      } else
        shapes

      if (axesVisible) {
        Axes.avatarWithTicks ::: withTurtle
      } else
        withTurtle
    }

    val allLights = {
      if (defaultLightsOn)
        DefaultLights.lights ::: lights
      else
        lights
    }

    val (distance, closestShape) = allShapes.foldLeft(
      (Double.MaxValue, None : Option[Shape]))(
        (result, shape) => shape.intersection(this) match {
          case Some(t) => {
            if (t < result._1) (t, Option(shape))
            else result
          }
          case None => result
        })

    closestShape match {
      case Some(t) => {
        val point = origin + direction * distance
        t.shade(point, allLights)
      }
      case None => {
        new Vector3d(0.4d, 0.5d, 0.6d)
      }
    }
  }
}
