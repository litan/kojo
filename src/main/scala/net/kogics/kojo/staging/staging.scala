/*
 * Copyright (C) 2010 Peter Lewerin
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

import util.Utils
import util.Math
import core.InputAware
import core.Point
import java.awt.BasicStroke
import edu.umd.cs.piccolo.PNode
import edu.umd.cs.piccolo.nodes.PPath
import edu.umd.cs.piccolo.util.PBounds
import edu.umd.cs.piccolo.event._
import java.awt.Color
import java.awt.Paint
import net.kogics.kojo.core.UnitLen
import net.kogics.kojo.core.InputAware
import lite.canvas.SpriteCanvas
import java.util.concurrent.Future
import edu.umd.cs.piccolo.activities.PActivity
import net.kogics.kojo.figure.Figure
import net.kogics.kojo.core.Turtle

import language.implicitConversions
import language.postfixOps

object Impl {
  @volatile var canvas: SpriteCanvas = _
  @volatile var turtle0: Turtle = _
  @volatile var figure0: Figure = _
  @volatile var API: API = _ 
}

/**
 * Staging API
 *
 * This object contains the API for using Staging within Kojo scripts.
 *
 * DISCLAIMER
 *
 * Parts of this interface is written to approximately conform to the
 * Processing API as described in the reference at
 * <URL: http://processing.org/reference/>.
 * The implementation code is the work of Peter Lewerin
 * (<peter.lewerin@tele2.se>) and is not in any way derived from the
 * Processing source.
 */
class API(canvas: SpriteCanvas) {
  Impl.API = this
  Impl.canvas = canvas
  Impl.turtle0 = canvas.turtle0
  Impl.figure0 = canvas.figure0
  //W#summary Developer home-page for the Staging Module
  //W
  //W=Introduction=
  //W
  //WThe Staging Module is currently being developed by Peter Lewerin.
  //WThe original impetus came from a desire to run Processing-style code in Kojo.
  //W
  //WAt this point, the shape hierarchy is the most complete part, but
  //Wutilities for color definition, time keeping etc are being added.
  //W
  //W=Examples=
  //W
  //W  * StagingHelloKojoExample
  //W  * StagingArrayExample
  //W  * StagingArrayTwoDeeExample
  //W  * StagingClockExample
  //W  * StagingColorWheelExample
  //W  * StagingCreatingColorsExample
  //W  * StagingDifferenceOfTwoSquaresExample
  //W  * StagingEasingExample
  //W  * StagingHueSaturationBrightnessExample
  //W  * StagingSineOfAnAngleExample
  //W
  //W=Overview=
  //W
  //W==Points==
  //W
  //WStaging uses {{{net.kogics.kojo.core.Point}}} for coordinates.
  //W

  //T PointTest begins
  def point(x: Double, y: Double) = Point(x, y)

  implicit def tupleDDToPoint(tuple: (Double, Double)) = Point(tuple._1, tuple._2)
  implicit def tupleDIToPoint(tuple: (Double, Int)) = Point(tuple._1, tuple._2)
  implicit def tupleIDToPoint(tuple: (Int, Double)) = Point(tuple._1, tuple._2)
  implicit def tupleIIToPoint(tuple: (Int, Int)) = Point(tuple._1, tuple._2)
  //implicit def baseShapeToPoint(b: BaseShape) = b.origin
  //implicit def awtPointToPoint(p: java.awt.geom.Point2D) = Point(p.getX, p.getY)
  //implicit def awtDimToPoint(d: java.awt.geom.Dimension2D) = Point(d.getWidth, d.getHeight)

  /** The point of origin, located at a corner of the user screen if
   * `screenSize` has been called, or the middle of the screen otherwise. */
  val O = Point(0, 0)
  //T PointTest ends

