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
  type SıralıDizi[T] = IndexedSeq[T]

  object Dizi {
    // Seq.from(ögeler) idi: varargs zaten bir ArraySeq olduğu için onu OLDUĞU GİBİ
    // döndürüyordu, yani Dizi(1,2,3) çıktıda DizikDizisi(1, 2, 3) görünüyordu.
    // Scala'nın kendi Seq(1,2,3)'ü List veriyor; artık biz de öyle. Eşitlik değişmedi.
    def apply[B](ögeler: B*): Dizi[B] = Seq(ögeler: _*)
    def boş[B]: Dizi[B] = Seq.empty[B]
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
    // collection.Seq.from(ögeler) idi -- Dizi ile aynı ArraySeq sorunu.
    def apply[B](ögeler: B*): Diz[B] = collection.Seq(ögeler: _*)
    def boş[B]: Diz[B] = collection.Seq.empty[B]
    def unapplySeq[B](dizi: Diz[B]) = collection.Seq.unapplySeq(dizi)
    def doldur[B](n1: Sayı)(f: Sayı => B) = collection.Seq.tabulate(n1)(f)
  }
  implicit class colSeqYöntemleri[T](d: Diz[T]) {
    type Belki[B] = Option[B]
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
    def yazıYap(baş: Yazı, ara: Yazı, sonu: Yazı): Yazı = d.mkString(baş, ara, sonu)
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

    // Yineleyici'ye giriş: sözlükte iterator -> yineleyici yazıyordu ama
    // hiçbir yerde açılmamıştı (bkz. yineleyici.scala)
    def yineleyici: Yineleyici[T] = d.iterator
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
    def bölİşle[A1, A2](işlev: T => Either[A1, A2]): (C2[A1], C2[A2]) = d.partitionMap(işlev)
    def bölDoğruKaldıkça(deneme: T => İkil): (Col, Col) = d.span(deneme)
    def bölYerinden(yeri: Sayı): (Col, Col) = d.splitAt(yeri)
    def öbekli(boy: Sayı): Yineleyici[Col] = d.grouped(boy)
    def kayarÖbekli(boy: Sayı): Yineleyici[Col] = d.sliding(boy)
    def kayarÖbekli(boy: Sayı, adım: Sayı): Yineleyici[Col] = d.sliding(boy, adım)
    def öbekleİşle[K, B](anahtar: T => K)(değer: T => B): Eşlek[K, C2[B]] = d.groupMap(anahtar)(değer)
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
    def tara[S >: T](z: S)(işlev: (S, S) => S): C2[S] = d.scan(z)(işlev)
    def taraSoldan[B](z: B)(işlev: (B, T) => B): C2[B] = d.scanLeft(z)(işlev)
    def taraSağdan[B](z: B)(işlev: (T, B) => B): C2[B] = d.scanRight(z)(işlev)
    def enUfağıBelki[S >: T](implicit sıralama: math.Ordering[S]): Belki[T] = d.minOption(sıralama)
    def enUfağıBelki[B](iş: T => B)(implicit karşılaştırma: math.Ordering[B]): Belki[T] = d.minByOption(iş)(karşılaştırma)
    def enİrisiBelki[S >: T](implicit sıralama: math.Ordering[S]): Belki[T] = d.maxOption(sıralama)
    def enİrisiBelki[B](iş: T => B)(implicit karşılaştırma: math.Ordering[B]): Belki[T] = d.maxByOption(iş)(karşılaştırma)

    // --- ekleme, çıkarma -----------------------------------------------
    def sonunaEkle[S >: T](öge: S): C2[S] = d.appended(öge)
    def önüneEkle[S >: T](öge: S): C2[S] = d.prepended(öge)
    def sonunaEkleHepsini[S >: T](öbürü: YinelenebilirBirKere[S]): C2[S] = d.appendedAll(öbürü)
    def önüneEkleHepsini[S >: T](öbürü: YinelenebilirBirKere[S]): C2[S] = d.prependedAll(öbürü)
    def uzat[S >: T](boy: Sayı, öge: S): C2[S] = d.padTo(boy, öge)
    def yama[S >: T](nereden: Sayı, yenisi: YinelenebilirBirKere[S], kaçTane: Sayı): C2[S] =
      d.patch(nereden, yenisi, kaçTane)
    def fark[S >: T](öbürü: Diz[S]): Col = d.diff(öbürü)
    def kesişim[S >: T](öbürü: Diz[S]): Col = d.intersect(öbürü)
    def bileşim[S >: T](öbürü: Diz[S]): C2[S] = d.concat(öbürü)

    // --- seçme, düzleştirme, ikili işlemler ----------------------------
    def seçİşle[B](işlev: PartialFunction[T, B]): C2[B] = d.collect(işlev)
    def seçİşleİlk[B](işlev: PartialFunction[T, B]): Belki[B] = d.collectFirst(işlev)
    def düzleştir[B](implicit delil: T => YinelenebilirBirKere[B]): C2[B] = d.flatten(delil)
    def devrik[B](implicit delil: T => Yinelenebilir[B]): C2[C2[B]] = d.transpose(delil)
    def ikiliyiAç[A1, A2](implicit delil: T => (A1, A2)): (C2[A1], C2[A2]) = d.unzip(delil)
    def ikileHepsini[B, S >: T](öbürü: Yinelenebilir[B], buDolgu: S, oDolgu: B): C2[(S, B)] =
      d.zipAll(öbürü, buDolgu, oDolgu)
    def tersİşle[B](işlev: T => B): C2[B] = d.reverse.map(işlev)


    // todo: more to come
  }

  // todo: duplicates in yazi.scala and dizin.scala and more
  implicit class SeqYöntemleri[T](d: Dizi[T]) {
    type Belki[B] = Option[B]
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
    def yazıYap(baş: Yazı, ara: Yazı, sonu: Yazı): Yazı = d.mkString(baş, ara, sonu)
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
    def bölİşle[A1, A2](işlev: T => Either[A1, A2]): (C2[A1], C2[A2]) = d.partitionMap(işlev)
    def bölDoğruKaldıkça(deneme: T => İkil): (Col, Col) = d.span(deneme)
    def bölYerinden(yeri: Sayı): (Col, Col) = d.splitAt(yeri)
    def öbekli(boy: Sayı): Yineleyici[Col] = d.grouped(boy)
    def kayarÖbekli(boy: Sayı): Yineleyici[Col] = d.sliding(boy)
    def kayarÖbekli(boy: Sayı, adım: Sayı): Yineleyici[Col] = d.sliding(boy, adım)
    def öbekleİşle[K, B](anahtar: T => K)(değer: T => B): Eşlek[K, C2[B]] = d.groupMap(anahtar)(değer)
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
    def tara[S >: T](z: S)(işlev: (S, S) => S): C2[S] = d.scan(z)(işlev)
    def taraSoldan[B](z: B)(işlev: (B, T) => B): C2[B] = d.scanLeft(z)(işlev)
    def taraSağdan[B](z: B)(işlev: (T, B) => B): C2[B] = d.scanRight(z)(işlev)
    def enUfağıBelki[S >: T](implicit sıralama: math.Ordering[S]): Belki[T] = d.minOption(sıralama)
    def enUfağıBelki[B](iş: T => B)(implicit karşılaştırma: math.Ordering[B]): Belki[T] = d.minByOption(iş)(karşılaştırma)
    def enİrisiBelki[S >: T](implicit sıralama: math.Ordering[S]): Belki[T] = d.maxOption(sıralama)
    def enİrisiBelki[B](iş: T => B)(implicit karşılaştırma: math.Ordering[B]): Belki[T] = d.maxByOption(iş)(karşılaştırma)

    // --- ekleme, çıkarma -----------------------------------------------
    def sonunaEkle[S >: T](öge: S): C2[S] = d.appended(öge)
    def önüneEkle[S >: T](öge: S): C2[S] = d.prepended(öge)
    def sonunaEkleHepsini[S >: T](öbürü: YinelenebilirBirKere[S]): C2[S] = d.appendedAll(öbürü)
    def önüneEkleHepsini[S >: T](öbürü: YinelenebilirBirKere[S]): C2[S] = d.prependedAll(öbürü)
    def uzat[S >: T](boy: Sayı, öge: S): C2[S] = d.padTo(boy, öge)
    def yama[S >: T](nereden: Sayı, yenisi: YinelenebilirBirKere[S], kaçTane: Sayı): C2[S] =
      d.patch(nereden, yenisi, kaçTane)
    def fark[S >: T](öbürü: Diz[S]): Col = d.diff(öbürü)
    def kesişim[S >: T](öbürü: Diz[S]): Col = d.intersect(öbürü)
    def bileşim[S >: T](öbürü: Diz[S]): C2[S] = d.concat(öbürü)

    // --- seçme, düzleştirme, ikili işlemler ----------------------------
    def seçİşle[B](işlev: PartialFunction[T, B]): C2[B] = d.collect(işlev)
    def seçİşleİlk[B](işlev: PartialFunction[T, B]): Belki[B] = d.collectFirst(işlev)
    def düzleştir[B](implicit delil: T => YinelenebilirBirKere[B]): C2[B] = d.flatten(delil)
    def devrik[B](implicit delil: T => Yinelenebilir[B]): C2[C2[B]] = d.transpose(delil)
    def ikiliyiAç[A1, A2](implicit delil: T => (A1, A2)): (C2[A1], C2[A2]) = d.unzip(delil)
    def ikileHepsini[B, S >: T](öbürü: Yinelenebilir[B], buDolgu: S, oDolgu: B): C2[(S, B)] =
      d.zipAll(öbürü, buDolgu, oDolgu)
    def tersİşle[B](işlev: T => B): C2[B] = d.reverse.map(işlev)


    // more to come
  }

  implicit class IndexedSeqYöntemleri[T](d: SıralıDizi[T]) { // used in alfabeta in othello
    type Belki[B] = Option[B]
    type Col = SıralıDizi[T]
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
    def topla[T2 >: T](implicit num: scala.math.Numeric[T2]) = d.sum(num)    // foldLeft(num.zero)(num.plus)
    def çarp[T2 >: T](implicit num: scala.math.Numeric[T2]) = d.product(num) // foldLeft(num.one)(num.times)
    def yinelemesiz = d.distinct
    def yinelemesizİşlevle[T2](işlev: T => T2): Col = d.distinctBy(işlev)
    def yazıYap: Yazı = d.mkString
    def yazıYap(ara: Yazı): Yazı = d.mkString(ara)
    def yazıYap(baş: Yazı, ara: Yazı, sonu: Yazı): Yazı = d.mkString(baş, ara, sonu)
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
    def bölİşle[A1, A2](işlev: T => Either[A1, A2]): (C2[A1], C2[A2]) = d.partitionMap(işlev)
    def bölDoğruKaldıkça(deneme: T => İkil): (Col, Col) = d.span(deneme)
    def bölYerinden(yeri: Sayı): (Col, Col) = d.splitAt(yeri)
    def öbekli(boy: Sayı): Yineleyici[Col] = d.grouped(boy)
    def kayarÖbekli(boy: Sayı): Yineleyici[Col] = d.sliding(boy)
    def kayarÖbekli(boy: Sayı, adım: Sayı): Yineleyici[Col] = d.sliding(boy, adım)
    def öbekleİşle[K, B](anahtar: T => K)(değer: T => B): Eşlek[K, C2[B]] = d.groupMap(anahtar)(değer)
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
    def tara[S >: T](z: S)(işlev: (S, S) => S): C2[S] = d.scan(z)(işlev)
    def taraSoldan[B](z: B)(işlev: (B, T) => B): C2[B] = d.scanLeft(z)(işlev)
    def taraSağdan[B](z: B)(işlev: (T, B) => B): C2[B] = d.scanRight(z)(işlev)
    def enUfağıBelki[S >: T](implicit sıralama: math.Ordering[S]): Belki[T] = d.minOption(sıralama)
    def enUfağıBelki[B](iş: T => B)(implicit karşılaştırma: math.Ordering[B]): Belki[T] = d.minByOption(iş)(karşılaştırma)
    def enİrisiBelki[S >: T](implicit sıralama: math.Ordering[S]): Belki[T] = d.maxOption(sıralama)
    def enİrisiBelki[B](iş: T => B)(implicit karşılaştırma: math.Ordering[B]): Belki[T] = d.maxByOption(iş)(karşılaştırma)

    // --- ekleme, çıkarma -----------------------------------------------
    def sonunaEkle[S >: T](öge: S): C2[S] = d.appended(öge)
    def önüneEkle[S >: T](öge: S): C2[S] = d.prepended(öge)
    def sonunaEkleHepsini[S >: T](öbürü: YinelenebilirBirKere[S]): C2[S] = d.appendedAll(öbürü)
    def önüneEkleHepsini[S >: T](öbürü: YinelenebilirBirKere[S]): C2[S] = d.prependedAll(öbürü)
    def uzat[S >: T](boy: Sayı, öge: S): C2[S] = d.padTo(boy, öge)
    def yama[S >: T](nereden: Sayı, yenisi: YinelenebilirBirKere[S], kaçTane: Sayı): C2[S] =
      d.patch(nereden, yenisi, kaçTane)
    def fark[S >: T](öbürü: Diz[S]): Col = d.diff(öbürü)
    def kesişim[S >: T](öbürü: Diz[S]): Col = d.intersect(öbürü)
    def bileşim[S >: T](öbürü: Diz[S]): C2[S] = d.concat(öbürü)

    // --- seçme, düzleştirme, ikili işlemler ----------------------------
    def seçİşle[B](işlev: PartialFunction[T, B]): C2[B] = d.collect(işlev)
    def seçİşleİlk[B](işlev: PartialFunction[T, B]): Belki[B] = d.collectFirst(işlev)
    def düzleştir[B](implicit delil: T => YinelenebilirBirKere[B]): C2[B] = d.flatten(delil)
    def devrik[B](implicit delil: T => Yinelenebilir[B]): C2[C2[B]] = d.transpose(delil)
    def ikiliyiAç[A1, A2](implicit delil: T => (A1, A2)): (C2[A1], C2[A2]) = d.unzip(delil)
    def ikileHepsini[B, S >: T](öbürü: Yinelenebilir[B], buDolgu: S, oDolgu: B): C2[(S, B)] =
      d.zipAll(öbürü, buDolgu, oDolgu)
    def tersİşle[B](işlev: T => B): C2[B] = d.reverse.map(işlev)


    // more to come
  }

  implicit class IterableMethods[T](d: Yinelenebilir[T]) {
    type Col = Yinelenebilir[T]
    type C2[B] = Yinelenebilir[B]
    type Eşlek[A, D] = Map[A, D]

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
    def ikile[S](öbürü: YinelenebilirBirKere[S]) = d.zip(öbürü)
    def ikileSırayla = d.zipWithIndex
    def ikileKonumla = d.zipWithIndex
    def öbekle[A](iş: (T) => A): Eşlek[A, Col] = d.groupBy(iş)
    // more to come
  }

  implicit class ImmutableIterableMethods[T](d: collection.immutable.Iterable[T]) {
    type Col = collection.immutable.Iterable[T]
    type C2[B] = collection.immutable.Iterable[B]
    type Eşlek[A, D] = collection.immutable.Map[A, D]
    type Küme[T] = Set[T]
    type Yöney[T] = Vector[T]

    def ele(deneme: T => İkil): Col = d.filter(deneme)
    def eleDeğilse(deneme: T => İkil): Col = d.filterNot(deneme)
    def işle[A](işlev: T => A): C2[A] = d.map(işlev)
    def düzİşle[A](işlev: T => C2[A]): C2[A] = d.flatMap(işlev)
    def herbiriİçin[S](işlev: T => S): Birim = d.foreach(işlev)

    def dizine: Dizin[T] = d.toList
    def diziye: Dizi[T] = d.toSeq
    def kümeye: Küme[T] = d.toSet
    def yöneye: Yöney[T] = d.toVector
    def dizime[S >: T](implicit delil: scala.reflect.ClassTag[S]): Dizim[S] = new Dizim(d.toArray(delil))
    def eşleğe[A, D](implicit delil: T <:< (A, D)): Eşlek[A, D] = d.toMap
    def eşleme[A, D](implicit delil: T <:< (A, D)): Eşlem[A, D] = Eşlem.değişmezden(d.toMap)
    def ikile[S](öbürü: YinelenebilirBirKere[S]): Yinelenebilir[(T, S)] = d.zip(öbürü)
    def ikileSırayla:Yinelenebilir[(T, Sayı)] = d.zipWithIndex
    def ikileKonumla:Yinelenebilir[(T, Sayı)] = d.zipWithIndex
    def öbekle[A](iş: (T) => A): Eşlek[A, Col] = d.groupBy(iş)
    // more to come
  }

  // IteratorMethods buradaydı (yalnız işle ve herbiriİçin; tangle-trk.kojo için
  // eklenmişti). yineleyici.scala'daki YineleyiciYöntem onun üst kümesi; ikisi
  // birden dursaydı Yineleyici üzerinde işle/herbiriİçin çağrısı ÇİFT ÖRTÜK
  // olurdu (ambiguous implicit). Bu yüzden burada tutulmuyor.

}
