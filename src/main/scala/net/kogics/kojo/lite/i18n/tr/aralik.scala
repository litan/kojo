/*
 * Copyright (C) 2021
 *   Bulent Basaran <ben@scala.org> https://github.com/bulent2k2
 *   Lalit Pant <pant.lalit@gmail.com>
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
package net.kogics.kojo.lite.i18n.tr

// translating Range
//
// Aralık ARTIK BİR TÜR TAKMA ADI (Küme = Set, Dizin = List kalıbı).
// Eskiden Range'i saran bir case class'tı; o zaman `Aralık(1, 10)` yazan
// öğrenci ~20 yöntem görüyordu, `1 |-| 10` yazan ise Range olduğu için
// SıralıDizi sarmalayıcısının ~110 yöntemini. İki yüz birleşti: ikisi de
// artık aynı şey ve aynı yöntemleri görüyor.
//
// Case class'ın özel gösterimi (`Aralık(1, 4, 7)`) kaybolmadı: yazı()/yazıya()
// olarak aşağıdaki örtük sınıfa taşındı. Örtük gösterim (REPL çıktısı) için
// translate.scala'da Range -> Aralık çevirisi var.
object Aralık {
  // Dönüş türleri takma adla yazılıyor: `type Aralık = Range` olduğu için
  // davranış aynı, ama kod tamamlamada ve tür ipucunda öğrenci `Range` değil
  // `Aralık` görüyor. (translate.scala'daki metin çevirisi tür imzalarını
  // yakalayamıyor: orada ad satırın SONUNDA geliyor.)
  def apply(ilki: Sayı, sonuncu: Sayı, adım: Sayı = 1): Aralık = Range(ilki, sonuncu, adım)
  def kapalı(ilki: Sayı, sonuncu: Sayı, adım: Sayı = 1): Aralık = Range.inclusive(ilki, sonuncu, adım)
  // copied from class Builtins ../../Builtins.scala
  def kesirden(ilki: Kesir, sonuncu: Kesir, adım: Kesir) = Range.BigDecimal(ilki, sonuncu, adım)
  def kesirdenAçık(ilki: Kesir, sonuncu: Kesir, adım: Kesir) = Range.BigDecimal(ilki, sonuncu, adım)
  def kesirdenKapalı(ilki: Kesir, sonuncu: Kesir, adım: Kesir) = Range.BigDecimal.inclusive(ilki, sonuncu, adım)

  /**
   * Öğrenci dostu gösterim: `Aralık(1, 4, 7)`, uzun aralıklarda kısaltılmış.
   *
   * `Aralık` bir tür takma adı olduğu için `toString` ezilemiyor; bu yüzden
   * gösterim İKİ yerden geliyor ve ikisi de buraya bakıyor: `yazıya`/`yazı()`
   * yöntemi (aşağıda) ve çıktı panelinin metin çevirisi
   * (translate.scala'daki `regexpChanges`). Tek gövde, tek biçim.
   */
  def gösterim(r: Range): Yazı = {
    val gövde =
      if (r.size <= 10) r.mkString("(", ", ", ")")
      else {
        val (b, s2) = (r.take(5), r.drop(r.size - 5))
        b.mkString("(", ", ", " ...") + s2.mkString(" ", ", ", ")")
      }
    s"Aralık$gövde"
  }
}

// also see: trait IntMethodsInTurkish in sayi.scala
trait RangeMethodsInTurkish {
  implicit class RangeYöntemleri(r: Range) {
    // Aralık case class'ından taşınanlar (eski adlar korunuyor)
    def ilki: Sayı = r.start
    def sonuncu: Sayı = r.end
    def adımı: Sayı = r.step
    def adım: Sayı = r.step
    def uzunluğu: Sayı = r.size
    def başı: Sayı = r.head
    def sonu: Sayı = r.last
    def herÖgeİçin(komutlar: Sayı => Birim): Birim = r.foreach(komutlar)
    // Öğrenci dostu gösterim: uzun aralıkları kısaltır. toString ezilemez
    // (Aralık artık bir tür takma adı), ama bu yöntem eski çıktıyı verir.
    def yazı(): Yazı = yazıya
    def yazıya: Yazı = Aralık.gösterim(r)
    def adım(c: Sayı): Aralık = r by c
    def diziye = r.toSeq
    def dizine = r.toList
    def boyu = r.length
    def içindeMi(s: Sayı) = r.contains(s)

    def işle[B](f: Sayı => B) = r.map(f)
    def elekle(deneme: Sayı => İkil) = r.withFilter(deneme)
    def düzİşle[B](f: Sayı => YinelenebilirBirKere[B]) = r.flatMap(f)
    def herbiriİçin(f: (Sayı) => Unit) = r.foreach(f)
    def indirge(iş: (Sayı, Sayı) => Sayı): Sayı = diziye.reduce(iş)
    def soldanKatla[B](z: B)(iş: (B, Sayı) => B): B = diziye.foldLeft(z)(iş)
    def sağdanKatla[B](z: B)(iş: (Sayı, B) => B): B = diziye.foldRight(z)(iş)
  }
}
