// Based on ideas from https://generativeartistry.com/tutorials/hypnotic-squares/

size(600, 600)
cleari()
val white = Color(0xF2F5F1)
setBackground(white)
setSpeed(superFast)
setPenColor(darkGray)
setPenThickness(3)
val cb = canvasBounds
val nx = 7
val ny = 7
val dx = cb.width / nx
val dy = cb.height / ny

val minSize = 3.0
def square(len: Double, startSteps: Int, steps: Int, xMov: Int, yMov: Int) {
    repeat(4) {
        forward(len)
        right(90)
    }
    if (steps >= 0) {
        val newSize = dx * steps / startSteps + minSize
        val pos = position
        var newX = pos.x + (len - newSize) / 2
        var newY = pos.y + (len - newSize) / 2
        val shiftx = ((newX - pos.x) / (steps + 2)) * xMov
        val shifty = ((newY - pos.y) / (steps + 2)) * yMov
        newX = newX + shiftx
        newY = newY + shifty
        setPosition(newX, newY)
        square(newSize, startSteps, steps - 1, xMov, yMov)
    }
}

def block(x: Int, y: Int) {
    setPosition(cb.x + x * dx, cb.y + y * dy)
    val startSteps = random(2, 8)
    val mx = random(-1, 2)
    val my = random(-1, 2)
    square(dx, startSteps, startSteps - 1, mx, my)
}

repeatFor(0 until nx) { x =>
    repeatFor(0 until ny) { y =>
        block(x, y)
    }
}
