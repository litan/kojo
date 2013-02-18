/*
 * Copyright (C) 2009 Lalit Pant <pant.lalit@gmail.com>
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

import java.util.concurrent.CountDownLatch

import scala.tools.nsc.interpreter.Results

trait CodeRunner {
  def start()
  def interruptInterpreter(): Unit
  def runCode(code: String): Unit
  def runWorksheet(code: String): Unit
  def parseCode(code: String, browseAst: Boolean): Unit
  def compileCode(code: String): Unit
  def compileRunCode(code: String): Unit
  def varCompletions(prefix: Option[String]): (List[String], Int)
  def keywordCompletions(prefix: Option[String]): (List[String], Int)
  def memberCompletions(code: String, caretOffset: Int, objid: String, prefix: Option[String]): (List[CompletionInfo], Int)
  def typeAt(code: String, caretOffset: Int): String
  def activateTw(): Unit
  def activateStaging(): Unit
  def activateMw(): Unit
  def activateD3(): Unit
  def resetInterp(): Unit
  def runContext: RunContext
}

object Interpreter {
  type Settings = scala.tools.nsc.Settings
  val IR = Results
}


trait Interpreter {
  import Interpreter._
  def bind(name: String, boundType: String, value: Any): IR.Result
  def interpret(code: String): IR.Result
}

trait RunContext {
  def initInterp(interp: Interpreter)
  def compilerPrefix: String
  def onInterpreterInit(): Unit
  def onInterpreterStart(code: String): Unit
  def onRunError(): Unit
  def onRunSuccess(): Unit
  def onRunInterpError(): Unit
  def onCompileStart(): Unit
  def onCompileError(): Unit
  def onCompileSuccess(): Unit
  def onInternalCompilerError(): Unit

  def reportOutput(outText: String): Unit
  def reportWorksheetOutput(result: String, lineNum: Int): Unit
  def reportError(errMsg: String): Unit
  def reportException(errText: String): Unit
  def reportSmartError(errText: String, line: Int, column: Int, offset: Int): Unit

  def astStopPhase: String
}
