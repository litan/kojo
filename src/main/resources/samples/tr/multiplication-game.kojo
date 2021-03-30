tümEkran()

val oyunSüresi = 30 // Bir dakika sürsün istersen 60 girebilirsin

// iri1 ve iri2 değerlerini değiştirerek oyunun zorluğunu ayarlayabilirsin:
val iri1 = 9
val iri2 = 10

val işlem = "x" 

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
    yanıt = sayı1 * sayı2 
    yanıtKutusu.setText("")
    yanıtUzunluğu = yanıt.toString.length
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
val yazıYüzü = yazıyüzü("Sans Serif", 60)
val yanıtKutusu = new ay.Yazıgirdisi(0) {
    // bu komutlar Yazıgirdisi türünün metodları.
    // Henüz Türkçe'leri yok
    setFont(yazıYüzü)
    setColumns(3)
    setHorizontalAlignment(ay.değişmez.merkez)
    setBackground(aaRengi)
    setBorder(ay.çerçeveci.çizgiKenar(siyah))
}

def işlemPanosu = new ay.Sütun(
    new ay.Sıra(
        new ay.Tanıt(sayıYazısı(sayı1)) {
            setFont(yazıYüzü)
            setHorizontalAlignment(ay.değişmez.merkez)
        },
        new ay.Tanıt(işlem) {
            setFont(yazıYüzü)
            setHorizontalAlignment(ay.değişmez.merkez)
        },
        new ay.Tanıt(sayıYazısı(sayı2)) {
            setFont(yazıYüzü)
            setHorizontalAlignment(ay.değişmez.merkez)
        }
    ) {
        setBackground(aaRengi)
    },
    yanıtKutusu
) {
    setBackground(aaRengi)
    setBorder(ay.çerçeveci.boşKenar)
}

def yeniAraYüz() {
    araYüz.sil()
    araYüz = götür(-150, 0) -> Resim.arayüz(işlemPanosu)
    çiz(araYüz)
    yanıtKutusu.takeFocus() // basılan tuşlar yanıtKutusuna gelsin
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
yanıtKutusu.addKeyListener(new ay.olay.TuşUyarlayıcısı {

    // her tuşa basıldığında bu çalışır
    override def keyPressed(e: ay.olay.TuşaBasmaOlayı) {
        if (e.getKeyCode == Kc.VK_ESCAPE) {  // Escape tuşuna basılınca
            e.consume()
            durdur()
            tümEkran() // zaten tüm ekran olduğu için bu komutla tüm ekrandan çıkar.. aç/kapa düğmesi gibi yani
        }
    }
    // her karakter okunduğunda da bu çalışır
    override def keyTyped(e: ay.olay.TuşaBasmaOlayı) {
        if (!e.getKeyChar.isDigit) {  // sayı olmayan girdileri boş verelim
            e.consume()
        }
    }
    // her tuştan kalkışta bakalım yanıt hazır ve doğru mu
    override def keyReleased(e: ay.olay.TuşaBasmaOlayı) {
        if (yanıtVerdiMi(e)) {
            val x = yanıtKutusu.value
            yanıtaBak(x)
        }
        else {
            yanıtKutusu.setForeground(siyah)
        }
    }
    
    def yanıtVerdiMi(e: ay.olay.TuşaBasmaOlayı) = {
        yanıtKutusu.getText.length >= yanıtUzunluğu
    }
    def yanıtaBak(x: Sayı) {
        if (x == yanıt) {
            yanıtKutusu.setForeground(Renk(0, 220, 0))
            doğrular += 1
            if (!bittiMi && yeterinceSüreKaldıMı) {
                sırayaSok(0.3) {
                    yeniSoru()
                    yeniAraYüz()
                    yanıtKutusu.setForeground(siyah)
                }
            }
        }
        else {
            yanıtKutusu.setForeground(kırmızı)
            yanlışlar += 1
            if (!bittiMi) {
                yeniAraYüz()
            }
        }
    }

})

def çizMesaj(m: Yazı, r: Renk) {
    val te = textExtent(m, 30)
    val resim = kalemRengi(r) * götür(ta.x + (ta.en - te.width) / 2, 0) -> Resim.yazı(m, 30)
    çiz(resim)
}

def süreyiYönet() {
    def skor(ds: Sayı, ys: Sayı) = ds - ys
    var geçenSüre = 0
    val sayaç = götür(ta.x + 10, ta.y + 50) -> Resim.yazıRenkli(geçenSüre, 40, mavi)
    çiz(sayaç)
    sayaç.girdiyiAktar(Resim.tuvalBölgesi)

    yineleSayaçla(1000) {
        geçenSüre += 1
        sayaç.güncelle(geçenSüre)

        if (geçenSüre == oyunSüresi) {
            bittiMi = doğru
            val mesaj = s"""      Oyun bitti!

            
            |Doğru yanıtlar: $doğrular
            |Yanlış yanıtlar: $yanlışlar
            |Skor: ${skor(doğrular, yanlışlar)}
            """
            yanıtKutusu.hide
            araYüz.sil()
            çizMesaj(mesaj.stripMargin, siyah)
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
