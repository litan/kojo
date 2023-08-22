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

import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage
import java.awt.image.BufferedImageOp
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Paint
import java.awt.Shape
import java.util.concurrent.Future

import scala.collection.mutable.ArrayBuffer

import com.jhlabs.image.AbstractBufferedImageOp
import com.vividsolutions.jts.geom.util.AffineTransformation
import com.vividsolutions.jts.geom.Coordinate
import com.vividsolutions.jts.geom.Geometry
import com.vividsolutions.jts.geom.TopologyException
import edu.umd.cs.piccolo.activities.PActivity
import edu.umd.cs.piccolo.nodes.PPath
import edu.umd.cs.piccolo.nodes.PText
import edu.umd.cs.piccolo.PNode
import net.kogics.kojo.core.Cm
import net.kogics.kojo.core.Inch
import net.kogics.kojo.core.Picture
import net.kogics.kojo.core.Pixel
import net.kogics.kojo.core.SCanvas
import net.kogics.kojo.kgeom.PolyLine
import net.kogics.kojo.kmath.{ Kmath => Math }
import net.kogics.kojo.picture.PicCache.freshPics
import net.kogics.kojo.util.Utils

trait GeomPolygon { self: Picture =>
  lazy val geomPoly = {
    val gc = picGeom.getCoordinates
    val ab = new ArrayBuffer[Coordinate]
    ab ++= gc
    ab += gc(0)
    Gf.createPolygon(ab.toArray)
  }
}

trait UnsupportedOps {
  def notSupported(name: String, reason: String) = throw new UnsupportedOperationException(
    s"$name - operation not available $reason:\n${toString}"
  )
}

trait CorePicOps extends GeomPolygon with UnsupportedOps { self: Picture with RedrawStopper =>
  protected val camera = canvas.getCamera
  protected var axes: PNode = _
  protected var _picGeom: Geometry = _
  protected var _pgTransform: AffineTransformation = _
  protected var _zIndex = -1

  def pgTransform = {
    if (_pgTransform == null) {
      _pgTransform = t2t(tnode.getTransformReference(true))
    }
    _pgTransform
  }

  protected def realDraw(): Unit

  def draw(): Unit = {
    realDraw()
    //    Need to do the following if we ever have turtle commands that modify the turtle's layer transform
    //    Utils.runInSwingThread {
    //      pgTransform = t2t(tnode.getTransformReference(true))
    //    }
  }

  private def t2t(t: AffineTransform): AffineTransformation = {
    val ms = Array.fill(6)(0.0)
    val ms2 = Array.fill(6)(0.0)
    t.getMatrix(ms)
    ms2(0) = ms(0) // m00
    ms2(1) = ms(2) // m01
    ms2(2) = ms(4) // m02
    ms2(3) = ms(1) // m10
    ms2(4) = ms(3) // m11
    ms2(5) = ms(5) // m12
    new AffineTransformation(ms2)
  }

  def transformBy(trans: AffineTransform) = Utils.runInSwingThread {
    tnode.transformBy(trans)
    //    pgTransform.composeBefore(t2t(trans))
    _pgTransform = null
  }

  def setTransform(trans: AffineTransform) = Utils.runInSwingThread {
    tnode.setTransform(trans)
    _pgTransform = null
  }

  def rotateAboutPoint(angle: Double, x: Double, y: Double): Unit = {
    translate(x, y)
    rotate(angle)
    translate(-x, -y)
  }

  def rotate(angle: Double): Unit = {
    transformBy(AffineTransform.getRotateInstance(angle.toRadians))
  }

  private def safeScaleFactor(factor: Double) = if (factor == 0) Double.MinPositiveValue else factor

  def scale(factor: Double): Unit = {
    val safeFactor = safeScaleFactor(factor)
    transformBy(AffineTransform.getScaleInstance(safeFactor, safeFactor))
  }

  def scaleAboutPoint(factor: Double, x: Double, y: Double): Unit = {
    translate(x, y)
    scale(factor)
    translate(-x, -y)
  }

  def scaleAboutPoint(factorX: Double, factorY: Double, x: Double, y: Double): Unit = {
    translate(x, y)
    scale(factorX, factorY)
    translate(-x, -y)
  }

