// Bu program çalışınca fareyi tuvale götür.
silVeSakla()
dez k1 = yeniKaplumbağa(-500, 0)
dez k2 = yeniKaplumbağa(500, 0)

// davran komuduyla çalışan komut dizisi arkadan gelen komutlarla paralel çalışır
// bu sayede iki kaplumbağa eş zamanlı hareket ederler
k1.davran { kap => // komut dizisi içinde bu kaplumbağaya "kap" adını verdik
    kap.kalemRenginiKur(Renk(0, 0, 255, 120))
    kap.kalemKalınlığınıKur(4)
    yineleDoğruysa(doğru) {
      kap.noktayaDön(fareKonumu)
      kap.ileri(2)
    } 
}

k2.davran { o => // istediğimiz adı verebiliriz. "kap" demek zorunda değiliz :-)
    o.kalemRenginiKur(Renk(0, 255, 0, 120))
    yineleDoğruysa(doğru) {
      o.çevir(k1)
      o.ileri(1)
    }
}
