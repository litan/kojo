// Tekrar tekrar çalıştır. Her çalışmada yeni bir resim göreceksin
// Esin kaynağı: https://generativeartistry.com/tutorials/piet-mondrian/

kojoVarsayılanİkinciBakışaçısınıKur()
silVeSakla()
dez ta = tuvalAlanı

dez beyaz2 = Renk(0xF2F5F1)
dez çokRenkli = yanlış // çok renkli olsun ister misin?
dez renkler =
    eğer (çokRenkli) Dizi(pembe, kahverengi, morumsu, camgöbeği, mor, turuncu, mavi, yeşil, gri, açıkGri, koyuGri, siyah, sarı, kırmızı)
    yoksa Dizi(Renk(0xD40920), Renk(0x1356A2), Renk(0xF7D842))

durum sınıf Dikdörtgen(x: Kesir, y: Kesir, en: Kesir,
                      boy: Kesir, den renk: Renk = beyaz2)

// yatay böleceksek: z -> y, yoksa dikey böleceksek: z -> x
tanım dikdörtgenleriBöl(dörtgenler: Yöney[Dikdörtgen], z: Kesir, yatay: İkil) = {
    den çıktı = dörtgenler
    yineleİçin(dörtgenler.boyu - 1 |-| 0 adım -1) { i =>
        dez dörtgen = dörtgenler(i)
        dez (ufak, iri) = eğer (yatay) (dörtgen.y, dörtgen.y + dörtgen.boy) yoksa (dörtgen.x, dörtgen.x + dörtgen.en)
        eğer (z > ufak && z < iri) {
            eğer (rastgeleİkil) {
                çıktı = çıktı.dilim(0, i) ++ çıktı.dilim(i + 1, çıktı.boyu)
                çıktı = çıktı ++ böl(dörtgen, z, yatay)
            }
        }
    }
    çıktı
}

tanım böl(dörtgen: Dikdörtgen, z: Kesir, yatay: İkil) = {
    dez (x, y, en, boy) = (dörtgen.x, dörtgen.y, dörtgen.en, dörtgen.boy)
    dez gen = z - (eğer (yatay) y yoksa x)
    dez dörtgenA = eğer (yatay) Dikdörtgen(x, y, en, gen) yoksa Dikdörtgen(x, y, gen, boy)
    dez dörtgenB = eğer (yatay) Dikdörtgen(x, z, en, boy - gen) yoksa Dikdörtgen(z, y, en - gen, boy)
    Yöney(dörtgenA, dörtgenB)
}

// bütün tuvali kaplayan bir dikdörtgenle başlıyoruz
den dörtgenler = Yöney(Dikdörtgen(ta.x, ta.y, ta.eni, ta.boyu))
dez n = 7
dez adımx = ta.eni / (n + 1)
dez adımy = ta.boyu / (n + 1)

yineleİçin(1 |-| n) { i =>
    dörtgenler = dikdörtgenleriBöl(dörtgenler, ta.x + i * adımx, yanlış) // dikey böl
    dörtgenler = dikdörtgenleriBöl(dörtgenler, ta.y + i * adımy, doğru) // yatay böl
}

dörtgenler = rastgeleKarıştır(dörtgenler)
renkler.ikileSırayla.işle {
    durum (renk, sıra) => eğer (sıra < dörtgenler.boyu)
        dörtgenler(sıra).renk = renk
}

tanım dikdörtgendenResim(r: Dikdörtgen) = {
    kalemRengi(siyah) * boyaRengi(r.renk) * kalemBoyu(10) * götür(r.x, r.y) ->
        Resim.dikdörtgen(r.en, r.boy)
}
çiz(dörtgenler.işle(dikdörtgendenResim))
