//#yükle tr/anaTanimlar
//#yükle tr/eTahta

class Bellek(tahta: ETahta) {
    val odaSayısı = tahta.odaSayısı
    // ileri geri gidiş için gerekli bellek
    private val eskiTahtalar = EsnekDizim.boş[Dizim[Array[Taş]]] // todo
    private val oyuncular = EsnekDizim.boş[Taş]
    private val hamleler = EsnekDizim.boş[Belki[Oda]]
    def saklaTahtayı(yeniHamleMi: İkil, hane: Belki[Oda]) = {
        if (yeniHamleMi) while (tahta.hamleSayısı() <= eskiTahtalar.sayı) {
            eskiTahtalar.çıkar(eskiTahtalar.sayı - 1)
            oyuncular.çıkar(oyuncular.sayı - 1)
            hamleler.çıkar(hamleler.sayı - 1)
        }
        val yeniTahta = Dizim.boş[Taş](odaSayısı, odaSayısı)
        for (x <- tahta.satırAralığı; y <- tahta.satırAralığı)
            yeniTahta(y)(x) = tahta.taş(y, x)
        eskiTahtalar += yeniTahta
        oyuncular += tahta.oyuncu()
        hamleler += hane
    }
    def verilenHamleTahtasınaGeç(hamle: Sayı) = {
        val eski = eskiTahtalar(hamle)
        for (x <- tahta.satırAralığı; y <- tahta.satırAralığı)
            tahta.taşıKur(y)(x)(eski(y)(x))
        tahta.oyuncu.kur(oyuncular(hamle))
        tahta.sonHamle = hamleler(hamle)
        tahta.sıraGeriDöndüMü =
            if (hamle > 1) tahta.oyuncu() == oyuncular(hamle - 1)
            else yanlış
    }
    def yeniHamleYapıldı = {
        yeniHamleEnYeniGeriAlKomutundanDahaGüncel = doğru
    }
    // yeni bir hamleden önce geri/ileri çalışmaz ki
    private var yeniHamleEnYeniGeriAlKomutundanDahaGüncel = doğru
    def geriAl = if (tahta.hamleSayısı() > 1) {
        if (yeniHamleEnYeniGeriAlKomutundanDahaGüncel) {
            yeniHamleEnYeniGeriAlKomutundanDahaGüncel = yanlış
            saklaTahtayı(yanlış, tahta.sonHamle)
        }
        tahta.oyunBitti = yanlış
        tahta.hamleSayısı.azalt()
        verilenHamleTahtasınaGeç(tahta.hamleSayısı() - 1)
    }
    def ileriGit = if (tahta.hamleSayısı() < eskiTahtalar.sayı) {
        verilenHamleTahtasınaGeç(tahta.hamleSayısı())
        tahta.hamleSayısı.artır()
    }

    def başaAl() = {
        eskiTahtalar.sil()
        oyuncular.sil()
        hamleler.sil()
    }

}
