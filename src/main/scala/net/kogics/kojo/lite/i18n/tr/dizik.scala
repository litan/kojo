/*
 * Copyright (C) 2025
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

trait DizikYöntemleri {
  import scala.reflect.ClassTag

  type Dizik[T] = Array[T]
  object Dizik {
    def apply[T](ögeler: T*)(implicit arg: ClassTag[T]) = Array(ögeler: _*)
    def boş[T](implicit arg: ClassTag[T]) = Array.empty
    def boş[T: ClassTag](b1: Sayı) = Array.ofDim[T](b1)
    def boş[T: ClassTag](b1: Sayı, b2: Sayı) = Array.ofDim[T](b1, b2)
    def boş[T: ClassTag](b1: Sayı, b2: Sayı, b3: Sayı) = Array.ofDim[T](b1, b2, b3)
    def doldur[T: ClassTag](b1: Sayı)(e: => T) = Array.fill[T](b1)(e)
    def doldur[T: ClassTag](b1: Sayı, b2: Sayı)(e: => T) = Array.fill[T](b1, b2)(e)
    def doldur[T: ClassTag](b1: Sayı, b2: Sayı, b3: Sayı)(e: => T) = Array.fill[T](b1, b2, b3)(e)
  }
  // todo: copied from dizi.scala
  implicit class ArrayMethods[T](d: Dizik[T]) {
    type Belki[B] = Option[B]
    type Col = Dizik[T]
    type Eşlek[A, D] = collection.immutable.Map[A, D]
    def başı: T = d.head
    def kuyruğu: Col = d.tail
    def önü: Col = d.init
    def sonu: T = d.last
    def boyu: Sayı = d.length
    def boşMu: İkil = d.isEmpty
    def doluMu: İkil = d.nonEmpty
    def ele(deneme: T => İkil): Col = d.filter(deneme)
    def eleDeğilse(deneme: T => İkil): Col = d.filterNot(deneme)
    // https://www.scala-lang.org/api/2.13.x/scala/Array.html
    // map[B](f: (T) => B)(implicit ct: ClassTag[B]): Dizik[B]
    // Builds a new array by applying a function to all elements of this array.
    def işle[A](işlev: T => A)(implicit ct: ClassTag[A]): Dizik[A] = d.map(işlev)(ct)
    def işleYerinde(işlev: (T) => T): Dizik[T] = d.mapInPlace(işlev)
    def düzİşle[A: ClassTag](işlev: T => Dizik[A]): Dizik[A] = d.flatMap(işlev)
    def sıralı(implicit ord: Ordering[T]): Col = d.sorted(ord)
    def sırala[A](i: T => A)(implicit ord: Ordering[A]): Col = d.sortBy(i)
    def sırayaSok(önce: (T, T) => İkil): Col = d.sortWith(önce)
    def indirge[B >: T](işlem: (B, B) => B): B = d.reduce(işlem)
    def soldanKatla[T2](z: T2)(işlev: (T2, T) => T2): T2 = d.foldLeft(z)(işlev)
    def sağdanKatla[T2](z: T2)(işlev: (T, T2) => T2): T2 = d.foldRight(z)(işlev)
    // https://github.com/scala/scala/blob/v2.12.7/src/library/scala/collection/TraversableOnce.scala#L1
    def topla[T2 >: T](implicit num: scala.math.Numeric[T2]) = d.sum(num) // foldLeft(num.zero)(num.plus)
    def çarp[T2 >: T](implicit num: scala.math.Numeric[T2]) = d.product(num) // foldLeft(num.one)(num.times)
    def yinelemesiz = d.distinct
    def yinelemesizİşlevle[T2](işlev: T => T2): Col = d.distinctBy(işlev)
    def yazıYap: Yazı = d.mkString
    def yazıYap(ara: Yazı): Yazı = d.mkString(ara)
    def yazıYap(başı: Yazı, ara: Yazı, sonu: Yazı): Yazı = d.mkString(başı, ara, sonu)
    def tersi = d.reverse
    def değiştir[S >: T: ClassTag](yeri: Sayı, değeri: S): Dizik[S] = d.updated(yeri, değeri)
    def değiştirYerinde(yeri: Sayı, değeri: T): Birim = d.update(yeri, değeri)
    def herbiriİçin[S](işlev: T => S): Birim = d.foreach(işlev)
    def varMı(deneme: T => İkil): İkil = d.exists(deneme)
    def hepsiDoğruMu(deneme: T => İkil): İkil = d.forall(deneme)
    def hepsiİçinDoğruMu(deneme: T => İkil): İkil = d.forall(deneme)
    // def içeriyorMu[S >: T](öge: S): İkil = d.contains(öge)
    def içeriyorMu(öge: T): İkil = d.contains(öge)
    def içeriyorMuDilim(dilim: Col): İkil = d.containsSlice(dilim)
    def al(n: Sayı): Col = d.take(n)
    def alDoğruKaldıkça(deneme: T => İkil): Col = d.takeWhile(deneme)
    def alSağdan(n: Sayı): Col = d.takeRight(n)
    def düşür(n: Sayı): Col = d.drop(n)
    def düşürDoğruKaldıkça(deneme: T => İkil): Col = d.dropWhile(deneme)
    def düşürSağdan(n: Sayı): Col = d.dropRight(n)
    // def sırası[S >: T](öge: S): Sayı = d.indexOf(öge)
    // def sırası[S >: T](öge: S, başlamaNoktası: Sayı): Sayı = d.indexOf(öge, başlamaNoktası)
    // def sırasıSondan[S >: T](öge: S): Sayı = d.lastIndexOf(öge)
    // def sırasıSondan[S >: T](öge: S, sonNokta: Sayı): Sayı = d.lastIndexOf(öge, sonNokta)
    def sırası(öge: T): Sayı = d.indexOf(öge)
    def sırası(öge: T, başlamaNoktası: Sayı): Sayı = d.indexOf(öge, başlamaNoktası)
    def sırasıSondan(öge: T): Sayı = d.lastIndexOf(öge)
    def sırasıSondan(öge: T, sonNokta: Sayı): Sayı = d.lastIndexOf(öge, sonNokta)

    def dizine = d.toList
    def diziye = d.toSeq
    def kümeye = d.toSet
    def yöneye = d.toVector
    def dizime[S >: T](implicit delil: scala.reflect.ClassTag[S]): Dizim[S] = new Dizim(d.toArray(delil))
    def eşleğe[A, D](implicit delil: T <:< (A, D)): Eşlek[A, D] = d.toMap
    def eşleme[A, D](implicit delil: T <:< (A, D)): Eşlem[A, D] = Eşlem.değişmezden(d.toMap)
    def say(işlev: T => İkil): Sayı = d.count(işlev)

    def dilim(nereden: Sayı, nereye: Sayı) = d.slice(nereden, nereye)
    def ikile[S](öbürü: YinelenebilirBirKere[S]) = d.zip(öbürü)
    def ikileSırayla = d.zipWithIndex
    def ikileKonumla = d.zipWithIndex
    def öbekle[A](iş: (T) => A): Eşlek[A, Col] = d.groupBy(iş)

    def enUfağı[B >: T](implicit sıralama: math.Ordering[B]): T = d.min(sıralama)
    def enUfağı[B](iş: (T) => B)(implicit karşılaştırma: math.Ordering[B]): T = d.minBy(iş)(karşılaştırma)
    def enİrisi[B >: T](implicit sıralama: math.Ordering[B]): T = d.max(sıralama)
    def enİrisi[B](iş: (T) => B)(implicit karşılaştırma: math.Ordering[B]): T = d.maxBy(iş)(karşılaştırma)

    // --- uçlar, arama --------------------------------------------------
    // Dizik = Array: yöntemleri ArrayOps'tan geliyor, o yüzden küme
    // ötekilerden biraz dar (ör. findLast, corresponds, indexOfSlice yok).
    // Yeni bir dizik ÜRETEN yöntemler ClassTag ister; onu ayrıca alıyoruz.
    def başıBelki: Belki[T] = d.headOption
    def sonuBelki: Belki[T] = d.lastOption
    def bul(deneme: T => İkil): Belki[T] = d.find(deneme)
    def nerede(deneme: T => İkil): Sayı = d.indexWhere(deneme)
    def nerede(deneme: T => İkil, başlamaNoktası: Sayı): Sayı = d.indexWhere(deneme, başlamaNoktası)
    def neredeSondan(deneme: T => İkil): Sayı = d.lastIndexWhere(deneme)
    def sıralar: Range = d.indices
    def başındaMı[S >: T](dizi: Dizik[S]): İkil = d.startsWith(dizi)
    def sonundaMı[S >: T](dizi: Dizik[S]): İkil = d.endsWith(dizi)

    // --- bölme, öbekleme -----------------------------------------------
    def böl(deneme: T => İkil)(implicit delil: ClassTag[T]): (Col, Col) = d.partition(deneme)
    def bölİşle[A1: ClassTag, A2: ClassTag](işlev: T => Either[A1, A2]): (Dizik[A1], Dizik[A2]) = d.partitionMap(işlev)
    def bölDoğruKaldıkça(deneme: T => İkil)(implicit delil: ClassTag[T]): (Col, Col) = d.span(deneme)
    def bölYerinden(yeri: Sayı)(implicit delil: ClassTag[T]): (Col, Col) = d.splitAt(yeri)
    def öbekli(boy: Sayı)(implicit delil: ClassTag[T]): Yineleyici[Col] = d.grouped(boy)
    def kayarÖbekli(boy: Sayı)(implicit delil: ClassTag[T]): Yineleyici[Col] = d.sliding(boy)
    def kayarÖbekli(boy: Sayı, adım: Sayı)(implicit delil: ClassTag[T]): Yineleyici[Col] = d.sliding(boy, adım)
    def öbekleİşle[K, B: ClassTag](anahtar: T => K)(değer: T => B): Eşlek[K, Dizik[B]] = d.groupMap(anahtar)(değer)
    def kombinasyonlar(ögeSayısı: Sayı)(implicit delil: ClassTag[T]): Yineleyici[Col] = d.combinations(ögeSayısı)
    def permütasyonlar(implicit delil: ClassTag[T]): Yineleyici[Col] = d.permutations
    def kuyruklar(implicit delil: ClassTag[T]): Yineleyici[Col] = d.tails
    def önler(implicit delil: ClassTag[T]): Yineleyici[Col] = d.inits

    // --- katlama, tarama -----------------------------------------------
    def katla[S >: T](z: S)(işlev: (S, S) => S): S = d.fold(z)(işlev)
    def tara[S >: T: ClassTag](z: S)(işlev: (S, S) => S): Dizik[S] = d.scan(z)(işlev)
    def taraSoldan[B: ClassTag](z: B)(işlev: (B, T) => B): Dizik[B] = d.scanLeft(z)(işlev)
    def taraSağdan[B: ClassTag](z: B)(işlev: (T, B) => B): Dizik[B] = d.scanRight(z)(işlev)

    // --- ekleme, çıkarma -----------------------------------------------
    def sonunaEkle[S >: T: ClassTag](öge: S): Dizik[S] = d.appended(öge)
    def önüneEkle[S >: T: ClassTag](öge: S): Dizik[S] = d.prepended(öge)
    def sonunaEkleHepsini[S >: T: ClassTag](öbürü: YinelenebilirBirKere[S]): Dizik[S] = d.appendedAll(öbürü)
    def önüneEkleHepsini[S >: T: ClassTag](öbürü: YinelenebilirBirKere[S]): Dizik[S] = d.prependedAll(öbürü)
    def uzat[S >: T: ClassTag](boy: Sayı, öge: S): Dizik[S] = d.padTo(boy, öge)
    def yama[S >: T: ClassTag](nereden: Sayı, yenisi: YinelenebilirBirKere[S], kaçTane: Sayı): Dizik[S] =
      d.patch(nereden, yenisi, kaçTane)
    def fark[S >: T](öbürü: Dizi[S])(implicit delil: ClassTag[T]): Col = d.diff(öbürü)
    def kesişim[S >: T](öbürü: Dizi[S])(implicit delil: ClassTag[T]): Col = d.intersect(öbürü)

    // --- seçme, düzleştirme, ikili işlemler ----------------------------
    def seçİşle[B: ClassTag](işlev: PartialFunction[T, B]): Dizik[B] = d.collect(işlev)
    def seçİşleİlk[B](işlev: PartialFunction[T, B]): Belki[B] = d.collectFirst(işlev)
    def düzleştir[B](implicit delil: T => YinelenebilirBirKere[B], delil2: ClassTag[B]): Dizik[B] = d.flatten(delil, delil2)
    def devrik[B](implicit delil: T => Dizik[B], delil2: ClassTag[B]): Dizik[Dizik[B]] = d.transpose(delil)
    def ikiliyiAç[A1, A2](implicit delil: T => (A1, A2), d1: ClassTag[A1], d2: ClassTag[A2]): (Dizik[A1], Dizik[A2]) =
      d.unzip(delil, d1, d2)
    def ikileHepsini[B, S >: T: ClassTag](öbürü: Yinelenebilir[B], buDolgu: S, oDolgu: B): Dizik[(S, B)] =
      d.zipAll(öbürü, buDolgu, oDolgu)
    def tersİşle[B: ClassTag](işlev: T => B)(implicit delil: ClassTag[T]): Dizik[B] = d.reverse.map(işlev)

    // todo: more to come
  }

}

trait EsnekDizikYöntemleri {
  import collection.mutable.ArrayBuffer

  type EsnekDizik[T] = ArrayBuffer[T]
  object EsnekDizik {
    def apply[T](ögeler: T*): EsnekDizik[T] = ArrayBuffer(ögeler: _*)
    def boş[T]: EsnekDizik[T] = ArrayBuffer.empty[T]
    def doldur[T](b1: Sayı)(e: => T): EsnekDizik[T] = ArrayBuffer.fill[T](b1)(e)
    def doldur[T](b1: Sayı, b2: Sayı)(e: => T): EsnekDizik[EsnekDizik[T]] = ArrayBuffer.fill[T](b1, b2)(e)
    def doldur[T](b1: Sayı, b2: Sayı, b3: Sayı)(e: => T): EsnekDizik[EsnekDizik[EsnekDizik[T]]] = ArrayBuffer.fill[T](b1, b2, b3)(e)
    def diziden[T](dizi: YinelenebilirBirKere[T]): EsnekDizik[T] = ArrayBuffer.from(dizi)
    def birleştir[T](diziler: Yinelenebilir[T]*): EsnekDizik[T] = ArrayBuffer.concat(diziler: _*)
    def yinele[T](ilk: T, uzunluk: Sayı)(işlev: (T) => T): EsnekDizik[T] = ArrayBuffer.iterate(ilk, uzunluk)(işlev);
    // todo: more to come, tabulate, unfold, unapplySeq
  }
  // todo: copied from dizi.scala
  implicit class ArrayBufferMethods[T](d: EsnekDizik[T]) {
    type Belki[B] = Option[B]
    type Col = EsnekDizik[T]
    type Eşlek[A, D] = collection.immutable.Map[A, D]
    def çıkar(yer: Sayı) = d.remove(yer)
    def ekle(öge: T) = d.append(öge)
    def başı: T = d.head
    def kuyruğu: Col = d.tail
    def önü: Col = d.init
    def sonu: T = d.last
    def boyu: Sayı = d.length
    def boşMu: İkil = d.isEmpty
    def doluMu: İkil = d.nonEmpty
    def ele(deneme: T => İkil): Col = d.filter(deneme)
    def eleDeğilse(deneme: T => İkil): Col = d.filterNot(deneme)
    // https://www.scala-lang.org/api/2.13.3/scala/collection/mutable/ArrayBuffer$.html
    // Builds a new array by applying a function to all elements of this array.
    def işle[A](işlev: T => A): EsnekDizik[A] = d.map(işlev)
    def işleYerinde(işlev: (T) => T): EsnekDizik[T] = d.mapInPlace(işlev)
    def düzİşle[A](işlev: (T) => YinelenebilirBirKere[A]): EsnekDizik[A] = d.flatMap(işlev)
    def sıralı(implicit ord: Ordering[T]): Col = d.sorted(ord)
    def sırala[A](i: T => A)(implicit ord: Ordering[A]): Col = d.sortBy(i)
    def sırayaSok(önce: (T, T) => İkil): Col = d.sortWith(önce)
    def indirge[B >: T](işlem: (B, B) => B): B = d.reduce(işlem)
    def soldanKatla[T2](z: T2)(işlev: (T2, T) => T2): T2 = d.foldLeft(z)(işlev)
    def sağdanKatla[T2](z: T2)(işlev: (T, T2) => T2): T2 = d.foldRight(z)(işlev)
    // https://github.com/scala/scala/blob/v2.12.7/src/library/scala/collection/TraversableOnce.scala#L1
    def topla[T2 >: T](implicit num: scala.math.Numeric[T2]) = d.sum(num) // foldLeft(num.zero)(num.plus)
    def çarp[T2 >: T](implicit num: scala.math.Numeric[T2]) = d.product(num) // foldLeft(num.one)(num.times)
    def yinelemesiz = d.distinct
    def yinelemesizİşlevle[T2](işlev: T => T2): Col = d.distinctBy(işlev)
    def yazıYap: Yazı = d.mkString
    def yazıYap(ara: Yazı): Yazı = d.mkString(ara)
    def yazıYap(başı: Yazı, ara: Yazı, sonu: Yazı): Yazı = d.mkString(başı, ara, sonu)
    def tersi = d.reverse
    def değiştir[S >: T](yeri: Sayı, değeri: S): EsnekDizik[S] = d.updated(yeri, değeri)
    def değiştirYerinde(yeri: Sayı, değeri: T): Birim = d.update(yeri, değeri)
    def herbiriİçin[S](işlev: T => S): Birim = d.foreach(işlev)
    def varMı(deneme: T => İkil): İkil = d.exists(deneme)
    def hepsiDoğruMu(deneme: T => İkil): İkil = d.forall(deneme)
    def hepsiİçinDoğruMu(deneme: T => İkil): İkil = d.forall(deneme)
    // def içeriyorMu[S >: T](öge: S): İkil = d.contains(öge)
    def içeriyorMu(öge: T): İkil = d.contains(öge)
    def içeriyorMuDilim(dilim: Col): İkil = d.containsSlice(dilim)
    def al(n: Sayı): Col = d.take(n)
    def alDoğruKaldıkça(deneme: T => İkil): Col = d.takeWhile(deneme)
    def alSağdan(n: Sayı): Col = d.takeRight(n)
    def düşür(n: Sayı): Col = d.drop(n)
    def düşürDoğruKaldıkça(deneme: T => İkil): Col = d.dropWhile(deneme)
    def düşürSağdan(n: Sayı): Col = d.dropRight(n)
    // def sırası[S >: T](öge: S): Sayı = d.indexOf(öge)
    // def sırası[S >: T](öge: S, başlamaNoktası: Sayı): Sayı = d.indexOf(öge, başlamaNoktası)
    // def sırasıSondan[S >: T](öge: S): Sayı = d.lastIndexOf(öge)
    // def sırasıSondan[S >: T](öge: S, sonNokta: Sayı): Sayı = d.lastIndexOf(öge, sonNokta)
    def sırası(öge: T): Sayı = d.indexOf(öge)
    def sırası(öge: T, başlamaNoktası: Sayı): Sayı = d.indexOf(öge, başlamaNoktası)
    def sırasıSondan(öge: T): Sayı = d.lastIndexOf(öge)
    def sırasıSondan(öge: T, sonNokta: Sayı): Sayı = d.lastIndexOf(öge, sonNokta)

    def dizine = d.toList
    def diziye = d.toSeq
    def kümeye = d.toSet
    def yöneye = d.toVector
    def dizime[S >: T](implicit delil: scala.reflect.ClassTag[S]): Dizim[S] = new Dizim(d.toArray(delil))
    def eşleğe[A, D](implicit delil: T <:< (A, D)): Eşlek[A, D] = d.toMap
    def eşleme[A, D](implicit delil: T <:< (A, D)): Eşlem[A, D] = Eşlem.değişmezden(d.toMap)
    def say(işlev: T => İkil): Sayı = d.count(işlev)

    def dilim(nereden: Sayı, nereye: Sayı) = d.slice(nereden, nereye)
    def ikile[S](öbürü: YinelenebilirBirKere[S]) = d.zip(öbürü)
    def ikileSırayla = d.zipWithIndex
    def ikileKonumla = d.zipWithIndex
    def öbekle[A](iş: (T) => A): Eşlek[A, Col] = d.groupBy(iş)

    def enUfağı[B >: T](implicit sıralama: math.Ordering[B]): T = d.min(sıralama)
    def enUfağı[B](iş: (T) => B)(implicit karşılaştırma: math.Ordering[B]): T = d.minBy(iş)(karşılaştırma)
    def enİrisi[B >: T](implicit sıralama: math.Ordering[B]): T = d.max(sıralama)
    def enİrisi[B](iş: (T) => B)(implicit karşılaştırma: math.Ordering[B]): T = d.maxBy(iş)(karşılaştırma)

    // --- uçlar, arama --------------------------------------------------
    def başıBelki: Belki[T] = d.headOption
    def sonuBelki: Belki[T] = d.lastOption
    def bul(deneme: T => İkil): Belki[T] = d.find(deneme)
    def bulSondan(deneme: T => İkil): Belki[T] = d.findLast(deneme)
    def nerede(deneme: T => İkil): Sayı = d.indexWhere(deneme)
    def nerede(deneme: T => İkil, başlamaNoktası: Sayı): Sayı = d.indexWhere(deneme, başlamaNoktası)
    def neredeSondan(deneme: T => İkil): Sayı = d.lastIndexWhere(deneme)
    def dilimSırası[S >: T](dilim: Diz[S]): Sayı = d.indexOfSlice(dilim)
    def dilimSırasıSondan[S >: T](dilim: Diz[S]): Sayı = d.lastIndexOfSlice(dilim)
    def sıralar: Range = d.indices
    def başındaMı[S >: T](dizi: Yinelenebilir[S]): İkil = d.startsWith(dizi)
    def sonundaMı[S >: T](dizi: Yinelenebilir[S]): İkil = d.endsWith(dizi)
    def karşılıklıMı[S](öbürü: Diz[S])(deneme: (T, S) => İkil): İkil = d.corresponds(öbürü)(deneme)

    // --- bölme, öbekleme -----------------------------------------------
    def böl(deneme: T => İkil): (Col, Col) = d.partition(deneme)
    def bölİşle[A1, A2](işlev: T => Either[A1, A2]): (EsnekDizik[A1], EsnekDizik[A2]) = d.partitionMap(işlev)
    def bölDoğruKaldıkça(deneme: T => İkil): (Col, Col) = d.span(deneme)
    def bölYerinden(yeri: Sayı): (Col, Col) = d.splitAt(yeri)
    def öbekli(boy: Sayı): Yineleyici[Col] = d.grouped(boy)
    def kayarÖbekli(boy: Sayı): Yineleyici[Col] = d.sliding(boy)
    def kayarÖbekli(boy: Sayı, adım: Sayı): Yineleyici[Col] = d.sliding(boy, adım)
    def öbekleİşle[K, B](anahtar: T => K)(değer: T => B): Eşlek[K, EsnekDizik[B]] = d.groupMap(anahtar)(değer)
    def öbekleİşleİndirge[K, B](anahtar: T => K)(değer: T => B)(indirge: (B, B) => B): Eşlek[K, B] =
      d.groupMapReduce(anahtar)(değer)(indirge)
    def kombinasyonlar(ögeSayısı: Sayı): Yineleyici[Col] = d.combinations(ögeSayısı)
    def permütasyonlar: Yineleyici[Col] = d.permutations
    def kuyruklar: Yineleyici[Col] = d.tails
    def önler: Yineleyici[Col] = d.inits

    // --- katlama, indirgeme, tarama ------------------------------------
    def katla[S >: T](z: S)(işlev: (S, S) => S): S = d.fold(z)(işlev)
    def indirgeSoldan[S >: T](işlem: (S, T) => S): S = d.reduceLeft(işlem)
    def indirgeSağdan[S >: T](işlem: (T, S) => S): S = d.reduceRight(işlem)
    def indirgeBelki[S >: T](işlem: (S, S) => S): Belki[S] = d.reduceOption(işlem)
    def indirgeSoldanBelki[S >: T](işlem: (S, T) => S): Belki[S] = d.reduceLeftOption(işlem)
    def indirgeSağdanBelki[S >: T](işlem: (T, S) => S): Belki[S] = d.reduceRightOption(işlem)
    // tara: katla gibi, ama ara sonuçların HEPSİNİ verir
    def tara[S >: T](z: S)(işlev: (S, S) => S): EsnekDizik[S] = d.scan(z)(işlev)
    def taraSoldan[B](z: B)(işlev: (B, T) => B): EsnekDizik[B] = d.scanLeft(z)(işlev)
    def taraSağdan[B](z: B)(işlev: (T, B) => B): EsnekDizik[B] = d.scanRight(z)(işlev)
    def enUfağıBelki[S >: T](implicit sıralama: math.Ordering[S]): Belki[T] = d.minOption(sıralama)
    def enUfağıBelki[B](iş: T => B)(implicit karşılaştırma: math.Ordering[B]): Belki[T] = d.minByOption(iş)(karşılaştırma)
    def enİrisiBelki[S >: T](implicit sıralama: math.Ordering[S]): Belki[T] = d.maxOption(sıralama)
    def enİrisiBelki[B](iş: T => B)(implicit karşılaştırma: math.Ordering[B]): Belki[T] = d.maxByOption(iş)(karşılaştırma)

    // --- ekleme, çıkarma -----------------------------------------------
    def sonunaEkle[S >: T](öge: S): EsnekDizik[S] = d.appended(öge)
    def önüneEkle[S >: T](öge: S): EsnekDizik[S] = d.prepended(öge)
    def sonunaEkleHepsini[S >: T](öbürü: YinelenebilirBirKere[S]): EsnekDizik[S] = d.appendedAll(öbürü)
    def önüneEkleHepsini[S >: T](öbürü: YinelenebilirBirKere[S]): EsnekDizik[S] = d.prependedAll(öbürü)
    def uzat[S >: T](boy: Sayı, öge: S): EsnekDizik[S] = d.padTo(boy, öge)
    def yama[S >: T](nereden: Sayı, yenisi: YinelenebilirBirKere[S], kaçTane: Sayı): EsnekDizik[S] =
      d.patch(nereden, yenisi, kaçTane)
    def fark[S >: T](öbürü: Diz[S]): Col = d.diff(öbürü)
    def kesişim[S >: T](öbürü: Diz[S]): Col = d.intersect(öbürü)
    def bileşim[S >: T](öbürü: Diz[S]): EsnekDizik[S] = d.concat(öbürü)

    // --- seçme, düzleştirme, ikili işlemler ----------------------------
    def seçİşle[B](işlev: PartialFunction[T, B]): EsnekDizik[B] = d.collect(işlev)
    def seçİşleİlk[B](işlev: PartialFunction[T, B]): Belki[B] = d.collectFirst(işlev)
    def düzleştir[B](implicit delil: T => YinelenebilirBirKere[B]): EsnekDizik[B] = d.flatten(delil)
    def devrik[B](implicit delil: T => Yinelenebilir[B]): EsnekDizik[EsnekDizik[B]] = d.transpose(delil)
    def ikiliyiAç[A1, A2](implicit delil: T => (A1, A2)): (EsnekDizik[A1], EsnekDizik[A2]) = d.unzip(delil)
    def ikileHepsini[B, S >: T](öbürü: Yinelenebilir[B], buDolgu: S, oDolgu: B): EsnekDizik[(S, B)] =
      d.zipAll(öbürü, buDolgu, oDolgu)
    def tersİşle[B](işlev: T => B): EsnekDizik[B] = d.reverse.map(işlev)

    // todo: more to come
  
    // --- YERİNDE değiştirenler -------------------------------------------
    // Bu yöntemler diziğin KENDİSİNİ değiştirir (yeni bir dizik vermez).
    // Değişmez karşılıkları yukarıda: ele/işle/sıralı/al/düşür/yama/uzat...
    def eleYerinde(deneme: T => İkil): Col = { d.filterInPlace(deneme); d }
    def düzİşleYerinde(işlev: T => YinelenebilirBirKere[T]): Col = { d.flatMapInPlace(işlev); d }
    def sıralıYerinde(implicit sıralama: Ordering[T]): Col = { d.sortInPlace()(sıralama); d }
    def sıralaYerinde[B](iş: T => B)(implicit sıralama: Ordering[B]): Col = { d.sortInPlaceBy(iş)(sıralama); d }
    def sırayaSokYerinde(önce: (T, T) => İkil): Col = { d.sortInPlaceWith(önce); d }
    def alYerinde(n: Sayı): Col = { d.takeInPlace(n); d }
    def alSağdanYerinde(n: Sayı): Col = { d.takeRightInPlace(n); d }
    def alDoğruKaldıkçaYerinde(deneme: T => İkil): Col = { d.takeWhileInPlace(deneme); d }
    def düşürYerinde(n: Sayı): Col = { d.dropInPlace(n); d }
    def düşürSağdanYerinde(n: Sayı): Col = { d.dropRightInPlace(n); d }
    def düşürDoğruKaldıkçaYerinde(deneme: T => İkil): Col = { d.dropWhileInPlace(deneme); d }
    def dilimYerinde(nereden: Sayı, nereye: Sayı): Col = { d.sliceInPlace(nereden, nereye); d }
    def yamaYerinde(nereden: Sayı, yenisi: YinelenebilirBirKere[T], kaçTane: Sayı): Col =
      { d.patchInPlace(nereden, yenisi.iterator.to(Seq), kaçTane); d }
    def uzatYerinde(boy: Sayı, öge: T): Col = { d.padToInPlace(boy, öge); d }

    // --- ekleme / çıkarma (hepsi yerinde) --------------------------------
    def ekleHepsini(ögeler: YinelenebilirBirKere[T]): Col = { d.addAll(ögeler); d }
    @deprecated("eylemle başlayan ada geçildi: ekleHepsini kullanın", "Eylül 2026")
    def hepsiniEkle(ögeler: YinelenebilirBirKere[T]): Col = ekleHepsini(ögeler)
    def ekleBaşa(öge: T): Col = { d.prepend(öge); d }
    def ekleBaşaHepsini(ögeler: YinelenebilirBirKere[T]): Col = { d.prependAll(ögeler); d }
    def ekleAraya(yeri: Sayı, öge: T): Birim = d.insert(yeri, öge)
    def ekleArayaHepsini(yeri: Sayı, ögeler: YinelenebilirBirKere[T]): Birim = d.insertAll(yeri, ögeler)
    @deprecated("eylemle başlayan ada geçildi: ekleBaşa kullanın", "Eylül 2026")
    def başaEkle(öge: T): Col = ekleBaşa(öge)
    @deprecated("eylemle başlayan ada geçildi: ekleBaşaHepsini kullanın", "Eylül 2026")
    def başaEkleHepsini(ögeler: YinelenebilirBirKere[T]): Col = ekleBaşaHepsini(ögeler)
    @deprecated("eylemle başlayan ada geçildi: ekleAraya kullanın", "Eylül 2026")
    def araEkle(yeri: Sayı, öge: T): Birim = ekleAraya(yeri, öge)
    @deprecated("eylemle başlayan ada geçildi: ekleArayaHepsini kullanın", "Eylül 2026")
    def araEkleHepsini(yeri: Sayı, ögeler: YinelenebilirBirKere[T]): Birim = ekleArayaHepsini(yeri, ögeler)
    def çıkar(yeri: Sayı, kaçTane: Sayı): Birim = d.remove(yeri, kaçTane)
    def çıkarÖgeyi(öge: T): Col = { d.subtractOne(öge); d }
    def çıkarHepsini(ögeler: YinelenebilirBirKere[T]): Col = { d.subtractAll(ögeler); d }
    def baştanKırp(kaçTane: Sayı): Birim = d.dropInPlace(kaçTane)
    def sondanKırp(kaçTane: Sayı): Birim = d.dropRightInPlace(kaçTane)
    def boşalt(): Birim = d.clear()
    def kopyası: EsnekDizik[T] = d.clone()
}

}
