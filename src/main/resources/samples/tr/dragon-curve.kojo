// Łukasz Lew adında bir yazılımcının katkısıyla

tanım ejder(derinlik: Sayı, açı: Kesir): Birim = {
    eğer (derinlik == 0) {
        ileri(10)
    } yoksa {
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
