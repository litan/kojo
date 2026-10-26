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

object templates {
  val keywordTemplates = Map(
    "dez" -> "dez ${değişmezinAdı} = ${değer}",
    "den" -> "den ${değişkeninAdı} = ${değer}",
    "eğer" -> "eğer (${koşul}) {\n    ${cursor}\n}",
    "yoksa" -> "yoksa {\n    ${cursor}\n}",
    "için" -> "için (i <- 1 |-| ${üstSınır}) {\n    ${cursor}\n}",
    "yineleDoğruKaldıkça" -> "yineleDoğruKaldıkça (${koşul}) {\n    ${cursor}\n}",
    "tanım" -> "tanım ${işlevinAdı}(${girdi1}: ${Tür1}, ${girdi2}: ${Tür2}): ${ÇıktınınTürü} = {\n    ${cursor}\n}",
  )
  // used in ~/kojo-repo/src/main/scala/net/kogics/kojo/xscala/CodeTemplates.scala
  // ../../../xscala/CodeTemplates.scala
  /* IMPORTANT NOTES:
   *  1) need to have one (and exactly one?) ${cursor} in each template below
   *  2) need to have at least one char after ${cursor}
   */
  val codeTemplatesRaw = Map(
    "eğer_ve_yoksa" -> "eğer (${koşul}) {\n    ${cursor}\n}\nyoksa {\n    \n}",
    "tanım_işlev" -> "tanım ${işlevinAdı}(${girdi1}: ${Tür1}) = {\n    ${cursor}\n}",
    "tanım_işlev_2_girdili" -> "tanım ${işlevinAdı}(${girdi1}: ${Tür1}, ${girdi2}: ${Tür2}) = {\n    ${cursor}\n}",
    "tanım_işlev_çıktı_türü_ile" -> "tanım ${işlevinAdı}(${girdi1}: ${Tür1}): ${çıktıTürü} = {\n    ${cursor}\n}",
    "tanım_komut_girdisiz" -> "tanım ${komutunAdı}() {\n    ${cursor}\n}",
    "tanım_komut_1_girdili" -> "tanım ${komutunAdı}(${girdi1}: ${Tür1}) {\n ${cursor}\n}",
    "tanım_komut_2_girdili" -> "tanım ${komutunAdı}(${girdi1}: ${Tür1}, ${girdi2}: ${Tür2}) {\n    ${cursor}\n}",
    "için_komutu_aralıklı" -> "için (${sayı} <- ${ilkSayı} |-| ${sonSayı}) {\n    ${cursor}\n}",
    "için_komutu_adımlı_aralıklı" -> "için (${sayı} <- ${ilkSayı} |-| ${sonSayı} adım ${adım}) {\n    ${cursor}\n}",
    "için_dizi"    -> "dez sonuç = için {\n    ${öge} <- ${dizi}\n} ver ${deyiş}${cursor}\n",
    "için_diziler" -> "dez sonuç = için {\n    ${öge} <- ${dizi}\n    ${öge2} <- ${dizi2}\n    eğer ${koşul}\n} ver ${deyiş}${cursor}\n",
    "için_dizi_komut"    -> "için (${öge} <- ${dizi}) {\n    ${cursor}\n}",
    "için_diziler_komut" -> "için {\n    ${öge} <- ${dizi}\n    ${öge2} <- ${dizi2}\n} {\n    ${cursor}\n}",
    "dez_türlü" -> "dez ${değişmezinAdı}: ${TürAdı} = ${değer}${cursor}\n",
    "den_türlü" -> "den ${değişkeninAdı}: ${TürAdı} = ${değer}${cursor}\n",
  )

  // Gated alias kept for direct callers; early editor registration uses codeTemplatesRaw.
  val codeTemplates: Map[String, String] = if (!isTurkish) Map() else codeTemplatesRaw
}
