// Anay Kamat'ın katkısı

silVeSakla()

def tekDal = kalemRengi(turuncu) -> Resim.noktadan { gn => // GeoNokta
    gn.başla()
    gn.nokta(0, 0) //   iki yeni nokta ...
    gn.nokta(0, 100) //    ... üstüste
    gn.bitir()
}

def dallar(kalınlık: Kesir, adım: Sayı): Resim =
    if (adım == 0) {
        kalemBoyu(kalınlık) -> tekDal
    }
    else {
        Resim.dizi(
            kalemBoyu(kalınlık) -> tekDal,
            götür(0, 100) * büyüt(0.5) -> dallar(kalınlık * 2, adım - 1),
            götür(0, 50) * döndür(30) * büyüt(0.5) -> dallar(kalınlık * 2, adım - 1),
            götür(0, 50) * döndür(-30) * büyüt(0.5) -> dallar(kalınlık * 2, adım - 1)
        )
    }

val açıkMavi = Renk.kym(172, 212, 250)

artalanıKur(Renk.doğrusalDeğişim(0, -100, kahverengi, 0, 100, açıkMavi))
çizMerkezde(dallar(0.5, 7))
