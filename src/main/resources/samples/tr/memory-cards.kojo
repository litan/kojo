val derece = 1 // zorluk derecesi. Kart sayısı = derece * 10
gerekli(derece >= 1 && derece <= 3, "derece sadece 1, 2 ya da 3 olabilir")

tümEkran()
silVeSakla()

case class Kart(sayı: Sayı) {
    def renkVer(r: Resim) = boyaRengi(kartAA) * kalemRengi(koyuGri) -> r
    val yç = yazıÇerçevesi(sayı.yazıya, 60)
    val rÖnü = renkVer(Resim.dizi(
        Resim.dikdörtgen(80, 120),
        götür((80 - yç.eni) / 2, yç.boyu + (120 - yç.boyu) / 2)
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
    var etkin = doğru
    def açık() {
        etkin = yanlış
        rÖnü.boyamaRenginiKur(kartParlakAA)
        sırayaSok(1) { rÖnü.boyamaRenginiKur(kartAA) }
    }

    rArkası.fareyeTıklayınca { (_, _) => if (etkin) tıkla(this) }
    rÖnü.fareyeTıklayınca { (_, _) => if (etkin) tıkla(this) }
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
    if (dünya.kart1.yokMu) {
        kart.çevir()
        // sadece birinci kartı değiştirmek istiyoruz
        // Onun için tam kopyasını alıp sadece birinci kartı değiştiriyoruz
        dünya = dünya.copy(kart1 = Biri(kart)) // copy: dünyanın benzeri ama kart1 farklı
        hamleleriArtır()
    }
    else if (dünya.kart2.yokMu) {
        // kart1 var. Onun için Biri(3).al => 3
        // Ama Belki bir kart yerine Hiçbiri olsaydı şunu kullanmak gerekirdi:
        //   kart.alYoksa(x) => x
        val kart1 = dünya.kart1.al
        if (!(kart aynıMı kart1)) { // dikkat! == ya da eşitMi yerine aynıMı metodunu kullanıyoruz
            kart.çevir()
            dünya = dünya.copy(kart2 = Biri(kart)) // copy: benzer bir dünya ama kart2 farklı
            hamleleriArtır()
            if (kart1 == kart) {
                kart1.açık()
                kart.açık()
            }
        }
    }
    else if (dünya.kart3.yokMu) {
        val kart1 = dünya.kart1.al
        val kart2 = dünya.kart2.al
        if (!(kart aynıMı kart1) && !(kart aynıMı kart2)) {
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
def kartlarıDağıt(n: Sayı) = for (i <- 1 |-| n) yield Kart(i)

var dünya = Dünya(Hiçbiri, Hiçbiri, Hiçbiri, Hamleler(0))

val kartAA = Renk(0, 255, 0, 127)
val kartParlakAA = Renk(0, 0, 255, 127)
val yarısı = kartSayısı / 2
val kartlar = rastgeleKarıştır(kartlarıDağıt(yarısı) ++ kartlarıDağıt(yarısı))

for (i <- 0 |-| kartSayısı / 5 - 1) {
    for (j <- 0 |-| 4) {
        kartlar(i * 5 + j).kartÇek(i, j)
    }
}

çiz(götür(-tuvalAlanı.eni / 2 + 50, 0) -> dünya.hamleler.durum)
tuvaliEtkinleştir()
