//#yükle tr/durum

nesne ABa { // alfa-beta arama
    den sayaç = 0
    dez debug = doğru
    tanım hamleYap(drm: Durum): Belki[Oda] = {
        den çıktı: Belki[Oda] = Hiçbiri
        sayaç = 0
        zamanTut(s"Alfa-beta ${düzeydenUstalığa} arama") {
            çıktı = alfaBetaHamle(drm)
        }(s"sürdü.")
        eğer (debug) {
            dez oyuncu = drm.sıra eşle {
                durum Beyaz => "Beyaz"
                durum Siyah => "Siyah"
            }
            dez hamle: Yazı = çıktı.işle(oda => s"Oda(${oda.str},${oda.stn})").alYoksa("yok")
            satıryaz(s"sayaç=$sayaç $oyuncu -> $hamle,")
        }
        çıktı
    }
    tanım yeter(derinlik: Sayı): İkil = { // yeterince derin ve çok "düşündük" mü?
        //satıryaz((derinlik, sayaç))
        derinlik <= 0 && sayaç > hamleSayısıÜstSınırı
    }
    tanım alfaBetaHamle(drm: Durum): Belki[Oda] =
        eğer (drm.seçenekler.boşMu) Hiçbiri
        yoksa eğer (drm.seçenekler.boyu == 1) {
            eğer (debug) {
                den tekHamle = drm.seçenekler.başı
                satıryaz(s"Tek hamle var: ($tekHamle,${drm.oyna(tekHamle).skor})")
            }
            Biri(drm.seçenekler.başı)
        }
        yoksa // todo: karşı oyuncunun skorunu azaltan birden çok hamle varsa rastgele seç
        eğer (debug) {
            dez hepsi = için (hamle <- drm.seçenekler) ver hamle -> abHamle(drm.oyna(hamle), aramaDerinliğiSınırı)
            satıryaz(hepsi)
            eğer (yanlış) Biri(drm.sıra eşle {
                durum Siyah => hepsi.enUfağı(_._2)._1
                durum Beyaz => hepsi.enİrisi(_._2)._1
            })
            yoksa Biri(hepsi.enUfağı(_._2)._1)
        }
        yoksa Biri({
            için (hamle <- drm.seçenekler)
                ver hamle -> abHamle(drm.oyna(hamle), aramaDerinliğiSınırı)
        }.enUfağı(_._2)._1)

    tanım abHamle(drm: Durum, derinlik: Sayı): Sayı =
        eğer (drm.bitti || yeter(derinlik)) drm.skor
        yoksa eğer (drm.seçenekler.boşMu) azalt2(yeni Durum(drm.tahta, drm.karşıTaş), derinlik - 1, Sayı.EnUfağı, Sayı.Enİrisi)
        yoksa azalt(drm, derinlik, Sayı.EnUfağı, Sayı.Enİrisi)

    tanım azalt(drm: Durum, derinlik: Sayı, alfa: Sayı, beta: Sayı): Sayı =
        eğer (drm.bitti || yeter(derinlik)) drm.skor
        yoksa eğer (drm.seçenekler.boşMu) -artır2(yeni Durum(drm.tahta, drm.karşıTaş), derinlik - 1, Sayı.EnUfağı, Sayı.Enİrisi)
        yoksa {
            den yeniBeta = beta
            sayaç += drm.seçenekler.boyu
            drm.seçenekler.herbiriİçin { hamle => // onun hamleleri
                yeniBeta = enUfağı(yeniBeta, artır(drm.oyna(hamle), derinlik - 1, alfa, yeniBeta))
                eğer (alfa >= yeniBeta) geriDön alfa
            }
            yeniBeta
        }
    tanım artır(drm: Durum, derinlik: Sayı, alfa: Sayı, beta: Sayı): Sayı =
        eğer (drm.bitti || yeter(derinlik)) drm.skor
        yoksa eğer (drm.seçenekler.boşMu) -azalt2(yeni Durum(drm.tahta, drm.karşıTaş), derinlik - 1, Sayı.EnUfağı, Sayı.Enİrisi)
        yoksa {
            den yeniAlfa = alfa
            sayaç += drm.seçenekler.boyu
            drm.seçenekler.herbiriİçin { hamle =>
                yeniAlfa = enİrisi(yeniAlfa, azalt(drm.oyna(hamle), derinlik - 1, yeniAlfa, beta))
                eğer (yeniAlfa >= beta) geriDön beta
            }
            yeniAlfa
        }
    tanım azalt2(drm: Durum, derinlik: Sayı, alfa: Sayı, beta: Sayı): Sayı = {
        belirt(drm.seçenekler.doluMu, "azalt2 hata")
        den yeniAlfa = alfa
        sayaç += drm.seçenekler.boyu
        drm.seçenekler.herbiriİçin { hamle =>
            yeniAlfa = enİrisi(yeniAlfa, azalt(drm.oyna(hamle), derinlik - 1, yeniAlfa, beta))
            eğer (yeniAlfa >= beta) geriDön beta
        }
        yeniAlfa
    }
    tanım artır2(drm: Durum, derinlik: Sayı, alfa: Sayı, beta: Sayı): Sayı = {
        belirt(drm.seçenekler.doluMu, "artır2 hata")
        den yeniBeta = beta
        sayaç += drm.seçenekler.boyu
        drm.seçenekler.herbiriİçin { hamle =>
            yeniBeta = enUfağı(yeniBeta, artır(drm.oyna(hamle), derinlik - 1, alfa, yeniBeta))
            eğer (alfa >= yeniBeta) geriDön alfa
        }
        yeniBeta
    }

    tanım ustalık(derece: Ustalık): Birim = {
        dez ikili = derece eşle {
            durum Er       => (3, 25000)
            durum Çırak    => (4, 50000)
            durum Kalfa    => (5, 100000)
            durum Usta     => (6, 200000)
            durum Doktor   => (7, 500000)
            durum Aheste   => (7, 500000)
            durum Deha     => (8, 1000000)
            durum ÇokSabır => (8, 1000000)
            durum _        => (5, 100000)
        }
        aramaDerinliğiSınırı = ikili._1
        hamleSayısıÜstSınırı = ikili._2
    }

    tanım düzeydenUstalığa: Ustalık =
        eğer (aramaDerinliğiSınırı < 3) ErdenAz
        yoksa eğer (aramaDerinliğiSınırı > 8) DehadanÇok
        yoksa {
            aramaDerinliğiSınırı eşle {
                durum 3 => Er
                durum 4 => Çırak
                durum 5 => Kalfa
                durum 6 => Usta
                durum 7 => Doktor
                durum 8 => Deha
            }
        }
    den aramaDerinliğiSınırı = 3
    den hamleSayısıÜstSınırı = 25000

}

