/*
 * Copyright (C) 2010 Peter Lewerin <peter.lewerin@tele2.se>
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
package staging

import core.Point
import edu.umd.cs.piccolo.event.PBasicInputEventHandler
import edu.umd.cs.piccolo.event.PInputEvent
import util.Utils
import Impl.API

object Inputs {
  import edu.umd.cs.piccolo.event._
  //import java.awt.event.InputEvent

  @volatile
  var mousePos: Point = API.O
  @volatile
  var prevMousePos: Point = API.O
  @volatile
  var stepMousePos: Point = API.O
  @volatile
  var mouseBtn = 0
  @volatile
  var mousePressedFlag = false

  val pressedKeys = new collection.mutable.HashSet[Int]
  val heldReleasedKeys = new collection.mutable.HashSet[Int]
  def isKeyPressed(key: Int) = pressedKeys.contains(key)
  var keyPressedHandler: Option[PInputEvent => Unit] = None
  var keyReleasedHandler: Option[PInputEvent => Unit] = None

  def removeKeyHandlers() {
    keyPressedHandler = None
    keyReleasedHandler = None
    pressedKeys.clear()
    heldReleasedKeys.clear()
  }
  def setKeyPressedHandler(handler: PInputEvent => Unit) {
    keyPressedHandler = Some(handler)
  }
  def setKeyReleasedHandler(handler: PInputEvent => Unit) {
    keyReleasedHandler = Some(handler)
  }

  def activityStep() = {
    prevMousePos = stepMousePos
    stepMousePos = mousePos
  }

  def init() {
    Utils.runInSwingThread {
      val iel = new PBasicInputEventHandler {
        // This method is invoked when a node gains the keyboard focus.
        override def keyboardFocusGained(e: PInputEvent) {
        }
        // This method is invoked when a node loses the keyboard focus.
        override def keyboardFocusLost(e: PInputEvent) {
        }
        // Will get called whenever a key has been pressed down.
        override def keyPressed(e: PInputEvent) {
          val evtCode = e.getKeyCode
          if (heldReleasedKeys contains evtCode) {
            // system generated release - eat it up
            heldReleasedKeys remove evtCode
            // but call key press handlers anyway
            keyPressedHandler.foreach { _ apply e }
          }
          else {
            pressedKeys.add(evtCode)
            keyPressedHandler.foreach { _ apply e }
          }
        }
        // Will get called whenever a key has been released.
        override def keyReleased(e: PInputEvent) {
          val evtCode = e.getKeyCode
          heldReleasedKeys add evtCode
          Utils.schedule(0.002) {
            if (heldReleasedKeys contains evtCode) {
              // actual key release
              pressedKeys remove evtCode
              heldReleasedKeys remove evtCode
              keyReleasedHandler.foreach { _ apply e }
            }
          }
        }
        // Will be called at the end of a full keystroke (down then up).
        override def keyTyped(e: PInputEvent) {
        }
        // Will be called at the end of a full click (mouse pressed followed by mouse released).
        override def mouseClicked(e: PInputEvent) {
          super.mouseClicked(e)
          val p = e.getPosition
          mousePos = Point(p.getX, p.getY)
          mouseBtn = e.getButton
        }
        // Will be called when a drag is occurring.
        override def mouseDragged(e: PInputEvent) {
          super.mouseDragged(e)
          val p = e.getPosition
          mousePos = Point(p.getX, p.getY)
        }
        // Will be invoked when the mouse enters a specified region.
        override def mouseEntered(e: PInputEvent) {
          super.mouseEntered(e)
          //e.pushCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR))
          val p = e.getPosition
          mousePos = Point(p.getX, p.getY)
        }
        // Will be invoked when the mouse leaves a specified region.
        override def mouseExited(e: PInputEvent) {
          super.mouseExited(e)
          //e.popCursor
          val p = e.getPosition
          mousePos = Point(p.getX, p.getY)
          mousePressedFlag = false
        }
        // Will be called when the mouse is moved.
        override def mouseMoved(e: PInputEvent) {
          super.mouseMoved(e)
          val p = e.getPosition
          mousePos = Point(p.getX, p.getY)
        }
        // Will be called when a mouse button is pressed down.
        override def mousePressed(e: PInputEvent) {
          super.mousePressed(e)
          val p = e.getPosition
          mousePos = Point(p.getX, p.getY)
          mouseBtn = e.getButton
          mousePressedFlag = true
          Impl.canvas.getRoot.getDefaultInputManager.setKeyboardFocus(this)
        }
        // Will be called when any mouse button is released.
        override def mouseReleased(e: PInputEvent) {
          super.mouseReleased(e)
          val p = e.getPosition
          mousePos = Point(p.getX, p.getY)
          mouseBtn = e.getButton
          mousePressedFlag = false
        }
        // This method is invoked when the mouse wheel is rotated.
        override def mouseWheelRotated(e: PInputEvent) {
          super.mouseWheelRotated(e)
          val p = e.getPosition
          mousePos = Point(p.getX, p.getY)
        }
        // This method is invoked when the mouse wheel is rotated by a block.
        override def mouseWheelRotatedByBlock(e: PInputEvent) {
          super.mouseWheelRotatedByBlock(e)
          val p = e.getPosition
          mousePos = Point(p.getX, p.getY)
        }
      }

      Impl.canvas.addGlobalEventListener(iel)
    }
  }
}  
