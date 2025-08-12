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

package net.kogics.kojo.picture

import java.awt._
import java.awt.geom._
import java.net.URL
import javax.swing.event.PopupMenuEvent
import javax.swing.event.PopupMenuListener
import javax.swing.JComboBox
import javax.swing.JComponent
import javax.swing.JPanel

import scala.collection.mutable.ArrayBuffer

import com.vividsolutions.jts.geom.Coordinate
import com.vividsolutions.jts.geom.Geometry
import edu.umd.cs.piccolo.nodes.PImage
import edu.umd.cs.piccolo.nodes.PPath
import edu.umd.cs.piccolo.util.PPaintContext
import edu.umd.cs.piccolo.PNode
import edu.umd.cs.piccolox.nodes.PClip
import edu.umd.cs.piccolox.pswing.PSwing
import net.kogics.kojo.core.Picture
import net.kogics.kojo.core.SCanvas
import net.kogics.kojo.picture.PicCache.freshPic
import net.kogics.kojo.staging.CapJoinConstants._
import net.kogics.kojo.util.Constants
import net.kogics.kojo.util.Utils

trait PicShapeOps { self: Picture with CorePicOps =>
  def realDraw() = Utils.runInSwingThread {
    tnode.setVisible(true)
  }

  def bounds = Utils.runInSwingThreadAndPause {
    tnode.getFullBounds
  }

  def britMod(f: Double) = Utils.runInSwingThread {
    tnode.setPaint(Utils.britMod(extractFillColor(tnode.getPaint), f))
  }

  def hueMod(f: Double) = Utils.runInSwingThread {
    tnode.setPaint(Utils.hueMod(extractFillColor(tnode.getPaint), f))
  }

  def satMod(f: Double) = Utils.runInSwingThread {
    tnode.setPaint(Utils.satMod(extractFillColor(tnode.getPaint), f))
  }

  def setFillColor(color: java.awt.Paint) = Utils.runInSwingThread {
    tnode.setPaint(color)
  }

  def setPenColor(color: java.awt.Paint) = Utils.runInSwingThread {
    _setPenColor(tnode, color)
  }
  protected def _setPenColor(node: PNode, color: java.awt.Paint): Unit = {
    node.asInstanceOf[PPath].setStrokePaint(color)
  }

  private var lineCap: Int = DefaultCap
  private var lineJoin: Int = DefaultJoin

  def setPenThickness(th: Double) = Utils.runInSwingThread {
    _setPenThickness(tnode, th)
  }
  protected def _setPenThickness(node: PNode, th: Double): Unit = {
    val (cap, join) = capJoin(th)
    val stroke = new BasicStroke(th.toFloat, cap, join)
    node.asInstanceOf[PPath].setStroke(stroke)
  }

  override def setNoPen(): Unit = Utils.runInSwingThread {
    val pnode = tnode.asInstanceOf[PPath]
    pnode.setStroke(null)
    pnode.setStrokePaint(null)
    pnode.repaint()
  }

  def setPenCapJoin(cap: Int, join: Int) = Utils.runInSwingThread {
    _setPenCapJoin(tnode, cap, join)
  }

  def _setPenCapJoin(node: PNode, cap: Int, join: Int): Unit = {
    lineCap = cap
    lineJoin = join
    val th = node.asInstanceOf[PPath].getStroke.asInstanceOf[BasicStroke].getLineWidth
    val stroke = new BasicStroke(th.toFloat, cap, join)
    node.asInstanceOf[PPath].setStroke(stroke)
  }

  private def capJoin(t: Double) = {
    val Cap = if (lineCap == DefaultCap) {
      if (t * self.canvas.camScale < 1) CapThin else CapThick
    }
    else {
      lineCap
    }
    val Join = if (lineJoin == DefaultJoin) {
      if (t * self.canvas.camScale < 1) JoinThin else JoinThick
    }
    else {
      lineJoin
    }
    (Cap, Join)
  }

  def morph(fn: Seq[net.kogics.kojo.kgeom.PolyLine] => Seq[net.kogics.kojo.kgeom.PolyLine]) =
    notSupported("morph", "for non-turtle picture")
  def dumpInfo(): Unit = {}
  def foreachPolyLine(fn: net.kogics.kojo.kgeom.PolyLine => Unit) =
    notSupported("foreachPolyLine", "for non-turtle picture")
}

