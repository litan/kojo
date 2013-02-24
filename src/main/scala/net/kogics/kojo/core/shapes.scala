/*
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

package net.kogics.kojo.core

import scala.collection._

trait VisualElement {
  def hide()
  def show()
  def setColor(color: java.awt.Color)
}

trait Labelled extends VisualElement {
  def showNameInLabel()
  def showNameValueInLabel()
  def showValueInLabel()
  def hideLabel()
  def showLabel()
  def setLabel(label: String)
}

object Point {
  def apply(x: Double, y: Double) = new Point(x, y)
  def unapply(p: Point) = Some((p.x, p.y))
}
class Point(val x: Double, val y: Double) {
  def +(that: Point) = new Point(this.x + that.x, this.y + that.y)
  def -(that: Point) = new Point(this.x - that.x, this.y - that.y)
  def unary_- = new Point(-x, -y)
  def onX() = new Point(x, 0)
  def onY() = new Point(0, y)
  override def toString = "Point(%.2f , %.2f)" format(x, y)
  // Martin says (Programming in Scala, 1st ed, ch 28):
  override def hashCode: Int = 41 * (41 + x.hashCode) + y.hashCode
  override def equals(other: Any) = other match {
    case that: Point =>
      (that canEqual this) && (this.x == that.x) && (this.y == that.y)
    case _ =>
      false
  }
  def canEqual(other: Any) = other.isInstanceOf[Point]
}
class Line(val p1: Point, val p2: Point)
//class LineSegment(p1: Point, p2: Point) extends Line(p1, p2)
class Ellipse(val center: Point, val w: Double, val h: Double)
class Circle(center: Point, val radius: Double) extends Ellipse(center, 2*radius, 2*radius)
class Arc(val onEll: Ellipse, val start: Double, val extent: Double)
class Angle(val size: Double)
class Text(val content: String)

class Rectangle(val bLeft: Point, val tRight: Point) {
  val width = tRight.x - bLeft.x
  val height = tRight.y - bLeft.y
  override def toString = "Rectangle(%.2f , %.2f , %.2f , %.2f)" format(bLeft.x, bLeft.y, tRight.x, tRight.y)
}
class RoundRectangle(
  override val bLeft: Point,
  override val tRight: Point,
  rx: Double, ry: Double
) extends Rectangle(bLeft, tRight)
// class Square(bLeft: Point, tRight: Point) extends Rectangle(bLeft, tRight

trait MoveablePoint {
  def cx: Double
  def cy: Double
}

// a group of VisualElements
trait VisualElements extends VisualElement {
  def add(ves: VisualElement*)
}
