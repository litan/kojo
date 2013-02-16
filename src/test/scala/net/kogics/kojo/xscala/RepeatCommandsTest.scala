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

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers


@RunWith(classOf[JUnitRunner])
class RepeatCommandsTest extends FunSuite with ShouldMatchers {

  val rcs = new RepeatCommands {}
  
  test("repeat") {
    var x = 0
    rcs.repeat(4) {
      x += 1
    }
    x should equal(4)
  }
  
  test("repeati") {
    var x = 0
    var idxs: List[Int] = Nil
    rcs.repeati(4) { i =>
      x += 1
      idxs = i :: idxs
    }
    x should equal(4)
    idxs should equal(List(4,3,2,1))
  }
  
  test("repeatWhile") {
    var x = 0
    rcs.repeatWhile(x < 10) {
      x += 1
    }
    x should equal(10)
  }

  test("repeatUntil") {
    var x = 0
    rcs.repeatUntil(x > 10) {
      x += 1
    }
    x should equal(11)
  }
}
