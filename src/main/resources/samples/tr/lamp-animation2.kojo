// Gaz lambası yazılımcığımızı kaplumbağanın resim yöntemlerini ve
// geçiş nesnesini kullanarak yeniden yazalım

silVeSakla()
artalanıKur(renkler.darkSlateBlue) // eflatuna yakın koyu mavi

dez alev = Resim {
    boyamaRenginiKur(Renk.doğrusalDeğişim(0, 0, kırmızı, 0, 130, sarı))
    kalemRenginiKur(sarı)
    kalemKalınlığınıKur(3)
    sol(45)
    sağ(90, 100)
    sağ(90)
    sağ(90, 100)
}

dez lamba = Resim {
    boyamaRenginiKur(Renk.doğrusalDeğişim(0, 10, kırmızı, 0, -25, kahverengi))
    kalemKalınlığınıKur(2)
    kalemRenginiKur(siyah)
    sol(120)
    sağ(60, 100)
    sağ(210)
    sol(120, 115)
    sağ(210)
    sağ(60, 100)
}

çiz(lamba)

tanım büyütme(dizi: Dizi[Kesir]) = dizi(0)

tanım alevlendir(dizi: Dizi[Kesir]) = büyüt(büyütme(dizi)) -> alev

dez canlandırma = Geçiş(1, Dizi(1), Dizi(0.8), YumuşakGeçiş.DörtlüGirdiÇıktı, alevlendir, doğru)
dez canlandırma2 = canlandırmaDizisi(canlandırma, canlandırma.tersten)
oynat(canlandırma2.sonsuzYinelenme)