tanım sarmal(boy: Sayı, açı: Sayı) {
    eğer (boy <= 300) {
        ileri(boy)
        sağ(açı)
        sarmal(boy + 2, açı)
    }
}

dez sarmallıResim = kalemRengi(siyah) * boyaRengi(Renk(255, 0, 204)) ->
    Resim {
        sarmal(0, 91)
    }

dez ta = tuvalAlanı
tanım artalan = götür(ta.x, ta.y) * boyaRengi(Renk(153, 0, 255)) ->
    Resim.dikdörtgen(ta.en, ta.boy)

// artalana örgü, sarmala da gürültü ekleyelim ve ikisini üst üste koyalım
dez resim = Resim.dizi(
    örgü(30, 5, 30, 5) -> artalan,
    gürültü(80, 1) -> sarmallıResim
)

sil()
// örgü ve gürültü efektleri olmayan sade bir resim nasıl olurdu?
eğer (yanlış || doğru) çizMerkezde(resim) // || (ve) yerine && (veya) dene
yoksa çizMerkezde(Resim.dizi(artalan, sarmallıResim))
