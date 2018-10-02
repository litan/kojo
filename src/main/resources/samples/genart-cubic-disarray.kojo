// Based on ideas from https://generativeartistry.com/tutorials/cubic-disarray/

switchToDefault2Perspective()
cleari()
setBackground(Color(60, 63, 65))
var cb = canvasBounds
zoomXY(1, -1, cb.width / 2, -cb.height / 2)
cb = canvasBounds
val n = 10
val stepx = cb.width / (n + 1)
val stepy = cb.height / (n + 1)

val randomDisplacement = 15
val rotateMultiplier = 20

case class Rectangle(w: Double, h: Double, r: Double, tx: Double, ty: Double)

def rectPic(r: Rectangle): Picture = penWidth(2) * penColor(white) * fillColor(darkGray) *
    trans(r.tx, r.ty) * rot(r.r) -> Picture.rectangle(r.w, r.h)

def rect(i: Double, j: Double, w: Double, h: Double) = {
    var plusOrMinus = if (randomBoolean) 1 else -1
    val rotAmt =
        j / cb.height * plusOrMinus * randomDouble(1) * rotateMultiplier

    plusOrMinus = if (randomBoolean) 1 else -1
    val translateAmt =
        j / cb.height * plusOrMinus * randomDouble(1) * randomDisplacement

    Rectangle(w, h, rotAmt, i + translateAmt, j)
}

var rects = Vector.empty[Rectangle]

repeatFor(0 to n) { ny =>
    val y = cb.y + ny * stepy
    repeatFor(0 to n) { nx =>
        val x = cb.x + nx * stepx
        rects = rects :+ rect(x, y, stepx, stepy)
    }
}

draw(rects.map(rectPic))
// zoomXY(0.9, -0.9, cb.width / 2, -cb.height / 2)
