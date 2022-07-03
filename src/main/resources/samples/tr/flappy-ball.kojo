// Yukarı ve aşağı oklarıyla kanatlı topa yön vererek
// engellere çarpadan bir dakika uçurabilir misin?
// Dikkat et, tavana çarpma ve yere düşürme!

silVeSakla()
çizSahne(siyah)
/* ekranTazelemeHızınıGöster(kırmızı, 20)
   ekranTazelemeHızınıKur(50) */

val oyunSüresi = 60  // istersen değiştir!
val hız = -5 // engellerin yatay hızı
val topunHızı = 5  // dikey hız, iniş ve yükseliş için
val yerçekimi = 0.1
var düşüşHızı = 0.0
val topunZarfı = götür(49, 31) -> Resim.daire(30)
val top = Resim.küme(
    Resim.imge("/media/flappy-ball/ballwing1.png", topunZarfı),
    Resim.imge("/media/flappy-ball/ballwing2.png", topunZarfı)
)
çiz(top)
çizVeSakla(topunZarfı)

val ta = tuvalAlanı
var engeller = Küme.boş[Resim]
def engelKur() {
    val boy = rastgele((0.5 * ta.boyu).sayıya) + 50
    val (x, y) = (ta.eni, if (rastgeleİkil) ta.boyu / 2 - boy else -ta.boyu / 2)
    val engel = boyaRengi(renkler.blueViolet) * kalemRengi(renksiz) *
        götür(x, y) -> Resim.dikdörtgen(rastgele(30) + 30, boy)
    engeller += engel
    çiz(engel)
}

yineleSayaçla(1000) {
    engelKur()
}

canlandır {
    engeller herbiriİçin { engel =>  // engellerin herbiri içın yapmamız gereken şeyler
        if (engel.konum.x + 60 < ta.x) {  // tuvalin soluna geçenleri silelim
            engel.sil()
            engeller -= engel
        }
        else { // diğerlerini hareket ettirelim ve top çarparsa oyunu durdurarlım
            engel.götür(hız, 0)
            if (top.çarptıMı(engel)) {
                top.saydamlığıKur(0.3)
                mesajVer("Tekrar dene", kırmızı)
                durdur()
            }
        }
    }
    top.sonrakiniGöster()  // topun kanatlarını çırpıyoruz
    if (tuşaBasılıMı(tuşlar.VK_UP)) {
        düşüşHızı = 0
        top.götür(0, topunHızı)
    }
    else if (tuşaBasılıMı(tuşlar.VK_DOWN)) {
        düşüşHızı = 0
        top.götür(0, -topunHızı)
    }
    else {
        düşüşHızı = düşüşHızı + yerçekimi
        top.götür(0, -düşüşHızı)
    }
    if (top.çarptıMı(Resim.tuvalinSınırları)) {
        top.saydamlığıKur(0.3)
        mesajVer("Tekrar dene", kırmızı)
        durdur()
    }
}

def mesajVer(m: Yazı, r: Renk) {
    çizMerkezdeYazı(m, r, 30)
}

oyunSüresiniGeriyeSayarakGöster(oyunSüresi, "Tebrikler!", yeşil, 20)
tuvaliEtkinleştir()
