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

import collection.mutable.{Map}

// todo: add more to the interface
object Eşlem {
  def boş[A,D] = new Eşlem[A,D](Map.empty[A,D])
  def apply[A,D](elems: (A,D)*) = new Eşlem[A,D](Map.from(elems))
  def değişmezden[A,D](m: collection.immutable.Map[A,D]) = new Eşlem[A,D](Map.from(m.iterator))
}
case class Eşlem[A,D](val m: Map[A,D]) {
  // todo: duplicated most of the api in Eşlek
  type Belki[T] = Option[T]
  def eşli(a: A) = m.contains(a)
  def eşle(ikili: (A, D)) = m += ikili
  def +=(ikili: (A, D)) = this eşle ikili
  def -=(birinci: A) = m -= birinci
  def herbiriİçin(komutlar: ((A, D)) => Birim) = m.foreach(komutlar)
  def herÖgeİçin(komutlar: ((A, D)) => Birim) = m.foreach(komutlar)
  def sayı: Sayı = m.size
  def dizi = m.toSeq
  def al(a: A): Belki[D] = m.get(a)
  def alYoksa(a: A, varsayılanDeğer: => D) = m.getOrElse(a, varsayılanDeğer)
  def apply(a: A) = m(a)

  def anahtarKümesi = m.keySet
  def anahtarlar = m.keys
  def kaldır = m.lift
  def değerler = m.values

  def başı = m.head
  def kuyruğu = m.tail
  def önü = m.init
  def sonu = m.last
  def boyu: Sayı = m.size
  def boşMu: İkil = m.isEmpty
  def doluMu: İkil = m.nonEmpty
  def ele(deneme: ((A, D)) => İkil) = m.filter(deneme)
  def eleDeğilse(deneme: ((A, D)) => İkil) = m.filterNot(deneme)
  def işle[A2, D2](işlev: ((A, D)) => (A2, D2)) = m.map(işlev)
  def işle[C](işlev: ((A, D)) => C) = m.map(işlev)
  // todo: Dizi[B] or Iterable?
  def düzİşle[B](işlev: ((A, D)) => collection.mutable.Iterable[B]) = m.flatMap(işlev)
  def indirge[B >: (A, D)](işlem: (B, B) => B): B = m.reduce(işlem)
  def indirgeSoldan[B >: (A, D)](işlem: (B, (A, D)) => B): B = m.reduceLeft(işlem)
  def indirgeSağdan[B >: (A, D)](işlem: ((A, D), B) => B): B = m.reduceRight(işlem)
  def indirgeSoldanBelki[B >: (A, D)](işlem: (B, (A, D)) => B): Belki[B] = m.reduceLeftOption(işlem)
  def indirgeSağdanBelki[B >: (A, D)](işlem: ((A, D), B) => B): Belki[B] = m.reduceRightOption(işlem)
  def kalta[B >: (A, D)](z: B)(işlev: (B, B) => B): B = m.fold(z)(işlev)
  def soldanKatla[B](z: B)(işlev: (B, (A, D)) => B): B = m.foldLeft(z)(işlev)
  def sağdanKatla[B](z: B)(işlev: ((A, D), B) => B): B = m.foldRight(z)(işlev)

  def topla[T >: (A, D)](implicit num: scala.math.Numeric[T]) = m.sum(num) 
  def çarp[T >: (A, D)](implicit num: scala.math.Numeric[T]) = m.product(num)

  def yazıYap: Yazı = m.mkString
  def yazıYap(ara: Yazı): Yazı = m.mkString(ara)
  def yazıYap(baş: Yazı, ara: Yazı, son: Yazı): Yazı = m.mkString(baş, ara, son)
  def değiştir(a: A, d: D) = m.clone().addOne(a -> d)
  def varMı(deneme: ((A, D)) => İkil): İkil = m.exists(deneme)

  def hepsiDoğruMu(deneme: ((A, D)) => İkil): İkil = m.forall(deneme)
  def hepsiİçinDoğruMu(deneme: ((A, D)) => İkil): İkil = m.forall(deneme)

  def içeriyorMu(anahtar: A): İkil = m.contains(anahtar)

  def alSırayla(n: Sayı) = m.take(n)
  def alDoğruKaldıkça(deneme: ((A, D)) => İkil) = m.takeWhile(deneme)
  def alSağdan(n: Sayı) = m.takeRight(n)
  def düşür(n: Sayı) = m.drop(n)
  def düşürDoğruKaldıkça(deneme: ((A, D)) => İkil) = m.dropWhile(deneme)
  def düşürSağdan(n: Sayı) = m.dropRight(n)

  def dizine = m.toList
  def diziye = m.toSeq
  def kümeye = m.toSet
  def yöneye = m.toVector
  def dizime[C >: (A, D)](implicit delil: scala.reflect.ClassTag[C]): Dizim[C] = new Dizim(m.toArray(delil))
  def say(işlev: ((A, D)) => İkil): Sayı = m.count(işlev)

  def ikile[C](öbürü: scala.collection.IterableOnce[C]) = m.zip(öbürü)
  def ikileSırayla = m.zipWithIndex

  //
  def varsayılanDeğerle(d: D) = m.withDefaultValue(d: D)
}

