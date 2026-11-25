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

object translate {
  private def common(str: String) = { kutuları(str)
    // Aralık bir tür takma adı olduğu için toString ezilemiyor; Range'in
    // 2.13 gösterimi "Range 1 to 5 by 2" biçiminde. NumericRange önce
    // çevriliyor, yoksa "NumericAralık" gibi bir şey çıkardı.
    .replace("NumericRange ", "SayısalAralık ")
    .replace("Range ", "Aralık ")
    .replace("net.kogics.kojo.lite.i18n.tr.", "")
    .replace("UserCode.this.TurkishAPI.", "")
    .replace("UserCode", "KullanıcınınYazılımı")
    .replace("net.kogics.kojo.core.Slow", "yavaş")
    .replace("net.kogics.kojo.core.Medium", "orta")
    .replace("net.kogics.kojo.core.Fast", "hızlı")
    .replace("net.kogics.kojo.core.SuperFast", "çokHızlı")
    .replace("net.kogics.kojo.core.Pixel", "noktaSayısı")
    .replace("net.kogics.kojo.core.Cm", "santim")
    .replace("net.kogics.kojo.core.Inch", "inç")
    .replace("net.kogics.kojo.core.Point", "Nokta")
    .replace("net.kogics.kojo.doodle.Color", "Renk")
    .replace("java.awt.Color", "Renk")
    .replace("ArrayCharSequence", "HarfDizisindenDizik")
    .replace("SeqCharSequence", "HarfDizisindenDizi")
    .replace("Wrapper", "Sarıcı")
    .replace("type", "tür")
    .replace("this", "bu")
    .replace("Object", "Nesne")
    .replace("Unit", "Birim")
    .replace("Nothing", "Hiç")
    .replace("Null", "Yok")
    .replace("AnyRef", "HerGönder")
    .replace("AnyVal", "HerDeğer")
    .replace("Any", "Her")
    .replace("ArraySeq", "DizikDizisi")
    .replace("ArrayBuffer", "EsnekDizim")
    .replace("Array", "Dizik")
    .replace("LazyList", "MiskinDizin")
    .replace("List", "Dizin")
    .replace("IndexedSeq", "SıralıDizi")
    .replace("Seq", "Dizi")
    .replace("Character", "JHarf")
    .replace("Char", "Harf")
    .replace("toString", "yazıya")
    .replace("String", "Yazı")
    .replace("Boolean", "İkil")
    .replace("Byte", "Lokma")
    .replace("Short", "Kısa")
    .replace("java.math.BigInteger", "JİriSayı")
    .replace("scala.math.BigInt", "İriSayı")
    .replace("math.BigInt", "İriSayı")
    .replace("BigInt", "İriSayı")
    .replace("Int", "Sayı")
    .replace("Long", "Uzun")
    .replace("Float", "UfakKesir")
    .replace("Double", "Kesir")
    .replace("scala.math.BigDecimal", "İriKesir")
    .replace("math.BigDecimal", "İriKesir")
    .replace("BigDecimal", "İriKesir")
    .replace("Integer", "JSayı")
    .replace("Number", "JTemelSayı")
    .replace("HashMap", "KıymaEşlek")
    .replace("Map", "Eşlek")
    .replace("HashSet", "KıymaKüme")
    .replace("Set", "Küme")
    .replace("Vector", "Yöney")
    .replace("Point", "Nokta")
    .replace("true", "doğru")
    .replace("false", "yanlış")
    .replace("implicit ", "örtük ")
    .replace("Iterator", "Yineleyici")
    .replace("IterableOnce", "YinelenebilirBirKere")
    .replace("Iterable", "Yinelenebilir")
    /* todo: add more basic types */
  }

  def typeInfo(str: String) = { common(str
    .replace("net.kogics.kojo.lite.i18n.TurkishAPI.", "")
    // Code Completion on Matematik e.g. has this as type:
    .replace("net.kogics.kojo.lite.i18n.TurkishAPI", "")
  )
  }