trait NonVectorPicOps { self: Picture with CorePicOps =>
  def realDraw() = Utils.runInSwingThread {
    tnode.setVisible(true)
  }

  def bounds = Utils.runInSwingThreadAndPause {
    tnode.getFullBounds
  }

  def britMod(f: Double) = notSupported("britMod", "for non-vector picture")

  def hueMod(f: Double) = notSupported("hueMod", "for non-vector picture")

  def satMod(f: Double) = notSupported("satMod", "for non-vector picture")

  def setFillColor(color: java.awt.Paint) = notSupported("setFillColor", "for non-vector picture")

  def setPenColor(color: java.awt.Paint) = notSupported("setPenColor", "for non-vector picture")

  def setPenThickness(th: Double) = notSupported("setPenThickness", "for non-vector picture")

  def setPenCapJoin(cap: Int, join: Int) = notSupported("setPenCapJoin", "for non-vector picture")

  def initGeom(): Geometry = notSupported("initGeometry", "for non-vector picture")

  def morph(fn: Seq[net.kogics.kojo.kgeom.PolyLine] => Seq[net.kogics.kojo.kgeom.PolyLine]) =
    notSupported("morph", "for non-vector picture")
  def dumpInfo(): Unit = {}
  def foreachPolyLine(fn: net.kogics.kojo.kgeom.PolyLine => Unit) =
    notSupported("foreachPolyLine", "for non-vector picture")
}

object KPath {
  val TEMP_RECTANGLE = new Rectangle2D.Float()
  val TEMP_ELLIPSE = new Ellipse2D.Float()
  val TEMP_ARC = new Arc2D.Float()
  val TEMP_LINE = new Line2D.Float()

  def createRectangle(x: Float, y: Float, width: Float, height: Float): PPath = {
    TEMP_RECTANGLE.setRect(x, y, width, height)
    new KPath(TEMP_RECTANGLE)
  }

  def createEllipse(x: Float, y: Float, width: Float, height: Float): PPath = {
    TEMP_ELLIPSE.setFrame(x, y, width, height)
    new KPath(TEMP_ELLIPSE)
  }

  def createArc(r: Float, angle: Float): PPath = {
    val d = 2 * r
    TEMP_ARC.setArc(-r, -r, d, d, 0, -angle, Arc2D.OPEN)
    new KPath(TEMP_ARC)
  }

  def createLine(x1: Float, y1: Float, x2: Float, y2: Float): PPath = {
    TEMP_LINE.setLine(x1, y1, x2, y2)
    new KPath(TEMP_LINE)
  }
}

class KPath(s: Shape) extends PPath(s) {
  override def paint(paintContext: PPaintContext): Unit = {
    val g2 = paintContext.getGraphics
    g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE)
    super.paint(paintContext)
  }
}

class CirclePic(r: Double)(implicit val canvas: SCanvas)
    extends Picture
    with CorePicOps
    with CorePicOps2
    with TNodeCacher
    with RedrawStopper
    with PicShapeOps {
  def initGeom(): com.vividsolutions.jts.geom.Geometry = {
    def x(t: Double) = r * math.cos(t.toRadians)
    def y(t: Double) = r * math.sin(t.toRadians)
    def cPoints = for (i <- 1 to 360) yield newCoordinate(x(i), y(i))
    Gf.createLineString(cPoints.toArray)
  }

  def makeTnode: edu.umd.cs.piccolo.PNode = Utils.runInSwingThreadAndPause {
    val fr = r.toFloat
    val d = 2 * fr
    val node = KPath.createEllipse(-fr, -fr, d, d)
    _setPenColor(node, Color.red)
    _setPenThickness(node, 2 / canvas.camScale)
    node.setPaint(null)
    node.setVisible(false)
    picLayer.addChild(node)
    node
  }

  def copy: net.kogics.kojo.core.Picture = new CirclePic(r)
}

