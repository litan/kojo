silVeSakla()
artalanıKur(beyaz)
val ta = tuvalAlanı

val kenazUzunluğu = 150

val açıklama = """
Yukarıdaki resimde üç top var. Biri gizli! Yeşil topu kaplumbağacığın 
başı, büyük kırmızı topu da başını çevirdiği yön olarak düşünelim. 
Başlangıçta kaplumbağacığın başı yukarıya bakıyor. Yani açısı 0 derece.

Şimdi kırmızı topu tıklayıp sürükleyerek hareket ettirebilirsin.
Açı nasıl değişiyor? 

Bu açı, kaplumbağacığı döndürmek için kullandığımız komutların girdisi.
Eksi değerler de kullanabilirsin. Örneğin şu iki komut aynı işi yapar:
  sağ(60)
  sol(-60)
"""
val bilgi = Resim.yazı(açıklama)
//bilgi.kalemKalınlığınıKur(16)   // todo: Turkish throws an exception. English doesn't!
bilgi.kalemRenginiKur(koyuGri)
val te = yazıÇerçevesi(açıklama, 16).height  // todo: Rectangle.height: Dikdörtgen.boyu
bilgi.konumuKur(ta.x + 10, ta.y + te + 10)

def açıResmiTanımı(x: Kesir, y: Kesir) = Resim {
    kalemRenginiKur(siyah)
    konumVeYönüBelleğeYaz()
    ileri(kenazUzunluğu)
    zıpla(-kenazUzunluğu)
    noktayaGit(x, y)
    val açı = yuvarla(360 - doğrultu + 90).toInt % 360 // toInt: kesirden sayıya
    konumVeYönüGeriYükle()
    yazı(f"$açı%4d°")
    val açıYarıçapı = 50
    zıpla(açıYarıçapı)
    sağ(90)
    sağ(açı, açıYarıçapı)
}

var açıResmi = açıResmiTanımı(0, kenazUzunluğu)
açıResmi.konumuKur(0, 0)

val kaplumbağanınKonumu = boyaRengi(yeşil) * kalemRengi(siyah) -> Resim.daire(5)
val doğrununUcu = boyaRengi(kahverengi) * kalemRengi(siyah) -> Resim.daire(5)
doğrununUcu.konumuKur(0, kenazUzunluğu)

var kırmızıTop = Resim.daire(10)
kırmızıTop.boyamaRenginiKur(kırmızı)
kırmızıTop.kalemRenginiKur(siyah)
kırmızıTop.konumuKur(0, kenazUzunluğu)
kırmızıTop.fareyiSürükleyince { (x, y) =>
    açıResmi.sil()
    açıResmi = açıResmiTanımı(x, y)
    çiz(açıResmi)
    kırmızıTop.konumuKur(x, y)
    açıResmi.ardaAl()
}

çiz(bilgi, kaplumbağanınKonumu, açıResmi, kırmızıTop, doğrununUcu)
