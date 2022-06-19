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

import java.util.{Calendar, Date, TimeZone}

trait CalendarInTurkish {
  type Takvim = Calendar
  type Tarih = Date
  type SaatDilimi = TimeZone
  object Takvim {
    def buAn: Takvim = Calendar.getInstance()
    def tarih(buan: Takvim): Tarih = buan.getTime
    def saatDilimi(buan: Takvim) = buan.getTimeZone
    def saat(buan: Takvim): Sayı = buan.get(Calendar.HOUR_OF_DAY)
    def dakika(buan: Takvim): Sayı = buan.get(Calendar.MINUTE)
    def saniye(buan: Takvim): Sayı = buan.get(Calendar.SECOND)
    //more to come
  }
  case class BuAn() {
    val buan = Takvim.buAn
    val saniye = Takvim.saniye(buan)
    val dakika = Takvim.dakika(buan)
    val saat   = Takvim.saat(buan)
    def yazıya = Takvim.tarih(buan).toString
    //more to come
  }
}
