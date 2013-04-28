package net.kogics.kojo
package lite

import java.awt.Color

import javax.swing.Action
import javax.swing.JCheckBoxMenuItem
import javax.swing.JFrame

import net.kogics.kojo.core.NoopSpriteListener
import net.kogics.kojo.core.SpriteListener
import net.kogics.kojo.util.Utils

class NoOpKojoCtx extends core.KojoCtx {
  Utils.kojoCtx = this
  def activityListener = NoopSpriteListener
  def setActivityListener(l: SpriteListener) {}
  def canvasLocation = null
  type ActionLike = Action
  def fullScreenCanvasAction() = null
  def fullScreenOutputAction() = null
  def updateMenuItem(mi: JCheckBoxMenuItem, action: ActionLike) {}
  def setStorytellerWidth(width: Int) {}
  def activateDrawingCanvasHolder() {}
  def activateDrawingCanvas() {}
  def activateScriptEditor() {}
  def activateOutputPane() {}
  def makeStagingVisible() {}
  def makeTurtleWorldVisible() {}
  def makeMathWorldVisible() {}
  def makeStoryTellerVisible() {}
  def make3DCanvasVisible() {}
  def baseDir: String = System.getProperty("user.dir")
  def stopScript(): Unit = {}
  def stopInterpreter() {}
  def stopActivity() {}
  def stopStory() {}
  def scrollOutputToEnd() {}
  def frame: JFrame = null
  def fileOpened(file: java.io.File) {}
  def fileModified() {}
  def fileSaved() {}
  def fileClosed() {}
  var llsdir = ""
  def getLastLoadStoreDir() = llsdir
  def setLastLoadStoreDir(dir: String) { llsdir = dir }
  def drawingCanvasActivated() {}
  def mwActivated() {}
  def d3Activated() {}
  var lc: Color = _
  def lastColor = lc
  def lastColor_=(c: Color) { lc = c }
  def knownColors: List[String] = List()
  def isVerboseOutput = false
  def showVerboseOutput() {}
  def hideVerboseOutput() {}
  def isSriptShownInOutput = false
  def showScriptInOutput() {}
  def hideScriptInOutput() {}
  def clearOutput() {}
  def kprintln(outText: String) {}
  def readInput(prompt: String): String = ""
  def setAstStopPhase(phase: String): Unit = {}
  def astStopPhase: String = "typer"
  def setScript(code: String): Unit = {}
  def insertCodeInline(code: String): Unit = {}
  def insertCodeBlock(code: String): Unit = {}
  def clickRun(): Unit = {}
  def userLanguage: String = "en"
  def userLanguage_=(lang: String) {}
  def switchToDefaultPerspective() {}
  def switchToScriptEditingPerspective() {}
  def switchToWorksheetPerspective() {}
  def switchToStoryViewingPerspective() {}
  def switchToHistoryBrowsingPerspective() {}
  def switchToCanvasPerspective() {}
  def setOutputBackground(color: Color) {}
  def setOutputForeground(color: Color) {}
  def setOutputFontSize(n: Int) {}
  def formatSource() {}
  def showStatusText(text: String): Unit = {}
  def showStatusCaretPos(line: Int, col: Int): Unit = {}
  def setEditorTabSize(ts: Int): Unit = {}
  var fps = 50
  var screenDPI = 72
  def subKojo: Boolean = false
}
