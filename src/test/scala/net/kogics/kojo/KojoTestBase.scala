/*
 * Copyright (C) 2012 Lalit Pant <pant.lalit@gmail.com>
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

import net.kogics.kojo.lite.canvas.SpriteCanvas
import javax.swing.JFrame
import java.awt.Color

class KojoTestContext extends core.KojoCtx {
  def activateDrawingCanvas() {}
  def activateScriptEditor() {}
  def activateOutputPane() {}
  def makeStagingVisible() {}
  def makeTurtleWorldVisible() {}
  def makeMathWorldVisible() {}
  def makeStoryTellerVisible() {}
  def make3DCanvasVisible() {}
  def baseDir: String = System.getProperty("user.dir")
  def stopInterpreter() {}
  def stopAnimation() {}
  def scrollOutputToEnd() {}
  def frame: JFrame = null
  def fileOpened(file: java.io.File) {}
  def fileClosed() {}
  var llsdir = ""
  def getLastLoadStoreDir() = llsdir
  def setLastLoadStoreDir(dir: String) {llsdir = dir}
  def saveAsFile() {}
  def drawingCanvasActivated() {}
  def mwActivated(){}
  def d3Activated() {}
  var lc: Color = _
  def lastColor = lc
  def lastColor_=(c: Color) {lc = c}
  def knownColors: List[String] = List()
  def isVerboseOutput = false
  def showVerboseOutput() {}
  def hideVerboseOutput() {}
  def isSriptShownInOutput = false
  def showScriptInOutput() {}
  def hideScriptInOutput() {}
  def clearOutput() {}
  def userLanguage: String = "en"
  def userLanguage_=(lang: String) {}
  def switchToDefaultPerspective() {}
  def switchToScriptEditingPerspective() {}
  def switchToStoryViewingPerspective() {}
  def switchToHistoryBrowsingPerspective() {}
  def switchToCanvasPerspective() {}
  var fps = 50
}

class KojoTestBase {
  val kojoTestCtx = new KojoTestContext
  SpriteCanvas.initedInstance(kojoTestCtx)
  mathworld.GeoGebraCanvas.initedInstance(kojoTestCtx)
  story.StoryTeller.initedInstance(kojoTestCtx)
}

