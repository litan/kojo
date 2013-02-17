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

import java.awt.Color
import java.awt.event.ActionEvent

import javax.swing.AbstractAction
import javax.swing.Action
import javax.swing.JColorChooser

import net.kogics.kojo.lite.EditorFileSupport
import net.kogics.kojo.util.Utils

import core.CodeExecutionSupport

class ChooseColor(execSupport: CodeExecutionSupport) extends AbstractAction(Utils.loadString("S_ChooseColor")) {
  val ctx = execSupport.kojoCtx
  def actionPerformed(e: ActionEvent) {
    val sColor = JColorChooser.showDialog(null, util.Utils.stripDots(e.getActionCommand), ctx.lastColor)
    if (sColor != null) {
      val cprint = execSupport.showOutput(_: String, _: Color)
      cprint("\u2500" * 3 + "\n", sColor)
      print("Selected Color:   ")
      cprint("\u2588" * 6 + "\n", sColor)
      val color = if (sColor.getAlpha < 255) {
        "Color(%d, %d, %d, %d)" format (sColor.getRed, sColor.getGreen, sColor.getBlue, sColor.getAlpha)
      }
      else {
        "Color(%d, %d, %d)" format (sColor.getRed, sColor.getGreen, sColor.getBlue)
      }
      println(color)
      println("Example usage: setPenColor(%s)" format (color))
      cprint("\u2500" * 3 + "\n", sColor)
      ctx.lastColor = sColor
    }
  }
}

object CloseFile {
  var action: Action = _
  def onFileOpen() {
    action.setEnabled(true)
  }
  def onFileClose() {
    action.setEnabled(false)
  }
}

class CloseFile(fileSupport: EditorFileSupport)
  extends AbstractAction(Utils.loadString("S_Close"), Utils.loadIcon("/images/extra/close.gif")) {
  setEnabled(false)
  CloseFile.action = this

  def actionPerformed(e: ActionEvent) {
    try {
      fileSupport.closeFileAndClrEditor()
    }
    catch {
      case e: RuntimeException => // user cancelled
    }
  }
}

class NewFile(fileSupport: EditorFileSupport)
  extends AbstractAction(Utils.loadString("S_New"), Utils.loadIcon("/images/extra/new.gif")) {

  val saveAs = new SaveAs(fileSupport)

  def actionPerformed(e: ActionEvent) {
    try {
      fileSupport.closeFileAndClrEditor()
      saveAs.actionPerformed(e);
    }
    catch {
      case e: RuntimeException => // user cancelled
    }
  }
}