  def scale(xFactor: Double, yFactor: Double): Unit = {
    transformBy(AffineTransform.getScaleInstance(safeScaleFactor(xFactor), safeScaleFactor(yFactor)))
  }

  def shear(shearX: Double, shearY: Double): Unit = {
    transformBy(AffineTransform.getShearInstance(shearX, shearY))
  }

  def translate(x: Double, y: Double): Unit = {
    transformBy(AffineTransform.getTranslateInstance(x, y))
  }

  def offset(x: Double, y: Double) = Utils.runInSwingThread {
    tnode.offset(x, y)
    _pgTransform = null
  }

  def opacityMod(f: Double) = Utils.runInSwingThread {
    tnode.setTransparency(Math.constrain(tnode.getTransparency * (1 + f), 0, 1).toFloat)
  }

  def position = Utils.runInSwingThreadAndPause {
    val o = tnode.getOffset
    new core.Point(o.getX, o.getY)
  }

  def setPosition(x: Double, y: Double) = Utils.runInSwingThread {
    tnode.setOffset(x, y)
    _pgTransform = null
  }

  def setZIndex(zIndex: Int): Unit = Utils.runInSwingThread {
    _zIndex = zIndex
    val parent = tnode.getParent
    parent.removeChild(tnode)
    val nc = parent.getChildrenCount
    var idx = 0
    var found = false
    while (idx < nc && !found) {
      val pic = parent.getChild(idx).getAttribute("pic")
      val siblingZi = if (pic == null) -1 else pic.asInstanceOf[CorePicOps]._zIndex
      if (siblingZi > zIndex) {
        found = true
        parent.addChild(idx, tnode)
      }
      else {
        idx += 1
      }
    }
    if (!found) {
      parent.addChild(idx, tnode)
    }
  }

  def heading = Utils.runInSwingThreadAndPause {
    tnode.getRotation.toDegrees
  }

  def setHeading(angle: Double) = Utils.runInSwingThread {
    rotate(angle - heading)
    _pgTransform = null
  }

  def scaleFactor = Utils.runInSwingThreadAndPause {
    val tr = tnode.getTransformReference(true)
    (tr.getScaleX, tr.getScaleY)
  }

  def setScaleFactor(x: Double, y: Double): Unit = {
    throw new RuntimeException("use setScale(f) instead of setScaleFactor(x, y)")
  }

  def setScale(f: Double) = Utils.runInSwingThread {
    tnode.setScale(f)
    _pgTransform = null
  }

  def transform = Utils.runInSwingThreadAndPause {
    tnode.getTransformReference(true)
  }

  def opacity: Double = Utils.runInSwingThreadAndPause {
    tnode.getTransparency
  }

  def setOpacity(o: Double) = Utils.runInSwingThread {
    tnode.setTransparency(o.toFloat)
  }

  def flipX(): Unit = {
    transformBy(AffineTransform.getScaleInstance(1, -1))
  }

  def flipY(): Unit = {
    transformBy(AffineTransform.getScaleInstance(-1, 1))
  }

  def axesOn() = Utils.runInSwingThread {
    if (axes == null) {
      val (size, delta, num, bigt) = canvas.unitLen match {
        case Pixel => (200.0f, 20.0f, 10, 5)
        case Inch  => (4.0f, 0.25f, 16, 4)
        case Cm    => (10f, .5f, 20, 2)
      }
      val camScale = canvas.camScale.toFloat
      val tickSize = 3 / camScale
      val overrun = 5 / camScale
      def line(x1: Float, y1: Float, x2: Float, y2: Float) = {
        val l = PPath.createLine(x1, y1, x2, y2)
        l.setStroke(new BasicStroke(2 / camScale))
        l
      }
      def text(s: String, x: Double, y: Double) = {
        Utils.textNode(s, x, y, camScale)
      }
      axes = new PNode
      axes.addChild(line(-overrun, 0, size, 0))
      axes.addChild(line(0, -overrun, 0, size))
      for (i <- 1 to num) {
        val ts = if (i % bigt == 0) 2 * tickSize else tickSize
        axes.addChild(line(i * delta, ts, i * delta, -ts))
        axes.addChild(line(-ts, i * delta, ts, i * delta))
      }
      axes.addChild(text("x", size - delta / 2, delta))
      axes.addChild(text("y", delta / 2, size))
      tnode.addChild(axes)
    }
    else {
      axes.setVisible(true)
    }
    tnode.repaint()
  }

