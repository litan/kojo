//#yükle tr/anaTanimlar
//#yükle tr/eTahta
//#yükle tr/bellek
//#yükle tr/alfabeta

sınıf Arayüz( // tahtayı ve taşları çizelim ve canlandıralım
    tahta:      ETahta,
    bellek:     Bellek,
    bilgisayar: Taş) {
    gizli tanım tahtayıKur = {
        silVeSakla
        çıktıyıSil()
        tümEkranTuval
        artalanıKur(koyuGri)
        dez içKöşeler = EsnekDizim.boş[Resim]
        dez içKöşeKalemRengi = Renk(255, 215, 85, 101) // soluk sarımsı bir renk
        için (x <- tahta.satırAralığı; y <- tahta.satırAralığı) {
            dez oda = Oda(y, x)
            dez kRenk = eğer (tahta.içKöşeMi(oda)) içKöşeKalemRengi yoksa mor
            dez r = kalemRengi(kRenk) * boyaRengi(taşınRengi(tahta.taş(y, x))) *
                götür(odanınNoktası(oda)) -> Resim.dikdörtgen(boy, boy)
            r.çiz()
            kareninOdası += (r -> oda)
            odanınKaresi += (oda -> r)
            eğer (kRenk == içKöşeKalemRengi) içKöşeler += r
            kareyiTanımla(r)
        }
        içKöşeler.dizi.işle(_.öneAl())
    }
    dez odaSayısı = tahta.odaSayısı
    // karelerin boyu inç başına nokta sayısı. 64 => 1cm'de yaklaşık 25 nokta den (/ 64 2.54)
    dez boy = 50
    dez (köşeX, köşeY) = (-odaSayısı / 2 * boy, -odaSayısı / 2 * boy) // tahtayı ortalamak için sol alt köşesini belirle
    dez (b2, b3, b4) = (boy / 2, boy / 3, boy / 4)
    dez kareninOdası = Eşlem.boş[Resim, Oda]
    dez odanınKaresi = Eşlem.boş[Oda, Resim]

    tanım boya(hane: Oda, taş: Taş) = odanınKaresi(hane).boyamaRenginiKur(taşınRengi(taş))

    gizli tanım odanınNoktası(oda: Oda, solAltKöşe: İkil = doğru) =
        eğer (solAltKöşe) Nokta(köşeX + oda.stn * boy, köşeY + oda.str * boy)
        yoksa Nokta(köşeX + oda.stn * boy + b2, köşeY + oda.str * boy + b2)

    gizli tanım bilgisayarınSırasıMı = tahta.oyuncu() == bilgisayar && !tahta.hamleYoksa

    gizli tanım kareyiTanımla(k: Resim) = {
        dez oda = kareninOdası(k)
        k.fareyeTıklayınca { (_, _) =>
            eğer (!bilgisayarŞimdiAramaYapıyor) {
                tahta.taş(oda) eşle {
                    durum Yok =>
                        dez yasal = tahta.hamleyiDene(oda)
                        eğer (yasal.boyu > 0) {
                            hamleyiYap(yasal, oda)
                            tahta.sıraGeriDöndüMü = yanlış
                            eğer (bittiMi()) bittiKaçKaç(tahta)
                            yoksa eğer (tahta.hamleYoksa) {
                                tahta.sıraGeriDöndüMü = doğru
                                sırayıÖbürOyuncuyaGeçir
                                satıryaz(s"Yasal hamle yok. Sıra yine ${tahta.oyuncu().adı}ın")
                                skoruGüncelle
                            }
                        }
                    durum _ =>
                }
                eğer (bilgisayarınSırasıMı) {
                    skorBilgisayarHamleArıyor
                    artalandaOynat { öneri() }
                }
            }
        }
        tanım odaRengi = taşınRengi(tahta.taş(oda))
        tanım renk = taşınRengi(tahta.oyuncu())
        k.fareGirince { (x, y) =>
            ipucu.konumuKur(odanınNoktası(oda, yanlış) - Nokta(b2, -b2))
            tahta.taş(oda) eşle {
                durum Yok => eğer (tahta.hamleyiDene(oda).boyu > 0) {
                    k.boyamaRenginiKur(renk)
                    ipucu.güncelle(s"${tahta.hamleGetirisi(oda)}")
                    hamleninÇevireceğiTaşlarıGöster(oda)
                }
                yoksa {
                    ipucu.güncelle(s"$oda")
                }
                durum _ => ipucu.güncelle(s"$oda")
            }
            ipucu.göster()
            ipucu.öneAl()
            ipucu.girdiyiAktar(k)
        }
        k.fareÇıkınca { (_, _) =>
            hamleninÇevireceğiTaşlarıSil()
            k.boyamaRenginiKur(odaRengi)
            k.saydamlık(1) // hamleninÇevireceğiTaşlarıGöster saydamlığı değiştiriyor
            ipucu.gizle()
        }
    }
    gizli dez boşOdaRengi = Renk(10, 111, 23) // koyuYeşil
    gizli tanım taşınRengi(t: Taş) = t eşle {
        durum Yok   => boşOdaRengi
        durum Beyaz => beyaz
        durum Siyah => siyah
    }

    tahtayıKur

    tanım bittiKaçKaç(tahta: ETahta) = eğer (!tahta.oyunBitti) {
        tahta.oyunBitti = doğru
        skorBitiş
        satıryaz(s"Oyun bitti.\n${tahta.kaçkaç()}")
    }

    tanım hamleyiGöster(oda: Oda) = {
        hamleResminiSil
        eğer (hamleResmiAçık) {
            hamleResmi = götür(odanınNoktası(oda, yanlış)) * kalemRengi(mavi) * kalemBoyu(3) *
                boyaRengi(renksiz) -> Resim.daire(b4)
            hamleResmi.girdiyiAktar(odanınKaresi(oda))
            hamleResmi.çiz()
        }
    }
    tanım hamleResminiSil = hamleResmi.sil()
    gizli den hamleResmi: Resim = Resim.daire(b4)
    gizli den hamleResmiAçık = yanlış
    tanım hamleyiAçKapa(d: Resim) = {
        hamleResmiAçık = !hamleResmiAçık
        eğer (hamleResmiAçık) düğmeSeçili(d) yoksa düğmeTepkisi(d)
        tahta.sonHamle eşle {
            durum Biri(hane) => hamleyiGöster(hane)
            durum _          =>
        }
    }

    tanım seçenekleriGöster = {
        seçenekResimleri.herbiriİçin { r => r.sil() }
        eğer (seçeneklerAçık) {
            dez sıralı = tahta.yasallar.işle { oda => (oda, tahta.hamleGetirisi(oda)) }.sırala { p => p._2 }.tersi
            eğer (sıralı.boyu > 0) {
                dez enİriGetiri = sıralı.başı._2
                seçenekResimleri = sıralı işle {
                    durum (oda, getirisi) =>
                        dez renk = eğer (getirisi == enİriGetiri) sarı yoksa turuncu
                        dez göster = götür(odanınNoktası(oda, yanlış)) * kalemRengi(renk) * kalemBoyu(3) *
                            boyaRengi(renksiz) -> Resim.daire(b4)
                        göster.girdiyiAktar(odanınKaresi(oda))
                        göster.çiz()
                        göster
                }
            }
        }
    }
    gizli den seçenekResimleri: Dizi[Resim] = Dizi()
    gizli den seçeneklerAçık = yanlış
    gizli tanım seçenekleriAçKapa(d: Resim) = {
        seçeneklerAçık = !seçeneklerAçık
        seçenekleriGöster
        eğer (seçeneklerAçık) düğmeSeçili(d) yoksa düğmeTepkisi(d)
        eğer (!seçeneklerAçık) seçenekleriKapa(d)
    }
    gizli tanım seçenekleriKapa(d: Resim) = {
        seçeneklerAçık = yanlış
        seçenekResimleri.herbiriİçin { r => r.sil() }
        düğmeTepkisi(d)
        d.kalemRenginiKur(renksiz)
    }

    den hamleninÇevireceğiTaşlar: Diz[Oda] = Diz()
    tanım hamleninÇevireceğiTaşlarıGöster(oda: Oda) = eğer (seçeneklerAçık) {
        dez yasal = tahta.hamleyiDene(oda)
        eğer (yasal.boyu > 0) {
            dez odalar = EsnekDizim(oda)
            yasal.herbiriİçin { komşu =>
                odalar += komşu.oda
                tahta.gerisi(komşu).alDoğruKaldıkça { oda =>
                    tahta.taş(oda) != Yok && tahta.taş(oda) != tahta.oyuncu()
                } herbiriİçin { o2 => odalar += o2 }
            }
            hamleninÇevireceğiTaşlar = odalar.diziye
            hamleninÇevireceğiTaşlar.işle { o =>
                odanınKaresi(o).boyamaRenginiKur(oyuncununSolukTaşRengi)
                odanınKaresi(o).saydamlık(0.8)
            }
        }
    }
    tanım hamleninÇevireceğiTaşlarıSil() = eğer (seçeneklerAçık) {
        hamleninÇevireceğiTaşlar.işle { o =>
            odanınKaresi(o).boyamaRenginiKur(taşınRengi(tahta.taş(o)))
            odanınKaresi(o).saydamlık(1)
        }
        hamleninÇevireceğiTaşlar = Diz()
    }
    tanım oyuncununSolukTaşRengi = tahta.oyuncu() eşle {
        durum Beyaz => Renk(244, 213, 244) // açık mor
        durum Siyah => Renk(98, 8, 97) // koyu mor
        durum _     => boşOdaRengi
    }

    tanım ileri() =
        eğer (!bilgisayarŞimdiAramaYapıyor) {
            bellek.ileriGit
            taşlarıGüncelle
            bittiMi()
        }

    tanım geri() =
        eğer (!bilgisayarŞimdiAramaYapıyor) {
            bellek.geriAl
            taşlarıGüncelle
        }

    tanım taşlarıGüncelle = {
        için (y <- tahta.satırAralığı; x <- tahta.satırAralığı)
            boya(Oda(y, x), tahta.taş(y, x))
        skoruGüncelle
        tahta.sonHamle eşle {
            durum Biri(hane) => hamleyiGöster(hane)
            durum _          => hamleResminiSil
        }
        seçenekleriGöster
    }
    tanım bittiMi() =
        eğer (tahta.hamleYoksa) {
            sırayıÖbürOyuncuyaGeçir
            eğer (tahta.hamleYoksa) {
                skorBitiş
                doğru
            }
            yoksa {
                sırayıÖbürOyuncuyaGeçir
                yanlış
            }
        }
        yoksa yanlış

    tanım yeniOyun() = eğer (!bilgisayarŞimdiAramaYapıyor && tahta.hamleSayısı() != 1) {
        tahta.başaAl("Yeni oyun:")
        için (x <- tahta.satırAralığı; y <- tahta.satırAralığı)
            boya(Oda(y, x), tahta.taş(y, x))
        bellek.başaAl()
        skorBaşlangıç
        hamleResminiSil
        eğer (bilgisayarınSırasıMı) {
            skorBilgisayarHamleArıyor
            artalandaOynat { öneri() }
        }
        seçenekleriGöster
    }

    tanım hamleyiYap(yasal: Dizi[Komşu], hane: Oda, duraklamaSüresi: Kesir = 0.0): Birim = {
        // todo: iyileştirmek için eTahta'da tanımla: tanım hamleYap(hane: Oda): Dizi[Oda]  çıktı olarak boyanması gereken odaları sun. İngilizce'ye tercüme ederken de öyle yaptım.
        tanım bütünTaşlarıÇevir = yasal.herbiriİçin { komşu =>
            dez komşuTaş = tahta.taş(komşu.oda)
            tahta.gerisi(komşu).alDoğruKaldıkça { oda =>
                tahta.taş(oda) != Yok && tahta.taş(oda) != tahta.oyuncu()
            }.herbiriİçin(taşıAltÜstYap(_))
            taşıAltÜstYap(komşu.oda)
        }
        tanım taşıAltÜstYap(oda: Oda): Birim = {
            tahta.taşıKur(oda)(tahta.oyuncu())
            boya(oda, tahta.oyuncu())
        }
        bellek.saklaTahtayı(doğru, tahta.sonHamle)
        tahta.sonHamle = Biri(hane)
        bütünTaşlarıÇevir
        tahta.taşıKur(hane)(tahta.oyuncu())
        boya(hane, tahta.oyuncu())
        satıryaz(s"Hamle ${tahta.hamleSayısı()} ${tahta.oyuncu()} $hane:")
        tahta.yaz
        sırayıÖbürOyuncuyaGeçir
        tahta.hamleSayısı.artır()
        bellek.yeniHamleYapıldı
        skoruGüncelle
        hamleyiGöster(hane)
        eğer (duraklamaSüresi > 0) durakla(duraklamaSüresi)
    }

    tanım sırayıÖbürOyuncuyaGeçir = {
        tahta.oyuncu.değiştir()
        seçenekleriGöster
    }

    tanım özdevin(süre: Kesir = 0.0) = zamanTut("Özdevinimli oyun") {
        özdevinimliOyun(
            // abArama,  // yavaş! bütün Kojo'yu donduruyor!
            köşeYaklaşımı,
            süre)
    }("sürdü")
    tanım özdevinimliOyun( // özdevinim ve bir kaç hamle seçme yöntemi/yaklaşımı (heuristic)
        yaklaşım:        İşlev1[Dizi[Oda], Belki[Oda]],
        duraklamaSüresi: Kesir /*saniye*/
    ) = {
        dez dallanma = EsnekDizim.boş[Sayı]
        den oyna = doğru
        yineleDoğruKaldıkça (oyna) yaklaşım(tahta.yasallar) eşle {
            durum Biri(oda) =>
                dallanma += tahta.yasallar.boyu
                hamleyiYap(tahta.hamleyiDene(oda), oda, duraklamaSüresi)
            durum _ =>
                sırayıÖbürOyuncuyaGeçir
                yaklaşım(tahta.yasallar) eşle {
                    durum Biri(oda) =>
                        satıryaz(s"Yasal hamle yok. Sıra yine ${tahta.oyuncu().adı}ın")
                        dallanma += tahta.yasallar.boyu
                        hamleyiYap(tahta.hamleyiDene(oda), oda, duraklamaSüresi)
                    durum _ =>
                        bittiKaçKaç(tahta)
                        eğer (dallanma.sayı > 0) {
                            dez d = dallanma.dizi
                            satıryaz(s"Oyun ${d.boyu} kere dallandı. Dal sayıları: ${d.yazıYap(",")}")
                            satıryaz(s"Ortalama dal sayısı: ${yuvarla(d.topla / (1.0 * d.boyu), 1)}")
                            satıryaz(s"En iri dal sayısı: ${d.enİrisi}")
                        }
                        oyna = yanlış
                }
        }
    }

    tanım abArama(yasallar: Dizi[Oda]): Belki[Oda] = // yasallar yerine tahtadanTahta işlevini girdi olarak kullanıyor
        ABa.hamleYap(yeni Durum(tahtadanTahta, tahta.oyuncu()))
    den bilgisayarŞimdiAramaYapıyor = yanlış
    tanım öneri(): Birim = {
        bilgisayarŞimdiAramaYapıyor = doğru
        skorBilgisayarHamleArıyor
        dez aTahta = tahtadanTahta
        dez drm = yeni Durum(aTahta, tahta.oyuncu())
        eğer (drm.bitti) bittiKaçKaç(tahta)
        yoksa {
            dez hamle = ABa.hamleYap(drm) eşle {
                durum Biri(oda) => oda
                durum _ =>
                    sırayıÖbürOyuncuyaGeçir
                    ABa.hamleYap(yeni Durum(drm.tahta, drm.karşıTaş)) eşle {
                        durum Biri(oda) => oda
                        durum _         => bildir yeni Exception("Burada olmamalı")
                    }

            }
            hamleyiYap(tahta.hamleyiDene(hamle), hamle)
            eğer (!bittiMi()) {
                eğer (tahta.hamleYoksa) {
                    sırayıÖbürOyuncuyaGeçir
                    satıryaz(s"Yasal hamle yok. Sıra yine ${tahta.oyuncu().adı}ın")
                    eğer (bilgisayarınSırasıMı) {
                        öneri()
                    }
                }
            }
        }
        bilgisayarŞimdiAramaYapıyor = yanlış
    }
    tanım tahtadanTahta: Tahta = { // elektronik tahtadan arama tahtası oluşturalım
        dez tane = odaSayısı
        den t = yeni Tahta(tane, Yöney.doldur(tane * tane)(0))
        tanım diziden(dizi: Dizi[(Sayı, Sayı)])(taş: Taş) = t = t.koy(dizi.işle(p => Oda(p._1, p._2)), taş)
        için (t <- Dizi(Beyaz, Siyah))
            diziden(için (y <- 0 |- tane; x <- 0 |- tane; eğer (t == tahta.taş(y, x))) ver (y, x))(t)
        t
    }
    tanım köşeYaklaşımı(yasallar: Dizi[Oda]): Belki[Oda] = rastgeleSeç(yasallar.ele(tahta.köşeMi(_))) eşle {
        durum Biri(oda) => Biri(oda) // köşe bulduk!
        durum _ => rastgeleSeç(yasallar.ele(tahta.içKöşeMi(_))) eşle {
            durum Biri(oda) => Biri(oda)
            durum _ => { // tuzakKenarlar tuzakKöşeleri içeriyor
                dez tuzakKenarOlmayanlar = yasallar.eleDeğilse(tahta.tuzakKenarMı(_))
                enİriGetirililerArasındanRastgele(
                    eğer (!tuzakKenarOlmayanlar.boşMu) tuzakKenarOlmayanlar
                    yoksa { // tuzak kenarlardan getirisi en iri olanlardan seçiyoruz
                        dez tuzakKöşeOlmayanlar = yasallar.eleDeğilse(tahta.tuzakKöşeMi(_))
                        eğer (tuzakKöşeOlmayanlar.boşMu) yasallar yoksa tuzakKöşeOlmayanlar
                    }
                )
            }
        }
    }
    tanım rastgeleSeç[T](dizi: Dizi[T]): Belki[T] = eğer (dizi.boşMu) Hiçbiri else
        Biri(dizi.düşür(rastgele(dizi.boyu)).başı)
    tanım enİriGetirililerArasındanRastgele(yasallar: Dizi[Oda]): Belki[Oda] =
        rastgeleSeç(enGetirililer(yasallar))
    tanım enGetirililer(yasallar: Dizi[Oda]): Dizi[Oda] = {
        tanım bütünEnİriler[A, B: Ordering](d: Dizin[A])(iş: A => B): Dizin[A] = {
            d.sırala(iş).tersi eşle {
                durum Dizin()       => Dizin()
                durum baş :: kuyruk => baş :: kuyruk.alDoğruKaldıkça { oda => iş(oda) == iş(baş) }
            }
        }
        bütünEnİriler(yasallar.dizine) { tahta.hamleGetirisi(_) }
    }

    gizli tanım düğme(x: Kesir, y: Kesir, boya: Renk, mesaj: Yazı) = {
        dez d = götür(x, y) * kalemRengi(renksiz) * boyaRengi(boya) -> Resim.dizi(
            götür(boy / 5, b2 + b4 / 3) -> Resim.yazıRenkli(mesaj, 10, beyaz),
            kalemBoyu(3) -> Resim.daire(boy * 9 / 20))
        düğmeTepkisi(d)
        d.çiz()
        d
    }
    gizli tanım düğmeTepkisi(d: Resim, rFareGirince: Renk = beyaz, rFareÇıkınca: Renk = renksiz) = {
        d.fareGirince { (_, _) => d.kalemRenginiKur(rFareGirince) }
        d.fareÇıkınca { (_, _) => d.kalemRenginiKur(rFareÇıkınca) }
    }
    gizli tanım düğmeSeçili(d: Resim) = düğmeTepkisi(d, renksiz, beyaz)

    gizli dez (dx, dy) = ((0.8 + odaSayısı) * boy + köşeX, köşeY + b2)
    gizli dez d0 = {
        dez d = düğme(dx, dy + 2 * boy, pembe, "öneri")
        d.fareGirince { (_, _) =>
            d.kalemRenginiKur(eğer (tahta.yasallar.boşMu) kırmızı yoksa beyaz)
        }
        d.fareyeTıklayınca { (_, _) =>
            eğer (!bilgisayarŞimdiAramaYapıyor && !tahta.hamleYoksa) {
                artalandaOynat { öneri() }
            }
        }
    }
    gizli dez d1 = {
        dez d = düğme(dx, dy + boy, sarı, "seçenekler")
        d.fareyeTıklayınca { (_, _) => seçenekleriAçKapa(d) }
        d
    }
    gizli dez d2 = {
        dez d = düğme(dx + boy, dy + boy, mavi, "son hamle aç/kapa")
        d.kalemRenginiKur(renksiz) // başlangıçta son hamleyi görmeyelim
        d.fareyeTıklayınca { (_, _) => hamleyiAçKapa(d) }
        d
    }
    düğme(dx + boy, dy + 2 * boy, turuncu, "tüm ekran aç/kapa").fareyeTıklayınca { (_, _) => tümEkranTuval() }
    düğme(dx, dy, kırmızı, "özdevin").fareyeTıklayınca { (_, _) => eğer (!bilgisayarŞimdiAramaYapıyor) özdevin() }
    düğme(dx + boy, dy, yeşil, "yeni oyun").fareyeTıklayınca { (_, _) => yeniOyun() }
    düğme(dx, dy + 3 * boy, açıkGri, "geri").fareyeTıklayınca { (_, _) => geri() }
    düğme(dx + boy, dy + 3 * boy, renkler.blanchedAlmond, "ileri").fareyeTıklayınca { (_, _) => ileri() }
    gizli dez skorYazısı = {
        dez y = {
            dez tahtaTavanı = dy + (odaSayısı - 0.75) * boy
            dez düğmelerinTavanı = dy + 5 * boy
            enİrisi(tahtaTavanı, düğmelerinTavanı)
        }
        dez yazı = götür(dx - b3, y) -> Resim.yazı(s"", yazıyüzü("JetBrains Mono", 20), sarı)
        yazı.çiz(); yazı
    }
    tanım skorBitiş = {
        dez fark = tahta.say(Beyaz) - tahta.say(Siyah)
        dez msj =
            eğer (fark > 0)
                s"Beyaz $fark taşla kazandı"
            yoksa eğer (fark < 0)
                s"Siyah ${-fark} taşla kazandı"
            yoksa "Berabere!"
        skorYazısı.güncelle(s"$msj\n${tahta.kaçkaç(doğru)}")
    }
    tanım skorBaşlangıç = skorYazısı.güncelle(s"${tahta.oyuncu().adı.ilkHarfiBüyült} başlar")
    tanım skoruGüncelle = skorYazısı.güncelle(s"${tahta.hamleSayısı()}. hamle${eğer (tahta.sıraGeriDöndüMü) " yine " yoksa " "}${tahta.oyuncu().adı}ın\n${tahta.kaçkaç(doğru)}")
    tanım skorBilgisayarHamleArıyor = skorYazısı.güncelle(s"${tahta.hamleSayısı()}. hamle. Bilgisayar arıyor...\n${tahta.kaçkaç(doğru)}")
    skorBaşlangıç

    gizli dez ipucu = Resim.yazıRenkli("", 20, kırmızı)
    ipucu.çiz()

    tuşaBasınca { t =>
        t eşle {
            durum tuşlar.sağ    => ileri()
            durum tuşlar.sol    => geri()
            durum tuşlar.yukarı =>
                eğer (!bilgisayarŞimdiAramaYapıyor) {
                    artalandaOynat { öneri() }
                }
            durum _ =>
        }
    }

    eğer (bilgisayarınSırasıMı) {
        artalandaOynat { öneri() }
    }
}
