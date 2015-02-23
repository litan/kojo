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
import geogebra.common.kernel.geos.GeoRay

object MwRay {

  val lGen = new LabelGenerator("Ray")

  def apply(ggbApi: GgbAPI, p1: MwPoint, p2: MwPoint) = {
    net.kogics.kojo.util.Throttler.throttle()
    val ray = Utils.runInSwingThreadAndWait {
      val gRay = ggbApi.getKernel.Ray(lGen.next(), p1.gPoint, p2.gPoint)
      new MwRay(ggbApi, gRay.asInstanceOf[GeoRay], p1, p2)
    }
    ray
  }
}

class MwRay(ggbApi: GgbAPI, gRay: GeoRay, p1: MwPoint, p2: MwPoint)
extends MwLine(ggbApi, gRay, p1, p2) with MwShape {

  ctorDone()

  override protected def geogebraElement = gRay
}
