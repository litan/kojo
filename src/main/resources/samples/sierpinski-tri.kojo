cleari()
val offWhite = cm.hex(0xF2F5F1)
setBackground(offWhite)
setSpeed(superFast)
right(30)
setPenColor(cm.darkBlue.fadeOut(0.4))
setFillColor(cm.darkSeaGreen)

val size = 400

def triangle(n: Double) {
    repeat(3) {
        forward(n)
        right(120)
    }
}

def sier(n: Double) {
    savePosHe()
    if (n < 10) {
        triangle(n)
    }
    else {
        setPenThickness(25 * n / size)
        triangle(n)
        sier(n / 2)
        hop(n / 2)
        sier(n / 2)
        left(60)
        hop(-n / 2)
        right(60)
        sier(n / 2)
    }
    restorePosHe()
}

setPosition(-200, -150)
sier(size)
