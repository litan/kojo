// Example contributed by Łukasz Lew

def dragon(depth: Int, angle: Double): Unit = {
    if (depth == 0) {
        forward(10)
        return ;
    }

    left(angle)
    dragon(depth - 1, angle.abs)
    right(angle)

    right(angle)
    dragon(depth - 1, -angle.abs)
    left(angle)
}

clear()
setBackground(white)
setAnimationDelay(20)
setPenThickness(7)
setPenColor(Color(0x365348))

hop(-100)
dragon(10, 45)
