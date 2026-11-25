// Benzetim yolculuğu, adım 3/12: bilardo masası.
// Kenarlar artık koda gömülü sayı değil; masayı çizdik ve sekme sınırlarını
// masanın kenarlarından hesaplıyoruz. Topun yarıçapını da hesaba katıyoruz ki
// top kenarı aşmasın.
silVeSakla
gridiGöster
eksenleriGöster
dez yç = 10
dez top = Resim.daire(yç)
top.çiz
dez (solKenar, sağKenar) = (-200, 200); dez en = sağKenar - solKenar
dez (enAltKnr, enÜstKnr) = (-100, 100); dez boy = enÜstKnr - enAltKnr
çiz(kalemRengi(siyah) * götür(solKenar, enAltKnr) ->
    Resim.dikdörtgen(en, boy))
top.kondur(solKenar + 20, enAltKnr + 30)
den (dx, dy) = (2, 4)
canlandır {
    top.kondur(top.konum.x + dx, top.konum.y + dy)
    eğer(top.konum.x >= sağKenar - yç ||
         top.konum.x <= solKenar + yç) {
        dx = -dx
    }
    eğer(top.konum.y >= enÜstKnr - yç ||
         top.konum.y <= enAltKnr + yç) {
        dy = -dy
    }
}
