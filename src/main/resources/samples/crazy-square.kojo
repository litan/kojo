// Contributed by Aditya Pant
// Control the square by using the arrow keys,
// and don't let it touch the edges of the canvas
// If you can do this for 15 seconds, you win!

toggleFullScreenCanvas()
cleari()
drawStage(yellow)
val cb = canvasBounds

val za = Picture {
    setFillColor(blue)
    setPenColor(blue)
    repeat(4) {
        forward(50)
        right(90)
    }
    hop(50)
    right(90)
    hop(20)
    setFillColor(red)
    repeat(4) {
        forward(10)
        right(90)
    }
}
draw(za)
val speed = 10
val move = 5
animate {
    val tx = random(speed) + 1
    val ty = random(speed) + 1
    za.translate(tx, ty)
    za.rotate(.5)
    if (za.collidesWith(stageBorder)) {
        drawMessage("You Lose", red)
        stopAnimation()
    }
    if (isKeyPressed(Kc.VK_LEFT)) {
        za.translate(-move * 3, 0)
    }
    if (isKeyPressed(Kc.VK_UP)) {
        za.translate(0, move * 2)
    }
    if (isKeyPressed(Kc.VK_RIGHT)) {
        za.translate(move * 3, 0)
    }
    if (isKeyPressed(Kc.VK_DOWN)) {
        za.translate(0, -move * 3)
    }
}

def drawMessage(m: String, c: Color) {
    val te = textExtent(m, 30)
    val pic = penColor(c) * trans(cb.x + (cb.width - te.width) / 2, 0) ->
        Picture.text(m, 30)
    draw(pic)
}

def manageGameTime() {
    var gameTime = 0
    val timeLabel = trans(cb.x + 10, cb.y + 50) -> Picture.textu(gameTime,
        20, blue)
    draw(timeLabel)
    timeLabel.forwardInputTo(stageArea)

    timer(1000) {
        gameTime += 1
        timeLabel.update(gameTime)

        if (gameTime == 15) {
            drawMessage("You Win", green)
            stopAnimation()
        }
    }
}

manageGameTime()
activateCanvas()
