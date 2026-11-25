/*
 * Copyright (C) 2025
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

import net.kogics.kojo.lite.JoyStick // todo new type translation

import net.kogics.kojo.lite.i18n.tr.{Resim, Yöney2B}

trait Kumanda {
  type KumandaKolu = JoyStick
  
  implicit class KumandaYöntemleri(k: KumandaKolu) {
    def çiz(): Birim = k.draw()

    def yöney = Yöney2B(k.currentVector)

    def kondur(x: Kesir, y: Kesir) = k.setPosition(x, y)
    def konumuKur(x: Kesir, y: Kesir): Birim = k.setPosition(x, y)
    def oynat(oyuncu: Resim, hız: Kesir = 1.0): Birim = k.movePlayer(oyuncu.p, hız)
    def oynat(oyuncu: Resim, hız: Kesir, yönKısıtı: Yöney2B): Birim = k.movePlayer(oyuncu.p, hız, yönKısıtı.v)
    def oynatSahneİçinde(oyuncu: Resim, hız: Kesir = 1.0): Birim = k.movePlayerWithinStage(oyuncu.p, hız)
    def oynatSahneİçinde(oyuncu: Resim, hız: Kesir, yönKısıtı: Yöney2B): Birim =
      k.movePlayerWithinStage(oyuncu.p, hız, yönKısıtı.v)

    def çevreRenginiKur(r: Renk): Birim = k.setPerimeterColor(r)
    def çevreKalemRenginiKur(r: Renk): Birim = k.setPerimeterPenColor(r)
    def kolRenginiKur(r: Renk): Birim = k.setControlColor(r)
  }
}
