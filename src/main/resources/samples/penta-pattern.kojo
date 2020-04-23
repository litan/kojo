clear()
setSpeed(fast) // speed up the turtle
setPenColor(cm.gray)
var clr = cm.rgba(255, 0, 0, 127) // start with a semi transparent red color
repeat(18) {
    setFillColor(clr)
    repeat(5) {
        forward(100)
        right(72)
    }
    clr = clr.spin(360 / 18) // change color hue
    right(360 / 18)
}
