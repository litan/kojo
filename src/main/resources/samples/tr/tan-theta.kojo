def eğriÇizgesi(baş: Kesir, son: Kesir, adım: Kesir)(işlev: Kesir => Kesir) {
    atla(baş, işlev(baş))
    var bu = baş
    while (bu <= son) {
        lineTo(bu, işlev(bu))
        bu += adım
    }
}
silVeSakla()
hızıKur(çokHızlı)
arkaplanıKurDik(sarı, Renk(255, 204, 0))
kalemKalınlığınıKur(0.01)
boyamaRenginiKur(cm.linearGradient(
    0, 0, Renk(0, 0, 0, 230),
    1, 1, Renk(0, 102, 255),
    doğru))
eğriÇizgesi(-12, 12, 0.1) { x => math.tan(x) }
zoomXY(40, 10, 0, 0)
