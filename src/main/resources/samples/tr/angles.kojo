// Açı nedir ve nasıl ölçülür biliyorsun, değil mi? 
// Tam bir dönüşe 360 derece ya da 2 çarpı pi radyan deriz.
// Dereceleri anladık. Dik açı yani 90 derece çarpı 4 eşittir tam bir dönüş.
// Ya radyan ne demek? Bu yazılımcıkla devinimli çizim yaparak radyan kavramını
// gözümüzle görerek anlayalım.
// Radyan İngilizce'de radian diye yazılır. Radius yani yarıçaptan gelir.
// Bu yazılımcık şu kaynaklarda esinlenmiştir:
// (1) C. K. Raju'nın Sınırsız Kalkülüs (Calculus without limits) adlı ders notları
// (2) http://1ucasvb.tumblr.com/

dez araSüresi = 1.3 // saniye. Her yeni çizimden sonra bu kadar ara verelim
dez yçBoyu = 200.0 // yarıçapın uzunluğu bu olsun
dez çeyrekYÇ = yçBoyu / 4
dez renk = mavi
dez açıRengi = Renk(0, 204, 51)
dez eskiAçıRengi = açıkGri
dez yayRengi = gri

// Yarıçapı çizerek başlayalım
tanım yarıçapıÇiz(açı: Sayı) = kalemRengi(renk) * döndür(açı) -> Resim.dizi(
    boyaRengi(renk) -> Resim.daire(3),
    Resim.yatay(yçBoyu),
    götür(yçBoyu, 0) * boyaRengi(renk) -> Resim.daire(3)
)

tanım eğriYçÇiz(başı: Kesir, açı: Kesir) = kalemRengi(renk) * döndür(başı) -> Resim.dizi(
    götür(yçBoyu, 0) * boyaRengi(renk) -> Resim.daire(3),
    Resim.yay(yçBoyu, açı),
    döndür(açı) * götür(yçBoyu, 0) * boyaRengi(renk) -> Resim.daire(3)
)

tanım yayÇiz(açı: Sayı) = kalemRengi(yayRengi) -> Resim.yay(yçBoyu, açı)

tanım açıÇiz(başı: Kesir, açı: Kesir) = döndür(başı) -> Resim.dizi(
    Resim.yatay(yçBoyu),
    döndür(açı) -> Resim.yatay(yçBoyu),
    Resim.yay(yçBoyu / 4, açı)
)

// tuvale 7 tane resim çizeceğiz
den birYay = yayÇiz(0)
den birYarıçap = yarıçapıÇiz(0)
den yçYazısı = götür(yçBoyu / 2, -5) -> Resim.yazı("yarıçap", 20)
den birAçı: Resim = Resim.yay(0, 0)
den açıYazısı: Resim = Resim.yazı("", 20)
den işaret: Resim = Resim.yatay(0)
den işaret2: Resim = Resim.yatay(0)

tanım doğruÇiz(x1: Kesir, y1: Kesir, x2: Kesir, y2: Kesir) = {
    dez uzunluk = karekökü(karesi(x2 - x1) + karesi(y2 - y1))
    dez açı = tanjantınAçısı((y2 - y1) / (x2 - x1)) // verilen tanjanta denk gelen açıyı bulalım
    götür(x1, y1) * döndür(dereceye(açı)) -> Resim.yatay(uzunluk)
}

// tam sayıyı önce kesirli sayıya sonra da radyandan dereceye çevirelim:
tanım sayıdanDereceye(s: Sayı) = s.kesire.dereceye
tanım dereceye(k: Kesir) = k.dereceye

tanım radyanAçıÇiz(kaçRadyan: Sayı) {
    durakla(araSüresi) // çizime ufak bir ara verelim ki çok çabuk geçmesin
    eğer (kaçRadyan != 1) {
        birYarıçap = eğriYçÇiz(sayıdanDereceye(kaçRadyan - 1), sayıdanDereceye(1))
        çiz(birYarıçap)
    }
    birAçı.kalemRenginiKur(eskiAçıRengi)
    birAçı = kalemRengi(açıRengi) -> açıÇiz(0, sayıdanDereceye(kaçRadyan))
    çiz(birAçı)
    açıYazısı.sil()
    işaret.sil()
    işaret2.sil()
    açıYazısı = götür(-18, -yçBoyu / 4) -> Resim.yazı(s"$kaçRadyan radyan", 20)
    çiz(açıYazısı)
    eğer (kaçRadyan < 4) { // sinüs ve kosinüs de çok faydalı tanjant gibi
        işaret = doğruÇiz(0, -çeyrekYÇ, çeyrekYÇ - (çeyrekYÇ - çeyrekYÇ * kosinüs(30.radyana)), çeyrekYÇ * sinüs(30.radyana))
        çiz(işaret)
        işaret2 = doğruÇiz(0, -çeyrekYÇ, yçBoyu - (yçBoyu - yçBoyu * kosinüs(30.radyana)), yçBoyu * sinüs(30.radyana))
        çiz(işaret2)
    }
}

tanım piAçısınınKatınıÇiz(katı: Sayı) {
    durakla(araSüresi)
    dez pi = piSayısı  // biliyorsun, pi yaklaşık 3.14 yani üçten biraz çok
    birYarıçap = eğer (katı == 1)
        eğriYçÇiz(sayıdanDereceye(3), dereceye(pi - 3))
    else
        eğriYçÇiz(sayıdanDereceye(6), dereceye(2*pi - 6))
    çiz(birYarıçap)
    birAçı.kalemRenginiKur(eskiAçıRengi)
    birAçı = kalemRengi(açıRengi) -> açıÇiz(0, dereceye(pi * katı))
    çiz(birAçı)
    açıYazısı.sil()
    açıYazısı = götür(-18, -yçBoyu / 4) -> Resim.yazı(s"${katı}π radyan", 20)
    çiz(açıYazısı)
}

den resimSayısı = 1
tanım resimÇek() {
    durakla(1.0)
    // çizimiKaydet(s"açılar-$resimSayısı")
    resimSayısı += 1
}

silVeSakla()
çiz(birYarıçap)
durakla(araSüresi)
çiz(yçYazısı)
resimÇek()
yineleKere(1 to 360) { i =>
    birYarıçap.döndür(1)
    birYay.sil()
    birYay = yayÇiz(i)
    çiz(birYay)
    durakla(0.003)
}
durakla(araSüresi)
resimÇek()
yçYazısı.kondur(yçBoyu + 5, yçBoyu / 2 + 10)
birYarıçap.döndürMerkezli(-90, yçBoyu, 0)

durakla(araSüresi)
resimÇek()
birYarıçap.sil()

birYarıçap = eğriYçÇiz(0, dereceye(1))
çiz(birYarıçap)
resimÇek()

yineleKere(1 to 3) { kaçRadyan =>
    radyanAçıÇiz(kaçRadyan)
    resimÇek()
}
piAçısınınKatınıÇiz(1)
resimÇek()
yineleKere(4 to 6) { kaçRadyan =>
    radyanAçıÇiz(kaçRadyan)
    resimÇek()
}
piAçısınınKatınıÇiz(2)
resimÇek()
