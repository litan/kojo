// Anay Kamat'ın katkısı

silVeSakla()

def lamba =
    // todo: linearGradient == doğrusalDeğişim
    kalemBoyu(2) *
        kalemRengi(siyah) *
        boyaRengi(cm.linearGradient(0, 10, kırmızı, 0, -40, kahverengi)) ->
        Resim.yoldan { yol =>
            yol.moveTo(0, 0) //  noktaya git
            yol.arc(-100, 0, -60) //  yay çiz
            yol.arc(100, 0, 120)
            yol.arc(0, 0, -60)
        }

def alev =
    kalemBoyu(3) *
        kalemRengi(sarı) *
        boyaRengi(cm.linearGradient(0, 0, kırmızı, 0, 130, sarı)) ->
        Resim.yoldan { yol =>
            yol.moveTo(0, 0) //  noktaya git
            yol.arc(0, 139, 90) //  yay çiz
            yol.arc(0, 0, 90)
        }

case class Evrim(çerçeveDizini: Sayı) {
    def büyütmeOranı: Kesir = 1.0 - 0.1 * sinüs(radyana(çerçeveDizini))
    def sonrakiEvre = Evrim(çerçeveDizini + 5)
}

def diya(evrim: Evrim): Resim =
    Resim.dizi(
        büyüt(evrim.büyütmeOranı) -> alev,
        lamba
    )

def evir(evre: Evrim) = evre.sonrakiEvre

artalanıKur(renkler.darkSlateBlue) // eflatuna yakın koyu mavi
canlandırYenidenÇizerek(Evrim(0), evir, diya)
