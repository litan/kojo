silVeSakla()
canlandırmaHızınıKur(10)

dez kirliBeyaz = renkler.hex(0xF2F5F1)
artalanıKur(kirliBeyaz)
dez mavimsi = renkler.darkBlue.fadeOut(0.4)
kalemRenginiKur(mavimsi)
dez koyuYeşil = renkler.darkSeaGreen
boyamaRenginiKur(koyuYeşil)

dez uzunluk = 400

// boyu verilen bir eşkenar üçgen çizelim
tanım üçgen(boy: Kesir) {
    yinele(3) {
        ileri(boy)
        sağ(120)
    }
}

tanım sierpinski(boy: Kesir) {
    konumVeYönüBelleğeYaz()
    eğer (boy < 10) {
        üçgen(boy)
    } yoksa {
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
