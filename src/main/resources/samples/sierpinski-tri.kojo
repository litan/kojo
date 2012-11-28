// standing up equilateral triangle
def triangle(n: Double) {
    right(30)
    repeat (3) {
        forward(n)
        right(120)
    }
    left(30)
}

def innerTriangles(n: Double, levels: Int) {
    def one2two() {
        right(30)
        forward(n/2)
        left(30)
    }
    
    def two2three() {
        right(120 + 30)
        forward(n/2)
        left(120 + 30)
    }
    
    def three2one() {
        left(90)
        forward(n/2)
        right(90)
    }
    
    val l = if (levels > 10) 10 else levels
    setPenThickness(l)
    setPenColor(color(l.toInt*20, l.toInt*25, 100))
    
    // left bottom (#1) inner triangle
    triangle(n/2)
    one2two()
    // top (#2) inner triangle
    triangle(n/2)
    two2three()
    // bottom right (#3) inner triangle
    triangle(n/2)
    three2one()

    if (levels == 1) return
    // do next level 

    // seir within bottom left
    innerTriangles(n/2, levels-1)
    one2two()
    
    // seir within top
    innerTriangles(n/2, levels-1)
    two2three()
    // seir within bottom right
    innerTriangles(n/2, levels-1)
    three2one()
}

def sierpinski(n: Double, levels: Int) {
    saveStyle()
    setFillColor(yellow)
    triangle(n)
    restoreStyle()
    innerTriangles(n, levels)
}

clear()
setAnimationDelay(10)
setPenColor(blue)
setPenThickness(1)
sierpinski(300, 5)
