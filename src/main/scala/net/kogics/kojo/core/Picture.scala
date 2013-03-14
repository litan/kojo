package net.kogics.kojo
package core

import java.awt.Color
import java.awt.Paint
import java.awt.geom.AffineTransform

import com.vividsolutions.jts.geom.Geometry

import net.kogics.kojo.kgeom.PolyLine
import net.kogics.kojo.util.Vector2D

import edu.umd.cs.piccolo.PNode
import edu.umd.cs.piccolo.util.PBounds

trait Picture extends InputAware {
  def canvas: SCanvas
  def myNode = tnode
  def decorateWith(painter: Painter): Unit
  def draw(): Unit
  def erase(): Unit
  def isDrawn(): Boolean
  def bounds: PBounds
  def rotate(angle: Double): Picture
  def rotateAboutPoint(angle: Double, x: Double, y: Double): Picture
  def scale(factor: Double): Picture
  def scale(x: Double, y: Double): Picture
  def translate(x: Double, y: Double): Picture
  def transv(v: Vector2D) = translate(v.x, v.y)
  def offset(x: Double, y: Double)
  def flipX(): Picture
  def flipY(): Picture
  def opacityMod(f: Double): Unit
  def hueMod(f: Double): Unit
  def satMod(f: Double): Unit
  def britMod(f: Double): Unit
  def transformBy(trans: AffineTransform)
  def dumpInfo(): Unit
  def copy: Picture
  def tnode: PNode
  def axesOn(): Unit
  def axesOff(): Unit
  def visible(): Unit
  def invisible(): Unit
  def toggleV(): Unit
  def isVisible(): Boolean
  def intersects(other: Picture): Boolean
  def collidesWith(other: Picture) = intersects(other)
  def collisions(others: Set[Picture]): Set[Picture] = {
    others.filter {this intersects _}
  }
  def collision(others: Seq[Picture]): Option[Picture] = {
    others.filter {this intersects _}.headOption
  } 
  def intersection(other: Picture): Geometry
  def distanceTo(other: Picture): Double
  def area: Double
  def perimeter: Double
  def picGeom: Geometry
  
  def position: Point
  def setPosition(x: Double, y: Double): Unit
  def setPosition(p: Point): Unit = setPosition(p.x, p.y)
  def heading: Double
  def setHeading(angle: Double)
  def setPenColor(color: Color)
  def setPenThickness(th: Double)
  def setFillColor(color: Paint)
  @deprecated("Use picture.react instead", "2.1")
  def act(fn: Picture => Unit) = react(fn)
  def react(fn: Picture => Unit)
  @deprecated("Use picture.react instead", "2.1")
  def animate(fn: => Unit) {
    react { me =>
      fn
    }
  }
  // provide these explicitly, so that subclasses that are case
  // classes can live within sets and maps
  override def equals(other: Any) = this eq other.asInstanceOf[AnyRef]
  override def hashCode = System.identityHashCode(this)
  
  def morph(fn: Seq[PolyLine] => Seq[PolyLine])
  def foreachPolyLine(fn: PolyLine => Unit) 

  // Todo
  // def bounceOff(other: Picture, vec: Vector2D)
}
