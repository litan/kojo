// Sling the ball (with the mouse) towards the target on the top-right. 
// Then draw paddles on the canvas (with the mouse) to guide the ball 
// away from the obstacles and towards the target.
// You win if you hit the target within a minute
cleari()
drawStage(darkGray)
val ballSize = 20
val cb = canvasBounds

val ballE = penColor(red) -> PicShape.circle2(ballSize)
val ball1 = PicShape.image("/media/collidium/ball1.png", ballE)
val ball2 = PicShape.image("/media/collidium/ball2.png", ballE)
val ball3 = PicShape.image("/media/collidium/ball3.png", ballE)
val ball4 = PicShape.image("/media/collidium/ball4.png", ballE)

val ball =
    trans(cb.x + 60 + random(50), cb.y + 60 + random(50)) -> picBatch(ball1, ball2, ball3, ball4)

val target = trans(-cb.x - 60 - random(50), -cb.y - 60 - random(50)) *
    penColor(red) *
    fillColor(red) -> PicShape.circle(ballSize / 2)

val obstacles = (1 to 3).map { n =>
    val delta = cb.width / 4
    trans(cb.x + n * delta, cb.y + cb.height / 4) * penColor(white) * penWidth(8) -> PicShape.vline(cb.height / 2)
}

draw(ball, target)
drawAndHide(ballE)
obstacles.foreach { o => draw(o) }

import collection.mutable.ArrayBuffer
def line(ps: ArrayBuffer[Point]) = Picture {
    jumpTo(ps(0).x, ps(0).y)
    moveTo(ps(1).x, ps(1).y)
}
val slingPts = ArrayBuffer.empty[Point]
var sling = PicShape.hline(1)
var paddle = rot(10) -> PicShape.hline(300)
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
    sling = line(slingPts)
    sling.draw()
}

ball.onMouseRelease { (x, y) =>
    sling.erase()
    ball.forwardInputTo(stageArea)
    var vel = if (slingPts.size == 1)
        Vector2D(1, 1)
    else
        Vector2D(slingPts(0).x - slingPts(1).x, slingPts(0).y - slingPts(1).y).limit(5)

    val startTime = epochTime
    def timeLabelp(t: Double) = trans(cb.x + 10, cb.y + 50) -> PicShape.text(f"$t%.0f", 20)
    var timeLabel = timeLabelp(0)
    draw(timeLabel)

    animate {
        val gameTime = epochTime - startTime
        ball.transv(vel)
        ball.showNext()
        if (ball.collidesWith(stageBorder)) {
            playMp3Sound("/media/collidium/hit.mp3")
            vel = bouncePicVectorOffStage(ball, vel)
        }
        else if (ball.collidesWith(paddle)) {
            playMp3Sound("/media/collidium/hit.mp3")
            vel = bouncePicVectorOffPic(ball, vel, paddle)
            while (ball.collidesWith(paddle)) {
                ball.transv(vel)
            }
        }
        else if (ball.collidesWith(target)) {
            target.setFillColor(green)
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

        timeLabel.erase()
        timeLabel = timeLabelp(gameTime)
        draw(timeLabel)

        if (gameTime > 60) {
            draw(PicShape.text("Time up! You Lose", 30))
            stopAnimation()
        }
    }
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
    paddle.erase()
    paddle = line(paddlePts)
    paddle.draw()
}

stageArea.onMouseRelease { (x, y) =>
    paddle.forwardInputTo(stageArea)
}

target.forwardInputTo(stageArea)
obstacles.foreach { o => o.forwardInputTo(stageArea) }
