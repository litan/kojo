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

import java.awt.Color
import java.awt.geom.Point2D

import javax.swing.Action
import javax.swing.JCheckBoxMenuItem
import javax.swing.JFrame

import bibliothek.gui.dock.common.DefaultSingleCDockable
import javax.swing.JMenu

import net.kogics.kojo.doodle

trait KojoCtx {
  def activityListener: SpriteListener
  def setActivityListener(l: SpriteListener): Unit
  def canvasLocation: java.awt.Point
  type ActionLike <: Action
  def fullScreenCanvasAction(): ActionLike
  def fullScreenOutputAction(): ActionLike
  def updateMenuItem(mi: JCheckBoxMenuItem, action: ActionLike): Unit
  def setStorytellerWidth(width: Int): Unit
  def activateDrawingCanvasHolder(): Unit
  def activateDrawingCanvas(): Unit
  def activateScriptEditor(): Unit
  def activateOutputPane(): Unit
  def makeStagingVisible(): Unit
  def makeTurtleWorldVisible(): Unit
  def makeStoryTellerVisible(): Unit
  def makeTraceWindowVisible(tw: DefaultSingleCDockable): Unit
  def baseDir: String
  def stopScript(): Unit
  def stopInterpreter(): Unit
  def stopActivity(): Unit
  def stopStory(): Unit
  def scrollOutputToEnd(): Unit
  def frame: JFrame
  def fileOpened(file: java.io.File): Unit
  def fileModified(): Unit
  def fileSaved(): Unit
  def fileClosed(): Unit
  def getLastLoadStoreDir: String
  def setLastLoadStoreDir(dir: String): Unit
  def lastColor: Color
  def lastColor_=(c: Color): Unit
  def knownColors: List[String]
  def knownColor(name: String): Color
  def knownColors2: List[String]
  def knownColor2(name: String): doodle.Color
  def isVerboseOutput: Boolean
  def showVerboseOutput(): Unit
  def hideVerboseOutput(): Unit
  def isSriptShownInOutput: Boolean
  def showScriptInOutput(): Unit
  def hideScriptInOutput(): Unit
  def clearOutput(): Unit
  def kprintln(outText: String): Unit
  def readInput(prompt: String): String
  def setAstStopPhase(phase: String): Unit
  def astStopPhase: String
  def setScript(code: String, caretAtEnd: Boolean): Unit
  def insertCodeInline(code: String): Unit
  def insertCodeBlock(code: String): Unit
  def clickRun(): Unit
  def clickInterpreterRun(): Unit
  def clickWorksheetRun(): Unit
  def userLanguage: String
  def userLanguage_=(lang: String): Unit
  def switchToDefaultPerspective(): Unit
  def switchToDefault2Perspective(): Unit
  def switchToScriptEditingPerspective(): Unit
  def switchToWorksheetPerspective(): Unit
  def switchToStoryViewingPerspective(): Unit
  def switchToHistoryBrowsingPerspective(): Unit
  def switchToOutputStoryViewingPerspective(): Unit
  def setOutputBackground(color: Color): Unit
  def setOutputForeground(color: Color): Unit
  def setOutputFontSize(size: Int): Unit
  def formatSource(): Unit
  def setEditorTabSize(ts: Int): Unit
  def setEditorFont(name: String): Unit
  def showStatusText(text: String): Unit
  def showStatusCaretPos(line: Int, col: Int): Unit
  def showAppWaitCursor(): Unit
  def hideAppWaitCursor(): Unit
  def picLine(p1: Point2D.Double, p2: Point2D.Double): Picture
  def repaintCanvas(): Unit
  def screenDpiFontDelta: Int
  def baseFontSize: Int
  def hiDpiFontIncrease: Int
  def menuReady(m: JMenu): Unit

  var fps: Int
  var screenDPI: Int
  def subKojo: Boolean
  def saveCodexIdPass(id: String, pass: String): Unit
  def codexId: String
  def codexPassword: String

  def getEditorText: String
  def clearOutputError(): Unit
  def insertOutputError(text: String): Unit
}
