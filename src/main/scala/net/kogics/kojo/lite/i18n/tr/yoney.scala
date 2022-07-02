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
  implicit class YöneyYöntemleri[A](y: Yöney[A]) {
    type Eşlek[A, D] = collection.immutable.Map[A, D]
    // başı, kuyruğu, boyu metodları ... dizi.scala'daki SeqYöntemleri'nden geliyor
    // değiştir metodu da var orada ama Seq return ediyor ve Game of Life kodu çalışmıyor.
    def değiştir[B >: A](yeri: Sayı, değeri: B): Yöney[B] = y.updated(yeri, değeri)
    // todo: more to come, first do the methods that return Yöney

    def kuyruğu: Yöney[A] = y.tail
    def önü: Yöney[A] = y.init
    def eleDeğilse(deneme: A => İkil): Yöney[A] = y.filterNot(deneme)
    def işle[B](işlev: A => B): Yöney[B] = y.map(işlev)
    def düzİşle[B](işlev: A => Dizi[B]): Yöney[B] = y.flatMap(işlev)
    def sıralı(implicit ord: Ordering[A]): Yöney[A] = y.sorted(ord)
    def sırala[B](i: A => B)(implicit ord: Ordering[B]): Yöney[A] = y.sortBy(i)
    def sırayaSok(önce: (A, A) => İkil): Yöney[A] = y.sortWith(önce)
    def yinelemesiz = y.distinct
    def yinelemesizİşlevle[B](işlev: A => B): Yöney[A] = y.distinctBy(işlev)
    def tersi = y.reverse
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
