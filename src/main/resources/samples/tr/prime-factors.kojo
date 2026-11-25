// #çalışmasayfası

kojoÇalışmaSayfalıBakışaçısınıKur()
çıktıyıSil()

// Bir önceki Asal Sayılar örneğinden asal sayıların sonu gelmeyen miskin dizinini burada kullanalım
dez asallar: MiskinDizin[Sayı] = 2 #:: MiskinDizin.sayalım(3, 2).ele { asalMı }
tanım asalMı(n: Sayı) = asallar.alDoğruKaldıkça { a => a * a <= n }.hepsiDoğruMu { a => n % a != 0 }
asallar.al(6).dizine

// Bir sayının bütün asal çarpanlarını bulalım (prime factors of a number)
// Wikipedia'nın 'Fundamental theorem of arithmetic' adlı makalesine bakmak ister misin?
tanım asalÇarpanlar(n: Sayı) = asallar.alDoğruKaldıkça(a => a <= n).ele { a => n % a == 0 }.dizine
asalÇarpanlar(40)

// Hem asal çarpanları hem de herbirinden kaç tane gerektiğini bulalım
tanım asalÇarpanlarınHerbiri(n: Sayı): Dizin[Sayı] = {
    eğer (n == 1) Boş
    yoksa {
        dez asalÇarpan = asallar.düşürDoğruKaldıkça { asal => n % asal != 0 }.başı // bunu daha verimli kılabilir miyiz?
        asalÇarpan :: asalÇarpanlarınHerbiri(n / asalÇarpan)
    }
}
asalÇarpanlarınHerbiri(40)

// Kaç tane asal sayı var? 9 tane den desek?
dez deneyler = için (ilkKaçAsal <- 1 |-| 9) ver asallar.al(ilkKaçAsal).dizine
deneyler.herbiriİçin { d =>
    dez y = d.işle(n => f"$n%2s") // her sayı iki basamaklı yazılsın (" 5" ve "11" gibi) ki hiza bozulmasın
    dez çab = d.indirge(_ * _) + 1
    dez (mesaj, kaçTane) =
        eğer (asalMı(çab)) ("", 1)
        yoksa {
            dez açLer = asalÇarpanlarınHerbiri(çab)
            (s"${açLer.yazıYap("= ", " x ", "")}", açLer.boyu)
        }
    satıryaz(f"${y.yazıYap("", " x ", "  + 1 = ")}%52s $çab%-9s $mesaj%-20s Daha büyük $kaçTane asal sayı bulduk")
}
