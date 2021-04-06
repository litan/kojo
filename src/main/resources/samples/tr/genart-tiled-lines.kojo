// Şuradan esinlendi https://generativeartistry.com/tutorials/tiled-lines/

kojoVarsayılanİkinciBakışaçısınıKur()
silVeSakla()
artalanıKur(Renk(60, 63, 65))
val ta = tuvalAlanı
val n = 20 // 20 (yatay) x 20 (dikey) yani 400 dikdörtgene bölelim bütün tuvali
val yatayAdım = ta.eni / n
val dikeyAdım = ta.boyu / n

// Köşegenleri bir "case class" ile tanımlamak ve kullanmak çok kolay
// (x, y) koordinatlarından başlayıp eni kadar sağa ve boyu kadar yukarı
// ama boyu eksi bir sayı olursa, yukarı yerine aşağıya giden köşegen
case class Köşegen(x: Kesir, y: Kesir, eni: Kesir, boyu: Kesir)

// kalemin kalınlığını ve rengini değiştirmeyi dene
def çizgidenResim(ç: Köşegen) =
    kalemBoyu(2) * kalemRengi(beyaz) * götür(ç.x, ç.y) ->
        Resim.köşegen(ç.eni, ç.boyu)

// rastgele iki köşegenden birini seçiyoruz
def çizgi(x: Kesir, y: Kesir, eni: Kesir, boyu: Kesir) = {
    val soldanSağa = rastgeleİkil
    if (soldanSağa) Köşegen(x, y, eni, boyu) else Köşegen(x, y + boyu, eni, -boyu)
}

// tuvalin sol alt köşesinden çizmeye başlayacağız
var çizgiler = Yöney.boş[Köşegen] // aslında çizgileri hesaplayıp bu yöneye kaydedeceğiz
val solAltKöşeninXkoordinatı = ta.x
val solAltKöşeninYkoordinatı = ta.y
yineleİçin(0 until n) { sütun =>
    val x = solAltKöşeninXkoordinatı + sütun * yatayAdım
    yineleİçin(0 until n) { satır =>
        val y = solAltKöşeninYkoordinatı + satır * dikeyAdım
        çizgiler = çizgiler :+ çizgi(x, y, yatayAdım, dikeyAdım)
    }
}
// burada da hepsini çizeceğiz
çiz(çizgiler.map(çizgidenResim))
