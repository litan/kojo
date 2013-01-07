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
import java.util.prefs.Preferences
import bibliothek.gui.dock.common.CGrid
import bibliothek.gui.dock.common.CControl
import bibliothek.gui.dock.common.mode.ExtendedMode

object KojoCtx extends core.Singleton[KojoCtx] {
  protected def newInstance = new KojoCtx
}

class KojoCtx extends core.KojoCtx {

  val prefs = Preferences.userRoot().node("Kojolite-Prefs")

  @volatile var _userLanguage = prefs.get("user.language", System.getProperty("user.language"))
  if (_userLanguage != null && _userLanguage.trim != "") {
    java.util.Locale.setDefault(new java.util.Locale(_userLanguage))
    System.setProperty("user.language", _userLanguage)
  }

  var topcs: TopCs = _
  var frame: JFrame = _
  var saveAsActionListener: ActionListener = _
  var codeSupport: CodeExecutionSupport = _
  var control: CControl = _
  @volatile var fps = 50

  def switchToDefaultPerspective() {
    val grid = new CGrid(control)
    grid.add(0, 0, 1, 3, topcs.hih)
    grid.add(1, 0, 2, 3, topcs.sth)
    grid.add(3, 0, 3, 2, topcs.d3h)
    grid.add(3, 0, 3, 2, topcs.mwh)
    grid.add(3, 0, 3, 2, topcs.dch)
    grid.add(3, 2, 1.75, 1, topcs.seh)
    grid.add(4.75, 2, 1.25, 1, topcs.owh)
    control.getContentArea.deploy(grid)

    topcs.hih.setExtendedMode(ExtendedMode.MINIMIZED)
    topcs.sth.setExtendedMode(ExtendedMode.MINIMIZED)
    activateScriptEditor()
  }

  def switchToScriptEditingPerspective() {
    val grid = new CGrid(control)
    grid.add(0, 0, 1, 3, topcs.hih)
    grid.add(1, 0, 2, 3, topcs.sth)
    grid.add(3, 0, 3, 2, topcs.d3h)
    grid.add(3, 0, 3, 2, topcs.mwh)
    grid.add(3, 0, 3, 2, topcs.dch)
    grid.add(3, 2, 1.75, 1, topcs.seh)
    grid.add(4.75, 2, 1.25, 1, topcs.owh)
    control.getContentArea.deploy(grid)

    topcs.dch.setExtendedMode(ExtendedMode.MINIMIZED)
    topcs.hih.setExtendedMode(ExtendedMode.MINIMIZED)
    topcs.sth.setExtendedMode(ExtendedMode.MINIMIZED)
    activateScriptEditor()
  }

  def switchToStoryViewingPerspective() {
    val grid = new CGrid(control)
    grid.add(0, 0, 1, 3, topcs.hih)
    grid.add(1, 0, 2, 3, topcs.sth)
    grid.add(3, 0, 3, 2, topcs.d3h)
    grid.add(3, 0, 3, 2, topcs.mwh)
    grid.add(3, 0, 3, 2, topcs.dch)
    grid.add(3, 2, 1.75, 1, topcs.seh)
    grid.add(4.75, 2, 1.25, 1, topcs.owh)
    control.getContentArea.deploy(grid)

    topcs.hih.setExtendedMode(ExtendedMode.MINIMIZED)
    topcs.seh.setExtendedMode(ExtendedMode.MINIMIZED)
    topcs.owh.setExtendedMode(ExtendedMode.MINIMIZED)
    topcs.sth.toFront()
  }

  def switchToHistoryBrowsingPerspective() {
    val grid = new CGrid(control)
    grid.add(0, 0, 1, 3, topcs.hih)
    grid.add(1, 0, 2, 3, topcs.sth)
    grid.add(3, 0, 3, 2, topcs.d3h)
    grid.add(3, 0, 3, 2, topcs.mwh)
    grid.add(3, 0, 3, 2, topcs.dch)
    grid.add(3, 2, 1.75, 1, topcs.seh)
    grid.add(4.75, 2, 1.25, 1, topcs.owh)
    control.getContentArea.deploy(grid)

    topcs.dch.setExtendedMode(ExtendedMode.MINIMIZED)
    topcs.sth.setExtendedMode(ExtendedMode.MINIMIZED)
    topcs.owh.setExtendedMode(ExtendedMode.MINIMIZED)
    topcs.hih.toFront()
  }

