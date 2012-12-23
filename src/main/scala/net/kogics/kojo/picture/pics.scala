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

import core.Picture
import core.Pixel
import core.UnitLen
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Paint
import java.awt.geom.AffineTransform
import scala.collection.mutable.ArrayBuffer
import kgeom.PolyLine
import util.Utils
import util.Math
import util.Vector2D
import edu.umd.cs.piccolo.PNode
import edu.umd.cs.piccolo.nodes.PPath
import edu.umd.cs.piccolo.util.PBounds
import com.vividsolutions.jts.geom._
import com.vividsolutions.jts.geom.util.AffineTransformation
import net.kogics.kojo.lite.canvas.SpriteCanvas
import net.kogics.kojo.core.Pixel
import net.kogics.kojo.core.Inch
import net.kogics.kojo.core.Cm

object Impl {
  val canvas = SpriteCanvas.instance
  val camera = canvas.getCamera
  val picLayer = canvas.pictures
  val Gf = new GeometryFactory
}

trait CorePicOps { self: Picture with RedrawStopper =>
  var axes: PNode = _
  var _picGeom: Geometry = _
  var pgTransform = new AffineTransformation

  def realDraw(): Unit

  def draw() {
    realDraw()
  }

  def erase() = Utils.runInSwingThread {
    Impl.picLayer.removeChild(tnode)
    //    Impl.picLayer.repaint()
  }

  def t2t(t: AffineTransform): AffineTransformation = {
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
    pgTransform = t2t(tnode.getTransformReference(true))
    tnode.repaint()
  }

  def rotateAboutPoint(angle: Double, x: Double, y: Double) = {
    translate(x, y)
    rotate(angle)
    translate(-x, -y)
  }

  def rotate(angle: Double) = {
    transformBy(AffineTransform.getRotateInstance(angle.toRadians))
    this
  }

  def scale(factor: Double) = {
    transformBy(AffineTransform.getScaleInstance(factor, factor))
    this
  }

  def scale(x: Double, y: Double) = {
    transformBy(AffineTransform.getScaleInstance(x, y))
    this
  }

  def translate(x: Double, y: Double) = {
    transformBy(AffineTransform.getTranslateInstance(x, y))
    this
  }

  def offset(x: Double, y: Double) = Utils.runInSwingThread {
    tnode.offset(x, y)
    pgTransform = t2t(tnode.getTransformReference(true))
    tnode.repaint()
  }

  def opacityMod(f: Double) = Utils.runInSwingThread {
    tnode.setTransparency(Math.constrain(tnode.getTransparency * (1 + f), 0, 1).toFloat)
    tnode.repaint()
  }

  def position = Utils.runInSwingThreadAndPause {
    val o = tnode.getOffset
    new core.Point(o.getX, o.getY)
  }

  def setPosition(x: Double, y: Double) = Utils.runInSwingThread {
    tnode.setOffset(x, y)
    pgTransform = t2t(tnode.getTransformReference(true))
    tnode.repaint()
  }

  def heading = Utils.runInSwingThreadAndPause {
    tnode.getRotation.toDegrees
  }

  def setHeading(angle: Double) = Utils.runInSwingThread {
    rotate(angle - heading)
    pgTransform = t2t(tnode.getTransformReference(true))
    tnode.repaint()
  }

  def flipX() = {
    transformBy(AffineTransform.getScaleInstance(1, -1))
    this
  }

  def flipY() = {
    transformBy(AffineTransform.getScaleInstance(-1, 1))
    this
  }

  def axesOn() = Utils.runInSwingThread {
    if (axes == null) {
      val (size, delta, num, bigt) = Impl.canvas.unitLen match {
        case Pixel => (200.0f, 20.0f, 10, 5)
        case Inch => (4.0f, 0.25f, 16, 4)
        case Cm => (10f, .5f, 20, 2)
      }
      val camScale = Impl.canvas.camScale.toFloat
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
      tnode.repaint()
    }
  }

  def invisible() = Utils.runInSwingThread {
    if (tnode.getVisible) {
      tnode.setVisible(false)
      tnode.repaint()
    }
  }

  def toggleV() = Utils.runInSwingThread {
    if (tnode.getVisible) {
      tnode.setVisible(false)
    }
    else {
      tnode.setVisible(true)
    }
    tnode.repaint()
  }
  
  def isVisible() = Utils.runInSwingThreadAndPause { tnode.getVisible() }

  def initGeom(): Geometry
  def picGeom: Geometry = {
    if (!drawn) {
      throw new IllegalStateException("Access geometry after it is drawn.")
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
    pgTransform.transform(_picGeom)
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
      Impl.Gf.createGeometryCollection(null)
    }
    else if (tnode.getVisible && other.tnode.getVisible) {
      try {
        picGeom.intersection(other.picGeom)
      }
      catch {
        case te: TopologyException =>
          println("Unable to determine intersection - " + te.getMessage())
          Impl.Gf.createGeometryCollection(null)
      }
    }
    else {
      Impl.Gf.createGeometryCollection(null)
    }
  }

