// Benzetim yolculuğu, adım 4/12: yerçekimi.
// Dikey hız artık sabit değil: her adımda yerçekimi kadar azalıyor. Yere
// değince işaret dönüyor, yani top hiç enerji kaybetmeden zıplıyor -- bir
// sonraki adımda buna sürtünme ve enerji kaybı ekleyeceğiz.
silVeSakla
gridiGöster
eksenleriGöster
dez (yerçekimi, yarıçap) = (0.2, 9.0)
dez top = Resim.daire(yarıçap)
dez (ta, ç) = (tuvalAlanı, 2 * yarıçap)
dez (başla, yer, sol, sağ) = (-ta.y - ç, ta.y + ç, ta.x + ç, -ta.x - ç)
çiz(götür(sol, başla) * boyaRengi(sarı) -> top)
den (dx, dy) = (1.2, 0.0)
canlandır {
    dez k = top.konum
    dy = eğer(k.y > yer) dy -yerçekimi yoksa -dy
    eğer(k.x < sol || k.x > sağ) dx *= - 1
    top.kondur(k.x + dx, k.y + dy)
}
