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
  type Harf = Char
  type Yazı = String

  type Belki[T] = Option[T]
  type Biri[T] = Some[T]
  val Hiçbiri = None
  object Biri {
    def apply[T](elem: T) = Some(elem)
    def unapply[T](b: Belki[T]) = b match {
      case None    => Hiçbiri
      case Some(n) => n
    }
  }

  type Dizi[B] = collection.Seq[B]
  type Dizin[A] = List[A]
  type MiskinDizin[C] = LazyList[C]

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
  }
  object Dizin {
    def apply[A](elems: A*): List[A] = List.from(elems)
  }
  object Sayılar {
    def apply(elemanlar: Sayı*): Sayılar = Vector.from(elemanlar)
  }
  object MiskinDizin {
    def sayalım(başlangıç: Sayı, kaçarKaçar: Sayı = 1) = LazyList.from(başlangıç, kaçarKaçar)
  }

  val (doğru, yanlış) = (true, false)
  val (yavaş, orta, hızlı, çokHızlı) = (Slow, Medium, Fast, SuperFast)
  val (noktaSayısı, santim, inç) = (Pixel, Cm, Inch)
  val Boş = scala.collection.immutable.Nil

  type ResimDosyası = richBuiltins.Image
}
