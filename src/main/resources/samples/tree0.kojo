cleari()
setSpeed(fast)
setBackground(white)

def branch(n: Double) {
    forward(n)
}

def tree(n: Double) {
    savePosHe()
    setPenColor(cm.rgb(n.toInt, math.abs(255 - n * 3).toInt, 125))
    if (n <= 4) {
        setPenThickness(0.5)
        branch(n / 2)
    }
    else {
        setPenThickness(n / 7)
        branch(n)
        right(25)
        tree(0.8 * n - 2)
        left(25)
        left(20)
        tree(n - 10)
    }
    restorePosHe()
}

hop(-200)
tree(90)
