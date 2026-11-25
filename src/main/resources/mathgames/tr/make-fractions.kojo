// Copyright (C) 2015 Anusha Pant <anusha.pant@gmail.com>
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

den kesirÇizimi = Resim.yatay(0) // şimdilik
dez düğme = ay.Düğme("Kesiri çizelim") {
    dene {
        kesirÇizimi.sil()
        ondalıkVeYüzde.sil()
        kesirÇizimi = kesiriÇiz(girdi1.değeri.sayıya, girdi2.değeri.sayıya)
        çiz(kesirÇizimi)
        düğme2.etkinliğiKur(doğru)
    }
    yakala {  // "dene" içinde bir yanılgı yani bir hata olunca burada yakalarız
        durum e: ÇalışmaSırasıKuralDışı => // Java ve Scala'da en genel yanılgı, kural dışı durum türü
            eğer (girdi1.değeri.boşMu) {
                satıryaz("Pay boş!")
            }
            yoksa eğer (girdi1.değeri.sayıyaBelki.boşMu) {
                satıryaz("Pay tam sayı değil.")
            }
            yoksa eğer (girdi2.değeri.boşMu) {
                satıryaz("Payda boş!")
            }
            yoksa eğer (girdi2.değeri.sayıyaBelki.boşMu) {
                satıryaz("Payda tam sayı değil.")
            }
            yoksa eğer (girdi2.değeri.sayıya == 0) {
                satıryaz("Payda 1 ya da daha büyük olmalı")
            }
            yoksa {
                satıryaz("Bir hata var!")
            }
    }
}

tanım ondalığıVeYüzdeyiYaz(pay: Kesir, payda: Kesir) = {
    den ondalık = pay / payda
    den yüzde = ondalık * 100
    Resim.dizi(
        götür(ta.x + 10, -ta.y - 50) -> Resim.arayüz(ay.Tanıt(f"Ondalık olarak: $ondalık%.2f")),
        götür(ta.x + 10, -ta.y - 70) -> Resim.arayüz(ay.Tanıt(f"Yüzde olarak: $yüzde%.2f"))
    )
}

den ondalıkVeYüzde = Resim.yatay(0) // şimdilik
dez düğme2: ay.Düğme = ay.Düğme("Kesiri ondalık ve yüzde olarak görelim") {
    dene {
        ondalıkVeYüzde.sil()
        ondalıkVeYüzde = ondalığıVeYüzdeyiYaz(girdi1.değeri.kesire, girdi2.değeri.kesire)
        çiz(ondalıkVeYüzde)
        düğme2.etkinliğiKur(yanlış)
        düğme.pencereİçindekiOdağıİste()
    }
    yakala {
        durum e: ÇalışmaSırasıKuralDışı =>  // 'case' yani durum şu ise: Java ve Scala'da en genel yanılgı, kural dışı durum türü
    }
}

tanım kesiriÇiz(pay1: Sayı, payda1: Sayı) = Resim {
    dez mavimsi = Renk(90, 199, 255)
    den pay = mutlakDeğer(pay1)
    den payda = mutlakDeğer(payda1)
    dez tam = eğer (pay <= payda) {
        0
    }
    yoksa {
        dez pay2 = pay
        pay = eğer (payda == 0) pay2 yoksa pay2 % payda
        eğer (payda == 0) pay1 yoksa pay1 / payda1
    }
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
    eğer (payda < 1) {
        konumuKur(-150, 0)
        tuvaleYaz("Payda 1 ya da daha büyük olmalı")
    } 
    yoksa {
        eğer (tam != 0) {
            konumuKur(-5, 160)
            tuvaleYaz(s"$tam")
            konumuKur(-5, 140)
            tuvaleYaz(eğer (tam > 0) "+" yoksa "-")
            konumuKur(0, 0)
        }
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
}

silVeSakla()
girdi1.girdiOdağıOl()  // klavye girdisini pay olarak okuyalım
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

düğme2.etkinliğiKur(yanlış)  // başta etkisiz olsun ikinci düğme
