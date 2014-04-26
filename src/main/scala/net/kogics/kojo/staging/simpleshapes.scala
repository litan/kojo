/*
 * Copyright (C) 2010 Peter Lewerin <peter.lewerin@tele2.se>
 * Copyright (C) 2010 Lalit Pant <pant.lalit@gmail.com>
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

package net.kogics.kojo
package staging

import edu.umd.cs.piccolo._
import edu.umd.cs.piccolo.nodes._
import edu.umd.cs.piccolo.util._
import edu.umd.cs.piccolo.event._

import net.kogics.kojo.util.Utils

import javax.swing._

import core._
import math._
import Impl.API

import language.postfixOps

class Dot(val origin: Point) extends StrokedShape {
  val path = PPath.createLine(
    origin.x.toFloat, origin.y.toFloat,
    origin.x.toFloat, origin.y.toFloat
  )

  override def toString = "Staging.Dot(" + origin + ")"
}
object Dot {
  def apply(p: Point) = {
    val shape = new Dot(p)
    // The JMM (Java Memory Model) - section 17.5 - says that
    // after the constructor for an object is done, its final fields (and the objects they refer to)
    // are visible to other threads without synchronization.
    // Refs:
    // http://java.sun.com/docs/books/jls/third_edition/html/memory.html#66562
    // http://www.cs.umd.edu/~pugh/java/memoryModel/jsr-133-faq.html#finalRight
    // So, shape.node should be visible to the Swing thread
    Impl.figure0.addPnode(shape.node)
    shape
  }
}

class Line(val origin: Point, val endpoint: Point) extends SimpleShape {
  val path =
    PPath.createLine(origin.x.toFloat, origin.y.toFloat, endpoint.x.toFloat, endpoint.y.toFloat)

  override def toString = "Staging.Line(" + origin + ", " + endpoint + ")"
}
object Line {
  def apply(p1: Point, p2: Point) = {
    val shape = new Line(p1, p2)
    Impl.figure0.addPnode(shape.node)
    shape
  }
}

class Rectangle(val origin: Point, val endpoint: Point) extends SimpleShape {
  // precondition endpoint > origin
  require(width > 0 && height > 0, "A Rectangles's width and height should be more than 0.")
  val path =
    PPath.createRectangle(origin.x.toFloat, origin.y.toFloat, width.toFloat, height.toFloat)

  override def toString = "Staging.Rectangle(" + origin + ", " + endpoint + ")"
}
object Rectangle {
  def apply(p1: Point, p2: Point) = {
    val shape = new Rectangle(p1, p2)
    Impl.figure0.addPnode(shape.node)
    shape
  }
}

class RoundRectangle(
  val origin: Point,
  val endpoint: Point,
  val curvature: Point
) extends SimpleShape with Rounded {
  // precondition endpoint > origin
  require(width > 0 && height > 0, "A RoundRectangles's width and height should be more than 0.")
  val path =
    PPath.createRoundRectangle(
      origin.x.toFloat, origin.y.toFloat,
      width.toFloat, height.toFloat,
      curvature.x.toFloat, curvature.y.toFloat
    )

  override def toString =
    "Staging.RoundRectangle(" + origin + ", " + endpoint + ", " + curvature + ")"
}
object RoundRectangle {
  def apply(p1: Point, p2: Point, p3: Point) = {
    val shape = new RoundRectangle(p1, p2, p3)
    Impl.figure0.addPnode(shape.node)
    shape
  }
}

class Ellipse(val origin: Point, val endpoint: Point) extends Elliptical {
  val path = PPath.createEllipse(
    (origin.x - radiusX).toFloat, (origin.y - radiusY).toFloat,
    width.toFloat, height.toFloat
  )

  override def toString = "Staging.Ellipse(" + origin + "," + endpoint + ")"
}
object Ellipse {
  def apply(p1: Point, p2: Point) = {
    val shape = new Ellipse(p1, p2)
    Impl.figure0.addPnode(shape.node)
    shape
  }
}

class Arc(
  val origin: Point, val endpoint: Point,
  val start: Double, val extent: Double,
  val kind: Int
) extends Elliptical {
  val path = new PPath
  path.setPathTo(new java.awt.geom.Arc2D.Double(
      (origin.x - radiusX), (origin.y - radiusY), width, height,
      -start, -extent, kind
    ))

  override def toString = "Staging.Arc(" + origin + "," + endpoint + start + "," + extent + ")"
}
object Arc {
  def apply(p1: Point, p2: Point, s: Double, e: Double, k: Int) = {
    val shape = new Arc(p1, p2, s, e, k)
    Impl.figure0.addPnode(shape.node)
    shape
  }
}

class Cross(val origin: Point,
            val endpoint: Point,
            val crossWidth: Double,
            val ratio: Double,
            val greek: Boolean) extends SimpleShape with CrossShape {
  val pts = crossDims(width, height, crossWidth, ratio, greek) points
  val path = PPath.createPolyline((pts map { case Point(x, y) =>
          new java.awt.geom.Point2D.Double(x + origin.x, y + origin.y)
      }).toArray)
  path.closePath

  override def toString = "Staging.Cross(" +
  origin + "," + endpoint + "," + crossWidth + "," +
  ratio + "," + greek + "," + ")"
}
object Cross {
  def apply(origin: Point, endpoint: Point, crossWidth: Double, ratio: Double, greek: Boolean) = 
  {
    val shape = new Cross(origin, endpoint, crossWidth, ratio, greek)
    Impl.figure0.addPnode(shape.node)
    shape
  }
}

class CrossOutline(val origin: Point,
                   val endpoint: Point,
                   val crossWidth: Double,
                   val ratio: Double,
                   val greek: Boolean) extends SimpleShape with CrossShape {
  val pts = crossDims(width, height, crossWidth, ratio, greek) outlinePoints
  val path = PPath.createPolyline((pts map { case Point(x, y) =>
          new java.awt.geom.Point2D.Double(x + origin.x, y + origin.y)
      }).toArray)
  path.closePath

  override def toString = "Staging.CrossOutline(" +
  origin + "," + endpoint + "," + crossWidth + "," +
  ratio + "," + greek + "," + ")"
}
object CrossOutline {
  def apply(origin: Point, endpoint: Point, crossWidth: Double, ratio: Double, greek: Boolean) = 
  {
    val shape = new CrossOutline(origin, endpoint, crossWidth, ratio, greek)
    Impl.figure0.addPnode(shape.node)
    shape
  }
}

class Saltire(val origin: Point,
              val endpoint: Point,
              val crossWidth: Double) extends SimpleShape {
  val points = Saltire.saltirePoints(width, height, crossWidth)
  val path = PPath.createPolyline((points map { case Point(x, y) =>
          new java.awt.geom.Point2D.Double(x + origin.x, y + origin.y)
      }).toArray)
  path.closePath

  override def toString = "Staging.Saltire(" + origin + "," + endpoint + "," + crossWidth + ")"
}
object Saltire {
  def saltirePoints(len: Double, wid: Double, cw: Double) = {
    val hl  = len / 2
    val hw  = wid / 2
    val hcw = cw / 2
    val my  = cw * 0.36
    val mx  = cw * 0.83
    List(
      // 0-1     3-4
      // f  \   /  5
      //  \   V   /
      //   \  2  /
      //   >e   6<
      //   /  a  \
      //  /   A   \
      // d  /   \  7
      // c-b     9-8
      Point(0,         wid),
      Point(0 + hcw,   wid),
      Point(hl,        hw + my),
      Point(len - hcw, wid),
      Point(len,       wid),
      Point(len,       wid - hcw),
      Point(hl + mx,   hw),
      Point(len,       hcw),
      Point(len,       0),
      Point(len - hcw, 0),
      Point(hl,        hw - my),
      Point(hcw,       0),
      Point(0,         0),
      Point(0,         hcw),
      Point(hl - mx,   hw),
      Point(0,         wid - hcw)
    )
  }

  def apply(origin: Point, endpoint: Point, crossWidth: Double) = {
    val shape = new Saltire(origin, endpoint, crossWidth)
    Impl.figure0.addPnode(shape.node)
    shape
  }
}

class SaltireOutline(val origin: Point,
                     val endpoint: Point,
                     val crossWidth: Double) extends SimpleShape {
  val path = new PPath

  // TODO scale outset to crossWidth
  val points = Saltire.saltirePoints(width, height, crossWidth)
  (points map (origin + _) grouped(4) zipWithIndex) foreach {
    case (Seq(_, Point(x0, y0), Point(x1, y1), Point(x2, y2)), 0) =>
      path.moveTo(x0.toFloat - 1, y0.toFloat)
      path.lineTo(x0.toFloat + 2, y0.toFloat)
      path.lineTo(x1.toFloat,     y1.toFloat + 1)
      path.lineTo(x2.toFloat - 2, y2.toFloat)
      path.lineTo(x2.toFloat + 1, y2.toFloat)
      path.lineTo(x1.toFloat,     y1.toFloat - 1)
      path.closePath
    case (Seq(_, Point(x0, y0), Point(x1, y1), Point(x2, y2)), 1) =>
      path.moveTo(x0.toFloat,     y0.toFloat + 2)
      path.lineTo(x0.toFloat,     y0.toFloat - 1)
      path.lineTo(x1.toFloat + 4, y1.toFloat)
      path.lineTo(x2.toFloat,     y2.toFloat + 1)
      path.lineTo(x2.toFloat,     y2.toFloat - 2)
      path.lineTo(x1.toFloat - 1, y1.toFloat)
      path.closePath
    case (Seq(_, Point(x0, y0), Point(x1, y1), Point(x2, y2)), 2) =>
      path.moveTo(x0.toFloat + 1, y0.toFloat)
      path.lineTo(x0.toFloat - 2, y0.toFloat)
      path.lineTo(x1.toFloat,     y1.toFloat - 1)
      path.lineTo(x2.toFloat + 2, y2.toFloat)
      path.lineTo(x2.toFloat - 1, y2.toFloat)
      path.lineTo(x1.toFloat,     y1.toFloat + 1)
      path.closePath
    case (Seq(_, Point(x0, y0), Point(x1, y1), Point(x2, y2)), 3) =>
      path.moveTo(x0.toFloat,     y0.toFloat - 2)
      path.lineTo(x0.toFloat,     y0.toFloat + 1)
      path.lineTo(x1.toFloat - 4, y1.toFloat)
      path.lineTo(x2.toFloat,     y2.toFloat - 1)
      path.lineTo(x2.toFloat,     y2.toFloat + 2)
      path.lineTo(x1.toFloat + 1, y1.toFloat)
      path.closePath
  }

  override def toString = "Staging.SaltireOutline(" + origin + "," + endpoint + "," + crossWidth + ")"
}
object SaltireOutline {
  def apply(origin: Point, endpoint: Point, crossWidth: Double) = {
    val shape = new SaltireOutline(origin, endpoint, crossWidth)
    Impl.figure0.addPnode(shape.node)
    shape
  }
}

class Vector(val origin: Point, val endpoint: Point, val length: Double) extends SimpleShape {
  val path = new PPath

  val vlength = API.dist(origin, endpoint)
  val arrowHalfWidth = length / 3

  def init = {
    path.moveTo(origin.x.toFloat, origin.y.toFloat)
    val (x, y) = ((origin.x + vlength).toFloat, origin.y.toFloat)
    path.lineTo(x, y)
    path.moveTo(x, y)
    path.lineTo(x - length.toFloat, y - arrowHalfWidth.toFloat)
    path.lineTo(x - length.toFloat, y + arrowHalfWidth.toFloat)
    path.closePath
  }

  init

  val angle =
    if (origin.x < endpoint.x) { math.asin((endpoint.y - origin.y) / vlength) }
  else { math.Pi - math.asin((endpoint.y - origin.y) / vlength) }

  node.rotateAboutPoint(angle, origin.x, origin.y)

  override def toString = "Staging.Vector(" + origin + ", " + endpoint + ")"
}
object Vector {
  def apply(p1: Point, p2: Point, length: Double) = Utils.runInSwingThreadAndWait {
    val shape = new Vector(p1, p2, length)
    Impl.figure0.pnode(shape.node)
    shape
  }
}

object Star {
  def apply(origin: Point, inner: Double, outer: Double, points: Int) = Utils.runInSwingThreadAndWait {
    val a = math.Pi / points // the angle between outer and inner point
    val pts = Seq.tabulate(2 * points){ i =>
      val aa = math.Pi / 2 + a * i
      if (i % 2 == 0) { origin + Point(outer * cos(aa), outer * sin(aa)) }
      else { origin + Point(inner * cos(aa), inner * sin(aa)) }
    }
    Polygon(pts)
  }
}

class Path(val origin: Point) extends StrokedShape {
  val path = new PPath
  moveTo(origin.x.toFloat, origin.y.toFloat)

  def lineTo(x: Double, y: Double) {
    Utils.runInSwingThread {
      path.lineTo(x.toFloat, y.toFloat)
    }
  }

  def moveTo(x: Double, y: Double) {
    Utils.runInSwingThread {
      path.moveTo(x.toFloat, y.toFloat)
    }
  }
}
object Path {
  def apply(p1: Point) = Utils.runInSwingThreadAndWait {
    val shape = new Path(p1)
    Impl.figure0.pnode(shape.node)
    shape
  }
}
