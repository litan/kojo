def spiral(size: Int, angle: Int) {
    if (size <= 300) {
        forward(size)
        right(angle)
        spiral(size + 2, angle)
    }
}

def spiralp = penColor(black) * fillColor(Color(255, 0, 204)) -> Picture {
    spiral(0, 91)
}
clear()
setPenThickness(1)
setAnimationDelay(0)
val cb = canvasBounds
def bg = trans(cb.x, cb.y) * fillColor(Color(153, 0, 255)) -> PicShape.rect(cb.height, cb.width)
val pic = GPics(
    weave(30, 5, 30, 5) -> bg,
    noise(80, 1) -> spiralp
)
draw(pic)