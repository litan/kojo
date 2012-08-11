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
package action

import java.awt.event.ActionEvent
import javax.swing.AbstractAction
import lite.CodeExecutionSupport
import net.kogics.kojo.core.KojoCtx

class NewFile(ctx: KojoCtx) extends AbstractAction("New...") {
  val saveAs = new SaveAs(ctx)
  
  def actionPerformed(e: ActionEvent) {
    try {
      CodeExecutionSupport.instance.closeFileAndClrEditor()
      saveAs.actionPerformed(e);
    }
    catch {
      case e: RuntimeException => // user cancelled
    }
  }
}
