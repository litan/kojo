tümEkran()

val oyunSüresi = 30 // Bir dakika sürsün istersen 60 girebilirsin

// ufak1 ve ufak2 değerlerini değiştirerek oyunun zorluğunu ayarlayabilirsin:
val ufak1 = 60  // ilk sayı ufak1 ile iki katı arasında
val ufak2 = 20  // ikinci sayı ufak2 ile iri2 arasında
val iri2 = ufak1

val işlem = "-"

def farklıSayı(n: Sayı, ufak: Sayı, iri: Sayı) = { 
    var n2 = 0
    do {
        n2 = ufak + rastgele(iri - ufak) 
    } while (n2 == n)
    n2
}

def yeniSoru() {
    sayı1 = farklıSayı(sayı1, ufak1, 2 * ufak1) 
    sayı2 = farklıSayı(sayı2, ufak2, iri2) 
    yanıt = sayı1 - sayı2 
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

// java'nın arayüz kütüphanesi çok zengin -- ne yazık ki Türkçe'si yok bildiğim kadarıyla
import javax.swing.SwingConstants
import javax.swing.BorderFactory
val yazıYüzü = Font("Sans Serif", 60)
val yanıtKutusu = new TextField(0) {
    setFont(yazıYüzü)
    setColumns(3)
    setHorizontalAlignment(SwingConstants.CENTER)
    setBackground(aaRengi)
    setBorder(BorderFactory.createLineBorder(siyah))
}

def işlemPanosu = new ColPanel(
    new RowPanel(
        new Label(sayıYazısı(sayı1)) {
            setFont(yazıYüzü)
            setHorizontalAlignment(SwingConstants.CENTER)
        },
        new Label(işlem) {
            setFont(yazıYüzü)
            setHorizontalAlignment(SwingConstants.CENTER)
        },
        new Label(sayıYazısı(sayı2)) {
            setFont(yazıYüzü)
            setHorizontalAlignment(SwingConstants.CENTER)
        }
    ) {
        setBackground(aaRengi)
    },
    yanıtKutusu
) {
    setBackground(aaRengi)
    setBorder(BorderFactory.createEmptyBorder)
}

def yeniAraYüz() {
    araYüz.sil()
    araYüz = götür(-150, 0) -> Resim.arayüz(işlemPanosu)
    çiz(araYüz)
    yanıtKutusu.takeFocus()
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

import java.awt.event.{ KeyAdapter, KeyEvent }
yanıtKutusu.addKeyListener(new KeyAdapter {
    def yanıtaBak(x: Sayı) {
        if (x == yanıt) {
            yanıtKutusu.setForeground(Renk(0, 220, 0))
            doğrular += 1
            if (!bittiMi && yeterinceSüreKaldıMı) {
                schedule(0.3) {
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

    def yanıtVerdiMi(e: KeyEvent) = {
        yanıtKutusu.getText.length >= yanıtUzunluğu
    }

    override def keyPressed(e: KeyEvent) {
        if (e.getKeyCode == Kc.VK_ESCAPE) {
            e.consume()
            durdur()
            tümEkran() // zaten tüm ekran olduğu için bu komutla tüm ekrandan çıkar.. aç/kapa düğmesi gibi yani
        }
    }

    override def keyTyped(e: KeyEvent) {
        if (!e.getKeyChar.isDigit) {
            e.consume()
        }
    }

    override def keyReleased(e: KeyEvent) {
        if (yanıtVerdiMi(e)) {
            val x = yanıtKutusu.value
            yanıtaBak(x)
        }
        else {
            yanıtKutusu.setForeground(siyah)
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
