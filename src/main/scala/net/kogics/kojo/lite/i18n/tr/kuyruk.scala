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

import collection.mutable.{Stack, Queue, PriorityQueue}

object Yığın {
  def boş[T] = new Yığın[T]()
  def apply[T](elemanlar: T*) = {
    val y = new Yığın[T]()
    for (e <- elemanlar) y.s.push(e)
    y
  }
  def doldur[T](y2: Yığın[T]) = {
    val y = new Yığın[T]()
    y.koyHepsini(y2.dizi)
    y
  }
}
case class Yığın[T]() {
  val s = Stack.empty[T]
  def this(y: Yığın[T]) = {
    this()
    this.koyHepsini(y.s.toList.tail)
  }
  def koy(e: T) = s.push(e)
  def al() = s.pop()
  def tane = s.size
  def tepe = s.head
  def dizi = s.toSeq
  def diziye = s.toSeq
  def koyHepsini(dizi: scala.collection.IterableOnce[T]) =
    s.pushAll(dizi)
}

trait QueueMethodsInTurkish {
  type ÖncelikSırası[T] = PriorityQueue[T]
  object ÖncelikSırası {
    def apply[T](elems: T*)(implicit sıralama: Ordering[T]): ÖncelikSırası[T] = PriorityQueue.from(elems)(sıralama)
  }

  implicit class mutPriQueMethods[T](d: PriorityQueue[T]) {
    type Col = ÖncelikSırası[T]
    type C2[B] = ÖncelikSırası[B]
    type Eşlek[A, D] = collection.immutable.Map[A, D]
    type Dizi[B] = Seq[B]
    type Iter[A] = collection.mutable.Iterable[A]
    def ekle(öge: T) = d.addOne(öge)
    def ekle(ögeler: T*) = d.enqueue(ögeler: _*)
    def baştanAl(): T = d.dequeue()
    def baştanAlHepsini[T2 >: T]: Dizi[T2] = d.dequeueAll
    def sil(): Birim = d.clear()
    def ikizle(): Col = d.clone()

    def başı: T = d.head
    def kuyruğu: Col = d.tail
    def önü: Col = d.init
    def sonu: T = d.last
    def boyu: Sayı = d.length
    def boşMu: İkil = d.isEmpty
    def doluMu: İkil = d.nonEmpty
    def ele(deneme: T => İkil): Col = d.filter(deneme)
    def eleDeğilse(deneme: T => İkil): Col = d.filterNot(deneme)
    def işle[A](işlev: T => A): Iter[A] = d.map(işlev)
    def düzİşle[A](işlev: T => C2[A]): Iter[A] = d.flatMap(işlev)
    def indirge[B >: T](işlem: (B, B) => B): B = d.reduce(işlem)
    def soldanKatla[T2](z: T2)(işlev: (T2, T) => T2): T2 = d.foldLeft(z)(işlev)
    def sağdanKatla[T2](z: T2)(işlev: (T, T2) => T2): T2 = d.foldRight(z)(işlev)
    // https://github.com/scala/scala/blob/v2.12.7/src/library/scala/collection/TraversableOnce.scala#L1
    def topla[T2 >: T](implicit num: scala.math.Numeric[T2]) = d.sum(num)    // foldLeft(num.zero)(num.plus)
    def çarp[T2 >: T](implicit num: scala.math.Numeric[T2]) = d.product(num) // foldLeft(num.one)(num.times)
    def yazıYap: Yazı = d.mkString
    def yazıYap(ara: Yazı): Yazı = d.mkString(ara)
    def yazıYap(baş: Yazı, ara: Yazı, son: Yazı): Yazı = d.mkString(baş, ara, son)
    def tersi = d.reverse
    def herbiriİçin[S](işlev: T => S): Birim = d.foreach(işlev)
    def varMı(deneme: T => İkil): İkil = d.exists(deneme)
    def hepsiDoğruMu(deneme: T => İkil): İkil = d.forall(deneme)
    def hepsiİçinDoğruMu(deneme: T => İkil): İkil = d.forall(deneme)
    def al(n: Sayı): Col = d.take(n)
    def alDoğruKaldıkça(deneme: T => İkil): Col = d.takeWhile(deneme)
    def alSağdan(n: Sayı): Col = d.takeRight(n)
    def düşür(n: Sayı): Col = d.drop(n)
    def düşürDoğruKaldıkça(deneme: T => İkil): Col = d.dropWhile(deneme)
    def düşürSağdan(n: Sayı): Col = d.dropRight(n)

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
    def öbekle[A](iş: (T) => A): Eşlek[A, Col] = d.groupBy(iş)

    def enUfağı[B >: T](implicit sıralama: math.Ordering[B]): T = d.min(sıralama)
    def enUfağı[B](iş: (T) => B)(implicit karşılaştırma: math.Ordering[B]): T = d.minBy(iş)(karşılaştırma)
    def enİrisi[B >: T](implicit sıralama: math.Ordering[B]): T = d.max(sıralama)
    def enİrisi[B](iş: (T) => B)(implicit karşılaştırma: math.Ordering[B]): T = d.maxBy(iş)(karşılaştırma)

    // more to come

  }

