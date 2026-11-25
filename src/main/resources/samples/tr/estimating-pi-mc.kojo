/*
  π sayısını yaklaşık olarak bulmanın bir yolu da Monte Carlo metodu. 
  Bu yazılımcığı Massimo Maria Ghisalberti yazmış.
  Fikir aslında çok basit. 2x2 boyutlarında bir karenin içine yarıçapı 1 olan bir daire çizelim.
  Karenin alanı dört. Dairenin alanı ise π olur. 
  Şimdi birsürü rastgele nokta seçelim. Bunların kaç tanesi dairenin içine düşer? 
  Daha çok bilgi ve detay istersen, şuraya bak: https://academo.org/demos/estimating-pi-monte-carlo/
*/
tanım π(toplamNoktaSayısı: Sayı): Kesir = {
    tanım merkezdenUzaklık(x: Kesir, y: Kesir) = karekökü(karesi(x) + karesi(y))
    den daireninİçineDüşenNoktaSayısı = 0
    yinele(toplamNoktaSayısı) {
        dez (noktaX, noktaY) = (rastgeleKesir(1), rastgeleKesir(1))
        dez daireninİçineDüştüMü = merkezdenUzaklık(noktaX, noktaY) <= 1
        eğer (daireninİçineDüştüMü) daireninİçineDüşenNoktaSayısı += 1
    }
    // neden dörtle çarptık? Farkettiysen noktalar karenin dörtte birine düşüyor, üst sağ çeyreğine..
    4.0 * daireninİçineDüşenNoktaSayısı / toplamNoktaSayısı
}

satıryaz(s"100 rastgele nokta: ${π(100)}")
satıryaz(s"1000 rastgele nokta: ${π(1000)}")
satıryaz(s"10000 rastgele nokta: ${π(10000)}")
satıryaz(s"100000 rastgele nokta: ${π(100000)}")
satıryaz(s"1000000 rastgele nokta: ${π(1000000)}")
// satıryaz(s"10000000 rastgele nokta: ${π(10000000)}")
