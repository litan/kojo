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
import geogebra.common.kernel.geos.GeoConic
import geogebra.common.kernel.geos.GeoNumeric

object MwCircle {
  val lGen = new LabelGenerator("Crc")

  def apply(ggbApi: GgbAPI, p1: MwPoint, r: Double) = {
    net.kogics.kojo.util.Throttler.throttle()
    val circle = Utils.runInSwingThreadAndWait {
      val gCircle = ggbApi.getKernel.getAlgoDispatcher.Circle(lGen.next(), p1.gPoint, new GeoNumeric(ggbApi.getConstruction, r))
      new MwCircle(ggbApi, gCircle, p1, r)
    }
    circle
  }
}

class MwCircle(val ggbApi: GgbAPI, val gCircle: GeoConic, p1: MwPoint, r: Double) extends Circle(p1, r) with MwShape {

  ctorDone()

  protected def geogebraElement = gCircle
}
