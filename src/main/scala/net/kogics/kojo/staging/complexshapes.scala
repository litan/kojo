/*
 * Copyright (C) 2010 Peter Lewerin <peter.lewerin@tele2.se>
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

import util.Utils
import edu.umd.cs.piccolo._
import edu.umd.cs.piccolo.nodes._
import edu.umd.cs.piccolo.util._
import edu.umd.cs.piccolo.event._

import javax.swing._

import core._

class Polyline(val points: Seq[Point]) extends PolyShape with StrokedShape {
  val path = PPath.createPolyline((points map {
        case Point(x, y) => new java.awt.geom.Point2D.Double(x, y)
      }).toArray)

  override def toString = "Staging.Polyline(" + points + ")"
}
object Polyline {
  def apply(pts: Seq[Point]) = Utils.runInSwingThreadAndWait {
    val shape = new Polyline(pts)
    Impl.figure0.pnode(shape.node)
    shape
  }
}

class Polygon(val points: Seq[Point]) extends PolyShape with StrokedShape {
  val path = PPath.createPolyline((points map {
        case Point(x, y) => new java.awt.geom.Point2D.Double(x, y)
      }).toArray)
  path.closePath

  override def toString = "Staging.Polygon(" + points + ")"
}
object Polygon {
  def apply(pts: Seq[Point]) = Utils.runInSwingThreadAndWait {
    val shape = new Polygon(pts)
    Impl.figure0.pnode(shape.node)
    shape
  }
}

class LinesShape(val points: Seq[Point]) extends PolyShape with StrokedShape {
  val path = new PPath

  def init = {
    points grouped(2) foreach {
      case Nil =>
      case Seq(Point(x1, y1), Point(x2, y2)) =>
        path.moveTo(x1.toFloat, y1.toFloat)
        path.lineTo(x2.toFloat, y2.toFloat)
      case p :: Nil =>
    }
  }

  init

  override def toString = "Staging.LinesShape(" + points + ")"
}
object LinesShape {
  def apply(pts: Seq[Point]) = Utils.runInSwingThreadAndWait {
    val shape = new LinesShape(pts)
    Impl.figure0.pnode(shape.node)
    shape
  }
}

class TrianglesShape(val points: Seq[Point]) extends PolyShape with StrokedShape {
  val path = new PPath

  def init = {
    points grouped(3) foreach {
      case Nil =>
      case Seq(Point(x0, y0), Point(x1, y1), Point(x2, y2)) =>
        path.moveTo(x0.toFloat, y0.toFloat)
        path.lineTo(x1.toFloat, y1.toFloat)
        path.lineTo(x2.toFloat, y2.toFloat)
        path.closePath
      case _ =>
    }
  }

  init

  override def toString = "Staging.TrianglesShape(" + points + ")"
}
object TrianglesShape {
  def apply(pts: Seq[Point]) = Utils.runInSwingThreadAndWait {
    val shape = new TrianglesShape(pts)
    Impl.figure0.pnode(shape.node)
    shape
  }
}

class TriangleStripShape(val points: Seq[Point]) extends PolyShape with StrokedShape {
  val path = new PPath

  def init = {
    points sliding(3) foreach {
      case Nil =>
      case Seq(Point(x0, y0), Point(x1, y1), Point(x2, y2)) =>
        path.moveTo(x0.toFloat, y0.toFloat)
        path.lineTo(x1.toFloat, y1.toFloat)
        path.lineTo(x2.toFloat, y2.toFloat)
        path.closePath
      case _ =>
    }
  }

  init

  override def toString = "Staging.TriangleStripShape(" + points + ")"
}
object TriangleStripShape {
  def apply(pts: Seq[Point]) = Utils.runInSwingThreadAndWait {
    val shape = new TriangleStripShape(pts)
    Impl.figure0.pnode(shape.node)
    shape
  }
}

class QuadsShape(val points: Seq[Point]) extends PolyShape with StrokedShape {
  val path = new PPath

  def init = {
    points grouped(4) foreach {
      case Nil =>
      case Seq(Point(x0, y0), Point(x1, y1), Point(x2, y2), Point(x3, y3)) =>
        path.moveTo(x0.toFloat, y0.toFloat)
        path.lineTo(x1.toFloat, y1.toFloat)
        path.lineTo(x2.toFloat, y2.toFloat)
        path.lineTo(x3.toFloat, y3.toFloat)
        path.closePath
      case _ =>
    }
  }

  init

  override def toString = "Staging.QuadsShape(" + points + ")"
}
object QuadsShape {
  def apply(pts: Seq[Point]) = Utils.runInSwingThreadAndWait {
    val shape = new QuadsShape(pts)
    Impl.figure0.pnode(shape.node)
    shape
  }
}

class QuadStripShape(val points: Seq[Point]) extends PolyShape with StrokedShape {
  val path = new PPath

  def init = {
    points sliding(4, 2) foreach {
      case Nil =>
      case Seq(Point(x0, y0), Point(x1, y1), Point(x2, y2), Point(x3, y3)) =>
        path.moveTo(x0.toFloat, y0.toFloat)
        path.lineTo(x1.toFloat, y1.toFloat)
        path.lineTo(x2.toFloat, y2.toFloat)
        path.lineTo(x3.toFloat, y3.toFloat)
        path.closePath
      case _ =>
    }
  }

  init

  override def toString = "Staging.QuadStripShape(" + points + ")"
}
object QuadStripShape {
  def apply(pts: Seq[Point]) = Utils.runInSwingThreadAndWait {
    val shape = new QuadStripShape(pts)
    Impl.figure0.pnode(shape.node)
    shape
  }
}

class HexShape(val points: Seq[Point]) extends PolyShape with StrokedShape {
  val path = new PPath

  def init = {
    points grouped(6) foreach {
      case Nil =>
      case Seq(Point(x0, y0), Point(x1, y1), Point(x2, y2), Point(x3, y3), Point(x4, y4), Point(x5, y5)) =>
        path.moveTo(x0.toFloat, y0.toFloat)
        path.lineTo(x1.toFloat, y1.toFloat)
        path.lineTo(x2.toFloat, y2.toFloat)
        path.lineTo(x3.toFloat, y3.toFloat)
        path.lineTo(x4.toFloat, y4.toFloat)
        path.lineTo(x5.toFloat, y5.toFloat)
        path.closePath
      case _ =>
    }
  }

  init

  override def toString = "Staging.HexShape(" + points + ")"
}
object HexShape {
  def apply(pts: Seq[Point]) = Utils.runInSwingThreadAndWait {
    val shape = new HexShape(pts)
    Impl.figure0.pnode(shape.node)
    shape
  }
}

class TriangleFanShape(override val origin: Point, val points: Seq[Point]) extends PolyShape
                                                                              with StrokedShape {
  val path = new PPath

  def init = {
    points grouped(2) foreach {
      case Nil =>
      case Seq(Point(x1, y1), Point(x2, y2)) =>
        path.moveTo(origin.x.toFloat, origin.y.toFloat)
        path.lineTo(x1.toFloat, y1.toFloat)
        path.lineTo(x2.toFloat, y2.toFloat)
      case _ =>
    }
  }

  init

  override def toString = "Staging.QuadStripShape(" + origin + "," + points + ")"
}
object TriangleFanShape {
  def apply(p0: Point, pts: Seq[Point]) = Utils.runInSwingThreadAndWait {
    val shape = new TriangleFanShape(p0, pts)
    Impl.figure0.pnode(shape.node)
    shape
  }
}
