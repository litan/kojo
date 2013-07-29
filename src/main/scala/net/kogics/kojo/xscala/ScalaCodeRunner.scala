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
package net.kogics.kojo
package xscala

import java.io.File
import java.io.PrintWriter
import java.io.Writer
import java.util.logging.Level
import java.util.logging.Logger

import scala.actors.Actor
import scala.collection.mutable.ListBuffer

import net.kogics.kojo.core.D3Mode
import net.kogics.kojo.util.Typeclasses.mkIdentity
import net.kogics.kojo.util.Typeclasses.some

import CodeCompletionUtils.InternalMethodsRe
import CodeCompletionUtils.InternalVarsRe
import CodeCompletionUtils.Keywords
import CodeCompletionUtils.MethodDropFilter
import CodeCompletionUtils.VarDropFilter
import core.Interpreter.IR
import core.Interpreter.Settings
import core.CodeRunner
import core.CodingMode
import core.CompletionInfo
import core.MwMode
import core.RunContext
import core.SCanvas
import core.StagingMode
import core.TwMode
import util.Utils

class ScalaCodeRunner(val runContext: RunContext) extends CodeRunner {
  val Log = Logger.getLogger("ScalaCodeRunner")
  val outputHandler = new InterpOutputHandler(runContext)

  // for debugging only!
  def kojointerp = codeRunner.interp
  def pcompiler = codeRunner.compilerAndRunner.pcompiler
  def compiler = codeRunner.compilerAndRunner.compiler

  val codeRunner = makeCodeRunner

  private def makeCodeRunner = {
    val actor = new InterpActor
    actor.start()
    actor
  }

  def start() = {
    codeRunner ! Init
  }

  def resetInterp() = codeRunner.resetInterp()

  if (Utils.libJars.size > 0) {
    kprintln(Utils.libJars.mkString("\n---\nJars (within libk) available for use:\n * ", "\n * ", "\n---\n"))
  }

  def kprintln(s: String) = print(s)

  def runCode(code: String) {
    // Runs on swing thread
    codeRunner ! RunCode(code)
  }

  def runWorksheet(code: String) {
    codeRunner ! RunWorksheet(code)
  }

  def compileRunCode(code: String) {
    codeRunner ! CompileRunCode(code)
  }

  def compileCode(code: String) {
    codeRunner ! CompileCode(code)
  }

  def parseCode(code: String, browseAst: Boolean) {
    codeRunner ! ParseCode(code, browseAst)
  }

  def interruptInterpreter() = InterruptionManager.interruptInterpreter()

  case object Init
  case class RunCode(code: String)
  case class RunWorksheet(code: String)
  case class CompileRunCode(code: String)
  case class CompileCode(code: String)
  case class ParseCode(code: String, browseAst: Boolean)
  case class VarCompletionRequest(prefix: Option[String])
  case class KeywordCompletionRequest(prefix: Option[String])
  case class MemberCompletionRequest(code: String, caretOffset: Int, objid: String, prefix: Option[String])
  case class TypeAtRequest(code: String, caretOffset: Int)
  case class CompletionResponse(data: (List[String], Int))
  case class CompletionResponse2(data: (List[CompletionInfo], Int))
  case object ActivateTw
  case object ActivateStaging
  case object ActivateMw
  case object ActivateD3

  def varCompletions(prefix: Option[String]): (List[String], Int) = {
    val resp = (codeRunner !? VarCompletionRequest(prefix)).asInstanceOf[CompletionResponse]
    resp.data
  }

  def keywordCompletions(prefix: Option[String]): (List[String], Int) = {
    val resp = (codeRunner !? KeywordCompletionRequest(prefix)).asInstanceOf[CompletionResponse]
    resp.data
  }

  def memberCompletions(code: String, caretOffset: Int, objid: String, prefix: Option[String]): (List[CompletionInfo], Int) = {
    val resp = (codeRunner !? MemberCompletionRequest(code, caretOffset, objid, prefix)).asInstanceOf[CompletionResponse2]
    resp.data
  }

  def typeAt(code: String, caretOffset: Int): String = {
    (codeRunner !? TypeAtRequest(code, caretOffset)).asInstanceOf[String]
  }

  def activateTw() {
    codeRunner ! ActivateTw
  }

