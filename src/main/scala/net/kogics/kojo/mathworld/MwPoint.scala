/*
 * Copyright (C) 2009 Lalit Pant <pant.lalit@gmail.com>
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
import net.kogics.kojo.core._
import geogebra.common.kernel.geos.GeoPoint
import geogebra.common.plugin.GgbAPI

object MwPoint {

  val lGen = new LabelGenerator("Pt")

  def apply(ggbApi: GgbAPI, x: Double, y: Double, label: Option[String]): MwPoint = {
    net.kogics.kojo.util.Throttler.throttle()
    val pt = Utils.runInSwingThreadAndWait {
      val ret = new MwPoint(ggbApi, ggbApi.getKernel.getAlgoDispatcher.Point(label.getOrElse(lGen.next()), x, y, false))
      if (label.isDefined) {
        ret.showLabel()
      }
      ret
    }
    pt
  }

  def apply(ggbApi: GgbAPI, on: MwLine, x: Double, y: Double) = {
    val pt = Utils.runInSwingThreadAndWait {
      new MwPoint(ggbApi, ggbApi.getKernel.getAlgoDispatcher.Point(lGen.next(), on.gLine, x, y, true, false))
    }
    pt
  }
}

class MwPoint(val ggbApi: GgbAPI, val gPoint: GeoPoint) extends Point(gPoint.x, gPoint.y) with MwShape with MoveablePoint {

  ctorDone()
  
  def cx = gPoint.x
  def cy = gPoint.y

  def moveTo(x: Double, y: Double) {
    Utils.runInSwingThread {
      gPoint.setCoords(x, y, 1)
      repaint()
    }
  }

  protected def geogebraElement = gPoint
}
