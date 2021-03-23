// Aya füze indirme oyunu! Çok basit bir oyun. Amacımız şunları görmek ve işlemek 
// 1) bilgisayar oyunlarının önemli kavramlarını: yerçekimi, itiş gücü, çarpışmalar, vb...
// 2) işlevler, nesneler, sınıflar, sınıf metodları ve değişkenleri
// 3) oyunun nesnelerini konuşlandırmak ve hareket ettirmek için temel matematik işlemler

// Oynamak için klavyede yukarı oka basarak iniş modülüne gaz ver

silVeSakla()

// uzayın rengi.. HSL: hue (ton), saturation (parlaklık), lightness (aydınlık)
çizSahne(renkler.hsl(240, 0.20, 0.16))

val ta = tuvalAlanı
def xMerkezKonum(resminEni: Kesir) = { ta.x + (ta.en - resminEni) / 2 }

class İnişModülü {
    val bedenEni = 40; val bedenBoyu = 70
    val ateşEni = 20; val ateşBoyu = 35
    val beden = boyaRengi(kırmızı) -> Resim.dikdörtgen(bedenEni, bedenBoyu)
    beden.kondur(xMerkezKonum(bedenEni), ta.y + ta.boy - bedenBoyu - 10)
    val ateş = boyaRengi(turuncu) -> Resim.dikdörtgen(ateşEni, ateşBoyu)
    ateşKonumunuKur()

    val yerçekimi = Yöney2B(0, -0.1) // konum, hız ve ivme'nin x ve y boyutları (z yani üçüncü boyuta gerek yok bu oyunda)
    var hız = Yöney2B(0, 0)
    val sıfırİtiş = Yöney2B(0, 0)
    val yukarıİtiş = Yöney2B(0, 1)
    var itiş = sıfırİtiş

    def ateşKonumunuKur() {
        ateş.kondur(
            beden.konum.x + (bedenEni - ateşEni) / 2,
            beden.konum.y - (ateşBoyu - 15)
        )
    }

    def çiz() {
        beden.çiz()
        ateş.çiz()
        ateş.gizle()
    }

    def adım() {
        // yukarı tuşuna basılı mı?
        if (tuşaBasılıMı(tuşlar.VK_UP)) {
            itişVar()
        } else {
            itişYok()
        }
        hız = hız + yerçekimi
        hız = hız + itiş

        beden.götür(hız)
        ateşKonumunuKur()

        if (beden.çarptıMı(Resim.tuvalinTavanı)) {
            hız = sahneKenarındanYansıtma(beden, hız)
        }
    }

    def itişVar() {
        itiş = yukarıİtiş
        ateş.göster()
    }

    def itişYok() {
        itiş = sıfırİtiş
        ateş.gizle()
    }
}

class Ay {
    val resim = Resim {
        kalemRenginiKur(renkler.lightBlue)
        boyamaRenginiKur(renkler.darkGray)
        sağ(45)
        sağ(90, 500)
    }

    // Ayın eni yaklaşık olarak 710 piksel (inç başına nokta sayısı)
    resim.kondur(xMerkezKonum(710), ta.y)

    def çiz() {
        resim.çiz()
    }

    def ölç(im: İnişModülü) {
        if (im.beden.çarptıMı(resim)) {
            if (im.hız.y.abs > 3) {
                çizMerkezdeYazı("Çarptı ve parçalandı :-(", kırmızı, 39)
            }
            else {
                çizMerkezdeYazı("Yumuşak iniş! :-)", yeşil, 30)
            }
            durdur() // canlandırmaları durduralım
        }
    }

}

val im = new İnişModülü()
im.çiz()

val ay = new Ay()
ay.çiz()

canlandır {
    im.adım()
    ay.ölç(im)
}
tuvaliEtkinleştir()
