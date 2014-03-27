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
import java.awt.Cursor
import java.awt.Dimension
import java.awt.Toolkit
import java.awt.geom.Point2D
import java.io.File
import java.util.prefs.Preferences

import javax.swing.JCheckBoxMenuItem
import javax.swing.JFrame

import net.kogics.kojo.action.CloseFile
import net.kogics.kojo.core.DelegatingSpriteListener
import net.kogics.kojo.core.Picture
import net.kogics.kojo.core.SpriteListener
import net.kogics.kojo.lite.action.FullScreenBaseAction
import net.kogics.kojo.lite.action.FullScreenCanvasAction
import net.kogics.kojo.lite.action.FullScreenOutputAction
import net.kogics.kojo.lite.action.FullScreenSupport
import net.kogics.kojo.lite.i18n.LangInit
import net.kogics.kojo.story.StoryTeller
import net.kogics.kojo.util.Utils

import bibliothek.gui.dock.common.CControl
import bibliothek.gui.dock.common.CGrid
import bibliothek.gui.dock.common.CLocation
import bibliothek.gui.dock.common.DefaultSingleCDockable
import bibliothek.gui.dock.common.mode.ExtendedMode

class KojoCtx(val subKojo: Boolean) extends core.KojoCtx {

  val prefs = Preferences.userRoot().node("Kojolite-Prefs")
  @volatile var _userLanguage = LangInit.init(prefs)
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
    val seHeight = 1.1
    grid.add(0, 0, 1, 3, topcs.hih)
    grid.add(1, 0, 2, 3, topcs.sth)
    grid.add(3, 0, 3, 3 - seHeight, topcs.d3h)
    grid.add(3, 0, 3, 3 - seHeight, topcs.aah)
    grid.add(3, 0, 3, 3 - seHeight, topcs.mwh)
    grid.add(3, 0, 3, 3 - seHeight, topcs.dch)
    grid.add(3, 2, 1.7, seHeight, topcs.seh)
    grid.add(4.75, 2, 1.3, seHeight, topcs.owh)
    control.getContentArea.deploy(grid)

    topcs.hih.setExtendedMode(ExtendedMode.MINIMIZED)
    topcs.sth.setExtendedMode(ExtendedMode.MINIMIZED)
    activateScriptEditor()
  }

  def switchToDefault2Perspective() = Utils.runInSwingThread {
    val grid = new CGrid(control)
    val seWidth = 2
    val owHeight = 0.9
    grid.add(0, 0, 1, 3, topcs.hih)
    grid.add(1, 0, 2, 3, topcs.sth)
    grid.add(3, 0, seWidth, 3, topcs.seh)
    grid.add(3 + seWidth, 0, 7 - seWidth, 3 - owHeight, topcs.d3h)
    grid.add(3 + seWidth, 0, 7 - seWidth, 3 - owHeight, topcs.mwh)
    grid.add(3 + seWidth, 0, 7 - seWidth, 3 - owHeight, topcs.dch)
    grid.add(3 + seWidth, 2, 7 - seWidth, owHeight, topcs.owh)
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

  def switchToOutputStoryViewingPerspective() = Utils.runInSwingThread {
    val grid = new CGrid(control)
    grid.add(0, 0, 1, 2, topcs.hih)
    grid.add(1, 0, 1, 2, topcs.d3h)
    grid.add(1, 0, 1, 2, topcs.mwh)
    grid.add(1, 0, 1, 2, topcs.dch)
    grid.add(2, 0, 1, 2, topcs.sth)
    grid.add(3, 0, 3, 1, topcs.seh)
    grid.add(3, 1, 3, 1, topcs.owh)

    control.getContentArea.deploy(grid)

    topcs.hih.setExtendedMode(ExtendedMode.MINIMIZED)
    topcs.dch.setExtendedMode(ExtendedMode.MINIMIZED)
    topcs.sth.toFront()
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

  def makeTraceWindowVisible(tw: DefaultSingleCDockable) = Utils.runInSwingThread {
    if (!tw.isShowing) {
      control.addDockable(tw)
      tw.setLocation(CLocation.base.normalWest(0.3))
      tw.setVisible(true)
    }
    tw.toFront()
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
  def clickRun() = execSupport.compileRunCode()

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

  def setEditorFont(name: String) {
    topcs.seh.se.setFont(name)
  }

  def showStatusText(text: String) {
    statusBar.showText(text)
  }

  def showStatusCaretPos(line: Int, col: Int) {
    statusBar.showCaretPos(line, col)
  }

  def showAppWaitCursor() {
    val gp = frame.getGlassPane()
    gp.setVisible(true)
    gp.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR))
  }

  def hideAppWaitCursor() {
    val gp = frame.getGlassPane()
    gp.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR))
    gp.setVisible(false)
  }

  def picLine(p1: Point2D.Double, p2: Point2D.Double): Picture = {
    implicit val canvas = topcs.dch.dc
    picture.Pic { t =>
      t.setPosition(p1.x, p1.y)
      t.moveTo(p2.x, p2.y)
    }
  }

  def repaintCanvas() {
    topcs.dch.dc.repaint()
  }
}