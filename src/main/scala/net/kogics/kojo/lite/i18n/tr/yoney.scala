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

trait VectorMethodsInTurkish {
  type Yöney[T] = Vector[T]
  object Yöney {
    def apply[T](elemanlar: T*) = Vector.from(elemanlar)
    def unapplySeq[T](yler: Vector[T]) = Vector.unapplySeq(yler)
    def boş[T] = Vector.empty[T]
  }

  implicit class YöneyYöntemleri[A](y: Yöney[A]) {
    type Eşlek[A, D] = collection.immutable.Map[A, D]
    def başı: A = y.head
    def kuyruğu: Yöney[A] = y.tail
    def önü: Yöney[A] = y.init
    def sonu: A = y.last
    def boyu: Sayı = y.length
    def boşMu: İkil = y.isEmpty
    def doluMu: İkil = y.nonEmpty
    def ele(deneme: A => İkil): Diz[A] = y.filter(deneme)
    def eleDeğilse(deneme: A => İkil): Yöney[A] = y.filterNot(deneme)
    def işle[B](işlev: A => B): Yöney[B] = y.map(işlev)
    def düzİşle[B](işlev: A => Dizi[B]): Yöney[B] = y.flatMap(işlev)
    def sıralı(implicit ord: Ordering[A]): Yöney[A] = y.sorted(ord)
    def sırala[B](i: A => B)(implicit ord: Ordering[B]): Yöney[A] = y.sortBy(i)
    def sırayaSok(önce: (A, A) => İkil): Yöney[A] = y.sortWith(önce)
    def indirge[B >: A](işlem: (B, B) => B): B = y.reduce(işlem)
    def soldanKatla[B](z: B)(işlev: (B, A) => B): B = y.foldLeft(z)(işlev)
    def sağdanKatla[B](z: B)(işlev: (A, B) => B): B = y.foldRight(z)(işlev)
    // https://github.com/scala/scala/blob/v2.12.7/src/library/scala/collection/TraversableOnce.scala#L1
    def topla[B >: A](implicit num: scala.math.Numeric[B]) = y.sum(num)    // foldLeft(num.zero)(num.plus)
    def çarp[B >: A](implicit num: scala.math.Numeric[B]) = y.product(num) // foldLeft(num.one)(num.times)
    def yinelemesiz = y.distinct
    def yinelemesizİşlevle[B](işlev: A => B): Yöney[A] = y.distinctBy(işlev)
    def yazıYap: Yazı = y.mkString
    def yazıYap(ara: Yazı): Yazı = y.mkString(ara)
    def yazıYap(baş: Yazı, ara: Yazı, son: Yazı): Yazı = y.mkString(baş, ara, son)
    def tersi = y.reverse
    def değiştir[B >: A](yeri: Sayı, değeri: B): Yöney[B] = y.updated(yeri, değeri)
    def herbiriİçin[S](işlev: A => S): Birim = y.foreach(işlev)
    def varMı(deneme: A => İkil): İkil = y.exists(deneme)
    def hepsiDoğruMu(deneme: A => İkil): İkil = y.forall(deneme)
    def hepsiİçinDoğruMu(deneme: A => İkil): İkil = y.forall(deneme)
    def içeriyorMu[S >: A](öge: S): İkil = y.contains(öge)
    def içeriyorMuDilim(dilim: Diz[A]): İkil = y.containsSlice(dilim)
    def al(n: Sayı): Yöney[A] = y.take(n)
    def alDoğruKaldıkça(deneme: A => İkil): Yöney[A] = y.takeWhile(deneme)
    def alSağdan(n: Sayı): Yöney[A] = y.takeRight(n)
    def düşür(n: Sayı): Yöney[A] = y.drop(n)
    def düşürDoğruKaldıkça(deneme: A => İkil): Yöney[A] = y.dropWhile(deneme)
    def düşürSağdan(n: Sayı): Yöney[A] = y.dropRight(n)
    def sırası[S >: A](öge: S): Sayı = y.indexOf(öge)
    def sırası[S >: A](öge: S, başlamaNoktası: Sayı): Sayı = y.indexOf(öge, başlamaNoktası)
    def sırasıSondan[S >: A](öge: S): Sayı = y.lastIndexOf(öge)
    def sırasıSondan[S >: A](öge: S, sonNokta: Sayı): Sayı = y.lastIndexOf(öge, sonNokta)

    def dizine = y.toList
    def diziye = y.toSeq
    def kümeye = y.toSet
    def yöneye = y.toVector
    def dizime[S >: A](implicit delil: scala.reflect.ClassTag[S]): Dizim[S] = new Dizim(y.toArray(delil))
    def eşleğe[A2, D](implicit delil: A <:< (A2, D)): Eşlek[A2, D] = y.toMap
    def eşleme[A2, D](implicit delil: A <:< (A2, D)): Eşlem[A2, D] = Eşlem.değişmezden(y.toMap)
    def say(işlev: A => İkil): Sayı = y.count(işlev)

    def dilim(nereden: Sayı, nereye: Sayı) = y.slice(nereden, nereye)
    def ikile[B](öbürü: scala.collection.IterableOnce[B]) = y.zip(öbürü)
    def ikileSırayla = y.zipWithIndex
    def ikileKonumla = y.zipWithIndex
    def öbekle[A2](iş: (A) => A2): Eşlek[A2, Yöney[A]] = y.groupBy(iş)
    // more to come
  }
}