  /*
   java.lang.IllegalArgumentException: More than Int.MaxValue elements.
   at scala.collection.immutable.NumericRange$.check$1(NumericRange.scala:431)
   at scala.collection.immutable.NumericRange$.count(NumericRange.scala:441)
   */
  def beforeCommon(str: String) = { str
    .replace("More than Int.MaxValue elements.", "Sayı.Enİrisi değerinden çok ögesi var.")
    .replace("java.lang.NullPointerException", "BoşGöstergeHatası")
    // https://stackoverflow.com/questions/68608157/how-can-i-fix-this-warning-the-fonts-times-and-times-are-not-available-fo
    .replace("""Warning: the fonts "Times" and "Times" are not available for the Java logical font "Serif", which may have unexpected appearance or behavior. Re-enable the "Times" font to remove this warning.""", """Uyarı: "Times" arayüzü "Serif" özelliğini desteklemiyor. Onun için bazı görünüşler bozuk olabilir. Bilgisayarınızda "Times" arayüzünü tekrar kurarak bu uyarıyı temizleyebilirsiniz. Daha detaylı bilgi için, bakınız: https://stackoverflow.com/questions/68608157/how-can-i-fix-this-warning-the-fonts-times-and-times-are-not-available-fo""")
    // macos'te: /Users/ben/.kojo/lite/libk
    /*
     Scanning libk...
     Additional jars available (within libk) - 25

     Scanning extensions...
     Additional jars available (within /Users/ben/.kojo/extension/kojo-music) - 4
     */
    // todo: bbx to localization level 4?
    // or simply do it in ../../../xscala/ScalaCodeRunner2.scala using level 1:
    //   ~/kojo-repo/src/main/resources/net/kogics/kojo/lite/Bundle_tr.properties
    .replace("Scanning libk...", s"Kütüp taraması yapılıyor (sabit diskte ${Yardımcı.kütüpDizini} dizininde)...")
    .replace("Scanning extensions...", s"Eklenti taraması yapılıyor (sabit diskte ${Yardımcı.eklentiDizini} dizininde)...")
    .replace("Additional jars available ", "Ek jar dosyaları bulundu") // clears a trailing space
    .replace("(within libk) - ", ". Sayısı: ") // adds a leading dot
    .replace("(within ", " (dizin: ") // adds a leading space. todo: only if match, concat to the end: " tane." How? Use the regexp below

    .replace("Unapplied methods are only converted to functions when a function type is expected.",
      "Yöntem isimleri ancak bir işlev türü bekleniyorsa işleve çevrilir.")

    .replace("You can make this conversion explicit by writing ", "Bu çevrimi yapmak için şöyle yazabilirsin: ")

    .replace(" or ", " ya da ")

    .replace(" instead of ", " şöyle yazmak yerine: ")

    .replace("postfix operator ", "arkadan gelen işlemci: ")
    .replace("needs to be enabled", "henüz etkinleştirilmemiş.")

    .replace("by making the implicit value scala.language.postfixOps visible.", "Etkinleştirmek için scala.language.postfixOps örtük değerinin görülebilmesi gerek.")

    .replace("This can be achieved by adding the import clause 'import scala.language.postfixOps'", "Şunu eklerseniz olur: 'getir scala.language.postfixOps'")

    .replace("or by setting the compiler option -language:postfixOps.", "Ya da derleyicinin şu seçeneğini kullanın: -language:postfixOps.")

    .replace("See the Scaladoc for value scala.language.postfixOps for a discussion", "Bunu daha iyi anlamak için şu sayfaya bakabilirsiniz:")
    .replace("why the feature needs to be explicitly enabled.", "https://stackoverflow.com/questions/13011204/scalas-postfix-ops")
  }

  // Çıplak None -> Hiçbiri. Düz replace OLMAZ: "Nonetheless" ya da bir
  // betikteki NoneOfThese gibi sözcüklerin içini de bozardı. Sözcük sınırı
  // (\b) ile eşliyoruz; "None.get" ve "= None" yakalanıyor çünkü nokta ve
  // boşluk sınır sayılıyor.
  private val çıplakNone = """\bNone\b""".r

  // Left( / Right( / Some( / Option[ de aynı özeni istiyor. Bunlar eskiden düz
  // replace'ti; açılış ayracı SAĞ sınırı veriyor ama SOL sınırı VERMİYOR, yani
  // öğrencinin kodundaki originTopLeft(3) -> originTopSol(3), handSome(5) ->
  // handBiri(5) oluyordu. Ölçüldü. Artık None ile aynı sözcük sınırı kuralı.
  private val kutuAdları = """\b(?:Left\(|Right\(|Some\(|Option\[)""".r
  private val kutuKarşılığı =
    Map("Left(" -> "Sol(", "Right(" -> "Sağ(", "Some(" -> "Biri(", "Option[" -> "Belki[")

  /** common'un ilk adımı: Sol/Sağ/Biri/Belki. typeInfo da common'dan geçtiği
    * için tür imzalarında da çalışıyor -- bunları common'dan çıkarmak
    * "Option[Int]" ipucunu İngilizce bırakıyordu. */
  private def kutuları(str: String) =
    if (!str.contains("Left(") && !str.contains("Right(") &&
        !str.contains("Some(") && !str.contains("Option[")) str
    else
      kutuAdları.replaceAllIn(str, m => java.util.regex.Matcher.quoteReplacement(kutuKarşılığı(m.matched)))

