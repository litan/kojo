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

import collection.mutable.{Stack, Queue, PriorityQueue, ArrayDeque}

// Yığın ARTIK BİR TÜR TAKMA ADI (Aralık ile aynı karar, Eylül 2026).
// Eskiden Stack'i saran bir case class'tı; o yüzden Stack'in bir collection.Seq
// olmasından gelen ~100 yöntemi (bul, böl, tara, enİrisi...) HİÇ görmüyordu.
// Artık görüyor: Diz sarmalayıcısı Yığın'a da uygulanıyor.
// Eski adların hepsi aşağıdaki örtük sınıfta korundu.
object Yığın {
  def boş[T]: Yığın[T] = Stack.empty[T]
  // Yığın(1, 2, 3) = "1'i it, 2'yi it, 3'ü it", yani TEPEDE 3 olur.
  // Scala'nın kendi Stack(1, 2, 3)'ü tepeye 1'i koyar (gösterimi tepeden
  // başlıyor); buradaki davranış baştan beri itme sırasıydı ve tür takma adına
  // geçerken korundu. ikojo da bu davranışa hizalandı.
  def apply[T](elemanlar: T*): Yığın[T] = { val y = Stack.empty[T]; y.pushAll(elemanlar); y }
  // Başka bir yığının kopyası, AYNI sırada (tepe yine tepede).
  // Eski gerçekleme y2.dizi'yi (tepeden başlayarak) yeniden itiyordu, yani
  // kopyayı TERSİNE çeviriyordu; eski test yalnız boyuta baktığı için
  // görülmemişti. Bu bir hata düzeltmesi.
  def doldur[T](y2: Yığın[T]): Yığın[T] = Stack.from(y2)
}

