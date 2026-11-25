/*
 * Copyright (C) 2026
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

/**
 * Yineleyici (Iterator) -- ögeleri BİR KEZ, baştan sona gezdiren şey.
 *
 * Neden gerekliydi: öbekli, kayarÖbekli, kombinasyonlar, permütasyonlar,
 * kuyruklar, önler... hepsi Yineleyici döndürüyor. Yineleyicinin Türkçesi
 * olmadığı için öğrenci tam orada İngilizce duvara çarpıyordu:
 *   Dizi(1, 2, 3, 4).öbekli(2).toList   <- toList İngilizce
 * Artık:
 *   Dizi(1, 2, 3, 4).öbekli(2).dizine
 *
 * ÖNEMLİ: Yineleyici tek kullanımlıktır. Buradaki yöntemlerin çoğu onu
 * TÜKETİR; aynı yineleyiciyi ikinci kez gezemezsin. İki kez gerekiyorsa
 * ikizYap ile çoğalt ya da önce dizine/diziye ile bir topluluğa çevir.
 */
trait YineleyiciYöntemleri {
  implicit class YineleyiciYöntem[T](y: Yineleyici[T]) {
    type Belki[B] = Option[B]
    type Eşlek[A, D] = collection.immutable.Map[A, D]

    // --- çekirdek: elle gezmek ------------------------------------------
    // döngü (y.dahaVarMı) { satıryaz(y.sıradaki) }
    def dahaVarMı: İkil = y.hasNext
    def sıradaki: T = y.next()

    // --- eleme ve işleme (yineleyici TÜKENİR) ----------------------------
    def ele(deneme: T => İkil): Yineleyici[T] = y.filter(deneme)
    def eleDeğilse(deneme: T => İkil): Yineleyici[T] = y.filterNot(deneme)
    def işle[B](işlev: T => B): Yineleyici[B] = y.map(işlev)
    def düzİşle[B](işlev: T => YinelenebilirBirKere[B]): Yineleyici[B] = y.flatMap(işlev)
    def düzleştir[B](implicit delil: T => YinelenebilirBirKere[B]): Yineleyici[B] = y.flatten(delil)
    def seçİşle[B](işlev: PartialFunction[T, B]): Yineleyici[B] = y.collect(işlev)
    def seçİşleİlk[B](işlev: PartialFunction[T, B]): Belki[B] = y.collectFirst(işlev)
    def herbiriİçin[S](işlev: T => S): Birim = y.foreach(işlev)

    // --- arama ------------------------------------------------------------
    def bul(deneme: T => İkil): Belki[T] = y.find(deneme)
    def varMı(deneme: T => İkil): İkil = y.exists(deneme)
    def hepsiDoğruMu(deneme: T => İkil): İkil = y.forall(deneme)
    def hepsiİçinDoğruMu(deneme: T => İkil): İkil = y.forall(deneme)
    def say(deneme: T => İkil): Sayı = y.count(deneme)
    def içeriyorMu(öge: Her): İkil = y.contains(öge)
    def sırası[S >: T](öge: S): Sayı = y.indexOf(öge)
    def nerede(deneme: T => İkil): Sayı = y.indexWhere(deneme)
    def karşılıklıMı[S](öbürü: YinelenebilirBirKere[S])(deneme: (T, S) => İkil): İkil =
      y.corresponds(öbürü)(deneme)
    def gösterdikleriAynıMı[S >: T](öbürü: YinelenebilirBirKere[S]): İkil = y.sameElements(öbürü)

    // --- kesip biçme ------------------------------------------------------
    def al(kaçTane: Sayı): Yineleyici[T] = y.take(kaçTane)
    def alDoğruKaldıkça(deneme: T => İkil): Yineleyici[T] = y.takeWhile(deneme)
    def düşür(kaçTane: Sayı): Yineleyici[T] = y.drop(kaçTane)
    def düşürDoğruKaldıkça(deneme: T => İkil): Yineleyici[T] = y.dropWhile(deneme)
    def dilim(nereden: Sayı, nereye: Sayı): Yineleyici[T] = y.slice(nereden, nereye)
    def böl(deneme: T => İkil): (Yineleyici[T], Yineleyici[T]) = y.partition(deneme)
    def bölDoğruKaldıkça(deneme: T => İkil): (Yineleyici[T], Yineleyici[T]) = y.span(deneme)
    def öbekli(boy: Sayı): Yineleyici[Dizi[T]] = y.grouped(boy).map(_.toSeq)
    def kayarÖbekli(boy: Sayı): Yineleyici[Dizi[T]] = y.sliding(boy).map(_.toSeq)
    def kayarÖbekli(boy: Sayı, adım: Sayı): Yineleyici[Dizi[T]] = y.sliding(boy, adım).map(_.toSeq)
    def yinelemesiz: Yineleyici[T] = y.distinct
    def yinelemesizİşlevle[B](işlev: T => B): Yineleyici[T] = y.distinctBy(işlev)

    // --- birleştirme ------------------------------------------------------
    def bileşim[S >: T](öbürü: YinelenebilirBirKere[S]): Yineleyici[S] = y.concat(öbürü)
    def uzat[S >: T](boy: Sayı, öge: S): Yineleyici[S] = y.padTo(boy, öge)
    def yama[S >: T](nereden: Sayı, yenisi: Yineleyici[S], kaçTane: Sayı): Yineleyici[S] =
      y.patch(nereden, yenisi, kaçTane)
    def ikile[S](öbürü: YinelenebilirBirKere[S]): Yineleyici[(T, S)] = y.zip(öbürü)
    def ikileHepsini[S >: T, B](öbürü: Yineleyici[B], buDolgu: S, oDolgu: B): Yineleyici[(S, B)] =
      y.zipAll(öbürü, buDolgu, oDolgu)
    def ikileSırayla: Yineleyici[(T, Sayı)] = y.zipWithIndex

    // --- katlama ve indirgeme (tüketir) ----------------------------------
    def indirge[S >: T](işlem: (S, S) => S): S = y.reduce(işlem)
    def indirgeBelki[S >: T](işlem: (S, S) => S): Belki[S] = y.reduceOption(işlem)
    def indirgeSoldan[S >: T](işlem: (S, T) => S): S = y.reduceLeft(işlem)
    def indirgeSağdan[S >: T](işlem: (T, S) => S): S = y.reduceRight(işlem)
    def katla[S >: T](başlangıç: S)(işlem: (S, S) => S): S = y.fold(başlangıç)(işlem)
    def soldanKatla[B](başlangıç: B)(işlem: (B, T) => B): B = y.foldLeft(başlangıç)(işlem)
    def sağdanKatla[B](başlangıç: B)(işlem: (T, B) => B): B = y.foldRight(başlangıç)(işlem)
    def taraSoldan[B](başlangıç: B)(işlem: (B, T) => B): Yineleyici[B] = y.scanLeft(başlangıç)(işlem)
    def topla[S >: T](implicit sayısal: Numeric[S]): S = y.sum(sayısal)
    def çarp[S >: T](implicit sayısal: Numeric[S]): S = y.product(sayısal)
    def enUfağı[S >: T](implicit sıralama: Ordering[S]): T = y.min(sıralama)
    def enİrisi[S >: T](implicit sıralama: Ordering[S]): T = y.max(sıralama)
    def enUfağıBelki[S >: T](implicit sıralama: Ordering[S]): Belki[T] = y.minOption(sıralama)
    def enİrisiBelki[S >: T](implicit sıralama: Ordering[S]): Belki[T] = y.maxOption(sıralama)
    def enUfağı[B](iş: T => B)(implicit karşılaştırma: Ordering[B]): T = y.minBy(iş)(karşılaştırma)
    def enİrisi[B](iş: T => B)(implicit karşılaştırma: Ordering[B]): T = y.maxBy(iş)(karşılaştırma)
    def enUfağıBelki[B](iş: T => B)(implicit karşılaştırma: Ordering[B]): Belki[T] = y.minByOption(iş)(karşılaştırma)
    def enİrisiBelki[B](iş: T => B)(implicit karşılaştırma: Ordering[B]): Belki[T] = y.maxByOption(iş)(karşılaştırma)
    def indirgeSoldanBelki[S >: T](işlem: (S, T) => S): Belki[S] = y.reduceLeftOption(işlem)
    def indirgeSağdanBelki[S >: T](işlem: (T, S) => S): Belki[S] = y.reduceRightOption(işlem)
    // Yineleyici.scanRight kullanımdan kalktı; kendi gövdesi zaten buydu -- geri koyma.
    def taraSağdan[B](başlangıç: B)(işlem: (T, B) => B): Yineleyici[B] =
      collection.mutable.ArrayBuffer.from(y).scanRight(başlangıç)(işlem).iterator
    def bölYerinden(yeri: Sayı): (Yineleyici[T], Yineleyici[T]) = y.splitAt(yeri)
    // sıradaki'nin hata vermeyen biçimi (başıBelki/sonuBelki ile aynı kalıp)
    def sıradakiBelki: Belki[T] = y.nextOption()

    // --- boyut ve boşluk --------------------------------------------------
    // DİKKAT: boyu yineleyiciyi TÜKETİR -- saydıktan sonra elinde bir şey kalmaz.
    def boyu: Sayı = y.length
    def boşMu: İkil = y.isEmpty
    def doluMu: İkil = y.nonEmpty

    // --- çoğaltma ve önden bakma -----------------------------------------
    // ikizYap: aynı ögeleri gezen İKİ yineleyici verir (özgünü artık kullanma).
    def ikizYap: (Yineleyici[T], Yineleyici[T]) = y.duplicate
    // bellekli: tüketmeden önden bakabilmek için -- b.başı ilerletmez.
    def bellekli: collection.BufferedIterator[T] = y.buffered

    // --- topluluğa çevirme ------------------------------------------------
    def dizine: Dizin[T] = y.toList
    def diziye: Dizi[T] = y.toSeq
    def kümeye: Set[T] = y.toSet
    def yöneye: Vector[T] = y.toVector
    def dizime[S >: T](implicit delil: scala.reflect.ClassTag[S]): Dizim[S] = new Dizim(y.toArray(delil))
    def eşleğe[A, D](implicit delil: T <:< (A, D)): Eşlek[A, D] = y.toMap
    def yazıYap: Yazı = y.mkString
    def yazıYap(ara: Yazı): Yazı = y.mkString(ara)
    def yazıYap(başı: Yazı, ara: Yazı, sonu: Yazı): Yazı = y.mkString(başı, ara, sonu)
  }

  /**
   * bellekli'nin verdiği yineleyici. Yukarıdaki bütün yöntemler buna da
   * uygulanıyor (BufferedIterator bir Yineleyici'dir); tek eksik, TÜKETMEDEN
   * öne bakmayı sağlayan head idi.
   */
  implicit class BellekliYineleyiciYöntem[T](b: collection.BufferedIterator[T]) {
    // başı okumak yineleyiciyi İLERLETMEZ -- sıradaki'den farkı bu
    def başı: T = b.head
  }
}