  //W
  //W==User Screen==
  //W
  //WThe zoom level and axis orientations can be set using `screenSize`.
  //W
  //T ScreenMethodsTest begins
  def screenWidth = Screen.rect.getWidth.toInt
  def screenHeight = Screen.rect.getHeight.toInt
  def screenSize(width: Int, height: Int) = Screen.size(width, height)

  /** The middle point of the user screen, or (0, 0) if `screenSize` hasn't
   * been called. */
  def screenMid = Screen.rect.getCenter2D

  /** The extreme point of the user screen (i.e. the opposite corner from
   * the point of origin), or (0, 0) if `screenSize` hasn't been called. */
  def screenExt = Screen.rect.getExt

  /** Fills the user screen with the specified color. */
  def background(bc: Paint) = {
    withStyle(bc, null, 1) { rectangle(O, screenExt) }
  }
  //T ScreenMethodsTest ends

  //W
  //W==Simple shapes and text==
  //W
  //WGiven `Point`s or _x_ and _y_ coordinate values, simple shapes like dots,
  //Wlines, rectangles, ellipses, and elliptic arcs can be drawn.  Texts can
  //Walso be placed in this way.
  //W
  //T SimpleShapesTest begins
  def dot(x: Double, y: Double): Dot = Dot(Point(x, y))
  def dot(p: Point): Dot = Dot(p)

  def line(x1: Double, y1: Double, x2: Double, y2: Double) =
    Line(Point(x1, y1), Point(x2, y2))
  def line(p1: Point, p2: Point) =
    Line(p1, p2)

  def vector(x1: Double, y1: Double, x2: Double, y2: Double, a: Double) =
    Vector(Point(x1, y1), Point(x2, y2), a)
  def vector(p1: Point, p2: Point, a: Double) =
    Vector(p1, p2, a)

  def rectangle(x: Double, y: Double, w: Double, h: Double) =
    Rectangle(Point(x, y), Point(x + w, y + h))
  def rectangle(p: Point, w: Double, h: Double) =
    Rectangle(p, Point(p.x + w, p.y + h))
  def rectangle(p1: Point, p2: Point) =
    Rectangle(p1, p2)
  def square(x: Double, y: Double, s: Double) =
    Rectangle(Point(x, y), Point(x + s, y + s))
  def square(p: Point, s: Double) =
    Rectangle(p, Point(p.x + s, p.y + s))

  def roundRectangle(
    x: Double, y: Double,
    w: Double, h: Double,
    rx: Double, ry: Double
  ) =
    RoundRectangle(Point(x, y), Point(x + w, y + h), Point(rx, ry))
  def roundRectangle(
    p: Point,
    w: Double, h: Double,
    rx: Double, ry: Double
  ) =
    RoundRectangle(p, Point(p.x + w, p.y + h), Point(rx, ry))
  def roundRectangle(p1: Point, p2: Point, rx: Double, ry: Double) =
    RoundRectangle(p1, p2, Point(rx, ry))
  def roundRectangle(p1: Point, p2: Point, p3: Point) =
    RoundRectangle(p1, p2, p3)

  def ellipse(cx: Double, cy: Double, rx: Double, ry: Double) =
    Ellipse(Point(cx, cy), Point(cx + rx, cy + ry))
  def ellipse(p: Point, rx: Double, ry: Double) =
    Ellipse(p, Point(p.x + rx, p.y + ry))
  def ellipse(p1: Point, p2: Point) =
    Ellipse(p1, p2)
  def circle(x: Double, y: Double, r: Double) =
    Ellipse(Point(x, y), Point(x + r, y + r))
  def circle(p: Point, r: Double) =
    Ellipse(p, Point(p.x + r, p.y + r))

