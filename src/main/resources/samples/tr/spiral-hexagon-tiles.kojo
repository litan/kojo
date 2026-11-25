// Mike Brown tarafından İngilizce olarak yazılmıştır.
// Bülent Başaran Türkçe'ye çevirip bir de beyaz/boş bir taş eklemiştir.

// Düzlemi altıgen taşlarla kaplayalım. Benzer eş kenarlılar arasında sadece
// altıgen, kare ve eşkenar üçgenle bu iş yapılabilir. (Üçgen taşların yarısı
// baş aşağı durursa ancak).

// Bu yazılımcıkta merkezde bir altıgen taşla başlıyor ve onun etrafında dönerek
// taşlar ekliyoruz. İki tür taş desenimiz var. Her dönüşte değişik bir tür kullanıyoruz.
// Ayrıca taşların renklerini de her dönüşte biraz değiştiriyoruz.

// Sen de değişik bir tür taş tasarlamak ister misin? Aynı büyüklükte bir altıgen olmalı
// ve kaplumbağa sol alt köşede çizmeye başlayıp yine aynı noktada çizimi bitirmeli.
// Bir de başlarken ve bittiğinde kuzeye bakıyor olmalı.
// Aşağıdaki notlarda biraz daha açıklama bulacaksın.

dez kenarUzunluğu: Kesir = 40 // taşımızın kenar uzunluğu

dez dönüşSayısı = 8 // kaç dönüş olsun? Merkezdeki taşı saymıyoruz.
dez yavaşlatma = 4 // 0 bir anda çizer. 1000 çok yavaşlatır ve her bir taşın nasıl çizildiğini görebilirsin

sil()
yaklaş(0.4) // 8 dönüşün sığması için uzaklaşalım biraz

// Başlangıç renkleri ve merkezdeki taş türü. Bunları her dönüşte değiştiriyoruz
den renk1 = Renk(235, 0, 20)
den renk2 = Renk(20, 0, 235)
den renk3 = Renk(0, 235, 20)
den taşTürü = 3 // üçüncü taşı merkeze koyalım. İstersen ikinciyi veya birinci de koyabilirsin

// Kendi taş tasarımını bu üçüncü taş tanımına ekleyebilir,
// ya da dördüncü bir taş ekleyebilirsin.
// Unutma: sol alt köşede başlayıp bitirmek gerekiyor. Başladığında kuzeye bakıyor
// olacak kaplumbağa. Bitirince de kuzeye baksın. Kenar uzunluğu da değişmesin.
// Bu üçüncü taş en sade desen :-) Bunula başlamak senin için biraz daha kolay olur umarım..
tanım taş3 {
    kalemRenginiKur(renk3)
    kalemiİndir()
    sol(30) // sol alt köşeden çizilen kenarın 90+30 dereceyle çizilmesi gerek
    yinele(6) {
        ileri(kenarUzunluğu)
        sağ(60)
    }
    sağ(30)
    kalemiKaldır
}

tanım taş1 {
    kalemRenginiKur(renk1)
    kalemiİndir()
    sol(30)
    yinele(6) {
        yinele(3) { // altıgenin her kenarına bir eşkenar üçgen çizelim
            ileri(kenarUzunluğu)
            sağ(120)
        }
        // üçgenin taban olmayan iki kenarının orta noktalarını bağlayalım
        // bu sayede taşın ortasında bir eşkenar üçgen daha oluşacak
        sağ(60)
        yinele(3) {
            ileri(kenarUzunluğu / 2)
            sol(60)
        }
        sağ(180)
    }
    kalemiKaldır()
    sağ(30)
    kalemiKaldır

    // bir beşgen çizelim!
    kalemRenginiKur(renk2)
    kalemiİndir
    sol(30)
    yinele(6) {
        yinele(5) { // altıgenin bu kenarı beşgenin tabanı olsun
            ileri(kenarUzunluğu)
            sağ(72)
        }
        ileri(kenarUzunluğu) // altıgenin bir sonraki kenarına geçelim
        sağ(60)
    }
    kalemiKaldır
    sağ(30) // kuzeye bakalım
}

