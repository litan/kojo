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
import scala.tools.nsc.interpreter.shell.ReplReporterImpl

import net.kogics.kojo.core.Interpreter

class KojoInterpreter(settings: Interpreter.Settings, out: PrintWriter) extends StoppableCodeRunner with Interpreter {
  val interp = new IMain(settings, None, settings, new ReplReporterImpl(settings, out)) {
    protected override def parentClassLoader = classOf[KojoInterpreter].getClassLoader
  }
  //  interp.setContextClassLoader()

  def bind(name: String, boundType: String, value: Any) = interp.bind(name, boundType, value)
  def interpret(code: String) = {
    interp.classLoader.asContext {
      interp.interpret(code)
    }
  }
  @annotation.nowarn
  def completions(id: String): List[String] = {
    interp.presentationCompile(id.length + 1, s"$id.") match {
      case Right(value) =>
        value.candidates(0)._2
      case Left(_) => Nil
    }
  }
  def unqualifiedIds = interp.unqualifiedIds
  def stop(interpThread: Thread): Unit = {
    interpThread.interrupt()
  }
  def reset() = interp.reset()
}
