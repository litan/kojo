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

// translating math library
object matematik {
  def piSayısı: Kesir = math.Pi
  def eSayısı: Kesir = math.E
  val gücü = kuvveti _
  def yuvarla(sayı: Number, basamaklar: Sayı = 0): Kesir = {
    val faktor = math.pow(10, basamaklar).toDouble
    math.round(sayı.doubleValue * faktor).toLong / faktor
  }
  def karesi(x: Kesir): Kesir = math.pow(x, 2)
  def karekökü(x: Kesir): Kesir = math.sqrt(x)
  def kuvveti(x: Kesir, k: Kesir): Kesir = math.pow(x, k)
  def eüssü(x: Kesir): Kesir = math.exp(x)
  def onlukTabandaLogu(x: Kesir): Kesir = math.log10(x)
  def doğalLogu(x: Kesir): Kesir = math.log(x)
  def logaritması(x: Kesir): Kesir = math.log(x)
  def logTabanlı(x: Kesir, t: Kesir) = math.log(x) / math.log(t)
  def radyana(açı: Kesir): Kesir = math.toRadians(açı)
  def dereceye(açı: Kesir): Kesir = math.toDegrees(açı)
  def sinüs(açı: Kesir): Kesir = math.sin(açı)
  def kosinüs(açı: Kesir): Kesir = math.cos(açı)
  def tanjant(açı: Kesir): Kesir = math.tan(açı)
  def sinüsünAçısı(x: Kesir): Kesir = math.asin(x)
  def kosinüsünAçısı(x: Kesir): Kesir = math.acos(x)
  def tanjantınAçısı(x: Kesir): Kesir = math.atan(x)
  def taban(x: Kesir): Kesir = math.floor(x)
  def tavan(x: Kesir): Kesir = math.ceil(x)
  def yakını(x: Kesir): Kesir = math.rint(x)
  def işareti(x: Number): Sayı = x.doubleValue.sign.toInt
  def sayıya(x: Number): Sayı = x.doubleValue.toInt
  def rastgele = math.random()
  def mutlakDeğer(x: Sayı): Sayı = math.abs(x)
  def mutlakDeğer(x: Uzun): Uzun = math.abs(x)
  def mutlakDeğer(x: Kesir): Kesir = math.abs(x)
  def mutlakDeğer(x: UfakKesir): UfakKesir = math.abs(x)
  def yakın(x: Kesir): Uzun = math.round(x)
  def yakın(x: UfakKesir): Sayı = math.round(x)
  def enİrisi(x: Sayı, y: Sayı): Sayı = math.max(x, y)
  def enUfağı(x: Sayı, y: Sayı): Sayı = math.min(x, y)
  def enİrisi(x: Uzun, y: Uzun): Uzun = math.max(x, y)
  def enUfağı(x: Uzun, y: Uzun): Uzun = math.min(x, y)
  def enİrisi(x: Kesir, y: Kesir): Kesir = math.max(x, y)
  def enUfağı(x: Kesir, y: Kesir): Kesir = math.min(x, y)
  def enİrisi(x: UfakKesir, y: UfakKesir): UfakKesir = math.max(x, y)
  def enUfağı(x: UfakKesir, y: UfakKesir): UfakKesir = math.min(x, y)
}
