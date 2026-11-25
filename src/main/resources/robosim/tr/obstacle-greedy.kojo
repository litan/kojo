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

// duvara gelince rastgele sağa ya da sola baksın ve ilk bulduğu açık yolda ilerlesin
tanım döngü() {
    den u = robot.engeleUzaklık

    // Önümüzdeki engel uzaktaysa ( uzaklık >= 6 ) ona doğru gidelim.
    // Yoksa, yani önümüzdeki engel yakınsa, durup rastgele sağa ya da sola dönelim.
    // Yeni yöne göre önümüzdeki engel yeterince uzaksa ( uzaklık > 20 ), o yönde ilerleriz...
    // Yoksa aynı yönde dönmeye devam ederiz..

    eğer (u >= 6) {
        robot.ileri(50)
    }
    yoksa {
        dez sağaDönelimMi = rastgeleİkil
        yineleOlanaKadar(u > 20) {
            eğer (sağaDönelimMi) {
                robot.sağ(100)
            }
            yoksa {
                robot.sol(100)
            }
            u = robot.engeleUzaklık
        }
    }
}
