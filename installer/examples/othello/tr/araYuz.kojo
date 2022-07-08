//#yükle tr/anaTanimlar
//#yükle tr/eTahta
//#yükle tr/bellek
//#yükle tr/alfabeta

class Arayüz( // tahtayı ve taşları çizelim ve canlandıralım
    tahta:      ETahta,
    bellek:     Bellek,
    bilgisayar: Taş) {
    private def tahtayıKur = {
        silVeSakla; /* tümEkranTuval; */ artalanıKur(koyuGri)
        val içKöşeler = EsnekDizim.boş[Resim]
        val içKöşeKalemRengi = Renk(255, 215, 85, 101) // soluk sarımsı bir renk
        for (x <- tahta.satırAralığı; y <- tahta.satırAralığı) {
            val oda = Oda(y, x)
            val kRenk = if (tahta.içKöşeMi(oda)) içKöşeKalemRengi else mor
            val r = kalemRengi(kRenk) * boyaRengi(taşınRengi(tahta.taş(y, x))) *
                götür(odanınNoktası(oda)) -> Resim.dikdörtgen(boy, boy)
            r.çiz()
            kareninOdası += (r -> oda)
            odanınKaresi += (oda -> r)
            if (kRenk == içKöşeKalemRengi) içKöşeler += r
            kareyiTanımla(r)
        }
        içKöşeler.dizi.map(_.öneAl())
    }
    val odaSayısı = tahta.odaSayısı
    // karelerin boyu inç başına nokta sayısı. 64 => 1cm'de yaklaşık 25 nokta var (/ 64 2.54)
    val boy = 50
    val (köşeX, köşeY) = (-odaSayısı / 2 * boy, -odaSayısı / 2 * boy) // tahtayı ortalamak için sol alt köşesini belirle
    val (b2, b3, b4) = (boy / 2, boy / 3, boy / 4)
    val kareninOdası = Eşlem.boş[Resim, Oda]
    val odanınKaresi = Eşlem.boş[Oda, Resim]

    def boya(hane: Oda, taş: Taş) = odanınKaresi(hane).boyamaRenginiKur(taşınRengi(taş))

    private def odanınNoktası(oda: Oda, solAltKöşe: İkil = doğru) =
        if (solAltKöşe) Nokta(köşeX + oda.stn * boy, köşeY + oda.str * boy)
        else Nokta(köşeX + oda.stn * boy + b2, köşeY + oda.str * boy + b2)

    private def kareyiTanımla(k: Resim) = {
        val oda = kareninOdası(k)
        k.fareyeTıklayınca { (_, _) =>
            tahta.taş(oda) match {
                case Yok =>
                    val yasal = tahta.hamleyiDene(oda)
                    if (yasal.size > 0) {
                        hamleyiYap(yasal, oda)
                        if (bittiMi) bittiKaçKaç(tahta)
                        else if (tahta.hamleYoksa) {
                            sırayıÖbürOyuncuyaGeçir
                            satıryaz(s"Yasal hamle yok. Sıra yine ${tahta.oyuncu().adı}ın")
                            skoruGüncelle
                        }
                        else {
                            if (tahta.oyuncu() == bilgisayar && !tahta.hamleYoksa) {
                                skorBilgisayarHamleArıyor
                                /* burada yaparsak abArama sırasında herşey donuyor ve bizim
                                   aldığımız taşlar abArama hamlesini yapana kadar dönmüyor */
                                // öneri // todo
                            }
                        }
                    }
                case _ =>
            }
        }
        def odaRengi = taşınRengi(tahta.taş(oda))
        def renk = taşınRengi(tahta.oyuncu())
        k.fareGirince { (x, y) =>
            ipucu.konumuKur(odanınNoktası(oda, yanlış) - Nokta(b2, -b2))
            tahta.taş(oda) match {
                case Yok => if (tahta.hamleyiDene(oda).size > 0) {
                    k.boyamaRenginiKur(renk)
                    ipucu.güncelle(s"${tahta.hamleGetirisi(oda)}")
                }
                else {
                    ipucu.güncelle(s"$oda")
                }
                case _ => ipucu.güncelle(s"$oda")
            }
            ipucu.göster()
            ipucu.öneAl()
            ipucu.girdiyiAktar(k)
        }
        k.fareÇıkınca { (_, _) =>
            k.boyamaRenginiKur(odaRengi)
            ipucu.gizle()
            // todo: çıkmadan, ya da tekrar tıklamadan çalışmıyor!
            if (tahta.oyuncu() == bilgisayar && !tahta.hamleYoksa) öneri
        }
    }
    private val boşOdaRengi = Renk(10, 111, 23) // koyuYeşil
    private def taşınRengi(t: Taş) = t match {
        case Yok   => boşOdaRengi
        case Beyaz => beyaz
        case Siyah => siyah
    }

    tahtayıKur

    def bittiKaçKaç(tahta: ETahta) = if (!tahta.oyunBitti) {
        tahta.oyunBitti = doğru
        skorBitiş
        satıryaz(s"Oyun bitti.\n${tahta.kaçkaç()}")
    }

    def hamleyiGöster(oda: Oda) = {
        hamleResminiSil
        if (hamleResmiAçık) {
            hamleResmi = götür(odanınNoktası(oda, yanlış)) * kalemRengi(mavi) * kalemBoyu(3) *
                boyaRengi(renksiz) -> Resim.daire(b4)
            hamleResmi.girdiyiAktar(odanınKaresi(oda))
            hamleResmi.çiz()
        }
    }
    def hamleResminiSil = hamleResmi.sil()
    private var hamleResmi: Resim = Resim.daire(b4)
    private var hamleResmiAçık = yanlış
    def hamleyiAçKapa(d: Resim) = {
        hamleResmiAçık = !hamleResmiAçık
        if (hamleResmiAçık) düğmeSeçili(d) else düğmeTepkisi(d)
        tahta.sonHamle match {
            case Biri(hane) => hamleyiGöster(hane)
            case _          =>
        }
    }

    def seçenekleriGöster = {
        seçenekResimleri.foreach { r => r.sil() }
        if (seçeneklerAçık) {
            val sıralı = tahta.yasallar.map { oda => (oda, tahta.hamleGetirisi(oda)) }.sortBy { p => p._2 }.reverse
            if (sıralı.size > 0) {
                val enİriGetiri = sıralı.head._2
                seçenekResimleri = sıralı map {
                    case (oda, getirisi) =>
                        val renk = if (getirisi == enİriGetiri) sarı else turuncu
                        val göster = götür(odanınNoktası(oda, yanlış)) * kalemRengi(renk) * kalemBoyu(3) *
                            boyaRengi(renksiz) -> Resim.daire(b4)
                        göster.girdiyiAktar(odanınKaresi(oda))
                        göster.çiz()
                        göster
                }
            }
        }
    }
    private var seçenekResimleri: Dizi[Resim] = Dizi()
    private var seçeneklerAçık = yanlış
    private def seçenekleriAçKapa(d: Resim) = {
        seçeneklerAçık = !seçeneklerAçık
        seçenekleriGöster
        if (seçeneklerAçık) düğmeSeçili(d) else düğmeTepkisi(d)
        if (!seçeneklerAçık) seçenekleriKapa(d)
    }
    private def seçenekleriKapa(d: Resim) = {
        seçeneklerAçık = yanlış
        seçenekResimleri.foreach { r => r.sil() }
        düğmeTepkisi(d)
        d.kalemRenginiKur(renksiz)
    }

    def ileri = {
        bellek.ileriGit
        taşlarıGüncelle
        bittiMi
    }
    def geri = {
        bellek.geriAl
        taşlarıGüncelle
    }
    def taşlarıGüncelle = {
        for (y <- tahta.satırAralığı; x <- tahta.satırAralığı)
            boya(Oda(y, x), tahta.taş(y, x))
        skoruGüncelle
        tahta.sonHamle match {
            case Biri(hane) => hamleyiGöster(hane)
            case _          => hamleResminiSil
        }
        seçenekleriGöster
    }
    def bittiMi =
        if (tahta.hamleYoksa) {
            sırayıÖbürOyuncuyaGeçir
            if (tahta.hamleYoksa) {
                skorBitiş
                doğru
            }
            else {
                sırayıÖbürOyuncuyaGeçir
                yanlış
            }
        }
        else yanlış

    def yeniOyun = if (tahta.hamleSayısı() != 1) {
        tahta.başaAl("Yeni oyun:")
        for (x <- tahta.satırAralığı; y <- tahta.satırAralığı)
            boya(Oda(y, x), tahta.taş(y, x))
        bellek.başaAl()
        skorBaşlangıç
        hamleResminiSil
        if (tahta.oyuncu() == bilgisayar && !tahta.hamleYoksa) öneri
    }

    def hamleyiYap(yasal: Dizi[Komşu], hane: Oda, duraklamaSüresi: Kesir = 0.0): Birim = {
        // todo: iyileştirmek için eTahta'da tanımla: def hamleYap(hane: Oda): Dizi[Oda]  çıktı olarak boyanması gereken odaları sun. İngilizce'ye tercüme ederken de öyle yaptım.
        def bütünTaşlarıÇevir = yasal.foreach { komşu =>
            val komşuTaş = tahta.taş(komşu.oda)
            tahta.gerisi(komşu).takeWhile { oda =>
                tahta.taş(oda) != Yok && tahta.taş(oda) != tahta.oyuncu()
            }.foreach(taşıAltÜstYap(_))
            taşıAltÜstYap(komşu.oda)
        }
        def taşıAltÜstYap(oda: Oda): Birim = {
            tahta.taşıKur(oda)(tahta.oyuncu())
            boya(oda, tahta.oyuncu())
        }
        bellek.saklaTahtayı(doğru, tahta.sonHamle)
        tahta.sonHamle = Biri(hane)
        bütünTaşlarıÇevir
        tahta.taşıKur(hane)(tahta.oyuncu())
        boya(hane, tahta.oyuncu())
        satıryaz(s"Hamle ${tahta.hamleSayısı()} ${tahta.oyuncu()} $hane:")
        tahta.yaz
        sırayıÖbürOyuncuyaGeçir
        tahta.hamleSayısı.artır()
        bellek.yeniHamleYapıldı
        skoruGüncelle
        hamleyiGöster(hane)
        if (duraklamaSüresi > 0) durakla(duraklamaSüresi)
    }

    def sırayıÖbürOyuncuyaGeçir = {
        tahta.oyuncu.değiştir()
        seçenekleriGöster
    }

    def özdevin(süre: Kesir = 0.0) = zamanTut("Özdevinimli oyun") {
        özdevinimliOyun(
            // abArama,  // yavaş! bütün Kojo'yu donduruyor!
            köşeYaklaşımı,
            süre)
    }("sürdü")
    def özdevinimliOyun( // özdevinim ve bir kaç hamle seçme yöntemi/yaklaşımı (heuristic)
        yaklaşım:        İşlev1[Dizi[Oda], Belki[Oda]],
        duraklamaSüresi: Kesir /*saniye*/
    ) = {
        val dallanma = EsnekDizim.boş[Sayı]
        var oyna = doğru
        while (oyna) yaklaşım(tahta.yasallar) match {
            case Biri(oda) =>
                dallanma += tahta.yasallar.size
                hamleyiYap(tahta.hamleyiDene(oda), oda, duraklamaSüresi)
            case _ =>
                sırayıÖbürOyuncuyaGeçir
                yaklaşım(tahta.yasallar) match {
                    case Biri(oda) =>
                        satıryaz(s"Yasal hamle yok. Sıra yine ${tahta.oyuncu().adı}ın")
                        dallanma += tahta.yasallar.size
                        hamleyiYap(tahta.hamleyiDene(oda), oda, duraklamaSüresi)
                    case _ =>
                        bittiKaçKaç(tahta)
                        if (dallanma.sayı > 0) {
                            val d = dallanma.dizi
                            satıryaz(s"Oyun ${d.size} kere dallandı. Dal sayıları: ${d.mkString(",")}")
                            satıryaz(s"Ortalama dal sayısı: ${yuvarla(d.sum / (1.0 * d.size), 1)}")
                            satıryaz(s"En iri dal sayısı: ${d.max}")
                        }
                        oyna = yanlış
                }
        }
    }

    def abArama(yasallar: Dizi[Oda]): Belki[Oda] = // yasallar yerine tahtadanTahta işlevini girdi olarak kullanıyor
        ABa.hamleYap(new Durum(tahtadanTahta, tahta.oyuncu()))
    def öneri: Birim = {
        val aTahta = tahtadanTahta
        val durum = new Durum(aTahta, tahta.oyuncu())
        if (durum.bitti) bittiKaçKaç(tahta)
        else {
            val hamle = ABa.hamleYap(durum) match {
                case Biri(oda) => oda
                case _ =>
                    sırayıÖbürOyuncuyaGeçir
                    ABa.hamleYap(new Durum(durum.tahta, durum.karşıTaş)) match {
                        case Biri(oda) => oda
                        case _         => throw new Exception("Burada olmamalı")
                    }

            }
            hamleyiYap(tahta.hamleyiDene(hamle), hamle)
            bittiMi
        }
    }
    def tahtadanTahta: Tahta = { // elektronik tahtadan arama tahtası oluşturalım
        val tane = odaSayısı
        var t = new Tahta(tane, Vector.fill(tane * tane)(0))
        def diziden(dizi: Dizi[(Sayı, Sayı)])(taş: Taş) = t = t.koy(dizi.map(p => Oda(p._1, p._2)), taş)
        for (t <- Dizi(Beyaz, Siyah))
            diziden(for (y <- 0 until tane; x <- 0 until tane; if (t == tahta.taş(y, x))) yield (y, x))(t)
        t
    }
    def köşeYaklaşımı(yasallar: Dizi[Oda]): Belki[Oda] = rastgeleSeç(yasallar.filter(tahta.köşeMi(_))) match {
        case Biri(oda) => Biri(oda) // köşe bulduk!
        case _ => rastgeleSeç(yasallar.filter(tahta.içKöşeMi(_))) match {
            case Biri(oda) => Biri(oda)
            case _ => { // tuzakKenarlar tuzakKöşeleri içeriyor
                val tuzakKenarOlmayanlar = yasallar.filterNot(tahta.tuzakKenarMı(_))
                enİriGetirililerArasındanRastgele(
                    if (!tuzakKenarOlmayanlar.isEmpty) tuzakKenarOlmayanlar
                    else { // tuzak kenarlardan getirisi en iri olanlardan seçiyoruz
                        val tuzakKöşeOlmayanlar = yasallar.filterNot(tahta.tuzakKöşeMi(_))
                        if (tuzakKöşeOlmayanlar.isEmpty) yasallar else tuzakKöşeOlmayanlar
                    }
                )
            }
        }
    }
    def rastgeleSeç[T](dizi: Dizi[T]): Belki[T] = if (dizi.isEmpty) Hiçbiri else
        Biri(dizi.drop(rastgele(dizi.size)).head)
    def enİriGetirililerArasındanRastgele(yasallar: Dizi[Oda]): Belki[Oda] =
        rastgeleSeç(enGetirililer(yasallar))
    def enGetirililer(yasallar: Dizi[Oda]): Dizi[Oda] = {
        def bütünEnİriler[A, B: Ordering](d: Dizin[A])(iş: A => B): Dizin[A] = {
            d.sortBy(iş).reverse match {
                case Dizin()       => Dizin()
                case baş :: kuyruk => baş :: kuyruk.takeWhile { oda => iş(oda) == iş(baş) }
            }
        }
        bütünEnİriler(yasallar.toList) { tahta.hamleGetirisi(_) }
    }

    private def düğme(x: Kesir, y: Kesir, boya: Renk, mesaj: Yazı) = {
        val d = götür(x, y) * kalemRengi(renksiz) * boyaRengi(boya) -> Resim.dizi(
            götür(boy / 5, b2 + b4 / 3) -> Resim.yazıRenkli(mesaj, 10, beyaz),
            kalemBoyu(3) -> Resim.daire(boy * 9 / 20))
        düğmeTepkisi(d)
        d.çiz()
        d
    }
    private def düğmeTepkisi(d: Resim, rFareGirince: Renk = beyaz, rFareÇıkınca: Renk = renksiz) = {
        d.fareGirince { (_, _) => d.kalemRenginiKur(rFareGirince) }
        d.fareÇıkınca { (_, _) => d.kalemRenginiKur(rFareÇıkınca) }
    }
    private def düğmeSeçili(d: Resim) = düğmeTepkisi(d, renksiz, beyaz)

    private val (dx, dy) = ((0.8 + odaSayısı) * boy + köşeX, köşeY + b2)
    private val d0 = {
        val d = düğme(dx, dy + 2 * boy, pembe, "öneri")
        d.fareGirince { (_, _) =>
            d.kalemRenginiKur(if (tahta.yasallar.isEmpty) kırmızı else beyaz)
        }
        /* todo: çalışmadı
        var running = yanlış
        def run(flag: => İkil) = flag
        d.fareyeTıklayınca { (_, _) => running = doğru; skorBilgisayarHamleArıyor }
        d.fareÇıkınca { (_, _) => if (run(running)) öneri; running = yanlış; düğmeTepkisi(d) } */
        // todo: skor ne yazık ki güncellenmiyor arama sırasında bütün arayüz donuyor
        d.fareyeTıklayınca { (_, _) => skorBilgisayarHamleArıyor; öneri }
    }
    private val d1 = {
        val d = düğme(dx, dy + boy, sarı, "seçenekler")
        d.fareyeTıklayınca { (_, _) => seçenekleriAçKapa(d) }
        d
    }
    private val d2 = {
        val d = düğme(dx + boy, dy + boy, mavi, "son hamle aç/kapa")
        d.kalemRenginiKur(renksiz) // başlangıçta son hamleyi görmeyelim
        d.fareyeTıklayınca { (_, _) => hamleyiAçKapa(d) }
        d
    }
    düğme(dx + boy, dy + 2 * boy, turuncu, "tüm ekran aç/kapa").fareyeTıklayınca { (_, _) => tümEkranTuval() }
    düğme(dx, dy, kırmızı, "özdevin").fareyeTıklayınca { (_, _) => özdevin() }
    düğme(dx + boy, dy, yeşil, "yeni oyun").fareyeTıklayınca { (_, _) => yeniOyun; seçenekleriGöster }
    düğme(dx, dy + 3 * boy, açıkGri, "geri").fareyeTıklayınca { (_, _) => geri }
    düğme(dx + boy, dy + 3 * boy, renkler.blanchedAlmond, "ileri").fareyeTıklayınca { (_, _) => ileri }
    private val skorYazısı = {
        val y = {
            val tahtaTavanı = dy + (odaSayısı - 0.75) * boy
            val düğmelerinTavanı = dy + 5 * boy
            enİrisi(tahtaTavanı, düğmelerinTavanı)
        }
        val yazı = götür(dx - b3, y) -> Resim.yazıRenkli(s"", 20, sarı)
        yazı.çiz(); yazı
    }
    def skorBitiş = {
        val fark = tahta.say(Beyaz) - tahta.say(Siyah)
        val msj =
            if (fark > 0)
                s"Beyaz $fark taşla kazandı"
            else if (fark < 0)
                s"Siyah ${-fark} taşla kazandı"
            else "Berabere!"
        skorYazısı.güncelle(s"$msj\n${tahta.kaçkaç(doğru)}")
    }
    def skorBaşlangıç = skorYazısı.güncelle(s"${tahta.oyuncu().adı.capitalize} başlar")
    def skoruGüncelle = skorYazısı.güncelle(s"${tahta.hamleSayısı()}. hamle${if (bellek.sıraGeriDöndüMü) " yine " else " "}${tahta.oyuncu().adı}ın\n${tahta.kaçkaç(doğru)}")
    def skorBilgisayarHamleArıyor = skorYazısı.güncelle(s"${tahta.hamleSayısı()}. hamle. Bilgisayar arıyor...\n${tahta.kaçkaç(doğru)}")
    skorBaşlangıç

    private val ipucu = Resim.yazıRenkli("", 20, kırmızı)
    ipucu.çiz()

    tuşaBasınca { t =>
        t match {
            case tuşlar.VK_RIGHT => ileri
            case tuşlar.VK_LEFT  => geri
            case tuşlar.VK_UP    => öneri
            case _               =>
        }
    }

    if (tahta.oyuncu() == bilgisayar && !tahta.hamleYoksa) öneri

}