class EllipsePic(rx: Double, ry: Double)(implicit val canvas: SCanvas)
    extends Picture
    with CorePicOps
    with CorePicOps2
    with TNodeCacher
    with RedrawStopper
    with PicShapeOps {
  def initGeom(): com.vividsolutions.jts.geom.Geometry = {
    def x(t: Double) = rx * math.cos(t.toRadians)
    def y(t: Double) = ry * math.sin(t.toRadians)
    def cPoints = for (i <- 1 to 360) yield newCoordinate(x(i), y(i))
    Gf.createLineString(cPoints.toArray)
  }

  def makeTnode: edu.umd.cs.piccolo.PNode = Utils.runInSwingThreadAndPause {
    val frx = rx.toFloat
    val fry = ry.toFloat
    val dx = 2 * frx
    val dy = 2 * fry
    val node = KPath.createEllipse(-frx, -fry, dx, dy)
    _setPenColor(node, Color.red)
    _setPenThickness(node, 2 / canvas.camScale)
    node.setPaint(null)
    node.setVisible(false)
    picLayer.addChild(node)
    node
  }

  def copy: net.kogics.kojo.core.Picture = new EllipsePic(rx, ry)
}

class ArcPic(r: Double, angle: Double)(implicit val canvas: SCanvas)
    extends Picture
    with CorePicOps
    with CorePicOps2
    with TNodeCacher
    with RedrawStopper
    with PicShapeOps {
  def initGeom(): com.vividsolutions.jts.geom.Geometry = {
    def x(t: Double) = r * math.cos(t.toRadians)
    def y(t: Double) = r * math.sin(t.toRadians)
    val step = if (angle > 0) 1 else -1
    var cPoints = for (i <- 0 to angle.toInt by step) yield newCoordinate(x(i), y(i))
    if (angle.floor != angle) {
      cPoints = cPoints :+ newCoordinate(x(angle), y(angle))
    }
    Gf.createLineString(cPoints.toArray)
  }

  def makeTnode: edu.umd.cs.piccolo.PNode = Utils.runInSwingThreadAndPause {
    val fr = r.toFloat
    val node = KPath.createArc(fr, angle.toFloat)
    _setPenColor(node, Color.red)
    _setPenThickness(node, 2 / canvas.camScale)
    node.setPaint(null)
    node.setVisible(false)
    picLayer.addChild(node)
    node
  }

  def copy: net.kogics.kojo.core.Picture = new ArcPic(r, angle)
}

class RectanglePic(w: Double, h: Double)(implicit val canvas: SCanvas)
    extends Picture
    with CorePicOps
    with CorePicOps2
    with TNodeCacher
    with RedrawStopper
    with PicShapeOps {
  def initGeom(): com.vividsolutions.jts.geom.Geometry = {
    val cab = new ArrayBuffer[Coordinate]
    cab += newCoordinate(0, 0)
    cab += newCoordinate(0, h)
    cab += newCoordinate(w, h)
    cab += newCoordinate(w, 0)
    cab += newCoordinate(0, 0)
    Gf.createLineString(cab.toArray)
  }

  def makeTnode: edu.umd.cs.piccolo.PNode = Utils.runInSwingThreadAndPause {
    val node = KPath.createRectangle(0, 0, w.toFloat, h.toFloat)
    _setPenColor(node, Color.red)
    _setPenThickness(node, 2 / canvas.camScale)
    node.setPaint(null)
    node.setVisible(false)
    picLayer.addChild(node)
    node
  }

  def copy: net.kogics.kojo.core.Picture = new RectanglePic(w, h)
}

class LinePic(x: Double, y: Double)(implicit val canvas: SCanvas)
    extends Picture
    with CorePicOps
    with CorePicOps2
    with TNodeCacher
    with RedrawStopper
    with PicShapeOps {
  def initGeom(): com.vividsolutions.jts.geom.Geometry = {
    val cab = new ArrayBuffer[Coordinate]
    cab += newCoordinate(0, 0)
    cab += newCoordinate(x, y)
    Gf.createLineString(cab.toArray)
  }

  def makeTnode: edu.umd.cs.piccolo.PNode = Utils.runInSwingThreadAndPause {
    val node = KPath.createLine(0, 0, x.toFloat, y.toFloat)
    _setPenColor(node, Color.red)
    _setPenThickness(node, 2 / canvas.camScale)
    node.setPaint(null)
    node.setVisible(false)
    picLayer.addChild(node)
    node
  }

  def copy: net.kogics.kojo.core.Picture = new LinePic(x, y)
}

