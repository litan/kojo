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

class SvgPath(val path: PPath) extends StrokedShape {
  val origin = Point(path.getX, path.getY)

  override def toString = "Staging.SvgPath(" + path + ")"
}
object SvgPath {
  var pPath: PPath = _

//  case class Point(x: Float, y: Float)
  
  type CurvePointGroup = (Point, Point, Point)
  type QuadPointGroup = (Point, Point)

  var currentPoint: Point = Point(0, 0)
  var currentControlPoint = currentPoint
  def reflectedControlPoint = Point(
    2 * currentPoint.x - currentControlPoint.x,
    2 * currentPoint.y - currentControlPoint.y
  )

  trait Coords {
    var t1: Point = _
    var t2: Point = _
    var t: Point = _

    def adjust(p: Point): Point = p match { case Point(x, y) =>
        adjust(x.toFloat, y.toFloat)
    }
    def adjust(x: Float, y: Float): Point

    def complementX(y: Float): Point
    def complementY(x: Float): Point
  }

  trait AbsoluteCoords extends Coords {
    def adjust(x: Float, y: Float) = Point(x, y)

    def complementX(y: Float) = Point(currentPoint.x, y)
    def complementY(x: Float) = Point(x, currentPoint.y)
  }


  trait RelativeCoords extends Coords {
    def adjust(x: Float, y: Float) =
      Point(currentPoint.x + x, currentPoint.y + y)

    def complementX(y: Float) =
      Point(currentPoint.x, currentPoint.y + y)
    def complementY(x: Float) =
      Point(currentPoint.x + x, currentPoint.y)
  }

  abstract sealed class SVGCmd { def apply (): Unit }

  trait MoveTo extends Coords {
    val pts: List[Point]
    def foreach(fn: Point => Unit) {
      pts.tail map (adjust(_)) foreach fn
      currentPoint = adjust(pts.last)
    }
    def apply () = {
      if (pts.nonEmpty) adjust(pts.head) match { case Point(x, y) =>
          pPath.moveTo(x.toFloat, y.toFloat)
          currentPoint = adjust(pts.head)
      }
      this foreach { case Point(x, y) =>
          pPath.lineTo(x.toFloat, y.toFloat)
      }
    }
  }

  case class MoveToAbs(pts: List[Point]) extends SVGCmd
                                            with MoveTo
                                            with AbsoluteCoords

  case class MoveToRel(pts: List[Point]) extends SVGCmd
                                            with MoveTo
                                            with RelativeCoords

  trait LineTo extends Coords {
    val pts: List[Point]
    def foreach(fn: Point => Unit) {
      pts map adjust foreach fn
      currentPoint = pts.last
    }
    def apply () = this foreach { case(Point(x, y)) =>
        pPath.lineTo(x.toFloat, y.toFloat)
    }
  }

  case class LineToAbs(pts: List[Point]) extends SVGCmd
                                            with LineTo
                                            with AbsoluteCoords

  case class LineToRel(pts: List[Point]) extends SVGCmd
                                            with LineTo
                                            with RelativeCoords

  trait HorizontalLine extends Coords {
    val xs: List[Float]
    def foreach(fn: Point => Unit) {
      xs foreach { case x0: Float =>
          t = complementY(x0)
          fn(t)
      }
      currentPoint = t
    }
    def apply () = this foreach { case(Point(x, y)) =>
        pPath.lineTo(x.toFloat, y.toFloat)
    }
  }

  case class HLineToAbs(xs: List[Float]) extends SVGCmd
                                            with HorizontalLine
                                            with AbsoluteCoords

  case class HLineToRel(xs: List[Float]) extends SVGCmd
                                            with HorizontalLine
                                            with RelativeCoords

  trait VerticalLine extends Coords {
    val ys: List[Float]
    def foreach(fn: Point => Unit) {
      ys foreach { case y0: Float =>
          t = complementX(y0)
          fn(t)
      }
      currentPoint = t
    }
    def apply () = this foreach { case(Point(x, y)) =>
        pPath.lineTo(x.toFloat, y.toFloat)
    }
  }

  case class VLineToAbs(ys: List[Float]) extends SVGCmd
                                            with VerticalLine
                                            with AbsoluteCoords