  type Kuyruk[T] = Queue[T]
  object Kuyruk {
    def apply[T](elems: T*): Kuyruk[T] = Queue.from(elems)
  }
  implicit class mutQueueMethods[T](d: Queue[T]) {
    type Col = Kuyruk[T]
    type C2[T] = Kuyruk[T]
    type Eşlek[A, D] = collection.immutable.Map[A, D]
    type Dizi[B] = Seq[B]
    type Iter[A] = collection.mutable.Iterable[A]
    def ekle(öge: T) = d.addOne(öge)
    def ekleHepsini(ögeler: Dizi[T]): Col = d.enqueueAll(ögeler)
    def baştanAl(): T = d.dequeue()
    def baştanAlHepsini(deneme: (T) => Boolean): Dizi[T] = d.dequeueAll(deneme)
    def sil(): Birim = d.clear()
    def ikizle(): Col = d.clone()

    def başı: T = d.head
    def kuyruğu: Col = d.tail
    def önü: Col = d.init
    def sonu: T = d.last
    def boyu: Sayı = d.length
    def boşMu: İkil = d.isEmpty
    def doluMu: İkil = d.nonEmpty
    def ele(deneme: T => İkil): Col = d.filter(deneme)
    def eleDeğilse(deneme: T => İkil): Col = d.filterNot(deneme)
    def işle[A](işlev: T => A): Iter[A] = d.map(işlev)
    def düzİşle[A](işlev: T => C2[A]): Iter[A] = d.flatMap(işlev)
    def indirge[B >: T](işlem: (B, B) => B): B = d.reduce(işlem)
    def soldanKatla[T2](z: T2)(işlev: (T2, T) => T2): T2 = d.foldLeft(z)(işlev)
    def sağdanKatla[T2](z: T2)(işlev: (T, T2) => T2): T2 = d.foldRight(z)(işlev)
    // https://github.com/scala/scala/blob/v2.12.7/src/library/scala/collection/TraversableOnce.scala#L1
    def topla[T2 >: T](implicit num: scala.math.Numeric[T2]) = d.sum(num)    // foldLeft(num.zero)(num.plus)
    def çarp[T2 >: T](implicit num: scala.math.Numeric[T2]) = d.product(num) // foldLeft(num.one)(num.times)
    def yazıYap: Yazı = d.mkString
    def yazıYap(ara: Yazı): Yazı = d.mkString(ara)
    def yazıYap(baş: Yazı, ara: Yazı, son: Yazı): Yazı = d.mkString(baş, ara, son)
    def tersi = d.reverse
    def herbiriİçin[S](işlev: T => S): Birim = d.foreach(işlev)
    def varMı(deneme: T => İkil): İkil = d.exists(deneme)
    def hepsiDoğruMu(deneme: T => İkil): İkil = d.forall(deneme)
    def hepsiİçinDoğruMu(deneme: T => İkil): İkil = d.forall(deneme)
    def al(n: Sayı): Col = d.take(n)
    def alDoğruKaldıkça(deneme: T => İkil): Col = d.takeWhile(deneme)
    def alSağdan(n: Sayı): Col = d.takeRight(n)
    def düşür(n: Sayı): Col = d.drop(n)
    def düşürDoğruKaldıkça(deneme: T => İkil): Col = d.dropWhile(deneme)
    def düşürSağdan(n: Sayı): Col = d.dropRight(n)

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
    def öbekle[A](iş: (T) => A): Eşlek[A, Col] = d.groupBy(iş)

    def enUfağı[B >: T](implicit sıralama: math.Ordering[B]): T = d.min(sıralama)
    def enUfağı[B](iş: (T) => B)(implicit karşılaştırma: math.Ordering[B]): T = d.minBy(iş)(karşılaştırma)
    def enİrisi[B >: T](implicit sıralama: math.Ordering[B]): T = d.max(sıralama)
    def enİrisi[B](iş: (T) => B)(implicit karşılaştırma: math.Ordering[B]): T = d.maxBy(iş)(karşılaştırma)

    // more to come
  }
}
