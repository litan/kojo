// #worksheet

kojoÇalışmaSayfalıBakışaçısınıKur()
çıktıyıSil()

// Bir önceki Asal Sayılar örneğinden asal sayıların sonu gelmeyen miskin dizinini burada kullanalım
val asallar: MiskinDizin[Sayı] = 2 #:: MiskinDizin.sayalım(3, 2).ele { asalMı }
def asalMı(n: Sayı) = asallar.alDoğruKaldıkça { a => a * a <= n }.hepsiDoğruMu { a => n % a != 0 }
asallar.al(6).dizine

// Bir sayının bütün asal çarpanlarını bulalım (prime factors of a number)
// Wikipedia'nın 'Fundamental theorem of arithmetic' adlı makalesine bakmak ister misin?
def asalÇarpanlar(n: Sayı) = asallar.alDoğruKaldıkça(a => a <= n).ele { a => n % a == 0 }.dizine
asalÇarpanlar(40)

// Hem asal çarpanları hem de herbirinden kaç tane gerektiğini bulalım
def asalÇarpanlarınHerbiri(n: Sayı): Dizin[Sayı] = {
    if (n == 1) Boş
    else {
        val asalÇarpan = asallar.düşürDoğruKaldıkça { asal => n % asal != 0 }.başı // bunu daha verimli kılabilir miyiz?
        asalÇarpan :: asalÇarpanlarınHerbiri(n / asalÇarpan)
    }
}
asalÇarpanlarınHerbiri(40)

// Kaç tane asal sayı var? 9 tane var desek?
val deneyler = for (ilkKaçAsal <- 1 |-| 9) yield asallar.al(ilkKaçAsal).dizine
deneyler.herbiriİçin { d =>
    val y = d.işle(n => f"$n%2s") // her sayı iki basamaklı yazılsın (" 5" ve "11" gibi) ki hiza bozulmasın
    val çab = d.indirge(_ * _) + 1
    val (mesaj, kaçTane) =
        if (asalMı(çab)) ("", 1)
        else {
            val açLer = asalÇarpanlarınHerbiri(çab)
            (s"${açLer.yazıYap("= ", " x ", "")}", açLer.boyu)
        }
    satıryaz(f"${y.yazıYap("", " x ", "  + 1 = ")}%52s $çab%-9s $mesaj%-20s Daha büyük $kaçTane asal sayı bulduk")
}
