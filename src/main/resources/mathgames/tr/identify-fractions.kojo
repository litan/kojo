// Copyright (C) 2016 Anusha Pant <anusha.pant@gmail.com>
// The contents of this file are subject to
// the GNU General Public License Version 3 (http://www.gnu.org/copyleft/gpl.html)

// Bülent Başaran (ben@scala.org) Türkçe'ye çevirirken ufak tefek değişiklikler yaptı.

dez yy = Yazıyüzü("Sans Serif", 40)
dez artalanRengi = Renk(255, 232, 181)

dez girdi1 = yeni ay.Yazıgirdisi("") {
    yazıYüzünüKur(yy)
    sütunSayısınıKur(2)
    yatayDüzeniKur(ay.değişmez.merkez)
    artalanıKur(artalanRengi)
}
dez girdi2 = yeni ay.Yazıgirdisi("") {
    yazıYüzünüKur(yy)
    sütunSayısınıKur(2)
    yatayDüzeniKur(ay.değişmez.merkez)
    artalanıKur(artalanRengi)
}

tanım tıklayıncaYazıyıSil(yg: ay.Yazıgirdisi[Yazı]) {
    yg.odakDinleyiciEkle(yeni ay.olay.OdakUyarlayıcısı {
        // odağı kazanınca yazıyı siliverelim
        baskın tanım focusGained(o: ay.olay.OdakOlayı) {
            yg.yazıyıKur("")
        }
    })
}
tıklayıncaYazıyıSil(girdi1)
tıklayıncaYazıyıSil(girdi2)

den pay = rastgele(6) + 2
den payda = rastgele(10) + 2
eğer (pay == payda) {
    pay = payda - 1
}
eğer (pay > payda) {
    pay = payda
    payda = pay + 2
}

tanım kesiriÇiz(a: Sayı, b: Sayı) = Resim {
    dez mavimsi = Renk(90, 199, 255)
    den pay = a
    den payda = b
    tanım paydayıÇiz() {
        // paydaları ayıran çizgiler çemberin yarıçapı
        yinele(payda) {
            ileri(110)
            zıpla(-110)
            sağ(360.0 / payda)
        }
    }
    tanım payıÇiz() {
        boyamaRenginiKur(mavimsi)
        ileri(110)
        sağ()
        sağ((360.0 / payda) * pay, 110)
        sağ()
        ileri(110)
    }
    tanım çizim = {
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

den kesirÇizimi = kesiriÇiz(pay, payda)
den etiket = ay.Tanıt("")
den yanıt = Resim.arayüz(etiket)
çiz(yanıt)
den etiket2 = ay.Tanıt("")
den yanıt2 = Resim.arayüz(etiket)
çiz(yanıt2)
den renk = siyah

dez düğme = ay.Düğme("Doğru mu?") {
    yanıt.sil()
    yanıt2.sil()
    eğer (girdi1.value.toIntOption.isDefined && girdi2.value.toIntOption.isDefined) {
        dez ortakBölen = enİriOrtakPayda(pay, payda)
        belirt(pay <= payda, "Pay paydan büyük olmamalı")
        dez sadePay = pay / ortakBölen
        dez sadePayda = payda / ortakBölen
        dez g1 = girdi1.değeri.sayıya // oyuncunun girdisi Sayı olarak
        dez g2 = girdi2.değeri.sayıya
        dez o2 = enİriOrtakPayda(g1, g2)
        eğer (g1 == sadePay && g2 == sadePayda) {
            etiket = ay.Tanıt("Doğru.")
            renk = Renk(0, 143, 0) // koyu yeşilimsi
            etiket2 = ay.Tanıt(" ")
        }
        yoksa eğer (g1 / o2 == sadePay && g2 / o2 == sadePayda) {
            etiket = ay.Tanıt("Doğru ama sade değil.")
            renk = turuncu
            etiket2 = ay.Tanıt(s"Bu oranı $sadePay / $sadePayda olarak yazalım")
        }
        yoksa {
            etiket = ay.Tanıt("Yanlış.")
            renk = kırmızı
            etiket2 = ay.Tanıt(" ")
        }
        etiket.önalanıKur(renk)
    }
    yoksa {
        etiket = ay.Tanıt("Pay ve payda tam sayı olmalı.")
        etiket.önalanıKur(kırmızı)
    }
    etiket.yazıYüzünüKur(yazıyüzü("Serif", 20))
    yanıt = Resim.arayüz(etiket)
    yanıt2 = Resim.arayüz(etiket2)
    çiz(götür(ta.x + 20, -ta.y - 40) -> yanıt)
    çiz(götür(ta.x + 20, -ta.y - 80) -> yanıt2)
}

dez düğme2 = ay.Düğme("Yeni soru") {
    kesirÇizimi.sil()
    girdi1.yazıyıKur("")
    girdi2.yazıyıKur("")
    yanıt.sil()
    yanıt2.sil()
    pay = rastgele(4) + 2
    payda = rastgele(8) + 2
    eğer (pay == payda) {
        pay = payda - 1
    }
    eğer (pay > payda) {
        pay = payda
        payda = pay + 2
    }
    kesirÇizimi = kesiriÇiz(pay, payda)
    çiz(kesirÇizimi)
}

silVeSakla()
girdi1.girdiOdağıOl() // klavye girdisini pay olarak okuyalım
dez ta = tuvalAlanı
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
