// noktaları köşe olarak kullanan üçgenler çizeceğiz
// İngilizce adı "delaunay triangulation" olan bir üçgen döşeme metod kullanıyoruz
// tıklayarak yeni noktalar ekle
silVeSakla()
artalanıKur(beyaz)
yaklaşmayaİzinVerme()
// üç noktayla başlayalım
val noktalar = EsnekDizim(Nokta(-100, -50), Nokta(100, -50), Nokta(-100, 50))
fareyeTıklıyınca { (x, y) =>
    noktalar.ekle(Nokta(x, y)) // istediğin noktaları ekleyelım
    if (noktalar.sayı > 3) {
        üçgenleriÇiz()
    }
}

def üçgenleriÇiz() {
    Resim.sil()
    val üçgenler = üçgenDöşeme(noktalar.diziye)
    üçgenler.herbiriİçin { üçgen =>
        çiz(Resim {
            // her üçgenin üç noktası var: a, b, c
            val (a, b, c) = (üçgen.a, üçgen.b, üçgen.c)
            val doğrusalEğim = Renk.doğrusalDeğişim(a.x, a.y, siyah, b.x, b.y, mavi)
            boyamaRenginiKur(doğrusalEğim)
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
