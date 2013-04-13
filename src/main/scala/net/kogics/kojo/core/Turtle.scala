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

import java.awt.Stroke

import scala.collection.mutable.ArrayBuffer

import net.kogics.kojo.kgeom.PolyLine
import net.kogics.kojo.util.Utils

import edu.umd.cs.piccolo.PLayer

trait Turtle extends TurtleMover {
  def clear(): Unit
  def remove(): Unit
  def act(fn: Turtle => Unit) = Utils.runAsyncMonitored(fn(this))
  def react(fn: Turtle => Unit): Unit
  def distanceTo(other: Turtle): Double
  def towards(t: Turtle): Unit = { val pos = t.position; towards(pos.x, pos.y)}
  // stuff for the pictures module
  def tlayer: PLayer
  private[kojo] def penPaths: ArrayBuffer[PolyLine]
  private[kojo] def lineStroke: Stroke
}
