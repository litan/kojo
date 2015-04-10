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

import java.awt.BasicStroke
import java.awt.Color
import java.awt.Component
import java.awt.Image
import java.awt.RenderingHints
import java.awt.geom.Arc2D
import java.awt.geom.Rectangle2D

import javax.swing.JComboBox
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.event.PopupMenuEvent
import javax.swing.event.PopupMenuListener

import scala.collection.mutable.ArrayBuffer

import com.vividsolutions.jts.geom.Coordinate
import com.vividsolutions.jts.geom.Geometry

import net.kogics.kojo.core.Picture
import net.kogics.kojo.core.SCanvas
import net.kogics.kojo.util.Constants
import net.kogics.kojo.util.Utils

import edu.umd.cs.piccolo.PNode
import edu.umd.cs.piccolo.nodes.PImage
import edu.umd.cs.piccolo.nodes.PPath
import edu.umd.cs.piccolo.nodes.PText
import edu.umd.cs.piccolo.util.PPaintContext
import edu.umd.cs.piccolox.pswing.PSwing

trait PicShapeOps { self: Picture with CorePicOps =>
  def realDraw() = Utils.runInSwingThread {
    tnode.setVisible(true)
  }

  def bounds = Utils.runInSwingThreadAndPause {
    tnode.getFullBounds
  }

  def decorateWith(painter: Painter) {
    painter(null)
  }

  def britMod(f: Double) = Utils.runInSwingThread {
    tnode.setPaint(Utils.britMod(fillColor(tnode.getPaint), f))
  }

  def hueMod(f: Double) = Utils.runInSwingThread {
    tnode.setPaint(Utils.hueMod(fillColor(tnode.getPaint), f))
  }

  def satMod(f: Double) = Utils.runInSwingThread {
    tnode.setPaint(Utils.satMod(fillColor(tnode.getPaint), f))
  }

  def setFillColor(color: java.awt.Paint) = Utils.runInSwingThread {
    tnode.setPaint(color)
  }

  def setPenColor(color: java.awt.Paint) = Utils.runInSwingThread {
    _setPenColor(tnode, color)
  }
  protected def _setPenColor(node: PNode, color: java.awt.Paint) {
    node.asInstanceOf[PPath].setStrokePaint(color)
  }

  def setPenThickness(th: Double) = Utils.runInSwingThread {
    _setPenThickness(tnode, th)
  }
  protected def _setPenThickness(node: PNode, th: Double) {
    val stroke = new BasicStroke(th.toFloat, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)
    node.asInstanceOf[PPath].setStroke(stroke)
  }

  def morph(fn: Seq[net.kogics.kojo.kgeom.PolyLine] => Seq[net.kogics.kojo.kgeom.PolyLine]) = notSupported("morph", "for non-turtle picture")
  def dumpInfo() {}
  def foreachPolyLine(fn: net.kogics.kojo.kgeom.PolyLine => Unit) = notSupported("foreachPolyLine", "for non-turtle picture")
}

trait NonVectorPicOps { self: Picture with CorePicOps =>
  def realDraw() = Utils.runInSwingThread {
    tnode.setVisible(true)
  }

  def bounds = Utils.runInSwingThreadAndPause {
    tnode.getFullBounds
  }

  def decorateWith(painter: Painter) = notSupported("decorateWith", "for non-vector picture")

  def britMod(f: Double) = notSupported("britMod", "for non-vector picture")

  def hueMod(f: Double) = notSupported("hueMod", "for non-vector picture")

  def satMod(f: Double) = notSupported("satMod", "for non-vector picture")

  def setFillColor(color: java.awt.Paint) = notSupported("setFillColor", "for non-vector picture")

  def setPenColor(color: java.awt.Paint) = notSupported("setPenColor", "for non-vector picture")

  def setPenThickness(th: Double) = notSupported("setPenThickness", "for non-vector picture")

  def initGeom(): Geometry = notSupported("initGeometry", "for non-vector picture")

  def morph(fn: Seq[net.kogics.kojo.kgeom.PolyLine] => Seq[net.kogics.kojo.kgeom.PolyLine]) = notSupported("morph", "for non-vector picture")
  def dumpInfo() {}
  def foreachPolyLine(fn: net.kogics.kojo.kgeom.PolyLine => Unit) = notSupported("foreachPolyLine", "for non-vector picture")
}

