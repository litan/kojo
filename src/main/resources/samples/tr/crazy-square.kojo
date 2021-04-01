// Aditya Pant'ın katkısıdır.
// Ok tuşlarıyla çılgın kareye yön verebilirsin.
// Öndeki küçük kırmızı fara dikkat et. Yukarı ok kareyi farın olduğu yöne
// doğru hızlandırıyor. Aşağı ok tam tersine. Sağ ve sol okları da tahmin edersin.
// Tuvalin kenarlarına 60 saniye süresince çarpmamaya çalış.

val oyunSüresi = 60
val hız = 10 // rastgele hareketin hızı. Kolaylaştırmak için bunu azaltabilirsin
val itiş = 15 // tuşlarla kareye verilen hız

tümEkran()
silVeSakla()
çizSahne(sarı)

val çılgın = Resim {
    def kare(boy: Sayı) = yinele(4) { ileri(boy); sağ(90) }
    boyamaRenginiKur(mavi)
    kalemRenginiKur(mavi)
    kare(50)
    zıpla(50); sağ; zıpla(20); sol
    boyamaRenginiKur(kırmızı)
    kare(10)
}
çiz(çılgın)

canlandır {
    val yatayKayış = rastgele(hız) + 1
    val dikeyKayış = rastgele(hız) + 1
    çılgın.götür(yatayKayış, dikeyKayış)
    çılgın.döndür(.5)
    if (çılgın.çarptıMı(Resim.tuvalSınırları)) {
        çizMerkezdeYazı("Bir daha dene", kırmızı, 30)
        durdur()
    }
    if (tuşaBasılıMı(tuşlar.VK_LEFT)) {
        çılgın.götür(-itiş, 0)
    }
    if (tuşaBasılıMı(tuşlar.VK_UP)) {
        çılgın.götür(0, itiş)
    }
    if (tuşaBasılıMı(tuşlar.VK_RIGHT)) {
        çılgın.götür(itiş, 0)
    }
    if (tuşaBasılıMı(tuşlar.VK_DOWN)) {
        çılgın.götür(0, -itiş)
    }
}

oyunSüresiniGöster(oyunSüresi, "Tebrikler!", yeşil, 30)
tuvaliEtkinleştir()

/* 

Yukarıda iki hazır kojo komudu kullanarak işimizi kolaylaştırdık:

    çizMerkezdeYazı("Bir daha dene", kırmızı, 30)
    oyunSüresiniGöster(oyunSüresi, "Tebrikler!", yeşil, 30)
    
Birincisi epey basit. Verilen mesajı tuvalin tam ortasına verilen boy 
ve renkte yazıyor.

İkincisi epey becerikli. Oyunun süresini kuruyor, geçen süreyi tuvalin sol alt 
köşesinde gösteriyor ve süre tamamlanınca verilen mesajı istenilen renk ve boyda
yazıyor. Bunları kojo'nun sunduğu daha temel komutlarla yazmak da mümkün. 
Bana inanmazsan okumaya devam et! Kurcalayıp değiştirmek istersen hiç çekinme.
Oyunda da kullanmak için yukarıda kullandığımız hazır komut adlarına 2 sayısını 
eklemen yeter. Ama bir de oyunSüresiniGöster2 komudunu tanımından sonraya taşıyıver.

val ta = tuvalAlanı
def çizMerkezdeYazı2(yazı: Yazı, renk: Renk, boy: Sayı) {
    val uzunluk = yazıÇerçevesi(yazı, boy).width // yazının uzunluğu. Yüksekliği için height metodu da var..
    val resim = kalemRengi(renk) * götür(ta.x + (ta.eni - uzunluk) / 2, 0) ->
        Resim.yazı(yazı, 30)
    çiz(resim)
}
def oyunSüresiniGöster2(oyunSüresi: Sayı, mesaj: Yazı, renk: Renk, boy: Sayı) {
    var geçenSüre = 0
    val süreTanıtımı = götür(ta.x + 10, ta.y + 50) -> Resim.yazıRenkli(geçenSüre, 20, mavi)
    çiz(süreTanıtımı)
    süreTanıtımı.girdiyiAktar(Resim.tuvalBölgesi)

    yineleSayaçla(1000) { // her bin milisaniyede bir yinelenecek bu döngü
        geçenSüre += 1
        süreTanıtımı.güncelle(geçenSüre)

        if (geçenSüre == oyunSüresi) {
            çizMerkezdeYazı2(mesaj, renk, boy)
            durdur()
        }
    }
}
*/
