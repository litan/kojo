//#yükle tr/anaTanimlar

object ABa { // alfa-beta arama
    def hamleYap(drm: Durum): Belki[Oda] = {
        var çıktı: Belki[Oda] = Hiçbiri
        zamanTut(s"Alfa-beta ${düzeydenUstalığa} arama") {
            çıktı = alfaBetaHamle(drm)
        }("sürdü")
        çıktı
    }

    def alfaBetaHamle(drm: Durum): Belki[Oda] =
        if (drm.seçenekler.boşMu) Hiçbiri
        else // todo: karşı oyuncunun skorunu azaltan birden çok hamle varsa rastgele seç
            Biri((for (hamle <- drm.seçenekler) yield hamle ->
                abHamle(drm.oyna(hamle), aramaDerinliğiSınırı)
            ).enUfağı(_._2)._1)

    // todo: yasal hamle olmadığı zaman arama kısa kesilmemeli!
    def abHamle(drm: Durum, derinlik: Sayı): Sayı =
        if (drm.bitti || derinlik == 0 || drm.seçenekler.boşMu) drm.skor
        else azalt(drm, derinlik, Sayı.EnUfağı, Sayı.Enİrisi) // todo

    def azalt(drm: Durum, derinlik: Sayı, alfa: Sayı, beta: Sayı): Sayı =
        if (drm.bitti || derinlik == 0 || drm.seçenekler.boşMu) drm.skor
        else {
            var yeniBeta = beta
            drm.seçenekler.herbiriİçin { hamle => // onun hamleleri
                val yeniDurum = drm.oyna(hamle)
                yeniBeta = enUfağı(yeniBeta, artır(yeniDurum, derinlik - 1, alfa, yeniBeta))
                if (alfa >= yeniBeta) return alfa
            }
            yeniBeta
        }
    def artır(drm: Durum, derinlik: Sayı, alfa: Sayı, beta: Sayı): Sayı =
        if (drm.bitti || derinlik == 0 || drm.seçenekler.boşMu) drm.skor
        else {
            var yeniAlfa = alfa
            drm.seçenekler.herbiriİçin { hamle =>
                val yeniDurum = drm.oyna(hamle)
                yeniAlfa = enİrisi(yeniAlfa, azalt(yeniDurum, derinlik - 1, yeniAlfa, beta))
                if (yeniAlfa >= beta) return beta
            }
            yeniAlfa
        }

    def ustalık(derece: Ustalık) = {
        aramaDerinliğiSınırı = derece match {
            case Er       => 3
            case Çırak    => 4
            case Kalfa    => 5
            case Usta     => 6
            case Doktor   => 7
            case Aheste   => 7
            case Deha     => 8
            case ÇokSabır => 8
            case _        => 5
        }
    }
    def düzeydenUstalığa: Ustalık =
        if (aramaDerinliğiSınırı < 3) ErdenAz
        else if (aramaDerinliğiSınırı > 8) DehadanÇok
        else {
            aramaDerinliğiSınırı match {
                case 3 => Er
                case 4 => Çırak
                case 5 => Kalfa
                case 6 => Usta
                case 7 => Doktor
                case 8 => Deha
            }
        }
    var aramaDerinliğiSınırı = 3

}

class Durum(val tahta: Tahta, val sıra: Taş) {
    val karşıTaş = if (sıra == Beyaz) Siyah else Beyaz
    def skor = tahta.drm(sıra) - tahta.drm(karşıTaş)
    def bitti = oyunBittiMi
    def oyunBittiMi = {
        if (yasallar.boyu > 0) yanlış else {
            val yeniDurum = new Durum(tahta, karşıTaş)
            yeniDurum.yasallar.boyu == 0
        }
    }
    def seçenekler = yasallar
    def yasallar = tahta.yasallar(sıra)
    def oyna(oda: Oda) = {
        val yTahta = tahta.oyna(sıra, oda)
        new Durum(yTahta, karşıTaş)
    }
}

