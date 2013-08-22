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

import net.kogics.kojo.core.Picture
import net.kogics.kojo.lite.ScriptEditor
import net.kogics.kojo.lite.topc.TraceHolder
import net.kogics.kojo.picture.GPics
import net.kogics.kojo.util.Utils

class TracingGUI(scriptEditor: ScriptEditor, kojoCtx: core.KojoCtx) {
  val events: JPanel = new JPanel
  events.setLayout(new BoxLayout(events, BoxLayout.Y_AXIS))
  events.setBackground(Color.white)
  val traceHolder = new TraceHolder(null)
  traceHolder.setCloseable(true)
  traceHolder.setMaximizable(false)
  traceHolder.setMinimizable(false)
  traceHolder.setExternalizable(false)

  var currMarker: Option[Picture] = None
  var eventDesc: JTextArea = _
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
    def findLine(me: MethodEvent): Option[(Point2D.Double, Point2D.Double)] = {
      me.turtlePoints match {
        case tp @ Some(_) => tp
        case None         => findLine2(me.subcalls)
      }
    }

    def findLine2(scs: Seq[MethodEvent]): Option[(Point2D.Double, Point2D.Double)] = scs match {
      case Seq() => None
      case Seq(x, xs @ _*) => findLine(x) match {
        case tp @ Some(_) => tp
        case None         => findLine2(xs)
      }
    }
    addEvent(me, findLine(me))
  }
  
  def addEndEvent(me: MethodEvent) {
    addEvent(me, None)
  }
  
  private def addEvent(me: MethodEvent, oll: => Option[(Point2D.Double, Point2D.Double)]) = {
    lazy val lastLine = oll
    val meDesc = me.toString
    val uiLevel = me.level + 1
    val taText = if (me.ended) me.exit(uiLevel) else me.entry(uiLevel)
    val lineNum = if (me.ended) me.exitLineNum else me.entryLineNum

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
            if (me.sourceName == "scripteditor" && lineNum > 0)
              scriptEditor.markTraceLine(lineNum)
            else
              scriptEditor.markTraceLine(me.callerLineNum)

            currMarker foreach { _.erase }
            kojoCtx.repaintCanvas()
            lastLine foreach { ll =>
              val pic1 = kojoCtx.picLine(ll._1, ll._2)
              val pic2 = kojoCtx.picLine(ll._1, ll._2)
              val marker = GPics(pic1, pic2)
              marker.draw()
              pic1.setPenColor(Color.black)
              pic1.setPenThickness(10)
              pic2.setPenColor(Color.yellow)
              pic2.setPenThickness(4)
              currMarker = Some(marker)
            }

          }
        })
      }

      events.add(te)
      events.revalidate()
    }
  }
}