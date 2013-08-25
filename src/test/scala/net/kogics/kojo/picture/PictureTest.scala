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
import java.util.concurrent.CountDownLatch
import net.kogics.kojo.lite.NoOpKojoCtx
import net.kogics.kojo.lite.canvas.SpriteCanvas

@RunWith(classOf[JUnitRunner])
class PictureTest extends FunSuite with xscala.RepeatCommands {

  val kojoCtx = new NoOpKojoCtx
  implicit val spriteCanvas = new SpriteCanvas(kojoCtx)
  val Staging = new staging.API(spriteCanvas) // required for the animation test

  val size = 50
  val pt = 2.0
  val w = size + pt
  val h = size + pt
  val bx = -pt / 2
  val by = -pt / 2
  val w2 = w * 2
  val bx2 = size * 2

  def testPic = Pic { t =>
    import t._
    invisible()
    setAnimationDelay(0)
    repeat(4) {
      forward(size)
      right
    }
  }

  def testHpic3(a1: Double, a2: Double) = HPics(
    testPic,
    testPic,
    rot(a1) -> HPics(
      testPic,
      testPic,
      rot(a2) -> HPics(testPic, testPic)
    ),
    testPic
  )

  def testLine = Pic { t =>
    import t._
    right()
    forward(100)
  }

  test("picture bounds") {
    val p = testPic
    p.draw()
    val b = p.bounds
    b.x should be(bx plusOrMinus 0.01)
    b.y should be(by plusOrMinus 0.01)
    b.width should be(w plusOrMinus 0.01)
    b.height should be(h plusOrMinus 0.01)
  }

  test("picture translation") {
    val p = trans(50, 0) -> testPic
    p.draw()
    val b = p.bounds
    b.x should be(50 + bx plusOrMinus 0.01)
  }

  test("picture scaling") {
    val p = scale(2, 2) -> testPic
    p.draw()
    val b = p.bounds
    b.x should be(bx * 2 plusOrMinus 0.01)
    b.width should be(w * 2 plusOrMinus 0.01)
    b.height should be(h * 2 plusOrMinus 0.01)
  }

  test("picture scaling after translation") {
    val p = trans(50, 0) * scale(2, 2) -> testPic
    p.draw()
    val b = p.bounds
    b.x should be(50 + 2 * bx plusOrMinus 0.01)
    b.width should be(w * 2 plusOrMinus 0.01)
    b.height should be(h * 2 plusOrMinus 0.01)
  }

  test("picture translation after scaling") {
    val p = scale(2, 2) * trans(50, 0) -> testPic
    p.draw()
    val b = p.bounds
    b.x should be(50 * 2 + 2 * bx plusOrMinus 0.01)
    b.width should be(w * 2 plusOrMinus 0.01)
    b.height should be(h * 2 plusOrMinus 0.01)
  }

  test("3-hpics hp3 bounds") {
    val a1 = 30.0
    val a2 = 40.0
    val p = testHpic3(a1, a2)
    p.draw()

    val hp2 = p.pics(2).asInstanceOf[Rot].tpic.asInstanceOf[HPics]
    val hp3 = hp2.pics(2).asInstanceOf[Rot].tpic.asInstanceOf[HPics]

    val b3 = hp3.bounds
    doublesEqual(b3.x, bx2 - math.cos((90 - a2).toRadians) * h, 3.0) should be(true)
    doublesEqual(b3.width, math.cos((90 - a2).toRadians) * h + math.cos(a2.toRadians) * w2, 1.0) should be(true)
    doublesEqual(b3.height, math.cos(a2.toRadians) * h + math.sin(a2.toRadians) * w2, 1.0) should be(true)
  }

  test("3-hpics hp2 bounds") {
    val a1 = 30.0
    val a2 = 40.0
    val p = testHpic3(a1, a2)
    p.draw()

    val hp2 = p.pics(2).asInstanceOf[Rot].tpic.asInstanceOf[HPics]
    val hp3 = hp2.pics(2).asInstanceOf[Rot].tpic.asInstanceOf[HPics]

    val b3 = hp3.bounds
    val b2 = hp2.bounds
    doublesEqual(b2.x, bx2 - b3.height * math.cos((90 - a1).toRadians), 3.0) should be(true)
    doublesEqual(b2.width, (b3.width - math.cos((90 - a2).toRadians) * h + w2) * math.cos(a1.toRadians) + b3.height * math.cos((90 - a1).toRadians), 1.0) should be(true)
    doublesEqual(b2.height, b3.height * math.sin((90 - a1).toRadians) + (b3.width - math.cos((90 - a2).toRadians) * h + w2) * math.cos((90 - a1).toRadians), 1.0) should be(true)
  }

  test("heading 1") {
    val pic = testLine
    pic.rotate(390)
    pic.heading should be(30.0 plusOrMinus 0.001)
  }

  test("heading 2") {
    val pic = testLine
    pic.setHeading(2 * 360 + 20)
    pic.heading should be(20.0 plusOrMinus 0.001)
  }

  test("position 1") {
    val pic = testLine
    pic.translate(100, 50)
    pic.position.x should be(100)
    pic.position.y should be(50)
  }

  test("position + heading") {
    val pic = testLine
    pic.rotate(30)
    pic.translate(100, 0)
    pic.position.x should be(100 * math.cos(30.toRadians) plusOrMinus 0.001)
    pic.position.y should be(100 * math.sin(30.toRadians) plusOrMinus 0.001)

    pic.setPosition(150, 50)
    pic.position.x should be(150)
    pic.position.y should be(50)
    pic.heading should be(30.0 plusOrMinus 0.001)
  }

  test("react provides correct me for transforms") {
    val pic = rot(30) -> testPic
    @volatile var pic2: core.Picture = null
    pic.draw()
    val latch = new CountDownLatch(1)
    pic.react { me =>
      pic2 = me
      pic.canvas.stopAnimation()
      latch.countDown()
    }
    latch.await()
    pic2 should be(pic)
  }
}