den i = 0
sınıf Oyun(tane: Sayı) {
    dez t = yeniTahta(tane)
    dez drm = yeni Durum(t, Siyah)

    den enİriSayaç = 0
    tanım oyna: Durum = {
        dez çıktı = döngü(drm)
        satıryaz(s"En İri Sayaç=$enİriSayaç")
        çıktı
    }

    getir scala.annotation.tailrec
    @tailrec  // ttodo
    gizli tanım döngü(drm: Durum): Durum = {
        drm.tahta.yaz("")
        i += 1
        eğer (drm.oyunBittiMi) geriDön drm
        eğer (i > tane * tane) { satıryaz("çok uzadı!"); geriDön drm }
        dez (eskiDurum, hamle) = ABa.hamleYap(drm) eşle {
            durum Biri(oda) => {
                eğer (ABa.sayaç > enİriSayaç) enİriSayaç = ABa.sayaç
                (drm, oda)
            }
            durum _ => {
                dez drm2 = yeni Durum(drm.tahta, drm.karşıTaş)
                satıryaz(s"Sıra yine ${drm2.sıra}'de")
                ABa.hamleYap(drm2) eşle {
                    durum Biri(oda) => {
                        eğer (ABa.sayaç > enİriSayaç) enİriSayaç = ABa.sayaç
                        (drm2, oda)
                    }
                    durum _ => bildir yeni KuralDışı("Burada olmamalı")
                }
            }
        }
        dez yeniDurum = eskiDurum.oyna(hamle)
        satıryaz(s"$i. hamle ${eskiDurum.sıra} $hamle:")
        döngü(yeniDurum)
    }
}

tanım dene1 = {
    dez tane = 4
    den t = yeni Tahta(tane, Yöney.doldur(tane * tane)(0))
    satıryaz("t"); t.yaz()
    dez foo = t.koy(Oda(1, 1), Beyaz)
    t = t.koy(Dizi(Oda(2, 2), Oda(3, 3)), Beyaz)
    t = t.koy(Dizi(Oda(2, 3), Oda(3, 2)), Siyah)
    dez t2 = t.oyna(Siyah, Oda(1, 2))
    satıryaz("t2"); t2.yaz()
    dez t3 = t2.oyna(Beyaz, Oda(1, 3))
    satıryaz("t3"); t3.yaz()
    satıryaz("t"); t.yaz()
    foo.yaz()
}

