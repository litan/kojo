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

import java.io.File

import scala.reflect.internal.util.BatchSourceFile
import scala.tools.nsc.Global
import scala.tools.nsc.Settings
import scala.tools.nsc.reporters.StoreReporter

import org.junit.Assert._
import org.junit.Test

/**
 * Eskitilmiş tuş adlarının derleyici uyarısının GERÇEKTEN çıktığını ve
 * öğrenciye DOĞRU adı söylediğini doğrular.
 *
 * NEDEN VAR (kojojs-dev#76): tuş adları göçünde on snake_case yazım
 * @deprecated takma ada dönüştü. O annotation'lar "eskitmek silmek değil"
 * sözünü tutan tek şey. Silinirlerse hiçbir sınama kızarmıyordu; onu
 * kojojs-dev'deki `araclar/tus-yazim-denetle.py` gözcüsü kapattı -- ama o
 * gözcü annotation'ın VARLIĞINI tutuyor, METNİNİ tutmuyor:
 *
 *     @deprecated("", "")      <- gözcüden geçer, öğrenciye hiçbir şey söylemez
 *
 * Kaydın 1. önerisi buydu ve "doğru olan bu" diye işaretlenmişti: uyarının
 * kendisini beklemek. Burada yapılan o. Kural tabanlı gözcü metni okuyamaz
 * çünkü annotation bir DERLEME ZAMANI işareti: değeri de, adın varlığını da
 * değiştirmiyor, JVM'de varsayılan olarak CLASS saklamalı, yani çalışma
 * anında sınanamıyor. Derleyiciyi çağırmak gerekiyor.
 *
 * NASIL: klavye.scala'dan (yılan, deve) çiftleri okunuyor -- takma adın
 * hedefi zaten kendi gövdesinde yazılı (`val sil_geri = silGeri`), yani
 * beklenen adı türetmeye gerek yok, kaynaktan geliyor. Sonra her takma adı
 * kullanan küçük bir parçacık nsc.Global ile derlenip uyarılar toplanıyor.
 *
 * Sav: her yılan ad için, hem KENDİ adını hem de DEVE hedefini anan bir
 * eskitme uyarısı çıkmalı. Boş ya da yanlış hedefli bir ileti bu savı
 * geçemez.
 *
 * DİKKAT -- ÇİFTLER @deprecated'DEN TÜRETİLMEZ. İlk yazdığım hâl yalnız
 * annotation taşıyan satırları topluyordu ve mutasyonla ölçünce görüldü:
 * annotation TÜMDEN SİLİNİNCE o ad listeden de düşüyor, beklenti kalmıyor
 * ve sınama yeşil kalıyordu. Denetlenecek listeyi denetlenen şeyden
 * türetmek, bu deponun #69'da adını koyduğu döngüselliğin aynısı. Şimdi
 * liste `val yılan = deve` satırlarından, annotation'a BAKILMADAN
 * kuruluyor: annotation yoksa uyarı da çıkmaz, sav düşer.
 *
 * Yılan adların hepsinin bu biçimde (`= deveAd`) olduğu ayrıca sayılıyor;
 * başka biçimde bir yılan ad sessizce atlanmasın.
 */
class EskitmeUyarisiTest {

  private val klavyeYolu = "src/main/scala/net/kogics/kojo/lite/i18n/tr/klavye.scala"

  // Yılan yazımlı her tanım -- gövdesine bakmadan. Sayım için.
  private val YILAN = """(?:^|;)\s*val\s+[a-zçğıöşü][A-Za-zÇĞİÖŞÜçğıöşü0-9]*(?:_[A-Za-zÇĞİÖŞÜçğıöşü0-9]+)+\s*[=:]""".r
  // Takma ad satırı: `val sil_geri = silGeri`. Türkçe harfler ad karakteri.
  private val ÇİFT = """^\s*val\s+([a-zçğıöşü][A-Za-zÇĞİÖŞÜçğıöşü0-9]*(?:_[A-Za-zÇĞİÖŞÜçğıöşü0-9]+)+)\s*=\s*([A-Za-zÇĞİÖŞÜçğıöşü0-9]+)\s*$""".r

