// Esin kaynağımız: https://generativeartistry.com/tutorials/cubic-disarray/

kojoVarsayılanİkinciBakışaçısınıKur()
silVeSakla()
artalanıKur(Renk(60, 63, 65))
var ta = tuvalAlanı
yaklaşXY(1, -1, ta.eni / 2, -ta.boyu / 2)
ta = tuvalAlanı
// n x n tane dikdörtgen çizelim
val n = 8
val yatayAdım = ta.eni / n
val dikeyAdım = ta.boyu / n

val rastgeleKıpırdatma = 15
val rastgeleDöndürme = 20

case class Dikdörtgen(en: Kesir, boy: Kesir, açı: Kesir, yerX: Kesir, yerY: Kesir)

def dikdörtgen(yerX: Kesir, yerY: Kesir, en: Kesir, boy: Kesir) = {
    // yerX * yerY yerine karesi(yerX) yaparsan ne olur? Ya da karesi(yerY)?
    val düzensizlikOranı = 2 * yerX * yerY / (ta.boyu * ta.eni)
    val dönüşAçısı = (if (rastgeleİkil) 1 else -1) * düzensizlikOranı *
        rastgeleKesir(1) * rastgeleDöndürme
    val kaydırma = (if (rastgeleİkil) 1 else -1) * düzensizlikOranı *
        rastgeleKesir(1) * rastgeleKıpırdatma
    Dikdörtgen(en, boy, dönüşAçısı, yerX + kaydırma, yerY)
}

var dörtgenler = Yöney.boş[Dikdörtgen]
yineleİçin(0 |-| n) { yerY =>
    val y = ta.y + yerY * dikeyAdım
    yineleİçin(0 |-| n) { yerX =>
        val x = ta.x + yerX * yatayAdım
        dörtgenler = dörtgenler :+ dikdörtgen(x, y, yatayAdım, dikeyAdım)
    }
}

çiz(dörtgenler.map { d =>
    kalemBoyu(2) * kalemRengi(beyaz) * boyaRengi(koyuGri) *
        götür(d.yerX, d.yerY) * döndür(d.açı) -> Resim.dikdörtgen(d.en, d.boy)
})

// tuvali biraz uzaklaştırıp bakmayı unutma!