  def axesOff() = Utils.runInSwingThread {
    if (axes != null) {
      axes.setVisible(false)
      tnode.repaint()
    }
  }

  def visible() = Utils.runInSwingThread {
    if (!tnode.getVisible) {
      tnode.setVisible(true)
    }
  }

  def invisible() = Utils.runInSwingThread {
    if (tnode.getVisible) {
      tnode.setVisible(false)
    }
  }

  def toggleV() = Utils.runInSwingThread {
    if (tnode.getVisible) {
      tnode.setVisible(false)
    }
    else {
      tnode.setVisible(true)
    }
  }

  def isVisible = Utils.runInSwingThreadAndPause { tnode.getVisible }

  protected def initGeom(): Geometry
  def picGeom: Geometry = Utils.runInSwingThreadAndWait {
    if (!drawn) {
      throw new IllegalStateException("Cannot access a Picture's geometry before it is drawn.")
    }

    if (_picGeom == null) {
      try {
        _picGeom = initGeom()
      }
      catch {
        case ise: IllegalStateException =>
          throw ise
        case t: Throwable =>
          throw new IllegalStateException("Unable to create geometry for picture - " + t.getMessage, t)
      }
    }
    // thought related to getting geometry in global coords; runs into problems with, for e.g, a translated HPics inside a translated HPics
    //    t2t(tnode.getLocalToGlobalTransform(null)).transform(_picGeom)
    pgTransform.transform(_picGeom)
  }

  def distanceTo(other: Picture) = Utils.runInSwingThreadAndPause {
    picGeom.distance(other.picGeom)
  }

  def perimeter = Utils.runInSwingThreadAndPause {
    picGeom.getLength
  }

  def area = Utils.runInSwingThreadAndPause {
    geomPoly.getArea
  }

  override def toString() = s"Picture with Id: ${System.identityHashCode(this)}"

  protected def extractFillColor(fillPaint: Paint) = fillPaint match {
    case null     => Color.white
    case c: Color => c
    case _        => throw new IllegalStateException("You can't extract rgb values of non Color paints")
  }

  override def toImage = Utils.runInSwingThreadAndWait {
    tnode.toImage.asInstanceOf[BufferedImage]
  }

  def showNext(gap: Long): Unit = notSupported("showNext", "for non-batch picture")
  def update(newData: Any): Unit = notSupported("update", "for immutable picture")
}

// Ops that transforms cannot delegate to their underlying tpic
trait CorePicOps2 extends GeomPolygon { self: Picture =>
  def picLayer = canvas.pictures
  var reactions = Vector.empty[Future[PActivity]]

  def react(fn: Picture => Unit): Unit = {
    if (!isDrawn) {
      throw new IllegalStateException("Ask picture to react after you draw it.")
    }
    val reaction = canvas.animate {
      fn(this)
    }
    reactions :+= reaction
  }

  def stopReactions() = Utils.runInSwingThread {
    reactions.foreach { canvas.stopAnimationActivity(_) }
    reactions = Vector.empty
  }

  def erase() = Utils.runInSwingThread {
    stopReactions()
    invisible()
    picLayer.removeChild(tnode)
    //    picLayer.repaint()
  }

  def intersects(other: Picture) = Utils.runInSwingThreadAndPause {
    if (this == other) {
      false
    }
    else if (tnode.getVisible && other.tnode.getVisible) {
      picGeom.intersects(other.picGeom)
    }
    else {
      false
    }
  }

  def intersection(other: Picture) = Utils.runInSwingThreadAndPause {
    if (this == other) {
      Gf.createGeometryCollection(null)
    }
    else if (tnode.getVisible && other.tnode.getVisible) {
      try {
        picGeom.intersection(other.picGeom)
      }
      catch {
        case te: TopologyException =>
          println("Unable to determine intersection - " + te.getMessage())
          Gf.createGeometryCollection(null)
      }
    }
    else {
      Gf.createGeometryCollection(null)
    }
  }

  def contains(other: Picture) = Utils.runInSwingThreadAndPause {
    if (this == other) {
      false
    }
    else if (tnode.getVisible && other.tnode.getVisible) {
      geomPoly.covers(other.picGeom)
    }
    else {
      false
    }
  }

