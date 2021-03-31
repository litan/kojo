def sarmal(adım: Sayı, açı: Sayı) {
    if (adım <= 300) {
        ileri(adım)
        sağ(açı)
        sarmal(adım + 2, açı)
    }
}
sil()
kalemRenginiKur(siyah)
boyamaRenginiKur(yeşil)
artalanıKurYatay(kırmızı, sarı)
kalemKalınlığınıKur(1)
hızıKur(çokHızlı)
sarmal(0, 91)
