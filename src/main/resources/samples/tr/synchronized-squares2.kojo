// Bu örnekle eş zamanlı çizim yapmanın değişik bir yolunu görelim.
// artalandaOynat komudu yerine davran metoduyla yapacağız eş zamanlı kodlamamızı
silVeSakla()

// kare çizmek için yeni bir komut tanımlayalım
// k:       kareyi çizen kaplumbağa
// boy:     karenin kenar uzunluğu
// bekleme: kaplumbağanın hızını ayarlayarak eş zamanlı çizmek için
tanım kare(k: Kaplumbağa, boy: Sayı, bekleme: Sayı) = {
    k.canlandırmaHızınıKur(bekleme)
    yinele(4) {
        k.ileri(boy)
        k.sağ()
    }
}
dez k1 = yeniKaplumbağa(0, 0)
dez k2 = yeniKaplumbağa(-200, 100)
dez k3 = yeniKaplumbağa(250, 100)
dez k4 = yeniKaplumbağa(250, -50)
dez k5 = yeniKaplumbağa(-200, -50)

// davran metoduyla çalışan komutlar işlemci tarafından paralel çalıştırılır
k1.davran { k =>
    kare(k, 100, 100)
}
k2.davran { k => kare(k, 50, 200) }
k3.davran { o => kare(o, 50, 200) }
k4.davran { o => kare(o, 50, 200) }
k5.davran { o => kare(o, 50, 200) }
