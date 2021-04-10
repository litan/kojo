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

case class LYapısı(
    belit:        Yazı,
    açı:          Kesir,
    uzunluk:      Sayı  = 100,
    büyütmeOranı: Kesir = 0.6
)(kurallar: Bölümselİşlev[Harf, Yazı]) {
    var kuram = belit
    var nesil = 0
    def evir() {
        nesil += 1
        kuram = kuram.map { h =>
            // h harfi için tanımlanmışsa, yani h kuralı varsa:
            if (kurallar.isDefinedAt(h)) kurallar(h) else h
            // dik çizgilerin yerine nesil sayısını koy:
        }.mkString.replaceAll("""\|""", nesil.toString)
    }

    def çiz() {
        def sayıMı(c: Char) = Harf.sayıMı(c)
        val üretilmişSayı = new EsnekYazı
        def düzGidebilir() {
            if (üretilmişSayı.size != 0) { // boş değilse
                val n = üretilmişSayı.toString.toInt // esnek yazıdan sayıya çevir
                üretilmişSayı.clear() // sil
                ileri(uzunluk * kuvveti(büyütmeOranı, n))
            }
        }
        kuram.foreach { c =>
            if (!sayıMı(c)) {
                düzGidebilir()
            }

            c match {
                case 'F' => ileri(uzunluk)
                case 'f' => ileri(uzunluk)
                case 'G' =>
                    kalemiKaldır(); ileri(uzunluk); kalemiİndir()
                case '['            => konumVeYönüBelleğeYaz()
                case ']'            => konumVeYönüGeriYükle()
                case '+'            => sağ(açı)
                case '-'            => sol(açı)
                case s if sayıMı(s) => üretilmişSayı.append(s) // sona ekle
                case _              =>
            }
        }
        düzGidebilir()
    }
}

val ydBölüm = LYapısı("[G]--G", 90, 100, 0.65) { case 'G' => "|[+G][-G]" }
val eğikYdBölüm = LYapısı("[G]--G", 80, 100, 0.65) { case 'G' => "|[+G][-G]" }
val çalı = LYapısı("G", 20) { case 'G' => "|[+G]|[-G]+G" }
val ağaç = LYapısı("G", 8, 100, 0.35) {
    case 'G' => "|[+++++G][-------G]-|[++++G][------G]-|[+++G][-----G]-|G"
}
val kilim = LYapısı("F-F-F-F", 90, 1) { case 'F' => "F[F]-F+F[--F]+F-F" }
val koch = LYapısı("F", 90, 10) { case 'F' => "F-F+F+F-F" } // koch kartanesi
val sierp = LYapısı("F", 60, 2) { // sierpinski üçgeni
    case 'F' => "f+F+f"
    case 'f' => "F-f-F"
}
val ejder = LYapısı("FX", 90, 20) {
    case 'X' => "X+YF"
    case 'Y' => "FX-Y"
}
val söğüt = LYapısı("X", 25, 4) {
    case 'X' => "F-[[X]+X]+F[+FX]-X"
    case 'F' => "FF"
}

val seçim = 8

def dahaKalınÇiz(kalınlık: Sayı) = { kalemKalınlığınıKur(kalınlık) }
val (örnek, nesil, ayarlamalar) = seçim match {
    case 0 => (ydBölüm, 6, () => { dahaKalınÇiz(2); yaklaş(1.8) })
    case 1 => (eğikYdBölüm, 6, () => { dahaKalınÇiz(3); yaklaş(1.8) })
    case 2 => (çalı, 6, () => { dahaKalınÇiz(2); yaklaş(1.4, 0, 150) })
    case 3 => (ağaç, 4, () => yaklaş(2, 0, 80))
    case 4 => (kilim, 5, () => yaklaş(1.5, -120, 120))
    case 5 => (koch, 5, () => yaklaş(0.2, -600, 1200))
    case 6 => (sierp, 8, () => yaklaş(0.9, -200, 250))
    case 7 => (ejder, 10, () => yaklaş(0.5, 200, 80))
    case _ => (söğüt, 7, () => yaklaş(0.3, 0, 600))
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
