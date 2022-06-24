// #worksheet (satırın başındaki bir komut. Bu yazılımcığı çalışma sayfası olarak çalıştırıyor)

kojoÇalışmaSayfalıBakışaçısınıKur()
// Bu çalışma sayfasından çıkmak için Pencere menüsündeki
// Varsayılan Bakışaçısı (Daha Büyük Düzenleyiciyle) komuduna tıklayabilirsin.
// Kojo'yu başlangıçtaki konuma getirir.

// Asal sayıları sen de ilginç buluyor musun?
// Bak şu yazıyı benim üniversitede çok sevdiğim matematik hocam yazmış.
// Sen de beğenirsin umarım:
//     https://bilimteknik.tubitak.gov.tr/makale/kac-tane-asal-sayi-var

// Haydi gel asal sayıları bulmanın bir kaç yöntemini görelim

// (1) Erastosthenes'in eleği çok eski ama o kadar da becerikli bir metod
// Eskiler kalbur da derler. Evvel zaman içinde, kalbur saman içinde!
// (1a) Dizin'lerle yazalım ilk önce (İngilizce'de List)
def elek(sayılar: Dizin[Sayı]): Dizin[Sayı] = sayılar match {
    case Boş            => Boş
    case sayı :: gerisi => sayı :: elek(gerisi.ele { e => e % sayı != 0 })
}
elek((2 |-| 50).dizine)

// (1b) Dizin kullanarak yazdığımız çözümü bir MiskinDizin (İngilizce'si LazyList)
// kullanarak yeniden yazalım
def elek2(sayılar: MiskinDizin[Sayı]): MiskinDizin[Sayı] = sayılar match {
    case sayı #:: gerisi => sayı #:: elek2(gerisi.ele { e => e % sayı != 0 })
}
elek2(MiskinDizin.sayalım(2)).al(15).dizine

// (1c) En baştan MiskinDizin kullanarak da sade birşekilde çözebiliriz
def elek3(sayılar: MiskinDizin[Sayı]): MiskinDizin[Sayı] = {
    sayılar.başı #:: elek3(sayılar.kuyruğu.ele { e => e % sayılar.başı != 0 })
}
elek3(MiskinDizin.sayalım(2)).al(15).dizine

// (2) MiskinDizin yöntemini kullanarak daha verimli bir yöntem de şudur:
val asallar: MiskinDizin[Sayı] = 2 #:: MiskinDizin.sayalım(3, 2).ele { asalMı }
def asalMı(n: Sayı) = asallar.alDoğruKaldıkça { j => j * j <= n }.hepsiİçinDoğruMu { p => n % p != 0 }
asallar.al(15).dizine
