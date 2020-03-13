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

package net.kogics.kojo
package xscala

import java.io.OutputStreamWriter
import java.io.PrintWriter

import scala.tools.nsc.Settings
import scala.tools.nsc.interpreter._

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.Matchers
import org.scalatest.junit.JUnitRunner

import net.kogics.kojo.lite.NoOpKojoCtx
import net.kogics.kojo.lite.TestEnv

@RunWith(classOf[JUnitRunner])
class InterpTest extends FunSuite with Matchers {
  import language.reflectiveCalls

  def fixture = new {
    val settings = new Settings()
    settings.usejavacp.value = true
    val interp = new KojoInterpreter(settings, new PrintWriter(new OutputStreamWriter(System.out)))
  }

  test("interpreter reset") {
    val context = fixture
    context.interp.interpret("""def twice(n: Int) = 2*n""")

    val result1 = context.interp.interpret("""twice(5)""")
    result1 should be(Results.Success)
    context.interp.unqualifiedIds should contain("twice")

    context.interp.reset()

    val result2 = context.interp.interpret("""twice(5)""")
    result2 should be(Results.Error)
    context.interp.unqualifiedIds should not contain ("twice")
  }

  test("interpreter imports") {
    val context = fixture

    val code = """
object builtins {
  object Tw {
    def clear() {}
    def undo() {}
  }
    
  object TSCanvas {
    def zoom() {}
  }
}
"""

    context.interp.interpret(code)
    context.interp.interpret("""import builtins._""")
    context.interp.interpret("import TSCanvas._")
    context.interp.interpret("import Tw._")
    context.interp.unqualifiedIds should contain("zoom")
  }

  test("interpreter imports (real)") {
    val context = fixture
    val testEnv = TestEnv(new NoOpKojoCtx)
    val execSupport = testEnv.execSupport

    println("Sending typeAt msg")
    val typeAt = execSupport.codeRunner.typeAt("", 1)
    println("typeAt msg response:" + typeAt)
    execSupport.codeRunner.runContext.initInterp(context.interp)
    println(context.interp.unqualifiedIds)
    context.interp.interpret("import TSCanvas._")
    context.interp.interpret("import Tw._")
    context.interp.unqualifiedIds should contain("zoom")
  }
}
