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
import net.kogics.kojo.lite.CodeExecutionSupport

class SwitchMode(codeSupport: CodeExecutionSupport) extends AbstractAction {
  def actionPerformed(e: ActionEvent) {
    e.getActionCommand match {
      case "Tw" => codeSupport.activateTw()
      case "Staging" => codeSupport.activateStaging()
      case "Mw" => codeSupport.activateMw()
      case "D3" => codeSupport.activateD3()
    }
  }
  
  def updateCb(cb: JCheckBoxMenuItem) {
    cb.getActionCommand match {
      case "Tw" => cb.setSelected(codeSupport.isTwActive)
      case "Staging" => cb.setSelected(codeSupport.isStagingActive)
      case "Mw" => cb.setSelected(codeSupport.isMwActive)
      case "D3" => cb.setSelected(codeSupport.isD3Active)
    }
  }
}
