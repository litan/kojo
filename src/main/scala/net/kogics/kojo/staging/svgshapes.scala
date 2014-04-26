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

import edu.umd.cs.piccolo._
import edu.umd.cs.piccolo.nodes._
import edu.umd.cs.piccolo.util._
import edu.umd.cs.piccolo.event._

//import net.kogics.kojo.util.Utils

import javax.swing._

import core._

import language.postfixOps

object SvgShape {
  import Impl.API
  
  def getAttr (ns: scala.xml.Node, s: String): Option[String] = {
    ns \ ("@" + s) text match {
      case "" => None
      case z  => Some(z)
    }
  }

  private def matchXY (ns: scala.xml.Node, xn: String = "x", yn: String = "y") = {
    val x = (getAttr(ns, xn) getOrElse "0").toDouble
    val y = (getAttr(ns, yn) getOrElse "0").toDouble
    Point(x, y)
  }

  private def matchWH (ns: scala.xml.Node) = {
    val w = (getAttr(ns, "width") getOrElse "0").toDouble
    val h = (getAttr(ns, "height") getOrElse "0").toDouble
    require(w >= 0, "Bad width for XML element " + ns)
    require(h >= 0, "Bad height for XML element " + ns)
    (w, h)
  }

  private def matchRXY (ns: scala.xml.Node) = {
    val x = (getAttr(ns, "rx") getOrElse "0").toDouble
    val y = (getAttr(ns, "ry") getOrElse "0").toDouble
    require(x >= 0, "Bad rx for XML element " + ns)
    require(y >= 0, "Bad ry for XML element " + ns)
    val rx = if (x != 0) x else y
    val ry = if (y != 0) y else x
    Point(rx, ry)
  }

  private def matchFill(ns: scala.xml.Node) = getAttr(ns, "fill")

  private def matchStroke(ns: scala.xml.Node) = getAttr(ns, "stroke")

  private def matchStrokeWidth(ns: scala.xml.Node) = getAttr(ns, "stroke-width")

  private def matchPoints (ns: scala.xml.Node): Seq[Point] = {
    val pointsStr = ns \ "@points" text
    val splitter = "(:?,\\s*|\\s+)".r
    val pointsItr = (splitter split pointsStr) map (_.toDouble) grouped(2)
    (pointsItr map { a => Point(a(0), a(1)) }).toList
  }

  def setStyle(ns: scala.xml.Node) {
    val fc_? = matchFill(ns)
    val sc_? = matchStroke(ns)
    val sw_? = matchStrokeWidth(ns)
    Style.save
    fc_? foreach { fc => API.fill(ColorMaker.color(fc)) }
    sc_? foreach { sc => API.stroke(ColorMaker.color(sc)) }
    sw_? foreach { sw => API.strokeWidth(sw.toDouble) }
  }

  private def matchRect(ns: scala.xml.Node) = {
    val p0 = matchXY(ns)
    val (width, height) = matchWH(ns)
    val p1 = p0 + Point(width, height)
    val p2 = matchRXY(ns)
    setStyle(ns)
    val res =
      if (p2.x != 0.0 || p2.y != 0.0) {
        RoundRectangle(p0, p1, p2)
      } else {
        Rectangle(p0, p1)
      }
    Style.restore
    res
  }

  private def matchCircle(ns: scala.xml.Node) = {
    val p0 = matchXY(ns, "cx", "cy")
    val r = (getAttr(ns, "r") getOrElse "0").toDouble
    val p1 = p0 + Point(r, r)
    setStyle(ns)
    val res = Ellipse(p0, p1)
    Style.restore
    res
  }

  private def matchEllipse(ns: scala.xml.Node) = {
    val p0 = matchXY(ns, "cx", "cy")
    val p1 = p0 + matchRXY(ns)
    setStyle(ns)
    val res = Ellipse(p0, p1)
    Style.restore
    res
  }

  private def matchLine(ns: scala.xml.Node) = {
    val p1 = matchXY(ns, "x1", "y1")
    val p2 = matchXY(ns, "x2", "y2")
    setStyle(ns)
    val res = Line(p1, p2)
    Style.restore
    res
  }

  private def matchText(ns: scala.xml.Node) = {
    //TODO hmm not working
    val p1 = matchXY(ns)
    // should also support dx/dy, rotate, textLength, lengthAdjust
    // and font attributes (as far as piccolo/awt can support them)
    setStyle(ns)
    val res = Text(ns.text, p1)
    Style.restore
    res
  }

  private def matchPath(ns: scala.xml.Node): Shape = {
    val d = getAttr(ns, "d")
    if (d.nonEmpty) SvgPath(d.get)
    else new Shape { val node = null }
  }

  def apply(node: scala.xml.Node): Shape = {
    // should handle some of
    //   color, fill-rule, stroke, stroke-dasharray, stroke-dashoffset,
    //   stroke-linecap, stroke-linejoin, stroke-miterlimit, stroke-width,
    //   color-interpolation, color-rendering
    // and
    //   transform-list
    //
    node match {
      case <rect></rect> =>
        matchRect(node)
      case <circle></circle> =>
        matchCircle(node)
      case <ellipse></ellipse> =>
        matchEllipse(node)
      case <line></line> =>
        matchLine(node)
      case <text></text> =>
        matchText(node)
      case <polyline></polyline> =>
        setStyle(node)
        val res = Polyline(matchPoints(node))
        Style.restore
        res
      case <polygon></polygon> =>
        setStyle(node)
        val res = Polygon(matchPoints(node))
        Style.restore
        res
      case <path></path> =>
        matchPath(node)
      case <g>{ shapes @ _* }</g> =>
        new Shape { val node = null }
        //for (s <- shapes) yield SvgShape(s)
      case <svg>{ shapes @ _* }</svg> =>
        new Shape { val node = null }
        //for (s <- shapes) yield SvgShape(s)
      case _ => // unknown element, ignore
        new Shape { val node = null }
    }
  }
}
