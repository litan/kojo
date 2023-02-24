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

import java.util.Calendar
import java.util.Date
import java.util.TimeZone

trait CalendarAndTimeUtilsInTurkish {
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
    // more to come
  }
  case class BuAn() {
    val buan = Takvim.buAn
    val saniye = Takvim.saniye(buan)
    val dakika = Takvim.dakika(buan)
    val saat = Takvim.saat(buan)
    def yazıya = Takvim.tarih(buan).toString
    // more to come
  }

  // from ../../CoreBuiltins.scala
  def buAn: Uzun = System.currentTimeMillis()
  def buSaniye: Kesir = System.currentTimeMillis() / 1000.0

  // from ../svInit.scala
  def buAn2: Uzun = System.nanoTime
  def buSaniye2: İriKesir = BigDecimal(System.nanoTime) / BigDecimal("1000000000") // seconds
  @annotation.nowarn
  def sayıyaKadarSay(n: İriSayı, sessiz: İkil = yanlış): Kesir = {
    def buSaniye1 = BigDecimal(buSaniye)
    var c: BigInt = 1
    if (!sessiz) {
      print(s"*** 1'den saymaya başlıyoruz ... ")
    }
    val startTid = buSaniye2 // or buSaniye1
    while (c < n) {
      c = c + 1 // this is one of the simplest operations :-)
    }
    val stoppTid = buSaniye2 // buSaniye1
    val tid = stoppTid - startTid
    val timeInSec = (tid * 10).toLong / 10.0
    if (!sessiz) {
      println("" + n + " *** BİTTİ!")
      print("Geçen süre ")
      if (tid < 0.1)
        println((tid * 1000).round(new java.math.MathContext(2)) + " milisaniye.")
      else
        println(timeInSec + " saniye.")
    }
    timeInSec
  }

}
