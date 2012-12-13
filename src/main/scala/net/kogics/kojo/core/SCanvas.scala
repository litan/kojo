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

import edu.umd.cs.piccolo.util.PBounds
import java.awt.Paint

trait SCanvas extends TSCanvasFeatures {
  // stuff gets added here (instead of in the base class) if any of the following conditions hold:
  // 1) there's a name clash with TurtleWorld 
  // 2) the builtin method name is different from the canvas method name
  def turtle0: Turtle
  def clear(): Unit
  def activate(): Unit
  def cbounds: PBounds
  def setCanvasBackground(c: Paint)
  def kojoCtx: KojoCtx
}