class CirclePic(r: Double)(implicit val canvas: SCanvas) extends Picture with CorePicOps with CorePicOps2
  with TNodeCacher with RedrawStopper with PicShapeOps {
  def initGeom(): com.vividsolutions.jts.geom.Geometry = {
    def x(t: Double) = r * math.cos(t.toRadians)
    def y(t: Double) = r * math.sin(t.toRadians)
    def cPoints = for (i <- 1 to 360) yield newCoordinate(x(i), y(i))
    Gf.createLineString(cPoints.toArray)
  }

  def makeTnode: edu.umd.cs.piccolo.PNode = Utils.runInSwingThreadAndPause {
    val fr = r.toFloat
    val d = 2 * fr
    val node = PPath.createEllipse(-fr, -fr, d, d)
    _setPenColor(node, Color.red)
    _setPenThickness(node, 2 / canvas.camScale)
    node.setPaint(null)
    node.setVisible(false)
    picLayer.addChild(node)
    node
  }

  def copy: net.kogics.kojo.core.Picture = new CirclePic(r)
}

class ArcPic(r: Double, angle: Double)(implicit val canvas: SCanvas) extends Picture with CorePicOps with CorePicOps2
  with TNodeCacher with RedrawStopper with PicShapeOps {
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
    val d = 2 * fr
    val node = new PPath
    node.setPathTo(new java.awt.geom.Arc2D.Float(-fr, -fr, d, d, 0, -angle.toFloat, Arc2D.OPEN))
    _setPenColor(node, Color.red)
    _setPenThickness(node, 2 / canvas.camScale)
    node.setPaint(null)
    node.setVisible(false)
    picLayer.addChild(node)
    node
  }

  def copy: net.kogics.kojo.core.Picture = new ArcPic(r, angle)
}

class ImagePic(img: Image, envelope: Option[Picture])(implicit val canvas: SCanvas) extends Picture with CorePicOps with CorePicOps2
  with TNodeCacher with RedrawStopper with NonVectorPicOps {

  def makeTnode: edu.umd.cs.piccolo.PNode = Utils.runInSwingThreadAndPause {
    val inode = new PImage(img) {
      override def paint(paintContext: PPaintContext) {
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
  extends ImagePic(Utils.loadImageC(file), envelope) {
  override def copy: net.kogics.kojo.core.Picture = new FileImagePic(file, envelope)
}

class SwingPic(swingComponent: JComponent)(implicit val canvas: SCanvas) extends Picture with CorePicOps with CorePicOps2
  with TNodeCacher with RedrawStopper with NonVectorPicOps {

  def pswingHook(ps: PSwing) {}

  def makeTnode: edu.umd.cs.piccolo.PNode = Utils.runInSwingThreadAndPause {
    val pswing = new PSwing(swingComponent)
    def handleCombo(combo: JComboBox) {
      combo.addItem(Constants.DropDownCanvasPadding)
      combo.addPopupMenuListener(new PopupMenuListener {
        def popupMenuWillBecomeVisible(e: PopupMenuEvent) {
          combo.setBounds(getNodeBoundsInCanvas(pswing, combo))
          if (insidePanel(combo)) {
            combo.revalidate()
          }
        }
        def popupMenuWillBecomeInvisible(e: PopupMenuEvent) {}
        def popupMenuCanceled(e: PopupMenuEvent) {}
      })
    }
    def handleComponent(comp: Component) {
      comp match {
        case combo: JComboBox => handleCombo(combo)
        case jp: JPanel       => jp.getComponents foreach { handleComponent }
        case _                =>
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

  private def getNodeBoundsInCanvas(pSwing: PSwing, combo: JComboBox) = {
    val (deltax, deltay) = if (insidePanel(combo)) (combo.getX, combo.getY) else (0, 0)
    val r1c = new Rectangle2D.Double(pSwing.getX + deltax, pSwing.getY + deltay, combo.getWidth, combo.getHeight)
    pSwing.localToGlobal(r1c)
    canvas.getCamera.viewToLocal(r1c)
    r1c.getBounds
  }

  def copy = notSupported("copy", "for Swing picture")
  override def toString() = s"SwingPic (Id: ${System.identityHashCode(this)})"
}

class TextPic(text: String, size: Int, color: Color)(implicit val canvas: SCanvas) extends Picture with CorePicOps with CorePicOps2
  with TNodeCacher with RedrawStopper with PicShapeOps {

  def initGeom(): Geometry = notSupported("initGeometry", "for text picture")
  var ptext: PText = _

  def makeTnode: edu.umd.cs.piccolo.PNode = Utils.runInSwingThreadAndPause {
    ptext = Utils.textNode(text, 0, 0, canvas.camScale, size)
    ptext.setTextPaint(color)
    ptext.setPaint(null)

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

  override def update(newData: Any) = Utils.runInSwingThread {
    ptext.setText(newData.toString)
  }

  def copy: net.kogics.kojo.core.Picture = new TextPic(text, size, color)
  override def toString() = s"TextPic (Id: ${System.identityHashCode(this)})"
}
