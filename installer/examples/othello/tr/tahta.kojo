//#yükle tr/anaTanimlar

// Sadece alfabeta kullanıyor bunu

sınıf Tahta(dez tane: Sayı, dez tahta: Sayılar) {
    tanım drm(t: Taş) = {
        say(t) // + 4 * isay(t)(köşeMi) + 2 * isay(t)(köşeyeİkiUzakMı)
        // + isay(t)(içKöşeMi)
        // - 2 * isay(t)(tuzakKöşeMi) - isay(t)(tuzakKenarMı)
        //
    }
    tanım yaz(msj: Yazı = "", tab: Yazı = "") = {
        için (y <- satırAralığıSondan) {
            dez satır = için (x <- satırAralığı) ver s2t(tahta(y * tane + x))
            satıryaz(satır.yazıYap(s"$tab", " ", ""))
        }
        eğer (msj.boyu > 0) satıryaz(msj)
        satıryaz(s"$tab Beyazlar: ${say(Beyaz)} durum: ${drm(Beyaz)}")
        satıryaz(s"$tab Siyahlar: ${say(Siyah)} durum: ${drm(Siyah)}")
    }
    tanım koy(oda: Oda, taş: Taş) = {
        yeni Tahta(tane, tahta.değiştir(oda.y * tane + oda.x, t2s(taş)))
    }
    tanım koy(odalar: Dizi[Oda], taş: Taş) = {
        den yeniTahta = tahta
        için (o <- odalar) { yeniTahta = yeniTahta.değiştir(o.y * tane + o.x, t2s(taş)) }
        yeni Tahta(tane, yeniTahta)
    }
    tanım taş(o: Oda): Taş = s2t(tahta(o.y * tane + o.x))
    tanım oyunVarMı(oyuncu: Taş) = yasallar(oyuncu).boyu > 0
    tanım yasallar(oyuncu: Taş) = {
        (için (y <- satırAralığı; x <- satırAralığı eğer taş(Oda(y, x)) == Yok)
            ver Oda(y, x)) ele { çevirilecekKomşuDiziler(oyuncu, _).boyu > 0 }
    }
    tanım oyna(oyuncu: Taş, oda: Oda) = {
        dez odalar = EsnekDizim(oda)
        dez karşı = eğer (oyuncu == Beyaz) Siyah yoksa Beyaz
        çevirilecekKomşuDiziler(oyuncu, oda).herbiriİçin { komşu =>
            odalar += komşu.oda
            gerisi(komşu).alDoğruKaldıkça(taş(_) == karşı).herbiriİçin { o => odalar += o }
        }
        koy(odalar.dizi, oyuncu)
    }

    gizli tanım çevirilecekKomşuDiziler(oyuncu: Taş, oda: Oda): Dizi[Komşu] =
        komşularıBul(oda) ele { komşu =>
            dez karşı = eğer (oyuncu == Beyaz) Siyah yoksa Beyaz
            taş(komşu.oda) == karşı && sonuDaYasalMı(komşu, oyuncu)._1
        }

    gizli tanım komşularıBul(o: Oda): Dizi[Komşu] = Dizi(
        Komşu(D, Oda(o.y, o.x + 1)), Komşu(B, Oda(o.y, o.x - 1)),
        Komşu(K, Oda(o.y + 1, o.x)), Komşu(G, Oda(o.y - 1, o.x)),
        Komşu(KD, Oda(o.y + 1, o.x + 1)), Komşu(KB, Oda(o.y + 1, o.x - 1)),
        Komşu(GD, Oda(o.y - 1, o.x + 1)), Komşu(GB, Oda(o.y - 1, o.x - 1))) ele {
            k => odaMı(k.oda)
        }

    gizli tanım sonuDaYasalMı(k: Komşu, oyuncu: Taş): (İkil, Sayı) = {
        dez diziTaşlar = gerisi(k)
        dez sıraTaşlar = diziTaşlar.düşürDoğruKaldıkça { o =>
            taş(o) != oyuncu && taş(o) != Yok
        }
        eğer (sıraTaşlar.boşMu) (yanlış, 0) yoksa {
            dez oda = sıraTaşlar.başı
            (taş(oda) == oyuncu, 1 + diziTaşlar.boyu - sıraTaşlar.boyu)
        }
    }
    gizli tanım gerisi(k: Komşu): Dizi[Oda] = {
        dez sıra = EsnekDizim.boş[Oda]
        dez (x, y) = (k.oda.x, k.oda.y)
        k.yön eşle {
            durum D => için (i <- x + 1 |-| sonOda) /* */ sıra += Oda(y, i)
            durum B => için (i <- x - 1 |-| 0 by -1) /**/ sıra += Oda(y, i)
            durum K => için (i <- y + 1 |-| sonOda) /* */ sıra += Oda(i, x)
            durum G => için (i <- y - 1 |-| 0 by -1) /**/ sıra += Oda(i, x)
            durum KD => // hem y hem x artacak
                eğer (x >= y) için (i <- x + 1 |-| sonOda) /*         */ sıra += Oda(y + i - x, i)
                yoksa için (i <- y + 1 |-| sonOda) /*                */ sıra += Oda(i, x + i - y)
            durum GB => // hem y hem x azalacak
                eğer (x >= y) için (i <- y - 1 |-| 0 by -1) /*        */ sıra += Oda(i, x - y + i)
                yoksa için (i <- x - 1 |-| 0 by -1) /*               */ sıra += Oda(y - x + i, i)
            durum KB => // y artacak x azalacak
                eğer (x + y >= sonOda) için (i <- y + 1 |-| sonOda) /**/ sıra += Oda(i, x + y - i)
                yoksa için (i <- x - 1 |-| 0 by -1) /*               */ sıra += Oda(y + x - i, i)
            durum GD => // y azalacak x artacak
                eğer (x + y >= sonOda) için (i <- x + 1 |-| sonOda) /**/ sıra += Oda(y + x - i, i)
                yoksa için (i <- y - 1 |-| 0 by -1) /*               */ sıra += Oda(i, x + y - i)
        }
        sıra.dizi
    }

    dez sonOda = tane - 1
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
    tanım köşeyeİkiUzakMı: Oda => İkil = {
        durum Oda(y, x) =>
            ((y == 0 || y == sonOda) && (x == 2 || x == sonOda - 2)) ||
                ((y == 2 || y == sonOda - 2) &&
                    (x == 0 || x == 2 || x == sonOda - 2 || x == sonOda))
    }
    tanım içKöşeMi: Oda => İkil = {
        durum Oda(y, x) => (x == 2 && (y == 2 || y == sonOda - 2)) ||
            (x == sonOda - 2 && (y == 2 || y == sonOda - 2))
    }
    tanım odaMı: Oda => İkil = {
        durum Oda(y, x) => 0 <= y && y < tane && 0 <= x && x < tane
    }
    tanım isay(t: Taş)(iş: Oda => İkil) = (için (x <- satırAralığı; y <- satırAralığı; eğer taş(Oda(y, x)) == t && iş(Oda(y, x))) ver 1).boyu
    tanım say(t: Taş) = isay(t) { o => doğru }

    gizli tanım t2s(t: Taş) = t eşle {
        durum Beyaz => 1
        durum Siyah => 2
        durum _     => 0
    }
    gizli tanım s2t(s: Sayı) = s eşle {
        durum 1 => Beyaz
        durum 2 => Siyah
        durum _ => Yok
    }
}

