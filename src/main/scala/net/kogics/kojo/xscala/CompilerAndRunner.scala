/*
 * Copyright (C) 2010 Lalit Pant <pant.lalit@gmail.com>
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

import java.net.URL

import scala.collection.mutable.ListBuffer
import scala.reflect.internal.util.BatchSourceFile
import scala.reflect.internal.util.OffsetPosition
import scala.reflect.internal.util.Position
import scala.tools.nsc.Global
import scala.tools.nsc.Settings
import scala.tools.nsc.interactive
import scala.tools.nsc.interactive.Response
import scala.tools.nsc.interpreter.AbstractFileClassLoader
import scala.tools.nsc.io.VirtualDirectory
import scala.tools.nsc.reporters.Reporter
import scala.tools.nsc.util.ScalaClassLoader.URLClassLoader
import scala.tools.util.PathResolver

import net.kogics.kojo.core.RunContext

import core.CompletionInfo
import core.Interpreter.IR
import util.Utils

trait CompilerListener {
  def error(msg: String, line: Int, column: Int, offset: Int, lineContent: String)
  def warning(msg: String, line: Int, column: Int)
  def info(msg: String, line: Int, column: Int)
  def message(msg: String)
}

// This class borrows code and ideas from scala.tools.nsc.Interpreter
class CompilerAndRunner(makeSettings: () => Settings,
                        initCode: => Option[String],
                        listener: CompilerListener,
                        runContext: RunContext) extends StoppableCodeRunner {

  var counter = 0
  // The Counter above is used to define/create a new wrapper object for every run. The calling of the entry() 
  //.method within this object results in the initialization of the object, which causes the user submitted 
  // code to run.
  // If we don't increment the counter, the user code will not run (an object is initialized only once)
  // If this approach turns out to be too memory intensive, I'm sure there are other ways of running user 
  // submitted code.'
  val prefixHeader = "object Wrapper"
  val prefix0 = runContext.compilerPrefix

  def prefix = "%s%s\n" format (prefix0, initCode.getOrElse(""))

  def prefixLines = prefix.lines.size

  val codeTemplate = """%s
%s
}
"""

  var offsetDelta: Int = _

  val virtualDirectory = new VirtualDirectory("(memory)", None)

  def makeSettings2() = {
    val stng = makeSettings()
    stng.outputDirs.setSingleOutput(virtualDirectory)
    stng.deprecation.value = true
    stng.feature.value = true
    stng.unchecked.value = true
    stng
  }

  val settings = makeSettings2()

  val compilerClasspath: List[URL] = new PathResolver(settings) asURLs
  var classLoader = makeClassLoader
  // needed to prevent pcompiler from making the interp's classloader as 
  // its context loader (which causes a mem leak)
  // we could make pcompiler lazy, but then the first completion takes a big hit 
  classLoader.setAsContext()  

  private def makeClassLoader = {
    val parent = new URLClassLoader(compilerClasspath, getClass.getClassLoader())
    new AbstractFileClassLoader(virtualDirectory, parent)
  }
  private def loadByName(s: String): Class[_] = (classLoader loadClass s)

  val reporter = new Reporter {
    override def info0(position: Position, msg: String, severity: Severity, force: Boolean) {
      severity.count += 1
      lazy val line = position.line - prefixLines - 1 // we added an extra line after the prefix in the code template. Take it off
      lazy val offset = position.startOrPoint - offsetDelta - 1 // we added an extra newline char after the prefix
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

  def pfxWithCounter = "%s%d%s" format (prefixHeader, counter, prefix)

  def compile(code0: String, stopPhase: List[String] = List("cleanup")) = {
    val pfx = pfxWithCounter
    offsetDelta = pfx.length
    val code = codeTemplate format (pfx, code0)

    if (compiler.settings.stopAfter.value != stopPhase) {
      // There seems to be a bug in the PhasesSetting contains method
      // which makes the compiler not see the new stopAfter value
      // So we make a new Settings
      compiler.currentSettings = makeSettings2()
      compiler.settings.stopAfter.value = stopPhase
    }

    val run = new compiler.Run
    reporter.reset
    run.compileSources(List(new BatchSourceFile("scripteditor", code)))
    //    println(s"[Debug] Script checking done till phase: ${compiler.globalPhase.prev}")
    if (reporter.hasErrors) IR.Error else IR.Success
  }

  def compileAndRun(code0: String) = {
    virtualDirectory.clear()
    counter += 1
    val result = compile(code0, Nil)

    if (result == IR.Success) {
      if (Thread.interrupted) {
        listener.message("Thread interrupted")
        IR.Error
      }
      else {
        try {
          classLoader = makeClassLoader
          classLoader.setAsContext()
          val loadedResultObject = loadByName("Wrapper%d" format (counter))
          loadedResultObject.getMethod("entry").invoke(loadedResultObject)
          IR.Success
        }
        catch {
          case t: Throwable =>
            var realT = t
            while (realT.getCause != null) {
              realT = realT.getCause
            }
            if (realT.isInstanceOf[InterruptedException]) {
              listener.message("Execution thread interrupted.")
            }
            else {
              listener.message(Utils.stackTraceAsString(realT))
            }
            IR.Error
        }
      }
    }
    else {
      IR.Error
    }
  }

  def stop(interpThread: Thread) {
    interpThread.interrupt()
  }

  def parse(code0: String, browseAst: Boolean) = {
    compiler.currentSettings = makeSettings2()
    val pfx = pfxWithCounter
    offsetDelta = pfx.length
    val code = codeTemplate format (pfx, code0)

    compiler.settings.stopAfter.value = stopPhase()
    if (browseAst) {
      compiler.settings.browse.value = stopPhase()
    }
    val run = new compiler.Run
    reporter.reset
    run.compileSources(List(new BatchSourceFile("scripteditor", code)))

    if (reporter.hasErrors) {
      IR.Error
    }
    else {
      compiler.printAllUnits()
      IR.Success
    }
  }

  // phase after which you want to stop
  private def stopPhase() = {
    val ret = runContext.astStopPhase
    if (ret != null && ret != "") List(ret) else Nil
  }

  val preporter = new Reporter {
    override def info0(position: Position, msg: String, severity: Severity, force: Boolean) {
    }
  }
  /*lazy */ val pcompiler = new interactive.Global(settings, preporter)

  def typeAt(code0: String, offset: Int): String = {
    import interactive._
    import scala.reflect.internal.Trees

    classLoader.setAsContext()
    val pfx = pfxWithCounter
    val offsetDelta = pfx.length
    val code = codeTemplate format (pfx, code0)

    val source = new BatchSourceFile("scripteditor", code)
    val pos = new OffsetPosition(source, offset + offsetDelta + 1)

    var r1 = new Response[Unit]
    pcompiler.askReload(List(source), r1)

    var resp = new Response[pcompiler.Tree]
    pcompiler.askTypeAt(pos, resp)
    resp.get match {
      case Left(x) =>
        x match {
          case t: pcompiler.ValOrDefDef => t.tpt.toString
          case t: pcompiler.TypeDef     => t.name.toString
          case t: pcompiler.ClassDef    => t.name.toString
          case _                        => x.tpe.toString
        }

      case Right(y) =>
        // println("Right:" + y)
        ""
    }
  }

  import core.CompletionInfo

  def completions(code0: String, offset: Int, selection: Boolean): List[CompletionInfo] = {
    def addMarkerAfterOffset(c: String) = {
      "%s  ; // %s" format (c.substring(0, offset), c.substring(offset, c.length))
    }

    import interactive._

    classLoader.setAsContext()
    val pfx = pfxWithCounter
    val offsetDelta = pfx.length
    val code = codeTemplate format (pfx, addMarkerAfterOffset(code0))

    val source = new BatchSourceFile("scripteditor", code)
    val pos = new OffsetPosition(source, offset + offsetDelta + 1)

    var r1 = new Response[Unit]
    pcompiler.askReload(List(source), r1)

    var resp = new Response[List[pcompiler.Member]]
    if (selection) {
      pcompiler.askTypeCompletion(pos, resp)
    }
    else {
      pcompiler.askScopeCompletion(pos, resp)
    }
    resp.get match {
      case Left(x) =>
        // do this in imperitive style as opposed to using a filter and a map - to better deal 
        // with expections
        val elb = new ListBuffer[CompletionInfo]
        x.foreach { e =>
          try {
            if ((
              (e.sym.isMethod && !e.sym.isConstructor && e.sym.isPublic) ||
              (e.sym.isValue && !e.sym.isMethod && e.sym.nameString != "this") ||
              ((e.sym.isPackage || e.sym.isClass || e.sym.isType) && e.sym.isPublic)
            ) &&
              (
                e.tpe != pcompiler.NoType &&
                e.tpe != pcompiler.ErrorType
              )) {
              var prio = 100
              e match {
                case tm: pcompiler.TypeMember =>
                  if (tm.implicitlyAdded) prio += 20
                  if (tm.inherited) prio += 10
                case sm: pcompiler.ScopeMember =>
                  if (sm.implicitlyAdded) prio += 20
                case _ =>
              }

              // give vals and vars lower priority because we can't seem to distinguish 
              // between private and public vals/vars.
              // This way they go below the methods
              if (e.sym.isValue && !e.sym.isMethod) prio += 5
              if (e.sym.isClass || e.sym.isType) prio += 100
              if (e.sym.isPackage) prio += 200
              val cinfo = CompletionInfo(e.sym.nameString, e, prio)
              elb += cinfo
            }
          }
          catch {
            case t: Throwable =>
              println("Completion Problem 0: " + t.getMessage())
            // ignore, and move on to the next one
          }
        }
        elb.toList

      case Right(y) => /* println("Completion warning: %s" format(y)); */ Nil
    }
  }
}
