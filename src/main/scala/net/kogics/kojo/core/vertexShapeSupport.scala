package net.kogics.kojo.core

import java.awt.geom.GeneralPath

import processing.core.PMatrix3D

import scala.collection.mutable.ArrayBuffer
import scala.language.implicitConversions

object VertexShapeSupport {
  val curveTightness = 0f
  val bezierBasisMatrix = new PMatrix3D(-1, 3, -3, 1, 3, -6, 3, 0, -3, 3, 0, 0, 1, 0, 0, 0)
  val s = curveTightness

  val curveBasisMatrix = new PMatrix3D
  curveBasisMatrix.set((s - 1) / 2f, (s + 3) / 2f, (-3 - s) / 2f, (1 - s) / 2f, 1 - s, (-5 - s) / 2f, s + 2, (s - 1) / 2f, (s - 1) / 2f, 0, (1 - s) / 2f, 0, 0, 1, 0, 0)

  val bezierBasisInverse = bezierBasisMatrix.get
  bezierBasisInverse.invert

  val curveToBezierMatrix = new PMatrix3D
  curveToBezierMatrix.set(curveBasisMatrix)
  curveToBezierMatrix.preApply(bezierBasisInverse)
}

trait VertexShapeSupport {
  def shapeDone(path: GeneralPath): Unit
  def shapePath = new java.awt.geom.GeneralPath

  sealed trait ShapeVertex
  case class Vertex(x: Double, y: Double) extends ShapeVertex
  case class CurveVertex(x: Double, y: Double) extends ShapeVertex
  case class QuadVertex(x1: Double, y1: Double, x2: Double, y2: Double) extends ShapeVertex
  case class BezierVertex(x1: Double, y1: Double, x2: Double, y2: Double, x3: Double, y3: Double) extends ShapeVertex

  private var shapeVertices: collection.mutable.ArrayBuffer[ShapeVertex] = _
  private var curveCoordX: Array[Float] = _
  private var curveCoordY: Array[Float] = _
  private var curveDrawX: Array[Float] = _
  private var curveDrawY: Array[Float] = _

  lazy val curveInit = {
    curveCoordX = new Array[Float](4)
    curveCoordY = new Array[Float](4)
    curveDrawX = new Array[Float](4)
    curveDrawY = new Array[Float](4)
  }

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

  private def curveVertexSegment(gpath: GeneralPath, x1: Float, y1: Float, x2: Float, y2: Float, x3: Float, y3: Float, x4: Float, y4: Float): Unit = {
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
    gpath.curveTo(curveDrawX(1), curveDrawY(1), curveDrawX(2), curveDrawY(2), curveDrawX(3), curveDrawY(3))
  }

  def endShape() = {
    val tempPath = shapePath
    val pt0 = shapeVertices(0) match {
      case v @ Vertex(x, y) =>
        shapeVertices.remove(0); v
      case CurveVertex(x, y) => Vertex(x, y)
      case _                 => Vertex(0, 0)
    }
    tempPath.moveTo(pt0.x, pt0.y)
    val curveVertexs = ArrayBuffer.empty[CurveVertex]
    shapeVertices.foreach {
      case Vertex(x, y) => tempPath.lineTo(x, y)
      case cv @ CurveVertex(_, _) =>
        curveVertexs.append(cv)
        val cvlen = curveVertexs.length
        if (cvlen > 3) {
          curveVertexSegment(
            tempPath,
            curveVertexs(cvlen - 4).x.toFloat, curveVertexs(cvlen - 4).y.toFloat,
            curveVertexs(cvlen - 3).x.toFloat, curveVertexs(cvlen - 3).y.toFloat,
            curveVertexs(cvlen - 2).x.toFloat, curveVertexs(cvlen - 2).y.toFloat,
            curveVertexs(cvlen - 1).x.toFloat, curveVertexs(cvlen - 1).y.toFloat
          )
        }
      case QuadVertex(x1, y1, x2, y2)           => tempPath.quadTo(x1, y1, x2, y2)
      case BezierVertex(x1, y1, x2, y2, x3, y3) => tempPath.curveTo(x1, y1, x2, y2, x3, y3)
    }
    shapeVertices = null
    shapeDone(tempPath)
  }
}

// RichPath adds vertex shape support to GeneralPath
object RichPath {
  implicit def rp2p(rp: RichPath): GeneralPath = rp.path
}
class RichPath(val path: GeneralPath) extends VertexShapeSupport {
  // enable using setPosition instead of moveTo - to be consistent with turtle usage.
  def setPosition(x: Double, y: Double) = path.moveTo(x, y)
  def shapeDone(path2: GeneralPath): Unit = {}
  override def shapePath: GeneralPath = path
}
