// #include /robosim/tr/robot.kojo
// #include /robosim/tr/cevre.kojo

// Yüklediğimiz yazılımcıkları şuradan da okuyabilirsin:
//   https://github.com/litan/kojo/tree/master/src/main/resources/robosim/tr

silVeSakla()
//yaklaşmayaİzinVerme()

// Benzeşme alanı benim bilgisayarımda tuvale sığmadı. 
// Onun için biraz uzaklaşmakta fayda var:
yaklaş(0.6)
// ya da tüm ekran tuvale geç:
// tümEkran()

artalanıKur(renkler.khaki)
çiz(duvarlar) // duvarlar environment1.kojo adlı yazılımcık içinde tanımlanıyor

// duvarları biraz oynatmak ya da yeniden çizmek istersen, eksenleri ve gridi aç:
eksenleriGöster()
gridiGöster()

val robot = Robot(-400, -240, duvarlar)
robot.göster()

yineleDoğruysa(doğru) { // yani hep tekrar edecek...
  döngü()
}

// duvara gelince en açık yolu bularak ilerlesin
def döngü() {
    val u = robot.engeleUzaklık

    // bir engele yaklaşınca (ya da duvara çarpınca), önümüzü soldan sağa doğru tarayalım.
    // En açık yolu bulalım ve onda ilerleyelim.

    if (u > 6 && !robot.çarptıMı(duvarlar)) {
        robot.ileri(5000 / robot.hız)
    }
    else {
        // döne döne en açık yolu, yani engelin en uzakta olduğu açıyı bulalım
        var enİriUzaklık = 0.0 
        var yeniYöneDönüşSüresi = 0.0 // ona dönmek için geçen süreyi anımsayalım
        val dönüşAçısı = 90
        val toplamDönüşSüresi = 1000 * dönüşAçısı / robot.dönüşHızı
        val adımSayısı = 10
        val dönüşSüresi = toplamDönüşSüresi / adımSayısı
        var sağaDönelimMi = yanlış

        // soldan sağa tarayalım. Önce sol tarafı tarayalım
        yineleİçin(1 to adımSayısı) { sayı =>
            robot.sol(dönüşSüresi)
            val u = robot.engeleUzaklık
            if (u > enİriUzaklık) {
                enİriUzaklık = u
                yeniYöneDönüşSüresi = sayı * dönüşSüresi
            }
        }
        // tekrar öne bakalım
        robot.sağ(toplamDönüşSüresi)
        // şimdi de sağ tarafı tarayalım
        yineleİçin(1 to adımSayısı) { sayı =>
            robot.sağ(dönüşSüresi)
            val u = robot.engeleUzaklık
            if (u > enİriUzaklık) {
                sağaDönelimMi = doğru
                enİriUzaklık = u
                yeniYöneDönüşSüresi = sayı * dönüşSüresi
            }
        }
        
        if (sağaDönelimMi) {
            robot.sol(toplamDönüşSüresi - yeniYöneDönüşSüresi)
        }
        else {
            robot.sol(toplamDönüşSüresi)
            robot.sol(yeniYöneDönüşSüresi)
        }
        val uzaklık = enUfağı(40, enİriUzaklık)
        robot.ileri(1000 * uzaklık / robot.hız)
    }
}

