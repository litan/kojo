// Copyright (C) 2015 Anusha Pant <anusha.pant@gmail.com>
// The contents of this file are subject to
// the GNU General Public License Version 3
//    http://www.gnu.org/copyleft/gpl.html

// Bülent Başaran (ben@scala.org) Türkçe'ye çevirirken ufak tefek değişiklikler yaptı.

// Enter tuşuyla tümEkranı aç kapa yapın
// d tuşuyla deneme yapmak için bütün satır ve sütünları evle doldurun
// Büyük boşluk tuşuyla soruyu rastgele değiştirin
// Escape tuşuyla pes edin ve iki dakika bitmeden oyuna son verin

dez oyunSüresi = 120 // saniye
// bu iki sayıyı azaltarak oyunu kolaylaştırabilir, artırarak da zorlaştırabilirsin:
dez enÇokKaçSatır = 9
dez enÇokKaçSütun = 9
dez mesaj = "En az 4 satır ve 4 sütun olmalı"
gerekli(enÇokKaçSatır > 3, mesaj)
gerekli(enÇokKaçSütun > 3, mesaj)

den tümEkran = yanlış
tümEkranTuval(); tümEkran = doğru
// yaklaşmayaİzinVerme()
dez ta = tuvalAlanı
dez evBoyu = enUfağı(200 / enÇokKaçSatır, 300 / enÇokKaçSütun)
tanım arayüzTanımı(sayılar: (Sayı, Sayı, Sayı)) = Resim.diziDikey(
    götür(-ta.x - 300, ta.y + 60) -> Resim.arayüz(yanıtPenceresi),
    götür(ta.x + 50, -ta.y + 100) -> evResmi(sayılar._1, sayılar._2, sayılar._3),
    götür(ta.x + 20, -ta.y - 340) * kalemRengi(mavi) -> Resim.yazı("Kaç ev var?", 30)
)
tanım yeniArayüz(sayılar: (Sayı, Sayı, Sayı)) {
    arayüz.sil()
    arayüz = arayüzTanımı(sayılar)
    çiz(arayüz)
    arayüz.ardaAl() // yoksa oyun bittiğinde yazdığımız yazı altta kalıyor. Neden?
    yanıtPenceresi.girdiOdağıOl() // klayve girdisini üstüne alsın ve ne gerekiyorsa yapsın
}
den arayüz: Resim = Resim.yatay(1) // şimdilik
 
// Yazılımcığımızın en becerikli, en çok çalışan kısmı bu:
dez yanıtPenceresi = yeni ay.Yazıgirdisi(0) {
    yazıYüzünüKur(yazıyüzü("Sans Serif", 60))
    sütunSayısınıKur(5)
    yatayDüzeniKur(ay.değişmez.merkez)
    artalanıKur(artalanRengi)
    çerçeveyiKur(ay.çerçeveci.çizgiKenar(siyah))
}

tanım ev() {
    kalemRenginiKur(siyah)
    boyamaRenginiKur(Renk(204, 102, 0))
    yinele(4) {
        ileri(evBoyu)
        sağ()
    }
    zıpla(evBoyu)
    sol()
    zıpla(5)
    boyamaRenginiKur(kırmızı)
    sağ(120)
    yinele(3) {
        ileri(evBoyu + 10)
        sağ(120)
    }
    sağ(60)
    zıpla(5)
    sol() // şimdi kuzeye bakıyor
    zıpla(-evBoyu)
}

tanım yazıYaz(y: Sayı, dx: Kesir = 0.0, dy: Kesir = 0.0) = {
    dez k = konum
    konumuKur(k.x + dx, k.y + dy)
    biçimleriBelleğeYaz()
    kalemRenginiKur(yeşil)
    tuvaleYaz(f"$y")
    biçimleriGeriYükle()
    konumuKur(k.x, k.y)
}

