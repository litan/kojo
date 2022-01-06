// Pulsating lamp/diya - using turtle pictures and transitions

cleari()
setBackground(cm.darkSlateBlue)

val flame = Picture {
    setFillColor(cm.linearGradient(0, 0, cm.red, 0, 130, cm.yellow))
    setPenColor(cm.yellow)
    setPenThickness(3)
    left(45)
    right(90, 100)
    right(90)
    right(90, 100)
}

val diya = Picture {
    setFillColor(cm.linearGradient(0, 10, cm.red, 0, -25, cm.brown))
    setPenThickness(2)
    setPenColor(cm.black)
    left(120)
    right(60, 100)
    right(180)
    right(30)
    left(120, 115)
    right(180)
    right(30)
    right(60, 100)
}

draw(diya)

def scaleProp(s: Seq[Double]) = s(0)

def makeflame(s: Seq[Double]) = {
    scale(scaleProp(s)) -> flame
}

val anim = Transition(1, Seq(1), Seq(0.8), easing.QuadInOut, makeflame, true)

val anim2 = animSeq(anim, anim.reversed).repeatedForever

run(anim2)