  def arc(cx: Double, cy: Double, rx: Double, ry: Double, s: Double, e: Double) =
    Arc(Point(cx, cy), Point(cx + rx, cy + ry), s, e, java.awt.geom.Arc2D.PIE)
  def arc(p: Point, rx: Double, ry: Double, s: Double, e: Double) =
    Arc(p, Point(p.x + rx, p.y + ry), s, e, java.awt.geom.Arc2D.PIE)
  def arc(p1: Point, p2: Point, s: Double, e: Double) =
    Arc(p1, p2, s, e, java.awt.geom.Arc2D.PIE)
  def pieslice(cx: Double, cy: Double, rx: Double, ry: Double, s: Double, e: Double) =
    Arc(Point(cx, cy), Point(cx + rx, cy + ry), s, e, java.awt.geom.Arc2D.PIE)
  def pieslice(p: Point, rx: Double, ry: Double, s: Double, e: Double) =
    Arc(p, Point(p.x + rx, p.y + ry), s, e, java.awt.geom.Arc2D.PIE)
  def pieslice(p1: Point, p2: Point, s: Double, e: Double) =
    Arc(p1, p2, s, e, java.awt.geom.Arc2D.PIE)
  def openArc(cx: Double, cy: Double, rx: Double, ry: Double, s: Double, e: Double) =
    Arc(Point(cx, cy), Point(cx + rx, cy + ry), s, e, java.awt.geom.Arc2D.OPEN)
  def openArc(p: Point, rx: Double, ry: Double, s: Double, e: Double) =
    Arc(p, Point(p.x + rx, p.y + ry), s, e, java.awt.geom.Arc2D.OPEN)
  def openArc(p1: Point, p2: Point, s: Double, e: Double) =
    Arc(p1, p2, s, e, java.awt.geom.Arc2D.OPEN)
  def chord(cx: Double, cy: Double, rx: Double, ry: Double, s: Double, e: Double) =
    Arc(Point(cx, cy), Point(cx + rx, cy + ry), s, e, java.awt.geom.Arc2D.CHORD)
  def chord(p: Point, rx: Double, ry: Double, s: Double, e: Double) =
    Arc(p, Point(p.x + rx, p.y + ry), s, e, java.awt.geom.Arc2D.CHORD)
  def chord(p1: Point, p2: Point, s: Double, e: Double) =
    Arc(p1, p2, s, e, java.awt.geom.Arc2D.CHORD)

  def text(s: String, x: Double, y: Double) = Text(s, Point(x, y))
  def text(s: String, p: Point) = Text(s, p)

  def star(cx: Double, cy: Double, inner: Double, outer: Double, points: Int) =
    Star(Point(cx, cy), inner, outer, points)
  def star(p: Point, inner: Double, outer: Double, points: Int) =
    Star(p, inner, outer, points)
  def star(p1: Point, p2: Point, p3: Point, points: Int) =
    Star(p1, dist(p1, p2), dist(p1, p3), points)

  def cross(p1: Point, p2: Point, cw: Double, r: Double = 1, greek: Boolean = false) =
    Cross(p1, p2, cw, r, greek)
  def crossOutline(p1: Point, p2: Point, cw: Double, r: Double = 1, greek: Boolean = false) =
    CrossOutline(p1, p2, cw, r, greek)
  def saltire(p1: Point, p2: Point, s: Double) = Saltire(p1, p2, s)
  def saltireOutline(p1: Point, p2: Point, s: Double) = SaltireOutline(p1, p2, s)
  //T SimpleShapesTest ends
  
  //W
  //W==Complex Shapes==
  //W
  //WGiven a sequence of `Point`s, a number of complex shapes can be drawn,
  //Wincluding basic polylines and polygons, and patterns of polylines/polygons.
  //W
  //T ComplexShapesTest begins
  def polyline(pts: Seq[Point]) = Polyline(pts)

  def polygon(pts: Seq[Point]): Polygon = Polygon(pts)
  def triangle(p0: Point, p1: Point, p2: Point) = polygon(Seq(p0, p1, p2))
  def quad(p0: Point, p1: Point, p2: Point, p3: Point) =
    polygon(Seq(p0, p1, p2, p3))

