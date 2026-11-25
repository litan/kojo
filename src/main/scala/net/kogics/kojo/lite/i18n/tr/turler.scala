/*
 * Copyright (C) 2024
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

// import scala.language.implicitConversions

trait TürÇevirileri {
  // a private trait can't be instantiated!
  // type HerDeğerinEşliği = scala.AnyValCompanion
  type Uygulama = App
  type Eklenir = java.lang.Appendable
  type MatematikselHata = java.lang.ArithmeticException
  type Okİlişkisi[T] = ArrowAssoc[T]
  type KendiKapanır = java.lang.AutoCloseable

  type HarfDizisi = java.lang.CharSequence
  type Sınıf[T] = java.lang.Class[T]

  type JTemelSayı = java.lang.Number

  // todo: def sınıfı[T] = classOf[T]

  type KuraldışıSınıfDökümü = ClassCastException
  type DöngüselSınıfHatası = java.lang.ClassCircularityError
  type HatalıSınıfDüzeni = java.lang.ClassFormatError
  type SınıfYükleyici = java.lang.ClassLoader
  type SınıfBulunamadı = java.lang.ClassNotFoundException
  type SınıfDeğeri[T] = java.lang.ClassValue[T]

  // needed for methods of resim.scala used in ../trInit.scala
  type Bellekteİmge = java.awt.image.BufferedImage
  type Bellekteİmgeİşlemi = java.awt.image.BufferedImageOp
  type İmgeİşlemi = net.kogics.kojo.picture.ImageOp

  // use tuples, case classes or other structure types when more args seem to be needed
  type İşlev1[D, R] = Function1[D, R]
  type İşlev2[D1, D2, R] = Function2[D1, D2, R]
  type İşlev3[D1, D2, D3, R] = Function3[D1, D2, D3, R]

  // Various types dumped upon error in Kojo:
  type BelirtimHatası = java.lang.AssertionError
  type KuraldışıGirdiHatası = java.lang.IllegalArgumentException
  type EksikTanımHatası = scala.NotImplementedError
  type SınırDışınaTaşmaHatası = java.lang.IndexOutOfBoundsException
  type DizikSınırıDışınaTaşmaHatası = ArrayIndexOutOfBoundsException
  type DizikYüklemeHatası = java.lang.ArrayStoreException
  type BoşGöstergeHatası = java.lang.NullPointerException
  type İşParçacığıÖlümü = java.lang.ThreadDeath
  type ÖnyüklemeYöntemHatası = java.lang.BootstrapMethodError
  type BellekliYineleyici[T] = scala.collection.BufferedIterator[T]

  // types from scala.Predef
  // https://www.scala-lang.org/files/archive/api/2.13.9/scala/Predef$.html

  type Jİkil = java.lang.Boolean
  type JLokma = java.lang.Byte
  type JKesir = java.lang.Double
  type JUfakKesir = java.lang.Float
  type JSayı = Integer
  type JUzun = java.lang.Long
  type JKısa = java.lang.Short
  type JİriSayı = java.math.BigInteger

  def İkildenİkile(i: Jİkil): İkil = Boolean2boolean(i)
  def LokmadanLokmaya(l: JLokma): Lokma = Byte2byte(l)
  def UfakKesirdenUfakKesire(u: JUfakKesir): UfakKesir = Float2float(u)
  def KesirdenKesire(k: JKesir): Kesir = Double2double(k)
  def SayıdanSayıya(s: JSayı): Sayı = Integer2int(s)
  def UzundanUzuna(u: JUzun): Uzun = Long2long(u)
  def KısadanKısaya(k: JKısa): Kısa = Short2short(k)

  def ikildenİkile(i: İkil): Jİkil = boolean2Boolean(i)
  def lokmadanLokmaya(l: Lokma): JLokma = byte2Byte(l)
  def ufakKesirdenUfakKesire(u: UfakKesir): JUfakKesir = float2Float(u)
  def kesirdenKesire(k: Kesir): JKesir = double2Double(k)
  def sayıdanSayıya(s: Sayı): JSayı = int2Integer(s)
  def uzundanUzuna(u: Uzun): JUzun = long2Long(u)
  def kısadanKısaya(k: Kısa): JKısa = short2Short(k)

  // private type Dizik[T]=Array[T]
  private type SıralıDizi[T] = IndexedSeq[T]

  type DizikDizisi[T] = collection.mutable.ArraySeq[T]
  type İkilDiziğiDizisi = collection.mutable.ArraySeq.ofBoolean
  type LokmaDiziğiDizisi = collection.mutable.ArraySeq.ofByte
  type KesirDiziğiDizisi = collection.mutable.ArraySeq.ofDouble
  type UfakKesirDiziğiDizisi = collection.mutable.ArraySeq.ofFloat
  type SayıDiziğiDizisi = collection.mutable.ArraySeq.ofInt
  type UzunDiziğiDizisi = collection.mutable.ArraySeq.ofLong
  type GöndergeDiziğiDizisi[T <: HerGönder] = collection.mutable.ArraySeq.ofRef[T]
  type KısaDiziğiDizisi = collection.mutable.ArraySeq.ofShort
  type BirimDiziğiDizisi = collection.mutable.ArraySeq.ofUnit

  type Dizikİşlemleri[T] = collection.ArrayOps[T]
  type Varsılİkil = runtime.RichBoolean
  type VarsılLokma = runtime.RichByte
  type VarsılHarf = runtime.RichChar
  type VarsılKesir = runtime.RichDouble
  type VarsılUfakKesir = runtime.RichFloat
  type VarsılSayı = runtime.RichInt
  type VarsılUzun = runtime.RichLong
  type VarsılKısa = runtime.RichShort

/*
  implicit def genelDizikSargısı[T](xler: Dizik[T]): DizikDizisi[T] = genericWrapArray(xler)
  implicit def ikilDiziğiSargısı(xler: Dizik[İkil]): İkilDiziğiDizisi = wrapBooleanArray(xler)
  implicit def lokmaDiziğiSargısı(xler: Dizik[Lokma]): LokmaDiziğiDizisi = wrapByteArray(xler)
  implicit def harfDiziğiSargısı(xler: Dizik[Harf]): HarfDiziğiDizisi = wrapCharArray(xler)
  implicit def kesirDiziğiSargısı(xler: Dizik[Kesir]): KesirDiziğiDizisi = wrapDoubleArray(xler)
  implicit def ufakKesirDiziğiSargısı(xler: Dizik[UfakKesir]): UfakKesirDiziğiDizisi = wrapFloatArray(xler) 
  implicit def sayıDiziğiSargısı(xler: Dizik[Sayı]): SayıDiziğiDizisi = wrapIntArray(xler)
  implicit def uzunDiziğiSargısı(xler: Dizik[Uzun]): UzunDiziğiDizisi = wrapLongArray(xler)
  implicit def göndergeDiziğiSargısı[T <: HerGönder](xler: Dizik[T]): GöndergeDiziğiDizisi[T] =  wrapRefArray(xler)
  implicit def kısaDiziğiSargısı(xler: Dizik[Kısa]): KısaDiziğiDizisi = wrapShortArray(xler)
  implicit def birimDiziğiSargısı(xler: Dizik[Birim]): BirimDiziğiDizisi = wrapUnitArray(xler)
  */

}
