silVeSakla()
canlandırmaHızınıKur(10)

val kirliBeyaz = renkler.hex(0xF2F5F1)
artalanıKur(kirliBeyaz)
val mavimsi = renkler.darkBlue.fadeOut(0.4)
kalemRenginiKur(mavimsi)
val koyuYeşil = renkler.darkSeaGreen
boyamaRenginiKur(koyuYeşil)

val uzunluk = 400

// boyu verilen bir eşkenar üçgen çizelim
def üçgen(boy: Kesir) {
    yinele(3) {
        ileri(boy)
        sağ(120)
    }
}

def sierpinski(boy: Kesir) {
    konumVeYönüBelleğeYaz()
    if (boy < 10) {
        üçgen(boy)
    } else {
        kalemKalınlığınıKur(25 * boy / uzunluk)
        üçgen(boy)
        sierpinski(boy / 2)
        zıpla(boy / 2)
        sierpinski(boy / 2)
        sol(60)
        zıpla(- boy / 2)
        sağ(60)
        sierpinski(boy / 2)
    }
    konumVeYönüGeriYükle()
}

sağ(30)
konumuKur(-200, -150)
sierpinski(uzunluk) // kenar uzunluğunu burda girelim
