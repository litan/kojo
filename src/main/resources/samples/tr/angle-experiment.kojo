silVeSakla()
artalanıKur(beyaz)
dez ta = tuvalAlanı

dez kenazUzunluğu = 150

dez açıklama = """
Yukarıdaki resimde üç top var. Biri gizli! Yeşil topu kaplumbağacığın 
başı, büyük kırmızı topu da başını çevirdiği yön olarak düşünelim. 
Başlangıçta kaplumbağacığın başı yukarıya bakıyor. Buna 0 derece diyelim.

Şimdi kırmızı topu tıklayıp sürükleyerek hareket ettirebilirsin.
Açı nasıl değişiyor? 

Bu açı, kaplumbağacığı döndürmek için kullandığımız komutların girdisi.
Eksi değerler de kullanabilirsin. Örneğin şu iki komut aynı işi yapar:
  sağ(60)
  sol(-60)
"""
dez bilgi = Resim.yazı(açıklama)
//bilgi.kalemKalınlığınıKur(16)   // todo: Turkish throws an exception. English doesn't!
bilgi.kalemRenginiKur(koyuGri)
dez te = yazıÇerçevesi(açıklama, 16).boyu
bilgi.konumuKur(ta.x + 10, ta.y + te + 10)

dez yukarıKaymaBoyu = 30 // çizdiğimiz yay yazıların üstünden geçmesin
tanım açıResmiTanımı(x: Kesir, y: Kesir) = Resim {
    kalemRenginiKur(siyah)
    zıpla(yukarıKaymaBoyu)
    konumVeYönüBelleğeYaz()
    ileri(kenazUzunluğu)
    zıpla(-kenazUzunluğu)
    noktayaGit(x, y)
    dez açı = yuvarla(360 - doğrultu + 90).sayıya % 360
    konumVeYönüGeriYükle()
    yazı(f"$açı%4d°")
    dez açıYarıçapı = 60
    zıpla(açıYarıçapı)
    sağ() // kaplumbağacık sağa baksın
    sağ(açı, açıYarıçapı) // açısı verilen yayı çizelim
}

den açıResmi = açıResmiTanımı(0, kenazUzunluğu)
açıResmi.konumuKur(0, 0)

dez kaplumbağanınKonumu = götür(0, yukarıKaymaBoyu) * boyaRengi(yeşil) * kalemRengi(siyah) -> Resim.daire(5)
dez doğrununUcu = boyaRengi(kahverengi) * kalemRengi(siyah) -> Resim.daire(5)
doğrununUcu.konumuKur(0, kenazUzunluğu + yukarıKaymaBoyu)

den kırmızıTop = Resim.daire(10)
kırmızıTop.boyamaRenginiKur(kırmızı)
kırmızıTop.kalemRenginiKur(siyah)
kırmızıTop.konumuKur(0, kenazUzunluğu + yukarıKaymaBoyu)
kırmızıTop.fareyiSürükleyince { (x, y) =>
    açıResmi.sil()
    açıResmi = açıResmiTanımı(x, y)
    çiz(açıResmi)
    kırmızıTop.konumuKur(x, y)
    açıResmi.ardaAl()
}

dez yardımDüğmesi = götür(ta.x + 10, ta.y + 10) -> Resim.diziDüzenli(
    boyaRengi(renkler.lightBlue) * kalemRengi(koyuGri) -> Resim.dikdörtgen(200, 40),
    Resim.dikeyBoşluk(5),
    kalemRengi(siyah) -> Resim.yazı("Yardım için tıkla!", 20)
)
yardımDüğmesi.fareyeTıklayınca { (x, y) =>
    yardımDüğmesi.sil()
    çiz(bilgi)
}
çiz(yardımDüğmesi, kaplumbağanınKonumu, açıResmi, kırmızıTop, doğrununUcu)
