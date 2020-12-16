// Açı nedir ve nasıl ölçülür görmek ister misin?
// Tam bir dönüşe 360 derece ya da 2 çarpı pi radyan deriz.
// Dereceleri anladık. Dik açı yani 90 derece çarpı 4 eşittir tam bir dönüş.
// Bu devinimli çizimle radyan kavramını görerek anlayalım.
// Radyan İngilizce'de radian diye yazılır. Radius yani yarıçaptan gelir.
// Bu yazılımcık şunlardan esinlenmiştir:
// (1) C. K. Raju'nın Sınırsız Kalkülüs (Calculus without limits) adlı ders notları
// (2) http://1ucasvb.tumblr.com/ 
val araSüresi = 1     // saniye
val yçBoyu = 200.0    // yarıçapın uzunluğu bu olsun
val çeyrekYÇ = yçBoyu / 4
val renk = mavi
val açıRengi = Color(0, 204, 51)
val eskiAçıRengi = açıkGri
val yayRengi = gri

// Yarıçapı çizerek başlayalım. Şu ingilizce terimleri bilmekte fayda var:
// pen kalem, color renk, rot rotation dönüş, GPics Kojo'nun resim çizim birimlerinden biri
// fill boyama, Picture resim, circle daire, hline horizontal line yatay doğru, trans transform dönüşüm
def yarıçapıÇiz(açı: Sayı) = penColor(renk) * rot(açı) -> GPics(
    fillColor(renk) -> Picture.circle(3),
    Picture.hline(yçBoyu),
    trans(yçBoyu, 0) * fillColor(renk) -> Picture.circle(3)
)

def eğriYçÇiz(start: Kesir, açı: Kesir) = penColor(renk) * rot(start) -> GPics(
    trans(yçBoyu, 0) * fillColor(renk) -> Picture.circle(3),
    Picture.arc(yçBoyu, açı),
    rot(açı) * trans(yçBoyu, 0) * fillColor(renk) -> Picture.circle(3)
)

def yayÇiz(açı: Sayı) = penColor(yayRengi) -> Picture.arc(yçBoyu, açı)

def açıÇiz(start: Kesir, açı: Kesir) = rot(start) -> GPics(
    Picture.hline(yçBoyu),
    rot(açı) -> Picture.hline(yçBoyu),
    Picture.arc(yçBoyu / 4, açı)  // yay
)

// tuvale 7 biçim çizim yapacağız. 
var birYay = yayÇiz(0)
var birYarıçap = yarıçapıÇiz(0)
var yçYazısı = trans(yçBoyu / 2, -5) -> Picture.text("yarıçap", 20)
var birAçı: Picture = Picture.arc(0, 0)
var açıYazısı: Picture = Picture.text("", 20)
var işaret: Picture = Picture.hline(0)
var işaret2: Picture = Picture.hline(0)

def doğruÇiz(x1: Kesir, y1: Kesir, x2: Kesir, y2: Kesir) = {
    val uzunluk = math.sqrt(math.pow(x2 - x1, 2) + math.pow(y2 - y1, 2))
    val açı = math.atan((y2 - y1) / (x2 - x1))
    trans(x1, y1) * rot(dereceye(açı)) -> Picture.hline(uzunluk)
}

// tam sayıyı önce kesirli sayıya sonra da radyandan dereceye çevirelim:
def sayıdanDereceye(s: Sayı) = s.toDouble.toDegrees
def dereceye(k: Kesir) = k.toDegrees

def radyanAçıÇiz(e: Sayı) {
    pause(araSüresi)  // çizime ufak bir ara verelim ki çok çabuk geçmesin
    if (e != 1) {
        birYarıçap = eğriYçÇiz(sayıdanDereceye(e - 1), sayıdanDereceye(1))
        draw(birYarıçap) // draw çiz demek
    }
    birAçı.setPenColor(eskiAçıRengi)
    birAçı = penColor(açıRengi) -> açıÇiz(0, sayıdanDereceye(e))
    draw(birAçı)
    açıYazısı.erase()
    işaret.erase()
    işaret2.erase()
    açıYazısı = trans(-18, -yçBoyu / 4) -> Picture.text(s"$e radyan", 20)
    draw(açıYazısı)
    if (e < 4) {
        işaret = doğruÇiz(0, -çeyrekYÇ, çeyrekYÇ - (çeyrekYÇ - çeyrekYÇ * math.cos(30.toRadians)), çeyrekYÇ * math.sin(30.toRadians))
        draw(işaret)
        işaret2 = doğruÇiz(0, -çeyrekYÇ, yçBoyu - (yçBoyu - yçBoyu * math.cos(30.toRadians)), yçBoyu * math.sin(30.toRadians))
        draw(işaret2)
    }
}

def piAçısıÇiz(e: Sayı) {
    pause(araSüresi)
    birYarıçap = if (e == 1)
        eğriYçÇiz(sayıdanDereceye(3), dereceye(math.Pi - 3))
    else
        eğriYçÇiz(sayıdanDereceye(6), dereceye(2 * math.Pi - 6))
    draw(birYarıçap)
    birAçı.setPenColor(eskiAçıRengi)
    birAçı = penColor(açıRengi) -> açıÇiz(0, dereceye(math.Pi * e))
    draw(birAçı)
    açıYazısı.erase()
    açıYazısı = trans(-18, -yçBoyu / 4) -> Picture.text(s"${e}π radyan", 20)
    draw(açıYazısı)
}

var resimSayısı = 1
def resimÇek() {
    pause(0.5)
//    exportImage(s"açılar-$resimSayısı")
    resimSayısı += 1
}

silVeSakla()
draw(birYarıçap)
pause(araSüresi)
draw(yçYazısı)
resimÇek()
yineleKere(1 to 360) { i =>
    birYarıçap.rotate(1)
    birYay.erase()
    birYay = yayÇiz(i)
    draw(birYay)
    pause(0.001)
}
pause(araSüresi)
resimÇek()
yçYazısı.setPosition(yçBoyu + 5, yçBoyu / 2 + 10)
birYarıçap.rotateAboutPoint(-90, yçBoyu, 0)

pause(araSüresi)
resimÇek()
birYarıçap.erase()

birYarıçap = eğriYçÇiz(0, dereceye(1))
draw(birYarıçap)
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