  def beside(other: Picture): Picture = HPics2(this, other)
  def above(other: Picture): Picture = VPics2(other, this)
  def on(other: Picture): Picture = GPics2(other, this)

  // Transforms that are applied before drawing
  def thatsRotated(angle: Double): Picture = PreDrawTransform { pic => pic.rotate(angle) }(this)
  def thatsRotatedAround(angle: Double, x: Double, y: Double): Picture =
    PreDrawTransform { pic => pic.rotateAboutPoint(angle, x, y) }(this)
  def thatsTranslated(x: Double, y: Double): Picture = PreDrawTransform { pic => pic.translate(x, y) }(this)
  def thatsScaled(factor: Double): Picture = PreDrawTransform { pic => pic.scale(factor) }(this)
  def thatsScaled(factorX: Double, factorY: Double): Picture =
    PreDrawTransform { pic => pic.scale(factorX, factorY) }(this)
  def thatsScaledAround(factor: Double, x: Double, y: Double): Picture =
    PreDrawTransform { pic => pic.scaleAboutPoint(factor, x, y) }(this)
  def thatsScaledAround(factorX: Double, factorY: Double, x: Double, y: Double): Picture =
    PreDrawTransform { pic => pic.scaleAboutPoint(factorX, factorY, x, y) }(this)
  def thatsSheared(shearX: Double, shearY: Double): Picture =
    PreDrawTransform { pic => pic.shear(shearX, shearY) }(this)
  def thatsFilledWith(color: Paint): Picture = PostDrawTransform { pic => pic.setFillColor(color) }(this)
  def thatsStrokeColored(color: Paint): Picture = PostDrawTransform { pic => pic.setPenColor(color) }(this)
  def thatsStrokeSized(t: Double): Picture = PostDrawTransform { pic => pic.setPenThickness(t) }(this)

  def withEffect(filter: BufferedImageOp): Picture = ApplyFilter(filter)(epic(this))
  def withEffect(filterOp: ImageOp): Picture = {
    val filter2 = new AbstractBufferedImageOp {
      def filter(src: BufferedImage, dest: BufferedImage) = filterOp.filter(src)
    }
    withEffect(filter2)
  }
  def withFlippedX: Picture = FlipY(this)
  def withFlippedY: Picture = FlipX(this)
  def withFading(distance: Int): Picture = Fade(distance)(epic(this))
  def withBlurring(radius: Int): Picture = Blur(radius)(epic(this))
  def withAxes: Picture = PostDrawTransform { pic => pic.axesOn() }(this)
  def withLocalBounds: Picture = PostDrawTransform { pic => picLocalBounds(pic) }(this)
  def withOpacity(opacity: Double): Picture = PostDrawTransform { pic => pic.setOpacity(opacity) }(this)
  def withPosition(x: Double, y: Double): Picture = PostDrawTransform { pic => pic.setPosition(x, y) }(this)
  def withZIndex(zIndex: Int): Picture = PostDrawTransform { pic => pic.setZIndex(zIndex) }(this)
  def withClipping(clipShape: Shape): Picture = new ClipPic(this, clipShape)(canvas)
  def withClipping(clipPic: Picture): Picture = new ClipPicWithPic(this, clipPic)(canvas)
  def withMask(maskPic: Picture): Picture = this.withEffect(new MaskOp(maskPic))
  def withPenCapJoin(capJoin: (Int, Int)): Picture = PostDrawTransform { pic => pic.setPenCapJoin(capJoin) }(this)
  def withNoPen(): Picture = PostDrawTransform { pic => pic.setNoPen() }(this)
}

trait RedrawStopper extends Picture {
  @volatile var drawn = false
  def isDrawn = drawn
  def checkDraw(msg: String): Unit = {
    if (drawn) {
      throw new RuntimeException(msg)
    }
  }
  abstract override def draw(): Unit = {
    checkDraw("You can't redraw a picture")
    drawn = true
    super.draw()
  }
}

trait TNodeCacher {
  def makeTnode: PNode
  @volatile var _tnode: PNode = _
  def tnode = {
    if (_tnode == null) {
      _tnode = makeTnode
      _tnode.addAttribute("pic", this)
    }
    _tnode
  }
}

