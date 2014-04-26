// This example is based on Kojo Pictures
def p = PicShape.image(Costume.womanWaving) 
// def p = PicShape.image("filename")

def beside(p1: Picture, p2: Picture) = scale(0.5, 1) -> HPics(p1, p2)
def above(p1: Picture, p2: Picture) = scale(1, 0.5) -> VPics(p2, p1)

def pattern(n: Int): Picture = {
    if (n <= 1)
        p
    else
        beside(p, above(pattern(n - 1), pattern(n - 1)))
}

cleari()
draw(pattern(6))