  def linesShape(pts: Seq[Point]) = LinesShape(pts)
  def trianglesShape(pts: Seq[Point]) = TrianglesShape(pts)
  def triangleStripShape(pts: Seq[Point]) = TriangleStripShape(pts)
  def quadsShape(pts: Seq[Point]) = QuadsShape(pts)
  def quadStripShape(pts: Seq[Point]) = QuadStripShape(pts)
  def triangleFanShape(p0: Point, pts: Seq[Point]) = TriangleFanShape(p0, pts)
  //T ComplexShapesTest ends

  //W
  //W==SVG Shapes==
  //W
  //WGiven an SVG element, the corresponding shape can be drawn.
  //W
  //T SvgShapesTest begins
  def svgShape(node: scala.xml.Node) = SvgShape(node)
  //T SvgShapesTest ends
  
  def sprite(x: Double, y: Double, fname: String) = Sprite(point(x, y), fname)
  def path(x: Double, y: Double) = Path(point(x, y))
  def group(shapes: List[Shape]) = Composite(shapes)
  def group(shapes: Shape *) = Composite(shapes)

  //W
  //W==Color==
  //W
  //WColor values can be created with the method `color`, or using a
  //W_color-maker_.  The methods `fill`, `noFill`,
  //W`stroke`, and `noStroke` set the colors used to draw the insides and edges
  //Wof figures.  The method `strokeWidth` doesn't actually affect color but is
  //Wtypically used together with the color setting methods.  The method
  //W`withStyle` allows the user to set fill color, stroke color, and stroke
  //Wwidth temporarily.
  //W
  //W
  //T ColorTest begins
  def grayColors(grayMax: Int) =
    ColorMaker(GRAY(grayMax))
  def grayColorsWithAlpha(grayMax: Int, alphaMax: Int) =
    ColorMaker(GRAYA(grayMax, alphaMax))
  def rgbColors(rMax: Int, gMax: Int, bMax: Int) =
    ColorMaker(RGB(rMax, gMax, bMax))
  def rgbColorsWithAlpha(rMax: Int, gMax: Int, bMax: Int, alphaMax: Int) =
    ColorMaker(RGBA(rMax, gMax, bMax, alphaMax))
  def hsbColors(hMax: Int, sMax: Int, bMax: Int) =
    ColorMaker(HSB(hMax, sMax, bMax))
  def namedColor(s: String) = ColorMaker.color(s)
  def fill(c: Paint) = Impl.figure0.setFillColor(c)
  def noFill() = Impl.figure0.setFillColor(null)
  def stroke(c: Paint) = Impl.figure0.setPenColor(c)
  def noStroke() = Impl.figure0.setPenColor(null)
  def strokeWidth(w: Double) = Impl.figure0.setPenThickness(w)

  def setPenColor(color: Paint) = stroke(color)
  def setFillColor(color: Paint) = fill(color)
  def setPenThickness(w: Double) = strokeWidth(w)
  
  def withStyle(fc: Paint, sc: Paint, sw: Double)(body: => Unit) =
    Style(fc, sc, sw)(body)
  implicit def ColorToRichColor (c: java.awt.Color) = RichColor(c)
  def lerpColor(from: RichColor, to: RichColor, amt: Double) =
    RichColor.lerpColor(from, to, amt)
  //T ColorTest ends

  Inputs.init()

  //W
  //W==Timekeeping==
  //W
  //WA number of methods report the current time.
  //W
  //T TimekeepingTest begins
  def millis = System.currentTimeMillis()
  def time = System.currentTimeMillis()/1000.0

  import java.util.Calendar
  def second = Calendar.getInstance().get(Calendar.SECOND)
  def minute = Calendar.getInstance().get(Calendar.MINUTE)
  def hour   = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
  def day    = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
  def month  = Calendar.getInstance().get(Calendar.MONTH) + 1
  def year   = Calendar.getInstance().get(Calendar.YEAR)
  //T UtilsTest ends