trait MapMethodsInTurkish {
  type Eşlek[A, D] = collection.immutable.Map[A, D]
  object Eşlek {
    def apply[A, D](elems: (A, D)*) = collection.immutable.Map.from(elems)
  }
  implicit class EşlekYöntemleri[A, D](m: Eşlek[A, D]) {
    // todo: copied most of the api from Eşlem above
    type C = Eşlek[A, D]
    type Belki[T] = Option[T]
    def eşli(a: A) = m.contains(a)
    def herbiriİçin(komutlar: ((A, D)) => Birim) = m.foreach(komutlar)
    def herÖgeİçin(komutlar: ((A, D)) => Birim) = m.foreach(komutlar)
    def sayı: Sayı = m.size
    def dizi = m.toSeq
    def al(a: A): Belki[D] = m.get(a)
    def alYoksa(a: A, varsayılanDeğer: => D) = m.getOrElse(a, varsayılanDeğer)

    def anahtarKümesi = m.keySet
    def anahtarlar = m.keys
    def kaldır = m.lift
    def değerler = m.values

    def başı = m.head
    def kuyruğu = m.tail
    def önü = m.init
    def sonu = m.last
    def boyu: Sayı = m.size
    def boşMu: İkil = m.isEmpty
    def doluMu: İkil = m.nonEmpty
    def ele(deneme: ((A, D)) => İkil) = m.filter(deneme)
    def eleDeğilse(deneme: ((A, D)) => İkil) = m.filterNot(deneme)
    def işle[A2, D2](işlev: ((A, D)) => (A2, D2)) = m.map(işlev)
    def işle[C](işlev: ((A, D)) => C) = m.map(işlev)
    // todo: Dizi[B] or Iterable?
    def düzİşle[B](işlev: ((A, D)) => collection.mutable.Iterable[B]) = m.flatMap(işlev)
    def indirge[B >: (A, D)](işlem: (B, B) => B): B = m.reduce(işlem)
    def indirgeSoldan[B >: (A, D)](işlem: (B, (A, D)) => B): B = m.reduceLeft(işlem)
    def indirgeSağdan[B >: (A, D)](işlem: ((A, D), B) => B): B = m.reduceRight(işlem)
    def indirgeSoldanBelki[B >: (A, D)](işlem: (B, (A, D)) => B): Belki[B] = m.reduceLeftOption(işlem)
    def indirgeSağdanBelki[B >: (A, D)](işlem: ((A, D), B) => B): Belki[B] = m.reduceRightOption(işlem)
    def kalta[B >: (A, D)](z: B)(işlev: (B, B) => B): B = m.fold(z)(işlev)
    def soldanKatla[B](z: B)(işlev: (B, (A, D)) => B): B = m.foldLeft(z)(işlev)
    def sağdanKatla[B](z: B)(işlev: ((A, D), B) => B): B = m.foldRight(z)(işlev)

    def topla[T >: (A, D)](implicit num: scala.math.Numeric[T]) = m.sum(num)
    def çarp[T >: (A, D)](implicit num: scala.math.Numeric[T]) = m.product(num)

    def yazıYap: Yazı = m.mkString
    def yazıYap(ara: Yazı): Yazı = m.mkString(ara)
    def yazıYap(baş: Yazı, ara: Yazı, son: Yazı): Yazı = m.mkString(baş, ara, son)
    def varMı(deneme: ((A, D)) => İkil): İkil = m.exists(deneme)

    def hepsiDoğruMu(deneme: ((A, D)) => İkil): İkil = m.forall(deneme)
    def hepsiİçinDoğruMu(deneme: ((A, D)) => İkil): İkil = m.forall(deneme)

    def içeriyorMu(anahtar: A): İkil = m.contains(anahtar)

    def alSırayla(n: Sayı) = m.take(n)
    def alDoğruKaldıkça(deneme: ((A, D)) => İkil) = m.takeWhile(deneme)
    def alSağdan(n: Sayı) = m.takeRight(n)
    def düşür(n: Sayı) = m.drop(n)
    def düşürDoğruKaldıkça(deneme: ((A, D)) => İkil) = m.dropWhile(deneme)
    def düşürSağdan(n: Sayı) = m.dropRight(n)

    def dizine = m.toList
    def diziye = m.toSeq
    def kümeye = m.toSet
    def yöneye = m.toVector
    def dizime[C >: (A, D)](implicit delil: scala.reflect.ClassTag[C]): Dizim[C] = new Dizim(m.toArray(delil))
    def say(işlev: ((A, D)) => İkil): Sayı = m.count(işlev)

    def ikile[C](öbürü: scala.collection.IterableOnce[C]) = m.zip(öbürü)
    def ikileSırayla = m.zipWithIndex

    //
    def varsayılanDeğerle(d: D) = m.withDefaultValue(d: D)
    // öbekle, bölükle, bölüklereAyır, parçala, gruplaştır
    def öbekle(iş: ((A, D)) => A): Eşlek[A, Eşlek[A, D]] = m.groupBy(iş)
    def değiştirilmiş[D1 >: D](a: A, d: D1): Eşlek[A, D1] = m.updated(a, d)

    // todo: more to come
  }
}
