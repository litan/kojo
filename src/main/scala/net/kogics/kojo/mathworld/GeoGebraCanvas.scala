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

import geogebra.GeoGebraPanel
import geogebra.gui.menubar.GeoGebraMenuBar
import net.kogics.kojo.core.KojoCtx
import java.io.File
import geogebra.common.main.App

class GeoGebraCanvas(kojoCtx: KojoCtx) extends GeoGebraPanel {
  setMaxIconSize(24)

  setShowAlgebraInput(true)
  setShowMenubar(false)
  setShowToolbar(true)

  setShowMenubar(true)
  setShowToolbar(true)
  buildGUI()

  val ggbApi = getGeoGebraAPI
  val Mw = new MathWorld(kojoCtx, ggbApi, this, new Algo(ggbApi))
}
