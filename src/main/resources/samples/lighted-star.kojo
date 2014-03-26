def p = Picture {
    setPenColor(noColor)
    setFillColor(Color(0, 102, 255))
    repeat(4) {
        forward(300)
        right()
    }
    setFillColor(Color(255, 255, 51))
    setPosition(100, 100)
    repeat(5) {
        forward(100)
        right(720 / 5)
    }
}

cleari()
val l1 = SpotLight(0.2, 0.8, 300, 30, 130)
val l2 = SpotLight(0.5, 0.4, 0, 70, 80)
val pic = lights(l1, l2) -> p
draw(trans(-150, -150) -> pic)
