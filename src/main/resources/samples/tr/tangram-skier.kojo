// a kayakçı based on tangram pieces

val boy = 4
val d = karekökü(2 * boy * boy)
val d2 = d / 2
val d4 = d / 4

def r1 = Resim {
    ileri(boy)
    sağ(135)
    ileri(d2)
    sağ()
    ileri(d2)
}

def r2 = r1

def r3 = Resim {
    sağ()
    ileri(boy / 2)
    sol(135)
    ileri(d4)
    sol()
    ileri(d4)
}

def r4 = r3

def r5 = Resim {
    sağ()
    ileri(boy / 2)
    sol()
    ileri(boy / 2)
    sol(135)
    ileri(d2)
}

def r6 = Resim {
    yinele(4) {
        ileri(d4)
        sağ()
    }
}

def r7 = Resim {
    sağ()
    ileri(boy / 2)
    sol(45)
    ileri(d4)
    sol(135)
    ileri(boy / 2)
    sol(45)
    ileri(d4)
}

def kayak = Resim {
    ileri(3)
}

val başlamaNoktası = Nokta(11, 1)
val kayakçı = kalemRengi(siyah) * götür(başlamaNoktası) * büyüt(0.4) -> Resim.dizi(
    götür(-2, -2) * döndür(-75) -> kayak,
    boyaRengi(mor) * döndür(-120) -> r3,
    boyaRengi(sarı) * döndür(150) * götür(0, -3.5) -> r1,
    boyaRengi(mavi) * yansıtY * döndür(120) * götür(1.5, 0) -> r7,
    boyaRengi(kırmızı) * döndür(150) * götür(-1, -4.5) -> r5,
    boyaRengi(yeşil) * döndür(-165) * götür(-4.47, -3.9) -> r4,
    boyaRengi(turuncu) * döndür(150) * götür(1, -6.5) -> r2,
    boyaRengi(kırmızı) * götür(-1.75, 5.4) * döndürMerkezli(30, d4, 0) -> r6
)

def parça = Resim {
    sağ()
    konumVeYönüBelleğeYaz()
    kalemKalınlığınıKur(0.2) // 2 milimetre
    ileri(3)
    konumVeYönüGeriYükle()
}

val yer = kalemRengi(kahverengi) * götür(-13, -8) * döndür(10) -> Resim.diziYatay(
    parça,
    götür(0, 0.5) -> parça,
    götür(0, 1) -> parça,
    götür(0, 1.5) -> parça,
    götür(0, 2) -> parça,
    götür(0, 2.5) -> parça,
    götür(0, 3) -> parça,
    götür(0, 3.5) -> parça
)

def santimeÇevir(nokta: Kesir) = 2.54 / 96 * nokta

def ağaç(uzaklık: Kesir) {
    if (uzaklık > santimeÇevir(4)) {
        kalemKalınlığınıKur(uzaklık / 7)
        kalemRenginiKur(Renk(uzaklık.toInt, mutlakDeğer(255 - uzaklık * 3).toInt, 125))
        ileri(uzaklık)
        sağ(25)
        ağaç(uzaklık * 0.8 - santimeÇevir(2))
        sol(45)
        ağaç(uzaklık - santimeÇevir(10))
        sağ(20)
        geri(uzaklık)
    }
}

def ağaçResmi = Resim {
    ağaç(1.5)
}

def ağaçlarıYap(n: Sayı): Resim = {
    def yap(n: Sayı, büyütmeOranı: Kesir): Resim = {
        if (n == 1) {
            büyüt(büyütmeOranı) -> ağaçResmi
        }
        else {
            büyüt(büyütmeOranı) -> Resim.diziYatay(
                ağaçResmi,
                yap(n - 1, büyütmeOranı)
            )
        }
    }
    yap(n, 0.9)
}

val ağaçlar = döndür(7) * götür(-10, 1) -> ağaçlarıYap(9)
silVeÇizimBiriminiKur(santim)
gizle()
yaklaş(0.4, -1, -1)
çiz(yer, ağaçlar, kayakçı)

canlandır {
    if (kayakçı.çarptıMı(yer)) {
        kayakçı.götür(-0.09, 0)
    }
    else {
        kayakçı.götür(-0.09, -0.045)
    }
    if (kayakçı.uzaklık(yer) > 2) {
        kayakçı.kondur(başlamaNoktası)
    }
}

kayakçı.fareyeTıklayınca { (x, y) =>
    kayakçı.kondur(başlamaNoktası)
}
