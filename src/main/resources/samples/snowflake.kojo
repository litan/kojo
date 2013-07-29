def triLine(n: Double, iter: Int) {
    if (iter == 1) {
        forward(n)
    }
    else {
        triLine(n / 3, iter-1)
        left(60)
        triLine(n / 3, iter-1)
        right(120)
        triLine(n / 3, iter-1)
        left(60)
        triLine(n / 3, iter-1)
    }
}

def kochFlake(n: Int, iter: Int) {
    right(30)
    repeat(3) {
        triLine(n, iter)
        right(120)
    }
}

cleari()
setPenThickness(1)
setPenColor(gray)
setFillColor(lightGray)
setAnimationDelay(50)
setPosition(-150, -50)
kochFlake(300, 5)
