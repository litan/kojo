// #yükle /robosim/tr/robot.kojo
// #yükle /robosim/tr/cevre.kojo

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
çiz(duvarlar) // duvarlar cevre.kojo adlı yazılımcık içinde tanımlanıyor

// duvarları biraz oynatmak ya da yeniden çizmek istersen, şunlar faydalı olur:
eksenleriGöster()
ızgarayıGöster()

dez robot = Robot(-400, -240, duvarlar)
robot.göster()

yineleDoğruysa(doğru) { // yani hep tekrar edecek...
  döngü()
}

// duvara gelince en açık yolu bularak ilerlesin
tanım döngü() {
    dez u = robot.engeleUzaklık

    // bir engele yaklaşınca (ya da duvara çarpınca), önümüzü soldan sağa doğru tarayalım.
    // En açık yolu bulalım ve onda ilerleyelim.

    eğer (u > 6 && !robot.çarptıMı(duvarlar)) {
        robot.ileri(5000 / robot.hız)
    }
    yoksa {
        // döne döne en açık yolu, yani engelin en uzakta olduğu açıyı bulalım
        den enİriUzaklık = 0.0 
        den yeniYöneDönüşSüresi = 0.0 // ona dönmek için geçen süreyi anımsayalım
        dez dönüşAçısı = 90
        dez toplamDönüşSüresi = 1000 * dönüşAçısı / robot.dönüşHızı
        dez adımSayısı = 10
        dez dönüşSüresi = toplamDönüşSüresi / adımSayısı
        den sağaDönelimMi = yanlış

        // soldan sağa tarayalım. Önce sol tarafı tarayalım
        yineleİçin(1 to adımSayısı) { sayı =>
            robot.sol(dönüşSüresi)
            dez u = robot.engeleUzaklık
            eğer (u > enİriUzaklık) {
                enİriUzaklık = u
                yeniYöneDönüşSüresi = sayı * dönüşSüresi
            }
        }
        // tekrar öne bakalım
        robot.sağ(toplamDönüşSüresi)
        // şimdi de sağ tarafı tarayalım
        yineleİçin(1 to adımSayısı) { sayı =>
            robot.sağ(dönüşSüresi)
            dez u = robot.engeleUzaklık
            eğer (u > enİriUzaklık) {
                sağaDönelimMi = doğru
                enİriUzaklık = u
                yeniYöneDönüşSüresi = sayı * dönüşSüresi
            }
        }
        
        eğer (sağaDönelimMi) {
            robot.sol(toplamDönüşSüresi - yeniYöneDönüşSüresi)
        }
        yoksa {
            robot.sol(toplamDönüşSüresi)
            robot.sol(yeniYöneDönüşSüresi)
        }
        dez uzaklık = enUfağı(40, enİriUzaklık)
        robot.ileri(1000 * uzaklık / robot.hız)
    }
}

