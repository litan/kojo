// İngilizce'de Tic Tac Toe diye bilinen oyun
// Oyun kuramının sıfır toplamlı oyunları çözmek için geliştirdiği
// meşhur minimax algoritmasını göreceğiz bu yazılımcıkta!
// Daha hızlı çalışsın diye alfa-beta (a-b) budaması kullandık.
// Usta hatta yenilmez bir oyuncuya karşı oynamak nasıl olacak bakalım :-)
// Daha da ilginci yenilmeyen bir strateji nasıl programlanır onu göreceğiz.

trait Hane
case object Bilgisayar extends Hane // bilgisayar daire çizerek hamle yapacak
case object İnsan extends Hane // oyuncu çarpım işareti çizerek hamle yapacak
case object Boş extends Hane // tahtadaki boşluklar

silVeSakla()
val ta = tuvalAlanı
artalanıKur(siyah)
//yaklaşmayaİzinVerme()
val boy = 100

val tahnanınBoyu = boy * 3
val tahtaX = ta.x + (ta.eni - tahnanınBoyu) / 2
val tahtaY = ta.y + (ta.boyu - tahnanınBoyu) / 2

val kenarPayı = 20
val çizgininKalınlığı = 8

def artalan() {
    kalemRenginiKur(renksiz)
    boyamaRenginiKur(siyah)
    val pay = çizgininKalınlığı / 2
    konumuKur(pay, pay)
    yinele(4) {
        ileri(boy - 2 * pay)
        sağ(90)
    }
}

def çarpı = Resim {
    artalan()
    kalemKalınlığınıKur(çizgininKalınlığı)
    kalemRenginiKur(Renk.ada(200, 1.00, 0.50))
    konumuKur(kenarPayı, kenarPayı)
    noktayaGit(boy - kenarPayı, boy - kenarPayı)
    konumuKur(boy - kenarPayı, kenarPayı)
    noktayaGit(kenarPayı, boy - kenarPayı)
}

def daire = Resim {
    artalan()
    kalemKalınlığınıKur(çizgininKalınlığı)
    kalemRenginiKur(Renk.ada(120, 0.86, 0.64))
    konumuKur(boy / 2, kenarPayı)
    açıyaDön(0)
    val boy2 = boy - 2 * kenarPayı
    sol(360, boy2 / 2)
}

val çizgiler = Resim {
    kalemKalınlığınıKur(çizgininKalınlığı)
    yineleİçin(1 |-| 2) { n =>
        konumuKur(boy * n, 0)
        noktayaGit(boy * n, 3 * boy)
    }
    yineleİçin(1 |-| 2) { n =>
        konumuKur(0, boy * n)
        noktayaGit(3 * boy, boy * n)
    }
}

var tahta = Dizim.doldur[Hane](3, 3)(Boş)
var hamleResimleri = Dizim.doldur[Resim](3, 3)(boşResim)
def boşResim: Resim = Resim {}

var birSonrakiHamleÇarpıMı = doğru
var oyunBittiMi = yanlış

def değerlendir: Sayı = {
    if (oyunuKazandıMı(İnsan)) {
        -10
    }
    else if (oyunuKazandıMı(Bilgisayar)) {
        10
    }
    else {
        0
    }
}
  
val sıfırBirİki = 0 |-| 2
def hamleKalmadıMı: İkil = {
    var bütünHanelerDolduMu = doğru
    yineleİçin(sıfırBirİki) { x =>
        yineleİçin(sıfırBirİki) { y =>
            if (tahta(x)(y) == Boş) {
                bütünHanelerDolduMu = yanlış
            }
        }
    }
    bütünHanelerDolduMu
}

val AlfaMin = -1000
val BetaMax = 1000

// minimax: kısaca rakibin en iyi hamlesine engel olma stratejisi
// İngilizce'de: minimize the maximum value the opponent could get with any move
def minimax(
    aramaDerinliği:     Sayı,
    bilgisayarınSırası: İkil,
    alpha:              Sayı, beta: Sayı
): Sayı = {
    val kaçKaç = değerlendir
    if (kaçKaç == 10 || kaçKaç == -10) {
        return kaçKaç
    }
    if (hamleKalmadıMı) {
        return 0
    }
    if (bilgisayarınSırası) {
        var değer = AlfaMin
        var yeniAlfa = alpha
        yineleİçin(sıfırBirİki) { x =>
            yineleİçin(sıfırBirİki) { y =>
                if (tahta(x)(y) == Boş) {
                    tahta(x)(y) = Bilgisayar
                    değer = enİrisi(değer, minimax(aramaDerinliği + 1, !bilgisayarınSırası, yeniAlfa, beta))
                    tahta(x)(y) = Boş
                    if (değer >= beta) {
                        return değer
                    }
                    yeniAlfa = enİrisi(yeniAlfa, değer)
                }
            }
        }
        return değer
    }
    else {
        var değer = BetaMax
        var yeniBeta = beta
        yineleİçin(sıfırBirİki) { x =>
            yineleİçin(sıfırBirİki) { y =>
                if (tahta(x)(y) == Boş) {
                    tahta(x)(y) = İnsan
                    değer = enUfağı(değer, minimax(aramaDerinliği + 1, !bilgisayarınSırası, alpha, yeniBeta))
                    tahta(x)(y) = Boş
                    if (değer <= alpha) {
                        return değer
                    }
                    yeniBeta = enUfağı(yeniBeta, değer)
                }
            }
        }
        return değer
    }
}

