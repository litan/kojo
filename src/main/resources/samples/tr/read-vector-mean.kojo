// Bütün ekranı kapla (eğer zaten tüm ekrandaysak, tüm ekrandan çıkartır)
toggleFullScreenOutput()
çıktıyıSil()
// çıktı ekranının arka plan ve yazı rengini değiştir
setOutputBackground(siyah)
setOutputTextColor(gri)
println("Gelin bir yöney (vektör) kuralım. Ögelerini aşağıda girer misin?")
var vec = Vector[Int]()
val n = readInt("Yöney kaç boyutlu olsun, yani kaç ögesi olacak?")
setOutputTextColor(sarı)
for (i <- 1 to n) {
    val e = readInt(s"$i. öğe nedir?")
    vec = vec :+ e
}
setOutputTextColor(green)
satıryaz(s"Girdiğin yöney: ${vec.mkString("[", ",", "]")}")
satıryaz(f"    Ortalaması: ${vec.sum.toDouble / vec.size}%.2f")
satıryaz(f"      Uzunluğu: ${math.sqrt(vec.map(x => x*x).sum)}%.2f")  // pisagor bu kadar basit! (8-)
