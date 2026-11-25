durum sınıf Robot(x0: Sayı, y: Sayı, duvarlar: Resim) {
    dez renk = renkler.darkMagenta
    dez en = 20
    dez boy = 30
    dez x = x0 + (50 - en) / 2
    dez uzaklıkAlgıcısı = götür(en / 2, boy) * boyaRengi(renk.lighten(0.5)) *
        kalemRengi(renk) -> Resim.daire(5)
    dez sesDalgası = götür(en / 2, boy) * boyaRengi(ColorMaker.lightBlue) *
        kalemRengi(ColorMaker.lightSlateGray) * kalemBoyu(1) -> Resim.daire(en / 2)
    dez gövde = boyaRengi(renk) * kalemRengi(renk) -> Resim.dikdörtgen(en, boy)
    dez hız = 200.0 // saniyede kaç nokta hızla ilerleyelim
    dez dönüşHızı = 180.0 // saniyede kaç açı dönelim
    uzaklıkAlgıcısı.eksenleriGöster()

    gövde.konumuKur(x, y)
    uzaklıkAlgıcısı.konumuKur(x, y)
    sesDalgası.konumuKur(x, y)

    tanım engeleUzaklık = {
        sesDalgası.göster()
        den u = 0
        yineleDoğruKaldıkça (!sesDalgası.çarptıMı(duvarlar)) {
            sesDalgası.götür(0, 3)
            u += 3
        }
        sesDalgası.götür(0, -u)
        sesDalgası.gizle()
        u
    }

    tanım göster() {
        çiz(gövde, uzaklıkAlgıcısı, sesDalgası)
        sesDalgası.gizle()
    }

    tanım ileri(ms: Kesir) {
        // ms milisaniye süresince ileri git
        dez u = hız * ms / 1000
        dez adımSayısı = 10
        dez adımUzunluğu = u / adımSayısı
        yinele(adımSayısı) {
            gövde.götür(0, adımUzunluğu)
            uzaklıkAlgıcısı.götür(0, adımUzunluğu)
            sesDalgası.götür(0, adımUzunluğu)
            durakla(ms / adımSayısı / 1000)
        }
    }

    tanım sol(ms: Kesir) {
        dez açı = dönüşHızı * ms / 1000
        dez adımSayısı = 30
        dez adımAçısı = açı / adımSayısı
        yinele(adımSayısı) {
            gövde.döndürMerkezli(adımAçısı, en / 2, boy / 2)
            uzaklıkAlgıcısı.döndürMerkezli(adımAçısı, 0, -boy / 2)
            sesDalgası.döndürMerkezli(adımAçısı, 0, -boy / 2)
            durakla(ms / adımSayısı / 1000)
        }
    }

    tanım sağ(ms: Kesir) {
        dez açı = dönüşHızı * ms / 1000
        dez adımSayısı = 30
        dez adımAçısı = açı / adımSayısı
        yinele(adımSayısı) {
            gövde.döndürMerkezli(-adımAçısı, en / 2, boy / 2)
            uzaklıkAlgıcısı.döndürMerkezli(-adımAçısı, 0, -boy / 2)
            sesDalgası.döndürMerkezli(-adımAçısı, 0, -boy / 2)
            durakla(ms / adımSayısı / 1000)
        }
    }

    tanım çarptıMı(other: Resim) = {
        gövde.çarptıMı(other) || uzaklıkAlgıcısı.çarptıMı(other)
    }
}
