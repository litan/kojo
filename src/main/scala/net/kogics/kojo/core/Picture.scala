package net.kogics.kojo
package core

import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage
import java.awt.image.BufferedImageOp
import java.awt.Paint
import java.awt.Shape

import com.vividsolutions.jts.geom.Geometry
import edu.umd.cs.piccolo.util.PBounds
import edu.umd.cs.piccolo.PNode
import net.kogics.kojo.kgeom.PolyLine
import net.kogics.kojo.picture.ImageOp
import net.kogics.kojo.util.Utils
import net.kogics.kojo.util.Vector2D

trait Picture extends InputAware {
  def canvas: SCanvas
  def pnode = tnode
  def draw(): Unit
  def erase(): Unit
  def isDrawn: Boolean
  def bounds: PBounds
  def rotate(angle: Double): Unit
  def rotateAboutPoint(angle: Double, x: Double, y: Double): Unit
  def scale(factor: Double): Unit
  def scaleAboutPoint(factor: Double, x: Double, y: Double): Unit
  def scaleAboutPoint(factorX: Double, factorY: Double, x: Double, y: Double): Unit
  def scale(xFactor: Double, yFactor: Double): Unit
  def shear(shearX: Double, shearY: Double): Unit
  def translate(x: Double, y: Double): Unit
  def translate(v: Vector2D): Unit = translate(v.x, v.y): Unit
  def transv(v: Vector2D) = translate(v.x, v.y): Unit
  def offset(x: Double, y: Double): Unit
  def offset(v: Vector2D): Unit = offset(v.x, v.y): Unit
  def offsetv(v: Vector2D) = offset(v.x, v.y): Unit
  def flipX(): Unit
  def flipY(): Unit
  def opacityMod(f: Double): Unit
  def hueMod(f: Double): Unit
  def satMod(f: Double): Unit
  def britMod(f: Double): Unit
  def transformBy(trans: AffineTransform): Unit
  def setTransform(trans: AffineTransform): Unit
  def dumpInfo(): Unit
  def copy: Picture
  def tnode: PNode
  def axesOn(): Unit
  def axesOff(): Unit
  def visible(): Unit
  def invisible(): Unit
  def toggleV(): Unit
  def isVisible: Boolean
  def intersects(other: Picture): Boolean
  def collidesWith(other: Picture) = intersects(other)
  def collisions(others: Set[Picture]): Set[Picture] = {
    others.filter { this intersects _ }
  }
  def collision(others: Seq[Picture]): Option[Picture] = {
    others.find { this intersects _ }
  }
  def intersection(other: Picture): Geometry
  def contains(other: Picture): Boolean
  def distanceTo(other: Picture): Double
  def isCloser(other: Picture, distance: Double): Boolean = distanceTo(other) < distance
  def area: Double
  def perimeter: Double
  def picGeom: Geometry

  def position: Point
  def setPosition(x: Double, y: Double): Unit
  def setPosition(p: Point): Unit = setPosition(p.x, p.y)
  def heading: Double
  def setHeading(angle: Double): Unit
  def setRotation(angle: Double) = setHeading(angle)
  def scaleFactor: (Double, Double)
  def setScaleFactor(x: Double, y: Double): Unit
  def setScale(f: Double): Unit
  def transform: AffineTransform
  def setPenColor(color: Paint): Unit
  def setPenThickness(th: Double): Unit
  def setNoPen(): Unit = {
    setPenColor(null)
    setPenThickness(0)
  }
  def setPenCapJoin(capJoin: (Int, Int)): Unit = setPenCapJoin(capJoin._1, capJoin._2)
  def setPenCapJoin(cap: Int, join: Int): Unit
  def setFillColor(color: Paint): Unit
  def opacity: Double
  def setOpacity(o: Double): Unit
  def setZIndex(zIndex: Int): Unit
  @deprecated("Use picture.react instead", "2.1")
  def act(fn: Picture => Unit) = react(fn)
  def react(fn: Picture => Unit): Unit
  @deprecated("Use picture.react instead", "2.1")
  def animate(fn: => Unit): Unit = {
    react { me =>
      fn
    }
  }
  def stopReactions(): Unit
  // provide these explicitly, so that subclasses that are case
  // classes can live within sets and maps
  override def equals(other: Any) = this eq other.asInstanceOf[AnyRef]
  override def hashCode = System.identityHashCode(this)

  def morph(fn: Seq[PolyLine] => Seq[PolyLine]): Unit
  def foreachPolyLine(fn: PolyLine => Unit): Unit
  def toImage: BufferedImage
  def forwardInputTo(p: Picture) = Utils.runInSwingThread {
    tnode.getInputEventListeners.foreach { tnode.removeInputEventListener(_) }
    p.tnode.getInputEventListeners.foreach { tnode.addInputEventListener(_) }
  }
  def moveToFront() = Utils.runInSwingThread {
    tnode.moveToFront()
  }
  def moveToBack() = Utils.runInSwingThread {
    tnode.moveToBack()
  }

