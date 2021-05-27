silVeSakla()
hızıKur(hızlı)
artalanıKur(beyaz)

def dal(boy: Kesir) {
    ileri(boy)
}

def ağaç(boy: Kesir) {
    konumVeYönüBelleğeYaz()
    // toInt metodu kesirli sayıyı tam sayıya çeviriyor
    // yani boy 1.75 olursa boy.toInt 1 oluyor
    // n % 255 işlemi ile de 255'ten küçük olmasını
    def renk = renkler.rgb(boy.toInt % 255, mutlakDeğer(255 - boy * 3).toInt % 255, 125)
    kalemRenginiKur(renk)
    if (boy <= 4) {
        kalemKalınlığınıKur(0.5)
        dal(boy / 2)
    }
    else {
        kalemKalınlığınıKur(boy / 7)
        dal(boy)
        sağ(25)
        ağaç(0.8 * boy- 2)
        sol(45)
        ağaç(boy - 10)
    }
    konumVeYönüGeriYükle()
}

konumuKur(100, -200)
ağaç(90) // 100, 120 ve 150 gibi boyları da dene!
