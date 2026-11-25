silVeSakla()
çıktıyıSil()
çıktıArtalanınıKur(siyah)
çıktıYazıRenginiKur(gri)
den yöney = Yöney[Sayı]()
satıryaz("Gelin bir yöney (vektör) kuralım ve elemanlarının çubuk grafiğini çizelim.")
satıryaz("Ögelerini aşağıda girer misin?")
dez n = sayıOku("Yöney kaç boyutlu olsun, yani kaç öğesi olacak?")
çıktıYazıRenginiKur(sarı)
için (i <- 1 to n) {
    dez e = sayıOku(s"$i. öğe nedir? (0 ve 20 arası olsun)")
    yöney = yöney :+ e
}
çıktıYazıRenginiKur(yeşil)
satıryaz(s"Girdiğin yöney: ${yöney.yazıYap("[", ",", "]")}")
satıryaz("Tuvalde bu yöneyin elemanlarından oluşan bir çubuk grafiği çizdik (10/1 ölçeğinde)")

tanım çubuk(n: Sayı) = Resim.dikdörtgen(30, n * 10)
dez çubuklar = yöney.işle { n => çubuk(n) }
eksenleriGöster()
ızgarayıGöster()
dez çubukGrafiği = Resim.diziYatay(çubuklar).boşluk(5)
çiz(çubukGrafiği)
yazılımcıkDüzenleyicisiniEtkinleştir()
