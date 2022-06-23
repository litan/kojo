/*
 Mandelbrot Kümesinin (MK) o garip resimlerini görmüşsündür. MK modern
 matematiğin gizemli icat ve keşiflerinin en önde gelenlerindendir
 desek fazla abartmış olmayız. Bu yazılımcıkla MK'nin nereden geldiğini
 görecek ve ne olup bittiğini daha iyi anlayacağız.

 Fareyi tıklayıp sürükleyerek MK'nin istediğin bir köşesine yaklaş.
 Sonra geri okuyla daha önceki pencereye döne. İleri okuyla da
 tekrar ileri git. İlk yaklaşma birkaç saniye alır. Ama ileri/geri okları
 hemen çalışır. Yukarı okuyla biraz yaklaş. Aşağı okuyla da biraz uzaklaş.
 Yukarı oku yaklaşırken farenin konumununu merkez alır. Fareyi oynatarak
 yaklaştığın noktayı seçebilirsin.

 Kare kare film çekmek istersen, O tuşuyla gözüne kestirdiğin bir noktaya
 ortala. Y tuşuyla istediğin kadar yaklaş ve gerektikçe O ile tekrar ortala.
 Eğer merkezden uzak bir nokta seçersen, O tuşu Y tuşunun beş katı zaman
 alabilir. Benim bilgisayarımda Y yaklaşık 2 saniye, O ise en çok 10 saniye
 alıyor. Haberin olsun. İstediğin zaman R tuşuyla geriye doğru hızla oynatarak
 en başa gittikten sonra P tuşuyla ileri oynatabilirsin. P ile de en
 sona gittikten sonra tekrar R tuşuyla geri oynatabilirsin. P tuşundan sonra
 tekrar O ve Y kullanmaya devam edebilir ve istediğin zaman da kameranla
 çekim yapabilirsin.

 P ve R tuşları saniyede yaklaşık 30 çizim oynatırlar ki filmin göze güzel
 ve yumuşak hareketli görünsün. Ama çektiğin filmi yavaşlatarak izlemende
 fayda olacaktır.

 Sana ve arkadaşlarına keyifli keşifler diliyorum.

 Bu örnek şu kaynaktan esinlendi:
     http://justindomke.wordpress.com/2008/11/29/mandelbrot-in-scala/
 Bir de şunlara bakıver:
     https://tr.wikipedia.org/wiki/Mandelbrot_kümesi
     https://en.wikipedia.org/wiki/Mandelbrot_set
 */

/* çizim çok yavaşsa, örneğin beş on saniyeden çok sürüyorsa
 * buradaki değişmez değerleri küçültmeyi dene */
val kenar = 400 // kümemizin resmi k x k büyüklüğünde bir kare
val yinelemeSınırı = 2000 // bu da resmin çözünürlüğünü artırıyor
//tümEkran()

/*
 x*x = 25 denklemini çözüp x=5 demek kolay. Peki x*x = -1 denklemini
 çözümü nedir?  Eskiler çözümü yok demişler uzun yıllar boyu. Neden
 bazı denklemleri çözemediğimizi bir türlü anlayamamışlar. Denklemleri
 zoraki ikiye ayırmışlar, ama pek de iyi olmamış.  Sonunda bir meraklı
 matematikci biraz hayal gücünü kullanmış ve neden olmasın demiş...
 Bir çözüm hayal etmek, başta hayal de olsa, sonra çok faydalı olmuş.
 Onun için ne dersin -1'in kareköküne i diyelim mi? i sayısının
 varlığını kabul edersek artık sayı kavramımızı gerçel sayılardan çok
 daha büyük bir uzaya büyütebiliriz.  Bir boyuttan iki boyuta sıçrarız!
 Bu sayılardan MK ve onun şekline de o sayede varacağız.  Yatay eksen
 gerçel sayılar, dikey eksen de i'nin katlarını yani hayali kısmını
 temsil edecek. Genel olarak her karmaşık sayıyı iki tane gerçel sayı
 olarak ifade edebiliriz. Ama karmaşık (ingilizce complex'ten gelmiş)
 bence pek yakışmıyor bu zengin icada. Biz adını varsıl koyalım. Ne
 dersin? Şöyle yazabiliriz:

    v = (x, y) = x + i * y

 Burada x ve y birer normal (gerçel) sayı.

   v1 = (x1, y1) => v1 + v2 = x1+x2 + i*(y1+y2)
   v2 = (x2, y2) => v1 * v2 = x1*x2 + (x1*y2 + x2*y1)i - y1*y2

 uzunluk: (0, 0) noktasından (x, y) noktasına çizilen doğru
 parçasının uzunluğu
 */

