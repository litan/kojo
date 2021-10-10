// Esinti kaynağı: https://generativeartistry.com/tutorials/hypnotic-squares/

tuvalBoyutlarınıKur(600, 600)
silVeSakla()
val beyazımsı = Renk(0xF2F5F1)
artalanıKur(beyazımsı)
hızıKur(çokHızlı)
kalemRenginiKur(koyuGri)
kalemKalınlığınıKur(3)
val ta = tuvalAlanı
val kareSayısı = 7
val kenarUzunluğu = enUfağı(ta.eni / kareSayısı, ta.boyu / kareSayısı)

def kare(boyu: Kesir, başlamaAdımı: Sayı, adımlar: Sayı, mx: Sayı, my: Sayı) {
    yinele(4) {
        ileri(boyu)
        sağ(90)
    }
    val enUfakUzunluk = 3.0
    if (adımlar >= 0) {
        val yeniUzunluk = kenarUzunluğu * adımlar / başlamaAdımı + enUfakUzunluk
        val yeri = konum // kaplumbağanın konumu
        var yeniX = yeri.x + (boyu - yeniUzunluk) / 2
        var yeniY = yeri.y + (boyu - yeniUzunluk) / 2
        val yatayKayış = ((yeniX - yeri.x) / (adımlar + 2)) * mx
        val dikeyKayış = ((yeniY - yeri.y) / (adımlar + 2)) * my
        yeniX = yeniX + yatayKayış
        yeniY = yeniY + dikeyKayış
        konumuKur(yeniX, yeniY)
        kare(yeniUzunluk, başlamaAdımı, adımlar - 1, mx, my)
    }
}

def kutuİçindeKareler(x: Sayı, y: Sayı) {
    konumuKur(ta.x + x * kenarUzunluğu, ta.y + y * kenarUzunluğu)
    // rastgele kaç kare olsun?
    val başlamaAdımı = rastgele(2, 8) // 2,3,4,5,6 ya da 7
    // merkezdeki kare noktayı yatay ya da dikey olarak rastgele kaydıralım
    val mx = rastgele(-1, 2) // -1, 0 ya da 1
    val my = rastgele(-1, 2) // mx, my ikisi de 0 ise, tam merkezde olacak
    kare(kenarUzunluğu, başlamaAdımı, başlamaAdımı - 1, mx, my)
}

// until: bir yere kadar, burada, bir sayıya kadar anlamında: 
//   0,1,2,3,4,5,6 (yani 7 dahil değil)
yineleİçin(0 until kareSayısı) { x =>
    yineleİçin(0 until kareSayısı) { y =>
        kutuİçindeKareler(x, y)
    }
}
