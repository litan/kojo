// Łukasz Lew adında bir yazılımcının katkısıyla

def ejder(derinlik: Sayı, açı: Kesir): Birim = {
    if (derinlik == 0) {
        ileri(10)
    } else {
        sol(açı)
        ejder(derinlik - 1, açı.mutlakDeğer)
        sağ(açı)

        sağ(açı)
        ejder(derinlik - 1, -açı.mutlakDeğer)
        sol(açı)
    }
}

sil()
artalanıKur(beyaz)
canlandırmaHızınıKur(20)
kalemKalınlığınıKur(7)
kalemRenginiKur(Renk(0x365348))

zıpla(-100)
ejder(10, 45)
