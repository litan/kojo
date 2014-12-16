package net.kogics.kojo.core

import java.awt.Color

trait CodeExecutionSupport {
  def activateTw(): Unit
  def activateStaging(): Unit
  def activateMw(): Unit
  def activateD3(): Unit
  def isTwActive: Boolean
  def isStagingActive: Boolean
  def isMwActive: Boolean
  def isD3Active: Boolean
  def kojoCtx: KojoCtx
  def showOutput(outText: String, color: Color): Unit
  def commandHistory: CommandHistory
  def loadCodeFromHistory(historyIdx: Int): Unit
}