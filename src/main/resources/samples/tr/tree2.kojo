// Anay Kamat'ın katkısı

silVeSakla()

def tekDal = kalemRengi(turuncu) -> Resim.noktadan { gn => // GeoNokta == VertexShape
    import gn._
    beginShape() //   başla
    vertex(0, 0) //   iki yeni nokta ...
    vertex(0, 100) //    ... üstüste
    endShape() //     bitir
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

val açıkMavi = renkler.rgb(172, 212, 250)

artalanıKur(renkler.linearGradient(0, -100, kahverengi, 0, 100, açıkMavi))
çizMerkezde(dallar(0.5, 7))
