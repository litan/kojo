cleari()
setBackgroundH(ColorMaker.hsl(210, 1.00, 0.1), ColorMaker.hsl(210, 1.00, 0.15))

val sqSize = 200
val diag = math.sqrt(2 * sqSize * sqSize)
val duration = 5 // seconds
val bg = cm.linearGradient(0, 0, ColorMaker.hsl(18, 1.00, 0.50), sqSize / 2, sqSize / 2, cm.gold)
val bg2 = cm.linearGradient(0, 0, cm.gold, sqSize / 2, sqSize / 2, ColorMaker.hsl(18, 1.00, 0.50))

def t(sz: Double) = Picture.fromPath { p =>
    p.moveTo(0, 0)
    p.lineTo(0, sz)
    p.lineTo(sz, 0)
    p.closePath()
}

def t1 = t(sqSize)
def t2 = t(diag / 2)

def xProp(s: Seq[Double]) = s(0)
def yProp(s: Seq[Double]) = s(1)
def rProp(s: Seq[Double]) = s(2)

def maket1(s: Seq[Double]) =
    t1.withRotation(rProp(s)).withTranslation(xProp(s), yProp(s))
        .withTranslation(-sqSize / 2, -sqSize / 2) // to center
        .withFillColor(bg)
        .withPenColor(darkGray)

def maket2(s: Seq[Double]) =
    t2.withRotation(rProp(s)).withTranslation(xProp(s), yProp(s))
        .withTranslation(-sqSize / 2, -sqSize / 2) // to center
        .withFillColor(bg2)
        .withPenColor(darkGray)

val anim1 = Transition(
    duration,
    Seq(-200, -200, 360),
    Seq(0, 0, 0),
    easing.Linear,
    maket1,
    false
)

val anim2 = Transition(
    duration,
    Seq(-200, 200, -360),
    Seq(sqSize / 2, sqSize / 2, 45),
    easing.QuadInOut,
    maket2,
    false
)

val anim3 = Transition(
    duration,
    Seq(200, 200, -360),
    Seq(sqSize / 2, sqSize / 2, -45),
    easing.QuadInOut,
    maket2,
    false
)

val anim = animPar(anim1, anim2, anim3)

run(anim)
