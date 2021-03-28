// noktaları köşe olarak kullanan üçgenler çizeceğiz
// İngilizce adı "delaunay triangulation" olan bir üçgen döşeme metod kullanıyoruz
// tıklayarak yeni noktalar ekle
silVeSakla()
artalanıKur(beyaz)
yaklaşmayaİzinVerme()
val noktalar = ArrayBuffer(Nokta(-100, -50), Nokta(100, -50), Nokta(-100, 50))
fareyeTıklıyınca { (x, y) =>
    noktalar.append(Nokta(x, y))
    if (noktalar.size > 3) {
        üçgenleriÇiz()
    }
}

def üçgenleriÇiz() {
    Resim.sil()
    val üçgenler = üçgenDöşeme(noktalar) 
    üçgenler.foreach { üçgen =>
        çiz(Resim {
            // her üçgenin üç noktası var: a, b, c
            val (a, b, c) = (üçgen.a, üçgen.b, üçgen.c)
            val doğrusalDeğişim = renkler.linearGradient(a.x, a.y, siyah, b.x, b.y, mavi)
            boyamaRenginiKur(doğrusalDeğişim)
            kalemRenginiKur(gri)
            konumuKur(a.x, a.y)
            noktayaGit(b.x, b.y)
            noktayaGit(c.x, c.y)
        })
    }
}

val mesaj1 = kalemRengi(siyah) -> Resim.yazı("Başlamak için tıkla", 40)
val mesaj2 = kalemRengi(siyah) -> Resim.yazı("Tıklayarak devam et", 30)
val mesaj = Resim.diziDikeyDüzenli(mesaj2, Resim.dikeyBoşluk(20), mesaj1)
çizMerkezde(mesaj)
