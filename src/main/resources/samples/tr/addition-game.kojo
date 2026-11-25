// DİKKAT: toplama/çıkarma ve çarpma oyunlarının yazılımcıkları birbirlerine çok benziyor.
// Onun için birini değiştirirken diğerlerinde de aynı değişiklikleri yapmak iyi olur.
tümEkran()

dez oyunSüresi = 30 // Bir dakika sürsün istersen 60 girebilirsin

// iri1 ve iri2 değerlerini değiştirerek oyunun zorluğunu ayarlayabilirsin:
dez iri1 = 10
dez iri2 = 10

dez işlem = "+"

tanım farklıSayı(n: Sayı, m: Sayı) = {
    den n2 = 0
    yap {
        n2 = 2 + rastgele(m - 1)
    } yineleDoğruKaldıkça (n2 == n)
    n2
}

tanım yeniSoru() {
    sayı1 = farklıSayı(sayı1, iri1)
    sayı2 = farklıSayı(sayı2, iri2)
    yanıt = sayı1 + sayı2
    /*
     *  DİKKAT: BURADAN SONRASI toplama/çıkarma ve çarpma oyunlarında aynı
     */
    yanıtKutusu.yazıyıKur("")
    yanıtUzunluğu = yanıt.yazıya.boyu
}

tanım sayıYazısı(n: Sayı) = s" $n "
dez aaRengi = renkler.khaki // artalan yani arkaplan rengi

den sayı1 = 0
den sayı2 = 0
den yanıt = 0
den yanıtUzunluğu = 0
den bittiMi = yanlış

// java'nın arayüz kütüphanesi çok zengin
// birkaç tanesini Türkçe'ye çevirdik. ay modülüne koyduk.
dez yazıYüzü = Yazıyüzü("Sans Serif", 60)  // kıvrıksız çizikli yazı
dez yanıtKutusu = yeni ay.Yazıgirdisi(0) {
    // bu komutlar Yazıgirdisi türünün metodları:
    yazıYüzünüKur(yazıYüzü)
    sütunSayısınıKur(3)
    yatayDüzeniKur(ay.değişmez.merkez)
    artalanıKur(aaRengi)
    çerçeveyiKur(ay.çerçeveci.çizgiKenar(siyah))
}

tanım işlemPanosu = yeni ay.Sütun(
    yeni ay.Sıra(
        yeni ay.Tanıt(sayıYazısı(sayı1)) {
            yazıYüzünüKur(yazıYüzü)
            yatayDüzeniKur(ay.değişmez.merkez)
        },
        yeni ay.Tanıt(işlem) {
            yazıYüzünüKur(yazıYüzü)
            yatayDüzeniKur(ay.değişmez.merkez)
        },
        yeni ay.Tanıt(sayıYazısı(sayı2)) {
            yazıYüzünüKur(yazıYüzü)
            yatayDüzeniKur(ay.değişmez.merkez)
        }
    ) {
        artalanıKur(aaRengi)
    },
    yanıtKutusu
) {
    artalanıKur(aaRengi)
    çerçeveyiKur(ay.çerçeveci.boşKenar)
}

tanım yeniAraYüz() {
    araYüz.sil()
    araYüz = götür(-150, 0) -> Resim.arayüz(işlemPanosu)
    çiz(araYüz)
    yanıtKutusu.girdiOdağıOl() // basılan tuşlar yanıtKutusuna gelsin
}

den doğrular = 0
den yanlışlar = 0

den sonSorununZamanı = buAn
tanım yeterinceSüreKaldıMı = {
    dez kalanSüre = buAn - sonSorununZamanı
    eğer (kalanSüre > 100) {
        sonSorununZamanı = buAn
        doğru
    }
    yoksa yanlış
}

// yanıtKutusunun üstünde tuşlara basıldıkça birşeyler yapmamız gerek
yanıtKutusu.girdiDinleyiciEkle(yeni ay.olay.TuşUyarlayıcısı {

    // her tuşa basıldığında bu çalışır
    baskın tanım keyPressed(e: ay.olay.TuşaBasmaOlayı) {
        eğer (e.tuşKodu == tuşlar.kaç) { // Escape yani kaç tuşuna basılınca
            e.tüket()
            durdur()
            tümEkran() // zaten tüm ekran olduğu için bu komutla tüm ekrandan çıkar.. aç/kapa düğmesi gibi yani
        }
    }
    // her karakter okunduğunda da bu çalışır
    baskın tanım keyTyped(e: ay.olay.TuşaBasmaOlayı) {
        eğer (!e.tuşHarfi.sayıMı) { // sayı olmayan girdileri boşverelim
            e.tüket()
        }
    }
    // her tuştan kalkışta bakalım yanıt hazır ve doğru mu
    baskın tanım keyReleased(e: ay.olay.TuşaBasmaOlayı) {
        eğer (yanıtVerdiMi(e)) {
            dez x = yanıtKutusu.değeri
            yanıtaBak(x)
        }
        yoksa {
            yanıtKutusu.önalanıKur(siyah)
        }
    }

    tanım yanıtVerdiMi(e: ay.olay.TuşaBasmaOlayı) = {
        yanıtKutusu.yazıyıAl.boyu >= yanıtUzunluğu
    }
    tanım yanıtaBak(x: Sayı) {
        eğer (x == yanıt) {
            yanıtKutusu.önalanıKur(Renk(0, 220, 0)) // parlak bir yeşil
            doğrular += 1
            eğer (!bittiMi && yeterinceSüreKaldıMı) {
                sırayaSok(0.3) {
                    yeniSoru()
                    yeniAraYüz()
                    yanıtKutusu.önalanıKur(siyah)
                }
            }
        }
        yoksa {
            yanıtKutusu.önalanıKur(kırmızı)
            yanlışlar += 1
            eğer (!bittiMi) {
                yeniAraYüz()
            }
        }
    }

})

tanım çizMesaj(m: Yazı, r: Renk) {
    dez çerçeve = yazıÇerçevesi(m, 30)
    dez resim = kalemRengi(r) * götür(ta.x + (ta.eni - çerçeve.eni) / 2, 0) -> Resim.yazı(m, 30)
    çiz(resim)
}

tanım süreyiYönet() {
    tanım skor(doğruSayısı: Sayı, yanlışSayısı: Sayı) = doğruSayısı - yanlışSayısı
    den kalanSüre = oyunSüresi
    dez sayaç = götür(ta.x + 10, ta.y + 50) -> Resim.yazıRenkli(kalanSüre, 40, mavi)
    çiz(sayaç)
    sayaç.girdiyiAktar(Resim.tuvalBölgesi)

    yineleSayaçla(1000) {
        kalanSüre -= 1
        sayaç.güncelle(kalanSüre)

        eğer (kalanSüre == 0) {
            bittiMi = doğru
            dez mesaj = s"""      Oyun bitti!

            
            |Doğru yanıtlar: $doğrular
            |Yanlış yanıtlar: $yanlışlar
            |Skor: ${skor(doğrular, yanlışlar)}
            """
            yanıtKutusu.sakla
            araYüz.sil()
            çizMesaj(mesaj.kenarPayınıÇıkar, siyah)
            durdur()
        }
    }
}

silVeSakla()
çizSahne(aaRengi)
dez ta = tuvalAlanı
den araYüz: Resim = Resim.yatay(1)
yeniSoru()
yeniAraYüz()
süreyiYönet()
