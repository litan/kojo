/*
 * Copyright (C) 2012 Jerzy Redlarski <5xinef@gmail.com>
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

package net.kogics.kojo.d3

import java.awt.event.MouseMotionListener
import java.awt.event.MouseListener
import java.awt.event.MouseEvent

trait MouseControlledMover extends MouseMotionListener with MouseListener {
  var canvas : Option[Canvas3D]
  var lastX = 0
  var lastY = 0
  var leftIsPressed = false
  var rightIsPressed = false

  override def mousePressed(e : MouseEvent) {
    if (!leftIsPressed && e.getButton == MouseEvent.BUTTON1) {
      lastX = e.getX
      lastY = e.getY
      leftIsPressed = true
    }
    if (!rightIsPressed && e.getButton == MouseEvent.BUTTON3) {
      lastX = e.getX
      lastY = e.getY
      rightIsPressed = true
    }
  }

  override def mouseReleased(e : MouseEvent) {
    if (e.getButton == MouseEvent.BUTTON1)
      leftIsPressed = false
    if (e.getButton == MouseEvent.BUTTON3)
      rightIsPressed = false
  }

  override def mouseDragged(e : MouseEvent) {
    // shift - panning (strafe)
    // zoom - changes angle in perspective, scale in orthographic
    // ctrl - lock to one axis
    // alt - lock to the other axis
    canvas.foreach(canvas => {
      if (canvas.mouseControl && (leftIsPressed || rightIsPressed)) {

        val xDiff = lastX - e.getX
        val yDiff = lastY - e.getY
        lastX = e.getX
        lastY = e.getY

        if (e.isShiftDown()) {
          // panning / strafing
          if (leftIsPressed) {
            if (e.isControlDown()) {
              // restrict to left/right
              canvas.camera = canvas.camera.strafeRight(xDiff * Defaults.mouseControlDistanceRatio)
            } else if (e.isAltDown()) {
              // restrict to up/down
              canvas.camera = canvas.camera.strafeDown(yDiff * Defaults.mouseControlDistanceRatio)
            } else {
              // unrestricted panning
              canvas.camera = canvas.camera.strafeRight(xDiff * Defaults.mouseControlDistanceRatio)
            		  .strafeDown(yDiff * Defaults.mouseControlDistanceRatio)
            }
          }
        } else {
          // forward + rotations movement
          if (e.isControlDown()) {
            // restrict to yaw and roll
            if (leftIsPressed) {
              canvas.camera = canvas.camera.turn(-xDiff * Defaults.mouseControlAngleRatio)
            }
            if (rightIsPressed) {
              canvas.camera = canvas.camera.roll(xDiff * Defaults.mouseControlAngleRatio)
            }
          } else if (e.isAltDown()) {
            // restrict to pitch and movement
            if (leftIsPressed) {
              canvas.camera = canvas.camera.pitch(-yDiff * Defaults.mouseControlAngleRatio)
            }
            if (rightIsPressed) {
              canvas.camera = canvas.camera.forward(yDiff * Defaults.mouseControlDistanceRatio)
            }
          } else {
            // unrestricted movement
            if (leftIsPressed) {
              canvas.camera = canvas.camera.turn(-xDiff * Defaults.mouseControlAngleRatio)
            		  .pitch(yDiff * Defaults.mouseControlAngleRatio)
            }
            if (rightIsPressed) {
              canvas.camera = canvas.camera.roll(xDiff * Defaults.mouseControlAngleRatio)
            		  .forward(yDiff * Defaults.mouseControlDistanceRatio)
            }
          }
        }
        if (canvas.renderLock.available)
          canvas.renderAsynchronous()
      }
    })
  }

  override def mouseExited(e : MouseEvent) {
    leftIsPressed = false
    rightIsPressed = false
  }

  override def mouseEntered(e : MouseEvent) {}
  override def mouseClicked(e : MouseEvent) {}
  override def mouseMoved(e : MouseEvent) {}
}