  def moveToBackAboveStage() = Utils.runInSwingThread {
    val p = tnode.getParent
    if (p != null) {
      val nc = p.getChildrenCount
      var idx = 0
      var found = false
      while (idx < nc && !found) {
        if (p.getChild(idx) == canvas.stageArea.tnode) {
          found = true
        }
        else {
          idx += 1
        }
      }
      if (found) {
        p.removeChild(tnode)
        p.addChild(idx + 1, tnode)
      }
      else {
        moveToBack()
      }
    }
  }

  def showNext(): Unit = showNext(100)
  def showNext(gap: Long): Unit
  def update(newData: Any): Unit
  def checkDraw(msg: String): Unit
  def beside(other: Picture): Picture
  def above(other: Picture): Picture
  def below(other: Picture): Picture = other.above(this)
  def on(other: Picture): Picture
  def under(other: Picture): Picture = other.on(this)
  def animateToPosition(x: Double, y: Double, inMillis: Long)(onEnd: => Unit): Unit = Utils.runInSwingThread {
    import edu.umd.cs.piccolo.activities.PActivity
    import edu.umd.cs.piccolo.activities.PActivity.PActivityDelegate
    val pos0 = position
    val delay = inMillis; val amtx = x - pos0.x; val amty = y - pos0.y

    val animActivity = new PActivity(delay) {
      override def activityStep(elapsedTime: Long): Unit = {
        val frac = elapsedTime.toDouble / delay
        setPosition(pos0.x + amtx * frac, pos0.y + amty * frac)
      }
    }

    animActivity.setDelegate(new PActivityDelegate {
      override def activityStarted(activity: PActivity): Unit = {}
      override def activityStepped(activity: PActivity): Unit = {}
      override def activityFinished(activity: PActivity): Unit = {
        setPosition(x, y)
        onEnd
      }
    })
    canvas.animateActivity(animActivity)
  }
  def animateToPositionDelta(dx: Double, dy: Double, inMillis: Long)(onEnd: => Unit): Unit = {
    val pos0 = position
    animateToPosition(pos0.x + dx, pos0.y + dy, inMillis)(onEnd)
  }

  def withRotation(angle: Double): Picture = thatsRotated(angle)
  def withRotationAround(angle: Double, x: Double, y: Double): Picture = thatsRotatedAround(angle, x, y)
  def withTranslation(x: Double, y: Double): Picture = thatsTranslated(x, y)
  def withScaling(factor: Double): Picture = thatsScaled(factor)
  def withScaling(factorX: Double, factorY: Double): Picture = thatsScaled(factorX, factorY)
  def withScalingAround(factor: Double, x: Double, y: Double): Picture = thatsScaledAround(factor, x, y)
  def withScalingAround(factorX: Double, factorY: Double, x: Double, y: Double): Picture =
    thatsScaledAround(factorX, factorY, x, y)
  def withShear(shearX: Double, shearY: Double): Picture = thatsSheared(shearX, shearY)
  def withFillColor(color: Paint): Picture = thatsFilledWith(color)
  def withPenColor(color: Paint): Picture = thatsStrokeColored(color)
  def withPenThickness(t: Double): Picture = thatsStrokeSized(t)

  // Transforms that are applied before drawing
  def thatsRotated(angle: Double): Picture
  def thatsRotatedAround(angle: Double, x: Double, y: Double): Picture
  def thatsTranslated(x: Double, y: Double): Picture
  def thatsScaled(factor: Double): Picture
  def thatsScaled(factorX: Double, factorY: Double): Picture
  def thatsScaledAround(factor: Double, x: Double, y: Double): Picture
  def thatsScaledAround(factorX: Double, factorY: Double, x: Double, y: Double): Picture
  def thatsSheared(shearX: Double, shearY: Double): Picture
  def thatsFilledWith(color: Paint): Picture
  def thatsStrokeColored(color: Paint): Picture
  def thatsStrokeSized(t: Double): Picture

  def withEffect(filter: BufferedImageOp): Picture
  def withEffect(filter: ImageOp): Picture
  def withFlippedX: Picture
  def withFlippedY: Picture
  def withFading(distance: Int): Picture
  def withBlurring(radius: Int): Picture
  def withAxes: Picture
  def withLocalBounds: Picture
  def withOpacity(opacity: Double): Picture
  def withPosition(x: Double, y: Double): Picture
  def withZIndex(idx: Int): Picture
  def withClipping(clipShape: Shape): Picture
  def withClipping(clipPic: Picture): Picture
  def withMask(maskPic: Picture): Picture
  def withPenCapJoin(capJoin: (Int, Int)): Picture
  def withNoPen(): Picture
}
