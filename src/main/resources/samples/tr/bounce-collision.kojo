// Benzetim yolculuğu, adım 12/12: gerçek bilardo.
// 3. adımdaki masaya geri döndük, ama artık elimizde Cisim ve Yöney var: toplar
// yalnız duvardan değil birbirinden de sekiyor. Eşit kütleli iki top çarpışınca
// merkezlerini birleştiren doğrultudaki hız bileşenlerini TAKAS eder; o doğrultuya
// dik olan bileşen hiç değişmez. Bütün çarpışma kuralı bundan ibaret.
dez (yç, sürtünme) = (12.0, 0.9995)
dez (solKenar, sağKenar) = (-200.0, 200.0)
dez (enAltKnr, enÜstKnr) = (-100.0, 100.0)

durum sınıf Resimcik(boy: Kesir, renk: Renk) { dez dönüşüm = kalemRengi(renk) * boyaRengi(renk) }
durum sınıf Yöney(den x: Kesir, den y: Kesir) {
  tanım ekle(v2: Yöney) = { x += v2.x; y += v2.y; bu }
  tanım ölçekle(c: Kesir) = { x *= c; y *= c; bu }
  tanım eksi(v2: Yöney) = Yöney(x - v2.x, y - v2.y) // yeni yöney: ekle gibi bunu değiştirmez
  tanım çarpım(v2: Yöney) = x * v2.x + y * v2.y     // iç çarpım
  tanım uzunluk = karekökü(x * x + y * y)
}
durum sınıf Cisim(kütle: Kesir, ilkKonum: Nokta, hız: Yöney, resim: Resimcik) {
  dez p = öteleme(ilkKonum.x, ilkKonum.y) * resim.dönüşüm -> Resim.daire(resim.boy)
  çiz(p)
}

sil(); gizle()
çiz(kalemRengi(siyah) * götür(solKenar, enAltKnr) ->
    Resim.dikdörtgen(sağKenar - solKenar, enÜstKnr - enAltKnr))

// Beyaz top soldan geliyor, öbür dördü karşıda dizili
dez cisimler = Diz(
  Cisim(1.0, Nokta(-150, 0), Yöney(4.0, 0.3), Resimcik(yç, gri)),
  Cisim(1.0, Nokta(60, 0), Yöney(0.0, 0.0), Resimcik(yç, kırmızı)),
  Cisim(1.0, Nokta(84, 13), Yöney(0.0, 0.0), Resimcik(yç, mavi)),
  Cisim(1.0, Nokta(84, -13), Yöney(0.0, 0.0), Resimcik(yç, yeşil)),
  Cisim(1.0, Nokta(108, 0), Yöney(0.0, 0.0), Resimcik(yç, mor)),
)

// İki top değiyorsa ve birbirine YAKLAŞIYORSA çarpıştır. Kütleler eşit olduğu için
// takas basit; eşit olmasaydı bileşenleri kütlelere göre paylaştırmak gerekirdi.
tanım çarpış(a: Cisim, b: Cisim): Birim = {
  dez (pa, pb) = (a.p.konum, b.p.konum)
  dez ara = Yöney(pb.x - pa.x, pb.y - pa.y)
  dez d = ara.uzunluk
  eğer (d > 0 && d < 2 * yç) {
    dez birim = Yöney(ara.x / d, ara.y / d) // merkezleri birleştiren doğrultu
    dez yaklaşma = a.hız.eksi(b.hız).çarpım(birim)
    eğer (yaklaşma > 0) { // uzaklaşıyorlarsa karışma, yoksa toplar birbirine yapışır
      a.hız.ekle(Yöney(-yaklaşma * birim.x, -yaklaşma * birim.y))
      b.hız.ekle(Yöney(yaklaşma * birim.x, yaklaşma * birim.y))
    }
    dez itme = (2 * yç - d) / 2 // üst üste binmeyi aç
    a.p.konumuKur(pa.x - birim.x * itme, pa.y - birim.y * itme)
    b.p.konumuKur(pb.x + birim.x * itme, pb.y + birim.y * itme)
  }
}

canlandır {
  için (b <- cisimler) {
    b.hız.ölçekle(sürtünme) // 5. adımdaki sürtünme: toplar sonunda duruyor
    b.p.konumuKur(b.p.konum.x + b.hız.x, b.p.konum.y + b.hız.y)
    dez k = b.p.konum // duvara doğru gidiyorsa sek; "değdi mi" demek yetmez, top yapışır
    eğer ((k.x <= solKenar + yç && b.hız.x < 0) || (k.x >= sağKenar - yç && b.hız.x > 0))
        b.hız.x = -b.hız.x
    eğer ((k.y <= enAltKnr + yç && b.hız.y < 0) || (k.y >= enÜstKnr - yç && b.hız.y > 0))
        b.hız.y = -b.hız.y
  }
  için (i <- cisimler.sıralar; j <- cisimler.sıralar eğer j > i)
    çarpış(cisimler(i), cisimler(j))
}
