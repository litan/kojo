// örnek/template -- sayfa eklemek için bu dosyayı kopyala ve sonra
// ../kojo-documentation.kojo içine yükle:
// # yükle ~/kojo/include/kojo-kilavuz/ek-sayfa.kojo
// ve şu satıra ekle:
//   dez öykü = Story(..., ek.sayfa, ...)

nesne ek {

  dez gülerYüz: Yazı = "☺"

  dez kod10 = """dez girdi = satıroku("Adınız nedir? ")
satıryaz("Merhaba " + girdi + "! Ne var ne yok?")"""

  dez sayfa = Page(
    name = "daha çok komut var",
    body = tPage("Ek Sayfa",
      "Yine küçük örneklerle genel yazılımlar yazmak için çok faydalı olan birkaç komut öğrenelim...".p,
      table(
        row("yaz".c, "yaz(kaplumbağa0)".c, "yaz('a')".c, """yaz("Selam!")""".c),
        row("satıryaz".c, """satıryaz("Merhaba!")""".c),
        row("satıroku".c, kod10.c),
        row("".c),
      ),
      "İngilizce karşılıkları da burada:".p,
      table(
        row("yaz".c, "print".c),
        row("satıryaz".c, "println".c),
        row("satıroku".c, "readln".c),
      ),
      "Daha çok komut var, inşallah yakında hepsini ekleriz buraya.".p,
      gülerYüz.p
    )
  )

}
