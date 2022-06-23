// örnek olsun diye küçük bir oyun yazıverelim
// oyunun kahramanlarını da tangram parçalarıyla oluşturalım

tümEkranTuval

val oyunSüresi = 20

val uzunluk = 4
val d = karekökü(2 * uzunluk * uzunluk)
val d2 = d / 2
val d4 = d / 4

// tangramda yedi şekil var
// önce iki büyük dik üçgen
def parça1 = Resim {
    ileri(uzunluk)
    sağ(135)
    ileri(d2)
    sağ()
    ileri(d2)
}
def parça2 = parça1
// sonra iki küçük dik üçgen
def parça3 = Resim {
    sağ()
    ileri(uzunluk / 2)
    sol(135)
    ileri(d4)
    sol()
    ileri(d4)
}
def parça4 = parça3
// bir kare
def parça6 = Resim {
    yinele(4) {
        ileri(d4)
        sağ()
    }
}
// orta boy dik üçgen
def parça5 = Resim {
    sağ()
    ileri(uzunluk / 2)
    sol()
    ileri(uzunluk / 2)
    sol(135)
    ileri(d2)
}
// bu da yamuk
def parça7 = Resim {
    sağ()
    ileri(uzunluk / 2)
    sol(45)
    ileri(d4)
    sol(135)
    ileri(uzunluk / 2)
    sol(45)
    ileri(d4)
}
// bu da tangram insan
def tangram = Resim.dizi(
    döndür(-120) -> parça3,
    döndür(150) * götür(0, -3.5) -> parça1,
    yansıtY * döndür(120) * götür(1.5, 0) -> parça7,
    döndür(150) * götür(-1, -4.5) -> parça5,
    döndür(-165) * götür(-4.47, -3.9) -> parça4,
    döndür(150) * götür(1, -6.5) -> parça2,
    götür(-1.75, 5.4) * döndürMerkezli(30, d4, 0) -> parça6
)

silVeÇizimBiriminiKur(santim)
val ta = tuvalAlanı
val enİriX = ta.x.mutlakDeğer
val enİriY = ta.y.mutlakDeğer
val kaçan = boyaRengi(sarı) * götür(enİriX / 3, 2) * büyüt(0.3) -> tangram
val kovalayan = boyaRengi(siyah) * büyüt(0.3) -> tangram
val kovalayan2 = boyaRengi(siyah) * götür(-enİriX / 2, 0) * büyüt(0.3) -> tangram
val kovalayan3 = boyaRengi(siyah) * götür(2 * enİriX / 3, 0) * büyüt(0.3) -> tangram
val kovalayan4 = boyaRengi(siyah) * götür(-enİriX / 2, enİriY / 2) * büyüt(0.3) -> tangram
val kovalayan5 = boyaRengi(siyah) * götür(2 * enİriX / 3, enİriY / 2) * büyüt(0.3) -> tangram

müzikMp3üÇalDöngülü("/media/music-loops/Cave.mp3")
gizle()
çizSahne(Renk(150, 150, 255))
çiz(kaçan, kovalayan, kovalayan2, kovalayan3, kovalayan4, kovalayan5)

val hızOranı = 1.5
val hız = 0.4

val hızYöneyi = Yöney2B(0, hız)
val hızYöneyi2 = Yöney2B(hız, 0)
val hızYöneyi3 = Yöney2B(-hız, 0)
val hızYöneyi4 = hızYöneyi2
val hızYöneyi5 = hızYöneyi3

var hızDefteri: Eşlem[Resim, Yöney2B] = _

canlandırmaBaşlayınca {
    hızDefteri = Eşlem(
        kovalayan -> hızYöneyi,
        kovalayan2 -> hızYöneyi2,
        kovalayan3 -> hızYöneyi3,
        kovalayan4 -> hızYöneyi4,
        kovalayan5 -> hızYöneyi5
    )
}

kaçan.canlan { bu =>
    if (tuşaBasılıMı(tuşlar.VK_RIGHT)) {
        bu.götür(hız * hızOranı, 0)
    }
    if (tuşaBasılıMı(tuşlar.VK_LEFT)) {
        bu.götür(-hız * hızOranı, 0)
    }
    if (tuşaBasılıMı(tuşlar.VK_UP)) {
        bu.götür(0, hız * hızOranı)
    }
    if (tuşaBasılıMı(tuşlar.VK_DOWN)) {
        bu.götür(0, -hız * hızOranı)
    }
}

def koşuşturma(bu: Resim) {
    var yeniHızYöneyi = hızDefteri(bu).döndür(rastgeleKesir(10) - 5)
    bu.hızınıDönüştür(yeniHızYöneyi)
    if (bu.çarptıMı(Resim.tuvalinSınırları)) {
        yeniHızYöneyi = sahneKenarındanYansıtma(bu, yeniHızYöneyi)
        bu.hızınıDönüştür(yeniHızYöneyi)
    }
    hızDefteri.eşle(bu -> yeniHızYöneyi)
}

kovalayan.canlan(koşuşturma)
kovalayan2.canlan(koşuşturma)
kovalayan3.canlan(koşuşturma)
kovalayan4.canlan(koşuşturma)
kovalayan5.canlan(koşuşturma)

val kovalayanlar = List(kovalayan, kovalayan2, kovalayan3, kovalayan4, kovalayan5)

oyunSüresiniGöster(oyunSüresi, "Tebrikler!", yeşil, 30, 1, 2)
val bitişMesajı = büyüt(3) * götür(-20, 0) -> Resim { yazı("Çarpıştınız :-(\nBir daha dene!") }
çizVeSakla(bitişMesajı)

kaçan.canlan { bu =>
    if (varMı(bu.çarpışma(kovalayanlar))) {
        durdur()
        bu.boyamaRenginiKur(kahverengi)
        bitişMesajı.konumuKur(-3, 0) // 2 santim!
        bitişMesajı.göster()
    }

    // oyunda belli durumlarda değişik ses efekti yapmanın bir yolu da bu
    if (kaçan.çarptıMı(Resim.tuvalinSınırları)) {
        if (!müzikMp3üÇalıyorMu) {
            müzikMp3üÇal("/media/music-loops/DrumBeats.mp3")
        }
    }
    else {
        müzikMp3üKapat()
    }
}

tuşaBasınca { k =>
    k match {
        case tuşlar.VK_ESCAPE =>
            durdur()
        case _ =>
    }
}

tuvaliEtkinleştir()
