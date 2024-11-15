cleari()
clearOutput()

val sprite = Picture.image(Costume.womanWaving)
// Make the sprite point to your own image via the following:
// val assetsDir = "/your/asset/folder"
// val sprite = Picture.image(s"$assetsDir/your-sprite.png")

draw(sprite)

// double or float array to be printed
val doubleArray = true

println("Load your own sprite image above by changing the code as described at the start of the script.\n")
println("Then click around the sprite image to create a polygonal boundary for the sprite (and feel free to zoom in while doing this).\n")
println("Click twice (slowly) at the same point to finish off the boundary.\n")
println("The last point (that you clicked twice at) is connected to the first point to create the boundary.\n")

var points = ArrayBuffer.empty[Point]
var pointsPic: Picture = Picture.rectangle(0, 0)
pointsPic.draw()

def updateBoundary() {
    val pt = points.head
    val pointsPic2 = picStack(
        Picture.circle(5)
            .withPenColor(blue).withFillColor(blue).withPosition(pt.x, pt.y),
        Picture.fromVertexShape { s =>
            s.beginShape()
            points.foreach { pt =>
                s.vertex(pt.x, pt.y)
            }
            s.endShape()
        }.withPenColor(blue).withPenThickness(5)
    )
    pointsPic2.draw()
    pointsPic.erase()
    pointsPic = pointsPic2
    pointsPic.tnode.repaint()
}

onMouseClick { (x, y) =>
    points.append(Point(x, y))
    updateBoundary()

    val len = points.length
    if (points(len - 2) == points(len - 1)) {
        points.remove(len - 1)
        points.append(points.head)
        val pts = points
            .flatMap(pt => Array(pt.x, pt.y))
            .map(n => if (doubleArray) f"${n}%.1f" else f"${n}%.1ff")

        println("Boundary Array:\n")
        println(pts.mkString("Array(", ", ", ")"))
        println("\nCopy the above array into your code as required.")
        updateBoundary()
        points = ArrayBuffer.empty[Point]
    }
}

activateCanvas()
