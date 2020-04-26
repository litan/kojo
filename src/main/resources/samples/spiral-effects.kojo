def spiral(size: Int, angle: Int) {
    if (size <= 300) {
        forward(size)
        right(angle)
        spiral(size + 2, angle)
    }
}

val spiralp = penColor(black) * fillColor(cm.rgb(255, 0, 204)) -> Picture {
    spiral(0, 91)
}
clear()
val cb = canvasBounds
def bg = trans(cb.x, cb.y) * fillColor(cm.rgb(153, 0, 255)) -> Picture.rectangle(cb.width, cb.height)
val pic = picStack(
    weave(30, 5, 30, 5) -> bg,
    noise(80, 1) -> spiralp
)
draw(pic)