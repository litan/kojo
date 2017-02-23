// Copyright (C) 2016 Aditya Pant <adi2.pant@gmail.com>
// The contents of this file are subject to 
// the GNU General Public License Version 3 (http://www.gnu.org/copyleft/gpl.html)

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
        PicShape.text(m, 30)
    draw(pic)
}

def manageGameTime() {
    var gameTime = 0
    val timeLabel = trans(cb.x + 10, cb.y + 50) -> PicShape.textu(gameTime,
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
