package net.kogics.kojo
package lite

import java.awt.Color
import java.awt.geom.Point2D

import javax.swing.Action
import javax.swing.JCheckBoxMenuItem
import javax.swing.JFrame
import javax.swing.JMenu

import net.kogics.kojo.core.NoopSpriteListener
import net.kogics.kojo.core.Picture
import net.kogics.kojo.core.SpriteListener
import net.kogics.kojo.util.Utils

import bibliothek.gui.dock.common.DefaultSingleCDockable

/**A Kojo context, which does nothing. Useful for test drivers.*/
class NoOpKojoCtx extends core.KojoCtx {
  Utils.kojoCtx = this
  def activityListener = NoopSpriteListener
  def setActivityListener(l: SpriteListener): Unit = {}
  def canvasLocation = null
  type ActionLike = Action
  def fullScreenCanvasAction() = null
  def fullScreenOutputAction() = null
  def updateMenuItem(mi: JCheckBoxMenuItem, action: ActionLike): Unit = {}
  def setStorytellerWidth(width: Int): Unit = {}
  def activateDrawingCanvasHolder(): Unit = {}
  def activateDrawingCanvas(): Unit = {}
  def activateScriptEditor(): Unit = {}
  def activateOutputPane(): Unit = {}
  def makeStagingVisible(): Unit = {}
  def makeTurtleWorldVisible(): Unit = {}
  def makeMathWorldVisible(): Unit = {}
  def makeStoryTellerVisible(): Unit = {}
  def make3DCanvasVisible(): Unit = {}
  def makeTraceWindowVisible(tw: DefaultSingleCDockable): Unit = {}
  def baseDir: String = System.getProperty("user.dir")
  def stopScript(): Unit = {}
  def stopInterpreter(): Unit = {}
  def stopActivity(): Unit = {}
  def stopStory(): Unit = {}
  def scrollOutputToEnd(): Unit = {}
  def frame: JFrame = null
  def fileOpened(file: java.io.File): Unit = {}
  def fileModified(): Unit = {}
  def fileSaved(): Unit = {}
  def fileClosed(): Unit = {}
  var llsdir = ""
  def getLastLoadStoreDir = llsdir
  def setLastLoadStoreDir(dir: String): Unit = { llsdir = dir }
  def drawingCanvasActivated(): Unit = {}
  def mwActivated(): Unit = {}
  def d3Activated(): Unit = {}
  var lc: Color = _
  def lastColor = lc
  def lastColor_=(c: Color): Unit = { lc = c }
  def knownColors: List[String] = List()
  def knownColor(name: String) = Color.white
  def knownColors2: List[String] = List()
  def knownColor2(name: String) = doodle.Color.white
  def isVerboseOutput = false
  def showVerboseOutput(): Unit = {}
  def hideVerboseOutput(): Unit = {}
  def isSriptShownInOutput = false
  def showScriptInOutput(): Unit = {}
  def hideScriptInOutput(): Unit = {}
  def clearOutput(): Unit = {}
  def kprintln(outText: String): Unit = {}
  def readInput(prompt: String): String = ""
  def setAstStopPhase(phase: String): Unit = {}
  def astStopPhase: String = "typer"
  def setScript(code: String): Unit = {}
  def insertCodeInline(code: String): Unit = {}
  def insertCodeBlock(code: String): Unit = {}
  def clickRun(): Unit = {}
  def clickInterpreterRun(): Unit = {}
  def clickWorksheetRun(): Unit = {}
  def userLanguage: String = "en"
  def userLanguage_=(lang: String): Unit = {}
  def switchToDefaultPerspective(): Unit = {}
  def switchToDefault2Perspective(): Unit = {}
  def switchToScriptEditingPerspective(): Unit = {}
  def switchToWorksheetPerspective(): Unit = {}
  def switchToStoryViewingPerspective(): Unit = {}
  def switchToHistoryBrowsingPerspective(): Unit = {}
  def switchToOutputStoryViewingPerspective(): Unit = {}
  def setOutputBackground(color: Color): Unit = {}
  def setOutputForeground(color: Color): Unit = {}
  def setOutputFontSize(n: Int): Unit = {}
  def formatSource(): Unit = {}
  def showStatusText(text: String): Unit = {}
  def showStatusCaretPos(line: Int, col: Int): Unit = {}
  def setEditorTabSize(ts: Int): Unit = {}
  def setEditorFont(name: String): Unit = {}
  def showAppWaitCursor(): Unit = {}
  def hideAppWaitCursor(): Unit = {}
  def picLine(p1: Point2D.Double, p2: Point2D.Double): Picture = null
  def repaintCanvas(): Unit = {}
  def appProperty(key: String): Option[String] = None
  def screenDpiFontDelta: Int = 0
  def baseFontSize: Int = 0
  def hiDpiFontIncrease: Int = 0
  def menuReady(m: JMenu): Unit = {}

  var fps = 50
  var screenDPI = 72
  def subKojo: Boolean = false
  def saveCodexIdPass(id: String, pass: String): Unit = {}
  def codexId: String = ""
  def codexPassword: String = ""
}
