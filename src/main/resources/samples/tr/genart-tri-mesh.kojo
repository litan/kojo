// Esin kaynağımız: https://generativeartistry.com/tutorials/triangular-mesh/

kojoVarsayılanİkinciBakışaçısınıKur()
silVeSakla
artalanıKur(Renk(60, 63, 65))
val ta = tuvalAlanı
val n = 10
val yatayAdımBoyu = ta.eni / n
val dikeyAdımBoyu = ta.boyu / n

case class Nokta(x: Kesir, y: Kesir)

def nokta(x: Kesir, y: Kesir) = Nokta(x, y)

var noktalar = Dizim.boş[Nokta](n + 2, n + 2)

yineleİçin(0 |-| n + 1) { sıra =>
    val y = ta.y + sıra * dikeyAdımBoyu
    yineleİçin(0 |-| n + 1) { sütun =>
        val x =
            if (sıra % 2 == 0)
                ta.x + sütun * yatayAdımBoyu
            else
                ta.x + sütun * yatayAdımBoyu + yatayAdımBoyu / 2
        noktalar(sütun)(sıra) = nokta(
            x + rastgeleKesir(1) * yatayAdımBoyu - yatayAdımBoyu / 2,
            y + rastgeleKesir(1) * dikeyAdımBoyu - dikeyAdımBoyu / 2)
    }
}

var üçgenler = Yöney.boş[Yöney[Nokta]]

yineleİçin(0 |-| n) { sıra =>
    var üçgen = Yöney.boş[Nokta]
    yineleİçin(0 |-| n) { sütun =>
        if (sıra % 2 == 0) {
            üçgen = üçgen :+ noktalar(sütun)(sıra)
            üçgen = üçgen :+ noktalar(sütun)(sıra + 1)
        }
        else {
            üçgen = üçgen :+ noktalar(sütun)(sıra + 1)
            üçgen = üçgen :+ noktalar(sütun)(sıra)
        }
    }
    üçgenler = üçgenler :+ üçgen
}

val griRenkler = 
    (1 |-| 16).işle { n =>
        val n2 = n * 15
        Renk(n2, n2, n2)
    }
def rastgeleGriRenk() = griRenkler(rastgele(16))
def üçgenResim(n1: Nokta, n2: Nokta, n3: Nokta) = kalemBoyu(0) * kalemRengi(beyaz) *
    boyaRengi(rastgeleGriRenk) -> Resim.yoldan { yol =>
        yol.kondur(n1.x, n1.y)  // yolun başlangıcı
        yol.doğruÇiz(n2.x, n2.y)  // ikinci noktaya doğru çizelim
        yol.doğruÇiz(n3.x, n3.y)  // üçüncü noktaya doğru çizelim
        yol.başaDön()  // son noktadan ilk noktaya doğru çizelim
    }

üçgenler.herbiriİçin { sıra =>
    yineleİçin(0 |-| sıra.boyu - 3) { n =>
        çiz(üçgenResim(sıra(n), sıra(n + 1), sıra(n + 2)))
    }
}
