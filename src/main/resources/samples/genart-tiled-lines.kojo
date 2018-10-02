// Based on ideas from https://generativeartistry.com/tutorials/tiled-lines/

switchToDefault2Perspective()
cleari()
setBackground(Color(60, 63, 65))
val cb = canvasBounds
val n = 20
val deltax = cb.width / (n + 1)
val deltay = cb.height / (n + 1)

case class Line(x: Double, y: Double, w: Double, h: Double)

def linePic(l: Line) = penWidth(2) * penColor(white) * trans(l.x, l.y) ->
    Picture.line(l.w, l.h)

def line(x: Double, y: Double, w: Double, h: Double) = {
    val leftToRight = randomBoolean
    if (leftToRight) Line(x, y, w, h) else Line(x, y + h, w, -h)
}

var lines = Vector.empty[Line]

repeatFor(0 to n) { nx =>
    val x = cb.x + nx * deltax
    repeatFor(0 to n) { ny =>
        val y = cb.y + ny * deltay
        lines = lines :+ line(x, y, deltax, deltay)
    }
}

draw(lines.map(linePic))

