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

import net.kogics.kojo.lite.NoOpRunContext

// Tests with User supplied init code loaded into the compiler
class CAndRWithSTHelpersTest extends CompilerAndRunnerTestBase {
  
  def userCode = """import TSCanvas._; import Tw._
class X {
    val x1 = 10
    var x2 = 20
    
    def m1() = x1 + x2
}

val x = new X
x.m1()
def fwd(n: Double) = forward(n)
"""

  def makeRunner() = {
    new CompilerAndRunner({() => settings}, Some(userCode), listener, new NoOpRunContext)
  }
}
