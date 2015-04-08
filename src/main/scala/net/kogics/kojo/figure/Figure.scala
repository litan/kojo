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
package figure

import edu.umd.cs.piccolo._
import edu.umd.cs.piccolo.nodes._
import edu.umd.cs.piccolo.util._
import edu.umd.cs.piccolo.event._
import edu.umd.cs.piccolo.activities.PActivity
import edu.umd.cs.piccolo.activities.PActivity.PActivityDelegate
import javax.swing._
import java.awt.{ Point => _, List => _, _ }
import net.kogics.kojo.util.Utils
import core._
import java.util.concurrent.Future
import java.util.concurrent.FutureTask
import java.util.concurrent.Callable
import net.kogics.kojo.util.FutureResult

object Figure {
  def apply(canvas: SCanvas, initX: Double = 0d, initY: Double = 0): Figure = {
    val fig = Utils.runInSwingThreadAndWait {
      new Figure(canvas, initX, initY)
    }
    fig
  }
}

class Figure private (canvas: SCanvas, initX: Double, initY: Double) {
  private val bgLayer = new PLayer
  private val fgLayer = new PLayer
  private var currLayer = bgLayer

  // if fgLayer is bigger than bgLayer, (re)painting does not happen very cleanly
  // needs a better fix than the one below
  bgLayer.setBounds(-500, -500, 1000, 1000)

  private val camera = canvas.getCamera
  val DefaultColor = Color.red
  val DefaultFillColor: Color = null
  def DefaultStroke = new BasicStroke((2 / canvas.camScale).toFloat, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)
  @volatile private var listener: SpriteListener = NoopSpriteListener

  private var figAnimations: List[PActivity] = Nil
  private var _lineColor: Paint = _
  private var _fillColor: Paint = _
  private var _lineStroke: Stroke = _

  @volatile var startFn: Option[() => Unit] = None
  @volatile var stopFn: Option[() => Unit] = None

  camera.addLayer(camera.getLayerCount - 1, bgLayer)
  camera.addLayer(camera.getLayerCount - 1, fgLayer)
  init()

  def init() {
    bgLayer.setOffset(initX, initY)
    fgLayer.setOffset(initX, initY)
    _lineColor = DefaultColor
    _fillColor = DefaultFillColor
    _lineStroke = DefaultStroke
  }

  def fillColor = Utils.runInSwingThreadAndWait {
    _fillColor
  }

  def lineColor = Utils.runInSwingThreadAndWait {
    _lineColor
  }

  def lineStroke = Utils.runInSwingThreadAndWait {
    _lineStroke
  }

  def repaint() {
    bgLayer.repaint()
    fgLayer.repaint()
  }

  def clear() {
    Utils.runInSwingThread {
      stop()
      bgLayer.removeAllChildren()
      fgLayer.removeAllChildren()
      init()
      repaint()
      stopFn = None
      startFn = None
      canvas.kojoCtx.fps = 50
    }
  }

  def fgClear() {
    Utils.runInSwingThread {
      fgLayer.removeAllChildren()
      repaint()
    }
  }

  def remove() {
    Utils.runInSwingThread {
      camera.removeLayer(bgLayer)
      camera.removeLayer(fgLayer)
    }
  }

  def setPenColor(color: Paint) {
    Utils.runInSwingThread {
      _lineColor = color
    }
  }

  def setPenThickness(t: Double) {
    Utils.runInSwingThread {
      _lineStroke = new BasicStroke(t.toFloat, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)
    }
  }

  def setLineStroke(st: Stroke) {
    Utils.runInSwingThread {
      _lineStroke = st
    }
  }

  def setFillColor(color: Paint) {
    Utils.runInSwingThread {
      _fillColor = color
    }
  }

  def addPnode(node: PNode) = Utils.runInSwingThread {
    pnode(node)
  }

