tanım eğreltiOtu(x: Kesir) {
    eğer (x > 1) {
        biçimleriBelleğeYaz()
        kalemKalınlığınıKur(x / 10 + 1)
        kalemRenginiKur(Renk(0, mutlakDeğer(200 - x * 3).sayıya, 40))
        ileri(x)
        sağ(100)
        eğreltiOtu(x * 0.4)
        sol(200)
        eğreltiOtu(x * 0.4)
        sağ(95)
        eğreltiOtu(x * 0.8)
        sağ(5)
        geri(x)
        biçimleriGeriYükle()
    }
}

tanım eğreltiOtuResim = Resim {
    eğreltiOtu(45)
}

silVeSakla()
canlandırmaHızınıKur(10)
artalanıKurDik(Renk(255, 255, 150), beyaz)

dez otVeYansıması = Resim.diziDikey(
    yansıtX -> (soluk(230) * bulanık(2) -> eğreltiOtuResim),
    götür(-20, 0) * kalemRengi(Renk(234, 234, 234)) * kalemBoyu(1) -> Resim.yatay(40),
    götür(0, 3) -> eğreltiOtuResim
).boşluk(10)

çizMerkezde(
  Resim.diziYatay(yansıtY * büyüt(0.6) -> otVeYansıması, otVeYansıması).boşluk(100)
)
