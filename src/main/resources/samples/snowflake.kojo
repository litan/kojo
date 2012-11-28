def lines(count: Int, length: Int) {
    if (count == 1) forward(length)
    else {
        lines(count-1, length)
        left(60)
        lines(count-1, length)
        right(120)
        lines(count-1, length)
        left(60)
        lines(count-1, length)
    }
}

def koch(count: Int, length: Int) {
    right(30)
    lines(count, length)
    right(120)
    lines(count, length)
    right(120)
    lines(count, length)
}

clear()
invisible()
setPenThickness(1)
setPenColor(color(128, 128, 128))
setFillColor(color(0xC9C0BB))
setAnimationDelay(50)
penUp()
back(100)
left()
forward(150)
right()
penDown()
koch(5, 5)
