// Benzetim yolculuğu, adım 9/12: aynı benzetim, Yöney ve Cisim türleriyle.
// Dört cisim: iki çift ikiz yıldız. Renkler saydam olduğu için yörüngeler
// üst üste binince yeni renkler çıkıyor. fırça'yı 0 yaparsanız yalnız cisimleri
// görürsünüz; büyütürseniz yörüngeler kalınlaşır.
dez (kon, boy, ilkHız, yerçekimiSabiti, örnekle) = (50.0, 5.0, 1.0, 100.0, 4)
dez fırça = 6           // yörüngeleri boyar. 0 = kapalı
dez saydamlık = 50
dez yumuşatma = boy * 3 // en yakın yaklaşma sınırı: çekim bundan yakında artık büyümez

dez kırmızı = renkKur(255, 0, 0, saydamlık)
dez yeşil = renkKur(0, 255, 0, saydamlık)
dez mavi = renkKur(0, 110, 255, saydamlık)
dez turuncu = renkKur(255, 255, 0, saydamlık) // aslında sarı

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

tanım serpil = rastgele(10) // ikizleri birbirinin tıpkısı olmaktan kurtaran küçük kayma
dez cisimler = Diz(
  Cisim(1.0, Nokta(kon + serpil, kon + serpil), Yöney(ilkHız, 0.0), Resimcik(boy, kırmızı)),
  Cisim(1.0, Nokta(-kon + serpil, -kon + serpil), Yöney(-ilkHız, 0.0), Resimcik(boy, mavi)),
  Cisim(1.0, Nokta(-kon + serpil, kon + serpil), Yöney(0.0, 0.0), Resimcik(boy, yeşil)),
  Cisim(1.0, Nokta(kon + serpil, -kon + serpil), Yöney(0.0, 0.0), Resimcik(boy, turuncu)),
)

tanım dv(p1: Nokta, p2: Nokta, kütle2: Kesir) = { // hızdaki değişim (ivme)
  dez v = Yöney(p2.x - p1.x, p2.y - p1.y)
  dez uz = enİrisi(yumuşatma, v.uzunluk)
  v.ölçekle(yerçekimiSabiti * kütle2 / (uz * uz * v.sıfırdanUzak))
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