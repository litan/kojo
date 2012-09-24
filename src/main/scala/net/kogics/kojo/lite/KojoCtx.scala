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
package lite

import net.kogics.kojo.util.Utils
import java.io.File
import bibliothek.gui.dock.common.CLocation
import javax.swing.JFrame
import net.kogics.kojo.action.CloseFile
import java.awt.event.ActionListener
import java.awt.event.ActionEvent
import java.awt.Color
import net.kogics.kojo.xscala.Builtins

object KojoCtx extends core.Singleton[KojoCtx] {
  protected def newInstance = new KojoCtx
}

class KojoCtx extends core.KojoCtx {

  var topcs: TopCs = _
  var frame: JFrame = _
  var saveAsActionListener: ActionListener = _
  var codeSupport: CodeExecutionSupport = _

  def activateDrawingCanvas() {
    topcs.dch.toFront()
    topcs.dch.dc.requestFocusInWindow()
  }

  def activateScriptEditor() {
    topcs.seh.activate()
  }

  def makeTurtleWorldVisible() {
    if (!topcs.dch.isShowing) {
      topcs.dch.toFront()
    }
  }

  def makeStagingVisible() = makeTurtleWorldVisible()

  def makeMathWorldVisible() {
    if (!topcs.mwh.isShowing) {
      topcs.mwh.toFront()
    }
  }

  def makeStoryTellerVisible() {
    topcs.sth.setLocation(CLocation.base.normalWest(0.5))
    //    topcs.sth.setExtendedMode(ExtendedMode.NORMALIZED)
  }

  def make3DCanvasVisible() {
    if (!topcs.d3h.isShowing) {
      topcs.d3h.toFront()
    }
  }

  def drawingCanvasActivated() {
    topcs.d3h.otherPaneActivated()
    topcs.mwh.otherPaneActivated()
  }

  def mwActivated() {
    topcs.d3h.otherPaneActivated()
  }

  def d3Activated() {
    topcs.mwh.otherPaneActivated()
  }

  def baseDir: String = System.getProperty("user.dir")

  def stopAnimation() = Utils.runInSwingThread {
    CodeExecutionSupport.instance.stopAnimation()
  }

  def stopInterpreter() = Utils.runInSwingThread {
    CodeExecutionSupport.instance.stopInterpreter()
  }

  def scrollOutputToEnd() {
    topcs.owh.scrollToEnd()
  }

  def fileOpened(file: File) {
    topcs.seh.setTitleText("%s - %s" format ("Script Editor", file.getName()))
    CloseFile.onFileOpen()
  }

  def fileClosed() {
    topcs.seh.setTitleText("Script Editor")
    CloseFile.onFileClose()
  }

  @volatile var lastLoadStoreDir = ""
  def getLastLoadStoreDir() = lastLoadStoreDir
  def setLastLoadStoreDir(dir: String) {
    lastLoadStoreDir = dir
  }

  def saveAsFile() {
    saveAsActionListener.actionPerformed(new ActionEvent(frame, 0, "Save As"))
  }

  @volatile var _lastColor = Color.white
  def lastColor: Color = _lastColor
  def lastColor_=(c: Color) {
    _lastColor = c
  }

  def knownColors = staging.KColor.knownColors

  def isVerboseOutput = {
    codeSupport.verboseOutput == true
  }
  
  def showVerboseOutput() {
    codeSupport.verboseOutput = true
  }
  
  def hideVerboseOutput() = {
    codeSupport.verboseOutput = false
  }
  
  def isSriptShownInOutput = {
    codeSupport.showCode == true
  }

  def showScriptInOutput() {
    codeSupport.showCode = true
  }
  
  def hideScriptInOutput() {
    codeSupport.showCode = false
  }

  def clearOutput() {
    codeSupport.clrOutput()
  }

}