class Tahta(val tane: Sayı, val tahta: Sayılar) {
    def drm(t: Taş) = {
        say(t) // + 4 * isay(t)(köşeMi) + 2 * isay(t)(köşeyeİkiUzakMı)
        // + isay(t)(içKöşeMi)
        // - 2 * isay(t)(tuzakKöşeMi) - isay(t)(tuzakKenarMı)
        //
    }
    def yaz(msj: Yazı = "", tab: Yazı = "") = {
        for (y <- satırAralığıSondan) {
            val satır = for (x <- satırAralığı) yield s2t(tahta(y * tane + x))
            satıryaz(satır.yazıYap(s"$tab", " ", ""))
        }
        if (msj.boyu > 0) satıryaz(msj)
        satıryaz(s"$tab Beyazlar: ${say(Beyaz)} durum: ${drm(Beyaz)}")
        satıryaz(s"$tab Siyahlar: ${say(Siyah)} durum: ${drm(Siyah)}")
    }
    def koy(oda: Oda, taş: Taş) = {
        new Tahta(tane, tahta.değiştir(oda.y * tane + oda.x, t2s(taş)))
    }
    def koy(odalar: Dizi[Oda], taş: Taş) = {
        var yeniTahta = tahta
        for (o <- odalar) { yeniTahta = yeniTahta.değiştir(o.y * tane + o.x, t2s(taş)) }
        new Tahta(tane, yeniTahta)
    }
    def taş(o: Oda): Taş = s2t(tahta(o.y * tane + o.x))
    def oyunVarMı(oyuncu: Taş) = yasallar(oyuncu).boyu > 0
    def yasallar(oyuncu: Taş) = {
        (for (y <- satırAralığı; x <- satırAralığı if taş(Oda(y, x)) == Yok)
            yield Oda(y, x)) ele { çevirilecekKomşuDiziler(oyuncu, _).boyu > 0 }
    }
    def oyna(oyuncu: Taş, oda: Oda) = {
        val odalar = EsnekDizim(oda)
        val karşı = if (oyuncu == Beyaz) Siyah else Beyaz
        çevirilecekKomşuDiziler(oyuncu, oda).herbiriİçin { komşu =>
            odalar += komşu.oda
            gerisi(komşu).alDoğruKaldıkça(taş(_) == karşı).herbiriİçin { o => odalar += o }
        }
        koy(odalar.dizi, oyuncu)
    }

    private def çevirilecekKomşuDiziler(oyuncu: Taş, oda: Oda): Dizi[Komşu] =
        komşularıBul(oda) ele { komşu =>
            val karşı = if (oyuncu == Beyaz) Siyah else Beyaz
            taş(komşu.oda) == karşı && sonuDaYasalMı(komşu, oyuncu)._1
        }

    private def komşularıBul(o: Oda): Dizi[Komşu] = Dizi(
        Komşu(D, Oda(o.y, o.x + 1)), Komşu(B, Oda(o.y, o.x - 1)),
        Komşu(K, Oda(o.y + 1, o.x)), Komşu(G, Oda(o.y - 1, o.x)),
        Komşu(KD, Oda(o.y + 1, o.x + 1)), Komşu(KB, Oda(o.y + 1, o.x - 1)),
        Komşu(GD, Oda(o.y - 1, o.x + 1)), Komşu(GB, Oda(o.y - 1, o.x - 1))) ele {
            k => odaMı(k.oda)
        }

