silVeSakla()
hızıKur(çokHızlı)
artalanıKurYatay(Renk(0, 0, 0), mavi)
kalemRenginiKur(Renk(255, 102, 102))
kalemKalınlığınıKur(2)

// RGBA: red/green/blue/alpha alpha: saydamlık oranı
val boya = renkler.radialMultipleGradient(0, 0, 150,
    Dizi(0, 0.7, 1),
    Dizi(
        renkler.rgba(255, 0, 0, 245),
        renkler.rgba(215, 0, 0, 245),
        renkler.rgba(185, 0, 0, 245)
    ),
    doğru
)
boyamaRenginiKur(boya)

atla(-100, -110)
yinele(6120 / 85) {
    ileri(250)
    sağ(85)
}
