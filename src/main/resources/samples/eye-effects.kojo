cleari()
setBackgroundH(cm.rgb(0, 0, 0), cm.rgb(51, 204, 255))
val fill = cm.radialMultipleGradient(
    0, 0, 150,
    Seq(0, 0.7, 1),
    Seq(cm.rgba(255, 0, 0, 245), cm.rgba(215, 0, 0, 245), cm.rgba(185, 0, 0, 245)),
    true
)
val p = penColor(white) * penWidth(2) * fillColor(fill) -> Picture {
    repeat(6120 / 85) {
        forward(250)
        right(85)
    }
}
val l1 = SpotLight(0.9, 0.5, 180, 30, 400)
val pic = lights(l1) * noise(40, 1) -> p
drawCentered(pic)
