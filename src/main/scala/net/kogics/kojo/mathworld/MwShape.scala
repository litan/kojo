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

import java.util.logging._
import net.kogics.kojo.util.Utils
import net.kogics.kojo.core.Labelled
import geogebra.common.plugin.GgbAPI
import geogebra.common.kernel.geos.GeoElement
import geogebra.awt.GColorD

trait MwShape extends Labelled {

  val ggbApi: GgbAPI
  protected def geogebraElement: GeoElement

  protected def ctorDone() {
//    ggbApi.getApplication.storeUndoInfo()
    hide()
    hideLabel()
//    repaint()
  }

  def repaint() {
//    geogebraElement.updateRepaint()
    geogebraElement.updateCascade()
    ggbApi.getKernel.notifyRepaint()
  }

  def hide() {
    Utils.runInSwingThread {
      geogebraElement.setEuclidianVisible(false)
      repaint()
    }
  }

  def show() {
    Utils.runInSwingThread {
      geogebraElement.setEuclidianVisible(true)
      repaint()
    }
  }

  def setColor(color: java.awt.Color) {
    Utils.runInSwingThread {
      geogebraElement.setObjColor(new GColorD(color))
      repaint()
    }
  }

  def setLineThickness(t:  Int) {
    Utils.runInSwingThread {
      geogebraElement.setLineThickness(t)
      repaint()
    }
  }

  def showNameInLabel() {
    Utils.runInSwingThread {
      geogebraElement.setLabelVisible(true)
      geogebraElement.setLabelMode(GeoElement.LABEL_NAME)
      repaint()
    }
  }

  def showNameValueInLabel() {
    Utils.runInSwingThread {
      geogebraElement.setLabelVisible(true)
      geogebraElement.setLabelMode(GeoElement.LABEL_NAME_VALUE)
      repaint()
    }
  }

  def showValueInLabel() {
    Utils.runInSwingThread {
      geogebraElement.setLabelVisible(true)
      geogebraElement.setLabelMode(GeoElement.LABEL_VALUE)
      repaint()
    }
  }

  def hideLabel() {
    Utils.runInSwingThread {
      geogebraElement.setLabelVisible(false)
      repaint()
    }
  }

  def showLabel() {
    Utils.runInSwingThread {
      geogebraElement.setLabelVisible(true)
      repaint()
    }
  }

  def setLabel(label: String) {
    Utils.runInSwingThread {
      geogebraElement.setLabelVisible(true)
      geogebraElement.setLabel(label)
      repaint()
    }
  }

  def label = {
    Utils.runInSwingThreadAndWait {
      geogebraElement.getLabelSimple()
    }
  }
}
