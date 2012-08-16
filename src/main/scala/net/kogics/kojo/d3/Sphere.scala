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

class Sphere(location : Vector3d,
			 orientation : Quaternion4d,
			 material : Material,
			 val radius : Double) extends Shape(location, orientation, material) {

  def intersection(ray : Ray) = {

    val dist = location - ray.origin
    val B = ray.direction * dist
    val D = B * B - dist * dist + radius * radius
    if (D < 0.0d)
      None
    else {
      val t0 = B - sqrt(D)
      val t1 = B + sqrt(D)
      var retvalue = false
      var t = Double.MaxValue
      if ((t0 > 0.001d) && (t0 < t)) {
        t = t0
        retvalue = true
      }
      if ((t1 > 0.001d) && (t1 < t)) {
        t = t1
        retvalue = true
      }
      if (retvalue)
        Option(t)
      else
        None
    }
  }
  
  def shade(point : Vector3d, lights : List[Light]) = {
    val localPontCoordinates = point - location
    val normal = localPontCoordinates.normalized
    
    var color = Vector3d(0.0d, 0.0d, 0.0d)
    
    for (light <- lights) {
      if(light.isInstanceOf[PointLight]) {
        val pointLight = light.asInstanceOf[PointLight]
        val toLight = (pointLight.position - point)

        val cosine = normal * toLight.normalized

        if (cosine >= 0.0) {
          val toLightMagnitude = toLight.magnitude
          color += material.color ** pointLight.brightness * cosine /
          	(toLightMagnitude * toLightMagnitude)
        }
      }
    }
      
    color += Light.ambient
    
    color
  }
}