    private def sonuDaYasalMı(k: Komşu, oyuncu: Taş): (İkil, Sayı) = {
        val diziTaşlar = gerisi(k)
        val sıraTaşlar = diziTaşlar.düşürDoğruKaldıkça { o =>
            taş(o) != oyuncu && taş(o) != Yok
        }
        if (sıraTaşlar.boşMu) (yanlış, 0) else {
            val oda = sıraTaşlar.başı
            (taş(oda) == oyuncu, 1 + diziTaşlar.boyu - sıraTaşlar.boyu)
        }
    }
    private def gerisi(k: Komşu): Dizi[Oda] = {
        val sıra = EsnekDizim.boş[Oda]
        val (x, y) = (k.oda.x, k.oda.y)
        k.yön match {
            case D => for (i <- x + 1 |-| sonOda) /* */ sıra += Oda(y, i)
            case B => for (i <- x - 1 |-| 0 by -1) /**/ sıra += Oda(y, i)
            case K => for (i <- y + 1 |-| sonOda) /* */ sıra += Oda(i, x)
            case G => for (i <- y - 1 |-| 0 by -1) /**/ sıra += Oda(i, x)
            case KD => // hem y hem x artacak
                if (x >= y) for (i <- x + 1 |-| sonOda) /*         */ sıra += Oda(y + i - x, i)
                else for (i <- y + 1 |-| sonOda) /*                */ sıra += Oda(i, x + i - y)
            case GB => // hem y hem x azalacak
                if (x >= y) for (i <- y - 1 |-| 0 by -1) /*        */ sıra += Oda(i, x - y + i)
                else for (i <- x - 1 |-| 0 by -1) /*               */ sıra += Oda(y - x + i, i)
            case KB => // y artacak x azalacak
                if (x + y >= sonOda) for (i <- y + 1 |-| sonOda) /**/ sıra += Oda(i, x + y - i)
                else for (i <- x - 1 |-| 0 by -1) /*               */ sıra += Oda(y + x - i, i)
            case GD => // y azalacak x artacak
                if (x + y >= sonOda) for (i <- x + 1 |-| sonOda) /**/ sıra += Oda(y + x - i, i)
                else for (i <- y - 1 |-| 0 by -1) /*               */ sıra += Oda(i, x + y - i)
        }
        sıra.dizi
    }

    val sonOda = tane - 1
    val satırAralığı = 0 |-| sonOda
    val satırAralığıSondan = sonOda |-| 0 by -1

    def tuzakKenarMı: Oda => İkil = {
        case Oda(str, stn) => str == 1 || stn == 1 || str == sonOda - 1 || stn == sonOda - 1
    }
    def tuzakKöşeMi: Oda => İkil = {
        case Oda(y, x) => (x == 1 && (y == 1 || y == sonOda - 1)) ||
            (x == sonOda - 1 && (y == 1 || y == sonOda - 1))
    }
    def köşeMi: Oda => İkil = {
        case Oda(str, stn) => if (str == 0) stn == 0 || stn == sonOda else
            str == sonOda && (stn == 0 || stn == sonOda)
    }
    def köşeyeİkiUzakMı: Oda => İkil = {
        case Oda(y, x) =>
            ((y == 0 || y == sonOda) && (x == 2 || x == sonOda - 2)) ||
                ((y == 2 || y == sonOda - 2) &&
                    (x == 0 || x == 2 || x == sonOda - 2 || x == sonOda))
    }
    def içKöşeMi: Oda => İkil = {
        case Oda(y, x) => (x == 2 && (y == 2 || y == sonOda - 2)) ||
            (x == sonOda - 2 && (y == 2 || y == sonOda - 2))
    }
    def odaMı: Oda => İkil = {
        case Oda(y, x) => 0 <= y && y < tane && 0 <= x && x < tane
    }
    def isay(t: Taş)(iş: Oda => İkil) = (for (x <- satırAralığı; y <- satırAralığı; if taş(Oda(y, x)) == t && iş(Oda(y, x))) yield 1).boyu
    def say(t: Taş) = isay(t) { o => doğru }

    private def t2s(t: Taş) = t match {
        case Beyaz => 1
        case Siyah => 2
        case _     => 0
    }
    private def s2t(s: Sayı) = s match {
        case 1 => Beyaz
        case 2 => Siyah
        case _ => Yok
    }
}

