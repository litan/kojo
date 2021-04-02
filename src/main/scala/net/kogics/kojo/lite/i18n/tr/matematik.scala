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
  def karesi(x: Kesir): Kesir = math.pow(x, 2)
  def karekökü(x: Kesir): Kesir = math.sqrt(x)
  def kuvveti(x: Kesir, k: Kesir): Kesir = math.pow(x, k)
  def eüssü(x: Kesir): Kesir = math.exp(x)
  def onlukTabandaLogu(x: Kesir): Kesir = math.log10(x)
  def doğalLogu(x: Kesir): Kesir = math.log(x)
  def logaritması(x: Kesir): Kesir = math.log(x)
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
}
