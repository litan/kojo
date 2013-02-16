package net.kogics.kojo
package lite

import javax.swing.JFrame
import java.awt.Color
import net.kogics.kojo.core.NoopSpriteListener
import net.kogics.kojo.util.Utils
import net.kogics.kojo.core.SpriteListener

class NoOpKojoCtx extends core.KojoCtx {
  Utils.kojoCtx = this
  def activityListener = NoopSpriteListener
  def setActivityListener(l: SpriteListener) {}
  def canvasLocation = null
  def fullScreenCanvasAction() = null
  def fullScreenOutputAction()= null
  def setStorytellerWidth(width: Int) {}
  def stopActivity() {}   
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
  def stopInterpreter() {}
  def stopStory() {}
  def scrollOutputToEnd() {}
  def frame: JFrame = null
  def fileOpened(file: java.io.File) {}
  def fileModified() {}
  def fileSaved() {}
  def fileClosed() {}
  var llsdir = ""
  def getLastLoadStoreDir() = llsdir
  def setLastLoadStoreDir(dir: String) {llsdir = dir}
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
  def switchToWorksheetPerspective() {}
  def switchToStoryViewingPerspective() {}
  def switchToHistoryBrowsingPerspective() {}
  def switchToCanvasPerspective() {}
  def setOutputBackground(color: Color) {}
  def setOutputForeground(color: Color) {}
  def setOutputFontSize(n: Int) {}
  def formatSource() {}
  var fps = 50
}
