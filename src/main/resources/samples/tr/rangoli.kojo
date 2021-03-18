tümEkran
def kenar(k: Kaplumbağa, a: Kesir) = artalandaOynat {
    k.canlandırmaHızınıKur(200)
    k.kalemRenginiKur(siyah)
    k.sağ()
    k.ileri(1200)
    yinele(15){
        k.boyamaRenginiKur(kırmızı)
        k.dön(a)
        k.ileri(40)
        k.dön(a)
        k.ileri(40)
        k.dön(a)

        k.boyamaRenginiKur(mavi)
        k.dön(a)
        k.ileri(40)
        k.dön(a)
        k.ileri(40)
        k.dön(a)
    }
    k.gizle()
}

def çiçek(k: Kaplumbağa, r: Renk) = artalandaOynat {
    k.canlandırmaHızınıKur(400)
    k.kalemRenginiKur(siyah)
    k.boyamaRenginiKur(r)
    yinele(4){
        k.sağ()
        yinele(90){
            k.dön(-2)
            k.ileri(2)
        }
    }
    k.gizle()
}

silVeSakla()

val k1=yeniKaplumbağa(-600,-150)
val k2=yeniKaplumbağa(-600, 150)

kenar(k1,120)
kenar(k2,-120)

val ortadakiKaplumbağa = yeniKaplumbağa(0, 0)
artalandaOynat {
    import ortadakiKaplumbağa._
    atla(-50,100)
    canlandırmaHızınıKur(200)
    kalemRenginiKur(siyah)
    boyamaRenginiKur(yeşil)
    yinele(6){
        dön(-120)
        yinele(90){
            dön(-2)
            ileri(2)
        }
    }
    gizle()
}

val k3=yeniKaplumbağa(-300,100)
val k4=yeniKaplumbağa(-400,0)
val k5=yeniKaplumbağa(-500,100)
val k6=yeniKaplumbağa(-600,0)

val k7=yeniKaplumbağa(200,100)
val k8=yeniKaplumbağa(300,0)
val k9=yeniKaplumbağa(400,100)
val k10=yeniKaplumbağa(500,0)

çiçek(k3, turuncu)
çiçek(k4, sarı)
çiçek(k5, kırmızı)
çiçek(k6, mor)

çiçek(k7, turuncu)
çiçek(k8, sarı)
çiçek(k9, kırmızı)
çiçek(k10, mor)
