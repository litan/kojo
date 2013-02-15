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
import net.kogics.kojo.lite.NoOpKojoCtx
import net.kogics.kojo.lite.canvas.SpriteCanvas

@RunWith(classOf[JUnitRunner])
class PictureEqualsHashTest extends FunSuite with xscala.RepeatCommands {
  
  val kojoCtx = new NoOpKojoCtx
  implicit val spriteCanvas = new SpriteCanvas(kojoCtx)
  
  case class Box1(p: Painter) extends Pic(p) {
    override def copy: Box1 = new Box1(p)
  }

  case class Box2(p: Painter) extends Pic(p) {
    override def copy: Box2 = new Box2(p)
  }

  val p: Painter = { t =>
    import t._
    repeat (4) {
      forward(50)
      right()
    }
  }

  test("living in a set") {
    val b11 = Box1(p)
    b11.translate(-100, 0)
    val b12 = Box1(p)
    val b2 = Box2(p)
    b2.translate(100, 0)
    
    val others = Set(b11, b12, b2)
    others.size should be(3)
    b11 should not be(b12)
  }

  test("living in a map") {
    val b11 = Box1(p)
    b11.translate(-100, 0)
    val b12 = Box1(p)
    val b2 = Box2(p)
    b2.translate(100, 0)
    
    val picMap = Map(
      b11 -> "b11",
      b12 -> "b12",
      b2 -> "b2"
    )
    picMap.size should be(3)
    picMap(b11) should be("b11")
    picMap(b12) should be("b12")
    picMap(b2) should be("b2")
  }
}
