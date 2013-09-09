/*
 * Copyright (C) 2012 Lalit Pant <pant.lalit@gmail.com>
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
package livecoding

import javax.swing.text.Document
import javax.swing.text.JTextComponent

class IpmProvider(mctx: ManipulationContext) {
  val manips = List(new IntManipulator(mctx), new FloatManipulator(mctx), new ColorManipulator(mctx))
  
  def isHyperlinkPoint(pane: JTextComponent, offset: Int): Boolean = {
    manips.exists {_ isHyperlinkPoint(pane, offset)}
  }
  
  def getHyperlinkSpan(pane: JTextComponent, offset: Int): Array[Int] = {
    val manip = manips.find { _ isHyperlinkPoint(pane, offset) }
    manip.map { _ getHyperlinkSpan(pane, offset)}.getOrElse(null)
  }
  
  def performClickAction(pane: JTextComponent, offset: Int) {
    val manip = manips.find { _ isHyperlinkPoint(pane, offset) }
    manip.foreach { _ activate(pane, offset)}
  }
}
