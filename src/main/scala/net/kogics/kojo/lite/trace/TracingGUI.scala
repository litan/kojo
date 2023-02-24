/*
 * Copyright (C) 2013 "Sami Jaber" <jabersami@gmail.com>
 * Copyright (C) 2013 Lalit Pant <pant.lalit@gmail.com>
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
package trace

import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.geom.Point2D
import java.awt.Color
import java.awt.GradientPaint
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JSplitPane
import javax.swing.JTextArea
import javax.swing.SwingConstants

import scala.collection.mutable.ArrayBuffer

import bibliothek.gui.dock.common.event.CDockableStateListener
import bibliothek.gui.dock.common.intern.CDockable
import bibliothek.gui.dock.common.mode.ExtendedMode
import net.kogics.kojo.core.Picture
import net.kogics.kojo.lite.topc.TraceHolder
import net.kogics.kojo.lite.ScriptEditor
import net.kogics.kojo.picture.GPics
import net.kogics.kojo.util.Utils

class TracingGUI(scriptEditor: ScriptEditor, kojoCtx: core.KojoCtx) {
  val events: JPanel = new JPanel
  events.setLayout(new BoxLayout(events, BoxLayout.Y_AXIS))
  events.setBackground(Theme.currentTheme.tracingBg)
  val traceHolder = new TraceHolder(null)
  traceHolder.setCloseable(true)
  traceHolder.setMaximizable(false)
  traceHolder.setMinimizable(false)
  traceHolder.setExternalizable(false)

  @volatile var currMarker: Option[Picture] = None
  @volatile var currPicsMarker = Vector[Picture]()
  @volatile var currUiElems = new ArrayBuffer[JComponent](0)
  val markingClr = new GradientPaint(0, 0, Color.black, 5, 5, Color.yellow, true)
  @volatile var eventDesc: JTextArea = _
  var eventHolder: JSplitPane = _
  val highlightColor = Theme.currentTheme.tracingHighlightColor

  traceHolder.addCDockableStateListener(new CDockableStateListener {
    def visibilityChanged(dockable: CDockable): Unit = {
      if (!dockable.isVisible) {
        events.removeAll()
        traceHolder.remove(eventHolder)
        eventHolder = null
      }
    }
    def extendedModeChanged(dockable: CDockable, mode: ExtendedMode): Unit = {}
  })

  def reset() = Utils.runInSwingThread {
    events.removeAll()
    eventDesc = new JTextArea("Click on an Event to see its details.")
    eventDesc.setEditable(false)
    eventDesc.setLineWrap(true)
    eventDesc.setWrapStyleWord(true)

    if (eventHolder != null) {
      traceHolder.remove(eventHolder)
    }

    eventHolder = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(events), new JScrollPane(eventDesc))
    eventHolder.setDividerLocation(500)
    eventHolder.setOneTouchExpandable(true)

    traceHolder.add(eventHolder)
    kojoCtx.makeTraceWindowVisible(traceHolder)
  }

  def addEnterEvent(me: MethodEvent): Unit = {
    addEvent(me)
  }

  def addExitEvent(me: MethodEvent): Unit = {
    addEvent(me)
  }

  private def addEvent(me: MethodEvent): Unit = {
    @annotation.nowarn
    def meSubLines(me: MethodEvent): Seq[(Point2D.Double, Point2D.Double)] = {
      def nodeSeq = me.turtlePoints match {
        case Some(tp) => Vector(tp)
        case None     => Vector()
      }

      me.subcalls match {
        case Seq()   => nodeSeq
        case x +: xs => nodeSeq ++ meSubLines(x) ++ (xs.flatMap(meSubLines))

      }
    }

    @annotation.nowarn
    def meSubTurns(me: MethodEvent): Seq[(Point2D.Double, Double, Double)] = {
      def nodeSeq = me.turtleTurn match {
        case Some(tt) => Vector(tt)
        case None     => Vector()
      }

      me.subcalls match {
        case Seq()   => nodeSeq
        case x +: xs => nodeSeq ++ meSubTurns(x) ++ (xs.flatMap(meSubTurns))
      }
    }

    def mePictures(me: MethodEvent): Vector[Picture] = me.picture match {
      case Some(p) => Vector(p)
      case None    => me.subcalls.flatMap { mePictures(_) }
    }

    val meDesc = me.toString
    val uiLevel = me.level
    val ended = me.ended
    val taText = if (ended) me.exit(uiLevel) else me.entry(uiLevel)
    val lineNum = if (ended) me.exitLineNum else me.entryLineNum

    Utils.runInSwingThread {
      val te = new JButton(taText) {
        setBackground(Theme.currentTheme.tracingBg)
        setHorizontalAlignment(SwingConstants.LEFT)
        me.uiElems += this

        addMouseListener(new MouseAdapter {
          override def mousePressed(e: MouseEvent): Unit = {
            eventDesc.setText(meDesc)
            Utils.runLaterInSwingThread {
              Utils.scrollToOffset(0, eventDesc)
            }
            if (ended && me.sourceName == "scripteditor" && lineNum > 0) {
              scriptEditor.markTraceLine(lineNum)
            }
            else if (me.callerSourceName == "scripteditor") {
              scriptEditor.markTraceLine(me.callerLineNum)
            }
            else {
              scriptEditor.markTraceLine(-1)
            }

            currMarker.foreach { _.erase() }
            currPicsMarker.foreach { p => p.setPenColor(Color.red); p.setPenThickness(2) }
            kojoCtx.repaintCanvas()
            currMarker = None

            currPicsMarker = mePictures(me)
            if (currPicsMarker.size > 0) {
              currPicsMarker.foreach { p => p.setPenColor(markingClr); p.setPenThickness(6) }
            }
            else {
              val subLines = meSubLines(me)
              if (subLines.size > 0) {
                if (subLines.size < 375) {
                  val picCol = new ArrayBuffer[Picture]
                  subLines.foreach { ll =>
                    val pic1 = picture.stroke(Color.black) * picture.strokeWidth(10) -> kojoCtx.picLine(ll._1, ll._2)
                    val pic2 = picture.stroke(Color.yellow) * picture.strokeWidth(4) -> kojoCtx.picLine(ll._1, ll._2)
                    val marker = GPics(pic1, pic2)
                    picCol += marker
                  }
                  val markerPic = GPics(picCol.toList)
                  currMarker = Some(markerPic)
                  markerPic.draw()
                }
                else {
                  var minx, miny = Double.MaxValue
                  var maxx, maxy = Double.MinValue

                  def processBounds(p: Point2D.Double): Unit = {
                    if (p.x < minx) minx = p.x
                    else if (p.x > maxx) maxx = p.x

                    if (p.y < miny) miny = p.y
                    else if (p.y > maxy) maxy = p.y
                  }

                  subLines.foreach { ll =>
                    val p1 = ll._1; val p2 = ll._2
                    processBounds(p1)
                    processBounds(p2)
                  }
                  val p1 = new Point2D.Double(minx, miny)
                  val p2 = new Point2D.Double(minx, maxy)
                  val p3 = new Point2D.Double(maxx, maxy)
                  val p4 = new Point2D.Double(maxx, miny)
                  val pic1 = picture.stroke(Color.black) -> kojoCtx.picLine(p1, p2)
                  val pic2 = picture.stroke(Color.yellow) -> kojoCtx.picLine(p2, p3)
                  val pic3 = picture.stroke(Color.black) -> kojoCtx.picLine(p3, p4)
                  val pic4 = picture.stroke(Color.yellow) -> kojoCtx.picLine(p4, p1)
                  val markerPic = picture.strokeWidth(5) -> GPics(pic1, pic2, pic3, pic4)
                  currMarker = Some(markerPic)
                  markerPic.draw()
                }
              }
              else {
                val subTurns = meSubTurns(me)
                def turnPic(turn: (Point2D.Double, Double, Double)) = {
                  val pos = turn._1; val oldTheta = turn._2; val newTheta = turn._3
                  val arm11 = picture.stroke(Color.black) * picture.strokeWidth(10) * picture.rotp(
                    Utils.rad2degrees(oldTheta),
                    pos.x,
                    pos.y
                  ) -> kojoCtx.picLine(pos, new Point2D.Double(pos.x + 25, pos.y))
                  val arm12 = picture.stroke(Color.yellow) * picture.strokeWidth(4) * picture.rotp(
                    Utils.rad2degrees(oldTheta),
                    pos.x,
                    pos.y
                  ) -> kojoCtx.picLine(pos, new Point2D.Double(pos.x + 25, pos.y))
                  val arm21 = picture.stroke(Color.yellow) * picture.strokeWidth(10) * picture.rotp(
                    Utils.rad2degrees(newTheta),
                    pos.x,
                    pos.y
                  ) -> kojoCtx.picLine(pos, new Point2D.Double(pos.x + 25, pos.y))
                  val arm22 = picture.stroke(Color.black) * picture.strokeWidth(4) * picture.rotp(
                    Utils.rad2degrees(newTheta),
                    pos.x,
                    pos.y
                  ) -> kojoCtx.picLine(pos, new Point2D.Double(pos.x + 25, pos.y))
                  picture.strokeWidth(5) -> GPics(arm11, arm12, arm21, arm22)
                }
                if (subTurns.size > 0) {
                  val picCol = new ArrayBuffer[Picture]
                  subTurns.foreach { turn =>
                    picCol += turnPic(turn)
                  }
                  val markerPic = GPics(picCol.toList)
                  currMarker = Some(markerPic)
                  markerPic.draw()
                }
              }
            }
          }
          override def mouseReleased(e: MouseEvent): Unit = {
            currUiElems.foreach { _.setBackground(Theme.currentTheme.tracingBg) }
            me.uiElems.foreach { _.setBackground(highlightColor) }
            currUiElems = me.uiElems
          }
        })
      }

      events.add(te)
      events.add(Box.createVerticalStrut(3))
      events.revalidate()
    }
  }
}
