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

class Cube(location : Vector3d,
		   orientation : Quaternion4d,
		   material : Material,
           val dimension : Double) extends Shape(location, orientation, material) {

  def intersection(ray : Ray) = {
    
    val localRayOrigin = orientation.rotate(ray.origin - location)
    val localRayDirection = orientation.rotate(ray.direction)

    val xTest = oneDimensionTest(ray, localRayOrigin.x, localRayDirection.x,
                                 Double.NegativeInfinity, Double.PositiveInfinity)
    xTest match {
      case None => None
      case Some((tNear, tFar)) => {
        val yTest = oneDimensionTest(ray, localRayOrigin.y, localRayDirection.y,
                                 tNear, tFar)
        yTest match {
          case None => None
          case Some((tNear, tFar)) => {
              val zTest = oneDimensionTest(ray, localRayOrigin.z, localRayDirection.z,
                                    tNear, tFar)
              zTest match {
                case None => None
                case Some((tNear, tFar)) => Option(tNear)
              }
          }
        }
      }
    }
  }
  
  def oneDimensionTest(ray : Ray,
                       origin : Double,
                       direction : Double,
                       tNear : Double,
                       tFar : Double) : Option[(Double, Double)] = {
    if((direction == 0d) && (abs(origin) > dimension)) {
      None
    }
    else {
      val t1 = (-dimension - origin) / direction
      val t2 = (dimension - origin) / direction
      val tNearer = max(min(t1,t2), tNear)
      val tFarer = min(max(t1,t2), tFar)
      if(tNearer > tFarer || tFarer < 0d)
    	  None
      else
    	  Option(tNearer, tFarer)
    }
  }
  
  def shade(point : Vector3d, lights : List[Light]) = {
    val localPointCoordinates = orientation.rotate(point - location)
    val normal = -orientation.rotate(-localNormal(localPointCoordinates))
    
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
  
  def localNormal(pointCoordinates : Vector3d) = {
    val normalizedCoordinates = pointCoordinates.normalized
    
    val maxValue = max(max(abs(normalizedCoordinates.x),
    					abs(normalizedCoordinates.y)),
    					abs(normalizedCoordinates.z))
    
    if(abs(normalizedCoordinates.x) == maxValue) {
      if(normalizedCoordinates.x > 0d)
    	  Vector3d(1d, 0d, 0d)
      else
    	  Vector3d(-1d, 0d, 0d)
    }
    else if(abs(normalizedCoordinates.y) == maxValue) {
      if(normalizedCoordinates.y > 0d)
    	  Vector3d(0d, 1d, 0d)
      else
    	  Vector3d(0d, -1d, 0d)
    }
    else {
      if(normalizedCoordinates.z > 0d)
    	  Vector3d(0d, 0d, 1d)
      else
    	  Vector3d(0d, 0d, -1d)
    }
  }
}