  def distanceTo(other: Picture) = Utils.runInSwingThreadAndPause {
    picGeom.distance(other.picGeom)
  }

  def toPolygon(g: Geometry) = {
    val gc = g.getCoordinates
    val ab = new ArrayBuffer[Coordinate]
    ab ++= gc
    ab += gc(0)
    Impl.Gf.createPolygon(Impl.Gf.createLinearRing(ab.toArray), null)
  }

  def area = Utils.runInSwingThreadAndPause {
    toPolygon(picGeom).getArea
  }

  def perimeter = Utils.runInSwingThreadAndPause {
    picGeom.getLength
  }

  def myCanvas = Impl.canvas
}

trait CorePicOps2 { self: Picture =>
  // TODO: need to get intersection methods in here too
  // they follow same pattern with respect to transforms
  def act(fn: Picture => Unit) {
    if (!isDrawn) {
      throw new IllegalStateException("Ask picture to act after you draw it.")
    }
    staging.API.loop {
      fn(this)
    }
  }
}

trait RedrawStopper extends Picture {
  @volatile var drawn = false
  def isDrawn = drawn
  abstract override def draw() {
    if (drawn) {
      throw new RuntimeException("You can't redraw a picture")
    }
    else {
      drawn = true
      super.draw()
    }
  }
}

trait TNodeCacher {
  def makeTnode: PNode
  @volatile var _tnode: PNode = _
  def tnode = {
    if (_tnode == null) {
      _tnode = makeTnode
    }
    _tnode
  }
}

object Pic {
  def apply(painter: Painter) = new Pic(painter)
}

class Pic(painter: Painter) extends Picture with CorePicOps with CorePicOps2 with TNodeCacher with RedrawStopper {
  @volatile var _t: turtle.Turtle = _
  val ErrMsg = "Unable to create picture turtle. This could be because you have a draw() call after an animate{ } or morph{ } call"

  def t = {
    if (_t == null) Utils.runInSwingThreadAndWait(1000, ErrMsg) {
      if (_t == null) {
        val tt = Impl.canvas.newInvisibleTurtle(0, 0)
        tt.setAnimationDelay(0)
        val tl = tt.tlayer
        Impl.camera.removeLayer(tl)
        Impl.picLayer.addChild(tl)
        tl.repaint()
        Impl.picLayer.repaint()
        _t = tt
      }
      else {
        _t
      }
    }
    _t
  }

  def makeTnode = t.tlayer

  def decorateWith(painter: Painter) = painter(t)
  def realDraw() {
    painter(t)
    Utils.runInSwingThread {
      val tl = tnode
      tl.invalidateFullBounds()
      tl.repaint()
      Impl.picLayer.repaint
    }
  }

  def bounds = Utils.runInSwingThreadAndPause {
    tnode.getFullBounds
  }

  def initGeom() = {
    val cab = new ArrayBuffer[Coordinate]
    val pp = t.penPaths
    pp.foreach { pl =>
      pl.points.foreach { pt =>
        cab += new Coordinate(pt.x, pt.y)
      }
    }
    if (cab.size == 1) {
      cab += cab(0)
    }
    Impl.Gf.createLineString(cab.toArray)
  }

  private def fillColor(fillPaint: Paint) = fillPaint match {
    case null => Color.white
    case c: Color => c
    case _ => throw new IllegalStateException("You can't extract rgb values of non Color paints")
  }

  def hueMod(f: Double) = Utils.runInSwingThread {
    val pp = t.penPaths
    pp.foreach { pl =>
      pl.setPaint(Utils.hueMod(fillColor(pl.getPaint), f))
      pl.repaint()
    }
  }

  def satMod(f: Double) = Utils.runInSwingThread {
    val pp = t.penPaths
    pp.foreach { pl =>
      pl.setPaint(Utils.satMod(fillColor(pl.getPaint), f))
      pl.repaint()
    }
  }

  def britMod(f: Double) = Utils.runInSwingThread {
    val pp = t.penPaths
    pp.foreach { pl =>
      pl.setPaint(Utils.britMod(fillColor(pl.getPaint), f))
      pl.repaint()
    }
  }

  def setPenColor(color: Color) = Utils.runInSwingThread {
    val pp = t.penPaths
    pp.foreach { pl =>
      pl.setStrokePaint(color)
      pl.repaint()
    }
  }

  def setPenThickness(th: Double) = Utils.runInSwingThread {
    val pp = t.penPaths
    t.setPenThickness(th)
    pp.foreach { pl =>
      pl.setStroke(t.lineStroke)
      pl.repaint()
    }
  }

