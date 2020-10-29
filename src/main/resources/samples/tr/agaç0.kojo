def ağaç(uzaklık: Çift) {
    if (uzaklık > 4) {
        kalemKalınlığınıKur(uzaklık/7)
        kalemRenginiKur(cm.rgb(uzaklık.toInt, math.abs(255-uzaklık*3).toInt, 125))
        ileri(uzaklık)
        sağ(25)
        ağaç(uzaklık*0.8-2)
        left(45)
        ağaç(uzaklık-10)
        sağ(20)
        ileri(-uzaklık)
    }
}

sil()
// hızıKur(hızlı)
setSpeed(fast)
zıpla(-200)
ağaç(90)
