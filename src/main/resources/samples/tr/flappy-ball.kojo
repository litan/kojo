// Yukarı ve aşağı oklarıyla kanatlı topa yön vererek
// engellere çarpadan bir dakika uçurabilir misin?
// Dikkat et, tavana çarpma ve yere düşürme!

silVeSakla()
çizSahne(siyah)
/* ekranTazelemeHızınıGöster(kırmızı, 20)
   ekranTazelemeHızınıKur(50) */

dez oyunSüresi = 60  // istersen değiştir!
dez hız = -5 // engellerin yatay hızı
dez topunHızı = 5  // dikey hız, iniş ve yükseliş için
dez yerçekimi = 0.1
den düşüşHızı = 0.0
dez topunZarfı = götür(49, 31) -> Resim.daire(30)
dez top = Resim.küme(
    Resim.imge(Çizim.topKanatlı1, topunZarfı),
    Resim.imge(Çizim.topKanatlı2, topunZarfı)
)
çiz(top)
çizVeSakla(topunZarfı)

dez ta = tuvalAlanı
den engeller = Küme.boş[Resim]
tanım engelKur() {
    dez boy = rastgele((0.5 * ta.boyu).sayıya) + 50
    dez (x, y) = (ta.eni, eğer (rastgeleİkil) ta.boyu / 2 - boy yoksa -ta.boyu / 2)
    dez engel = boyaRengi(renkler.blueViolet) * kalemRengi(renksiz) *
        götür(x, y) -> Resim.dikdörtgen(rastgele(30) + 30, boy)
    engeller += engel
    çiz(engel)
}

yineleSayaçla(1000) {
    engelKur()
}

canlandır {
    engeller herbiriİçin { engel =>  // engellerin herbiri içın yapmamız gereken şeyler
        eğer (engel.konum.x + 60 < ta.x) {  // tuvalin soluna geçenleri silelim
            engel.sil()
            engeller -= engel
        }
        yoksa { // diğerlerini hareket ettirelim ve top çarparsa oyunu durdurarlım
            engel.götür(hız, 0)
            eğer (top.çarptıMı(engel)) {
                top.saydamlığıKur(0.3)
                mesajVer("Tekrar dene", kırmızı)
                durdur()
            }
        }
    }
    top.sonrakiniGöster()  // topun kanatlarını çırpıyoruz
    eğer (tuşaBasılıMı(tuşlar.yukarı)) {
        düşüşHızı = 0
        top.götür(0, topunHızı)
    }
    yoksa eğer (tuşaBasılıMı(tuşlar.aşağı)) {
        düşüşHızı = 0
        top.götür(0, -topunHızı)
    }
    yoksa {
        düşüşHızı = düşüşHızı + yerçekimi
        top.götür(0, -düşüşHızı)
    }
    eğer (top.çarptıMı(Resim.tuvalinSınırları)) {
        top.saydamlığıKur(0.3)
        mesajVer("Tekrar dene", kırmızı)
        durdur()
    }
}

tanım mesajVer(m: Yazı, r: Renk) {
    çizMerkezdeYazı(m, r, 30)
}

oyunSüresiniGeriyeSayarakGöster(oyunSüresi, "Tebrikler!", yeşil, 20)
tuvaliEtkinleştir()
