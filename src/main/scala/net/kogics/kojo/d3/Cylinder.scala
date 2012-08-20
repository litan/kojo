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

class Cylinder(location : Vector3d,
			   orientation : Quaternion4d,
			   material : Material,
               val radius : Double,
               val height : Double) extends Shape(location, orientation, material) {

  def intersection(ray : Ray) = {
    
    val localRayOrigin = orientation.rotate(ray.origin - location)
    val localRayDirection = orientation.rotate(ray.direction)

    val a = localRayDirection.x * localRayDirection.x + localRayDirection.y * localRayDirection.y
    val b = 2 * (localRayOrigin.x * localRayDirection.x + localRayOrigin.y * localRayDirection.y)
    val c = localRayOrigin.x * localRayOrigin.x + localRayOrigin.y * localRayOrigin.y - radius * radius
    
    val D = b * b - 4 * a * c
    if (D < 0.0d)
      None
    else {
      val t0 = (-b - sqrt(D)) / (2 * a)
      val t1 = (-b + sqrt(D)) / (2 * a)
      val z0 = localRayOrigin.z + localRayDirection.z * t0
      val z1 = localRayOrigin.z + localRayDirection.z * t1
      
      var retvalue = false
      var t = Double.MaxValue
      if ((z0 < height) && (z0 > 0d) && (t0 > 0.001d) && (t0 < t)) {
        t = t0
        retvalue = true
      }
      if ((z1 < height) && (z1 > 0d) && (t1 > 0.001d) && (t1 < t)) {
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
    val localPointCoordinates = orientation.rotate(point - location)
    val localNormal = (localPointCoordinates - Vector3d(0d, 0d, localPointCoordinates.z)).normalized
    val normal = -orientation.rotate(-localNormal)
    
    var color = Vector3d(0.0d, 0.0d, 0.0d)
    
    for (light <- lights) {
      if(light.isInstanceOf[PointLight]) {
        val pointLight = light.asInstanceOf[PointLight]
        val toLight = (pointLight.position - point)

        val cosine = normal * toLight.normalized

        if (cosine >= 0.0) {
          val toLightMagnitude = toLight.magnitude
          color += material.color ** pointLight.brightness * cosine / (toLightMagnitude * toLightMagnitude)
        }
      }
    }
      
    color += Light.ambient
    
    color
  }
}
