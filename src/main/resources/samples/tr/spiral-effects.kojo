def sarmal(boy: Sayı, açı: Sayı) {
    if (boy <= 300) {
        ileri(boy)
        sağ(açı)
        sarmal(boy + 2, açı)
    }
}

val sarmallıResim = penColor(siyah) * fillColor(Renk(255, 0, 204)) -> Picture {
    sarmal(0, 91)
}
sil()

val gb = canvasBounds // çizim gözünün boyutları. Örneğin: PBounds[x=-610.5,y=-229.5,width=1221.0,height=459.0]
def arkaplan = trans(gb.x, gb.y) * fillColor(Renk(153, 0, 255)) -> Picture.rectangle(gb.width, gb.height)

// arkaplanın üstüne sarmalı koyalım:
val resim = picStack(
    weave(30, 5, 30, 5) -> arkaplan,
    noise(80, 1) -> sarmallıResim
)
val hepsi = doğru  // bunu yanlışa çevirerek arkaplanın etkisini kaldırabilirsin
//val hepsi = yanlış
if (hepsi) {
    draw(resim)
} else {
    draw(sarmallıResim)
}


def sarmal(boy: Sayı, açı: Sayı) {
    if (boy <= 300) {
        ileri(boy)
        sağ(açı)
        sarmal(boy + 2, açı)
    }
}

val sarmallıResim = penColor(siyah) * fillColor(Renk(255, 0, 204)) -> Picture {
    sarmal(0, 91)
}
sil()

val gb = canvasBounds // çizim gözünün boyutları. Örneğin: PBounds[x=-610.5,y=-229.5,width=1221.0,height=459.0]
def arkaplan = trans(gb.x, gb.y) * fillColor(Renk(153, 0, 255)) -> Picture.rectangle(gb.width, gb.height)

// arkaplanın üstüne sarmalı koyalım:
val resim = picStack(
    weave(30, 5, 30, 5) -> arkaplan,
    noise(80, 1) -> sarmallıResim
)
draw(resim)