object Pic {
  def apply(painter: Painter)(implicit canvas: SCanvas) = new Pic(painter)
}

class Pic(painter: Painter)(implicit val canvas: SCanvas)
    extends Picture
    with CorePicOps
    with CorePicOps2
    with TNodeCacher
    with RedrawStopper {
  @volatile var _t: canvas.TurtleLike = _
  val ErrMsg =
    "Unable to create picture turtle. This could be because you have a draw() call after an animate{ } or morph{ } call"

  def t = {
    if (_t == null) Utils.runInSwingThreadAndWait(10000, ErrMsg) {
      if (_t == null) {
        val tt = canvas.newInvisibleTurtle(0, 0)
        tt.setAnimationDelay(0)
        val tl = tt.tlayer
        camera.removeLayer(tl)
        picLayer.addChild(tl)
        _t = tt
      }
      else {
        _t
      }
    }
    _t
  }

  def makeTnode = t.tlayer

  def realDraw(): Unit = {
    painter(t)
    Utils.runInSwingThread {
      val tl = tnode
      tl.invalidateFullBounds()
      tl.repaint()
      picLayer.repaint()
    }
  }

  def bounds = Utils.runInSwingThreadAndPause {
    tnode.getFullBounds
  }

  protected def initGeom() = {
    val cab = new ArrayBuffer[Coordinate]
    val pp = t.penPaths
    pp.foreach { pl =>
      if (pl.points.size > 1) {
        pl.points.foreach { pt =>
          cab += newCoordinate(pt.x, pt.y)
        }
      }
    }
    if (cab.size == 1) {
      cab += cab(0)
    }
    Gf.createLineString(cab.toArray)
  }

  def hueMod(f: Double) = Utils.runInSwingThread {
    val pp = t.penPaths
    pp.foreach { pl =>
      pl.setPaint(Utils.hueMod(extractFillColor(pl.getPaint), f))
    }
  }

  def satMod(f: Double) = Utils.runInSwingThread {
    val pp = t.penPaths
    pp.foreach { pl =>
      pl.setPaint(Utils.satMod(extractFillColor(pl.getPaint), f))
    }
  }

  def britMod(f: Double) = Utils.runInSwingThread {
    val pp = t.penPaths
    pp.foreach { pl =>
      pl.setPaint(Utils.britMod(extractFillColor(pl.getPaint), f))
    }
  }

  def setPenColor(color: Paint) = Utils.runInSwingThread {
    t.setPenColor(color)
    val pp = t.penPaths
    pp.foreach { pl =>
      pl.setStrokePaint(color)
      pl.repaint()
    }
    val iter = t.tlayer.getChildrenIterator
    while (iter.hasNext) {
      iter.next match {
        case text: PText => text.setTextPaint(color)
        case _           =>
      }
    }
  }

  def setPenThickness(th: Double) = Utils.runInSwingThread {
    t.setPenThickness(th)
    val pp = t.penPaths
    pp.foreach { pl =>
      pl.setStroke(t.lineStroke)
      pl.repaint()
    }
  }

  def setPenCapJoin(cap: Int, join: Int) = Utils.runInSwingThread {
    t.setPenCapJoin(cap, join)
    val pp = t.penPaths
    pp.foreach { pl =>
      pl.setStroke(t.lineStroke)
      pl.repaint()
    }
  }

  def setFillColor(color: Paint) = Utils.runInSwingThread {
    t.setFillColor(color)
    val pp = t.penPaths
    pp.foreach { pl =>
      pl.setPaint(color)
    }
  }

  def copy: Picture = Pic(painter)

  def dumpInfo() = Utils.runInSwingThreadAndPause {
    println(">>> Pic Start - " + System.identityHashCode(this))
    println("Bounds: " + bounds)
    println("Tnode: " + System.identityHashCode(tnode))
    println("Turtle Polylines")
    val pp = t.penPaths
    pp.foreach { pl =>
      println(pl.points)
      println(pl.getPaint)
    }
    println("<<< Pic End\n")
  }

  def morph(fn: Seq[PolyLine] => Seq[PolyLine]) = Utils.runInSwingThread {
    val newPaths = fn(t.penPaths.toSeq)
    if (t.penPaths != newPaths) {
      t.penPaths.foreach { tnode.removeChild }
      t.penPaths.clear()
      t.penPaths ++= newPaths
      t.penPaths.foreach { tnode.addChild }
      _picGeom = null
      tnode.repaint()
    }
  }

  def foreachPolyLine(fn: PolyLine => Unit): Unit = {
    val plines = Utils.runInSwingThreadAndPause { t.penPaths.toArray }
    plines.foreach { fn }
  }
}

