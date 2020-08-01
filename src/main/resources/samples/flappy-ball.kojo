// Use the up/down keys to prevent the ball from colliding with the
// oncoming obstacles or the stage border.
// You win if you keep the ball in play for a minute
cleari()
drawStage(black)
// setRefreshRate(50)

val cb = canvasBounds
def obstacle(h: Int, w: Int) = Picture.rect(h, w)
val playerE = trans(49, 31) -> Picture.circle(30)

var obstacles = Set.empty[Picture]
def createObstacle() {
    val height = random((0.5 * cb.height).toInt) + 50
    val trxy = if (randomBoolean) (cb.width / 2, cb.height / 2 - height)
    else (cb.width / 2, -cb.height / 2)
    val obs = fillColor(ColorMaker.blueViolet) * penColor(noColor) *
        trans(trxy._1, trxy._2) -> obstacle(height, random(30) + 30)
    obstacles += obs
    draw(obs)
}

val speed = -5
val pspeed = 5
val gravity = 0.1
var fallSpeed = 0.0
val pl1 = Picture.image("/media/flappy-ball/ballwing1.png", playerE)
val pl2 = Picture.image("/media/flappy-ball/ballwing2.png", playerE)
val player = picBatch(pl1, pl2)
draw(player)
drawAndHide(playerE)
// createObstacle()

timer(1000) {
    createObstacle()
}

animate {
    obstacles foreach { obs =>
        if (obs.position.x + 60 < cb.x) {
            obs.erase()
            obstacles -= obs
        }
        else {
            obs.translate(speed, 0)
            if (player.collidesWith(obs)) {
                player.setOpacity(0.3)
                drawMessage("You Lose", Color(255, 24, 27))
                stopAnimation()
            }
        }
    }

    player.showNext()
    if (isKeyPressed(Kc.VK_UP)) {
        fallSpeed = 0
        player.translate(0, pspeed)
    }
    else if (isKeyPressed(Kc.VK_DOWN)) {
        fallSpeed = 0
        player.translate(0, -pspeed)
    }
    else {
        fallSpeed = fallSpeed + gravity
        player.translate(0, -fallSpeed)
    }
    if (player.collidesWith(stageBorder)) {
        player.setOpacity(0.3)
        drawMessage("You Lose", red)
        stopAnimation()
    }
}

def drawMessage(m: String, c: Color) {
    drawCenteredMessage(m, c, 30)
}

def manageGameTime() {
    showGameTime(60, "You Win", green, 20)
}

manageGameTime()
activateCanvas()