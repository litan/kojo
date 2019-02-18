/*
 * Copyright (C) 2011 Lalit Pant <pant.lalit@gmail.com>
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

import java.io.PrintWriter

import scala.tools.nsc.interpreter._

import net.kogics.kojo.core.Interpreter

class KojoInterpreter(settings: Interpreter.Settings, out: PrintWriter) extends StoppableCodeRunner with Interpreter {
  val interp = new IMain(settings, out) {
    override protected def parentClassLoader = classOf[KojoInterpreter].getClassLoader
  }
  //  interp.setContextClassLoader()
  val completer = new PresentationCompilerCompleter(interp)

  def setMaxPrintStringLen(n: Int): Unit = {
    interp.isettings.maxPrintString = n
  }
  def bind(name: String, boundType: String, value: Any) = interp.bind(name, boundType, value)
  def interpret(code: String) = {
    interp.classLoader.asContext {
      interp.interpret(code)
    }
  }
  def completions(id: String) = {
    val c = completer.complete(s"$id.", id.length + 1)
    c.candidates
  }
  def unqualifiedIds = interp.unqualifiedIds
  def stop(interpThread: Thread) {
    interpThread.interrupt()
    //    interp.lineManager.cancel()
  }
  def reset() = interp.reset()

  //  def evalExpr[T: Manifest](line: String): T = false
}
