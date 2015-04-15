// Use the up/down keys to prevent the ball from colliding with the
// oncoming obstacles or the stage border. 
// You win if you keep the ball in play for a minute
cleari()
drawStage(black)
val cb = canvasBounds
def obstacle(h: Int, w: Int) = PicShape.rect(h, w)
val playerE = trans(49, 31) -> PicShape.circle(30)

var obstacles = Set.empty[Picture]
def createObstacle() {
    val height = random((0.5 * cb.height).toInt) + 50
    val trxy = if (randomBoolean) (cb.width / 2, cb.height / 2 - height)
    else (cb.width / 2, -cb.height / 2)
    val obs = fillColor(Color(12, 34, 100)) * penColor(noColor) *
        trans(trxy._1, trxy._2) -> obstacle(height, random(30) + 30)
    obstacles += obs
    draw(obs)
}

val speed = -5
val pspeed = 5
val gravity = 0.1
var fallSpeed = 0.0
val pl1 = PicShape.image("/media/flappy-ball/ballwing1.png", playerE)
val pl2 = PicShape.image("/media/flappy-ball/ballwing2.png", playerE)
val player = picBatch(pl1, pl2)
draw(player)
drawAndHide(playerE)
createObstacle()
var lastObsCreateTime = epochTime

animate {
    val currTime = epochTime
    if (currTime - lastObsCreateTime > 1) {
        createObstacle()
        lastObsCreateTime = currTime
    }

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
}

player.react { self =>
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
    val te = textExtent(m, 30)
    val pic = penColor(c) * trans(cb.x + (cb.width - te.width) / 2, 0) -> PicShape.text(m, 30)
    draw(pic)
}

def manageGameTime() {
    var gameTime = 0
    val timeLabel = trans(cb.x + 10, cb.y + 50) -> PicShape.textu(gameTime, 20, blue)
    draw(timeLabel)
    timeLabel.forwardInputTo(stageArea)

    timer(1000) {
        gameTime += 1
        timeLabel.update(gameTime)

        if (gameTime == 60) {
            drawMessage("You Win", green)
            stopAnimation()
        }
    }
}

manageGameTime()
activateCanvas()
