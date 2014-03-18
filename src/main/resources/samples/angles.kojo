// An animation that tries to show:
// What are angles and how are they measured?
val pauseTime = 1 // seconds
val rLen = 100.0
val smallR = rLen / 4
val radColor = blue
val angleColor = Color(0, 204, 51)
val oldAngleColor = lightGray
val arcColor = gray
def radius(a: Int) = penColor(radColor) * rot(a) -> GPics(
    fillColor(radColor) -> PicShape.circle(3),
    PicShape.hline(rLen),
    trans(rLen, 0) * fillColor(radColor) -> PicShape.circle(3)
)

def curvedRadius(start: Double, a: Double) = penColor(radColor) * rot(start) -> GPics(
    trans(rLen, 0) * fillColor(radColor) -> PicShape.circle(3),
    PicShape.arc(rLen, a),
    rot(a) * trans(rLen, 0) * fillColor(radColor) -> PicShape.circle(3)
)

def arc(a: Int) = penColor(arcColor) -> PicShape.arc(rLen, a)

def angle(start: Double, a: Double) = rot(start) -> GPics(
    PicShape.hline(rLen),
    rot(a) -> PicShape.hline(rLen),
    PicShape.arc(rLen / 4, a)
)

var currArc = arc(0)
var currRadius = radius(0)
var rLabel = trans(rLen / 2, -5) -> PicShape.text("r", 20)
var currAngle: Picture = PicShape.arc(0, 0)
var aLabel: Picture = PicShape.text("", 20)
var pointer: Picture = PicShape.hline(0)
var pointer2: Picture = PicShape.hline(0)

def lineFrom(x1: Double, y1: Double, x2: Double, y2: Double) = {
    val distance = math.sqrt(math.pow(x2 - x1, 2) + math.pow(y2 - y1, 2))
    val angle = math.atan((y2 - y1) / (x2 - x1))
    trans(x1, y1) * rot(angle.toDegrees) -> PicShape.hline(distance)
}
def drawRadianAngle(e: Int) {
    def label = if (e == 1) s"$e radian" else s"$e radians"
    pause(pauseTime)
    if (e != 1) {
        currRadius = curvedRadius((e - 1).toDouble.toDegrees, 1.toDouble.toDegrees)
        draw(currRadius)
    }
    currAngle.setPenColor(oldAngleColor)
    currAngle = penColor(angleColor) -> angle(0, e.toDouble.toDegrees)
    draw(currAngle)
    aLabel.erase()
    pointer.erase()
    pointer2.erase()
    aLabel = trans(-18, -rLen / 4) -> PicShape.text(label, 20)
    draw(aLabel)
    if (e < 4) {
        pointer = lineFrom(0, -smallR, smallR - (smallR - smallR * math.cos(30.toRadians)), smallR * math.sin(30.toRadians))
        draw(pointer)
        pointer2 = lineFrom(0, -smallR, rLen - (rLen - rLen * math.cos(30.toRadians)), rLen * math.sin(30.toRadians))
        draw(pointer2)
    }
}

def drawPiAngle(e: Int) {
    def label = if (e == 1) s"π radians" else s"${e}π radians"
    pause(pauseTime)
    currRadius = if (e == 1)
        curvedRadius(3.toDouble.toDegrees, (math.Pi - 3).toDegrees)
    else
        curvedRadius(6.toDouble.toDegrees, (2 * math.Pi - 6).toDegrees)
    draw(currRadius)
    currAngle.setPenColor(oldAngleColor)
    currAngle = penColor(angleColor) -> angle(0, (math.Pi * e).toDegrees)
    draw(currAngle)
    aLabel.erase()
    aLabel = trans(-18, -rLen / 4) -> PicShape.text(label, 20)
    draw(aLabel)
}

var snapCtr = 1
def snapImage() {
    pause(0.5)
//    exportImage(s"angles-$snapCtr")
    snapCtr += 1
}

cleari()
draw(currRadius)
pause(pauseTime)
draw(rLabel)
snapImage()
repeatFor(1 to 360) { i =>
    currRadius.rotate(1)
    currArc.erase()
    currArc = arc(i)
    draw(currArc)
    pause(0.001)
}
pause(pauseTime)
snapImage()
rLabel.setPosition(rLen + 5, rLen / 2 + 10)
currRadius.rotateAboutPoint(-90, rLen, 0)

pause(pauseTime)
snapImage()
currRadius.erase()

currRadius = curvedRadius(0, 1.toDegrees)
draw(currRadius)
snapImage()

repeatFor(1 to 3) { e =>
    drawRadianAngle(e)
    snapImage()
}
drawPiAngle(1)
snapImage()
repeatFor(4 to 6) { e =>
    drawRadianAngle(e)
    snapImage()
}
drawPiAngle(2)
snapImage()
