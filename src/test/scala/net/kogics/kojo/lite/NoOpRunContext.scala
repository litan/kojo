package net.kogics.kojo.lite

import net.kogics.kojo.core.Interpreter
import net.kogics.kojo.core.RunContext

class NoOpRunContext extends RunContext {
  def initInterp(interp: Interpreter): Unit = {}
  val compilerPrefix = """ {
  val builtins = net.kogics.kojo.lite.Builtins.instance
  import builtins._
  class UserCode {
"""
  def onInterpreterInit(): Unit = {}
  def onInterpreterStart(code: String): Unit = {}
  def onRunError(): Unit = {}
  def onRunSuccess(): Unit = {}
  def onRunInterpError(): Unit = {}
  def onCompileStart(): Unit = {}
  def onCompileError(): Unit = {}
  def onCompileSuccess(): Unit = {}
  def onInternalCompilerError(): Unit = {}

  def reportOutput(outText: String): Unit = {}
  def reportWorksheetOutput(result: String, lineNum: Int): Unit = {}
  def reportError(errMsg: String): Unit = {}
  def reportException(errText: String): Unit = {}
  def reportSmartError(errText: String, line: Int, column: Int, offset: Int): Unit = {}

  def astStopPhase: String = "typer"
  def isStoryRunning: Boolean = false
  def baseDir: String = "."
}
