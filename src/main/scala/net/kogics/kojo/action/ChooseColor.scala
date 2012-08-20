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

import java.awt.event.ActionEvent
import javax.swing.AbstractAction
import javax.swing.JColorChooser
import java.awt.Color
import net.kogics.kojo.lite.CodeExecutionSupport
import net.kogics.kojo.core.KojoCtx

class ChooseColor(ctx: KojoCtx) extends AbstractAction {
  def actionPerformed(e: ActionEvent) {
    val sColor = JColorChooser.showDialog(null, util.Utils.stripDots(e.getActionCommand), ctx.lastColor)
    if (sColor != null) {
      val cprint = CodeExecutionSupport.instance.showOutput(_: String, _: Color)
      cprint("\u2500" * 3 + "\n", sColor)
      print("Selected Color:   ")
      cprint("\u2588" * 6 + "\n", sColor)
      val color = if (sColor.getAlpha < 255) {
        "Color(%d, %d, %d, %d)" format(sColor.getRed, sColor.getGreen, sColor.getBlue, sColor.getAlpha)
      }
      else {
        "Color(%d, %d, %d)" format(sColor.getRed, sColor.getGreen, sColor.getBlue)
      }
      println(color)
      println("Example usage: setPenColor(%s)" format(color))
      cprint("\u2500" * 3 + "\n", sColor)
      ctx.lastColor = sColor
    }
  }
}
