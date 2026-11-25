// Benzetim yolculuğu, adım 5/12: sürtünme ve enerji kaybı.
// Üç yeni katsayı: havada yavaşlama, yerde yavaşlama, ve her zıplamada yitip
// giden enerji. Dikey hız yeterince küçülünce top yuvarlanmaya başlıyor.
silVeSakla
gridiGöster
eksenleriGöster
dez (yarıçap, yerçekimi, zıplamadaEnerjiKaybı) = (9, 0.2, 0.79)
dez (havaSürtünmesi, yerSürtünmesi) = (0.999, 0.99)
dez top = Resim.daire(yarıçap)
dez (ta, yç) = (tuvalAlanı, yarıçap)
dez (başla, yer, sol, sağ) = (ta.Y - yç, ta.y + yç, ta.x + yç, ta.X - yç)
çiz(götür(sol, başla) * boyaRengi(sarı) -> top)
den (dx, dy) = (3.0, 0.0)
canlandır {
    top.kondur(top.konum.x + dx, top.konum.y + dy)
    dx *= havaSürtünmesi
    dy = eğer(top.konum.y > yer) {
        dy - yerçekimi
    } yoksa {
        top.kondur(top.konum.x, yer)
        eğer(dy.mutlakDeğer < 0.9) {
            dx *= yerSürtünmesi
            0
        } yoksa {
            -dy * zıplamadaEnerjiKaybı
        }
    }
    eğer(top.konum.x < sol || top.konum.x > sağ) {
        dx *= - 1
    }
}
