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
package net.kogics.kojo.lite.i18n

import scala.tools.nsc.Settings

import org.junit.Test
import org.junit.Assert._
import org.junit.Assume.assumeTrue

import net.kogics.kojo.xscala.CompilerListener
import net.kogics.kojo.lite.NoOpRunContext
import net.kogics.kojo.xscala.CompilerAndRunner

/**
 * tr/help.scala'daki koleksiyon örneklerinin GERÇEKTEN yazdığı şeyi yazdığını
 * doğrular. Belgedeki her örnek burada çalıştırılıp yanındaki sonuçla
 * karşılaştırılıyor; bir yöntemin davranışı değişirse yardım metni sessizce
 * yanlış kalmıyor, test kırılıyor.
 *
 * Hepsi TEK bir programda derlenip koşuluyor: 78 örnek için 78 derleme değil,
 * bir derleme.
 */
/**
 * Örneklerin sonuçlarını toplar. Neden println değil: derlenen kod başka bir
 * iş parçacığında koşuyor ve Predef.println Console.out'a yazıyor -- Console.out
 * ilklendiğinde yakalanan System.out'u tutuyor, sonradan System.setOut yapmak
 * ona işlemiyor. Statik bir toplayıcı bu sorunu tümden atlıyor.
 */
object KoleksiyonYardımıToplayıcı {
  val sonuçlar = new java.util.concurrent.ConcurrentHashMap[String, String]
  // Çıktı penceresinde görünen biçimi saklıyoruz: Kojo REPL çıktısını
  // translate.result'tan geçiriyor (List -> Dizin, Set -> Küme, ...), belgedeki
  // sonuç da öğrencinin GÖRDÜĞÜ metin olmalı.
  def koy(ad: String, değer: Any): Unit =
    sonuçlar.put(ad, tr.translate.result(String.valueOf(değer)))
}

class KoleksiyonYardımıTest {
  private val toplayıcı = "net.kogics.kojo.lite.i18n.KoleksiyonYardımıToplayıcı"

  private def örnekleriKoştur(): Map[String, String] = {
    val kod = tr.help.koleksiyonYöntemleri
      .map {
        case (ad, _, _, örnek, _, _) =>
          s"""try { $toplayıcı.koy("$ad", $örnek) }
             |catch { case t: Throwable => $toplayıcı.koy("$ad", "KURALDIŞI: " + t) }""".stripMargin
      }
      .mkString("\n")

    val derlemeHataları = scala.collection.mutable.ListBuffer.empty[String]
    val listener = new CompilerListener {
      // ilk hatada durmuyoruz: bir koşuda hepsini görelim
      def error(msg: String, line: Int, column: Int, offset: Int, lineContent: String): Unit =
        derlemeHataları += s"satır $line: $msg -- ${lineContent.trim}"
      def warning(msg: String, line: Int, column: Int): Unit = {}
      def info(msg: String, line: Int, column: Int): Unit = {}
      def message(msg: String): Unit = {}
    }
    val runner = new CompilerAndRunner(
      { () =>
        val s = new Settings(); s.usejavacp.value = true; s
      },
      Some("import net.kogics.kojo.lite.i18n.TurkishAPI._"),
      listener,
      new NoOpRunContext
    )

    KoleksiyonYardımıToplayıcı.sonuçlar.clear()
    runner.compileAndRun(kod)

    // Bu kontrol olmazsa tek bir derleme hatası bütün örnekleri düşürür ve
    // test "78 örnek hiç çalışmadı" diye yanıltıcı biçimde patlar.
    if (derlemeHataları.nonEmpty)
      fail(s"${derlemeHataları.size} örnek derlenmedi:\n" + derlemeHataları.mkString("\n"))

    import scala.jdk.CollectionConverters._
    KoleksiyonYardımıToplayıcı.sonuçlar.asScala.toMap
  }

  // These help examples are written with Turkish KEYWORDS (dez, tanım, eğer ...),
  // which only the keyword-patched Scala compiler accepts. Kojo upstream builds
  // on the stock compiler, where they cannot compile by construction - so probe
  // once and skip instead of reporting a failure that is not a defect.
  private def türkçeAnahtarSözcüklerDerleniyor: Boolean = {
    val hatalar = scala.collection.mutable.ListBuffer.empty[String]
    val dinleyici = new CompilerListener {
      def error(msg: String, line: Int, column: Int, offset: Int, lineContent: String): Unit = hatalar += msg
      def warning(msg: String, line: Int, column: Int): Unit = {}
      def info(msg: String, line: Int, column: Int): Unit = {}
      def message(msg: String): Unit = {}
    }
    val koşucu = new CompilerAndRunner(
      { () => val s = new Settings(); s.usejavacp.value = true; s },
      Some("import net.kogics.kojo.lite.i18n.TurkishAPI._"),
      dinleyici,
      new NoOpRunContext
    )
    koşucu.compileForRunning("dez x = 1")
    hatalar.isEmpty
  }

