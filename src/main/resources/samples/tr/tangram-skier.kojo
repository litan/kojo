// Tangram adlı Çin bulmacasını duydun mu? Sadece yedi parçayla neler
// yapılır neler?! Biz de bir kayakçı yapalım ve kaydıralım.

dez boy = 4
dez d = karekökü(2 * boy * boy) // ~= 5.657
dez d2 = d / 2 // ~= 2.828
dez d4 = d / 4 // ~= 1.414 (tam olarak ikinin karekökü)

// Tangram'daki yedi geometrik parçayı birer resim olarak tanımlayalım:
//   r1, r2, r3, r4 ve r5 birer üçgen
//   r6 kare
//   r7 paralel kenar
tanım r1 = Resim { // büyük üçgenler
    ileri(boy)
    sağ(135)
    ileri(d2)
    sağ()
    ileri(d2)
}
tanım r2 = r1
tanım r3 = Resim { // küçük üçgenler
    sağ()
    ileri(boy / 2)
    sol(135)
    ileri(d4)
    sol()
    ileri(d4)
}
tanım r4 = r3
tanım r5 = Resim { // ortanca üçgen
    sağ()
    ileri(boy / 2)
    sol()
    ileri(boy / 2)
    sol(135)
    ileri(d2)
}
tanım r6 = Resim { // kare
    yinele(4) {
        ileri(d4)
        sağ()
    }
}
tanım r7 = Resim { // paralel kenar
    tanım ikiKenar = { ileri(boy / 2); sol(45); ileri(d4) }
    sağ(); ikiKenar
    sol(135); ikiKenar
}

tanım kayak = Resim {
    ileri(3)
}

dez başlamaNoktası = Nokta(11, 1)
dez kayakçı = kalemRengi(siyah) * götür(başlamaNoktası) * büyüt(0.4) -> Resim.dizi(
    götür(-2, -2) * döndür(-75) -> kayak,
    boyaRengi(mor) * döndür(-120) -> r3,
    boyaRengi(sarı) * döndür(150) * götür(0, -3.5) -> r1,
    boyaRengi(mavi) * yansıtY * döndür(120) * götür(1.5, 0) -> r7,
    boyaRengi(kırmızı) * döndür(150) * götür(-1, -4.5) -> r5,
    boyaRengi(yeşil) * döndür(-165) * götür(-4.47, -3.9) -> r4,
    boyaRengi(turuncu) * döndür(150) * götür(1, -6.5) -> r2,
    boyaRengi(kırmızı) * götür(-1.75, 5.4) * döndürMerkezli(30, d4, 0) -> r6
)

tanım parça = Resim {
    sağ()
    konumVeYönüBelleğeYaz()
    kalemKalınlığınıKur(0.2) // 2 milimetre
    ileri(3)
    konumVeYönüGeriYükle()
}

dez yer = kalemRengi(kahverengi) * götür(-13, -8) * döndür(10) -> Resim.diziYatay(
    parça,
    götür(0, 0.5) -> parça,
    götür(0, 1) -> parça,
    götür(0, 1.5) -> parça,
    götür(0, 2) -> parça,
    götür(0, 2.5) -> parça,
    götür(0, 3) -> parça,
    götür(0, 3.5) -> parça
)

tanım santimeÇevir(nokta: Kesir) = 2.54 / 96 * nokta

tanım ağaç(uzaklık: Kesir) {
    eğer (uzaklık > santimeÇevir(4)) {
        kalemKalınlığınıKur(uzaklık / 7)
        kalemRenginiKur(Renk(uzaklık.sayıya, mutlakDeğer(255 - uzaklık * 3).sayıya, 125))
        ileri(uzaklık)
        sağ(25)
        ağaç(uzaklık * 0.8 - santimeÇevir(2))
        sol(45)
        ağaç(uzaklık - santimeÇevir(10))
        sağ(20)
        geri(uzaklık)
    }
}

tanım ağaçResmi = Resim {
    ağaç(1.5)
}

tanım ağaçlarıYap(n: Sayı): Resim = {
    tanım ağaçlar(n: Sayı, büyütmeOranı: Kesir): Resim = {
        eğer (n == 1) {
            büyüt(büyütmeOranı) -> ağaçResmi
        }
        yoksa {
            büyüt(büyütmeOranı) -> Resim.diziYatay(
                ağaçResmi,
                ağaçlar(n - 1, büyütmeOranı)
            )
        }
    }
    ağaçlar(n, 0.9)
}

dez ağaçlar = döndür(7) * götür(-10, 1) -> ağaçlarıYap(9)
silVeÇizimBiriminiKur(santim)
gizle()
yaklaş(0.4, -1, -1)
çiz(yer, ağaçlar, kayakçı)

canlandır {
    dez hız = 0.20 // biraz daha hızlandırmak ister misin?
    dez (hızYatay, hızDikey) = (-hız, -hız / 2)
    eğer (kayakçı.çarptıMı(yer)) {
        kayakçı.götür(hızYatay, 0)
    }
    yoksa {
        kayakçı.götür(hızYatay, hızDikey)
    }
    eğer (kayakçı.uzaklık(yer) > 2) {
        kayakçı.kondur(başlamaNoktası)
    }
}

kayakçı.fareyeTıklayınca { (x, y) =>
    kayakçı.kondur(başlamaNoktası)
}

eğer (yanlış) { // bunu doğruya çevirerek sadece üç boy üçgeni çizdirebilirsin
    silVeÇizimBiriminiKur(santim)
    gizle()
    çiz(Resim.diziYatay(r1, r3, r5))
}
