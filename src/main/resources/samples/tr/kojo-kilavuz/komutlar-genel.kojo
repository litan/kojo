nesne komutlarGenel {

  dez gülerYüz: Yazı = "☺"

  dez kod10 = """dez gülerYüz: Yazı = "☺"
dez girdi = satıroku("Adınız nedir? ")
satıryaz("Merhaba " + girdi + s" $gülerYüz!" + " Ne var ne yok?")"""

  dez kod15 = """belirt(1 < 2, "bir genelde ikiden küçük olmalı")
// 1 yerine 3 yazıp tekrar çalıştır ki belirtmek istediğimiz durum yanlış olunca ne olduğunu görelim
"""

  dez kod20 = """durum sınıf Kişi(ad: Yazı, yaş: Sayı) {
    gerekli(yaş > 0 && yaş < 1000, "kişinin yaşı yanlış girildi")
    satıryaz(s"$ad $yaş yaşında")
}
çıktıyıSil
dez k1 = Kişi("Mustafa Kemal", 143)
 // bir de 1 yerine -1 girip çalıştır
dez k2 = Kişi("Garip Durum", 1)"""

  dez sayfa = Page(
    name = "daha çok komut var",
    body = tPage("Genel yazılım komutları",
      "Bazı basit komutlar çok faydalıdır. Sık kullanılanları burada görelim.".p,
      table(
        row("yaz".c, """yaz("Selam!")""".c, "yaz('a')".c, "yaz(kaplumbağa0)".c ),
        row("satıryaz".c, """satıryaz("Merhaba!")""".c),
        row("satıroku".c, kod10.c),
        row("belirt(belit, mesaj)".c, kod15.c),
        row("gerekli(koşul, mesaj)".c, kod20.c),
      ),
      "İngilizce karşılıkları da burada:".p,
      table(
        row("yaz(nesne)".c, "print(object)".c),
        row("satıryaz(nesne)".c, "println(object)".c),
        row("satıroku(istem)".c, "readln(prompt)".c),
        row(""),
        row("belirt(belit, mesaj)".c, "assert(assumption, message)".c),
        row("gerekli(koşul, mesaj)".c, "require(condition, message)".c),
      ),
      "Daha çok komut var, inşallah bir ara hepsini ekleriz buraya.".p,
      gülerYüz.p
    )
  )

}
