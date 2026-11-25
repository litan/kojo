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

trait RenkYöntemleri {
  import java.awt.{ Color => AColor }
  type DeğişimliBoya = java.awt.GradientPaint
  type Renk = AColor
  type DRenk = DColor

  val DRenk = DColor

  val renkler = net.kogics.kojo.doodle.Color

  // this works, too, but, it is not good to mix turkish and english names
/*  implicit class renkler(r: DColor.type) {
    def beyazlatılmışBadem = r.blanchedAlmond
  } */

  // promote a select few to top namespace for ease of use
  val renksiz = Renkler.renksiz
  val siyah = Renkler.siyah
  val beyaz = Renkler.beyaz
  val açıkGri = Renkler.açıkGri
  val camgöbeği = Renkler.camgöbeği
  val gri = Renkler.gri
  val kahverengi = Renkler.kahverengi
  val koyuGri = Renkler.koyuGri
  val kırmızı = Renkler.kırmızı
  val mavi = Renkler.mavi
  val mor = Renkler.mor
  val morumsu = Renkler.morumsu
  val pembe = Renkler.pembe
  val sarı = Renkler.sarı
  val turuncu = Renkler.turuncu
  val yeşil = Renkler.yeşil

  object Renkler {
    // container for them all to make it easier to find

    val renksiz: Renk = net.kogics.kojo.staging.KColor.noColor
    val mavi: Renk = AColor.blue
    val kırmızı: Renk = AColor.red
    val sarı: Renk = AColor.yellow
    val yeşil: Renk = AColor.green
    val mor: Renk = new AColor(0x740f73)  // CoreBuiltins
    val pembe: Renk = AColor.pink
    val kahverengi: Renk = new AColor(0x583a0b) // CoreBuiltins
    val siyah: Renk = AColor.black
    val beyaz: Renk = AColor.white
    val gri: Renk = AColor.gray
    val koyuGri: Renk = AColor.darkGray
    val açıkGri: Renk = AColor.lightGray
    val turuncu: Renk = AColor.orange
    val morumsu: Renk = AColor.magenta
    val camgöbeği: Renk = AColor.cyan

    val (altınbaşak, altın, yeşilimsiSarı, zeytin, orkidePembesi, somon, 
      denizYeşili, kurşunMavisi, kurşunGrisi, turkuaz, menekşe, haki, mercan,
      gökMavisi, çelikMavisi, beyazlatılmışBadem) =
      (DRenk.goldenrod, DRenk.gold, DRenk.greenYellow, DRenk.olive, DRenk.orchid , DRenk.salmon, 
        DRenk.seaGreen , DRenk.slateBlue , DRenk.slateGray , DRenk.turquoise , DRenk.violet, DRenk.khaki, DRenk.coral,
        DRenk.skyBlue, DRenk.steelBlue, DRenk.blanchedAlmond)

    val (koyuMavi, koyuCamgöbeği, koyuAltınbaşak, koyuKlasikGri, koyuYeşil, koyuHaki,
      koyuMorumsu, koyuZeytinYeşili, koyuTuruncu, koyuOrkidePembesi, koyuKırmızı, koyuSomon,
      koyuDenizYeşili, koyuKurşunMavisi, koyuKurşunGrisi, koyuTurkuaz, koyuMenekşe) =
      (DRenk.darkBlue, DRenk.darkCyan, DRenk.darkGoldenrod, DRenk.darkGrayClassic, DRenk.darkGreen, DRenk.darkKhaki,
        DRenk.darkMagenta, DRenk.darkOliveGreen, DRenk.darkOrange, DRenk.darkOrchid, DRenk.darkRed, DRenk.darkSalmon,
        DRenk.darkSeaGreen, DRenk.darkSlateBlue, DRenk.darkSlateGray, DRenk.darkTurquoise, DRenk.darkViolet)

    val (açıkMavi, açıkCamgöbeği, açıkYeşil, açıkMercan, açıkAltınbaşakSarısı, açıkPembe,
      açıkSomon, açıkDenizYeşili, açıkGökMavisi, açıkKurşunGrisi, açıkÇelikMavisi, açıkSarı) =
      (DRenk.lightBlue, DRenk.lightCyan, DRenk.lightGreen, DRenk.lightCoral, DRenk.lightGoldenrodYellow, DRenk.lightPink,
        DRenk.lightSalmon, DRenk.lightSeaGreen, DRenk.lightSkyBlue, DRenk.lightSlateGray, DRenk.lightSteelBlue, DRenk.lightYellow)

  }

  implicit class ColorYöntemleri(r: Renk) {
    // ~/kojo-repo/src/main/scala/net/kogics/kojo/doodle/Color.scala
    // ../../../doodle/Color.scala
    def çevir(açı: Kesir): DRenk = net.kogics.kojo.util.Utils.awtColorToDoodleColor(r).spin(açı)
    def çevirOranla(oran: Kesir): DRenk = net.kogics.kojo.util.Utils.awtColorToDoodleColor(r).spinBy(oran)
    def dahaAçıkYap(açıklık: Kesir): DRenk = net.kogics.kojo.util.Utils.awtColorToDoodleColor(r).lighten(açıklık)
    def dahaKoyuYap(koyuluk: Kesir): DRenk = net.kogics.kojo.util.Utils.awtColorToDoodleColor(r).darken(koyuluk)
  }

  implicit class ColorYöntemleri2(r: DColor) {
    // kojo/src/main/scala/net/kogics/kojo/doodle/Color.scala
    // ../../../doodle/Color.scala
    def çevir(açı: Kesir) = r.spin(açı)
  }

