//#yükle tr/anaTanimlar
//#yükle tr/eTahta

sınıf Bellek(tahta: ETahta) {
    dez odaSayısı = tahta.odaSayısı
    // ileri geri gidiş için gerekli bellek
    gizli dez eskiTahtalar = EsnekDizim.boş[Dizim[Array[Taş]]] // todo
    gizli dez oyuncular = EsnekDizim.boş[Taş]
    gizli dez hamleler = EsnekDizim.boş[Belki[Oda]]
    tanım saklaTahtayı(yeniHamleMi: İkil, hane: Belki[Oda]) = {
        eğer (yeniHamleMi) yineleDoğruKaldıkça (tahta.hamleSayısı() <= eskiTahtalar.sayı) {
            eskiTahtalar.çıkar(eskiTahtalar.sayı - 1)
            oyuncular.çıkar(oyuncular.sayı - 1)
            hamleler.çıkar(hamleler.sayı - 1)
        }
        dez yeniTahta = Dizim.boş[Taş](odaSayısı, odaSayısı)
        için (x <- tahta.satırAralığı; y <- tahta.satırAralığı)
            yeniTahta(y)(x) = tahta.taş(y, x)
        eskiTahtalar += yeniTahta
        oyuncular += tahta.oyuncu()
        hamleler += hane
    }
    tanım verilenHamleTahtasınaGeç(hamle: Sayı) = {
        dez eski = eskiTahtalar(hamle)
        için (x <- tahta.satırAralığı; y <- tahta.satırAralığı)
            tahta.taşıKur(y)(x)(eski(y)(x))
        tahta.oyuncu.kur(oyuncular(hamle))
        tahta.sonHamle = hamleler(hamle)
        tahta.sıraGeriDöndüMü =
            eğer (hamle > 1) tahta.oyuncu() == oyuncular(hamle - 1)
            yoksa yanlış
    }
    tanım yeniHamleYapıldı = {
        yeniHamleEnYeniGeriAlKomutundanDahaGüncel = doğru
    }
    // yeni bir hamleden önce geri/ileri çalışmaz ki
    gizli den yeniHamleEnYeniGeriAlKomutundanDahaGüncel = doğru
    tanım geriAl = eğer (tahta.hamleSayısı() > 1) {
        eğer (yeniHamleEnYeniGeriAlKomutundanDahaGüncel) {
            yeniHamleEnYeniGeriAlKomutundanDahaGüncel = yanlış
            saklaTahtayı(yanlış, tahta.sonHamle)
        }
        tahta.oyunBitti = yanlış
        tahta.hamleSayısı.azalt()
        verilenHamleTahtasınaGeç(tahta.hamleSayısı() - 1)
    }
    tanım ileriGit = eğer (tahta.hamleSayısı() < eskiTahtalar.sayı) {
        verilenHamleTahtasınaGeç(tahta.hamleSayısı())
        tahta.hamleSayısı.artır()
    }

    tanım başaAl() = {
        eskiTahtalar.sil()
        oyuncular.sil()
        hamleler.sil()
    }

}