// Lafı uzun oldu, ama yazılımı kısa!
case class VarsılSayı(x: Kesir, y: Kesir) {
    def +(v2: VarsılSayı) = VarsılSayı(x + v2.x, y + v2.y)
    def *(v2: VarsılSayı) = VarsılSayı(x * v2.x - y * v2.y, x * v2.y + v2.x * y)
    def uzunluğu = karekökü(x * x + y * y)
}

// Mandelbrot kümesini yazmadan önce bir de dikdörtgen türü oluşturalım.
// Epey faydalı olacak:
case class Dörtgen(x1: Kesir, x2: Kesir, y1: Kesir, y2: Kesir) {
    val en = x2 - x1
    val boy = y2 - y1
    val alanı = en * boy
    val solalt = Nokta(x1, y1)
    val sağüst = Nokta(x2, y2)
    val ortaNoktası = (x, y)
    val (x, y) = ((x2 + x1) / 2, (y2 + y1) / 2)
    val yazı = {
        val a = alanı
        if (a > 0.0001) s"${yuvarla(a, 5)}" else f"${a}%2.3e"
    }
    val dörtlü = (x1, x2, y1, y2)
    def büyüt(oran: Kesir): Dörtgen = {
        if (oran <= 0 || oran >= 10.0) this else {
            val o2 = 0.5 * oran
            val en2 = o2 * (x2 - x1)
            val boy2 = o2 * (y2 - y1)
            Dörtgen(x - en2, x + en2, y - boy2, y + boy2)
        }
    }
    def ortala(nokta: Nokta) = {
        val xKayma = nokta.x - x
        val yKayma = nokta.y - y
        Dörtgen(x1 + xKayma, x2 + xKayma, y1 + yKayma, y2 + yKayma)
    }
}
object Dörtgen {
    // bazen dört kesir girmek yerine, iki nokta girerek de bir dörtgen yapmak da
    // isteyebiliriz: Dörtgen(Nokta(x, y), Nokta(x2, y2)) şeklinde. bu apply metodu onu sağlıyor
    def apply(solalt: Nokta, sağüst: Nokta): Dörtgen = new Dörtgen(solalt.x, sağüst.x, solalt.y, sağüst.y)
    // hatta farkli girdilerle birden çok apply tanımı yapabiliriz gerekirse. Örneğin:
    def apply(d: Dörtgen, oranX: Kesir, oranY: Kesir): Dörtgen = ??? // oran kesirleriyle çarpalım d'nin köşelerini. (sana bıraktım :)
    def apply(solalt: Nokta, eni: Kesir, boyu: Kesir): Dörtgen = ??? // ya da böyle yapabiliriz. Yaratıcılığa açık bir konu...
}

/*
 Mandelbrot kümesi varsıl sayılarla tanımlanan basit bir işlevle
 ortaya çıkmış.  Normal matematik fonksiyonları f(x) veya g(x) diye
 yazılır. Ama varsıl sayılar kullanınca x yerine v yazmakta ve f(v)
 demekte fayda var. MK'yi aşağıdaki m(v) işleviyle tanımlarız:

     m(n+1) = m(n)*m(n) + v
     m(0) = 0
          =>
     m(1) = v
     m(2) = v^2 + v      (v^2 = v*v)
     m(3) = v^3 + v^2    (v^3 = v*v*v)
     ...

 n büyüdükçe m(n) ufak kalırsa o zaman v sayısı M kümemizin içinde
 oluyor ve onu siyah yapıyoruz. Diğer renkler m'nin sonsuza gitmesine
 neden olan v sayılarını gösteriyor. Ne kadar çabuk m>2 olduğuna
 bakarak renk veriyoruz...
 */

