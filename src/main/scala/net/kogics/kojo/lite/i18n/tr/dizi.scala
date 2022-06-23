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

trait SeqMethodsInTurkish {
  // todo: duplicates in yazi.scala and dizin.scala and more
  implicit class SeqYöntemleri[T](d: Dizi[T]) {
    def başı: T = d.head
    def kuyruğu: Dizi[T] = d.tail
    def önü: Dizi[T] = d.init
    def sonu: T = d.last
    def boyu: Sayı = d.length
    def boşMu: İkil = d.isEmpty
    def doluMu: İkil = d.nonEmpty
    def ele(deneme: T => İkil): Dizi[T] = d.filter(deneme)
    def eleDeğilse(deneme: T => İkil): Dizi[T] = d.filterNot(deneme)
    def işle[A](işlev: T => A): Dizi[A] = d.map(işlev)
    def düzİşle[A](işlev: T => Dizi[A]): Dizi[A] = d.flatMap(işlev)
    def sıralı(implicit ord: Ordering[T]): Dizi[T] = d.sorted(ord)
    def sırala[A](i: T => A)(implicit ord: Ordering[A]): Dizi[T] = d.sortBy(i)
    def sırayaSok(önce: (T, T) => İkil): Dizi[T] = d.sortWith(önce)
    def indirge[B >: T](işlem: (B, B) => B): B = d.reduce(işlem)
    def soldanKatla[T2](z: T2)(işlev: (T2, T) => T2): T2 = d.foldLeft(z)(işlev)
    def sağdanKatla[T2](z: T2)(işlev: (T, T2) => T2): T2 = d.foldRight(z)(işlev)
    // https://github.com/scala/scala/blob/v2.12.7/src/library/scala/collection/TraversableOnce.scala#L1
    def topla[T2 >: T](implicit num: scala.math.Numeric[T2]) = d.sum(num)    // foldLeft(num.zero)(num.plus)
    def çarp[T2 >: T](implicit num: scala.math.Numeric[T2]) = d.product(num) // foldLeft(num.one)(num.times)
    def yinelemesiz = d.distinct
    def yinelemesizİşlevle[T2](işlev: T => T2): Dizi[T] = d.distinctBy(işlev)
    def yazıYap: Yazı = d.mkString
    def yazıYap(ara: Yazı): Yazı = d.mkString(ara)
    def yazıYap(baş: Yazı, ara: Yazı, son: Yazı): Yazı = d.mkString(baş, ara, son)
    def tersi = d.reverse
    def değiştir[S >: T](yeri: Sayı, değeri: S): Dizi[S] = d.updated(yeri, değeri)
    def herbiriİçin[S](işlev: T => S): Birim = d.foreach(işlev)
    def varMı(deneme: T => İkil): İkil = d.exists(deneme)
    def hepsiDoğruMu(deneme: T => İkil): İkil = d.forall(deneme)
    def hepsiİçinDoğruMu(deneme: T => İkil): İkil = d.forall(deneme)
    def içeriyorMu[S >: T](öge: S): İkil = d.contains(öge)
    def içeriyorMuDilim[T](dilim: Dizi[T]): İkil = d.containsSlice(dilim)
    def al(n: Sayı): Dizi[T] = d.take(n)
    def alDoğruKaldıkça(deneme: T => İkil): Dizi[T] = d.takeWhile(deneme)
    def alSağdan(n: Sayı): Dizi[T] = d.takeRight(n)
    def düşür(n: Sayı): Dizi[T] = d.drop(n)
    def düşürDoğruKaldıkça(deneme: T => İkil): Dizi[T] = d.dropWhile(deneme)
    def düşürSağdan(n: Sayı): Dizi[T] = d.dropRight(n)
    def sırası[S >: T](öge: S): Sayı = d.indexOf(öge)
    def sırası[S >: T](öge: S, başlamaNoktası: Sayı): Sayı = d.indexOf(öge, başlamaNoktası)
    def sırasıSondan[S >: T](öge: S): Sayı = d.lastIndexOf(öge)
    def sırasıSondan[S >: T](öge: S, sonNokta: Sayı): Sayı = d.lastIndexOf(öge, sonNokta)

    def dizine = d.toList
    def diziye = d.toSeq
    def kümeye = d.toSet
    def yöneye = d.toVector
    def dizime[S >: T](implicit delil: scala.reflect.ClassTag[S]): Dizim[S] = new Dizim(d.toArray(delil))
    def eşleme[K, V](implicit delil: T <:< (K, V)): Eşlem[K, V] = Eşlem.değişmezden(d.toMap)
    def say(işlev: T => İkil): Sayı = d.count(işlev)

    def dilim(nereden: Sayı, nereye: Sayı) = d.slice(nereden, nereye)
    def ikile[S](öbürü: scala.collection.IterableOnce[S]) = d.zip(öbürü)
    def ikileSırayla = d.zipWithIndex

    // more to come
  }
}
