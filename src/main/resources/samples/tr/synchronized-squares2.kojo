// Bu örnekle eş zamanlı çizim yapmanın değişik bir yolunu görelim.
// artalandaOynat komudu yerine davran metoduyla yapacağız eş zamanlı kodlamamızı
silVeSakla()

// kare çizmek için yeni bir komut tanımlayalım
// k:       kareyi çizen kaplumbağa
// boy:     karenin kenar uzunluğu
// bekleme: kaplumbağanın hızını ayarlayarak eş zamanlı çizmek için
def kare(k: Kaplumbağa, boy: Sayı, bekleme: Sayı) = {
    k.canlandırmaHızınıKur(bekleme)
    yinele(4) {
        k.ileri(boy)
        k.sağ()
    }
}
val k1 = yeniKaplumbağa(0, 0)
val k2 = yeniKaplumbağa(-200, 100)
val k3 = yeniKaplumbağa(250, 100)
val k4 = yeniKaplumbağa(250, -50)
val k5 = yeniKaplumbağa(-200, -50)

// davran metoduyla çalışan komutlar işlemci tarafından paralel çalıştırılır
k1.davran { bu => 
    kare(bu, 100, 100)    
}
k2.davran { şu => kare(şu, 50, 200) }
k3.davran { bu => kare(bu, 50, 200) }
k4.davran { o => kare(o, 50, 200) }
k5.davran { o => kare(o, 50, 200) }
