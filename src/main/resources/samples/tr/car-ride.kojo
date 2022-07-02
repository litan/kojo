// Arabayı sürmek için dört ok tuşunu kullan
// Mavi arabalara çarpma
// Her çarpışmayla dermanı azalır, her saniye dermanı artar
// Yoldan çıkarsan yani sağ ya da sol kenara çarparsan oyun biter
// Dermanın biterse oyun biter
// Kazanmak için bir dakika boyunca arabayı sür
kojoVarsayılanİkinciBakışaçısınıKur()
silVeSakla()
çizSahne(siyah)
// ekranTazelemeHızınıKur(50)
val oyunSüresi = 60

val ta = tuvalAlanı
val arabaBoyu = 100
val çizgiBoyu = 80

//  car1.png ve car2.png yani araba resimlerinin boyutlarına epey yakın bir çokkenarlı biçim çizelim
val arabayaZarf = götür(48, 14) -> Resim {  //todo: 2 -> 48
    yinele (2) {
        ileri(70); sol(45); ileri(20); sol(45)
        ileri(18); sol(45); ileri(20); sol(45)
    }
}
def araba(imge: Yazı) = Resim.imge(imge, arabayaZarf)

val arabalar = Eşlem.boş[Resim, Yöney2B]
val arabaHızı = 3
val sürücüTepkiHızı = 3
var sürücüHızı = Yöney2B(0, 0)
// çarpma anından sonra 0.3 saniye boyunca direksiyon, hız ve fren yani
// ileri geri sağ sol okları çalışmasın
var etkisizlikSüresi = 0L

val frenSesiÇalar = yeniMp3Çalar
val çarpışmaSesiÇalar = yeniMp3Çalar

def arabaYap() {
    val a = götür(oyuncu.konum.x + rastgeleDoğalKesir * ta.eni / 10, ta.y + ta.boyu) ->
        araba("/media/car-ride/car2.png")
    a.çiz
    arabalar += a -> Yöney2B(0, -arabaHızı)
}
var yolÇizgileri = Küme.boş[Resim]
def yolÇizgisiYap() {
    val eni = 20
    val yç = boyaRengi(beyaz) * kalemRengi(beyaz) *
        götür(ta.x + ta.eni / 2 - eni / 2, ta.y + ta.boyu) -> Resim.dikdörtgen(eni, çizgiBoyu)
    yç.çiz
    yolÇizgileri += yç
}

val oyuncu = araba("/media/car-ride/car1.png")
çiz(oyuncu)
çizVeSakla(arabayaZarf)

// 0.8 saniyede bir yeni araba ve çizgi gelsin yukarıdan
yineleSayaçla(800) {
    yolÇizgisiYap()
    arabaYap()
}