  // shorter aliases like RenkKYM, RenkDD and RenkADA for ease of use
  // This one is also to show up in completion pop-up help (Renk type overrides Renk function!)
  def RenkKYM(kırmızı: Sayı, yeşil: Sayı, mavi: Sayı, saydam: Sayı = 255): Renk = Renk(kırmızı, yeşil, mavi, saydam)
  def renkKur(kırmızı: Sayı, yeşil: Sayı, mavi: Sayı, saydam: Sayı = 255): Renk = Renk(kırmızı, yeşil, mavi, saydam)
  def RenkDD(x1: Sayı, y1: Sayı, renk1: Renk, x2: Sayı, y2: Sayı, renk2: Renk, dönüşlü: İkil = false): DeğişimliBoya =
    Renk.doğrusalDeğişim(x1, y1, renk1, x2, y2, renk2, dönüşlü)
  def RenkADA(arıRenk: Sayı, doygunluk: Sayı, aydınlık: Sayı) =
    Renk.ada(arıRenk / 360.0, doygunluk/100.0, aydınlık/100.0)


  def RenkDoğrusalDeğişim(x1: Sayı, y1: Sayı, renk1: Renk, x2: Sayı, y2: Sayı, renk2: Renk, dönüşlü: İkil = false): DeğişimliBoya =
    Renk.doğrusalDeğişim(x1, y1, renk1, x2, y2, renk2, dönüşlü)
  /* Ref from CoreBuiltins:
   def ColorHSB(h: Double, s: Double, b: Double) =
   java.awt.Color.getHSBColor((h / 360).toFloat, (s / 100).toFloat, (b / 100).toFloat)
   */
  def RenkArıRenkDoygunlukAydınlık(arıRenk: Sayı, doygunluk: Sayı, aydınlık: Sayı) =
    Renk.ada(arıRenk / 360.0, doygunluk/100.0, aydınlık/100.0)

  object Renk {
    def apply(kırmızı: Sayı, yeşil: Sayı, mavi: Sayı, saydam: Sayı = 255): Renk = {
      for ((s, t) <- List(kırmızı -> "kırmızı", yeşil -> "yeşil", mavi -> "mavi", saydam -> "saydam")) require(s >= 0 && s <= 255, s"$t değeri 0'dan 255'e kadar olmalı. Ne daha büyük, ne daha küçük.")
      new AColor(kırmızı, yeşil, mavi, saydam)
    }
    def apply(rgbHex: Sayı): Renk = new AColor(rgbHex, false)
    def apply(rgbHex: Sayı, alfaDahilMi: İkil): Renk = new AColor(rgbHex, alfaDahilMi)

    def kym(kırmızı: Sayı, yeşil: Sayı, mavi: Sayı): DRenk = {
      DColor.rgb(kırmızı, yeşil, mavi)
    }
    def kyms(kırmızı: Sayı, yeşil: Sayı, mavi: Sayı, saydamlık: Sayı): DRenk = {
      DColor.rgba(kırmızı, yeşil, mavi, saydamlık)
    }
    def ada(arıRenk: Kesir, doygunluk: Kesir, açıklık: Kesir): DRenk = DColor.hsla(arıRenk, doygunluk, açıklık, 1.0)
    def adas(arıRenk: Kesir, doygunluk: Kesir, açıklık: Kesir, saydamlık: Kesir): DRenk =
      DColor.hsla(arıRenk, doygunluk, açıklık, saydamlık)
    def doğrusalDeğişim(
      x1: Kesir,
      y1: Kesir,
      renk1: Renk,
      x2: Kesir,
      y2: Kesir,
      renk2: Renk,
      dalgalıDevam: İkil = false
    ) = {
      DColor.linearGradient(x1, y1, renk1, x2, y2, renk2, dalgalıDevam)
    }
    // (x1: Double, y1: Double, x2: Double, y2: Double, distribution: collection.Seq[Double], colors: collection.Seq[AwtColor], cyclic: Boolean = false)
    def doğrusalÇokluDeğişim(
      x1: Kesir,
      y1: Kesir,
      x2: Kesir,
      y2: Kesir,
      dağılım: Dizi[Kesir],
      renkler: Dizi[Renk],
      dalgalıDevam: İkil = false
    ) = {
      DColor.linearMultipleGradient(x1, y1, x2, y2, dağılım, renkler, dalgalıDevam)
    }
    // (cx: Double, cy: Double, c1: java.awt.Color, radius: Double, c2: java.awt.Color, cyclic: Boolean):
    def merkezdenDışarıDoğruDeğişim(
      merkezX: Kesir,
      merkezY: Kesir,
      renk1: Renk,
      yarıçap: Kesir,
      renk2: Renk,
      dalgalıDevam: İkil
    ) = {
      DColor.radialGradient(merkezX, merkezY, renk1, yarıçap, renk2, dalgalıDevam)
    }
    // (x: Double, y: Double, radius: Double, distribution: collection.Seq[Double], colors: collection.Seq[AwtColor], cyclic: Boolean = false)
    def merkezdenDışarıDoğruÇokluDeğişim(
      merkezX: Kesir,
      merkezY: Kesir,
      yarıçap: Kesir,
      dağılım: Dizi[Kesir],
      renkler: Dizi[Renk],
      dalgalıDevam: İkil = false
    ) = {
      DColor.radialMultipleGradient(merkezX, merkezY, yarıçap, dağılım, renkler, dalgalıDevam)
    }

  }
}
