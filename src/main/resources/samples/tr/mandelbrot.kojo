/*
 Mandelbrot Kümesinin (MK) o garip resimlerini görmüşsündür. MK modern
 matematiğin gizemli icat ve keşiflerinin en önde gelenlerindendir
 desek fazla abartmış olmayız. Bu yazılımcıkla MK'nin nereden geldiğini
 görecek ve ne olup bittiğini daha iyi anlayacağız.

 Fareyi tıklayıp sürükleyerek MK'nin istediğin bir köşesine yaklaş.
 Sonra geri okuyla daha önceki pencereye döne. İleri okuyla da
 tekrar ileri git. İlk yaklaşma birkaç saniye alır. Ama ileri/geri okları
 hemen çalışır. Yukarı okuyla biraz yaklaş. Aşağı okuyla da biraz uzaklaş.
 Yukarı oku yaklaşırken farenin konumununu merkez alır. Fareyi oynatarak
 yaklaştığın noktayı seçebilirsin.

 Kare kare film çekmek istersen, O tuşuyla gözüne kestirdiğin bir noktaya
 ortala. Y tuşuyla istediğin kadar yaklaş ve gerektikçe O ile tekrar ortala.
 Eğer merkezden uzak bir nokta seçersen, O tuşu Y tuşunun beş katı zaman
 alabilir. Benim bilgisayarımda Y yaklaşık 2 saniye, O ise en çok 10 saniye
 alıyor. Haberin olsun. İstediğin zaman R tuşuyla geriye doğru hızla oynatarak
 en başa gittikten sonra P tuşuyla ileri oynatabilirsin. P ile de en
 sona gittikten sonra tekrar R tuşuyla geri oynatabilirsin. P tuşundan sonra
 tekrar O ve Y kullanmaya devam edebilir ve istediğin zaman da kameranla
 çekim yapabilirsin.

 P ve R tuşları saniyede yaklaşık 30 çizim oynatırlar ki filmin göze güzel
 ve yumuşak hareketli görünsün. Ama çektiğin filmi yavaşlatarak izlemende
 fayda olacaktır.

 Sana ve arkadaşlarına keyifli keşifler diliyorum.

 Bu örnek şu kaynaktan esinlendi:
     http://justindomke.wordpress.com/2008/11/29/mandelbrot-in-scala/
 Bir de şunlara bakıver:
     https://tr.wikipedia.org/wiki/Mandelbrot_kümesi
     https://en.wikipedia.org/wiki/Mandelbrot_set
 */

/* çizim çok yavaşsa, örneğin beş on saniyeden çok sürüyorsa
 * buradaki değişmez değerleri küçültmeyi dene */
dez kenar = 500 // kümemizin resmi k x k büyüklüğünde bir kare
dez yinelemeSınırı = 3000 // bu da resmin çözünürlüğünü artırıyor
//tümEkran()

/*
 x*x = 25 denklemini çözüp x=5 demek kolay. Peki x*x = -1 denklemini
 çözümü nedir?  Eskiler çözümü yok demişler uzun yıllar boyu. Neden
 bazı denklemleri çözemediğimizi bir türlü anlayamamışlar. Denklemleri
 zoraki ikiye ayırmışlar, ama pek de iyi olmamış.  Sonunda bir meraklı
 matematikci biraz hayal gücünü kullanmış ve neden olmasın demiş...
 Bir çözüm hayal etmek, başta hayal de olsa, sonra çok faydalı olmuş.
 Onun için ne dersin -1'in kareköküne i diyelim mi? i sayısının
 varlığını kabul edersek artık sayı kavramımızı gerçel sayılardan çok
 daha büyük bir uzaya büyütebiliriz.  Bir boyuttan iki boyuta sıçrarız!
 Bu sayılardan MK ve onun şekline de o sayede varacağız.  Yatay eksen
 gerçel sayılar, dikey eksen de i'nin katlarını yani hayali kısmını
 temsil edecek. Genel olarak her karmaşık sayıyı iki tane gerçel sayı
 olarak ifade edebiliriz. Ama karmaşık (ingilizce complex'ten gelmiş)
 bence pek yakışmıyor bu zengin icada. Biz adını varsıl koyalım. Ne
 dersin? Şöyle yazabiliriz:

    v = (x, y) = x + i * y

 Burada x ve y birer normal (gerçel) sayı.

   v1 = (x1, y1) => v1 + v2 = x1+x2 + i*(y1+y2)
   v2 = (x2, y2) => v1 * v2 = x1*x2 + (x1*y2 + x2*y1)i - y1*y2

 uzunluk: (0, 0) noktasından (x, y) noktasına çizilen doğru
 parçasının uzunluğu
 */

