// Copyright (C) 2015 Anusha Pant <anusha.pant@gmail.com>
// The contents of this file are subject to
// the GNU General Public License Version 3
//    http://www.gnu.org/copyleft/gpl.html
// Bülent Başaran Türkçe'ye çevirirken ufak tefek değişiklikler yaptı.

val oyunSüresi = 120
val enÇokKaçSatır = 10
val enÇokKaçSütun = 10

tümEkranTuval()
yaklaşmayaİzinVerme()

val evBoyu = 200 / enİrisi(enÇokKaçSatır, enÇokKaçSütun)
def arayüzTanımı(sayılar: (Sayı, Sayı, Sayı)) = Resim.diziDikey(
    götür(-ta.x - 300, ta.y + 60) -> Resim.arayüz(yanıtPenceresi),
    götür(ta.x + 50, -ta.y) -> evResmi(sayılar._1, sayılar._2, sayılar._3),
    götür(ta.x + 20, -ta.y - 240) * kalemRengi(siyah) -> Resim.yazı("Kaç ev var?")
)
def yeniArayüz(sayılar: (Sayı, Sayı, Sayı)) {
    arayüz.sil()
    arayüz = arayüzTanımı(sayılar)
    çiz(arayüz)
    yanıtPenceresi.takeFocus() // klayve girdisini üstüne alsın
}
var arayüz: Resim = Resim.yatay(1) // şimdilik

def ev() {
    kalemRenginiKur(siyah)
    boyamaRenginiKur(Renk(204, 102, 0))
    yinele(4) {
        ileri(evBoyu)
        sağ()
    }
    zıpla(evBoyu)
    sol()
    zıpla(5)
    boyamaRenginiKur(kırmızı)
    sağ(120)
    yinele(3) {
        ileri(evBoyu + 10)
        sağ(120)
    }
    sağ(60)
    zıpla(5)
    sol() // şimdi kuzeye bakıyor
    zıpla(-evBoyu)
}

def evResmi(satırSayısı: Sayı, sütunSayısı: Sayı, artıkSayısı: Sayı) = Resim {
    yinele(satırSayısı) {
        yinele(sütunSayısı) {
            ev()
            sağ()
            zıpla(evBoyu + 25)
            sol()
        }
        sol()
        zıpla(sütunSayısı * (evBoyu + 25))
        sol()
        zıpla(2.6 * evBoyu)
        sol(180)
    }
    yinele(artıkSayısı) {
        ev()
        sağ()
        zıpla(evBoyu + 25)
        sol()
    }
}

val artalanRengi = Renk(208, 144, 73)

var yanıt = 0
var yanıtUzunluğu = 0
var süreBittiMi = yanlış

def farklıBirSayı(n: Sayı, m: Sayı) = {
    def sayı = 2 + rastgele(m - 1)
    var n2 = 0
    do {
        n2 = sayı
    } while (n2 == n)
    n2
}

val yy = yazıyüzü("Sans Serif", 60)
val yanıtPenceresi = new ay.Yazıgirdisi(0) {
    // daha çok bilgi için, google: swing textfield api
    setFont(yy)
    setColumns(5)
    setHorizontalAlignment(ay.değişmez.merkez)
    setBackground(artalanRengi)
    setBorder(ay.çerçeveci.çizgiKenar(siyah))
}

def yeniSoru(s1: Sayı, s2: Sayı) = {
    val sayı1: Sayı = farklıBirSayı(s1, enÇokKaçSatır - 1)
    val sayı2: Sayı = farklıBirSayı(s2, enÇokKaçSütun)
    val sayı3: Sayı = rastgeleDiziden(1 to sayı2 - 1)
    val sayılar = (sayı1, sayı2, sayı3)
    yanıtıKur(sayılar)
    sayılar
}

def yanıtıKur(s: (Sayı, Sayı, Sayı)) = {
    yanıt = s._1 * s._2 + s._3
    yanıtPenceresi.setText("")
    yanıtUzunluğu = yanıt.toString.length
}

var doğruYanıtSayısı = 0
var yanlışYanıtSayısı = 0

var sonSorununSorulduğuZaman = buAn
def yeterinceZamanVarMı = {
    val fark = buAn - sonSorununSorulduğuZaman
    if (fark > 100) {
        sonSorununSorulduğuZaman = buAn
        doğru
    }
    else yanlış
}

