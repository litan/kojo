def resim = Resim {
    kalemRenginiKur(renksiz)
    boyamaRenginiKur(Renk(0, 102, 255))
    yinele(4) {
        ileri(300)
        sağ()
    }
    boyamaRenginiKur(Renk(255, 255, 51))
    atla(100, 100)
    yinele(5) {
        ileri(100)
        sağ(720 / 5)
    }
}

silVeSakla()
val sı1 = SahneIşığı(0.2, 0.8, 300, 30, 130)
val sı2 = SahneIşığı(0.5, 0.4, 0, 70, 80)
val resim2 = ışıklar(sı1, sı2) -> resim
çizMerkezde(resim2)
