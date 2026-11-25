silVeSakla()
hızıKur(çokHızlı)
artalanıKurYatay(Renk(0, 0, 0), mavi)
kalemRenginiKur(Renk(255, 102, 102))
kalemKalınlığınıKur(2)

// girdiler: merkezX, merkezY, yarıçap, dağılım, renkler, dalgalıDevam
// KYMS: kırmızı/yeşil/mavi/saydamlık oranı
dez boya = Renk.merkezdenDışarıDoğruÇokluDeğişim(0, 0, 150,
    Dizi(0, 0.7, 1),
    Dizi(
        Renk.kyms(255, 0, 0, 245),
        Renk.kyms(215, 0, 0, 245),
        Renk.kyms(185, 0, 0, 245)
    ),
    doğru
)
boyamaRenginiKur(boya)

atla(-100, -110)
yinele(6120 / 85) {
    ileri(250)
    sağ(85)
}
