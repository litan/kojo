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

package net.kogics.kojo.core

import javax.swing.JFrame
import java.awt.Color

trait KojoCtx {
  def activateDrawingCanvas()
  def activateScriptEditor()
  def activateOutputPane()
  def makeStagingVisible()
  def makeTurtleWorldVisible()
  def makeMathWorldVisible()
  def makeStoryTellerVisible()
  def make3DCanvasVisible()
  def baseDir: String
  def stopInterpreter(): Unit
  def stopAnimation(): Unit
  def scrollOutputToEnd(): Unit
  def frame: JFrame
  def fileOpened(file: java.io.File): Unit
  def fileClosed(): Unit
  def getLastLoadStoreDir(): String
  def setLastLoadStoreDir(dir: String): Unit
  def saveAsFile(): Unit
  def drawingCanvasActivated(): Unit
  def mwActivated(): Unit
  def d3Activated(): Unit
  def lastColor: Color
  def lastColor_=(c: Color)
  def knownColors: List[String]
  def isVerboseOutput: Boolean
  def showVerboseOutput(): Unit
  def hideVerboseOutput(): Unit
  def isSriptShownInOutput: Boolean
  def showScriptInOutput(): Unit
  def hideScriptInOutput(): Unit
  def clearOutput(): Unit
}
