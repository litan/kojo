// #yükle /samples/tr/oyku-tanimlari

// kodu yazarken todo:
// yükle ~/kojo/include/kojo-kilavuz/ornek-yazilimlar
// release için:
// #yükle /samples/tr/kojo-kilavuz/ornek-yazilimlar

MenüSeçeneği.menüsüVarMı = yanlış

dez sayfaBiçimi = "color:black;background-color:#aaddFF; margin:15px;font-size:small;"
dez ortalamaBiçimi = "text-align:center;"

/*
 * En basit sayfalarla başlayalım ki örnek olsun
 */

dez sayfaSon = Page(
  name = "Son sayfa",
  body = <body style={ sayfaBiçimi + ortalamaBiçimi }>
    { için (i <- 1 to 5) ver { <br/> } }
    <h3><b>Sevgiler</b>, <b> saygılar</b> ve <b>başarılar</b></h3>
    <p>Şimdilik hoşçakal!</p>
    </body>
)

// Yeni bir sayfa oluşturmak da çok kolay. Ama bunda table/çizelge yok!
// Aşağıdaki sayfaY'ye de bak
dez sayfaX = Page(
  name = "BURAYA SAYFANIN ADINI YAZ",
  body = <body style={ sayfaBiçimi + ortalamaBiçimi }>
    { için (i <- 1 to 5) ver { <br/> } }
    <h2> BURAYA SAYFANIN BAŞLIĞINI YAZ H2</h2>
    <h3> BURAYA SAYFANIN BAŞLIĞINI YAZ H3 </h3>
    <p>
    Bu bir paragraf.
    </p>
    <p> Bu başka bir paragraf. </p>
    </body>
)

dez gülerYüz: Yazı = "☺"
dez örnekYazılım1 = """dez dizi = için(
    g <- Aralık(1, 10);
    t <- Dizi(karekökü(2), karekökü(3), piSayısı)
) ver gücü(t, g)
tanım aşağıYukarı(k: Kesir): Kesir = k.sayıya.kesire
// k.sayıya.kesire yerine şunu dene:
// yuvarla(k, 2)
dez d2 = dizi.işle(aşağıYukarı(_)).sıralı
satıryaz(d2.işle(x => f"${x.sayıya}%d").yazıYap(" "))"""

dez sayfaY = Page(
  name = "BURAYA ADINI YAZ 2",
  body = tPage("Bu içinde çizelge (İngilizcesi table) olan sayfa için",
    "Giriş paragrafı".p,
    table(
      row("ilk sıra", "ikinci sütun"),
      row("ikinci sıra: yanında küçük yazılımcık", "dez x = 3.14159".c),
      row("üçüncü sıra: yanında büyük yazılımcık", örnekYazılım1.c)
    ),
    "bu da ikinci paragraf. Bir sayfada birden fazla çizelge de olabilir:".p,
    table(
      row("e sayısı", "ikinin karekökü", "eksi birinki", "4", "5".c, "komik oldu!"),
      row("dez e = 2.718".c, "dez b = 1.4142".c, "dez i = karekökü(-1)".c, "(2, 4)".c, "(2, 5)".c, "(2, 6)".c),
      row("sütun", "sayısı", "farklı", "olsa", "da", "olur", gülerYüz)
    ),
    "Bu da bitiriş paragrafından bir öncesi.".p,
    "Artık bitti bu küçük örnek. Umarız faydalı oldu...".p
  )
)

dez sayfaAra = Page(
  name = "Ara sayfa",
  body = <body style={ sayfaBiçimi + ortalamaBiçimi }>
    { için (i <- 1 to 5) ver { <br/> } }
    <h3>Yinelemekte fayda var</h3>
    <p>
    <b>Yardım</b> menüsündeki <b>Scala'ya Giriş</b> kılavuzuna da
    bir bak. Orada pek çok küçücük örnek var. Onları olduğu gibi çalıştırmak,
  sonra istediğin gibi değiştirip tekrar çalıştırmak çok kolay.
    </p> <p>
    Yanlız kılavuz epey uzun. Giriş dahil 19 bölümden oluşuyor.
    Ama hepsini sırayla okuman gerekmez. İlk iki bölümü okuduktan sonra
    sona yakın olan <em> Çizim ve Oyun </em> bölümüne bak. Onlardan çok şey öğreneceksin.
    </p> <p> Ondan sonra sırayla hepsini oku. Bu sayede sonradan anlayacaksın ki bilgisayar
    programlamayı epey öğrenivermişsin. Ondan sonrası sana ve hevesine kalmış.
    </p>
    </body>
)