  def switchToCanvasPerspective() {
    val grid = new CGrid(control)
    // total width = 4, total height = 3
    grid.add(0, 0, 1, 1.75, topcs.sth)
    grid.add(0, 1.75, 1, 1.25, topcs.owh)
    grid.add(1, 0, 3, 1.75, topcs.d3h)
    grid.add(1, 0, 3, 1.75, topcs.mwh)
    grid.add(1, 0, 3, 1.75, topcs.dch)

    grid.add(0, 3, 0, 0, topcs.hih)
    grid.add(1, 1.75, 3, 1.25, topcs.seh)
    control.getContentArea.deploy(grid)

    topcs.hih.setExtendedMode(ExtendedMode.MINIMIZED)
    topcs.seh.setExtendedMode(ExtendedMode.MINIMIZED)
    topcs.sth.setExtendedMode(ExtendedMode.MINIMIZED)
    topcs.owh.setExtendedMode(ExtendedMode.MINIMIZED)
    activateDrawingCanvasHolder()
  }

  def activateDrawingCanvasHolder() {
    topcs.dch.activate()
  }

  def activateDrawingCanvas() {
    topcs.dch.activateCanvas()
  }

  def activateScriptEditor() {
    topcs.seh.activate()
  }

  def activateOutputPane() {
    topcs.owh.toFront()
    //    topcs.owh.ow.requestFocusInWindow()
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
    if (!topcs.sth.isShowing) {
      topcs.sth.setExtendedMode(ExtendedMode.NORMALIZED)
      topcs.sth.toFront()
      //      topcs.sth.setLocation(CLocation.base.normalWest(0.5))
    }
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

  def baseDir: String = getLastLoadStoreDir + "/"

  def stopAnimation() = Utils.runInSwingThread {
    CodeExecutionSupport.instance.stopAnimation()
  }

  def stopInterpreter() = Utils.runInSwingThread {
    CodeExecutionSupport.instance.stopInterpreter()
  }

  def scrollOutputToEnd() {
    topcs.owh.scrollToEnd()
  }

  var fileName = ""
  def fileOpened(file: File) {
    fileName = file.getName
    topcs.seh.setTitleText("%s - %s" format ("Script Editor", fileName))
    CloseFile.onFileOpen()
  }

  def fileModified() {
    topcs.seh.setTitleText("%s - %s*" format ("Script Editor", fileName))
  }

  def fileSaved() {
    topcs.seh.setTitleText("%s - %s" format ("Script Editor", fileName))
  }

  def fileClosed() {
    fileName = ""
    topcs.seh.setTitleText("Script Editor")
    CloseFile.onFileClose()
  }

  @volatile var lastLoadStoreDir = prefs.get("lastLoadStoreDir", "")
  def getLastLoadStoreDir() = lastLoadStoreDir
  def setLastLoadStoreDir(dir: String) {
    lastLoadStoreDir = dir
    prefs.put("lastLoadStoreDir", lastLoadStoreDir)
  }

  def saveAsFile() {
    saveAsActionListener.actionPerformed(new ActionEvent(frame, 0, "Save As"))
  }

  @volatile var _lastColor = new Color(Integer.parseInt(prefs.get("lastColor", Integer.toString(Color.red.getRGB()))), true)
  def lastColor: Color = _lastColor
  def lastColor_=(c: Color) {
    _lastColor = c
    prefs.put("lastColor", Integer.toString(_lastColor.getRGB()))
  }

  def userLanguage: String = _userLanguage
  def userLanguage_=(lang: String) {
    _userLanguage = lang
    prefs.put("user.language", _userLanguage)
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

  def setOutputBackground(color: Color) {
    codeSupport.setOutputBackground(color)
  }

  def setOutputForeground(color: Color) {
    codeSupport.setOutputForeground(color)
  }
}