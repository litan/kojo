// Based on ideas from https://generativeartistry.com/tutorials/piet-mondrian/

switchToDefault2Perspective()
cleari()
val cb = canvasBounds

val white = Color(0xF2F5F1)
val colors = Seq(Color(0xD40920), Color(0x1356A2), Color(0xF7D842))

case class Rectangle(x: Double, y: Double, width: Double,
                     height: Double, var c: Color = white)

def rectPic(r: Rectangle) = {
    penColor(black) * fillColor(r.c) * penWidth(10) * trans(r.x, r.y) ->
        Picture.rectangle(r.width, r.height)
}

def splitRectsX(rects: Vector[Rectangle], x: Double) = {
    var ret = rects
    repeatFor(rects.length - 1 to 0 by -1) { i =>
        val square = rects(i)
        if (x > square.x && x < square.x + square.width) {
            if (randomBoolean) {
                ret = ret.slice(0, i) ++ ret.slice(i + 1, ret.length)
                ret = ret ++ splitOnX(square, x)
            }
        }
    }
    ret
}

def splitRectsY(rects: Vector[Rectangle], y: Double) = {
    var ret = rects
    repeatFor(rects.length - 1 to 0 by -1) { i =>
        val square = rects(i)
        if (y > square.y && y < square.y + square.height) {
            if (randomBoolean) {
                ret = ret.slice(0, i) ++ ret.slice(i + 1, ret.length)
                ret = ret ++ splitOnY(square, y)
            }
        }
    }
    ret
}

def splitOnX(square: Rectangle, splitAt: Double) = {
    val squareA = Rectangle(
        square.x,
        square.y,
        square.width - (square.width - splitAt + square.x),
        square.height)

    val squareB = Rectangle(
        splitAt,
        square.y,
        square.width - splitAt + square.x,
        square.height)

    Vector(squareA, squareB)
}

def splitOnY(square: Rectangle, splitAt: Double) = {
    val squareA = Rectangle(
        square.x,
        square.y,
        square.width,
        square.height - (square.height - splitAt + square.y))

    val squareB = Rectangle(
        square.x,
        splitAt,
        square.width,
        square.height - splitAt + square.y)

    Vector(squareA, squareB)
}

var rects = Vector(Rectangle(cb.x, cb.y, cb.width, cb.height))
val n = 7
val deltax = cb.width / (n + 1)
val deltay = cb.height / (n + 1)

repeatFor(1 to n) { i =>
    rects = splitRectsX(rects, cb.x + i * deltax)
    rects = splitRectsY(rects, cb.y + i * deltay)
}
// println(s"rects made: ${rects.size}; drawing")
colors.foreach { c =>
    val idx = Math.floor(randomDouble(1) * rects.length).toInt
    rects(idx).c = c
}
draw(rects.map(rectPic))
