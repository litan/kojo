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

import scala.math._ // karekök ve trigonometri işlemleri yapmak için

val kenarUzunluğu: Kesir = 40 // taşımızın kenar uzunluğu

val dönüşSayısı = 8 // kaç dönüş olsun? Merkezdeki taşı saymıyoruz.
val yavaşlatma = 2 // 0 bir anda çizer. 1000 epey yavaşlatır ve her bir taşın nasıl çizildiğini görebilirsin

sil()
yaklaşXY(0.4, 0.4, 0, 0) // 8 dönüşün sığması için uzaklaşalım biraz
// todo  yaklaş(0.3)

// Başlangıç renkleri ve merkezdeki taş türü. Bunları her dönüşte değiştiriyoruz
var renk1 = Renk(235, 0, 20)
var renk2 = Renk(20, 0, 235)
var renk3 = Renk(0, 235, 20)
var taşTürü = 3 // üçüncü taşı merkeze koyalım. İstersen ikinciyi veya birinci de koyabilirsin

// Kendi taş tasarımını bu üçüncü taş tanımına ekleyebilir,
// ya da dördüncü bir taş ekleyebilirsin.
// Unutma: sol alt köşede başlayıp bitirmek gerekiyor. Başladığında kuzeye bakıyor
// olacak kaplumbağa. Bitirince de kuzeye baksın. Kenar uzunluğu da değişmesin.
// Bu üçüncü taş en sade desen :-) Bunula başlamak senin için biraz daha kolay olur umarım..
def taş3 {
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

def taş1 {
    kalemRenginiKur(renk1)
    kalemiİndir()
    sol(30)
    yinele(6) {
        yinele(3) {
            ileri(kenarUzunluğu)
            sağ(120)
        }
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

    kalemRenginiKur(renk2)
    kalemiİndir
    sol(30)
    yinele(6) {
        yinele(5) {
            ileri(kenarUzunluğu)
            sağ(72)
        }
        ileri(kenarUzunluğu)
        sağ(60)
    }
    kalemiKaldır
    sağ(30)
}

def taş2 {
    sağ()
    ileri(kenarUzunluğu / 2)
    kalemRenginiKur(renk2)
    kalemiİndir
    daire(kenarUzunluğu * sqrt(3.0) / 2)
    kalemiKaldır
    geri(kenarUzunluğu / 2)
    sol()

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

def birTaşÇiz(taşTürü: Int) = taşTürü match {
    case 1 => taş1
    case 2 => taş2
    case 3 => taş3
}

def birKolÇiz(taşSayısı: Int, doğrultu: Double, taşTürü: Integer) {
    // burdaDur(s"birKolÇiz taşSayısı: $taşSayısı")
    yinele(taşSayısı) {
        sol(doğrultu)
        birTaşÇiz(taşTürü)
        sağ(doğrultu)
        sağ(doğrultu % 120 - 30)
        ileri(kenarUzunluğu)
        sol(2 * (doğrultu % 120 - 30))
        ileri(kenarUzunluğu)
        sağ(doğrultu % 120 - 30)

    }
    sağ(60)
}
def birDönüş(kısaKenar: Int) {
    //burdaDur(s"birDönüş kısaKenar: $kısaKenar")
    taşTürü = taşTürü match {
        case 1 => 2
        case 2 => 3
        case 3 => 1
    }
    birKolÇiz(kısaKenar, 0, taşTürü)
    birKolÇiz(kısaKenar + 1, 60, taşTürü)
    birKolÇiz(kısaKenar + 1, 120, taşTürü)
    birKolÇiz(kısaKenar + 1, 180, taşTürü)
    birKolÇiz(kısaKenar + 1, 240, taşTürü)
    birKolÇiz(kısaKenar + 2, 300, taşTürü)
}
canlandırmaHızınıKur(yavaşlatma)
kalemiKaldır() // sadece taşları çizerken kalemi indireceğiz
birTaşÇiz(taşTürü)
sol(30)
ileri(kenarUzunluğu)
sol(60)
ileri(kenarUzunluğu)
sağ()
for (i <- 0 to dönüşSayısı - 1) {
    if (dönüşSayısı != 0) { // Sıfıra bölmeyelim
        renk1 = Renk(235 - 128 / dönüşSayısı * (i + 1), 128 / dönüşSayısı * (i + 1), 20)
        renk2 = Renk(20, 128 / dönüşSayısı * (i + 1), 255 - 128 / dönüşSayısı * (i + 1))
        renk3 = Renk(128 / dönüşSayısı * (i + 1), 20, 255 - 128 / dönüşSayısı * (i + 1))
    }
    birDönüş(i)
}
