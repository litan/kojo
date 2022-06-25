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

trait StringMethodsInTurkish {
  type Yazı = String
  type EsnekYazı=collection.mutable.StringBuilder

  object Yazı {
    type Harf = Char
    def olarak(n: Nesne) = String.valueOf(n)
    def olarak(n: Kesir) = String.valueOf(n)
    def olarak(n: Sayı) = String.valueOf(n)
    def olarak(n: İkil) = if (n) "doğru" else "yanlış"
    def olarak(n: Harf) = String.valueOf(n)
    def olarak(n: UfakKesir) = String.valueOf(n)
    def olarak(n: Uzun) = String.valueOf(n)
    def olarak(n: Array[Char]) = String.valueOf(n)
    def olarak(n: Array[Char], nereden: Sayı, kaçTane: Sayı) = String.valueOf(n, nereden, kaçTane)
  }

  implicit class YazıYöntemleri(y: Yazı) {
    type Harf = Char
    // todo: duplicated from dizi.scala with minor changes
    def başı: Harf = y.head
    def kuyruğu: Yazı = y.tail
    def önü: Yazı = y.init
    def sonu: Harf = y.last
    def boyu: Sayı = y.length
    def boşMu: İkil = y.isEmpty
    def doluMu: İkil = y.nonEmpty
    def ele(deneme: Harf => İkil): Yazı = y.filter(deneme)
    def eleDeğilse(deneme: Harf => İkil): Yazı = y.filterNot(deneme)
    def işle(işlev: Harf => Harf): Yazı = y.map(işlev)
    def işle[A](işlev: Harf => A): Dizi[A] = y.map(işlev)
    def düzİşle(işlev: Harf => Yazı): Yazı = y.flatMap(işlev)
    def düzİşle[A](işlev: Harf => Dizi[A]): Dizi[A] = y.flatMap(işlev)
    def sıralı(implicit ord: Ordering[Harf]): Yazı = y.sorted(ord)
    def sırala[A](i: Harf => A)(implicit ord: Ordering[A]): Yazı = y.sortBy(i)
    def sırayaSok(önce: (Harf, Harf) => İkil): Yazı = y.sortWith(önce)
    def indirge[B >: Harf](işlem: (B, B) => B): B = y.reduce(işlem)
    def soldanKatla[T2](z: T2)(işlev: (T2, Harf) => T2): T2 = y.foldLeft(z)(işlev)
    def sağdanKatla[T2](z: T2)(işlev: (Harf, T2) => T2): T2 = y.foldRight(z)(işlev)
    // https://github.com/scala/scala/blob/v2.12.7/src/library/scala/collection/TraversableOnce.scala#L1
    def topla[T2 >: Harf](implicit num: scala.math.Numeric[T2]) = y.sum(num)    // foldLeft(num.zero)(num.plus)
    def çarp[T2 >: Harf](implicit num: scala.math.Numeric[T2]) = y.product(num) // foldLeft(num.one)(num.times)
    def yinelemesiz = y.distinct
    def yinelemesizİşlevle[T2](işlev: Harf => T2): Yazı = y.distinctBy(işlev)
    def yazıYap: Yazı = y.mkString
    def yazıYap(ara: Yazı): Yazı = y.mkString(ara)
    def yazıYap(baş: Yazı, ara: Yazı, son: Yazı): Yazı = y.mkString(baş, ara, son)
    def tersi = y.reverse
    def değiştir(yeri: Sayı, değeri: Harf): Yazı = y.updated(yeri, değeri)
    // todo: doesn't compile
    // def değiştir[S >: Harf](yeri: Sayı, değeri: S): Dizi[S] = y.updated(yeri, değeri)
    def herbiriİçin[S](işlev: Harf => S): Birim = y.foreach(işlev)
    def varMı(deneme: Harf => İkil): İkil = y.exists(deneme)
    def hepsiDoğruMu(deneme: Harf => İkil): İkil = y.forall(deneme)
    def hepsiİçinDoğruMu(deneme: Harf => İkil): İkil = y.forall(deneme)
    def içeriyorMu[S >: Harf](öge: S): İkil = y.contains(öge)
    def içeriyorMu(dilim: Yazı): İkil = y.contains(dilim)
    def içeriyorMuDilim(dilim: Yazı): İkil = y.containsSlice(dilim) // identical to contains
    def al(n: Sayı): Yazı = y.take(n)
    def alDoğruKaldıkça(deneme: Harf => İkil): Yazı = y.takeWhile(deneme)
    def alSağdan(n: Sayı): Yazı = y.takeRight(n)
    def düşür(n: Sayı): Yazı = y.drop(n)
    def düşürDoğruKaldıkça(deneme: Harf => İkil): Yazı = y.dropWhile(deneme)
    def düşürSağdan(n: Sayı): Yazı = y.dropRight(n)
    def sırası(dilim: Yazı): Sayı = y.indexOf(dilim)
    def sırası(dilim: Yazı, başlamaNoktası: Sayı): Sayı = y.indexOf(dilim, başlamaNoktası)
    def sırası[S >: Harf](öge: S): Sayı = y.indexOf(öge)
    def sırası[S >: Harf](öge: S, başlamaNoktası: Sayı): Sayı = y.indexOf(öge, başlamaNoktası)
    def sırasıSondan(dilim: Yazı): Sayı = y.lastIndexOf(dilim)
    def sırasıSondan(dilim: Yazı, sonNokta: Sayı): Sayı = y.lastIndexOf(dilim, sonNokta)
    def sırasıSondan[S >: Harf](öge: S): Sayı = y.lastIndexOf(öge)
    def sırasıSondan[S >: Harf](öge: S, sonNokta: Sayı): Sayı = y.lastIndexOf(öge, sonNokta)

    def say(işlev: Harf => İkil): Sayı = y.count(işlev)

    // yazı'ya özel
    def kısalt = y.trim
    def değiştir(a: Harf, b: Harf) = y.replace(a, b)
    def değiştir(xler: Yazı, yler: Yazı) = y.replace(xler, yler)
    def değiştirHepsini(xler: Yazı, yler: Yazı) = y.replaceAll(xler, yler)
    def değiştirİlkini(xler: Yazı, yler: Yazı) = y.replaceFirst(xler, yler)
    // https://www.scala-lang.org/api/2.12.7/scala/collection/immutable/StringLike.html
    def böl(delim: Harf): Dizin[Yazı] = y.split(delim).toList
    def böl(delim: Dizim[Harf]): Dizin[Yazı] = y.split(delim.a).toList
    // From java https://www.baeldung.com/string/split
    def böl(delim: Yazı, enÇokParça: Sayı = 0): Dizin[Yazı] = y.split(delim, enÇokParça).toList
    def büyükHarfe: Yazı = y.map(_.toUpper)
    def küçükHarfe: Yazı = y.map(_.toLower)
    def kıyasla(öbürü: Yazı): Sayı = y.compareTo(öbürü)
    def kıyaslaKüçükHarfBüyükHarfAyrımıYapmadan(öbürü: Yazı): Sayı = y.compareToIgnoreCase(öbürü)
    def harf(sıra: Sayı): Char = y.charAt(sıra)
    def eşitMiKüçükHarfBüyükHarfAyrımıYapmadan(öbürü: Yazı): İkil = y.equalsIgnoreCase(öbürü)
    def parçası(nereden: Sayı): Yazı = y.substring(nereden)
    def parçası(nereden: Sayı, nereye: Sayı): Yazı = y.substring(nereden, nereye)

    def başındaMı(öbürü: Yazı): İkil = y.startsWith(öbürü)
    def başındaMı(öbürü: Yazı, başlamaNoktası: Sayı): İkil = y.startsWith(öbürü, başlamaNoktası)
    def sonundaMı(öbürü: Yazı): İkil = y.endsWith(öbürü)

    def kenarPayınıÇıkar = y.stripMargin

    def dizime[S >: Harf](implicit delil: scala.reflect.ClassTag[S]): Dizim[S] = new Dizim(y.toArray(delil))
    def ikile = y.toBoolean
    def lokmaya = y.toByte
    def kesire = y.toDouble
    def ufakKesire = y.toFloat
    def sayıya = y.toInt
    def dizine = y.toList
    def eşleme[K, V](implicit delil: Harf <:< (K, V)): Eşlem[K, V] = Eşlem.değişmezden(y.toMap)
    def diziye = y.toSeq
    def kümeye = y.toSet
    def kısaya = y.toShort
    def yöneye = y.toVector

    def ikileBelki = y.toBooleanOption
    def lokmayaBelki = y.toByteOption
    def kesireBelki = y.toDoubleOption
    def ufakKesireBelki = y.toFloatOption
    def sayıyaBelki = y.toIntOption
    def kısayaBelki = y.toShortOption
    // more to come..
  }

  implicit class EsnekYazıYöntemleri(ey: EsnekYazı) {
    def boşMu = ey.size == 0
    def doluMu = ey.size != 0
    def boyu = ey.size
    def sil() = ey.clear()
    def ekle[T](x: T) = ey.append(x)
    def yazıya = ey.toString
    def sayıya = ey.toString.toInt
  }

}
