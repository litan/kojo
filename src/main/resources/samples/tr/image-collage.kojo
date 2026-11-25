// Kojo ile yüklenen bazı imgelerle bir örnek resim çizelim 
tanım kadın = Resim.imge(Görünüş.elSallayanKadın)
tanım araba = Resim.imge(Görünüş.araba)
tanım kalem = Resim.imge(Görünüş.kalem)
tanım yarasa1 = Resim.imge(Görünüş.yarasa1)
tanım yarasa2 = Resim.imge(Görünüş.yarasa2)
tanım başlık = kalemRengi(mor) -> Resim {
    // yazı yazarken çiziklerin ucunu biraz kıvırmaya serif deniyor İngilizce'de.
    // Sans serif'de sanserif diye okunur, kıvrıksız demek (sans da fransızca bizim -siz/-sız çekim eki anlamında).
    yazıYüzünüKur(Yazıyüzü("Serif", 28)) // denemek istersen: Yazıyüzü("Sans Serif", 28)
    kaplumbağa.yazı("Çılgın yarasalar!")
}

dez resim = Resim.dizi(
    götür(-150, -150) -> kadın, 
    götür(-50, 91) * döndür(-10) * saydamlık(-0.3) -> araba,
    götür(94 , 44) * saydamlık(-0.2) -> kalem,
    götür(-50, 50) * büyüt(0.5) -> yarasa1,
    götür(150, 50) * büyüt(0.5) -> yarasa2,
    götür(-72, 50) -> başlık
)

silVeSakla()
çizMerkezde(resim)
