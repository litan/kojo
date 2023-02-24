/*
 * Copyright (C) 2010 Lalit Pant <pant.lalit@gmail.com>
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
package codingmode

import java.awt.event.ActionEvent
import javax.swing.AbstractAction
import javax.swing.JCheckBoxMenuItem

import net.kogics.kojo.core.CodeExecutionSupport
import net.kogics.kojo.core.TwMode
import net.kogics.kojo.core.VanillaMode

class SwitchMode(execSupport: CodeExecutionSupport) extends AbstractAction {
  def actionPerformed(e: ActionEvent): Unit = {
    e.getActionCommand match {
      case TwMode.code      => execSupport.activateTw()
      case VanillaMode.code => execSupport.activateVn()
    }
  }

  def updateCb(cb: JCheckBoxMenuItem): Unit = {
    cb.getActionCommand match {
      case TwMode.code      => cb.setSelected(execSupport.isTwActive)
      case VanillaMode.code => cb.setSelected(execSupport.isVnActive)
    }
  }
}
