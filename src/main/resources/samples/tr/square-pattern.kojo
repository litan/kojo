sil()
hızıKur(hızlı) // kaplumbamızı hızlandıralım
kalemRenginiKur(gri)

// Şeffaf bir kırmızı renkle başlıyalım
// Renk işlevi dört sayı girdisi alır: kırmızı, yeşil, mavi, saydamlık.
// Hepsi de 0..255 arasında olmalılar.
den renk = Renk(255, 0, 0, 150)
yinele(15) {
    boyamaRenginiKur(renk)
    yinele(4) {
        ileri(100)
        sağ(90)
    }
    // rengi biraz değiştirelim. Çevir yönteminin girdisi bir açı
    renk = renk.çevir(360 / 15)
    sağ(360 / 15)
}