object Pic0 {
  def apply(painter: Painter)(implicit canvas: SCanvas) = new Pic0(painter)
}

class Pic0(painter: Painter)(implicit canvas0: SCanvas) extends Pic(painter) {
  override def realDraw(): Unit = {
    try {
      canvas.setDefTurtle(t)
      super.realDraw()
    }
    finally {
      canvas.restoreDefTurtle()
    }
  }
  override def copy: Picture = Pic0(painter)
}

abstract class BasePicList(val pics: List[Picture])
    extends Picture
    with CorePicOps
    with CorePicOps2
    with TNodeCacher
    with RedrawStopper {
  if (pics.isEmpty) {
    throw new IllegalArgumentException("A Picture List needs to have at least one Picture.")
  }
  def canvas = pics.head.canvas
  @volatile var padding = 0.0
  def makeTnode = Utils.runInSwingThreadAndPause {
    val tn = new PNode()
    pics.foreach { pic =>
      tn.addChild(pic.tnode)
    }
    picLayer.addChild(tn)
    tn
  }

  def bounds = Utils.runInSwingThreadAndPause {
    tnode.getFullBounds
  }

  def hueMod(f: Double): Unit = {
    pics.foreach { pic =>
      pic.hueMod(f)
    }
  }

  def satMod(f: Double): Unit = {
    pics.foreach { pic =>
      pic.satMod(f)
    }
  }

  def britMod(f: Double): Unit = {
    pics.foreach { pic =>
      pic.britMod(f)
    }
  }

  def setPenColor(color: Paint): Unit = {
    pics.foreach { pic =>
      pic.setPenColor(color)
    }
  }

  def setPenThickness(th: Double): Unit = {
    pics.foreach { pic =>
      pic.setPenThickness(th)
    }
  }

  def setPenCapJoin(cap: Int, join: Int): Unit = {
    pics.foreach { pic =>
      pic.setPenCapJoin(cap, join)
    }
  }

  def setFillColor(color: Paint): Unit = {
    pics.foreach { pic =>
      pic.setFillColor(color)
    }
  }

  def morph(fn: Seq[PolyLine] => Seq[PolyLine]) = Utils.runInSwingThread {
    pics.foreach { pic =>
      pic.morph(fn)
    }
  }

  def foreachPolyLine(fn: PolyLine => Unit): Unit = {
    pics.foreach { pic =>
      pic.foreachPolyLine(fn)
    }
  }

  def withGap(n: Double): Picture = {
    padding = n
    this
  }

  protected def initGeom() = {
    var pg = pics(0).picGeom
    pics.tail.foreach { pic =>
      pg = pg.union(pic.picGeom)
    }
    pg
  }

  protected def picsCopy: List[Picture] = pics.map { _.copy }

  def dumpInfo(): Unit = {
    println("--- ")
    println("Pic List Bounds: " + bounds)
    println("Pic List Tnode: " + System.identityHashCode(tnode))
    println("--- ")

    pics.foreach { pic =>
      pic.dumpInfo()
    }
  }
}

object HPics {
  def apply(pics: Picture*): HPics = new HPics(freshPics(pics).toList)
  def apply(pics: collection.Seq[Picture]): HPics = new HPics(freshPics(pics).toList)
}

class HPics(pics: List[Picture]) extends BasePicList(pics) {
  def realDraw(): Unit = {
    var prevPic: Option[Picture] = None
    pics.foreach { pic =>
      pic.invisible()
      pic.draw()
      prevPic match {
        case Some(ppic) =>
          val pbounds = ppic.bounds
          val bounds = pic.bounds
          val tx = pbounds.getMaxX - bounds.getMinX
          pic.offset(tx, 0)
        case None =>
      }
      pic.visible()
      prevPic = Some(pic)
    }
  }

