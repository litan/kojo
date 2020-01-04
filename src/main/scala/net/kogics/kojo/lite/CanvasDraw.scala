package net.kogics.kojo.lite

class CanvasDraw(g2d: java.awt.Graphics2D, width: Double, height: Double, val b: Builtins) {
  import b._

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

  import java.awt.geom.AffineTransform

  var fillColor: Color = null
  var strokeColor: Color = cm.red
  var strokeThickness = 2.0
  var shapeVertices: collection.mutable.ArrayBuffer[(Double, Double)] = null
  var matrices = List.empty[AffineTransform]
  var penCap = ROUND
  var penJoin = MITER

  var stroke = makeStroke(strokeThickness, penCap, penJoin)
  val tempEllipse = new java.awt.geom.Ellipse2D.Double
  val tempLine = new java.awt.geom.Line2D.Double
  val tempRect = new java.awt.geom.Rectangle2D.Double
  val tempPath = new java.awt.geom.GeneralPath

  def makeStroke(n: Double, cap: Int, join: Int) =
    new java.awt.BasicStroke(n.toFloat, cap, join)

  def noStroke() {
    strokeColor = null
  }

  def noFill() {
    fillColor = null
  }

  def fill(r: Int, g: Int, b: Int) {
    fillColor = cm.rgb(r, g, b)
  }

  def fill(c: Color) {
    fillColor = c
  }

  def fill(n: Int, a: Int): Unit = {
    fillColor = cm.rgba(n, n, n, a)
  }

  def stroke(gray: Int, alpha: Int) {
    strokeColor = cm.rgba(gray, gray, gray, alpha)
  }

  def stroke(r: Int, g: Int, b: Int) {
    strokeColor = cm.rgb(r, g, b)
  }

  def stroke(c: Color) {
    strokeColor = c
  }

  def strokeWeight(n: Double) {
    strokeThickness = n
    stroke = makeStroke(strokeThickness, penCap, penJoin)
  }

  def drawShape(s: java.awt.Shape) {
    if (fillColor != null) {
      g2d.setPaint(fillColor)
      g2d.fill(s)
    }
    if (strokeColor != null) {
      g2d.setPaint(strokeColor)
      g2d.setStroke(stroke)
      g2d.draw(s)
    }
  }

  def background(c: Color) {
    tempRect.setRect(0, 0, width, height)
    g2d.setPaint(c)
    g2d.fill(tempRect)
  }

  def background(n: Int) {
    tempRect.setRect(0, 0, width, height)
    g2d.setPaint(cm.rgb(n, n, n))
    g2d.fill(tempRect)
  }

  def background(n: Int, a: Int) {
    tempRect.setRect(0, 0, width, height)
    g2d.setPaint(cm.rgba(n, n, n, a))
    g2d.fill(tempRect)
  }

  def ellipse(cx: Double, cy: Double, w: Double, h: Double) {
    tempEllipse.setFrame(cx - w / 2, cy - h / 2, w, h)
    drawShape(tempEllipse)
  }

  def line(x1: Double, y1: Double, x2: Double, y2: Double) {
    tempLine.setLine(x1, y1, x2, y2)
    drawShape(tempLine)
  }

  def rect(x1: Double, y1: Double, x2: Double, y2: Double) {
    tempRect.setRect(x1, y1, x2, y2)
    drawShape(tempRect)
  }

  def translate(x: Double, y: Double) {
    g2d.translate(x, y)
  }

  def rotate(angle: Double) {
    g2d.rotate(angle)
  }

  def scale(f: Double) {
    g2d.scale(f, f)
  }

  def scale(fx: Double, fy: Double) {
    g2d.scale(fx, fy)
  }

  def pushMatrix() {
    matrices = g2d.getTransform :: matrices
  }

  def popMatrix() {
    g2d.setTransform(matrices.head)
    matrices = matrices.tail
  }

  def blendMode(c: java.awt.Composite) {
    g2d.setComposite(c)
  }

  def beginShape() {
    shapeVertices = ArrayBuffer.empty[(Double, Double)]
  }

  def vertex(x: Double, y: Double) {
    shapeVertices.append((x, y))
  }

  def endShape() = {
    tempPath.reset()
    val pt = shapeVertices.remove(0)
    tempPath.moveTo(pt._1, pt._2)
    shapeVertices.foreach { pt =>
      tempPath.lineTo(pt._1, pt._2)
    }
    shapeVertices = null
    drawShape(tempPath)
  }

  def strokeCap(n: Int) {
    penCap = n
    stroke = makeStroke(strokeThickness, penCap, penJoin)
  }

  def strokeJoin(n: Int) {
    penJoin = n
    stroke = makeStroke(strokeThickness, penCap, penJoin)
  }
}
