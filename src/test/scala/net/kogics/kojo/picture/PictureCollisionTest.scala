/*
 * Copyright (C) 2009-2012 Lalit Pant <pant.lalit@gmail.com>
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
import util.Utils._
import net.kogics.kojo.lite.NoOpKojoCtx
import net.kogics.kojo.lite.canvas.SpriteCanvas

@RunWith(classOf[JUnitRunner])
class PictureCollisionTest extends FunSuite with xscala.RepeatCommands {
  
  val kojoCtx = new NoOpKojoCtx
  implicit val spriteCanvas = new SpriteCanvas(kojoCtx)

  val size = 50.0
  val delta = 1e-7
  val blue = java.awt.Color.blue

  def testBox0(n: Double) = Pic { t =>
    import t._
    repeat(4) {
      forward(n)
      right
    }
  }

  def testBox = testBox0(size)

  def testTriangle = Pic { t =>
    import t._
    right()
    repeat(3) {
      forward(size)
      left(120)
    }
  }
  
  def testHVPic = HPics(
    VPics(testBox0(25), testBox0(25)),
    VPics(testBox0(25), testBox0(25))
  )
  
  test("box-box non collision") {
    val p1 = trans(-size/2, 0) -> testBox
    val p2 = trans(size/2, 0) -> testBox
    p1.draw()
    p2.draw()
    p1.collidesWith(p2) should be(false)
  }

  test("box-box collision") {
    val p1 = trans(-size/2+delta, 0) -> testBox
    val p2 = trans(size/2-delta, 0) -> testBox
    p1.draw()
    p2.draw()
    p1.collidesWith(p2) should be(true)
  }

  test("box-tri non collision") {
    val p1 = testBox
    val p2 = trans(0, -math.sin(60.toRadians) * size) -> testTriangle
    p1.draw()
    p2.draw()
    p1.collidesWith(p2) should be(false)
  }

  test("box-tri collision") {
    val p1 = testBox
    val p2 = trans(0, -math.sin(60.toRadians) * size + 10 * delta) -> testTriangle
    p1.draw()
    p2.draw()
    p1.collidesWith(p2) should be(true)
  }
  
  test("box with many boxes collision") {
    val p1 = fill(blue) -> testBox
    val p2 = fill(blue) * trans(size, 0) -> testBox
    val p3 = fill(blue) * trans(2*size, 0) -> testBox
    val p4 = fill(blue) * trans(3*size/2, size/2) -> testBox

    p1.draw()
    p2.draw()
    p3.draw()
    p4.draw()

    val others = Set(p1,p2,p3)
    others.size should be(3)
    
    val cols = p4.collisions(others)
    cols.size should be(2)
    cols.contains(p1) should be(false)
    cols.contains(p2) should be(true)
    cols.contains(p3) should be(true)
  }

  test("box with many boxes collision, option version") {
    val p1 = fill(blue) -> testBox
    val p2 = fill(blue) * trans(size, 0) -> testBox
    val p3 = fill(blue) * trans(2*size, 0) -> testBox
    val p4 = fill(blue) * trans(3*size/2, size/2) -> testBox

    p1.draw()
    p2.draw()
    p3.draw()
    p4.draw()

    val others = List(p1,p2,p3)
    
    val col = p4.collision(others)
    col match {
      case Some(p) if p == p2 => assert(true)
      case _ => assert(false, "Should have found p2 as collision object")
    }
  }
  
  test("box with many boxes - non collision") {
    val p1 = fill(blue) -> testBox
    val p2 = fill(blue) * trans(size, 0) -> testBox
    val p3 = fill(blue) * trans(2*size, 0) -> testBox
    val p4 = fill(blue) * trans(3*size/2, size+delta) -> testBox

    p1.draw()
    p2.draw()
    p3.draw()
    p4.draw()

    val others = Set(p1,p2,p3)
    others.size should be(3)
    
    val cols = p4.collisions(others)
    cols.size should be(0)
    cols.contains(p1) should be(false)
    cols.contains(p2) should be(false)
    cols.contains(p3) should be(false)
  }

  test("box with many boxes - non collision, option version") {
    val p1 = fill(blue) -> testBox
    val p2 = fill(blue) * trans(size, 0) -> testBox
    val p3 = fill(blue) * trans(2*size, 0) -> testBox
    val p4 = fill(blue) * trans(3*size/2, size+delta) -> testBox

    p1.draw()
    p2.draw()
    p3.draw()
    p4.draw()

    val others = List(p1,p2,p3)
    
    val col = p4.collision(others)
    col match {
      case None => assert(true)
      case _ => assert(false, "Should have found no collision object")
    }
  }

  test("hvpics-hvpics non collision") {
    val p1 = trans(-size/2-1, 0) -> testHVPic
    val p2 = trans(size/2+1, 0) -> testHVPic
    p1.draw()
    p2.draw()
    p1.collidesWith(p2) should be(false)
  }

  test("hvpics-hvpics collision") {
    val p1 = trans(-size/2, 0) -> testHVPic
    val p2 = trans(size/2, 0) -> testHVPic
    p1.draw()
    p2.draw()
    p1.collidesWith(p2) should be(true)
  }
  
  test("no self collisions") {
    val p1 = fill(blue) -> testBox
    val p2 = fill(blue) * trans(size, 0) -> testBox
    val p3 = fill(blue) * trans(2*size, 0) -> testBox
    val p4 = fill(blue) * trans(3*size/2, size/2) -> testBox

    p1.draw()
    p2.draw()
    p3.draw()
    p4.draw()

    val others = Set(p1,p2,p3,p4)
    others.size should be(4)
    
    val cols = p4.collisions(others)
    cols.size should be(2)
    cols.contains(p4) should be(false)
  }
}
