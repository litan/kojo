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

import scala.reflect.ClassTag

trait VectorMethodsInTurkish {
  type Yöney[T] = Vector[T]
  object Yöney {
    def apply[T](elemanlar: T*) = Vector.from(elemanlar)
    def unapplySeq[T](yler: Vector[T]) = Vector.unapplySeq(yler)
    def boş[T] = Vector.empty[T]
    def doldur[T:ClassTag](b1: Sayı)(e: => T) = Vector.fill[T](b1)(e)
  }

  implicit class YöneyYöntemleri[A](y: Yöney[A]) {
    type Belki[B] = Option[B]
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
    def yazıYap(başı: Yazı, ara: Yazı, sonu: Yazı): Yazı = y.mkString(başı, ara, sonu)
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
    def ikile[B](öbürü: YinelenebilirBirKere[B]) = y.zip(öbürü)
    def ikileSırayla = y.zipWithIndex
    def ikileKonumla = y.zipWithIndex
    def öbekle[A2](iş: (A) => A2): Eşlek[A2, Yöney[A]] = y.groupBy(iş)

    def enUfağı[B >: A](implicit sıralama: math.Ordering[B]): A = y.min(sıralama)
    def enUfağı[B](iş: (A) => B)(implicit karşılaştırma: math.Ordering[B]): A = y.minBy(iş)(karşılaştırma)
    def enİrisi[B >: A](implicit sıralama: math.Ordering[B]): A = y.max(sıralama)
    def enİrisi[B](iş: (A) => B)(implicit karşılaştırma: math.Ordering[B]): A = y.maxBy(iş)(karşılaştırma)

    // --- uçlar, arama --------------------------------------------------
    def başıBelki: Belki[A] = y.headOption
    def sonuBelki: Belki[A] = y.lastOption
    def bul(deneme: A => İkil): Belki[A] = y.find(deneme)
    def bulSondan(deneme: A => İkil): Belki[A] = y.findLast(deneme)
    def nerede(deneme: A => İkil): Sayı = y.indexWhere(deneme)
    def nerede(deneme: A => İkil, başlamaNoktası: Sayı): Sayı = y.indexWhere(deneme, başlamaNoktası)
    def neredeSondan(deneme: A => İkil): Sayı = y.lastIndexWhere(deneme)
    def dilimSırası[S >: A](dilim: Diz[S]): Sayı = y.indexOfSlice(dilim)
    def dilimSırasıSondan[S >: A](dilim: Diz[S]): Sayı = y.lastIndexOfSlice(dilim)
    def sıralar: Range = y.indices
    def başındaMı[S >: A](dizi: Yinelenebilir[S]): İkil = y.startsWith(dizi)
    def sonundaMı[S >: A](dizi: Yinelenebilir[S]): İkil = y.endsWith(dizi)
    def karşılıklıMı[S](öbürü: Diz[S])(deneme: (A, S) => İkil): İkil = y.corresponds(öbürü)(deneme)

    // --- bölme, öbekleme -----------------------------------------------
    def böl(deneme: A => İkil): (Yöney[A], Yöney[A]) = y.partition(deneme)
    def bölİşle[A1, A2](işlev: A => Either[A1, A2]): (Yöney[A1], Yöney[A2]) = y.partitionMap(işlev)
    def bölDoğruKaldıkça(deneme: A => İkil): (Yöney[A], Yöney[A]) = y.span(deneme)
    def bölYerinden(yeri: Sayı): (Yöney[A], Yöney[A]) = y.splitAt(yeri)
    def öbekli(boy: Sayı): Yineleyici[Yöney[A]] = y.grouped(boy)
    def kayarÖbekli(boy: Sayı): Yineleyici[Yöney[A]] = y.sliding(boy)
    def kayarÖbekli(boy: Sayı, adım: Sayı): Yineleyici[Yöney[A]] = y.sliding(boy, adım)
    def öbekleİşle[K, B](anahtar: A => K)(değer: A => B): Eşlek[K, Yöney[B]] = y.groupMap(anahtar)(değer)
    def öbekleİşleİndirge[K, B](anahtar: A => K)(değer: A => B)(indirge: (B, B) => B): Eşlek[K, B] =
      y.groupMapReduce(anahtar)(değer)(indirge)
    def kombinasyonlar(ögeSayısı: Sayı): Yineleyici[Yöney[A]] = y.combinations(ögeSayısı)
    def permütasyonlar: Yineleyici[Yöney[A]] = y.permutations
    def kuyruklar: Yineleyici[Yöney[A]] = y.tails
    def önler: Yineleyici[Yöney[A]] = y.inits

    // --- katlama, indirgeme, tarama ------------------------------------
    def katla[S >: A](z: S)(işlev: (S, S) => S): S = y.fold(z)(işlev)
    def indirgeSoldan[S >: A](işlem: (S, A) => S): S = y.reduceLeft(işlem)
    def indirgeSağdan[S >: A](işlem: (A, S) => S): S = y.reduceRight(işlem)
    def indirgeBelki[S >: A](işlem: (S, S) => S): Belki[S] = y.reduceOption(işlem)
    def indirgeSoldanBelki[S >: A](işlem: (S, A) => S): Belki[S] = y.reduceLeftOption(işlem)
    def indirgeSağdanBelki[S >: A](işlem: (A, S) => S): Belki[S] = y.reduceRightOption(işlem)
    // tara: katla gibi, ama ara sonuçların HEPSİNİ verir
    def tara[S >: A](z: S)(işlev: (S, S) => S): Yöney[S] = y.scan(z)(işlev)
    def taraSoldan[B](z: B)(işlev: (B, A) => B): Yöney[B] = y.scanLeft(z)(işlev)
    def taraSağdan[B](z: B)(işlev: (A, B) => B): Yöney[B] = y.scanRight(z)(işlev)
    def enUfağıBelki[S >: A](implicit sıralama: math.Ordering[S]): Belki[A] = y.minOption(sıralama)
    def enUfağıBelki[B](iş: A => B)(implicit karşılaştırma: math.Ordering[B]): Belki[A] = y.minByOption(iş)(karşılaştırma)
    def enİrisiBelki[S >: A](implicit sıralama: math.Ordering[S]): Belki[A] = y.maxOption(sıralama)
    def enİrisiBelki[B](iş: A => B)(implicit karşılaştırma: math.Ordering[B]): Belki[A] = y.maxByOption(iş)(karşılaştırma)

    // --- ekleme, çıkarma -----------------------------------------------
    def sonunaEkle[S >: A](öge: S): Yöney[S] = y.appended(öge)
    def önüneEkle[S >: A](öge: S): Yöney[S] = y.prepended(öge)
    def sonunaEkleHepsini[S >: A](öbürü: YinelenebilirBirKere[S]): Yöney[S] = y.appendedAll(öbürü)
    def önüneEkleHepsini[S >: A](öbürü: YinelenebilirBirKere[S]): Yöney[S] = y.prependedAll(öbürü)
    def uzat[S >: A](boy: Sayı, öge: S): Yöney[S] = y.padTo(boy, öge)
    def yama[S >: A](nereden: Sayı, yenisi: YinelenebilirBirKere[S], kaçTane: Sayı): Yöney[S] =
      y.patch(nereden, yenisi, kaçTane)
    def fark[S >: A](öbürü: Diz[S]): Yöney[A] = y.diff(öbürü)
    def kesişim[S >: A](öbürü: Diz[S]): Yöney[A] = y.intersect(öbürü)
    def bileşim[S >: A](öbürü: Diz[S]): Yöney[S] = y.concat(öbürü)

    // --- seçme, düzleştirme, ikili işlemler ----------------------------
    def seçİşle[B](işlev: PartialFunction[A, B]): Yöney[B] = y.collect(işlev)
    def seçİşleİlk[B](işlev: PartialFunction[A, B]): Belki[B] = y.collectFirst(işlev)
    def düzleştir[B](implicit delil: A => YinelenebilirBirKere[B]): Yöney[B] = y.flatten(delil)
    def devrik[B](implicit delil: A => Yinelenebilir[B]): Yöney[Yöney[B]] = y.transpose(delil)
    def ikiliyiAç[A1, A2](implicit delil: A => (A1, A2)): (Yöney[A1], Yöney[A2]) = y.unzip(delil)
    def ikileHepsini[B, S >: A](öbürü: Yinelenebilir[B], buDolgu: S, oDolgu: B): Yöney[(S, B)] =
      y.zipAll(öbürü, buDolgu, oDolgu)
    def tersİşle[B](işlev: A => B): Yöney[B] = y.reverse.map(işlev)

    // more to come
  }
}
