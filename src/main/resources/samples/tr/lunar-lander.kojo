// Aya füze indirme oyunu! Çok basit bir oyun. Amacımız şunları görmek ve işlemek 
// 1) bilgisayar oyunlarının önemli kavramlarını: yerçekimi, itiş gücü, çarpışmalar, vb...
// 2) işlevler, nesneler, sınıflar, sınıf metodları ve değişkenleri
// 3) oyunun nesnelerini konuşlandırmak ve hareket ettirmek için temel matematik işlemler

// Oynamak için klavyede yukarı oka basarak iniş modülüne gaz ver

silVeSakla()

// uzayın rengi.. ADA (HSL): arı-renk/ton (hue), doygunluk/parlaklık (saturation), aydınlık (lightness)
çizSahne(Renk.ada(240, 0.20, 0.16))

dez ta = tuvalAlanı
tanım xMerkezKonum(resminEni: Kesir) = { ta.x + (ta.en - resminEni) / 2 }

sınıf İnişModülü {
    dez bedenEni = 40; dez bedenBoyu = 70
    dez ateşEni = 20; dez ateşBoyu = 35
    dez beden = boyaRengi(kırmızı) -> Resim.dikdörtgen(bedenEni, bedenBoyu)
    beden.kondur(xMerkezKonum(bedenEni), ta.y + ta.boy - bedenBoyu - 10)
    dez ateş = boyaRengi(turuncu) -> Resim.dikdörtgen(ateşEni, ateşBoyu)
    ateşKonumunuKur()

    dez yerçekimi = Yöney2B(0, -0.1) // konum, hız ve ivme'nin x ve y boyutları (z yani üçüncü boyuta gerek yok bu oyunda)
    den hız = Yöney2B(0, 0)
    dez sıfırİtiş = Yöney2B(0, 0)
    dez yukarıİtiş = Yöney2B(0, 1)
    den itiş = sıfırİtiş

    tanım ateşKonumunuKur() {
        ateş.kondur(
            beden.konum.x + (bedenEni - ateşEni) / 2,
            beden.konum.y - (ateşBoyu - 15)
        )
    }

    tanım çiz() {
        beden.çiz()
        ateş.çiz()
        ateş.gizle()
    }

    tanım adım() {
        // yukarı tuşuna basılı mı?
        eğer (tuşaBasılıMı(tuşlar.yukarı)) {
            itişVar()
        } yoksa {
            itişYok()
        }
        hız = hız + yerçekimi
        hız = hız + itiş

        beden.götür(hız)
        ateşKonumunuKur()

        eğer (beden.çarptıMı(Resim.tuvalinTavanı)) {
            hız = sahneKenarındanYansıtma(beden, hız)
        }
    }

    tanım itişVar() {
        itiş = yukarıİtiş
        ateş.göster()
    }

    tanım itişYok() {
        itiş = sıfırİtiş
        ateş.gizle()
    }
}

sınıf Ay {
    dez resim = Resim {
        kalemRenginiKur(renkler.lightBlue)
        boyamaRenginiKur(renkler.darkGray)
        sağ(45)
        sağ(90, 500)
    }

    // Ayın eni yaklaşık olarak 710 piksel (inç başına nokta sayısı)
    resim.kondur(xMerkezKonum(710), ta.y)

    tanım çiz() {
        resim.çiz()
    }

    tanım ölç(im: İnişModülü) {
        eğer (im.beden.çarptıMı(resim)) {
            eğer (im.hız.y.mutlakDeğer > 3) {
                çizMerkezdeYazı("Çarptı ve parçalandı :-(", kırmızı, 39)
            }
            yoksa {
                çizMerkezdeYazı("Yumuşak iniş! :-)", yeşil, 30)
            }
            durdur() // canlandırmaları durduralım
        }
    }

}

dez im = yeni İnişModülü()
im.çiz()

dez ay = yeni Ay()
ay.çiz()

canlandır {
    im.adım()
    ay.ölç(im)
}
tuvaliEtkinleştir()
