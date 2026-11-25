// Şuradan esinlendi https://generativeartistry.com/tutorials/tiled-lines/

kojoVarsayılanİkinciBakışaçısınıKur()
silVeSakla()
artalanıKur(Renk(60, 63, 65))
dez ta = tuvalAlanı
dez n = 20 // 20 (yatay) x 20 (dikey) yani 400 dikdörtgene bölelim bütün tuvali
dez yatayAdım = ta.eni / n
dez dikeyAdım = ta.boyu / n

// Köşegenleri bir "durum class" ile tanımlamak ve kullanmak çok kolay
// (x, y) koordinatlarından başlayıp eni kadar sağa ve boyu kadar yukarı
// ama boyu eksi bir sayı olursa, yukarı yerine aşağıya giden köşegen
durum sınıf Köşegen(x: Kesir, y: Kesir, eni: Kesir, boyu: Kesir)

// kalemin kalınlığını ve rengini değiştirmeyi dene
tanım çizgidenResim(ç: Köşegen) =
    kalemBoyu(2) * kalemRengi(beyaz) * götür(ç.x, ç.y) ->
        Resim.köşegen(ç.eni, ç.boyu)

// rastgele iki köşegenden birini seçiyoruz
tanım çizgi(x: Kesir, y: Kesir, eni: Kesir, boyu: Kesir) = {
    dez soldanSağa = rastgeleİkil
    eğer (soldanSağa) Köşegen(x, y, eni, boyu) yoksa Köşegen(x, y + boyu, eni, -boyu)
}

// tuvalin sol alt köşesinden çizmeye başlayacağız
den çizgiler = Yöney.boş[Köşegen] // aslında çizgileri hesaplayıp bu yöneye kaydedeceğiz
dez solAltKöşeninXkoordinatı = ta.x
dez solAltKöşeninYkoordinatı = ta.y
yineleİçin(0 |- n) { sütun =>
    dez x = solAltKöşeninXkoordinatı + sütun * yatayAdım
    yineleİçin(0 |- n) { satır =>
        dez y = solAltKöşeninYkoordinatı + satır * dikeyAdım
        çizgiler = çizgiler :+ çizgi(x, y, yatayAdım, dikeyAdım)
    }
}
// burada da hepsini çizeceğiz
çiz(çizgiler.işle(çizgidenResim))
