// Bütün ekranı kapla (eğer zaten tüm ekrandaysak, tüm ekrandan çıkartır)
tümEkranÇıktı()
silVeSakla()
çıktıyıSil()
çıktıArtalanınıKur(siyah)
çıktıYazıRenginiKur(gri)
satıryaz("Gelin bir yöney (vektör) kuralım. Öğelerini aşağıda girer misin?")
den yöney = Yöney[Sayı]()
dez n = sayıOku("Yöney kaç boyutlu olsun, yani kaç öğesi olacak?")
çıktıYazıRenginiKur(sarı)
için (i <- 1 |-| n) {
    dez e = sayıOku(s"$i. öğe nedir?")
    yöney = yöney :+ e
}
çıktıYazıRenginiKur(yeşil)
satıryaz(s"Girdiğin yöney: ${yöney.yazıYap("[", ",", "]")}")
satıryaz(f"    Ortalaması: ${yöney.topla.kesire / yöney.boyu}%.2f")
satıryaz(f"      Uzunluğu: ${karekökü(yöney.işle(x => x*x).topla)}%.2f")  // pisagor bu kadar basit! (8-)
