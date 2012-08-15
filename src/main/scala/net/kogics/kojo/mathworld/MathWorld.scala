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

package net.kogics.kojo
package mathworld

import geogebra.plugin.GgbAPI
import net.kogics.kojo.core._
import net.kogics.kojo.util._

object MathWorld extends InitedSingleton[MathWorld] {
  def initedInstance(kojoCtx: KojoCtx, ggbApi: GgbAPI) = synchronized {
    instanceInit()
    val ret = instance()
    ret._kojoCtx = kojoCtx
    ret._ggbApi = ggbApi
    ret._Algo = new Algo(ggbApi)
    ret
  }

  protected def newInstance = new MathWorld
}

class MathWorld {
  @volatile var _kojoCtx: KojoCtx = _
  @volatile var _ggbApi: GgbAPI = _
  @volatile var _Algo: Algo = _

  private def ensureVisible() {
    _kojoCtx.makeMathWorldVisible()
  }

  def clear() {
    Utils.runInSwingThreadAndWait {
      ensureVisible()
      _ggbApi.getApplication.setSaved()
      _ggbApi.getApplication.fileNew()
    }
  }

  // for unit tests
  private[mathworld] def _clear2() {
    Utils.runInSwingThread {
      _ggbApi.getApplication.setSaved()
      _ggbApi.getApplication.fileNew()
    }
  }

  def showAxes() {
    Utils.runInSwingThread {
      _ggbApi.setAxesVisible(true, true)
      _ggbApi.getKernel.notifyRepaint()
    }
  }

  def hideAxes() {
    Utils.runInSwingThread {
      _ggbApi.setAxesVisible(false, false)
      _ggbApi.getKernel.notifyRepaint()
    }
  }

  def showGrid() {
    Utils.runInSwingThread {
      _ggbApi.setGridVisible(true)
      _ggbApi.getKernel.notifyRepaint()
    }
  }

  def hideGrid() {
    Utils.runInSwingThread {
      _ggbApi.setGridVisible(false)
      _ggbApi.getKernel.notifyRepaint()
    }
  }

  def showAlgebraView() {
    Utils.runInSwingThread {
      _ggbApi.getApplication.setShowAlgebraView(true)
      _ggbApi.getApplication.updateCenterPanel(true)
      _ggbApi.getApplication.setDefaultCursor()
    }
  }

  def hideAlgebraView() {
    Utils.runInSwingThread {
      _ggbApi.getApplication.setShowAlgebraView(false)
      _ggbApi.getApplication.updateCenterPanel(true)
      _ggbApi.getApplication.setDefaultCursor()
    }
  }

  def zoom(factor: Double, cx: Double, cy: Double) {
    Utils.runInSwingThread {
      val view = _ggbApi.getApplication.getEuclidianView
      val newZoom = factor * geogebra.euclidian.EuclidianView.SCALE_STANDARD
      view.setCoordSystem(view.getWidth/2 - cx * newZoom, view.getHeight/2 + cy * newZoom, newZoom, newZoom)
    }
  }

  def switchTo() = ensureVisible()

  def point(x: Double, y: Double, label: String=null): MwPoint = MwPoint(_ggbApi, x, y, Option(label))
  def pointOn(on: MwLine, x: Double, y: Double): MwPoint = MwPoint(_ggbApi, on, x, y)

  def line(p1: MwPoint, p2: MwPoint): MwLine = MwLine(_ggbApi, p1, p2)

  def lineSegment(p1: MwPoint, p2: MwPoint): MwLineSegment = MwLineSegment(_ggbApi, p1, p2)
  def lineSegment(p: MwPoint, len: Double): MwLineSegment = MwLineSegment(_ggbApi, p, len)

  def ray(p1: MwPoint, p2: MwPoint): MwRay = MwRay(_ggbApi, p1, p2)

  def angle(p1: MwPoint, p2: MwPoint, p3: MwPoint): MwAngle = MwAngle(_ggbApi, p1, p2, p3)
  def angle(p1: MwPoint, p2: MwPoint, size: Double): MwAngle = MwAngle(_ggbApi, p1, p2, size * math.Pi / 180)
  
  def text(content: String, x: Double, y: Double): MwText = {
    MwText(_ggbApi, content, x, y)
  }

  def circle(center: MwPoint, radius: Double): MwCircle = {
    MwCircle(_ggbApi, center, radius)
  }

  def figure(name: String) = new MwFigure(name)

  def intersect(l1: MwLine, l2: MwLine): MwPoint  = _Algo.intersect(_ggbApi, l1, l2)
  def intersect(l: MwLine, c: MwCircle): Seq[MwPoint] = _Algo.intersect(_ggbApi, l, c)
  def intersect(c: MwCircle, l: MwLine): Seq[MwPoint] = intersect(l, c)
  def intersect(c1: MwCircle, c2: MwCircle): Seq[MwPoint] = _Algo.intersect(_ggbApi, c1, c2)

  def midpoint(ls: MwLineSegment): MwPoint = _Algo.midpoint(ls)
  def perpendicular(l: MwLine, p: MwPoint): MwLine = _Algo.perpendicular(l, p)
  def parallel(l: MwLine, p: MwPoint): MwLine = _Algo.parallel(l, p)

  def show(shapes: VisualElement*) {
    Utils.runInSwingThread {
      shapes.foreach {s => s.show}
    }
  }
  
  // quick and dirty stuff for now
  import geogebra.kernel._

  def variable(name: String, value: Double, min: Double, max: Double, increment: Double, x: Int, y: Int) {
    Throttler.throttle()
    Utils. runInSwingThread {
      val number = new GeoNumeric(_ggbApi.getConstruction)
      number.setEuclidianVisible(true)
      number.setSliderLocation(x, y)
      number.setAbsoluteScreenLocActive(true)
      number.setIntervalMin(min)
      number.setIntervalMax(max)
      number.setAnimationStep(increment)
      number.setValue(value)
      number.setLabel(name)
      number.setLabelMode(GeoElement.LABEL_NAME_VALUE)
      number.setLabelVisible(true)
      number.update()
    }
  }

  def evaluate(cmd: String) {
    Throttler.throttle()
    Utils. runInSwingThread {
      _ggbApi.evalCommand(cmd)
    }
  }

  def turtle(x: Double, y: Double) = {
    Utils.runInSwingThreadAndWait {
      new MwTurtle(x, y)
    }
  }
}
