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
import geogebra.common.plugin.GgbAPI
import geogebra.common.kernel.geos.GeoAngle
import geogebra.common.kernel.geos.GeoNumeric
import geogebra.common.kernel.geos.GeoPoint

object MwAngle {

  val lGen = new LabelGenerator("Ang")

  def apply(ggbApi: GgbAPI, p1: MwPoint, p2: MwPoint, p3: MwPoint) = {
    net.kogics.kojo.util.Throttler.throttle()
    val angle = Utils.runInSwingThreadAndWait {
      new MwAngle(ggbApi, ggbApi.getKernel.getAlgoDispatcher.Angle(lGen.next(), p1.gPoint, p2.gPoint, p3.gPoint),
                  p1, p2, p3)
    }
    angle
  }

  def apply(ggbApi: GgbAPI, p1: MwPoint, p2: MwPoint, size: Double) = {
    net.kogics.kojo.util.Throttler.throttle()
    val angle = Utils.runInSwingThreadAndWait {
      val ap = ggbApi.getKernel.getAlgoDispatcher.Angle(Array(lGen.next(),
                                            MwPoint.lGen.next()), p1.gPoint, p2.gPoint,
                                      new GeoNumeric(ggbApi.getConstruction, size), true)
      new MwAngle(ggbApi, ap(0).asInstanceOf[GeoAngle], p1, p2, new MwPoint(ggbApi, ap(1).asInstanceOf[GeoPoint]))
    }
    angle
  }
}

trait AnglePoints {
  val p1: MwPoint
  val p2: MwPoint
  val p3: MwPoint
}

class MwAngle(val ggbApi: GgbAPI, val gAngle: GeoAngle, 
              val p1: MwPoint, val p2: MwPoint, val p3: MwPoint)
extends net.kogics.kojo.core.Angle(gAngle.getRawAngle) with MwShape with AnglePoints {

  showNameValueInLabel()
  ctorDone()

  protected def geogebraElement = gAngle
}
