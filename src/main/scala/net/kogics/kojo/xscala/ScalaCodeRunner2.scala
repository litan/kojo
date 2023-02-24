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
package net.kogics.kojo.xscala

import java.io.File
import java.io.PrintWriter
import java.io.Writer
import java.util.logging.Level
import java.util.logging.Logger

import scala.collection.mutable.ListBuffer
import scala.concurrent.duration._
import scala.concurrent.Await

import akka.actor.Actor
import akka.actor.Props
import akka.pattern.ask
import akka.util.Timeout
import net.kogics.kojo.core
import net.kogics.kojo.core.CodeRunner
import net.kogics.kojo.core.CodingMode
import net.kogics.kojo.core.CompletionInfo
import net.kogics.kojo.core.Interpreter.IR
import net.kogics.kojo.core.Interpreter.Settings
import net.kogics.kojo.core.RunContext
import net.kogics.kojo.core.TwMode
import net.kogics.kojo.core.VanillaMode
import net.kogics.kojo.util.Typeclasses.mkIdentity
import net.kogics.kojo.util.Typeclasses.some
import net.kogics.kojo.util.Utils

class ScalaCodeRunner2(val runContext: RunContext, val defaultMode: CodingMode) extends CodeRunner {
  val Log = Logger.getLogger("ScalaCodeRunner")
  val outputHandler = new InterpOutputHandler(runContext)

  // have a place to store interp actor state, in case of a restart of the actor
  val actorState = new InterpActorState
  // create the actor
  val codeRunner = makeCodeRunner

  private def makeCodeRunner = {
    val actor = Utils.actorSystem.actorOf(Props(new InterpActor), name = "InterpActor")
    actor
  }

  // its fine to access kojoInterpreter from within a Kojo script
  // as the script runs within an actor thread with appropriate Java Memory Model safety
  def kojoInterpreter = actorState.interpreter

  def start() = {
    codeRunner ! Init
  }

  def resetInterp(): Unit = {
    assert(false, "ResetInterp")
  }
  // entry point for interp reset from GUI
  def resetInterpUI() = codeRunner ! ResetInterp

  def kprintln(s: String) = print(s)

  val numLibkJars = Utils.numFilesInDir(Utils.libDir, "jar")
  if (numLibkJars > 0) {
    println("\nScanning libk...")
    println(s"Additional jars available (within libk) - $numLibkJars")
  }

  val extenstionDirs = Utils.dirsInDir(Utils.extensionsDir)
  if (extenstionDirs.length > 0) {
    println("\nScanning extensions...")
    extenstionDirs.foreach { dir =>
      val numJars = Utils.numFilesInDir(dir, "jar")
      println(s"Additional jars available (within $dir) - $numJars")
    }
  }

  def runCode(code: String): Unit = {
    // Runs on swing thread
    codeRunner ! RunCode(code)
  }

  def evalExpression(expr: String): Option[Any] = {
    try {
      val str = s"val ans = { $expr }"
      val result = kojoInterpreter.interp.interpret(str)
      if (result == IR.Success) kojoInterpreter.interp.valueOfTerm("ans") else None
    }
    catch {
      case t: Throwable =>
        println(t.getMessage)
        None
    }
  }

  def runWorksheet(code: String): Unit = {
    codeRunner ! RunWorksheet(code)
  }

  def compileRunCode(code: String): Unit = {
    codeRunner ! CompileRunCode(code)
  }

  def compileCode(code: String): Unit = {
    codeRunner ! CompileCode(code)
  }

