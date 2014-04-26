clear()
setAnimationDelay(10) // speed up the turtle
setPenColor(gray)
var clr = Color(255, 0, 0, 127) // start with a semi transparent red color
repeat(18) {
    setFillColor(clr)
    repeat(5) {
        forward(100)
        right(72)
    }
    clr = hueMod(clr, 0.15) // change color hue
    right(360/18)
}
