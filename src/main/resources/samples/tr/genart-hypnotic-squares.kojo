// Esinti kaynağı: https://generativeartistry.com/tutorials/hypnotic-squares/

tuvalBoyutlarınıKur(600, 600)
silVeSakla()
dez beyazımsı = Renk(0xF2F5F1)
artalanıKur(beyazımsı)
hızıKur(çokHızlı)
kalemRenginiKur(koyuGri)
kalemKalınlığınıKur(3)
dez ta = tuvalAlanı
dez kareSayısı = 7
dez kenarUzunluğu = enUfağı(ta.eni / kareSayısı, ta.boyu / kareSayısı)

tanım kare(boyu: Kesir, başlamaAdımı: Sayı, adımlar: Sayı, mx: Sayı, my: Sayı) {
    yinele(4) {
        ileri(boyu)
        sağ(90)
    }
    dez enUfakUzunluk = 3.0
    eğer (adımlar >= 0) {
        dez yeniUzunluk = kenarUzunluğu * adımlar / başlamaAdımı + enUfakUzunluk
        dez yeri = konum // kaplumbağanın konumu
        den yeniX = yeri.x + (boyu - yeniUzunluk) / 2
        den yeniY = yeri.y + (boyu - yeniUzunluk) / 2
        dez yatayKayış = ((yeniX - yeri.x) / (adımlar + 2)) * mx
        dez dikeyKayış = ((yeniY - yeri.y) / (adımlar + 2)) * my
        yeniX = yeniX + yatayKayış
        yeniY = yeniY + dikeyKayış
        konumuKur(yeniX, yeniY)
        kare(yeniUzunluk, başlamaAdımı, adımlar - 1, mx, my)
    }
}

tanım kutuİçindeKareler(x: Sayı, y: Sayı) {
    konumuKur(ta.x + x * kenarUzunluğu, ta.y + y * kenarUzunluğu)
    // rastgele kaç kare olsun?
    dez başlamaAdımı = rastgele(2, 8) // 2,3,4,5,6 ya da 7
    // merkezdeki kare noktayı yatay ya da dikey olarak rastgele kaydıralım
    dez mx = rastgele(-1, 2) // -1, 0 ya da 1
    dez my = rastgele(-1, 2) // mx, my ikisi de 0 ise, tam merkezde olacak
    kare(kenarUzunluğu, başlamaAdımı, başlamaAdımı - 1, mx, my)
}

// |-: bir yere kadar, burada, bir sayıya kadar anlamında: 
//   0,1,2,3,4,5,6 (yani 7 dahil değil)
yineleİçin(0 |- kareSayısı) { x =>
    yineleİçin(0 |- kareSayısı) { y =>
        kutuİçindeKareler(x, y)
    }
}
