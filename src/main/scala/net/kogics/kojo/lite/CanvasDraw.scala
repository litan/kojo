package net.kogics.kojo.lite

import java.awt.geom.GeneralPath

import net.kogics.kojo.core.VertexShapeSupport

class CanvasDraw(g2d: java.awt.Graphics2D, width: Double, height: Double, val b: Builtins) extends VertexShapeSupport {
  import b._

  def randomSeed(s: Long): Unit = {
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
  var matrices = List.empty[AffineTransform]
  var penCap = ROUND
  var penJoin = MITER

  var stroke = makeStroke(strokeThickness, penCap, penJoin)
  val tempEllipse = new java.awt.geom.Ellipse2D.Double
  val tempArc = new java.awt.geom.Arc2D.Double
  val tempLine = new java.awt.geom.Line2D.Double
  val tempRect = new java.awt.geom.Rectangle2D.Double
  val tempPath = new java.awt.geom.GeneralPath
  var loop = true

  def makeStroke(n: Double, cap: Int, join: Int) =
    new java.awt.BasicStroke(n.toFloat, cap, join)

  def noLoop(): Unit = {
    loop = false
  }

  def noStroke(): Unit = {
    strokeColor = null
  }

  def noFill(): Unit = {
    fillColor = null
  }

  def fill(r: Int, g: Int, b: Int): Unit = {
    fillColor = cm.rgb(r, g, b)
  }

  def fill(c: Color): Unit = {
    fillColor = c
  }

  def fill(n: Int, a: Int): Unit = {
    fillColor = cm.rgba(n, n, n, a)
  }

  def stroke(gray: Int, alpha: Int): Unit = {
    strokeColor = cm.rgba(gray, gray, gray, alpha)
  }

  def stroke(r: Int, g: Int, b: Int): Unit = {
    strokeColor = cm.rgb(r, g, b)
  }

  def stroke(c: Color): Unit = {
    strokeColor = c
  }

  def strokeWeight(n: Double): Unit = {
    strokeThickness = n
    stroke = makeStroke(strokeThickness, penCap, penJoin)
  }

  def drawShape(s: java.awt.Shape): Unit = {
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

  def shapeDone(path: GeneralPath): Unit = {
    drawShape(path)
    path.reset()
  }

  def background(c: Color): Unit = {
    tempRect.setRect(0, 0, width, height)
    g2d.setPaint(c)
    g2d.fill(tempRect)
  }

  def background(n: Int): Unit = {
    tempRect.setRect(0, 0, width, height)
    g2d.setPaint(cm.rgb(n, n, n))
    g2d.fill(tempRect)
  }

  def background(n: Int, a: Int): Unit = {
    tempRect.setRect(0, 0, width, height)
    g2d.setPaint(cm.rgba(n, n, n, a))
    g2d.fill(tempRect)
  }

  def ellipse(cx: Double, cy: Double, w: Double, h: Double): Unit = {
    tempEllipse.setFrame(cx - w / 2, cy - h / 2, w, h)
    drawShape(tempEllipse)
  }

  def arc(cx: Double, cy: Double, w: Double, h: Double, start: Double, extent: Double): Unit = {
    tempArc.setAngleStart(start.toDegrees)
    tempArc.setAngleExtent(extent.toDegrees)
    tempArc.setFrame(cx - w / 2, cy - h / 2, w, h)
    drawShape(tempArc)
  }

  def line(x1: Double, y1: Double, x2: Double, y2: Double): Unit = {
    tempLine.setLine(x1, y1, x2, y2)
    drawShape(tempLine)
  }

  def rect(x1: Double, y1: Double, x2: Double, y2: Double): Unit = {
    tempRect.setRect(x1, y1, x2, y2)
    drawShape(tempRect)
  }

  def point(x: Double, y: Double): Unit = {
    line(x - 0.01 / 2, y, x + 0.01 / 2, y)
  }

  def translate(x: Double, y: Double): Unit = {
    g2d.translate(x, y)
  }

  def rotate(angle: Double): Unit = {
    g2d.rotate(angle)
  }

  def scale(f: Double): Unit = {
    g2d.scale(f, f)
  }

  def scale(fx: Double, fy: Double): Unit = {
    g2d.scale(fx, fy)
  }

  def pushMatrix(): Unit = {
    matrices = g2d.getTransform :: matrices
  }

  def popMatrix(): Unit = {
    g2d.setTransform(matrices.head)
    matrices = matrices.tail
  }

  def blendMode(c: java.awt.Composite): Unit = {
    g2d.setComposite(c)
  }

  def strokeCap(n: Int): Unit = {
    penCap = n
    stroke = makeStroke(strokeThickness, penCap, penJoin)
  }

  def strokeJoin(n: Int): Unit = {
    penJoin = n
    stroke = makeStroke(strokeThickness, penCap, penJoin)
  }
}
