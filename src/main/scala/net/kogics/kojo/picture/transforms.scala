/*
 * Copyright (C) 2011 Lalit Pant <pant.lalit@gmail.com>
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
package picture

import java.awt.geom.AffineTransform
import java.awt.Paint
import java.awt.Shape

import net.kogics.kojo.core.Picture
import net.kogics.kojo.kgeom.PolyLine
import net.kogics.kojo.picture.PicCache.freshPic
import util.Utils

trait Transformer extends Picture with CorePicOps2 {
  val tpic: Picture
  def canvas = tpic.canvas
  def bounds = tpic.bounds
  def dumpInfo() = tpic.dumpInfo()
  def rotate(angle: Double) = tpic.rotate(angle)
  def rotateAboutPoint(angle: Double, x: Double, y: Double) = tpic.rotateAboutPoint(angle, x, y)
  def scale(factor: Double) = tpic.scale(factor)
  def scaleAboutPoint(factor: Double, x: Double, y: Double) = tpic.scaleAboutPoint(factor, x, y)
  def scaleAboutPoint(factorX: Double, factorY: Double, x: Double, y: Double) =
    tpic.scaleAboutPoint(factorX, factorY, x, y)
  def scale(xFactor: Double, yFactor: Double) = tpic.scale(xFactor, yFactor)
  def shear(shearX: Double, shearY: Double): Unit = tpic.shear(shearX, shearY)
  def opacityMod(f: Double) = tpic.opacityMod(f)
  def hueMod(f: Double) = tpic.hueMod(f)
  def satMod(f: Double) = tpic.satMod(f)
  def britMod(f: Double) = tpic.britMod(f)
  def translate(x: Double, y: Double) = tpic.translate(x, y)
  def offset(x: Double, y: Double) = tpic.offset(x, y)
  def flipX() = tpic.flipX()
  def flipY() = tpic.flipY()
  def transformBy(trans: AffineTransform) = tpic.transformBy(trans)
  def transform = tpic.transform
  def setTransform(trans: AffineTransform) = tpic.setTransform(trans)
  def tnode = tpic.tnode
  def axesOn() = tpic.axesOn()
  def axesOff() = tpic.axesOff()
  def toggleV() = tpic.toggleV()
  def position = tpic.position
  def setPosition(x: Double, y: Double) = tpic.setPosition(x, y)
  def heading = tpic.heading
  def setHeading(angle: Double) = tpic.setHeading(angle)
  def scaleFactor = tpic.scaleFactor
  def setScaleFactor(x: Double, y: Double) = tpic.setScaleFactor(x, y)
  def setScale(f: Double) = tpic.setScale(f)
  def setPenColor(color: Paint) = tpic.setPenColor(color)
  def setPenThickness(th: Double) = tpic.setPenThickness(th)
  def setPenCapJoin(cap: Int, join: Int) = tpic.setPenCapJoin(cap, join)
  def setFillColor(color: Paint) = tpic.setFillColor(color)
  def opacity = tpic.opacity
  def setOpacity(o: Double) = tpic.setOpacity(o)
  def setZIndex(zIndex: Int): Unit = tpic.setZIndex(zIndex)
  def morph(fn: Seq[PolyLine] => Seq[PolyLine]) = tpic.morph(fn)
  def foreachPolyLine(fn: PolyLine => Unit) = tpic.foreachPolyLine(fn)
  def distanceTo(other: Picture) = tpic.distanceTo(other)
  def area = tpic.area
  def perimeter = tpic.perimeter
  def picGeom = tpic.picGeom
  def visible() = tpic.visible()
  def invisible() = tpic.invisible()
  def isDrawn = tpic.isDrawn
  def checkDraw(msg: String) = tpic.checkDraw(msg)
  def isVisible = tpic.isVisible
  override def toImage = tpic.toImage
  def showNext(gap: Long) = tpic.showNext(gap)
  def update(newData: Any) = tpic.update(newData)
}

abstract class Transform(pic: Picture) extends Transformer {
  val tpic = freshPic(pic)
}

case class Rot(angle: Double)(pic: Picture) extends Transform(pic) {
  def draw(): Unit = {
    tpic.rotate(angle)
    tpic.draw()
  }
  def copy = Rot(angle)(pic.copy)
  override def toString() = s"Rot($angle) (Id: ${System.identityHashCode(this)}) -> ${tpic.toString}"
}

case class Rotp(angle: Double, x: Double, y: Double)(pic: Picture) extends Transform(pic) {
  def draw(): Unit = {
    tpic.rotateAboutPoint(angle, x, y)
    tpic.draw()
  }
  def copy = Rotp(angle, x, y)(pic.copy)
  override def toString() = s"Rotp($angle, $x, $y) (Id: ${System.identityHashCode(this)}) -> ${tpic.toString}"
}

case class Scale(factor: Double)(pic: Picture) extends Transform(pic) {
  def draw(): Unit = {
    tpic.scale(factor)
    tpic.draw()
  }
  def copy = Scale(factor)(pic.copy)
  override def toString() = s"Scale($factor) (Id: ${System.identityHashCode(this)}) -> ${tpic.toString}"
}

case class ScaleXY(x: Double, y: Double)(pic: Picture) extends Transform(pic) {
  def draw(): Unit = {
    tpic.scale(x, y)
    tpic.draw()
  }
  def copy = ScaleXY(x, y)(pic.copy)
  override def toString() = s"Scale($x, $y) (Id: ${System.identityHashCode(this)}) -> ${tpic.toString}"
}

case class Trans(x: Double, y: Double)(pic: Picture) extends Transform(pic) {
  def draw(): Unit = {
    tpic.translate(x, y)
    tpic.draw()
  }
  def copy = Trans(x, y)(pic.copy)
  override def toString() = s"Trans($x, $y) (Id: ${System.identityHashCode(this)}) -> ${tpic.toString}"
}

case class Offset(x: Double, y: Double)(pic: Picture) extends Transform(pic) {
  def draw(): Unit = {
    tpic.offset(x, y)
    tpic.draw()
  }
  def copy = Offset(x, y)(pic.copy)
  override def toString() = s"Offset($x, $y) (Id: ${System.identityHashCode(this)}) -> ${tpic.toString}"
}

case class Position(x: Double, y: Double)(pic: Picture) extends Transform(pic) {
  def draw(): Unit = {
    tpic.setPosition(x, y)
    tpic.draw()
  }
  def copy = Position(x, y)(pic.copy)
  override def toString() = s"Position($x, $y) (Id: ${System.identityHashCode(this)}) -> ${tpic.toString}"
}

case class FlipY(pic: Picture) extends Transform(pic) {
  def draw(): Unit = {
    tpic.flipY()
    tpic.draw()
  }
  def copy = FlipY(pic.copy)
  override def toString() = s"FlipY(Id: ${System.identityHashCode(this)}) -> ${tpic.toString}"
}

case class FlipX(pic: Picture) extends Transform(pic) {
  def draw(): Unit = {
    tpic.flipX()
    tpic.draw()
  }
  def copy = FlipX(pic.copy)
  override def toString() = s"FlipX(Id: ${System.identityHashCode(this)}) -> ${tpic.toString}"
}

case class AxesOn(pic: Picture) extends Transform(pic) {
  def draw(): Unit = {
    tpic.draw()
    tpic.axesOn()
  }
  def copy = AxesOn(pic.copy)
  override def toString() = s"AxesOn(Id: ${System.identityHashCode(this)}) -> ${tpic.toString}"
}

case class Opac(f: Double)(pic: Picture) extends Transform(pic) {
  def draw(): Unit = {
    tpic.opacityMod(f)
    tpic.draw()
  }
  def copy = Opac(f)(pic.copy)
  override def toString() = s"Opac($f)(Id: ${System.identityHashCode(this)}) -> ${tpic.toString}"
}

case class Hue(f: Double)(pic: Picture) extends Transform(pic) {
  Utils.checkHsbModFactor(f)

  def draw(): Unit = {
    tpic.draw()
    tpic.hueMod(f)
  }
  def copy = Hue(f)(pic.copy)
  override def toString() = s"Hue($f) (Id: ${System.identityHashCode(this)}) -> ${tpic.toString}"
}

case class Sat(f: Double)(pic: Picture) extends Transform(pic) {
  Utils.checkHsbModFactor(f)

  def draw(): Unit = {
    tpic.draw()
    tpic.satMod(f)
  }
  def copy = Sat(f)(pic.copy)
  override def toString() = s"Sat($f) (Id: ${System.identityHashCode(this)}) -> ${tpic.toString}"
}

case class Brit(f: Double)(pic: Picture) extends Transform(pic) {
  Utils.checkHsbModFactor(f)

  def draw(): Unit = {
    tpic.draw()
    tpic.britMod(f)
  }
  def copy = Brit(f)(pic.copy)
  override def toString() = s"Brit($f) (Id: ${System.identityHashCode(this)}) -> ${tpic.toString}"
}

case class Fill(color: Paint)(pic: Picture) extends Transform(pic) {
  def draw(): Unit = {
    tpic.setFillColor(color)
    tpic.draw()
  }
  override def copy = Fill(color)(pic.copy)
  override def toString() = s"Fill($color) (Id: ${System.identityHashCode(this)}) -> ${tpic.toString}"
}

case class Stroke(color: Paint)(pic: Picture) extends Transform(pic) {
  def draw(): Unit = {
    tpic.setPenColor(color)
    tpic.draw()
  }

  override def copy = Stroke(color)(pic.copy)
  override def toString() = s"Stroke($color) (Id: ${System.identityHashCode(this)}) -> ${tpic.toString}"
}

case class StrokeWidth(w: Double)(pic: Picture) extends Transform(pic) {
  def draw(): Unit = {
    tpic.setPenThickness(w)
    tpic.draw()
  }
  override def copy = StrokeWidth(w)(pic.copy)
  override def toString() = s"StrokeWidth($w) (Id: ${System.identityHashCode(this)}) -> ${tpic.toString}"
}

case class PreDrawTransform(fn: Picture => Unit)(pic: Picture) extends Transform(pic) {
  def draw(): Unit = {
    fn(tpic)
    tpic.draw()
  }
  override def copy = PreDrawTransform(fn)(pic.copy)
  override def toString() = s"PreDrawTransform (Id: ${System.identityHashCode(this)} -> ${tpic.toString})"
}

case class PostDrawTransform(fn: Picture => Unit)(pic: Picture) extends Transform(pic) {
  def draw(): Unit = {
    tpic.draw()
    fn(tpic)
  }
  override def copy = PostDrawTransform(fn)(pic.copy)
  override def toString() = s"PostDrawTransform (Id: ${System.identityHashCode(this)} -> ${tpic.toString})"
}

abstract class ComposableTransformer extends Function1[Picture, Picture] { outer =>
  def apply(p: Picture): Picture
  def ->(p: Picture) = apply(p)
  def *(other: ComposableTransformer) = new ComposableTransformer {
    def apply(p: Picture): Picture = {
      outer.apply(other.apply(p))
    }
  }
}

case class Rotc(angle: Double) extends ComposableTransformer {
  def apply(p: Picture) = Rot(angle)(p)
}

case class Rotpc(angle: Double, x: Double, y: Double) extends ComposableTransformer {
  def apply(p: Picture) = Rotp(angle, x, y)(p)
}

case class Scalec(factor: Double) extends ComposableTransformer {
  def apply(p: Picture) = Scale(factor)(p)
}

case class ScaleXYc(x: Double, y: Double) extends ComposableTransformer {
  def apply(p: Picture) = ScaleXY(x, y)(p)
}

case class Opacc(f: Double) extends ComposableTransformer {
  def apply(p: Picture) = Opac(f)(p)
}

case class Huec(f: Double) extends ComposableTransformer {
  def apply(p: Picture) = Hue(f)(p)
}

case class Satc(f: Double) extends ComposableTransformer {
  def apply(p: Picture) = Sat(f)(p)
}

case class Britc(f: Double) extends ComposableTransformer {
  def apply(p: Picture) = Brit(f)(p)
}

case class Transc(x: Double, y: Double) extends ComposableTransformer {
  def apply(p: Picture) = Trans(x, y)(p)
}

case class Offsetc(x: Double, y: Double) extends ComposableTransformer {
  def apply(p: Picture) = Offset(x, y)(p)
}

case object FlipYc extends ComposableTransformer {
  def apply(p: Picture) = FlipY(p)
}

case object FlipXc extends ComposableTransformer {
  def apply(p: Picture) = FlipX(p)
}

case object AxesOnc extends ComposableTransformer {
  def apply(p: Picture) = AxesOn(p)
}

case class Fillc(color: Paint) extends ComposableTransformer {
  def apply(p: Picture) = p.thatsFilledWith(color)
}

case class Strokec(color: Paint) extends ComposableTransformer {
  def apply(p: Picture) = p.thatsStrokeColored(color)
}

case class StrokeWidthc(w: Double) extends ComposableTransformer {
  def apply(p: Picture) = p.thatsStrokeSized(w)
}

case class Clippedc(s: Shape) extends ComposableTransformer {
  def apply(p: Picture) = p.withClipping(s)
}

case class PreDrawTransformc(fn: Picture => Unit) extends ComposableTransformer {
  def apply(p: Picture) = PreDrawTransform(fn)(p)
}

case class PostDrawTransformc(fn: Picture => Unit) extends ComposableTransformer {
  def apply(p: Picture) = PostDrawTransform(fn)(p)
}