def mKümesi(d: Dörtgen): İmge = {
    bilgiVer(sıra, d)
    sonDörtgen = d
    if (bellek.eşli(d)) bellek(d) else {
        val img = imge(kenar, kenar)
        val oranx = (d.x2 - d.x1) / kenar
        val orany = (d.y2 - d.y1) / kenar
        import renklendirme.renk
        val iri: Kesir = (1.0 * yinelemeSınırı) * kenar * kenar
        zamanTut(f"mKümesi (nokta sayısı x yineleme sınırı)$iri%2.2e yineleme:") {
            for { xi <- 0 |- kenar; yi <- 0 |- kenar } {
                val x = d.x1 + xi * oranx
                val y = d.y1 + yi * orany
                val v = VarsılSayı(kayma + x, y)
                var z = VarsılSayı(0, 0)
                var i = 0
                while (z.uzunluğu < 2 && i < yinelemeSınırı) {
                    z *= z; z += v; i += 1 // işte bütün küme buradan çıkıyor!
                }
                // küme içindeki noktalar hep siyah. diğerleri renkli olacak
                imgeNoktasınıKur(img, xi, yi, if (z.uzunluğu < 2) siyah else (renk(i, x, y)))
            }
        }()
        bellek eşle (d -> img)
        img
    }
}

var eskiUzunluk = 0.0
val epsilon = 0.000001
def bilgiVer(s: Sayı, d: Dörtgen): Birim = {
    val u = VarsılSayı(d.x, d.y).uzunluğu
    satıryaz(f"$s%2d. Alan: ${d.yazı}%-10s " + (
        if (mutlakDeğer(u - eskiUzunluk) < epsilon) "" else
            f"Merkez: (${d.x}%2.8f, ${d.y}%2.8f) Uzunluk: ${u}%2.8f"
    ))
    eskiUzunluk = u
}
object renklendirme {
    /* https://stackoverflow.com/questions/16500656/which-color-gradient-is-used-to-color-mandelbrot-in-wikipedia
       https://en.wikipedia.org/wiki/Monotone_cubic_interpolation  */
    def renk(i: Sayı, x: Kesir, y: Kesir) = {
        val yumuşak = log2(log2(x * x + y * y) / 2)
        renkler(sayıya(karekökü(i + 10 - yumuşak) * 256) % renkler.size)
    }
    lazy val renkler = Dizi.doldur(2048) { i => k2kym(i / 2048.0) }
    def log2(x: Kesir) = logaritması(x) / logaritması(2)
    def k2kym(x: Kesir) = Renk(k2d(x, kd), k2d(x, yd), k2d(x, md))
    // 0 ve 1 arasını beşe bölüp beş renk seçiyoruz. Başladığımız renkle bitiriyoruz.
    // Arasını da yumuşak geçiş benzeri renklerle dolduruyoruz
    val kk = Dizi(0.0, 0.16, 0.42, 0.6425, 0.8575, 1.0)
    val kd = Dizi(0, 32, 237, 255, 0, 0) // kırmızı
    val yd = Dizi(7, 107, 255, 170, 2, 7) // yeşil
    val md = Dizi(100, 203, 255, 0, 1, 100) // mavi
    // önce doğrusal olarak arayı dolduralım -- cubic yapmayı sonraya ve sana bırakıyorum
    def k2d(x: Kesir, rd: => Dizi[Sayı]) = {
        // iki noktayı doğruyla bağlayalım: x1,y1 <-> x2,y2  x1<x<x2. O zaman y nedir?
        def y(x1: Kesir, x2: Kesir, y1: Kesir, y2: Kesir) =
            y1 + (x - x1) * (y2 - y1) / (x2 - x1)
        sayıya(if (x <= kk(0)) rd(0)
        else if (x <= kk(1)) y(kk(0), kk(1), rd(0), rd(1))
        else if (x <= kk(2)) y(kk(1), kk(2), rd(1), rd(2))
        else if (x <= kk(3)) y(kk(2), kk(3), rd(2), rd(3))
        else if (x <= kk(4)) y(kk(3), kk(4), rd(3), rd(4))
        else y(kk(4), kk(5), rd(4), rd(5))
        )
    }
}

