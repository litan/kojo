def ağaç(boy: Kesir) {
    // toInt metodu kesirli sayıyı tam sayıya çeviriyor
    // yani boy 1.75 olursa boy.toInt 1 oluyor
    def renk = Renk(boy.toInt % 255, math.abs(255 - boy * 3).toInt % 255, 125)
    if (boy > 4) {
        kalemKalınlığınıKur(boy / 7)
        kalemRenginiKur(renk)
        ileri(boy)
        sağ(25)
        ağaç(boy * 0.8 - 2)
        sol(45)
        ağaç(boy - 10)
        sağ(20)
        ileri(-boy)
    }
}
sil()
hızıKur(hızlı)
konumuKur(100, -200)
ağaç(90) // 100, 120 ve 150 gibi boyları da dene!
