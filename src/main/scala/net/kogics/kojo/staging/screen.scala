/*
 * Copyright (C) 2010 Peter Lewerin <peter.lewerin@tele2.se>
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
package staging

import edu.umd.cs.piccolo._
import edu.umd.cs.piccolo.nodes._
import edu.umd.cs.piccolo.util._
import edu.umd.cs.piccolo.event._

//import net.kogics.kojo.util.Utils

import javax.swing._

import core._

object Screen {
  val rect = Bounds(0, 0, 0, 0)

  def size(width: Int, height: Int) = {
    // TODO 560 is a value that works on my system, should be less ad-hoc
    val factor = 560
    val xfactor = factor / (if (width < 0) -(height.abs) else height.abs) // sic!
    val yfactor = factor / height
    Impl.canvas.zoomXY(xfactor, yfactor, width / 2, height / 2)
    rect.setRect(0, 0, width.abs, height.abs)
    (rect.getWidth.toInt, rect.getHeight.toInt)
  }
}
