/*
 * Copyright (C) 2021 Anay Kamat <kamatanay@gmail.com>
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

package net.kogics.kojo.core

import java.awt.geom.{Arc2D, GeneralPath}

class Rich2DPath(p: GeneralPath) {
  def arc(endPointX: Double, endPointY: Double, angleOfArc: Double): Unit = {
    val arcAngle = -(angleOfArc % 360)
    val currentPoint = p.getCurrentPoint
    val startPoint: (Double, Double) = (currentPoint.getX, currentPoint.getY)
    val endPoint: (Double, Double) = (endPointX, endPointY)

    val midPointOfSegment = (
      (startPoint._1 + endPoint._1) / 2,
      (startPoint._2 + endPoint._2) / 2,
    )

    val directionVector = (
      (endPoint._1 - startPoint._1) / 2,
      (endPoint._2 - startPoint._2) / 2,
    )

    val lengthOfDirectionVector = Math.hypot(directionVector._1, directionVector._2)

    val unitVector = (
      directionVector._1 / lengthOfDirectionVector,
      directionVector._2 / lengthOfDirectionVector,
    )

    val leftPerpendicularVector = (
      unitVector._2,
      -unitVector._1
    )

    val t: Double = Math.abs(arcAngle) match {
      case value if value == 0.0 => lengthOfDirectionVector
      case value if Math.abs(value) == 180.0 => 0.0
      case _ => lengthOfDirectionVector / Math.tan(Math.toRadians(arcAngle) / 2.0)
    }

    val centerPoint = (
      midPointOfSegment._1 + leftPerpendicularVector._1 * t,
      midPointOfSegment._2 + leftPerpendicularVector._2 * t,
    )

    val pointToFindRadius = (
      centerPoint._1 - startPoint._1,
      centerPoint._2 - startPoint._2,
    )


    val radius = Math.hypot(pointToFindRadius._1, pointToFindRadius._2)

    val arcStartX = centerPoint._1 - radius
    val arcStartY = centerPoint._2 - radius


    val width: Double = 2.0 * radius

    val (x0, y0) = centerPoint
    val (x1, y1) = startPoint
    val (x2, y2) = endPoint

    val startAngle: Double = (-180 / Math.PI * Math.atan2(y1 - y0, x1 - x0))

    val arc = new Arc2D.Double(arcStartX, arcStartY, width, width, startAngle, arcAngle, Arc2D.OPEN)
    p.append(arc, true)
  }
}
