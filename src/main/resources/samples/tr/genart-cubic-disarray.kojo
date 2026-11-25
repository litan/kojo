// Esin kaynağımız: https://generativeartistry.com/tutorials/cubic-disarray/

kojoVarsayılanİkinciBakışaçısınıKur()
silVeSakla()
artalanıKur(Renk(60, 63, 65))
den ta = tuvalAlanı
yaklaşXY(1, -1, ta.eni / 2, -ta.boyu / 2)
ta = tuvalAlanı
// n x n tane dikdörtgen çizelim
dez n = 8
dez yatayAdım = ta.eni / n
dez dikeyAdım = ta.boyu / n

dez rastgeleKıpırdatma = 15
dez rastgeleDöndürme = 20

durum sınıf Dikdörtgen(en: Kesir, boy: Kesir, açı: Kesir, yerX: Kesir, yerY: Kesir)

tanım dikdörtgen(yerX: Kesir, yerY: Kesir, en: Kesir, boy: Kesir) = {
    // yerX * yerY yerine karesi(yerX) yaparsan ne olur? Ya da karesi(yerY)?
    dez düzensizlikOranı = 2 * yerX * yerY / (ta.boyu * ta.eni)
    dez dönüşAçısı = (eğer (rastgeleİkil) 1 yoksa -1) * düzensizlikOranı *
        rastgeleKesir(1) * rastgeleDöndürme
    dez kaydırma = (eğer (rastgeleİkil) 1 yoksa -1) * düzensizlikOranı *
        rastgeleKesir(1) * rastgeleKıpırdatma
    Dikdörtgen(en, boy, dönüşAçısı, yerX + kaydırma, yerY)
}

den dörtgenler = Yöney.boş[Dikdörtgen]
yineleİçin(0 |-| n) { yerY =>
    dez y = ta.y + yerY * dikeyAdım
    yineleİçin(0 |-| n) { yerX =>
        dez x = ta.x + yerX * yatayAdım
        dörtgenler = dörtgenler :+ dikdörtgen(x, y, yatayAdım, dikeyAdım)
    }
}

çiz(dörtgenler.işle { d =>
    kalemBoyu(2) * kalemRengi(beyaz) * boyaRengi(koyuGri) *
        götür(d.yerX, d.yerY) * döndür(d.açı) -> Resim.dikdörtgen(d.en, d.boy)
})

// tuvali biraz uzaklaştırıp bakmayı unutma!
