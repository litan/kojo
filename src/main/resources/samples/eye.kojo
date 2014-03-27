clear()
setAnimationDelay(0)
setBackgroundH(Color(0, 0, 0), blue)
setPenColor(Color(255, 102, 102))
setPenThickness(2)
changePosition(-80, -150)
setFillColor(
    ColorRadialG(0, 0, 150, Seq(0, 0.7, 1), Seq(Color(255, 0, 0, 245), Color(215, 0, 0, 245), Color(185, 0, 0, 245)), true)
)
repeat(6120 / 85) {
    forward(250)
    right(85)
}
