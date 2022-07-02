// Başlamak için fareyle topa tıklayıp geri çek, sapan atıyormuş gibi
// Fareyle raketler çizerek topa yol ver. Bir dakika içinde hedefi vur.

kojoVarsayılanİkinciBakışaçısınıKur()
silVeSakla()

çizSahne(koyuGri)
val ta = tuvalAlanı
val engelSayısı = 5
val engellerArasıUzaklık = ta.eni / (engelSayısı + 1)
val topunGöreceKonumuBaşta = (engellerArasıUzaklık / 4).sayıya
def topunGöreceKonumu = topunGöreceKonumuBaşta + rastgele(topunGöreceKonumuBaşta)
val topunBoyu = 20

val topunZarfı = kalemRengi(kırmızı) * götür(topunBoyu, topunBoyu) -> Resim.daire(topunBoyu)
val top1 = Resim.imge("/media/collidium/ball1.png", topunZarfı)
val top2 = Resim.imge("/media/collidium/ball2.png", topunZarfı)
val top3 = Resim.imge("/media/collidium/ball3.png", topunZarfı)
val top4 = Resim.imge("/media/collidium/ball4.png", topunZarfı)

val top = büyüt(0.5) -> Resim.küme(top1, top2, top3, top4)
top.götür(ta.x + topunGöreceKonumu, ta.y + topunGöreceKonumu)

val hedef = götür(-ta.x - topunGöreceKonumu, -ta.y - topunGöreceKonumu) *
    kalemRengi(kırmızı) * boyaRengi(kırmızı) -> Resim.daire(topunBoyu / 4)

val duvarBoyası = DokumaBoya("/media/collidium/bwall.png", 0, 0)
val engeller = (1 |-| engelSayısı).işle { n =>
    götür(ta.x + n * engellerArasıUzaklık, ta.y + ta.boyu / 4) *
        boyaRengi(duvarBoyası) * kalemRengi(renksiz) ->
        Resim.dikdörtgen(12, ta.boyu / 2)
}

çiz(top, hedef)
çizVeSakla(topunZarfı)
engeller.herbiriİçin { o => çiz(o) }
sesMp3üÇal("/media/collidium/hit.mp3")

def doğruÇiz(ps: EsnekDizim[Nokta], r: Renk) = Resim {
    val boy = 4
    def karecik() {
        zıpla(-boy / 2)
        sol(90)
        zıpla(-boy / 2)
        yinele(4) {
            ileri(boy)
            sağ(90)
        }
        zıpla(boy / 2)
    }
    kalemRenginiKur(r)
    boyamaRenginiKur(r)
    konumuKur(ps(0).x, ps(0).y)
    noktayaGit(ps(1).x, ps(1).y)
    karecik()
    sağ(90)
    konumuKur(ps(0).x, ps(0).y)
    karecik()
}
val sapanNoktaları = EsnekDizim.boş[Nokta]
var sapan = Resim.yatay(1)
var raket = Resim.yatay(1)
var geçiciRaket = raket
çizVeSakla(raket)

top.fareyeBasınca { (x, y) =>
    sapanNoktaları += Nokta(top.konum.x + topunBoyu/2, top.konum.y + topunBoyu/2)
}
top.fareyiSürükleyince { (x, y) =>
    if (sapanNoktaları.sayı > 1) {
        sapanNoktaları.çıkar(1)
    }
    sapanNoktaları += Nokta(x, y)
    sapan.sil()
    sapan = doğruÇiz(sapanNoktaları, yeşil)
    sapan.çiz()
}
top.fareyiBırakınca { (x, y) =>
    sapan.sil()
    top.girdiyiAktar(Resim.tuvalBölgesi)
    var hız = if (sapanNoktaları.sayı == 1)
        Yöney2B(1, 1)
    else
        Yöney2B(
            sapanNoktaları(0).x - sapanNoktaları(1).x,
            sapanNoktaları(0).y - sapanNoktaları(1).y
        ).sınırla(7)

    canlandır {
        top.götür(hız)
        top.sonrakiniGöster()
        if (top.çarptıMı(Resim.tuvalinSınırları)) {
            sesMp3üÇal("/media/collidium/hit.mp3")
            hız = sahneKenarındanYansıtma(top, hız)
        }
        else if (top.çarptıMı(raket)) {
            sesMp3üÇal("/media/collidium/hit.mp3")
            hız = engeldenYansıtma(top, hız, raket)
            top.götür(hız)
        }
        else if (top.çarptıMı(hedef)) {
            hedef.kalemRenginiKur(yeşil)
            hedef.boyamaRenginiKur(yeşil)
            çizMerkezdeYazı("Yaşasın! Kazandın!", yeşil, 20)
            durdur()
            sesMp3üÇal("/media/collidium/win.mp3")
        }
        top.çarpışma(engeller) match {
            case Biri(engel) =>
                sesMp3üÇal("/media/collidium/hit.mp3")
                hız = engeldenYansıtma(top, hız, engel)
                while (top.çarptıMı(engel)) {
                    top.götür(hız)
                }
            case Hiçbiri =>
        }
    }
    oyunSüresiniGeriyeSayarakGöster(60, "Süre doldu. Tekrar dene", renkler.lightBlue, 20) // açık mavi
}

val raketNoktaları = EsnekDizim.boş[Nokta]
Resim.tuvalBölgesi.fareyeBasınca { (x, y) =>
    raket.sil()
    raketNoktaları.sil()
    raketNoktaları += Nokta(x, y)
}
Resim.tuvalBölgesi.fareyiSürükleyince { (x, y) =>
    if (raketNoktaları.sayı > 1) {
        raketNoktaları.çıkar(1)
    }
    raketNoktaları += Nokta(x, y)
    geçiciRaket.sil()
    geçiciRaket = doğruÇiz(raketNoktaları, renkler.aquamarine)
    geçiciRaket.çiz()
}
Resim.tuvalBölgesi.fareyiBırakınca { (x, y) =>
    if (geçiciRaket.çarptıMı(top)) {
        geçiciRaket.sil()
    }
    else {
        raket = geçiciRaket
        raket.kalemRenginiKur(sarı)
        raket.boyamaRenginiKur(sarı)
        raket.girdiyiAktar(Resim.tuvalBölgesi)
    }
}
hedef.girdiyiAktar(Resim.tuvalBölgesi)
engeller.herbiriİçin { o => o.girdiyiAktar(Resim.tuvalBölgesi) }
// Bu oyun fikri ve ses mp3'lerini şuradan aldık: https://github.com/shadaj/collidium
