// İngilizce'de Tic Tac Toe diye bilinen oyun
// Oyun kuramının sıfır toplamlı oyunları çözmek için geliştirdiği
// meşhur minimax algoritmasını göreceğiz bu yazılımcıkta!
// Daha hızlı çalışsın diye alfa-beta (a-b) budaması kullandık.
// Usta hatta yenilmez bir oyuncuya karşı oynamak nasıl olacak bakalım :-)
// Daha da ilginci yenilmeyen bir strateji nasıl programlanır onu göreceğiz.

sınıf Hane
durum nesne Bilgisayar yayar Hane // bilgisayar daire çizerek hamle yapacak
durum nesne İnsan yayar Hane // oyuncu çarpım işareti çizerek hamle yapacak
durum nesne Boş yayar Hane // tahtadaki boşluklar

silVeSakla()
dez ta = tuvalAlanı
artalanıKur(siyah)
//yaklaşmayaİzinVerme()
dez boy = 100

dez tahnanınBoyu = boy * 3
dez tahtaX = ta.x + (ta.eni - tahnanınBoyu) / 2
dez tahtaY = ta.y + (ta.boyu - tahnanınBoyu) / 2

dez kenarPayı = 20
dez çizgininKalınlığı = 8

tanım artalan() {
    kalemRenginiKur(renksiz)
    boyamaRenginiKur(siyah)
    dez pay = çizgininKalınlığı / 2
    konumuKur(pay, pay)
    yinele(4) {
        ileri(boy - 2 * pay)
        sağ(90)
    }
}

tanım çarpı = Resim {
    artalan()
    kalemKalınlığınıKur(çizgininKalınlığı)
    kalemRenginiKur(Renk.ada(200, 1.00, 0.50))
    konumuKur(kenarPayı, kenarPayı)
    noktayaGit(boy - kenarPayı, boy - kenarPayı)
    konumuKur(boy - kenarPayı, kenarPayı)
    noktayaGit(kenarPayı, boy - kenarPayı)
}

tanım daire = Resim {
    artalan()
    kalemKalınlığınıKur(çizgininKalınlığı)
    kalemRenginiKur(Renk.ada(120, 0.86, 0.64))
    konumuKur(boy / 2, kenarPayı)
    açıyaDön(0)
    dez boy2 = boy - 2 * kenarPayı
    sol(360, boy2 / 2)
}

dez çizgiler = Resim {
    kalemKalınlığınıKur(çizgininKalınlığı)
    yineleİçin(1 |-| 2) { n =>
        konumuKur(boy * n, 0)
        noktayaGit(boy * n, 3 * boy)
    }
    yineleİçin(1 |-| 2) { n =>
        konumuKur(0, boy * n)
        noktayaGit(3 * boy, boy * n)
    }
}

den tahta = Dizim.doldur[Hane](3, 3)(Boş)
den hamleResimleri = Dizim.doldur[Resim](3, 3)(boşResim)
tanım boşResim: Resim = Resim {}

den birSonrakiHamleÇarpıMı = doğru
den oyunBittiMi = yanlış

tanım değerlendir: Sayı = {
    eğer (oyunuKazandıMı(İnsan)) {
        -10
    }
    yoksa eğer (oyunuKazandıMı(Bilgisayar)) {
        10
    }
    yoksa {
        0
    }
}
  
dez sıfırBirİki = 0 |-| 2
tanım hamleKalmadıMı: İkil = {
    den bütünHanelerDolduMu = doğru
    yineleİçin(sıfırBirİki) { x =>
        yineleİçin(sıfırBirİki) { y =>
            eğer (tahta(x)(y) == Boş) {
                bütünHanelerDolduMu = yanlış
            }
        }
    }
    bütünHanelerDolduMu
}

dez AlfaMin = -1000
dez BetaMax = 1000

// minimax: kısaca rakibin en iyi hamlesine engel olma stratejisi
// İngilizce'de: minimize the maximum value the opponent could get with any move
tanım minimax(
    aramaDerinliği:     Sayı,
    bilgisayarınSırası: İkil,
    alpha:              Sayı, beta: Sayı
): Sayı = {
    dez kaçKaç = değerlendir
    eğer (kaçKaç == 10 || kaçKaç == -10) {
        geriDön kaçKaç
    }
    eğer (hamleKalmadıMı) {
        geriDön 0
    }
    eğer (bilgisayarınSırası) {
        den değer = AlfaMin
        den yeniAlfa = alpha
        yineleİçin(sıfırBirİki) { x =>
            yineleİçin(sıfırBirİki) { y =>
                eğer (tahta(x)(y) == Boş) {
                    tahta(x)(y) = Bilgisayar
                    değer = enİrisi(değer, minimax(aramaDerinliği + 1, !bilgisayarınSırası, yeniAlfa, beta))
                    tahta(x)(y) = Boş
                    eğer (değer >= beta) {
                        geriDön değer
                    }
                    yeniAlfa = enİrisi(yeniAlfa, değer)
                }
            }
        }
        geriDön değer
    }
    yoksa {
        den değer = BetaMax
        den yeniBeta = beta
        yineleİçin(sıfırBirİki) { x =>
            yineleİçin(sıfırBirİki) { y =>
                eğer (tahta(x)(y) == Boş) {
                    tahta(x)(y) = İnsan
                    değer = enUfağı(değer, minimax(aramaDerinliği + 1, !bilgisayarınSırası, alpha, yeniBeta))
                    tahta(x)(y) = Boş
                    eğer (değer <= alpha) {
                        geriDön değer
                    }
                    yeniBeta = enUfağı(yeniBeta, değer)
                }
            }
        }
        geriDön değer
    }
}

