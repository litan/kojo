//#yükle tr/anaTanimlar

// elektronik tahta
sınıf ETahta(
    dez odaSayısı: Sayı, // satır ve sütunların oda sayısı
    dez kimBaşlar: Taş,
    dez çeşni:     Sayı) {
    gerekli(3 < odaSayısı, "En küçük tahtamız 4x4lük. odaSayısı değerini artırın") // başlangıç taşları sığmıyor
    gerekli(20 > odaSayısı, "En büyük tahtamız 19x19luk. odaSayısı değerini azaltın") // çok yavaşlıyor
    gerekli(kimBaşlar != Yok, "Beyaz ya da Siyah başlamalı")
    tanım yaz = için (y <- satırAralığıSondan) satıryaz(tahta(y).yazıYap(" "))
    tanım say(t: Taş) = (için (x <- satırAralığı; y <- satırAralığı; eğer tahta(y)(x) == t) ver 1).boyu
    dez hamleSayısı = yeni HamleSayısı
    den oyunBitti = yanlış
    den sonHamle: Belki[Oda] = _ // son hamleyi tuvalde göstermek ve geri/ileri için gerekli
    den sıraGeriDöndüMü = yanlış // geri/ileri durumunda skoru doğru yazmak için

    tanım yasallar = (için {
        x <- satırAralığı; y <- satırAralığı; eğer tahta(y)(x) == Yok
    } ver Oda(y, x)) ele { hamleyiDene(_).boyu > 0 }
    tanım hamleYoksa = yasallar.boyu == 0
    tanım taş(oda: Oda): Taş = tahta(oda.str)(oda.stn)
    tanım taş(y: Sayı, x: Sayı): Taş = tahta(y)(x)
    tanım taşıKur(y: Sayı)(x: Sayı)(taş: Taş): Birim = tahta(y)(x) = taş
    tanım taşıKur(oda: Oda)(taş: Taş): Birim = tahta(oda.str)(oda.stn) = taş
    gizli dez tahta = Dizim.doldur[Taş](odaSayısı, odaSayısı)(Yok)
    dez oyuncu = yeni Oyuncu(kimBaşlar)
    tanım kaçkaç(kısa: İkil = yanlış) =
        eğer (kısa) s"Beyaz: ${say(Beyaz)}\nSiyah: ${say(Siyah)}"
        yoksa s"Beyazlar: ${say(Beyaz)}\nSiyahlar: ${say(Siyah)}"
    tanım başaAl(başlık: Yazı = "") = {
        için (x <- satırAralığı; y <- satırAralığı) taşıKur(Oda(y, x))(Yok)
        başlangıçTaşlarınıKur
        oyuncu.başaAl() // todo: yeni tahta?
        hamleSayısı.başaAl()
        oyunBitti = yanlış
        sonHamle = Hiçbiri
        eğer (başlık.boyu > 0) satıryaz(başlık)
        yaz
    }
    tanım başlangıçTaşlarınıKur = {
        tanım diziden(dizi: Dizi[(Sayı, Sayı)])(taş: Taş) = için { (y, x) <- dizi } taşıKur(Oda(y, x))(taş)
        tanım dörtTane: Oda => Birim = {
            durum Oda(y, x) =>
                diziden(Dizi((y, x), (y + 1, x + 1)))(Beyaz)
                diziden(Dizi((y + 1, x), (y, x + 1)))(Siyah)
        }
        dez orta: Sayı = odaSayısı / 2
        çeşni eşle {
            durum 2 => // boş tahtayla oyun başlayamıyor
            durum 1 =>
                gerekli((odaSayısı > 6), "Bu çeşni için 7x7 ya da daha iri bir tahta gerekli")
                dörtTane(Oda(1, 1))
                dörtTane(Oda(sonOda - 2, sonOda - 2))
                dörtTane(Oda(1, sonOda - 2))
                dörtTane(Oda(sonOda - 2, 1))
            durum _ =>
                dez çiftse = odaSayısı % 2 == 0
                eğer (çiftse) dörtTane(Oda(orta - 1, orta - 1)) yoksa {
                    dez (a, b) = (orta - 1, orta + 1)
                    diziden(Dizi(a -> a, b -> b))(Beyaz)
                    diziden(Dizi((a, b), (b, a)))(Siyah)
                    eğer (yanlış) { // (a, b) odaları boş kalıyor her a ve b çift sayısı için
                        diziden(Dizi(a + 1 -> a, (b - 1, b)))(Beyaz)
                        diziden(Dizi((a, b - 1), (b, a + 1)))(Siyah)
                    }
                    yoksa {
                        diziden(Dizi((a + 1, a), (a + 1, b), (b + 1, b - 1), (a - 1, b - 1)))(Beyaz)
                        diziden(Dizi((a, a + 1), (b, a + 1), (a + 1, a - 1), (a + 1, b + 1)))(Siyah)
                    }
                }
        }
    }

    dez sonOda = odaSayısı - 1
    dez satırAralığı = 0 |-| sonOda
    dez satırAralığıSondan = sonOda |-| 0 by -1

    tanım tuzakKenarMı: Oda => İkil = {
        durum Oda(str, stn) => str == 1 || stn == 1 || str == sonOda - 1 || stn == sonOda - 1
    }
    tanım tuzakKöşeMi: Oda => İkil = {
        durum Oda(y, x) => (x == 1 && (y == 1 || y == sonOda - 1)) ||
            (x == sonOda - 1 && (y == 1 || y == sonOda - 1))
    }
    tanım köşeMi: Oda => İkil = {
        durum Oda(str, stn) => eğer (str == 0) stn == 0 || stn == sonOda else
            str == sonOda && (stn == 0 || stn == sonOda)
    }
    tanım içKöşeMi: Oda => İkil = {
        durum Oda(y, x) => (x == 2 && (y == 2 || y == sonOda - 2)) ||
            (x == sonOda - 2 && (y == 2 || y == sonOda - 2))
    }
    tanım odaMı: Oda => İkil = {
        durum Oda(y, x) => 0 <= y && y < odaSayısı && 0 <= x && x < odaSayısı
    }

    tanım hamleyiDene(oda: Oda): Dizi[Komşu] = komşularıBul(oda).ele { komşu =>
        dez komşuTaş = taş(komşu.oda)
        komşuTaş != Yok && komşuTaş != oyuncu() && sonuDaYasalMı(komşu, oyuncu())._1
    }
    tanım hamleGetirisi(oda: Oda): Sayı = komşularıBul(oda).işle { komşu =>
        dez komşuTaş = taş(komşu.oda)
        dez sonunaKadar = sonuDaYasalMı(komşu, oyuncu())
        eğer (komşuTaş != Yok && komşuTaş != oyuncu() && sonunaKadar._1) sonunaKadar._2 yoksa 0
    }.sum

    tanım komşularıBul(o: Oda): Dizi[Komşu] = Dizi(
        Komşu(D, Oda(o.str, o.stn + 1)), Komşu(B, Oda(o.str, o.stn - 1)),
        Komşu(K, Oda(o.str + 1, o.stn)), Komşu(G, Oda(o.str - 1, o.stn)),
        Komşu(KD, Oda(o.str + 1, o.stn + 1)), Komşu(KB, Oda(o.str + 1, o.stn - 1)),
        Komşu(GD, Oda(o.str - 1, o.stn + 1)), Komşu(GB, Oda(o.str - 1, o.stn - 1))) ele {
            k => odaMı(k.oda)
        }

    tanım sonuDaYasalMı(k: Komşu, oyuncu: Taş): (İkil, Sayı) = {
        dez diziTaşlar = gerisi(k)
        dez sıraTaşlar = diziTaşlar.düşürDoğruKaldıkça { o =>
            taş(o) != oyuncu && taş(o) != Yok
        }
        eğer (sıraTaşlar.boşMu) (yanlış, 0) yoksa {
            dez oda = sıraTaşlar.başı
            (taş(oda) == oyuncu, 1 + diziTaşlar.boyu - sıraTaşlar.boyu)
        }
    }
    tanım gerisi(k: Komşu): Dizi[Oda] = {
        dez sOda = sonOda
        dez sıra = EsnekDizim.boş[Oda]
        dez (x, y) = (k.oda.stn, k.oda.str)
        k.yön eşle {
            durum D => için (i <- x + 1 |-| sOda) /*   */ sıra += Oda(y, i)
            durum B => için (i <- x - 1 |-| 0 by -1) /**/ sıra += Oda(y, i)
            durum K => için (i <- y + 1 |-| sOda) /*   */ sıra += Oda(i, x)
            durum G => için (i <- y - 1 |-| 0 by -1) /**/ sıra += Oda(i, x)
            durum KD => // hem str hem stn artacak
                eğer (x >= y) için (i <- x + 1 |-| sOda) /*       */ sıra += Oda(y + i - x, i)
                yoksa için (i <- y + 1 |-| sOda) /*              */ sıra += Oda(i, x + i - y)
            durum GB => // hem str hem stn azalacak
                eğer (x >= y) için (i <- y - 1 |-| 0 by -1) /*    */ sıra += Oda(i, x - y + i)
                yoksa için (i <- x - 1 |-| 0 by -1) /*           */ sıra += Oda(y - x + i, i)
            durum KB => // str artacak stn azalacak
                eğer (x + y >= sOda) için (i <- y + 1 |-| sOda) /**/ sıra += Oda(i, x + y - i)
                yoksa için (i <- x - 1 |-| 0 by -1) /*           */ sıra += Oda(y + x - i, i)
            durum GD => // str azalacak stn artacak
                eğer (x + y >= sOda) için (i <- x + 1 |-| sOda) /**/ sıra += Oda(y + x - i, i)
                yoksa için (i <- y - 1 |-| 0 by -1) /*           */ sıra += Oda(i, x + y - i)
        }
        sıra.diziye
    }

    başaAl()
}