  /**
   * 2.13'ün Range gösterimini öğrenci dostu Türkçe biçime çevirir.
   *
   * `common`'daki düz `Range ` -> `Aralık ` yer değiştirmesi YARIM kalıyordu:
   * "inexact Range 1 until 200 by 7" -> "inexact Aralık 1 until 200 by 7".
   * `empty`/`inexact` önekleri ile `to`/`until`/`by` edatları İngilizce
   * kalıyor ve tek tek `replace` ile çevrilemiyorlar -- öğrencinin kendi
   * metnindeki aynı sözcükleri de bozarlardı. Bu yüzden kalıbın TAMAMI tek
   * bir düzenli deyişle yakalanıp aralık yeniden kuruluyor ve
   * `Aralık.gösterim` ile yazdırılıyor -- `yazıya`'nın verdiği biçimin aynısı.
   *
   * 2.13 Range.toString gövdesi:
   *   s"${prefix}Range $start $preposition $end$stepped"
   *   prefix = "empty " | "inexact " | "",  preposition = "to" | "until",
   *   stepped = "" | s" by $step"
   *
   * NumericRange'e DOKUNULMUYOR (harf öncesi bakışı onu eliyor); onu
   * common'daki "NumericRange " -> "SayısalAralık " yer değiştirmesi
   * karşılıyor. Ondalıklı sınırları burada saymak ayrı bir iş.
   */
  private val aralıkKalıbı =
    raw"(?<![A-Za-z])(?:empty |inexact )?Range (-?\d+) (to|until) (-?\d+)(?: by (-?\d+))?".r

  private def aralığıÇevir(str: String) = {
    if (!str.contains("Range ")) str
    else
      aralıkKalıbı.replaceAllIn(
        str,
        m => {
          val sonuç =
            try {
              // NOT: `son` yamalı derleyicide anahtar kelime (final) -- `bitiş`
              val (ilk, bitiş) = (m.group(1).toInt, m.group(3).toInt)
              val adım = Option(m.group(4)).map(_.toInt).getOrElse(1)
              // adım 0 Range'i patlatır; öyle bir gösterim zaten üretilmez
              if (adım == 0) m.matched
              else {
                val r =
                  if (m.group(2) == "to") Range.inclusive(ilk, bitiş, adım) else Range(ilk, bitiş, adım)
                // r.size çok uzun aralıklarda fırlatıyor ("More than Int.MaxValue elements")
                Aralık.gösterim(r)
              }
            }
            catch { case _: Throwable => m.matched }
          java.util.regex.Matcher.quoteReplacement(sonuç)
        }
      )
  }

  // İki düzenli deyiş de uygulanıyor: aralık gösterimi ile çıplak None
  // birbirinden bağımsız -- biri ötekinin çıktısında yeni eşleşme üretmiyor.
  def regexpChanges(str: String) = çıplakNone.replaceAllIn(aralığıÇevir(str), "Hiçbiri")