  def setFillColor(color: Paint) = Utils.runInSwingThread {
    val pp = t.penPaths
    pp.foreach { pl =>
      pl.setPaint(color)
      pl.repaint()
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
    val newPaths = fn(t.penPaths)
    if (t.penPaths != newPaths) {
      t.penPaths.foreach { tnode.removeChild }
      t.penPaths.clear()
      t.penPaths ++= newPaths
      t.penPaths.foreach { tnode.addChild }
      _picGeom = null
      tnode.repaint()
    }
  }

  def foreachPolyLine(fn: PolyLine => Unit) {
    val plines = Utils.runInSwingThreadAndPause { t.penPaths.toArray }
    plines.foreach { fn }
  }
}

object Pic0 {
  def apply(painter: Painter) = new Pic0(painter)
}

class Pic0(painter: Painter) extends Pic(painter) {
  override def realDraw() {
    try {
      Impl.canvas.setDefTurtle(t)
      super.realDraw()
    }
    finally {
      Impl.canvas.restoreDefTurtle()
    }
  }
  override def copy: Picture = Pic0(painter)
}

abstract class BasePicList(val pics: List[Picture])
  extends Picture with CorePicOps with CorePicOps2 with TNodeCacher with RedrawStopper {
  if (pics.size == 0) {
    throw new IllegalArgumentException("A Picture List needs to have at least one Picture.")
  }
  @volatile var padding = 0.0
  def makeTnode = Utils.runInSwingThreadAndPause {
    val tn = new PNode()
    pics.foreach { pic =>
      Impl.picLayer.removeChild(pic.tnode)
      tn.addChild(pic.tnode)
    }
    Impl.picLayer.addChild(tn)
    tn
  }

  def bounds = Utils.runInSwingThreadAndPause {
    tnode.getFullBounds
  }

  def decorateWith(painter: Painter) {
    pics.foreach { pic =>
      pic.decorateWith(painter)
    }
  }

  def hueMod(f: Double) {
    pics.foreach { pic =>
      pic.hueMod(f)
    }
  }

  def satMod(f: Double) {
    pics.foreach { pic =>
      pic.satMod(f)
    }
  }

  def britMod(f: Double) {
    pics.foreach { pic =>
      pic.britMod(f)
    }
  }

  def setPenColor(color: Color) {
    pics.foreach { pic =>
      pic.setPenColor(color)
    }
  }

  def setPenThickness(th: Double) {
    pics.foreach { pic =>
      pic.setPenThickness(th)
    }
  }

  def setFillColor(color: Paint) {
    pics.foreach { pic =>
      pic.setFillColor(color)
    }
  }

  def morph(fn: Seq[PolyLine] => Seq[PolyLine]) = Utils.runInSwingThread {
    pics.foreach { pic =>
      pic.morph(fn)
    }
  }

  def foreachPolyLine(fn: PolyLine => Unit) {
    pics.foreach { pic =>
      pic.foreachPolyLine(fn)
    }
  }

  def withGap(n: Double): Picture = {
    padding = n
    this
  }

  def initGeom() = {
    var pg = pics(0).picGeom
    pics.tail.foreach { pic =>
      pg = pg union pic.picGeom
    }
    pg
  }

  protected def picsCopy: List[Picture] = pics.map { _ copy }

  def dumpInfo() {
    println("--- ")
    println("Pic List Bounds: " + bounds)
    println("Pic List Tnode: " + System.identityHashCode(tnode))
    println("--- ")

    pics.foreach { pic =>
      pic.dumpInfo
    }
  }
}

object HPics {
  def apply(pics: Picture*): HPics = new HPics(pics.toList)
  def apply(pics: List[Picture]): HPics = new HPics(pics)
}

class HPics(pics: List[Picture]) extends BasePicList(pics) {
  def realDraw() {
    var ox = 0.0
    pics.foreach { pic =>
      pic.translate(ox, 0)
      pic.draw()
      val nbounds = pic.bounds
      ox = nbounds.getMinX + nbounds.getWidth + padding
    }
  }

  def copy = HPics(picsCopy).withGap(padding)

  override def dumpInfo() {
    println(">>> HPics Start - " + System.identityHashCode(this))
    super.dumpInfo()
    println("<<< HPics End\n\n")
  }
}

object VPics {
  def apply(pics: Picture*): VPics = new VPics(pics.toList)
  def apply(pics: List[Picture]): VPics = new VPics(pics)
}

class VPics(pics: List[Picture]) extends BasePicList(pics) {
  def realDraw() {
    var oy = 0.0
    pics.foreach { pic =>
      pic.translate(0, oy)
      pic.draw()
      val nbounds = pic.bounds
      oy = nbounds.getMinY + nbounds.getHeight + padding
    }
  }

  def copy = VPics(picsCopy).withGap(padding)

  override def dumpInfo() {
    println(">>> VPics Start - " + System.identityHashCode(this))
    super.dumpInfo()
    println("<<< VPics End\n\n")
  }
}

object GPics {
  def apply(pics: Picture*): GPics = new GPics(pics.toList)
  def apply(pics: List[Picture]): GPics = new GPics(pics)
}

class GPics(pics: List[Picture]) extends BasePicList(pics) {
  def realDraw() {
    pics.foreach { pic =>
      pic.draw()
    }
  }

  def copy = GPics(picsCopy).withGap(padding)

  override def dumpInfo() {
    println(">>> GPics Start - " + System.identityHashCode(this))
    super.dumpInfo()
    println("<<< GPics End\n\n")
  }
}
