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

  test("Translations of math API should work -- misc") {
    val x = 1.234
    yuvarla(x) should be(1)
    yuvarla(x, 1) should be(1.2)
    val y = 1.5005
    yuvarla(y) should be(2)
    yuvarla(y,3) should be(1.501)
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

  test("Translation of Range should work") {
    val a = new Aralık(1, 10, 3)
    a.ilk shouldBe 1
    a.son shouldBe 10
    a.adım shouldBe 3
    a.dizin() shouldBe List(1, 4, 7)
    a.yazı() shouldBe "Aralık(1, 4, 7)"
    a.map(_ * 2) shouldBe Vector(2, 8, 14)
    a.flatMap(s => List(s, s*s)) shouldBe Vector(1, 1, 4, 16, 7, 49)

    val a2 = new Aralık(1, 200, 7)
    a2.dizin().size shouldBe 29
    a2.başı shouldBe 1
    a2.sonu shouldBe 197
    a2.uzunluğu shouldBe 29

    val a3 = Aralık.kapalı(1, 10, 3)
    a3.dizin() shouldBe List(1, 4, 7, 10)
    (for (i <- a3 if i % 2 != 0) yield i) shouldBe Vector(1, 7)

    val a4 = Aralık.kapalı(5, 1, -1)
    a4.dizin() shouldBe List(5, 4, 3, 2, 1)
    val a5 = Aralık(5, 1, -1)
    a5.dizin() shouldBe List(5, 4, 3, 2)
  }

  test("Translations of mutable.Stack should work") {
    val y1 = Yığın.boş[Sayı]
    y1.tane should be(0)
    y1.koy(1)
    y1.tane should be(1)
    val y2 = Yığın(1,2,3)
    y2.tane should be(3)
    val y3 = Yığın.doldur(y2)
    y3.tane should be(3)
    // todo more!
  }

  test("Translations of mutable.Map should work") {
    val e1 = Eşlem.boş[Yazı, Sayı]
    e1 eşli ("anahtar") should be(yanlış)
    e1 eşle ("anahtar" -> 99)
    e1 eşli ("anahtar") should be(doğru)
    e1("anahtar") should be(99)
    e1 eşle ("b" -> 88)
    e1 eşli ("b") should be(doğru)
    e1("b") should be(88)
    val l = e1.m.toSeq
    l.size should be(2)
    l.head match {
      case ("b", 88) => l.tail.head should be("anahtar", 99)
      case _ => l.tail.head should be("b", 88)
    }
    val e2 = Eşlem(
      "mavi" -> 1,
      "yeşil" -> 2,
      "sarı" -> 3
    )
    e2.sayı should be(3)
    e2("mavi") should be(1)
    var e3 = Eşlem( 1 -> 1, 2 -> 4, 4 -> 16, 16 -> 256 )
    e3 += (10 -> 100)
    e3(10) should be(100)
  }

  test("Translation of Array should work") {
    val s0 = Dizim.boş[Harf](10)
    s0.boyut should be(1)
    val s1 = Dizim.boş[Sayı](3, 3)
    s1.boyut should be(2)
    s1(0) should be(Array(0, 0, 0))
    s1(0)(0) should be(0)
    s1(0)(1) = 1
    s1(0) should be(Array(0, 1, 0))
    val s2 = Dizim.doldur[Sayı](2, 2)(5)
    s2.boyut should be(2)
    s2(0) should be(Array(5, 5))
    s2(0)(0) should be(5)
  }

  test("Translation of mutable.ArrayBuffer should work") {
    import net.kogics.kojo.core.Point
    val noktalar = EsnekDizim(Point(-100, -50), Point(100, -50), Point(-100, 50))
    noktalar.sayı should be(3)
    noktalar.ekle(Point(100, 100))
    noktalar.sayı should be(4)
    def deneme(nler: Seq[Point]) = nler.toList.tail
    deneme(noktalar.dizi).size should be(3)
  }

  test("Translations of Vector should work") {
    val y1 = Yöney(3, 4)
    y1(0) should be(3)
    y1.size should be(2)
    val y1b = y1 :+ 5
    y1b(2) should be(5)
    val y2 = Yöney.boş[Yazı]
    y2.size should be(0)
    val y2b = y2 :+ "Merhaba"
    (y2b :+ "Dünya!").size should be(2)
    y2b(0)(2) should be('r')
  }

  test("Translation for Set should work") {
    var k1 = Küme.boş[Sayı]
    k1.size should be(0)
    k1 += 3
    k1.size should be(1)
    k1(3) should be(true)
    k1(5) should be(false)
    k1 += 5
    k1(5) should be(true)
    k1.foreach { e =>
      (e<=5) should be(true)
      (e>=3) should be(true)
    }
    k1 -= 3
    k1(3) should be(false)
    k1(5) should be(true)
    var k2 = Küme(51, 18, 14, 10, 6)
    k2.size should be(5)
    for (s <- List(6, 10, 14, 18, 51)) { k2(s) should be(true) }
    k2(2) should be(false)
    k2 += 2
    k2(2) should be(true)
  }

  test("Translations of Character should work") {
    for (c <- '0' to '9') Harf.sayıMı(c) should be(true)
    Harf.sayıMı(' ') should be(false)
    Harf.sayıMı('a') should be(false)
    Harf.harfMi('a') should be(true)

    /*
     Harf.enUfağı should be('\u0000')
     Harf.enİrisi should be('\uffff')
     */
  }
  test("Translations needed for mandelbrot sample should work") {
    case class Dörtgen(x1: Kesir, x2: Kesir, y1: Kesir, y2: Kesir) {
      def alanı() = (x2 - x1) * (y2 - y1)
      def ortaNoktası = (x, y)
      val (x, y) = ((x2 + x1) / 2, (y2 + y1) / 2)
      def yazı = {
        val a = alanı()
        if (a > 0.0001) s"${yuvarla(a, 5)}" else f"${a}%2.3e"
      }
      def dörtlü = (x1, x2, y1, y2)
      def büyüt(oran: Kesir): Dörtgen = {
        if (oran <= 0 || oran >= 10.0) this else {
          val o2 = 0.5 * oran
          val en2 = o2 * (x2 - x1)
          val boy2 = o2 * (y2 - y1)
          Dörtgen(x - en2, x + en2, y - boy2, y + boy2)
        }
      }
    }
    class Pencere {
      def koy(d: Dörtgen) = bakışlar.koy(d)
      def al(): Dörtgen = bakışlar.al()
      def boşMu() = bakışlar.tane == 0
      def boşalt() = while (!boşMu()) al()
      private val bakışlar = Yığın.boş[Dörtgen]
    }
    val p1 = new Pencere
    p1.boşMu() should be(doğru)
    val d1 = Dörtgen(1, 2, 3, 4)
    val d2 = Dörtgen(0, 1, 2, 3)
    p1.koy(d1)
    p1.boşMu() should be(yanlış)
    p1.koy(d2)
    p1.al() should be (d2)
    p1.al() should be (d1)
    p1.boşMu() should be(doğru)
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
