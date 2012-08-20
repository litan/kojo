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

class Plane(location : Vector3d,
			orientation : Quaternion4d,
			material : Material) extends Shape(location, orientation, material) {

  def intersection(ray : Ray) = {
    
    val localRayOrigin = orientation.rotate(ray.origin - location)
    val localRayDirection = orientation.rotate(ray.direction)

    val normal = Vector3d(0d, 0d, 1d)
    
    val EdotD = normal * localRayDirection
    
    if (EdotD == 0.0d)
      None
    else {
      val t = (normal * (-localRayOrigin)) / EdotD
      
      if (t > 0.001d)
        Option(t)
      else
        None
    }
  }
  
  def shade(point : Vector3d, lights : List[Light]) = {
    
    val normal = -orientation.rotate(Vector3d(0d, 0d, 1d))
    
    var color = Vector3d(0.0d, 0.0d, 0.0d)
    
    for (light <- lights) {
      if(light.isInstanceOf[PointLight]) {
        val pointLight = light.asInstanceOf[PointLight]
        val toLight = (pointLight.position - point)

        val cosine = normal * toLight.normalized

        val toLightMagnitude = toLight.magnitude
        color += material.color ** pointLight.brightness * abs(cosine) / (toLightMagnitude * toLightMagnitude)
      }
    }
      
    color += Light.ambient
    
    color
  }
}
