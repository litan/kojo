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

trait TuvalÇizimYöntemleri {
  type TuvalÇizim = net.kogics.kojo.lite.CanvasDraw

  // from ../../CanvasDraw.scala
  implicit class canvasDrawMethods(tç: TuvalÇizim) {
    type Yazıyüzü = java.awt.Font // duplicate in arayuz.scala
    def fırça(kırmızı: Sayı, yeşil: Sayı, mavi: Sayı, saydam: Sayı = 255): Birim = tç.stroke(kırmızı, yeşil, mavi, saydam)
    def fırça(r: Renk): Birim = tç.stroke(r)
    def fırçasız(): Birim = tç.noStroke()
    def fırçaAğırlığı(k: Kesir): Birim = tç.strokeWeight(k)
    def boya(kırmızı: Sayı, yeşil: Sayı, mavi: Sayı, saydam: Sayı = 255): Birim = tç.fill(kırmızı, yeşil, mavi, saydam)
    def boya(renk: Renk): Birim = tç.fill(renk)
    def boyasız(): Birim = tç.noFill()
    def artalan(kırmızı: Sayı, yeşil: Sayı, mavi: Sayı, saydam: Sayı = 255): Birim = tç.background(kırmızı, yeşil, mavi, saydam)
    def artalan(renk: Renk): Birim = tç.background(renk)

    def şekil(şekil: java.awt.Shape): Birim = tç.drawShape(şekil)
    def imge(imge: İmge, x: Sayı, y: Sayı): Birim = tç.image(imge, x, y)
    def elips(x: Kesir, y: Kesir, en: Kesir, boy: Kesir): Birim = tç.ellipse(x, y, en, boy)
    def yay(merkezX: Kesir, merkezY: Kesir, enYarıçapı: Kesir, boyYarıçapı: Kesir, başlangıçAçısı: Kesir, yayınAçısı: Kesir): Birim =
      tç.arc(merkezX, merkezY, enYarıçapı, boyYarıçapı, başlangıçAçısı, yayınAçısı)
    def nokta(x: Kesir, y: Kesir): Birim = tç.point(x, y)
    def çizgi(x1: Kesir, y1: Kesir, x2: Kesir, y2: Kesir): Birim = tç.line(x1, y1, x2, y2)
    def dikdörtgen(x1: Kesir, y1: Kesir, x2: Kesir, y2: Kesir): Birim = tç.rect(x1, y1, x2, y2)
    def yazıYüzü(yy: Yazıyüzü): Birim = tç.textFont(yy)
    def yazı(yazı: Yazı, x: Kesir, y: Kesir): Birim = tç.text(yazı, x, y)
  }
}
