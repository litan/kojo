// Łukasz Lew adında bir yazılımcının katkısıyla

def ejder(derinlik: Sayı, açı: Kesir): Birim = {
    if (derinlik == 0) {
        ileri(10)
        return
    }

    sol(açı)
    ejder(derinlik - 1, açı.abs)  // .abs mutlak değeri verir. -1.5.abs = 1.5
    sağ(açı)

    sağ(açı)
    ejder(derinlik - 1, -açı.abs)
    sol(açı)
}

sil()
artalanıKur(beyaz)
canlandırmaHızınıKur(20)
kalemKalınlığınıKur(7)
kalemRenginiKur(Renk(0x365348))

zıpla(-100)
ejder(10, 45)