class PathPic(pathMaker: => GeneralPath)(implicit val canvas: SCanvas)
    extends Picture
    with CorePicOps
    with CorePicOps2
    with TNodeCacher
    with RedrawStopper
    with PicShapeOps {
  lazy val path = pathMaker
  def initGeom(): com.vividsolutions.jts.geom.Geometry = {
    val cab = new ArrayBuffer[Coordinate]
    val iter = path.getPathIterator(null, 1)
    val pts = new Array[Float](6)
    var prevMoveTo: Option[Point2D.Float] = None
    while (!iter.isDone) {
      iter.currentSegment(pts) match {
        case PathIterator.SEG_MOVETO =>
          prevMoveTo = Some(new Point2D.Float(pts(0), pts(1)))
        case PathIterator.SEG_LINETO =>
          prevMoveTo.foreach { pt =>
            cab += newCoordinate(pt.x, pt.y)
            prevMoveTo = None
          }
          cab += newCoordinate(pts(0), pts(1))
        case unexpected =>
          println(s"Warning: unexpected segment type - $unexpected - in path geometry")
      }
      iter.next()
    }
    if (cab.size == 1) {
      cab += cab(0)
    }
    Gf.createLineString(cab.toArray)
  }

  def makeTnode: edu.umd.cs.piccolo.PNode = Utils.runInSwingThreadAndPause {
    val node = new KPath(path)
    _setPenColor(node, Color.red)
    _setPenThickness(node, 2 / canvas.camScale)
    node.setPaint(null)
    node.setVisible(false)
    picLayer.addChild(node)
    node
  }

  def copy: net.kogics.kojo.core.Picture = new PathPic(path)
}

// a picture that lets you use the full Java2D API for drawing via a Graphics2D
class Java2DPic(w: Double, h: Double, fn: Graphics2D => Unit)(implicit val canvas: SCanvas)
    extends Picture
    with CorePicOps
    with CorePicOps2
    with TNodeCacher
    with RedrawStopper
    with NonVectorPicOps {
  override def initGeom(): com.vividsolutions.jts.geom.Geometry = {
    val cab = new ArrayBuffer[Coordinate]
    cab += newCoordinate(0, 0)
    cab += newCoordinate(0, h)
    cab += newCoordinate(w, h)
    cab += newCoordinate(w, 0)
    cab += newCoordinate(0, 0)
    Gf.createLineString(cab.toArray)
  }

  lazy val (imageWithDrawing, imageG2D) = {
    val graphicsConfiguration =
      GraphicsEnvironment.getLocalGraphicsEnvironment.getDefaultScreenDevice.getDefaultConfiguration
    val buffImg = graphicsConfiguration.createCompatibleImage(w.toInt, h.toInt, Transparency.TRANSLUCENT)
    val gbi = buffImg.createGraphics
    new PPaintContext(gbi).setRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING)
    gbi.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE)
    fn(gbi)
    (buffImg, gbi)
  }

  def makeTnode: edu.umd.cs.piccolo.PNode = Utils.runInSwingThreadAndPause {
    val node = new PNode {
      var drawn = false
      override def paint(paintContext: PPaintContext): Unit = {
        val g2 = paintContext.getGraphics
        g2.drawImage(imageWithDrawing, null, 0, 0)
        drawn = true
      }

      override def setParent(newParent: PNode): Unit = {
        super.setParent(newParent)
        if (newParent == null && drawn) {
          imageG2D.dispose()
        }
      }
    }
    node.setBounds(0, 0, w, h)
    node.setVisible(false)
    picLayer.addChild(node)
    node
  }

  def update(): Unit = {
    fn(imageG2D)
    tnode.repaint()
  }

  def copy: net.kogics.kojo.core.Picture = new Java2DPic(w, h, fn)
}

