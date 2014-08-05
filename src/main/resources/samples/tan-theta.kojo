def plot(start: Double, end: Double,
         step: Double)(f: Double => Double) {
    setPosition(start, f(start))
    var curr = start
    while (curr <= end) {
        moveTo(curr, f(curr))
        curr += step
    }
}
cleari()
setAnimationDelay(0)
setBackgroundV(yellow, Color(255, 204, 0))
setPenThickness(0.01)
setFillColor(
    ColorG(0, 0, Color(0, 0, 0, 230),
        1, 1, Color(0, 102, 255), true))
plot(-12, 12, 0.1) { x => math.tan(x) }
zoomXY(40, 10, 0, 0)
