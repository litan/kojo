// Bütün ekranı kapla (eğer zaten tüm ekrandaysak, tüm ekrandan çıkartır)
tümEkranÇıktı()
silVeSakla()
çıktıyıSil()
çıktıArtalanınıKur(siyah)
çıktıYazıRenginiKur(gri)
satıryaz("Gelin bir yöney (vektör) kuralım. Öğelerini aşağıda girer misin?")
var yöney = Yöney[Sayı]()
val n = sayıOku("Yöney kaç boyutlu olsun, yani kaç öğesi olacak?")
çıktıYazıRenginiKur(sarı)
for (i <- 1 |-| n) {
    val e = sayıOku(s"$i. öğe nedir?")
    yöney = yöney :+ e
}
çıktıYazıRenginiKur(yeşil)
satıryaz(s"Girdiğin yöney: ${yöney.yazıYap("[", ",", "]")}")
satıryaz(f"    Ortalaması: ${yöney.topla.kesire / yöney.boyu}%.2f")
satıryaz(f"      Uzunluğu: ${karekökü(yöney.işle(x => x*x).topla)}%.2f")  // pisagor bu kadar basit! (8-)
