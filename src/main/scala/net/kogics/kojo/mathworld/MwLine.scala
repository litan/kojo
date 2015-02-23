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

package net.kogics.kojo.mathworld

import net.kogics.kojo.util.Utils
import net.kogics.kojo.core._
import geogebra.common.plugin.GgbAPI
import geogebra.common.kernel.geos.GeoLine

object MwLine {
  val lGen = new LabelGenerator("Ln")

  def apply(ggbApi: GgbAPI, p1: MwPoint, p2: MwPoint) = {
    net.kogics.kojo.util.Throttler.throttle()
    val line = Utils.runInSwingThreadAndWait {
      val gLine = ggbApi.getKernel.getAlgoDispatcher.Line(lGen.next(), p1.gPoint, p2.gPoint)
      new MwLine(ggbApi, gLine, p1, p2)
    }
    line
  }
}

class MwLine(val ggbApi: GgbAPI, val gLine: GeoLine, override val p1: MwPoint, override val p2: MwPoint) extends Line(p1, p2) with MwShape {

  ctorDone()

  protected def geogebraElement = gLine
}
