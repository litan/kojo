nesne türler {

  dez kodXX = """dez foo = "bar"
yaz(foo)"""

  dez kod10 = """dez n: Nesne = yeni Nesne()
n.aynıMı(n)"""
  dez kod20 = """// ekrana yazmak bir yan etki
// Yani işlevsel bir çıktı değil
tanım söyle(adın: Yazı): Birim =
    yaz(s"merhaba $adın")

tanım satırBaşı(): Birim =
    satıryaz("")

söyle("Ayşe")
satırBaşı
söyle("Ali")

// Bu da faydasız bir değişmez
dez b: Birim = ()
b // birim etki yok etki"""
  dez kod25 = """dez çatal1: İkil = (doğru || yanlış)
dez çatal2: Seçim = (doğru && yanlış)"""
  dez kod30 = """için(s <- Diz(doğru, yanlış, doğru))
    yaz(eğer(s) "Doğru "
    yoksa "Yanlış ")"""
  dez kod40 = """dez b1: Lokma = 1 << 6
yaz(Lokma.EnUfağı, Lokma.Enİrisi)
// Hata verir "type mismatch":
// dez b2: Lokma = 1 << 7"""
  dez kod50 = """satıryaz(Uzun.Enİrisi + 2)
dez i = İriSayı(Uzun.Enİrisi)
satıryaz(i * i * i)"""
  dez kod60 = """dez ufak = 1.000001
dez payda = 10000000.0
satıryaz(ufak / payda)
dez x = İriKesir(ufak)
satıryaz(x / payda)"""
  dez kod70 = """dez h = '☺'
dez j = (h + 1).harfe
yaz((h - 1).harfe, h, j)"""
  dez kod80 = """dez ey = yeni EsnekYazı("Merhaba ")
dez ad = satıroku("Adın ne?")
ey.ekle(ad)
ey.ekle("! Ne var ne yok?")
satıryaz(ey)"""

  dez kod1020 = """Sayılar(0, 1, 2, 3)
    .işle(s => 10 * s * s)
    .herbiriİçin(yaz)"""

  dez kod1510 = """dez bkk = BKK(
  "https://upload.wikimedia.org/",
  "wikipedia/commons/thumb/a/a5/",
  "Flower_poster_2.jpg/",
  "330px-Flower_poster_2.jpg")
Resim.imge(bkk).veBüyüt(0.3).çiz"""

  dez kod1710 = """belirt(
  5 < 7,
  "ilk girdi ikinciden küçük olmalı")
// 5 yerine 8 yazıp tekrar çalıştır
"""

  dez kod1720 = """durum sınıf Kişi(ad: Yazı, yaş: Sayı) {
  gerekli(yaş > 0 && yaş < 1000,
    "kişinin yaşı yanlış")
  satıryaz(s"$ad $yaş yaşında")
}
çıktıyıSil
dez k1 = Kişi("Mustafa Kemal", 143)
 // bir de 1 yerine -1 girip çalıştır
dez k2 = Kişi("Garip Durum", 1)"""

  dez kod1730 = """// bazen ne istediğimizi biliriz
// ama nasıl olacağını bilemeyiz ya,
// o zaman böyle boş bir tanım
// yazmak faydalı olabilir
tanım deney1(girdi: Sayı) = ???
// bunu doğru yapıp tekrar çalıştır:
dez hazırsa = yanlış
eğer(hazırsa) deney1(42)"""

  dez kod2010 = """silVeSakla
dez ay_uzunluk = ay.Yazıgirdisi(60)
dez ay_renkler = ay.Salındıraç("mavi", "yeşil", "sarı")
Resim.arayüz(ay.Tanıt("Sayı ve Renk Seç:")).veGötür(-100,120).çiz
Resim.arayüz(ay_uzunluk).veGötür(-100, 100).çiz
Resim.arayüz(ay_renkler).veGötür(-100, 80).çiz
Resim.arayüz(ay.Düğme("Sayı ve Renk Seçimlerini Yaz"){
  satıryaz("=" * 10)
  satıryaz(s"Sayı: ${ay_uzunluk.değeri}")
  satıryaz(s"Renk: ${ay_renkler.değeri}")}
).veGötür(-100, 60).çiz"""

  dez kod2020 = """dez solAltKöşe = Nokta(tuvalAlanı.x, tuvalAlanı.y)
dez sağAltKöşe = Nokta(tuvalAlanı.x + tuvalAlanı.eni, tuvalAlanı.y)
dez solÜstKöşe = Nokta(tuvalAlanı.x, tuvalAlanı.Y)
dez sağÜstKöşe = Nokta(tuvalAlanı.x + tuvalAlanı.eni, tuvalAlanı.Y)
dez hepsi = yeni Dikdörtgen(solAltKöşe, sağÜstKöşe)
yaz(hepsi)"""

  dez sayfa = Page(
    name = "türler",
    body = tPage("Her değerin bir Türü var",
      "Burada ilk sütunda Türkçe türleri sıraladık. İkinci sütunda İngilizce karşılıkları var. Scala'daki her yazılım parçacığı bir nesne. Her nesnenin de bir türü var. Türlerin hepsi de birbirine bağlı. Bunu bir çizimle görmekte fayda var. İngilizcesini şu İnternet sayfasında görebilirsin:".p,
      "Yönlü tür çizgesi (directed graph of Scala types)".link("docs.scala-lang.org/resources/images/tour/unified-types-diagram.svg"),
      "Türlerin birliği önemli bir konu. Hakkında daha çok bilgi aşağıda:".p,
      "Scala turundan sayfa.".link("docs.scala-lang.org/tour/unified-types.html"),
      table(
        // tr/package.scala
        row("Nesne".c, "Object".c, kod10.c, "Herşey bir Nesne olabilir mi? Evet! Java nesnelerinin temel türü"),
        row("Birim".c, "Unit".c, kod20.c, "İşlevsel bir çıktısı olmayan yöntemler ve komutların çıktı türü"),
        row("Her".c, "Any".c, "dez h: Her = 1".c, "Her Scala nesnesinın temel türü. Kendi temeli de Nesne. Yönlü tür çizgesinin başlama noktası"),
        row("HerDeğer".c, "AnyVal".c, "dez h: HerDeğer = 2".c, "Her Scala değerinin temel türü. Kendi temeli de Her."),
        row("HerGönder".c, "AnyRef".c, "dez h: HerGönder = Diz(1, 2, 3)".c, "İşlevlerin, karmaşık ve çoğul içeriği olan türlerin temeli. Ref yani İngilizce 'Reference' sözcüğünü gönderge olarak çevirdik. Gösteren de diyebiliriz. İngilizce 'pointer' sözcüğü de benzer bir kavramdır."),
        row("Hiç".c, "Nothing".c, "dez y: Hiç = ???".c, "En özel ve hiç nesnesi olmayan tek tür. Onun için de bu örneği çalıştırınca hata verir. Yönlü tür çizgesinin bitiş noktası yani en özel tür bu nesnesiz tek türdür."),
        row("Yok".c, "Null".c, "dez y: Yok = yok".c, "Java'dan kalma. Hiç kullanma!"),
        row("İkil".c, "Boolean".c, kod25.c, "En sade ve basit türlerden biri. Sadece iki değeri var. Ama çok da faydalı"),
        row("Seçim".c, "Boolean".c, kod30.c, "O kadar temel bir tür ki Türkçeye çevirirken iki isim verdik ona!"),
        row("Lokma".c, "Byte".c, kod40.c, "Bir Lokma aslında sekiz parçadan oluşuyor. O küçük parçaların İngilizce adı 'Bit.' Her parça aslında bir İkil ya da bir Seçim"),
        row("Kısa".c, "Short".c, "Kısa.Enİrisi".c, "Eğer küçük sayılarlaysa işimiz, o zaman bu tür yeter bize"),
        row("Sayı".c, "Int".c, "Sayı.Enİrisi".c, "Genelde kullandığımız sayma sayılarının türü budur"),
        row("Uzun".c, "Long".c, "Uzun.Enİrisi".c, "Eğer epey büyük sayılar gerekiyorsa, Sayı türü yerine bunu kullanırız"),
        row("İriSayı".c, "BigInt".c, kod50.c, "Sayıların sınırı olmasın dersek, bu türü kullanabiliriz. Ama hem daha çok bellek tutar hem de daha uzun sürer çalışması"),
        row("Kesir".c, "Double".c, "Kesir.Enİrisi".c, "Kesirli sayıların türü. Epey büyüktür en irileri. İki lokma bellek tutar her kesir sayı"),
        row("UfakKesir".c, "Float".c, "UfakKesir.Enİrisi".c, "Kesirli sayıların tek lokma yer tutanı"),
        row("İriKesir".c, "BigDecimal".c, kod60.c, "Kesirli sayıların en hassası (ama çok lokma yer tutar!)"),
        row("Harf".c, "Char".c, kod70.c, "Char ve Character diye iki tür var. Bizim Harf ikisini de kapsıyor"),
        row("Yazı".c, "String".c, """"*-*" * 5""".c, "Çok işe yarar bu tür. Scala'ya Giriş kılavuzunda kendi sayfası ve pek çok örneği var"),
        row("EsnekYazı".c, "StringBuilder".c, kod80.c, "Bu da biraz daha esnek olanı. Şuradan: collection.mutable"),
        row("".c, "".c, "".c),
        row("Belki".c, "Option".c, "".c),
        row("Biri".c, "Some".c, "".c),
        row("Hiçbiri".c, "None".c, "".c, "Aslında aynı adlı yegane bir değişmez değerin türü"),
        row("".c, "".c, "".c),
        row("Aralık".c, "Range".c, "".c),
        row("Diz".c, "collection.Seq".c, "".c),
        row("Dizi".c, "Seq".c, "".c),
        row("Dizik".c, "Array".c, "".c),
        row("Dizim".c, "Array".c, "".c, "Bu Dizik'in eskisi"),
        row("EsnekDizim".c, "ArrayBuffer".c, "".c, "eskisi. collection.mutable'dan"),
        row("EsnekDizik".c, "ArrayBuffer".c, "".c, "collection.mutable'dan"),
        row("Dizin".c, "List".c, "".c),
        row("SıralıDizi".c, "IndexedSeq".c, "".c),
        row("Eşlek".c, "Map".c, "".c, "collection.immutable.Map"),
        row("Eşlem".c, "Map".c, "".c, "collection.mutable.Map"),
        row("Küme".c, "Set".c, "".c),
        row("MiskinDizin".c, "LazyList".c, "".c),
        row("".c, "".c, "".c),
        row("Kuyruk".c, "Queue".c, "".c, "collection.mutable"),
        row("ÖncelikSırası".c, "PriorityQueue".c, "".c, "collection.mutable"),
        row("Yığın".c, "Stack".c, "".c, "collection.mutable"),
        row("".c, "".c, "".c),
        row("Yöney".c, "Vector".c, "".c, "collection.immutable"),
        row("Yineleyici".c, "Iterator".c, "".c),
        row("".c, "".c, "".c),
        row("Gelecek".c, "Future".c, "".c, "scala.concurrent"),
        row("İşletimBağlamı".c, "ExecutionContext".c, "".c, "scala.concurrent"),
        // trInit.scala
        row("".c, "".c, "".c),
        row("Sayılar".c, "Vector[Int]".c, kod1020.c),
        row("".c, "".c, "".c),
        row("Boya".c, "Paint".c, "".c, "java.awt'den"),
        row("Renk".c, "Color".c, "".c, "java.awt'den"),
        row("Yazıyüzü".c, "Font".c, "".c, "java.awt"),
        row("Hız".c, "Speed".c, "".c, "net.kogics.kojo.core'dan"),
        row("Nokta".c, "Point".c, "".c, "net.kogics.kojo.core'dan"),
        row("Dikdörtgen".c, "Rectangle".c, "".c, "net.kogics.kojo.core'dan"),
        row("Üçgen".c, "Triangle2D".c, "".c, "io.github.jdiemke.triangulation'dan"),
        row("Yöney2B".c, "Vector2D".c, "".c, "net.kogics.kojo.util'den"),
        row("Resim".c, "Picture".c, "".c),
        row("".c, "".c, "".c),
        row("BuAn".c, "Now".c, "BuAn().yazıya".c, "Now adında bir tür yok, ama BuAn adında bir durum sınıfımız yani türümüz var"),
        row("Takvim".c, "Calendar".c, "".c, "java.util"),
        row("Tarih".c, "Date".c, "".c, "java.util"),
        row("SaatDilimi".c, "TimeZone".c, "".c, "java.util"),
        row("".c, "".c, "".c),
        row("Bölümselİşlev".c, "PartialFunction".c, "".c),
        row("İşlev1".c, "Function1".c, "".c),
        row("İşlev2".c, "Function2".c, "".c),
        row("İşlev3".c, "Function3".c, "".c),
      ),
      "İlerledikçe faydalı olacak türler".p,
      table(
        row("KuralDışı".c, "Exception".c, "".c),
        row("ÇalışmaSırasıKuralDışı".c, "RuntimeException".c, "".c),
        row("".c, "".c, "".c),
        row("BaskınYazıyaYöntemiyle".c, "".c, "".c, "bir tür özelliği"),
        row("Eşsizlik".c, "".c, "".c, "bir tür özelliği"),
        row("".c, "".c, "".c),
        row("UzunlukBirimi".c, "UnitLen".c, "".c, "net.kogics.kojo.core'dan"),
        row("Biçim".c, "Shape".c, "".c, "java.awt'den"),
        row("GeoYol".c, "GeneralPath".c, "".c, "java.awt.geom'dan"),
        row("GeoNokta".c, "VertexShape".c, "".c, "net.kogics.kojo.core'dan"),
        row("Grafik2B".c, "Graphics2D".c, "".c, "scala.swing'den"),
        row("İmge".c, "Image".c, "".c, "java.awt.Image"),
        row("İmgeİşlemi".c, "ImageOp".c, "".c, "net.kogics.kojo.picture.ImageOp"),
        row("Bellekteİmge".c, "BufferedImage".c, "".c, "java.awt.image'dan"),
        row("Bellekteİmgeİşlemi".c, "BufferedImageOp".c, "".c, "java.awt.image'dan"),
        row("ÇiniDünyası".c, "tiles".c, "".c, "net.kogics.kojo.tiles"),
        row("ÇiniXY".c, "TileXY".c, "".c, "tiles'dan"),
        row("BirSayfaKostüm".c, "SpriteSheet".c, "".c, "tiles'dan"),
        row("Mp3Çalar".c, "KMp3".c, "".c, "net.kogics.kojo.music'den"),
        row("Canlandırma".c, "Animation".c, "".c, "net.kogics.kojo.???'den"),
        row("BKK".c, "URL".c, kod1510.c, "Birörnek Kaynak Konumlayıcısı: java.net.URL'den"),
        row("".c, "".c, "".c),
        row("BelirtimHatası".c, "java.lang.AssertionError".c, kod1710.c, "".c),
        row("KuraldışıGirdiHatası".c, "java.lang.IllegalArgumentException".c, kod1720.c, "".c),
        row("EksikTanımHatası".c, "scala.NotImplementedError".c, kod1730.c, "".c),
        row("SınırDışınaTaşmaHatası".c, "java.lang.IndexOutOfBoundsException".c, "".c),
        row("BoşGöstergeHatası".c, "java.lang.NullPointerException".c, "".c),
        row("MatematikselHata".c, "java.lang.ArithmeticException".c, "".c),
        row("İşParçacığıÖlümü".c, "java.lang.ThreadDeath".c, "".c),
      ),
      "Tür eşi olmayan nesneler de var. Onların yöntemleri çok işimize yarar:".p,
      table(
        row("ay".c, "UI".c, "arayüz'ün kısaltması", kod2010.c),
        row("tuvalAlanı".c, "canvasBounds".c, "tuval alanı hakkında faydalı bilgiler", kod2020.c),
      ),
      "Şimdilik bu kadar. Devamı yarın".p,
    )
  )

}
