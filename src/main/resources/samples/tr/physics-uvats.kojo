// Sabit ivmeyle hızlanan bir arabayı simüle edelim
// Hızının ve konumunun değişimini de çiziyoruz

// 'd' veya esc (Escape) tuşuna basarak durdurabilirsin

// tümEkran()
silVeSakla()
eksenleriGöster()
ızgarayıGöster()
yaklaş(0.60, 400, 300)

dez ivme = 20.0 // ivme
dez hız0 = 10.0 // başlangıç hızı. Bunu -100 ya da -200 yapmayı dene!

dez araba = yeniKaplumbağa(0, 0, Görünüş.araba)

dez konumEğrisi = yeniKaplumbağa(0, 0, Görünüş.kalem)
konumEğrisi.kalemRenginiKur(mavi)

dez hızEğrisi = yeniKaplumbağa(0, hız0, Görünüş.kalem)
hızEğrisi.kalemRenginiKur(yeşil)

dez z0 = buSaniye // göreceli olarak bu anın zamanı
dez geçenZaman = yeniKaplumbağa(100, -50)

tanım zamanıGöster(y: Yazı) {
    geçenZaman.sil()
    geçenZaman.gizle()
    geçenZaman.kalemRenginiKur(mavi)
    geçenZaman.yazı(y)
}

canlandır {
    dez z = buSaniye - z0 // program başlayalı beri kaç saniye geçmiş
    // kinematik denklemlerle arabanın hızını ve ivmesini hesaplayalım
    dez hız = hız0 + ivme * z 
    dez konum = hız0 * z + 0.5 * ivme * z * z 
    araba.konumuKur(konum, 10)
    konumEğrisi.ilerle(z * 50, konum)
    hızEğrisi.ilerle(z * 50, hız)
    zamanıGöster(f"Zaman: $z%.1f saniye")
}

tuvaliEtkinleştir()
tuşaBasınca { t =>
    t eşle {
      durum tuşlar.d   => durdur()
      durum tuşlar.kaç => durdur()  // sol üstteki Escape tuşu
      durum _ =>
    }
}
