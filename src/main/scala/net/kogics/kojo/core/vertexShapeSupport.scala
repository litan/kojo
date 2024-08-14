package net.kogics.kojo.core

import java.awt.geom.GeneralPath

import scala.collection.mutable.ArrayBuffer
import scala.language.implicitConversions

import processing.core.PMatrix3D

object VertexShapeSupport {
  val curveTightness = 0f
  val bezierBasisMatrix = new PMatrix3D(-1, 3, -3, 1, 3, -6, 3, 0, -3, 3, 0, 0, 1, 0, 0, 0)
  val s = curveTightness

  val curveBasisMatrix = new PMatrix3D
  curveBasisMatrix.set(
    (s - 1) / 2f,
    (s + 3) / 2f,
    (-3 - s) / 2f,
    (1 - s) / 2f,
    1 - s,
    (-5 - s) / 2f,
    s + 2,
    (s - 1) / 2f,
    (s - 1) / 2f,
    0,
    (1 - s) / 2f,
    0,
    0,
    1,
    0,
    0
  )

  val bezierBasisInverse = bezierBasisMatrix.get
  bezierBasisInverse.invert

  val curveToBezierMatrix = new PMatrix3D
  curveToBezierMatrix.set(curveBasisMatrix)
  curveToBezierMatrix.preApply(bezierBasisInverse)
}

trait VertexShapeSupport {
  def shapeDone(path: GeneralPath): Unit
  def shapePath = new java.awt.geom.GeneralPath

  private sealed trait ShapeVertex
  private case class Vertex(x: Double, y: Double) extends ShapeVertex
  private case class CurveVertex(x: Double, y: Double) extends ShapeVertex
  private case class QuadVertex(x1: Double, y1: Double, x2: Double, y2: Double) extends ShapeVertex
  private case class BezierVertex(x1: Double, y1: Double, x2: Double, y2: Double, x3: Double, y3: Double)
      extends ShapeVertex
  private case class ArcVertex(x: Double, y: Double, angle: Double) extends ShapeVertex

  private var shapeVertices: collection.mutable.ArrayBuffer[ShapeVertex] = _
  private var curveCoordX: Array[Float] = _
  private var curveCoordY: Array[Float] = _
  private var curveDrawX: Array[Float] = _
  private var curveDrawY: Array[Float] = _
  private var firstVertex = true

  private lazy val curveInit = {
    curveCoordX = new Array[Float](4)
    curveCoordY = new Array[Float](4)
    curveDrawX = new Array[Float](4)
    curveDrawY = new Array[Float](4)
  }

  private def checkBegin(): Unit = {
    require(shapeVertices != null, "Do a beginShape() before adding vertices to a shape")
  }

  def beginShape(): Unit = {
    shapeVertices = ArrayBuffer.empty[ShapeVertex]
    firstVertex = true
  }

  def vertex(x: Double, y: Double): Unit = {
    checkBegin()
    shapeVertices.append(Vertex(x, y))
  }

  def quadraticVertex(cx: Double, cy: Double, x2: Double, y2: Double): Unit = {
    checkBegin()
    shapeVertices.append(QuadVertex(cx, cy, x2, y2))
  }

  def bezierVertex(cx1: Double, cy1: Double, cx2: Double, cy2: Double, x2: Double, y2: Double): Unit = {
    checkBegin()
    shapeVertices.append(BezierVertex(cx1, cy1, cx2, cy2, x2, y2))
  }

  def curveVertex(x: Double, y: Double): Unit = {
    checkBegin()
    shapeVertices.append(CurveVertex(x, y))
  }

  def arcVertex(x: Double, y: Double, angle: Double): Unit = {
    checkBegin()
    shapeVertices.append(ArcVertex(x, y, angle))
  }

  private def rtToXy(r: Double, theta: Double): Point = {
    val t = theta.toRadians
    val x = r * math.cos(t)
    val y = r * math.sin(t)
    Point(x, y)
  }

  def vertexRt(r: Double, theta: Double): Unit = {
    val p = rtToXy(r, theta)
    vertex(p.x, p.y)
  }

  def curveVertexRt(r: Double, theta: Double): Unit = {
    val p = rtToXy(r, theta)
    curveVertex(p.x, p.y)
  }

  private def curveVertexSegment(
      gpath: GeneralPath,
      x1: Float,
      y1: Float,
      x2: Float,
      y2: Float,
      x3: Float,
      y3: Float,
      x4: Float,
      y4: Float
  ): Unit = {
    import VertexShapeSupport.curveToBezierMatrix
    curveInit

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
    if (firstVertex) {
      gpath.moveTo(curveDrawX(0), curveDrawY(0))
      firstVertex = false
    }
    gpath.curveTo(curveDrawX(1), curveDrawY(1), curveDrawX(2), curveDrawY(2), curveDrawX(3), curveDrawY(3))
  }

  def endShape() = {
    require(shapeVertices.length > 0, "A shape should have at least one vertex")

    def checkCurveFirstVertex(): Unit = {
      if (firstVertex) {
        throw new RuntimeException("A curved shape should start with a vertex")
      }
    }

    val tempPath = shapePath
    lazy val tempRichPath = new Rich2DPath(tempPath)
    val curveVertices = ArrayBuffer.empty[CurveVertex]
    shapeVertices.foreach {
      case Vertex(x, y) =>
        if (firstVertex) {
          tempPath.moveTo(x, y)
          firstVertex = false
        }
        else {
          tempPath.lineTo(x, y)
        }
      case cv @ CurveVertex(_, _) =>
        curveVertices.append(cv)
        val cvlen = curveVertices.length
        if (cvlen > 3) {
          curveVertexSegment(
            tempPath,
            curveVertices(cvlen - 4).x.toFloat,
            curveVertices(cvlen - 4).y.toFloat,
            curveVertices(cvlen - 3).x.toFloat,
            curveVertices(cvlen - 3).y.toFloat,
            curveVertices(cvlen - 2).x.toFloat,
            curveVertices(cvlen - 2).y.toFloat,
            curveVertices(cvlen - 1).x.toFloat,
            curveVertices(cvlen - 1).y.toFloat
          )
        }
      case QuadVertex(x1, y1, x2, y2) =>
        checkCurveFirstVertex(); tempPath.quadTo(x1, y1, x2, y2)
      case BezierVertex(x1, y1, x2, y2, x3, y3) =>
        checkCurveFirstVertex(); tempPath.curveTo(x1, y1, x2, y2, x3, y3)
      case ArcVertex(x1, y1, angle1) =>
        checkCurveFirstVertex(); tempRichPath.arcTo(x1, y1, angle1)
    }
    shapeVertices = null
    shapeDone(tempPath)
  }
}

class VertexShape(val path: GeneralPath) extends VertexShapeSupport {
  def shapeDone(path2: GeneralPath): Unit = {}
  override def shapePath: GeneralPath = path
}
