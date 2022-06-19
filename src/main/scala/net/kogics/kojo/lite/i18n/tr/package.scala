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

package net.kogics.kojo.lite.i18n

import net.kogics.kojo.core.{Turtle, UnitLen, Pixel, Cm, Inch, VertexShape, Rectangle}
import java.awt.{Color, Paint}
import java.awt.image.BufferedImage
import net.kogics.kojo.core.{Speed, Slow, Medium, Fast, SuperFast, Point}
import io.github.jdiemke.triangulation.Triangle2D
import net.kogics.kojo.lite.{CoreBuiltins, Builtins}

package object tr {
  var builtins: CoreBuiltins = _ //unstable reference to module
  lazy val richBuiltins = builtins.asInstanceOf[Builtins]

  // some type aliases in Turkish -- Ctrl-t to return type info will also be in turkish
  type Nesne = Object
  type Birim = Unit
  type Her   = Any
  type HerDeğer  = AnyVal
  type HerGönder = AnyRef
  type Yok   = Null
  val yok: Yok = null
  type Hiç   = Nothing

  type Renk = Color
  type Boya = Paint

  type Hız  = Speed
  type Nokta = Point
  type Dikdörtgen = Rectangle
  type Üçgen = Triangle2D

  // Ref: https://docs.scala-lang.org/overviews/scala-book/built-in-types.html
  type İkil = Boolean
  type Seçim = Boolean
  // no type for Bit. But if there were, how about Parçacık?

  // We have Byte/Short/Int/Long which all default to Int and BigInt
  // val n = 1
  // Sayma sayıları
  type Lokma   = Byte
  type Kısa    = Short
  type Sayı    = Int
  type Uzun    = Long
  type İriSayı = BigInt
  // We have Float/Double which default to Double and BigDecimal
  //   val x = 1.0
  // Kesirli sayılar
  type UfakKesir = Float
  type Kesir = Double
  type İriKesir = BigDecimal
  // Yazı
  // todo type Harf = Char
  type Yazı = String
  type EsnekYazı=collection.mutable.StringBuilder

  type Belki[T] = Option[T]
  type Biri[T] = Some[T]
  val Hiçbiri = None
  object Biri {
    def apply[T](elem: T): Belki[T] = Some(elem)
    def unapply[T](b: Belki[T]) = b match {
      case None    => Hiçbiri
      case Some(n) => Biri(n)
    }
  }

  type Dizi[B] = Seq[B]
  type DeğişkenDizi[B] = collection.Seq[B]
  type Dizin[A] = List[A]
  type MiskinDizin[C] = LazyList[C]

  type Yöney[T] = Vector[T]
  object Yöney {
    def apply[T](elemanlar: T*) = Vector.from(elemanlar)
    def unapplySeq[T](yler: Vector[T]) = Vector.unapplySeq(yler)
    def boş[T] = Vector.empty[T]
  }
  type Küme[T] = Set[T]
  object Küme {
    def apply[T](elemanlar: T*) = Set.from(elemanlar)
    def boş[T] = Set.empty[T]
  }

  // Used in Conway's game of life code in the tutorial
  type Sayılar = Vector[Sayı]
  type UzunlukBirimi = UnitLen

  // ../../../core/vertexShapeSupport.scala
  type GeoYol = java.awt.geom.GeneralPath
  type GeoNokta = VertexShape
  type Grafik2B = scala.swing.Graphics2D

  object Nokta {
    def apply(x: Kesir, y: Kesir) = new Point(x, y)
    def unapply(p: Nokta) = Some((p.x, p.y))
  }
  object Dizi {
    def apply[B](elems: B*): Seq[B] = Seq.from(elems)
    def unapplySeq[B](dizi: Seq[B]) = Seq.unapplySeq(dizi)
    def doldur[B](n1: Sayı)(f: Sayı => B) = Seq.tabulate(n1)(f)
    def doldur[B](n1: Sayı, n2: Sayı)(f: (Sayı, Sayı) => B) = Seq.tabulate(n1, n2)(f)
    def doldur[B](n1: Sayı, n2: Sayı, n3: Sayı)(f: (Sayı, Sayı, Sayı) => B) = Seq.tabulate(n1, n2, n3)(f)
    def doldur[B](n1: Sayı, n2: Sayı, n3: Sayı, n4: Sayı)(f: (Sayı, Sayı, Sayı, Sayı) => B) = Seq.tabulate(n1, n2, n3, n4)(f)
    def doldur[B](n1: Sayı, n2: Sayı, n3: Sayı, n4: Sayı, n5: Sayı)(f: (Sayı, Sayı, Sayı, Sayı, Sayı) => B) =
      Seq.tabulate(n1, n2, n3, n4, n5)(f)
  }
  object Dizin {
    def apply[A](elems: A*): List[A] = List.from(elems)
    def unapplySeq[A](list: List[A])  = List.unapplySeq(list)
  }
  object Sayılar {
    def apply(elemanlar: Sayı*): Sayılar = Vector.from(elemanlar)
    def unapplySeq(ss: Sayılar) = Vector.unapplySeq(ss)
  }
  object MiskinDizin {
    def sayalım(başlangıç: Sayı, kaçarKaçar: Sayı = 1) = LazyList.from(başlangıç, kaçarKaçar)
  }

  val (doğru, yanlış) = (true, false)
  val (yavaş, orta, hızlı, çokHızlı) = (Slow, Medium, Fast, SuperFast)
  val (noktaSayısı, santim, inç) = (Pixel, Cm, Inch)
  val Boş = collection.immutable.Nil

  type İmge = richBuiltins.Image // java.awt.Image
  type Bellekteİmge = BufferedImage
  type Bellekteİmgeİşlemi = java.awt.image.BufferedImageOp

  type İşlev1[D,R] = Function1[D,R]
  type İşlev2[D1,D2,R] = Function2[D1,D2,R]
  type İşlev3[D1,D2,D3,R] = Function3[D1,D2,D3,R]
  type Bölümselİşlev[D,R] = PartialFunction[D,R]

  class Mp3Çalar(p: net.kogics.kojo.music.KMp3) {
    def çalıyorMu = p.isMp3Playing
    def sesMp3üÇal(mp3dosyası: Yazı) = p.playMp3Sound(mp3dosyası)
    def çal(mp3dosyası: Yazı) = p.playMp3(mp3dosyası)
    def durdur() = p.stopMp3()
    
    def önyükle(mp3dosyası: Yazı) = p.preloadMp3(mp3dosyası)
    
    def döngülüÇal(mp3dosyası: Yazı) = p.playMp3Loop(mp3dosyası)
    def döngüyüDurdur() = p.stopMp3Loop()
  }
}
