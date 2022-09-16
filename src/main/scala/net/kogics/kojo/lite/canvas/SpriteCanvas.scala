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
import java.awt.event.{ActionEvent, ActionListener, ComponentAdapter, ComponentEvent, InputEvent, MouseEvent}
import java.awt.geom.Point2D
import java.util.concurrent.Future
import java.util.logging.Logger

import javax.swing.JCheckBoxMenuItem
import javax.swing.JMenuItem
import javax.swing.JPopupMenu
import javax.swing.event.PopupMenuEvent
import javax.swing.event.PopupMenuListener

import net.kogics.kojo.core.Cm
import net.kogics.kojo.core.Inch
import net.kogics.kojo.core.Pixel
import net.kogics.kojo.core.SCanvas
import net.kogics.kojo.core.UnitLen
import net.kogics.kojo.figure.Figure
import net.kogics.kojo.picture.PicCache
import net.kogics.kojo.staging.Rectangle
import net.kogics.kojo.turtle.Turtle
import net.kogics.kojo.util.FileChooser
import net.kogics.kojo.util.Utils

import edu.umd.cs.piccolo.PCanvas
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
import edu.umd.cs.piccolox.pswing.PSwingCanvas

class SpriteCanvas(val kojoCtx: core.KojoCtx) extends PSwingCanvas with SCanvas {
  val origLayer = getLayer()

  val Log = Logger.getLogger(getClass.getName);
  val AxesColor = Theme.currentTheme.canvasAxesColor
  val GridColor = Theme.currentTheme.canvasGridColor
  val TickColor = Theme.currentTheme.canvasTickColor
  val TickLabelColor = Theme.currentTheme.canvasTickLabelColor
  val TickIntegerLabelColor = Theme.currentTheme.canvasTickIntegerLabelColor

  def Dpi = kojoCtx.screenDPI
  @volatile var unitLen: UnitLen = Pixel

  def pCanvas: PCanvas = this

  def setUnitLength(ul: UnitLen): Unit = {
    throw new UnsupportedOperationException("Use clearWithUL(unit) instead of setUnitLength(unit) and clear().")
  }

  private def realSetUnitLength(ul: UnitLen): Unit = {
    unitLen = ul
  }

  var outputFn: String => Unit = { msg =>
    Log.info(msg)
  }

  val backgroundColor = Theme.currentTheme.canvasBg
  var currentBackground: Paint = backgroundColor
  var currentBackgroundRect: Rectangle = null

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

  var _showAxes = false
  var _showGrid = false
  var _showProt = false
  var _showScale = false
  var prot: core.Picture = _
  var ruler: core.Picture = _

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
  val pictures = origLayer
  //  getCamera.addLayer(getCamera.getLayerCount - 1, pictures)
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
    setAutopan(false)
    override def pan(event: PInputEvent): Unit = {
      super.pan(event)
      Utils.schedule(0.05) {
        updateAxesAndGrid()
      }
    }