  def activateStaging() {
    codeRunner ! ActivateStaging
  }

  def activateMw() {
    codeRunner ! ActivateMw
  }

  def activateD3() {
    codeRunner ! ActivateD3
  }

  object InterruptionManager {
    @volatile var interpreterThread: Option[Thread] = None
    @volatile var interruptTimer: Option[javax.swing.Timer] = None
    @volatile var stoppable: Option[StoppableCodeRunner] = None

    def interruptionInProgress = interruptTimer.isDefined

    def interruptInterpreter() {
      // Runs on swing thread
      if (interruptionInProgress) {
        Log.info("Interruption of Interpreter Requested")
        Log.info("Interruption in progress. Bailing out")
        return
      }

      //      kprintln("Attempting to stop Script...\n")

      if (interpreterThread.isDefined) {
        Log.info("Interruption of Interpreter Requested")
        Log.info("Interrupting Interpreter thread...")
        interruptTimer = Some(Utils.schedule(4) {
          // don't need to clean out interrupt state because Kojo needs to be shut down anyway
          // and in fact, cleaning out the interrupt state will mess with a delayed interruption
          Log.info("Interrupt timer fired")
          kprintln("Unable to stop script.\nPlease restart the Kojo Environment unless you see a 'Script Stopped' message soon.\n")
        })
        outputHandler.interpOutputSuppressed = true
        stoppable.get.stop(interpreterThread.get)
      }
    }

    def onInterpreterStart(stoppable: StoppableCodeRunner) {
      // Runs on Actor pool thread
      // we store the thread every time the interp runs
      // allows interp to switch between react and receive without impacting
      // interruption logic
      interpreterThread = Some(Thread.currentThread)
      this.stoppable = Some(stoppable)
    }

    def onInterpreterFinish() {
      Log.info("Interpreter Done notification received")
      // Runs on Actor pool thread
      // might not be called for runaway computations
      // in which case Kojo has to be restarted
      if (interruptTimer.isDefined) {
        Log.info("Cancelling interrupt timer")
        interruptTimer.get.stop
        interruptTimer = None
        outputHandler.interpOutputSuppressed = false
        kprintln("Script Stopped.\n")
      }
      interpreterThread = None
      stoppable = None
    }
  }

  class InterpActor extends Actor {

    var interp: KojoInterpreter = _
    var compilerAndRunner: CompilerAndRunner = _

    def safeProcessResponse[T](default: T)(fn: => T) {
      try {
        reply(fn)
      }
      catch {
        case t: Throwable =>
          Log.warning("Problem returning response: " + t.getMessage)
          reply(default)
      }
    }

    def safeProcessCompletionReq(fn: => (List[String], Int)) {
      try {
        reply(CompletionResponse(fn))
      }
      catch {
        case t: Throwable =>
          Log.warning("Problem finding completions: " + t.getMessage)
          reply(CompletionResponse(List(), 0))
      }
    }

    def safeProcessCompletionReq2(fn: => (List[CompletionInfo], Int)) {
      try {
        reply(CompletionResponse2(fn))
      }
      catch {
        case t: Throwable =>
          Log.warning("Problem finding completions: " + t.getMessage)
          reply(CompletionResponse2(List(), 0))
      }
    }

    def activateTurtleMode() {
      outputHandler.withOutputSuppressed {
        interp.interpret("import TSCanvas._")
        interp.interpret("import Tw._")
      }
      cmodeInit = "import TSCanvas._; import Tw._"
      mode = TwMode
      CodeCompletionUtils.activateTw()
      loadInitScripts(TwMode)
    }

    // meant to be called from scripts - so that it runs on the interp thread
    def resetInterp() = Utils.safeProcess {
      if (Thread.currentThread != InterruptionManager.interpreterThread.getOrElse(null)) {
        throw new RuntimeException("Resetting Interp from outside Interp running thread!")
      }

      interp.reset()
      initInterp()
      mode match {
        case TwMode =>
          outputHandler.withOutputSuppressed {
            interp.interpret("import TSCanvas._")
            interp.interpret("import Tw._")
            loadInitScripts(TwMode)
          }

        case StagingMode =>
          val imports = "import TSCanvas._; import Staging._"
          outputHandler.withOutputSuppressed {
            interp.interpret(imports)
            loadInitScripts(StagingMode)
          }

        case MwMode =>
          val imports = "import Mw._"
          outputHandler.withOutputSuppressed {
            interp.interpret(imports)
            loadInitScripts(MwMode)
          }

        case D3Mode =>
          val imports = "import D3._"
          outputHandler.withOutputSuppressed {
            interp.interpret(imports)
            loadInitScripts(D3Mode)
          }
      }
    }

