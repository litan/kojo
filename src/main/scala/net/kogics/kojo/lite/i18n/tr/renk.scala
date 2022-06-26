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

import net.kogics.kojo.doodle.{Color => DColor}

trait ColorMethodsInTurkish {
  import java.awt.{Color => AColor}
  type Renk = AColor

  implicit class ColorYöntemleri(r: Renk) {
    // kojo/src/main/scala/net/kogics/kojo/doodle/Color.scala
    // ../../../doodle/Color.scala
    def çevir(açı: Kesir) = net.kogics.kojo.util.Utils.awtColorToDoodleColor(r).spin(açı)
  }

  implicit class ColorYöntemleri2(r: DColor) {
    // kojo/src/main/scala/net/kogics/kojo/doodle/Color.scala
    // ../../../doodle/Color.scala
    def çevir(açı: Kesir) = r.spin(açı)
  }

  object Renk {
    def apply(kırmızı: Sayı, yeşil: Sayı, mavi: Sayı, saydam: Sayı = 255): Renk = new AColor(kırmızı, yeşil, mavi, saydam)
    def apply(rgbHex: Sayı): Renk = new AColor(rgbHex, yanlış)
    def apply(rgbHex: Sayı, alfaDahilMi: İkil): Renk = new AColor(rgbHex, alfaDahilMi)

    def kym(kırmızı: Sayı, yeşil: Sayı, mavi: Sayı) = {
      DColor.rgb(kırmızı, yeşil, mavi)
    }
    def kyms(kırmızı: Sayı, yeşil: Sayı, mavi: Sayı, saydamlık: Sayı) = {
      DColor.rgba(kırmızı, yeşil, mavi, saydamlık)
    }
    def ada(arıRenk: Kesir, doygunluk: Kesir, açıklık: Kesir) = DColor.hsla(arıRenk, doygunluk, açıklık, 1.0)
    def adas(arıRenk: Kesir, doygunluk: Kesir, açıklık: Kesir, saydamlık: Kesir) = DColor.hsla(arıRenk, doygunluk, açıklık, saydamlık)
    def doğrusalDeğişim(x1: Kesir, y1: Kesir, renk1: Renk, x2: Kesir, y2: Kesir, renk2: Renk, dalgalıDevam: İkil = yanlış) = {
      DColor.linearGradient(x1, y1, renk1, x2, y2, renk2, dalgalıDevam)
    }
    // (x1: Double, y1: Double, x2: Double, y2: Double, distribution: collection.Seq[Double], colors: collection.Seq[AwtColor], cyclic: Boolean = false)
    def doğrusalÇokluDeğişim(x1: Kesir, y1: Kesir, x2: Kesir, y2: Kesir, dağılım: Dizi[Kesir], renkler: Dizi[Renk], dalgalıDevam: İkil = yanlış) = {
      DColor.linearMultipleGradient(x1, y1, x2, y2, dağılım, renkler, dalgalıDevam)
    }
    // (cx: Double, cy: Double, c1: java.awt.Color, radius: Double, c2: java.awt.Color, cyclic: Boolean):
    def merkezdenDışarıDoğruDeğişim(merkezX: Kesir, merkezY: Kesir, renk1: Renk, yarıçap: Kesir, renk2: Renk, dalgalıDevam: İkil) = {
      DColor.radialGradient(merkezX, merkezY, renk1, yarıçap, renk2, dalgalıDevam)
    }
    // (x: Double, y: Double, radius: Double, distribution: collection.Seq[Double], colors: collection.Seq[AwtColor], cyclic: Boolean = false)
    def merkezdenDışarıDoğruÇokluDeğişim(merkezX: Kesir, merkezY: Kesir, yarıçap: Kesir, dağılım: Dizi[Kesir], renkler: Dizi[Renk], dalgalıDevam: İkil = yanlış) = {
      DColor.radialMultipleGradient(merkezX, merkezY, yarıçap, dağılım, renkler, dalgalıDevam)
    }

  }
}
