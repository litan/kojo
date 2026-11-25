/*
 * Copyright (C) 2026
 *   Bulent Basaran <ben@scala.org> https://github.com/bulent2k2
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

import scala.tools.nsc.Settings

import org.junit.Test
import org.junit.Assert._
import org.junit.Assume.assumeTrue

import net.kogics.kojo.lite.NoOpRunContext
import net.kogics.kojo.xscala.CompilerAndRunner
import net.kogics.kojo.xscala.CompilerListener

/**
 * Yardım metinlerindeki (tr/help.scala'nın content haritası) örneklerin hâlâ
 * DERLENDİĞİNİ doğrular.
 *
 * Koleksiyon örnekleri gibi çalıştırılıp sonucuyla karşılaştırılamıyorlar:
 * çoğu bir şey çizer, tuval ister. Ama derlenmeleri bile yeterince değerli --
 * bir komut adı değişince ya da girdi sayısı artınca yardım metni sessizce
 * bayatlamasın.
 */
object YardımÖrnekleri {
  // Gerçek kod olmayan girdiler. Bunlar kasten dışarıda; ad değişirse test
  // "bilinmeyen muafiyet" diye uyarır, yani liste de bayatlamıyor.
  val muaflar: Map[String, String] = Map(
    "a_kalıp" -> "yardım metni şablonu: komut/yöntem yer tutucu, gerçek kod değil"
  )

  // Koleksiyon girdileri KoleksiyonYardımıTest'te zaten koşuluyor (hem de
  // sonuçlarıyla birlikte); burada elle yazılmış kaplumbağa/dil örnekleri var.
  private def koleksiyonAdları: Set[String] = tr.help.koleksiyonYöntemleri.map(_._1).toSet

  // content HTML tutuyor: <div>...</div>.toString kaçış yapıyor, yani metinde
  // _ < 3 yerine _ &lt; 3 duruyor. Derlemeden önce geri çevirmek gerek.
  // Bazı örneklerde <pre> içinde <strong>için</strong> gibi vurgular var --
  // yardım penceresinde anahtar sözcüğü kalınlaştırmak için. Derlerken atılmalı.
  private def etiketleriAt(s: String): String = s.replaceAll("<[^>]+>", "")

  private def kaçışıAç(s: String): String =
    s.replace("&lt;", "<").replace("&gt;", ">").replace("&quot;", "\"")
      .replace("&#39;", "'").replace("&apos;", "'").replace("&amp;", "&")

  def örnekler: List[(String, String)] =
    tr.help.content.toList.sortBy(_._1).filterNot { case (ad, _) => koleksiyonAdları.contains(ad) }.flatMap {
      case (ad, metin) =>
        "(?s)<pre>(.*?)</pre>".r
          .findAllMatchIn(metin)
          .map(m => kaçışıAç(etiketleriAt(m.group(1))).trim)
          .filter(_.nonEmpty)
          .zipWithIndex
          .map { case (kod, i) => (if (i == 0) ad else s"$ad #${i + 1}", kod) }
          .toList
    }
}

class YardımÖrnekleriTest {

  // Tek derleyici, hepsi için. Her örnek için yenisini kurmak 22 kat pahalıydı.
  private val hatalar = scala.collection.mutable.ListBuffer.empty[String]

  private lazy val runner = {
    val listener = new CompilerListener {
      def error(msg: String, line: Int, column: Int, offset: Int, lineContent: String): Unit =
        hatalar += s"satır $line: $msg${if (lineContent.trim.isEmpty) "" else s" -- ${lineContent.trim}"}"
      def warning(msg: String, line: Int, column: Int): Unit = {}
      def info(msg: String, line: Int, column: Int): Unit = {}
      def message(msg: String): Unit = {}
    }
    new CompilerAndRunner(
      { () => val s = new Settings(); s.usejavacp.value = true; s },
      // Kojo'nun betiklere gerçekte eklediğinin aynısı:
      // src/main/resources/i18n/initk/tr.tw.kojo
      // kaplumbağa olmadan ileri/sağ/daire gibi komutlar görünmüyor.
      Some(
        """val TurkishAPI = net.kogics.kojo.lite.i18n.TurkishAPI
          |import TurkishAPI.{bi => _, _ }
          |import kaplumbağa.{sil => _, _}""".stripMargin
      ),
      listener,
      new NoOpRunContext
    )
  }

  private def derle(kod: String): List[String] = {
    hatalar.clear()
    runner.compileForRunning(kod) // derler, ÇALIŞTIRMAZ
    hatalar.toList
  }

  // These help examples are written with Turkish KEYWORDS (dez, tanım, eğer ...),
  // which only the keyword-patched Scala compiler accepts. Kojo upstream builds
  // on the stock compiler, where they cannot compile by construction - so probe
  // once and skip instead of reporting a failure that is not a defect.
  private def türkçeAnahtarSözcüklerDerleniyor: Boolean = derle("dez x = 1").isEmpty

  @Test
  def örneklerDerleniyor(): Unit = {
    assumeTrue(
      "stock Scala compiler: Turkish keywords are not available, skipping",
      türkçeAnahtarSözcüklerDerleniyor
    )
    val hepsi = YardımÖrnekleri.örnekler
    assertTrue("yardım metinlerinde hiç örnek bulunamadı", hepsi.size >= 10)

    val bozuk = hepsi.collect {
      case (ad, kod) if !YardımÖrnekleri.muaflar.contains(ad.takeWhile(_ != ' ')) =>
        (ad, kod, derle(kod))
    }.filter(_._3.nonEmpty)

    assertTrue(
      s"${bozuk.size}/${hepsi.size} yardım örneği derlenmiyor:\n" +
        bozuk.map { case (ad, kod, h) => s"--- $ad\n${kod.linesIterator.map("    " + _).mkString("\n")}\n  ${h.mkString("\n  ")}" }
          .mkString("\n"),
      bozuk.isEmpty
    )
  }

  @Test
  def muafiyetListesiBayatlamamış(): Unit = {
    val adlar = YardımÖrnekleri.örnekler.map(_._1.takeWhile(_ != ' ')).toSet
    val kalmayanlar = YardımÖrnekleri.muaflar.keySet -- adlar
    assertTrue(s"muaf listesinde artık var olmayan girdiler: $kalmayanlar", kalmayanlar.isEmpty)
  }
}
