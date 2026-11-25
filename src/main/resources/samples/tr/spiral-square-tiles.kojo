// Mike Brown tarafından yazılmıştır.
// Umarım çıkan deseni beğenmişsindir. Sarmalın nasıl döndüğünü gördün mü?
// Türkçe'ye çevirirken ben de (Bülent Başaran) birazcık değiştirdim.
// Nasıl çalıştığını kolayca anlaman için "buradaDur" komudunu ekledim.
// Ama onu açıklamaya çevirdim (yani başına bu satırda olduğu gibi iki tane
// taksim işareti koydum) ki ilk seferde çizimi durdurmasın.
// Yazılımın nasıl çalıştığını anlamak istersen aşağıda buradaDur komudunu
// bul ve satırbaşındaki iki taksim imgesini çıkararak çalışır hale getiriver.
// Bu arada yazılımda aradığın sözcüğü bulmanın kolay yolu Kontrol-f yapmak.
// Yani kontrol tuşunu basıp bırakmadan f tuşuna basmak. Buradaki f, İngilizce
// "find" yani bul anlamına gelen sözcükten geliyor. Hatta ilk önce buraya yazdığım
// buradaDur sözcüğünü çift tıklayarak seçip ondan sonra Kontrol-f yaparsan
// bulmak daha da kolaylaşır. Deneyiver şimdi. Aşağıda yeni bir göz açıldı mı?
// Benim bilgisayarımda Komut-. yaptığımda "Bul ve Yer değiştir" gözü kapanıyor.
// Senin bilgisayarında komut tuşu var mı? Yoksa Alt ya da Windows tuşlarını dene.
sil()

dez yavaşlatma = 4 // 0 bir anda çizer. 100 epey yavaşlatır
dez dönüşSayısı = 8 // kaç kere dönsün sarmalımız? Merkezdeki taşı saymıyoruz

dez kenarUzunluğu = 50 // taşımızın kenar uzunluğu.

tanım taşıÇiz {
    kalemiİndir()
    // önce bir kare çizelim
    kalemRenginiKur(kırmızı)
    yinele(4) {
        ileri(kenarUzunluğu)
        sağ()
    }
    // her kenara da birer tane eşkenar üçgen çizelim
    kalemRenginiKur(blue)
    yinele(4) {
        sağ(60)
        ileri(kenarUzunluğu)
        sol(120)
        ileri(kenarUzunluğu)
        sağ(150)
    }
    kalemiKaldır // bir sonraki taşın ilk noktasına giderken çizim yapmak istemiyoruz
}

tanım koluÇiz(taşSayısı: Sayı, doğrultu: Kesir) {
    // Bu yazılımcık çalışırken bu komudun içine her girişinde
    // durarak sarmalın kollarının nasıl çizildiğini görebiliriz:
    // buradaDur(s"taşSayısı: $taşSayısı")
    yinele(taşSayısı) {
        sol(doğrultu)
        taşıÇiz
        sağ(doğrultu)
        ileri(kenarUzunluğu)
    }
    sağ(90)
}

tanım sarmalıÇiz(taşSayısı: Sayı) {
    koluÇiz(taşSayısı, 0) //       1  3  5 ...
    koluÇiz(taşSayısı + 1, 90) //  2  4  6 ...
    koluÇiz(taşSayısı + 1, 180) // 2  5  7 ...
    koluÇiz(taşSayısı + 2, 270) // 3  6  8 ...
}

canlandırmaHızınıKur(yavaşlatma)
// merkezdeki taşı çizelim önce
dez merkezdekiTaşıBelirginleştir = yanlış // bunu doğru yap istersen
eğer (merkezdekiTaşıBelirginleştir) {
    biçimleriBelleğeYaz()
    kalemKalınlığınıKur(4)
}
taşıÇiz
eğer (merkezdekiTaşıBelirginleştir)
    biçimleriGeriYükle()

// etrafına sarmalı çizmek için kaplumbağacığımızı konuşlandıralım
sol()
ileri(kenarUzunluğu)
sağ()

// Her sarmal kolun ilk kenarı en kısa kenar. Yani taş sayısı az.
// Her dönüşte ikişer ikişer artıyor.
// 1, 3, 5, ... 2 x dönüşSayısı değerlerini alacak
için (kısaKenardakiTaşSayısı <- 1 to 2 * dönüşSayısı by 2) {
    sarmalıÇiz(kısaKenardakiTaşSayısı)
}
