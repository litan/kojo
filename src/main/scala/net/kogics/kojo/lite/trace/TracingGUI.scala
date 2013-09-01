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

import java.awt.Color
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.geom.Point2D
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JSplitPane
import javax.swing.JTextArea
import javax.swing.SwingConstants
import scala.collection.mutable.ArrayBuffer
import net.kogics.kojo.core.Picture
import net.kogics.kojo.lite.ScriptEditor
import net.kogics.kojo.lite.topc.TraceHolder
import net.kogics.kojo.picture.GPics
import net.kogics.kojo.util.Utils
import net.kogics.kojo.kgeom.PolyLine

class TracingGUI(scriptEditor: ScriptEditor, kojoCtx: core.KojoCtx) {
  val events: JPanel = new JPanel
  events.setLayout(new BoxLayout(events, BoxLayout.Y_AXIS))
  events.setBackground(Color.white)
  val traceHolder = new TraceHolder(null)
  traceHolder.setCloseable(true)
  traceHolder.setMaximizable(false)
  traceHolder.setMinimizable(false)
  traceHolder.setExternalizable(false)

  @volatile var currMarker: Option[Picture] = None
  @volatile var currPicMarker: Option[Picture] = None
  @volatile var eventDesc: JTextArea = _
  var eventHolder: JSplitPane = _

  def reset() {
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

  def addStartEvent(me: MethodEvent) {
    def findSubLines(me: MethodEvent): Seq[(Point2D.Double, Point2D.Double)] = {
      def nodeSeq = me.turtlePoints match {
        case Some(tp) => Vector(tp)
        case None     => Vector()
      }

      me.subcalls match {
        case Seq()           => nodeSeq
        case Seq(x, xs @ _*) => nodeSeq ++ findSubLines(x) ++ (xs flatMap findSubLines)

      }
    }

    addEvent(me, findSubLines(me))
  }

  def addEndEvent(me: MethodEvent) {
    addEvent(me, Nil)
  }

  private def addEvent(me: MethodEvent, oll: => Seq[(Point2D.Double, Point2D.Double)]) = {
    lazy val subLines = oll
    val meDesc = me.toString
    val uiLevel = me.level + 1
    val ended = me.ended
    val taText = if (ended) me.exit(uiLevel) else me.entry(uiLevel)
    val lineNum = if (ended) me.exitLineNum else me.entryLineNum

    Utils.runInSwingThread {
      val te = new JButton(taText) {
        setBackground(Color.white)
        setHorizontalAlignment(SwingConstants.LEFT)

        addMouseListener(new MouseAdapter {
          override def mouseClicked(e: MouseEvent) {
            eventDesc.setText(meDesc)
            Utils.runLaterInSwingThread {
              Utils.scrollToOffset(0, eventDesc)
            }
            if (ended && me.sourceName == "scripteditor" && lineNum > 0)
              scriptEditor.markTraceLine(lineNum)
            else
              scriptEditor.markTraceLine(me.callerLineNum)

            currMarker foreach { _.erase() }
            currPicMarker foreach { p => p.setPenColor(Color.red); p.setPenThickness(2) }
            kojoCtx.repaintCanvas()

            if (me.picture.isDefined) {
              val markerPic = me.picture.get
              currPicMarker = me.picture
              markerPic.setPenColor(Color.black)
              markerPic.setPenThickness(6)
            }
            else {
              if (subLines.size < 50) {
                val picCol = new ArrayBuffer[Picture]
                subLines foreach { ll =>
                  val pic1 = picture.stroke(Color.black) * picture.strokeWidth(10) -> kojoCtx.picLine(ll._1, ll._2)
                  val pic2 = picture.stroke(Color.yellow) * picture.strokeWidth(4) -> kojoCtx.picLine(ll._1, ll._2)
                  val marker = GPics(pic1, pic2)
                  picCol += marker
                }
                if (picCol.size > 0) {
                  val markerPic = GPics(picCol.toList)
                  currMarker = Some(markerPic)
                  markerPic.draw()
                }
                else {
                  currMarker = None
                }
              }
              else {
                var minx, miny = Double.MaxValue
                var maxx, maxy = Double.MinValue

                subLines foreach { ll =>
                  val p1 = ll._1; val p2 = ll._2
                  def processBounds(p: Point2D.Double) {
                    if (p.x < minx) minx = p.x
                    else if (p.x > maxx) maxx = p.x

                    if (p.y < miny) miny = p.y
                    else if (p.y > maxy) maxy = p.y
                  }
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
                currMarker = Some(picture.strokeWidth(5) -> GPics(pic1, pic2, pic3, pic4))
                currMarker foreach { _.draw() }
              }
            }
          }
        })
      }

      events.add(te)
      events.revalidate()
    }
  }
}