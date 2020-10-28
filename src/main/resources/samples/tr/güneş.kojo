çıktıyıSil()
satıryaz("Haydi bir güneş çizelim!")

/* Sayıların üstünde farenin sağ tuşuna tıklayın.
 * Fare yerine dokunuşlu bir bilgisayarda (touchpad), iki parmakla tıklayın.
 * Sayının değeri değiştikçe tuvaldeki çizim nasıl
 * değisiyor görebilirsiniz...
 * Aynı şekilde renkleri de değiştirebiliriz. 
 * Ama şimdilik Türkçe bilmiyor. Onun için aşağıda sarı yerine Color'a tıkla.
 */

val kolSayısı=95
val kolUzunluğu=336
val güzelRenk=false
val kolRengi=if(güzelRenk) Color(255, 208, 0) else sarı

sil()
görünmez()
setSpeed(superFast)  // Türkçesi yakında çıkacak!

kalemRenginiKur(kolRengi)
val açı=360.0/kolSayısı
yinele(kolSayısı) {
    ileri(kolUzunluğu)
    ileri(-kolUzunluğu)
    sağ(açı)
}
