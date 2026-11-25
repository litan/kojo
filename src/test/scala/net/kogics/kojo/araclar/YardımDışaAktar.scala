/*
 * Copyright (C) 2026
 *   Bulent Basaran <ben@scala.org> https://github.com/bulent2k2
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
package net.kogics.kojo.araclar

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

import net.kogics.kojo.lite.i18n.tr

/**
 * tr/help.scala'daki yardım metinlerini JSON olarak dışa aktarır; ikojo'nun
 * sözlüğü (kojojs-dev/sozluk/koco-sozlugu.html) bunu gömerek kullanıyor.
 *
 * NEDEN BİR ARAÇ: sözlüğü elle yazsaydık ikinci bir doğruluk kaynağı olurdu
 * ve bayatlardı. Böyle üretilince kaynak tek: help.scala. Oradaki örnekler
 * de KoleksiyonYardımıTest ve YardımÖrnekleriTest ile zaten doğrulanıyor.
 *
 * Test kaynaklarında duruyor ki paketlenen jar'a girmesin.
 *
 * Çalıştırma:
 *   ./sbt.sh 'Test/runMain net.kogics.kojo.araclar.YardımDışaAktar <çıktı.json>'
 */
object YardımDışaAktar {
  private def kaçır(s: String): String = {
    val b = new StringBuilder
    s.foreach {
      case '"'  => b ++= "\\\""
      case '\\' => b ++= "\\\\"
      case '\n' => b ++= "\\n"
      case '\r' => b ++= "\\r"
      case '\t' => b ++= "\\t"
      // </script> gömülü JSON'u bölmesin diye tüm < karakterleri kaçırılıyor
      case '<'  => b ++= "\\u003c"
      case c if c < ' ' => b ++= f"\\u${c.toInt}%04x"
      case c    => b += c
    }
    b.toString
  }

  private def alan(ad: String, değer: String) = s""""$ad":"${kaçır(değer)}""""

  def json: String = {
    val koleksiyon = tr.help.koleksiyonYöntemleri.map {
      case (ad, imza, açıklama, örnek, sonuç, türler) =>
        // sil ve yazı gibi adlar hem elle yazılmış bir komutta hem burada var;
        // content onları BİRLEŞTİRİYOR. Yalnız yapılandırılmış alanları
        // yazsaydık kaplumbağa tarafı dışa aktarımda kaybolurdu.
        val birleşik = tr.help.content.get(ad).filter(_.contains("Bu ad başka bir türde de"))
        val ek = birleşik.map(h => s""","${"html"}":"${kaçır(h)}"""").getOrElse("")
        // Yöntemin bulunduğu türler. Eskiden bu alan ATILIYORDU: masaüstündeki
        // tamamlama yardımı türleri söylerken sözlük söylemiyordu (yalnız
        // content'te birleşen iki girdinin gömülü html'inde görünüyordu).
        // BOŞ alan "beşlinin hepsinde var" demek -- anlamlı, o yüzden boşken de
        // yazılıyor; okuyan taraf ikisini ayırt edebilsin.
        // NOT: `tür` yamalı derleyicide anahtar kelime (type) -- değişken adı olamaz.
        val türAlanı = s""","${"türler"}":"${kaçır(türler)}""""
        s"""  "${kaçır(ad)}":{"tür":"yöntem",${alan("imza", imza)},${alan("açıklama", açıklama)},""" +
          s"""${alan("örnek", örnek)},${alan("sonuç", sonuç)}${türAlanı}$ek}"""
    }
    val koleksiyonAdları = tr.help.koleksiyonYöntemleri.map(_._1).toSet
    val elle = tr.help.content.toList.sortBy(_._1).collect {
      case (ad, html) if !koleksiyonAdları.contains(ad) =>
        s"""  "${kaçır(ad)}":{"tür":"metin",${alan("html", html)}}"""
    }
    (koleksiyon.sortBy(identity) ++ elle).mkString("{\n", ",\n", "\n}\n")
  }

  def main(args: Array[String]): Unit = {
    val hedef = if (args.nonEmpty) args(0) else "yardim.json"
    Files.write(Paths.get(hedef), json.getBytes(StandardCharsets.UTF_8))
    val yöntem = tr.help.koleksiyonYöntemleri.size
    println(s"$hedef yazıldı: ${tr.help.content.size} anahtar " +
      s"($yöntem yöntem, ${tr.help.content.size - yöntem} yalnız metin)")
  }
}
