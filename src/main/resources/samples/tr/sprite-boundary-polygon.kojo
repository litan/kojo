silipSakla()
çıktıyıSil()
ızgarayıGöster
eksenleriGöster

// bir köstüm etrafına şöyle bir çokgen tanımlamak istiyoruz:
// dez örnek = Dizik(-0.5, -4.0, 90.5, -0.0, 77.5, 98.0, 110.5, 137.0, 105.5, 164.0, 70.5, 154.0, 45.5, 182.0, 25.5, 181.0, -2.5, 102.0, -0.5, -4.0)

// örnek köstüm:
dez görünüş = Resim.imge(Görünüş.kadınElSallarken)
// kendi resmini diskten şöyle yükleyebilirsin
// dez resimDizini = "/senin/verilerin/resimler"
// dez görünüş = Resim.imge(s"$resimDizini/resim.png")

çiz(görünüş)
// dizik ögeleri Kesir ya da UfakKesir olarak yazılabilir
dez kesirDiziği = doğru // UfakKesir için yanlış olmalı
// dez ufakKesirliÖrnek = Dizik(-47.5f, -19.0f, 141.5f, -3.0f, 146.5f, 207.0f, -2.5f, 188.0f, -47.5f, -19.0f)

satıryaz("10. satırı değiştirerek istediğin kostümü yükle.\n")
satıryaz("Sonra onun etrafına tıklayarak bir çokgen çiz. Tıklamadan önce\ngerekirse tuvalde resme yaklaşabilirsin.\n")

satıryaz("Son noktayı girmek için çift tıkla.\n")
satıryaz("Son nokta kendiliğinden ilk noktaya bağlanır. Bu da çokgeni tamamlar.\n")

den noktalar = EsnekDizik.boş[Nokta]
den noktaResmi: Resim = Resim.dikdörtgen(0, 0)
noktaResmi.çiz()

tanım çerçeveyiGüncelle() {
    dez ilkNokta = noktalar.başı
    dez noktaResmi2 = Resim.dizi(
        Resim.daire(5)
            .veKalemRengiyle(mavi).veBoya(mavi)
            .veKondur(ilkNokta.x, ilkNokta.y),
        Resim.noktadan { s =>
            s.başla()
            noktalar.herbiriİçin { n =>
                s.nokta(n.x, n.y)
            }
            s.bitir()
        }.veKalemRengiyle(mavi).veKalemKalınlığıyla(5)
    )
    noktaResmi2.çiz()
    noktaResmi.sil()
    noktaResmi = noktaResmi2
    noktaResmi.çizBaştan()
}

fareyeTıklıyınca { (x, y) =>
    noktalar.ekle(Nokta(x, y))
    çerçeveyiGüncelle()

    dez noktaSayısı = noktalar.boyu
    eğer (noktalar(noktaSayısı - 2) == noktalar(noktaSayısı - 1)) {
        noktalar.çıkar(noktaSayısı - 1)
        noktalar.ekle(noktalar.başı)
        dez nlar2 = noktalar
            .düzİşle(n => Dizik(n.x, n.y))
            .işle(n => f"$n%.1f" + (eğer (kesirDiziği) "" yoksa "f"))

        satıryaz("Çerçeve:\n")
        satıryaz(nlar2.yazıYap("Dizik(", ", ", ")"))
        satıryaz("\nBu çerçeveyi kendi yazılımında kullan.")
        çerçeveyiGüncelle()
        noktalar = EsnekDizik.boş[Nokta]
    }
}

tuvaliEtkinleştir()
