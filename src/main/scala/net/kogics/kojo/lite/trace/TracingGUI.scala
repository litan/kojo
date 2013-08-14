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
import java.awt.Dimension
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.geom.Point2D

import javax.swing.BoxLayout
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JSplitPane
import javax.swing.JTextArea

import net.kogics.kojo.core.Picture
import net.kogics.kojo.lite.topc.TraceHolder
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

    if (eventHolder != null) {
      traceHolder.remove(eventHolder)
    }

    eventHolder = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(events), new JScrollPane(eventDesc))
    eventHolder.setDividerLocation(500)
    eventHolder.setOneTouchExpandable(true)

    traceHolder.add(eventHolder)
    kojoCtx.makeTraceWindowVisible(traceHolder)
  }

  def addEvent(me: MethodEvent, oll: Option[(Point2D.Double, Point2D.Double)]) = {
    lazy val lastLine = oll
    val meDesc = me.toString
    val ended = me.ended
    val uiLevel = me.level + 1
    val taText = if (me.ended) "< " * uiLevel + me.exit else "> " * uiLevel + me.entry
    val lineNum = if (me.ended) me.exitLineNum else me.entryLineNum

    Utils.runInSwingThread {
      val te = new JTextArea(taText) {
        override def getMaximumSize = new Dimension(Short.MaxValue, getPreferredSize.getHeight.toInt)
        setEditable(false)
        setLineWrap(true)
        setWrapStyleWord(true)

        addMouseListener(new MouseAdapter {
          override def mouseClicked(e: MouseEvent) {
            eventDesc.setText(meDesc)
            if (me.sourceName == "scripteditor")
              scriptEditor.markTraceLine(lineNum)
            else
              scriptEditor.markTraceLine(me.callerLineNum)

            currMarker foreach { _.erase }
            kojoCtx.repaintCanvas()
            lastLine foreach { ll =>
              val pic = kojoCtx.picLine(ll._1, ll._2)
              currMarker = Some(pic)
              pic.draw()
              pic.setPenColor(Color.black)
              pic.setPenThickness(4)
            }

          }
        })
      }

      events.add(te)
      events.revalidate()
    }
  }
}