  //W
  //W==Math==
  //W
  //WA number of methods perform number processing tasks.
  //W
  //T MathTest begins
  def constrain(value: Double, min: Double, max: Double) =
    Math.constrain(value, min, max)

  def norm(value: Double, low: Double, high: Double) =
    Math.map(value, low, high, 0, 1)

  def map(value: Double, low1: Double, high1: Double, low2: Double, high2: Double) =
    Math.map(value, low1, high1, low2, high2)

  def lerp(value1: Double, value2: Double, amt: Double) =
    Math.lerp(value1, value2, amt)

  def sq(x: Double) = x * x
  def sqrt(x: Double) = math.sqrt(x)

  def dist(x0: Double, y0: Double, x1: Double, y1: Double) =
    sqrt(sq(x1 - x0) + sq(y1 - y0))
  def dist(p1: Point, p2: Point) =
    sqrt(sq(p2.x - p1.x) + sq(p2.y - p1.y))

  def mag(x: Double, y: Double) = dist(0, 0, x, y)
  def mag(p: Point) = dist(0, 0, p.x, p.y)

  //W
  //W==Trigonometry==
  //W
  //WA number of methods perform trigonometry tasks.
  //W
  val PI = math.Pi
  val TWO_PI = 2*PI
  val HALF_PI = PI/2
  val QUARTER_PI = PI/4
  def sin(a: Double) = math.sin(a)
  def cos(a: Double) = math.cos(a)
  def tan(a: Double) = math.tan(a)
  def asin(a: Double) = math.asin(a)
  def acos(a: Double) = math.acos(a)
  def atan(a: Double) = math.atan(a)
  def radians(deg: Double) = deg.toRadians
  def degrees(rad: Double) = rad.toDegrees
  //T MathTest ends

  //W

  //W==Control==
  //W
  //WThere are methods for execution control and mouse feedback.
  //W
  //T ControlTest begins
  def loop(fn: => Unit) = Impl.figure0.refresh(fn)
  def animate(fn: => Unit) = loop(fn)
  def stop() = Impl.figure0.stopRefresh()
  def stopActivity(a: Future[PActivity]) = Impl.figure0.stopAnimation(a)
  def reset() = {
    Impl.canvas.clearStaging()
    Impl.canvas.turtle0.invisible()
  }
  def clear() = reset()
  def clearWithUL(ul: UnitLen) {
    Impl.canvas.clearStagingWul(ul)
    Impl.canvas.turtle0.invisible()
  }
  def switchTo() = Impl.canvas.makeStagingVisible()
  def wipe() = Impl.figure0.fgClear()
  def onAnimationStop(fn: => Unit) = Impl.figure0.onStop(fn)

  def mouseX() = Inputs.stepMousePos.x
  def mouseY() = Inputs.stepMousePos.y
  def pmouseX() = Inputs.prevMousePos.x
  def pmouseY() = Inputs.prevMousePos.y
  val LEFT = 1
  val CENTER = 2
  val RIGHT = 3
  def mouseButton = Inputs.mouseBtn
  def mousePressed = Inputs.mousePressedFlag

  def interpolatePolygon(pts1: Seq[Point], pts2: Seq[Point], n: Int) {
    require(pts1.size == pts2.size, "The polygons don't match up.")

    var g0 = polygon(pts1)
    for (i <- 0 to n ; amt = i / n.toFloat) {
      val pts = (pts1 zip pts2) map { case(p1, p2) =>
          point(lerp(p1.x, p2.x, amt), lerp(p1.y, p2.y, amt))
      }
      g0.hide
      g0 = polygon(pts)
      for (i <- 0 to 10) {
        net.kogics.kojo.util.Throttler.throttle()
      }
    }
  }
  //T ControlTest ends

  // expose some types in the API
  type Shape = staging.Shape
  type StrokedShape = staging.StrokedShape

  //W
  //W=Usage=
  //W
} // end of API