  case class VLineToRel(ys: List[Float]) extends SVGCmd
                                            with VerticalLine
                                            with RelativeCoords

  trait Curve extends Coords {
    val cs: List[CurvePointGroup]
    def foreach(fn: CurvePointGroup => Unit) {
      cs foreach {
        case (p1, p2, p) =>
          t1 = adjust(p1)
          t2 = adjust(p2)
          t = adjust(p)
          fn(t1, t2, t)
      }
      currentControlPoint = t2
      currentPoint = t
    }
    def apply () = this foreach { case(Point(x1, y1), Point(x2, y2), Point(x3, y3)) =>
        pPath.curveTo(x1.toFloat, y1.toFloat, x2.toFloat, y2.toFloat, x3.toFloat, y3.toFloat)
    }
  }

  case class CurveToAbs(cs: List[CurvePointGroup]) extends SVGCmd
                                                      with Curve
                                                      with AbsoluteCoords

  case class CurveToRel(cs: List[CurvePointGroup]) extends SVGCmd
                                                      with Curve
                                                      with RelativeCoords

  trait SmoothCurve extends Coords {
    val cs: List[(Point, Point)]
    def foreach(fn: CurvePointGroup => Unit) {
      cs foreach {
        case (p2, p) =>
          t1 = reflectedControlPoint
          t2 = adjust(p2)
          t = adjust(p)
          fn(t1, t2, t)
      }
      currentControlPoint = t2
      currentPoint = t
    }
    def apply () = this foreach { case(Point(x1, y1), Point(x2, y2), Point(x3, y3)) =>
        pPath.curveTo(x1.toFloat, y1.toFloat, x2.toFloat, y2.toFloat, x3.toFloat, y3.toFloat)
    }
  }

  case class SmoothCurveToAbs(cs: List[(Point, Point)]) extends SVGCmd
                                                           with SmoothCurve
                                                           with AbsoluteCoords

  case class SmoothCurveToRel(cs: List[(Point, Point)]) extends SVGCmd
                                                           with SmoothCurve
                                                           with RelativeCoords

  trait QuadBezier extends Coords {
    val cs: List[QuadPointGroup]
    def foreach(fn: QuadPointGroup => Unit) {
      cs foreach {
        case (p1: Point, p: Point) =>
          t1 = adjust(p1)
          t = adjust(p)
          fn(t1, t)
      }
      currentControlPoint = t1
      currentPoint = t
    }
    def apply () = this foreach { case(Point(x1, y1), Point(x2, y2)) =>
        pPath.quadTo(x1.toFloat, y1.toFloat, x2.toFloat, y2.toFloat)
    }
  }
  case class QuadBezierAbs(cs: List[QuadPointGroup]) extends SVGCmd
                                                        with QuadBezier
                                                        with AbsoluteCoords
  case class QuadBezierRel(cs: List[QuadPointGroup]) extends SVGCmd
                                                        with QuadBezier
                                                        with RelativeCoords

  trait SmoothQuadBezier extends Coords {
    val cs: List[Point]
    def foreach(fn: QuadPointGroup => Unit) {
      cs foreach {
        case p =>
          t1 = reflectedControlPoint
          t = adjust(p)
          fn(t1, t)
      }
      currentControlPoint = t1
      currentPoint = t
    }
    def apply () = this foreach { case(Point(x1, y1), Point(x2, y2)) =>
        pPath.quadTo(x1.toFloat, y1.toFloat, x2.toFloat, y2.toFloat)
    }
  }

  case class SmoothQuadBezierAbs(cs: List[Point]) extends SVGCmd
                                                     with SmoothQuadBezier
                                                     with AbsoluteCoords

  case class SmoothQuadBezierRel(cs: List[Point]) extends SVGCmd
                                                     with SmoothQuadBezier
                                                     with RelativeCoords

  case class EllipticalArcAbs(as: List[_]) extends SVGCmd
                                              with AbsoluteCoords {
    def apply () = {}
  }

  case class EllipticalArcRel(as: List[_]) extends SVGCmd
                                              with RelativeCoords {
    def apply () = {}
  }

  case class Close() extends SVGCmd {
    def apply () = pPath.closePath
  }

  import scala.util.parsing.combinator._