  @Test
  def örneklerYazdıklarınıYazıyor(): Unit = {
    assumeTrue(
      "stock Scala compiler: Turkish keywords are not available, skipping",
      türkçeAnahtarSözcüklerDerleniyor
    )
    val gerçek = örnekleriKoştur()
    val yanlışlar = tr.help.koleksiyonYöntemleri.flatMap {
      case (ad, _, _, örnek, beklenen, _) =>
        gerçek.get(ad) match {
          case None       => Some(s"$ad: örnek hiç çalışmadı -- $örnek")
          case Some(`beklenen`) => None
          case Some(v)    => Some(s"$ad: `$örnek`\n     belgede: $beklenen\n     gerçek : $v")
        }
    }
    assertTrue(
      s"${yanlışlar.size} örnek belgedeki sonucu vermiyor:\n" + yanlışlar.mkString("\n"),
      yanlışlar.isEmpty
    )
  }

  @Test
  def türkçeSarmalayıcılarBilinenSahipSayılıyor(): Unit = {
    // Bu adlar uydurma değil: tamamlama koşturulup ölçüldü (bkz. commit iletisi).
    // Biri değişirse yardım metinleri sessizce görünmez olur, o yüzden çivili.
    val ölçülenSahipler = List(
      "net.kogics.kojo.lite.i18n.tr.SeqMethodsInTurkish.SeqYöntemleri",
      "net.kogics.kojo.lite.i18n.tr.SeqMethodsInTurkish.ImmutableIterableMethods",
      "net.kogics.kojo.lite.i18n.tr.StackMethodsInTurkish.YığınYöntemleri",
      "net.kogics.kojo.lite.i18n.tr.StringMethodsInTurkish.YazıYöntemleri",
      "net.kogics.kojo.lite.i18n.tr.Eşlem"
    )
    ölçülenSahipler.foreach { sahip =>
      assertTrue(s"$sahip tanınmıyor", tr.trDestesiMi(sahip))
    }
    assertFalse(tr.trDestesiMi("scala.collection.immutable.List"))
    assertFalse(tr.trDestesiMi(null))
  }

  @Test
  def elleYazılanYardımlarKaybolmadı(): Unit = {
    // `elleYazılan ++ koleksiyonYardımı` deseydik aynı adlı girdiler sessizce
    // ezilirdi. Nitekim bir süre ezildi: sil (tuvali sil) ve yazı (tuvale yaz)
    // -- ikisi de öğrencinin ilk öğrendiği komutlardan. Artık birleşiyorlar.
    val çakışanlar = List("sil", "yazı")
    çakışanlar.foreach { ad =>
      val metin = tr.help.content.getOrElse(ad, fail(s"$ad için yardım yok").asInstanceOf[String])
      assertTrue(
        s"$ad girdisinde iki anlam da yok -- koleksiyon girdisi elle yazılanı ezmiş olabilir",
        metin.contains("Bu ad başka bir türde de kullanılıyor")
      )
    }
    // sil girdisi hâlâ tuvalden söz ediyor mu (elle yazılan kısım duruyor mu)
    assertTrue("sil girdisinde tuval/çizim anlatımı kaybolmuş", tr.help.content("sil").contains("tuval"))
  }

  @Test
  def herYöntemİçinYardımVar(): Unit = {
    val içerik = tr.help.content
    tr.help.koleksiyonYöntemleri.foreach {
      case (ad, imza, _, _, _, _) =>
        val metin = içerik.getOrElse(ad, fail(s"$ad için yardım metni yok").asInstanceOf[String])
        assertTrue(s"$ad metninde imza geçmiyor", metin.contains(imza))
    }
  }

