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

// Note: The seq/map/set/list like collection types we provide translations for are in the following files:
//   dizim dizin eslem kume miskindizin yazı yoney
// And here are (all?) the collection types in Turkish:
//   Diz Dizi Dizik Dizim Dizin EsnekDizim EsnekYazı Eşlem Eşlek Küme MiskinDizin Yazı Yığın Yöney
trait SeqMethodsInTurkish {
  object Dizi {
    def apply[B](elems: B*): Dizi[B] = Seq.from(elems)
    def unapplySeq[B](dizi: Dizi[B]) = Seq.unapplySeq(dizi)
    def doldur[B](n1: Sayı)(f: Sayı => B) = Seq.tabulate(n1)(f)
    def doldur[B](n1: Sayı, n2: Sayı)(f: (Sayı, Sayı) => B) = Seq.tabulate(n1, n2)(f)
    def doldur[B](n1: Sayı, n2: Sayı, n3: Sayı)(f: (Sayı, Sayı, Sayı) => B) = Seq.tabulate(n1, n2, n3)(f)
    def doldur[B](n1: Sayı, n2: Sayı, n3: Sayı, n4: Sayı)(f: (Sayı, Sayı, Sayı, Sayı) => B) = Seq.tabulate(n1, n2, n3, n4)(f)
    def doldur[B](n1: Sayı, n2: Sayı, n3: Sayı, n4: Sayı, n5: Sayı)(f: (Sayı, Sayı, Sayı, Sayı, Sayı) => B) =
      Seq.tabulate(n1, n2, n3, n4, n5)(f)
  }

  // collection.Seq[B]
  object Diz {
    def apply[B](elems: B*): Diz[B] = collection.Seq.from(elems)
    def unapplySeq[B](dizi: Diz[B]) = collection.Seq.unapplySeq(dizi)
    def doldur[B](n1: Sayı)(f: Sayı => B) = collection.Seq.tabulate(n1)(f)
  }
  implicit class colSeqYöntemleri[T](d: Diz[T]) {
    type Col = Diz[T]
    type C2[B] = Diz[B]
    type Eşlek[A, D] = collection.immutable.Map[A, D]
    // duplicate below in Dizi
    def başı: T = d.head
    def kuyruğu: Col = d.tail
    def önü: Col = d.init
    def sonu: T = d.last
    def boyu: Sayı = d.length
    def boşMu: İkil = d.isEmpty
    def doluMu: İkil = d.nonEmpty
    def ele(deneme: T => İkil): Col = d.filter(deneme)
    def eleDeğilse(deneme: T => İkil): Col = d.filterNot(deneme)
    def işle[A](işlev: T => A): C2[A] = d.map(işlev)
    def düzİşle[A](işlev: T => C2[A]): C2[A] = d.flatMap(işlev)
    def sıralı(implicit ord: Ordering[T]): Col = d.sorted(ord)
    def sırala[A](i: T => A)(implicit ord: Ordering[A]): Col = d.sortBy(i)
    def sırayaSok(önce: (T, T) => İkil): Col = d.sortWith(önce)
    def indirge[B >: T](işlem: (B, B) => B): B = d.reduce(işlem)
    def soldanKatla[T2](z: T2)(işlev: (T2, T) => T2): T2 = d.foldLeft(z)(işlev)
    def sağdanKatla[T2](z: T2)(işlev: (T, T2) => T2): T2 = d.foldRight(z)(işlev)
    // https://github.com/scala/scala/blob/v2.12.7/src/library/scala/collection/TraversableOnce.scala#L1
    def topla[T2 >: T](implicit num: scala.math.Numeric[T2]) = d.sum(num)    // foldLeft(num.zero)(num.plus)
    def çarp[T2 >: T](implicit num: scala.math.Numeric[T2]) = d.product(num) // foldLeft(num.one)(num.times)
    def yinelemesiz = d.distinct
    def yinelemesizİşlevle[T2](işlev: T => T2): Col = d.distinctBy(işlev)
    def yazıYap: Yazı = d.mkString
    def yazıYap(ara: Yazı): Yazı = d.mkString(ara)
    def yazıYap(baş: Yazı, ara: Yazı, son: Yazı): Yazı = d.mkString(baş, ara, son)
    def tersi = d.reverse
    def değiştir[S >: T](yeri: Sayı, değeri: S): C2[S] = d.updated(yeri, değeri)
    def herbiriİçin[S](işlev: T => S): Birim = d.foreach(işlev)
    def varMı(deneme: T => İkil): İkil = d.exists(deneme)
    def hepsiDoğruMu(deneme: T => İkil): İkil = d.forall(deneme)
    def hepsiİçinDoğruMu(deneme: T => İkil): İkil = d.forall(deneme)
    def içeriyorMu[S >: T](öge: S): İkil = d.contains(öge)
    def içeriyorMuDilim(dilim: Col): İkil = d.containsSlice(dilim)
    def al(n: Sayı): Col = d.take(n)
    def alDoğruKaldıkça(deneme: T => İkil): Col = d.takeWhile(deneme)
    def alSağdan(n: Sayı): Col = d.takeRight(n)
    def düşür(n: Sayı): Col = d.drop(n)
    def düşürDoğruKaldıkça(deneme: T => İkil): Col = d.dropWhile(deneme)
    def düşürSağdan(n: Sayı): Col = d.dropRight(n)
    def sırası[S >: T](öge: S): Sayı = d.indexOf(öge)
    def sırası[S >: T](öge: S, başlamaNoktası: Sayı): Sayı = d.indexOf(öge, başlamaNoktası)
    def sırasıSondan[S >: T](öge: S): Sayı = d.lastIndexOf(öge)
    def sırasıSondan[S >: T](öge: S, sonNokta: Sayı): Sayı = d.lastIndexOf(öge, sonNokta)

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

    // todo: more to come
  }