  class SVGPathParser extends JavaTokenParsers {
    def drawto: Parser[Any]        = lineto | closepath | hlineto | vlineto | curveto | sCurveto | qBezierto | sqBezierto | elliptArc
    def svgpath: Parser[Any]       = rep(pathcommand)
    def pathcommand: Parser[Any]   = moveto~rep(drawto)                   ^^ {
      case a~b => (a, b)
    }
    def closepath: Parser[SVGCmd]  = ("Z" | "z")                          ^^ {
      case _ => Close()
    }
    def moveto: Parser[SVGCmd]     = ("M" | "m")~repsep(coordPair, oc)    ^^ {
      case "M"~b => MoveToAbs(b)
      case "m"~b => MoveToRel(b)
    }
    def lineto: Parser[SVGCmd]     = ("L" | "l")~repsep(coordPair, oc)    ^^ {
      case "L"~b => LineToAbs(b)
      case "l"~b => LineToRel(b)
    }
    def hlineto: Parser[SVGCmd]    = ("H" | "h")~repsep(number, oc)       ^^ {
      case "H"~b => HLineToAbs(b)
      case "h"~b => HLineToRel(b)
    }
    def vlineto: Parser[SVGCmd]    = ("V" | "v")~repsep(number, oc)       ^^ {
      case "V"~b => VLineToAbs(b)
      case "v"~b => VLineToRel(b)
    }
    def curveto: Parser[SVGCmd]    = ("C" | "c")~repsep(coordPairTri, oc) ^^ {
      case "C"~b => CurveToAbs(b)
      case "c"~b => CurveToRel(b)
    }
    def sCurveto: Parser[SVGCmd]   = ("S" | "s")~repsep(coordPairDbl, oc) ^^ {
      case "S"~b => SmoothCurveToAbs(b)
      case "s"~b => SmoothCurveToRel(b)
    }
    def qBezierto: Parser[SVGCmd]  = ("Q" | "q")~repsep(coordPairDbl, oc) ^^ {
      case "Q"~b => QuadBezierAbs(b)
      case "q"~b => QuadBezierRel(b)
    }
    def sqBezierto: Parser[SVGCmd] = ("T" | "t")~repsep(coordPair, oc)    ^^ {
      case "T"~b => SmoothQuadBezierAbs(b)
      case "t"~b => SmoothQuadBezierRel(b)
    }
    def elliptArc: Parser[Any]     = ("A" | "a")~repsep(arcArg, oc)       ^^ {
      case "A"~b => EllipticalArcAbs(b)
      case "a"~b => EllipticalArcRel(b)
    }
    def coordPair: Parser[Point]   = number~oc~number                     ^^ {
      case a~c~b => Point(a.asInstanceOf[Float], b.asInstanceOf[Float])
    }
    def coordPairDbl: Parser[QuadPointGroup] =
      coordPair~oc~coordPair                                              ^^ {
        case a~c~b => (a, b)
      }
    def coordPairTri: Parser[CurvePointGroup] =
      coordPair~oc~coordPair~oc~coordPair                                 ^^ {
        case a~d~b~e~c => (a, b, c)
      }
    def arcArg: Parser[Any]        =
      number~oc~number~oc~number~oc~flag~oc~flag~oc~coordPair             ^^ {
        case n1~a~n2~b~n3~c~f1~d~f2~e~cp => (n1, n2, n3, f1, f2, cp)
      }
    def flag: Parser[Int]          = ("0" | "1")                  ^^ (_.toInt)
    def oc: Parser[Any]            = opt(",")
    def number: Parser[Float]      = floatingPointNumber        ^^ (_.toFloat)
  }

  object ParseExpr extends SVGPathParser {
    def apply (d: String) = parseAll(svgpath, d)
  }

  def apply(d: String) = {
    pPath = new PPath
    
    try {
      ParseExpr(d).get match {
        case List((move: SVGCmd, drawcmds: List[SVGCmd])) =>
          move()
          drawcmds foreach (_())
        case Nil =>
      }
    }
    catch {
      // TODO if exceptions crop up, handle them here.  If not, eventually
      // take out exception handling entirely.
      case e: Throwable => throw e
    }

    Utils.runInSwingThreadAndWait {
      val shape = new SvgPath(pPath)
      Impl.figure0.pnode(shape.node)
      shape
    }
  }
}