abstract class ColorModes
case class RGB(r: Int, g: Int, b: Int) extends ColorModes
case class RGBA(r: Int, g: Int, b: Int, a: Int) extends ColorModes
case class HSB(h: Int, s: Int, b: Int) extends ColorModes
case class HSBA(h: Int, s: Int, b: Int, a: Int) extends ColorModes
case class GRAY(v: Int) extends ColorModes
case class GRAYA(v: Int, a: Int) extends ColorModes

//T ShapeMethodsTest begins
trait Shape  extends InputAware {
  def myCanvas = Impl.canvas
  def myNode = node
  def node: PNode
  var sizeFactor = 1.0

  def hide() {
    Utils.runInSwingThread {
      node.setVisible(false)
    }
    Impl.canvas.repaint()
  }
  def show() {
    Utils.runInSwingThread {
      node.setVisible(true)
    }
    Impl.canvas.repaint()
  }
  def erase() {
    Impl.figure0.removePnode(node)
  }
  def fill_=(color: Paint) {
    Utils.runInSwingThread {
      node.setPaint(color)
      node.repaint()
    }
  }
  def fill(color: Paint) {
    fill = color
  }
  def fill = Utils.runInSwingThreadAndWait {
    node.getPaint
  }
  def setFillColor(color: Paint) = fill(color)

  def rotate(amount: Double) = turn(amount)
  
  def rotateTo(angle: Double) = {
    turn(angle - _theta.toDegrees)
  }
  
  def scale(amount: Double) = {
    Utils.runInSwingThread {
      node.scale(amount)
      node.repaint()
    }
    sizeFactor *= amount
  }
  def scaleTo(size: Double) = {
    scale(size / sizeFactor)
  }
  
  def translate(p: Point) {
    translate(p.x, p.y)
  }
  
  def offset(p: Point) {
    offset(p.x, p.y)
  }

  def translate(x: Double, y: Double) = {
    Utils.runInSwingThread {
      node.translate(x, y)
      node.repaint()
    }
  }

  def offset(x: Double, y: Double) = {
    Utils.runInSwingThread {
      node.offset(x, y)
      node.repaint()
    }
  }

  def offset = Utils.runInSwingThreadAndWait {
    val o = node.getOffset
    Point(o.getX, o.getY)
  }

  import turtle.TurtleHelper._
  protected var _theta: Double = 0

  def turn(angle: Double) {
    Utils.runInSwingThread {
      _theta = thetaAfterTurn(angle, _theta)
      node.setRotation(_theta)
      node.repaint()
    }
  }
  
  def heading = Utils.runInSwingThreadAndWait {
    _theta.toDegrees
  }
  def orientation = heading
  def setHeading(angle: Double) = rotateTo(angle)

  def act(fn: Shape => Unit) = Impl.API.loop {
    fn(this)
  }
}
//T ShapeMethodsTest ends

trait Rounded {
  val curvature: Point
  def radiusX = curvature.x
  def radiusY = curvature.y
}


trait BaseShape extends Shape with core.RichTurtleCommands {
  val origin: Point
  import turtle.TurtleHelper._
  def setPosition(p: Point) {
    setPosition(p.x, p.y)
  }

  def setPosition(x: Double, y: Double) {
    Utils.runInSwingThread {
      node.setOffset(x - origin.x, y - origin.y)
      node.repaint()
    }
  }
  
  def position = Utils.runInSwingThreadAndWait {
    val o = node.getOffset
    Point(o.getX + origin.x, o.getY + origin.y)
  }  
  
  def forward(n: Double) {
    Utils.runInSwingThread {
      val pos = position
      val xy = posAfterForward(pos.x, pos.y, _theta, n)
      setPosition(xy._1, xy._2)
    }
  }
  
  def towards(x: Double, y: Double) {
    Utils.runInSwingThread {
      val pos = position
      _theta = thetaTowards(pos.x, pos.y, x, y, _theta)
      node.setRotation(_theta)
      node.repaint()
    }
  }
}

trait StrokedShape extends BaseShape {
  val path: PPath
  def node = path

