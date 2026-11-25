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
  type Pair = (A, D)
  // todo: duplicated most of the api in Eşlek
  type Belki[T] = Option[T]
  def eşli(a: A) = m.contains(a)
  def eşEkle(ikili: Pair) = m += ikili
  def +=(ikili: Pair) = this eşEkle ikili
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
  def indirge[B >: Pair](işlem: (B, B) => B): B = m.reduce(işlem)
  def indirgeSoldan[B >: Pair](işlem: (B, Pair) => B): B = m.reduceLeft(işlem)
  def indirgeSağdan[B >: Pair](işlem: (Pair, B) => B): B = m.reduceRight(işlem)
  def indirgeSoldanBelki[B >: Pair](işlem: (B, Pair) => B): Belki[B] = m.reduceLeftOption(işlem)
  def indirgeSağdanBelki[B >: Pair](işlem: (Pair, B) => B): Belki[B] = m.reduceRightOption(işlem)
  def katla[B >: Pair](z: B)(işlev: (B, B) => B): B = m.fold(z)(işlev)
  @deprecated("yazım hatasıydı: katla kullanın", "Eylül 2026")
  def kalta[B >: Pair](z: B)(işlev: (B, B) => B): B = katla(z)(işlev)
  def soldanKatla[B](z: B)(işlev: (B, Pair) => B): B = m.foldLeft(z)(işlev)
  def sağdanKatla[B](z: B)(işlev: (Pair, B) => B): B = m.foldRight(z)(işlev)

  def topla[T >: Pair](implicit num: scala.math.Numeric[T]) = m.sum(num) 
  def çarp[T >: Pair](implicit num: scala.math.Numeric[T]) = m.product(num)

  def yazıYap: Yazı = m.mkString
  def yazıYap(ara: Yazı): Yazı = m.mkString(ara)
  def yazıYap(başı: Yazı, ara: Yazı, sonu: Yazı): Yazı = m.mkString(başı, ara, sonu)
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
  def dizime[C >: Pair](implicit delil: scala.reflect.ClassTag[C]): Dizim[C] = new Dizim(m.toArray(delil))
  def say(işlev: (Pair) => İkil): Sayı = m.count(işlev)

  def ikile[C](öbürü: YinelenebilirBirKere[C]) = m.zip(öbürü)
  def ikileSırayla = m.zipWithIndex

  //
  def varsayılanDeğerle(d: D) = m.withDefaultValue(d: D)

  def enUfağı[B >: Pair](implicit sıralama: math.Ordering[B]): Pair = m.min(sıralama)
  // `B >: Pair` idi: `enUfağı(_._2)` çağrısında B = Any çıkıp Ordering bulunamıyordu.
  // enİrisi'nin işlevli biçimi zaten `[B]`; ikisi artık tutarlı.
  def enUfağı[B](iş: (Pair) => B)(implicit karşılaştırma: math.Ordering[B]): Pair = m.minBy(iş)(karşılaştırma)
  def enİrisi[B >: Pair](implicit sıralama: math.Ordering[B]): Pair = m.max(sıralama)
  def enİrisi[B](iş: (Pair) => B)(implicit karşılaştırma: math.Ordering[B]): Pair = m.maxBy(iş)(karşılaştırma)

  // --- uçlar, arama --------------------------------------------------
  // Eşlek/Eşlem bir İKİLİLER (anahtar -> değer) topluluğu; aşağıdaki
  // yöntemlerin "ögesi" bir ikili, yani Pair = (A, D).
  def başıBelki: Belki[Pair] = m.headOption
  def sonuBelki: Belki[Pair] = m.lastOption
  def bul(deneme: Pair => İkil): Belki[Pair] = m.find(deneme)

  // --- bölme, öbekleme -----------------------------------------------
  def böl(deneme: Pair => İkil) = m.partition(deneme)
  def bölİşle[A1, A2](işlev: Pair => Either[A1, A2]) = m.partitionMap(işlev)
  def bölDoğruKaldıkça(deneme: Pair => İkil) = m.span(deneme)
  def bölYerinden(yeri: Sayı) = m.splitAt(yeri)
  def öbekli(boy: Sayı) = m.grouped(boy)
  def kayarÖbekli(boy: Sayı) = m.sliding(boy)
  def kayarÖbekli(boy: Sayı, adım: Sayı) = m.sliding(boy, adım)
  def öbekleİşle[K, B](anahtar: Pair => K)(değer: Pair => B) = m.groupMap(anahtar)(değer)
  def öbekleİşleİndirge[K, B](anahtar: Pair => K)(değer: Pair => B)(indirge: (B, B) => B) =
    m.groupMapReduce(anahtar)(değer)(indirge)
  def kuyruklar = m.tails
  def önler = m.inits

  // --- indirgeme, tarama ----------------------------------------------
  def indirgeBelki[B >: Pair](işlem: (B, B) => B): Belki[B] = m.reduceOption(işlem)
  def tara[B >: Pair](z: B)(işlev: (B, B) => B) = m.scan(z)(işlev)
  def taraSoldan[B](z: B)(işlev: (B, Pair) => B) = m.scanLeft(z)(işlev)
  def taraSağdan[B](z: B)(işlev: (Pair, B) => B) = m.scanRight(z)(işlev)
  def enUfağıBelki[B >: Pair](implicit sıralama: math.Ordering[B]): Belki[Pair] = m.minOption(sıralama)
  def enUfağıBelki[B](iş: Pair => B)(implicit karşılaştırma: math.Ordering[B]): Belki[Pair] =
    m.minByOption(iş)(karşılaştırma)
  def enİrisiBelki[B >: Pair](implicit sıralama: math.Ordering[B]): Belki[Pair] = m.maxOption(sıralama)
  def enİrisiBelki[B](iş: Pair => B)(implicit karşılaştırma: math.Ordering[B]): Belki[Pair] =
    m.maxByOption(iş)(karşılaştırma)

  // --- seçme, dilimleme, ikili işlemler -------------------------------
  def seçİşle[B](işlev: PartialFunction[Pair, B]) = m.collect(işlev)
  def seçİşleİlk[B](işlev: PartialFunction[Pair, B]): Belki[B] = m.collectFirst(işlev)
  def dilim(nereden: Sayı, nereye: Sayı) = m.slice(nereden, nereye)
  def düzleştir[B](implicit delil: Pair => YinelenebilirBirKere[B]) = m.flatten(delil)
  def devrik[B](implicit delil: Pair => Yinelenebilir[B]) = m.transpose(delil)
  def ikiliyiAç[A1, A2](implicit delil: Pair => (A1, A2)) = m.unzip(delil)
  def ikileHepsini[B, S >: Pair](öbürü: Yinelenebilir[B], buDolgu: S, oDolgu: B) =
    m.zipAll(öbürü, buDolgu, oDolgu)

  // --- Eşlem'e özgü: YERİNDE değiştirenler ------------------------------
  // Eşlem DEĞİŞİR bir eşlem: bunlar eşlemin kendisini değiştirir.
  def koy(anahtar: A, değer: D): Belki[D] = m.put(anahtar, değer)
  def güncelle(anahtar: A, değer: D): Birim = m.update(anahtar, değer)
  def güncelleİşlevle(anahtar: A)(işlev: Belki[D] => Belki[D]): Belki[D] = m.updateWith(anahtar)(işlev)
  def alYoksaEkle(anahtar: A, değer: => D): D = m.getOrElseUpdate(anahtar, değer)
  def çıkar(anahtar: A): Belki[D] = m.remove(anahtar)
  def eleYerinde(deneme: ((A, D)) => İkil): this.type = { m.filterInPlace((a, d) => deneme((a, d))); this }
  def değerleriİşleYerinde(işlev: (A, D) => D): this.type = { m.mapValuesInPlace(işlev); this }
  def ekleHepsini(ikililer: YinelenebilirBirKere[(A, D)]): this.type = { m.addAll(ikililer); this }
  @deprecated("eylemle başlayan ada geçildi: ekleHepsini kullanın", "Eylül 2026")
  def hepsiniEkle(ikililer: YinelenebilirBirKere[(A, D)]): this.type = ekleHepsini(ikililer)
  def çıkarHepsini(anahtarlar: YinelenebilirBirKere[A]): this.type = { m.subtractAll(anahtarlar); this }
  def boşalt(): Birim = m.clear()

  // --- Eşlem'e özgü: değiştirmeyenler -----------------------------------
  def değerleriİşle[D2](işlev: D => D2): collection.immutable.Map[A, D2] = m.view.mapValues(işlev).toMap
  def anahtarlarıEle(deneme: A => İkil): collection.immutable.Map[A, D] = m.view.filterKeys(deneme).toMap
  def herİkiliİçin(komut: (A, D) => Birim): Birim = m.foreachEntry(komut)
  def anahtarYineleyici: Yineleyici[A] = m.keysIterator
  def değerYineleyici: Yineleyici[D] = m.valuesIterator
  def varsayılanı(anahtar: A): D = m.default(anahtar)
  def varsayılanlı(işlev: A => D): Eşlem[A, D] = Eşlem(m.withDefault(işlev))
  def eşleğe: collection.immutable.Map[A, D] = m.toMap
}

