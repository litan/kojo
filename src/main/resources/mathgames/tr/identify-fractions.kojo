// Copyright (C) 2016 Anusha Pant <anusha.pant@gmail.com>
// The contents of this file are subject to
// the GNU General Public License Version 3 (http://www.gnu.org/copyleft/gpl.html)

// Bülent Başaran (ben@scala.org) Türkçe'ye çevirirken ufak tefek değişiklikler yaptı.

val yy = yazıyüzü("Sans Serif", 40)
val artalanRengi = Renk(255, 232, 181)

val girdi1 = new ay.Yazıgirdisi("") {
    // daha çok bilgi için, google: swing textfield api
    setFont(yy)
    setColumns(2)
    setHorizontalAlignment(ay.değişmez.merkez)
    setBackground(artalanRengi)
}
val girdi2 = new ay.Yazıgirdisi("") {
    setFont(yy)
    setColumns(2)
    setHorizontalAlignment(ay.değişmez.merkez)
    setBackground(artalanRengi)
}

def tıklayıncaYazıyıSil(yg: ay.Yazıgirdisi[Yazı]) {
    yg.addFocusListener(new java.awt.event.FocusAdapter {
        override def focusGained(e: java.awt.event.FocusEvent) {
            yg.setText("")
        }
    })
}
tıklayıncaYazıyıSil(girdi1)
tıklayıncaYazıyıSil(girdi2)

var pay = rastgele(6) + 2
var payda = rastgele(10) + 2
if (pay == payda) {
    pay = payda - 1
}
if (pay > payda) {
    pay = payda
    payda = pay + 2
}

def kesiriÇiz(a: Sayı, b: Sayı) = Resim {
    val mavimsi = Renk(90, 199, 255)
    var pay = a
    var payda = b
    def paydayıÇiz() {
        // paydaları ayıran çizgiler çemberin yarıçapı
        yinele(payda) {
            ileri(110)
            zıpla(-110)
            sağ(360.0 / payda)
        }
    }
    def payıÇiz() {
        boyamaRenginiKur(mavimsi)
        ileri(110)
        sağ()
        sağ((360.0 / payda) * pay, 110)
        sağ()
        ileri(110)
    }
    def çizim = {
        sağ(90)
        zıpla(110)
        sol(90)
        kalemRenginiKur(siyah)
        sol(360, 110)
        sol()
        zıpla(110)
        sağ()
        konumVeYönüBelleğeYaz()
        payıÇiz()
        konumVeYönüGeriYükle()
        paydayıÇiz()
        zıpla(-110)

        // sağ tarafa uzun bir kule çizelim
        boyamaRenginiKur(renksiz)
        sağ()
        zıpla(160)
        sol()
        yinele(2) {
            ileri(220)
            sağ()
            ileri(50)
            sağ()
        }
        // alttan pay/payda kısmını mavimsi renge boyayalım
        boyamaRenginiKur(mavimsi)
        yinele(2) {
            ileri(220.0 * pay / payda)
            sağ()
            ileri(50)
            sağ()
        }
        // katları (yatay çizgilerle) çizelim
        boyamaRenginiKur(renksiz)
        yinele(payda - 1) {
            ileri(220.0 / payda)
            sağ()
            ileri(50)
            zıpla(-50)
            sol()
        }
    }
    çizim
}

var kesirÇizimi = kesiriÇiz(pay, payda)
var etiket = ay.Tanıt("")
var yanıt = Resim.arayüz(etiket)
çiz(yanıt)
var etiket2 = ay.Tanıt("")
var yanıt2 = Resim.arayüz(etiket)
çiz(yanıt2)
var renk = siyah

def enİriBölen(pay: Sayı, payda: Sayı): Sayı = {
    kmath.hcf(pay, payda) // highest common factor yani en iri ortak bölen
}

val düğme = ay.Düğme("Doğru mu?") {
    yanıt.sil()
    yanıt2.sil()
    if (girdi1.value != "" && girdi2.value != "") {
        val ortakBölen = enİriBölen(pay, payda)
        belirt(pay <= payda, "Pay paydan büyük olmamalı")
        val sadePay = pay / ortakBölen
        val sadePayda = payda / ortakBölen
        val g1 = girdi1.value.toInt // oyuncunun girdisi Sayı olarak
        val g2 = girdi2.value.toInt
        val o2 = enİriBölen(g1, g2)
        if (g1 == sadePay && g2 == sadePayda) {
            etiket = ay.Tanıt("Doğru.")
            renk = Renk(0, 143, 0) // koyu yeşilimsi
            etiket2 = ay.Tanıt(" ")
        }
        else if (g1 / o2 == sadePay && g2 / o2 == sadePayda) {
            etiket = ay.Tanıt("Doğru ama sade değil.")
            renk = turuncu
            etiket2 = ay.Tanıt(s"Bu oranı $sadePay / $sadePayda olarak yazalım")
        }
        else {
            etiket = ay.Tanıt("Yanlış.")
            renk = kırmızı
            etiket2 = ay.Tanıt(" ")
        }
    }
    etiket.setForeground(renk)
    etiket.setFont(Font("Serif", 20))
    yanıt = Resim.arayüz(etiket)
    yanıt2 = Resim.arayüz(etiket2)
    çiz(götür(ta.x + 20, -ta.y - 40) -> yanıt)
    çiz(götür(ta.x + 20, -ta.y - 80) -> yanıt2)
}

val düğme2 = Button("Yeni soru") {
    kesirÇizimi.sil()
    girdi1.value = ""
    girdi2.value = ""
    yanıt.sil()
    yanıt2.sil()
    pay = rastgele(4) + 2
    payda = rastgele(8) + 2
    if (pay == payda) {
        pay = payda - 1
    }
    if (pay > payda) {
        pay = payda
        payda = pay + 2
    }
    kesirÇizimi = kesiriÇiz(pay, payda)
    çiz(kesirÇizimi)
}

silVeSakla()
girdi1.takeFocus() // klavye girdisini pay olarak okuyalım
val ta = tuvalAlanı
çiz(
    götür(ta.x, ta.y) -> Resim.arayüz(
        ay.Satır(
            ay.Sütun(
                girdi1,
                ay.BölmeÇizgisi(mavi, doğru),
                girdi2
            ),
            düğme,
            düğme2
        )
    )
)

çiz(kesirÇizimi)
