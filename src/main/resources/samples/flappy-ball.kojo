// Use the up/down keys to prevent the ball from colliding with the
// oncoming obstacles or the stage border. 
// You win if you keep the ball in play for a minute
cleari()
drawStage(black)
def obstacle(h: Int, w: Int) = PicShape.rect(h, w)
def player = PicShape.circle(30)

var obstacles = Set.empty[Picture]
val cb = canvasBounds
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
val player1 = fillColor(Color(238, 106, 2)) * penColor(gray) -> player
draw(player1)
createObstacle()
val startTime = epochTime
var lastObsCreateTime = startTime
def timeLabelp(t: Double) = trans(cb.x + 10, cb.y + 50) -> PicShape.text(f"$t%.0f", 20)
var timeLabel = timeLabelp(0)
draw(timeLabel)

animate {
    val currTime = epochTime
    val gameTime = currTime - startTime
    timeLabel.erase()
    timeLabel = timeLabelp(gameTime)
    draw(timeLabel)

    if (gameTime > 60) {
        player1.setFillColor(yellow)
        draw(PicShape.text("You Win", 30))
        stopAnimation()
    }

    if (currTime - lastObsCreateTime > 1) {
        createObstacle()
        lastObsCreateTime = currTime
    }

    obstacles foreach { obs =>
        if (obs.position.x < cb.x) {
            obs.erase()
            obstacles -= obs
        }
        else {
            obs.translate(speed, 0)
            if (player1.collidesWith(obs)) {
                player1.setFillColor(red)
                stopAnimation()
            }
        }
    }
}

player1.react { self =>
    if (isKeyPressed(Kc.VK_UP)) {
        fallSpeed = 0
        player1.translate(0, pspeed)
    }
    else if (isKeyPressed(Kc.VK_DOWN)) {
        fallSpeed = 0
        player1.translate(0, -pspeed)
    }
    else {
        fallSpeed = fallSpeed + gravity
        player1.translate(0, -fallSpeed)
    }
    if (player1.collidesWith(stageBorder)) {
        player1.setFillColor(red)
        stopAnimation()
    }
}

activateCanvas()