  def parseCode(code: String, browseAst: Boolean): Unit = {
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
  case object ActivateVn
  case object ResetInterp
  val MaxResponseTime = 2000.seconds

  def askCodeRunner(m: Any): Any = {
    implicit val timeout = Timeout(MaxResponseTime)
    val fresult = codeRunner.ask(m)
    Await.result(fresult, timeout.duration)
  }

  def varCompletions(prefix: Option[String]): (List[String], Int) = {
    val resp = this.askCodeRunner(VarCompletionRequest(prefix)).asInstanceOf[CompletionResponse]
    resp.data
  }

  def keywordCompletions(prefix: Option[String]): (List[String], Int) = {
    val resp = this.askCodeRunner(KeywordCompletionRequest(prefix)).asInstanceOf[CompletionResponse]
    resp.data
  }

  def memberCompletions(
      code: String,
      caretOffset: Int,
      objid: String,
      prefix: Option[String]
  ): (List[CompletionInfo], Int) = {
    val resp =
      this.askCodeRunner(MemberCompletionRequest(code, caretOffset, objid, prefix)).asInstanceOf[CompletionResponse2]
    resp.data
  }

  def typeAt(code: String, caretOffset: Int): String = {
    this.askCodeRunner(TypeAtRequest(code, caretOffset)).asInstanceOf[String]
  }

  def activateTw(): Unit = {
    codeRunner ! ActivateTw
  }

  def activateVn(): Unit = {
    codeRunner ! ActivateVn
  }

  object InterruptionManager {
    var interpreterThread: Option[Thread] = None
    @volatile var interruptTimer: Option[javax.swing.Timer] = None
    var stoppable: Option[StoppableCodeRunner] = None

    def interruptionInProgress = interruptTimer.isDefined

    @annotation.nowarn
    def interruptInterpreter(): Unit = synchronized {
      // Runs on swing thread
      if (interruptionInProgress) {
        Log.info("Interruption of Interpreter Requested")
        Log.info("Interruption in progress. Bailing out")
        return
      }

      //      kprintln("Attempting to stop Script...\n")

      if (interpreterThread.isDefined) {
        Log.info("Interruption of Interpreter Requested")
        interruptTimer = Some(Utils.schedule(4) {
          // don't need to clean out interrupt state because Kojo needs to be shut down anyway
          // and in fact, cleaning out the interrupt state will mess with a delayed interruption
          Log.info("Interrupt timer fired")
          println("Unable to stop script.\nDoing a forced-stop. It's best to just restart Kojo!")
          interpreterThread.get.stop()
        })
        outputHandler.interpOutputSuppressed = true
        Log.info("Interrupting Interpreter thread...")
        stoppable.foreach { _.stop(interpreterThread.get) }
      }
    }

    def onInterpreterStart(stoppable: StoppableCodeRunner): Unit = synchronized {
      // Runs on Actor pool thread
      // we store the thread every time the interp runs
      // allows interp to switch between react and receive without impacting
      // interruption logic
      interpreterThread = Some(Thread.currentThread)
      this.stoppable = Some(stoppable)
    }

    def onInterpreterFinish(): Unit = synchronized {
      Log.info("Interpreter Done notification received")
      // Runs on Actor pool thread
      // might not be called for runaway computations
      // in which case Kojo has to be restarted

      if (Thread.interrupted) {
        // also clears thread interrupted flag
        Log.info("Thread was interrupted after compiling/running.")
      }

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

    def reply(m: Any): Unit = {
      sender() ! m
    }

    def interp: KojoInterpreter = {
      if (!interpInited) {
        interpInited = true
        actorState.interpInited = true
      }
      _interp
    }
    var _interp: KojoInterpreter = _
    var interpInited = false

    var compilerAndRunner: CompilerAndRunner = _

    override def postRestart(reason: Throwable): Unit = {
      val msg = "Code runner actor - postRestart triggered."
      Log.info(msg)
      compilerAndRunner = actorState.compilerAndRunner
      _interp = actorState.interpreter
      interpInited = actorState.interpInited
    }

    def safeProcessResponse[T](default: T)(fn: => T): Unit = {
      try {
        reply(fn)
      }
      catch {
        case t: Throwable =>
          Log.warning("Problem returning response: " + t.getMessage)
          reply(default)
      }
    }

    def safeProcessCompletionReq(fn: => (List[String], Int)): Unit = {
      try {
        reply(CompletionResponse(fn))
      }
      catch {
        case t: Throwable =>
          Log.warning("Problem finding completions: " + t.getMessage)
          reply(CompletionResponse(List(), 0))
      }
    }

    def safeProcessCompletionReq2(fn: => (List[CompletionInfo], Int)): Unit = {
      try {
        reply(CompletionResponse2(fn))
      }
      catch {
        case t: Throwable =>
          Log.warning("Problem finding completions: " + t.getMessage)
          reply(CompletionResponse2(List(), 0))
      }
    }

    def activateTurtleMode(): Unit = {
      // not the first turtle mode activation
      outputHandler.withOutputSuppressed {
        interp.interpret("import TSCanvas._")
        interp.interpret("import Tw._")
      }
      cmodeInit = "import TSCanvas._; import Tw._"
      mode = TwMode
      CodeCompletionUtils.activateTw()
      loadInitScripts(TwMode)
    }

    def activateVnMode(): Unit = {
      // Make turtle/picture world available to compiler even in Vanilla mode
      // Interpreter based completions/help are still missing in this mode.
      cmodeInit = "import TSCanvas._; import Tw._"
      mode = VanillaMode
      CodeCompletionUtils.activateVn()
      if (interpInited) {
        loadInitScripts(VanillaMode)
      }
    }

    // meant to be called from scripts - so that it runs on the interp thread
    def resetInterp() = {
      if (Thread.currentThread != InterruptionManager.interpreterThread.getOrElse(null)) {
        throw new RuntimeException("Resetting Interp from outside Interp running thread!")
      }
      realResetInterp()
    }

    private def realResetInterp() = Utils.safeProcess {
      interp.reset()
      initInterp()
      mode match {
        case TwMode =>
          outputHandler.withOutputSuppressed {
            interp.interpret("import TSCanvas._")
            interp.interpret("import Tw._")
            loadInitScripts(TwMode)
          }

        case VanillaMode =>
          outputHandler.withOutputSuppressed {
            loadInitScripts(VanillaMode)
          }
      }
    }

    def receive = {
      // Runs on Actor pool thread.
      // while(true) receive - ensures we stay on the same thread

      case Init =>
        Utils.safeProcess {
          loadInterp()
          printInitScriptsLoadMsg()
          if (defaultMode == TwMode) {
            initInterp()
            activateTurtleMode()
          }
          else {
            activateVnMode()
          }
          runContext.onInterpreterInit()
          loadCompiler()
        }

      case ActivateTw =>
        Utils.safeProcess {
          interp.reset()
          initInterp()
          activateTurtleMode()
        }

      case ActivateVn =>
        Utils.safeProcess {
          interp.reset()
          initInterp()
          activateVnMode()
        }

      case CompileCode(code) =>
        try {
          Log.info("CodeRunner actor compiling code:\n---\n%s\n---\n".format(code))
          InterruptionManager.onInterpreterStart(compilerAndRunner)
          runContext.onCompileStart()

          val ret = compile(code)
          Log.info("CodeRunner actor done compiling code. Return value %s".format(ret.toString))

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
          Log.info("CodeRunner actor compiling/running code:\n---\n%s\n---\n".format(code))
          InterruptionManager.onInterpreterStart(compilerAndRunner)
          runContext.onInterpreterStart(code)

          val ret = compileAndRun(code)
          Log.info("CodeRunner actor done compiling/running code. Return value %s".format(ret.toString))

          if (ret == IR.Success) {
            runContext.onRunSuccess()
          }
          else {
            Utils.clearGuiBatchQ()
            if (InterruptionManager.interruptionInProgress)
              runContext.onRunSuccess() // user cancelled running code; no errors
            else runContext.onRunError()
          }
        }
        catch {
          case _: InterruptedException =>
            println("Code runner actor - Interrupted")
          case t: Throwable =>
            Log.log(Level.SEVERE, "CompilerAndRunner Problem", t)
            runContext.onRunInterpError()
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
          Log.info("CodeRunner actor parsing code:\n---\n%s\n---\n".format(code))
          InterruptionManager.onInterpreterStart(compilerAndRunner)
          runContext.onCompileStart()

          val ret = compilerAndRunner.parse(code, browseAst)
          Log.info("CodeRunner actor done parsing code. Return value %s".format(ret.toString))

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

      case ResetInterp =>
        realResetInterp()
    }

    def runCode(code: String, asWorksheet: Boolean): Unit = {
      try {
        Log.info("CodeRunner actor running code:\n---\n%s\n---\n".format(code))
        InterruptionManager.onInterpreterStart(interp)
        runContext.onInterpreterStart(code)

        val ret = interpret(code, asWorksheet)
        Log.info("CodeRunner actor done running code. Return value %s".format(ret.toString))

        if (ret == IR.Incomplete) showIncompleteCodeMsg(code)

        if (ret == IR.Success) {
          runContext.onRunSuccess()
        }
        else {
          Utils.clearGuiBatchQ()
          if (InterruptionManager.interruptionInProgress)
            runContext.onRunSuccess() // user cancelled running code; no errors
          else runContext.onRunError()
        }
      }
      catch {
        case t: Throwable =>
          Log.log(Level.SEVERE, "Interpreter Problem", t)
          runContext.onRunInterpError()
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
      iSettings.nowarn.value = true
      iSettings
    }

    def compilerInitCode: Option[String] = {
      some(cmodeInit) |+| Utils.initCode(mode)
    }

    def loadCompiler(): Unit = {
      compilerAndRunner =
        new CompilerAndRunner(makeSettings, compilerInitCode, new CompilerOutputHandler(runContext), runContext)
      actorState.compilerAndRunner = compilerAndRunner
    }

    def initInterp(): Unit = {
      outputHandler.withOutputSuppressed {
        runContext.initInterp(interp)
      }
    }

    def printInitScriptsLoadMsg(): Unit = {
      if (Utils.initScripts.size > 0) {
        kprintln(Utils.initScripts.mkString("\n---\nLoading Init Scripts (from initk):\n * ", "\n * ", "\n---\n"))
      }

      if (Utils.installInitScripts.size > 0) {
        kprintln(
          Utils.installInitScripts
            .mkString("\n---\nLoading Init Scripts (from install initk):\n * ", "\n * ", "\n---\n")
        )
      }
    }

    def loadInitScripts(mode: CodingMode): Unit = {
      Utils.initCode(mode).foreach { code =>
        // println("\nRunning initk code...")
        println()
        print(Utils.loadString("S_OutputInitkRunning"))
        println("...")
        runCode(code, false)
      }
    }

    def loadInterp(): Unit = {
      val iSettings = makeSettings()

      _interp = new KojoInterpreter(iSettings, new GuiPrintWriter())
      // backdoor to give access to the interpreter inside the script editor. Used by beginner challenges.
      actorState.interpreter = _interp
    }

    def createCp(xs: List[String]): String = {
      xs.mkString(File.pathSeparatorChar.toString)
    }

    def interpretWorksheetLines(lines: List[(String, Int)], inError: Boolean = false): IR.Result = lines match {
      case Nil => IR.Success
      case (code, lnum) :: tail =>
        outputHandler.worksheetLineNum = Some(lnum)
        //        println("Interpreting:\n--%s--" format code)
        interp.interpret(code) match {
          case IR.Success =>
            outputHandler.clearWorksheetError(); interpretWorksheetLines(lines.tail)
          case IR.Error =>
            if (InterruptionManager.interruptionInProgress) { outputHandler.flushWorksheetError(); IR.Error }
            else
              tail match {
                case Nil =>
                  outputHandler.flushWorksheetError(); IR.Error
                case (code2, lnum2) :: tail2 => interpretWorksheetLines((s"$code\n$code2", lnum) :: tail2, true)
              }
          case IR.Incomplete =>
            if (inError) { outputHandler.flushWorksheetError(); IR.Error }
            else
              tail match {
                case Nil                     => IR.Incomplete
                case (code2, lnum2) :: tail2 => interpretWorksheetLines((s"$code\n$code2", lnum2) :: tail2)
              }
        }
    }

    def interpretAsWorksheet(code: String, includedLines: Int): IR.Result = {
      var inMultiLineComment = false
      def shouldIgnoreLine(line: String): Boolean = {
        line.trim match {
          case ""                      => true
          case l if l.startsWith("//") => true
          case l if l.startsWith("/*") =>
            inMultiLineComment = true; true
          case l if l.startsWith("*/") =>
            inMultiLineComment = false; true
          case l if l.startsWith("*") && inMultiLineComment => true
          case _                                            => false
        }
      }
      val lines = code.split("\n").toList.zipWithIndex.filter { case (line, _) => !shouldIgnoreLine(line) }
      try {
        outputHandler.includedLines = includedLines
        interpretWorksheetLines(lines)
      }
      finally {
        outputHandler.worksheetLineNum = None
        outputHandler.includedLines = 0
      }
    }

    def interpretAllLines(code: String): IR.Result = interp.interpret(code)

    def interpret(code0: String, asWorksheet: Boolean): IR.Result = {
      try {
        val (code, includedLines, _) = Utils.preProcessInclude(code0)
        if (asWorksheet)
          interpretAsWorksheet(code, includedLines)
        else
          interpretAllLines(code)
      }
      catch {
        case iae: IllegalArgumentException =>
          runContext.reportError(Utils.exceptionMessage(iae)); IR.Error
        case e: Throwable => throw e
      }
    }

    def compileAndRun(code: String): IR.Result = {
      compilerAndRunner.compileAndRun(code)
    }

    def compile(code: String): IR.Result = {
      compilerAndRunner.compile(code)
    }

    def showIncompleteCodeMsg(code: String): Unit = {
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
      if (interpInited) {
        def varFilter(s: String) = !VarDropFilter.contains(s) && !InternalVarsRe.matcher(s).matches
        val c2s = interp.unqualifiedIds.filter { s => ignoreCaseStartsWith(s, pfx) && varFilter(s) }
        (c2s, pfx.length)
      }
      else {
        (Nil, pfx.length)
      }
    }

    def keywordCompletions(prefix: Option[String]): (List[String], Int) = {
      val pfx = prefix.getOrElse("")
      val c2s = Keywords.filter { s => s != null && ignoreCaseStartsWith(s, pfx) }
      (c2s, pfx.length)
    }

    def memberCompletions(
        code0: String,
        caretOffset: Int,
        objid: String,
        prefix: Option[String]
    ): (List[CompletionInfo], Int) = {
      val pfx = prefix.getOrElse("")
      val offset = caretOffset - pfx.length
      val code =
        if (pfx.length > 0)
          code0.substring(0, offset).concat(code0.substring(offset + pfx.length))
        else code0

      compilerAndRunner.completions(code, offset, objid != null) match {
        case Nil =>
          val ics = completions(objid).filter { ignoreCaseStartsWith(_, pfx) }
          (ics.map { CompletionInfo(core.MemberKind.Var, _, "", "", 0, false, Nil, Nil, "", "") }, pfx.length)
        case _ @ccs =>
          (ccs.filter { ci => ignoreCaseStartsWith(ci.name, pfx) }, pfx.length)
      }
    }

    def typeAt(code: String, caretOffset: Int): String = {
      compilerAndRunner.typeAt(code, caretOffset)
    }
  }

  class GuiWriter extends Writer {
    override def write(s: String): Unit = {
      outputHandler.showInterpOutput(s)
    }

    def write(cbuf: Array[Char], off: Int, len: Int): Unit = {
      outputHandler.showInterpOutput(new String(cbuf, off, len))
    }

    def close(): Unit = {}
    def flush(): Unit = {}
  }

  class GuiPrintWriter() extends PrintWriter(new GuiWriter(), false) {

    override def write(s: String): Unit = {
      // intercept string writes and forward to the GuiWriter's string write() method
      out.write(s)
    }
  }
}

class InterpActorState {
  @volatile var interpreter: KojoInterpreter = _
  @volatile var interpInited = false
  @volatile var compilerAndRunner: CompilerAndRunner = _
}