tanım yeniTahta(tane: Sayı, çeşni: Sayı = 0): Tahta = {
    den t = yeni Tahta(tane, Yöney.doldur(tane * tane)(0))

    tanım diziden(dizi: Dizi[(Sayı, Sayı)])(taş: Taş) = t = t.koy(dizi.işle(p => Oda(p._1, p._2)), taş)
    tanım dörtTane: Oda => Birim = {
        durum Oda(y, x) =>
            diziden(Dizi((y, x), (y + 1, x + 1)))(Beyaz)
            diziden(Dizi((y + 1, x), (y, x + 1)))(Siyah)
    }
    dez orta: Sayı = tane / 2
    dez sonu = tane - 1
    çeşni eşle {
        durum 2 => // boş tahtayla oyun başlayamıyor
        durum 1 =>
            gerekli((tane > 6), "Bu çeşni için 7x7 ya da daha iri bir tahta gerekli")
            dörtTane(Oda(1, 1))
            dörtTane(Oda(sonu - 2, sonu - 2))
            dörtTane(Oda(1, sonu - 2))
            dörtTane(Oda(sonu - 2, 1))
        durum _ =>
            dez çiftse = tane % 2 == 0
            eğer (çiftse) dörtTane(Oda(orta - 1, orta - 1))
            yoksa {
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
    t
}