trait StackMethodsInTurkish {
  implicit class YığınYöntemleri[T](y: Yığın[T]) {
    // it/koy ve çek/al ikili adlar: kitapçıkta ikisi de geçiyor
    def it(öge: T): Yığın[T] = y.push(öge)
    def koy(öge: T): Yığın[T] = y.push(öge)
    def koyHepsini(dizi: YinelenebilirBirKere[T]): Yığın[T] = y.pushAll(dizi)
    def itHepsini(dizi: YinelenebilirBirKere[T]): Yığın[T] = y.pushAll(dizi)
    def çek(): T = y.pop()
    def al(): T = y.pop()
    def tepesi: T = y.top
    def tepe: T = y.top
    def tane: Sayı = y.size
    def dizi: Dizi[T] = y.toSeq
    def sil(): Birim = y.clear()
    type Col = Yığın[T]
    type Belki[B] = Option[B]

    // --- yığını toplu boşaltma (Eylül 2026 turu) ---------------------------
    // Adlandırma kuralı: EYLEMLE başla -- çekHepsini, ekleAraya, çıkarSondan.
    def çekHepsini: Diz[T] = y.popAll()                    // tepeden dibe
    def alHepsini: Dizi[T] = y.removeAll()                 // çekHepsini ile aynı
    def alHepsiniTersten: Dizi[T] = y.removeAllReverse()   // dipten tepeye
    def çekDoğruKaldıkça(deneme: T => İkil): Diz[T] = y.popWhile(deneme)
    def çekBelki: Belki[T] = y.removeHeadOption()
    // Yığında "son" = DİP. çıkarSondan tepedekini değil, en alttakini alır.
    def çıkarSondan(): T = y.removeLast()
    def çıkarSondanBelki: Belki[T] = y.removeLastOption()
    def sondanÇıkar(): T = y.removeLast()                  // çıkarSondan takma adı
    def sondanÇıkarBelki: Belki[T] = y.removeLastOption()  // çıkarSondanBelki takma adı
    def çıkarSondanDoğruKaldıkça(deneme: T => İkil): Dizi[T] = y.removeLastWhile(deneme)
    def çıkarİlkUyanı(deneme: T => İkil): Belki[T] = y.removeFirst(deneme)

    // --- yerinde değiştirenler ---------------------------------------------
    def eleYerinde(deneme: T => İkil): Col = { y.filterInPlace(deneme); y }
    def işleYerinde(işlev: T => T): Col = { y.mapInPlace(işlev); y }
    def düzİşleYerinde(işlev: T => YinelenebilirBirKere[T]): Col = { y.flatMapInPlace(işlev); y }
    def sıralıYerinde(implicit sıralama: Ordering[T]): Col = { y.sortInPlace()(sıralama); y }
    def sıralaYerinde[B](iş: T => B)(implicit sıralama: Ordering[B]): Col = { y.sortInPlaceBy(iş)(sıralama); y }
    def sırayaSokYerinde(önce: (T, T) => İkil): Col = { y.sortInPlaceWith(önce); y }
    def alYerinde(kaçTane: Sayı): Col = { y.takeInPlace(kaçTane); y }
    def alSağdanYerinde(kaçTane: Sayı): Col = { y.takeRightInPlace(kaçTane); y }
    def alDoğruKaldıkçaYerinde(deneme: T => İkil): Col = { y.takeWhileInPlace(deneme); y }
    def düşürYerinde(kaçTane: Sayı): Col = { y.dropInPlace(kaçTane); y }
    def düşürSağdanYerinde(kaçTane: Sayı): Col = { y.dropRightInPlace(kaçTane); y }
    def düşürDoğruKaldıkçaYerinde(deneme: T => İkil): Col = { y.dropWhileInPlace(deneme); y }
    def dilimYerinde(nereden: Sayı, nereye: Sayı): Col = { y.sliceInPlace(nereden, nereye); y }
    def uzatYerinde(boy: Sayı, öge: T): Col = { y.padToInPlace(boy, öge); y }
    def yamaYerinde(nereden: Sayı, yenisi: YinelenebilirBirKere[T], kaçTane: Sayı): Col = {
      y.patchInPlace(nereden, yenisi, kaçTane); y
    }

    // --- konumla erişim (0 = TEPE) -----------------------------------------
    def güncelle(yeri: Sayı, öge: T): Birim = y.update(yeri, öge)
    def ekleAraya(yeri: Sayı, öge: T): Birim = y.insert(yeri, öge)
    def ekleArayaHepsini(yeri: Sayı, ögeler: YinelenebilirBirKere[T]): Birim = y.insertAll(yeri, ögeler)
    def çıkar(yeri: Sayı): T = y.remove(yeri)
    def ekleHepsini(ögeler: YinelenebilirBirKere[T]): Col = { y.addAll(ögeler); y }
    def çıkarHepsini(ögeler: YinelenebilirBirKere[T]): Col = { y.subtractAll(ögeler); y }
    def dizime[S >: T](implicit delil: scala.reflect.ClassTag[S]): Dizim[S] = new Dizim(y.toArray(delil))

  }
}

trait QueueMethodsInTurkish {
  type ÖncelikSırası[T] = PriorityQueue[T]
  /**
   * `ÖncelikSırası` ile aynı tür -- "kuyruk" ailesiyle aynı sözcüğü yeğleyenler için.
   *
   * Tür takma adı TEK BAŞINA yetmiyor: `ÖncelikKuyruğu.boş[Sayı]` yazabilmek
   * için eşlik eden nesnenin de bir adı olmalı (aşağıdaki `val`), yoksa
   * "not found: value" hatası veriyor.
   */
  type ÖncelikKuyruğu[T] = PriorityQueue[T]
  /** C++'daki `deque`: iki ucundan da ekleme/çıkarma yapılan kuyruk. */
  type İkiUçluKuyruk[T] = ArrayDeque[T]
  object ÖncelikSırası {
    def apply[T](elems: T*)(implicit sıralama: Ordering[T]): ÖncelikSırası[T] = PriorityQueue.from(elems)(sıralama)
    def boş[T](implicit sıralama: Ordering[T]): ÖncelikSırası[T] = PriorityQueue.empty[T](sıralama)
  }

  /** `ÖncelikSırası` nesnesinin takma adı -- `ÖncelikKuyruğu.boş` / `ÖncelikKuyruğu(...)` için. */
  val ÖncelikKuyruğu = ÖncelikSırası

