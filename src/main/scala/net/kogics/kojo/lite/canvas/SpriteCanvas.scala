/*
 * Copyright (C) 2009 Lalit Pant <pant.lalit@gmail.com>
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
package lite
package canvas

import java.awt.Color
import java.awt.Dimension
import java.awt.GradientPaint
import java.awt.Paint
import java.awt.Toolkit
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.awt.event.InputEvent
import java.awt.geom.Point2D
import java.io.File
import java.util.concurrent.Future
import java.util.logging.Logger

import javax.swing.JCheckBoxMenuItem
import javax.swing.JMenuItem
import javax.swing.JPopupMenu
import javax.swing.event.PopupMenuEvent
import javax.swing.event.PopupMenuListener

import net.kogics.kojo.core
import net.kogics.kojo.core.Cm
import net.kogics.kojo.core.Inch
import net.kogics.kojo.core.Picture
import net.kogics.kojo.core.Pixel
import net.kogics.kojo.core.SCanvas
import net.kogics.kojo.core.UnitLen
import net.kogics.kojo.util.FileChooser

import edu.umd.cs.piccolo.PCanvas
import edu.umd.cs.piccolo.PLayer
import edu.umd.cs.piccolo.PNode
import edu.umd.cs.piccolo.activities.PActivity
import edu.umd.cs.piccolo.event.PBasicInputEventHandler
import edu.umd.cs.piccolo.event.PInputEvent
import edu.umd.cs.piccolo.event.PInputEventFilter
import edu.umd.cs.piccolo.event.PInputEventListener
import edu.umd.cs.piccolo.event.PPanEventHandler
import edu.umd.cs.piccolo.event.PZoomEventHandler
import edu.umd.cs.piccolo.nodes.PPath
import edu.umd.cs.piccolo.nodes.PText
import edu.umd.cs.piccolo.util.PPaintContext
import figure.Figure
import turtle.Turtle
import util.Utils

class SpriteCanvas(val kojoCtx: core.KojoCtx) extends PCanvas with SCanvas {
  val Log = Logger.getLogger(getClass.getName);
  val AxesColor = new Color(100, 100, 100)
  val GridColor = new Color(200, 200, 200)
  val TickColor = new Color(150, 150, 150)
  val TickLabelColor = new Color(50, 50, 50)
  val TickIntegerLabelColor = Color.blue

  def Dpi = kojoCtx.screenDPI
  @volatile var unitLen: UnitLen = Pixel

  def pCanvas: PCanvas = this

  def setUnitLength(ul: UnitLen) {
    throw new UnsupportedOperationException("Use clearWithUL(unit) instead of setUnitLength(unit) and clear().")
  }

  private def realSetUnitLength(ul: UnitLen) {
    unitLen = ul
  }

  var outputFn: String => Unit = { msg =>
    Log.info(msg)
  }

  setBackground(Color.white)
  setPreferredSize(new Dimension(200, 400))
  setDefaultRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING)
  setAnimatingRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING)
  setInteractingRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING)

  //  edu.umd.cs.piccolo.util.PDebug.debugBounds = true
  //  edu.umd.cs.piccolo.util.PDebug.debugFullBounds = true
  //  edu.umd.cs.piccolo.util.PDebug.debugPaintCalls = true

  @volatile var turtles: List[Turtle] = Nil
  var puzzlers: List[Turtle] = Nil
  var figures: List[Figure] = Nil
  var eventListeners: List[PInputEventListener] = Nil

  var showAxes = false
  var showGrid = false
  var showProt = false
  var showScale = false

  val grid = new PNode()
  val axes = new PNode()
  getCamera.addChild(grid)
  getCamera.addChild(axes)

  initCamera()

  addComponentListener(new ComponentAdapter {
    override def componentResized(e: ComponentEvent) = initCamera()
  })

  val figure = newFigure()
  @volatile var turtle = newTurtle()
  val pictures = new PLayer
  getCamera.addLayer(pictures)
  implicit val picCanvas = this

  def turtle0 = turtle
  val figure0 = figure
  val origTurtle = turtle

  type TurtleLike = Turtle
  private[kojo] def setDefTurtle(t: TurtleLike) = {
    turtle = t
  }

  private[kojo] def restoreDefTurtle() = {
    turtle = origTurtle
  }

  val panHandler = new PPanEventHandler() {
    //    setAutopan(false)
    override def pan(event: PInputEvent) {
      super.pan(event)
      Utils.schedule(0.05) {
        updateAxesAndGrid()
      }
    }

    override def dragActivityStep(event: PInputEvent) {
      super.dragActivityStep(event)
      Utils.schedule(0.05) {
        updateAxesAndGrid()
      }
    }
  }

  val zoomHandler = new PZoomEventHandler {
    override def dragActivityStep(event: PInputEvent) {
      if (event.isHandled) {
        return
      }

      super.dragActivityStep(event)
      event.setHandled(true)
      Utils.schedule(0.05) {
        updateAxesAndGrid()
      }
    }
  }

  panHandler.getEventFilter.setNotMask(InputEvent.SHIFT_MASK)
  zoomHandler.setEventFilter(new PInputEventFilter(InputEvent.BUTTON1_MASK | InputEvent.SHIFT_MASK))

  setPanEventHandler(panHandler)
  setZoomEventHandler(zoomHandler)

  addInputEventListener(new PBasicInputEventHandler {
    val popup = new Popup()

    def showPopup(e: PInputEvent) {
      if (e.isPopupTrigger) {
        val pos = e.getCanvasPosition
        popup.show(SpriteCanvas.this, pos.getX.toInt, pos.getY.toInt);
      }
    }

    override def mousePressed(e: PInputEvent) = showPopup(e)
    override def mouseReleased(e: PInputEvent) = showPopup(e)

    override def mouseMoved(e: PInputEvent) {
      val pos = e.getPosition
      val prec0 = Math.round(getCamera.getViewTransformReference.getScale / camScale) - 1
      val prec = {
        if (prec0 < 0) 0
        else if (prec0 > 18) 18
        else prec0
      }
      val statusStr = "Mouse Position: (%%.%df, %%.%df)" format (prec, prec)
      kojoCtx.showStatusText(statusStr format (pos.getX, pos.getY));
    }

    override def mouseWheelRotated(e: PInputEvent) {
      zoomBy(1 - e.getWheelRotation * 0.1, e.getPosition)
    }
  })

  def camScale = unitLen match {
    case Pixel => 1
    case Inch  => Dpi
    case Cm    => Dpi / 2.54
  }

  private def initCamera() {
    val size = getSize(null)
    val scale = camScale
    getCamera.getViewTransformReference.setToScale(scale, -scale)
    getCamera.setViewOffset(size.getWidth / 2f, size.getHeight / 2f)
    updateAxesAndGrid()
  }

  def gridOn() {
    Utils.runInSwingThreadAndWait {
      if (!showGrid) {
        showGrid = true
        updateAxesAndGrid()
        repaint()
      }
    }
  }

  def gridOff() {
    Utils.runInSwingThreadAndWait {
      if (showGrid) {
        showGrid = false
        grid.removeAllChildren()
        repaint()
      }
    }
  }

  def axesOn() {
    Utils.runInSwingThreadAndWait {
      if (!showAxes) {
        showAxes = true
        updateAxesAndGrid()
        repaint()
      }
    }
  }

  def axesOff() {
    Utils.runInSwingThreadAndWait {
      if (showAxes) {
        showAxes = false
        axes.removeAllChildren()
        repaint()
      }
    }
  }

  def updateAxesAndGrid() {

    //    def isInteger(d: Double) = Utils.doublesEqual(d.floor, d, 0.0000000001)
    def isInteger(label: String) = {
      val d = Utils.sanitizeDoubleString(label).toDouble
      Utils.doublesEqual(d.floor, d, 0.0000000001)
    }

    val seedDelta = unitLen match {
      case Pixel => 50
      case Inch  => Dpi
      case Cm    => Dpi / 2.54
    }

    if (!(showGrid || showAxes))
      return

    val scale = getCamera.getViewScale
    val MaxPrec = 10
    val prec0 = Math.round(scale)
    val prec = prec0 match {
      case p if p < 10  => 0
      case p if p < 50  => 2
      case p if p < 100 => 4
      case p if p < 150 => 6
      case p if p < 200 => 8
      case _            => MaxPrec
    }

    val labelPrec = if (scale % seedDelta == 0) math.log10(scale).round else prec

    val labelText = "%%.%df" format (labelPrec)
    val deltaFinder = "%%.%df" format (if (prec == 0) prec else prec - 1)

    val delta = {
      val d = seedDelta
      val d0 = d / scale
      if (d0 > 10) {
        math.round(d0 / 10) * 10
      }
      else {
        val d2 = Utils.sanitizeDoubleString(deltaFinder.format(d0)).toDouble
        if (d2.compare(0) != 0) d2 else 0.0000000005 // MaxPrec-1 zeroes
      }
    }

    val viewBounds = getCamera.getViewBounds()
    val width = viewBounds.width.toFloat
    val height = viewBounds.height.toFloat
    val vbx = viewBounds.x.toFloat
    val vby = viewBounds.y.toFloat

    import java.awt.geom._
    val screenCenter = new Point2D.Double(vbx + width / 2, vby + height / 2)

    val deltap = new Point2D.Double(delta, delta)
    val numxTicks = Math.ceil(width / deltap.getY).toInt + 4
    val numyTicks = Math.ceil(height / deltap.getX).toInt + 4
    val tickSize = 3

    val xStart = {
      val x = viewBounds.x
      if (x < 0) Math.floor(x / deltap.getX) * deltap.getX
      else Math.ceil(x / deltap.getX) * deltap.getX
    } - 2 * deltap.getX

    val yStart = {
      val y = viewBounds.y
      if (y < 0) Math.floor(y / deltap.getY) * deltap.getY
      else Math.ceil(y / deltap.getY) * deltap.getY
    } - 2 * deltap.getY

    grid.removeAllChildren()
    axes.removeAllChildren()

    val xmin = xStart - deltap.getX
    val xmax = xStart + (numxTicks + 1) * deltap.getX

    val ymin = yStart - deltap.getY
    val ymax = yStart + (numyTicks + 1) * deltap.getY

    if (showAxes) {
      val xa1 = getCamera.viewToLocal(new Point2D.Double(xmin, 0))
      var xa2 = getCamera.viewToLocal(new Point2D.Double(xmax, 0))
      val xAxis = PPath.createLine(xa1.getX.toFloat, xa1.getY.toFloat, xa2.getX.toFloat, xa2.getY.toFloat)
      xAxis.setStrokePaint(AxesColor)
      axes.addChild(xAxis)

      val ya1 = getCamera.viewToLocal(new Point2D.Double(0, ymin))
      val ya2 = getCamera.viewToLocal(new Point2D.Double(0, ymax))
      val yAxis = PPath.createLine(ya1.getX.toFloat, ya1.getY.toFloat, ya2.getX.toFloat, ya2.getY.toFloat)
      yAxis.setStrokePaint(AxesColor)
      axes.addChild(yAxis)
    }

    // gridlines and ticks on y axis
    for (i <- 0 until numyTicks) {
      val ycoord = yStart + i * deltap.getY
      if (showGrid) {
        // gridOn
        val pt1 = getCamera.viewToLocal(new Point2D.Double(xmin, ycoord))
        val pt2 = getCamera.viewToLocal(new Point2D.Double(xmax, ycoord))
        val gridline = PPath.createLine(pt1.getX.toFloat, pt1.getY.toFloat, pt2.getX.toFloat, pt2.getY.toFloat)
        gridline.setStrokePaint(GridColor)
        grid.addChild(gridline)
      }
      if (showAxes) {
        val pt1 = getCamera.viewToLocal(new Point2D.Double(-tickSize / scale, ycoord))
        val pt2 = getCamera.viewToLocal(new Point2D.Double(tickSize / scale, ycoord))
        val tick = PPath.createLine(pt1.getX.toFloat, pt1.getY.toFloat, pt2.getX.toFloat, pt2.getY.toFloat)
        tick.setStrokePaint(TickColor)
        axes.addChild(tick)

        if (!Utils.doublesEqual(ycoord, 0, 1 / math.pow(10, prec + 1))) {
          val label = new PText(labelText format (ycoord))
          label.setOffset(pt2.getX.toFloat, pt2.getY.toFloat)
          if (isInteger(label.getText)) {
            label.setText("%.0f" format (ycoord))
            label.setTextPaint(TickIntegerLabelColor)
          }
          else {
            label.setTextPaint(TickLabelColor)
          }
          axes.addChild(label)
        }
      }
    }

    // gridlines and ticks on x axis
    for (i <- 0 until numxTicks) {
      val xcoord = xStart + i * deltap.getX
      if (showGrid) {
        val pt1 = getCamera.viewToLocal(new Point2D.Double(xcoord, ymax))
        val pt2 = getCamera.viewToLocal(new Point2D.Double(xcoord, ymin))
        val gridline = PPath.createLine(pt1.getX.toFloat, pt1.getY.toFloat, pt2.getX.toFloat, pt2.getY.toFloat)
        gridline.setStrokePaint(GridColor)
        grid.addChild(gridline)
      }
      if (showAxes) {
        val pt1 = getCamera.viewToLocal(new Point2D.Double(xcoord, tickSize / scale))
        val pt2 = getCamera.viewToLocal(new Point2D.Double(xcoord, -tickSize / scale))
        val tick = PPath.createLine(pt1.getX.toFloat, pt1.getY.toFloat, pt2.getX.toFloat, pt2.getY.toFloat)
        tick.setStrokePaint(TickColor)
        axes.addChild(tick)

        if (Utils.doublesEqual(xcoord, 0, 1 / math.pow(10, prec + 1))) {
          val label = new PText("0")
          label.setOffset(pt2.getX.toFloat + 2, pt2.getY.toFloat)
          label.setTextPaint(TickIntegerLabelColor)
          axes.addChild(label)
        }
        else {
          val label = new PText(labelText format (xcoord))
          label.setOffset(pt2.getX.toFloat, pt2.getY.toFloat)
          if (isInteger(label.getText)) {
            label.setText("%.0f" format (xcoord))
            label.setTextPaint(TickIntegerLabelColor)
          }
          else {
            label.setTextPaint(TickLabelColor)
          }
          if (label.getText.length > 5) {
            label.rotateInPlace(45.toRadians)
          }
          axes.addChild(label)
        }
      }
    }

    //    outputFn("\nScale: %f\n" format(scale))
    //    outputFn("Deltap: %s\n" format(deltap.toString))
  }

  // meant to be called from swing thread
  private def currZoom = getCamera.getViewTransformReference.getScaleX

  // zoom, leaving the current center point (in canvas/world coordinates) unchanged
  def zoom(factor0: Double): Unit = {
    require(factor0 != 0, "Zoom factor can't be 0.")
    Utils.runInSwingThreadAndWait {
      val size = getSize(null)
      val cx = new Point2D.Double(size.width / 2d, size.height / 2d)
      val cp = getCamera.localToView(cx)
      zoom(factor0, cp.getX, cp.getY)
    }
  }

  // zoom, leaving the current mouse position point (in canvas/world coordinates) unchanged
  def zoomBy(factor: Double, mousePos: Point2D): Unit = {
    require(factor != 0, "Zoom factor can't be 0.")
    Utils.runInSwingThreadAndWait {
      //      val tx = getCamera.getViewTransformReference.getTranslateX
      //      val ty = getCamera.getViewTransformReference.getTranslateY
      //      val oldZoom = currZoom
      //      getCamera.getViewTransformReference.setToIdentity()
      //      getCamera.getViewTransformReference.scale(oldZoom, -oldZoom)
      //      getCamera.getViewTransformReference.setOffset(tx, ty)
      //      getCamera.getViewTransformReference.translate(mousePos.getX, mousePos.getY)
      //      getCamera.getViewTransformReference.scale(factor, factor)
      //      getCamera.getViewTransformReference.translate(-mousePos.getX, -mousePos.getY)

      getCamera.getViewTransformReference.scaleAboutPoint(factor, mousePos.getX, mousePos.getY)
      updateAxesAndGrid()
      repaint()
    }
  }

  def zoom(factor0: Double, cx: Double, cy: Double): Unit = {
    require(factor0 != 0, "Zoom factor can't be 0.")
    Utils.runInSwingThreadAndWait {
      val size = getSize(null)
      val factor = factor0 * camScale
      getCamera.getViewTransformReference.setToScale(factor, -factor)
      getCamera.getViewTransformReference.setOffset(size.getWidth / 2d, size.getHeight / 2d)
      getCamera.getViewTransformReference.translate(-cx, -cy)
      updateAxesAndGrid()
      repaint()
    }
  }

  def zoomXY(xfactor0: Double, yfactor0: Double, cx: Double, cy: Double) {
    require(xfactor0 != 0, "Zoom factor can't be 0.")
    require(yfactor0 != 0, "Zoom factor can't be 0.")
    Utils.runInSwingThreadAndWait {
      val xfactor = xfactor0 * camScale
      val yfactor = yfactor0 * camScale
      val size = getSize(null)
      getCamera.getViewTransformReference.setToScale(xfactor, -yfactor)
      getCamera.getViewTransformReference.setOffset(
        size.getWidth / 2d - cx * xfactor.abs,
        size.getHeight / 2d + cy * yfactor.abs
      )
      updateAxesAndGrid()
      repaint()
    }
  }

  import java.io.File
  private def exportImageHelper(filePrefix: String, width: Int, height: Int): java.io.File = {
    val outfile = File.createTempFile(filePrefix + "-", ".png")
    exportImageToFile(outfile, width, height)
    outfile
  }

  private def exportImageToFile(outfile: File, width: Int, height: Int) {
    val image = getCamera.toImage(width, height, java.awt.Color.white)
    javax.imageio.ImageIO.write(image.asInstanceOf[java.awt.image.BufferedImage], "png", outfile)
  }

  def exportImage(filePrefix: String): File = {
    exportImageHelper(filePrefix, getWidth, getHeight)
  }

  def exportImage(filePrefix: String, width: Int, height: Int): File = {
    exportImageHelper(filePrefix, width, height)
  }

  def exportThumbnail(filePrefix: String, height: Int): File = {
    exportImageHelper(filePrefix, (getWidth.toFloat / getHeight * height).toInt, height)
  }

  def forceClear() {
    kojoCtx.stopScript()
    clear()
  }

  def makeStagingVisible() {
    kojoCtx.makeStagingVisible()
  }

  def clearStaging() {
    realSetUnitLength(Pixel)
    realClearStaging()
  }

  def clearStagingWul(ul: UnitLen) {
    realSetUnitLength(ul)
    realClearStaging()
  }

  def clear() {
    realSetUnitLength(Pixel)
    realClear()
  }

  def clearWithUL(ul: UnitLen) {
    realSetUnitLength(ul)
    realClear()
  }

  def realClearStaging() {
    makeStagingVisible()
    clearHelper()
  }

  def realClear() {
    kojoCtx.makeTurtleWorldVisible()
    clearHelper()
  }

  private def clearHelper() {
    // can't stop animation because it kills animations that run from within 
    // code blocks inside stories
    // kojoCtx.stopAnimation()  
    gridOff()
    axesOff()
    Utils.runInSwingThreadAndWait {
      setBackground(Color.white)
      showProt = false
      showScale = false
      turtles.foreach { t => if (t == origTurtle) t.clear() else t.remove() }
      turtles = List(turtles.last)

      figures.foreach { f => if (f == figure) f.clear() else f.remove() }
      figures = List(figures.last)

      eventListeners.foreach { el => removeInputEventListener(el) }
      eventListeners = Nil
      staging.Inputs.removeKeyHandlers()
      getRoot.getDefaultInputManager.setKeyboardFocus(null)

      pictures.removeAllChildren()
      zoom(1, 0, 0)
    }
    clearStage()
  }

  def clearPuzzlers() {
    stop()
    Utils.runInSwingThreadAndWait {
      puzzlers.foreach { t => t.remove() }
      puzzlers = Nil
    }
  }

  def stop() = {
    Utils.runInSwingThreadAndWait {
      puzzlers.foreach { t => t.stop }
      turtles.foreach { t => t.stop }
      figures.foreach { f => f.stop }
      kojoCtx.activityListener.pendingCommandsDone()
      Utils.schedule(0.5) {
        kojoCtx.activityListener.pendingCommandsDone()
      }
    }
  }

  def wipe = Utils.runInSwingThread {
    pictures.removeAllChildren()
  }

  def newFigure(x: Int = 0, y: Int = 0) = {
    val fig = Utils.runInSwingThreadAndWait {
      val f = Figure(this, x, y)
      f.setSpriteListener(kojoCtx.activityListener)
      figures = f :: figures
      f
    }
    this.repaint()
    fig
  }

  def newTurtle(x: Double = 0, y: Double = 0, costume: String = "/images/turtle32.png") = {
    val ttl = Utils.runInSwingThreadAndWait {
      val t = new Turtle(this, costume, x, y)
      turtles = t :: turtles
      t
    }
    this.repaint()
    ttl
  }

  // needs to be called on swing thread
  private[kojo] def newInvisibleTurtle(x: Double = 0, y: Double = 0) = {
    new Turtle(this, "/images/turtle32.png", x, y, true)
  }

  def newPuzzler(x: Int = 0, y: Int = 0) = {
    val pzl = Utils.runInSwingThreadAndWait {
      val t = new Turtle(this, "/images/puzzler32.png", x, y, false, true)
      t.setPenThickness(1)
      t.setPenColor(Color.blue)
      t.setAnimationDelay(10)
      puzzlers = t :: puzzlers
      t
    }
    this.repaint()
    pzl
  }

  def onKeyPress(fn: Int => Unit) = Utils.runInSwingThread {
    staging.Inputs.setKeyPressedHandler { e =>
      fn(e.getKeyCode)
    }
  }

  def onKeyRelease(fn: Int => Unit) = Utils.runInSwingThread {
    staging.Inputs.setKeyReleasedHandler { e =>
      fn(e.getKeyCode)
    }
  }

  def onMouseClick(fn: (Double, Double) => Unit) = Utils.runInSwingThread {
    val eh = new PBasicInputEventHandler {
      override def mousePressed(event: PInputEvent) {
        val pos = event.getPosition
        Utils.runAsyncQueued {
          fn(pos.getX, pos.getY)
        }
      }
    }
    eventListeners = eh :: eventListeners
    addInputEventListener(eh)
  }

  var globalEl: PInputEventListener = _
  def addGlobalEventListener(l: PInputEventListener) {
    globalEl = l
    addInputEventListener(l)
  }

  def activate() {
    def grabFocus() {
      requestFocusInWindow()
      getRoot.getDefaultInputManager.setKeyboardFocus(globalEl)
    }
    // try to make this play well with 
    // (a) a slow system or a fast system
    // (b) a toggle call at the beginning of a script or an activate call at the end
    Utils.schedule(0) { grabFocus() }
    Utils.schedule(0.3) { grabFocus() }
    Utils.schedule(0.9) { grabFocus() }
  }

  def cbounds = Utils.runInSwingThreadAndWait { getCamera.getViewBounds() }

  def setCanvasBackground(c: Paint) = Utils.runInSwingThread {
    c match {
      case color: Color if color.getAlpha == 255 =>
        // full screen windows get messed up with transparent color backgrounds
        // so set window background only if color is fully opaque 
        setBackground(color)
      case _ =>
        val bounds = cbounds
        val rect = staging.Impl.API.rectangle(bounds.x, bounds.y, bounds.width, bounds.height)
        rect.setFillColor(c)
        rect.setPenThickness(0)
        rect.setPenColor(c)
    }
  }
  def setBackgroundH(c1: Color, c2: Color) = Utils.runInSwingThread {
    val bounds = cbounds
    val paint = new GradientPaint(bounds.x.toFloat, 0, c1, (bounds.x + bounds.width).toFloat, 0, c2)
    setCanvasBackground(paint)
  }
  def setBackgroundV(c1: Color, c2: Color) = Utils.runInSwingThread {
    val bounds = cbounds
    val paint = new GradientPaint(0, bounds.y.toFloat, c1, 0, (bounds.y + bounds.height).toFloat, c2)
    setCanvasBackground(paint)
  }

  def animate(fn: => Unit): Future[PActivity] = figure0.refresh(fn)
  def animateActivity(a: PActivity) = getRoot.addActivity(a)
  def stopAnimation() = figure0.stopRefresh()

  import core.Picture
  val noPic = picture.Pic { t =>
  }
  @volatile var stage: Picture = _
  @volatile var stageLeft: Picture = _
  @volatile var stageTop: Picture = _
  @volatile var stageRight: Picture = _
  @volatile var stageBot: Picture = _

  def clearStage() {
    stage = noPic
    stageLeft = noPic
    stageTop = noPic
    stageRight = noPic
    stageBot = noPic
  }

  def drawStage(fillc: Paint) {
    def border(size: Double) = picture.stroke(Color.darkGray) -> picture.Pic { t =>
      t.forward(size)
    }
    val cb = cbounds
    val xmax = cb.x.abs
    val ymax = cb.y.abs

    stageLeft = picture.trans(-xmax, -ymax) -> border(cb.height)
    stageTop = picture.trans(-xmax, ymax) * picture.rot(-90) -> border(cb.width)
    stageRight = picture.trans(xmax, -ymax) -> border(cb.height)
    stageBot = picture.trans(-xmax, -ymax) * picture.rot(-90) -> border(cb.width)

    stage = picture.GPics(
      stageLeft,
      stageTop,
      stageRight,
      stageBot)

    stage.draw()
    setCanvasBackground(fillc)
  }

  class Popup() extends JPopupMenu {

    val axesItem = new JCheckBoxMenuItem(Utils.loadString("S_ShowAxes"))
    axesItem.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent) {
        if (axesItem.isSelected) {
          axesOn()
        }
        else {
          axesOff()
        }
      }
    })
    add(axesItem)

    val gridItem = new JCheckBoxMenuItem(Utils.loadString("S_ShowGrid"))
    gridItem.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent) {
        if (gridItem.isSelected) {
          gridOn()
        }
        else {
          gridOff()
        }
      }
    })
    add(gridItem)

    addSeparator()

    val protItem = new JCheckBoxMenuItem(Utils.loadString("S_ShowProtractor"))
    protItem.addActionListener(new ActionListener {
      @volatile var prot: core.Picture = _
      def protOn() {
        if (prot == null) {
          println("Drag Protractor to move it around; Shift-Drag to rotate.")
        }
        showProt = true
        prot = picture.protractor(camScale)
        // can draw from GUI thread because anim delay is zero, and latch will not be used
        prot.draw()
      }

      def protOff() {
        showProt = false
        prot.invisible()
      }

      override def actionPerformed(e: ActionEvent) {
        if (protItem.isSelected) {
          protOn()
        }
        else {
          protOff()
        }
      }
    })
    add(protItem)

    val scaleItem = new JCheckBoxMenuItem(Utils.loadString("S_ShowScale"))
    scaleItem.addActionListener(new ActionListener {
      @volatile var ruler: core.Picture = _
      def scaleOn() {
        if (ruler == null) {
          println("Drag Scale to move it around; Shift-Drag to rotate.")
        }
        showScale = true
        ruler = picture.ruler(camScale)
        // can draw from GUI thread because anim delay is zero, and latch will not be used
        ruler.draw()
      }

      def scaleOff() {
        showScale = false
        ruler.invisible()
      }

      override def actionPerformed(e: ActionEvent) {
        if (scaleItem.isSelected) {
          scaleOn()
        }
        else {
          scaleOff()
        }
      }
    })
    add(scaleItem)

    val saveAsImage = new JMenuItem(Utils.loadString("S_SaveAsImage"))
    saveAsImage.addActionListener(new ActionListener {
      val fchooser = new FileChooser(kojoCtx)
      override def actionPerformed(e: ActionEvent) {
        val file = fchooser.chooseFile("PNG Image File", "png", Utils.stripDots(Utils.loadString("S_SaveAs")))
        if (file != null) {
          exportImageToFile(file, SpriteCanvas.this.getWidth, SpriteCanvas.this.getHeight)
        }
      }
    })
    add(saveAsImage)

    addSeparator()

    val resetPanZoomItem = new JMenuItem(Utils.loadString("S_ResetPanZoom"))
    resetPanZoomItem.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent) {
        initCamera()
      }
    })
    add(resetPanZoomItem)
    val clearItem = new JMenuItem(Utils.loadString("S_Clear"))
    clearItem.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent) {
        forceClear()
      }
    })
    add(clearItem)

    addSeparator()

    val fsCanvasAction = kojoCtx.fullScreenCanvasAction()
    val fullScreenItem: JCheckBoxMenuItem = new JCheckBoxMenuItem(fsCanvasAction)
    add(fullScreenItem)

    addSeparator()

    add("<html><em>%s</em></html>" format (Utils.loadString("S_MouseActions")))
    addPopupMenuListener(new PopupMenuListener {
      def popupMenuWillBecomeVisible(e: PopupMenuEvent) {
        axesItem.setState(showAxes)
        gridItem.setState(showGrid)
        protItem.setState(showProt)
        scaleItem.setState(showScale)
        kojoCtx.updateMenuItem(fullScreenItem, fsCanvasAction)
      }
      def popupMenuWillBecomeInvisible(e: PopupMenuEvent) {}
      def popupMenuCanceled(e: PopupMenuEvent) {}
    })
  }
}