val s = yeniSoru(0, 0)
import java.awt.event.{ KeyAdapter, KeyEvent }
yanıtPenceresi.addKeyListener(new KeyAdapter {
    var sayılarAnımsa = s
    def yanıtıDenetle(x: Sayı) {
        if (x == yanıt) {
            yanıtPenceresi.setForeground(yeşil)
            doğruYanıtSayısı += 1
            if (!süreBittiMi && yeterinceZamanVarMı) {
                sırayaSok(0.3) {
                    val sayılar = yeniSoru(sayılarAnımsa._1, sayılarAnımsa._2)
                    sayılarAnımsa = sayılar
                    yeniArayüz(sayılar)
                    yanıtPenceresi.setForeground(siyah)
                }
            }
        }
        else {
            yanıtPenceresi.setForeground(kırmızı)
            yanlışYanıtSayısı += 1
            if (!süreBittiMi) {
                yeniArayüz(sayılarAnımsa)
            }
        }
    }

    def yanıtHazırMı(e: KeyEvent) = {
        yanıtPenceresi.getText.length >= yanıtUzunluğu
    }

    // escape tuşuna basınca oyuna son verelim:
    override def keyPressed(e: KeyEvent) {
        if (e.getKeyCode == tuşlar.VK_ESCAPE) {
            e.consume()
            oyunSüresineBak(doğru)
            durdur()
            tümEkranTuval()  // tüm ekran modunu kapatalım
        }  // t tuşu yazılımcığımızı test etmek için:
        else if (e.getKeyCode == tuşlar.VK_T) {
            val sayılar = (enÇokKaçSatır, enÇokKaçSütun, enÇokKaçSütun)
            yanıtıKur(sayılar)
            yeniArayüz(sayılar)
        } // büyük boşluk tuşuna basarak soruyu değiştirebiliriz:
        else if (e.getKeyCode == tuşlar.VK_SPACE) {
            val sayılar = yeniSoru(0, 0)
            yanıtıKur(sayılar)
            yeniArayüz(sayılar)
        }
    }

    // sayı dışındaki girdileri yok sayalım
    override def keyTyped(e: KeyEvent) {
        if (!e.getKeyChar.isDigit) {
            e.consume()
        }
    }

    override def keyReleased(e: KeyEvent) {
        if (yanıtHazırMı(e)) {
            val x = yanıtPenceresi.value
            yanıtıDenetle(x)
        }
        else {
            yanıtPenceresi.setForeground(siyah)
        }
    }
})

def mesajıYaz(m: Yazı, renk: Renk) {
    val yç = yazıÇerçevesi(m, 30)
    val resim = kalemRengi(renk) * götür(ta.x + (ta.eni - yç.width) / 2, 0) ->
        Resim.yazı(m, 30)
    çiz(resim)
}

def oyunSüresineBak(escapeTuşunaBasıldıMı: İkil = yanlış) {
    def sonuç(dy: Sayı, yy: Sayı) = dy - yy
    val geçenSüreResmi = götür(ta.x + 10, ta.y + 50) -> Resim.yazıRenkli(
        geçenSüre,
        20, mavi)
    çiz(geçenSüreResmi)
    geçenSüreResmi.girdiyiAktar(Resim.tuvalBölgesi)

    yineleSayaçla(1000) {
        geçenSüre += 1
        geçenSüreResmi.güncelle(geçenSüre)

        if (geçenSüre == oyunSüresi || escapeTuşunaBasıldıMı) {
            süreBittiMi = doğru
            val durum = if (escapeTuşunaBasıldıMı) s"Oyun $geçenSüre saniye sonra durduruldu."
            else "Oyun bitti!"
            val mesaj = s"""      $durum
            |Doğrular: $doğruYanıtSayısı
            |Yanlışlar: $yanlışYanıtSayısı
            |Skor: ${sonuç(doğruYanıtSayısı, yanlışYanıtSayısı)}
            """
            arayüz.sil()
            mesajıYaz(mesaj.stripMargin, siyah) // stripMargin: boşlukları temizleme metodu
            durdur()
        }
    }
}

silVeSakla()
artalanıKur(artalanRengi)
val ta = tuvalAlanı

yeniArayüz(s)
var geçenSüre = 0
oyunSüresineBak()
sırayaSok(1) {
    yanıtPenceresi.takeFocus() // klayve girdisini üstüne alsın
}
