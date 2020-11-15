silVeSakla()
// Renk paletini açmak için, İngilizce'yi seçin ve
// sonra da Color()'ın üstüne tıklayın (ama önce Ctrl tuşuna basıp tutun)
val ingilizce = doğru  // yanlış
val türkçe = !ingilizce
val sarı1 = if (türkçe) Renk(180, 111, 3)  else Color(180, 111, 3)
val sarı2 = if (türkçe) Renk(245, 236, 74) else Color(245, 236, 74)
arkaplanıKurYatay(sarı1, sarı2)
yineleKere(1 to 600) { e =>
    atla(random(600) - 300, random(400) - 200)
    kalemRenginiKur(Renk(random(180), random(180), random(180), random(100) + 100))
    dot(35)
}
