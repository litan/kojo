// Arabayı sürmek için dört ok tuşunu kullan:
//   sağ, sol, yukarı ve aşağı.
// Mavi arabalara çarpma.
// Her çarpışmayla dermanı azalır, her saniye dermanı artar.
// Yoldan çıkarsan yani sağ ya da sol kenara çarparsan oyun biter.
// Derman biterse oyun biter.
// Kazanmak için bir dakika boyunca arabayı sür.
kojoVarsayılanİkinciBakışaçısınıKur()
silVeSakla()
çizSahne(siyah)
// ekranTazelemeHızınıKur(50)
dez oyunSüresi = 60

dez ta = tuvalAlanı
dez arabaBoyu = 100
dez çizgiBoyu = 80

//  araba resimlerinin boyutlarına epey yakın bir çokkenarlı biçim çizelim
dez arabayaZarf = götür(48, 14) -> Resim {
    yinele (2) {
        ileri(70); sol(45); ileri(20); sol(45)
        ileri(18); sol(45); ileri(20); sol(45)
    }
}
tanım araba(imge: Yazı) = Resim.imge(imge, arabayaZarf)

dez arabalar = Eşlem.boş[Resim, Yöney2B]
dez arabaHızı = 3
dez sürücüTepkiHızı = 3
den sürücüHızı = Yöney2B(0, 0)
// çarpma anından sonra 0.3 saniye boyunca direksiyon, hız ve fren yani
// ileri geri sağ sol okları çalışmasın
den etkisizlikSüresi = 0L

dez frenSesiÇalar = yeniMp3Çalar
dez çarpışmaSesiÇalar = yeniMp3Çalar

tanım arabaYap() {
    dez a = götür(oyuncu.konum.x + rastgeleDoğalKesir * ta.eni / 10, ta.y + ta.boyu) ->
        araba(Görünüş.araba2)
    a.çiz
    arabalar += a -> Yöney2B(0, -arabaHızı)
}
den yolÇizgileri = Küme.boş[Resim]
tanım yolÇizgisiYap() {
    dez eni = 20
    dez yç = boyaRengi(beyaz) * kalemRengi(beyaz) *
        götür(ta.x + ta.eni / 2 - eni / 2, ta.y + ta.boyu) -> Resim.dikdörtgen(eni, çizgiBoyu)
    yç.çiz
    yolÇizgileri += yç
}

dez oyuncu = araba(Görünüş.araba1)
çiz(oyuncu)
çizVeSakla(arabayaZarf)

// 0.8 saniyede bir yeni araba ve çizgi gelsin yukarıdan
yineleSayaçla(800) {
    yolÇizgisiYap()
    arabaYap()
}

