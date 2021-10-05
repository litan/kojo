cleari()
setBackground(white)
val cb = canvasBounds

val baseLen = 150

val msg = """
In the figure above, think of red circle as the position of the turtle
and the blue circle as the direction in which its nose is pointed.
Initially, the turtle's nose is pointed upwards.

Move the blue circle around to experiment with the input angle needed
for the right(?) command - to turn the turtle's nose
towards the blue circle (from it's initial upward direction).
"""
val info = Picture.text(msg)
info.setPenFontSize(16)
info.setPenColor(darkGray)
val te = textExtent(msg, 16).height
info.setPosition(cb.x + 10, cb.y + te + 10)

def anglePicMaker(x: Double, y: Double) = Picture {
    setPenColor(black)
    savePosHe()
    forward(baseLen)
    hop(-baseLen)
    lineTo(x, y)
    val angle = math.round(360 - heading + 90).toInt % 360
    restorePosHe()
    write(f"$angle%4d°")
    val arcRadius = 50
    hop(arcRadius)
    right(90)
    right(angle, arcRadius)
}

var anglePic = anglePicMaker(0, baseLen)
anglePic.setPosition(0, 0)

val turtlePos = fillColor(cm.tomato) * penColor(black) -> Picture.circle(5)
val lineEnd = fillColor(green) * penColor(black) -> Picture.circle(5)
lineEnd.setPosition(0, baseLen)

var hotSpot = Picture.circle(10)
hotSpot.setFillColor(blue)
hotSpot.setPenColor(black)
hotSpot.setPosition(0, baseLen)
hotSpot.onMouseDrag { (x, y) =>
    anglePic.erase()
    anglePic = anglePicMaker(x, y)
    draw(anglePic)
    hotSpot.setPosition(x, y)
    anglePic.moveToBack()
}

val showButton = trans(cb.x + 10, cb.y + 10) -> picStackCentered(
    fillColor(cm.lightBlue) * penColor(darkGray) -> Picture.rectangle(100, 40),
    Picture.vgap(5),
    penColor(black) -> Picture.text("Show Help")
)
showButton.onMouseClick { (x, y) =>
    showButton.erase()
    draw(info)
}

draw(showButton, turtlePos, anglePic, hotSpot, lineEnd)