  def pnode(node: PNode) = {
    // needs to be called on swing thread
    if (node.isInstanceOf[PPath]) {
      val p = node.asInstanceOf[PPath]
      p.setPaint(_fillColor)
      p.setStroke(_lineStroke)
      p.setStrokePaint(_lineColor)
    }
    else if (node.isInstanceOf[PText]) {
      val t = node.asInstanceOf[PText]
      t.setTextPaint(_lineColor)
    }
    currLayer.addChild(node)
    currLayer.repaint
    node
  }

  def removePnode(node: PNode): Unit = Utils.runInSwingThread {
    currLayer.removeChild(node)
    currLayer.repaint
  }

  private var pendingAnimations = Vector.empty[PActivity]
  private var inRunner = false

  def onRunStart() = Utils.runLaterInSwingThread {
    inRunner = true
  }

  def handleStartFn() {
    if (figAnimations.isEmpty && startFn.isDefined) {
      startFn.get.apply()
    }
  }

  def onRunDone() = Utils.runLaterInSwingThread {
    inRunner = false
    if (!pendingAnimations.isEmpty) {
      handleStartFn()
    }
    pendingAnimations foreach { figAnimation =>
      figAnimations = figAnimation :: figAnimations
      canvas.animateActivity(figAnimation)
    }

    pendingAnimations = Vector.empty
  }

  def refresh(fn: => Unit): Future[PActivity]  = refresh(1000 / canvas.kojoCtx.fps, 0)(fn)
  def refresh(rate: Long, delay: Long)(fn: => Unit): Future[PActivity] = {
    @volatile var figAnimation: PActivity = null
    val promise = new FutureResult[PActivity]

    Utils.runLaterInSwingThread {
      figAnimation = new PActivity(-1, rate, System.currentTimeMillis + delay) {
        override def activityStep(elapsedTime: Long) {
          currLayer = fgLayer
          try {
            staging.Inputs.activityStep
            fn
            if (isStepping) {
              listener.hasPendingCommands()
            }
          }
          catch {
            case t: Throwable =>
              println("Problem: " + t.toString())
              terminate(PActivity.TERMINATE_AND_FINISH)
              figAnimations = figAnimations filter { _ != this }
          }
          finally {
            //            repaint()
            currLayer = bgLayer
          }
        }
      }

      figAnimation.setDelegate(new PActivityDelegate {
        override def activityStarted(activity: PActivity) {}
        override def activityStepped(activity: PActivity) {}
        override def activityFinished(activity: PActivity) {
          listener.pendingCommandsDone()
        }
      })

      if (inRunner) {
        pendingAnimations = pendingAnimations :+ figAnimation
      }
      else {
        handleStartFn()
        figAnimations = figAnimation :: figAnimations
        canvas.animateActivity(figAnimation)
      }
      promise.set(figAnimation)
    }
    promise
  }

  def stopRefresh() = stop()

  def stop() {
    Utils.runInSwingThread {
      figAnimations.foreach { figAnimation =>
        figAnimation.terminate(PActivity.TERMINATE_AND_FINISH)
      }
      if (!figAnimations.isEmpty && stopFn.isDefined) {
        stopFn.get.apply()
      }
      figAnimations = Nil
      pendingAnimations = Vector.empty
    }
  }

  def stopAnimationActivity(f: Future[PActivity]) {
    Utils.runInSwingThread {
      val figAnimation = f.get
      figAnimation.terminate(PActivity.TERMINATE_AND_FINISH)
      figAnimations = figAnimations.filterNot { _ == figAnimation }
      if (figAnimations.isEmpty && stopFn.isDefined) {
        stopFn.get.apply()
      }
    }
  }

  private[kojo] def setSpriteListener(l: SpriteListener) {
    listener = l
  }

  def onStart(fn: => Unit) = Utils.runInSwingThread {
    startFn = Some(fn _)
  }

  def onStop(fn: => Unit) = Utils.runInSwingThread {
    stopFn = Some(fn _)
  }
}