  implicit class mutPriQueMethods[T](d: PriorityQueue[T]) {
    type Belki[B] = Option[B]
    type Col = ÖncelikSırası[T]
    type C2[B] = ÖncelikSırası[B]
    type Eşlek[A, D] = collection.immutable.Map[A, D]
    type Dizi[B] = Seq[B]
    type Iter[A] = collection.mutable.Iterable[A]
    def ekle(öge: T) = d.addOne(öge)
    def koy(öge: T) = d.addOne(öge) // Yığın/Kuyruk ile ortak ad
    def ekle(ögeler: T*) = d.enqueue(ögeler: _*)
    def baştanAl(): T = d.dequeue()
    def baştanAlHepsini[T2 >: T]: Dizi[T2] = d.dequeueAll.toList // ArraySeq veriyordu: çıktıda DizikDizisi görünüyordu
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
    def yazıYap(başı: Yazı, ara: Yazı, sonu: Yazı): Yazı = d.mkString(başı, ara, sonu)
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
    def ikile[S](öbürü: YinelenebilirBirKere[S]) = d.zip(öbürü)
    def ikileSırayla = d.zipWithIndex
    def ikileKonumla = d.zipWithIndex
    def öbekle[A](iş: (T) => A): Eşlek[A, Col] = d.groupBy(iş)

    def enUfağı[B >: T](implicit sıralama: math.Ordering[B]): T = d.min(sıralama)
    def enUfağı[B](iş: (T) => B)(implicit karşılaştırma: math.Ordering[B]): T = d.minBy(iş)(karşılaştırma)
    def enİrisi[B >: T](implicit sıralama: math.Ordering[B]): T = d.max(sıralama)
    def enİrisi[B](iş: (T) => B)(implicit karşılaştırma: math.Ordering[B]): T = d.maxBy(iş)(karşılaştırma)

    // more to come

  
    // --- ortak çekirdek ---------------------------------------------------
    // NOT: Koleksiyon ÜRETEN yöntemler Dizi veriyor, ÖncelikSırası değil.
    // Sebep: yeni bir öncelik sırası kurmak örtük bir Ordering ister ve
    // imzaları gereksiz karmaşıklaştırırdı. Sıralı bir sonuç gerekiyorsa
    // ÖncelikSırası(...) ile açıkça yeniden kurulur.
    def başıBelki: Belki[T] = d.headOption
    def sonuBelki: Belki[T] = d.lastOption
    def bul(deneme: T => İkil): Belki[T] = d.find(deneme)
    def böl(deneme: T => İkil): (Dizi[T], Dizi[T]) = {
      val (e, h) = d.iterator.toSeq.partition(deneme); (e, h)
    }
    def bölDoğruKaldıkça(deneme: T => İkil): (Dizi[T], Dizi[T]) = d.iterator.toSeq.span(deneme)
    def bölYerinden(yeri: Sayı): (Dizi[T], Dizi[T]) = d.iterator.toSeq.splitAt(yeri)
    def öbekli(boy: Sayı): Yineleyici[Dizi[T]] = d.iterator.toSeq.grouped(boy)
    def kayarÖbekli(boy: Sayı): Yineleyici[Dizi[T]] = d.iterator.toSeq.sliding(boy)
    def öbekleİşle[K, B](anahtar: T => K)(değer: T => B): Eşlek[K, Dizi[B]] =
      d.iterator.toSeq.groupMap(anahtar)(değer)
    def öbekleİşleİndirge[K, B](anahtar: T => K)(değer: T => B)(indirge: (B, B) => B): Eşlek[K, B] =
      d.iterator.toSeq.groupMapReduce(anahtar)(değer)(indirge)
    def kuyruklar: Yineleyici[Dizi[T]] = d.iterator.toSeq.tails
    def önler: Yineleyici[Dizi[T]] = d.iterator.toSeq.inits
    def katla[S >: T](z: S)(işlev: (S, S) => S): S = d.fold(z)(işlev)
    def indirgeSoldan[S >: T](işlem: (S, T) => S): S = d.reduceLeft(işlem)
    def indirgeSağdan[S >: T](işlem: (T, S) => S): S = d.reduceRight(işlem)
    def indirgeBelki[S >: T](işlem: (S, S) => S): Belki[S] = d.reduceOption(işlem)
    def indirgeSoldanBelki[S >: T](işlem: (S, T) => S): Belki[S] = d.reduceLeftOption(işlem)
    def indirgeSağdanBelki[S >: T](işlem: (T, S) => S): Belki[S] = d.reduceRightOption(işlem)
    def tara[S >: T](z: S)(işlev: (S, S) => S): Dizi[S] = d.iterator.toSeq.scan(z)(işlev)
    def taraSoldan[B](z: B)(işlev: (B, T) => B): Dizi[B] = d.iterator.toSeq.scanLeft(z)(işlev)
    def taraSağdan[B](z: B)(işlev: (T, B) => B): Dizi[B] = d.iterator.toSeq.scanRight(z)(işlev)
    def enUfağıBelki[S >: T](implicit sıralama: math.Ordering[S]): Belki[T] = d.minOption(sıralama)
    def enUfağıBelki[B](iş: T => B)(implicit karşılaştırma: math.Ordering[B]): Belki[T] = d.minByOption(iş)(karşılaştırma)
    def enİrisiBelki[S >: T](implicit sıralama: math.Ordering[S]): Belki[T] = d.maxOption(sıralama)
    def enİrisiBelki[B](iş: T => B)(implicit karşılaştırma: math.Ordering[B]): Belki[T] = d.maxByOption(iş)(karşılaştırma)
    def seçİşle[B](işlev: PartialFunction[T, B]): Dizi[B] = d.iterator.toSeq.collect(işlev)
    def seçİşleİlk[B](işlev: PartialFunction[T, B]): Belki[B] = d.collectFirst(işlev)
    def düzleştir[B](implicit delil: T => YinelenebilirBirKere[B]): Dizi[B] = d.iterator.toSeq.flatten(delil)
    def devrik[B](implicit delil: T => Yinelenebilir[B]): Dizi[Dizi[B]] = d.iterator.toSeq.transpose(delil)
    def ikiliyiAç[A1, A2](implicit delil: T => (A1, A2)): (Dizi[A1], Dizi[A2]) = d.iterator.toSeq.unzip(delil)
    def ikileHepsini[B, S >: T](öbürü: Yinelenebilir[B], buDolgu: S, oDolgu: B): Dizi[(S, B)] =
      d.iterator.toSeq.zipAll(öbürü, buDolgu, oDolgu)

    // --- YERİNDE değiştirenler -------------------------------------------
    def işleYerinde(işlev: T => T): Col = { d.mapInPlace(işlev); d }
    def ekleHepsini(ögeler: YinelenebilirBirKere[T]): Col = { d.addAll(ögeler); d }
    // İMZA ikojo ile birebir aynı olmalı: varargs yapılsaydı `koyHepsini(Seq(9))`
    // orada derlenir burada derlenmezdi (T = Seq[Sayı] çıkarılırdı).
    def koyHepsini(ögeler: YinelenebilirBirKere[T]): Col = { d.addAll(ögeler); d }
    @deprecated("eylemle başlayan ada geçildi: ekleHepsini kullanın", "Eylül 2026")
    def hepsiniEkle(ögeler: YinelenebilirBirKere[T]): Col = ekleHepsini(ögeler)
    def kuyruğa: Kuyruk[T] = d.toQueue
}

