/* Bu örnekte L-yapılarına bakacağız. L ne demek dersen, Lindenmayer adlı
 * Macaristan doğumlu bir bilginin icadı olduğu için adını L-yapısı olarak
 * kısaltıyoruz. İcatın doğum tarihinde tercüman -1 yaşındaydı diyerek bir
 * ipucu versem ne dersin? Daha fazla bilgi için şuna bakabilirsin:
 *     https://en.wikipedia.org/wiki/Aristid_Lindenmayer
 *
 * Bir L-yapısı pekçok biçim üretmek için kullanılabilir.
 * Değişik biçimler oluşturmak için yazılımcığın sonuna doğru
 * tanımlanmış olan iki değişmez değeri değiştirmeyi deneyebilirsin.
 *     % seçim -- değişik bir örnek seçmek için kullan
 *     % nesil -- L-yapısının kaç nesil çalışacağını belirler
 *
 * Daha çok bilgi için şu sayfa bakabilir ve gerekirse Google tercümanından
 * geçirebilirsin:
 *     http://lalitpant.blogspot.in/2012/05/playing-with-l-systems-in-kojo.html
*/

durum sınıf LYapısı(
    belit:        Yazı,
    açı:          Kesir,
    uzunluk:      Sayı  = 100,
    büyütmeOranı: Kesir = 0.6
)(kurallar: Bölümselİşlev[Harf, Yazı]) {
    den kuram = belit
    den nesil = 0
    tanım evir() {
        nesil += 1
        kuram = kuram.işle { h =>
            // h harfi için tanımlanmışsa, yani h kuralı varsa:
            eğer (kurallar.tanımlıMı(h)) kurallar(h) yoksa h
            // dik çizgilerin yerine nesil sayısını koy:
        }.yazıYap.değiştirHepsini("""\|""", nesil.yazıya)
    }

    tanım çiz() {
        tanım sayıMı(h: Harf) = Harf.sayıMı(h)
        dez üretilmişSayı = yeni EsnekYazı
        tanım düzGidebilir() {
            eğer (üretilmişSayı.doluMu) {
                dez n = üretilmişSayı.sayıya
                üretilmişSayı.sil()
                ileri(uzunluk * kuvveti(büyütmeOranı, n))
            }
        }
        kuram.herbiriİçin { c =>
            eğer (!sayıMı(c)) {
                düzGidebilir()
            }

            c eşle {
                durum 'F' => ileri(uzunluk)
                durum 'f' => ileri(uzunluk)
                durum 'G' =>
                    kalemiKaldır(); ileri(uzunluk); kalemiİndir()
                durum '['            => konumVeYönüBelleğeYaz()
                durum ']'            => konumVeYönüGeriYükle()
                durum '+'            => sağ(açı)
                durum '-'            => sol(açı)
                durum s eğer sayıMı(s) => üretilmişSayı.ekle(s) // sona ekleyelim
                durum _              =>
            }
        }
        düzGidebilir()
    }
}

dez ydBölüm = LYapısı("[G]--G", 90, 100, 0.65) { durum 'G' => "|[+G][-G]" }
dez eğikYdBölüm = LYapısı("[G]--G", 80, 100, 0.65) { durum 'G' => "|[+G][-G]" }
dez çalı = LYapısı("G", 20) { durum 'G' => "|[+G]|[-G]+G" }
dez ağaç = LYapısı("G", 8, 100, 0.35) {
    durum 'G' => "|[+++++G][-------G]-|[++++G][------G]-|[+++G][-----G]-|G"
}
dez kilim = LYapısı("F-F-F-F", 90, 1) { durum 'F' => "F[F]-F+F[--F]+F-F" }
dez koch = LYapısı("F", 90, 10) { durum 'F' => "F-F+F+F-F" } // koch kartanesi
dez sierp = LYapısı("F", 60, 2) { // sierpinski üçgeni
    durum 'F' => "f+F+f"
    durum 'f' => "F-f-F"
}
dez ejder = LYapısı("FX", 90, 20) {
    durum 'X' => "X+YF"
    durum 'Y' => "FX-Y"
}
dez söğüt = LYapısı("X", 25, 4) {
    durum 'X' => "F-[[X]+X]+F[+FX]-X"
    durum 'F' => "FF"
}

dez seçim = 8

tanım dahaKalınÇiz(kalınlık: Sayı) = { kalemKalınlığınıKur(kalınlık) }
dez (örnek, nesil, ayarlamalar) = seçim eşle {
    durum 0 => (ydBölüm, 6, () => { dahaKalınÇiz(2); yaklaş(1.8) })
    durum 1 => (eğikYdBölüm, 6, () => { dahaKalınÇiz(3); yaklaş(1.8) })
    durum 2 => (çalı, 6, () => { dahaKalınÇiz(2); yaklaş(1.4, 0, 150) })
    durum 3 => (ağaç, 4, () => yaklaş(2, 0, 80))
    durum 4 => (kilim, 5, () => yaklaş(1.5, -120, 120))
    durum 5 => (koch, 5, () => yaklaş(0.2, -600, 1200))
    durum 6 => (sierp, 8, () => yaklaş(0.9, -200, 250))
    durum 7 => (ejder, 10, () => yaklaş(0.5, 200, 80))
    durum _ => (söğüt, 7, () => yaklaş(0.3, 0, 600))
}

yinele(nesil) {
    örnek.evir()
}

silVeSakla()
canlandırmaHızınıKur(0)
kalemKalınlığınıKur(1)
kalemRenginiKur(Renk(166, 20, 20)) // kızıl
// çizimden önce bazı düzeltmeler yapalım ki çizim tuvalde güzel görünsün
ayarlamalar()
artalanıKurDik(Renk(30, 191, 168, 150), Renk(30, 76, 252, 200)) // turkuaz -> açık mavi
örnek.çiz()
