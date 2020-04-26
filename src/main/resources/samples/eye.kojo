clear()
setSpeed(superFast)
setBackgroundH(cm.rgb(0, 0, 0), blue)
setPenColor(cm.rgb(255, 102, 102))
setPenThickness(2)
changePosition(-100, -110)
setFillColor(
    cm.radialMultipleGradient(0, 0, 150,
        Seq(0, 0.7, 1),
        Seq(cm.rgba(255, 0, 0, 245), cm.rgba(215, 0, 0, 245), cm.rgba(185, 0, 0, 245)),
        true
    )
)
repeat(6120 / 85) {
    forward(250)
    right(85)
}
