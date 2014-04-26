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

import geogebra.kernel._
import net.kogics.kojo.util.Utils
import geogebra.common.plugin.GgbAPI
import geogebra.common.kernel.geos.GeoPoint

class Algo(ggbApi: GgbAPI) {

  def intersect(ggbApi: GgbAPI, l1: MwLine, l2: MwLine) = {
    val pt = Utils.runInSwingThreadAndWait {
      val gPoint = ggbApi.getKernel.IntersectLines(MwPoint.lGen.next(), l1.gLine, l2.gLine)
      new MwPoint(ggbApi, gPoint.asInstanceOf[GeoPoint])
    }
    pt
  }

  def intersect(ggbApi: GgbAPI, l: MwLine, c: MwCircle) = {
    val pts = Utils.runInSwingThreadAndWait {
      val gPoints = ggbApi.getKernel.getAlgoDispatcher.IntersectLineConic(
        Array(MwPoint.lGen.next(), MwPoint.lGen.next()), l.gLine, c.gCircle
      )
      gPoints.map {gPoint => new MwPoint(ggbApi, gPoint)}
    }
    pts
  }

  def intersect(ggbApi: GgbAPI, c1: MwCircle, c2: MwCircle) = {
    val pts = Utils.runInSwingThreadAndWait {
      val labels = for (idx <- 1 to 10) yield (MwPoint.lGen.next())
      val gPoints = ggbApi.getKernel.IntersectConics(labels.toArray, c1.gCircle, c2.gCircle)
      gPoints.map {gPoint => new MwPoint(ggbApi, gPoint.asInstanceOf[GeoPoint])}
    }
    pts
  }

  def midpoint(ls: MwLineSegment): MwPoint = {
    val pt = Utils.runInSwingThreadAndWait {
      val gPoint = ggbApi.getKernel.getAlgoDispatcher.Midpoint(MwPoint.lGen.next(), ls.gLineSegment)
      new MwPoint(ggbApi, gPoint)
    }
    pt
  }

  def perpendicular(l: MwLine, p: MwPoint): MwLine = {
    Utils.runInSwingThreadAndWait {
      val gLine = ggbApi.getKernel.getAlgoDispatcher.OrthogonalLine(MwLine.lGen.next(), p.gPoint, l.gLine)
      new MwLine(ggbApi, gLine, p, p)
    }
  }

  def parallel(l: MwLine, p: MwPoint): MwLine = {
    Utils.runInSwingThreadAndWait {
      val gLine = ggbApi.getKernel.getAlgoDispatcher.Line(MwLine.lGen.next(), p.gPoint, l.gLine)
      new MwLine(ggbApi, gLine, p, p)
    }
  }
}
