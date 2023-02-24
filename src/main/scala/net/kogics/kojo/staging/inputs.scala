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
  // import java.awt.event.InputEvent

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
  var mouseClickHandler: Option[PInputEvent => Unit] = None
  var mouseDragHandler: Option[PInputEvent => Unit] = None
  var mouseMoveHandler: Option[PInputEvent => Unit] = None

  def removeMouseKeyHandlers(): Unit = {
    removeKeyHandlers()
    removeMouseHandlers()
  }

  def removeKeyHandlers(): Unit = {
    keyPressedHandler = None
    keyReleasedHandler = None
    pressedKeys.clear()
    heldReleasedKeys.clear()
  }
  def setKeyPressedHandler(handler: PInputEvent => Unit): Unit = {
    keyPressedHandler = Some(handler)
  }
  def setKeyReleasedHandler(handler: PInputEvent => Unit): Unit = {
    keyReleasedHandler = Some(handler)
  }
  def removeMouseHandlers(): Unit = {
    mouseClickHandler = None
    mouseDragHandler = None
    mouseMoveHandler = None
  }
  def setMouseClickHandler(handler: PInputEvent => Unit): Unit = {
    mouseClickHandler = Some(handler)
  }
  def setMouseDragHandler(handler: PInputEvent => Unit): Unit = {
    mouseDragHandler = Some(handler)
  }
  def setMouseMoveHandler(handler: PInputEvent => Unit): Unit = {
    mouseMoveHandler = Some(handler)
  }

  def activityStep() = {
    prevMousePos = stepMousePos
    stepMousePos = mousePos
  }

  def init(): Unit = {
    Utils.runInSwingThread {
      val iel = new PBasicInputEventHandler {
        // This method is invoked when a node gains the keyboard focus.
        override def keyboardFocusGained(e: PInputEvent): Unit = {}
        // This method is invoked when a node loses the keyboard focus.
        override def keyboardFocusLost(e: PInputEvent): Unit = {}
        // Will get called whenever a key has been pressed down.
        override def keyPressed(e: PInputEvent): Unit = {
          val evtCode = Utils.getKeyCode(e)
          if (heldReleasedKeys contains evtCode) {
            // system generated release - eat it up
            heldReleasedKeys.remove(evtCode)
            // but call key press handlers anyway
            keyPressedHandler.foreach { _.apply(e) }
          }
          else {
            pressedKeys.add(evtCode)
            keyPressedHandler.foreach { _.apply(e) }
          }
        }
        // Will get called whenever a key has been released.
        override def keyReleased(e: PInputEvent): Unit = {
          val evtCode = Utils.getKeyCode(e)
          heldReleasedKeys.add(evtCode)
          Utils.schedule(0.002) {
            if (heldReleasedKeys contains evtCode) {
              // actual key release
              pressedKeys.remove(evtCode)
              heldReleasedKeys.remove(evtCode)
              keyReleasedHandler.foreach { _.apply(e) }
            }
          }
        }
        // Will be called at the end of a full keystroke (down then up).
        override def keyTyped(e: PInputEvent): Unit = {}
        // Will be called at the end of a full click (mouse pressed followed by mouse released).
        override def mouseClicked(e: PInputEvent): Unit = {
          super.mouseClicked(e)
          val p = e.getPosition
          mousePos = Point(p.getX, p.getY)
          mouseBtn = e.getButton
          mouseClickHandler.foreach { _.apply(e) }
        }
        // Will be called when a drag is occurring.
        override def mouseDragged(e: PInputEvent): Unit = {
          super.mouseDragged(e)
          val p = e.getPosition
          mousePos = Point(p.getX, p.getY)
          mouseDragHandler.foreach { _.apply(e) }
        }
        // Will be invoked when the mouse enters a specified region.
        override def mouseEntered(e: PInputEvent): Unit = {
          super.mouseEntered(e)
          // e.pushCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR))
          val p = e.getPosition
          mousePos = Point(p.getX, p.getY)
        }
        // Will be invoked when the mouse leaves a specified region.
        override def mouseExited(e: PInputEvent): Unit = {
          super.mouseExited(e)
          // e.popCursor
          val p = e.getPosition
          mousePos = Point(p.getX, p.getY)
          // mousePressedFlag = false
        }
        // Will be called when the mouse is moved.
        override def mouseMoved(e: PInputEvent): Unit = {
          super.mouseMoved(e)
          val p = e.getPosition
          mousePos = Point(p.getX, p.getY)
          mouseMoveHandler.foreach { _.apply(e) }
        }
        // Will be called when a mouse button is pressed down.
        override def mousePressed(e: PInputEvent): Unit = {
          super.mousePressed(e)
          val p = e.getPosition
          mousePos = Point(p.getX, p.getY)
          mouseBtn = e.getButton
          mousePressedFlag = true
          Impl.canvas.getRoot.getDefaultInputManager.setKeyboardFocus(this)
        }
        // Will be called when any mouse button is released.
        override def mouseReleased(e: PInputEvent): Unit = {
          super.mouseReleased(e)
          val p = e.getPosition
          mousePos = Point(p.getX, p.getY)
          mouseBtn = e.getButton
          mousePressedFlag = false
        }
        // This method is invoked when the mouse wheel is rotated.
        override def mouseWheelRotated(e: PInputEvent): Unit = {
          super.mouseWheelRotated(e)
          val p = e.getPosition
          mousePos = Point(p.getX, p.getY)
        }
        // This method is invoked when the mouse wheel is rotated by a block.
        override def mouseWheelRotatedByBlock(e: PInputEvent): Unit = {
          super.mouseWheelRotatedByBlock(e)
          val p = e.getPosition
          mousePos = Point(p.getX, p.getY)
        }
      }

      Impl.canvas.addGlobalEventListener(iel)
    }
  }
}
