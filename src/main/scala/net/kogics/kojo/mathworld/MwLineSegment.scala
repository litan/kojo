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
import geogebra.common.kernel.geos.GeoPoint
import geogebra.common.kernel.geos.GeoSegment
import geogebra.common.plugin.GgbAPI
import geogebra.common.kernel.geos.GeoNumeric

object MwLineSegment {

  val lGen = new LabelGenerator("Ls")

  def apply(ggbApi: GgbAPI, p1: MwPoint, p2: MwPoint) = {
    net.kogics.kojo.util.Throttler.throttle()
    val lineSegment = Utils.runInSwingThreadAndWait {
      val gLineSegment = ggbApi.getKernel.Segment(lGen.next(), p1.gPoint, p2.gPoint)
      new MwLineSegment(ggbApi, gLineSegment.asInstanceOf[GeoSegment], p1, p2)
    }
    lineSegment
  }

  def apply(ggbApi: GgbAPI, p: MwPoint, len: Double) = {
    net.kogics.kojo.util.Throttler.throttle()
    val lineSegment = Utils.runInSwingThreadAndWait {
      val segP = ggbApi.getKernel.getAlgoDispatcher.Segment(Array(lGen.next(), MwPoint.lGen.next()),
                                                  p.gPoint, new GeoNumeric(ggbApi.getConstruction, len))
      val p2 = new MwPoint(ggbApi, segP(1).asInstanceOf[GeoPoint])
      new MwLineSegment(ggbApi, segP(0).asInstanceOf[GeoSegment], p, p2)
    }
    lineSegment
  }
}

class MwLineSegment(ggbApi: GgbAPI, val gLineSegment: GeoSegment, p1: MwPoint, p2: MwPoint)
extends MwLine(ggbApi, gLineSegment, p1, p2) with MwShape {

  ctorDone()

  override protected def geogebraElement = gLineSegment
}
