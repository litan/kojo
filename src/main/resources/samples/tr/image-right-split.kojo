// Kojo'nun bildiği resimlerden biri el sallayan kadın
// Onu birkaç kez kullanarak bileşik bir resim oluşturalım

tanım imge = Resim.imge(Çizim.kadınElSallarken)
/* şunlardan herhangi birini de deneyebilirsin:
      Çizim.yarasa1 Çizim.araba Çizim.kalem ... */
// ya da kendi bilgisayarından bir resim dosyasını da kullanabilirsin:
// tanım imge = Resim.imge("/Users/ben/Resimler/deneme1.png")

tanım yanına(r1: Resim, r2: Resim) = büyüt(0.5, 1) -> Resim.diziYatay(r1, r2)
tanım üstüne(r1: Resim, r2: Resim) = büyüt(1, 0.5) -> Resim.diziDikey(r2, r1)

tanım resim(s: Sayı): Resim = {
    eğer (s <= 1)
        imge
    yoksa
        yanına(imge, üstüne(resim(s - 1), resim(s - 1)))
}

silVeSakla()
çiz(resim(7))
