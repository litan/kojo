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
package turtle

import java.awt.BasicStroke
import java.awt.Color
import java.awt.Font
import java.awt.Paint
import java.awt.Stroke
import java.awt.geom.Point2D
import java.util.concurrent.CountDownLatch

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

import com.vividsolutions.jts.geom.Coordinate
import com.vividsolutions.jts.geom.GeometryFactory

import net.kogics.kojo.core.Point
import net.kogics.kojo.core.SCanvas
import net.kogics.kojo.core.Style
import net.kogics.kojo.kgeom.PolyLine
import net.kogics.kojo.util.Throttler
import net.kogics.kojo.util.Utils

import TurtleHelper.distance
import TurtleHelper.posAfterForward
import TurtleHelper.thetaAfterTurn
import TurtleHelper.thetaTowards
import edu.umd.cs.piccolo.PLayer
import edu.umd.cs.piccolo.PNode
import edu.umd.cs.piccolo.activities.PActivity
import edu.umd.cs.piccolo.activities.PActivity.PActivityDelegate
import edu.umd.cs.piccolo.nodes.PImage
import edu.umd.cs.piccolo.nodes.PPath
import edu.umd.cs.piccolo.nodes.PText
import music.Music

class Turtle(canvas: SCanvas, costumeFile: String, initX: Double,
             initY: Double, hidden: Boolean = false, bottomLayer: Boolean = false) extends core.Turtle {

  //  private val Log = Logger.getLogger(getClass.getName)
  //  Log.info("Turtle being created in thread: " + Thread.currentThread.getName)

  private val layer = new PLayer
  def tlayer: PLayer = layer
  private val camera = canvas.getCamera
  if (bottomLayer) camera.addLayer(0, layer) else camera.addLayer(layer)
  @volatile private[turtle] var _animationDelay = 0l

  private val turtleImage = new PImage
  private val turtle = new PNode
  def camScale = canvas.camScale

  private val xBeam = PPath.createLine(0, 30, 0, -30)
  xBeam.setStrokePaint(Color.gray)
  private val yBeam = PPath.createLine(-20, 0, 50, 0)
  yBeam.setStrokePaint(Color.gray)

  private[kojo] val penPaths = new mutable.ArrayBuffer[PolyLine]
  private var lineColor: Paint = _
  private var fillColor: Paint = _
  private[kojo] var lineStroke: Stroke = _
  private var font: Font = _

  private val pens = makePens
  private val DownPen = pens._1
  private val UpPen = pens._2
  private[kojo] var pen: Pen = _

  private var theta: Double = _

  private val savedStyles = new mutable.Stack[Style]
  private val savedPosHe = new mutable.Stack[(Point2D.Double, Double)]
  private var isVisible: Boolean = _
  private var areBeamsOn: Boolean = _
  private var forwardAnimation: PActivity = _
  private var stopped = false
  private var costumes: Option[Vector[String]] = None
  private var currCostume = 0

  private[turtle] def changePos(x: Double, y: Double) {
    turtle.setOffset(x, y)
  }

  //  private [turtle] def _position: Point2D.Double = turtle.getOffset.asInstanceOf[Point2D.Double]
  private def _positionX = turtle.getXOffset
  private def _positionY = turtle.getYOffset

  def position: Point = Utils.runInSwingThreadAndWait {
    new Point(_positionX, _positionY)
  }

  private def changeHeading(newTheta: Double) {
    theta = newTheta
    turtle.setRotation(theta)
  }

  def distanceTo(x: Double, y: Double): Double = {
    distance(_positionX, _positionY, x, y)
  }

  private def towardsHelper(x: Double, y: Double): Double = {
    thetaTowards(_positionX, _positionY, x, y, theta)
  }

  def delayFor(dist: Double): Long = {
    if (_animationDelay < 1) {
      return _animationDelay
    }

    // _animationDelay is delay for 100 steps;
    // Here we calculate delay for specified distance
    val speed = 100f / _animationDelay
    val delay = Math.abs(dist) / speed
    delay.round
  }

  def initTImage(costumeFile: String) {
    turtleImage.setImage(Utils.loadImageC(costumeFile))
    turtleImage.getTransformReference(true).setToIdentity()
    turtleImage.getTransformReference(true).setToScale(1 / camScale, -1 / camScale)
    turtleImage.rotate(Utils.deg2radians(90))
    turtleImage.translate(-turtleImage.getWidth / 2, -turtleImage.getHeight / 2)
  }

  private[turtle] def init() {
    _animationDelay = 1000l
    changePos(initX, initY)
    initTImage(costumeFile)
    layer.addChild(turtle)

    pen = DownPen
    pen.init()
    resetRotation()

    if (hidden) {
      hideWorker()
    }
    else {
      showWorker()
    }
    beamsOffWorker()
  }

  init()

  private def thetaDegrees = Utils.rad2degrees(theta)
  private def thetaRadians = theta

  def turn(angle: Double) = Utils.runInSwingThread {
    realTurn(angle)
  }

  def animationDelay = _animationDelay

  def heading: Double = Utils.runInSwingThreadAndWait {
    thetaDegrees
  }

  def style: Style = Utils.runInSwingThreadAndWait {
    currStyle
  }

  private def currStyle = Style(pen.getColor, pen.getThickness, pen.getFillColor, pen.getFont, pen == DownPen)

  private def pointAfterForward(n: Double) = {
    val p1 = posAfterForward(_positionX, _positionY, theta, n)
    new Point2D.Double(p1._1, p1._2)
  }

  private def endForwardMove(pf: Point2D.Double) {
    pen.endMove(pf.x, pf.y)
    changePos(pf.x, pf.y)
    turtle.repaint()
  }

  // to be called on swing thread
  private def forwardNoAnim(n: Double) {
    endForwardMove(pointAfterForward(n))
  }

  def forward(n: Double): Unit = {
    if (Utils.inSwingThread) {
      forwardNoAnim(n)
      return
    }

    val aDelay = delayFor(n)

    if (aDelay < 10) {
      if (aDelay > 1) {
        Thread.sleep(aDelay)
      }
      else {
        Throttler.throttle()
      }
      Utils.runInSwingThread {
        forwardNoAnim(n)
      }
    }
    else {
      val latch = new CountDownLatch(1)
      Utils.runInSwingThread {
        def endAnim() {
          forwardAnimation = null
          stopped = false
        }

        val p0x = _positionX
        val p0y = _positionY
        val pf = pointAfterForward(n)
        pen.startMove(p0x, p0y)

        forwardAnimation = new PActivity(aDelay) {
          override def activityStep(elapsedTime: Long) {
            val frac = elapsedTime.toDouble / aDelay
            val currX = p0x * (1 - frac) + pf.x * frac
            val currY = p0y * (1 - frac) + pf.y * frac
            pen.move(currX, currY)
            changePos(currX, currY)
            turtle.repaint()
          }
        }

        forwardAnimation.setDelegate(new PActivityDelegate {
          override def activityStarted(activity: PActivity) {}
          override def activityStepped(activity: PActivity) {}
          override def activityFinished(activity: PActivity) {
            if (stopped) {
              val cpos = turtle.getOffset
              endForwardMove(cpos.asInstanceOf[Point2D.Double])
              endAnim()
              latch.countDown()
            }
            else {
              endForwardMove(pf)
              endAnim()
              latch.countDown()
            }
          }
        })
        canvas.animateActivity(forwardAnimation)
      }
      latch.await()
    }
  }

  private def realTurn(angle: Double) {
    val newTheta = thetaAfterTurn(angle, theta)
    changeHeading(newTheta)
    turtle.repaint()
  }

  def clear() = Utils.runInSwingThread {
    pen.clear()
    layer.removeAllChildren() // get rid of stuff not written by pen, like text nodes
    turtle.getTransformReference(true).setToIdentity()
    costumes = None
    currCostume = 0
    init()
    turtle.repaint()
  }

  // called for non-default turtles 
  def remove() = Utils.runInSwingThread {
    camera.removeLayer(layer)
    // this causes a very rare exeception for a subsequent endMove triggered by an earlier stop
    // so schedule memory reclamation for later
    // we're reclaiming memory by clearing turtle fields because there's a reference to turtles 
    // from the Piccolo animation timer, which makes them leak 
    // you can test all of this with VisualVM!
    Utils.schedule(60) {
      pen.clear()
      layer.removeAllChildren()
    }
  }

  def penUp() = Utils.runInSwingThread {
    pen = UpPen
  }

  def penDown() = Utils.runInSwingThread {
    if (pen != DownPen) {
      pen = DownPen
      pen.updatePosition()
    }
  }

  def towards(x: Double, y: Double) = {
    Throttler.throttleHard()
    Utils.runInSwingThread {
      val newTheta = towardsHelper(x, y)
      changeHeading(newTheta)
      turtle.repaint()
    }
  }

  def jumpTo(x: Double, y: Double) = {
    Throttler.throttleHard()
    Utils.runInSwingThread {
      jumpToHelper(x, y)
    }
  }

  private def jumpToHelper(x: Double, y: Double) {
    changePos(x, y)
    pen.updatePosition()
    turtle.repaint()
  }

  def moveTo(x: Double, y: Double) {
    if (_animationDelay < 5) {
      Throttler.throttle()
      Utils.runInSwingThread {
        val newTheta = towardsHelper(x, y)
        changeHeading(newTheta)
        val d = distanceTo(x, y)
        forwardNoAnim(d)
      }
    }
    else {
      val d = Utils.runInSwingThreadAndWait {
        val newTheta = towardsHelper(x, y)
        changeHeading(newTheta)
        distanceTo(x, y)
      }
      forward(d)
    }
  }

  def setAnimationDelay(d: Long) {
    if (d < 0) {
      throw new IllegalArgumentException("Negative delay not allowed")
    }
    // set it right here, as opposed to in the swing thread
    // because all users of _animation delay uses it within the calling thread 
    // users are forward, arc, and moveTo
    _animationDelay = d
  }

  def setPenColor(color: Paint) = Utils.runInSwingThread {
    pen.setColor(color)
  }

  def setPenThickness(t: Double) {
    if (t < 0) {
      throw new IllegalArgumentException("Negative thickness not allowed")
    }
    Utils.runInSwingThread {
      pen.setThickness(t)
    }
  }

  def setPenFontSize(n: Int) {
    if (n < 0) {
      throw new IllegalArgumentException("Negative font size not allowed")
    }
    Utils.runInSwingThread {
      pen.setFontSize(n)
    }
  }

  def setPenFont(font: Font) {
    Utils.runInSwingThread {
      pen.setFont(font)
    }
  }

  def setFillColor(color: Paint) = Utils.runInSwingThread {
    pen.setFillColor(color)
  }

  def saveStyle() = Utils.runInSwingThread {
    savedStyles.push(currStyle)
  }

  def savePosHe() = Utils.runInSwingThread {
    savedPosHe.push((new Point2D.Double(_positionX, _positionY), theta))
  }

  def restoreStyle() = Utils.runInSwingThread {
    if (savedStyles.size == 0) {
      throw new IllegalStateException("No saved style to restore")
    }
    val style = savedStyles.pop()
    if (style.down) {
      pen = DownPen
    }
    else {
      pen = UpPen
    }
    pen.setStyle(style)
  }

  def restorePosHe() = Utils.runInSwingThread {
    if (savedPosHe.size == 0) {
      throw new IllegalStateException("No saved Position and Heading to restore")
    }
    val (p, h) = savedPosHe.pop()
    jumpTo(p.x, p.y)
    changeHeading(h)
  }

  private def beamsOnWorker() {
    if (!areBeamsOn) {
      turtle.addChild(0, xBeam)
      turtle.addChild(1, yBeam)
      turtle.repaint()
      areBeamsOn = true
    }
  }

  private def beamsOffWorker() {
    if (areBeamsOn) {
      turtle.removeChild(xBeam)
      turtle.removeChild(yBeam)
      turtle.repaint()
      areBeamsOn = false
    }
  }

  def beamsOn() = Utils.runInSwingThread {
    beamsOnWorker()
  }

  def beamsOff() = Utils.runInSwingThread {
    beamsOffWorker()
  }

  def write(text: String) = Utils.runInSwingThread {
    pen.write(text)
  }

  def invisible() = Utils.runInSwingThread {
    hideWorker()
  }

  def visible() = Utils.runInSwingThread {
    showWorker()
  }

  private def hideWorker() {
    if (isVisible) {
      turtle.removeChild(turtleImage)
      beamsOffWorker()
      turtle.repaint()
      isVisible = false
    }
  }

  private def showWorker() {
    if (!isVisible) {
      turtle.addChild(turtleImage)
      turtle.repaint()
      isVisible = true
    }
  }

  def playSound(voice: core.Voice) = Utils.runInSwingThread {
    try {
      Music(voice).play()
    }
    catch {
      case e: Exception => println("Turtle Error while playing sound:\n" + e.getMessage)
    }
  }

  def arc(r: Double, a: Int) {
    def makeArc(lforward: Double => Unit, lturn: Double => Unit) {
      var i = 0
      val (lim, step, trn) = if (a > 0) (a, 2 * math.Pi * r / 360, 1) else (-a, 2 * math.Pi * r / 360, -1)
      while (i < lim) {
        lforward(step)
        lturn(trn)
        i += 1
      }
    }

    if (_animationDelay < 5) {
      Throttler.throttle()
      Utils.runInSwingThread {
        makeArc(forwardNoAnim _, realTurn _)
      }
    }
    else {
      makeArc(forward _, turn _)
    }
  }

  def setCostume(costumeFile: String) = {
    Throttler.throttleHard()
    Utils.runInSwingThread {
      setCostumeHelper(costumeFile)
    }
  }

  private def setCostumeHelper(costumeFile: String) = {
    initTImage(costumeFile)
    turtleImage.repaint()
  }

  private def resetRotation() {
    changeHeading(Utils.deg2radians(90))
  }

  private[kojo] def stop() = Utils.runInSwingThread {
    if (forwardAnimation != null) {
      stopped = true
      forwardAnimation.terminate(PActivity.TERMINATE_AND_FINISH)
    }
  }

  private def makePens(): (Pen, Pen) = {
    val downPen = new DownPen()
    val upPen = new UpPen()
    (downPen, upPen)
  }

  def setCostumes(costumeFiles: Vector[String]) = {
    require(costumeFiles.length > 1, "You need to specify at least two costumes")
    Throttler.throttleHard()
    Utils.runInSwingThread {
      setCostumeHelper(costumeFiles(0))
      costumes = Some(costumeFiles)
      currCostume = 0
    }
  }

  def nextCostume() = {
    Throttler.throttleHard()
    Utils.runInSwingThread {
      costumes foreach { cseq =>
        currCostume = if (currCostume == cseq.length - 1) 0 else currCostume + 1
        setCostumeHelper(cseq(currCostume))
      }
    }
  }

  def scaleCostume(f: Double) = Utils.runInSwingThread {
    turtle.scale(f)
    layer.repaint()
  }

  def changePosition(x: Double, y: Double) = {
    Throttler.throttleHard()
    Utils.runInSwingThread {
      jumpToHelper(_positionX + x, _positionY + y)
    }
  }

  def react(fn: core.Turtle => Unit) {
    canvas.animate {
      fn(this)
    }
  }

  def distanceTo(other: core.Turtle) = Utils.runInSwingThreadAndWait {
    val otherPos = other.position
    distanceTo(otherPos.x, otherPos.y)
  }

  lazy val Gf = new GeometryFactory
  private def coords(pl: PolyLine) = pl.points.map { pt => new Coordinate(pt.x, pt.y) }

  def perimeter = Utils.runInSwingThreadAndWait {
    penPaths.foldLeft(0.0) { (peri, pl) =>
      peri + { if (pl.size < 2) 0 else Gf.createLineString(coords(pl).toArray).getLength }
    }
  }

  def area = Utils.runInSwingThreadAndWait {
    def polyArea(pl: PolyLine) = {
      val gc = coords(pl)
      gc += gc(0)
      try {
        val polygon = Gf.createPolygon(Gf.createLinearRing(gc.toArray), null)
        polygon.getArea
      }
      catch {
        case ex: IllegalArgumentException => 0
      }
    }

    penPaths.foldLeft(0.0) { (area, pl) =>
      area + polyArea(pl)
    }
  }

  def dumpState() {
    Utils.runInSwingThread {
      val cIter = layer.getChildrenReference.iterator
      println("Turtle Layer (%d children):\n" format (layer.getChildrenReference.size))
      while (cIter.hasNext) {
        val node = cIter.next.asInstanceOf[PNode]
        println(stringRep(node))
      }
    }
  }

  private def stringRep(node: PNode): String = node match {
    case l: PolyLine =>
      new StringBuilder().append("  Polyline:\n").append("    Points: %s\n" format l.points).toString
    case n: PNode =>
      new StringBuilder().append("  PNode:\n").append("    Children: %s\n" format n.getChildrenReference).toString
  }

  abstract class AbstractPen extends Pen {
    //    val Log = Logger.getLogger(getClass.getName);

    val turtle = Turtle.this
    val CapThick = BasicStroke.CAP_ROUND
    val CapThin = BasicStroke.CAP_BUTT
    val JoinThick = BasicStroke.JOIN_ROUND
    val JoinThin = BasicStroke.JOIN_BEVEL
    val DefaultColor = Color.red
    val DefaultFillColor = null
    def DefaultStroke = {
      val t = 2 / camScale
      val (cap, join) = capJoin(t)
      new BasicStroke(t.toFloat, cap, join)
    }
    val DefaultFont = new Font(new PText().getFont.getName, Font.PLAIN, 18)

    private def capJoin(t: Double) = {
      val Cap = if (t * camScale < 1) CapThin else CapThick
      val Join = if (t * camScale < 1) JoinThin else JoinThick
      (Cap, Join)
    }

    def init() {
      lineColor = DefaultColor
      fillColor = DefaultFillColor
      lineStroke = DefaultStroke
      font = DefaultFont
      addNewPath()
    }

    def newPath(): PolyLine = {
      val penPath = new PolyLine()
      penPath.addPoint(_positionX, _positionY)
      penPath.setStroke(lineStroke)
      penPath.setStrokePaint(lineColor)
      penPath.setPaint(fillColor)
      penPath
    }

    protected def addNewPath() {
      val penPath = newPath()
      penPaths += penPath
      layer.addChild(layer.getChildrenCount - 1, penPath)
    }

    protected def removeLastPath() {
      val penPath = penPaths.last
      penPaths.remove(penPaths.size - 1)
      layer.removeChild(penPath)
    }

    def getColor = lineColor
    def getFillColor = fillColor
    def getThickness = lineStroke.asInstanceOf[BasicStroke].getLineWidth
    def getFontSize = font.getSize
    def getFont = font

    private def rawSetAttrs(color: Paint, thickness: Double, fColor: Paint, font0: Font) {
      lineColor = color
      val (cap, join) = capJoin(thickness)
      lineStroke = new BasicStroke(thickness.toFloat, cap, join)
      fillColor = fColor
      font = font0
    }

    def setColor(color: Paint) {
      lineColor = color
      addNewPath()
    }

    def setThickness(t: Double) {
      val (cap, join) = capJoin(t)
      lineStroke = new BasicStroke(t.toFloat, cap, join)
      addNewPath()
    }

    def setFontSize(n: Int) {
      setFont(new Font(font.getName, font.getStyle, n))
    }

    def setFont(f: Font) {
      font = f
      addNewPath()
    }

    def setFillColor(color: Paint) {
      fillColor = color
      addNewPath()
    }

    def setStyle(style: Style) {
      rawSetAttrs(style.penColor, style.penThickness, style.fillColor, style.font)
      addNewPath()
    }

    def clear() = {
      penPaths.foreach { penPath =>
        penPath.reset()
        layer.removeChild(penPath)
      }
      penPaths.clear()
    }
  }

  class UpPen extends AbstractPen {
    def startMove(x: Double, y: Double) {}
    def move(x: Double, y: Double) {}
    def endMove(x: Double, y: Double) {}
    def updatePosition() {}
    def write(text: String) {}
  }

  class DownPen extends AbstractPen {
    var tempLine = new PPath
    val lineAnimationColor = Color.orange

    def startMove(x: Double, y: Double) {
      tempLine.setStroke(lineStroke)
      tempLine.setStrokePaint(lineAnimationColor)
      tempLine.moveTo(x.toFloat, y.toFloat)
      layer.addChild(layer.getChildrenCount - 1, tempLine)
    }
    def move(x: Double, y: Double) {
      tempLine.lineTo(x.toFloat, y.toFloat)
      tempLine.repaint()
    }
    def endMove(x: Double, y: Double) {
      layer.removeChild(tempLine)
      tempLine.reset
      penPaths.last.lineTo(x, y)
      penPaths.last.repaint()
    }

    def updatePosition() {
      addNewPath()
    }

    def write(text: String) {
      val ptext = Utils.textNode(text, _positionX, _positionY, canvas.camScale)
      ptext.setFont(font)
      ptext.setTextPaint(pen.getColor)
      if (!Utils.doublesEqual(90.toRadians, theta, 0.001)) {
        ptext.setRotation(90.toRadians - theta)
      }
      layer.addChild(layer.getChildrenCount - 1, ptext)
      ptext.repaint()
    }
  }
}