dez derece = 1 // zorluk derecesi. Kart sayısı = derece * 10
gerekli(derece >= 1 && derece <= 3, "derece sadece 1, 2 ya da 3 olabilir")

tümEkran()
silVeSakla()

durum sınıf Kart(sayı: Sayı) {
    tanım renkVer(r: Resim) = boyaRengi(kartAA) * kalemRengi(koyuGri) -> r
    dez yç = yazıÇerçevesi(sayı.yazıya, 60)
    dez rÖnü = renkVer(Resim.dizi(
        Resim.dikdörtgen(80, 120),
        götür((80 - yç.eni) / 2, yç.boyu + (120 - yç.boyu) / 2)
            -> Resim.yazı(sayı, 60))
    )
    dez rArkası = renkVer(Resim.dikdörtgen(80, 120))
    tanım kartÇek(i: Sayı, j: Sayı) {
        çizVeSakla(rÖnü)
        çiz(götür(-200 + j * 100, -140 * derece + i * 140) -> rArkası)
        rÖnü.kondur(rArkası.konum)
    }
    tanım çevir() {
        eğer (rArkası.görünür) {
            rArkası.gizle()
            rÖnü.göster()
        }
        yoksa {
            rÖnü.gizle()
            rArkası.göster()
        }
    }
    den etkin = doğru
    tanım açık() {
        etkin = yanlış
        rÖnü.boyamaRenginiKur(kartParlakAA)
        sırayaSok(1) { rÖnü.boyamaRenginiKur(kartAA) }
    }

    rArkası.fareyeTıklayınca { (_, _) => eğer (etkin) tıkla(bu) }
    rÖnü.fareyeTıklayınca { (_, _) => eğer (etkin) tıkla(bu) }
}

durum sınıf Hamleler(n: Sayı) {
    dez gösterge = kalemRengi(siyah) -> Resim.yazı(s"Hamle Sayısı: $n", 20)
    tanım artır() = Hamleler(n + 1)
}

durum sınıf Dünya(
    kart1:    Belki[Kart],
    kart2:    Belki[Kart],
    kart3:    Belki[Kart],
    hamleler: Hamleler)

tanım tıkla(kart: Kart) {
    eğer (dünya.kart1.yokMu) {
        kart.çevir()
        // sadece birinci kartı değiştirmek istiyoruz
        // Onun için tam kopyasını alıp sadece birinci kartı değiştiriyoruz
        dünya = dünya.copy(kart1 = Biri(kart)) // copy: dünyanın benzeri ama kart1 farklı
        hamleleriArtır()
    }
    yoksa eğer (dünya.kart2.yokMu) {
        // kart1 var. Onun için Biri(3).al => 3
        // Ama Belki bir kart yerine Hiçbiri olsaydı şunu kullanmak gerekirdi:
        //   kart.alYoksa(x) => x
        dez kart1 = dünya.kart1.al
        eğer (!(kart aynıMı kart1)) { // dikkat! == ya da eşitMi yerine aynıMı metodunu kullanıyoruz
            kart.çevir()
            dünya = dünya.copy(kart2 = Biri(kart)) // copy: benzer bir dünya ama kart2 farklı
            hamleleriArtır()
            eğer (kart1 == kart) {
                kart1.açık()
                kart.açık()
            }
        }
    }
    yoksa eğer (dünya.kart3.yokMu) {
        dez kart1 = dünya.kart1.al
        dez kart2 = dünya.kart2.al
        eğer (!(kart aynıMı kart1) && !(kart aynıMı kart2)) {
            eğer (kart1 != kart2) {
                kart1.çevir()
                kart2.çevir()
            }
            dünya = Dünya(Hiçbiri, Hiçbiri, Hiçbiri, dünya.hamleler)
            tıkla(kart)
        }
    }
}

tanım hamleleriArtır() {
    tanım gösterge = dünya.hamleler.gösterge
    dez konum = gösterge.konum
    gösterge.sil()
    dünya = dünya.copy(hamleler = dünya.hamleler.artır())
    gösterge.kondur(konum)
    gösterge.çiz()
}

dez kartSayısı = derece * 2 * 5
tanım kartlarıDağıt(n: Sayı) = için (i <- 1 |-| n) ver Kart(i)

den dünya = Dünya(Hiçbiri, Hiçbiri, Hiçbiri, Hamleler(0))

dez kartAA = Renk(0, 255, 0, 127)
dez kartParlakAA = Renk(0, 0, 255, 127)
dez yarısı = kartSayısı / 2
dez kartlar = rastgeleKarıştır(kartlarıDağıt(yarısı) ++ kartlarıDağıt(yarısı))

için (i <- 0 |-| kartSayısı / 5 - 1) {
    için (j <- 0 |-| 4) {
        kartlar(i * 5 + j).kartÇek(i, j)
    }
}

çiz(götür(-tuvalAlanı.eni / 2 + 50, 0) -> dünya.hamleler.gösterge)
tuvaliEtkinleştir()
