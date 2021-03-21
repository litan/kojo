// #worksheet

kojoÇalışmaSayfalıBakışaçısınıKur()
çıktıyıSil()

// Bir önceki Asal Sayılar örneğinden asal sayıların sonu gelmeyen miskin dizinini burada kullanalım
val asallar: MiskinDizin[Sayı] = 2 #:: MiskinDizin.sayalım(3, 2).filter { asalMı }
def asalMı(n: Sayı) = asallar.takeWhile { j => j * j <= n }.forall { p => n % p != 0 }

// Bir sayının bütün asal çarpanlarını bulalım (prime factors of a number)
// Wikipedia'nın 'Fundamental theorem of arithmetic' adlı makalesine bakmak ister misin?
def asalÇarpanlar(n: Sayı) = asallar.takeWhile(p => p <= n).filter { p => n % p == 0 }.toList
asalÇarpanlar(40)

// Hem asal çarpanları hem de herbirinden kaç tane gerektiğini bulalım
def asalÇarpanlarınHerbiri(n: Sayı): Dizin[Sayı] = {
    if (n == 1) Boş
    else {
        val asalÇarpan = asallar.dropWhile { asal => n % asal != 0 }.head // bunu daha verimli kılabilir miyiz?
        asalÇarpan :: asalÇarpanlarınHerbiri(n / asalÇarpan)
    }
}
asalÇarpanlarınHerbiri(40)

// Kaç tane asal sayı var?
val deneyler = for (ilkKaçAsal <- 1 to 6) yield asallar.take(ilkKaçAsal).toList
deneyler.foreach{ d =>
    val y = d.map(n => f"$n%2s") // her sayı iki basamaklı yazılsın (" 5" ve "11" gibi) ki hiza bozulmasın 
    val çab = d.reduce(_ * _) + 1
    val mesaj = if (asalMı(çab)) "            Yeni bir asal sayı bulduk" else s"${asalÇarpanlarınHerbiri(çab).mkString("= ", " x ", "  Yeni iki asal bulduk")}"
    satıryaz(f"${y.mkString("", " x ", "  + 1 = ")}%40s $çab%-5s $mesaj%-40s")
}
