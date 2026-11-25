// Benzetim yolculuğu, adım 11/12: baskın merkezî kütle.
// Güneş sistemi taslağı: ortada ağır bir yıldız, çevresinde dört küçük cisim.
// Bir cisim baskın biçimde ağır olunca üç-cisim kaosu yerine düzgün yörüngeler
// çıkıyor -- Üç Cisim örneğiyle karşılaştırın, kod neredeyse aynı, kütleler farklı.
// Güneş(kırmızı), kuyrukluyıldız1(yeşil), Dünya(mavi), kuyrukluyıldız2(mor), Mars(turuncu)
dez örnekle = 11 // 1 ve üstü; yörüngeleri ne kadar yoğun izleyeceğimiz

durum sınıf Resimcik(boy: Kesir, renk: Renk) { dez dönüşüm = kalemRengi(renk) * boyaRengi(renk) }
durum sınıf Yöney(den x: Kesir, den y: Kesir) { // konum, hız ya da kuvveti temsil edebilir
  tanım ekle(v2: Yöney) = { x += v2.x; y += v2.y; bu }
  tanım ölçekle(c: Kesir) = { x *= c; y *= c; bu }
  tanım uzunlukKaresi = x * x + y * y
  tanım uzunluk = karekökü(uzunlukKaresi)
  // Burada merkezde baskın bir kütle var, cisimler birbirine pek yaklaşmıyor;
  // o yüzden yumuşatma yerine yalnız sıfıra bölmeyi engellemek yetiyor.
  tanım uzunluk3 = enİrisi(0.0001, uzunluk * uzunlukKaresi)
}
durum sınıf Cisim(kütle: Kesir, ilkKonum: Nokta, hız: Yöney, resim: Resimcik) {
  dez p = öteleme(ilkKonum.x, ilkKonum.y) * resim.dönüşüm -> Resim.daire(resim.boy)
  çiz(p)
}

sil(); gizle()

dez (kon, boy, ilkHız) = (40.0, 3.0, 2.0)
dez cisimler = Diz(
  Cisim(1000, Nokta(0, 0), Yöney(0, 0), Resimcik(6 * boy, kırmızı)),
  Cisim(2.0, Nokta(kon, 2 * kon), Yöney(ilkHız, -1.5 * ilkHız), Resimcik(2 * boy, yeşil)),
  Cisim(3.0, Nokta(4 * kon, 2 * kon), Yöney(ilkHız / 2, -ilkHız), Resimcik(2 * boy, mavi)),
  Cisim(5.0, Nokta(-6 * kon, 2 * kon), Yöney(-ilkHız / 3, ilkHız), Resimcik(3 * boy, mor)),
  Cisim(6.0, Nokta(0, -7 * kon), Yöney(-ilkHız, 0), Resimcik(3 * boy, turuncu)),
)

tanım dv(p1: Nokta, p2: Nokta, kütle2: Kesir) = { // hızdaki değişim (ivme)
  dez v = Yöney(p2.x - p1.x, p2.y - p1.y)
  v.ölçekle(kütle2 / v.uzunluk3)
}

den adım = 0
canlandır {
  eğer (adım % örnekle == 0)
    için (b <- cisimler) çiz(öteleme(b.p.konum.x, b.p.konum.y) * b.resim.dönüşüm -> Resim.daire(2))
  adım += 1
  için (b <- cisimler) {
    için (öbürü <- cisimler eğer öbürü != b)
      b.hız.ekle(dv(b.p.konum, öbürü.p.konum, öbürü.kütle))
    b.p.konumuKur(b.p.konum.x + b.hız.x, b.p.konum.y + b.hız.y)
  }
}
tuvaliYakınlaştır(0.3, 0.3, kon, -kon)
