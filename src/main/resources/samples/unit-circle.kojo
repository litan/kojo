// by Bulent Basaran ben@scala.org 2022
toggleFullScreenCanvas()
val numTurns = 7
val superImpose = false // sine goes left to right. cosine goes top to bottom. this super imposes another cosine curve to go left to right as well
cleari
val (rb, rc) = (4.0, 160.0) // radius of the small ball in orbit and the radius of its circular orbit
zoomXY(0.6, 0.6, 600, -2 * rc)
val numPerTurn = 120
val angle = 360.0 / numPerTurn
val (x1, y1) = (-rc, rc) // (0, 0) doesn't work!
val (cBall, cCos, cSin) = (blue, red, green)
val ball = trans(x1, y1) * fillColor(cBall) * penColor(cBall) * penThickness(4) -> Picture.circle(rb)
draw(ball)
// outline of the orbit of the ball
draw(trans(-2*rc , rc) * penColor(lightGray) * penThickness(0.5) -> Picture.circle(rc))
// waves going left to right:
val sin = trans(0, ball.position.y) * fillColor(noColor) * penColor(noColor) -> Picture.circle(rb)
draw(sin); sin.invisible()
var cos: Picture = _
if (superImpose) {
    cos = trans(0, 2 * rc) * fillColor(noColor) * penColor(noColor) -> Picture.circle(rb)
    draw(cos); cos.invisible()
}
// wave going top to bottom:
val co2 = trans(ball.position.x, -rc) * fillColor(noColor) * penColor(noColor) -> Picture.circle(rb)
draw(co2); co2.invisible()
val axisColor = black.fadeOut(0.5)
draw(trans(-2 * rc, -14 * rc) * penColor(axisColor) -> Picture.line(0, 17 * rc)) // v axis
draw(trans(-4 * rc, -rc) * penColor(axisColor) -> Picture.line(3.8 * rc, 0)) // h axis
draw(trans(0, -0.8 * rc) * penColor(axisColor) -> Picture.line(0, 3.8 * rc)) // v axis
draw(trans(-4 * rc, rc) * penColor(axisColor) -> Picture.line(17 * rc, 0)) // h axis
for (i <- 0 to 14) {
    draw(trans(120 * i, rc) * penColor(cSin) -> Picture.circle(rb)) // zeros of sin(t)
    if (superImpose) draw(trans(60 + 120 * i, rc) * penColor(cCos) -> Picture.circle(rb)) // zeros of cos(t)
    draw(trans(-2 * rc, -120 * i - rc - 120 / 2) * penColor(cCos) -> Picture.circle(rb)) // zeros of cos2(t)
}
// arrow pointing from the origin (-2 * rc, rc) to the rotating ball
// origin == the center of the circular motion
draw(trans(-2 * rc, rc) * penColor(axisColor) -> Picture.circle(rb))
var arrow = trans(-2 * rc, rc) * penColor(black) -> Picture.line(rc, 0)
draw(arrow)
def updateArrow(p: Point) {
    arrow.erase()
    arrow = trans(-2 * rc, rc) * penColor(black) -> Picture.line(p.x + 2 * rc, p.y - rc)
    draw(arrow)
}
var arc = trans(-2 * rc, rc) * penColor(blue) -> Picture.arc(rc / 4, 0); draw(arc)
def updateArc(p: Point) {
    arc.erase()
    val angle0 = math.asin((p.y - rc) / rc).toDegrees
    val angle = if (p.x < -2 * rc) { // left hand side: Q2 and Q3
        180 - angle0
    }
    else if (p.y < rc) { // Q4
        360 + angle0
    }
    else // Q1
        angle0
    arc = trans(-2 * rc, rc) * penColor(blue) -> Picture.arc(rc / 4, angle)
    draw(arc)
}
// ball's projection on the horizontal axis == cosine of the angle:
var xProj = trans(0, 0) -> Picture.line(1, 1); draw(xProj)
var xTracer = trans(0, 0) -> Picture.line(1, 1); draw(xTracer); xTracer.invisible()
def updateCos(p: Point) {
    xProj.erase(); xTracer.erase()
    xProj = trans(-2 * rc, p.y) * penThickness(4) * penColor(cCos) -> Picture.line(p.x + 2 * rc, 0)
    xTracer = trans(-2 * rc, p.y) * penThickness(0.5) * penColor(lightGray) -> Picture.line(17 * rc, 0)
    draw(xProj, xTracer)
}
// ball's projection on the vertical axis == sine of the angle:
var yProj = trans(0, 0) -> Picture.line(1, 1); draw(yProj)
var yTracer = trans(0, 0) -> Picture.line(1, 1); draw(yTracer); yTracer.invisible()
def updateSin(p: Point) {
    yProj.erase(); yTracer.erase()
    yProj = trans(ball.position.x, rc) * penThickness(4) * penColor(cSin) -> Picture.line(0, p.y - rc)
    yTracer = trans(ball.position.x, p.y - 17 * rc) * penThickness(0.5) * penColor(lightGray) -> Picture.line(0, 17 * rc)
    draw(yProj, yTracer)
}
val startTime = epochTime
var t = 0.0
def newBall = penThickness(4.0) * penColor(cBall) -> Picture.circle(rb)
var lastBall = newBall
animate {
    t += 2
    // ball rotating around the center in uniform speed
    if (t > numTurns * 240 + numPerTurn / 6 ) { // stop at 60 degrees
        ball.erase()
        stopAnimations()
        println(s"$numTurns turns around the center lasted ${round(epochTime - startTime)} seconds")
    }
    sin.setPosition(t, ball.position.y)
    draw(trans(sin.position.x, sin.position.y) * penThickness(1.0) * penColor(cSin) -> Picture.circle(rb))
    co2.setPosition(ball.position.x, -1 * rc - t + 2)
    draw(trans(co2.position.x, co2.position.y) * penThickness(1.0) * penColor(cCos) -> Picture.circle(rb))
    if (superImpose) {
        cos.setPosition(t, 4 * rc + ball.position.x - rc)
        draw(trans(cos.position.x, cos.position.y) * penThickness(1.0) * penColor(cCos) -> Picture.circle(rb))
    }
    //updateBall()
    ball.rotateAboutPoint(angle, x1, 0)
    lastBall.erase()
    lastBall = trans(ball.position.x, ball.position.y) -> newBall
    draw(lastBall)
    updateArrow(ball.position)
    updateSin(ball.position)
    updateCos(ball.position)
    updateArc(ball.position)
}
draw(trans(40, -100) -> Picture.textu("Sine wave => ...", Font("JetBrains Mono", 40), cSin))
draw(trans(-30, -200) * rotp(-90, 0, 0) -> Picture.textu("Cosine wave => ...", Font("JetBrains Mono", 40), cCos))
