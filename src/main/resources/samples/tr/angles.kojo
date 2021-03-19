// Açı nedir ve nasıl ölçülür görmek ister misin?
// Tam bir dönüşe 360 derece ya da 2 çarpı pi radyan deriz.
// Dereceleri anladık. Dik açı yani 90 derece çarpı 4 eşittir tam bir dönüş.
// Bu devinimli çizimle radyan kavramını görerek anlayalım.
// Radyan İngilizce'de radian diye yazılır. Radius yani yarıçaptan gelir.
// Bu yazılımcık şunlardan esinlenmiştir:
// (1) C. K. Raju'nın Sınırsız Kalkülüs (Calculus without limits) adlı ders notları
// (2) http://1ucasvb.tumblr.com/
val araSüresi = 1 // saniye
val yçBoyu = 200.0 // yarıçapın uzunluğu bu olsun
val çeyrekYÇ = yçBoyu / 4
val renk = mavi
val açıRengi = Renk(0, 204, 51)
val eskiAçıRengi = açıkGri
val yayRengi = gri

// Yarıçapı çizerek başlayalım
def yarıçapıÇiz(açı: Sayı) = kalemRengi(renk) * döndür(açı) -> resimDizisi(
    boyaRengi(renk) -> Resim.daire(3),
    Resim.yatay(yçBoyu),
    götür(yçBoyu, 0) * boyaRengi(renk) -> Resim.daire(3)
)

def eğriYçÇiz(start: Kesir, açı: Kesir) = kalemRengi(renk) * döndür(start) -> resimDizisi(
    götür(yçBoyu, 0) * boyaRengi(renk) -> Resim.daire(3),
    Resim.yay(yçBoyu, açı),
    döndür(açı) * götür(yçBoyu, 0) * boyaRengi(renk) -> Resim.daire(3)
)

def yayÇiz(açı: Sayı) = kalemRengi(yayRengi) -> Resim.yay(yçBoyu, açı)

def açıÇiz(start: Kesir, açı: Kesir) = döndür(start) -> resimDizisi(
    Resim.yatay(yçBoyu),
    döndür(açı) -> Resim.yatay(yçBoyu),
    Resim.yay(yçBoyu / 4, açı)
)

// tuvale 7 tane resim çizeceğiz
var birYay = yayÇiz(0)
var birYarıçap = yarıçapıÇiz(0)
var yçYazısı = götür(yçBoyu / 2, -5) -> Resim.yazı("yarıçap", 20)
var birAçı: Resim = Resim.yay(0, 0)
var açıYazısı: Resim = Resim.yazı("", 20)
var işaret: Resim = Resim.yatay(0)
var işaret2: Resim = Resim.yatay(0)

def doğruÇiz(x1: Kesir, y1: Kesir, x2: Kesir, y2: Kesir) = {
    // sqrt karekök bulur. pow sayının verilen üssünü bulur. Burada karesini alıyoruz
    val uzunluk = math.sqrt(math.pow(x2 - x1, 2) + math.pow(y2 - y1, 2))
    val açı = math.atan((y2 - y1) / (x2 - x1)) // ark-tanjant verilen tanjanta denk gelen açıyı bulur
    götür(x1, y1) * döndür(dereceye(açı)) -> Resim.yatay(uzunluk)
}

// tam sayıyı önce kesirli sayıya sonra da radyandan dereceye çevirelim:
def sayıdanDereceye(s: Sayı) = s.toDouble.toDegrees
def dereceye(k: Kesir) = k.toDegrees

def radyanAçıÇiz(e: Sayı) {
    durakla(araSüresi) // çizime ufak bir ara verelim ki çok çabuk geçmesin
    if (e != 1) {
        birYarıçap = eğriYçÇiz(sayıdanDereceye(e - 1), sayıdanDereceye(1))
        çiz(birYarıçap) // çiz çiz demek
    }
    birAçı.kalemRenginiKur(eskiAçıRengi)
    birAçı = kalemRengi(açıRengi) -> açıÇiz(0, sayıdanDereceye(e))
    çiz(birAçı)
    açıYazısı.sil()
    işaret.sil()
    işaret2.sil()
    açıYazısı = götür(-18, -yçBoyu / 4) -> Resim.yazı(s"$e radyan", 20)
    çiz(açıYazısı)
    if (e < 4) { // sinüs ve kosinüs de çok faydalı tanjant gibi
        işaret = doğruÇiz(0, -çeyrekYÇ, çeyrekYÇ - (çeyrekYÇ - çeyrekYÇ * math.cos(30.toRadians)), çeyrekYÇ * math.sin(30.toRadians))
        çiz(işaret)
        işaret2 = doğruÇiz(0, -çeyrekYÇ, yçBoyu - (yçBoyu - yçBoyu * math.cos(30.toRadians)), yçBoyu * math.sin(30.toRadians))
        çiz(işaret2)
    }
}

def piAçısıÇiz(e: Sayı) {
    durakla(araSüresi)
    birYarıçap = if (e == 1)
        eğriYçÇiz(sayıdanDereceye(3), dereceye(math.Pi - 3))
    else
        eğriYçÇiz(sayıdanDereceye(6), dereceye(2 * math.Pi - 6))
    çiz(birYarıçap)
    birAçı.kalemRenginiKur(eskiAçıRengi)
    birAçı = kalemRengi(açıRengi) -> açıÇiz(0, dereceye(math.Pi * e))
    çiz(birAçı)
    açıYazısı.sil()
    açıYazısı = götür(-18, -yçBoyu / 4) -> Resim.yazı(s"${e}π radyan", 20)
    çiz(açıYazısı)
}

var resimSayısı = 1
def resimÇek() {
    durakla(1.0)
    // çizimiKaydet(s"açılar-$resimSayısı")
    resimSayısı += 1
}

silVeSakla()
çiz(birYarıçap)
durakla(araSüresi)
çiz(yçYazısı)
resimÇek()
yineleKere(1 to 360) { i =>
    birYarıçap.döndür(1)
    birYay.sil()
    birYay = yayÇiz(i)
    çiz(birYay)
    durakla(0.003)
}
durakla(araSüresi)
resimÇek()
yçYazısı.kondur(yçBoyu + 5, yçBoyu / 2 + 10)
birYarıçap.döndürMerkezli(-90, yçBoyu, 0)

durakla(araSüresi)
resimÇek()
birYarıçap.sil()

birYarıçap = eğriYçÇiz(0, dereceye(1))
çiz(birYarıçap)
resimÇek()

yineleKere(1 to 3) { e =>
    radyanAçıÇiz(e)
    resimÇek()
}
piAçısıÇiz(1)
resimÇek()
yineleKere(4 to 6) { e =>
    radyanAçıÇiz(e)
    resimÇek()
}
piAçısıÇiz(2)
resimÇek()
