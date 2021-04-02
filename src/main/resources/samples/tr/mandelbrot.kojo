/* Mandelbrot kümesinin (MK) o garip resimlerini görmüşsündür.
 * MK modern matematiğin en gizemli icat ve keşiflerinin en önde
 * gelenlerindendir desek fazla abartmış olmayız.
 * Bu kısacık yazılımcıkla tam olarak ne olduğunu görecek ve daha iyi anlayacaksın.
 * Fareyi tıklayıp sürükleyerek istediğin kısımlarına yaklaşabilirsin.
 * Sonra da ileri ve geri oklarıyla daha önceki pencerelere dönebilirsin.
 *
 * Bu örnek şu kaynaktan esinlendi:
 *     http://justindomke.wordpress.com/2008/11/29/mandelbrot-in-scala/
 * Bir de şunlara bakıver:
 *     https://tr.wikipedia.org/wiki/Mandelbrot_kümesi
 *     https://en.wikipedia.org/wiki/Mandelbrot_set
 */
tümEkran()
// x*x = -1 denklemini çözümü nedir? Eskiler çözümü yok demişler uzun yıllar boyu.
// Ama bir çözüm hayal etmek, başta hayal de olsa, sonra çok faydalı olmuş.
// Onun için ne dersin -1'in kareköküne i diyelim mi? i sayısını kabul edersek
// artık sayı kavramımızı gerçel sayılardan çok daha büyük bir uzaya büyütebiliriz.
// Bir boyuttan iki boyuta sıçrarız! Bu sayılardan MK şekline de o sayede varacağız.
// Yatay eksen gerçel sayılar, dikey eksen de i'nin katlarını yani hayali kısmını
// temsil edecek. Genel olarak her karmaşık sayıyı iki tane gerçel sayı olarak
// ifade edebiliriz:
//   k = (x, y) = x + i * y    x, y: birer normal (gerçel) sayı
//  k1 = (a1, b1) => k1 + k2 = a1+a2 + i*(b1+b2)
//  k2 = (a2, b2) => k1 * k2 = a1*a2 + (a1*b2 + a2*b1)i - b1*b2
//  uzunluk: (0, 0) noktasından (x, y) noktasına çizilen doğru parçasının uzunluğu
case class VarsılSayı(x: Kesir, y: Kesir) {
    def +(k2: VarsılSayı) = VarsılSayı(x + k2.x, y + k2.y)
    def *(k2: VarsılSayı) = VarsılSayı(x * k2.x - y * k2.y, x * k2.y + k2.x * y)
    def uzunluğu = karekökü(x * x + y * y)
}
/* Mandelbrot kümesi varsıl sayıların bir işlevi (matematik fonksiyonu):
 *  m(n+1) = m(n)*m(n) + v
 *  m(0) = 0
 *    => 
 *  m(1) = v
 *  m(2) = v*v + v
 *  m(3) = v^3 + v^2
 *  ...
 *  n sonsuza gittikçe m ufak kalırsa o zaman v sayısı M kümemizin içinde oluyor
 *  ve onu siyah yapıyoruz. Diğer renkler m'nin sonsuza gitmesine neden olan v
 *  sayılarını gösteriyor. Ne kadar çabuk m>2 olduğuna bakarak renk veriyoruz...
 */ 

// eğer çizmesi çok yavaşlarsa, bunu 600'e indirin:
val kenar = 1200 // kümemizin resmi kenar x kenar büyüklüğünde bir kare olsun

def mKümesi(d: Dörtgen): Image = {
    satıryaz(s"$sıra: " + d.yazı)
    sonDörtgen = d
    val img = imge(kenar, kenar)
    for { xi <- 0 until kenar; yi <- 0 until kenar } {
        val x = d.x1 + xi * (d.x2 - d.x1) / kenar
        val y = d.y1 + yi * (d.y2 - d.y1) / kenar
        val c = VarsılSayı(x, y)
        var z = VarsılSayı(0, 0)
        var i = 0
        while (z.uzunluğu < 2 && i < yinelemeSınırı) {
            z *= z; z += c; i += 1  // işte bütün küme buradan çıkıyor!
        }
        imgeNoktasınıKur(img, xi, yi, if (z.uzunluğu < 2) black else (renk(i)))
    }
    img
}

val yinelemeSınırı = 255 // üçe bölünen bir sayı olsun!
var sıra = 1 // yaklaşma pencerelerinin sırasını çıktı gözüne yazmak için
case class Dörtgen(x1: Kesir, x2: Kesir, y1: Kesir, y2: Kesir) {
    def alanı() = (x2 - x1) * (y2 - y1)
    def yazı = {
        val a = alanı()
        if (a > 0.0001) s"${yuvarla(a, 5)}" else f"${a}%2.3e"
    }
    def dörtlü = (x1, x2, y1, y2)
}
var sonDörtgen = Dörtgen(0, 0, 0, 0)

