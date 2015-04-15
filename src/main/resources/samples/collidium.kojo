// Sling the ball (with the mouse) towards the target on the top-right. 
// Then draw paddles on the canvas (with the mouse) to guide the ball 
// away from the obstacles and towards the target.
// You win if you hit the target within a minute
switchToDefault2Perspective()
cleari()
drawStage(darkGray)
val cb = canvasBounds
val ballSize = 20

val ballE = penColor(red) * trans(ballSize, ballSize) -> PicShape.circle(ballSize)
val ball1 = PicShape.image("/media/collidium/ball1.png", ballE)
val ball2 = PicShape.image("/media/collidium/ball2.png", ballE)
val ball3 = PicShape.image("/media/collidium/ball3.png", ballE)
val ball4 = PicShape.image("/media/collidium/ball4.png", ballE)

val ball =
    trans(cb.x + 60 + random(50), cb.y + 60 + random(50)) -> picBatch(ball1, ball2, ball3, ball4)

val target = trans(-cb.x - 60 - random(50), -cb.y - 60 - random(50)) *
    penColor(red) *
    fillColor(red) -> PicShape.circle(ballSize / 4)

val wallTexture = TexturePaint("/media/collidium/bwall.png", 0, 0)
val obstacles = (1 to 3).map { n =>
    val delta = cb.width / 4
    trans(cb.x + n * delta, cb.y + cb.height / 4) * fillColor(wallTexture) * penColor(noColor) -> PicShape.rect(cb.height / 2, 12)
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
    jumpTo(ps(0).x, ps(0).y)
    moveTo(ps(1).x, ps(1).y)
    hop(-sqsz / 2)
    left(90)
    sq()
    right(90)
    jumpTo(ps(0).x, ps(0).y)
    hop(-sqsz / 2)
    left(90)
    sq()
}
val slingPts = ArrayBuffer.empty[Point]
var sling = PicShape.hline(1)
var paddle = PicShape.hline(1)
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
        ball.transv(vel)
        ball.showNext()
        if (ball.collidesWith(stageBorder)) {
            playMp3Sound("/media/collidium/hit.mp3")
            vel = bouncePicVectorOffStage(ball, vel)
        }
        else if (ball.collidesWith(paddle)) {
            playMp3Sound("/media/collidium/hit.mp3")
            vel = bouncePicVectorOffPic(ball, vel, paddle)
            ball.transv(vel)
        }
        else if (ball.collidesWith(target)) {
            target.setPenColor(green)
            target.setFillColor(green)
            drawMessage("Yaay! You Win", green)
            stopAnimation()
            playMp3Sound("/media/collidium/win.mp3")
        }

        ball.collision(obstacles) match {
            case Some(obstacle) =>
                playMp3Sound("/media/collidium/hit.mp3")
                vel = bouncePicVectorOffPic(ball, vel, obstacle)
                while (ball.collidesWith(obstacle)) {
                    ball.transv(vel)
                }
            case None =>
        }

    }
    manageGameTime()
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
    tempPaddle = line(paddlePts, black)
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
            drawMessage("Time up! You Lose", red)
            stopAnimation()
        }
    }
}