// Lafı uzun oldu, ama yazılımı kısa!
durum sınıf VarsılSayı(x: Kesir, y: Kesir) {
    tanım +(v2: VarsılSayı) = VarsılSayı(x + v2.x, y + v2.y)
    tanım *(v2: VarsılSayı) = VarsılSayı(x * v2.x - y * v2.y, x * v2.y + v2.x * y)
    tanım uzunluğu = karekökü(x * x + y * y)
}

// Mandelbrot kümesini yazmadan önce bir de dikdörtgen türü oluşturalım.
// Epey faydalı olacak:
durum sınıf Dörtgen(x1: Kesir, x2: Kesir, y1: Kesir, y2: Kesir) {
    dez en = x2 - x1
    dez boy = y2 - y1
    dez alanı = en * boy
    dez solalt = Nokta(x1, y1)
    dez sağüst = Nokta(x2, y2)
    dez ortaNoktası = (x, y)
    dez (x, y) = ((x2 + x1) / 2, (y2 + y1) / 2)
    dez yazı = {
        dez a = alanı
        eğer (a > 0.0001) s"${yuvarla(a, 5)}" yoksa f"${a}%2.3e"
    }
    dez dörtlü = (x1, x2, y1, y2)
    tanım büyüt(oran: Kesir): Dörtgen = {
        eğer (oran <= 0 || oran >= 10.0) bu yoksa {
            dez o2 = 0.5 * oran
            dez en2 = o2 * (x2 - x1)
            dez boy2 = o2 * (y2 - y1)
            Dörtgen(x - en2, x + en2, y - boy2, y + boy2)
        }
    }
    tanım ortala(nokta: Nokta) = {
        dez xKayma = nokta.x - x
        dez yKayma = nokta.y - y
        Dörtgen(x1 + xKayma, x2 + xKayma, y1 + yKayma, y2 + yKayma)
    }
}
nesne Dörtgen {
    // bazen dört kesir girmek yerine, iki nokta girerek de bir dörtgen yapmak da
    // isteyebiliriz: Dörtgen(Nokta(x, y), Nokta(x2, y2)) şeklinde. bu apply metodu onu sağlıyor
    tanım apply(solalt: Nokta, sağüst: Nokta): Dörtgen = yeni Dörtgen(solalt.x, sağüst.x, solalt.y, sağüst.y)
    // hatta farkli girdilerle birden çok apply tanımı yapabiliriz gerekirse. Örneğin:
    tanım apply(d: Dörtgen, oranX: Kesir, oranY: Kesir): Dörtgen = ??? // oran kesirleriyle çarpalım d'nin köşelerini. (sana bıraktım :)
    tanım apply(solalt: Nokta, eni: Kesir, boyu: Kesir): Dörtgen = ??? // ya da böyle yapabiliriz. Yaratıcılığa açık bir konu...
}

