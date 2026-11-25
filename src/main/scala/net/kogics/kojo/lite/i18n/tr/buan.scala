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

trait TakvimVeZamanYöntemleri {
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
  val Aylar = List("Ocak", "Şubat", "Mart", "Nisan", "Mayıs", "Haziran",
    "Temmuz", "Ağustos", "Eylül", "Ekim", "Kasım", "Aralık")
  val Günler = List("Pazartesi", "Salı", "Çarşamba", "Perşembe", "Cuma", "Cumartesi", "Pazar")
  case class BuAn() {
    val buan = Takvim.buAn
    val saniye = Takvim.saniye(buan)
    val dakika = Takvim.dakika(buan)
    val saat = Takvim.saat(buan)
    def İngilizce = Takvim.tarih(buan).toString
    def hepsi = {
      import java.time.{ ZonedDateTime, LocalTime, LocalDate } // newer and better API
      val tnow = LocalTime.now
      val dnow = LocalDate.now
      List(
        List(dnow.getDayOfMonth, Aylar(dnow.getMonth.getValue - 1), Günler(dnow.getDayOfWeek.getValue - 1)),
        List(tnow.getHour, tnow.getMinute, tnow.getSecond),
        List(ZonedDateTime.now.getZone),
        List(dnow.getYear)
      )
    }
    def gün = hepsi(0)
    def ay = hepsi(0)(1)
    def zaman = hepsi(1)
    def saatDilimi = hepsi(2)
    def yıl = hepsi(3)
    override def toString = yazıya
    def yazıya: Yazı = s"${gün.mkString(" ")}  ${zaman.mkString(":")} ${saatDilimi.mkString("")}  ${yıl.mkString("")}"
  }

  // from ../../CoreBuiltins.scala
  def buAn: Uzun = System.currentTimeMillis()
  def buSaniye: Kesir = System.currentTimeMillis() / 1000.0

  // from ../svInit.scala
  def buAn2: Uzun = System.nanoTime
  def buSaniye2: İriKesir = BigDecimal(System.nanoTime) / BigDecimal("1000000000") //seconds
  @annotation.nowarn
  def sayıyaKadarSay(n: İriSayı, sessiz: İkil = false): Kesir = {
    def buSaniye1 = BigDecimal(buSaniye)
    var c: BigInt = 1
    if (!sessiz) {
      print(s"*** 1'den saymaya başlıyoruz ... ")
    }
    val startTid = buSaniye2 // or buSaniye1
    while (c < n) {
      c = c + 1  // this is one of the simplest operations :-)
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