  // --- 6. alan (yöntemin bulunduğu türler) kaynaktan yeniden türetiliyor ---
  //
  // help.scala'daki her satırın son alanı yöntemin hangi türlerde göründüğünü
  // söylüyor; boşsa "beşlinin hepsinde var" demek. Elle yazılsa bayatlardı: bir
  // sarmalayıcıya yöntem eklendiğinde yardım metni sessizce yanlış kalırdı --
  // ki bu tam da bu alanın kapattığı hataydı (koşulsuz basılan "her toplulukta
  // aynı" cümlesi 113 satırda yanlıştı). Burada tr/*.scala taranıp aynı liste
  // yeniden kuruluyor ve karşılaştırılıyor.
  //
  // Tarama neden yeterli: sarmalayıcı örtük sınıflar arasında KALITIM YOK --
  // her tür kendi yöntemlerini ayrı ayrı sayıyor (tasarım gereği) -- yani
  // dosyalardaki `def` satırları sahipleri tam veriyor.

  private val trDizini = "src/main/scala/net/kogics/kojo/lite/i18n/tr"

  // örtük sınıf -> öğrencinin gördüğü tür adı. Burada olmayan sınıflar (iç
  // yardımcılar) türe sayılmıyor.
  private val sınıfTürü = Map(
    "dizi.scala:colSeqYöntemleri" -> "Diz",
    "dizi.scala:SeqYöntemleri" -> "Dizi",
    "dizi.scala:IndexedSeqYöntemleri" -> "SıralıDizi",
    "dizi.scala:IterableMethods" -> "Yinelenebilir",
    "dizi.scala:ImmutableIterableMethods" -> "Yinelenebilir",
    "dizin.scala:ListYöntemleri" -> "Dizin",
    "yoney.scala:YöneyYöntemleri" -> "Yöney",
    "kume.scala:SetYöntemleri" -> "Küme",
    "kuyruk.scala:mutQueueMethods" -> "Kuyruk",
    "kuyruk.scala:YığınYöntemleri" -> "Yığın",
    "kuyruk.scala:mutPriQueMethods" -> "ÖncelikSırası",
    "kuyruk.scala:İkiUçluKuyrukYöntemleri" -> "İkiUçluKuyruk",
    "eslem.scala:EşlekYöntemleri" -> "Eşlek",
    "eslem.scala:Eşlem" -> "Eşlem",
    "aralik.scala:RangeYöntemleri" -> "Aralık",
    "yazi.scala:YazıYöntemleri" -> "Yazı",
    // EsnekYazı (StringBuilder) haritada YOKTU: sil/harf/parçası/sayıya/kesire
    // burada da tanımlı, dolayısıyla üretilen listeler eksik çıkıyordu.
    "yazi.scala:EsnekYazıYöntemleri" -> "EsnekYazı",
    "harf.scala:HarfYöntemleri" -> "Harf",
    "yineleyici.scala:YineleyiciYöntem" -> "Yineleyici",
    "yineleyici.scala:BellekliYineleyiciYöntem" -> "Yineleyici",
    "ikisindenbiri.scala:İkisindenBiriYöntem" -> "İkisindenBiri",
    "ikisindenbiri.scala:İkisindenBiri" -> "İkisindenBiri",
    "belki.scala:BelkiYöntemleri" -> "Belki",
    "belki.scala:Belki" -> "Belki",
    "miskindizin.scala:LazyListYöntemleri" -> "MiskinDizin",
    "dizik.scala:ArrayMethods" -> "Dizik",
    "dizik.scala:ArrayBufferMethods" -> "EsnekDizik",
    "dizik.scala:EsnekDizik" -> "EsnekDizik",
    "dizim.scala:Dizim" -> "Dizim",
    "dizim.scala:EsnekDizim" -> "EsnekDizim"
  )