class ImagePic(img: Image, envelope: Option[Picture])(implicit val canvas: SCanvas)
    extends Picture
    with CorePicOps
    with CorePicOps2
    with TNodeCacher
    with RedrawStopper
    with NonVectorPicOps {

  def makeTnode: edu.umd.cs.piccolo.PNode = Utils.runInSwingThreadAndPause {
    val inode = new PImage(img) {
      override def paint(paintContext: PPaintContext): Unit = {
        if (paintContext.getScale == 1.0) {
          val g2 = paintContext.getGraphics()
          g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR)
        }
        super.paint(paintContext)
      }
    }

    inode.getTransformReference(true).setToScale(1 / canvas.camScale, -1 / canvas.camScale)
    inode.translate(0, -inode.getHeight)

    val node = new PNode
    node.setVisible(false)
    node.addChild(inode)
    picLayer.addChild(node)
    node
  }

  override def initGeom(): com.vividsolutions.jts.geom.Geometry = envelope match {
    case None =>
      val cab = new ArrayBuffer[Coordinate]
      val b = tnode.getFullBounds
      cab += newCoordinate(b.x, b.y)
      cab += newCoordinate(b.x, b.y + b.height)
      cab += newCoordinate(b.x + b.width, b.y + b.height)
      cab += newCoordinate(b.x + b.width, b.y)
      cab += newCoordinate(b.x, b.y)
      pgTransform.getInverse.transform(Gf.createLineString(cab.toArray))
    case Some(p) =>
      val pg = p.picGeom
      //      val cp = Pic { t =>
      //        val coords = pg.getCoordinates
      //        if (!coords.isEmpty) {
      //          val h = coords.head
      //          t.jumpTo(h.x, h.y)
      //          coords.tail.foreach { c =>
      //            t.moveTo(c.x, c.y)
      //          }
      //        }
      //      }
      //      cp.draw()
      //      tnode.addChild(cp.tnode)
      pg
  }

  def copy: net.kogics.kojo.core.Picture = new ImagePic(img, envelope)
  override def toString() = s"ImagePic (Id: ${System.identityHashCode(this)})"
}

class FileImagePic(file: String, envelope: Option[Picture])(implicit canvas: SCanvas)
    extends ImagePic(Utils.loadImage(file), envelope) {
  override def copy: net.kogics.kojo.core.Picture = new FileImagePic(file, envelope)
}

class UrlImagePic(url: URL, envelope: Option[Picture])(implicit canvas: SCanvas)
    extends ImagePic(Utils.loadUrlImageC(url), envelope) {
  override def copy: net.kogics.kojo.core.Picture = new UrlImagePic(url, envelope)
}

class SwingPic(swingComponent: JComponent)(implicit val canvas: SCanvas)
    extends Picture
    with CorePicOps
    with CorePicOps2
    with TNodeCacher
    with RedrawStopper
    with NonVectorPicOps {

  def pswingHook(ps: PSwing): Unit = {}

  def makeTnode: edu.umd.cs.piccolo.PNode = Utils.runInSwingThreadAndPause {
    val pswing = new PSwing(swingComponent)
    def handleCombo(combo: JComboBox[AnyRef]): Unit = {
      combo.addItem(Constants.DropDownCanvasPadding)
      combo.addPopupMenuListener(new PopupMenuListener {
        def popupMenuWillBecomeVisible(e: PopupMenuEvent): Unit = {
          combo.setBounds(getNodeBoundsInCanvas(pswing, combo))
          if (insidePanel(combo)) {
            combo.revalidate()
          }
        }
        def popupMenuWillBecomeInvisible(e: PopupMenuEvent): Unit = {}
        def popupMenuCanceled(e: PopupMenuEvent): Unit = {}
      })
    }
    def handleComponent(comp: Component): Unit = {
      comp match {
        case combo: JComboBox[_] => handleCombo(combo.asInstanceOf[JComboBox[AnyRef]])
        case jp: JPanel          => jp.getComponents.foreach { handleComponent }
        case _                   =>
      }
    }
    handleComponent(swingComponent)
    pswing.getTransformReference(true).setToScale(1 / canvas.camScale, -1 / canvas.camScale)
    pswing.translate(0, -pswing.getHeight)

    val node = new PNode
    node.addChild(pswing)
    picLayer.addChild(node)
    node
  }

  def insidePanel(c: JComponent) = c.getParent.isInstanceOf[JPanel]
  def panelOffset(c: Component): (Int, Int) = {
    var parent = c.getParent
    val ret = if (parent != null && parent.isInstanceOf[JPanel]) {
      val po = panelOffset(parent)
      (c.getX + po._1, c.getY + po._2)
    }
    else {
      (0, 0)
    }
    println(s"Panel offset of $c is $ret")
    ret
  }

  private def getNodeBoundsInCanvas(pSwing: PSwing, combo: JComboBox[_]) = {
    val (deltax, deltay) = if (insidePanel(combo)) (combo.getX, combo.getY) else (0, 0)
    val r1c = new Rectangle2D.Double(pSwing.getX + deltax, pSwing.getY + deltay, combo.getWidth, combo.getHeight)
    pSwing.localToGlobal(r1c)
    canvas.getCamera.viewToLocal(r1c)
    r1c.getBounds
  }

  def copy = notSupported("copy", "for Swing picture")
  override def toString() = s"SwingPic (Id: ${System.identityHashCode(this)})"
}

