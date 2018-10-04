// Based on ideas from https://generativeartistry.com/tutorials/triangular-mesh/

switchToDefault2Perspective()
cleari()
setBackground(Color(60, 63, 65))
val cb = canvasBounds
val n = 10
val stepx = cb.width / (n + 1)
val stepy = cb.height / (n + 1)

case class Point(x: Double, y: Double)

def pointPic(p: Point): Picture = penWidth(0) * penColor(white) *
    trans(p.x, p.y) -> Picture.circle(1)

def point(x: Double, y: Double) = {
    Point(x, y)
}

val grays = 
    (1 to 16).map { n =>
        val n2 = n * 15
        Color(n2, n2, n2)
    }

def randomGray() = {
    grays(random(16))
}

def trianglePic(p1: Point, p2: Point, p3: Point) = penWidth(0) * penColor(white) *
    fillColor(randomGray) -> Picture.fromPath { path =>
        path.moveTo(p1.x, p1.y)
        path.lineTo(p2.x, p2.y)
        path.lineTo(p3.x, p3.y)
        path.closePath()
    }

var points = Array.ofDim[Point](n + 2, n + 2)

repeatFor(0 to n + 1) { ny =>
    val y = cb.y + ny * stepy
    repeatFor(0 to n + 1) { nx =>
        val x =
            if (ny % 2 == 0)
                cb.x + nx * stepx
            else
                cb.x + nx * stepx + stepx / 2
        points(nx)(ny) = point(
            x + randomDouble(1) * stepx - stepx / 2,
            y + randomDouble(1) * stepy - stepy / 2)
    }
}

var triangleRows = Vector.empty[Vector[Point]]

repeatFor(0 to n) { ny =>
    var triangleRow = Vector.empty[Point]
    repeatFor(0 to n) { nx =>
        if (ny % 2 == 0) {
            triangleRow = triangleRow :+ points(nx)(ny)
            triangleRow = triangleRow :+ points(nx)(ny + 1)
        }
        else {
            triangleRow = triangleRow :+ points(nx)(ny + 1)
            triangleRow = triangleRow :+ points(nx)(ny)
        }
    }
    triangleRows = triangleRows :+ triangleRow
}

triangleRows.foreach { row =>
    repeatFor(0 to row.length - 3) { n =>
        draw(trianglePic(row(n), row(n + 1), row(n + 2)))
    }
}
