def sarmal(boy: Sayı, açı: Sayı) {
    if (boy <= 300) {
        ileri(boy)
        sağ(açı)
        sarmal(boy + 2, açı)
    }
}

val sarmallıResim = kalemRengi(siyah) * boyaRengi(Renk(255, 0, 204)) -> Resim {
    sarmal(0, 91)
}

val ta = tuvalAlanı
def artalan = götür(ta.x, ta.y) * boyaRengi(Renk(153, 0, 255)) -> Resim.dikdörtgen(ta.en, ta.boy)

// artalanın üstüne sarmalı koyalım:
val resim = resimDizisi(
    örgü(30, 5, 30, 5) -> artalan,
    gürültü(80, 1) -> sarmallıResim
)

sil()
çizMerkezde(resim)
