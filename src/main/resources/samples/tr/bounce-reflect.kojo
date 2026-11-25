// Benzetim yolculuğu, adım 2/12: yansıma.
// Durmak yerine sekiyoruz: kayma miktarını (dx, dy) değişkene aldık, duvara
// değince işaretini çeviriyoruz. Döngü artık hiç bitmiyor.
silVeSakla
gridiGöster
eksenleriGöster
dez yç = 10
dez top = Resim.daire(yç)
top.çiz
top.kondur(-150, -100)
den dx = 2
den dy = 1
canlandır {
    top.kondur(top.konum.x + dx, top.konum.y + dy)
    eğer(top.konum.x >= 200 || top.konum.x <= -200)
        dx = -dx
    eğer(top.konum.y >= 100 || top.konum.y <= -100)
        dy = -dy
}
