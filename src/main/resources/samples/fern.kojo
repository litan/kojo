def fern(x: Double) {
    if (x > 1) {
        saveStyle()
        setPenThickness(x / 10 + 1)
        setPenColor(Color(0, math.abs(200 - x * 3).toInt, 40))
        forward(x)
        right(100)
        fern(x * 0.4)
        left(200)
        fern(x * 0.4)
        right(95)
        fern(x * 0.8)
        right(5)
        back(x)
        restoreStyle()
    }
}

def fernp = Picture {
    fern(50)
}

cleari()
setAnimationDelay(10)
setBackgroundV(Color(255, 255, 150), white)
val pic = picCol(
      flipX -> (fade(280) * blur(2) -> fernp),
      trans(0, 5) -> fernp
)
draw(pic)