tanım taş2 {
    // Altıgenin içine bir daire çizelim
    // Merkezi taşın merkezinde olacak
    sağ()
    ileri(kenarUzunluğu / 2)
    kalemRenginiKur(renk2)
    kalemiİndir
    daire(kenarUzunluğu * karekökü(3.0) / 2) // yarıçap!
    kalemiKaldır
    geri(kenarUzunluğu / 2)
    sol() // ilk konum ve doğrultuya döndük

    // taşın içine kesişen kareler çizelim
    kalemRenginiKur(renk1)
    kalemiİndir
    yinele(6) {
        ileri(kenarUzunluğu)
        sağ()
        ileri(kenarUzunluğu)
        sağ()
        ileri(kenarUzunluğu)
        sağ(120)
    }
    kalemiKaldır

    // bir de küçük bir çember
    sağ(30)
    ileri(kenarUzunluğu / 2)
    sağ(90)
    kalemRenginiKur(renk2)
    kalemiİndir
    daire(kenarUzunluğu / 2)
    kalemiKaldır
    sol(90)
    geri(kenarUzunluğu / 2)
    sol(30)
}

// her dönüşte yeni bir taş/desen seçimi yapıyoruz
tanım birTaş(taşTürü: Int) = taşTürü eşle {
    durum 1 => taş1
    durum 2 => taş2
    durum 3 => taş3
}

// verilen taş sayısı kadar taşı bir doğrultu üzerinde çizeceğiz
tanım birKenar(taşSayısı: Int, doğrultu: Double, taşTürü: Integer) {
    // burdaDur(s"birKenar taşSayısı: $taşSayısı")
    yinele(taşSayısı) {
        sol(doğrultu)
        birTaş(taşTürü)
        sağ(doğrultu)
        sağ(doğrultu % 120 - 30)
        ileri(kenarUzunluğu)
        sol(2 * (doğrultu % 120 - 30))
        ileri(kenarUzunluğu)
        sağ(doğrultu % 120 - 30)
    }
    sağ(60)
}

// altı kenar çizerek bir dönüşü tamamlayacağız
tanım birDönüş(kısaKenar: Int) {
    // burdaDur(s"birDönüş kısaKenar: $kısaKenar")
    taşTürü = taşTürü eşle { // her dönüşte bir sonraki taş desenini kullanalım
        durum 1 => 2
        durum 2 => 3
        durum 3 => 1
    }
    // ilk dönüşte ilk kenar taş çizmez! Bir kenardaki taş sayısı her dönüşte artıyor:
    birKenar(kısaKenar, 0, taşTürü)       // 0  1  2 ...
    birKenar(kısaKenar + 1, 60, taşTürü)  // 1  2  3 ...
    birKenar(kısaKenar + 1, 120, taşTürü) // 1  2  3 ...
    birKenar(kısaKenar + 1, 180, taşTürü) // 1  2  3 ...
    birKenar(kısaKenar + 1, 240, taşTürü) // 1  2  3 ...
    birKenar(kısaKenar + 2, 300, taşTürü) // 2  3  4 ...
    //                              Toplam:  6 12 18 ...
}

// Bütün desenler ve tanımlar hazır. Artık çizebiliriz
canlandırmaHızınıKur(yavaşlatma)
kalemiKaldır() // sadece taşları çizerken kalemi indireceğiz

birTaş(taşTürü) // merkez taş
// bir sonraki dönüşün sol alt köşesindeki başlangıç konumuna ve doğrultusuna gidelim
sol(30); ileri(kenarUzunluğu)
sol(60); ileri(kenarUzunluğu); sağ()
için (i <- 0 to dönüşSayısı - 1) {
    eğer (dönüşSayısı != 0) { // ilk taş için renkleri değiştirmeye gerek yok
        dez r1 = 128 / dönüşSayısı * (i + 1)
        dez r2 = 235 - r1
        renk1 = Renk(r2, r1, 20)
        renk2 = Renk(20, r1, r2)
        renk3 = Renk(r1, 20, r2)
    }
    birDönüş(i)
}
