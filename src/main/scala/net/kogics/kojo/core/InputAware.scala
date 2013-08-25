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

package net.kogics.kojo.core

import edu.umd.cs.piccolo.event.PBasicInputEventHandler
import edu.umd.cs.piccolo.event.PInputEvent
import edu.umd.cs.piccolo.event.PInputEventFilter
import edu.umd.cs.piccolo.PCanvas
import edu.umd.cs.piccolo.PNode
import net.kogics.kojo.util.Utils

trait InputAware {
  def myCanvas: PCanvas
  def myNode: PNode
  
  def onMousePress(fn: (Double, Double) => Unit) = Utils.runInSwingThread {
    val h = new PBasicInputEventHandler {
      override def mousePressed(event: PInputEvent) {
        val pos = event.getPosition
        Utils.safeProcess {
          fn(pos.getX, pos.getY)
        }
      }
    }
    h.setEventFilter(new PInputEventFilter(java.awt.event.InputEvent.BUTTON1_MASK))
    myNode.addInputEventListener(h)
  }
  
  def onMouseClick(fn: (Double, Double) => Unit) = Utils.runInSwingThread {
    val h = new PBasicInputEventHandler {
      override def mouseClicked(event: PInputEvent) {
        val pos = event.getPosition
        Utils.safeProcess {
          fn(pos.getX, pos.getY)
        }
      }
    }
    h.setEventFilter(new PInputEventFilter(java.awt.event.InputEvent.BUTTON1_MASK))
    myNode.addInputEventListener(h)
  }
  
  def onMouseDrag(fn: (Double, Double) => Unit) = Utils.runInSwingThread {
    val h = new PBasicInputEventHandler {
      override def mouseDragged(event: PInputEvent) {
        val pos = event.getPosition
        event.setHandled(true)
        Utils.safeProcess {
          fn(pos.getX, pos.getY)
        }
      }
    }
    h.setEventFilter(new PInputEventFilter(java.awt.event.InputEvent.BUTTON1_MASK))
    myNode.addInputEventListener(h)
  }
  
//  import java.awt.event.KeyEvent
//  def onKeyPress(fn: Int => Unit) = Utils.runInSwingThread {
//    val eh = new PBasicInputEventHandler {
//      override def mousePressed(event: PInputEvent) {
//        myCanvas.getRoot.getDefaultInputManager.setKeyboardFocus(this)
//      }
//      override def keyPressed(e: PInputEvent) {
//        Utils.safeProcess {
//          fn(e.getKeyCode)
//        }
//      }
//    }
//    myNode.addInputEventListener(eh)
//  }
}