trait MapMethodsInTurkish {
  type Eşlek[A, D] = collection.immutable.Map[A, D]
  object Eşlek {
    def apply[A, D](elems: (A, D)*) = collection.immutable.Map.from(elems)
  }
  implicit class EşlekYöntemleri[A, D](m: Eşlek[A, D]) {
    // todo: copied most of the api from Eşlem above
    type Col = Eşlek[A, D]
    type Pair = (A, D)
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
    def işle[Col](işlev: ((A, D)) => Col) = m.map(işlev)
    // todo: Dizi[B] or Iterable?
    def düzİşle[B](işlev: ((A, D)) => collection.mutable.Iterable[B]) = m.flatMap(işlev)
    def indirge[B >: Pair](işlem: (B, B) => B): B = m.reduce(işlem)
    def indirgeSoldan[B >: Pair](işlem: (B, Pair) => B): B = m.reduceLeft(işlem)
    def indirgeSağdan[B >: Pair](işlem: (Pair, B) => B): B = m.reduceRight(işlem)
    def indirgeSoldanBelki[B >: Pair](işlem: (B, Pair) => B): Belki[B] = m.reduceLeftOption(işlem)
    def indirgeSağdanBelki[B >: Pair](işlem: (Pair, B) => B): Belki[B] = m.reduceRightOption(işlem)
    def katla[B >: Pair](z: B)(işlev: (B, B) => B): B = m.fold(z)(işlev)
    @deprecated("yazım hatasıydı: katla kullanın", "Eylül 2026")
    def kalta[B >: Pair](z: B)(işlev: (B, B) => B): B = katla(z)(işlev)
    def soldanKatla[B](z: B)(işlev: (B, Pair) => B): B = m.foldLeft(z)(işlev)
    def sağdanKatla[B](z: B)(işlev: (Pair, B) => B): B = m.foldRight(z)(işlev)

    def topla[T >: Pair](implicit num: scala.math.Numeric[T]) = m.sum(num)
    def çarp[T >: Pair](implicit num: scala.math.Numeric[T]) = m.product(num)

    def yazıYap: Yazı = m.mkString
    def yazıYap(ara: Yazı): Yazı = m.mkString(ara)
    def yazıYap(başı: Yazı, ara: Yazı, sonu: Yazı): Yazı = m.mkString(başı, ara, sonu)
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
    def dizime[C >: Pair](implicit delil: scala.reflect.ClassTag[C]): Dizim[C] = new Dizim(m.toArray(delil))
    def say(işlev: ((A, D)) => İkil): Sayı = m.count(işlev)

    def ikile[C](öbürü: YinelenebilirBirKere[C]) = m.zip(öbürü)
    def ikileSırayla = m.zipWithIndex

    //
    def varsayılanDeğerle(d: D) = m.withDefaultValue(d: D)
    // öbekle, bölükle, bölüklereAyır, parçala, gruplaştır
    def öbekle(iş: ((A, D)) => A): Eşlek[A, Eşlek[A, D]] = m.groupBy(iş)
    def değiştirilmiş[D1 >: D](a: A, d: D1): Eşlek[A, D1] = m.updated(a, d)

    def enUfağı[B >: Pair](implicit sıralama: math.Ordering[B]): Pair = m.min(sıralama)
    // `B >: Pair` idi: `enUfağı(_._2)` çağrısında B = Any çıkıp Ordering bulunamıyordu.
    // enİrisi'nin işlevli biçimi zaten `[B]`; ikisi artık tutarlı.
    def enUfağı[B](iş: (Pair) => B)(implicit karşılaştırma: math.Ordering[B]): Pair = m.minBy(iş)(karşılaştırma)
    def enİrisi[B >: Pair](implicit sıralama: math.Ordering[B]): Pair = m.max(sıralama)
    def enİrisi[B](iş: (Pair) => B)(implicit karşılaştırma: math.Ordering[B]): Pair = m.maxBy(iş)(karşılaştırma)
    // todo: more to come
  
    // --- uçlar, arama --------------------------------------------------
    // Eşlek/Eşlem bir İKİLİLER (anahtar -> değer) topluluğu; aşağıdaki
    // yöntemlerin "ögesi" bir ikili, yani Pair = (A, D).
    def başıBelki: Belki[Pair] = m.headOption
    def sonuBelki: Belki[Pair] = m.lastOption
    def bul(deneme: Pair => İkil): Belki[Pair] = m.find(deneme)

    // --- bölme, öbekleme -----------------------------------------------
    def böl(deneme: Pair => İkil) = m.partition(deneme)
    def bölİşle[A1, A2](işlev: Pair => Either[A1, A2]) = m.partitionMap(işlev)
    def bölDoğruKaldıkça(deneme: Pair => İkil) = m.span(deneme)
    def bölYerinden(yeri: Sayı) = m.splitAt(yeri)
    def öbekli(boy: Sayı) = m.grouped(boy)
    def kayarÖbekli(boy: Sayı) = m.sliding(boy)
    def kayarÖbekli(boy: Sayı, adım: Sayı) = m.sliding(boy, adım)
    def öbekleİşle[K, B](anahtar: Pair => K)(değer: Pair => B) = m.groupMap(anahtar)(değer)
    def öbekleİşleİndirge[K, B](anahtar: Pair => K)(değer: Pair => B)(indirge: (B, B) => B): Eşlek[K, B] =
      m.groupMapReduce(anahtar)(değer)(indirge)
    def kuyruklar = m.tails
    def önler = m.inits

    // --- indirgeme, tarama ----------------------------------------------
    def indirgeBelki[B >: Pair](işlem: (B, B) => B): Belki[B] = m.reduceOption(işlem)
    def tara[B >: Pair](z: B)(işlev: (B, B) => B) = m.scan(z)(işlev)
    def taraSoldan[B](z: B)(işlev: (B, Pair) => B) = m.scanLeft(z)(işlev)
    def taraSağdan[B](z: B)(işlev: (Pair, B) => B) = m.scanRight(z)(işlev)
    def enUfağıBelki[B >: Pair](implicit sıralama: math.Ordering[B]): Belki[Pair] = m.minOption(sıralama)
    def enUfağıBelki[B](iş: Pair => B)(implicit karşılaştırma: math.Ordering[B]): Belki[Pair] =
      m.minByOption(iş)(karşılaştırma)
    def enİrisiBelki[B >: Pair](implicit sıralama: math.Ordering[B]): Belki[Pair] = m.maxOption(sıralama)
    def enİrisiBelki[B](iş: Pair => B)(implicit karşılaştırma: math.Ordering[B]): Belki[Pair] =
      m.maxByOption(iş)(karşılaştırma)

    // --- seçme, dilimleme, ikili işlemler -------------------------------
    def seçİşle[B](işlev: PartialFunction[Pair, B]) = m.collect(işlev)
    def seçİşleİlk[B](işlev: PartialFunction[Pair, B]): Belki[B] = m.collectFirst(işlev)
    def dilim(nereden: Sayı, nereye: Sayı) = m.slice(nereden, nereye)
    def düzleştir[B](implicit delil: Pair => YinelenebilirBirKere[B]) = m.flatten(delil)
    def devrik[B](implicit delil: Pair => Yinelenebilir[B]) = m.transpose(delil)
    def ikiliyiAç[A1, A2](implicit delil: Pair => (A1, A2)) = m.unzip(delil)
    def ikileHepsini[B, S >: Pair](öbürü: Yinelenebilir[B], buDolgu: S, oDolgu: B) =
      m.zipAll(öbürü, buDolgu, oDolgu)

    // --- Eşlek'e özgü (hepsi YENİ eşlek verir, olanı değiştirmez) ---------
    def değiştirİşlevle(anahtar: A)(işlev: Belki[D] => Belki[D]): Col = m.updatedWith(anahtar)(işlev)
    def çıkarılmış(anahtar: A): Col = m.removed(anahtar)
    def hepsiÇıkarılmış(anahtarlar: YinelenebilirBirKere[A]): Col = m.removedAll(anahtarlar)
    def değerleriİşle[D2](işlev: D => D2): collection.immutable.Map[A, D2] = m.view.mapValues(işlev).toMap
    def anahtarlarıEle(deneme: A => İkil): Col = m.view.filterKeys(deneme).toMap
    def dönüştür[D2](işlev: (A, D) => D2): Eşlek[A, D2] = m.transform(işlev)
    def herİkiliİçin(komut: (A, D) => Birim): Birim = m.foreachEntry(komut)
    def anahtarYineleyici: Yineleyici[A] = m.keysIterator
    def değerYineleyici: Yineleyici[D] = m.valuesIterator
    def varsayılanı(anahtar: A): D = m.default(anahtar)
    def varsayılanlı(işlev: A => D): Eşlek[A, D] = m.withDefault(işlev)
    def eşleğe: collection.immutable.Map[A, D] = m.toMap
    def karşılıklıMı[S](öbürü: collection.Seq[S])(deneme: (Pair, S) => İkil): İkil = m.corresponds(öbürü)(deneme)
}
}
