silVeSakla()
çıktıyıSil()
çıktıArtalanınıKur(siyah)
çıktıYazıRenginiKur(gri)
var yöney = Yöney[Sayı]()
satıryaz("Gelin bir yöney (vektör) kuralım ve elemanlarının çubuk grafiğini çizelim.")
satıryaz("Ögelerini aşağıda girer misin?")
val n = sayıOku("Yöney kaç boyutlu olsun, yani kaç öğesi olacak?")
çıktıYazıRenginiKur(sarı)
for (i <- 1 to n) {
    val e = sayıOku(s"$i. öğe nedir? (0 ve 20 arası olsun)")
    yöney = yöney :+ e
}
çıktıYazıRenginiKur(yeşil)
satıryaz(s"Girdiğin yöney: ${yöney.yazıYap("[", ",", "]")}")
satıryaz("Tuvalde bu yöneyin elemanlarından oluşan bir çubuk grafiği çizdik (10/1 ölçeğinde)")

def çubuk(n: Sayı) = Resim.dikdörtgen(30, n * 10)
val çubuklar = yöney.işle { n => çubuk(n) }
eksenleriGöster()
gridiGöster()
val çubukGrafiği = Resim.diziYatay(çubuklar).boşluk(5)
çiz(çubukGrafiği)
yazılımcıkDüzenleyicisiniEtkinleştir()
