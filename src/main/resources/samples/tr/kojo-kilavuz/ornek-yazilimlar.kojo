// Şurada kullanıyoruz: kojo-documentation.kojo

dez kod10 = """silVeSakla
yaklaş(3)
ileri; sağ
biçimleriBelleğeYaz()
kalemKalınlığınıKur(10)
kalemRenginiKur(mavi)
ileri; sağ
biçimleriGeriYükle()
ileri; sağ"""

dez kod20 = """sil
durakla(0.5)
giysileriKur(Görünüş.araba,
  Görünüş.yarasa1,
  Görünüş.yarasa2)
durakla(0.5)
birsonrakiGiysi()
durakla(0.5)
birsonrakiGiysi()
durakla(0.5)
birsonrakiGiysi()
durakla(0.5)"""

dez kod30 = """silVeSakla
boyamaRenginiKur(mavi)
kare(40)
ileri(40)
boyamaRenginiKur(yeşil)
kare(40)"""

dez kod30b = """silVeSakla
hızıKur(hızlı)
yaklaş(0.5, 240, 160)
artalanıKur(Renk(107, 4, 189))
kalemRenginiKur(renksiz)
dez renkDizisi = Dizi(
  sarı, yeşil, pembe, kahverengi,
  siyah, gri, koyuGri, açıkGri,
  renksiz, beyaz, kırmızı, turuncu,
  mavi, mor, morumsu, camgöbeği
)
//beyazla renksiz'in farkına dikkat!

yineleDizinli(renkDizisi.boyu) { i =>
  boyamaRenginiKur(renkDizisi(i-1))
  kare(100); ileri(100)
  eğer(i % 4 == 0)
     konumuKur(100 * i / 4, 0)
}"""
dez kod30c = """// Türkçe'ye çevirdiğimiz renklerin
// hepsini Renkler altında topladık:
yineleİçin(Dizin(
    Renkler.açıkAltınbaşakSarısı,
    Renkler.koyuDenizYeşili,
    Renkler.gökMavisi,
    Renkler.haki)
) { renk =>
    boyamaRenginiKur(renk)
    kare(100); ileri(100)
}
konumuKur(500, 0)
// Henüz Türkçeleştiremediğimiz
// renkler de var.
// İşte birkaç örnek:
yineleİçin(Dizin(
    renkler.aliceBlue,
    renkler.hotpink,
    renkler.darkTurquoise,
    renkler.yellowGreen)
) { renk =>
    boyamaRenginiKur(renk)
    kare(100); ileri(100)
}
/* Diğer renkeri görmek istersen
aşağıda boş bir satıra 'Renkler.'
yazdıktan sonra Kontrol tuşunu basılı
tutup büyük boşluk tuşuna basıver.
Adı a harfiyle başlayan renkleri
bulmak istersen Renkler.a yazıp
Kontrol-Boşluk tuşuna bas */
// Aşağıdaki iki satırı kullanabilirsin
// Ama önce baştaki çift taksimi sil!
//   Renkler.
//   Renkler.a
// İngilizce renkler de şurada:
//   renkler.
//   renkler.a"""

dez kod40 = """sil
canlandırmaHızınıKur(100)
kalemKalınlığınıKur(10)
boyamaRenginiKur(mavi)
den say = 1
dez başlangıçAnı = buAn
yinele(8) {
    ileri(100)
    eğer(say%4 == 0) ileri(100)
    yoksa sağ
    say = say + 1
}
yinele(2) {
    sol
    ileri(100)
}
dez süre = yuvarla(
  (buAn - başlangıçAnı) / 1000.0,
1)
satıryaz(s"Çizim $süre saniye sürdü")
"""

dez giysileriKur_Örnek = """giysileriKur(Görünüş.yarasa1,
  Görünüş.yarasa2
)"""

