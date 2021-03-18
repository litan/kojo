// Değişik bir yöntem görelim.
// Kaplumbağaları eş zamanlı hareket ettirmek için
// kaplumbağa.davran yerine kaplumbağa.tepkiVer metodunu kullanalım
silVeSakla()
val k1 = yeniKaplumbağa(-500, 0)
k1.kalemRenginiKur(Renk(0, 0, 255, 120))
k1.kalemKalınlığınıKur(4)

val k2 = yeniKaplumbağa(500, 0)
k2.kalemRenginiKur(Renk(0, 255, 0, 120))

// tepkiVer'in çalıştırdığı komut dizisi GUI yani grafik ara yüz ile birlikte
// kojo'nun çalıştırdığı diğer komutlara paralel olarak çalışır.
// Yaklaşık olarak saniyede 30 kere çalıştırılırlar. 
// tepkiVer'in içinde çalışan komutlar çok zaman alırsa GUI yavaşlayabilir.
// Hatta en kötü durumda Kojo'yu tekrar çalıştırman gerekebilir.
k1.tepkiVer { bu =>
  bu.noktayaDön(fareKonumu)
  bu.ileri(2)
}

k2.tepkiVer { bu =>
  bu.çevir(k1)
  bu.ileri(1)
}
