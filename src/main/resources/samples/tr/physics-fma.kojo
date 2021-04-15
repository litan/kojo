// Newton'un ikinci kanunuyla (F = m * a) iki topun hareketini öngörebiliriz
silVeSakla()
def renkle(r: Renk) = boyaRengi(r) * kalemRengi(renksiz)
val fark = 200
val duvar = renkle(kahverengi) * götür(-45 -fark, -50) -> Resim.dikdörtgen(20, 200)
val top1 = renkle(yeşil) * götür(-fark, 0) -> Resim.daire(25)
val top2 = renkle(mavi) * götür(-fark, 100) -> Resim.daire(25)

çiz(duvar, top1, top2)

val kuvvet1 = 20 // Newton birimi yani kg * m / s^2
val kuvvet2 = 20

val kütle1 = 5 // kg
val kütle2 = 10

val ivme1 = kuvvet1 / kütle1 // m / s^2
val ivme2 = kuvvet2 / kütle2

val t0 = buSaniye

var i = 0
canlandır {
    // s = 1/2 * a * t^2
    val t = buSaniye - t0 // kaç saniye geçmiş
    val s1 = -fark + 0.5 * ivme1 * t * t // top1 kaç metre gitmiş
    val s2 = -fark + 0.5 * ivme2 * t * t

    top1.konumuKur(s1, top1.konum.y) // use pixels instead of meters
    top2.konumuKur(s2, top2.konum.y)
    val frekans = 46 // canlandır döngüsü saniyede yaklaşık 46 kere çalışıyor (benim bilgisayarımda)
    if (i % frekans == 0) satıryaz(i/frekans, yuvarla(t, 2)) // bu ikisi onun için hemen hemen aynı
    i += 1
}
