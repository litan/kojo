/*
 * Copyright (C) 2026 Bulent Basaran <bulent2k2@gmail.com>
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
package net.kogics.kojo.araclar

import org.junit.Test
import org.junit.Assert._

import net.kogics.kojo.lite.i18n.tr

/**
 * Dışa aktarılan yardım JSON'unun BİÇİMİNİ çiviler.
 *
 * Neden gerekli: `sozluk/yardim.json` bu üreticinin çıktısı ama kojojs-dev
 * deposunda DURUYOR. Orada onu doğrulayan bir şey yok -- kojojs-dev testleri
 * Scala.js, tarayıcıda koşuyor, yerel dosya okuyamıyor. Yani üretici burada
 * bir alanı düşürse, orada hiçbir test düşmez ve sayfa da hata vermez:
 * panel `if (y.türler !== undefined)` diyor, yani alan yoksa SUSAR. Özellik
 * izsiz kaybolur.
 *
 * Bu tam olarak yaşandı: Eylül 2026'da `türler` alanını ekleyen commit ile
 * onu kullanan sözlük ayrı sırayla birleşti; arada kalan pencerede yenileme
 * yönergesini izleyen biri 190 alanın hepsini sessizce silerdi (ölçüldü:
 * 380 satır fark, sıfır kırmızı test).
 *
 * Buradaki savlar çıktının ŞEKLİNE bakıyor, içeriğine değil -- içerik zaten
 * KoleksiyonYardımıTest'te çivili.
 */
class YardımDışaAktarTest {
  private lazy val satırlar = YardımDışaAktar.json.split("\n").toList

  // Üretici her girdiyi TEK satıra yazıyor (mkString("{\n", ",\n", "\n}\n")),
  // o yüzden satır tabanlı denetim güvenilir: her satır bu kalıba uyuyorsa
  // JSON yapı olarak da doğru kurulmuş demektir.
  private val girdiSatırı = """^  "(?:[^"\\]|\\.)*":\{"tür":"(yöntem|metin)",.*\},?$""".r

  private def girdiler = satırlar.drop(1).dropRight(1)

  @Test
  def herSatırGirdiKalıbınaUyuyor(): Unit = {
    assertEquals("ilk satır", "{", satırlar.head)
    assertEquals("son satır", "}", satırlar.last)
    val bozuk = girdiler.filterNot(girdiSatırı.pattern.matcher(_).matches)
    assertTrue(s"${bozuk.size} satır girdi kalıbına uymuyor:\n" + bozuk.take(3).mkString("\n"), bozuk.isEmpty)
  }

  @Test
  def yöntemSayısıTabloylaAynı(): Unit = {
    val yöntem = girdiler.count(_.contains(""""tür":"yöntem","""))
    assertEquals(
      "dışa aktarılan yöntem girdisi sayısı help.scala'daki tabloyla uyuşmuyor",
      tr.help.koleksiyonYöntemleri.size,
      yöntem
    )
    assertEquals("toplam girdi sayısı content ile uyuşmuyor", tr.help.content.size, girdiler.size)
  }

  @Test
  def herYöntemGirdisiTürlerAlanınıTaşıyor(): Unit = {
    // ALAN VARLIĞI sınanıyor, değeri değil: boş değer ("beşinin hepsinde var")
    // anlamlı bir cevap, eksik alan ise bilgi kaybı. Okuyan taraf (sözlük
    // paneli) ikisini ayırt ediyor, o yüzden ayrımın burada korunması gerek.
    val eksik = girdiler.filter(_.contains(""""tür":"yöntem",""")).filterNot(_.contains(""","türler":""""))
    assertTrue(
      s"${eksik.size} yöntem girdisinde türler alanı yok -- sözlük paneli bu bilgiyi\n" +
        "gösteriyor ve alan yoksa SESSİZCE susuyor:\n" + eksik.take(3).mkString("\n"),
      eksik.isEmpty
    )
  }

  @Test
  def metinGirdilerindeTürlerAlanıYok(): Unit = {
    // türler yalnız yöntemlere ait; metin girdilerinde bulunması üreticide
    // bir karışıklık olduğunu gösterir.
    val fazla = girdiler.filter(_.contains(""""tür":"metin",""")).filter(_.contains(""","türler":""""))
    assertTrue(s"${fazla.size} metin girdisinde türler alanı var:\n" + fazla.take(3).mkString("\n"), fazla.isEmpty)
  }
}