// Birim denemeler (unit tests)

tanım dene1b = { // hamle olmadığında sıra geçmesini dene
    dez tane = 4
    den t = yeni Tahta(tane, Yöney.doldur(tane * tane)(0))
    satıryaz("t"); t.yaz()
    t = t.koy(Dizi(Oda(0, 0), Oda(1, 0), Oda(2, 0),
        Oda(0, 1), Oda(1, 1), Oda(0, 2)), Beyaz)
    t = t.koy(Dizi(Oda(2, 1), Oda(2, 2), Oda(2, 3),
        Oda(1, 2)), Siyah)
    t.yaz()
    dez d = yeni Durum(t, Siyah)
    belirt(d.seçenekler.boşMu, "Sıra yine beyazın")
    belirt(ABa.hamleYap(d) == Hiçbiri, "Hamle yok")
    dez d2 = yeni Durum(t, Beyaz)
    belirt(d2.seçenekler.boyu == 4, "Dört seçenek")
    // satıryaz(ABa.hamleYap(d2))
    belirt(ABa.hamleYap(d2) == Biri(Oda(3, 2)), "Doğru hamle")
    t = t.oyna(Beyaz, Oda(3, 2))
    t.yaz()
}

tanım dene2 = {
    çıktıyıSil
    dez o = yeni Oyun(4) // 6
    //ABa.ustalık(Çırak)
    //ABa.ustalık(Çırak)
    ABa.aramaDerinliğiSınırı = 13
    ABa.hamleSayısıÜstSınırı = 1000000
    o.oyna
}

tanım dene2a = { // 4x4 tahta için dene2 ile aynı
    ABa.aramaDerinliğiSınırı = 13
    ABa.hamleSayısıÜstSınırı = 1000000
    den t = yeniTahta(4)
    den sıra: Taş = Siyah
    çıktıyıSil()
    için (hamleSayısı <- 1 |-| 14) {
        ABa.hamleYap(yeni Durum(t, sıra)) işle { oda =>
            satıryaz(s"Hamle $hamleSayısı: $sıra $oda oynadı:")
            t = t.oyna(sıra, oda)
            t.yaz()
        } alYoksa satıryaz(s"$sıra'ın hamlesi yok!")
        sıra = eğer (sıra == Siyah) Beyaz yoksa Siyah
    }
}

tanım dene2b = { // alfa beta arama doğru skoru kullanıyor mu?
    ABa.aramaDerinliğiSınırı = 13
    ABa.hamleSayısıÜstSınırı = 1000000
    den t = yeniTahta(4)
    çıktıyıSil()
    den say = 1
    için (
        (sıra, hamle) <- Dizi(
            Siyah -> Oda(0, 1),
            Beyaz -> Oda(0, 0),
            Siyah -> Oda(1, 0),
            Beyaz -> Oda(0, 2),
            Siyah -> Oda(0, 3),
            Beyaz -> Oda(2, 0),
            Siyah -> Oda(3, 0),
            Beyaz -> Oda(1, 3),
            Siyah -> Oda(2, 3),
            Beyaz -> Oda(3, 1),
            Siyah -> Oda(3, 2),
            Beyaz -> Oda(3, 3),
            Siyah -> Oda(-1, -1) // Bu hamle geçersiz. Hiç teşebbüs edilmeyecek.
        )
    ) ABa.hamleYap(yeni Durum(t, sıra)) işle { oda =>
        satıryaz(s"$say: $sıra için alfa-beta önerisi: $oda oynanan hamle: $hamle")
        t = t.oyna(sıra, hamle)
        t.yaz()
        say += 1
    } alYoksa satıryaz(s"$sıra'ın hamlesi yok!")
}

