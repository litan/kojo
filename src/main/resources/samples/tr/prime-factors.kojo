// #worksheet           

kojoÇalışmaSayfalıBakışaçısınıKur()

// Bir önceki Asal Sayılar örneğinden asal sayıların sonu gelmeyen miskin dizinini burada kullanalım 
val asallar: MiskinDizin[Sayı] = 2 #:: MiskinDizin.sayalım(3, 2).filter { asalMı }
def asalMı(n: Sayı) = asallar.takeWhile { j => j * j <= n }.forall { p => n % p != 0 }

// Bir sayının bütün asal çarpanlarını bulalım (prime factors of a number) 
// Wikipedia'nın 'Fundamental theorem of arithmetic' adlı makalesine bakmak istersen iyi olur 
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