    override def dragActivityStep(event: PInputEvent): Unit = {
      super.dragActivityStep(event)
      Utils.schedule(0.05) {
        updateAxesAndGrid()
      }
    }
  }

  val zoomHandler = new PZoomEventHandler {
    override def dragActivityStep(event: PInputEvent): Unit = {
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

  def disablePanAndZoom(): Unit = Utils.runInSwingThread {
    setPanEventHandler(null)
    setZoomEventHandler(null)
  }

  def enablePanAndZoom(): Unit = Utils.runInSwingThread {
    setPanEventHandler(panHandler)
    setZoomEventHandler(zoomHandler)
  }

  enablePanAndZoom()

  addInputEventListener(new PBasicInputEventHandler {
    val popup = new Popup()

    def showPopup(e: PInputEvent): Unit = {
      if (e.isPopupTrigger) {
        val pos = e.getCanvasPosition
        popup.show(SpriteCanvas.this, pos.getX.toInt + 10, pos.getY.toInt + 10);
      }
    }

    override def mousePressed(e: PInputEvent) = showPopup(e)
    override def mouseReleased(e: PInputEvent) = showPopup(e)

    override def mouseMoved(e: PInputEvent): Unit = {
      val pos = e.getPosition
      val prec0 = Math.round(getCamera.getViewTransformReference.getScale / camScale) - 1
      val prec = {
        if (prec0 < 0) 0
        else if (prec0 > 18) 18
        else prec0
      }
      val mousePositionName = Utils.loadString("S_MousePosition")
      val statusStr = s"$mousePositionName: (%.${prec}f, %.${prec}f)"
      kojoCtx.showStatusText(statusStr.format(pos.getX, pos.getY));
    }

    override def mouseWheelRotated(e: PInputEvent): Unit = {
      // If wheelRotation is 10, zoomFactor is 0, which causes an exception in zoomBy
      // If wheelRotation is > 10, zoomFactor is < 0.  The fix is to limit 
      // wheelRotation to 9.
      if (getZoomEventHandler != null) {
        val wheelRotation = math.min(e.getWheelRotation, 9)
        zoomBy(1 - wheelRotation * 0.1, e.getPosition)
      }
    }
  })

  def camScale = unitLen match {
    case Pixel => 1
    case Inch  => Dpi
    case Cm    => Dpi / 2.54
  }

  private def initCamera(): Unit = {
    val size = getSize(null)
    val scale = camScale
    getCamera.getViewTransformReference.setToScale(scale, -scale)
    getCamera.setViewOffset(size.getWidth / 2f, size.getHeight / 2f)
    setCanvasBackground(currentBackground)
    updateAxesAndGrid()
  }

  def showGrid(): Unit = {
    Utils.runInSwingThreadAndWait {
      if (!_showGrid) {
        _showGrid = true
        updateAxesAndGrid()
        repaint()
      }
    }
  }

  def hideGrid(): Unit = {
    Utils.runInSwingThreadAndWait {
      if (_showGrid) {
        _showGrid = false
        grid.removeAllChildren()
        repaint()
      }
    }
  }

  def showAxes(): Unit = {
    Utils.runInSwingThreadAndWait {
      if (!_showAxes) {
        _showAxes = true
        updateAxesAndGrid()
        repaint()
      }
    }
  }

  def hideAxes(): Unit = {
    Utils.runInSwingThreadAndWait {
      if (_showAxes) {
        _showAxes = false
        axes.removeAllChildren()
        repaint()
      }
    }
  }

  def showProtractor(x: Double = 0, y: Double = 0) = Utils.runInSwingThreadAndWait {
    if (!_showProt) {
      if (prot == null) {
        println("Drag Protractor to move it around; Shift-Drag to rotate.")
      }
      _showProt = true
      prot = picture.protractor(camScale)
      // can draw from GUI thread because anim delay is zero, and latch will not be used
      prot.draw()
    }
    prot.translate(x, y)
    prot
  }

  def hideProtractor() = Utils.runInSwingThreadAndWait {
    if (_showProt) {
      _showProt = false
      prot.erase()
    }
  }

  def showScale(x: Double = 0, y: Double = 0) = Utils.runInSwingThreadAndWait {
    if (!_showScale) {
      if (ruler == null) {
        println("Drag Scale to move it around; Shift-Drag to rotate.")
      }
      _showScale = true
      ruler = picture.ruler(camScale)
      // can draw from GUI thread because anim delay is zero, and latch will not be used
      ruler.draw()
    }
    ruler.translate(x, y)
    ruler
  }

  def hideScale() = Utils.runInSwingThreadAndWait {
    if (_showScale) {
      _showScale = false
      ruler.erase()
    }
  }

  def updateAxesAndGrid(): Unit = {
    if (!(_showGrid || _showAxes))
      return

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
        math.round(d0 / 10) * 10.0
      }
      else {
        val d2 = Utils.sanitizeDoubleString(deltaFinder.format(d0)).toDouble
        if (d2.compare(0) != 0) d2 else 0.0000000005 // MaxPrec-1 zeroes
      }
    }

    val viewBounds = getCamera.getViewBounds()
    val xmin = viewBounds.x
    val xmax = viewBounds.x + viewBounds.width
    val ymin = viewBounds.y
    val ymax = viewBounds.y + viewBounds.height
    import java.awt.geom._

    val deltap = new Point2D.Double(delta, delta)
    val numxTicks = Math.ceil(viewBounds.width.toInt / deltap.getX).toInt
    val numyTicks = Math.ceil(viewBounds.height.toInt / deltap.getY).toInt
    val tickSize = 3

    val xStart = {
      val x = xmin.toInt
      if (x < 0) Math.ceil(x / deltap.getX) * deltap.getX
      else Math.floor(x / deltap.getX) * deltap.getX
    }

    val yStart = {
      val y = ymin.toInt
      if (y < 0) Math.ceil(y / deltap.getY) * deltap.getY
      else Math.floor(y / deltap.getY) * deltap.getY
    }

    grid.removeAllChildren()
    axes.removeAllChildren()

    if (_showAxes) {
      val xa1 = getCamera.viewToLocal(new Point2D.Double(xmin, 0))
      val xa2 = getCamera.viewToLocal(new Point2D.Double(xmax, 0))
      val xAxis = PPath.createLine(xa1.getX.toFloat, xa1.getY.toFloat, xa2.getX.toFloat, xa2.getY.toFloat)
      xAxis.setStrokePaint(AxesColor)
      axes.addChild(xAxis)

      val ya1 = getCamera.viewToLocal(new Point2D.Double(0, ymin))
      val ya2 = getCamera.viewToLocal(new Point2D.Double(0, ymax))
      val yAxis = PPath.createLine(ya1.getX.toFloat, ya1.getY.toFloat, ya2.getX.toFloat, ya2.getY.toFloat)
      yAxis.setStrokePaint(AxesColor)
      axes.addChild(yAxis)
    }

    def inRange(a: Double, b: Double, c: Double) = a >= b && a <= c

    // gridlines and ticks on y axis
    for (i <- 0 until numyTicks) {
      val ycoord = yStart + i * deltap.getY
      if (_showGrid) {
        // gridOn
        val pt1 = getCamera.viewToLocal(new Point2D.Double(xmin, ycoord))
        val pt2 = getCamera.viewToLocal(new Point2D.Double(xmax, ycoord))
        val gridline = PPath.createLine(pt1.getX.toFloat, pt1.getY.toFloat, pt2.getX.toFloat, pt2.getY.toFloat)
        gridline.setStrokePaint(GridColor)
        grid.addChild(gridline)
      }
      if (_showAxes && inRange(ycoord, ymin, ymax)) {
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
      if (_showGrid) {
        val pt1 = getCamera.viewToLocal(new Point2D.Double(xcoord, ymax))
        val pt2 = getCamera.viewToLocal(new Point2D.Double(xcoord, ymin))
        val gridline = PPath.createLine(pt1.getX.toFloat, pt1.getY.toFloat, pt2.getX.toFloat, pt2.getY.toFloat)
        gridline.setStrokePaint(GridColor)
        grid.addChild(gridline)
      }
      if (_showAxes && inRange(xcoord, xmin, xmax)) {
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
    val czoom = currZoom
    if ((czoom < 1.0E-30 && factor < 1) ||
      (czoom > 1.0E30 && factor > 1)) {
      // restrict current zoom to practically usable range, with plenty to spare - canvas items 
      // disappear after a zoom of approximately 1.0E(+-)10. 
      // without restriction, the current zoom eventually becomes NaN or Infinity  
      return
    }

    Utils.runInSwingThreadAndWait {
      getCamera.getViewTransformReference.scaleAboutPoint(factor, mousePos.getX, mousePos.getY)
      updateAxesAndGrid()
      repaint()
    }
  }

  def zoom(factor: Double, cx: Double, cy: Double): Unit = {
    zoomXY(factor, factor, cx, cy)
  }

  def zoomXY(xfactor0: Double, yfactor0: Double, cx: Double, cy: Double): Unit = {
    require(xfactor0 != 0, "Zoom factor can't be 0.")
    require(yfactor0 != 0, "Zoom factor can't be 0.")
    Utils.runInSwingThreadAndWait {
      val xfactor = xfactor0 * camScale
      val yfactor = yfactor0 * camScale
      val size = getSize(null)
      getCamera.getViewTransformReference.setToScale(xfactor, -yfactor)
      getCamera.getViewTransformReference.setOffset(
        size.getWidth / 2d - cx * xfactor,
        size.getHeight / 2d + cy * yfactor
      )
      updateAxesAndGrid()
      repaint()
    }
  }

  def scroll(x: Double, y: Double): Unit = {
    Utils.runInSwingThreadAndWait {
      getCamera.getViewTransformReference.translate(-x, -y)
      updateAxesAndGrid()
      repaint()
    }
  }

  def viewRotate(a: Double): Unit = {
    Utils.runInSwingThreadAndWait {
      getCamera.getViewTransformReference.rotate(a.toRadians)
      updateAxesAndGrid()
      repaint()
    }
  }

  import java.io.File
  private def exportImageHelper(filePrefix: String, width: Int, height: Int): java.io.File = {
    val outfile = File.createTempFile(filePrefix + "-", ".png")
    exportImageToFile(outfile, width, height)
    println(s"Image saved in: $outfile")
    outfile
  }

  private def exportImageToFile(outfile: File, width: Int, height: Int): Unit = {
    val image = getCamera.toImage(width, height, currentBackground)
    javax.imageio.ImageIO.write(image.asInstanceOf[java.awt.image.BufferedImage], "png", outfile)
  }

  def exportImage(filePrefix: String): File = {
    exportImageHelper(filePrefix, getWidth, getHeight)
  }

  def exportImage(filePrefix: String, width: Int, height: Int): File = {
    exportImageHelper(filePrefix, width, height)
  }

  def exportThumbnail(filePrefix: String, height: Int): File = {
    exportImageHelper(filePrefix, widthForHeight(height), height)
  }

  def exportImageH(filePrefix: String, height: Int): File = {
    exportImageHelper(filePrefix, widthForHeight(height), height)
  }

  def exportImageW(filePrefix: String, width: Int): File = {
    exportImageHelper(filePrefix, width, heightForWidth(width))
  }

  def heightForWidth(width: Int): Int = {
    (getHeight.toFloat / getWidth * width).toInt
  }

  def widthForHeight(height: Int): Int = {
    (getWidth.toFloat / getHeight * height).toInt
  }

  def forceClear(): Unit = {
    kojoCtx.stopScript()
    clear()
  }

  def makeStagingVisible(): Unit = {
    kojoCtx.makeStagingVisible()
  }

  def clearStaging(): Unit = {
    realSetUnitLength(Pixel)
    realClearStaging()
  }

  def clearStagingWul(ul: UnitLen): Unit = {
    realSetUnitLength(ul)
    realClearStaging()
  }

  def clear(): Unit = {
    realSetUnitLength(Pixel)
    realClear()
  }

  def clearStepDrawing(): Unit = {
    origTurtle.clear()
  }

  def clearWithUL(ul: UnitLen): Unit = {
    realSetUnitLength(ul)
    realClear()
  }

  def realClearStaging(): Unit = {
    makeStagingVisible()
    clearHelper()
  }

  def realClear(): Unit = {
    kojoCtx.makeTurtleWorldVisible()
    clearHelper()
  }

  private def clearHelper(): Unit = {
    // can't stop animation because it kills animations that run from within 
    // code blocks inside stories
    // kojoCtx.stopAnimation()  
    gridOff()
    axesOff()
    Utils.runInSwingThreadAndWait {
      setBackgroundWrapper(backgroundColor)
      _showProt = false
      _showScale = false
      turtles.foreach { t => if (t == origTurtle) t.clear() else t.remove() }
      turtles = List(turtles.last)

      figures.foreach { f => if (f == figure) f.clear() else f.remove() }
      figures = List(figures.last)

      staging.Inputs.removeMouseKeyHandlers()
      getRoot.getDefaultInputManager.setKeyboardFocus(null)

      pictures.removeAllChildren()
      zoom(1, 0, 0)
      enablePanAndZoom()
      Builtins.instance.onClear()
    }
    PicCache.clear()
    Utils.clearGuiBatchQ()
    clearStage()
  }

  def clearPuzzlers(): Unit = {
    stop()
    Utils.runInSwingThreadAndWait {
      puzzlers.foreach { t => t.remove() }
      puzzlers = Nil
    }
  }

  def stop() = {
    Utils.runInSwingThreadAndWait {
      puzzlers.foreach { t => t.stop() }
      turtles.foreach { t => t.stop() }
      figures.foreach { f => f.stop() }
      staging.Inputs.removeMouseKeyHandlers()
      kojoCtx.activityListener.pendingCommandsDone()
      Utils.schedule(0.5) {
        kojoCtx.activityListener.pendingCommandsDone()
      }
    }
  }

  def wipe() = {
    Utils.runInSwingThread {
      pictures.removeAllChildren()
    }
    PicCache.clear()
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

  def onKeyPress(fn: Int => Unit) = Utils.runInSwingThread {
    staging.Inputs.setKeyPressedHandler { e =>
      fn(Utils.getKeyCode(e))
    }
  }

  def onKeyRelease(fn: Int => Unit) = Utils.runInSwingThread {
    staging.Inputs.setKeyReleasedHandler { e =>
      fn(Utils.getKeyCode(e))
    }
  }

  def onMouseClick(fn: (Double, Double) => Unit) = Utils.runInSwingThread {
    staging.Inputs.setMouseClickHandler { event =>
      val pos = event.getPosition
      fn(pos.getX, pos.getY)
    }
  }

  def onMouseDrag(fn: (Double, Double) => Unit) = Utils.runInSwingThread {
    disablePanAndZoom()
    staging.Inputs.setMouseDragHandler { event =>
      val pos = event.getPosition
      fn(pos.getX, pos.getY)
    }
  }

  def onMouseMove(fn: (Double, Double) => Unit) = Utils.runInSwingThread {
    staging.Inputs.setMouseMoveHandler { event =>
      val pos = event.getPosition
      fn(pos.getX, pos.getY)
    }
  }

  var globalEl: PInputEventListener = _
  def addGlobalEventListener(l: PInputEventListener): Unit = {
    globalEl = l
    addInputEventListener(l)
  }

  def activate(): Unit = {
    def grabFocus(): Unit = {
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

  def setBackgroundWrapper(c: Color): Unit = {
    setBackground(c)
    currentBackground = c
    if (currentBackgroundRect != null) {
      currentBackgroundRect.erase()
      currentBackgroundRect = null
    }
  }

  def setCanvasBackground(c: Paint) = Utils.runInSwingThread {
    c match {
      case color: Color if color.getAlpha == 255 =>
        // full screen windows get messed up with transparent color backgrounds
        // so set window background only if color is fully opaque 
        setBackgroundWrapper(color)
      case _ =>
        val bounds = cbounds
        if (currentBackgroundRect != null) {
          currentBackgroundRect.erase()
        }
        val rect = staging.Impl.API.rectangle(bounds.x, bounds.y, bounds.width, bounds.height)
        rect.setFillColor(c)
        rect.setPenThickness(0)
        rect.setPenColor(c)
        currentBackground = c
        currentBackgroundRect = rect
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

  def timer(rate: Long)(fn: => Unit): Future[PActivity] = figure0.refresh(rate, rate)(fn)
  def timerWithState[S](rate: Long, init: S)(code: S => S): Future[PActivity] = {
    var state = init
    timer(rate) {
      state = code(state)
    }
  }
  def animate(fn: => Unit): Future[PActivity] = timer(1000 / kojoCtx.fps)(fn)
  def animateWithState[S](init: S)(code: S => S): Future[PActivity] =
    timerWithState(1000 / kojoCtx.fps, init)(code)

  def animateActivity(a: PActivity) = getRoot.addActivity(a)
  def stopAnimation() = figure0.stopRefresh()
  def stopAnimationActivity(a: Future[PActivity]): Unit = {
    figure0.stopAnimationActivity(a)
  }
  def onAnimationStart(fn: => Unit): Unit = {
    figure0.onStart(fn)
  }
  def onAnimationStop(fn: => Unit): Unit = {
    figure0.onStop(fn)
  }
  def onRunStart(): Unit = {
    figure0.onRunStart()
  }
  def onRunDone(): Unit = {
    figure0.onRunDone()
  }
  def resetPanAndZoom(): Unit = {
    initCamera()
  }

  import core.Picture
  val noPic = picture.Pic { t =>
  }
  @volatile var stage: Picture = _
  @volatile var stageLeft: Picture = _
  @volatile var stageTop: Picture = _
  @volatile var stageRight: Picture = _
  @volatile var stageBot: Picture = _
  @volatile var stageArea: Picture = _

  def clearStage(): Unit = {
    stage = noPic
    stageLeft = noPic
    stageTop = noPic
    stageRight = noPic
    stageBot = noPic
    stageArea = noPic
  }

  def drawStage(fillc: Paint): Unit = {
    def left(size: Double) = picture.strokeWidth(0) -> picture.Pic { t =>
      t.forward(size)
    }
    def top(size: Double) = picture.strokeWidth(0) -> picture.Pic { t =>
      t.right()
      t.forward(size)
    }
    def right(size: Double) = picture.strokeWidth(0) -> picture.Pic { t =>
      t.right(180)
      t.forward(size)
    }
    def bottom(size: Double) = picture.strokeWidth(0) -> picture.Pic { t =>
      t.left()
      t.forward(size)
    }
    val cb = cbounds

    stageLeft = picture.trans(cb.x, cb.y) -> left(cb.height)
    stageTop = picture.trans(cb.x, cb.y + cb.height) -> top(cb.width)
    stageRight = picture.trans(cb.x + cb.width, cb.y + cb.height) -> right(cb.height)
    stageBot = picture.trans(cb.x + cb.width, cb.y) -> bottom(cb.width)
    stageArea = picture.trans(cb.x, cb.y) * picture.fill(fillc) *
      picture.stroke(Color.darkGray) -> picture.rect(cb.height, cb.width)

    stage = picture.GPics(
      stageLeft,
      stageTop,
      stageRight,
      stageBot
    )

    stageArea.draw()
    stage.draw()
  }

  override protected def sendInputEventToInputManager(event: InputEvent, typ: Int): Unit = {
    try {
      super.sendInputEventToInputManager(event, typ)
    }
    catch {
      case _: RuntimeException =>
        // Ignore events that Piccolo is unable to handle
    }
  }

  class Popup() extends JPopupMenu {

    val axesItem = new JCheckBoxMenuItem(Utils.loadString("S_ShowAxes"))
    axesItem.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
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
      override def actionPerformed(e: ActionEvent): Unit = {
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
      override def actionPerformed(e: ActionEvent): Unit = {
        if (protItem.isSelected) {
          showProtractor()
        }
        else {
          hideProtractor()
        }
      }
    })
    add(protItem)

    val scaleItem = new JCheckBoxMenuItem(Utils.loadString("S_ShowScale"))
    scaleItem.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        if (scaleItem.isSelected) {
          showScale()
        }
        else {
          hideScale()
        }
      }
    })
    add(scaleItem)

    val saveAsImage = new JMenuItem(Utils.loadString("S_SaveAsImage"))
    saveAsImage.addActionListener(new ActionListener {
      val fchooser = new FileChooser(kojoCtx)
      override def actionPerformed(e: ActionEvent): Unit = {
        val file = fchooser.chooseFile(Utils.stripDots(Utils.loadString("S_SaveAs")), "PNG Image File", "png")
        if (file != null) {
          Utils.appProperty("export.image.dpi") match {
            case Some(sDpi) =>
              val dpi = sDpi.toInt
              val dim = Utils.appProperty("export.image.dimension").getOrElse("height")
              val inches = Utils.appProperty("export.image.inches") match {
                case Some(inchVal) =>
                  PaperSize.fromString(inchVal) match {
                    case Some(ps) => if (dim == "height") ps.heightInches else ps.widthInches
                    case None     => inchVal.toDouble
                  }
                case None => 1.0
              }

              if (dim == "height") {
                val height = math.round(dpi * inches).toInt
                exportImageToFile(file, widthForHeight(height), height)
              }
              else if (dim == "width") {
                val width = math.round(dpi * inches).toInt
                exportImageToFile(file, width, heightForWidth(width))
              }

            case None =>
              exportImageToFile(file, SpriteCanvas.this.getWidth, SpriteCanvas.this.getHeight)
          }
        }
      }
    })
    add(saveAsImage)

    addSeparator()

    val resetPanZoomItem = new JMenuItem(Utils.loadString("S_ResetPanZoom"))
    resetPanZoomItem.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        initCamera()
      }
    })
    add(resetPanZoomItem)
    val clearItem = new JMenuItem(Utils.loadString("S_Clear"))
    clearItem.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
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
      def popupMenuWillBecomeVisible(e: PopupMenuEvent): Unit = {
        axesItem.setState(_showAxes)
        gridItem.setState(_showGrid)
        protItem.setState(_showProt)
        scaleItem.setState(_showScale)
        kojoCtx.updateMenuItem(fullScreenItem, fsCanvasAction)
      }
      def popupMenuWillBecomeInvisible(e: PopupMenuEvent): Unit = {}
      def popupMenuCanceled(e: PopupMenuEvent): Unit = {}
    })
  }
}
