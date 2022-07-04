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

trait SetMethodsInTurkish {
  type Küme[T] = Set[T]
  object Küme {
    def apply[T](elemanlar: T*) = Set.from(elemanlar)
    def boş[T] = Set.empty[T]
  }
  // todo: duplicates in yazi.scala and dizin.scala
  implicit class SetYöntemleri[T](d: Küme[T]) {
    type Eşlek[A, D] = collection.immutable.Map[A, D]
    def başı: T = d.head
    def kuyruğu: Küme[T] = d.tail
    def boyu: Sayı = d.size
    def boşMu: İkil = d.isEmpty
    def doluMu: İkil = d.nonEmpty
    def ele(deneme: T => İkil): Küme[T] = d.filter(deneme)
    def eleDeğilse(deneme: T => İkil): Küme[T] = d.filterNot(deneme)
    def işle[A](işlev: T => A): Küme[A] = d.map(işlev)
    def düzİşle[A](işlev: T => Küme[A]): Küme[A] = d.flatMap(işlev)
    def indirge[B >: T](işlem: (B, B) => B): B = d.reduce(işlem)
    def soldanKatla[T2](z: T2)(işlev: (T2, T) => T2): T2 = d.foldLeft(z)(işlev)
    def sağdanKatla[T2](z: T2)(işlev: (T, T2) => T2): T2 = d.foldRight(z)(işlev)
    // https://github.com/scala/scala/blob/v2.12.7/src/library/scala/collection/TraversableOnce.scala#L1
    def topla[T2 >: T](implicit num: scala.math.Numeric[T2]) = d.sum(num)    // foldLeft(num.zero)(num.plus)
    def çarp[T2 >: T](implicit num: scala.math.Numeric[T2]) = d.product(num) // foldLeft(num.one)(num.times)
    def yazıYap: Yazı = d.mkString
    def yazıYap(ara: Yazı): Yazı = d.mkString(ara)
    def yazıYap(baş: Yazı, ara: Yazı, son: Yazı): Yazı = d.mkString(baş, ara, son)
    def herbiriİçin[S](işlev: T => S): Birim = d.foreach(işlev)
    def varMı(deneme: T => İkil): İkil = d.exists(deneme)
    def hepsiDoğruMu(deneme: T => İkil): İkil = d.forall(deneme)
    def hepsiİçinDoğruMu(deneme: T => İkil): İkil = d.forall(deneme)
    def içeriyorMu(öge: T): İkil = d.contains(öge)
    def al(n: Sayı): Küme[T] = d.take(n)
    def alDoğruKaldıkça(deneme: T => İkil): Küme[T] = d.takeWhile(deneme)
    def alSağdan(n: Sayı): Küme[T] = d.takeRight(n)
    def düşür(n: Sayı): Küme[T] = d.drop(n)
    def düşürDoğruKaldıkça(deneme: T => İkil): Küme[T] = d.dropWhile(deneme)
    def düşürSağdan(n: Sayı): Küme[T] = d.dropRight(n)

    def dizine = d.toList
    def diziye = d.toSeq
    def kümeye = d.toSet
    def yöneye = d.toVector
    def dizime[S >: T](implicit delil: scala.reflect.ClassTag[S]): Dizim[S] = new Dizim(d.toArray(delil))
    def eşleğe[A, D](implicit delil: T <:< (A, D)): Eşlek[A, D] = d.toMap
    def eşleme[A, D](implicit delil: T <:< (A, D)): Eşlem[A, D] = Eşlem.değişmezden(d.toMap)
    def say(işlev: T => İkil): Sayı = d.count(işlev)

    def dilim(nereden: Sayı, nereye: Sayı) = d.slice(nereden, nereye)
    def ikile[S](öbürü: scala.collection.IterableOnce[S]) = d.zip(öbürü)
    def ikileSırayla = d.zipWithIndex
    def ikileKonumla = d.zipWithIndex
    def öbekle[A](iş: (T) => A): Eşlek[A, Küme[T]] = d.groupBy(iş)

    // more to come
  }
}