def yeniTahta(tane: Sayı, çeşni: Sayı = 0): Tahta = {
    var t = new Tahta(tane, Yöney.doldur(tane * tane)(0))

    def diziden(dizi: Dizi[(Sayı, Sayı)])(taş: Taş) = t = t.koy(dizi.işle(p => Oda(p._1, p._2)), taş)
    def dörtTane: Oda => Birim = {
        case Oda(y, x) =>
            diziden(Dizi((y, x), (y + 1, x + 1)))(Beyaz)
            diziden(Dizi((y + 1, x), (y, x + 1)))(Siyah)
    }
    val orta: Sayı = tane / 2
    val sonu = tane - 1
    çeşni match {
        case 2 => // boş tahtayla oyun başlayamıyor
        case 1 =>
            gerekli((tane > 6), "Bu çeşni için 7x7 ya da daha iri bir tahta gerekli")
            dörtTane(Oda(1, 1))
            dörtTane(Oda(sonu - 2, sonu - 2))
            dörtTane(Oda(1, sonu - 2))
            dörtTane(Oda(sonu - 2, 1))
        case _ =>
            val çiftse = tane % 2 == 0
            if (çiftse) dörtTane(Oda(orta - 1, orta - 1))
            else {
                val (a, b) = (orta - 1, orta + 1)
                diziden(Dizi(a -> a, b -> b))(Beyaz)
                diziden(Dizi((a, b), (b, a)))(Siyah)
                if (yanlış) { // (a, b) odaları boş kalıyor her a ve b çift sayısı için
                    diziden(Dizi(a + 1 -> a, (b - 1, b)))(Beyaz)
                    diziden(Dizi((a, b - 1), (b, a + 1)))(Siyah)
                }
                else {
                    diziden(Dizi((a + 1, a), (a + 1, b), (b + 1, b - 1), (a - 1, b - 1)))(Beyaz)
                    diziden(Dizi((a, a + 1), (b, a + 1), (a + 1, a - 1), (a + 1, b + 1)))(Siyah)
                }
            }
    }
    t
}

var i = 0
class Oyun(tane: Sayı) {
    val t = yeniTahta(tane)
    val drm = new Durum(t, Siyah)

    def oyna: Durum = döngü(drm)

    import scala.annotation.tailrec
    @tailrec
    private def döngü(drm: Durum): Durum = {
        drm.tahta.yaz("döngü")
        i += 1
        if (drm.oyunBittiMi) return drm
        if (i > tane * tane) { satıryaz("çok uzadı!"); return drm }
        val hamle = ABa.hamleYap(drm) match {
            case Biri(oda) => oda
            case _ => ABa.hamleYap(new Durum(drm.tahta, drm.karşıTaş)) match {
                case Biri(oda) => oda
                case _         => throw new Exception("Burada olmamalı")
            }
        }
        val yeniDurum = drm.oyna(hamle)
        satıryaz(s"$i. hamle ${drm.sıra} $hamle:")
        döngü(yeniDurum)
    }
}

def dene1 = {
    val tane = 4
    var t = new Tahta(tane, Yöney.doldur(tane * tane)(0))
    satıryaz("t"); t.yaz()
    val foo = t.koy(Oda(1, 1), Beyaz)
    t = t.koy(Dizi(Oda(2, 2), Oda(3, 3)), Beyaz)
    t = t.koy(Dizi(Oda(2, 3), Oda(3, 2)), Siyah)
    val t2 = t.oyna(Siyah, Oda(1, 2))
    satıryaz("t2"); t2.yaz()
    val t3 = t2.oyna(Beyaz, Oda(1, 3))
    satıryaz("t3"); t3.yaz()
    satıryaz("t"); t.yaz()
    foo.yaz()
}

def dene2 = {
    çıktıyıSil
    val o = new Oyun(4)
    //ABa.ustalık(Çırak)
    ABa.aramaDerinliğiSınırı = 2
    o.oyna
}
//dene1
/* Bu yazılımcığı otello.kojo ve menu.kojo kullanıyor.
   Onun için denemeleri artık çalıştırmıyoruz */
//dene2
satıryaz("arama motoru hazır")