case class Hamle(x: Sayı, y: Sayı)

def enİyiHamleyiBul: Hamle = {
    var enYüksekKazanç = -1000;
    var enİyiHamle = Hamle(-1, -1)
    yineleİçin(sıfırBirİki) { x =>
        yineleİçin(sıfırBirİki) { y =>
            if (tahta(x)(y) == Boş) {
                tahta(x)(y) = Bilgisayar
                val hamleninKazancı = minimax(0, yanlış, AlfaMin, BetaMax)
                tahta(x)(y) = Boş
                if (hamleninKazancı > enYüksekKazanç) {
                    enYüksekKazanç = hamleninKazancı
                    enİyiHamle = Hamle(x, y)
                }
            }
        }
    }
    enİyiHamle
}

def hamleYap(x: Sayı, y: Sayı, hamle: Hane, yeniResim: Resim) {
    yeniResim.konumuKur(tahtaX + x * boy, tahtaY + y * boy)
    tahta(x)(y) = hamle
    birSonrakiHamleÇarpıMı = !birSonrakiHamleÇarpıMı
    hamleResimleri(x)(y) = yeniResim
    çiz(yeniResim)
    kazanıldıMı()
    if (!oyunBittiMi) {
        if (hamleKalmadıMı) {
            oyunBittiMi = doğru
            bilgiVer("Berabere")
        }
    }
}

def bilgisayarHamlesiYap(resim: Resim) {
    val hamle = enİyiHamleyiBul
    val yeniResim = daire
    hamleYap(hamle.x, hamle.y, Bilgisayar, yeniResim)
}

def tahtayıÇiz() {
    çizgiler.konumuKur(tahtaX, tahtaY)
    çiz(çizgiler)
    yineleİçin(sıfırBirİki) { x =>
        yineleİçin(sıfırBirİki) { y =>
            val resim = Resim { artalan }
            resim.konumuKur(tahtaX + x * boy, tahtaY + y * boy)
            çiz(resim)
            resim.fareyeBasınca { (_, _) =>
                if (!oyunBittiMi) {
                    if (birSonrakiHamleÇarpıMı) {
                        val yeniResim = çarpı
                        resim.sil()
                        hamleYap(x, y, İnsan, yeniResim)
                        if (!oyunBittiMi) {
                            sırayaSok(0.5) {
                                bilgisayarHamlesiYap(resim)
                            }
                        }
                    }
                }
            }
            hamleResimleri(x)(y) = resim
            tahta(x)(y) = Boş
        }
    }
}

def oyunuKazandıMı(h: Hane): İkil = {
    // yerel tanımlar da yapabiliriz
    def sütun(x: Sayı) = tahta(x).diziye // tahta(x) bir Array. Dizi'ye çevirelim onu
    def satır(y: Sayı) = EsnekDizim(tahta(0)(y), tahta(1)(y), tahta(2)(y)).diziye
    def çapraz1 = EsnekDizim(tahta(0)(0), tahta(1)(1), tahta(2)(2)).diziye
    def çapraz2 = EsnekDizim(tahta(0)(2), tahta(1)(1), tahta(2)(0)).diziye

    val hedef = EsnekDizim(h, h, h).diziye
    yineleİçin(sıfırBirİki) { x =>
        if (sütun(x) == hedef)
            return doğru
    }
    yineleİçin(sıfırBirİki) { y =>
        if (satır(y) == hedef)
            return doğru
    }
    if (çapraz1 == hedef) {
        return doğru
    }
    çapraz2 == hedef
}

val bilgiYazısı = büyüt(2.0) * götür(-25, 15) * kalemRengi(beyaz) -> Resim.yazı("")
def bilgiVer(yazı: Yazı) {
    val resim = Resim.diziDikeyDüzenli(bilgiYazısı, Resim.dikeyBoşluk(ta.boyu - 100))
    çizMerkezde(resim)
    bilgiYazısı.güncelle(yazı)
}
// Geleceğin bilgisayar programcılarına önemli not. Genelde değer ve değişkenlerin yerel olmasında çok fayda var.
// Ama, burada bilgiYazısı değerinin bilgiVer yöntemi içinde yerel değer olması iyi olmaz! 
// Yoksa "Oyna!" yazısıyla "Berabere" ya da "Kaybettin" yazıları yüstüste gelir en sonda.
def kazanıldıMı() {
    if (oyunuKazandıMı(Bilgisayar)) {
        oyunBittiMi = doğru
        bilgiVer("Kaybettin")
    }
    else if (oyunuKazandıMı(İnsan)) {
        oyunBittiMi = doğru
        bilgiVer("Kazandın!?") // eğer yazılımcığımızda hata yoksa buraya hiç gelmemeliyiz!
    }
}

tahtayıÇiz()
bilgiVer("Oyna!")
