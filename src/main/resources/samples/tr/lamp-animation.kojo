// Anay Kamat'ın katkısıyla

silVeSakla()

def lamba =
    kalemBoyu(2) *
        kalemRengi(siyah) *
        boyaRengi(Renk.doğrusalDeğişim(0, 10, kırmızı, 0, -40, kahverengi)) ->
        Resim.yoldan { yol =>
            yol.kondur(0, 0)
            yol.yayÇiz(-100, 0, -60)
            yol.yayÇiz(100, 0, 120)
            yol.yayÇiz(0, 0, -60)
        }

def alev =
    kalemBoyu(3) *
        kalemRengi(sarı) *
        boyaRengi(Renk.doğrusalDeğişim(0, 0, kırmızı, 0, 130, sarı)) ->
        Resim.yoldan { yol =>
            yol.kondur(0, 0)
            yol.yayÇiz(0, 139, 90)
            yol.yayÇiz(0, 0, 90)
        }

case class Evrim(çerçeveSırası: Sayı) {
    def büyütmeOranı: Kesir = 1.0 - 0.1 * sinüs(radyana(çerçeveSırası))
    def sonrakiEvre = Evrim(çerçeveSırası + 5)
}

def yananLamba(evrim: Evrim): Resim =
    Resim.dizi(
        büyüt(evrim.büyütmeOranı) -> alev,
        lamba
    )

def evir(evre: Evrim) = evre.sonrakiEvre

artalanıKur(renkler.darkSlateBlue) // eflatuna yakın koyu mavi
canlandırYenidenÇizerek(Evrim(0), evir, yananLamba)
