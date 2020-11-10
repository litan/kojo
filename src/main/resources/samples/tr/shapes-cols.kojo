// Bu oldukça basit yazılımla bir kare çiziyoruz.
// Ama daha önemlisi, değişik renkler kullanmayı
// sayıları değiştirerek çizimlerin nasıl değiştiğini
// görmek için ne yapabileceğimizi öğreneceğiz

/* Aşağıdaki sayıların herhangi birinin üstünde farenin sağ tuşuna tıklayın.
 * Örneğin 4, 100 ya da 90. Fare yerine dokunuşlu bir bilgisayarda (touchpad), 
 * iki parmakla tıklayın. Sayının değeri değiştikçe tuvaldeki çizim nasıl
 * değisiyor görebilirsiniz...
 * Aynı şekilde renkleri de değiştirebiliriz. 
 * Ama Kojo şimdilik Türkçe renklerle yapamıyor bunu. Onun için aşağıda 
 * mavi yerine 'blue' sözcüğüne tıkla. */

sil()
hızıKur(çokHızlı)
// Farenin sağ tuşuyla blue sözcüğüne tıkla
// Ya da Ctrl tuşunu basık tutup sol tuşla da tıklayabilirsin
kalemRenginiKur(mavi)  // mavi yerine blue yaz!
// kalemRenginiKur(mavi)
boyamaRenginiKur(yeşil)  // yeşil yerine green yaz
yinele(4) {
    ileri(100)
    // Aşağıdaki 90 sayısına Ctrl+tıklayarak değişik açıları deneyebilirsin
    sağ(90)
}
