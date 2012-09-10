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
}

class KojoTestBase {
  SpriteCanvas.initedInstance(new KojoTestContext)
//  mathworld.GeoGebraCanvas.initedInstance(KojoCtx.instance())
//  story.StoryTeller.initedInstance(KojoCtx.instance())
}

