// Sağdaki oyuncu yukarı ok ve aşağı ok tuşlarıyla oyuyor
// Soldaki oyuncu da a ve z tuşlarıyla

silVeSakla()
çizSahne(koyuGri)

val raketinBoyu = 100
val raketinEni = 25
val topunYÇ = 15
val tuvalinBoyu = tuvalAlanı.boyu
val tuvalinEni = tuvalAlanı.eni
val raketinHızı = 5
val raketinİvmesi = 1.01
val topunİlkYatayHızı = 5 // yatay yöndeki ilk hız
val topunİvmesi = 1.001

def raket = kalemRengi(koyuGri) * boyaRengi(red) -> Resim.dikdörtgen(raketinEni, raketinBoyu)
def dikey = kalemRengi(beyaz) -> Resim.dikey(tuvalinBoyu)
def top0 = kalemRengi(renkler.rgb(0, 230, 0)) * boyaRengi(renkler.rgb(0, 230, 0)) -> Resim.daire(topunYÇ)

class RaketinHızı(hız0: Kesir, yukarıMıGidiyorduEnSon0: İkil) {
    var hız = hız0
    var yukarıMıGidiyorduEnSon = yukarıMıGidiyorduEnSon0

    def başaDön(yukarı: İkil) {
        hız = hız0
        yukarıMıGidiyorduEnSon = yukarı
    }

    def hızıArttır(artış: Kesir) { hız = hız + artış }
    def hızıKatla(oran: Kesir) { hız = hız * oran }
}

class SkorTutma(skor0: Sayı, solSkor: İkil) {
    var skor = skor0
    val yazısı = Resim.yazıRenkli(skor, 20, renkler.lightSteelBlue)
    yazısı.götür(if (solSkor) -60 else 40, tuvalinBoyu / 2 - 10)
    def arttır() {
        skor += 1
        yazısı.güncelle(skor)
    }
}

val üstVeAltKenar = Dizi(Resim.tuvalinTavanı, Resim.tuvalinTabanı)
val solRaket = götür(-tuvalinEni / 2, 0) -> raket
val sağRaket = götür(tuvalinEni / 2 - raketinEni, 0) -> raket
val araBölme = götür(0, -tuvalinBoyu / 2) -> dikey
val solÇizgi = götür(-tuvalinEni / 2 + raketinEni, -tuvalinBoyu / 2) -> dikey
val sağÇizgi = götür(tuvalinEni / 2 - raketinEni, -tuvalinBoyu / 2) -> dikey
val raketler = Dizi(solRaket, sağRaket)
val top = top0

çiz(solRaket, sağRaket, araBölme, solÇizgi, sağÇizgi, top)

val topunİlkHızı = Yöney2B(topunİlkYatayHızı, 3)
var topunBuankiHızı: Yöney2B = topunİlkHızı

val rakettenHıza = Eşlem(
    solRaket -> new RaketinHızı(raketinHızı, doğru),
    sağRaket -> new RaketinHızı(raketinHızı, doğru))

val sayıDurumu = Eşlem(
    solRaket -> new SkorTutma(0, doğru),
    sağRaket -> new SkorTutma(0, yanlış))

çiz(sayıDurumu(solRaket).yazısı)
çiz(sayıDurumu(sağRaket).yazısı)

canlandır {
    top.götür(topunBuankiHızı)
    if (varMı(top.çarpışma(raketler))) {
        topunBuankiHızı = Yöney2B(-topunBuankiHızı.x, topunBuankiHızı.y)
    }
    else if (varMı(top.çarpışma(üstVeAltKenar))) {
        topunBuankiHızı = Yöney2B(topunBuankiHızı.x, -topunBuankiHızı.y)
    }
    else if (top.çarptıMı(solÇizgi)) {
        top.konumuKur(0, 0)
        topunBuankiHızı = Yöney2B(-topunİlkHızı.x, topunİlkHızı.y)
        sayıDurumu(sağRaket).arttır()
    }
    else if (top.çarptıMı(sağÇizgi)) {
        top.konumuKur(0, 0)
        topunBuankiHızı = Yöney2B(topunİlkHızı.x, topunİlkHızı.y)
        sayıDurumu(solRaket).arttır()
    }
    else {
        topunBuankiHızı = (topunBuankiHızı * topunİvmesi).sınırla(11)
    }
    raketinDavranışı(solRaket, tuşlar.VK_A, tuşlar.VK_Z)
    raketinDavranışı(sağRaket, tuşlar.VK_UP, tuşlar.VK_DOWN)
}

def raketinDavranışı(raket: Resim, yukarıTuşu: Sayı, aşağıTuşu: Sayı) {
    val rHızı = rakettenHıza(raket)
    if (tuşaBasılıMı(yukarıTuşu) && !raket.çarptıMı(Resim.tuvalinTavanı)) {
        if (rHızı.yukarıMıGidiyorduEnSon) {
            rHızı.hızıKatla(raketinİvmesi)
        }
        else {
            rHızı.başaDön(!rHızı.yukarıMıGidiyorduEnSon)
        }
        raket.götür(0, rHızı.hız)
    }
    else if (tuşaBasılıMı(aşağıTuşu) && !raket.çarptıMı(Resim.tuvalinTabanı)) {
        if (!rHızı.yukarıMıGidiyorduEnSon) {
            rHızı.hızıKatla(raketinİvmesi)
        }
        else {
            rHızı.başaDön(!rHızı.yukarıMıGidiyorduEnSon)
        }
        raket.götür(0, -rHızı.hız)
    }
    else {
        rHızı.başaDön(rHızı.yukarıMıGidiyorduEnSon)
    }
}
tuvaliEtkinleştir()
