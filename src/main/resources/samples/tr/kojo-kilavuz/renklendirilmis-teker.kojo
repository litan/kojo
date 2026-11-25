// üçgenden tekerlek olur mu?
sil
canlandırmaHızınıKur(2) // çokHızlı'dan iki kat daha hızlı
// daha önce kullandığımız hızıKur(hız) komutuna benziyor,
// ama daha hassas ayar yapmamıza yarıyor. Girdisi adım atma süresini belirliyor
// onun için de bu iki komut ters çalışıyor:
// hız tarifi ve karşılık gelen adım atma süreleri yaklaşık olarak şöyle:
//   çokHızlı: 1
//   hızlı:    10
//   orta:     100 (varsayılan)
//   yavaş:    1000
yaklaş(0.2)
// 1-
kalemKalınlığınıKur(20)
kalemRenginiKur(rastgeleRenk)
dez boyu = 200
yinele(12) {
    sağ(30)
    // 2-
    kalemRenginiKur(rastgeleRenk)
    yinele(10) {
        // 3-
        kalemRenginiKur(rastgeleRenk)
        ileri(20)
        yinele(3) {  // gerisi ilk örnekle aynı
            ileri(boyu)
            dön(120)
            ileri(boyu)
        }
    }
}
