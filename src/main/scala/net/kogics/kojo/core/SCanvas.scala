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

package net.kogics.kojo.core

import java.awt.Paint
import java.util.concurrent.Future

import edu.umd.cs.piccolo.PCamera
import edu.umd.cs.piccolo.PCanvas
import edu.umd.cs.piccolo.PLayer
import edu.umd.cs.piccolo.activities.PActivity
import edu.umd.cs.piccolo.util.PBounds

trait SCanvas extends TSCanvasFeatures {
  // stuff gets added here (instead of in the base class) if any of the following conditions hold:
  // 1) there's a name clash with TurtleWorld 
  // 2) the builtin method name is different from the canvas method name
  def turtle0: Turtle
  def activate(): Unit
  def cbounds: PBounds
  def setCanvasBackground(c: Paint): Unit
  def kojoCtx: KojoCtx
  def animate(fn: => Unit): Future[PActivity]
  def animateActivity(a: PActivity): Unit
  def stopAnimation(): Unit
  // stuff for the pictures module
  def getCamera: PCamera
  def pictures: PLayer
  def pCanvas: PCanvas
  def unitLen: UnitLen
  type TurtleLike <: Turtle
  private[kojo] def newInvisibleTurtle(x: Double, y: Double): TurtleLike
  private[kojo] def setDefTurtle(t: TurtleLike): Unit
  private[kojo] def restoreDefTurtle(): Unit
}