  // Gösterilen tür -> sarmalayıcıları ona da uygulanan üst türler.
  // (Bir yöntem Dizi'de tanımlıysa Dizin'de de görünür.)
  private val üstTürler = Map(
    "Dizi" -> Set("Diz", "Yinelenebilir"),
    "Dizin" -> Set("Dizi", "Diz", "Yinelenebilir"),
    "Yöney" -> Set("Dizi", "Diz", "SıralıDizi", "Yinelenebilir"),
    "Küme" -> Set("Yinelenebilir"),
    "Kuyruk" -> Set("Diz", "Yinelenebilir"),
    "Yığın" -> Set("Diz", "Yinelenebilir"),
    "ÖncelikSırası" -> Set("Yinelenebilir"),
    "İkiUçluKuyruk" -> Set("Diz", "Yinelenebilir"),
    "Eşlek" -> Set("Yinelenebilir"),
    "Eşlem" -> Set("Yinelenebilir"),
    "MiskinDizin" -> Set("Dizi", "Diz", "Yinelenebilir"),
    "Dizik" -> Set("Yinelenebilir"),
    "EsnekDizik" -> Set("Diz", "Yinelenebilir"),
    "Aralık" -> Set("Dizi", "Diz", "SıralıDizi", "Yinelenebilir"),
    "Dizim" -> Set.empty[String],
    "EsnekDizim" -> Set.empty[String],
    "Yazı" -> Set.empty[String],
    "EsnekYazı" -> Set.empty[String],
    "Harf" -> Set.empty[String],
    "Yineleyici" -> Set.empty[String],
    "İkisindenBiri" -> Set.empty[String],
    "Belki" -> Set.empty[String]
  )

  // Üst türler tek başına gösterilmiyor: onları kalıtan somut türler zaten
  // ayrı ayrı yazılıyor, ikisi birden listeyi gereksiz şişirirdi.
  private val gizliTürler = Set("Yinelenebilir", "SıralıDizi", "Diz")

  // Yardım metnindeki "her toplulukta aynı" cümlesinin saydığı beşli
  private val beşli = List("Dizi", "Dizin", "Yöney", "Küme", "Kuyruk")

  // case/final/sealed/abstract önekleri de sayılmalı: `case class Eşlem` satırı
  // eşleşmezse içindeki yöntemler bir ÖNCEKİ sahibe yazılıyordu. Masaüstünde
  // companion object case class'tan önce geldiği için etiket tesadüfen doğru
  // çıkıyordu; sıra ters olsaydı sessizce yanlış olurdu.
  // Koleksiyon SAYILMAYAN örtük sınıflar. Bu küme ile sınıfTürü birlikte
  // tr/*.scala'daki BÜTÜN örtük sınıfları kapsamak zorunda (aşağıdaki sav).
  //
  // Neden gerekli: türetim sınıfTürü'nden besleniyor, ve bir sarmalayıcı
  // haritada yoksa hem tablo hem türetim birlikte eksiliyordu -- yani test
  // yeşil kalırken yardım metni yanlış oluyordu. EsnekYazı tam bu yoldan
  // geçti ve altı satırı sessizce eksik bıraktı. Artık 46. bir sarmalayıcı
  // eklendiğinde sessizce eksilmek yerine BURAYA ya da sınıfTürü'ne bir
  // karar yazmak gerekiyor.
  private val türeSayılmayanlar = Set(
    // sayı türleri -- koleksiyon değil
    "sayi.scala:SayıYöntemleri",
    "sayi.scala:KısaSayıYöntemleri",
    "sayi.scala:UzunSayıYöntemleri",
    "sayi.scala:LokmaYöntemleri",
    "sayi.scala:KesirYöntemleri",
    "sayi.scala:İriSayıYöntemleri",
    "sayi.scala:İriKesirYöntemleri",
    // çizim/geometri/renk
    "renk.scala:ColorYöntemleri",
    "renk.scala:ColorYöntemleri2",
    "geo.scala:RectYöntemleri",
    "geo.scala:GeoYolYöntemleri",
    "geo.scala:GeoNoktaYöntemleri",
    "tuvalcizim.scala:canvasDrawMethods",
    // arayüz ve olaylar
    "arayuz.scala:TuşaBasmaOlayıYöntemleri",
    "kumanda.scala:KumandaYöntemleri",
    // her türe uygulanan kök yöntemler -- bir türü değil, hepsini ilgilendiriyor
    "kokturler.scala:NesneYöntemleri",
    "kokturler.scala:HerNesneYöntemleri",
    "kokturler.scala:HerGönderYöntemleri",
    // koleksiyon olmayan öbür sarmalayıcılar
    "dosya.scala:DosyaYöntemleri",
    "gelecek.scala:FutureMethods",
    "bolumselislev.scala:PartialFunctionMethodsInTurkish",
    // ParDizi (Dizin.paralel) bilerek dışarıda: öğretilen topluluk takımının
    // parçası değil, yardım tablosunda da girdisi yok.
    "dizin.scala:ParListYöntemler"
  )

