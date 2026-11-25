tanım eğriÇizgesi(başı: Kesir, sonu: Kesir, adım: Kesir)(işlev: Kesir => Kesir) {
    atla(başı, işlev(başı))
    den girdi = başı
    yineleDoğruKaldıkça (girdi <= sonu) {
        dez çıktı = işlev(girdi)
        noktayaGit(girdi, çıktı)
        // tek satırda da yazabilirdik ama girdi ve çıktı ve nasıl kullandığımız belli olsun istedik:
        //   noktayaGit(işlev(girdi))
        girdi += adım
    }
}
silVeSakla()
hızıKur(çokHızlı)
artalanıKurDik(sarı, Renk(255, 204, 0))
kalemKalınlığınıKur(0.01)
boyamaRenginiKur(Renk.doğrusalDeğişim(
    0, 0, Renk(0, 0, 0, 230),
    1, 1, Renk(0, 102, 255),
    doğru))
eğriÇizgesi(-12, 12, 0.1) { x => tanjant(x) }
yaklaşXY(40, 10, 0, 0)
