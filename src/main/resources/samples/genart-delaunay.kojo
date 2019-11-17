// delaunay triangulation example
// keep clicking to keep drawing
cleari()
setBackground(white)
disablePanAndZoom()
val cb = canvasBounds
val points = ArrayBuffer(Point(-100, -50), Point(100, -50), Point(-100, 50))
onMouseClick { (x, y) =>
    points.append(Point(x, y))
    if (points.size > 3) {
        drawTriangles()
    }
}

def drawTriangles() {
    erasePictures()
    val trs = triangulate(points)
    trs.foreach { t =>
        val pic = Picture {
            val lg = cm.linearGradient(t.a.x, t.a.y, black, t.b.x, t.b.y, blue)
            setFillColor(lg)
            setPenColor(gray)
            setPosition(t.a.x, t.a.y)
            lineTo(t.b.x, t.b.y)
            lineTo(t.c.x, t.c.y)
        }
        draw(pic)
    }
}

val message1 = penColor(black) -> Picture.text("Click to get going", 40)
val message2 = penColor(black) -> Picture.text("Keep clicking to keep going", 30)

val msg = picColCentered(message2, Picture.vgap(20), message1)
drawCentered(msg)