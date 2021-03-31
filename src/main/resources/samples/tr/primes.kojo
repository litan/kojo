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
    case Boş     => Boş
    case sayı :: gerisi => sayı :: elek(gerisi.filter { e => e % sayı != 0 })
}
elek((2 to 50).toList)

// (1b) Dizin kullanarak yazdığımız çözümü bir MiskinDizin (İngilizce'si LazyList)
// kullanarak yeniden yazalım
def elek2(sayılar: MiskinDizin[Sayı]): MiskinDizin[Sayı] = sayılar match {
    case sayı #:: gerisi => sayı #:: elek2(gerisi.filter { e => e % sayı != 0 })
}
elek2(MiskinDizin.sayalım(2)).take(15).toList

// (1c) En baştan MiskinDizin kullanarak da sade birşekilde çözebiliriz
def elek3(sayılar: MiskinDizin[Sayı]): MiskinDizin[Sayı] = {
    sayılar.head #:: elek3(sayılar.tail.filter { e => e % sayılar.head != 0 })
}
elek3(MiskinDizin.sayalım(2)).take(15).toList

// (2) MiskinDizin yöntemini kullanarak daha verimli bir yöntem de şudur:
val asallar: MiskinDizin[Sayı] = 2 #:: MiskinDizin.sayalım(3, 2).filter { asalMı }
def asalMı(n: Sayı) = asallar.takeWhile { j => j * j <= n }.forall { p => n % p != 0 }
asallar.take(15).toList
