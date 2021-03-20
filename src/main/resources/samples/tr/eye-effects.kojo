silVeSakla()
artalanıKurYatay(Renk(0, 0, 0), Renk(51, 204, 255))
// ışığa merkezden eğim verme yöntemini kullanalım
val boya = renkler.radialMultipleGradient(
    0, 0, 150,
    Dizi(0, 0.7, 1),
    Dizi(Renk(255, 0, 0, 245), Renk(215, 0, 0, 245), Renk(185, 0, 0, 245)),
    doğru
)
val resim = kalemRengi(beyaz) * kalemBoyu(2) * boyaRengi(boya) -> Resim {
    yinele(6120 / 85) {
        ileri(250)
        sağ(85)
    }
}
// Beton gibi pürüzlü gösterelim
val resim2 = gürültü(40, 1) -> resim
// Bir de sahne ışığı tutalım
val resim3 = sahneIşığı(0.9, 0.5, 180, 30, 400) -> resim2
// tam ortaya çizelim
çizMerkezde(resim3)

/* Henüz Türkçeleştiremediğimiz beceriler varsa, onları da kullanmak mümkün:
 val picture = resim2.p
 val l1 = SpotLight(0.9, 0.5, 180, 30, 400)
 val picture2 = lights(l1) -> picture
 çizMerkezde(new Resim(picture2))
 */