  def copy = HPics(picsCopy).withGap(padding)

  override def dumpInfo(): Unit = {
    println(">>> HPics Start - " + System.identityHashCode(this))
    super.dumpInfo()
    println("<<< HPics End\n\n")
  }

  override def toString() = s"Picture Row (Id: ${System.identityHashCode(this)})"
}

object HPics2 {
  def apply(pics: Picture*): HPics2 = new HPics2(freshPics(pics).toList)
  def apply(pics: collection.Seq[Picture]): HPics2 = new HPics2(freshPics(pics).toList)
}

class HPics2(pics: List[Picture]) extends BasePicList(pics) {
  def realDraw(): Unit = {
    var prevPic: Option[Picture] = None
    pics.foreach { pic =>
      pic.invisible()
      pic.draw()
      prevPic match {
        case Some(ppic) =>
          val pbounds = ppic.bounds
          val bounds = pic.bounds
          val tx = pbounds.getMaxX - bounds.getMinX
          val ty = pbounds.getMinY - bounds.getMinY + (pbounds.height - bounds.height) / 2
          pic.offset(tx, ty)
        case None =>
      }
      pic.visible()
      prevPic = Some(pic)
    }
  }

  def copy = HPics2(picsCopy).withGap(padding)

  override def dumpInfo(): Unit = {
    println(">>> HPics2 Start - " + System.identityHashCode(this))
    super.dumpInfo()
    println("<<< HPics2 End\n\n")
  }

  override def toString() = s"Picture Row2 (Id: ${System.identityHashCode(this)})"
}

object VPics {
  def apply(pics: Picture*): VPics = new VPics(freshPics(pics).toList)
  def apply(pics: collection.Seq[Picture]): VPics = new VPics(freshPics(pics).toList)
}

class VPics(pics: List[Picture]) extends BasePicList(pics) {
  def realDraw(): Unit = {
    var prevPic: Option[Picture] = None
    pics.foreach { pic =>
      pic.invisible()
      pic.draw()
      prevPic match {
        case Some(ppic) =>
          val pbounds = ppic.bounds
          val bounds = pic.bounds
          val ty = pbounds.getMaxY - bounds.getMinY
          pic.offset(0, ty)
        case None =>
      }
      pic.visible()
      prevPic = Some(pic)
    }
  }

  def copy = VPics(picsCopy).withGap(padding)

  override def dumpInfo(): Unit = {
    println(">>> VPics Start - " + System.identityHashCode(this))
    super.dumpInfo()
    println("<<< VPics End\n\n")
  }

  override def toString() = s"Picture Column (Id: ${System.identityHashCode(this)})"
}

object VPics2 {
  def apply(pics: Picture*): VPics2 = new VPics2(freshPics(pics).toList)
  def apply(pics: collection.Seq[Picture]): VPics2 = new VPics2(freshPics(pics).toList)
}

class VPics2(pics: List[Picture]) extends BasePicList(pics) {
  def realDraw(): Unit = {
    var prevPic: Option[Picture] = None
    pics.foreach { pic =>
      pic.invisible()
      pic.draw()
      prevPic match {
        case Some(ppic) =>
          val pbounds = ppic.bounds
          val bounds = pic.bounds
          val tx = pbounds.getMinX - bounds.getMinX + (pbounds.width - bounds.width) / 2
          val ty = pbounds.getMaxY - bounds.getMinY
          pic.offset(tx, ty)
        case None =>
      }
      pic.visible()
      prevPic = Some(pic)
    }
  }

  def copy = VPics2(picsCopy).withGap(padding)

  override def dumpInfo(): Unit = {
    println(">>> VPics2 Start - " + System.identityHashCode(this))
    super.dumpInfo()
    println("<<< VPics2 End\n\n")
  }

  override def toString() = s"Picture Column2 (Id: ${System.identityHashCode(this)})"
}

object GPics {
  def apply(pics: Picture*): GPics = new GPics(freshPics(pics).toList)
  def apply(pics: collection.Seq[Picture]): GPics = new GPics(freshPics(pics).toList)
}

class GPics(pics: List[Picture]) extends BasePicList(pics) {
  def realDraw(): Unit = {
    pics.foreach { pic =>
      pic.draw()
    }
  }