  def stroke_=(color: Paint) {
    Utils.runInSwingThread {
      node.setStrokePaint(color)
      node.repaint()
    }
  }
  def stroke = Utils.runInSwingThreadAndWait {
    node.getStrokePaint
  }
  def stroke(color: Paint) {
    stroke = color
  }

  def strokeWidth(w: Double) {
    Utils.runInSwingThread {
      node.setStroke(new BasicStroke(w.toFloat, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND))
      node.repaint()
    }
  }
  def strokeWidth = Utils.runInSwingThreadAndWait {
    node.getStroke.asInstanceOf[BasicStroke].getLineWidth
  }

  def setPenColor(color: Paint) = stroke(color)
  def setPenThickness(w: Double) = strokeWidth(w)
}

trait SimpleShape extends StrokedShape {
  val endpoint: Point
  def width = endpoint.x - origin.x
  def height = endpoint.y - origin.y
}

trait Elliptical extends SimpleShape with Rounded {
  val curvature = endpoint - origin
  override def width = 2 * radiusX
  override def height = 2 * radiusY
}

class Text(val text: String, val origin: Point) extends BaseShape {
  import java.awt.Font
  val tnode = Utils.textNode(text, origin.x, origin.y, Impl.canvas.camScale, 14)
  def node = tnode

  def setPenColor(color: Paint) {
    Utils.runInSwingThread {
      tnode.setTextPaint(color)
      tnode.repaint()
    }
  }

  def setFontSize(s: Int) {
    Utils.runInSwingThread {
      val font = new Font(node.getFont.getName, Font.PLAIN, s)
      tnode.setFont(font)
      tnode.repaint()
    }
  }

  def setContent(content: String) {
    Utils.runInSwingThread {
      tnode.setText(content)
      tnode.repaint()
    }
  }

  override def toString = "Staging.Text(" + text + ", " + origin + ")"
}
object Text {
  def apply(s: String, p: Point) = Utils.runInSwingThreadAndWait {
    val shape = new Text(s, p)
    Impl.figure0.pnode(shape.node)
    shape
  }
}


trait PolyShape extends BaseShape {
  val points: Seq[Point]
  val origin = points(0)
  //def toPolygon: Polygon = Polygon(points)
  //def toPolyline: Polyline = Polyline(points)
}

trait CrossShape {
  val xdims = Array.fill(8){0.0}
  val ydims = Array.fill(8){0.0}
  def crossDims(len: Double, wid: Double, cw: Double, r: Double = 1, greek: Boolean = false) = {
    require(wid / 2 > cw)
    require(len / 2 > cw)
    val a = (wid - cw) / 2
    val b = a / (if (greek) 1 else r)
    val c = cw / 6
    val d = c / 2
    xdims(1) = a - c
    xdims(2) = a
    xdims(3) = a + d
    xdims(4) = a + cw - d
    xdims(5) = a + cw
    xdims(6) = a + cw + c
    xdims(7) = len
    ydims(1) = b - c
    ydims(2) = b
    ydims(3) = b + d
    ydims(4) = b + cw - d
    ydims(5) = b + cw
    ydims(6) = b + cw + c
    ydims(7) = wid
    this
  }
  def points() = List(
    Point(xdims(0), ydims(5)), Point(xdims(2), ydims(5)),
    Point(xdims(2), ydims(7)), Point(xdims(5), ydims(7)),
    Point(xdims(5), ydims(5)), Point(xdims(7), ydims(5)),
    Point(xdims(7), ydims(2)), Point(xdims(5), ydims(2)),
    Point(xdims(5), ydims(0)), Point(xdims(2), ydims(0)),
    Point(xdims(2), ydims(2)), Point(xdims(0), ydims(2))
  )
  def outlinePoints() = List(
    Point(xdims(0), ydims(6)), Point(xdims(1), ydims(6)), Point(xdims(1), ydims(7)),
    Point(xdims(3), ydims(7)), Point(xdims(3), ydims(4)), Point(xdims(0), ydims(4)),
    Point(xdims(6), ydims(7)), Point(xdims(6), ydims(6)), Point(xdims(7), ydims(6)),
    Point(xdims(7), ydims(4)), Point(xdims(4), ydims(4)), Point(xdims(4), ydims(7)),
    Point(xdims(7), ydims(1)), Point(xdims(6), ydims(1)), Point(xdims(6), ydims(0)),
    Point(xdims(4), ydims(0)), Point(xdims(4), ydims(3)), Point(xdims(7), ydims(3)),
    Point(xdims(1), ydims(0)), Point(xdims(1), ydims(1)), Point(xdims(0), ydims(1)),
    Point(xdims(0), ydims(3)), Point(xdims(3), ydims(3)), Point(xdims(3), ydims(0))
  )
}


