silipSakla()
artalanıKurYatay(Renk.ada(210, 1.00, 0.1), Renk.ada(210, 1.00, 0.15))

dez kareninKenarUzunluğu = 200
dez köşegen = karekökü(2 * kareninKenarUzunluğu * kareninKenarUzunluğu)
dez süre = 5 // saniye
dez canlıKırmızı = Renk.ada(18, 1.00, 0.50)

dez altın = Renkler.altınbaşak
dez bg = Renk.doğrusalDeğişim(0, 0, canlıKırmızı, kareninKenarUzunluğu / 2, kareninKenarUzunluğu / 2, altın)
dez bg2 = Renk.doğrusalDeğişim(0, 0, altın, kareninKenarUzunluğu / 2, kareninKenarUzunluğu / 2, canlıKırmızı)

tanım üçgen (ikizKenarBoyu: Kesir) = Resim.yoldan { yol =>
    yol.kondur(0, 0)
    yol.doğruÇiz(0, ikizKenarBoyu)
    yol.doğruÇiz(ikizKenarBoyu, 0)
    yol.başaDön()
}

tanım xKoord (d: Dizi[Kesir]) = d(0)
tanım yKoord (d: Dizi[Kesir]) = d(1)
tanım açısı (d: Dizi[Kesir]) = d(2)

tanım büyükÜçgen (d: Dizi[Kesir]) =
    götür(-kareninKenarUzunluğu / 2, -kareninKenarUzunluğu / 2) * // merkeze taşımak için
    götür(xKoord(d), yKoord(d)) *
    döndür(açısı(d)) * boyaRengi(bg) * kalemRengi(koyuGri) ->
        üçgen(kareninKenarUzunluğu)

tanım küçükÜçgen (d: Dizi[Kesir]) =
    götür(-kareninKenarUzunluğu / 2, -kareninKenarUzunluğu / 2) * // merkeze taşımak için
    götür(xKoord(d), yKoord(d)) *
    döndür(açısı(d)) * boyaRengi(bg2) * kalemRengi(koyuGri) ->
        üçgen(köşegen / 2)

dez canlı1 = Geçiş(
    süre,
    Dizi(-200, -200, 360),
    Dizi(0, 0, 0),
    YumuşakGeçiş.Doğrusal,
    büyükÜçgen,
    yanlış
)

dez canlı2 = Geçiş(
    süre,
    Dizi(-200, 200, -360),
    Dizi(kareninKenarUzunluğu / 2, kareninKenarUzunluğu / 2, 45),
    YumuşakGeçiş.DörtlüGirdiÇıktı,
    küçükÜçgen,
    yanlış
)

dez canlı3 = Geçiş(
    süre,
    Dizi(200, 200, -360),
    Dizi(kareninKenarUzunluğu / 2, kareninKenarUzunluğu / 2, -45),
    YumuşakGeçiş.DörtlüGirdiÇıktı,
    küçükÜçgen,
    yanlış
)

oynat(canlandırmaEşzamanlı(canlı1, canlı2, canlı3))
/* arka arkaya sırayla canlandırmak isteseydik, şunu kullanırdık:
   oynat(canlandırmaDizisi(canlı1, canlı2, canlı3))
 */
/* teker teker de oynatabilirdik. Yine eşzamanlı canlanırlardı:
   oynat(canlı2)
   oynat(canlı1)
   oynat(canlı3)
*/