  /** `val yılan = deve` satırlarından (yılan, deve) -- annotation'a BAKMADAN. */
  private def çiftler(dosya: File): (Seq[(String, String)], Int) = {
    val satırlar = scala.io.Source.fromFile(dosya, "UTF-8").getLines().toVector
    val çift = satırlar.collect { case ÇİFT(yılan, deve) => (yılan, deve) }
    val yılanSayısı = satırlar.count(s => YILAN.findFirstIn(s).isDefined)
    (çift, yılanSayısı)
  }

  private def sonda(çiftler: Seq[(String, String)]): String = {
    val kullanımlar = çiftler.zipWithIndex
      .map { case ((yılan, _), i) => s"  val kullanım$i: Any = k.tuşlar.$yılan" }
      .mkString("\n")
    // Tanım yerinin İÇİNDEN yapılan gönderme uyarı almayabilir; o yüzden
    // trait'i genişletmiyoruz, DIŞARIDAN bir örnek üstünden kullanıyoruz.
    s"""object EskitmeSondası {
       |  private val k = new net.kogics.kojo.lite.i18n.tr.KeyCodesInTurkish {}
       |$kullanımlar
       |}
       |""".stripMargin
  }

  @Test
  def eskitilmişTuşAdlarıDoğruHedefiSöylüyor(): Unit = {
    assertTrue("depo kökünden koşmalı", new File(klavyeYolu).exists())
    val (çift, yılanSayısı) = çiftler(new File(klavyeYolu))
    assertTrue(
      s"klavye.scala'da yılan yazımlı takma ad bulunamadı -- dosyanın biçimi mi değişti? ($klavyeYolu)",
      çift.nonEmpty
    )
    assertEquals(
      "yılan yazımlı tanımların hepsi `val yılan = deveAd` biçiminde olmalı, yoksa bu sınama " +
        "bazılarını sessizce atlar",
      yılanSayısı,
      çift.length
    )

    val ayarlar = new Settings
    ayarlar.usejavacp.value = true // sbt (fork) altında Kojo'nun tam sınıf yolu
    ayarlar.encoding.value = "UTF-8"
    ayarlar.deprecation.value = true // ASIL AYAR: uyarılar tek tek gelsin
    ayarlar.nowarn.value = false
    // Eskitme uyarısını refchecks üretiyor; typer'da durursak hiç çıkmaz.
    ayarlar.stopAfter.value = List("refchecks")

    val raportör = new StoreReporter(ayarlar)
    val global = new Global(ayarlar, raportör)
    new global.Run().compileSources(List(new BatchSourceFile("EskitmeSondası.scala", sonda(çift))))

    val hatalar = raportör.infos.toSeq.filter(_.severity == raportör.ERROR).map(_.msg)
    assertTrue(s"sonda derlenmedi (sav boşa geçmesin): ${hatalar.mkString("; ")}", hatalar.isEmpty)

    val uyarılar = raportör.infos.toSeq.filter(_.severity == raportör.WARNING).map(_.msg)
    assertTrue(
      s"hiç uyarı çıkmadı -- -deprecation ya da evre adı mı değişti? (${çift.length} takma ad kullanıldı)",
      uyarılar.nonEmpty
    )

    val eksik = çift.filterNot { case (yılan, deve) =>
      uyarılar.exists(u => u.contains(yılan) && u.contains(deve))
    }
    assertTrue(
      "şu eskitilmiş tuş adlarının uyarısı ya çıkmıyor ya da doğru camelCase adı söylemiyor:\n" +
        eksik.map { case (y, d) => s"  $y -> beklenen ileti '$d' anmalı" }.mkString("\n") +
        "\nçıkan uyarılar:\n" + uyarılar.map("  " + _).mkString("\n"),
      eksik.isEmpty
    )
  }
}