tanım evResmi(satırSayısı: Sayı, sütunSayısı: Sayı, artıkSayısı: Sayı) = Resim {
    den str = 0
    den stn = 0
    yinele(satırSayısı) {
        yinele(sütunSayısı) {
            ev()
            eğer (str == 0) {
                yazıYaz(stn, -14, 20); stn += 1
            }
            sağ()
            zıpla(evBoyu + 25)
            sol()
        }
        yazıYaz(str, -12, 10); str += 1
        sol()
        zıpla(sütunSayısı * (evBoyu + 25))
        sol()
        zıpla(2.6 * evBoyu)
        sol(180)
    }
    yinele(artıkSayısı) {
        ev()
        sağ()
        zıpla(evBoyu + 25)
        sol()
    }
}

dez artalanRengi = Renk(208, 144, 73)

den yanıt = 0
den yanıtUzunluğu = 0
den süreBittiMi = yanlış

// m == 2 => takılıp kalır!  (enÇokKaçSatır ve enÇokKaçSütun = 3 örneğin!)
tanım farklıBirSayı(n: Sayı, m: Sayı): Sayı = {
    tanım sayı() = 2 + rastgele(m - 1) // rastgele(1) = 0
    den n2 = 0
    den tekrar = 0
    yap {
        n2 = sayı()
        tekrar += 1
        eğer (tekrar == 100) {
            arayüz.sil()
            durdur()
            eğer (tümEkran == doğru) {
                tümEkranTuval(); tümEkran = yanlış
            }
            assert(yanlış, s"takıldı n=$n m=$m")
            return (0)
        }
    } yineleDoğruKaldıkça (n2 == n)
    n2
}

tanım yeniSoru(s1: Sayı, s2: Sayı) = {
    dez sayı1: Sayı = farklıBirSayı(s1, enÇokKaçSatır - 1)
    dez sayı2: Sayı = farklıBirSayı(s2, enÇokKaçSütun)
    dez sayı3: Sayı = rastgeleDiziden(1 to sayı2 - 1)
    dez sayılar = (sayı1, sayı2, sayı3)
    yanıtıKur(sayılar)
    sayılar
}

tanım yanıtıKur(s: (Sayı, Sayı, Sayı)) = {
    yanıt = s._1 * s._2 + s._3
    yanıtPenceresi.yazıyıKur("")
    yanıtUzunluğu = yanıt.yazıya.boyu
}

den doğruYanıtSayısı = 0
den yanlışYanıtSayısı = 0
den değiştirmeSayısı = 0

den sonSorununSorulduğuZaman = buAn
tanım yeterinceZamanVarMı() = {
    dez fark = buAn - sonSorununSorulduğuZaman
    eğer (fark > 100) {
        sonSorununSorulduğuZaman = buAn
        doğru
    }
    yoksa yanlış
}

