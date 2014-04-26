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

import com.vividsolutions.jts.geom.Coordinate

import net.kogics.kojo.core.Picture
import net.kogics.kojo.core.SCanvas
import net.kogics.kojo.util.Utils

import edu.umd.cs.piccolo.PNode
import edu.umd.cs.piccolo.nodes.PImage
import edu.umd.cs.piccolo.nodes.PPath
import edu.umd.cs.piccolo.util.PPaintContext
import edu.umd.cs.piccolox.pswing.PSwing

trait UnsupportedOps {
  def notSupported(name: String) = throw new UnsupportedOperationException(s"$name - operation not available for non-vector picture:\n${toString}") 
}

trait PicShapeOps extends UnsupportedOps { self: Picture with CorePicOps =>
  def realDraw() = Utils.runInSwingThread {
    tnode.setVisible(true)
    tnode.repaint()
  }

  def bounds = Utils.runInSwingThreadAndPause {
    tnode.getFullBounds
  }

  def decorateWith(painter: Painter) {
    painter(null)
  }

  def britMod(f: Double) = Utils.runInSwingThread {
    tnode.setPaint(Utils.britMod(fillColor(tnode.getPaint), f))
    tnode.repaint()
  }

  def hueMod(f: Double) = Utils.runInSwingThread {
    tnode.setPaint(Utils.hueMod(fillColor(tnode.getPaint), f))
    tnode.repaint()
  }

  def satMod(f: Double) = Utils.runInSwingThread {
    tnode.setPaint(Utils.satMod(fillColor(tnode.getPaint), f))
    tnode.repaint()
  }

  def setFillColor(color: java.awt.Paint) = Utils.runInSwingThread {
    tnode.setPaint(color)
    tnode.repaint()
  }

  def setPenColor(color: java.awt.Paint) = Utils.runInSwingThread {
    _setPenColor(tnode, color)
    tnode.repaint()
  }
  protected def _setPenColor(node: PNode, color: java.awt.Paint) {
    node.asInstanceOf[PPath].setStrokePaint(color)
  }

  def setPenThickness(th: Double) = Utils.runInSwingThread {
    _setPenThickness(tnode, th)
    tnode.repaint()
  }
  protected def _setPenThickness(node: PNode, th: Double) {
    val stroke = new BasicStroke(th.toFloat, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)
    node.asInstanceOf[PPath].setStroke(stroke)
  }

  def morph(fn: Seq[net.kogics.kojo.kgeom.PolyLine] => Seq[net.kogics.kojo.kgeom.PolyLine])  = notSupported("morph")
  def dumpInfo() {}
  def foreachPolyLine(fn: net.kogics.kojo.kgeom.PolyLine => Unit) = notSupported("foreachPolyLine")
}

trait NonVectorPicOps extends UnsupportedOps { self: Picture with CorePicOps =>
  def realDraw() = Utils.runInSwingThread {
    tnode.setVisible(true)
    tnode.repaint()
  }

  def bounds = Utils.runInSwingThreadAndPause {
    tnode.getFullBounds
  }
  
  def decorateWith(painter: Painter) = notSupported("decorateWith")

  def britMod(f: Double) = notSupported("britMod")

  def hueMod(f: Double) = notSupported("hueMod")

  def satMod(f: Double) = notSupported("satMod")

  def setFillColor(color: java.awt.Paint) = notSupported("setFillColor")

  def setPenColor(color: java.awt.Paint) = notSupported("setPenColor")

  def setPenThickness(th: Double) = notSupported("setPenThickness")

  def initGeom(): com.vividsolutions.jts.geom.Geometry = notSupported("initGeometry")
  
  def morph(fn: Seq[net.kogics.kojo.kgeom.PolyLine] => Seq[net.kogics.kojo.kgeom.PolyLine])  = notSupported("morph")
  def dumpInfo() {}
  def foreachPolyLine(fn: net.kogics.kojo.kgeom.PolyLine => Unit) = notSupported("foreachPolyLine")
}

class CirclePic(r: Double)(implicit val canvas: SCanvas) extends Picture with CorePicOps with CorePicOps2
  with TNodeCacher with RedrawStopper with PicShapeOps {
  def initGeom(): com.vividsolutions.jts.geom.Geometry = {
    def x(t: Double) = r * math.cos(t.toRadians)
    def y(t: Double) = r * math.sin(t.toRadians)
    def cPoints = for (i <- 1 to 360) yield new Coordinate(x(i), y(i))
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
    var cPoints = for (i <- 0 to angle.toInt by step) yield new Coordinate(x(i), y(i))
    if (angle.floor != angle) {
      cPoints = cPoints :+ new Coordinate(x(angle), y(angle))
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

class ImagePic(img: Image)(implicit val canvas: SCanvas) extends Picture with CorePicOps with CorePicOps2
  with TNodeCacher with RedrawStopper with NonVectorPicOps {

  def makeTnode: edu.umd.cs.piccolo.PNode = Utils.runInSwingThreadAndPause {
    val inode = new PImage(img) {
      override def paint(paintContext: PPaintContext) {
        val g2 = paintContext.getGraphics()
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR)
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

  def copy: net.kogics.kojo.core.Picture = new ImagePic(img)
  override def toString() = s"ImagePic (Id: ${System.identityHashCode(this)})"
}

class FileImagePic(file: String)(implicit canvas: SCanvas) extends ImagePic(Utils.loadImageC(file)) {
  override def copy: net.kogics.kojo.core.Picture = new FileImagePic(file)
}

class SwingPic(swingComponent: JComponent)(implicit val canvas: SCanvas) extends Picture with CorePicOps with CorePicOps2
  with TNodeCacher with RedrawStopper with NonVectorPicOps {

  def pswingHook(ps: PSwing) {}

  def makeTnode: edu.umd.cs.piccolo.PNode = Utils.runInSwingThreadAndPause {
    val pswing = new PSwing(swingComponent)
    def handleCombo(combo: JComboBox[String]) {
      combo.addItem(" " * 10)
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
        case combo: JComboBox[String] => handleCombo(combo)
        case jp: JPanel               => jp.getComponents foreach { handleComponent }
        case _                        =>
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

  private def getNodeBoundsInCanvas(pSwing: PSwing, combo: JComboBox[String]) = {
    val (deltax, deltay) = if (insidePanel(combo)) (combo.getX, combo.getY) else (0, 0)
    val r1c = new Rectangle2D.Double(pSwing.getX + deltax, pSwing.getY + deltay, combo.getWidth, combo.getHeight)
    pSwing.localToGlobal(r1c)
    canvas.getCamera.viewToLocal(r1c)
    r1c.getBounds
  }

  def copy = notSupported("copy")
  override def toString() = s"SwingPic (Id: ${System.identityHashCode(this)})"
}
