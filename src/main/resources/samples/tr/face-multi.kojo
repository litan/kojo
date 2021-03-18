silVeSakla()

// kare çizmek için yeni bir komut tanımlayalım
// k:       kareyi çizen kaplumbağa
// boy:     karenin kenar uzunluğu
// bekleme: kaplumbağanın hızını ayarlayarak eş zamanlı çizmek için
def kare(k: Kaplumbağa, boy: Sayı, bekleme: Sayı) = {
    k.canlandırmaHızınıKur(bekleme)
    yinele(4) {
        k.ileri(boy)
        k.sağ()
    }
}

def göz(k: Kaplumbağa, boy: Sayı, gecikmeSüresi: Sayı) {
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

val yüz = yeniKaplumbağa(-100, -100)
val göz1 = yeniKaplumbağa(-75, 25)
val göz2 = yeniKaplumbağa(25, 25)
val ağız = yeniKaplumbağa(-50, -50)
val burun = yeniKaplumbağa(0, -25)
val saç = yeniKaplumbağa(-110, 100)
val beden = yeniKaplumbağa(25, -125)
val ayaklar = yeniKaplumbağa(0, -150)

yüz.davran { bu =>
    bu.boyamaRenginiKur(kırmızı)
    kare(bu, 200, 200)
    bu.görünmez()
}

göz1.davran { bu =>
    bu.boyamaRenginiKur(sarı)
    göz(bu, 50, 800)
    bu.görünmez()
}

göz2.davran { bu =>
    bu.boyamaRenginiKur(sarı)
    göz(bu, 50, 800)
    bu.görünmez()
}

ağız.davran { bu =>
    bu.canlandırmaHızınıKur(2000)
    bu.kalemRenginiKur(sarı)
    bu.kalemKalınlığınıKur(14)
    bu.sağ()
    bu.ileri(100)
    bu.görünmez()
}

burun.davran { bu =>
    bu.canlandırmaHızınıKur(4000)
    bu.kalemRenginiKur(sarı)
    bu.kalemKalınlığınıKur(20)
    bu.ileri(50)
    bu.görünmez()
}

saç.davran { bu =>
    bu.canlandırmaHızınıKur(200)
    bu.sağ()
    bu.kalemRenginiKur(siyah)
    bu.kalemKalınlığınıKur(30)
    bu.ileri(220)
    bu.sol()
    yinele(10) {
        bu.ileri(25)
        bu.geri(25)
        bu.sol()
        bu.ileri(22)
        bu.sağ()
    }
    bu.ileri(25)
    bu.görünmez()
}

beden.davran { bu => 
    bu.boyamaRenginiKur(sarı)
    bu.daire(25)
    bu.görünmez()
}

ayaklar.davran { bu => 
    bu.canlandırmaHızınıKur(3000)
    bu.kalemRenginiKur(siyah)
    bu.kalemKalınlığınıKur(30)
    bu.sağ(180)
    bu.konumVeYönüBelleğeYaz()
    bu.sağ(30)
    bu.ileri(30)
    bu.konumVeYönüGeriYükle()
    bu.sol(30)
    bu.ileri(30)
    bu.görünmez()
}
