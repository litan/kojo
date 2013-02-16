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
  def onInterpreterInit(): Unit
  def onInterpreterStart(code: String): Unit
  def onRunError(): Unit
  def onRunSuccess(): Unit
  def onRunInterpError(): Unit
  def onCompileStart(): Unit
  def onCompileError(): Unit
  def onCompileSuccess(): Unit
  def onInternalCompilerError(): Unit

  def kprintln(outText: String): Unit
  def reportOutput(outText: String): Unit
  def reportWorksheetOutput(result: String, lineNum: Int): Unit
  def reportErrorMsg(errMsg: String): Unit
  def reportErrorText(errText: String): Unit
  def reportSmartErrorText(errText: String, line: Int, column: Int, offset: Int): Unit

  def readInput(prompt: String): String

  def showScriptInOutput(): Unit
  def hideScriptInOutput(): Unit
  def showVerboseOutput(): Unit
  def hideVerboseOutput(): Unit
  def clearOutput(): Unit
  def setScript(code: String): Unit
  def insertCodeInline(code: String): Unit
  def insertCodeBlock(code: String): Unit

  def stopActivity(): Unit
  def clickRun(): Unit
  def setAstStopPhase(phase: String): Unit
  def astStopPhase: String
}

class ProxyCodeRunner(codeRunnerMaker: () => CodeRunner) extends CodeRunner {
  val latch = new CountDownLatch(1)
  @volatile var codeRunner: CodeRunner = _

  new Thread(new Runnable {
      def run {
        codeRunner = codeRunnerMaker()
        latch.countDown()
      }
    }).start()

  def interruptInterpreter() {
    latch.await()
    codeRunner.interruptInterpreter()
  }

  def runCode(code: String) {
    latch.await()
    codeRunner.runCode(code)
  }

  def runWorksheet(code: String) {
    latch.await()
    codeRunner.runWorksheet(code)
  }

  def compileRunCode(code: String) {
    latch.await()
    codeRunner.compileRunCode(code)
  }

  def compileCode(code: String) {
    latch.await()
    codeRunner.compileCode(code)
  }

  def parseCode(code: String, browseAst: Boolean) {
    latch.await()
    codeRunner.parseCode(code, browseAst)
  }

  def varCompletions(prefix: Option[String]): (List[String], Int) = {
    latch.await()
    codeRunner.varCompletions(prefix)
  }

  def keywordCompletions(prefix: Option[String]): (List[String], Int) = {
    latch.await()
    codeRunner.keywordCompletions(prefix)
  }
  
  def memberCompletions(code: String, caretOffset: Int, objid: String, prefix: Option[String]): (List[CompletionInfo], Int) = {
    latch.await()
    codeRunner.memberCompletions(code, caretOffset, objid, prefix)
  }
  
  def typeAt(code: String, caretOffset: Int): String = {
    latch.await()
    codeRunner.typeAt(code, caretOffset)
  }
  
  def activateTw() {
    latch.await()
    codeRunner.activateTw()
  }
  
  def activateStaging() {
    latch.await()
    codeRunner.activateStaging()
  }
  
  def activateMw() {
    latch.await()
    codeRunner.activateMw()
  }
  
  def activateD3() {
    latch.await()
    codeRunner.activateD3()
  }

  def resetInterp() {
    latch.await()
    codeRunner.resetInterp()
  }
  
  def runContext: RunContext = {
    latch.await()
    codeRunner.runContext
  }
}
