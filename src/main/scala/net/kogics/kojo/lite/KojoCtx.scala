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

import java.awt.Color
import java.awt.Dimension
import java.awt.Toolkit
import java.io.File
import java.util.prefs.Preferences

import javax.swing.JCheckBoxMenuItem
import javax.swing.JFrame

import net.kogics.kojo.action.CloseFile
import net.kogics.kojo.core.DelegatingSpriteListener
import net.kogics.kojo.core.SpriteListener
import net.kogics.kojo.lite.action.FullScreenBaseAction
import net.kogics.kojo.lite.action.FullScreenCanvasAction
import net.kogics.kojo.lite.action.FullScreenOutputAction
import net.kogics.kojo.lite.action.FullScreenSupport
import net.kogics.kojo.story.StoryTeller
import net.kogics.kojo.util.Utils

import bibliothek.gui.dock.common.CControl
import bibliothek.gui.dock.common.CGrid
import bibliothek.gui.dock.common.mode.ExtendedMode

class KojoCtx(val subKojo: Boolean) extends core.KojoCtx {

  val prefs = Preferences.userRoot().node("Kojolite-Prefs")

  @volatile var _userLanguage = prefs.get("user.language", System.getProperty("user.language"))
  if (_userLanguage != null && _userLanguage.trim != "") {
    java.util.Locale.setDefault(new java.util.Locale(_userLanguage))
    System.setProperty("user.language", _userLanguage)
  }

  var topcs: TopCs = _
  var frame: JFrame = _
  var execSupport: CodeExecutionSupport = _
  var storyTeller: StoryTeller = _
  var control: CControl = _
  @volatile var fps = 50
  @volatile var screenDPI = Toolkit.getDefaultToolkit.getScreenResolution
  var statusBar: StatusBar = _
  Utils.kojoCtx = this

  val activityListener = new DelegatingSpriteListener
  def setActivityListener(l: SpriteListener) {
    activityListener.setRealListener(l)
  }

  type ActionLike = FullScreenBaseAction
  def fullScreenCanvasAction(): ActionLike = FullScreenCanvasAction(topcs.dch, this)
  def fullScreenOutputAction(): ActionLike = FullScreenOutputAction(topcs.owh)
  def updateMenuItem(mi: JCheckBoxMenuItem, action: ActionLike) = FullScreenSupport.updateMenuItem(mi, action)

  def setStorytellerWidth(width: Int) = Utils.runInSwingThread {
    topcs.sth.setResizeRequest(new Dimension(width, 0), true)
  }

  def canvasLocation = Utils.runInSwingThreadAndWait {
    topcs.dch.getContentPane.getLocationOnScreen
  }

