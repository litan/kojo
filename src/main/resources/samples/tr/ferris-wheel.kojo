tanım araba(k: Kaplumbağa, r: Renk, a: Kesir) = artalandaOynat {
    k.kalemRenginiKur(r)
    k.boyamaRenginiKur(r)
    k.dön(a)
    k.ileri(150)
    yinele(3){
        k.sağ()
        k.ileri(50)
    }
    k.sol()
    k.ileri(100)
}

sil()
// birden çok kaplumbağa varsa, varsayılan kaplumbağayı kullanmamakta fayda var.
// Onun için saklayalım onu: 
gizle()

dez k0 = yeniKaplumbağa(0,0)  
dez k1 = yeniKaplumbağa(0,0)
dez k2 = yeniKaplumbağa(0,0)
dez k3 = yeniKaplumbağa(0,0)
dez k4 = yeniKaplumbağa(0,0)
dez k5 = yeniKaplumbağa(0,0)
dez k6 = yeniKaplumbağa(0,0)
dez k7 = yeniKaplumbağa(0,0)
dez k8 = yeniKaplumbağa(0,0)
dez k9 = yeniKaplumbağa(0,0)
dez k10 = yeniKaplumbağa(0,0)
dez k11 = yeniKaplumbağa(0,0)

araba(k0, kırmızı,0)
araba(k1, sarı,30)
araba(k2, mavi,60)
araba(k3, yeşil,90)
araba(k4, turuncu,120)
araba(k5, mor,150)
araba(k6, kırmızı,180)
araba(k7, sarı,210)
araba(k8, mavi,240)
araba(k9, yeşil,270)
araba(k10, turuncu,300)
araba(k11, mor,330)