class Composite(val shapes: Seq[Shape]) extends Shape {
  val node = new PNode
  shapes foreach { shape =>
    node.addChild(shape.node)
  }

  override def toString = "Staging.Group(" + shapes.mkString(",") + ")"
}
object Composite {
  def apply(shapes: Seq[Shape]) = Utils.runInSwingThreadAndWait {
    val shape = new Composite(shapes)
    Impl.figure0.addPnode(shape.node)
    shape
  }
}

object Style {
  val savedStyles =
    new scala.collection.mutable.Stack[(Paint, Paint, java.awt.Stroke)]()
  val f = Impl.figure0

  def save {
    Utils.runInSwingThread {
      savedStyles push Tuple3(f.fillColor, f.lineColor, f.lineStroke)
    }
  }

  def restore {
    Utils.runInSwingThread {
      if (savedStyles nonEmpty) {
        val (fc, sc, st) = savedStyles.pop
        f.setFillColor(fc)
        f.setPenColor(sc)
        f.setLineStroke(st)
      }
    }
  }

  def apply(fc: Paint, sc: Paint, sw: Double)(body: => Unit) = {
    save
    Utils.runInSwingThread {
      f.setFillColor(fc)
      f.setPenColor(sc)
      f.setPenThickness(sw)
    }
    try { body }
    finally { restore }
  }
}


class Bounds(x1: Double, y1: Double, x2: Double, y2: Double) {
  val bounds = new PBounds(x1, y1, x2 - x1, y2 - y1)

  def getWidth = Utils.runInSwingThreadAndWait { bounds.getWidth }
  def getHeight = Utils.runInSwingThreadAndWait { bounds.getHeight }
  def getOrigin = Utils.runInSwingThreadAndWait {
    val p = bounds.getOrigin
    Point(p.getX, p.getY)
  }
  def getCenter2D = Utils.runInSwingThreadAndWait {
    val p = bounds.getCenter2D
    Point(p.getX, p.getY)
  }
  def getExt = Utils.runInSwingThreadAndWait {
    val p = bounds.getOrigin
    Point(p.getX + bounds.getWidth, p.getY + bounds.getHeight)
  }
  def resetToZero = Utils.runInSwingThreadAndWait { bounds.resetToZero }
  def inset(dx: Double, dy: Double) = Utils.runInSwingThreadAndWait {
    bounds.inset(dx, dy)
  }
  def setRect(x1: Double, y1: Double, x2: Double, y2: Double) {
    Utils.runInSwingThread {
      bounds.setRect(x1, y1, x2 - x1, y2 - y1)
    }
  }
}
object Bounds {
  def apply(b: PBounds) = Utils.runInSwingThreadAndWait {
    val x = b.getX
    val y = b.getY
    val w = b.getWidth
    val h = b.getHeight
    new Bounds(x, y, x + w, y + h)
  }
  def apply(x1: Double, y1: Double, x2: Double, y2: Double) = Utils.runInSwingThreadAndWait {
    new Bounds(x1, y1, x2, y2)
  }
}
