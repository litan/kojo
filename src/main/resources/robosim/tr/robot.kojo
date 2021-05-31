case class Robot(x0: Sayı, y: Sayı, duvarlar: Resim) {
    val renk = renkler.darkMagenta
    val en = 20
    val boy = 30
    val x = x0 + (50 - en) / 2
    val uzaklıkAlgıcısı = götür(en / 2, boy) * boyaRengi(renk.lighten(0.5)) *
        kalemRengi(renk) -> Resim.daire(5)
    val sesDalgası = götür(en / 2, boy) * boyaRengi(ColorMaker.lightBlue) *
        kalemRengi(ColorMaker.lightSlateGray) * kalemBoyu(1) -> Resim.daire(en / 2)
    val gövde = boyaRengi(renk) * kalemRengi(renk) -> Resim.dikdörtgen(en, boy)
    val hız = 200.0 // saniyede kaç nokta hızla ilerleyelim
    val dönüşHızı = 180.0 // saniyede kaç açı dönelim
    uzaklıkAlgıcısı.eksenleriGöster()

    gövde.konumuKur(x, y)
    uzaklıkAlgıcısı.konumuKur(x, y)
    sesDalgası.konumuKur(x, y)

    def engeleUzaklık = {
        sesDalgası.göster()
        var u = 0
        while (!sesDalgası.çarptıMı(duvarlar)) {
            sesDalgası.götür(0, 3)
            u += 3
        }
        sesDalgası.götür(0, -u)
        sesDalgası.gizle()
        u
    }

    def göster() {
        çiz(gövde, uzaklıkAlgıcısı, sesDalgası)
        sesDalgası.gizle()
    }

    def ileri(ms: Kesir) {
        // ms milisaniye süresince ileri git
        val u = hız * ms / 1000
        val adımSayısı = 10
        val adımUzunluğu = u / adımSayısı
        yinele(adımSayısı) {
            gövde.götür(0, adımUzunluğu)
            uzaklıkAlgıcısı.götür(0, adımUzunluğu)
            sesDalgası.götür(0, adımUzunluğu)
            durakla(ms / adımSayısı / 1000)
        }
    }

    def sol(ms: Kesir) {
        val açı = dönüşHızı * ms / 1000
        val adımSayısı = 30
        val adımAçısı = açı / adımSayısı
        yinele(adımSayısı) {
            gövde.döndürMerkezli(adımAçısı, en / 2, boy / 2)
            uzaklıkAlgıcısı.döndürMerkezli(adımAçısı, 0, -boy / 2)
            sesDalgası.döndürMerkezli(adımAçısı, 0, -boy / 2)
            durakla(ms / adımSayısı / 1000)
        }
    }

    def sağ(ms: Kesir) {
        val açı = dönüşHızı * ms / 1000
        val adımSayısı = 30
        val adımAçısı = açı / adımSayısı
        yinele(adımSayısı) {
            gövde.döndürMerkezli(-adımAçısı, en / 2, boy / 2)
            uzaklıkAlgıcısı.döndürMerkezli(-adımAçısı, 0, -boy / 2)
            sesDalgası.döndürMerkezli(-adımAçısı, 0, -boy / 2)
            durakla(ms / adımSayısı / 1000)
        }
    }

    def çarptıMı(other: Resim) = {
        gövde.çarptıMı(other) || uzaklıkAlgıcısı.çarptıMı(other)
    }
}