getir ay.olay.TuşaBasmaOlayı
dez s = yeniSoru(0, 0)
yanıtPenceresi.girdiDinleyiciEkle(yeni ay.olay.TuşUyarlayıcısı {
    den sayılarAnımsa = s  // yanlış yanıt verince arayüzü baştan kuruyoruz. Soruyu hatırlayalım ki oyuncu tekrar deneyebilsin
    tanım yanıtıDenetle(x: Sayı) {
        eğer (x == yanıt) {
            yanıtPenceresi.önalanıKur(yeşil)
            doğruYanıtSayısı += 1
            eğer (!süreBittiMi && yeterinceZamanVarMı()) {
                sırayaSok(0.3) {
                    dez sayılar = yeniSoru(sayılarAnımsa._1, sayılarAnımsa._2)
                    sayılarAnımsa = sayılar
                    yeniArayüz(sayılar)
                    yanıtPenceresi.önalanıKur(siyah)
                }
            }
        }
        yoksa {
            yanıtPenceresi.önalanıKur(kırmızı)
            yanlışYanıtSayısı += 1
            eğer (!süreBittiMi) {
                sırayaSok(0.3) {  // kırmızı biraz dursun ki görelim
                    yeniArayüz(sayılarAnımsa)
                }
            }
        }
    }

    tanım yanıtHazırMı(olay: TuşaBasmaOlayı) = {
        yanıtPenceresi.yazıyıAl.boyu >= yanıtUzunluğu
    }

    // escape tuşuna basınca oyuna son verelim:
    baskın tanım keyPressed(olay: TuşaBasmaOlayı) {
        eğer (olay.tuşKodu == tuşlar.kaç) { // escape tuşu
            olay.tüket()
            eğer (!oyunBitti) {
                oyunSüresineBak(doğru)
            }
            durdur()
            eğer (tümEkran) {
                tümEkranTuval(); tümEkran = yanlış // tüm ekran modunu kapatalım
            }
        } // d tuşu yazılımcığımızı test etmek için:
        yoksa eğer (olay.tuşKodu == tuşlar.d) {
            dez sayılar = (enÇokKaçSatır, enÇokKaçSütun, enÇokKaçSütun)
            sayılarAnımsa = sayılar
            yanıtıKur(sayılar)
            yeniArayüz(sayılar)
        } // büyük boşluk tuşuna basarak soruyu değiştirebiliriz:
        yoksa eğer (olay.tuşKodu == tuşlar.boşluk) {
            değiştirmeSayısı += 1
            dez sayılar = yeniSoru(0, 0)
            sayılarAnımsa = sayılar
            yeniArayüz(sayılar)
        }
        yoksa eğer (olay.tuşKodu == tuşlar.gir) {
            tümEkran = !tümEkran
            tümEkranTuval() // tüm ekran modunu aç/kapat
        }
    }

    // sayı dışındaki girdileri yok sayalım
    baskın tanım keyTyped(olay: TuşaBasmaOlayı) {
        eğer (!olay.tuşHarfi.sayıMı) {
            olay.tüket()
        }
    }

    baskın tanım keyReleased(olay: TuşaBasmaOlayı) {
        eğer (yanıtHazırMı(olay)) {
            dez x = yanıtPenceresi.değeri
            yanıtıDenetle(x)
        }
        yoksa {
            yanıtPenceresi.önalanıKur(siyah)
        }
    }
})

tanım mesajıYaz(m: Yazı, renk: Renk) {
    dez yç = yazıÇerçevesi(m, 30)
    dez resim = kalemRengi(renk) * götür(ta.x + (ta.eni - yç.width) / 2, 0) ->
        Resim.yazı(m, 30)
    çiz(resim)
}

den oyunBitti = yanlış
tanım oyunSüresineBak(escapeTuşunaBasıldıMı: İkil = yanlış) {
    tanım sonuç(dy: Sayı, yy: Sayı, ps: Sayı) = dy - yy - 0.5*ps
    dez geçenSüreResmi = götür(ta.x + 10, ta.y + 50) -> Resim.yazıRenkli(
        geçenSüre,
        20, mavi)
    çiz(geçenSüreResmi)
    geçenSüreResmi.girdiyiAktar(Resim.tuvalBölgesi)

    yineleSayaçla(1000) {
        geçenSüre += 1
        geçenSüreResmi.güncelle(geçenSüre)

        eğer (geçenSüre == oyunSüresi || escapeTuşunaBasıldıMı) {
            oyunBitti = doğru
            süreBittiMi = doğru
            dez evre = eğer (escapeTuşunaBasıldıMı) s"Oyun $geçenSüre saniye sonra durduruldu."
            yoksa "Oyun bitti!"
            dez mesaj = s"""$evre
            |Doğrular: $doğruYanıtSayısı
            |Yanlışlar: $yanlışYanıtSayısı
            |Pas geçme: $değiştirmeSayısı
            |Skor: ${sonuç(doğruYanıtSayısı, yanlışYanıtSayısı, değiştirmeSayısı)}
            """
            arayüz.sil() // Hem temizlik hem de escape tuşu çalışsın diye.
            mesajıYaz(mesaj.kenarPayınıÇıkar, yeşil) // stripMargin: boşlukları temizleme metodu
            durdur()
        }
    }
}

silVeSakla()
artalanıKur(artalanRengi)

yeniArayüz(s)
den geçenSüre = 0
oyunSüresineBak()
sırayaSok(1) {
    yanıtPenceresi.girdiOdağıOl() // klayve girdisini üstüne alsın
}
