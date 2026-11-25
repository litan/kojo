/*
 * Copyright (C) 2026
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

/**
 * İkisindenBiri (Either) -- ya soldaki değer, ya sağdaki. İkisi birden asla.
 *
 * Belki'ye benzer ama boş tarafı da bir değer taşır: Belki'de "yok" demek
 * yalnızca Hiçbiri'dir, burada "neden olmadığını" da söyleyebilirsin.
 * Gelenek: SAĞ taraf işin yolunda gittiği taraftır -- işle, düzİşle, alYoksa
 * hep sağa bakar. (İngilizcede right hem "sağ" hem "doğru" demek, espri oradan.)
 *
 * Neden eklendi: bölİşle (partitionMap) İkisindenBiri istiyor; Türkçesi
 * olmadığı için o yöntem belgelenemiyordu.
 */
trait İkisindenBiriYöntemleri {
  type İkisindenBiri[A, B] = Either[A, B]

  object Sol {
    def apply[A, B](değer: A): İkisindenBiri[A, B] = Left(değer)
    def açımla[A, B](i: İkisindenBiri[A, B]): Option[A] = Left.unapply(i.asInstanceOf[Left[A, B]])
  }
  object Sağ {
    def apply[A, B](değer: B): İkisindenBiri[A, B] = Right(değer)
  }

  object İkisindenBiri {
    // koşul doğruysa Sağ, değilse Sol
    def koşulla[A, B](koşul: İkil, sağdaki: => B, soldaki: => A): İkisindenBiri[A, B] =
      Either.cond(koşul, sağdaki, soldaki)
  }

  implicit class İkisindenBiriYöntem[A, B](i: İkisindenBiri[A, B]) {
    type Belki[T] = Option[T]

    // --- hangi taraf ------------------------------------------------------
    def solMu: İkil = i.isLeft
    def sağMı: İkil = i.isRight

    // --- iki tarafı birden ele almak --------------------------------------
    // katla: hangi taraftaysa ona uygun işlevi çalıştırır, TEK sonuç verir.
    // (Either'ın tek fold'u budur; koleksiyonlardaki katla başka iş yapar.)
    def katla[C](solİşlev: A => C, sağİşlev: B => C): C = i.fold(solİşlev, sağİşlev)
    // birleştir: iki taraf aynı türdeyse, hangisiyse o değeri verir
    def birleştir[C >: A](implicit delil: B <:< C): C = i.fold(a => a, b => delil(b))
    // takasla: sol ile sağ yer değiştirir
    def takasla: İkisindenBiri[B, A] = i.swap

    // --- sağ tarafla çalışmak (gelenek: sağ = yolunda giden taraf) --------
    def işle[C](işlev: B => C): İkisindenBiri[A, C] = i.map(işlev)
    def düzİşle[A1 >: A, C](işlev: B => İkisindenBiri[A1, C]): İkisindenBiri[A1, C] = i.flatMap(işlev)
    def düzleştir[A1 >: A, C](implicit delil: B <:< İkisindenBiri[A1, C]): İkisindenBiri[A1, C] =
      i.flatten(delil)
    def alYoksa[B1 >: B](varsayılan: => B1): B1 = i.getOrElse(varsayılan)
    def boşsaÖbürü[A1 >: A, B1 >: B](öbürü: => İkisindenBiri[A1, B1]): İkisindenBiri[A1, B1] =
      i.orElse(öbürü)
    def içeriyorMu[B1 >: B](değer: B1): İkil = i.contains(değer)
    def varMı(deneme: B => İkil): İkil = i.exists(deneme)
    def hepsiDoğruMu(deneme: B => İkil): İkil = i.forall(deneme)
    def herbiriİçin[U](işlev: B => U): Birim = i.foreach(işlev)

    // --- çevirme ----------------------------------------------------------
    def belkiye: Belki[B] = i.toOption // sağdaysa Biri, soldaysa Hiçbiri
    def diziye: Dizi[B] = i.toSeq
  }
}