    def act() {
      while (true) {
        receive {
          // Runs on Actor pool thread.
          // while(true) receive - ensures we stay on the same thread

          case Init =>
            Utils.safeProcess {
              loadInterp()
              printInitScriptsLoadMsg()
              activateTurtleMode()
              runContext.onInterpreterInit()
              loadCompiler()
            }

          case ActivateTw =>
            Utils.safeProcess {
              interp.reset()
              initInterp()
              activateTurtleMode()
            }

          case ActivateStaging =>
            Utils.safeProcess {
              interp.reset()
              initInterp()
              val imports = "import TSCanvas._; import Staging._"
              outputHandler.withOutputSuppressed {
                interp.interpret(imports)
              }
              cmodeInit = imports
              mode = StagingMode
              CodeCompletionUtils.activateStaging()
              loadInitScripts(StagingMode)
            }

          case ActivateMw =>
            Utils.safeProcess {
              interp.reset()
              initInterp()
              val imports = "import Mw._"
              outputHandler.withOutputSuppressed {
                interp.interpret(imports)
              }
              cmodeInit = imports
              mode = MwMode
              CodeCompletionUtils.activateMw()
              loadInitScripts(MwMode)
            }

          case ActivateD3 =>
            Utils.safeProcess {
              interp.reset()
              initInterp()
              val imports = "import D3._"
              outputHandler.withOutputSuppressed {
                interp.interpret(imports)
              }
              cmodeInit = imports
              mode = D3Mode
              CodeCompletionUtils.activateD3()
              loadInitScripts(D3Mode)
            }

          case CompileCode(code) =>
            try {
              Log.info("CodeRunner actor compiling code:\n---\n%s\n---\n" format (code))
              InterruptionManager.onInterpreterStart(compilerAndRunner)
              runContext.onCompileStart()

              val ret = compile(code)
              Log.info("CodeRunner actor done compiling code. Return value %s" format (ret.toString))

              if (ret == IR.Success) {
                runContext.onCompileSuccess()
              }
              else {
                runContext.onCompileError()
              }
            }
            catch {
              case t: Throwable =>
                Log.log(Level.SEVERE, "Compiler Problem", t)
                runContext.onInternalCompilerError()
            }
            finally {
              Log.info("CodeRunner actor doing final handling for code.")
              InterruptionManager.onInterpreterFinish()
            }

          case CompileRunCode(code) =>
            try {
              Log.info("CodeRunner actor compiling/running code:\n---\n%s\n---\n" format (code))
              InterruptionManager.onInterpreterStart(compilerAndRunner)
              runContext.onInterpreterStart(code)

              val ret = compileAndRun(code)
              Log.info("CodeRunner actor done compiling/running code. Return value %s" format (ret.toString))

              if (ret == IR.Success) {
                runContext.onRunSuccess()
              }
              else {
                if (InterruptionManager.interruptionInProgress) runContext.onRunSuccess() // user cancelled running code; no errors
                else runContext.onRunError()
              }
            }
            catch {
              case t: Throwable =>
                Log.log(Level.SEVERE, "CompilerAndRunner Problem", t)
                runContext.onRunInterpError
            }
            finally {
              Log.info("CodeRunner actor doing final handling for code.")
              InterruptionManager.onInterpreterFinish()
            }

          case RunCode(code) =>
            runCode(code, false)

          case RunWorksheet(code) =>
            runCode(code, true)

          case ParseCode(code, browseAst) =>
            try {
              Log.info("CodeRunner actor parsing code:\n---\n%s\n---\n" format (code))
              InterruptionManager.onInterpreterStart(compilerAndRunner)
              runContext.onCompileStart()

              val ret = compilerAndRunner.parse(code, browseAst)
              Log.info("CodeRunner actor done parsing code. Return value %s" format (ret.toString))

              if (ret == IR.Success) {
                runContext.onCompileSuccess()
              }
              else {
                runContext.onCompileError()
              }
            }
            catch {
              case t: Throwable =>
                Log.log(Level.SEVERE, "Compiler Problem", t)
                runContext.onInternalCompilerError()
            }
            finally {
              Log.info("CodeRunner actor doing final handling for code.")
              InterruptionManager.onInterpreterFinish()
            }

          case VarCompletionRequest(prefix) =>
            safeProcessCompletionReq {
              varCompletions(prefix)
            }

          case KeywordCompletionRequest(prefix) =>
            safeProcessCompletionReq {
              keywordCompletions(prefix)
            }

          case MemberCompletionRequest(code, caretOffset, objid, prefix) =>
            safeProcessCompletionReq2 {
              memberCompletions(code, caretOffset, objid, prefix)
            }

          case TypeAtRequest(code, caretOffset) =>
            safeProcessResponse("") {
              typeAt(code, caretOffset)
            }
        }
      }
    }