/*
 Mandelbrot kümesi varsıl sayılarla tanımlanan basit bir işlevle
 ortaya çıkmış.  Normal matematik fonksiyonları f(x) veya g(x) diye
 yazılır. Ama varsıl sayılar kullanınca x yerine v yazmakta ve f(v)
 demekte fayda var. MK'yi aşağıdaki m(v) işleviyle tanımlarız:

     m(n+1) = m(n)*m(n) + v
     m(0) = 0
          =>
     m(1) = v
     m(2) = v^2 + v      (v^2 = v*v)
     m(3) = v^3 + v^2    (v^3 = v*v*v)
     ...

 n büyüdükçe m(n) ufak kalırsa o zaman v sayısı M kümemizin içinde
 oluyor ve onu siyah yapıyoruz. Diğer renkler m'nin sonsuza gitmesine
 neden olan v sayılarını gösteriyor. Ne kadar çabuk m>2 olduğuna
 bakarak renk veriyoruz...
 */

tanım mKümesi(d: Dörtgen): İmge =
    // mKümesiTekİplik(d);
    mKümesiÇokİplik(d);

tanım mKümesiÇokİplik(d: Dörtgen): İmge = {
    bilgiVer(sıra, d)
    sonDörtgen = d
    eğer (bellek.eşli(d)) bellek(d) yoksa {
        dez img = imge(kenar, kenar)
        dez oranx = (d.x2 - d.x1) / kenar
        dez orany = (d.y2 - d.y1) / kenar
        getir renklendirme.renk
        dez iri: Kesir = (1.0 * yinelemeSınırı) * kenar * kenar
        zamanTut(f"mKümesi (nokta sayısı x yineleme sınırı)$iri%2.2e yineleme:") {
            // için { xi <- 0 |- kenar; yi <- 0 |- kenar }
            dez pd = (0 |- kenar).dizine.paralel
            pd.düzİşle { xi =>
                pd.işle { yi =>
                    dez x = d.x1 + xi * oranx
                    dez y = d.y1 + yi * orany
                    dez v = VarsılSayı(kayma + x, y)
                    den z = VarsılSayı(0, 0)
                    den i = 0
                    yineleDoğruKaldıkça (z.uzunluğu < 2 && i < yinelemeSınırı) {
                        z *= z; z += v; i += 1 // işte bütün küme buradan çıkıyor!
                    }
                    // küme içindeki noktalar hep siyah. diğerleri renkli olacak
                    imgeNoktasınıKur(img, xi, yi, eğer (z.uzunluğu < 2) siyah yoksa (renk(i, x, y)))
                }
            }
        }()
        bellek eşEkle (d -> img)
        img
    }
}

tanım mKümesiTekİplik(d: Dörtgen): İmge = { // sequential v paralel
    bilgiVer(sıra, d)
    sonDörtgen = d
    eğer (bellek.eşli(d)) bellek(d) yoksa {
        dez img = imge(kenar, kenar)
        dez oranx = (d.x2 - d.x1) / kenar
        dez orany = (d.y2 - d.y1) / kenar
        getir renklendirme.renk
        dez iri: Kesir = (1.0 * yinelemeSınırı) * kenar * kenar
        zamanTut(f"mKümesi (nokta sayısı x yineleme sınırı)$iri%2.2e yineleme:") {
            için { xi <- 0 |- kenar; yi <- 0 |- kenar } {
                dez x = d.x1 + xi * oranx
                dez y = d.y1 + yi * orany
                dez v = VarsılSayı(kayma + x, y)
                den z = VarsılSayı(0, 0)
                den i = 0
                yineleDoğruKaldıkça (z.uzunluğu < 2 && i < yinelemeSınırı) {
                    z *= z; z += v; i += 1 // işte bütün küme buradan çıkıyor!
                }
                // küme içindeki noktalar hep siyah. diğerleri renkli olacak
                imgeNoktasınıKur(img, xi, yi, eğer (z.uzunluğu < 2) siyah yoksa (renk(i, x, y)))
            }
        }()
        bellek eşEkle (d -> img)
        img
    }
}