  // todo: duplicates in yazi.scala and dizin.scala and more
  implicit class SeqYöntemleri[T](d: Dizi[T]) {
    type Col = Dizi[T]
    type C2[B] = Dizi[B]
    type Eşlek[A, D] = collection.immutable.Map[A, D]
    // duplicate above in Diz
    def başı: T = d.head
    def kuyruğu: Col = d.tail
    def önü: Col = d.init
    def sonu: T = d.last
    def boyu: Sayı = d.length
    def boşMu: İkil = d.isEmpty
    def doluMu: İkil = d.nonEmpty
    def ele(deneme: T => İkil): Col = d.filter(deneme)
    def eleDeğilse(deneme: T => İkil): Col = d.filterNot(deneme)
    def işle[A](işlev: T => A): C2[A] = d.map(işlev)
    def düzİşle[A](işlev: T => C2[A]): C2[A] = d.flatMap(işlev)
    def sıralı(implicit ord: Ordering[T]): Col = d.sorted(ord)
    def sırala[A](i: T => A)(implicit ord: Ordering[A]): Col = d.sortBy(i)
    def sırayaSok(önce: (T, T) => İkil): Col = d.sortWith(önce)
    def indirge[B >: T](işlem: (B, B) => B): B = d.reduce(işlem)
    def soldanKatla[T2](z: T2)(işlev: (T2, T) => T2): T2 = d.foldLeft(z)(işlev)
    def sağdanKatla[T2](z: T2)(işlev: (T, T2) => T2): T2 = d.foldRight(z)(işlev)
    // https://github.com/scala/scala/blob/v2.12.7/src/library/scala/collection/TraversableOnce.scala#L1
    def topla[T2 >: T](implicit num: scala.math.Numeric[T2]) = d.sum(num)    // foldLeft(num.zero)(num.plus)
    def çarp[T2 >: T](implicit num: scala.math.Numeric[T2]) = d.product(num) // foldLeft(num.one)(num.times)
    def yinelemesiz = d.distinct
    def yinelemesizİşlevle[T2](işlev: T => T2): Col = d.distinctBy(işlev)
    def yazıYap: Yazı = d.mkString
    def yazıYap(ara: Yazı): Yazı = d.mkString(ara)
    def yazıYap(baş: Yazı, ara: Yazı, son: Yazı): Yazı = d.mkString(baş, ara, son)
    def tersi = d.reverse
    def değiştir[S >: T](yeri: Sayı, değeri: S): C2[S] = d.updated(yeri, değeri)
    def herbiriİçin[S](işlev: T => S): Birim = d.foreach(işlev)
    def varMı(deneme: T => İkil): İkil = d.exists(deneme)
    def hepsiDoğruMu(deneme: T => İkil): İkil = d.forall(deneme)
    def hepsiİçinDoğruMu(deneme: T => İkil): İkil = d.forall(deneme)
    def içeriyorMu[S >: T](öge: S): İkil = d.contains(öge)
    def içeriyorMuDilim(dilim: Col): İkil = d.containsSlice(dilim)
    def al(n: Sayı): Col = d.take(n)
    def alDoğruKaldıkça(deneme: T => İkil): Col = d.takeWhile(deneme)
    def alSağdan(n: Sayı): Col = d.takeRight(n)
    def düşür(n: Sayı): Col = d.drop(n)
    def düşürDoğruKaldıkça(deneme: T => İkil): Col = d.dropWhile(deneme)
    def düşürSağdan(n: Sayı): Col = d.dropRight(n)
    def sırası[S >: T](öge: S): Sayı = d.indexOf(öge)
    def sırası[S >: T](öge: S, başlamaNoktası: Sayı): Sayı = d.indexOf(öge, başlamaNoktası)
    def sırasıSondan[S >: T](öge: S): Sayı = d.lastIndexOf(öge)
    def sırasıSondan[S >: T](öge: S, sonNokta: Sayı): Sayı = d.lastIndexOf(öge, sonNokta)

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

  implicit class IndexedSeqYöntemleri[T](d: DiziSıralı[T]) { // used in alfabeta in othello
    type Col = DiziSıralı[T]
    def ele(deneme: T => İkil): Col = d.filter(deneme)
    def enUfağı[B >: T](implicit sıralama: math.Ordering[B]): T = d.min(sıralama)
    def enUfağı[B](iş: (T) => B)(implicit karşılaştırma: math.Ordering[B]): T = d.minBy(iş)(karşılaştırma)
    def enİrisi[B >: T](implicit sıralama: math.Ordering[B]): T = d.max(sıralama)
    def enİrisi[B](iş: (T) => B)(implicit karşılaştırma: math.Ordering[B]): T = d.maxBy(iş)(karşılaştırma)
  }

  implicit class ImmutableIterableMethods[T](d: collection.immutable.Iterable[T]) {
    type Col = collection.immutable.Iterable[T]
    type C2[B] = collection.immutable.Iterable[B]
    type Eşlek[A, D] = collection.immutable.Map[A, D]

    def ele(deneme: T => İkil): Col = d.filter(deneme)
    def eleDeğilse(deneme: T => İkil): Col = d.filterNot(deneme)
    def işle[A](işlev: T => A): C2[A] = d.map(işlev)
    def düzİşle[A](işlev: T => C2[A]): C2[A] = d.flatMap(işlev)
    def herbiriİçin[S](işlev: T => S): Birim = d.foreach(işlev)

    def dizine = d.toList
    def diziye = d.toSeq
    def kümeye = d.toSet
    def yöneye = d.toVector
    def dizime[S >: T](implicit delil: scala.reflect.ClassTag[S]): Dizim[S] = new Dizim(d.toArray(delil))
    def eşleğe[A, D](implicit delil: T <:< (A, D)): Eşlek[A, D] = d.toMap
    def eşleme[A, D](implicit delil: T <:< (A, D)): Eşlem[A, D] = Eşlem.değişmezden(d.toMap)
    def ikile[S](öbürü: scala.collection.IterableOnce[S]) = d.zip(öbürü)
    def ikileSırayla = d.zipWithIndex
    def ikileKonumla = d.zipWithIndex
    def öbekle[A](iş: (T) => A): Eşlek[A, Col] = d.groupBy(iş)
    // more to come
  }

  // Needed for tangle++ (tangle-trk.kojo in kojo-denemeler) due to call to Set.subsets
  implicit class IteratorMethods[T](d: Yineleyici[T]) { // T is a Collection
    type Col = Yineleyici[T]
    type C2[A] = Yineleyici[A]

    def işle[B](işlev: T => B): C2[B] = d.map(işlev)
    def herbiriİçin[B](işlev: T => B): Birim = d.foreach(işlev)
  }

}
