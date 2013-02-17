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

  def kprintln(outText: String) {}
  def reportOutput(outText: String) {}
  def reportWorksheetOutput(result: String, lineNum: Int) {}
  def reportErrorMsg(errMsg: String) {}
  def reportErrorText(errText: String) {}
  def reportSmartErrorText(errText: String, line: Int, column: Int, offset: Int) {}

  def readInput(prompt: String) = ""

  def showScriptInOutput() {}
  def hideScriptInOutput() {}
  def showVerboseOutput() {}
  def hideVerboseOutput() {}
  def clearOutput() {}
  def setScript(code: String) {}
  def insertCodeInline(code: String) {}
  def insertCodeBlock(code: String) {}

  def stopActivity() {}
  def clickRun() {}
  def setAstStopPhase(phase: String) {}
  def astStopPhase: String = "typer"
}