/*
 *  Burada çizmeye başlıyoruz
 */
silVeSakla
artalanıKur(siyah)
val resimSolAltKöşe = Nokta(-kenar / 2, -kenar / 2)
val resimGötür = götür(resimSolAltKöşe.x, resimSolAltKöşe.y)
var sıra = 1 // yaklaşma pencerelerinin sırasını çıktı gözüne yazmak için
var sonDörtgen = Dörtgen(0, 0, 0, 0) // son çizdiğimiz kümenin boyutlarını anımsamak gerekli olacak
// Başlangıç penceremizi sola doğru kaydırıyoruz (kayma miktarını x koordinatlarindan çıkarıyoruz).
// Daha önce de yukarıda mKümesi'ni tanımlarken, v'ye aynı kayma miktarını ekledik.
// Peki, neden yaptık bu numarayı? Renklendirme metodunun bir sıkıntısı
// vardı. Onu rahatlatmak için. Pencereyi ve mKümesini aslına döndürürsen görürsün.
val kayma = 4
val başlangıç = Dörtgen(-2 - kayma, 1 - kayma, -1.5, 1.5)
var bellek = Eşlem.boş[Dörtgen, İmge]
var resim = resimGötür -> Resim.imge(mKümesi(başlangıç))
çiz(resim)
fareyiTanımla(resim)

def tuşlarıTanımla() {
    tuşaBasınca { t =>
        t match {
            case tuşlar.VK_SPACE => geri()
            case tuşlar.VK_LEFT  => geri()
            case tuşlar.VK_RIGHT => ileri()
            case tuşlar.VK_UP    => yaklaş(0.90) // fareyi merkez alır
            case tuşlar.VK_DOWN  => uzaklaş(1.25)
            case tuşlar.VK_P     => oynat()
            case tuşlar.VK_R     => geriOynat()
            case tuşlar.VK_O     => ortala()
            case tuşlar.VK_Y     => yaklaş(0.90, yanlış)
            case _               =>
        }
    }
}
tuşlarıTanımla()
// yaklaştıkça bir önceki bakış penceresini saklayalım ki ona geri dönebilelim
// geri gidince de daha ileridekileri saklayacağız ki ileri geri gidebilelim
class Pencere {
    def koy(d: Dörtgen) = bakışlar.koy(d)
    def al(): Dörtgen = bakışlar.al()
    def boşMu() = bakışlar.tane == 0
    def boşalt() = while (!boşMu()) al()
    private val bakışlar = Yığın.boş[Dörtgen]
}
val pGeri = new Pencere // geride kalan yani daha uzaktan bakışlar
val pİleri = new Pencere // ilerideki yani daha yakından bakışlar
def geri(): İkil = {
    if (pGeri.boşMu()) {
        satıryaz("En başa döndük. Daha geri gidemeyiz.")
        doğru
    }
    else {
        sıra -= 1
        resim.sil()
        pİleri.koy(sonDörtgen)
        resim = resimGötür -> Resim.imge(mKümesi(pGeri.al()))
        resim.çiz()
        fareyiTanımla(resim)
        yanlış
    }
}
def ileri(): İkil = {
    if (pİleri.boşMu()) {
        satıryaz("En sona geldik. Daha ileri gidemeyiz.")
        doğru
    }
    else {
        sıra += 1
        resim.sil()
        pGeri.koy(sonDörtgen)
        resim = resimGötür -> Resim.imge(mKümesi(pİleri.al()))
        resim.çiz()
        fareyiTanımla(resim)
        yanlış
    }
}
def yaklaş(oran: Kesir = 0.80, ortala: İkil = doğru, hedef: Nokta = fareKonumu) = {
    sıra += 1
    pGeri.koy(sonDörtgen)
    ayarla(oran, ortala, hedef)
}
def uzaklaş(oran: Kesir = 1.25) = {
    pİleri.koy(sonDörtgen)
    ayarla(oran)
}
def ayarla(oran: Kesir, ortala: İkil = yanlış, hedef: Nokta = fareKonumu) {
    val yeni =
        if (ortala && fareResmeDokunuyor)
            sonDörtgen.büyüt(oran).ortala(nTuvaldenMKye(hedef))
        else sonDörtgen.büyüt(oran)
    resim.sil()
    resim = resimGötür -> Resim.imge(mKümesi(yeni))
    resim.çiz()
    fareyiTanımla(resim)
}
def fareResmeDokunuyor() =
    fareKonumu.x > -kenar / 2 && fareKonumu.x < kenar / 2 &&
        fareKonumu.y > -kenar / 2 && fareKonumu.y < kenar / 2

