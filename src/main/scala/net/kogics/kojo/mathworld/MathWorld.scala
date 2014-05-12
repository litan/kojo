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

import net.kogics.kojo.core.KojoCtx
import net.kogics.kojo.core.VisualElement
import net.kogics.kojo.util.Throttler
import net.kogics.kojo.util.Utils

import geogebra.GeoGebraPanel
import geogebra.common.euclidian.EuclidianView
import geogebra.common.kernel.StringTemplate
import geogebra.common.kernel.arithmetic.ValidExpression
import geogebra.common.kernel.geos.GeoElement
import geogebra.common.kernel.geos.GeoNumeric
import geogebra.common.main.App
import geogebra.gui.GuiManagerD
import geogebra.main.AppD
import geogebra.plugin.GgbAPID

class MathWorld(val _kojoCtx: KojoCtx, _ggbApi: GgbAPID, _ggbPanel: GeoGebraPanel, _Algo: Algo) {
  lazy val _app = _ggbApi.getApplication.asInstanceOf[AppD]
  lazy val _guim = _app.getGuiManager.asInstanceOf[GuiManagerD]
  lazy val _kernel = _app.getKernel
  lazy val casView = new CasView(this)

  private def ensureVisible() {
    _kojoCtx.makeMathWorldVisible()
  }

  def clear() {
    Utils.runInSwingThreadAndWait {
      ensureVisible()
      _app.setSaved()
      _app.fileNew()
      casView.clear()
    }
  }

  // for unit tests
  private[mathworld] def _clear2() {
    Utils.runInSwingThread {
      _app.setSaved()
      _app.fileNew()
    }
  }

  def showAxes() {
    Utils.runInSwingThread {
      val app = _ggbApi.getApplication()
      app.getSettings().getEuclidian(1).setShowAxes(true, true)
      app.getSettings().getEuclidian(2).setShowAxes(true, true)
    }
  }

  def hideAxes() {
    Utils.runInSwingThread {
      val app = _ggbApi.getApplication()
      app.getSettings().getEuclidian(1).setShowAxes(false, false)
      app.getSettings().getEuclidian(2).setShowAxes(false, false)
    }
  }

  def showGrid() {
    Utils.runInSwingThread {
      _ggbApi.setGridVisible(true)
    }
  }

  def hideGrid() {
    Utils.runInSwingThread {
      _ggbApi.setGridVisible(false)
    }
  }

  def showAlgebraView() {
    Utils.runInSwingThread {
      _guim.setShowView(true, App.VIEW_ALGEBRA)
    }
  }

  def hideAlgebraView() {
    Utils.runInSwingThread {
      _guim.setShowView(false, App.VIEW_ALGEBRA)
    }
  }

  def showCASView() {
    Utils.runInSwingThread {
      _guim.setShowView(true, App.VIEW_CAS)
    }
  }

  def hideCASView() {
    Utils.runInSwingThread {
      _guim.setShowView(false, App.VIEW_CAS)
    }
  }

  def clearCASView() {
    casView.clear()
  }

  def casEval(in: String) = _kernel.getGeoGebraCAS().evaluateGeoGebraCAS(in, null)
  def casParse(in: String) = _kernel.getParser().parseGeoGebraCAS(in)
  def asString(ve: ValidExpression) = ve.toString(StringTemplate.defaultTemplate)
  def isEqualExpr(e1: String, e2: String) = {
    asString(casParse(e1)) == asString(casParse(e2))
  }

  def zoom(factor: Double, cx: Double, cy: Double) {
    Utils.runInSwingThread {
      val view = _ggbApi.getApplication.getEuclidianView1
      val newZoom = factor * EuclidianView.SCALE_STANDARD
      view.setCoordSystem(view.getWidth / 2 - cx * newZoom, view.getHeight / 2 + cy * newZoom, newZoom, newZoom)
    }
  }

  def switchTo() = ensureVisible()

  def point(x: Double, y: Double, label: String = null): MwPoint = MwPoint(_ggbApi, x, y, Option(label))
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

  def intersect(l1: MwLine, l2: MwLine): MwPoint = _Algo.intersect(_ggbApi, l1, l2)
  def intersect(l: MwLine, c: MwCircle): Seq[MwPoint] = _Algo.intersect(_ggbApi, l, c)
  def intersect(c: MwCircle, l: MwLine): Seq[MwPoint] = intersect(l, c)
  def intersect(c1: MwCircle, c2: MwCircle): Seq[MwPoint] = _Algo.intersect(_ggbApi, c1, c2)

  def midpoint(ls: MwLineSegment): MwPoint = _Algo.midpoint(ls)
  def perpendicular(l: MwLine, p: MwPoint): MwLine = _Algo.perpendicular(l, p)
  def parallel(l: MwLine, p: MwPoint): MwLine = _Algo.parallel(l, p)

  def show(shapes: VisualElement*) {
    Utils.runInSwingThread {
      shapes.foreach { s => s.show }
    }
  }

  // quick and dirty stuff for now
  import geogebra.kernel._

  def variable(name: String, value: Double, min: Double, max: Double, increment: Double, x: Int, y: Int) {
    Throttler.throttle()
    Utils.runInSwingThread {
      val number = new GeoNumeric(_ggbApi.getConstruction)
      number.setEuclidianVisible(true)
      number.setSliderLocation(x, y, true)
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
    Utils.runInSwingThread {
      _ggbApi.evalCommand(cmd)
    }
  }

  def turtle(x: Double, y: Double) = {
    Utils.runInSwingThreadAndWait {
      new MwTurtle(x, y, this)
    }
  }

}
