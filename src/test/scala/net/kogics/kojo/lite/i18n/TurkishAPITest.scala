/*
 * Copyright (C) 2020-21
 *   Bulent Basaran <ben@scala.org> https://github.com/bulent2k2
 *   Lalit Pant <pant.lalit@gmail.com>
 *   Christoph Knabe  http://public.beuth-hochschule.de/~knabe/
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

package net.kogics.kojo.lite.i18n

import org.scalatest.{Matchers, FunSuite}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

// ../../picture/PictureTest.scala
import net.kogics.kojo.lite.NoOpKojoCtx
import net.kogics.kojo.lite.canvas.SpriteCanvas
import net.kogics.kojo.util.Utils.doublesEqual
import net.kogics.kojo.staging

/**
  * Tests for TurkishAPI.
  *
  * @author Bulent Basaran   https://github.com/bulent2k2

  * Thanks to the original author (for GermanAPI):
  * Christoph Knabe  http://public.beuth-hochschule.de/~knabe/
  */
@RunWith(classOf[JUnitRunner])
class TurkishAPITest extends FunSuite with Matchers {
  
  import TurkishAPI._

  test("yinele(n){block} should repeat the block n times") {
    val sb = new StringBuilder(10)
    yinele(5){
      sb append "+-"
    }
    sb.toString should be("+-+-+-+-+-")
  }
  
  test("yineleDoğruysa(condition){block} should execute block while condition holds"){
    val sb = new StringBuilder(10)
    var i = 10
    yineleDoğruysa(i > 5){
      sb.append(i).append(' ')
      i -= 1
    }
    sb.toString should be("10 9 8 7 6 ")
  }
  
  test("yineleOlanaKadar(condition){block} should execute block while condition does not hold"){
    val sb = new StringBuilder(10)
    var i = 1
    yineleOlanaKadar(i > 5){
      sb.append(i).append(' ')
      i += 1
    }
    sb.toString should be("1 2 3 4 5 ")
  }
  
  test("yineleDizinli(n){i => fn(i)} should call fn n times with indices 1 to n"){
    val sb = new StringBuilder(10)
    yineleDizinli(5){i =>
      sb.append(i).append(' ')
    }
    sb.toString should be("1 2 3 4 5 ")
  }

  test("yineleİlktenSona(start, end){i => fn(i)} should call fn with the Int values from start to end"){
    val sb = new StringBuilder(10)
    yineleİlktenSona(1, 5){i =>
      sb.append(i).append(' ')
    }
    sb.toString should be("1 2 3 4 5 ")
  }
  
  test("yineleKere[T](iterable){e => fn(e)} should process all elements of iterable"){
    val sb = new StringBuilder(10)
    yineleKere(1 to 10 by 2){i =>
      sb.append(i).append(' ')
    }
    sb.toString should be("1 3 5 7 9 ")
  }

  test("yineleİçin[T](iterable){e => fn(e)} should process all elements of iterable"){
    val sb = new StringBuilder(10)
    yineleİçin(1 to 10 by 3){i =>
      sb.append(i).append(' ')
    }
    sb.toString should be("1 4 7 10 ")
  }

  test("İkil (Boolean in Turkish) should work") {
    val test0: İkil = yanlış
    val test1: İkil = doğru

    test0 || false should be(yanlış)
    test1 && true  should be(doğru)
  }

  test("Translation of Option should work") {
    val o1: Belki[Sayı] = Biri(3)
    varMı(o1) should be(true)
    yokMu(o1) should be(false)
    varMı(Hiçbiri) should be(false)
    yokMu(Hiçbiri) should be(true)
  }
  
  test("Translations of math API should work -- abs == mutlakDeğer") {
    val abs0: Kesir = mutlakDeğer(0)
    val abs1: Kesir = mutlakDeğer(-72.001)
    val abs2: Kesir = mutlakDeğer(42)
    abs0 should be(0)
    abs1 should be(72.001)
    abs2 should be(42)
  }

  test("Translations of math API should work -- max == enİrisi") {
    val m1: Sayı = enİrisi(-3, 5)
    val m2: Kesir = enİrisi(-30.05, 50.03)
    m1 should be(5)
    m2 should be(50.03)
  }

  test("Translations of math API should work -- pi, e and sqrt2") {
    val pi: Kesir = piSayısı
    val e: Kesir = eSayısı
    val s2: Kesir = gücü(2, 0.5)
    pi shouldBe 3.1415 +- 0.0001
    e shouldBe 2.7182 +- 0.0001
    s2 shouldBe 1.4142 +- 0.0001
  }

  test("Translation of require should work") {
    val pass = try {
      gerekli(true && doğru, "Bu doğru")
      true
    } catch {
      case _: Throwable => false
    }
    pass should be(true)
    val pass2 = try {
      gerekli(false || yanlış, "Bu da yanlış")
      false
    } catch {
      case _: Throwable => true
    }
    pass should be(true)
  }

  test("Translation of util.Random.shuffle should work") {
    val deste = Dizi(1, 2, 3, 4, 5)
    var count = 0
    while (rastgeleKarıştır(deste) == Dizi(1, 2, 3, 4, 5)) count += 1
    count < 10 should be(true)
    val d2 = Dizin(1, 2, 3, 4, 5)
    while (rastgeleKarıştır(deste) == Dizi(1, 2, 3, 4, 5)) count += 1
    count < 20 should be(true)
  }
  /* 
  // See: ~/src/kojo/git/kojo/src/test/scala/net/kogics/kojo/turtle/TurtleTest2.scala
  // ~/src/kojo/git/kojo/src/test/scala/net/kogics/kojo/lite/TestEnv.scala
  import net.kogics.kojo.lite.TestEnv
  import scala.language.reflectiveCalls
  val kojoCtx = new NoOpKojoCtx
  val foo = TestEnv(kojoCtx)

  // ~/src/kojo/git/kojo/src/main/scala/net/kogics/kojo/lite/CodeExecutionSupport.scala
  import net.kogics.kojo.lite.AppMode
  val codeRunner = AppMode.currentMode.scalaCodeRunner(kojoCtx)
  import net.kogics.kojo.lite.{CoreBuiltins, Builtins}
  val builtins = new Builtins(
    foo.TSCanvas,
    foo.Tw,
    foo.Staging,
    foo.storyTeller,
    foo.mp3player,
    foo.fuguePlayer,
    kojoCtx,
    codeRunner
  )
  
  TurkishInit.init(builtins)

  test("Translation of Turtle commands should work") {
    val k1 = yeniKaplumbağa(30, 40)
    k1.konum.x should be (30)
    k1.konum.y should be (40)
    k1.ileri()
    k1.konum.x should be (30)
    k1.konum.y should be (140)
    yinele(4) {
      k1.ileri()
      k1.sağ()
    }
    k1.konum.x should be (30)
    k1.konum.y should be (140)
    k1.doğrultu should be (90)
  }

  test("Picture from a Turtle drawing in Turkish") {
    val r = Resim { k: Kaplumbağa0 =>
      import k._
      yinele(4) {
        ileri()
        sağ()
      }
    }
    r.çiz()
    r.alan should be (10000)
  }

  test("Translations of Vector2D should work") {
    val y1 = Yöney2B(3, 4)
    y1.boyu should be(5)
  }

  test("Translations of Picture should work") {
    val r = götür(30, 40) * kalemRengi(mavi) -> Resim.dikdörtgen(100, 200)
    r.çizili should be(yanlış)
    r.alan should be(20000.0)
    r.konum.x should be(30)
    r.konum.y should be(40)
  }

   */
}
