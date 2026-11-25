tümEkran
tanım kenar(k: Kaplumbağa, a: Kesir) = artalandaOynat {
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

tanım çiçek(k: Kaplumbağa, r: Renk) = artalandaOynat {
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

dez k1=yeniKaplumbağa(-600,-150)
dez k2=yeniKaplumbağa(-600, 150)

kenar(k1,120)
kenar(k2,-120)

dez ortadakiKaplumbağa = yeniKaplumbağa(0, 0)
artalandaOynat {
    getir ortadakiKaplumbağa._
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

dez k3=yeniKaplumbağa(-300,100)
dez k4=yeniKaplumbağa(-400,0)
dez k5=yeniKaplumbağa(-500,100)
dez k6=yeniKaplumbağa(-600,0)

dez k7=yeniKaplumbağa(200,100)
dez k8=yeniKaplumbağa(300,0)
dez k9=yeniKaplumbağa(400,100)
dez k10=yeniKaplumbağa(500,0)

çiçek(k3, turuncu)
çiçek(k4, sarı)
çiçek(k5, kırmızı)
çiçek(k6, mor)

çiçek(k7, turuncu)
çiçek(k8, sarı)
çiçek(k9, kırmızı)
çiçek(k10, mor)
