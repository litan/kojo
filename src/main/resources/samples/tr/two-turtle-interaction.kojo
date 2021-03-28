// Bu program çalışınca fareyi tuvale götür.
silVeSakla()
val k1 = yeniKaplumbağa(-500, 0)
val k2 = yeniKaplumbağa(500, 0)

// davran komuduyla çalışan komut dizisi arkadan gelen komutlarla paralel çalışır
// bu sayede iki kaplumbağa eş zamanlı hareket ederler
k1.davran { bu => // komut dizisi içinde bu kaplumbağaya "bu" adını verdik
    bu.kalemRenginiKur(Renk(0, 0, 255, 120))
    bu.kalemKalınlığınıKur(4)
    yineleDoğruysa(doğru) {
      bu.noktayaDön(fareKonumu)
      bu.ileri(2)
    } 
}

k2.davran { o => // istediğimiz adı verebiliriz. Ama "bu" daha mantıklı
    o.kalemRenginiKur(Renk(0, 255, 0, 120))
    yineleDoğruysa(doğru) {
      o.çevir(k1)
      o.ileri(1)
    }
}
