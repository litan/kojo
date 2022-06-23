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
case class Aralık(ilk: Sayı, son: Sayı, adım: Sayı = 1) {
  val r = Range(ilk, son, adım)
  val başı = r.head
  val sonu = r.last
  val uzunluğu = r.size
  def dizine: Dizin[Sayı] = r.toList
  def diziye: Dizi[Sayı] = r.toSeq
  def yazı() = toString()
  def herÖgeİçin(komutlar: (Sayı) => Birim) = r.foreach(komutlar)
  override def toString() = {
    val yazı = if (r.size <= 10) r.mkString("(", ", ", ")")
    else {
      val (başı, sonu) = (r.take(5), r.drop(r.size - 5))
      başı.mkString("(", ", ", " ...") + sonu.mkString(" ", ", ", ")")
    }
    s"Aralık$yazı"
  }
  def map[B](f: Sayı => B) = r.map(f)
  def withFilter(pred: Sayı => İkil) = r.withFilter(pred)
  def flatMap[B](f: Sayı => IterableOnce[B]) = r.flatMap(f)
  def foreach(f: (Sayı) => Unit) = r.foreach(f)

  def indirge(iş: (Sayı, Sayı) => Sayı): Sayı = diziye.reduce(iş)
  def soldanKatla[B](z: B)(iş: (B, Sayı) => B): B = diziye.foldLeft(z)(iş)
  def sağdanKatla[B](z: B)(iş: (Sayı, B) => B): B = diziye.foldRight(z)(iş)
}

object Aralık {
  def apply(ilk: Sayı, son: Sayı, adım: Sayı = 1) = new Aralık(ilk, son, adım)
  def kapalı(ilk: Sayı, son: Sayı, adım: Sayı = 1) = new Aralık(
    ilk,
    if (adım > 0) son+1 else son-1,
    adım)
  // copied from class Builtins ../../Builtins.scala
  def kesirden(ilk: Kesir, son: Kesir, adım: Kesir) = Range.BigDecimal(ilk, son, adım)
  def kesirdenAçık(ilk: Kesir, son: Kesir, adım: Kesir) = Range.BigDecimal(ilk, son, adım)
  def kesirdenKapalı(ilk: Kesir, son: Kesir, adım: Kesir) = Range.BigDecimal.inclusive(ilk, son, adım)
}

// also see: trait IntMethodsInTurkish in sayi.scala
trait RangeMethodsInTurkish {
  implicit class RangeYöntemleri(r: Range) {
    def adım(c: Sayı): Range = r by c
    def diziye = r.toSeq
    def dizine = r.toList
    def boyu = r.length
    def içindeMi(s: Sayı) = r.contains(s)
    // todo: duplicate above

    def eşle[B](f: Sayı => B) = r.map(f)
    def elekle(dene: Sayı => İkil) = r.withFilter(dene)
    def düzEşle[B](f: Sayı => IterableOnce[B]) = r.flatMap(f)
    def herbiriİçin(f: (Sayı) => Unit) = r.foreach(f)
    def indirge(iş: (Sayı, Sayı) => Sayı): Sayı = diziye.reduce(iş)
    def soldanKatla[B](z: B)(iş: (B, Sayı) => B): B = diziye.foldLeft(z)(iş)
    def sağdanKatla[B](z: B)(iş: (Sayı, B) => B): B = diziye.foldRight(z)(iş)
  }
}
