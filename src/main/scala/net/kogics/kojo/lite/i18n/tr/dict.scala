/*
 * Copyright (C) 2021 June
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

object dict {
  val keywords = Map(
    "abstract" -> "soyut",
    "case" -> "durum",
    "catch" -> "yakala",
    "class" -> "sınıf",
    "def" -> "tanım",
    "do" -> "yap",
    "else" -> "yoksa",
    "extends" -> "genişlet",
    "false" -> "yanlış",
    "final" -> "son",
    "finally" -> "sonunda",
    "for" -> "yerine",
    "forSome" -> "bazı",
    "if" -> "eğer",
    "implicit" -> "örtük",
    "import" -> "getir",
    "lazy" -> "miskin",
    "match" -> "eşle",
    "new" -> "yeni",
    "null" -> "yok",
    "object" -> "nesne",
    "override" -> "baskın",
    "package" -> "deste",
    "private" -> "özel",
    "protected" -> "koru",
    "return" -> "dön",
    "sealed" -> "sınırlı",
    "super" -> "üst",
    "this" -> "bu",
    "throw" -> "at",
    "trait" -> "özellik",
    "try" -> "dene",
    "true" -> "doğru",
    "type" -> "tür",
    "val" -> "der",
    "var" -> "den",
    "while" -> "sırada",
    "with" -> "ekle",
    "yield" -> "ver"
  )
  // we skip java keywords listed in scala keywords above:
  val javaKeywords = Map(
    "assert" -> "belirt",
    "boolean" -> "ikil",
    "break" -> "çık",
    "byte" -> "lokma",
    "case" -> "durum",
    "char" -> "harf",
    "const" -> "değişmez",
    "continue" -> "devam",
    "default" -> "varsayılı",
    "double" -> "kesir",
    "enum" -> "sayılı",
    "float" -> "ufakkesir",
    "goto" -> "git",
    "implements" -> "tanımlar",
    "instanceof" -> "bireyi",
    "int" -> "sayı",
    "interface" -> "arayüz",
    "long" -> "uzun",
    "public" -> "açık",
    "native" -> "yerli",
    "short" -> "kısa",
    "static" -> "durgun",
    "strictfp" -> "kesinkesir",
    "switch" -> "makas",
    "synchronized" -> "anuyumlu",
    "throws" -> "atar",
    "transient" -> "geçici",
    "void" -> "türsüz",
    "volatile" -> "uçucu"
  )
  val type2en = Map(
    // type defs
    "Nesne" -> "Object",
    "Birim" -> "Unit",
    "Her" -> "Any",
    "HerDeğer" -> "AnyVal",
    "HerGönder" -> "AnyRef",
    "Yok" -> "Null",
    "Hiç" -> "Nothing",
    "Renk" -> "Color",
    "Boya" -> "Paint",
    "Hız" -> "Speed",
    "Nokta" -> "Point",
    "Dikdörtgen" -> "Rectangle",
    "Üçgen" -> "Triangle2D",
    "İkil" -> "Boolean",
    "Seçim" -> "Boolean",
    "Lokma" -> "Byte",
    "Kısa" -> "Short",
    "Sayı" -> "Int",
    "Uzun" -> "Long",
    "İriSayı" -> "BigInt",
    "UfakKesir" -> "Float",
    "Kesir" -> "Double",
    "İriKesir" -> "BigDecimal",
    "Harf" -> "Char",
    "Yazı" -> "String",
    "EsnekYazı" -> "collection.mutable.StringBuilder",
    "Belki" -> "Option",
    "Biri" -> "Some",
    "Dizi" -> "collection.Seq",
    "Dizin" -> "List",
    "MiskinDizin" -> "LazyList",
    "Yöney" -> "Vector",
    "Küme" -> "Set",
    "Sayılar" -> "Vector[Sayı]",
    "UzunlukBirimi" -> "UnitLen",
    "GeoYol" -> "java.awt.geom.GeneralPath",
    "GeoNokta" -> "VertexShape",
    "Grafik2B" -> "scala.swing.Graphics2D",
    "İmge" -> "Image",
    "Bellekteİmge" -> "BufferedImage",
    "Bellekteİmgeİşlemi" -> "java.awt.image.BufferedImageOp",
    "İşlev1" -> "Function1",
    "İşlev2" -> "Function2",
    "İşlev3" -> "Function3",
    "Bölümselİşlev" -> "PartialFunction",
    // class defs
    "Mp3Çalar" -> "net.kogics.kojo.music.KMp3",
  )
  val val2en = Map (
    // val defs
    "yok" -> "null",
    "Hiçbiri" -> "None",
    "doğru" -> "true",
    "yanlış" -> "false",
    "yavaş" -> "slow",
    "orta" -> "medium",
    "hızlı" -> "fast",
    "çokHızlı" -> "superFast",
    "noktaSayısı" -> "Pixel",
    "santim" -> "Cm",
    "inç" -> "Inch",
    "Boş" -> "collection.immutable.Nil",
  )
  val method2en = Map (
    "Yöney.boş" -> "Vector.empty",
    "Küme.boş" -> "Set.empty",
    "Dizi.doldur" -> "Seq.tabulate",
    "MiskinDizin.sayalım" -> "LazyList.from",
    "Harf.sayıMı" -> "Character.isDigit",
    "Harf.harfMi" -> "Character.isLetter",
    "Harf.yazıya" -> "Char.toString",
    "Harf.kutuyaKoy" -> "Char.box",
    "Harf.kutudanÇıkar" -> "Char.unbox",
    "Harf.sayıya" -> "Char.char2int",
    "Harf.uzuna" -> "Char.char2long",
    "Harf.kesire" -> "Char.char2double",
    "Harf.ufakkesire" -> "Char.char2float",
    "Harf.enUfağı" -> "Char.MaxValue",
    "Harf.enİrisi" -> "Char.MinValue",
  )
}
