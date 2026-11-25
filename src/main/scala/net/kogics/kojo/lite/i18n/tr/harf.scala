/*
 * Copyright (C) 2022
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

trait CharMethodsInTurkish {
  private type Dizik[T]=Array[T]

  type Harf = Char
  type JHarf = java.lang.Character
  type HarfDizisindenDizik=ArrayCharSequence
  type HarfDizisindenDizi=SeqCharSequence
  type HarfDiziğiDizisi = collection.mutable.ArraySeq.ofChar

  def HarftenHarfe(h: JHarf): Harf = Character2char(h)
  def harftenHarfe(h: Harf): JHarf = char2Character(h)

  def HarfDizisindenDizik(harfDiziği: Dizik[Harf]): HarfDizisindenDizik = ArrayCharSequence(harfDiziği)
  def HarfDizisindenDizi(harfDizisi: SıralıDizi[Harf]): HarfDizisindenDizi = SeqCharSequence(harfDizisi)

  object Harf {
    def sayıMı(h: Harf): İkil = Character.isDigit(h)
    def harfMi(h: Harf): İkil = Character.isLetter(h)
    // todo: more..

    def yazıya = "nesne scala.Harf" // toString returns "object scala.Char"
    def kutuyaKoy(h: Harf) = Char.box(h)
    def kutudanÇıkar(h: HerGönder) = Char.unbox(h)

    def sayıya(h: Harf) = Char.char2int(h)
    def uzuna(h: Harf) = Char.char2long(h)
    def kesire(h: Harf) = Char.char2double(h)
    def ufakkesire(h: Harf) = Char.char2float(h)

    val enUfağı = Char.MinValue // '\u0000'; ters yazılmıştı (MaxValue), TurkishAPITest'teki sav bu yüzden yorumdaydı
    val enİrisi = Char.MaxValue // '\uffff'
  }

  implicit class HarfYöntemleri(h: Harf) {
    def yazıya: Yazı = h.toString
    def büyükHarfe: Harf = h.toUpper
    def küçükHarfe: Harf = h.toLower
    def sayıya: Sayı = h.toInt
    def kesire: Kesir = h.toDouble
    def sayıMı: İkil = h.isDigit
    def boşlukMu: İkil = h.isWhitespace
    def küçükHarfMi: İkil = h.isLower
    def büyükHarfMi: İkil = h.isUpper
  }
}
