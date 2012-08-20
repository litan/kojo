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

package net.kogics.kojo.staging

import java.awt.Color

// if I make this a trait to mix into Builtins, the color completions 
// inside the C builtin do not show up nicely 
// because the colors get into C via inheritence, and show up lower down
// in the list of completions
object KColor {
  val blue = Color.blue
  val red = Color.red
  val yellow = Color.yellow
  val green = Color.green
  val orange = Color.orange
  val purple = new Color(0x740f73)
  val pink = Color.pink
  val brown = new Color(0x583a0b)
  val black = Color.black
  val white = Color.white
  val gray = Color.gray
  val lightGray = Color.lightGray
  val darkGray = new Color(64, 64, 64);
  val magenta = new Color(255, 0, 255);
  val cyan = new Color(0, 255, 255);
  val noColor = new Color(0, 0, 0, 0)

  def knownColors = List("blue", "red", "yellow", "green", "orange", "purple", "pink", "brown", "black", "white",
    "gray", "lightGray", "darkGray", "magenta", "cyan", "noColor"
  )
}
