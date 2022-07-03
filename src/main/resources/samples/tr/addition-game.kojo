// DİKKAT: toplama/çıkarma ve çarpma oyunlarının yazılımcıkları birbirlerine çok benziyor.
// Onun için birini değiştirirken diğerlerinde de aynı değişiklikleri yapmak iyi olur.
tümEkran()

val oyunSüresi = 30 // Bir dakika sürsün istersen 60 girebilirsin

// iri1 ve iri2 değerlerini değiştirerek oyunun zorluğunu ayarlayabilirsin:
val iri1 = 10
val iri2 = 10

val işlem = "+"

def farklıSayı(n: Sayı, m: Sayı) = {
    var n2 = 0
    do {
        n2 = 2 + rastgele(m - 1)
    } while (n2 == n)
    n2
}

def yeniSoru() {
    sayı1 = farklıSayı(sayı1, iri1)
    sayı2 = farklıSayı(sayı2, iri2)
    yanıt = sayı1 + sayı2
    /*
     *  DİKKAT: BURADAN SONRASI toplama/çıkarma ve çarpma oyunlarında aynı
     */
    yanıtKutusu.yazıyıKur("")
    yanıtUzunluğu = yanıt.yazıya.boyu
}

def sayıYazısı(n: Sayı) = s" $n "
val aaRengi = renkler.khaki // artalan yani arkaplan rengi

var sayı1 = 0
var sayı2 = 0
var yanıt = 0
var yanıtUzunluğu = 0
var bittiMi = yanlış

// java'nın arayüz kütüphanesi çok zengin
// birkaç tanesini Türkçe'ye çevirdik. ay modülüne koyduk.
val yazıYüzü = Yazıyüzü("Sans Serif", 60)  // kıvrıksız çizikli yazı
val yanıtKutusu = new ay.Yazıgirdisi(0) {
    // bu komutlar Yazıgirdisi türünün metodları:
    yazıYüzünüKur(yazıYüzü)
    sütunSayısınıKur(3)
    yatayDüzeniKur(ay.değişmez.merkez)
    artalanıKur(aaRengi)
    çerçeveyiKur(ay.çerçeveci.çizgiKenar(siyah))
}

def işlemPanosu = new ay.Sütun(
    new ay.Sıra(
        new ay.Tanıt(sayıYazısı(sayı1)) {
            yazıYüzünüKur(yazıYüzü)
            yatayDüzeniKur(ay.değişmez.merkez)
        },
        new ay.Tanıt(işlem) {
            yazıYüzünüKur(yazıYüzü)
            yatayDüzeniKur(ay.değişmez.merkez)
        },
        new ay.Tanıt(sayıYazısı(sayı2)) {
            yazıYüzünüKur(yazıYüzü)
            yatayDüzeniKur(ay.değişmez.merkez)
        }
    ) {
        artalanıKur(aaRengi)
    },
    yanıtKutusu
) {
    artalanıKur(aaRengi)
    çerçeveyiKur(ay.çerçeveci.boşKenar)
}

def yeniAraYüz() {
    araYüz.sil()
    araYüz = götür(-150, 0) -> Resim.arayüz(işlemPanosu)
    çiz(araYüz)
    yanıtKutusu.girdiOdağıOl() // basılan tuşlar yanıtKutusuna gelsin
}

var doğrular = 0
var yanlışlar = 0

var sonSorununZamanı = buAn
def yeterinceSüreKaldıMı = {
    val kalanSüre = buAn - sonSorununZamanı
    if (kalanSüre > 100) {
        sonSorununZamanı = buAn
        doğru
    }
    else yanlış
}

// yanıtKutusunun üstünde tuşlara basıldıkça birşeyler yapmamız gerek
yanıtKutusu.girdiDinleyiciEkle(new ay.olay.TuşUyarlayıcısı {

    // her tuşa basıldığında bu çalışır
    override def keyPressed(e: ay.olay.TuşaBasmaOlayı) {
        if (e.tuşKodu == tuşlar.VK_ESCAPE) { // Escape tuşuna basılınca
            e.tüket()
            durdur()
            tümEkran() // zaten tüm ekran olduğu için bu komutla tüm ekrandan çıkar.. aç/kapa düğmesi gibi yani
        }
    }
    // her karakter okunduğunda da bu çalışır
    override def keyTyped(e: ay.olay.TuşaBasmaOlayı) {
        if (!e.tuşHarfi.sayıMı) { // sayı olmayan girdileri boşverelim
            e.tüket()
        }
    }
    // her tuştan kalkışta bakalım yanıt hazır ve doğru mu
    override def keyReleased(e: ay.olay.TuşaBasmaOlayı) {
        if (yanıtVerdiMi(e)) {
            val x = yanıtKutusu.değeri
            yanıtaBak(x)
        }
        else {
            yanıtKutusu.önalanıKur(siyah)
        }
    }

    def yanıtVerdiMi(e: ay.olay.TuşaBasmaOlayı) = {
        yanıtKutusu.yazıyıAl.boyu >= yanıtUzunluğu
    }
    def yanıtaBak(x: Sayı) {
        if (x == yanıt) {
            yanıtKutusu.önalanıKur(Renk(0, 220, 0)) // parlak bir yeşil
            doğrular += 1
            if (!bittiMi && yeterinceSüreKaldıMı) {
                sırayaSok(0.3) {
                    yeniSoru()
                    yeniAraYüz()
                    yanıtKutusu.önalanıKur(siyah)
                }
            }
        }
        else {
            yanıtKutusu.önalanıKur(kırmızı)
            yanlışlar += 1
            if (!bittiMi) {
                yeniAraYüz()
            }
        }
    }

})

def çizMesaj(m: Yazı, r: Renk) {
    val çerçeve = yazıÇerçevesi(m, 30)
    val resim = kalemRengi(r) * götür(ta.x + (ta.eni - çerçeve.eni) / 2, 0) -> Resim.yazı(m, 30)
    çiz(resim)
}

def süreyiYönet() {
    def skor(doğruSayısı: Sayı, yanlışSayısı: Sayı) = doğruSayısı - yanlışSayısı
    var kalanSüre = oyunSüresi
    val sayaç = götür(ta.x + 10, ta.y + 50) -> Resim.yazıRenkli(kalanSüre, 40, mavi)
    çiz(sayaç)
    sayaç.girdiyiAktar(Resim.tuvalBölgesi)

    yineleSayaçla(1000) {
        kalanSüre -= 1
        sayaç.güncelle(kalanSüre)

        if (kalanSüre == 0) {
            bittiMi = doğru
            val mesaj = s"""      Oyun bitti!

            
            |Doğru yanıtlar: $doğrular
            |Yanlış yanıtlar: $yanlışlar
            |Skor: ${skor(doğrular, yanlışlar)}
            """
            yanıtKutusu.sakla
            araYüz.sil()
            çizMesaj(mesaj.kenarPayınıÇıkar, siyah)
            durdur()
        }
    }
}

silVeSakla()
çizSahne(aaRengi)
val ta = tuvalAlanı
var araYüz: Resim = Resim.yatay(1)
yeniSoru()
yeniAraYüz()
süreyiYönet()
