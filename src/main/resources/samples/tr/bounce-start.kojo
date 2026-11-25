// Kodun evrimleşme tarihçesi
// ==========================
// 1- En önce bir topu canlandır döngüsüyle hareket ettirdik
// 2- Sonra yansıtma becerisi ekledik
// 3- Sonra bilardo masasını da çizdik
// 4- Sonra zıplayan bir top olsun diye yerçekimi ekledik
// 5- Sonra sürtünme ve zıpladıkça enerji kaybı ekledik
// 6- Sonra uzayda ikiz cisim arasındaki kütle çekimini ekledik
// 7- Sonra üç cisim
// 8- Sonra dört; artık 12 ayrı değişken ve beş yardımcı
// 9- Sonra Yöney ve Cisim sınıflarının eklenmesi: aynı benzetim, üçte bir kod
// 10- Sonra aynı kodun başka başlangıç değerleriyle kararlı üçlü vermesi
// 11- Sonra baskın merkezî kütleli bir güneş sistemi
// 12- Ve masaya dönüş: toplar artık birbirinden de sekiyor
//
// Örnekler > Benzetim menüsündeki on iki örnek, tam da bu sırayla.
//
// Adım 1/12: en yalın hâli. Bir daire çiz, her adımda biraz kaydır.
silVeSakla
gridiGöster
eksenleriGöster
dez yç = 10 // yarıçap
dez top = Resim.daire(yç) // topun resmi
top.çiz // tuvale çizelim
top.kondur(-200, -100) // sol alt köşeden harekete başlasın
// Devinim için canlandır komutuyla bir döngü başlatalım:
canlandır {
    // Bu küme içindeki komutlar saniyede yaklaşık 40 kere yinelenir
    top.kondur(top.konum.x + 2, top.konum.y + 1)
    eğer(top.konum.x >= 200) // yeterince gidince döngüyü durduralım
        durdur
}