    def runCode(code: String, asWorksheet: Boolean) {
      try {
        Log.info("CodeRunner actor running code:\n---\n%s\n---\n" format (code))
        InterruptionManager.onInterpreterStart(interp)
        runContext.onInterpreterStart(code)

        val ret = interpret(code, asWorksheet)
        Log.info("CodeRunner actor done running code. Return value %s" format (ret.toString))

        if (ret == IR.Incomplete) showIncompleteCodeMsg(code)

        if (ret == IR.Success) {
          runContext.onRunSuccess()
        }
        else {
          if (InterruptionManager.interruptionInProgress) runContext.onRunSuccess() // user cancelled running code; no errors
          else runContext.onRunError()
        }
      }
      catch {
        case t: Throwable =>
          Log.log(Level.SEVERE, "Interpreter Problem", t)
          runContext.onRunInterpError
      }
      finally {
        Log.info("CodeRunner actor doing final handling for code.")
        InterruptionManager.onInterpreterFinish()
      }
    }

    var cmodeInit = ""
    var mode: CodingMode = _

    @volatile var classp: String = _
    val cachedJarsData = new ListBuffer[AnyRef]
    @volatile var numCachedJars = 0

    def makeSettings() = {
      val iSettings = new Settings()
      iSettings.usejavacp.value = true
      //      iSettings.deprecation.value = true
      //      iSettings.feature.value = true
      //      iSettings.unchecked.value = true
      iSettings
    }

    def compilerInitCode: Option[String] = {
      some(cmodeInit) |+| initCode(mode)
    }

    def loadCompiler() {
      compilerAndRunner = new CompilerAndRunner(makeSettings, compilerInitCode, new CompilerOutputHandler(runContext), runContext)
    }

    def initInterp() {
      outputHandler.withOutputSuppressed {
        runContext.initInterp(interp)
      }
    }

    def printInitScriptsLoadMsg() {
      if (Utils.initScripts.size > 0) {
        kprintln(Utils.initScripts.mkString("\n---\nLoading Init Scripts (from initk):\n * ", "\n * ", "\n---\n"))
      }

      if (Utils.installInitScripts.size > 0) {
        kprintln(Utils.installInitScripts.mkString("\n---\nLoading Init Scripts (from install initk):\n * ", "\n * ", "\n---\n"))
      }
    }

    def loadInitScripts(mode: CodingMode) {
      initCode(mode).foreach { code =>
        println("\nRunning initk code...")
        runCode(code, false)
      }
    }

    def initCode(mode: CodingMode): Option[String] = {
      if (Utils.isScalaTestAvailable) {
        some(Utils.scalaTestHelperCode) |+| Utils.kojoInitCode(mode)
      }
      else {
        Utils.kojoInitCode(mode)
      }
    }

    def loadInterp() {
      val iSettings = makeSettings()

      interp = new KojoInterpreter(iSettings, new GuiPrintWriter())
      initInterp()
    }

    def createCp(xs: List[String]): String = {
      xs.mkString(File.pathSeparatorChar.toString)
    }

