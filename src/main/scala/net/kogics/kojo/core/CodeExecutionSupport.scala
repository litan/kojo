package net.kogics.kojo.core

import java.awt.Color

trait CodeExecutionSupport {
  def activateTw(): Unit
  def activateVn(): Unit
  def isTwActive: Boolean
  def isVnActive: Boolean
  def kojoCtx: KojoCtx
  def showOutput(outText: String, color: Color): Unit
  def commandHistory: CommandHistory
  def loadCodeFromHistory(historyIdx: Int): Unit
}
