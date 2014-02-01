package net.kogics.kojo.picture

import java.awt.BasicStroke
import java.awt.Color
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
import edu.umd.cs.piccolox.pswing.PSwing

trait PicShapeOps { self: Picture with CorePicOps =>
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

  def morph(fn: Seq[net.kogics.kojo.kgeom.PolyLine] => Seq[net.kogics.kojo.kgeom.PolyLine]) {}
  def dumpInfo() {}
  def foreachPolyLine(fn: net.kogics.kojo.kgeom.PolyLine => Unit) {}
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
    def cPoints = for (i <- 0 to angle.toInt) yield new Coordinate(x(i), y(i))
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

  def copy: net.kogics.kojo.core.Picture = new CirclePic(r)
}

class ImagePic(file: String)(implicit val canvas: SCanvas) extends Picture with CorePicOps with CorePicOps2
  with TNodeCacher with RedrawStopper with PicShapeOps {

  def initGeom(): com.vividsolutions.jts.geom.Geometry = {
    throw new IllegalStateException("Geometry is not available for images")
  }

  def makeTnode: edu.umd.cs.piccolo.PNode = Utils.runInSwingThreadAndPause {
    val inode = new PImage(Utils.loadImageC(file))
    inode.getTransformReference(true).setToScale(1 / canvas.camScale, -1 / canvas.camScale)
    inode.translate(0, -inode.getHeight)

    val node = PPath.createRectangle(0, 0, inode.getWidth.toFloat, inode.getHeight.toFloat)
    node.setPaint(null)
    node.setStroke(null)
    node.setVisible(false)
    node.addChild(inode)
    picLayer.addChild(node)
    node
  }

  def copy: net.kogics.kojo.core.Picture = new ImagePic(file)
}

class SwingPic2(swingComponent: JComponent)(implicit val canvas: SCanvas) extends Picture with CorePicOps with CorePicOps2
  with TNodeCacher with RedrawStopper with PicShapeOps {

  def initGeom(): com.vividsolutions.jts.geom.Geometry = {
    throw new UnsupportedOperationException("Geometry is not available for Swing Pictures")
  }

  def pswingHook(ps: PSwing) {}

  def makeTnode: edu.umd.cs.piccolo.PNode = Utils.runInSwingThreadAndPause {
    val pswing = new PSwing(swingComponent)
    def handleCombo(comp: JComboBox) {
      comp.addItem(" " * 10)
      comp.addPopupMenuListener(new PopupMenuListener {
        def popupMenuWillBecomeVisible(e: PopupMenuEvent) {
          comp.setBounds(getNodeBoundsInCanvas(pswing, comp))
          comp.revalidate()
        }
        def popupMenuWillBecomeInvisible(e: PopupMenuEvent) {}
        def popupMenuCanceled(e: PopupMenuEvent) {}
      })
    }
    swingComponent match {
      case comp: JComboBox => handleCombo(comp)
      case jp: JPanel => jp.getComponents foreach { c =>
        c match {
          case comp: JComboBox => handleCombo(comp)
          case _               =>
        }
      }
      case _ =>
    }
    pswing.getTransformReference(true).setToScale(1 / canvas.camScale, -1 / canvas.camScale)
    pswing.translate(0, -pswing.getHeight)

    val node = new PNode
    node.addChild(pswing)
    picLayer.addChild(node)
    node
  }
  private def getNodeBoundsInCanvas(pSwing: PSwing, comp: JComboBox) = {
    val embeddedCombo = comp.getParent.isInstanceOf[JPanel]
    val (deltax, deltay) = if (embeddedCombo) (comp.getX, comp.getY) else (0, 0)
    val r1c = new Rectangle2D.Double(pSwing.getX + deltax, pSwing.getY + deltay, comp.getWidth, comp.getHeight)
    pSwing.localToGlobal(r1c)
    canvas.getCamera.viewToLocal(r1c)
    r1c.getBounds
  }

  def copy = throw new UnsupportedOperationException("Can't copy swing pictures")
}
