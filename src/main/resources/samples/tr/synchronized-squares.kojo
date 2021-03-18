// Bu örneğimizde birden çok kaplumbağaya eş zamanlı çizim yaptıracağız
silVeSakla()

// kare çizmek için yeni bir komut tanımlayalım
// k:       kareyi çizen kaplumbağa
// boy:     karenin kenar uzunluğu
// bekleme: kaplumbağayı yavaşlatmak ve eş zamanlı çizmek için
// 
// artlandaOynat komutu sayesinde kare komutu çalışmasını bitirmeden 
// diger komutları da başlatabiliriz
def kare(k: Kaplumbağa, boy: Sayı, bekleme: Sayı) = artalandaOynat {
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

kare(k1, 100, 100) // iki kat daha hızlı çizelim. Neden?
kare(k2, 50, 200)
kare(k3, 50, 200)
kare(k4, 50, 200)
kare(k5, 50, 200)