class TextPic(text: String, size: Int, color: Color)(implicit val canvas: SCanvas)
    extends Picture
    with CorePicOps
    with CorePicOps2
    with TNodeCacher
    with RedrawStopper
    with PicShapeOps {

  def initGeom(): Geometry = notSupported("initGeometry", "for text picture")
  val ptext = Utils.textNode(text, 0, 0, canvas.camScale, size)
  ptext.setTextPaint(color)
  ptext.setPaint(null)

  def makeTnode: edu.umd.cs.piccolo.PNode = Utils.runInSwingThreadAndPause {
    val node = new PNode
    node.setVisible(false)
    node.addChild(ptext)
    picLayer.addChild(node)
    node
  }

  override def setPenColor(color: java.awt.Paint) = Utils.runInSwingThread {
    ptext.setTextPaint(color)
  }

  override def setPenThickness(th: Double) = notSupported("setPenThickness", "for text picture")

  def setText(t: String) = Utils.runInSwingThread {
    ptext.setText(t)
  }

  def setPenFont(f: Font) = Utils.runInSwingThreadAndWait {
    ptext.setFont(f)
  }

  def setPenFontSize(n: Int) = Utils.runInSwingThread {
    val f = ptext.getFont
    ptext.setFont(f.deriveFont(n.toFloat))
  }

  override def update(newData: Any) = Utils.runInSwingThread {
    ptext.setText(newData.toString)
  }

  def copy: net.kogics.kojo.core.Picture = {
    val tp = new TextPic(text, size, color)
    // currently account for only font and blOrigin mutations
    tp.setPenFont(ptext.getFont)
    if (blOrigin) {
      tp.originBottomLeft()
    }
    tp
  }

  @volatile var blOrigin = false
  def originBottomLeft(): Unit = Utils.runInSwingThreadAndWait {
    blOrigin = true
    val b = ptext.getBounds
    ptext.translate(0, -b.height)
    if (isDrawn) {
      tnode.repaint()
    }
  }

  def withBottomLeftOrigin: Picture = {
    val ret = this.copy.asInstanceOf[TextPic]
    ret.originBottomLeft()
    ret
  }

  override def toString() = s"TextPic (Id: ${System.identityHashCode(this)})"
}

class KClip extends PClip {
  override def paintAfterChildren(paintContext: PPaintContext): Unit = {
    val g2 = paintContext.getGraphics
    g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE)
    super.paintAfterChildren(paintContext)
  }
}

class ClipPic(pic0: Picture, clipShape: Shape)(implicit val canvas: SCanvas)
    extends Picture
    with CorePicOps
    with CorePicOps2
    with TNodeCacher
    with RedrawStopper
    with PicShapeOps {

  val pic = freshPic(pic0)

  def initGeom(): com.vividsolutions.jts.geom.Geometry = {
    throw new RuntimeException("Clip pic does not yet support geometry")
  }

  def makeTnode: edu.umd.cs.piccolo.PNode = Utils.runInSwingThreadAndPause {
    val node = new KClip()
    node.append(clipShape, false)
    _setPenColor(node, null)
    _setPenThickness(node, 0)
    node.setPaint(null)
    node.addChild(pic.tnode)
    node.setVisible(false)
    picLayer.addChild(node)
    node
  }

  override def realDraw(): Unit = {
    pic.draw()
    super.realDraw()
  }

  def copy: net.kogics.kojo.core.Picture = new ClipPic(pic.copy, clipShape)
}

class ClipPicWithPic(pic: Picture, clipPic: Picture)(implicit canvas2: SCanvas)
    extends ClipPic(pic, toShape(freshPic(clipPic)))
