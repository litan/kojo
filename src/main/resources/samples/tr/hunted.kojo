// örnek olsun diye küçük bir oyun yazıverelim
// oyunun kahramanlarını da tangram parçalarıyla oluşturalım

tümEkranTuval

dez oyunSüresi = 20

dez uzunluk = 4
dez d = karekökü(2 * uzunluk * uzunluk)
dez d2 = d / 2
dez d4 = d / 4

// tangramda yedi şekil var
// önce iki büyük dik üçgen
tanım parça1 = Resim {
    ileri(uzunluk)
    sağ(135)
    ileri(d2)
    sağ()
    ileri(d2)
}
tanım parça2 = parça1
// sonra iki küçük dik üçgen
tanım parça3 = Resim {
    sağ()
    ileri(uzunluk / 2)
    sol(135)
    ileri(d4)
    sol()
    ileri(d4)
}
tanım parça4 = parça3
// bir kare
tanım parça6 = Resim {
    yinele(4) {
        ileri(d4)
        sağ()
    }
}
// orta boy dik üçgen
tanım parça5 = Resim {
    sağ()
    ileri(uzunluk / 2)
    sol()
    ileri(uzunluk / 2)
    sol(135)
    ileri(d2)
}
// yamuk
tanım parça7 = Resim {
    sağ()
    ileri(uzunluk / 2)
    sol(45)
    ileri(d4)
    sol(135)
    ileri(uzunluk / 2)
    sol(45)
    ileri(d4)
}
// tangram insan
tanım tangram = Resim.dizi(
    döndür(-120) -> parça3,
    döndür(150) * götür(0, -3.5) -> parça1,
    yansıtY * döndür(120) * götür(1.5, 0) -> parça7,
    döndür(150) * götür(-1, -4.5) -> parça5,
    döndür(-165) * götür(-4.47, -3.9) -> parça4,
    döndür(150) * götür(1, -6.5) -> parça2,
    götür(-1.75, 5.4) * döndürMerkezli(30, d4, 0) -> parça6
)

silVeÇizimBiriminiKur(santim)
dez ta = tuvalAlanı
dez enİriX = ta.x.mutlakDeğer
dez enİriY = ta.y.mutlakDeğer
dez kaçan = boyaRengi(sarı) * götür(enİriX / 3, 2) * büyüt(0.3) -> tangram
dez kovalayan = boyaRengi(siyah) * büyüt(0.3) -> tangram
dez kovalayan2 = boyaRengi(siyah) * götür(-enİriX / 2, 0) * büyüt(0.3) -> tangram
dez kovalayan3 = boyaRengi(siyah) * götür(2 * enİriX / 3, 0) * büyüt(0.3) -> tangram
dez kovalayan4 = boyaRengi(siyah) * götür(-enİriX / 2, enİriY / 2) * büyüt(0.3) -> tangram
dez kovalayan5 = boyaRengi(siyah) * götür(2 * enİriX / 3, enİriY / 2) * büyüt(0.3) -> tangram

müzikMp3üÇalDöngülü(Ses.mağarada)
gizle()
çizSahne(Renk(150, 150, 255))
çiz(kaçan, kovalayan, kovalayan2, kovalayan3, kovalayan4, kovalayan5)

dez hızOranı = 1.5
dez hız = 0.4

dez hızYöneyi = Yöney2B(0, hız)
dez hızYöneyi2 = Yöney2B(hız, 0)
dez hızYöneyi3 = Yöney2B(-hız, 0)
dez hızYöneyi4 = hızYöneyi2
dez hızYöneyi5 = hızYöneyi3

den hızDefteri: Eşlem[Resim, Yöney2B] = _

canlandırmaBaşlayınca {
    hızDefteri = Eşlem(
        kovalayan -> hızYöneyi,
        kovalayan2 -> hızYöneyi2,
        kovalayan3 -> hızYöneyi3,
        kovalayan4 -> hızYöneyi4,
        kovalayan5 -> hızYöneyi5
    )
}

kaçan.canlan { r =>
    eğer (tuşaBasılıMı(tuşlar.sağ)) {
        r.götür(hız * hızOranı, 0)
    }
    eğer (tuşaBasılıMı(tuşlar.sol)) {
        r.götür(-hız * hızOranı, 0)
    }
    eğer (tuşaBasılıMı(tuşlar.yukarı)) {
        r.götür(0, hız * hızOranı)
    }
    eğer (tuşaBasılıMı(tuşlar.aşağı)) {
        r.götür(0, -hız * hızOranı)
    }
}

tanım koşuşturma(r: Resim) {
    den yeniHızYöneyi = hızDefteri(r).döndür(rastgeleKesir(10) - 5)
    r.hızınıDönüştür(yeniHızYöneyi)
    eğer (r.çarptıMı(Resim.tuvalinSınırları)) {
        yeniHızYöneyi = sahneKenarındanYansıtma(r, yeniHızYöneyi)
        r.hızınıDönüştür(yeniHızYöneyi)
    }
    hızDefteri.eşEkle(r -> yeniHızYöneyi)
}

kovalayan.canlan(koşuşturma)
kovalayan2.canlan(koşuşturma)
kovalayan3.canlan(koşuşturma)
kovalayan4.canlan(koşuşturma)
kovalayan5.canlan(koşuşturma)

dez kovalayanlar = Dizi(kovalayan, kovalayan2, kovalayan3, kovalayan4, kovalayan5)

oyunSüresiniGeriyeSayarakGöster(oyunSüresi, "Tebrikler!", yeşil, 30, 1, 2)
dez bitişMesajı = büyüt(3) * götür(-20, 0) -> Resim { yazı("Çarpıştınız :-(\nBir daha dene!") }
çizVeSakla(bitişMesajı)

kaçan.canlan { r =>
    eğer (varMı(r.çarpışma(kovalayanlar))) {
        durdur()
        r.boyamaRenginiKur(kahverengi)
        bitişMesajı.konumuKur(-3, 0) // 2 santim!
        bitişMesajı.göster()
    }

    // oyunda belli durumlarda değişik ses efekti yapmanın başka bir yolu da böyle
    eğer (kaçan.çarptıMı(Resim.tuvalinSınırları)) {
        eğer (!müzikMp3üÇalıyorMu) {
            müzikMp3üÇal(Ses.basDavulVuruşları)
        }
    }
    yoksa {
        müzikMp3üKapat()
    }
}

tuşaBasınca { k =>
    k eşle {
        durum tuşlar.d => durdur()
        durum _ =>
    }
}

tuvaliEtkinleştir()
