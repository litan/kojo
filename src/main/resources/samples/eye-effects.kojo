cleari()
setAnimationDelay(0)
setBackgroundH(Color(0, 0, 0), Color(51, 204, 255))
changePosition(-80, -150)
val p = penColor(Color(255, 255, 255)) * penWidth(2) * fillColor(ColorRadialG(0, 0, 150, Seq(0, 0.7, 1), Seq(Color(255, 0, 0, 245), Color(215, 0, 0, 245), Color(185, 0, 0, 245)), true)) -> Picture {
    repeat(6120 / 85) {
        forward(250)
        right(85)
    }
}
val l1 = SpotLight(0.9, 0.5, 180, 30, 400)
val pic = trans(-100, -110) * lights(l1) * noise(40, 1) -> p
draw(pic)
