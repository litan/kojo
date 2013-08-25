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

import java.awt.Color
import java.awt.Paint
import java.awt.geom.AffineTransform

import net.kogics.kojo.core.Picture
import net.kogics.kojo.kgeom.PolyLine

import core.Picture
import util.Utils

trait Transformer extends Picture with CorePicOps2 {
  val tpic: Picture
  def canvas = tpic.canvas
  def bounds = tpic.bounds
  def dumpInfo() = tpic.dumpInfo()
  def rotate(angle: Double) = tpic.rotate(angle)
  def rotateAboutPoint(angle: Double, x: Double, y: Double) = tpic.rotateAboutPoint(angle, x, y)
  def scale(factor: Double) = tpic.scale(factor)
  def scale(x: Double, y: Double) = tpic.scale(x, y)
  def opacityMod(f: Double) = tpic.opacityMod(f)
  def hueMod(f: Double) = tpic.hueMod(f)
  def satMod(f: Double) = tpic.satMod(f)
  def britMod(f: Double) = tpic.britMod(f)
  def translate(x: Double, y: Double) = tpic.translate(x, y)
  def offset(x: Double, y: Double) = tpic.offset(x, y)
  def flipX() = tpic.flipX()
  def flipY() = tpic.flipY()
  def transformBy(trans: AffineTransform) = tpic.transformBy(trans)
  def decorateWith(painter: Painter) = tpic.decorateWith(painter)
  def tnode = tpic.tnode
  def axesOn() = tpic.axesOn()
  def axesOff() = tpic.axesOff()
  def toggleV() = tpic.toggleV()
  def position = tpic.position
  def setPosition(x: Double, y: Double) = tpic.setPosition(x, y)
  def heading = tpic.heading
  def setHeading(angle: Double) = tpic.setHeading(angle)
  def setPenColor(color: Color) = tpic.setPenColor(color)
  def setPenThickness(th: Double) = tpic.setPenThickness(th)
  def setFillColor(color: Paint) = tpic.setFillColor(color)
  def morph(fn: Seq[PolyLine] => Seq[PolyLine]) = tpic.morph(fn)
  def foreachPolyLine(fn: PolyLine => Unit) = tpic.foreachPolyLine(fn)
  def distanceTo(other: Picture) = tpic.distanceTo(other)
  def area = tpic.area
  def perimeter = tpic.perimeter
  def picGeom = tpic.picGeom
  def visible() = tpic.visible()
  def invisible() = tpic.invisible()
  def isDrawn() = tpic.isDrawn()
  def isVisible() = tpic.isVisible()
  def myCanvas = tpic.myCanvas
  def erase() = tpic.erase()
}

abstract class Transform(pic: Picture) extends Transformer {
  val tpic = pic
}

case class Rot(angle: Double)(pic: Picture) extends Transform(pic) {
  def draw() {
    pic.rotate(angle)
    pic.draw()
  }
  def copy = Rot(angle)(pic.copy)
}

case class Rotp(angle: Double, x: Double, y: Double)(pic: Picture) extends Transform(pic) {
  def draw() {
    pic.rotateAboutPoint(angle, x, y)
    pic.draw()
  }
  def copy = Rotp(angle, x, y)(pic.copy)
}

case class Scale(factor: Double)(pic: Picture) extends Transform(pic) {
  def draw() {
    pic.scale(factor)
    pic.draw()
  }
  def copy = Scale(factor)(pic.copy)
}

case class ScaleXY(x: Double, y: Double)(pic: Picture) extends Transform(pic) {
  def draw() {
    pic.scale(x, y)
    pic.draw()
  }
  def copy = ScaleXY(x, y)(pic.copy)
}

case class Trans(x: Double, y: Double)(pic: Picture) extends Transform(pic) {
  def draw() {
    pic.translate(x, y)
    pic.draw()
  }
  def copy = Trans(x, y)(pic.copy)
}

case class Offset(x: Double, y: Double)(pic: Picture) extends Transform(pic) {
  def draw() {
    pic.offset(x, y)
    pic.draw()
  }
  def copy = Trans(x, y)(pic.copy)
}

case class FlipY(pic: Picture) extends Transform(pic) {
  def draw() {
    pic.flipY()
    pic.draw()
  }
  def copy = FlipY(pic.copy)
}

case class FlipX(pic: Picture) extends Transform(pic) {
  def draw() {
    pic.flipX()
    pic.draw()
  }
  def copy = FlipX(pic.copy)
}

case class AxesOn(pic: Picture) extends Transform(pic) {
  def draw() {
    pic.draw()
    pic.axesOn()
  }
  def copy = AxesOn(pic.copy)
}

case class Opac(f: Double)(pic: Picture) extends Transform(pic) {
  def draw() {
    pic.opacityMod(f)
    pic.draw()
  }
  def copy = Opac(f)(pic.copy)
}

case class Hue(f: Double)(pic: Picture) extends Transform(pic) {
  Utils.checkHsbModFactor(f)
    
  def draw() {
    pic.draw()
    pic.hueMod(f)
  }
  def copy = Hue(f)(pic.copy)
}

case class Sat(f: Double)(pic: Picture) extends Transform(pic) {
  Utils.checkHsbModFactor(f)

  def draw() {
    pic.draw()
    pic.satMod(f)
  }
  def copy = Sat(f)(pic.copy)
}

case class Brit(f: Double)(pic: Picture) extends Transform(pic) {
  Utils.checkHsbModFactor(f)

  def draw() {
    pic.draw()
    pic.britMod(f)
  }
  def copy = Brit(f)(pic.copy)
}

object Deco {
  def apply(pic: Picture)(painter: Painter): Deco = Deco(pic)(painter)
}

class Deco(pic: Picture)(painter: Painter) extends Transform(pic) {
  def draw() {
    pic.decorateWith(painter) 
    pic.draw() 
  }
  def copy = Deco(pic.copy)(painter)
}
case class Fill(color: Paint)(pic: Picture) extends Deco(pic)({ t =>
    t.setFillColor(color)
  }) {
  override def copy = Fill(color)(pic.copy)
}

case class Stroke(color: Color)(pic: Picture) extends Deco(pic)({ t =>
    t.setPenColor(color)
  }) {
  override def copy = Stroke(color)(pic.copy)
}

case class StrokeWidth(w: Double)(pic: Picture) extends Deco(pic)({ t =>
    t.setPenThickness(w)
  }) {
  override def copy = StrokeWidth(w)(pic.copy)
}

abstract class ComposableTransformer extends Function1[Picture,Picture] {outer =>
  def apply(p: Picture): Picture
  def ->(p: Picture) = apply(p)
  def * (other: ComposableTransformer) = new ComposableTransformer {
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
  def apply(p: Picture) = Fill(color)(p)
}

case class Strokec(color: Color) extends ComposableTransformer {
  def apply(p: Picture) = Stroke(color)(p)
}

case class StrokeWidthc(w: Double) extends ComposableTransformer {
  def apply(p: Picture) = StrokeWidth(w)(p)
}

case class Decoc(painter: Painter) extends ComposableTransformer {
  def apply(p: Picture) = Deco(p)(painter)
}

