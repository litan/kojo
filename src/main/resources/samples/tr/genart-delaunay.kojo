// noktaları köşe olarak kullanan üçgenler çizeceğiz
// İngilizce adı "delaunay triangulation" olan bir üçgen döşeme metod kullanıyoruz
// tıklayarak yeni noktalar ekle
silVeSakla()
artalanıKur(beyaz)
yaklaşmayaİzinVerme()
// üç noktayla başlayalım
dez noktalar = EsnekDizim(Nokta(-100, -50), Nokta(100, -50), Nokta(-100, 50))
fareyeTıklıyınca { (x, y) =>
    noktalar.ekle(Nokta(x, y)) // istediğin noktaları ekleyelım
    eğer (noktalar.sayı > 3) {
        üçgenleriÇiz()
    }
}

tanım üçgenleriÇiz() {
    Resim.sil()
    dez üçgenler = üçgenDöşeme(noktalar.diziye)
    üçgenler.herbiriİçin { üçgen =>
        çiz(Resim {
            // her üçgenin üç noktası var: a, b, c
            dez (a, b, c) = (üçgen.a, üçgen.b, üçgen.c)
            dez doğrusalEğim = Renk.doğrusalDeğişim(a.x, a.y, siyah, b.x, b.y, mavi)
            boyamaRenginiKur(doğrusalEğim)
            kalemRenginiKur(gri)
            konumuKur(a.x, a.y)
            noktayaGit(b.x, b.y)
            noktayaGit(c.x, c.y)
        })
    }
}

dez mesaj1 = kalemRengi(siyah) -> Resim.yazı("Başlamak için tıkla", 40)
dez mesaj2 = kalemRengi(siyah) -> Resim.yazı("Tıklayarak devam et", 30)
dez mesaj = Resim.diziDikeyDüzenli(mesaj2, Resim.dikeyBoşluk(20), mesaj1)
çizMerkezde(mesaj)