  type Kuyruk[T] = Queue[T]
  object Kuyruk {
    def apply[T](elems: T*): Kuyruk[T] = Queue.from(elems)
    def boş[T]: Kuyruk[T] = Queue.empty[T] // Yığın.boş vardı, bunda yoktu
  }
  implicit class mutQueueMethods[T](d: Queue[T]) {
    type Belki[B] = Option[B]
    type Col = Kuyruk[T]
    type C2[T] = Kuyruk[T]
    type Eşlek[A, D] = collection.immutable.Map[A, D]
    type Dizi[B] = Seq[B]
    type Iter[A] = collection.mutable.Iterable[A]
    def ekle(öge: T) = d.addOne(öge)
    // ORTAK AD: `koy` üç yapıda da "içine bir öge koy" demek (Yığın'da push,
    // burada enqueue). Çıkarma için ortak ad KOYULMADI: buradaki `al(n)` zaten
    // take(n) -- öge çıkarmaz, kopya verir. `al()` eklemek o ikisini aynı ada
    // bindirirdi. Çıkarmanın adı `baştanAl`: hangi uçtan aldığını söylüyor.
    def koy(öge: T) = d.addOne(öge)
    def ekleHepsini(ögeler: Dizi[T]): Col = d.enqueueAll(ögeler)
    def koyHepsini(ögeler: Dizi[T]): Col = d.enqueueAll(ögeler)
    def baştanAl(): T = d.dequeue()
    def baştanAlHepsini(deneme: (T) => Boolean): Dizi[T] = d.dequeueAll(deneme).toList
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
    def yazıYap(başı: Yazı, ara: Yazı, sonu: Yazı): Yazı = d.mkString(başı, ara, sonu)
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
    def ikile[S](öbürü: YinelenebilirBirKere[S]) = d.zip(öbürü)
    def ikileSırayla = d.zipWithIndex
    def ikileKonumla = d.zipWithIndex
    def öbekle[A](iş: (T) => A): Eşlek[A, Col] = d.groupBy(iş)

    def enUfağı[B >: T](implicit sıralama: math.Ordering[B]): T = d.min(sıralama)
    def enUfağı[B](iş: (T) => B)(implicit karşılaştırma: math.Ordering[B]): T = d.minBy(iş)(karşılaştırma)
    def enİrisi[B >: T](implicit sıralama: math.Ordering[B]): T = d.max(sıralama)
    def enİrisi[B](iş: (T) => B)(implicit karşılaştırma: math.Ordering[B]): T = d.maxBy(iş)(karşılaştırma)

    // more to come
  
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
    def bölİşle[A1, A2](işlev: T => Either[A1, A2]): (Kuyruk[A1], Kuyruk[A2]) = d.partitionMap(işlev)
    def bölDoğruKaldıkça(deneme: T => İkil): (Col, Col) = d.span(deneme)
    def bölYerinden(yeri: Sayı): (Col, Col) = d.splitAt(yeri)
    def öbekli(boy: Sayı): Yineleyici[Col] = d.grouped(boy)
    def kayarÖbekli(boy: Sayı): Yineleyici[Col] = d.sliding(boy)
    def kayarÖbekli(boy: Sayı, adım: Sayı): Yineleyici[Col] = d.sliding(boy, adım)
    def öbekleİşle[K, B](anahtar: T => K)(değer: T => B): Eşlek[K, Kuyruk[B]] = d.groupMap(anahtar)(değer)
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
    def tara[S >: T](z: S)(işlev: (S, S) => S): Kuyruk[S] = d.scan(z)(işlev)
    def taraSoldan[B](z: B)(işlev: (B, T) => B): Kuyruk[B] = d.scanLeft(z)(işlev)
    def taraSağdan[B](z: B)(işlev: (T, B) => B): Kuyruk[B] = d.scanRight(z)(işlev)
    def enUfağıBelki[S >: T](implicit sıralama: math.Ordering[S]): Belki[T] = d.minOption(sıralama)
    def enUfağıBelki[B](iş: T => B)(implicit karşılaştırma: math.Ordering[B]): Belki[T] = d.minByOption(iş)(karşılaştırma)
    def enİrisiBelki[S >: T](implicit sıralama: math.Ordering[S]): Belki[T] = d.maxOption(sıralama)
    def enİrisiBelki[B](iş: T => B)(implicit karşılaştırma: math.Ordering[B]): Belki[T] = d.maxByOption(iş)(karşılaştırma)

    // --- ekleme, çıkarma -----------------------------------------------
    def sonunaEkle[S >: T](öge: S): Kuyruk[S] = d.appended(öge)
    def önüneEkle[S >: T](öge: S): Kuyruk[S] = d.prepended(öge)
    def sonunaEkleHepsini[S >: T](öbürü: YinelenebilirBirKere[S]): Kuyruk[S] = d.appendedAll(öbürü)
    def önüneEkleHepsini[S >: T](öbürü: YinelenebilirBirKere[S]): Kuyruk[S] = d.prependedAll(öbürü)
    def uzat[S >: T](boy: Sayı, öge: S): Kuyruk[S] = d.padTo(boy, öge)
    def yama[S >: T](nereden: Sayı, yenisi: YinelenebilirBirKere[S], kaçTane: Sayı): Kuyruk[S] =
      d.patch(nereden, yenisi, kaçTane)
    def fark[S >: T](öbürü: Diz[S]): Col = d.diff(öbürü)
    def kesişim[S >: T](öbürü: Diz[S]): Col = d.intersect(öbürü)
    def bileşim[S >: T](öbürü: Diz[S]): Kuyruk[S] = d.concat(öbürü)

    // --- seçme, düzleştirme, ikili işlemler ----------------------------
    def seçİşle[B](işlev: PartialFunction[T, B]): Kuyruk[B] = d.collect(işlev)
    def seçİşleİlk[B](işlev: PartialFunction[T, B]): Belki[B] = d.collectFirst(işlev)
    def düzleştir[B](implicit delil: T => YinelenebilirBirKere[B]): Kuyruk[B] = d.flatten(delil)
    def devrik[B](implicit delil: T => Yinelenebilir[B]): Kuyruk[Kuyruk[B]] = d.transpose(delil)
    def ikiliyiAç[A1, A2](implicit delil: T => (A1, A2)): (Kuyruk[A1], Kuyruk[A2]) = d.unzip(delil)
    def ikileHepsini[B, S >: T](öbürü: Yinelenebilir[B], buDolgu: S, oDolgu: B): Kuyruk[(S, B)] =
      d.zipAll(öbürü, buDolgu, oDolgu)
    def tersİşle[B](işlev: T => B): Kuyruk[B] = d.reverse.map(işlev)

    // --- YERİNDE değiştirenler -------------------------------------------
    // Kuyruğun KENDİSİNİ değiştirirler. Kuyruk mantığı: kuyruğaEkle sona
    // koyar, baştanÇıkar baştan alır (ilk giren ilk çıkar).
    def kuyruğaEkle(öge: T): Col = { d.enqueue(öge); d }
    def kuyruğaEkleHepsini(ögeler: YinelenebilirBirKere[T]): Col = { d.enqueueAll(ögeler); d }
    def baştanÇıkar(): T = d.dequeue()
    def baştanÇıkarBelki: Belki[T] = d.removeHeadOption()
    def baştanÇıkarDoğruKaldıkça(deneme: T => İkil): Diz[T] = d.removeHeadWhile(deneme)
    def baştanÇıkarKoşulla(deneme: T => İkil): Belki[T] = d.dequeueFirst(deneme)
    def baştanÇıkarHepsiniKoşulla(deneme: T => İkil): Diz[T] = d.dequeueWhile(deneme)
    def sondanÇıkar(): T = d.removeLast()
    def sondanÇıkarBelki: Belki[T] = d.removeLastOption()
    def ilki: T = d.front
    def eleYerinde(deneme: T => İkil): Col = { d.filterInPlace(deneme); d }
    def işleYerinde(işlev: T => T): Col = { d.mapInPlace(işlev); d }
    def sıralıYerinde(implicit sıralama: Ordering[T]): Col = { d.sortInPlace()(sıralama); d }
    def sıralaYerinde[B](iş: T => B)(implicit sıralama: Ordering[B]): Col = { d.sortInPlaceBy(iş)(sıralama); d }
    @deprecated("eylemle başlayan ada geçildi: ekleHepsini kullanın", "Eylül 2026")
    def hepsiniEkle(ögeler: YinelenebilirBirKere[T]): Col = ekleHepsini(ögeler)
    def ekleHepsini(ögeler: YinelenebilirBirKere[T]): Col = { d.addAll(ögeler); d }
    @deprecated("eylemle başlayan ada geçildi: ekleAraya kullanın", "Eylül 2026")
    def araEkle(yeri: Sayı, öge: T): Birim = ekleAraya(yeri, öge)
    def çıkar(yeri: Sayı): T = d.remove(yeri)
    def çıkarHepsini(ögeler: YinelenebilirBirKere[T]): Col = { d.subtractAll(ögeler); d }
    def boşalt(): Birim = d.clear()
    def sıralı(implicit sıralama: Ordering[T]): Diz[T] = d.sorted(sıralama)
    def sırala[B](iş: T => B)(implicit sıralama: Ordering[B]): Diz[T] = d.sortBy(iş)(sıralama)
    def sırayaSok(önce: (T, T) => İkil): Diz[T] = d.sortWith(önce)
    def içeriyorMu[S >: T](öge: S): İkil = d.contains(öge)
    def sırası[S >: T](öge: S): Sayı = d.indexOf(öge)
    def sırasıSondan[S >: T](öge: S): Sayı = d.lastIndexOf(öge)
    def yinelemesiz: Diz[T] = d.distinct
    def yinelemesizİşlevle[B](işlev: T => B): Diz[T] = d.distinctBy(işlev)
    // --- ArrayDeque'in yerinde değiştirenleri (Eylül 2026 turu) -----------
    // Adlandırma kuralı: EYLEMLE başla -- ekleAraya, alHepsini, çıkarSondan.
    def alYerinde(kaçTane: Sayı): Col = { d.takeInPlace(kaçTane); d }
    def alSağdanYerinde(kaçTane: Sayı): Col = { d.takeRightInPlace(kaçTane); d }
    def alDoğruKaldıkçaYerinde(deneme: T => İkil): Col = { d.takeWhileInPlace(deneme); d }
    def düşürYerinde(kaçTane: Sayı): Col = { d.dropInPlace(kaçTane); d }
    def düşürSağdanYerinde(kaçTane: Sayı): Col = { d.dropRightInPlace(kaçTane); d }
    def düşürDoğruKaldıkçaYerinde(deneme: T => İkil): Col = { d.dropWhileInPlace(deneme); d }
    def dilimYerinde(nereden: Sayı, nereye: Sayı): Col = { d.sliceInPlace(nereden, nereye); d }
    def uzatYerinde(boy: Sayı, öge: T): Col = { d.padToInPlace(boy, öge); d }
    def yamaYerinde(nereden: Sayı, yenisi: YinelenebilirBirKere[T], kaçTane: Sayı): Col = {
      d.patchInPlace(nereden, yenisi, kaçTane); d
    }
    def düzİşleYerinde(işlev: T => YinelenebilirBirKere[T]): Col = { d.flatMapInPlace(işlev); d }
    def sırayaSokYerinde(önce: (T, T) => İkil): Col = { d.sortInPlaceWith(önce); d }

    // --- konumla erişim, toplu çıkarma ------------------------------------
    def güncelle(yeri: Sayı, öge: T): Birim = d.update(yeri, öge)
    def ekleAraya(yeri: Sayı, öge: T): Birim = d.insert(yeri, öge)
    def ekleArayaHepsini(yeri: Sayı, ögeler: YinelenebilirBirKere[T]): Birim = d.insertAll(yeri, ögeler)
    def alHepsini: Dizi[T] = d.removeAll()
    def alHepsiniTersten: Dizi[T] = d.removeAllReverse()
    def çıkarİlkUyanı(deneme: T => İkil): Belki[T] = d.removeFirst(deneme)
    def çıkarSondanDoğruKaldıkça(deneme: T => İkil): Dizi[T] = d.removeLastWhile(deneme)
    // çıkarSondan/çıkarSondanBelki: sondanÇıkar ailesinin eylemle başlayan biçimi
    def çıkarSondan(): T = d.removeLast()
    def çıkarSondanBelki: Belki[T] = d.removeLastOption()

}

