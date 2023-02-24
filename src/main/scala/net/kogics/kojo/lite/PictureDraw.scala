/*
 * Copyright (C) 2020 Lalit Pant <pant.lalit@gmail.com>
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
package net.kogics.kojo.lite

import java.awt.geom.AffineTransform

import net.kogics.kojo.util.Utils

class PictureDraw(val b: Builtins) {
  import b._

  def frozen(): Unit = {
    _frozen = true
  }

  def randomSeed(s: Long): Unit = {
    setRandomSeed(s)
  }

  val ROUND = CapJoin.CAP_ROUND
  val PROJECT = CapJoin.CAP_SQUARE
  val SQUARE = CapJoin.CAP_BUTT
  val MITER = CapJoin.JOIN_MITER
  val BEVEL = CapJoin.JOIN_BEVEL

  var fillColor: Color = _
  var strokeColor: Color = _
  var strokeThickness = 0.0
  var shapeVertices: collection.mutable.ArrayBuffer[(Double, Double)] = null
  var matrices = List.empty[AffineTransform]
  var penCap = ROUND
  var penJoin = MITER
  val transform = new AffineTransform
  var _frozen = false

  reset()

  def reset(): Unit = {
    fillColor = null
    strokeColor = cm.red
    strokeThickness = 2.0
    shapeVertices = null
    matrices = List.empty[AffineTransform]
    penCap = ROUND
    penJoin = MITER
    transform.setToIdentity()
    _frozen = false
  }

  def background(c: Color): Unit = {
    setBackground(c)
    TSCanvas.erasePictures()
  }

  def background(n: Int): Unit = {
    setBackground(cm.rgb(n, n, n))
    TSCanvas.erasePictures()
  }

  def background(r: Int, g: Int, b: Int): Unit = {
    setBackground(cm.rgb(r, g, b))
    TSCanvas.erasePictures()
  }

  def noStroke(): Unit = {
    strokeThickness = 0
    strokeColor = noColor
  }

  def noFill(): Unit = {
    fillColor = null
  }

  def fill(c: Color): Unit = {
    fillColor = c
  }

  def fill(r: Int, g: Int, b: Int): Unit = {
    fillColor = cm.rgb(r, g, b)
  }

  def fill(n: Int, a: Int): Unit = {
    fillColor = cm.rgba(n, n, n, a)
  }

  def stroke(c: Color): Unit = {
    strokeColor = c
  }

  def stroke(gray: Int, alpha: Int): Unit = {
    strokeColor = cm.rgba(gray, gray, gray, alpha)
  }

  def strokeWeight(n: Double): Unit = {
    strokeThickness = n
  }

  private def applyState(p: Picture): Unit = {
    p.setPenColor(strokeColor)
    p.setPenThickness(strokeThickness)
    p.setPenCapJoin(penCap, penJoin)
    p.setFillColor(fillColor)
    p.setTransform(transform)
  }

  def drawPic(pic: Picture): Picture = {
    val pic2 = if (_frozen) Picture.effectablePic(pic) else pic
    pic2.draw()
    pic2
  }

  def returnPic(x: Double, y: Double, pic: Picture) = {
    applyState(pic)
    pic.translate(x, y)
    drawPic(pic)
  }

  def ellipse(cx: Double, cy: Double, width: Double, height: Double) = {
    returnPic(cx, cy, Picture.ellipse(width / 2, height / 2))
  }

  // a version of ellipse that does not clash with the turtle ellipse
  def ellip(cx: Double, cy: Double, width: Double, height: Double) = ellipse(cx, cy, width, height)

  def arc(cx: Double, cy: Double, width: Double, height: Double, start: Double, extent: Double) = {
    // not quite the full deal
    returnPic(cx, cy, Picture.arc(width / 2, -extent.toDegrees))
  }

  def line(x1: Double, y1: Double, x2: Double, y2: Double) = {
    returnPic(x1, y1, Picture.line(x2 - x1, y2 - y1))
  }

  def rectangle(x: Double, y: Double, width: Double, height: Double) = {
    returnPic(x, y, Picture.rectangle(width, height))
  }
  val rect = rectangle _ // canvas compatibility

  def point(x: Double, y: Double) = {
    line(x - 0.01 / 2, y, x + 0.01 / 2, y)
  }

  def turtleShape(x: Double, y: Double)(fn: Turtle => Unit) = {
    returnPic(x, y, Picture.fromTurtle(fn))
  }

  def beginShape(): Unit = {
    shapeVertices = ArrayBuffer.empty[(Double, Double)]
  }

  def vertex(x: Double, y: Double): Unit = {
    shapeVertices.append((x, y))
  }

  def endShape() = {
    val pic = Picture.fromPath { p =>
      val pt = shapeVertices.remove(0)
      p.moveTo(pt._1, pt._2)
      shapeVertices.foreach { pt =>
        p.lineTo(pt._1, pt._2)
      }
    }
    shapeVertices = null
    applyState(pic)
    drawPic(pic)
  }

  def translate(x: Double, y: Double): Unit = {
    transform.translate(x, y)
  }

  def rotate(angle: Double): Unit = {
    transform.rotate(angle)
  }

  def scale(f: Double): Unit = {
    transform.scale(f, f)
  }

  def scale(fx: Double, fy: Double): Unit = {
    transform.scale(fx, fy)
  }

  def pushMatrix(): Unit = {
    matrices = transform.clone.asInstanceOf[AffineTransform] :: matrices
  }

  def popMatrix(): Unit = {
    transform.setTransform(matrices.head)
    matrices = matrices.tail
  }

  def strokeCap(n: Int): Unit = {
    penCap = n
  }

  def strokeJoin(n: Int): Unit = {
    penJoin = n
  }

  def shape(pic: Picture, fx: Double, fy: Double) = {
    applyState(pic)
    pic.scale(fx, fy)
    drawPic(pic)
  }
}
