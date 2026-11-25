// Newton'un ikinci kanunuyla (F = m * a) iki topun hareketini öngörebiliriz
silVeSakla()
tanım renkle(r: Renk) = boyaRengi(r) * kalemRengi(renksiz)
dez fark = 200
dez duvar = renkle(kahverengi) * götür(-45 -fark, -50) -> Resim.dikdörtgen(20, 200)
dez top1 = renkle(yeşil) * götür(-fark, 0) -> Resim.daire(25)
dez top2 = renkle(mavi) * götür(-fark, 100) -> Resim.daire(25)

çiz(duvar, top1, top2)

dez kuvvet1 = 20 // Newton birimi yani kg * m / s^2
dez kuvvet2 = 20

dez kütle1 = 5 // kg. Bunu daha da hafeğer yapmayı dene. Örneğin 2 veya 1
dez kütle2 = 10

dez ivme1 = kuvvet1 / kütle1 // m / s^2
dez ivme2 = kuvvet2 / kütle2

dez t0 = buSaniye

den i = 0
canlandır {
    // s = 1/2 * a * t^2
    dez t = buSaniye - t0 // kaç saniye geçmiş
    dez s1 = -fark + 0.5 * ivme1 * t * t // top1 kaç metre gitmiş
    dez s2 = -fark + 0.5 * ivme2 * t * t

    top1.konumuKur(s1, top1.konum.y) // ekrandaki bir nokta (pixel) bir metreye karşılık
    top2.konumuKur(s2, top2.konum.y)
    dez frekans = 46 // canlandır döngüsü saniyede yaklaşık 46 kere çalışıyor (benim bilgisayarımda)
    eğer (i % frekans == 0) satıryaz(i/frekans, yuvarla(t, 2)) // bu ikisi onun için hemen hemen aynı
    i += 1
}
