package net.kogics.kojo.core

import java.awt.geom.{GeneralPath, PathIterator, Point2D}

import processing.core.PMatrix3D

import scala.collection.mutable.ArrayBuffer

trait VertexShapeSupport {
  def drawPath(path: GeneralPath): Unit

  sealed trait ShapeVertex

  case class Vertex(x: Double, y: Double) extends ShapeVertex

  case class CurveVertex(x: Double, y: Double) extends ShapeVertex

  case class QuadVertex(x1: Double, y1: Double, x2: Double, y2: Double) extends ShapeVertex

  case class BezierVertex(x1: Double, y1: Double, x2: Double, y2: Double, x3: Double, y3: Double) extends ShapeVertex

  var shapeVertices: collection.mutable.ArrayBuffer[ShapeVertex] = _

  def beginShape(): Unit = {
    shapeVertices = ArrayBuffer.empty[ShapeVertex]
  }

  def vertex(x: Double, y: Double): Unit = {
    shapeVertices.append(Vertex(x, y))
  }

  def quadraticVertex(x1: Double, y1: Double, x2: Double, y2: Double): Unit = {
    shapeVertices.append(QuadVertex(x1, y1, x2, y2))
  }

  def bezierVertex(x1: Double, y1: Double, x2: Double, y2: Double, x3: Double, y3: Double): Unit = {
    shapeVertices.append(BezierVertex(x1, y1, x2, y2, x3, y3))
  }

  def curveVertex(x: Double, y: Double): Unit = {
    shapeVertices.append(CurveVertex(x, y))
  }

  private var curveCoordX = new Array[Float](4)
  private var curveCoordY = new Array[Float](4)
  private var curveDrawX = new Array[Float](4)
  private var curveDrawY = new Array[Float](4)

  private var curveTightness = 0f
  // catmull-rom basis matrix, perhaps with optional s parameter
  private var curveBasisMatrix: PMatrix3D = new PMatrix3D
  private var bezierBasisInverse: PMatrix3D = null
  private var curveToBezierMatrix: PMatrix3D = new PMatrix3D
  private val bezierBasisMatrix = new PMatrix3D(-1, 3, -3, 1, 3, -6, 3, 0, -3, 3, 0, 0, 1, 0, 0, 0)

  private def curveInit(): Unit = { // allocate only if/when used to save startup time
    val s = curveTightness
    curveBasisMatrix.set((s - 1) / 2f, (s + 3) / 2f, (-3 - s) / 2f, (1 - s) / 2f, 1 - s, (-5 - s) / 2f, s + 2, (s - 1) / 2f, (s - 1) / 2f, 0, (1 - s) / 2f, 0, 0, 1, 0, 0)
    bezierBasisInverse = bezierBasisMatrix.get
    bezierBasisInverse.invert
    curveToBezierMatrix.set(curveBasisMatrix)
    curveToBezierMatrix.preApply(bezierBasisInverse)
  }

  curveInit()

  private def curveVertexSegment(gpath: GeneralPath, x1: Float, y1: Float, x2: Float, y2: Float, x3: Float, y3: Float, x4: Float, y4: Float): Unit = {
    curveCoordX(0) = x1
    curveCoordY(0) = y1
    curveCoordX(1) = x2
    curveCoordY(1) = y2
    curveCoordX(2) = x3
    curveCoordY(2) = y3
    curveCoordX(3) = x4
    curveCoordY(3) = y4
    curveToBezierMatrix.mult(curveCoordX, curveDrawX)
    curveToBezierMatrix.mult(curveCoordY, curveDrawY)
    gpath.curveTo(curveDrawX(1), curveDrawY(1), curveDrawX(2), curveDrawY(2), curveDrawX(3), curveDrawY(3))
  }


  def endShape() = {
    val tempPath = new java.awt.geom.GeneralPath
    val pt0 = shapeVertices(0) match {
      case v@Vertex(x, y) => shapeVertices.remove(0); v
      case CurveVertex(x, y) => Vertex(x, y)
      case _ => Vertex(0, 0)
    }
    tempPath.moveTo(pt0.x, pt0.y)
    val curveVertexs = ArrayBuffer.empty[CurveVertex]
    shapeVertices.foreach {
      case Vertex(x, y) => tempPath.lineTo(x, y)
      case cv@CurveVertex(_, _) =>
        curveVertexs.append(cv)
        val cvlen = curveVertexs.length
        if (cvlen > 3) {
          curveVertexSegment(tempPath,
            curveVertexs(cvlen - 4).x.toFloat, curveVertexs(cvlen - 4).y.toFloat,
            curveVertexs(cvlen - 3).x.toFloat, curveVertexs(cvlen - 3).y.toFloat,
            curveVertexs(cvlen - 2).x.toFloat, curveVertexs(cvlen - 2).y.toFloat,
            curveVertexs(cvlen - 1).x.toFloat, curveVertexs(cvlen - 1).y.toFloat
          )
        }
      case QuadVertex(x1, y1, x2, y2) => tempPath.quadTo(x1, y1, x2, y2)
      case BezierVertex(x1, y1, x2, y2, x3, y3) => tempPath.curveTo(x1, y1, x2, y2, x3, y3)
    }
    shapeVertices = null
    drawPath(tempPath)
    tempPath.reset()
  }
}


