def resim = Picture {
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
val l1 = SpotLight(0.2, 0.8, 300, 30, 130)
val l2 = SpotLight(0.5, 0.4, 0, 70, 80)
val resim2 = lights(l1, l2) -> resim
drawCentered(resim2)
