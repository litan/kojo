// Anay Kamat'ın katkısıyla

silVeSakla()

tanım lamba =
    kalemBoyu(2) *
        kalemRengi(siyah) *
        boyaRengi(Renk.doğrusalDeğişim(0, 10, kırmızı, 0, -40, kahverengi)) ->
        Resim.yoldan { yol =>
            yol.kondur(0, 0)
            yol.yayÇiz(-100, 0, -60)
            yol.yayÇiz(100, 0, 120)
            yol.yayÇiz(0, 0, -60)
        }

tanım alev =
    kalemBoyu(3) *
        kalemRengi(sarı) *
        boyaRengi(Renk.doğrusalDeğişim(0, 0, kırmızı, 0, 130, sarı)) ->
        Resim.yoldan { yol =>
            yol.kondur(0, 0)
            yol.yayÇiz(0, 139, 90)
            yol.yayÇiz(0, 0, 90)
        }

durum sınıf Evrim(çerçeveSırası: Sayı) {
    tanım büyütmeOranı: Kesir = 1.0 - 0.1 * sinüs(radyana(çerçeveSırası))
    tanım sonrakiEvre = Evrim(çerçeveSırası + 5)
}

tanım yananLamba(evrim: Evrim): Resim =
    Resim.dizi(
        büyüt(evrim.büyütmeOranı) -> alev,
        lamba
    )

tanım evir(evre: Evrim) = evre.sonrakiEvre

artalanıKur(renkler.darkSlateBlue) // eflatuna yakın koyu mavi
canlandırYenidenÇizerek(Evrim(0), evir, yananLamba)
