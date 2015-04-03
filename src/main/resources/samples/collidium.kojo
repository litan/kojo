cleari()
drawStage(darkGray)
val ballSize = 10
val cb = canvasBounds
val ball = trans(cb.x + 60 + random(50), cb.y + 60 + random(50)) *
    penColor(yellow) *
    fillColor(yellow) -> PicShape.circle(ballSize)

val target = trans(-cb.x - 60 - random(50), -cb.y - 60 - random(50)) *
    penColor(red) *
    fillColor(red) -> PicShape.circle(ballSize / 2)

val obstacles = (1 to 3).map { n =>
    val delta = cb.width / 4
    trans(cb.x + n * delta, cb.y + cb.height / 4) * penColor(white) * penWidth(8) -> PicShape.vline(cb.height / 2)
}

draw(ball, target)
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
    slingPts += Point(ball.position.x, ball.position.y)
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
        Vector2D(slingPts(0).x - slingPts(1).x, slingPts(0).y - slingPts(1).y).limit(ballSize)
    animate {
        ball.transv(vel)
        if (ball.collidesWith(stageBorder)) {
            vel = bouncePicVectorOffStage(ball, vel)
        }
        else if (ball.collidesWith(paddle)) {
            vel = bouncePicVectorOffPic(ball, vel, paddle)
            while (ball.collidesWith(paddle)) {
                ball.transv(vel)
            }
        }
        else if (ball.collidesWith(target)) {
            target.setFillColor(green)
            stopAnimation()
        }

        ball.collision(obstacles) match {
            case Some(obstacle) =>
                vel = bouncePicVectorOffPic(ball, vel, obstacle)
                while (ball.collidesWith(obstacle)) {
                    ball.transv(vel)
                }
            case None =>
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