/* Elimizdeki alfabeta motoru ile en iyi hamleleri oynarsak sonuç şöyle oluyor:
  S S B S
  S S B S
  S S S S
  B B B S
  Siyah 6 taşla kazanır (11-5)
  Bu çok yanlış, çünkü iyi oynanırsa 4x4 tahtada hep ikinci oynayan Beyaz kazanmalı!
    https://en.wikipedia.org/wiki/Computer_Othello#Othello_4_%C3%97_4
*/
tanım dene2c = { // alfa beta arama doğru skoru kullanıyor mu?
    ABa.aramaDerinliğiSınırı = 13
    ABa.hamleSayısıÜstSınırı = 1000000
    den t = yeniTahta(4)
    çıktıyıSil()
    den say = 1
    için (
        (sıra, hamle) <- Dizi(
            Siyah -> Oda(0, 1),
            Beyaz -> Oda(2, 0),
            Siyah -> Oda(3, 3),
            Beyaz -> Oda(0, 2),
            Siyah -> Oda(3, 1),
            Beyaz -> Oda(2, 3),
            Siyah -> Oda(1, 3),
            Beyaz -> Oda(0, 0),
            Siyah -> Oda(3, 0),
            Beyaz -> Oda(3, 2),
            Siyah -> Oda(1, 0),
            Siyah -> Oda(0, 3),
            Siyah -> Oda(-1, -1) // Bu hamle geçersiz. Hiç teşebbüs edilmeyecek.
        )
    ) ABa.hamleYap(yeni Durum(t, sıra)) işle { oda =>
        satıryaz(s"$say: $sıra için alfa-beta önerisi: $oda oynanan hamle: $hamle")
        t = t.oyna(sıra, hamle)
        t.yaz()
        say += 1
    } alYoksa satıryaz(s"$sıra'ın hamlesi yok!")
}

tanım birDiziHamleYap(hamleler: Dizi[(Taş, Oda)]) = {
    ABa.aramaDerinliğiSınırı = 13
    ABa.hamleSayısıÜstSınırı = 1000000
    den t = yeniTahta(4)
    çıktıyıSil()
    den say = 1
    için ((sıra, hamle) <- hamleler)
        ABa.hamleYap(yeni Durum(t, sıra)) işle { oda =>
            satıryaz(s"$say: $sıra için alfa-beta önerisi: $oda oynanan hamle: $hamle")
            t = t.oyna(sıra, hamle)
            t.yaz()
            say += 1
        } alYoksa satıryaz(s"$sıra'ın hamlesi yok!")
}

tanım dene2d = { // after fixing how we use artır2 and azalt2
    /*
     *  S S S B
     *  S B B B
     *  S B B B
     *  B B B B
     */
    birDiziHamleYap(Dizi(
        Siyah -> Oda(0, 1),
        Beyaz -> Oda(2, 0),
        Siyah -> Oda(3, 1),
        Beyaz -> Oda(0, 0),
        Siyah -> Oda(3, 2),
        Beyaz -> Oda(0, 2),
        Siyah -> Oda(1, 0),
        Beyaz -> Oda(2, 3),
        Siyah -> Oda(1, 3),
        Beyaz -> Oda(0, 3),
        Siyah -> Oda(3, 0),
        Beyaz -> Oda(3, 3))
    )
}

tanım dene2e = { // BUG5.kojo
    /*
    S S S S
    S S S S
    S B S S
    B B B S
    Oyun bitti.
    Beyazlar: 4
    Siyahlar: 12
 */
    birDiziHamleYap(Dizi(
        Siyah -> Oda(0, 1),
        Beyaz -> Oda(2, 0),
        Siyah -> Oda(3, 0),
        Beyaz -> Oda(0, 0),
        Siyah -> Oda(1, 0),
        Beyaz -> Oda(0, 2),
        Siyah -> Oda(0, 3),
        Beyaz -> Oda(2, 3),
        Siyah -> Oda(1, 3),
        Beyaz -> Oda(3, 1),
        Siyah -> Oda(3, 1),
        Siyah -> Oda(3, 3),
        Siyah -> Oda(3, 2)
    ))
}

tanım dene2f = { // BUG5b.kojo
    birDiziHamleYap(Dizi(
        Siyah -> Oda(0, 1),
        Beyaz -> Oda(0, 2),
        Siyah -> Oda(0, 3),
        Beyaz -> Oda(2, 0),
        Siyah -> Oda(3, 0),
        Beyaz -> Oda(0, 0),
        Siyah -> Oda(1, 0),
        Siyah -> Oda(3, 3)
    ))
}

/* Bu yazılımcığı otello.kojo ve menu.kojo kullanıyor.
   Onun için denemeleri artık çalıştırmıyoruz */
// dene1c
// dene2f
// satıryaz("arama motoru hazır")