def ortala() {
    val (hedefX, hedefY) = (fareKonumu.x, fareKonumu.y)
    val (başlangıçX, başlangıçY) = (0, 0)
    val (araX, araY) = (hedefX - başlangıçX, hedefY - başlangıçY)
    val oynamaOranı = enİrisi(
        mutlakDeğer(araX) / tuvalAlanı.en,
        mutlakDeğer(araY) / tuvalAlanı.boy)
    val adımSayısı =
        if (oynamaOranı > 0.10) 5 // yüzde ondan fazla hareket gerekiyor
        else if (oynamaOranı > 0.05) 3
        else 2
    satıryaz(s"$adımSayısı adımda ortalıyoruz")
    val (adımX, adımY) = (araX / adımSayısı, araY / adımSayısı)
    val n = Nokta(başlangıçX + adımX, başlangıçY + adımY)
    yinele(adımSayısı) {
        yaklaş(1.0, doğru, n)
    }
}
def oynat() {
    canlandır {
        if (ileri()) {
            durdur
            tuşlarıTanımla()
        }
    }
}
def geriOynat() {
    canlandır {
        if (geri()) {
            durdur
            tuşlarıTanımla()
        }
    }
}

def nTuvaldenMKye(n: Nokta): Nokta = Nokta(
    sonDörtgen.x1 + (sonDörtgen.en / kenar) * (n.x - resimSolAltKöşe.x),
    sonDörtgen.y1 + (sonDörtgen.boy / kenar) * (n.y - resimSolAltKöşe.y)
)
def tuvaldenMKye(d: Dörtgen): Dörtgen = Dörtgen(nTuvaldenMKye(d.solalt), nTuvaldenMKye(d.sağüst))

def fareyiTanımla(r: Resim) {
    var tıklananXY = (0.0, 0.0)
    var sürüklenenXY = (0.0, 0.0)
    var yaklaşmaKaresi: Resim = Resim.dikdörtgen(0, 0)
    r.fareyiSürükleyince { (x, y) =>
        val farkX = x - tıklananXY._1
        val farkY = y - tıklananXY._2
        val fark = enİrisi(mutlakDeğer(farkX), mutlakDeğer(farkY))
        val yeniX = tıklananXY._1 + fark * işareti(farkX)
        val yeniY = tıklananXY._2 + fark * işareti(farkY)
        yaklaşmaKaresi.sil()
        yaklaşmaKaresi = götür(
            enUfağı(yeniX, tıklananXY._1),
            enUfağı(yeniY, tıklananXY._2)) -> Resim.dikdörtgen(fark, fark)
        çiz(yaklaşmaKaresi)
        sürüklenenXY = (yeniX, yeniY)
    }
    r.fareyiBırakınca { (x, y) =>
        val tx1 = enUfağı(sürüklenenXY._1, tıklananXY._1)
        val tx2 = enİrisi(sürüklenenXY._1, tıklananXY._1)
        val ty1 = enUfağı(sürüklenenXY._2, tıklananXY._2)
        val ty2 = enİrisi(sürüklenenXY._2, tıklananXY._2)
        yaklaşmaKaresi.sil()
        resim.sil()
        val d = tuvaldenMKye(Dörtgen(tx1, tx2, ty1, ty2))
        pGeri.koy(sonDörtgen) // uzaklaşmak için geri dönmek isteyebiliriz
        pİleri.boşalt() // yeni bir dal, eski daldaki ileri pencerelere gerek yok artık
        sıra += 1
        resim = resimGötür -> Resim.imge(mKümesi(d))
        resim.çiz()
        fareyiTanımla(resim)
    }
    r.fareyeBasınca { (x, y) =>
        tıklananXY = (x, y)
    }
}
tuvaliEtkinleştir()