dez sayfaİlk = Page(
  name = "Ana sayfa",
  body = <body style={ sayfaBiçimi + ortalamaBiçimi }>

  { /* için (i <- 1 to 5) ver { <br/> } */ }

  <h3>Kojo'ya hoşgeldin!</h3>

  Kojo çok becerikli bir öğrenim ortamı. Kojo diye yazılıyor ama Koco diye okunuyor. <br/><br/>

  Hemen oynayarak öğrenmek için şunlarla başlayabilirsin: <b>Araçlar</b> menüsündeki <em>Yeni Başlayanlar İçin Alıştırmalar</em>, <em> Komut Paleti</em> ve <em> Kaplumbağa Yöneticisi</em>. Ayrıca <b>Örnekler</b> menüsündeki yazılımcıklara da en baştan başlayarak bakmakta fayda var. Ya da sayfayı çevir (aşağıdaki mavi yuvarlak içindeki beyaz üçgene bas) ve bu kılavuzu okumaya devam et. Göreceksin çok kolay. <br/><br/>

  Kojo'nun kaplumbağacığı çok becerikli ve pek çok komuttan anlıyor. Komutların listesi bir sonraki sayfada. Aşağıdaki mavi düğmelerden sağdakine basarak o sayfaya gidebilirsin. Geri dönmek de kolay. Hadi hemen dene. <br/><br/>

  <b>Yardım</b> menüsündeki <b>Scala'ya Giriş</b> kılavuzunu okuyarak da bilgini epey artırabilirsin. <br/><br/>

  Daha sonra <b>Sergi</b> ve <b>Araçlar</b> menülerindeki yazılımlara da göz at. <br/><br/>

  Kojo'yla bilgisayar programlamayı öğrenmeye Internet'ten devam edebilirsin: <br/>

  <a href="http://docs.kogics.net">Kojo Kılavuzu (İngilizce)</a>
  <p>Ama Google tercümanla kolaylıkla Türkçe'ye çevirebilirsin. </p>

  </body>
)