den eskiUzunluk = 0.0
dez epsilon = 0.000001
tanım bilgiVer(s: Sayı, d: Dörtgen): Birim = {
    dez u = VarsılSayı(d.x, d.y).uzunluğu
    satıryaz(f"$s%2d. Alan: ${d.yazı}%-10s " + (
        eğer (mutlakDeğer(u - eskiUzunluk) < epsilon) "" yoksa
            f"Merkez: (${d.x}%2.8f, ${d.y}%2.8f) Uzunluk: ${u}%2.8f"
    ))
    eskiUzunluk = u
}
nesne renklendirme {
    /* https://stackoverflow.com/questions/16500656/which-color-gradient-is-used-to-color-mandelbrot-in-wikipedia
       https://en.wikipedia.org/wiki/Monotone_cubic_interpolation  */
    tanım renk(i: Sayı, x: Kesir, y: Kesir) = {
        dez yumuşak = log2(log2(x * x + y * y) / 2)
        renkler(sayıya(karekökü(i + 10 - yumuşak) * 256) % renkler.boyu)
    }
    miskin dez renkler = Dizi.doldur(2048) { i => k2kym(i / 2048.0) }
    tanım log2(x: Kesir) = logaritması(x) / logaritması(2)
    tanım k2kym(x: Kesir) = Renk(k2d(x, kd), k2d(x, yd), k2d(x, md))
    // 0 ve 1 arasını beşe bölüp beş renk seçiyoruz. Başladığımız renkle bitiriyoruz.
    // Arasını da yumuşak geçiş benzeri renklerle dolduruyoruz
    dez kk = Dizi(0.0, 0.16, 0.42, 0.6425, 0.8575, 1.0)
    dez kd = Dizi(0, 32, 237, 255, 0, 0) // kırmızı
    dez yd = Dizi(7, 107, 255, 170, 2, 7) // yeşil
    dez md = Dizi(100, 203, 255, 0, 1, 100) // mavi
    // önce doğrusal olarak arayı dolduralım -- cubic yapmayı sonraya ve sana bırakıyorum
    tanım k2d(x: Kesir, rd: => Dizi[Sayı]) = {
        // iki noktayı doğruyla bağlayalım: x1,y1 <-> x2,y2  x1<x<x2. O zaman y nedir?
        tanım y(x1: Kesir, x2: Kesir, y1: Kesir, y2: Kesir) =
            y1 + (x - x1) * (y2 - y1) / (x2 - x1)
        sayıya(eğer (x <= kk(0)) rd(0)
        yoksa eğer (x <= kk(1)) y(kk(0), kk(1), rd(0), rd(1))
        yoksa eğer (x <= kk(2)) y(kk(1), kk(2), rd(1), rd(2))
        yoksa eğer (x <= kk(3)) y(kk(2), kk(3), rd(2), rd(3))
        yoksa eğer (x <= kk(4)) y(kk(3), kk(4), rd(3), rd(4))
        yoksa y(kk(4), kk(5), rd(4), rd(5))
        )
    }
}

/*
 *  Burada çizmeye başlıyoruz
 */
silVeSakla
artalanıKur(siyah)
dez resimSolAltKöşe = Nokta(-kenar / 2, -kenar / 2)
dez resimGötür = götür(resimSolAltKöşe.x, resimSolAltKöşe.y)
den sıra = 1 // yaklaşma pencerelerinin sırasını çıktı gözüne yazmak için
den sonDörtgen = Dörtgen(0, 0, 0, 0) // son çizdiğimiz kümenin boyutlarını anımsamak gerekli olacak
// Başlangıç penceremizi sola doğru kaydırıyoruz (kayma miktarını x koordinatlarindan çıkarıyoruz).
// Daha önce de yukarıda mKümesi'ni tanımlarken, v'ye aynı kayma miktarını ekledik.
// Peki, neden yaptık bu numarayı? Renklendirme metodunun bir sıkıntısı
// vardı. Onu rahatlatmak için. Pencereyi ve mKümesini aslına döndürürsen görürsün.
dez kayma = 4
dez başlangıç = Dörtgen(-2 - kayma, 1 - kayma, -1.5, 1.5)
tanım başla(n: Nokta): Dörtgen = başlangıç.ortala(n)
// değişik bir merkezden de başlayabiliriz, ve sonra h tuşuyla hızla yaklaşabiliriz...
dez başlangıç2 = başla(Nokta(-5.7864, 0))
dez başlangıç3 = başla(Nokta(-5.79, 0))
dez başlangıç4 = başla(Nokta(-4.7595625, 0.08515))
den bellek = Eşlem.boş[Dörtgen, İmge]
den resim = resimGötür -> Resim.imge(mKümesi(başlangıç/*2, 3 ya da 4 de var*/))
çiz(resim)
fareyiTanımla(resim)

