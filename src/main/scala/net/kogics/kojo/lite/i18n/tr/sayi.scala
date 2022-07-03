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

// also see: aralik.scala
trait NumMethodsInTurkish {
  type Sayılar = Vector[Sayı]   // Used in Conway's game of life code in the tutorial
  object Sayılar {
    def apply(elemanlar: Sayı*): Sayılar = Vector.from(elemanlar)
    def unapplySeq(ss: Sayılar) = Vector.unapplySeq(ss)
  }

  implicit class SayıYöntemleri(a: Sayı) {
    def |-(b: Sayı): Range = a until b
    def |-|(b: Sayı): Range = a to b
    def yazıya = a.toString
    def kesire = a.toDouble
    def mutlakDeğer = a.abs
    def enİrisi(b: Sayı) = a max b
    def enUfağı(b: Sayı) = a min b
  }

  implicit class KesirYöntemleri(a: Kesir) {
    def yazıya = a.toString
    def sayıya = a.toInt
    def dereceye = a.toDegrees
    def radyana = a.toRadians
    def mutlakDeğer = a.abs
    def enİrisi(b: Kesir) = a max b
    def enUfağı(b: Kesir) = a min b
    def taban = a.floor
    def tavan = a.ceil
    def yakın = a.round
  }

  object Lokma {
    def Enİrisi = Byte.MaxValue
    def EnUfağı = Byte.MinValue
  }

  object Sayı {
    def Enİrisi = Int.MaxValue
    def EnUfağı = Int.MinValue
  }

  object Kısa {
    def Enİrisi = Short.MaxValue
    def EnUfağı = Short.MinValue
  }

  object Uzun {
    def Enİrisi = Long.MaxValue
    def EnUfağı = Long.MinValue
  }

  object Kesir {
    def Enİrisi = Double.MaxValue
    def EnUfağı = Double.MinValue
  }

  object UfakKesir {
    def Enİrisi = Float.MaxValue
    def EnUfağı = Float.MinValue
  }

}
