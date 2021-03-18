silVeSakla()
artalanıKurYatay(Renk(0, 0, 0), Renk(51, 204, 255))
// 
val boya = renkler.radialMultipleGradient(
    0, 0, 150,
    Seq(0, 0.7, 1),
    Seq(Renk(255, 0, 0, 245), Renk(215, 0, 0, 245), Renk(185, 0, 0, 245)),
    doğru
)

val resim = kalemRengi(beyaz) * kalemBoyu(2) * boyaRengi(boya) -> Resim {
    yinele(6120 / 85) {
        ileri(250)
        sağ(85)
    }
}

// Beton gibi pürüzlü gösterelim
val resim2 = gürültü(40, 1) -> resim

// Henüz Türkçeleştiremediğimiz becerileri de kullanabiliriz
val picture = resim2.p

// Spot ışığı, yani ışığı az dağılan bir lambayla aydınlatalım
val l1 = SpotLight(0.9, 0.5, 180, 30, 400)
val picture2 = lights(l1) -> picture

// tam ortaya çizelim:
çizMerkezde(new Resim(picture2))
