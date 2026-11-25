// Sağdaki oyuncu yukarı ok ve aşağı ok tuşlarıyla oyuyor
// Soldaki oyuncu da a ve z tuşlarıyla
// Eğer en başta takılırsa, tekrar çalıştırıverin.

silVeSakla()
çizSahne(koyuGri)

dez bitişSayısı = 15
dez raketinBoyu = 100
dez raketinEni = 25
dez topunYÇ = 15
dez tuvalinBoyu = tuvalAlanı.boyu
dez tuvalinEni = tuvalAlanı.eni
dez raketinHızı = 400
dez raketinİvmesi = 1.01
dez topunİlkYatayHızı = 400 // yatay yöndeki ilk hız
dez topunİlkDikeyHızı = topunİlkYatayHızı * 0.65
dez topunİvmesi = 1.001

tanım raket = kalemRengi(koyuGri) * boyaRengi(red) -> Resim.dikdörtgen(raketinEni, raketinBoyu)
tanım dikey = kalemRengi(beyaz) -> Resim.dikey(tuvalinBoyu)
tanım top0 = kalemRengi(Renk.kym(0, 230, 0)) * boyaRengi(Renk.kym(0, 230, 0)) -> Resim.daire(topunYÇ)

sınıf RaketinHızı(hız0: Kesir, yukarıMıGidiyorduEnSon0: İkil) {
    den hız = hız0
    den yukarıMıGidiyorduEnSon = yukarıMıGidiyorduEnSon0

    tanım başaDön(yukarı: İkil) {
        hız = hız0
        yukarıMıGidiyorduEnSon = yukarı
    }

    tanım hızıArttır(artış: Kesir) { hız = hız + artış }
    tanım hızıKatla(oran: Kesir) { hız = hız * oran }
}

sınıf SkorTutma(skor0: Sayı, solSkor: İkil) {
    den skor = skor0
    dez yazısı = Resim.yazıRenkli(skor, 20, renkler.lightSteelBlue)
    yazısı.götür(eğer (solSkor) -60 yoksa 40, tuvalinBoyu / 2 - 10)
    tanım arttır() {
        sesSayı
        skor += 1
        yazısı.güncelle(skor)
        eğer (skor == bitişSayısı) durdur  // todo: ikisi de 14 olunca tiebreak olsun!
    }
}

dez üstVeAltKenar = Dizi(Resim.tuvalinTavanı, Resim.tuvalinTabanı)
dez solRaket = götür(-tuvalinEni / 2, -tuvalinBoyu / 3) -> raket
dez sağRaket = götür(tuvalinEni / 2 - raketinEni, tuvalinBoyu / 4) -> raket
dez araBölme = götür(0, -tuvalinBoyu / 2) -> dikey
dez solÇizgi = götür(-tuvalinEni / 2 + raketinEni, -tuvalinBoyu / 2) -> dikey
dez sağÇizgi = götür(tuvalinEni / 2 - raketinEni, -tuvalinBoyu / 2) -> dikey
dez raketler = Dizi(solRaket, sağRaket)
dez top = top0

çiz(solRaket, sağRaket, araBölme, solÇizgi, sağÇizgi, top)

dez topunİlkHızı = Yöney2B(topunİlkYatayHızı, topunİlkDikeyHızı)
den topunBuankiHızı: Yöney2B = topunİlkHızı

dez rakettenHıza = Eşlem(
    solRaket -> yeni RaketinHızı(raketinHızı, doğru),
    sağRaket -> yeni RaketinHızı(raketinHızı, doğru))

dez sayıDurumu = Eşlem(
    solRaket -> yeni SkorTutma(0, doğru),
    sağRaket -> yeni SkorTutma(0, yanlış))

çiz(sayıDurumu(solRaket).yazısı)
çiz(sayıDurumu(sağRaket).yazısı)

def sesVer = sesMp3üÇal(Ses.vuruş)
def sesSayı = sesMp3üÇal(Ses.arabaKazaYaptı)

sesSayı
sesVer // ilk seferde yüklemek çok zaman alıyor,
// devinim döngüsü içinde sorun yaratıyor. Onun için burada çağıralım.

den ölçüSayısı = 7
satıryaz("ETH: Ekran Tazeleme Hızı ya da Oranı, iki çizim arasındaki süre, milisaniye olarak")
satıryaz("   ETH   Çizim sayısı (saniyede)")
satıryaz("   ===   =======================")
canlandır {
    dez as = ikiÇizimArasıSüre
    eğer (ölçüSayısı > 0) {
        satıryaz(f"${(as * 1000).sayıya}%6d   ${1.0/as}%-6.1f")
        ölçüSayısı -= 1
    }

    top.götür(topunBuankiHızı * as)

    eğer (top.çarpışma(raketler).varMı) {
        sesVer
        topunBuankiHızı = Yöney2B(-topunBuankiHızı.x, topunBuankiHızı.y)
    }
    yoksa eğer (top.çarpışma(üstVeAltKenar).varMı) {
        top.götür(-topunBuankiHızı * as)
        topunBuankiHızı = Yöney2B(topunBuankiHızı.x, -topunBuankiHızı.y)
    }
    yoksa eğer (top.çarptıMı(solÇizgi)) {
        top.konumuKur(0, 0)
        topunBuankiHızı = Yöney2B(-topunİlkHızı.x, topunİlkHızı.y)
        sayıDurumu(sağRaket).arttır()
    }
    yoksa eğer (top.çarptıMı(sağÇizgi)) {
        top.konumuKur(0, 0)
        topunBuankiHızı = Yöney2B(topunİlkHızı.x, topunİlkHızı.y)
        sayıDurumu(solRaket).arttır()
    }
    yoksa {
        topunBuankiHızı = (topunBuankiHızı * topunİvmesi).sınırla(800)
    }
    raketinDavranışı(solRaket, tuşlar.a, tuşlar.z, as)
    raketinDavranışı(sağRaket, tuşlar.yukarı, tuşlar.aşağı, as)
}

tanım raketinDavranışı(raket: Resim, yukarıTuşu: Sayı, aşağıTuşu: Sayı, as: Kesir) {
    dez rHızı = rakettenHıza(raket)
    eğer (tuşaBasılıMı(yukarıTuşu) && !raket.çarptıMı(Resim.tuvalinTavanı)) {
        eğer (rHızı.yukarıMıGidiyorduEnSon) {
            rHızı.hızıKatla(raketinİvmesi)
        }
        yoksa {
            rHızı.başaDön(!rHızı.yukarıMıGidiyorduEnSon)
        }
        raket.götür(0, rHızı.hız * as)
    }
    yoksa eğer (tuşaBasılıMı(aşağıTuşu) && !raket.çarptıMı(Resim.tuvalinTabanı)) {
        eğer (!rHızı.yukarıMıGidiyorduEnSon) {
            rHızı.hızıKatla(raketinİvmesi)
        }
        yoksa {
            rHızı.başaDön(!rHızı.yukarıMıGidiyorduEnSon)
        }
        raket.götür(0, -rHızı.hız * as)
    }
    yoksa {
        rHızı.başaDön(rHızı.yukarıMıGidiyorduEnSon)
    }
}
tuvaliEtkinleştir()
