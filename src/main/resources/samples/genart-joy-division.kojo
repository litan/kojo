// Based on ideas from https://generativeartistry.com/tutorials/joy-division/

switchToDefault2Perspective()
cleari()
setBackground(Color(60, 63, 65))
val cb = canvasBounds
val n = 22
val stepx = cb.width / (n + 1) / 2
val stepy = cb.height / (n + 1)

case class Point(x: Double, y: Double)
case class Line(pts: Vector[Point])

def linePic(l: Line) = penWidth(2) * penColor(white) * fillColor(Color(57, 57, 57)) ->
    Picture.fromPath { path =>
        val pts = l.pts
        val start = pts(0)
        path.moveTo(start.x, start.y)
        repeatFor(1 to pts.length - 2) { i =>
            val xc = (pts(i).x + pts(i + 1).x) / 2;
            val yc = (pts(i).y + pts(i + 1).y) / 2;
            path.quadTo(pts(i).x, pts(i).y, xc, yc)
        }
        path.lineTo(pts.last.x, pts.last.y)
    }

var lines = Vector.empty[Line]

repeatFor(rangeTo(cb.y + cb.height - 5 * stepy, cb.y, -stepy)) { y =>
    var linePts = Vector.empty[Point]
    repeatFor(rangeTo(cb.x, cb.x + cb.width, stepx)) { x =>
        val f = math.abs(x / cb.width * 2)
        val f1 = (1 - f) * (1 - f) * 150
        linePts = linePts :+ Point(x, y + randomDouble(f1.toInt))
    }
    lines = lines :+ Line(linePts)
}

draw(lines.map(linePic))