dez kod50 = """silVeSakla
kalemRenginiKur(renksiz)
göster
giysileriKur(
  Görünüş.yarasa1,
  Görünüş.yarasa2)
yinele(20) {
    durakla(0.1)
    atla(konum.x + 10,
         konum.y + 5)
    birsonrakiGiysi()
}"""


dez yazı_Örnek = "yazı(Aralık(1, 200, 7))"

dez kod60 = """silVeSakla()
kalemRenginiKur(koyuGri)
canlandırmaHızınıKur(10)
kalemiKaldır()
noktayaGit(-300, -200)
açıyaDön(90)
kalemiİndir()
yazı(s"${yazıyüzleri.boyu} tane yazıyüzü var")
yazıyüzleri.ikileSırayla.herbiriİçin {
    durum(adı, sırası) =>
        zıpla()
        yazıYüzünüKur(yazıyüzü(adı, 18))
        yazı(f"$sırası%-3d: \t $adı")
}
yazıyüzleri.ikileSırayla.herbiriİçin(satıryaz)
satıryaz("Kaplumbağa bütün yazıyüzlerini" ++
    " yazabilmek için\nkuzeye" ++
    s" doğru epey yol aldı: y=${konum.y}")"""

dez kod60b = """// buna her tıklayışında
// tuval bir sayfa yukarı kayar
tuvaliKaydır(0, tuvalAlanı.boyu)
"""

dez kod60c = """//bu da aşağı dönmek için:
tuvaliKaydır(0, -tuvalAlanı.boyu)"""

dez kod60d = """dez yy = yazıyüzleri(127)
satıryaz(s"Sistemde ${yazıyüzleri.boyu} tane yazı yüzü var. Birinin adı: $yy.")
satıryaz("Onunla tuvale birşeyler yazalım:")
silVeSakla
kalemRenginiKur(mor)
yazıYüzünüKur(yazıyüzü(yy,18))
yazı("Yazma denemesi yapalım...")"""

dez kod70 = """silVeSakla
eksenleriGöster
ızgarayıGöster
zıpla(4)
yazı("1234")
sol; zıpla(50); sağ
// biraz küçültelim:
yazıBoyunuKur(10)
yazı("1234")
yaklaş(4.0, 0, 0)"""

dez kod80 = """sil
dez yk2 = yeniKaplumbağa(0, 100)
yk2.giysiKur(Görünüş.araba)
yk2.geri(180)
// iki kat hızlandıralım:
yk2.canlandırmaHızınıKur(500)
yk2.sağ()
yk2.ileri(300)
// ilki hala yavaş
sağ; ileri(300)"""

dez yazıYüzünüKur_Örnek = """yazıYüzünüKur(
  yazıyüzü("Times New Roman",
            36
  )
)"""

dez kod100 = """ileri
kalemiKaldır()
ileri
kalemiİndir()
ileri"""

dez kod110 = """sil()
kalemKalınlığınıKur(10)
üçgen()
kalemKalınlığınıKur(1)
ileri
üçgen()
"""

dez kod120 = """yinele(3) {
    gizle()
    ileri(100)
    göster()
    dön(120)
    ileri(100)
}"""

dez kod130 = """
// İngilizce'den çevirdiğimiz bazı türlerin adı
// İngilizce olabilir. Şaşırmatsın seni!
yazı(Dizi(1, 2, 3))"""

dez kod140 = """sil()
üçgen()
yaklaş(2, 100, 50)"""

dez kod140b = """// bu da tam tersi:
yaklaş(0.4)"""

dez kod150 = """sil
konumuKur(100, 0)
hızıKur(yavaş)
daire(); durakla(0.5)
hızıKur(orta)
daire(50); durakla(0.5)
hızıKur(hızlı)
daire(100); durakla(0.5)
hızıKur(çokHızlı)
yinele(50) {
    daire(200)
    kalemRenginiKur(rastgeleRenk)
    konumuDeğiştir(3, 0)
}"""