dez sayfaKaplumbağanınAnladığıKomutlar = Page(
  name = "Kaplumbağanın anladığı komutlar",
  body = tPage("Kaplumbağanın anladığı komutlar",
    "Kaplumbağamız yürürken çizgi çizip, boyama yapar. Onu yürüten ve yürürken çizim yaptıran komutları aşağıda A'dan Z'ye sıraladık. Her komut için kısa bir açıklama ve örnekler verdik. Ama önce şu komutları bilmekte fayda var:".p,
    table(
      row("gizle".c, "Kaplumbağayı gizle. Kaplumbağacık görünmese de çizim yapmaya devam eder.", kod120.c),
      row("göster".c, "Kaplumbağayı göster"),
      row("kalemiİndir".c, "Kalemi indir ve çizim yapmaya devam et. Örnek aşağıda."),
      row("kalemiKaldır".c, "Kalemi kaldır ki bundan sonra hareket ederken çizim yapma.", kod100.c),
      row("sil".c, "Tuvali temizler ve kaplumbağayı başlangıç noktasına döndürür"),
      row("silipSakla".c, "Tuvali temizler, kaplumbağayı başlangıç noktasına döndürür ve gizler. Diğer adı 'silVeSakla'", "silVeSakla".c),
    ),
    "Başlangıçta kalem inik. Onun için yürürken çizim yapacak. Üstteki ve alttaki komutarı istediğin sırada çalıştırabilirsin. Tıklaman yeter.".p,
    table(
      row("dön(120, 100)".c, "Yarıçapı 100 olan bir yay çizerek saat yönünün tersine 120 derece döner"),
      row("ev".c, "Eve yani x=0, y=0 noktasına dön ve doğrultuyu kuzeye çevir", "eksenleriGöster".c, "eksenleriGizle".c),
      row("geri(50)".c,"50 adım geriye gider", "geri".c, "ileri(20)".c),
      row("ileri(100)".c, "100 adım ilerler", "ileri".c, "geri(20)".c),
      row("ilerle(-200, -100)".c, "Tuvalde x=-200, y=-100 noktasına ilerle", "ızgarayıGöster".c, "ızgarayıGizle".c),
      row("sağ(60, 100)".c, "Sağa doğru 60 derecelik bir yay çiz. Yarıçapı 100 uzunluğunda olan bir çemberin yayı olsun"),
      row("sol(120, 50)".c, "Sola doğru 120 derecelik bir yay çiz. Yarıçapı 100 uzunluğunda olan bir çemberin yayı olsun"),
      row("yay(100, -135)".c, "dön komutu gibi ama önce yarıçapı giriyoruz sonra yayın açısını"),
    ),
    "Yönünü belirleyen ve yerini değiştiren ama çizim yapmayan komutlar (A'dan Z'ye sıralı):".p,
    table(
      row("atla(200, 100)".c, "Çizim yapmadan x=200, y=100 konumuna atla", "atla(0, 0)".c, "atla(-100, -200)".c),
      row("açıyaDön(210)".c, "Doğrultunu 210 açısına çevir", "kuzey".c, "açıyaDön(-45)".c),
      row("batı".c, "Doğrultunu batıya çevir. Benzer komutlar yanda", "doğu".c, "güney".c, "kuzey".c),
      row("doğu".c, "Doğrultunu doğuya çevir. Benzer komutlar yanda", "batı".c, "güney".c, "kuzey".c),
      row("dön(30)".c, "Saat yönünün tersine doğru 30 derece döner. Eksi girersen saat yönünde döner"),
      row("güney".c, "Doğrultunu güneye çevir. Benzer komutlar yanda", "batı".c, "doğu".c, "kuzey".c), 
      row("konumuDeğiştir(40, 30)".c, "Doğrultuyu değiştirmeden 40 sağa, 30 yukarı git", "konumuDeğiştir(-50, -50)".c),
      row("konumuKur(150, 100)".c, "Doğrultuyu değiştirmeden x=150, y=100 konumuna git"),
      row("konumVeYönüBelleğeYaz".c, "Şu anda bulunduğu konumu ve doğrultuyu belleğe yaz"),
      row("konumVeYönüGeriYükle".c, s"Bellekteki konuma git ve yine bellekteki doğrultuya dön. Eğer belleğe konum ve yön yazılı değilse hata verir: '${Utils.loadString("S_PROBLEM_DUE_TO_EXCEPTION")} - java.lang.IllegalStateException: No saved Position and Heading to restore.' Yani bellekte bilgi yok henüz diyor."),
      row("kuzey".c, "Doğrultunu kuzeye çevir. Benzer komutlar yanda", "batı".c, "doğu".c, "güney".c),
      row("noktayaDön(40, 60)".c, "Doğrultunu koordinatları verilen (x, y) noktasına çevir"),
      row("noktayaGit(0, -100)".c, "Doğrultunu x=100, y=-100 konumuna çevir ve o noktaya kadar ilerle"),
      row("noktayaGit(Nokta(40, 30))".c, "Yukardaki komutu böyle de çağırabiliriz. Programlama yaparken faydalı olur"),
      row("sağ".c, "Sağa döner. Yani saat yönünde 90 derece döner"),
      row("sağ(60)".c, "Sağa doğru 60 derece döner", "sağ(10)".c, "sağ(45)".c),
      row("sol".c, "Sola döner. Yani saat yönünün tersinde 90 derece döner"),
      row("sol(65)".c, "Sola doğru 45 derece dön", "sol(10)".c, "sol(45)".c),
      row("zıpla()".c, "25 adım öteye zıpla"),
      row("zıpla(100)".c, "100 adım öteye zıpla"),
    ),
    "Basit çizim komutları:".p,
    table(
      row("daire(20)".c, "Yarıçapı 20 olan bir daire çiz.", "üçgen(100)".c, "kare(100)".c, "nokta(100)".c),
      row("kare(30)".c, "Kenar uzunluğu 30 olan bir kare çiz", "kare()".c),
      row("nokta(30)".c, "30 kalınlığında bir kalemle bir nokta çiz", "zıpla; nokta(10)".c, "ileri(100); nokta".c),
      row("üçgen(30)".c, "Kenar uzunluğu 30 olan bir üçgen çiz"),
    ),
    "Şimdilik son bir örnek daha görelim. Biraz daha uzun. Ama sakın irkilme. Bunun çoğu sayfanın başındaki ilk örnekle aynı. yinele(3) döngüsünü aldık  ve iki döngü içinde tekrar kullandık:".p,
"""sil
canlandırmaHızınıKur(2) // çokHızlı'dan iki kat daha hızlı
// daha önce kullandığımız hızıKur(hız) komutuna benziyor,
// ama daha hassas ayar yapmamıza yarıyor. Girdisi adım atma süresini belirliyor
// onun için de bu iki komut ters çalışıyor:
// hız tarifi ve karşılık gelen adım atma süreleri yaklaşık olarak şöyle:
//   çokHızlı: 1
//   hızlı:    10
//   orta:     100 (varsayılan)
//   yavaş:    1000
yaklaş(0.2)
kalemKalınlığınıKur(20)
yinele(12) {
    sağ(30)
    yinele(10) {
        ileri(20)
        yinele(3) {  // gerisi ilk örnekle aynı
            ileri(100)
            dön(120)
            ileri(100)
        }
    }
}""".c
  )
)