canlandır {
    oyuncu.öneAl()
    val aktifMi = buAn - etkisizlikSüresi > 300
    if (aktifMi) {
        if (tuşaBasılıMı(tuşlar.VK_LEFT)) {
            sürücüHızı = Yöney2B(-sürücüTepkiHızı, 0)
            oyuncu.götür(sürücüHızı)
        }
        if (tuşaBasılıMı(tuşlar.VK_RIGHT)) {
            sürücüHızı = Yöney2B(sürücüTepkiHızı, 0)
            oyuncu.götür(sürücüHızı)
        }
        if (tuşaBasılıMı(tuşlar.VK_UP)) {
            sürücüHızı = Yöney2B(0, sürücüTepkiHızı)
            oyuncu.götür(sürücüHızı)
            if (!Mp3ÇalıyorMu) {
                sesMp3üÇal("/media/car-ride/car-accel.mp3")
            }
        }
        else {
            Mp3üDurdur()
        }
        if (tuşaBasılıMı(tuşlar.VK_DOWN)) {
            sürücüHızı = Yöney2B(0, -sürücüTepkiHızı)
            oyuncu.götür(sürücüHızı)
            if (!frenSesiÇalar.çalıyorMu) {
                frenSesiÇalar.sesMp3üÇal("/media/car-ride/car-brake.mp3")
            }
        }
        else {
            frenSesiÇalar.durdur()
        }
    }
    else {
        oyuncu.götür(sürücüHızı)
    }

    if (oyuncu.çarptıMı(Resim.tuvalinSolu) || oyuncu.çarptıMı(Resim.tuvalinSağı)) {
        çarpışmaSesiÇalar.sesMp3üÇal("/media/car-ride/car-crash.mp3")
        oyuncu.saydamlığıKur(0.5)
        çizMerkezdeYazı("Yoldan çıktın. Yine deneyebilirsin.", kırmızı, 30)
        durdur()
    }
    else if (oyuncu.çarptıMı(Resim.tuvalinTavanı)) {
        sürücüHızı = Yöney2B(0, -sürücüTepkiHızı)
        oyuncu.götür(sürücüHızı * 2)
        etkisizlikSüresi = buAn
    }
    else if (oyuncu.çarptıMı(Resim.tuvalinTabanı)) {
        sürücüHızı = Yöney2B(0, sürücüTepkiHızı)
        oyuncu.götür(sürücüHızı * 2)
        etkisizlikSüresi = buAn
    }

    arabalar.herbiriİçin { arabaVeHız =>
        val (araba, hız) = arabaVeHız
        //araba.öneAl()   // todo gerekli mi? Bitiş yazısı bazen altta kalıyor...
        if (oyuncu.çarptıMı(araba)) {
            çarpışmaSesiÇalar.sesMp3üÇal("/media/car-ride/car-crash.mp3")
            sürücüHızı = engeldenYansıtma(oyuncu, sürücüHızı - hız, araba) / 2
            oyuncu.götür(sürücüHızı * 3)
            araba.götür(-sürücüHızı * 3)
            etkisizlikSüresi = buAn
            dermanıAzalt()
        }
        else {
            val yeniHız = Yöney2B(hız.x + rastgeleKesir(1) / 2 - 0.25, hız.y)
            arabalar += araba -> yeniHız
            araba.götür(yeniHız)
        }
        if (araba.konum.y + arabaBoyu < ta.y) {
            araba.sil()
            arabalar -= araba
        }
    }

    yolÇizgileri.herbiriİçin { yç => 
        yç.götür(0, -arabaHızı * 2)
        if (yç.konum.y + çizgiBoyu < ta.y) {
            yç.sil()
            yolÇizgileri -= yç
        }
    }
}

var derman = 0
def dermanYazısı = s"Derman: $derman"
val dermanÇizimi = Resim.yazıRenkli(dermanYazısı, 20, renkler.aquamarine)
dermanÇizimi.götür(ta.x + 10, ta.y + ta.boyu - 10)
def dermanıArtır() {
    derman += 2
    dermanÇizimi.güncelle(dermanYazısı)
}
def dermanıAzalt() {
    derman -= 10
    dermanÇizimi.güncelle(dermanYazısı)
    if (derman < 0) {
        çizMerkezdeYazı("Derman kalmadı. Yine deneyebilirsin.", kırmızı, 30)
        durdur()
    }
}

def skorVeDermanıYönet(oyunSüresi: Sayı) {
    var kalanSüre = oyunSüresi
    val kalanSüreGösterimi = Resim.yazıRenkli(kalanSüre, 20, renkler.azure)
    kalanSüreGösterimi.götür(ta.x + 10, ta.y + 50)
    çiz(kalanSüreGösterimi)
    çiz(dermanÇizimi)
    kalanSüreGösterimi.girdiyiAktar(Resim.tuvalBölgesi)

    yineleSayaçla(1000) {
        kalanSüre -= 1
        kalanSüreGösterimi.güncelle(kalanSüre)
        dermanıArtır()

        if (kalanSüre == 0) {
            çizMerkezdeYazı("Süre doldu. Tebrikler!", yeşil, 30)
            durdur()
        }
    }
}

skorVeDermanıYönet(oyunSüresi)
müzikMp3üÇalDöngülü("/media/car-ride/car-move.mp3")
tuvaliEtkinleştir()

// Araba resimleri  google aracılığıyla şunlardan:
//    http://motor-kid.com/race-cars-top-view.html  ve
//    www.carinfopic.com
// Araba sesleri şurdan: http://soundbible.com