  /**
   * İki uçlu kuyruk (C++'daki `deque`, Scala'da `ArrayDeque`).
   *
   * İki ucundan da koyup iki ucundan da alabilirsin. Sırayı iki yönde gezmek
   * gereken işlerde (geri al/yinele, pencere kaydırma, iki uçlu arama)
   * kuyruk ile yığını ayrı ayrı tutmaktan kolay.
   *
   * ADLAR: `koy`/`ekle` sona koyar (Kuyruk ile aynı), `başaKoy` başa. Alma iki
   * uçlu olduğu için ortak `al` YOK -- `baştanAl`/`sondanAl` hangi uçtan
   * alındığını söylüyor.
   *
   * (Burada önce "Kuyruk yalnız baştan alır, Yığın yalnız tepeden; bu ikisini
   * birden yapar" yazıyordu. Aşağıdaki nota bakılırsa "yalnız" doğru değil.)
   *
   * ============ DİKKAT: BU YÖNTEMLER YIĞIN VE KUYRUĞA DA BULAŞIYOR ==========
   *
   * Scala 2.13'te `mutable.Stack` DE `mutable.Queue` DE `ArrayDeque`ten
   * türüyor. `İkiUçluKuyruk` bir tür TAKMA ADI olduğu için (sarmalayıcı sınıf
   * değil) aşağıdaki yöntemler `Yığın` ve `Kuyruk` değerlerinde de
   * kullanılabiliyor -- ve deque anlamıyla çalışıyorlar. Ölçüldü:
   *
   *   Yığın:  koy(1); koy(2)  -> tepe = 2          (doğru, push)
   *           başaKoy(0)      -> Dizi(0, 2, 1)
   *           sondanAl()      -> 1                 YIĞININ DİBİNDEN aldı
   *
   *   Kuyruk: koy(1); koy(2); başaKoy(0) -> başı = 0   KUYRUĞA KAYNAK yaptı
   *           sondanAl()      -> 2                 kuyruğun SONUNDAN aldı
   *
   * Yani `yığın.sondanAl()` LIFO'yu, `kuyruk.başaKoy(x)` FIFO'yu bozuyor ve
   * derleyici ses çıkarmıyor. Bu bir KAZA DEĞİL, bilinen bir bedel: tür takma
   * adı olmasaydı `İkiUçluKuyruk` bütün Diz/Yinelenebilir yöntemlerini
   * (ele, işle, bul...) kaybederdi. Sarmalayıcıya çevirmek sızıntıyı keser
   * ama o bedeli getirir.
   *
   * ORTAK ADLARDA sorun yok: `koy`, `başı`, `boyu` gibi adlarda daha ÖZEL olan
   * örtük sınıf kazanıyor (Stack <: ArrayDeque), yani `yığın.koy` push olarak
   * çalışıyor. Sızan şey yalnız İkiUçluKuyruk'a ÖZGÜ adlar.
   *
   * Bugünkü davranış `TurkishAPITest`te çivili -- yarın sessizce değişirse
   * kırmızı yanar.
   */
  object İkiUçluKuyruk {
    def apply[T](ögeler: T*): İkiUçluKuyruk[T] = ArrayDeque.from(ögeler)
    def boş[T]: İkiUçluKuyruk[T] = ArrayDeque.empty[T]
    def doldur[T](başkası: YinelenebilirBirKere[T]): İkiUçluKuyruk[T] = ArrayDeque.from(başkası)
  }

