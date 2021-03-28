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
  def mutlakDeğer(x: Kesir): Kesir = math.abs(x)
  def karesi(x: Kesir): Kesir = math.pow(x, 2)
  def karekökü(x: Kesir): Kesir = math.sqrt(x)
  def kuvveti(x: Kesir, k: Kesir): Kesir = math.pow(x, k)
  def onlukTabandaLogu(x: Kesir): Kesir = math.log10(x)
  def doğalLogu(x: Kesir): Kesir = math.log(x)
  def logaritması(x: Kesir): Kesir = math.log(x)
  def sinüs(x: Kesir): Kesir = math.sin(x)
  def kosinüs(x: Kesir): Kesir = math.cos(x)
  def tanjant(x: Kesir): Kesir = math.tan(x)
  def sinüsünAçısı(x: Kesir): Kesir = math.asin(x)
  def kosinüsünAçısı(x: Kesir): Kesir = math.acos(x)
  def tanjantınAçısı(x: Kesir): Kesir = math.atan(x)
  def yuvarla(sayı: Number, basamaklar: Sayı = 0): Kesir = {
    val faktor = math.pow(10, basamaklar).toDouble
    math.round(sayı.doubleValue * faktor).toLong / faktor
  }
  /* todo: how to import/export overloaded defs?
   def enİrisi(x: Sayı, y: Sayı): Sayı = math.max(x, y)
   def enUfağı(x: Sayı, y: Sayı): Sayı = math.min(x, y)
   ...
   */
}