dez sayfaTuvalKomutları = Page(
  name = "çizim",
  body = tPage("Çizim ve boyamayla ilgili komutlar",
    "Bazı komutlar kaplumbağanın nasıl çizim ve boyama yapacağını belirliyor. Örneğin:".p,
    table(
      row("biçimleriBelleğeYaz".c, "Kurulu biçim ayarlarını belleğe yaz: kalem rengi, kalınlığı ve yazısı, inik mi, kalkık mı ve boyama rengi", kod10.c),
      row("biçimleriGeriYükle".c, "Bir önceki komutla kaydedilen biçim ayalarını geri yükler"),
      row("kalemKalınlığınıKur(5)".c, "Başlangıçta 2 kalınlığıyla çizer.", kod110.c),
      row("kalemRenginiKur(mavi)".c, "Kırmızı yerine mavi kalemle çiz", "boyamaRenginiKur(yeşil); üçgen()".c),
      row("boyamaRenginiKur(mavi)".c, "Şekilleri maviye boya. Kenarların rengi kalemRenginiKur komutuyla değişir.", kod30.c, kod30b.c, kod30c.c),
    ),
    "Tuvalle ilgili komutlar:".p,
    table(
      row("canlandırmaHızınıKur(10)".c, "100 adımı atması 10 milisaniye alsın. Başlangıçta 1000 milisaniye aldığı için 100 kat hızlanır!"),
      row("eksenleriGizle()".c, "X ve Y eksenlerini saklar"),
      row("eksenleriGöster()".c, "X ve Y eksenlerini gösterir"),
      row("ızgarayıGizle()".c, "Izgarayı gizler. Bir başka adı da grid.", "gridiGizle()".c),
      row("ızgarayıGöster()".c, "Izgarayı çizer", "gridiGöster()".c),
      row("hızıKur(hızlı)".c, "canlandırmaHızınıKur komudunun basit hali", kod150.c),
      row("ışınlarıAç".c, "Dört yöne ışın tut. Öne doğru olan uzun olsun"),
      row("ışınlarıKapat".c, "Işınları söndür"),
      row("""tuvaleYaz("merhaba dünya!")""".c, "Yazı komutunun uzun adı. Orda ki örneğe de bak", kod130.c),
      row("tuvaliKaydır(100, 50)".c, "", "üçgen(); tuvaliKaydır(-100, -50)".c),
      row("tuvaliDöndür(45)".c, "", "üçgen(); tuvaliDöndür(-45)".c),
      row("yaklaş(3.0)".c, "Tuvale verilen oranda yaklaşarak çizimleri daha büyük göster.", kod140.c, kod140b.c),
      row("yaklaş(2.0, 200, 50)".c, "x=200, y=50 konumunu merkez alarak yaklaş ya da uzaklaş", "yaklaşmayıSil".c, "yaklaş(1.0, 0, 0)".c),
      row("yaklaşmayaİzinVerme()".c, ""),
      row("yaklaşmayıSil()".c, ""),
      row(yazı_Örnek.c, "tuvaleYaz komutunun kısa adı. Durduğu konumun hemen sağına verilen yazıyı yazar. Girdi yazı değilse yazıya çevrilir önce.", kod60.c, kod60b.c, kod60c.c, kod60d.c, "yaklaşmayıSil()".c),
      row("yazıBoyunuKur(24)".c, "Başlangıçta 18 boyunda yazar.", kod70.c),
      row("yazıyüzleri".c, ""),
      row(yazıYüzünüKur_Örnek.c, "Kaplumbağanın yazısının görünüşünü değiştirir"),
    ),
    "Bilgi veren komutlar:".p,
    table(
      row("canlandırmaHızı".c, "Kaplumbağanın 100 adım atması kaç milisaniye alır? Onu bildir. Başlangıçta 1000 milisaniye sürer. Örnekte de gördüğümüz gibi hızlandırılabilir.", kod40.c),
      row("doğrultu".c, "Şu anda baktığımız doğrultuyu açı olarak çıktıyla bildirir. 180 sola bakıyor demek. 270 de aşağıya"),
      row("kalemİnikMi".c, "Bu bilgi programlama yaparken faydalı olabilir", "kalemiİndir".c, "kalemiKaldır".c),
      row("konum".c, "Şu anki konumu bildir"),
    ),
    "Kaplumbağanın görünüşünü değiştiren komutlar:".p,
    table(
      row("birsonrakiGiysi".c, "Kurulu giysilerden bir sonrakini giy. Sonuncuyu giymişse en baştakine döner. Tek giysi varsa hiç birşey yapmaz", kod20.c),
      row("giysiKur(Görünüş.araba)".c, "Kaplumbağamızı tamamen değiştirmek de mümkün."),
      row(giysileriKur_Örnek.c, "Kaplumbağanın bir dizi giysisi olsun ki giysisini kolaylıkla değiştirebilelim.", kod50.c),
      row("giysiyiBüyült(2.0)".c, "Küçültmek için de 0.5 dene."),
      row("görünmez".c, "gizle komutuyla eş: kaplumbağayı gizler"),
      row("görünür".c, "göster komutuyla eş: gizliyse, kaplumbağayı gösterir"),
    ),
    "Kaplumbağamız bir yazılım nesnesi olduğu için komutlarını bir nesnenin yöntemi olarak da çağırabiliriz. Ama hangi nesne? Aşağıdaki ilk satıra bak.".p,
    "Scala ve Kojo'daki herşey bir yazılım nesnesi ve bunlara bizim kaplumbağamız da dahil! Adı aşağıda. Hatta bir kaplumbağa ile yetinmek zorunda değiliz. Birkaç tane kaplumbağa olsa daha da çok çizim yapabilir, hatta birbirleriyle etkileşime sokabiliriz. Yeni kaplumbağalar oluşturmak ve onlara çizim yaptırmak için aşağıdaki komutlara bakıver. Örnekler menüsünün Çoğul Kaplumbağa adlı alt menüsündeki örnek yazılımcıklara bakmayı da unutma!".p,
    table(
      row("kaplumbağa0".c, "Kaplumbağamızın nesne adı bu.", "kaplumbağa0.ileri(100)".c, "kaplumbağa0.ev".c),
      row("yeniKaplumbağa(100, 100)".c, "x=100 y=100 noktasında yeni bir kaplumbağa canlandırır.", kod80.c, "kaplumbağa0.sil".c),
    )
  )
)