  // Scala değiştiricileri SIRASIZ kabul ediyor: `implicit final class`,
  // `implicit private class`, `private implicit class`... hepsi geçerli. Bir
  // sıralamayı kalıba gömmek her seferinde bir kip dışarıda bırakıyordu ve
  // kaçan satır çift kayıp veriyor: yöntemleri bir önceki sahibe yazılıyor
  // (case class hatasının aynısı) VE herÖrtükSınıfSınıflandırılmış onu
  // bildirmiyor -- yani savın önlemek için var olduğu sessizliğin ta kendisi.
  //
  // O yüzden sıralama hiç sayılmıyor: değiştirici dizisi TEK GRUP olarak
  // yakalanıp `implicit` içinde aranıyor. Böylece yarın listeye bir
  // değiştirici eklense bile aynı tuzak kurulamıyor. `private[paket]` gibi
  // nitelikli biçimler de kapsanıyor. Altı yazım da sınanıyor
  // (kalıplarKipleriTanıyor).
  private val değiştirici = """(?:case|final|sealed|abstract|implicit|(?:private|protected)(?:\[[^\]]+\])?)"""
  private val sınıfBaşı =
    s"""^\\s*((?:$değiştirici\\s+)*)(?:class|trait|object)\\s+([^\\s\\[(]+)""".r

  /** Yakalanan değiştirici dizisinde `implicit` var mı. */
  private def örtükMü(değiştiriciler: String): Boolean =
    değiştiriciler.split("\\s+").contains("implicit")
  private val yöntemBaşı =
    """^\s*(?:@deprecated\S*\s*)?(?:final\s+)?def\s+([A-Za-zÇĞİIÖŞÜçğıöşü0-9_]+)""".r

  /**
   * tr dizinindeki kaynaklar tek geçişte taranıyor. Üç sonuç:
   *  - sahipler      : yöntem adı -> onu DOĞRUDAN tanımlayan gösterilen türler
   *  - örtükSınıflar : bulunan bütün `implicit class`lar (sınıflandırma savı için)
   *  - bütünSahipler : bulunan bütün sınıf/nesne/trait etiketleri (hayalet savı için)
   */
  private lazy val tarama: (Map[String, Set[String]], Set[String], Set[String]) = {
    val dizin = new java.io.File(trDizini)
    assertTrue(s"$trDizini bulunamadı (test proje kökünden koşmalı)", dizin.isDirectory)
    val ham = scala.collection.mutable.Map.empty[String, Set[String]].withDefaultValue(Set.empty)
    val örtükler = scala.collection.mutable.Set.empty[String]
    val bütün = scala.collection.mutable.Set.empty[String]
    dizin.listFiles().filter(_.getName.endsWith(".scala")).sortBy(_.getName).foreach { dosya =>
      var sahip = ""
      val kaynak = scala.io.Source.fromFile(dosya, "UTF-8")
      try kaynak.getLines().foreach { satır =>
        sınıfBaşı.findFirstMatchIn(satır).foreach { m =>
          sahip = dosya.getName + ":" + m.group(2)
          bütün += sahip
          if (örtükMü(m.group(1))) örtükler += sahip
        }
        yöntemBaşı.findFirstMatchIn(satır).foreach { m =>
          sınıfTürü.get(sahip).foreach(ad => ham(m.group(1)) = ham(m.group(1)) + ad)
        }
      }
      finally kaynak.close()
    }
    (ham.toMap, örtükler.toSet, bütün.toSet)
  }

  private def kaynaktakiSahipler: Map[String, Set[String]] = tarama._1

  /** Adın göründüğü türler: doğrudan tanımlayanlar + üstünden kalıtanlar. */
  private def türleriTüret(ad: String): List[String] = {
    val doğrudan = kaynaktakiSahipler.getOrElse(ad, Set.empty)
    üstTürler.collect {
      case (adı, üstler) if doğrudan(adı) || (doğrudan & üstler).nonEmpty => adı
    }.toList.filterNot(gizliTürler).sorted
  }