  def switchToDefaultPerspective() = Utils.runInSwingThread {
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

  def switchToScriptEditingPerspective() = Utils.runInSwingThread {
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

  def switchToWorksheetPerspective() = Utils.runInSwingThread {
    val grid = new CGrid(control)
    grid.add(0, 0, 1, 5, topcs.hih)
    grid.add(1, 0, 2, 5, topcs.sth)
    grid.add(3, 0, 3, 2, topcs.d3h)
    grid.add(3, 0, 3, 2, topcs.mwh)
    grid.add(3, 0, 3, 2, topcs.dch)
    grid.add(3, 2, 3, 1.65, topcs.seh)
    grid.add(3, 4, 3, 1.35, topcs.owh)
    control.getContentArea.deploy(grid)

    topcs.dch.setExtendedMode(ExtendedMode.MINIMIZED)
    topcs.hih.setExtendedMode(ExtendedMode.MINIMIZED)
    topcs.sth.setExtendedMode(ExtendedMode.MINIMIZED)
    activateScriptEditor()
  }

  def switchToStoryViewingPerspective() = Utils.runInSwingThread {
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

  def switchToHistoryBrowsingPerspective() = Utils.runInSwingThread {
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

  def switchToCanvasPerspective() = Utils.runInSwingThread {
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

  def activateDrawingCanvasHolder() = Utils.runInSwingThread {
    topcs.dch.activate()
  }

  def activateDrawingCanvas() = Utils.runInSwingThread {
    topcs.dch.activateCanvas()
  }

  def activateScriptEditor() = Utils.runInSwingThread {
    topcs.seh.activate()
  }

  def activateOutputPane() = Utils.runInSwingThread {
    topcs.owh.toFront()
    //    topcs.owh.ow.requestFocusInWindow()
  }

  def makeTurtleWorldVisible() = Utils.runInSwingThread {
    if (!topcs.dch.isShowing) {
      topcs.dch.toFront()
      activateScriptEditor()
    }
  }

  def makeStagingVisible() = makeTurtleWorldVisible()

  def makeMathWorldVisible() = Utils.runInSwingThread {
    if (!topcs.mwh.isShowing) {
      topcs.mwh.toFront()
      activateScriptEditor()
    }
  }

  def makeStoryTellerVisible() = Utils.runInSwingThread {
    if (!topcs.sth.isShowing) {
      topcs.sth.setExtendedMode(ExtendedMode.NORMALIZED)
      topcs.sth.toFront()
      //      topcs.sth.setLocation(CLocation.base.normalWest(0.5))
    }
  }

  def make3DCanvasVisible() = Utils.runInSwingThread {
    if (!topcs.d3h.isShowing) {
      topcs.d3h.toFront()
      activateScriptEditor()
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

  def stopScript() = execSupport.stopScript()
  def stopActivity() = execSupport.stopActivity()
  def stopInterpreter() = execSupport.stopInterpreter()
  def stopStory() = storyTeller.stop()

  def scrollOutputToEnd() {
    topcs.owh.scrollToEnd()
  }

  var fileName = ""
  def fileOpened(file: File) {
    fileName = file.getName
    topcs.seh.fileOpened(fileName)
    CloseFile.onFileOpen()
  }

  def fileModified() {
    topcs.seh.fileModified(fileName)
  }

  def fileSaved() {
    topcs.seh.fileSaved(fileName)
  }

  def fileClosed() {
    fileName = ""
    topcs.seh.fileClosed(fileName)
    CloseFile.onFileClose()
  }

  @volatile var lastLoadStoreDir = prefs.get("lastLoadStoreDir", "")
  def getLastLoadStoreDir() = lastLoadStoreDir
  def setLastLoadStoreDir(dir: String) {
    lastLoadStoreDir = dir
    prefs.put("lastLoadStoreDir", lastLoadStoreDir)
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
    execSupport.verboseOutput == true
  }

  def showVerboseOutput() {
    execSupport.verboseOutput = true
  }

  def hideVerboseOutput() = {
    execSupport.verboseOutput = false
  }

  def isSriptShownInOutput = {
    execSupport.showCode == true
  }

  def showScriptInOutput() {
    execSupport.showCode = true
  }

  def hideScriptInOutput() {
    execSupport.showCode = false
  }

  def clearOutput() = execSupport.clrOutput()
  def kprintln(outText: String) = execSupport.showOutput(outText)
  def readInput(prompt: String): String = execSupport.readInput(prompt)
  def setScript(code: String) = execSupport.setScript(code)
  def insertCodeInline(code: String) = execSupport.insertCodeInline(code)
  def insertCodeBlock(code: String) = execSupport.insertCodeBlock(code)
  def clickRun() = execSupport.runCode()

  @volatile var astStopPhase = "typer"
  def setAstStopPhase(phase: String) {
    astStopPhase = phase
  }

  def outputPane = topcs.owh.outputPane
  def setOutputBackground(color: Color) {
    outputPane.setOutputBackground(color)
  }

  def setOutputForeground(color: Color) {
    outputPane.setOutputForeground(color)
  }

  def setOutputFontSize(size: Int) {
    outputPane.setOutputFontSize(size)
  }

  def formatSource() {
    topcs.seh.se.formatAction.actionPerformed(null)
  }

  def setEditorTabSize(ts: Int) {
    topcs.seh.se.setTabSize(ts)
  }

  def showStatusText(text: String) {
    statusBar.showText(text)
  }

  def showStatusCaretPos(line: Int, col: Int) {
    statusBar.showCaretPos(line, col)
  }
}