dez sayfaDiğerKomutlar = Page(
  name = "daha çok komut var",
  body = tPage("Diğer komutlar, yöntemler, işlevler, değişmezler",
    "Diğer bütün Türkçe komutları da burada sıralayalım küçük örneklerle...".p,
    table(
      row("ilk sıra", "ikinci sütun"),
      row("ikinci sıra: yanında küçük yazılımcık", "dez x = 3.14159".c),
      row("üçüncü sıra: yanında büyük yazılımcık", örnekYazılım1.c)
    ),
    "bu da ikinci paragraf. Bir sayfada birden fazla çizelge de olabilir:".p,
    table(
      row("e sayısı", "ikinin karekökü", "eksi birinki", "4", "5".c, "komik oldu!"),
      row("dez e = 2.718".c, "dez b = 1.4142".c, "dez i = karekökü(-1)".c, "(2, 4)".c, "(2, 5)".c, "(2, 6)".c),
      row("sütun", "sayısı", "farklı", "olsa", "da", "olur", gülerYüz)
    ),
    "Bu da bitiriş paragrafından bir öncesi.".p,
    "Artık bitti bu küçük örnek. Umarız faydalı oldu...".p
  )
)

/*
 Sonunda sayfaların hepsini bir araya koyalım:
 */
// kodu yazarken: todo
// yükle ~/kojo/include/kojo-kilavuz/komutlar-genel.kojo
// yükle ~/kojo/include/kojo-kilavuz/ek-sayfa.kojo
// yükle ~/kojo/include/kojo-kilavuz/turler
// release için:
// #yükle /samples/tr/kojo-kilavuz/komutlar-genel
// #yükle /samples/tr/kojo-kilavuz/ek-sayfa
// #yükle /samples/tr/kojo-kilavuz/turler

// dez öykü = Story(sayfaKomutlar, sayfaİlk, sayfaAra, /* sayfaX, */ /* istediğin kadar sayfa ekleyebilirsin... */ sayfaSon)
dez öykü = Story(sayfaİlk, sayfaKaplumbağanınAnladığıKomutlar, sayfaTuvalKomutları, /* sayfaDiğerKomutlar, */ sayfaAra, komutlarGenel.sayfa, türler.sayfa, /*ek.sayfa, sayfaX, sayfaY, */ /* istediğin kadar sayfa ekleyebilirsin... */ sayfaSon)
stClear() // öyküyü temizle
stAddLinkHandler("example", öykü) {idx: Int =>
  stSetScript(codeExamples(idx))
  stClickRunInterpreterButton()
  clearOutput
}
stPlayStory(öykü) // öyküyü anlatmaya başla