  def result(str: String) = { common(beforeCommon(regexpChanges(str)))
    .replace("expected class or object definition", "gereken sınıf ya da nesne tanımı bulunamadı")
    .replace("val res", "dez sonuç")
    .replace("<not computed>", "<hesaplanmadı>")
    .replace("scala.collection.mutable.", "")
    .replace("scala.collection.immutable.", "")
    .replace("TurkishAPI.", "")
    .replace("Unable to stop script.", "Programın çalışmasını güzelce durduramadık.")
    .replace("Doing a forced-stop. It's best to just restart Kojo!", "Zorlayarak durduruyoruz. Kojo'yu kapatıp açmak iyi olur!")
    .replace("val ", "dez ")
    /*  .replace("var ", "den ")  var is a very common word in turkish */
    .replace("def ", "tanım ") // bbx todo: use regexp! hedef !=> hetanım
    .replace("hetanım", "hedef").replace("setanım", "sedef") // (:-)
    .replace("class ", "sınıf ")
    .replace("case ", "durum ")
    .replace("true", "doğru")
    .replace("false", "yanlış")
    .replace("mutated ", "değişti ")
    .replace("null", "yok")
    .replace("java.lang.ThreadDeath", "İşParçacığıÖlümü")
    .replace("java.lang.RuntimeException", "ÇalışmaSırasıKuralDışı")
    .replace("java.lang.Exception", "KuralDışı")
    .replace("java.lang.AssertionError", "BelirtimHatası")
    .replace("java.lang.IllegalArgumentException", "KuraldışıGirdiHatası")
    .replace("java.lang.IndexOutOfBoundsException", "SınırDışınaTaşmaHatası")
    .replace("scala.NotImplementedError", "EksikTanımHatası")
    .replace("java.lang.ArithmeticException", "MatematikselHata")
    .replace("java.util.NoSuchElementException", "OlmayanÖgeHatası")
    .replace("empty collection", "boş koleksiyon")
    .replace("java.util.MissingFormatWidthException", "YazıDüzenlemedeUzunlukEksikliğiHatası")
    .replace("is out of bounds", "sınırlar dışında")
    .replace("assertion failed", "belirtilen koşul sağlanmadı")
    .replace("requirement failed", "gerek koşul sağlanmadı")
    .replace("an implementation is missing", "bir tanım eksik")
    .replace("/ by zero", "Sıfıra bölüm")
    .replace("Unable to format", "Kod düzenlenemedi")
    .replace("max", "enİri")
    .replace("min", "enUfak")
    .replace("biçienUfak", "biçimin") // todo! biçiminde, biçiminin, ...
    .replace("head of empty", "başı istenen boş bir")
    .replace("Error", "Hata")
    .replace("object creation impossible", "nesne oluşturulamadı")
    .replace("Missing implementation for member of trait",
      "Temel tür olan özellik yöntemlerinden birinin tanımı eksik. Özelliğin adı")
    //
    .replace("Warning", "Uyarı")
    .replace("overloaded method apply with alternatives", "birkaç tane işlev uygulama () seçeneği var")
    .replace("cannot be applied to", "hiçbiri şu türe uygulanamıyor:")
    .replace("unclosed string literal", "çift tırnak işaretinin teki eksik")
    .replace("Missing closing brace", "kapatan kıvrık parantez")
    .replace("assumed here", "burada olduğu varsayıldı")
    .replace("You can't redraw a picture", "Resim bir kere çizildikten sonra yine çizilemez.")
    .replace("Problem: java.lang.IllegalStateException: Cannot access a Picture's geometry before it is drawn.",
      "Hata: KuralDışıDurumHatası: Resim çizilmeden önce kullanıldı.")
    /* Hata[28,13]: not enough arguments for method apply: (v1: Resim): İkil in trait Function1.
     Unspecified value parameter v1.
     top.çarptıMı()
     */
    .replace("not enough arguments for method apply", "İşlev uygulama yöntemi için yeterince girdi yok")
    .replace("in trait Function", "şu özellik için: İşlev")
    /*  Hata[3,6]: not enough arguments for method fonk1: (g1: Sayı): Sayı.
     *     Unspecified value parameter g1.
     *     fonk1()
     *          ^
     */
    .replace("not enough arguments for method ", "yeterince girdi yok. Yöntem ")
    .replace("Unspecified value parameter ", "eksik değer girdisi ")

    /* Hata[28,21]: too many arguments (found 2, expected 1) for method apply: (v1: Resim): İkil in trait Function1
     top.çarptıMı(engel, foo)
     *                   ^
     Hata[3,12]: too many arguments (found 2, expected 1) for method fonk1: (g1: Sayı): Sayı
     fonk1(100, 200)
     *          ^
     */
    .replace("too many arguments (found", "fazla girdi verildi (bulunan")
    //
    .replace(", expected ", ", beklenen ")
    .replace(" for method apply: ", " işlev uygulama yöntemi için: ")
    .replace(" for method ", " şu yöntem için: ")

    // Hata[31,5]: not found: value doğrudanÇağır
    .replace("not found: value", "değer bulunamadı:")
    .replace("Infinity", "Sonsuz")

    /*
     *  Hata[86,26]: tür mismatch;
     *   found   : Kesir
     *   required: Sayı, Uzun, Lokma, Kısa, İriSayı
     *          satıryaz(f"ETH: $as%d Çizim Sayısı: saniyede ${1.0/as}%.1f")
     *                          ^
     */
    .replace("tür mismatch;", "uyumsuz tür;")
    .replace("found   : ", "ne var   : ")
    .replace("required: ", "ne olmalı: ")

    /*
     * Hata[-25,1]: kapatan kıvrık parantez `}` burada olduğu varsayıldı
     * import TSCanvas._; import Tw._/*
     * ^
     */ */
    .replace("import TSCanvas._; import Tw._/*", "En sona yakın bir komut dizini olmalı!")

    // yinele(3) satıryaz
    .replace("missing argument list", "eksik girdi")
    .replace("in object TurkishAPI", " (Türkçe komutlardan biri)")
    .replace("in object", "şu nesne içinde:")

  }

}
