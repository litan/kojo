//#yükle tr/anaTanimlar
//#yükle tr/eTahta
//#yükle tr/araYuz
//#yükle tr/alfabeta

çıktıyıSil
dez çeşni = 0 // ya da 1
dez odaSayısı = 8
dez kimBaşlar = Siyah // Beyaz ya da Siyah başlayabilir. Seç :-)
dez bilgisayar = Siyah // Siyah ya da Beyaz oynar ya da Yok (yani oynamaz)

dez tahta = yeni ETahta(odaSayısı, kimBaşlar, çeşni)
dez bellek = yeni Bellek(tahta)
dez düzey = Usta
ABa.ustalık(düzey)
dez araYüz = yeni Arayüz(tahta, bellek, bilgisayar)
// 1) istersen bilgisayar çabucak bir oyunla başlayabilir
// araYüz.özdevin(0.02) 

// 2) Burada ise daha ciddi bir bilgisayar motorunu kullanıyoruz. 
// Epey çok zaman alıyor. Hamle başına 1, 3, 5 hatta 10 saniyeden çok bile 
// sürebilir. Hızlandırmak için Usta yerine Er, Çırak ya da Kalfa kurabilirsin:
/* 
zamanTut(s"$odaSayısı x $odaSayısı ustalık: $düzey") {
    araYüz.özdevinimliOyun(araYüz.abArama, 0.1)
}("sürdü")
*/
