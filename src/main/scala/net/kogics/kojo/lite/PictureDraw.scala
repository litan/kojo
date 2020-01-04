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

import net.kogics.kojo.util.Utils
import java.awt.geom.AffineTransform

class PictureDraw(val b: Builtins) {
  import b._

  def frozen(): Unit = {
    _frozen = true
  }

  def cm_hsv(h: Double, s: Double, v: Double) = {
    val hh = h
    var ll = (2 - s) * v
    val den = if (ll <= 1) ll else 2 - ll
    val ss = s * v / den
    ll /= 2
    cm.hsl(hh, ss, ll)
  }

  def cm_hsvuv(h: Double, s: Double, v: Double) = {
    val hh = h
    var ll = (2 - s) * v
    val den = if (ll <= 1) ll else 2 - ll
    val ss = s * v / den
    ll /= 2
    cm.hsluv(hh, ss, ll)
  }

  def randomSeed(s: Long) {
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

  def background(c: Color) {
    setBackground(c)
    TSCanvas.erasePictures()
  }

  def background(n: Int) {
    setBackground(cm.rgb(n, n, n))
    TSCanvas.erasePictures()
  }

  def background(r: Int, g: Int, b: Int) {
    setBackground(cm.rgb(r, g, b))
    TSCanvas.erasePictures()
  }

  def noStroke() {
    strokeThickness = 0
    strokeColor = noColor
  }

  def noFill() {
    fillColor = null
  }

  def fill(c: Color) {
    fillColor = c
  }

  def fill(r: Int, g: Int, b: Int) {
    fillColor = cm.rgb(r, g, b)
  }

  def fill(n: Int, a: Int): Unit = {
    fillColor = cm.rgba(n, n, n, a)
  }

  def stroke(c: Color) {
    strokeColor = c
  }

  def stroke(gray: Int, alpha: Int) {
    strokeColor = cm.rgba(gray, gray, gray, alpha)
  }

  def strokeWeight(n: Double) {
    strokeThickness = n
  }

  private def applyState(p: Picture) {
    p.setPenColor(strokeColor)
    p.setPenThickness(strokeThickness)
    p.setPenCapJoin(penCap, penJoin)
    p.setFillColor(fillColor)
    p.setTransform(transform)
  }

  val noOpFilter = new com.jhlabs.image.AbstractBufferedImageOp {
    import java.awt.image.BufferedImage
    def filter(src: BufferedImage, dest: BufferedImage) = src
  }

  def drawPic(pic: Picture): Picture = {
    val pic2 = if (_frozen) applyFilter(noOpFilter) -> pic else pic
    pic2.draw()
    pic2
  }

  def returnPic(x: Double, y: Double, pic: Picture) = {
    applyState(pic)
    pic.translate(x, y)
    drawPic(pic)
  }

  def ellipse(cx: Double, cy: Double, w: Double, h: Double) = {
    returnPic(cx, cy, Picture.ellipse(w / 2, h / 2))
  }

  // a version of ellipse that does not clash with the turtle ellipse
  def ellip(cx: Double, cy: Double, w: Double, h: Double) = ellipse(cx, cy, w, h)

  def line(x1: Double, y1: Double, x2: Double, y2: Double) = {
    returnPic(x1, y1, Picture.line(x2 - x1, y2 - y1))
  }

  def rect(x: Double, y: Double, w: Double, h: Double) = {
    returnPic(x, y, Picture.rectangle(w, h))
  }

  def rectangle(x: Double, y: Double, w: Double, h: Double) = rect(x, y, w, h)

  def turtleShape(x: Double, y: Double)(fn: Turtle => Unit) = {
    returnPic(x, y, Picture.fromTurtle(fn))
  }

  def beginShape() {
    shapeVertices = ArrayBuffer.empty[(Double, Double)]
  }

  def vertex(x: Double, y: Double) {
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

  def translate(x: Double, y: Double) {
    transform.translate(x, y)
  }

  def rotate(angle: Double) {
    transform.rotate(angle)
  }

  def scale(f: Double) {
    transform.scale(f, f)
  }

  def scale(fx: Double, fy: Double) {
    transform.scale(fx, fy)
  }

  def pushMatrix() {
    matrices = transform.clone.asInstanceOf[AffineTransform] :: matrices
  }

  def popMatrix() {
    transform.setTransform(matrices.head)
    matrices = matrices.tail
  }

  def strokeCap(n: Int) {
    penCap = n
  }

  def strokeJoin(n: Int) {
    penJoin = n
  }

  def shape(pic: Picture, fx: Double, fy: Double) = {
    applyState(pic)
    pic.scale(fx, fy)
    drawPic(pic)
  }
}
