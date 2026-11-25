silVeSakla()

// kare çizmek için yeni bir komut tanımlayalım
// k:       kareyi çizen kaplumbağa
// boy:     karenin kenar uzunluğu
// bekleme: kaplumbağanın hızını ayarlayarak eş zamanlı çizmek için
tanım kare(k: Kaplumbağa, boy: Sayı, bekleme: Sayı) = {
    k.canlandırmaHızınıKur(bekleme)
    yinele(4) {
        k.ileri(boy)
        k.sağ()
    }
}

tanım göz(k: Kaplumbağa, boy: Sayı, gecikmeSüresi: Sayı) {
    kare(k, boy, gecikmeSüresi)
    k.kalemiKaldır()
    k.ileri(boy / 4)
    k.sağ()
    k.ileri(boy / 4)
    k.sol()
    k.kalemiİndir()
    k.boyamaRenginiKur(koyuGri)
    kare(k, boy / 2, gecikmeSüresi)
}

dez yüz = yeniKaplumbağa(-100, -100)
dez göz1 = yeniKaplumbağa(-75, 25)
dez göz2 = yeniKaplumbağa(25, 25)
dez ağız = yeniKaplumbağa(-50, -50)
dez burun = yeniKaplumbağa(0, -25)
dez saç = yeniKaplumbağa(-110, 100)
dez beden = yeniKaplumbağa(25, -125)
dez ayaklar = yeniKaplumbağa(0, -150)

yüz.davran { kap =>
    kap.boyamaRenginiKur(kırmızı)
    kare(kap, 200, 200)
    kap.görünmez()
}

göz1.davran { kap =>
    kap.boyamaRenginiKur(sarı)
    göz(kap, 50, 800)
    kap.görünmez()
}

göz2.davran { kap =>
    kap.boyamaRenginiKur(sarı)
    göz(kap, 50, 800)
    kap.görünmez()
}

ağız.davran { kap =>
    kap.canlandırmaHızınıKur(2000)
    kap.kalemRenginiKur(sarı)
    kap.kalemKalınlığınıKur(14)
    kap.sağ()
    kap.ileri(100)
    kap.görünmez()
}

burun.davran { kap =>
    kap.canlandırmaHızınıKur(4000)
    kap.kalemRenginiKur(sarı)
    kap.kalemKalınlığınıKur(20)
    kap.ileri(50)
    kap.görünmez()
}

saç.davran { kap =>
    kap.canlandırmaHızınıKur(200)
    kap.sağ()
    kap.kalemRenginiKur(siyah)
    kap.kalemKalınlığınıKur(30)
    kap.ileri(220)
    kap.sol()
    yinele(10) {
        kap.ileri(25)
        kap.geri(25)
        kap.sol()
        kap.ileri(22)
        kap.sağ()
    }
    kap.ileri(25)
    kap.görünmez()
}

beden.davran { kap => 
    kap.boyamaRenginiKur(sarı)
    kap.daire(25)
    kap.görünmez()
}

ayaklar.davran { kap => 
    kap.canlandırmaHızınıKur(3000)
    kap.kalemRenginiKur(siyah)
    kap.kalemKalınlığınıKur(30)
    kap.sağ(180)
    kap.konumVeYönüBelleğeYaz()
    kap.sağ(30)
    kap.ileri(30)
    kap.konumVeYönüGeriYükle()
    kap.sol(30)
    kap.ileri(30)
    kap.görünmez()
}
