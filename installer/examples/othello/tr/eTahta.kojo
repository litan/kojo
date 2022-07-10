//#yükle tr/anaTanimlar

// elektronik tahta
class ETahta(
    val odaSayısı: Sayı, // satır ve sütunların oda sayısı
    val kimBaşlar: Taş,
    val çeşni:     Sayı) {
    gerekli(3 < odaSayısı, "En küçük tahtamız 4x4lük. odaSayısı değerini artırın") // başlangıç taşları sığmıyor
    gerekli(20 > odaSayısı, "En büyük tahtamız 19x19luk. odaSayısı değerini azaltın") // çok yavaşlıyor
    gerekli(kimBaşlar != Yok, "Beyaz ya da Siyah başlamalı")
    def yaz = for (y <- satırAralığıSondan) satıryaz(tahta(y).yazıYap(" "))
    def say(t: Taş) = (for (x <- satırAralığı; y <- satırAralığı; if tahta(y)(x) == t) yield 1).boyu
    val hamleSayısı = new HamleSayısı
    var oyunBitti = yanlış
    var sonHamle: Belki[Oda] = _ // son hamleyi tuvalde göstermek ve geri/ileri için gerekli
    def yasallar = (for {
        x <- satırAralığı; y <- satırAralığı; if tahta(y)(x) == Yok
    } yield Oda(y, x)) ele { hamleyiDene(_).boyu > 0 }
    def hamleYoksa = yasallar.boyu == 0
    def taş(oda: Oda): Taş = tahta(oda.str)(oda.stn)
    def taş(y: Sayı, x: Sayı): Taş = tahta(y)(x)
    def taşıKur(y: Sayı)(x: Sayı)(taş: Taş): Birim = tahta(y)(x) = taş
    def taşıKur(oda: Oda)(taş: Taş): Birim = tahta(oda.str)(oda.stn) = taş
    private val tahta = Dizim.doldur[Taş](odaSayısı, odaSayısı)(Yok)
    val oyuncu = new Oyuncu(kimBaşlar)
    def kaçkaç(kısa: İkil = yanlış) =
        if (kısa) s"Beyaz: ${say(Beyaz)}\nSiyah: ${say(Siyah)}"
        else s"Beyazlar: ${say(Beyaz)}\nSiyahlar: ${say(Siyah)}"
    def başaAl(başlık: Yazı = "") = {
        for (x <- satırAralığı; y <- satırAralığı) taşıKur(Oda(y, x))(Yok)
        başlangıçTaşlarınıKur
        oyuncu.başaAl() // todo: new tahta?
        hamleSayısı.başaAl()
        oyunBitti = yanlış
        sonHamle = Hiçbiri
        if (başlık.boyu > 0) satıryaz(başlık)
        yaz
    }
    def başlangıçTaşlarınıKur = {
        def diziden(dizi: Dizi[(Sayı, Sayı)])(taş: Taş) = for { (y, x) <- dizi } taşıKur(Oda(y, x))(taş)
        def dörtTane: Oda => Birim = {
            case Oda(y, x) =>
                diziden(Dizi((y, x), (y + 1, x + 1)))(Beyaz)
                diziden(Dizi((y + 1, x), (y, x + 1)))(Siyah)
        }
        val orta: Sayı = odaSayısı / 2
        çeşni match {
            case 2 => // boş tahtayla oyun başlayamıyor
            case 1 =>
                gerekli((odaSayısı > 6), "Bu çeşni için 7x7 ya da daha iri bir tahta gerekli")
                dörtTane(Oda(1, 1))
                dörtTane(Oda(sonOda - 2, sonOda - 2))
                dörtTane(Oda(1, sonOda - 2))
                dörtTane(Oda(sonOda - 2, 1))
            case _ =>
                val çiftse = odaSayısı % 2 == 0
                if (çiftse) dörtTane(Oda(orta - 1, orta - 1)) else {
                    val (a, b) = (orta - 1, orta + 1)
                    diziden(Dizi(a -> a, b -> b))(Beyaz)
                    diziden(Dizi((a, b), (b, a)))(Siyah)
                    if (yanlış) { // (a, b) odaları boş kalıyor her a ve b çift sayısı için
                        diziden(Dizi(a + 1 -> a, (b - 1, b)))(Beyaz)
                        diziden(Dizi((a, b - 1), (b, a + 1)))(Siyah)
                    }
                    else {
                        diziden(Dizi((a + 1, a), (a + 1, b), (b + 1, b - 1), (a - 1, b - 1)))(Beyaz)
                        diziden(Dizi((a, a + 1), (b, a + 1), (a + 1, a - 1), (a + 1, b + 1)))(Siyah)
                    }
                }
        }
    }

    val sonOda = odaSayısı - 1
    val satırAralığı = 0 |-| sonOda
    val satırAralığıSondan = sonOda |-| 0 by -1

    def tuzakKenarMı: Oda => İkil = {
        case Oda(str, stn) => str == 1 || stn == 1 || str == sonOda - 1 || stn == sonOda - 1
    }
    def tuzakKöşeMi: Oda => İkil = {
        case Oda(y, x) => (x == 1 && (y == 1 || y == sonOda - 1)) ||
            (x == sonOda - 1 && (y == 1 || y == sonOda - 1))
    }
    def köşeMi: Oda => İkil = {
        case Oda(str, stn) => if (str == 0) stn == 0 || stn == sonOda else
            str == sonOda && (stn == 0 || stn == sonOda)
    }
    def içKöşeMi: Oda => İkil = {
        case Oda(y, x) => (x == 2 && (y == 2 || y == sonOda - 2)) ||
            (x == sonOda - 2 && (y == 2 || y == sonOda - 2))
    }
    def odaMı: Oda => İkil = {
        case Oda(y, x) => 0 <= y && y < odaSayısı && 0 <= x && x < odaSayısı
    }

    def hamleyiDene(oda: Oda): Dizi[Komşu] = komşularıBul(oda).ele { komşu =>
        val komşuTaş = taş(komşu.oda)
        komşuTaş != Yok && komşuTaş != oyuncu() && sonuDaYasalMı(komşu, oyuncu())._1
    }
    def hamleGetirisi(oda: Oda): Sayı = komşularıBul(oda).işle { komşu =>
        val komşuTaş = taş(komşu.oda)
        val sonunaKadar = sonuDaYasalMı(komşu, oyuncu())
        if (komşuTaş != Yok && komşuTaş != oyuncu() && sonunaKadar._1) sonunaKadar._2 else 0
    }.sum

    def komşularıBul(o: Oda): Dizi[Komşu] = Dizi(
        Komşu(D, Oda(o.str, o.stn + 1)), Komşu(B, Oda(o.str, o.stn - 1)),
        Komşu(K, Oda(o.str + 1, o.stn)), Komşu(G, Oda(o.str - 1, o.stn)),
        Komşu(KD, Oda(o.str + 1, o.stn + 1)), Komşu(KB, Oda(o.str + 1, o.stn - 1)),
        Komşu(GD, Oda(o.str - 1, o.stn + 1)), Komşu(GB, Oda(o.str - 1, o.stn - 1))) ele {
            k => odaMı(k.oda)
        }

    def sonuDaYasalMı(k: Komşu, oyuncu: Taş): (İkil, Sayı) = {
        val diziTaşlar = gerisi(k)
        val sıraTaşlar = diziTaşlar.düşürDoğruKaldıkça { o =>
            taş(o) != oyuncu && taş(o) != Yok
        }
        if (sıraTaşlar.boşMu) (yanlış, 0) else {
            val oda = sıraTaşlar.başı
            (taş(oda) == oyuncu, 1 + diziTaşlar.boyu - sıraTaşlar.boyu)
        }
    }
    def gerisi(k: Komşu): Dizi[Oda] = {
        val son = sonOda
        val sıra = EsnekDizim.boş[Oda]
        val (x, y) = (k.oda.stn, k.oda.str)
        k.yön match {
            case D => for (i <- x + 1 |-| son) /*    */ sıra += Oda(y, i)
            case B => for (i <- x - 1 |-| 0 by -1) /**/ sıra += Oda(y, i)
            case K => for (i <- y + 1 |-| son) /*    */ sıra += Oda(i, x)
            case G => for (i <- y - 1 |-| 0 by -1) /**/ sıra += Oda(i, x)
            case KD => // hem str hem stn artacak
                if (x >= y) for (i <- x + 1 |-| son) /*      */ sıra += Oda(y + i - x, i)
                else for (i <- y + 1 |-| son) /*             */ sıra += Oda(i, x + i - y)
            case GB => // hem str hem stn azalacak
                if (x >= y) for (i <- y - 1 |-| 0 by -1) /*  */ sıra += Oda(i, x - y + i)
                else for (i <- x - 1 |-| 0 by -1) /*         */ sıra += Oda(y - x + i, i)
            case KB => // str artacak stn azalacak
                if (x + y >= son) for (i <- y + 1 |-| son) /**/ sıra += Oda(i, x + y - i)
                else for (i <- x - 1 |-| 0 by -1) /*         */ sıra += Oda(y + x - i, i)
            case GD => // str azalacak stn artacak
                if (x + y >= son) for (i <- x + 1 |-| son) /**/ sıra += Oda(y + x - i, i)
                else for (i <- y - 1 |-| 0 by -1) /*         */ sıra += Oda(i, x + y - i)
        }
        sıra.diziye
    }

    başaAl()
}
