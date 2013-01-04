/*
 * Copyright (C) 2009 Lalit Pant <pant.lalit@gmail.com>
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
package net.kogics.kojo.kgeom

import java.awt._
import java.awt.geom._

import edu.umd.cs.piccolo._
import edu.umd.cs.piccolo.util._

import scala.collection._

object PolyLine {
  def apply(points: Seq[Point2D.Double]) = {
    val result = new PolyLine()
    points.foreach { result.addPoint }
    result
  }
}

class PolyLine extends PNode {

  val polyLinePath = new Path2D.Double()

  val points = new mutable.ArrayBuffer[Point2D.Double]
  var stroke = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)
  var strokePaint: Paint = Color.red

  var closed = false

  def addPoint(x: Double, y: Double): Unit = addPoint(new Point2D.Double(x, y))
  def lineTo(x: Double, y: Double) = addPoint(new Point2D.Double(x, y))
  def removeLastPoint() {
    points.remove(points.size-1)
    buildGeneralPath()
  }

  def size = points.size

  def reset() {
    points.clear()
    polyLinePath.reset()
  }

  def setStroke(strk: Stroke) {
    stroke = strk.asInstanceOf[BasicStroke]
  }

  def setStrokePaint(c: Paint) {
    strokePaint = c
  }

  def strokeThickness = stroke.getLineWidth

  def addPoint(p: Point2D.Double): Unit = {
    points += p
    if (points.size == 1) {
      polyLinePath.reset()
      return
    }

    if (points.size == 2) {
      polyLinePath.moveTo(points(0).x, points(0).y)
    }

    polyLinePath.lineTo(p.x, p.y)
    
    updateBounds()
  }

  def close() {
    closed = true
  }

  override def paint(paintContext: PPaintContext) {
    val g2 = paintContext.getGraphics()
    val fillPaint = getPaint()

    if (fillPaint != null) {
      g2.setPaint(fillPaint)
      g2.fill(polyLinePath)
    }

    g2.setStroke(stroke)
    g2.setPaint(strokePaint)
    g2.draw(polyLinePath)
  }

  def updateBounds() {
    // the line below significantly slows things down for things like 36 circles
//    val b = stroke.createStrokedShape(polyLinePath).getBounds2D()
    val b = polyLinePath.getBounds2D()
    val w = stroke.getLineWidth
    super.setBounds(b.getX()-w/2.0, b.getY()-w/2.0, b.getWidth()+w, b.getHeight()+w)
    repaint()
  }

  def buildGeneralPath(): Path2D = {
    polyLinePath.reset()
    if (points.size == 1) return polyLinePath

    val piter = points.iterator
    if (piter.hasNext) {
      var point = piter.next
      val point0 = point
      polyLinePath.moveTo(point.x, point.y)
      while (piter.hasNext) {
        point = piter.next
        polyLinePath.lineTo(point.x, point.y)
      }
      if (closed) polyLinePath.lineTo(point0.x, point0.y)
    }
    polyLinePath
  }

  override def setBounds(x: Double, y: Double, width: Double, height: Double): Boolean = {
    println("Cannot set bounds")
    false
  }
  
  def map(fn: Point2D.Double => Point2D.Double) = {
    val result = PolyLine(points.map(fn))
    result.setPaint(getPaint)
    result.setStrokePaint(strokePaint)
    result.setStroke(stroke)
    result
  }
}