  @Test
  def kalıplarKipleriTanıyor(): Unit = {
    // Kalıp kaynağı SATIR SATIR tarıyor; tanımadığı bir yazım biçimi hata
    // vermiyor, sessizce atlanıyor. Scala değiştiricileri sırasız kabul
    // ettiği için burada BÜTÜN sıralamalar çivili -- bir sıralamayı kalıba
    // gömme denemesi iki kez ard arda bir kipi dışarıda bıraktı.
    val örtükOlanlar = List(
      "  implicit class Foo[T](x: T) {",
      "  implicit final class Foo[T](x: T) {",
      "  final implicit class Foo[T](x: T) {",
      "  implicit private class Foo[T](x: T) {",
      "  private implicit class Foo[T](x: T) {",
      "  implicit private final class Foo[T](x: T) {",
      "  private[i18n] implicit class Foo[T](x: T) {"
    )
    örtükOlanlar.foreach { satır =>
      val m = sınıfBaşı.findFirstMatchIn(satır)
      assertEquals(s"sınıfBaşı tanımadı: $satır", Some("Foo"), m.map(_.group(2)))
      assertTrue(s"örtük sayılmadı: $satır", m.exists(x => örtükMü(x.group(1))))
    }

    // Örtük OLMAYAN sahipler: sınıfBaşı görmeli, ama örtük sayılmamalı.
    List(
      "case class Eşlem[A, D](m: Map[A, D]) {",
      "object Eşlem {",
      "trait Foo {",
      "sealed abstract class Foo {",
      "private class Foo {"
    ).foreach { satır =>
      val m = sınıfBaşı.findFirstMatchIn(satır)
      assertTrue(s"sınıfBaşı tanımadı: $satır", m.nonEmpty)
      assertTrue(s"yanlışlıkla örtük sayıldı: $satır", !m.exists(x => örtükMü(x.group(1))))
    }

    // Sahip OLMAYAN satırlar hiç eşleşmemeli -- `classOf` ve desen eşleme
    // `class`/`case` sözcüklerini taşıyor ama sınıf başlığı değiller.
    List("    val c = classOf[Int]", "      case c: PIXI.Container => c.children", "  // class Foo")
      .foreach(satır => assertTrue(s"yanlış eşleşme: $satır", sınıfBaşı.findFirstMatchIn(satır).isEmpty))
  }

  @Test
  def herÖrtükSınıfSınıflandırılmış(): Unit = {
    val sınıflandırılmamış = (tarama._2 -- sınıfTürü.keys -- türeSayılmayanlar).toList.sorted
    assertTrue(
      s"${sınıflandırılmamış.size} örtük sınıf ne sınıfTürü'nde ne türeSayılmayanlar'da:\n" +
        sınıflandırılmamış.mkString("\n") +
        "\n(Koleksiyon türüyse sınıfTürü'ne, değilse türeSayılmayanlar'a ekleyin.)",
      sınıflandırılmamış.isEmpty
    )
  }

  @Test
  def haritadaHayaletKayıtYok(): Unit = {
    // Yeniden adlandırılan ya da silinen bir sarmalayıcı haritada kalırsa
    // sessizce ölü kayıt olur; sınıfTürü'ndeki her anahtar kaynakta bulunmalı.
    val hayalet = (sınıfTürü.keys.toSet ++ türeSayılmayanlar -- tarama._3).toList.sorted
    assertTrue(s"kaynakta karşılığı olmayan kayıt: ${hayalet.mkString(", ")}", hayalet.isEmpty)
  }

  @Test
  def yöntemAdlarıBenzersiz(): Unit = {
    // Sonuçlar ada göre bir haritada toplanıyor: aynı ad iki kez geçseydi biri
    // ötekini ezer, yarısı sessizce sınanmamış olurdu.
    val yinelenen = tr.help.koleksiyonYöntemleri.map(_._1).groupBy(identity).filter(_._2.size > 1).keys
    assertTrue(s"tabloda yinelenen ad var: ${yinelenen.mkString(", ")}", yinelenen.isEmpty)
  }

  @Test
  def türListesiKaynakla_uyuşuyor(): Unit = {
    val yanlışlar = tr.help.koleksiyonYöntemleri.flatMap {
      case (ad, _, _, _, _, yazan) =>
        val türler = türleriTüret(ad)
        assertTrue(s"$ad hiçbir Türkçe sarmalayıcıda bulunamadı", türler.nonEmpty)
        // Beşlinin hepsinde varsa alan BOŞ olmalı (eski cümle basılır).
        val olması = if (beşli.forall(türler.contains)) "" else türler.mkString(", ")
        if (yazan == olması) None else Some(s"$ad:\n     yazan  : '$yazan'\n     olması : '$olması'")
    }
    assertTrue(
      s"${yanlışlar.size} satırın tür listesi kaynakla uyuşmuyor:\n" + yanlışlar.mkString("\n"),
      yanlışlar.isEmpty
    )
  }
}
