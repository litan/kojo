//#yükle tr/tahta

sınıf Durum(dez tahta: Tahta, dez sıra: Taş) {
    dez karşıTaş = eğer (sıra == Beyaz) Siyah yoksa Beyaz
    tanım skor = tahta.drm(sıra) - tahta.drm(karşıTaş)
    tanım bitti = oyunBittiMi
    tanım oyunBittiMi = {
        eğer (yasallar.boyu > 0) yanlış yoksa {
            dez yeniDurum = yeni Durum(tahta, karşıTaş)
            yeniDurum.yasallar.boyu == 0
        }
    }
    tanım seçenekler = yasallar
    tanım yasallar = tahta.yasallar(sıra)
    tanım oyna(oda: Oda) = {
        dez yTahta = tahta.oyna(sıra, oda)
        yeni Durum(yTahta, karşıTaş)
    }
}
