// Benzetim yolculuğu, adım 10/12: aynı kod, başka başlangıç.
// Kararlı üçlü. Gerçek üçlü yıldız sistemleri genelde böyle kurulu: birbirine
// yakın bir ikili, ve ikisini birden uzaktan dolanan üçüncü bir cisim. Kaotik
// üçlüyle (Üç Cisim örneği) aradaki tek fark başlangıç değerleri -- kod aynı.
// Buradaki hızlar dairesel yörünge hızları; ikili kendi çevresinde döner, üçüncü
// onları bir gezegen gibi dolanır. 60 bin adım boyunca ölçüldü: yörünge dönemli.
dez (ikiliYarıçapı, uzakYarıçap, boy, ölçek, örnekle) = (20.0, 150.0, 5.0, 100.0, 4)
dez (ikiliKütle, uzakKütle) = (1.0, 0.2)
dez fırça = 0.5         // yörünge kalınlığı. 0 çizmez
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
tuvaliYakınlaştır(0.8, 0.8, 0, 0) // üçüncünün yörüngesi tuvale sığsın

// Dairesel yörünge hızları. İkili: her biri ortak merkezden ikiliYarıçapı kadar
// uzakta, çekim ölçek*kütle/(2*yarıçap)², merkezcil ivme hız²/yarıçap.
dez ikiliHızı = karekökü(ölçek * ikiliKütle / (4 * ikiliYarıçapı))
// Üçüncü: ikiliyi tek bir 2*kütle'lik cisim sayarsak hız = karekökü(ölçek*2*kütle/uzaklık)
dez uzakHız = karekökü(ölçek * 2 * ikiliKütle / uzakYarıçap)
// İkiliye ters tepme verelim ki sistemin toplam momentumu sıfır kalsın, yoksa
// bütün resim yavaşça sağa kayar.
dez geriTepme = uzakKütle * uzakHız / (2 * ikiliKütle)

dez cisimler = Diz(
  Cisim(ikiliKütle, Nokta(ikiliYarıçapı, 0), Yöney(-geriTepme, ikiliHızı), Resimcik(boy, kırmızı)),
  Cisim(ikiliKütle, Nokta(-ikiliYarıçapı, 0), Yöney(-geriTepme, -ikiliHızı), Resimcik(boy, mavi)),
  Cisim(uzakKütle, Nokta(0, -uzakYarıçap), Yöney(uzakHız, 0), Resimcik(boy * 0.7, yeşil)),
)

tanım dv(p1: Nokta, p2: Nokta, kütle2: Kesir) = { // hızdaki değişim, ivme
  dez v = Yöney(p2.x - p1.x, p2.y - p1.y)
  dez uz = enİrisi(yumuşatma, v.uzunluk)
  v.ölçekle(ölçek * kütle2 / (uz * uz * v.sıfırdanUzak))
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