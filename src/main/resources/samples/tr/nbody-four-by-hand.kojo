// Benzetim yolculuğu, adım 8/12: dördüncü cisim, hâlâ türsüz.
// Aynı dört cisim, ama türler olmadan: her cisim için ayrı bir değişken, her
// hız bileşeni için ayrı bir "den", ve kuvveti açıyla ayrıştıran beş yardımcı.
// Dört Cisim örneğiyle yan yana okuyun: aynı benzetim, aynı sonuç. Türler bir
// şey eklemiyor, ÇIKARIYOR -- 12 değişken bir diziye, beş yardımcı tek dv'ye
// iniyor, ve beşinci bir cisim eklemek orada tek satır, burada dört blok.
dez (kon, boy, ilkHız, yerçekimiSabiti, örnekle) = (50, 5, 1.0, 100, 4)
dez fırça = 6 // yörüngeleri boyar. 0 = kapalı
dez saydamlık = 50
dez kırmızı = renkKur(255, 0, 0, saydamlık)
dez yeşil = renkKur(0, 255, 0, saydamlık)
dez mavi = renkKur(0, 110, 255, saydamlık)
dez turuncu = renkKur(255, 255, 0, saydamlık) // aslında sarı
dez (c1, c2, c3, c4) = (kırmızı, mavi, yeşil, turuncu)
dez b1 = Resim.daire(boy).kalemRenkli(c1).boyalı(c1)
dez b2 = Resim.daire(boy).kalemRenkli(c2).boyalı(c2)
dez b3 = Resim.daire(boy).kalemRenkli(c3).boyalı(c3)
dez b4 = Resim.daire(boy).kalemRenkli(c4).boyalı(c4)
sil(); gizle()
çiz(b1.taşınmış(kon + rastgele(10), kon + rastgele(10)),
    b2.taşınmış(-kon + rastgele(10), -kon + rastgele(10)),
    b3.taşınmış(-kon + rastgele(10), kon + rastgele(10)),
    b4.taşınmış(kon + rastgele(10), -kon + rastgele(10)))
den (dx1, dy1, dx2, dy2, dx3, dy3, dx4, dy4) = (ilkHız, 0.0, -ilkHız, 0.0, 0.0, 0.0, 0.0, 0.0)
tanım açı(p1: Nokta, p2: Nokta): Kesir = tanjantınAçısı(mutlakDeğer((p1.y - p2.y) / (p1.x - p2.x)))
tanım uzaklık(p1: Nokta, p2: Nokta): Kesir = karekökü(kuvveti(p1.x - p2.x, 2) + kuvveti(p1.y - p2.y, 2))
tanım dikey(çapraz: Kesir, açı: Kesir) = çapraz * sinüs(açı)
tanım yatay(çapraz: Kesir, açı: Kesir) = çapraz * kosinüs(açı)
tanım kuvvet(uzaklık: Kesir) = yerçekimiSabiti / kuvveti(enİrisi(boy * 3, uzaklık), 2)
tanım dv(p1: Nokta, p2: Nokta) = {
  dez a = açı(p1, p2)
  dez f = kuvvet(uzaklık(p1, p2))
  dez (fx, fy) = (yatay(f, a), dikey(f, a))
  (eğer (p1.x > p2.x) -fx yoksa fx,
   eğer (p1.y > p2.y) -fy yoksa fy)
}
den adım = 1
canlandır {
  dez (p1, p2, p3, p4) = (b1.konum, b2.konum, b3.konum, b4.konum)
  eğer (fırça > 0 && adım % örnekle == 1) {
    için ((p, c) <- Diz((p1, c1), (p2, c2), (p3, c3), (p4, c4)))
      çiz(Resim.daire(fırça).kalemRenkli(c).boyalı(c).taşınmış(p.x, p.y))
  }
  adım += 1
  için (p <- Diz(p2, p3, p4)) { dez (fx, fy) = dv(p1, p); dx1 += fx; dy1 += fy }
  için (p <- Diz(p3, p4, p1)) { dez (fx, fy) = dv(p2, p); dx2 += fx; dy2 += fy }
  için (p <- Diz(p4, p1, p2)) { dez (fx, fy) = dv(p3, p); dx3 += fx; dy3 += fy }
  için (p <- Diz(p1, p2, p3)) { dez (fx, fy) = dv(p4, p); dx4 += fx; dy4 += fy }
  b1.konumuKur(p1.x + dx1, p1.y + dy1)
  b2.konumuKur(p2.x + dx2, p2.y + dy2)
  b3.konumuKur(p3.x + dx3, p3.y + dy3)
  b4.konumuKur(p4.x + dx4, p4.y + dy4)
}
