// Benzetim yolculuğu, adım 7/12: üçüncü cismi ekleyince kaos.
// Üç cisim, kaos. Üçlü yıldız sistemleri var; ama gerçekte genelde ikisi yakın,
// üçüncüsü büyük bir gezegen gibi uzak bir yörüngede oluyor (bkz. Kararlı Üçlü).
// Yoksa burada gördüğümüz gibi kaotik durumlar çıkıyor: üç cismin yörüngesi için
// kapalı bir çözüm yok. Başlangıç değerlerindeki en küçük oynama bile bir süre
// sonra bambaşka bir resim veriyor.
dez (knm, hız, boy, ölçek, örnekle) = (50.0, 1.0, 5.0, 100.0, 4)
dez fırça = 0.2         // yörünge kalınlığı. 0 çizmez
dez yumuşatma = boy * 3 // en yakın yaklaşma sınırı: çekim bundan yakında artık büyümez

durum sınıf Resimcik(boy: Kesir, renk: Renk) { dez dönüşüm = kalemRengi(renk) * boyaRengi(renk) }
durum sınıf Yöney(den x: Kesir, den y: Kesir) { // konum, hız ya da kuvveti temsil edebilir
  tanım ekle(v2: Yöney) = { x += v2.x; y += v2.y; bu }
  tanım ölçekle(c: Kesir) = { x *= c; y *= c; bu }
  tanım uzunlukKaresi = x * x + y * y
  tanım uzunluk = karekökü(uzunlukKaresi)
  tanım sıfırdanUzak = enİrisi(0.0001, uzunluk) // sıfıra bölmeyi önle
}
durum sınıf Cisim(kütle: Kesir, ilkKonum: Nokta, hız: Yöney, resim: Resimcik) {
  dez p = öteleme(ilkKonum.x, ilkKonum.y) * resim.dönüşüm -> Resim.daire(resim.boy)
  çiz(p)
}
sil(); gizle()

tanım dürt = rastgele(10) // üçlüyü tam bakışık olmaktan kurtaran küçük kayma
dez cisimler = Diz(
  Cisim(1.0, Nokta(knm + dürt, knm + dürt), Yöney(hız, 0.0), Resimcik(boy, kırmızı)),   // sağa fırlıyor
  Cisim(1.0, Nokta(-knm + dürt, -knm + dürt), Yöney(-hız, 0.0), Resimcik(boy, mavi)),   // sola
  Cisim(1.0, Nokta(-knm + dürt, knm + dürt), Yöney(0.0, 0.0), Resimcik(boy, yeşil)),    // başta duruyor
)

tanım dv(p1: Nokta, p2: Nokta, kütle2: Kesir) = { // hızdaki değişim, ivme
  dez v = Yöney(p2.x - p1.x, p2.y - p1.y)
  dez uz = enİrisi(yumuşatma, v.uzunluk)
  v.ölçekle(ölçek * kütle2 / (uz * uz * v.sıfırdanUzak)) // büyüklük ölçek*kütle/uz², yön birim
}

den adım = 1
canlandır {
  eğer (fırça > 0 && adım % örnekle == 1)
    için (b <- cisimler) çiz(öteleme(b.p.konum.x, b.p.konum.y) * b.resim.dönüşüm -> Resim.daire(fırça))
  adım += 1
  için (b <- cisimler) // önce bütün hızlar aynı görüntüden hesaplanır...
    için (öbürü <- cisimler eğer öbürü != b)
      b.hız.ekle(dv(b.p.konum, öbürü.p.konum, öbürü.kütle))
  için (b <- cisimler) // ...sonra herkes birlikte kıpırdar
    b.p.konumuKur(b.p.konum.x + b.hız.x, b.p.konum.y + b.hız.y)
}