package net.kogics.kojo.lite

import net.kogics.kojo.core.Interpreter
import net.kogics.kojo.core.RunContext

class NoOpRunContext extends RunContext {
  def initInterp(interp: Interpreter) {}
  val compilerPrefix = """ {
  val builtins = net.kogics.kojo.lite.Builtins.instance
  import builtins._
"""
  def onInterpreterInit() {}
  def onInterpreterStart(code: String) {}
  def onRunError() {}
  def onRunSuccess() {}
  def onRunInterpError() {}
  def onCompileStart() {}
  def onCompileError() {}
  def onCompileSuccess() {}
  def onInternalCompilerError() {}

  def reportOutput(outText: String) {}
  def reportWorksheetOutput(result: String, lineNum: Int) {}
  def reportError(errMsg: String) {}
  def reportException(errText: String) {}
  def reportSmartError(errText: String, line: Int, column: Int, offset: Int) {}

  def astStopPhase: String = "typer"
}
