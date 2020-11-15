sil()
hızıKur(çokHızlı)
arkaplanıKurYatay(Renk(0, 0, 0), mavi)
kalemRenginiKur(Renk(255, 102, 102))
kalemKalınlığınıKur(2)

// cm: color manager, yani renk idarecisi anlamına geliyor
// onun yöntemlerinden birini deneyelim:
val boya = cm.radialMultipleGradient(0, 0, 150,
  Seq(0, 0.7, 1),
  Seq(cm.rgba(255, 0, 0, 245), cm.rgba(215, 0, 0, 245), cm.rgba(185, 0, 0, 245)),
  true
)
boyamaRenginiKur(boya)

atla(-100, -110)
yinele(6120 / 85) {
    ileri(250)
    sağ(85)
}
