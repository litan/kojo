// Yazan: Bülent Başaran ben@scala.org Yılı: 2022
tümEkranTuval()
dez dönüşSayısı = 7
dez yatayKosinüsDalgasınıDaÇiz = yanlış // sinüs dalgasını soldan sağa, kosinüsü yukarıdan aşağıya çizeceğiz. İstersek, bir de soldan sağa giden kosinüs eğrisi çizebiliriz.
silVeSakla
dez (yt, yy) = (4.0, 160.0) // topun ve yörüngesinin yarıçapları
yaklaşXY(0.6, 0.6, 600, -2 * yy)
dez adımSayısı = 120
dez açı = 360.0 / adımSayısı
dez (x0, y0) = (-yy, yy) // Topun dönmeye başladığı nokta. (0, 0) çalışmaz!
dez (rTop, rKos, rSin) = (mavi, kırmızı, yeşil)
dez top = götür(x0, y0) * boyaRengi(rTop) * kalemRengi(rTop) * kalemBoyu(4) -> Resim.daire(yt)
çiz(top)
// yörüngeyi biraz soluk renkle çizelim
çiz(götür(-2 * yy, yy) * kalemRengi(açıkGri) * kalemBoyu(0.5) -> Resim.daire(yy))
// soldan sağa giden sinüs dalgasını çizmek için de bir küçük top kullanacağız:
dez sin = götür(0, top.konum.y) * boyaRengi(renksiz) * kalemRengi(renksiz) -> Resim.daire(yt)
çiz(sin); sin.gizle()
den koY: Resim = _
eğer (yatayKosinüsDalgasınıDaÇiz) {
    koY = götür(0, 2 * yy) * boyaRengi(renksiz) * kalemRengi(renksiz) -> Resim.daire(yt)
    çiz(koY); koY.gizle()
}
// yukarıdan aşağıya giden kosinüs dalgası:
dez kos = götür(top.konum.x, -yy) * boyaRengi(renksiz) * kalemRengi(renksiz) -> Resim.daire(yt)
çiz(kos); kos.gizle()
dez rEksen = siyah.fadeOut(0.5) // todo
çiz(götür(-2 * yy, -14 * yy) * kalemRengi(rEksen) -> Resim.düz(0, 17 * yy)) // dikey eksen
çiz(götür(-4 * yy, -yy) * kalemRengi(rEksen) -> Resim.düz(3.8 * yy, 0)) // yatay eksen
çiz(götür(0, -0.8 * yy) * kalemRengi(rEksen) -> Resim.düz(0, 3.8 * yy)) // dikey eksen
çiz(götür(-4 * yy, yy) * kalemRengi(rEksen) -> Resim.düz(17 * yy, 0)) // yatay eksen
için (i <- 0 to 14) { // eğrilerin ekseni kestiği noktalar
    çiz(götür(120 * i, yy) * kalemRengi(rSin) -> Resim.daire(yt)) // sinüs(z)
    eğer (yatayKosinüsDalgasınıDaÇiz) çiz(götür(60 + 120 * i, yy) * kalemRengi(rKos) -> Resim.daire(yt)) // yatay kosinüs(z)
    çiz(götür(-2 * yy, -120 * i - yy - 120 / 2) * kalemRengi(rKos) -> Resim.daire(yt)) // dikey kosinüs(z)
}
// merkezden yörüngedeki topa uzanan bir ok çizelim. Merkez: (-2 * yy, yy)
çiz(götür(-2 * yy, yy) * kalemRengi(rEksen) -> Resim.daire(yt))
den ok = götür(-2 * yy, yy) * kalemRengi(siyah) -> Resim.düz(yy, 0)
çiz(ok)
tanım okuÇevir(n: Nokta) {
    ok.sil()
    ok = götür(-2 * yy, yy) * kalemRengi(siyah) -> Resim.düz(n.x + 2 * yy, n.y - yy)
    çiz(ok)
}
den yay = götür(-2 * yy, yy) * kalemRengi(rTop) -> Resim.yay(yy / 4, 0); çiz(yay)
tanım yayıGüncelle(n: Nokta) {
    yay.sil()
    dez ilkAçı = sinüsünAçısı((n.y - yy) / yy).dereceye
    dez açı = eğer (n.x < -2 * yy) { // dikey eksenin sol tarafı: 2. veya 3. çeyrek
        180 - ilkAçı
    }
    yoksa eğer (n.y < yy) { // 4. çeyrek
        360 + ilkAçı
    }
    yoksa // 1. çeyrek
        ilkAçı
    yay = götür(-2 * yy, yy) * kalemRengi(rTop) -> Resim.yay(yy / 4, açı)
    çiz(yay)
}
// topun yatay eksen üzerine izdüşümü yani açının kosinüsü
den izDüşümüX = götür(0, 0) -> Resim.düz(1, 1); çiz(izDüşümüX)
den kayanEksenX = götür(0, 0) -> Resim.düz(1, 1); çiz(kayanEksenX); kayanEksenX.gizle()
tanım kosinüsüGüncelle(n: Nokta) {
    izDüşümüX.sil(); kayanEksenX.sil()
    izDüşümüX = götür(-2 * yy, n.y) * kalemBoyu(4) * kalemRengi(rKos) -> Resim.düz(n.x + 2 * yy, 0)
    kayanEksenX = götür(-2 * yy, n.y) * kalemBoyu(0.5) * kalemRengi(açıkGri) -> Resim.düz(17 * yy, 0)
    çiz(izDüşümüX, kayanEksenX)
}
// topun dikey eksen üzerine izdüşümü yani açının sinüsü
den izDüşümüY = götür(0, 0) -> Resim.düz(1, 1); çiz(izDüşümüY)
den kayanEksenY = götür(0, 0) -> Resim.düz(1, 1); çiz(kayanEksenY); kayanEksenY.gizle()
tanım sinüsüGüncelle(n: Nokta) {
    izDüşümüY.sil(); kayanEksenY.sil()
    izDüşümüY = götür(top.konum.x, yy) * kalemBoyu(4) * kalemRengi(rSin) -> Resim.düz(0, n.y - yy)
    kayanEksenY = götür(top.konum.x, n.y - 17 * yy) * kalemBoyu(0.5) * kalemRengi(açıkGri) -> Resim.düz(0, 17 * yy)
    çiz(izDüşümüY, kayanEksenY)
}
tanım yeniTop = kalemBoyu(4.0) * kalemRengi(rTop) -> Resim.daire(yt)
den birÖncekiTop = yeniTop
dez başlangıçAnı = buSaniye
den z = 0
canlandır { // top merkez çevresindeki yörüngesinde sabit bir hızla dönsün
    z += 2 // dalga boyunu ayarlayalım
    sin.kondur(z, top.konum.y)
    çiz(götür(sin.konum.x, sin.konum.y) * kalemBoyu(1.0) * kalemRengi(rSin) -> Resim.daire(yt))
    kos.kondur(top.konum.x, -1 * yy - z + 2)
    çiz(götür(kos.konum.x, kos.konum.y) * kalemBoyu(1.0) * kalemRengi(rKos) -> Resim.daire(yt))
    eğer (yatayKosinüsDalgasınıDaÇiz) {
        koY.kondur(z, 4 * yy + top.konum.x - yy)
        çiz(götür(koY.konum.x, koY.konum.y) * kalemBoyu(1.0) * kalemRengi(rKos) -> Resim.daire(yt))
    }
    top.döndürMerkezli(açı, x0, 0)
    birÖncekiTop.sil()
    birÖncekiTop = götür(top.konum.x, top.konum.y) -> yeniTop; çiz(birÖncekiTop)
    okuÇevir(top.konum); yayıGüncelle(top.konum)
    sinüsüGüncelle(top.konum); kosinüsüGüncelle(top.konum)
    eğer (z > dönüşSayısı * 240 + adımSayısı / 6) { // 60 derece geçince duralım
        top.sil()
        durdur()
        satıryaz(s"Merkez etrafında $dönüşSayısı dönüş ${yuvarla(buSaniye - başlangıçAnı)} saniye sürdü.")
    }
}
çiz(götür(40, -100) -> Resim.yazı("Sinüs dalga => ...", Font("JetBrains Mono", 40), rSin))
çiz(götür(-30, -200) * döndürMerkezli(-90, 0, 0) -> Resim.yazı("Kosinüs dalga => ...", Font("JetBrains Mono", 40), rKos))
