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
  // skala kılavuzunda kullanma sırasıyla (yaklaşık olarak)
  // excludes scala (2 and 3) and java keywords which are in the next map
  val myKeyWords = Map(
    "set" -> "kur",
    "to" -> "|-| anlamı: ilkSayıdanSonSayıyaKadar", // 1 to 4 -> Aralık.kapalı(1, 4)
    "until" -> "|- anlamı: ilkSayıdanSonSayıyaKadarAmaSonSayıHariç", // 1 until 4 -> Aralık(1, 4)
    "by" -> "adım", // 1 to 100 by 8
    "head" -> "başı", // List(1,2).head -> Dizin(1, 2).başı
    "tail" -> "kuyruğu",
    "toList" -> "dizine", // (1 until 11).toList
    "length" -> "boyu",
    "isEmpty" -> "boşMu",
    "filter" -> "ele",
    "filterNot" -> "eleDeğilse",
    "withFilter" -> "elekle",
    "map" -> "işle",
    "flatMap" -> "düzİşle", // "ABC".flatMap(_.toLower.toString * 3)
    "sorted" -> "sıralı",
    "sortBy" -> "sırala",       // def sortBy[B](f: A => B)(implicit ord: Ordering[B]): C
    "sortWith" -> "sırayaSok",  // def sortWith(lt: (A, A) => Boolean): C
    "reduce" -> "indirge",
    "fold" -> "katla",
    "foldLeft" -> "soldanKatla",
    "foldRight" -> "sağdanKatla",
    "distinct" -> "yinelemesiz",
    "distinctBy" -> "yinelemesizİşlevle",
    "toUpper" -> "büyükHarfe",
    "toLower" -> "küçükHarfe",
    "toUpperCase" -> "büyükHarfe",
    "toLowerCase" -> "küçükHarfe",
    "mkString" -> "yazıYap", // 1 to 100 by 9 mkString => Aralık(1, 100, 9).yazıYap
    // String.
    "split" -> "böl", // "Merhaba sevgili kardeş".böl
    "trim" -> "kısalt", // "eggs, milk, butter, Coco Puffs".split(",").map(_.trim)
    "replace" -> "değiştir",
    "replaceAll" -> "değiştirHepsini",
    "replaceFirst" -> "değiştirİlkini",
    "reverse" -> "tersi",
    "toString" -> "yazıya", // 2.toString (new Object).toString
    "foreach" -> "herbiriİçin",
    "compareTo" -> "kıyasla",
    "compareToIgnoreCase" -> "kıyaslaKüçükHarfBüyükHarfAyrımıYapmadan",
    "eq" -> "aynıMı",  // AnyRef
    "equals" -> "eşitMi",
    "equalsIgnoreCase" -> "eşitMiKüçükHarfBüyükHarfAyrımıYapmadan",
    "startsWith" -> "başındaMı",
    "endsWith" -> "sonundaMı",
    "contains" -> "içeriyorMu",
    "containsSlice" -> "içeriyorMuDilim",
    "element" -> "öge",
    "indexOf" -> "sırası", // ya da konumu
    "lastIndexOf" -> "sırasıSondan",
    "charAt" -> "harf",
    "substring" -> "parçası",
    "count" -> "say",
    "exists" -> "varMı",
    "take" -> "al",
    "takeRight" -> "alSağdan",
    "takeWhile" -> "alDoğruKaldıkça",
    "drop" -> "düşür",
    "dropRight" -> "düşürSağdan",
    "dropWhile" -> "düşürDoğruKaldıkça",
    "init" -> "önü",
    "last" -> "sonu",
    "forall" -> "hepsiİçinDoğruMu", // hepsiDoğruMu
    "slice" -> "dilim",
    "zip" -> "ikile",
    "zipWithIndex" -> "ikileSırayla",
    "spin" -> "çevir",
    "rgb" -> "kym",  // kırmızı yeşil mavi
    "rgba" -> "kyms",  // saydamlık
    "linearGradient" -> "doğrusalDeğişim",
    "linearMultipleGradient" -> "doğrusalÇokluDeğişim",
    "radialGradient" -> "merkezdenDışarıDoğruDeğişim",
    "radialMultipleGradient" -> "merkezdenDışarıDoğruÇokluDeğişim",
    "hsl" -> "ada",
    "hue" -> "arıRenk",
    "saturation" -> "doygunluk",
    "lightness" -> "açıklık",
    "alignment" -> "düzen",
    "align" -> "düzenleme",
    "horizontalAlignment" -> "yatayDüzen",
    "verticalAlignment" -> "dikeyDüzen",
    "takeFocus" -> "girdiOdağıOl",
    "addKeyListener" -> "girdiDinleyiciEkle",
    "foreground" -> "önalan",
    "background" -> "artalan",
    "isDigit" -> "sayıMı",
    "strip" -> "çıkar/kayış",
    "margin" -> "kenarPayı",
    "exception" -> "kuraldışı",
    "handler" -> "işleyici",
    "runtime" -> "çalışmaSırası",
    "compiler" -> "derleyici",
    "compile-time" -> "derlemeSırası",
    "context" -> "bağlam",
    "context-menu" -> "bağlamsal-menü",
    "Seq" -> "Dizi",
    "collection.Seq" -> "Diz/DeğişkenDizi",
    "Array" -> "Dizim veya Dizik",
    "ArrayBuffer" -> "EsnekDizim",
    "List" -> "Dizin",
    "LazyList" -> "MiskinDizin",
    "ListBuffer" -> "EsnekDizin", // used in StoryTeller.scala
    "mutable" -> "değişir",
    "immutable" -> "değişmez",
    "" -> "",
    "" -> "",
  )
  // ~/src/scala-2/src/reflect/scala/reflect/internal/StdNames.scala
  // https://docs.scala-lang.org/scala3/reference/syntax.html
  val keywords = Map(
    "abstract" -> "soyut",
    "case" -> "durum", // ya da olgu: case class -> durum sınıf, match/case eşle/durum
    "catch" -> "yakala",
    "class" -> "sınıf", // çeşit ya da cins. type -> tür
    "def" -> "tanım",
    "do" -> "yap",
    "else" -> "yoksa",
    "enum" -> "sayılı",
    "export" -> "götür",
    "extends" -> "yayar", // trait -> özellik, case class Leaf extends Tree -> durum sınıf Yaprak yayar Ağaç
    "false" -> "yanlış",
    "final" -> "son",
    "finally" -> "sonunda",
    "for" -> "için",  // yerine içinYinele
    "forSome" -> "bazı", // eskitilmiş ve dilden çıkarılmış
    "given" -> "birlikte",
    "if" -> "eğer",
    "implicit" -> "örtük",
    "import" -> "getir",
    "lazy" -> "miskin",
    "match" -> "eşle",
    "new" -> "yeni",
    "null" -> "yok",
    "object" -> "nesne",
    "override" -> "baskın", // override def -> baskın (üstüne) tanım
    "package" -> "deste",
    "private" -> "sakla",
    "protected" -> "koru",
    "return" -> "dön",
    "sealed" -> "damgalı",
    "super" -> "üst",
    "this" -> "bu",
    "then" -> "yoksa",
    "throw" -> "at",
    "trait" -> "özellik",
    "true" -> "doğru",
    "try" -> "dene",
    "type" -> "tür",
    "val" -> "dez", // değişmez değer
    "var" -> "den", // değişken değer
    "while" -> "yineleDoğruKaldıkça", // predicate: koşul
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
  // https://docs.scala-lang.org/scala3/reference/soft-modifier.html
  val softKeywords = Map(
    "as" -> "olsun",
    "derives" -> "türet",
    "end" -> "bitir",
    "extension" -> "genişleme",
    "infix" -> "arada",
    "inline" -> "satırarası",
    "opaque" -> "örtük",
    "open" -> "açık",
    "transparent" -> "saydam",
    "using" -> "kullanım",
  )

  val scalaTutorial = Map(
    "kojo" -> "#tr koco",
    "scala" -> "#tr skala",
    "tutorial" -> "kılavuz",
    "cheat-sheet" -> "kılavuzcuk",
  )

  val type2en = Map()
  val def2en = Map ()
  val val2en = Map ()
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
    "Aralık.başı" -> "Range.head",
    "Aralık.sonu" -> "Range.last",
    "Aralık.uzunluğu" -> "Range.size",
    "Aralık.dizin" -> "Range.toList",
    "Aralık.yazı" -> "Range.toString",
    "Aralık.herÖgeİçin" -> "Range.foreach",
    "ay.BölmeÇizgisi" -> "javax.swing.JSeparator",
    "ay.Parça" -> "javax.swing.JComponent",
    "ay.Satır" -> "javax.swing.RowPanel",
    "ay.Sıra" -> "javax.swing.RowPanel",
    "ay.Sütun" -> "javax.swing.ColPanel",
    "ay.Yazıgirdisi" -> "javax.swing.TextField",
    "ay.Yazıalanı" -> "javax.swing.TextArea",
    "ay.Tanıt" -> "javax.swing.Label",
    "ay.Düğme" -> "javax.swing.Button",
    "ay.Açkapa" -> "javax.swing.ToggleButton",
    "ay.Salındıraç" -> "javax.swing.DropDown",
    "ay.Kaydıraç" -> "javax.swing.Slider",
    "ay.çerçeveci.çizgiKenar" -> "javax.swing.BorderFactory.createLineBorder",
    "ay.çerçeveci.boşKenar" -> "javax.swing.BorderFactory.createEmptyBorder",
  )
  val altkumeler = Map (
    "ay" -> List("olay", "değişmez", "çerçeveci")
  )
  val packageName2en = Map (
    "ay" -> List("java.awt", "javax.swing"),
  )
  // todo: ./cinidunyasi.scala
  /* (sub)types:
   "ay.olay.TuşUyarlayıcısı" -> "java.awt.event.KeyAdapter",
   "ay.olay.TuşaBasmaOlayı" -> "java.awt.event.KeyEvent",
   */
  /* (sub)packages
   "ay.değişmez" -> "javax.swing.SwingConstants",
   "ay.çerçeveci" -> "javax.swing.BorderFactory",
   */
  /* values
   "ay.değişmez.merkez" -> "javax.swing.SwingConstants.CENTER",
   "ay.değişmez.taban" -> "javax.swing.SwingConstants.BOTTOM",
   "ay.değişmez.tavan" -> "javax.swing.SwingConstants.TOP",
   "ay.değişmez.sol" -> "javax.swing.SwingConstants.LEFT",
   "ay.değişmez.sağ" -> "javax.swing.SwingConstants.RIGHT",
   "ay.değişmez.doğu" -> "javax.swing.SwingConstants.EAST",
   "ay.değişmez.batı" -> "javax.swing.SwingConstants.WEST",
   "ay.değişmez.kuzey" -> "javax.swing.SwingConstants.NORTH",
   "ay.değişmez.güney" -> "javax.swing.SwingConstants.SOUTH",
   "ay.değişmez.kuzeydoğu" -> "javax.swing.SwingConstants.NORTH_EAST",
   "ay.değişmez.kuzeybatı" -> "javax.swing.SwingConstants.NORTH_WEST",
   "ay.değişmez.güneydoğu" -> "javax.swing.SwingConstants.SOUTH_EAST",
   "ay.değişmez.güneybatı" -> "javax.swing.SwingConstants.SOUTH_WEST",
   "ay.değişmez.yatay" -> "javax.swing.SwingConstants.HORIZONTAL",
   "ay.değişmez.dikey" -> "javax.swing.SwingConstants.VERTICAL",
   "ay.değişmez.önceki" -> "javax.swing.SwingConstants.PREVIOUS",
   "ay.değişmez.sonraki" -> "javax.swing.SwingConstants.NEXT",
   "ay.değişmez.önder" -> "javax.swing.SwingConstants.LEADING",
   "ay.değişmez.izler" -> "javax.swing.SwingConstants.TRAILING",
   */

}
