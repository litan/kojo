/*
 * Copyright (C) 2013 "Sami Jaber" <jabersami@gmail.com>
 * Copyright (C) 2013 Lalit Pant <pant.lalit@gmail.com>
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
package net.kogics.kojo.lite
package trace

import java.awt.Color
import java.awt.geom.Point2D
import java.io.File

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.asScalaIterator
import scala.collection.mutable.HashMap
import scala.reflect.internal.util.BatchSourceFile
import scala.reflect.internal.util.Position
import scala.tools.nsc.Global
import scala.tools.nsc.Settings
import scala.tools.nsc.reporters.Reporter
import scala.util.control.Breaks.break
import scala.util.control.Breaks.breakable
import scala.util.matching.Regex

import com.sun.jdi.AbsentInformationException
import com.sun.jdi.ArrayReference
import com.sun.jdi.Bootstrap
import com.sun.jdi.IntegerValue
import com.sun.jdi.InvocationException
import com.sun.jdi.LocalVariable
import com.sun.jdi.ObjectReference
import com.sun.jdi.ReferenceType
import com.sun.jdi.StackFrame
import com.sun.jdi.StringReference
import com.sun.jdi.ThreadReference
import com.sun.jdi.Value
import com.sun.jdi.VirtualMachine
import com.sun.jdi.connect.LaunchingConnector
import com.sun.jdi.event.BreakpointEvent
import com.sun.jdi.event.ClassPrepareEvent
import com.sun.jdi.event.MethodEntryEvent
import com.sun.jdi.event.MethodExitEvent
import com.sun.jdi.event.ThreadStartEvent
import com.sun.jdi.event.VMDeathEvent
import com.sun.jdi.event.VMDisconnectEvent
import com.sun.jdi.event.VMStartEvent
import com.sun.jdi.request.EventRequest

import net.kogics.kojo.core.RunContext
import net.kogics.kojo.core.Turtle
import net.kogics.kojo.core.TwMode
import net.kogics.kojo.lite.Builtins
import net.kogics.kojo.lite.ScriptEditor
import net.kogics.kojo.util.Utils
import net.kogics.kojo.xscala.CompilerOutputHandler

class Tracing(scriptEditor: ScriptEditor, builtins: Builtins, traceListener: TraceListener, runCtx: RunContext) {
  @volatile var currThread: ThreadReference = _
  val tmpdir = System.getProperty("java.io.tmpdir")
  val settings = makeSettings()
  val turtles = new HashMap[Long, Turtle]
  @volatile var evtReqs: Vector[EventRequest] = _
  @volatile var hiddenEventCount = 0
  @volatile var codeLines: Vector[String] = _
  @volatile var vmRunning = false

  val currEvtVec = new HashMap[String, MethodEvent]

  val listener = new CompilerOutputHandler(runCtx)

  val reporter = new Reporter {
    override def info0(position: Position, msg: String, severity: Severity, force: Boolean) {
      severity.count += 1
      lazy val line = position.line - lineNumOffset
      lazy val offset = position.startOrPoint - offsetDelta
      severity match {
        case ERROR if position.isDefined =>
          listener.error(msg, line, position.column, offset, position.lineContent)
        case WARNING if position.isDefined =>
          listener.warning(msg, line, position.column)
        case INFO if position.isDefined =>
          listener.info(msg, line, position.column)
        case _ =>
          listener.message(msg)
      }
    }
  }
  val compiler = new Global(settings, reporter)
  val tracingGUI = new TracingGUI(scriptEditor, builtins.kojoCtx)

  val prefix0 = """object Wrapper {
import net.kogics.kojo.lite.trace.TracingBuiltins._
import turtle0._
newTurtle(200, 200)
net.kogics.kojo.lite.i18n.LangInit()
def _main() {
"""

  val prefix = "%s%s\n" format (prefix0, Utils.initCode(TwMode).getOrElse(""))
  val prefixLines = prefix.lines.size
  @volatile var includedLines = 0
  def lineNumOffset = prefixLines + includedLines
  @volatile var offsetDelta = 0

  val codeTemplate = """%s%s
  }
def main(args: Array[String]) {
    _main()
  }
}
"""

  def stop() {
    traceListener.onEnd()
    if (vmRunning) {
      currThread.virtualMachine.exit(1)
    }
  }

  def compile(code00: String) = {
    val (code0, inclLines, includedChars) = Utils.preProcessInclude(code00)
    includedLines = inclLines
    offsetDelta = prefix.length + includedChars
    val code = codeTemplate format (prefix, code0)

    //    println(s"Tracing Code:\n$code\n---")

    val codeFile = new BatchSourceFile("scripteditor", code)
    val run = new compiler.Run
    reporter.reset
    run.compileSources(List(codeFile))
    if (reporter.hasErrors) {
      runCtx.onCompileError()
      // throw exception to stop trace
      throw new RuntimeException("Trace Compilation Error. Ensure that your program compiles correctly before trying to trace it.")
    }
  }

  def makeSettings() = {
    val iSettings = new Settings()
    iSettings.usejavacp.value = true
    iSettings.outputDirs.setSingleOutput(tmpdir)
    iSettings
  }

  def launchVM() = {
    val conns = Bootstrap.virtualMachineManager.allConnectors
    val connector = conns.find(_.name.equals("com.sun.jdi.RawCommandLineLaunch")).get.asInstanceOf[LaunchingConnector]

    // set connector arguments
    val connArgs = connector.defaultArguments()
    val command = connArgs.get("command")
    if (command == null)
      throw new Error("Bad launching connector")

    val port = 8001 + builtins.random(1000)

    val cmdLine = if (System.getProperty("os.name").contains("Windows"))
      s"""-Xrunjdwp:transport=dt_shmem,address=127.0.0.1:$port,suspend=y -classpath "$tmpdir${File.pathSeparator}${System.getProperty("java.class.path")}" -client -Xms32m -Xmx768m -XX:+UseConcMarkSweepGC -XX:+CMSClassUnloadingEnabled Wrapper"""
    else
      s"""-Xrunjdwp:transport=dt_socket,address=127.0.0.1:$port,suspend=y -classpath "$tmpdir${File.pathSeparator}${System.getProperty("java.class.path")}" -client -Xms32m -Xmx768m -XX:+UseConcMarkSweepGC -XX:+CMSClassUnloadingEnabled Wrapper"""

    val javaHome = System.getProperty("java.home")
    val javaExec =
      if (new File(javaHome + "/bin/javaw.exe").exists) {
        javaHome + "/bin/javaw"
      }
      else {
        javaHome + "/bin/java"
      }

    command.setValue(s""""$javaExec" $cmdLine""")

    //    println(s"command: $command")

    val address = connArgs.get("address")
    address.setValue(s"127.0.0.1:$port")

    val vm = connector.launch(connArgs)
    vm
  }

  val ignoreMethods = Set("main", "_main", "<init>", "<clinit>", "$init$", "repeat", "repeatWhile", "runInBackground")
  val turtleMethods = Set("setBackground", "forward", "right", "left", "turn", "clear", "cleari", "invisible", "jumpTo", "back", "setPenColor", "setFillColor", "setAnimationDelay", "setPenThickness", "penDown", "penUp", "savePosHe", "restorePosHe", "newTurtle", "changePosition", "scaleCostume", "setCostume", "setCostumes", "axesOn", "axesOff", "gridOn", "gridOff", "zoom")
  val notSupported = Set("Picture", "PicShape", "animate")

  def getThread(vm: VirtualMachine, name: String): ThreadReference =
    vm.allThreads.find(_.name == name).getOrElse(null)

  def trace(code: String) = {
    notSupported find { code.contains(_) } match {
      case Some(w) => println(s"Tracing not supported for programs with $w")
      case None    => realTrace(code)
    }
  }

  def realTrace(code: String) = Utils.runAsync {
    try {
      traceListener.onStart()
      turtles.clear()
      evtReqs = Vector[EventRequest]()
      currEvtVec.clear
      hiddenEventCount = 0
      codeLines = code.lines.toVector

      compile(code)
      val vm = launchVM()
      println("Tracing started...")
      val excludes = Array("java.*", "javax.*", "sun.*", "com.sun.*", "com.apple.*", "edu.umd.cs.piccolo.*")

      val evtQueue = vm.eventQueue

      tracingGUI.reset

      breakable {
        while (true) {
          val evtSet = evtQueue.remove()
          for (evt <- evtSet.eventIterator) {
            evt match {

              case threadStartEvt: ThreadStartEvent =>
                val name = threadStartEvt.thread.name
                if (name.contains("Thread-")) {
                  createMethodRequests(excludes, vm, threadStartEvt.thread)
                }

              case methodEnterEvt: MethodEntryEvent =>
                if (!(ignoreMethods.contains(methodEnterEvt.method.name) || methodEnterEvt.method.name.startsWith("apply"))) {
                  currThread = methodEnterEvt.thread
                  try {
                    handleMethodEntry(methodEnterEvt)
                  }
                  catch {
                    case t: Throwable =>
                      println(s"[Exception] [Method Enter] ${methodEnterEvt.method.name} -- ${t.getMessage}")
                      t.printStackTrace()
                  }
                }

              case methodExitEvt: MethodExitEvent =>
                if (!(ignoreMethods.contains(methodExitEvt.method.name) || methodExitEvt.method.name.startsWith("apply"))) {
                  currThread = methodExitEvt.thread
                  try {
                    handleMethodExit(methodExitEvt)
                  }
                  catch {
                    case t: Throwable =>
                      println(s"[Exception] [Method Exit] ${methodExitEvt.method.name} -- ${t.getMessage}")
                      t.printStackTrace()
                  }
                }

              case classPrepare: ClassPrepareEvent =>
                if (classPrepare.referenceType.name == "Wrapper$") {
                  classPrepare.request.disable()
                  createBreakpointRequest(classPrepare.referenceType, vm, currThread)
                }

              case bkpt: BreakpointEvent =>
                bkpt.request.disable()
                createMethodRequests(excludes, vm, currThread)
              //                watchThreadStarts()

              case vmDcEvt: VMDisconnectEvent =>
                vmRunning = false
                stop()
                println("VM Disconnected"); break

              case vmStartEvt: VMStartEvent =>
                vmRunning = true
                println("VM Started")
                currThread = vmStartEvt.thread
                createClassPrepareRequest(excludes, vm)

              case vmDeathEvt: VMDeathEvent =>
                vmRunning = false
                stop()
                println("VM Dead")

              case _ =>
                println("Other")
            }
          }
          evtSet.resume()
        }
      }
    }
    catch {
      case t: Throwable =>
        System.err.println(s"[Problem] -- ${t.getMessage}")
        t.printStackTrace()
        vmRunning = false
        stop()
    }
  }

  def printFrameVarInfo(stkfrm: StackFrame) {
    try {
      println(s"Visible Vars: ${stkfrm.visibleVariables}")
      println(s"Argument Values: ${stkfrm.getArgumentValues}")
    }
    catch {
      case t: Throwable =>
    }
  }

  def getCurrentMethodEvent: Option[MethodEvent] = currEvtVec.get(currThread.name)

  def updateCurrentMethodEvent(oNewEvt: Option[MethodEvent]) = oNewEvt match {
    case Some(newEvt) =>
      currEvtVec(currThread.name) = newEvt
    case None =>
      currEvtVec.remove(currThread.name)
  }

  def handleHiddenEvent(desc: String) {
    //    println(desc)
    hiddenEventCount += 1
    if (hiddenEventCount % 30 == 0) {
      print(".")
      if (hiddenEventCount % (30 * 30) == 0) {
        print("\n")
      }
    }
  }

  def targetToString(frameVal: Value) = {
    if (frameVal.isInstanceOf[ObjectReference] &&
      !frameVal.isInstanceOf[StringReference] &&
      !frameVal.isInstanceOf[ArrayReference]) {
      val objRef = frameVal.asInstanceOf[ObjectReference]
      val mthd = objRef.referenceType.methodsByName("toString").find(_.argumentTypes.size == 0).get

      evtReqs.foreach(_.disable)
      try {
        //        println(s"Invoking toString for $frameVal")
        val rtrndValue = objRef.invokeMethod(currThread, mthd, new java.util.ArrayList, 0)
        //        println(s"toString done: $rtrndValue")
        rtrndValue.asInstanceOf[StringReference].value()
      }
      catch {
        case inv: InvocationException =>
          println(s"error0 in invokeMethod: ${inv.exception}")
          frameVal.toString
        case inv: Throwable =>
          println(s"error in invokeMethod for $mthd: ${inv.getMessage}")
          frameVal.toString
      }
      finally {
        evtReqs.foreach(_.enable)
      }
    }
    else {
      frameVal.toString
    }
  }

  def localToString(frameVal: Value) = String.valueOf(frameVal)

  def desugar(name: String) = {
    val dindex = name.indexOf('$')
    if (dindex == -1) {
      name
    }
    else {
      val ret = name.substring(0, dindex)
      if (ret.length == 0) name else ret
    }
  }

  def handleMethodEntry(methodEnterEvt: MethodEntryEvent) {

    def methodArgs(value: Value => String): Seq[String] = try {
      if (methodEnterEvt.method.arguments.size > 0) {
        methodEnterEvt.method.arguments.map { n =>
          val frame = methodEnterEvt.thread.frame(0)
          val frameVal = frame.getValue(n)
          s"${n.name} = ${value(frameVal)}"
        }
      }
      else {
        Seq()
      }
    }
    catch {
      case e: AbsentInformationException => Seq("AbsentInformationException")
    }

    //    println(s"Prefix lines: ${prefixLines}")
    //    println(s"Included lines: ${includedLines}")
    //    println(s"Line Num Offset: ${lineNumOffset}")
    //    println(s"Line Num: ${methodEnterEvt.location.lineNumber}")
    //    println(s"Caller Line Num: ${currThread.frame(1).location.lineNumber}")

    val methodName = desugar(methodEnterEvt.method.name)
    val srcName = try { methodEnterEvt.location.sourceName } catch { case e: Throwable => "N/A" }
    val callerSrcName = try { currThread.frame(1).location.sourceName } catch { case _: Throwable => "N/A" }
    val lineNum = methodEnterEvt.location.lineNumber - lineNumOffset
    val callerLineNum = try { currThread.frame(1).location.lineNumber - lineNumOffset } catch { case _: Throwable => -1 }
    val callerLine = try { codeLines(callerLineNum - 1) } catch { case _: Throwable => "N/A" }
    val localArgs = try { methodEnterEvt.method.arguments.toList } catch { case e: AbsentInformationException => List[LocalVariable]() }
    val stkfrm = currThread.frame(0)
    val isTurtle = turtleMethods.contains(methodName)

    val newEvt = new MethodEvent()
    val mthdEvent = getCurrentMethodEvent
    newEvt.entryLineNum = lineNum
    newEvt.setParent(mthdEvent)
    newEvt.sourceName = srcName
    newEvt.callerSourceName = callerSrcName
    newEvt.callerLine = callerLine
    newEvt.callerLineNum = callerLineNum
    newEvt.methodName = methodName

    var ret: Option[(Point2D.Double, Point2D.Double)] = None
    if (isTurtle) {
      ret = runTurtleMethod(methodName, stkfrm, localArgs)
    }
    newEvt.turtlePoints = ret

    if ((srcName == "scripteditor" && lineNum > 0) || (callerSrcName == "scripteditor" && callerLine.contains(methodName))) {
      newEvt.args = methodArgs(targetToString)
      tracingGUI.addStartEvent(newEvt)
    }
    else {
      val desc = s"[Method Enter] ${methodName} -- ${methodEnterEvt.method.signature} -- ${methodEnterEvt.method.declaringType}"
      //      newEvt.args = methodArgs(localToString)
      handleHiddenEvent(desc)
    }

    updateCurrentMethodEvent(Some(newEvt))
  }

  def handleMethodExit(methodExitEvt: MethodExitEvent) {
    val methodName = desugar(methodExitEvt.method.name)
    val stkfrm = currThread.frame(0)
    val localArgs = try { methodExitEvt.method.arguments.toList } catch { case e: AbsentInformationException => List[LocalVariable]() }
    val retVal = methodExitEvt.returnValue

    runTurtleMethod2(methodName, stkfrm, localArgs, retVal)

    val mthdEvent = getCurrentMethodEvent
    mthdEvent.foreach { ce =>
      val lineNum = methodExitEvt.location.lineNumber - lineNumOffset
      val retValStr = localToString(retVal)
      ce.exitLineNum = lineNum

      if ((ce.sourceName == "scripteditor" && lineNum > 0) ||
        (ce.callerSourceName == "scripteditor" && ce.callerLine.contains(methodName) &&
          retValStr != "<void value>" && retValStr != "null")) {

        ce.returnVal = targetToString(retVal)
        tracingGUI.addEndEvent(ce)
      }
      else {
        ce.returnVal = retValStr
        val desc = s"[Method Exit] ${methodName} -- ${methodExitEvt.method.signature} -- ${methodExitEvt.method.declaringType}"
        handleHiddenEvent(desc)
      }
      updateCurrentMethodEvent(ce.parent)
    }
  }

  def runTurtleMethod(name: String, stkfrm: StackFrame, localArgs: List[LocalVariable]): Option[(Point2D.Double, Point2D.Double)] = {
    if (stkfrm.thisObject() == null) return None

    import builtins.Tw
    import builtins.TSCanvas
    var ret: Option[(Point2D.Double, Point2D.Double)] = None

    val turtle = {
      val caller = stkfrm.thisObject().uniqueID()
      turtles.getOrElse(caller, Tw.getTurtle)
    }

    name match {
      case "clear" =>
        TSCanvas.clear()
      case "cleari" =>
        TSCanvas.cleari()
      case "invisible" =>
        turtle.invisible
      case "forward" =>
        if (localArgs.length == 1) {
          val step = stkfrm.getValue(localArgs(0)).toString.toDouble
          turtle.forward(step)
          ret = turtle.lastLine
        }
      case "turn" =>
        val angle = stkfrm.getValue(localArgs(0)).toString.toDouble
        turtle.turn(angle)
      case "right" =>
      case "left"  =>
      case "back"  =>
      case "home" =>
        turtle.home
      case "jumpTo" =>
        val (x, y) = (stkfrm.getValue(localArgs(0)).toString.toDouble, stkfrm.getValue(localArgs(1)).toString.toDouble)
        turtle.jumpTo(x, y)
      case "setCostume" =>
        val str = stkfrm.getValue(localArgs(0)).toString
        println(str)
        turtle.setCostume(str.substring(1, str.length - 1))
      case "setPosition" =>
        val (x, y) = (stkfrm.getValue(localArgs(0)).toString.toDouble, stkfrm.getValue(localArgs(1)).toString.toDouble)
        turtle.setPosition(x, y)
      case "setPenColor" =>
        val color = getColor(stkfrm, localArgs)
        turtle.setPenColor(color)
      case "setFillColor" =>
        val color = getColor(stkfrm, localArgs)
        turtle.setFillColor(color)
      case "setAnimationDelay" =>
        val step = stkfrm.getValue(localArgs(0)).toString.toLong
        turtle.setAnimationDelay(step)
      case "setPenThickness" =>
        val thickness = stkfrm.getValue(localArgs(0)).toString.toDouble
        turtle.setPenThickness(thickness)
      case "penUp" =>
        turtle.penUp
      case "penDown" =>
        turtle.penDown
      case "savePosHe" =>
        turtle.savePosHe
      case "restorePosHe" =>
        turtle.restorePosHe
      case "newTurtle" =>
      // handled on the exit event
      case "changePosition" =>
        val (x, y) = (stkfrm.getValue(localArgs(0)).toString.toDouble, stkfrm.getValue(localArgs(1)).toString.toDouble)
        turtle.changePosition(x, y)
      case "scaleCostume" =>
        val a = stkfrm.getValue(localArgs(0)).toString.toDouble
        turtle.scaleCostume(a)
      //      case "setCostumes" =>
      //        val costumes = stkfrm.getValue(localArgs(0))
      //        turtle.setCostumes(costumes)
      case "setBackground" =>
        val c = getColor(stkfrm, localArgs)
        TSCanvas.tCanvas.setCanvasBackground(c)
      case "axesOn" =>
        TSCanvas.axesOn
      case "axesOff" =>
        TSCanvas.axesOff
      case "gridOn" =>
        TSCanvas.gridOn
      case "gridOff" =>
        TSCanvas.gridOff
      case "zoom" =>
        val (x, y, z) = (stkfrm.getValue(localArgs(0)).toString.toDouble, stkfrm.getValue(localArgs(1)).toString.toDouble, stkfrm.getValue(localArgs(0)).toString.toDouble)
        TSCanvas.zoom(x, y, z)
      case m @ _ =>
        println(s"**TODO** - Unimplemented Turtle method - $m")
    }
    ret
  }

  def runTurtleMethod2(name: String, stkfrm: StackFrame, localArgs: List[LocalVariable], retVal: Value) {
    name match {
      case "newTurtle" =>
        import builtins.TSCanvas
        if (localArgs.length == 3) {
          val (x, y, str) = (stkfrm.getValue(localArgs(0)).toString.toDouble, stkfrm.getValue(localArgs(1)).toString.toDouble, stkfrm.getValue(localArgs(2)).toString)
          val newTurtle = TSCanvas.newTurtle(x, y, str.substring(1, str.length - 1))
          val ref = retVal.asInstanceOf[ObjectReference].uniqueID()
          turtles(ref) = newTurtle
        }

      case _ =>
    }
  }

  def getColor(stkfrm: StackFrame, localArgs: List[LocalVariable]): Color = {
    val colorVal = stkfrm.getValue(localArgs(0)).asInstanceOf[ObjectReference]
    val str = targetToString(colorVal)
    val pattern = new Regex("\\d{1,3}")
    var rgb = Vector[Int]()
    (pattern findAllIn str).foreach(c => rgb = rgb :+ c.toInt)

    val alphaMthd = colorVal.referenceType.methodsByName("getAlpha")(0)
    val alphaValue = colorVal.invokeMethod(currThread, alphaMthd, new java.util.ArrayList, ObjectReference.INVOKE_SINGLE_THREADED)
    val alpha = alphaValue.asInstanceOf[IntegerValue].value

    new Color(rgb(0), rgb(1), rgb(2), alpha)
  }

  def watchThreadStarts() {
    val evtReqMgr = currThread.virtualMachine.eventRequestManager

    val thrdStartVal = evtReqMgr.createThreadStartRequest
    thrdStartVal.setSuspendPolicy(EventRequest.SUSPEND_ALL)
    //thrdStartVal.addThreadFilter(mainThread)
    thrdStartVal.enable()
    evtReqs = evtReqs :+ thrdStartVal
  }

  def createMethodRequests(excludes: Array[String], vm: VirtualMachine, thread: ThreadReference) {
    val evtReqMgr = vm.eventRequestManager

    val mthdEnterVal = evtReqMgr.createMethodEntryRequest
    excludes.foreach { mthdEnterVal.addClassExclusionFilter(_) }
    mthdEnterVal.addThreadFilter(thread)
    mthdEnterVal.setSuspendPolicy(EventRequest.SUSPEND_ALL)
    mthdEnterVal.enable()
    evtReqs = evtReqs :+ mthdEnterVal

    val mthdExitVal = evtReqMgr.createMethodExitRequest
    excludes.foreach { mthdExitVal.addClassExclusionFilter(_) }
    mthdExitVal.addThreadFilter(thread)
    mthdExitVal.setSuspendPolicy(EventRequest.SUSPEND_ALL)
    mthdExitVal.enable()
    evtReqs = evtReqs :+ mthdExitVal
  }

  def createClassPrepareRequest(excludes: Array[String], vm: VirtualMachine) {
    val evtReqMgr = vm.eventRequestManager
    val request = evtReqMgr.createClassPrepareRequest
    excludes.foreach { request.addClassExclusionFilter(_) }
    request.setSuspendPolicy(EventRequest.SUSPEND_ALL)
    request.enable()
  }

  def createBreakpointRequest(wrapperType: ReferenceType, vm: VirtualMachine, thread: ThreadReference) {
    val evtReqMgr = vm.eventRequestManager
    val realMain = wrapperType.methodsByName("_main")(0)
    val request = evtReqMgr.createBreakpointRequest(realMain.location)
    request.addThreadFilter(thread)
    request.setSuspendPolicy(EventRequest.SUSPEND_ALL)
    request.enable()
  }
}