// rastgele renklerle renklendirelim küme dışındaki noktaları
lazy val renkler = Dizi.doldur(yinelemeSınırı + 1) { n =>
    val x = n % 255 + 1
    Renk(rastgele(x), rastgele(x), 200)
}
def renk(i: Int) = renkler(i)
def renk2(i: Int) = {  // bu da alternatif renklendirme
    val band = yinelemeSınırı / 3
    if (i <= band)
        Renk(sayıya(i * 255.0 / band), 50, 100)
    else if (i <= 2 * band && i > band)
        Renk(75, sayıya((i - band) * 255.0 / band), 25)
    else
        Renk(10, 30, sayıya((i - 2 * band) * 255.0 / band))
}

// yaklaştıkça bir önceki bakış penceresini saklayalım ki ona geri dönebilelim
// geri gidince de daha ileridekileri saklayacağız ki ileri geri gidebilelim
class Pencere {
    import collection.mutable.Stack
    private val bakışlar = Stack.empty[Dörtgen]
    def boşMu() = bakışlar.size == 0
    def üsteKoy(d: Dörtgen) = bakışlar.push(d)
    def üsttenAl(): Dörtgen = bakışlar.pop()
    def boşalt() = while(!boşMu()) üsttenAl()
}
val pGeri = new Pencere  // geride kalan yani daha uzaktan bakışlar
val pİleri = new Pencere // ilerideki yani daha yakından bakışlar

// artık çizebiliriz
silVeSakla
artalanıKur(siyah)

val resimSolAltKöşe = Nokta(-kenar / 2, -kenar / 2)
val resimGötür = götür(resimSolAltKöşe.x, resimSolAltKöşe.y)
var resim = resimGötür -> Resim.imge(mKümesi(Dörtgen(-2, 1, -1.5, 1.5)))
çiz(resim)

tuşaBasınca { t =>
    t match {
        case tuşlar.VK_SPACE => geri()
        case tuşlar.VK_LEFT  => geri()
        case tuşlar.VK_RIGHT => ileri()
        case _               => satıryaz("Geri gitmek için boşluk tuşuna basın")
    }
}
fareyiTanımla(resim)
def geri() {
    if (pGeri.boşMu()) {
        satıryaz("En başa döndük. Daha geri gidemeyiz.")
    }
    else {
        sıra -= 1
        resim.sil()
        pİleri.üsteKoy(sonDörtgen)
        resim = resimGötür -> Resim.imge(mKümesi(pGeri.üsttenAl()))
        resim.çiz()
        fareyiTanımla(resim)
    }
}
def ileri() {
    if (pİleri.boşMu()) {
        satıryaz("En sona geldik. Daha ileri gidemeyiz.")
    }
    else {
        sıra += 1
        resim.sil()
        pGeri.üsteKoy(sonDörtgen)
        resim = resimGötür -> Resim.imge(mKümesi(pİleri.üsttenAl()))
        resim.çiz()
        fareyiTanımla(resim)
    }
}

var tıklananXY = (0.0, 0.0)
var sürüklenenXY = (0.0, 0.0)
var yaklaşmaKaresi: Resim = Resim.dikdörtgen(0, 0)

def fareyiTanımla(r: Resim) {
    r.fareyiSürükleyince { (x, y) =>
        val delx = x - tıklananXY._1
        val dely = y - tıklananXY._2
        val del = enİrisi(mutlakDeğer(delx), mutlakDeğer(dely))
        val newx = tıklananXY._1 + del * delx.signum
        val newy = tıklananXY._2 + del * dely.signum
        yaklaşmaKaresi.sil()
        yaklaşmaKaresi = götür(enUfağı(newx, tıklananXY._1), enUfağı(newy, tıklananXY._2)) -> Resim.dikdörtgen(del, del)
        çiz(yaklaşmaKaresi)
        sürüklenenXY = (newx, newy)
    }

    r.fareyiBırakınca { (x, y) =>
        val bx1 = enUfağı(sürüklenenXY._1, tıklananXY._1) - resimSolAltKöşe.x
        val bx2 = enİrisi(sürüklenenXY._1, tıklananXY._1) - resimSolAltKöşe.x
        val by1 = enUfağı(sürüklenenXY._2, tıklananXY._2) - resimSolAltKöşe.y
        val by2 = enİrisi(sürüklenenXY._2, tıklananXY._2) - resimSolAltKöşe.y
        val (ox1, ox2, oy1, oy2) = sonDörtgen.dörtlü
        val delx = (ox2 - ox1) / kenar
        val dely = (oy2 - oy1) / kenar
        yaklaşmaKaresi.sil()
        resim.sil()
        val d = Dörtgen(ox1 + delx * bx1, ox1 + delx * bx2,
            oy1 + dely * by1, oy1 + dely * by2)
        pGeri.üsteKoy(sonDörtgen) // uzaklaşmak için geri dönmek isteyebiliriz
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
