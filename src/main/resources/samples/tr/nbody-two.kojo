// Benzetim yolculuğu, adım 6/12: uzaya çıktık.
// Kütle çekimine ilelebet tutsak ikiz yıldızın dansı. Artık duvar yok; hızı
// değiştiren tek şey iki cismin birbirine uyguladığı çekim.
// yer:          yıldızların başlangıç noktası: x,y=40,40 biri, -40,-40 öbürü
// kçKatsayısı:  kütleçekim kuvveti (ve iki yıldızın dönüşsel ivmesi) bununla doğru orantılı
// ilkHız:       başlangıçta yıldızların birbirinden uzaklaşma hızı. Yatay doğrultuda
// örnekleme:    benzetimimiz yıldızları her saniyede kırk kere yeniden çizerek bize hareket
//               ediyor gibi gösterir. Yörüngeleri çizerken bu örnekleme peryodunu kullanırız.
//               Yani 13 hareketin sadece birini çizeriz.
dez (yer, boy, ilkHız, kçKatsayısı, örnekleme) = (40, 5, 1.1, 200, 13)
// Birinci yıldız kırmızı, ikincisi mavi, ama siyasi bir niyetimiz yok :-)
dez (r1, r2) = (kalemRengi(kırmızı) * boyaRengi(kırmızı), kalemRengi(mavi) * boyaRengi(mavi))
// c1 birinci cisimi temsil eden daire. c2 de ikinci cisim.
dez (c1, c2) = (r1 -> Resim.daire(boy), r2 -> Resim.daire(boy))
silVeSakla() // tuvali temizle ve kaplumbağayı sakla
çiz(götür(yer, yer) -> c1, götür(-yer, -yer) -> c2) // iki yıldızı başlangıç koordinatlarına götürelim ve çizelim
// cisimlerin hızı: delta x ve delta y, yani döngünün her adımında cisimlerin yatay ve dikey kayma miktarı
den (dx1, dy1, dx2, dy2) = (ilkHız, 0.0, -ilkHız, 0.0)
tanım açı(p1: Nokta, p2: Nokta): Kesir = tanjantınAçısı(mutlakDeğer((p1.y - p2.y) / (p1.x - p2.x)))
tanım uzaklık(p1: Nokta, p2: Nokta): Kesir = karekökü(kuvveti(p1.x - p2.x, 2) + kuvveti(p1.y - p2.y, 2))
// köşegenin dikey ve yatay izdüşümü
tanım dikey(köşegen: Kesir, açı: Kesir) = köşegen * sinüs(açı)
tanım yatay(köşegen: Kesir, açı: Kesir) = köşegen * kosinüs(açı)
// Çekim kuvveti iki cismin arasındaki uzaklığın karesiyle ters orantılı.
// Kütleleri, aynı olduğu için, göz ardı ediyoruz
tanım kuvvet(uzaklık: Kesir) = kçKatsayısı / kuvveti(enİrisi(0.01, uzaklık), 2)
den adım = 0 // yörüngeleri çizerken örneklemek için attığımız canlandırma adımlarını sayalım
canlandır { // bu komut aşağıdaki kümeyi saniyede 40 kere çalıştıracak. Herbirine bir adım diyelim.
    dez (p1, p2) = (c1.konum, c2.konum)
    adım += 1
    eğer (adım % örnekleme == 1) için ((p, c) <- Dizi((p1, r1), (p2, r2))) { çiz(götür(p.x, p.y) * c -> Resim.daire(0.6)) }
    dez (d, a) = (uzaklık(p1, p2), açı(p1, p2))
    dez f = kuvvet(d)
    dez (fx, fy) = (yatay(f, a), dikey(f, a))
    eğer (p1.x > p2.x) { dx1 -= fx; dx2 += fx } yoksa { dx1 += fx; dx2 -= fx }
    eğer (p1.y > p2.y) { dy1 -= fy; dy2 += fy } yoksa { dy1 += fy; dy2 -= fy }
    c1.konumuKur(p1.x + dx1, p1.y + dy1)
    c2.konumuKur(p2.x + dx2, p2.y + dy2)
}
