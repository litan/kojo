// Sling the ball (with the mouse) towards the target on the top-right.
// Then draw paddles on the canvas (with the mouse) to guide the ball
// away from the obstacles and towards the target.
// You win if you hit the target within a minute
switchToDefault2Perspective()
cleari()
// setRefreshRate(50)

drawStage(darkGray)
val cb = canvasBounds
val obsDelta = cb.width / 4
val ballDeltaBase = (obsDelta / 4).toInt
def ballDelta = ballDeltaBase + random(ballDeltaBase)
val ballSize = 20

val ballE = penColor(red) * trans(ballSize, ballSize) -> Picture.circle(ballSize)
val ball1 = Picture.image("/media/collidium/ball1.png", ballE)
val ball2 = Picture.image("/media/collidium/ball2.png", ballE)
val ball3 = Picture.image("/media/collidium/ball3.png", ballE)
val ball4 = Picture.image("/media/collidium/ball4.png", ballE)

val ball = picBatch(ball1, ball2, ball3, ball4)
ball.translate(cb.x + ballDelta, cb.y + ballDelta)

val target = trans(-cb.x - ballDelta, -cb.y - ballDelta) *
    penColor(red) *
    fillColor(red) -> Picture.circle(ballSize / 4)

val wallTexture = TexturePaint("/media/collidium/bwall.png", 0, 0)
val obstacles = (1 to 3).map { n =>
    trans(cb.x + n * obsDelta, cb.y + cb.height / 4) * fillColor(wallTexture) * penColor(noColor) -> Picture.rect(cb.height / 2, 12)
}

draw(ball, target)
drawAndHide(ballE)
obstacles.foreach { o => draw(o) }
playMp3Sound("/media/collidium/hit.mp3")

import collection.mutable.ArrayBuffer
def line(ps: ArrayBuffer[Point], c: Color) = Picture {
    val sqsz = 4
    def sq() {
        hop(-sqsz / 2)
        repeat(4) {
            forward(sqsz)
            right(90)
        }
        hop(sqsz / 2)
    }
    setPenColor(c)
    setFillColor(c)
    setPosition(ps(0).x, ps(0).y)
    lineTo(ps(1).x, ps(1).y)
    hop(-sqsz / 2)
    left(90)
    sq()
    right(90)
    setPosition(ps(0).x, ps(0).y)
    hop(-sqsz / 2)
    left(90)
    sq()
}
val slingPts = ArrayBuffer.empty[Point]
var sling = Picture.hline(1)
var paddle = Picture.hline(1)
var tempPaddle = paddle
drawAndHide(paddle)

ball.onMousePress { (x, y) =>
    slingPts += Point(ball.position.x + ballSize, ball.position.y + ballSize)
}

ball.onMouseDrag { (x, y) =>
    if (slingPts.size > 1) {
        slingPts.remove(1)
    }
    slingPts += Point(x, y)
    sling.erase()
    sling = line(slingPts, green)
    sling.draw()
}

ball.onMouseRelease { (x, y) =>
    sling.erase()
    ball.forwardInputTo(stageArea)
    var vel = if (slingPts.size == 1)
        Vector2D(1, 1)
    else
        Vector2D(slingPts(0).x - slingPts(1).x, slingPts(0).y - slingPts(1).y).limit(7)

    animate {
        ball.translate(vel)
        ball.showNext()
        if (ball.collidesWith(stageBorder)) {
            playMp3Sound("/media/collidium/hit.mp3")
            vel = bouncePicVectorOffStage(ball, vel)
        }
        else if (ball.collidesWith(paddle)) {
            playMp3Sound("/media/collidium/hit.mp3")
            vel = bouncePicVectorOffPic(ball, vel, paddle)
            ball.translate(vel)
        }
        else if (ball.collidesWith(target)) {
            target.setPenColor(green)
            target.setFillColor(green)
            drawCenteredMessage("Yaay! You Win", green, 20)
            stopAnimation()
            playMp3Sound("/media/collidium/win.mp3")
        }

        ball.collision(obstacles) match {
            case Some(obstacle) =>
                playMp3Sound("/media/collidium/hit.mp3")
                vel = bouncePicVectorOffPic(ball, vel, obstacle)
                while (ball.collidesWith(obstacle)) {
                    ball.translate(vel)
                }
            case None =>
        }

    }
    showGameTime(60, "Time up! You Lose", cm.lightBlue, 20)
}

val paddlePts = ArrayBuffer.empty[Point]
stageArea.onMousePress { (x, y) =>
    paddle.erase()
    paddlePts.clear()
    paddlePts += Point(x, y)
}

stageArea.onMouseDrag { (x, y) =>
    if (paddlePts.size > 1) {
        paddlePts.remove(1)
    }
    paddlePts += Point(x, y)
    tempPaddle.erase()
    tempPaddle = line(paddlePts, ColorMaker.aquamarine)
    tempPaddle.draw()
}

stageArea.onMouseRelease { (x, y) =>
    if (tempPaddle.collidesWith(ball)) {
        tempPaddle.erase()
    }
    else {
        paddle = tempPaddle
        paddle.setPenColor(yellow)
        paddle.setFillColor(yellow)
        paddle.forwardInputTo(stageArea)
    }
}

target.forwardInputTo(stageArea)
obstacles.foreach { o => o.forwardInputTo(stageArea) }
// Game idea and sounds from https://github.com/shadaj/collidium