tanım tuşlarıTanımla() {
    tuşaBasınca { t =>
        t eşle {
            durum tuşlar.boşluk => geri()
            durum tuşlar.sol    => geri()
            durum tuşlar.sağ    => ileri()
            durum tuşlar.yukarı => yaklaş(0.90) // fareyi merkez alır
            durum tuşlar.aşağı  => uzaklaş(1.25)
            durum tuşlar.p      => oynat()
            durum tuşlar.r      => geriOynat()
            durum tuşlar.o      => ortala()
            durum tuşlar.y      => yaklaş(0.90, yanlış)
            durum tuşlar.h      => yaklaş(0.01, yanlış) // hızlı yaklaş
            durum _             =>
        }
    }
}
tuşlarıTanımla()
// yaklaştıkça bir önceki bakış penceresini saklayalım ki ona geri dönebilelim
// geri gidince de daha ileridekileri saklayacağız ki ileri geri gidebilelim
sınıf Pencere {
    tanım koy(d: Dörtgen) = bakışlar.koy(d)
    tanım al(): Dörtgen = bakışlar.al()
    tanım boşMu() = bakışlar.tane == 0
    tanım boşalt() = yineleDoğruKaldıkça (!boşMu()) al()
    gizli dez bakışlar = Yığın.boş[Dörtgen]
}
dez pGeri = yeni Pencere // geride kalan yani daha uzaktan bakışlar
dez pİleri = yeni Pencere // ilerideki yani daha yakından bakışlar
tanım geri(): İkil = {
    eğer (pGeri.boşMu()) {
        satıryaz("En başa döndük. Daha geri gidemeyiz.")
        doğru
    }
    yoksa {
        sıra -= 1
        resim.sil()
        pİleri.koy(sonDörtgen)
        resim = resimGötür -> Resim.imge(mKümesi(pGeri.al()))
        resim.çiz()
        fareyiTanımla(resim)
        yanlış
    }
}
tanım ileri(): İkil = {
    eğer (pİleri.boşMu()) {
        satıryaz("En sona geldik. Daha ileri gidemeyiz.")
        doğru
    }
    yoksa {
        sıra += 1
        resim.sil()
        pGeri.koy(sonDörtgen)
        resim = resimGötür -> Resim.imge(mKümesi(pİleri.al()))
        resim.çiz()
        fareyiTanımla(resim)
        yanlış
    }
}
tanım yaklaş(oran: Kesir = 0.80, ortala: İkil = doğru, hedef: Nokta = fareKonumu) = {
    sıra += 1
    pGeri.koy(sonDörtgen)
    ayarla(oran, ortala, hedef)
}
tanım uzaklaş(oran: Kesir = 1.25) = {
    pİleri.koy(sonDörtgen)
    ayarla(oran)
}
tanım ayarla(oran: Kesir, ortala: İkil = yanlış, hedef: Nokta = fareKonumu) {
    dez dörtgen =
        eğer (ortala && fareResmeDokunuyor)
            sonDörtgen.büyüt(oran).ortala(nTuvaldenMKye(hedef))
        yoksa sonDörtgen.büyüt(oran)
    resim.sil()
    resim = resimGötür -> Resim.imge(mKümesi(dörtgen))
    resim.çiz()
    fareyiTanımla(resim)
}
tanım fareResmeDokunuyor() =
    fareKonumu.x > -kenar / 2 && fareKonumu.x < kenar / 2 &&
        fareKonumu.y > -kenar / 2 && fareKonumu.y < kenar / 2

