silVeSakla()
arkaplanıKurYatay(Renk(0, 0, 0), Renk(51, 204, 255))
// 
val boya = cm.radialMultipleGradient(
    0, 0, 150,
    Seq(0, 0.7, 1),
    Seq(Renk(255, 0, 0, 245), Renk(215, 0, 0, 245), Renk(185, 0, 0, 245)),
    doğru
)
// pen: kalem demek. Rengini ve kalınlığını giriyoruz. Sonra da boyasını
val resim = penColor(beyaz) * penWidth(2) * fillColor(boya) -> Picture {
    yinele(6120 / 85) {
        ileri(250)
        sağ(85)
    }
}
// Spot ışığı, yani ışığı az dağılan bir lambayla aydınlatalım
val l1 = SpotLight(0.9, 0.5, 180, 30, 400)
// noise gürültü demek. sanki bir beton gibi pürüzlü gösterelim
val resim2 = lights(l1) * noise(40, 1) -> resim
// tam ortaya çizelim:
drawCentered(resim2)
