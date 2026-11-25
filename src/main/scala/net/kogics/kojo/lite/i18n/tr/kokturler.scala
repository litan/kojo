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

// her nesne, herneyse! java Object?
trait CoreTypeMethodsInTurkish {
  trait BaskınYazıyaYöntemiyle {
    def yazıya: Yazı
    override def toString = yazıya
  }

  trait Eşsizlik {
    def kıymaKodu: Sayı
    override def hashCode = kıymaKodu
    override def equals(h2: Her) = h2.kıymaKodu == kıymaKodu
    // iki nesnenin "kıyma kodu" farklıysa, nesneler de farklıdır.
    // Değilse, o zaman daha yavaş olan equals yöntemi kullanılır.
    // kıymaKodu/hashCode yöntemini yeniden tanımlamak gerekebiliyor.
    // Ne zaman? Aynı görünen durum sınıf nesnelerini birbirinden ayırmak gerekince.
    // O halde çözüm bu özelliği yaymak ve kıymaKodu yöntemini yeniden tanımlamak
    // github/bulen2k2/kojo-denemeler/a__tam_turkce/tangle-trk.kojo programında kullanıyoruz bunu.
  }

  implicit class NesneYöntemleri(h: Nesne) {
    def kıymaKodu = h.hashCode
    def eşitMi(h2: Her) = h.equals(h2)
    // warnings:
    // def nesnesiMi[T] = h.isInstanceOf[T]
    def nesnesiOlarak[T] = h.asInstanceOf[T]
    def yazıya = h.toString
  }

  implicit class HerNesneYöntemleri(h: Her) {
    def kıymaKodu = h.hashCode
    // warnings:
    // def eşitMi(h2: Her) = h.equals(h2)
    // def nesnesiMi[T] = h.isInstanceOf[T]
    def nesnesiOlarak[T] = h.asInstanceOf[T]
    // https://contributors.scala-lang.org/t/what-is-the-purpose-of-tostring-in-scala/4779
    // def yazıya = h.getClass().getName() + '@' + Integer.toHexString(h.hashCode())
    def yazıya = h.toString
  }

  implicit class HerGönderYöntemleri(h: HerGönder) {
    def aynıMı(h2: HerGönder): İkil = h eq h2
  }
}
