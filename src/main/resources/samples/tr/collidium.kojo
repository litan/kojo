// Başlamak için fareyle topa tıklayıp geri çek, sapan atıyormuş gibi
// Fareyle raketler çizerek topa yol ver. Bir dakika içinde hedefi vur.

// Bu oyun fikri ve ses mp3'lerini şuradan aldık: https://github.com/shadaj/collidium

kojoVarsayılanİkinciBakışaçısınıKur()
silVeSakla()

çizSahne(koyuGri)
dez ta = tuvalAlanı
dez engelSayısı = 5
dez engellerArasıUzaklık = ta.eni / (engelSayısı + 1)
dez topunGöreceKonumuBaşta = (engellerArasıUzaklık / 4).sayıya
tanım topunGöreceKonumu = topunGöreceKonumuBaşta + rastgele(topunGöreceKonumuBaşta)
dez topunBoyu = 20

dez topunZarfı = kalemRengi(kırmızı) * götür(topunBoyu, topunBoyu) -> Resim.daire(topunBoyu)
dez top1 = Resim.imge(Çizim.top1, topunZarfı)
dez top2 = Resim.imge(Çizim.top2, topunZarfı)
dez top3 = Resim.imge(Çizim.top3, topunZarfı)
dez top4 = Resim.imge(Çizim.top4, topunZarfı)

dez top = büyüt(0.5) -> Resim.küme(top1, top2, top3, top4)
top.götür(ta.x + topunGöreceKonumu, ta.y + topunGöreceKonumu)

dez hedef = götür(-ta.x - topunGöreceKonumu, -ta.y - topunGöreceKonumu) *
    kalemRengi(kırmızı) * boyaRengi(kırmızı) -> Resim.daire(topunBoyu / 4)

dez duvarBoyası = DokumaBoya(Çizim.tuğla, 0, 0)
dez engeller = (1 |-| engelSayısı).işle { n =>
    götür(ta.x + n * engellerArasıUzaklık, ta.y + ta.boyu / 4) *
        boyaRengi(duvarBoyası) * kalemRengi(renksiz) ->
        Resim.dikdörtgen(12, ta.boyu / 2)
}

çiz(top, hedef)
çizVeSakla(topunZarfı)
engeller.herbiriİçin { o => çiz(o) }
sesMp3üÇal(Ses.vuruş)

tanım doğruÇiz(ps: EsnekDizim[Nokta], r: Renk) = Resim {
    dez boy = 4
    tanım karecik() {
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
dez sapanNoktaları = EsnekDizim.boş[Nokta]
den sapan = Resim.yatay(1)
den raket = Resim.yatay(1)
den geçiciRaket = raket
çizVeSakla(raket)

top.fareyeBasınca { (x, y) =>
    sapanNoktaları += Nokta(top.konum.x + topunBoyu/2, top.konum.y + topunBoyu/2)
}
top.fareyiSürükleyince { (x, y) =>
    eğer (sapanNoktaları.sayı > 1) {
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
    den hız = eğer (sapanNoktaları.sayı == 1)
        Yöney2B(1, 1)
    yoksa
        Yöney2B(
            sapanNoktaları(0).x - sapanNoktaları(1).x,
            sapanNoktaları(0).y - sapanNoktaları(1).y
        ).sınırla(7)

    canlandır {
        top.götür(hız)
        top.sonrakiniGöster()
        eğer (top.çarptıMı(Resim.tuvalinSınırları)) {
            sesMp3üÇal(Ses.vuruş)
            hız = sahneKenarındanYansıtma(top, hız)
        }
        yoksa eğer (top.çarptıMı(raket)) {
            sesMp3üÇal(Ses.vuruş)
            hız = engeldenYansıtma(top, hız, raket)
            top.götür(hız)
        }
        yoksa eğer (top.çarptıMı(hedef)) {
            hedef.kalemRenginiKur(yeşil)
            hedef.boyamaRenginiKur(yeşil)
            çizMerkezdeYazı("Yaşasın! Kazandın!", yeşil, 20)
            durdur()

          sesMp3üÇal(Ses.yaşasın)
        }
        top.çarpışma(engeller) eşle {
            durum Biri(engel) =>
                sesMp3üÇal(Ses.vuruş)
                hız = engeldenYansıtma(top, hız, engel)
                yineleDoğruKaldıkça (top.çarptıMı(engel)) {
                    top.götür(hız)
                }
            durum Hiçbiri =>
        }
    }
    oyunSüresiniGeriyeSayarakGöster(60, "Süre doldu. Tekrar dene", renkler.lightBlue, 20) // açık mavi
}

dez raketNoktaları = EsnekDizim.boş[Nokta]
Resim.tuvalBölgesi.fareyeBasınca { (x, y) =>
    raket.sil()
    raketNoktaları.sil()
    raketNoktaları += Nokta(x, y)
}
Resim.tuvalBölgesi.fareyiSürükleyince { (x, y) =>
    eğer (raketNoktaları.sayı > 1) {
        raketNoktaları.çıkar(1)
    }
    raketNoktaları += Nokta(x, y)
    geçiciRaket.sil()
    geçiciRaket = doğruÇiz(raketNoktaları, renkler.aquamarine)
    geçiciRaket.çiz()
}
Resim.tuvalBölgesi.fareyiBırakınca { (x, y) =>
    eğer (geçiciRaket.çarptıMı(top)) {
        geçiciRaket.sil()
    }
    yoksa {
        raket = geçiciRaket
        raket.kalemRenginiKur(sarı)
        raket.boyamaRenginiKur(sarı)
        raket.girdiyiAktar(Resim.tuvalBölgesi)
    }
}
hedef.girdiyiAktar(Resim.tuvalBölgesi)
engeller.herbiriİçin { o => o.girdiyiAktar(Resim.tuvalBölgesi) }
