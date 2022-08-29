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
  type HerGönder = AnyRef  // Gönderge, gönderme todo...
  type Yok   = Null
  val yok: Yok = null
  type Hiç   = Nothing

  type Boya = Paint
  type Renk = Color

  type Hız  = Speed
  type Nokta = Point
  type Dikdörtgen = Rectangle
  type Üçgen = Triangle2D

  // Ref: https://docs.scala-lang.org/overviews/scala-book/built-in-types.html
  type İkil = Boolean
  type Seçim = Boolean
  // no type for Bit. But if there were, how about Parçacık?

  // duplicated from sayi.scala as they are used in other traits
  type Lokma   = Byte
  type Kısa    = Short
  type Sayı    = Int
  type Uzun    = Long
  type İriSayı = BigInt
  type UfakKesir = Float
  type Kesir = Double
  type İriKesir = BigDecimal

  type Diz[B] = collection.Seq[B]
  type Dizi[B] = Seq[B]
  type Dizin[A] = List[A]
  type DiziSıralı[A] = IndexedSeq[A]
  type Yineleyici[Col] = Iterator[Col]

  type UzunlukBirimi = UnitLen

  // ../../../core/vertexShapeSupport.scala
  type GeoYol = java.awt.geom.GeneralPath
  type GeoNokta = VertexShape
  type Grafik2B = scala.swing.Graphics2D

  object Nokta {
    type Kesir = Double
    def apply(x: Kesir, y: Kesir) = new Point(x, y)
    def unapply(p: Nokta) = Some((p.x, p.y))
  }

  val (doğru, yanlış) = (true, false)
  val (yavaş, orta, hızlı, çokHızlı) = (Slow, Medium, Fast, SuperFast)
  val (noktaSayısı, santim, inç) = (Pixel, Cm, Inch)

  type İmge = richBuiltins.Image // java.awt.Image
  type Bellekteİmge = BufferedImage
  type Bellekteİmgeİşlemi = java.awt.image.BufferedImageOp

  type İşlev1[D,R] = Function1[D,R]
  type İşlev2[D1,D2,R] = Function2[D1,D2,R]
  type İşlev3[D1,D2,D3,R] = Function3[D1,D2,D3,R]

  type Yazı = String

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
