sil()
hızıKur(hızlı)
kalemRenginiKur(gri)
// Biraz şeffaf bır sarıyla başlayalım. Sarı = Kırmızı + Yeşil (Mavi yok)
// Renk işlevi dört sayı girdisi alır:
// kırmızı, yeşil, mavi, saydamlık.
// Hepsi de 0..255 arasında olmalılar. 
den renk = Renk(255, 255, 0, 127)
dez kaçTane = 18 // 18 tane beşgen çizelim. Bu sayıyı değiştirmeyi dene!
yinele(kaçTane) {
    boyamaRenginiKur(renk)
    yinele(5) {
        ileri(100)
        sağ(72)
    }
    dez birSonraki = 360 / kaçTane // bir sonraki açı ve renk için
    renk = renk.çevir(birSonraki)
    sağ(birSonraki)
}