canlandır {
    oyuncu.öneAl()
    dez aktifMi = buAn - etkisizlikSüresi > 300
    eğer (aktifMi) {
        eğer (tuşaBasılıMı(tuşlar.sol)) {
            sürücüHızı = Yöney2B(-sürücüTepkiHızı, 0)
            oyuncu.götür(sürücüHızı)
        }
        eğer (tuşaBasılıMı(tuşlar.sağ)) {
            sürücüHızı = Yöney2B(sürücüTepkiHızı, 0)
            oyuncu.götür(sürücüHızı)
        }
        eğer (tuşaBasılıMı(tuşlar.yukarı)) {
            sürücüHızı = Yöney2B(0, sürücüTepkiHızı)
            oyuncu.götür(sürücüHızı)
            eğer (!Mp3ÇalıyorMu) {
                sesMp3üÇal(Ses.arabaHızlanıyor)
            }
        }
        yoksa {
            Mp3üDurdur()
        }
        eğer (tuşaBasılıMı(tuşlar.aşağı)) {
            sürücüHızı = Yöney2B(0, -sürücüTepkiHızı)
            oyuncu.götür(sürücüHızı)
            eğer (!frenSesiÇalar.çalıyorMu) {
                frenSesiÇalar.sesMp3üÇal(Ses.arabaFrenYapıyor)
            }
        }
        yoksa {
            frenSesiÇalar.durdur()
        }
    }
    yoksa {
        oyuncu.götür(sürücüHızı)
    }

    eğer (oyuncu.çarptıMı(Resim.tuvalinSolu) || oyuncu.çarptıMı(Resim.tuvalinSağı)) {
        çarpışmaSesiÇalar.sesMp3üÇal(Ses.arabaKazaYaptı)
        oyuncu.saydamlığıKur(0.5)
        çizMerkezdeYazı("Yoldan çıktın. Yine deneyebilirsin.", kırmızı, 30)
        durdur()
    }
    yoksa eğer (oyuncu.çarptıMı(Resim.tuvalinTavanı)) {
        sürücüHızı = Yöney2B(0, -sürücüTepkiHızı)
        oyuncu.götür(sürücüHızı * 2)
        etkisizlikSüresi = buAn
    }
    yoksa eğer (oyuncu.çarptıMı(Resim.tuvalinTabanı)) {
        sürücüHızı = Yöney2B(0, sürücüTepkiHızı)
        oyuncu.götür(sürücüHızı * 2)
        etkisizlikSüresi = buAn
    }

    arabalar.herbiriİçin { arabaVeHız =>
        dez (araba, hız) = arabaVeHız
        //araba.öneAl()   // toyap gerekli mi? Bitiş yazısı bazen altta kalıyor...
        eğer (oyuncu.çarptıMı(araba)) {
            çarpışmaSesiÇalar.sesMp3üÇal(Ses.arabaKazaYaptı)
            sürücüHızı = engeldenYansıtma(oyuncu, sürücüHızı - hız, araba) / 2
            oyuncu.götür(sürücüHızı * 3)
            araba.götür(-sürücüHızı * 3)
            etkisizlikSüresi = buAn
            dermanıAzalt()
        }
        yoksa {
            dez yeniHız = Yöney2B(hız.x + rastgeleKesir(1) / 2 - 0.25, hız.y)
            arabalar += araba -> yeniHız
            araba.götür(yeniHız)
        }
        eğer (araba.konum.y + arabaBoyu < ta.y) {
            araba.sil()
            arabalar -= araba
        }
    }

    yolÇizgileri.herbiriİçin { yç => 
        yç.götür(0, -arabaHızı * 2)
        eğer (yç.konum.y + çizgiBoyu < ta.y) {
            yç.sil()
            yolÇizgileri -= yç
        }
    }
}

den derman = 0
tanım dermanYazısı = s"Derman: $derman"
dez dermanÇizimi = Resim.yazıRenkli(dermanYazısı, 20, renkler.aquamarine)
dermanÇizimi.götür(ta.x + 10, ta.y + ta.boyu - 10)
tanım dermanıArtır() {
    derman += 2
    dermanÇizimi.güncelle(dermanYazısı)
}
tanım dermanıAzalt() {
    derman -= 10
    dermanÇizimi.güncelle(dermanYazısı)
    eğer (derman < 0) {
        çizMerkezdeYazı("Derman kalmadı. Yine deneyebilirsin.", kırmızı, 30)
        durdur()
    }
}

tanım skorVeDermanıYönet(oyunSüresi: Sayı) {
    den kalanSüre = oyunSüresi
    dez kalanSüreGösterimi = Resim.yazıRenkli(kalanSüre, 20, renkler.azure)
    kalanSüreGösterimi.götür(ta.x + 10, ta.y + 50)
    çiz(kalanSüreGösterimi)
    çiz(dermanÇizimi)
    kalanSüreGösterimi.girdiyiAktar(Resim.tuvalBölgesi)

    yineleSayaçla(1000) {
        kalanSüre -= 1
        kalanSüreGösterimi.güncelle(kalanSüre)
        dermanıArtır()

        eğer (kalanSüre == 0) {
            çizMerkezdeYazı("Süre doldu. Tebrikler!", yeşil, 30)
            durdur()
        }
    }
}

skorVeDermanıYönet(oyunSüresi)
müzikMp3üÇalDöngülü(Ses.arabaGidiyor)
tuvaliEtkinleştir()

// Araba resimleri  google aracılığıyla şunlardan:
//    http://motor-kid.com/race-cars-top-view.html  ve
//    www.carinfopic.com
// Araba sesleri şurdan: http://soundbible.com
