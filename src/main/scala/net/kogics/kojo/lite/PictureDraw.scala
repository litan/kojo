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

class PictureDraw(val b: Builtins) {
  import b._

  def size(w: Double, h: Double) {
    width = w.toInt
    height = h.toInt
    // the resizing takes a couple of tries to settle down
    repeat(2) {
      setDrawingCanvasSize(width, height)
    }
  }

  def setup(fn: => Unit) = runInGuiThread {
    fn
  }

  def draw(fn: => Unit) = TSCanvas.animate {
    fn
  }

  private def wh = {
    lazy val cb = canvasBounds
    val w = if (width == 0) cb.width else width
    val h = if (height == 0) cb.height else height
    (w, h)
  }

  def topLeftOrigin() {
    val (w, h) = wh
    def work = TSCanvas.zoomXY(1, -1, w / 2, -h / 2)
    work
    Utils.schedule(0.5) {
      work
    }
  }

  def bottomLeftOrigin() {
    val (w, h) = wh
    def work = TSCanvas.zoomXY(1, 1, w / 2, h / 2)
    work
    Utils.schedule(0.5) {
      work
    }
  }

  def centerOrigin() {
    TSCanvas.zoom(1, 0, 0)
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

  def rangeTo(start: Int, end: Int, step: Int = 1) = start to end by step
  def rangeTill(start: Int, end: Int, step: Int = 1) = start until end by step

  def rangeTo(start: Double, end: Double, step: Double) = Range.BigDecimal.inclusive(start, end, step)
  def rangeTill(start: Double, end: Double, step: Double) = Range.BigDecimal(start, end, step)

  def distance(x1: Double, y1: Double, x2: Double, y2: Double) =
    math.sqrt(math.pow(x2 - x1, 2) + math.pow(y2 - y1, 2))

  import scala.language.implicitConversions
  implicit def bd2double(bd: BigDecimal) = bd.doubleValue

  def randomSeed(s: Long) {
    setRandomSeed(s)
  }

  val ROUND = CapJoin.CAP_ROUND //> ROUND: Int = 1
  val PROJECT = CapJoin.CAP_SQUARE
  val SQUARE = CapJoin.CAP_BUTT
  val MITER = CapJoin.JOIN_MITER
  val BEVEL = CapJoin.JOIN_BEVEL

  import java.awt.geom.AffineTransform

  var fillColor: Color = null
  var strokeColor: Color = cm.red
  var strokeThickness = 2.0
  var shapeVertices: collection.mutable.ArrayBuffer[(Double, Double)] = null
  var matrices = List.empty[AffineTransform]
  var penCap = ROUND
  var penJoin = MITER
  var width = 0
  var height = 0

  import java.awt.geom.AffineTransform
  val transform = new AffineTransform

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

  def ellipse(cx: Double, cy: Double, w: Double, h: Double) = {
    val el = offset(cx, cy) -> Picture.ellipse(w / 2, h / 2)
    applyState(el)
    el.draw()
    el
  }

  // a version of ellipse that does not clash with the turtle ellipse
  def ellip(cx: Double, cy: Double, w: Double, h: Double) = ellipse(cx, cy, w, h)

  def line(x1: Double, y1: Double, x2: Double, y2: Double) = {
    val l = offset(x1, y1) -> Picture.line(x2 - x1, y2 - y1)
    applyState(l)
    l.draw()
    l
  }

  def rect(x: Double, y: Double, w: Double, h: Double) = {
    val r = offset(x, y) -> Picture.rectangle(w, h)
    applyState(r)
    r.draw()
    r
  }

  def rectangle(x: Double, y: Double, w: Double, h: Double) = rect(x, y, width, height)

  def turtleShape(x: Double, y: Double)(fn: Turtle => Unit) = {
    val pic = offset(x, y) -> Picture.fromTurtle(fn)
    applyState(pic)
    pic.draw()
    pic
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
    pic.draw()
    pic
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
    pic.draw()
    pic.scale(fx, fy)
    pic
  }
}
