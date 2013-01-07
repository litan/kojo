/*
 * Copyright (C) 2013 Lalit Pant <pant.lalit@gmail.com>
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
package net.kogics.kojo.util

import java.awt.Color

object TerminalAnsiCodes {
  val AnsiRed = new Color(192, 0, 0)
  val AnsiGreen = new Color(0, 127, 0)
  val NormalColor = new Color(32, 32, 32)
  val ESC = "\u001b"

  def isColoredString(s: String) = s.startsWith(s"$ESC[")

  def parse(s: String): Seq[(String, Color)] = {
    val strs = s.split(s"$ESC\\[")
    strs filter { _.size > 0 } map { ss =>
      val mindex = ss.indexOf('m')
      val clr = ss.substring(0, mindex) match {
        case "0"  => NormalColor
        case "31" => AnsiRed
        case "32" => AnsiGreen
        case _    => NormalColor
      }
      (ss.substring(mindex + 1, ss.length), clr)
    }
  }
}