tanım ortala() {
    dez (hedefX, hedefY) = (fareKonumu.x, fareKonumu.y)
    dez (başlangıçX, başlangıçY) = (0, 0)
    dez (araX, araY) = (hedefX - başlangıçX, hedefY - başlangıçY)
    dez oynamaOranı = enİrisi(
        mutlakDeğer(araX) / tuvalAlanı.en,
        mutlakDeğer(araY) / tuvalAlanı.boy)
    dez adımSayısı =
        eğer (oynamaOranı > 0.10) 5 // yüzde ondan fazla hareket gerekiyor
        yoksa eğer (oynamaOranı > 0.05) 3
        yoksa 2
    satıryaz(s"$adımSayısı adımda ortalıyoruz")
    dez (adımX, adımY) = (araX / adımSayısı, araY / adımSayısı)
    dez n = Nokta(başlangıçX + adımX, başlangıçY + adımY)
    yinele(adımSayısı) {
        yaklaş(1.0, doğru, n)
    }
}
tanım oynat() {
    canlandır {
        eğer (ileri()) {
            durdur
            tuşlarıTanımla()
        }
    }
}
tanım geriOynat() {
    canlandır {
        eğer (geri()) {
            durdur
            tuşlarıTanımla()
        }
    }
}

tanım nTuvaldenMKye(n: Nokta): Nokta = Nokta(
    sonDörtgen.x1 + (sonDörtgen.en / kenar) * (n.x - resimSolAltKöşe.x),
    sonDörtgen.y1 + (sonDörtgen.boy / kenar) * (n.y - resimSolAltKöşe.y)
)
tanım tuvaldenMKye(d: Dörtgen): Dörtgen = Dörtgen(nTuvaldenMKye(d.solalt), nTuvaldenMKye(d.sağüst))

tanım fareyiTanımla(r: Resim) {
    den tıklananXY = (0.0, 0.0)
    den sürüklenenXY = (0.0, 0.0)
    den yaklaşmaKaresi: Resim = Resim.dikdörtgen(0, 0)
    r.fareyiSürükleyince { (x, y) =>
        dez farkX = x - tıklananXY._1
        dez farkY = y - tıklananXY._2
        dez fark = enİrisi(mutlakDeğer(farkX), mutlakDeğer(farkY))
        dez yeniX = tıklananXY._1 + fark * işareti(farkX)
        dez yeniY = tıklananXY._2 + fark * işareti(farkY)
        yaklaşmaKaresi.sil()
        yaklaşmaKaresi = götür(
            enUfağı(yeniX, tıklananXY._1),
            enUfağı(yeniY, tıklananXY._2)) -> Resim.dikdörtgen(fark, fark)
        çiz(yaklaşmaKaresi)
        sürüklenenXY = (yeniX, yeniY)
    }
    r.fareyiBırakınca { (x, y) =>
        dez tx1 = enUfağı(sürüklenenXY._1, tıklananXY._1)
        dez tx2 = enİrisi(sürüklenenXY._1, tıklananXY._1)
        dez ty1 = enUfağı(sürüklenenXY._2, tıklananXY._2)
        dez ty2 = enİrisi(sürüklenenXY._2, tıklananXY._2)
        yaklaşmaKaresi.sil()
        resim.sil()
        dez d = tuvaldenMKye(Dörtgen(tx1, tx2, ty1, ty2))
        pGeri.koy(sonDörtgen) // uzaklaşmak için geri dönmek isteyebiliriz
        pİleri.boşalt() // yeni bir dal, eski daldaki ileri pencerelere gerek yok artık
        sıra += 1
        resim = resimGötür -> Resim.imge(mKümesi(d))
        resim.çiz()
        fareyiTanımla(resim)
    }
    r.fareyeBasınca { (x, y) =>
        tıklananXY = (x, y)
    }
}
tuvaliEtkinleştir()
