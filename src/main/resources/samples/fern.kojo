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

clear()
setAnimationDelay(10)
setPenThickness(1.4)
setBackgroundV(Color(255, 255, 150), white)
hop(-100)
fern(60)
