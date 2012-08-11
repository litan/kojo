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

import net.kogics.kojo.core.InitedSingleton

import geogebra.GeoGebraPanel;
import geogebra.gui.menubar.GeoGebraMenuBar
import net.kogics.kojo.core.KojoCtx
import java.io.File

object GeoGebraCanvas extends InitedSingleton[GeoGebraCanvas] {
  def initedInstance(kojoCtx: KojoCtx) = synchronized {
    instanceInit()
    val ret = instance()
    MathWorld.initedInstance(kojoCtx, ret.ggbApi)
    ret
  }

  protected def newInstance = new GeoGebraCanvas
}

class GeoGebraCanvas extends GeoGebraPanel {
  setMaxIconSize(24)

  setShowAlgebraInput(true)
  setShowMenubar(false)
  setShowToolbar(true)

  buildGUI()
  app.getGuiManager().initMenubar()

  val ggbApi = getGeoGebraAPI

  def selectAllAction = app.getGuiManager().getMenuBar().asInstanceOf[GeoGebraMenuBar].getSelectAllAction

  def lastLoadStoreFile = {
    val cf = app.getCurrentFile
    if (cf == null) "" else cf.getAbsolutePath
  }

  def setLastLoadStoreFile(fileName: String) {
    if (fileName == null || fileName.trim() == "") {
      return
    }

    val file = new File(fileName)
    val parent = new File(file.getParent())
    if (parent.exists && parent.isDirectory) {
      app.setCurrentFile(file)
    }
  }

  def ensureWorkSaved(): Boolean = {
    app.isSaved || app.getGuiManager().saveCurrentFile()
  }
}
