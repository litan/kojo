/*
 * Copyright (C) 2012 Lalit Pant <pant.lalit@gmail.com>
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
package picture

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.junit.ShouldMatchersForJUnit._
import util.Utils
import xscala.RepeatCommands
import java.util.concurrent.CountDownLatch
import net.kogics.kojo.core.Picture
import java.util.concurrent.TimeUnit

@RunWith(classOf[JUnitRunner])
class CartooningTest extends KojoTestBase with FunSuite with RepeatCommands {

  def p = Pic { t =>
    repeat(4) {
      t.forward(50)
      t.right()
    }
  }

//  class PicA(p: Picture) extends ItemAnimator[Picture] {
//    val workItem = p
//  }
//
//  val testItem = new PicA(p)
//
//  test("scheduling") {
//    @volatile var inSwingT = false
//    val latch = new CountDownLatch(1)
//    testItem.schedule { p =>
//      inSwingT = Utils.inSwingThread
//      latch.countDown()
//    }
//    latch.await(1, TimeUnit.SECONDS)
//    inSwingT should be(true)
//  }
//
//  test("repeated scheduling") {
//    @volatile var inSwingT = false
//    @volatile var count = 0
//    val latch = new CountDownLatch(1)
//    testItem.scheduleRepeat(3) { p =>
//      inSwingT = Utils.inSwingThread
//      count += 1
//    }
//    testItem.schedule { p =>
//      latch.countDown()
//    }
//    latch.await(5, TimeUnit.SECONDS)
//    inSwingT should be(true)
//    count should be(3)
//  }
}