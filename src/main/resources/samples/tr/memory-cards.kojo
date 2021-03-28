val derece = 2 // zorluk derecesi. Kart sayısı = derece * 10
require(derece >= 1 && derece <= 3, "derece sadece 1, 2 ya da 3 olabilir")

tümEkran()
silVeSakla()

case class Kart(sayı: Sayı) {
    def renkVer(p: Resim) = boyaRengi(kartAA) * kalemRengi(koyuGri) -> p
    val yç = yazıÇerçevesi(sayı.toString, 60) // 35.toString -> "35"
    val rÖnü = renkVer(resimDizisi(
        Resim.dikdörtgen(80, 120),
        // yç bir Dikdörtgen .width eni .height da boyu
        götür((80 - yç.width) / 2, yç.height + (120 - yç.height) / 2)
            -> Resim.yazı(sayı, 60))
    )
    val rArkası = renkVer(Resim.dikdörtgen(80, 120))
    def kartÇek(i: Sayı, j: Sayı) {
        çizVeSakla(rÖnü)
        çiz(götür(-200 + j * 100, -140 * derece + i * 140) -> rArkası)
        rÖnü.kondur(rArkası.konum)
    }
    def çevir() {
        if (rArkası.görünür) {
            rArkası.gizle()
            rÖnü.göster()
        }
        else {
            rÖnü.gizle()
            rArkası.göster()
        }
    }
    var etkin = true
    def açık() {
        etkin = false
        rÖnü.boyamaRenginiKur(kartParlakAA)
        sırayaSok(1) { rÖnü.boyamaRenginiKur(kartAA) }
    }

    rArkası.fareyeTıklayınca { (x, y) => if (etkin) tıkla(this) }
    rÖnü.fareyeTıklayınca { (x, y) => if (etkin) tıkla(this) }
}

case class Hamleler(n: Sayı) {
    val durum = kalemRengi(siyah) -> Resim.yazı(s"Hamle Sayısı: $n", 20)
    def artır() = Hamleler(n + 1)
}

case class Dünya(
    kart1:    Belki[Kart],
    kart2:    Belki[Kart],
    kart3:    Belki[Kart],
    hamleler: Hamleler)

def tıkla(kart: Kart) {
    if (yokMu(dünya.kart1)) {
        kart.çevir()
        // sadece birinci kartı değiştirmek istiyoruz
        // Onun için tam kopyasını alıp sadece birinci kartı değiştiriyoruz
        dünya = dünya.copy(kart1 = Biri(kart))
        hamleleriArtır()
    }
    else if (yokMu(dünya.kart2)) {
        // kart1 var. Onun için Biri(3).get => 3
        // Ama Belki bir Kart yerine Hiçbiri olsaydı şunu kullanmak gerekirdi:
        //   kart.getOrElse(x) => x
        val kart1 = dünya.kart1.get
        if (!(kart eq kart1)) { // dikkat! == yerine eq (equal) metodunu kullanıyoruz
            kart.çevir()
            dünya = dünya.copy(kart2 = Biri(kart))
            hamleleriArtır()
            if (kart1 == kart) {
                kart1.açık()
                kart.açık()
            }
        }
    }
    else if (yokMu(dünya.kart3)) {
        val kart1 = dünya.kart1.get
        val kart2 = dünya.kart2.get
        if (!(kart eq kart1) && !(kart eq kart2)) {
            if (kart1 != kart2) {
                kart1.çevir()
                kart2.çevir()
            }
            dünya = Dünya(Hiçbiri, Hiçbiri, Hiçbiri, dünya.hamleler)
            tıkla(kart)
        }
    }
}

def hamleleriArtır() {
    def durum = dünya.hamleler.durum
    val konum = durum.konum
    durum.sil()
    dünya = dünya.copy(hamleler = dünya.hamleler.artır())
    durum.kondur(konum)
    durum.çiz()
}

val kartSayısı = derece * 2 * 5
def kartlarıDağıt(n: Sayı) = for (i <- 1 to n) yield Kart(i)

var dünya = Dünya(Hiçbiri, Hiçbiri, Hiçbiri, Hamleler(0))

val kartAA = Renk(0, 255, 0, 127)
val kartParlakAA = Renk(0, 0, 255, 127)
val yarısı = kartSayısı / 2
val kartlar = rastgeleKarıştır(kartlarıDağıt(yarısı) ++ kartlarıDağıt(yarısı))

for (i <- 0 to kartSayısı / 5 - 1) {
    for (j <- 0 to 4) {
        kartlar(i * 5 + j).kartÇek(i, j)
    }
}

çiz(götür(-tuvalAlanı.eni / 2 + 50, 0) -> dünya.hamleler.durum)
tuvaliEtkinleştir()