durum sınıf Hamle(x: Sayı, y: Sayı)

tanım enİyiHamleyiBul: Hamle = {
    den enYüksekKazanç = -1000;
    den enİyiHamle = Hamle(-1, -1)
    yineleİçin(sıfırBirİki) { x =>
        yineleİçin(sıfırBirİki) { y =>
            eğer (tahta(x)(y) == Boş) {
                tahta(x)(y) = Bilgisayar
                dez hamleninKazancı = minimax(0, yanlış, AlfaMin, BetaMax)
                tahta(x)(y) = Boş
                eğer (hamleninKazancı > enYüksekKazanç) {
                    enYüksekKazanç = hamleninKazancı
                    enİyiHamle = Hamle(x, y)
                }
            }
        }
    }
    enİyiHamle
}

tanım hamleYap(x: Sayı, y: Sayı, hamle: Hane, yeniResim: Resim) {
    yeniResim.konumuKur(tahtaX + x * boy, tahtaY + y * boy)
    tahta(x)(y) = hamle
    birSonrakiHamleÇarpıMı = !birSonrakiHamleÇarpıMı
    hamleResimleri(x)(y) = yeniResim
    çiz(yeniResim)
    kazanıldıMı()
    eğer (!oyunBittiMi) {
        eğer (hamleKalmadıMı) {
            oyunBittiMi = doğru
            bilgiVer("Berabere")
        }
    }
}

tanım bilgisayarHamlesiYap(resim: Resim) {
    dez hamle = enİyiHamleyiBul
    dez yeniResim = daire
    hamleYap(hamle.x, hamle.y, Bilgisayar, yeniResim)
}

tanım tahtayıÇiz() {
    çizgiler.konumuKur(tahtaX, tahtaY)
    çiz(çizgiler)
    yineleİçin(sıfırBirİki) { x =>
        yineleİçin(sıfırBirİki) { y =>
            dez resim = Resim { artalan }
            resim.konumuKur(tahtaX + x * boy, tahtaY + y * boy)
            çiz(resim)
            resim.fareyeBasınca { (_, _) =>
                eğer (!oyunBittiMi) {
                    eğer (birSonrakiHamleÇarpıMı) {
                        dez yeniResim = çarpı
                        resim.sil()
                        hamleYap(x, y, İnsan, yeniResim)
                        eğer (!oyunBittiMi) {
                            sırayaSok(0.5) {
                                bilgisayarHamlesiYap(resim)
                            }
                        }
                    }
                }
            }
            hamleResimleri(x)(y) = resim
            tahta(x)(y) = Boş
        }
    }
}

tanım oyunuKazandıMı(h: Hane): İkil = {
    // yerel tanımlar da yapabiliriz
    tanım sütun(x: Sayı) = tahta(x).diziye // tahta(x) bir Array. Dizi'ye çevirelim onu
    tanım satır(y: Sayı) = EsnekDizim(tahta(0)(y), tahta(1)(y), tahta(2)(y)).diziye
    tanım çapraz1 = EsnekDizim(tahta(0)(0), tahta(1)(1), tahta(2)(2)).diziye
    tanım çapraz2 = EsnekDizim(tahta(0)(2), tahta(1)(1), tahta(2)(0)).diziye

    dez hedef = EsnekDizim(h, h, h).diziye
    yineleİçin(sıfırBirİki) { x =>
        eğer (sütun(x) == hedef)
            geriDön doğru
    }
    yineleİçin(sıfırBirİki) { y =>
        eğer (satır(y) == hedef)
            geriDön doğru
    }
    eğer (çapraz1 == hedef) {
        geriDön doğru
    }
    çapraz2 == hedef
}

den mesajResmi: Resim = yok
tanım bilgiVer(yazı: Yazı) {
    // Geleceğin bilgisayar programcılarına önemli not: resimler çizildikleri
    // SIRAYLA üst üste binerler, en son çizilen en üstte durur. Mesajı hep
    // görebilmemiz için onu her seferinde YENİDEN, yani en son çiziyoruz.
    // Bir öncekini de siliyoruz; yoksa "Oyna!" ile "Kaybettin" üst üste gelirdi.
    eğer (mesajResmi != yok) mesajResmi.sil()
    dez metin = büyüt(2.0) * götür(-25, 15) * kalemRengi(beyaz) -> Resim.yazı(yazı)
    mesajResmi = Resim.diziDikeyDüzenli(metin, Resim.dikeyBoşluk(ta.boyu - 100))
    çizMerkezde(mesajResmi)
}
tanım kazanıldıMı() {
    eğer (oyunuKazandıMı(Bilgisayar)) {
        oyunBittiMi = doğru
        bilgiVer("Kaybettin")
    }
    yoksa eğer (oyunuKazandıMı(İnsan)) {
        oyunBittiMi = doğru
        bilgiVer("Kazandın!?") // eğer yazılımcığımızda hata yoksa buraya hiç gelmemeliyiz!
    }
}

tahtayıÇiz()
bilgiVer("Oyna!")
