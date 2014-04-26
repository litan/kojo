clear()
setAnimationDelay(10) // speed up the turtle
setPenColor(gray)
var clr = Color(255, 0, 0, 150) // start with a semi transparent red color
repeat(15) {
    setFillColor(clr)
    repeat(4) {
        forward(100)
        right(90)
    }
    clr = hueMod(clr, 0.05) // change color hue
    right(360/15)
}