    def interpretWorksheetLine(lines: List[(String, Int)]): IR.Result = lines match {
      case Nil => IR.Success
      case (code, lnum) :: tail =>
        outputHandler.worksheetLineNum = Some(lnum)
        //        println("Interpreting:\n--%s--" format code)
        interp.interpret(code) match {
          case IR.Success =>
            outputHandler.clearWorksheetError(); interpretWorksheetLine(lines.tail)
          case IR.Error =>
            tail match {
              case Nil =>
                outputHandler.flushWorksheetError(); IR.Error
              case (code2, lnum2) :: tail2 => interpretWorksheetLine((code + "\n" + code2, lnum) :: tail2)
            }
          case IR.Incomplete =>
            tail match {
              case Nil                     => IR.Incomplete
              case (code2, lnum2) :: tail2 => interpretWorksheetLine((code + "\n" + code2, lnum) :: tail2)
            }
        }
    }

    def interpretAsWorksheet(code: String): IR.Result = {
      val lines = code.split("\n").toList.zipWithIndex.filter { case (line, _) => line.trim() != "" && !line.trim().startsWith("//") }
      try {
        interpretWorksheetLine(lines)
      }
      finally {
        outputHandler.worksheetLineNum = None
      }
    }

    def interpretAllLines(code: String): IR.Result = interp.interpret(code)

    def interpret(code: String, asWorksheet: Boolean): IR.Result = {
      if (asWorksheet)
        interpretAsWorksheet(code)
      else
        interpretAllLines(code)
    }

    def compileAndRun(code: String): IR.Result = {
      compilerAndRunner.compileAndRun(code)
    }

    def compile(code: String): IR.Result = {
      compilerAndRunner.compile(code)
    }

    def showIncompleteCodeMsg(code: String) {
      val msg = """
      |error: Incomplete code fragment
      |You probably have a missing brace/bracket somewhere in your script
      """.stripMargin
      runContext.reportError(msg)
    }

    import CodeCompletionUtils._

    def ignoreCaseStartsWith(s1: String, s2: String) = s1.toLowerCase.startsWith(s2.toLowerCase)

    def completions(identifier: String) = {
      def methodFilter(s: String) = !MethodDropFilter.contains(s) && !InternalMethodsRe.matcher(s).matches

      Log.fine("Finding Identifier completions for: " + identifier)
      val completions = outputHandler.withOutputSuppressed {
        interp.completions(identifier).distinct.filter { s => methodFilter(s) }
      }
      Log.fine("Completions: " + completions)
      completions
    }

    def varCompletions(prefix: Option[String]): (List[String], Int) = {
      val pfx = prefix.getOrElse("")
      def varFilter(s: String) = !VarDropFilter.contains(s) && !InternalVarsRe.matcher(s).matches
      val c2s = interp.unqualifiedIds.filter { s => ignoreCaseStartsWith(s, pfx) && varFilter(s) }
      (c2s, pfx.length)
    }

    def keywordCompletions(prefix: Option[String]): (List[String], Int) = {
      val pfx = prefix.getOrElse("")
      val c2s = Keywords.filter { s => s != null && ignoreCaseStartsWith(s, pfx) }
      (c2s, pfx.length)
    }

    def memberCompletions(code: String, caretOffset: Int, objid: String, prefix: Option[String]): (List[CompletionInfo], Int) = {
      val pfx = prefix.getOrElse("")
      compilerAndRunner.completions(code, caretOffset - pfx.length, objid != null) match {
        case Nil =>
          (Nil, pfx.length)
        //          val ics = completions(objid).filter { ignoreCaseStartsWith(_, pfx) }
        //          (ics.map { CompletionInfo(_, null, 100) }, pfx.length)
        case _@ ccs =>
          (ccs.filter { ci => ignoreCaseStartsWith(ci.name, pfx) }, pfx.length)
      }
    }

    def typeAt(code: String, caretOffset: Int): String = {
      compilerAndRunner.typeAt(code, caretOffset)
    }
  }

  class GuiWriter extends Writer {
    override def write(s: String) {
      outputHandler.showInterpOutput(s)
    }

    def write(cbuf: Array[Char], off: Int, len: Int) {
      outputHandler.showInterpOutput(new String(cbuf, off, len))
    }

    def close() {}
    def flush() {}
  }

  class GuiPrintWriter() extends PrintWriter(new GuiWriter(), false) {

    override def write(s: String) {
      // intercept string writes and forward to the GuiWriter's string write() method
      out.write(s)
    }
  }
}