  def copy = GPics(picsCopy).withGap(padding)

  override def dumpInfo(): Unit = {
    println(">>> GPics Start - " + System.identityHashCode(this))
    super.dumpInfo()
    println("<<< GPics End\n\n")
  }

  override def toString() = s"Picture Stack (Id: ${System.identityHashCode(this)})"
}

object GPics2 {
  def apply(pics: Picture*): GPics2 = new GPics2(freshPics(pics).toList)
  def apply(pics: collection.Seq[Picture]): GPics2 = new GPics2(freshPics(pics).toList)
}

class GPics2(pics: List[Picture]) extends BasePicList(pics) {
  def realDraw(): Unit = {
    var prevPic: Option[Picture] = None
    pics.foreach { pic =>
      pic.invisible()
      pic.draw()
      prevPic match {
        case Some(ppic) =>
          val pbounds = ppic.bounds
          val bounds = pic.bounds
          val tx = pbounds.getMinX - bounds.getMinX + (pbounds.width - bounds.width) / 2
          val ty = pbounds.getMinY - bounds.getMinY + (pbounds.height - bounds.height) / 2
          pic.offset(tx, ty)
        case None =>
      }
      pic.visible()
      prevPic = Some(pic)
    }
  }

  def copy = GPics2(picsCopy).withGap(padding)

  override def dumpInfo(): Unit = {
    println(">>> GPics2 Start - " + System.identityHashCode(this))
    super.dumpInfo()
    println("<<< GPics2 End\n\n")
  }

  override def toString() = s"Picture Stack2 (Id: ${System.identityHashCode(this)})"
}

object BatchPics {
  def apply(pics: Picture*): BatchPics = new BatchPics(pics.toList)
  def apply(pics: collection.Seq[Picture]): BatchPics = new BatchPics(pics.toList)
}

class BatchPics(pics: List[Picture]) extends BasePicList(pics) {
  def realDraw(): Unit = {
    pics.head.draw()
    pics.tail.foreach { pic =>
      pic.draw()
      pic.invisible()
    }
  }

  var currPicIndex = 0
  var lastDraw = System.currentTimeMillis

  def currentPicture = pics(currPicIndex)

  override def showNext(gap: Long) = Utils.runInSwingThread {
    val currTime = System.currentTimeMillis
    if (currTime - lastDraw > gap) {
      pics(currPicIndex).invisible()
      currPicIndex += 1
      if (currPicIndex == pics.size) {
        currPicIndex = 0
      }
      pics(currPicIndex).visible()
      lastDraw = currTime
    }
  }

  override def picGeom: Geometry = Utils.runInSwingThreadAndWait {
    pgTransform.transform(pics(currPicIndex).picGeom)
  }

  def copy = BatchPics(picsCopy).withGap(padding)

  override def dumpInfo(): Unit = {
    println(">>> BatchPics Start - " + System.identityHashCode(this))
    super.dumpInfo()
    println("<<< BatchPics End\n\n")
  }

  override def toString() = s"Picture Batch (Id: ${System.identityHashCode(this)})"
}

class PicScreen {
  val pics = ArrayBuffer.empty[Picture]
  @volatile var drawn = false
  @volatile var showCmd: Option[() => Unit] = None
  @volatile var hideCmd: Option[() => Unit] = None

  def add(ps: Picture*): Unit = {
    ps.foreach { pics.append(_) }
  }

  def add(ps: collection.Seq[Picture]): Unit = {
    ps.foreach { pics.append(_) }
  }

  private def draw(): Unit = {
    pics.foreach { _.draw() }
  }

  def hide(): Unit = {
    pics.foreach { _.invisible() }
    hideCmd.foreach { c =>
      c()
    }
  }

  private def unhide(): Unit = {
    pics.foreach { _.visible() }
  }

  def show(): Unit = {
    if (!drawn) {
      draw()
      drawn = true
    }
    else {
      unhide()
    }

    showCmd.foreach { c =>
      c()
    }
  }

  def erase(): Unit = {
    pics.foreach { _.erase() }
  }

  def onShow(cmd: => Unit): Unit = {
    showCmd = Some(() => cmd)
  }

  def onHide(cmd: => Unit): Unit = {
    hideCmd = Some(() => cmd)
  }
}
