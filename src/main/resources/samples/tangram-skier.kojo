// a skier based on tangram pieces

val len = 4
val d = math.sqrt(2*len*len)
val d2 = d/2
val d4 = d/4

def p1 = Picture {
    forward(len)
    right(135)
    forward(d2)
    right()
    forward(d2)
}

def p2 = p1

def p3 = Picture {
    right()
    forward(len/2)
    left(135)
    forward(d4)
    left()
    forward(d4)
}

def p4 = p3

def p6 = Picture {
    repeat (4) {
        forward(d4)
        right()
    }
}


def p5 = Picture {
    right()
    forward(len/2)
    left()
    forward(len/2)
    left(135)
    forward(d2)
}

def p7 = Picture {
    right()
    forward(len/2)
    left(45)
    forward(d4)
    left(135)
    forward(len/2)
    left(45)
    forward(d4)
}

def ski = Picture {
    forward(3)
}

val skier = penColor(black) * trans(10, 1) * scale(0.6) -> GPics(
    trans(-2, -2) * rot(-75) -> ski,
    fillColor(purple) * rot(-120) -> p3,
    fillColor(yellow) * rot(150) * trans(0, -3.5) -> p1,
    fillColor(blue) * flipY * rot(120) * trans(1.5, 0) -> p7,
    fillColor(red) * rot(150) * trans(-1, -4.5) -> p5,
    fillColor(green) * rot(-165) * trans(-4.47, -3.9) -> p4,
    fillColor(orange) * rot(150) * trans(1, -6.5) -> p2,
    fillColor(red) * trans(-1.75, 5.4) * rotp(30, d4, 0) -> p6
)

def tile = Picture {
    right()
    forward(3)
}

val ground = penColor(brown) * trans(-13, -8) * rot(10) -> HPics(
    tile,
    trans(0, 0.5) -> tile,
    trans(0, 1) -> tile,
    trans(0, 1.5) -> tile,
    trans(0, 2) -> tile,
    trans(0, 2.5) -> tile,
    trans(0, 3) -> tile,
    trans(0, 3.5) -> tile
)

def toCm(p: Double) = 2.54 /96 * p

def tree(distance: Double) {
    if (distance > toCm(4)) {
        setPenThickness(distance/7)
        setPenColor(color(distance.toInt, math.abs(255-distance*3).toInt, 125))
        forward(distance)
        right(25)
        tree(distance*0.8-toCm(2))
        left(45)
        tree(distance-toCm(10))
        right(20)
        back(distance)
    }
}

def tp = Picture {
    tree(1.5)
}

def makeTrees(n: Int): Picture = {
    def mt(n: Int, sfactor: Double): Picture = {
        if (n == 1) {
            scale(sfactor) -> tp
        }
        else {
            scale(sfactor) -> HPics(
                tp,
                mt(n-1, sfactor)
            )
        }
    }
    mt(n, 0.9)
}

val trees = rot(7) * trans(-10, 0) -> makeTrees(9)
clearWithUL(Cm)
invisible()
draw(ground, trees, skier)

animate {
    if (skier.collidesWith(ground)) {
        skier.translate(-0.09, 0)
    }
    else {
        skier.translate(-0.09, -0.045)
    }
    if (skier.distanceTo(ground) > 1) {
        skier.setPosition(10, 1)
    }
}

skier.onMouseClick { (x, y) =>
    skier.setPosition(10, 1)
}