  implicit class İkiUçluKuyrukYöntemleri[T](d: İkiUçluKuyruk[T]) {
    type Col = İkiUçluKuyruk[T]
    type Belki[B] = Option[B]

    // --- koyma: iki uç ---
    def koy(öge: T): Col = d.append(öge)
    def ekle(öge: T): Col = d.append(öge)      // koy ile aynı
    def sonaKoy(öge: T): Col = d.append(öge)   // koy ile aynı, ucu açıkça söyler
    def başaKoy(öge: T): Col = d.prepend(öge)
    def koyHepsini(ögeler: YinelenebilirBirKere[T]): Col = { d.addAll(ögeler); d }
    def ekleHepsini(ögeler: YinelenebilirBirKere[T]): Col = { d.addAll(ögeler); d }

    // --- alma: iki uç (ortak `al` yok, bkz. sınıf notu) ---
    def baştanAl(): T = d.removeHead()
    def sondanAl(): T = d.removeLast()
    def baştanAlBelki: Belki[T] = d.removeHeadOption()
    def sondanAlBelki: Belki[T] = d.removeLastOption()

    // --- bakma (çıkarmadan) ---
    def başı: T = d.head
    def sonu: T = d.last
    def başıBelki: Belki[T] = d.headOption
    def sonuBelki: Belki[T] = d.lastOption

    // --- ölçü ve bakım ---
    def boyu: Sayı = d.length
    def tane: Sayı = d.size
    def boşMu: İkil = d.isEmpty
    def doluMu: İkil = d.nonEmpty
    def sil(): Birim = d.clear()
    def ikizle(): Col = d.clone()
    def dizi: Dizi[T] = d.toList
    def tersi: Col = ArrayDeque.from(d.reverse)
  }
}
