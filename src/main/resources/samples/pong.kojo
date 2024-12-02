// A Game of Pong
// Player on right uses Up/Down Arrow keys to control paddle
// Player on left uses A/Z keys to control paddle
cleari()
drawStage(cm.darkGreyClassic)

val PaddleH = 100
val PaddleW = 25
val BallR = 15
val Height = canvasBounds.height
val Width = canvasBounds.width
val PaddleSpeed = 400
val pScaleFactor = 1.01
val BallSpeed = 400
val bScaleFactor = 1.001

def paddle: Picture = {
    val pic = Picture.rectangle(PaddleW, PaddleH)
    pic.setPenColor(darkGray); pic.setFillColor(red)
    pic
}
def vline = {
    val pic = Picture.rectangle(0, Height)
    pic.setPenColor(white)
    pic
}
def ball = {
    val pic = Picture.circle(BallR)
    pic.setPenColor(cm.rgb(0, 230, 0)); pic.setFillColor(cm.rgb(0, 230, 0))
    pic
}

class PaddleVelocity(speed0: Double, lastUp0: Boolean) {
    var speed = speed0
    var lastUp = lastUp0

    def reset(lu: Boolean) {
        speed = speed0
        lastUp = lu
    }

    def incrSpeed(i: Double) { speed = speed + i }
    def scaleSpeed(f: Double) { speed = speed * f }
}

class Score(score0: Int, left0: Boolean) {
    var score = score0
    var left = left0
    val xt = if (left) -50 else 50
    val pScore = Picture.text(score.toString, 20, cm.lightSteelBlue)
    pScore.translate(xt, Height / 2 - 10)
    def incrScore() {
        score += 1
        pScore.setText(score.toString)
    }
}

val topbot = Seq(stageTop, stageBot)
val paddle1 = {
    val pic = paddle; pic.translate(-Width / 2, 0)
    pic
}
val paddle2 = {
    val pic = paddle; pic.translate(Width / 2 - PaddleW, 0)
    pic
}
val centerLine = {
    val pic = vline; pic.translate(0, -Height / 2)
    pic
}
val leftGutter = {
    val pic = vline; pic.translate(-Width / 2 + PaddleW, -Height / 2)
    pic
}
val rightGutter = {
    val pic = vline; pic.translate(Width / 2 - PaddleW, -Height / 2)
    pic
}
val gutters = Seq(leftGutter, rightGutter)
val paddles = Seq(paddle1, paddle2)
val gameBall = ball

draw(paddle1, paddle2, centerLine, leftGutter, rightGutter, gameBall)

val ballVel = Vector2D(BallSpeed, BallSpeed * 0.65)
var currBallVel: Vector2D = ballVel

val paddleVelocity = Map(
    paddle1 -> new PaddleVelocity(PaddleSpeed, true),
    paddle2 -> new PaddleVelocity(PaddleSpeed, true))

val scores = Map(
    paddle1 -> new Score(0, true),
    paddle2 -> new Score(0, false))

draw(scores(paddle1).pScore)
draw(scores(paddle2).pScore)

animate {
    val dt = frameDeltaTime
    gameBall.translate(currBallVel * dt)

    if (gameBall.collision(paddles).isDefined) {
        currBallVel = Vector2D(-currBallVel.x, currBallVel.y)
    }
    else if (gameBall.collision(topbot).isDefined) {
        gameBall.translate(-currBallVel * dt)
        currBallVel = Vector2D(currBallVel.x, -currBallVel.y)
    }
    else if (gameBall.collidesWith(leftGutter)) {
        gameBall.setPosition(0, 0)
        currBallVel = Vector2D(-ballVel.x, ballVel.y)
        scores(paddle2).incrScore()
    }
    else if (gameBall.collidesWith(rightGutter)) {
        gameBall.setPosition(0, 0)
        currBallVel = Vector2D(ballVel.x, ballVel.y)
        scores(paddle1).incrScore()
    }
    else {
        currBallVel = (currBallVel * bScaleFactor).limit(800)
    }
    paddleBehavior(paddle1, Kc.VK_A, Kc.VK_Z, dt)
    paddleBehavior(paddle2, Kc.VK_UP, Kc.VK_DOWN, dt)
}

def paddleBehavior(paddle: Picture, upkey: Int, downkey: Int, dt: Double) {
    val pVel = paddleVelocity(paddle)
    if (isKeyPressed(upkey) && !paddle.collidesWith(stageTop)) {
        if (pVel.lastUp) {
            pVel.scaleSpeed(pScaleFactor)
        }
        else {
            pVel.reset(!pVel.lastUp)
        }
        paddle.translate(0, pVel.speed * dt)
    }
    else if (isKeyPressed(downkey) && !paddle.collidesWith(stageBot)) {
        if (!pVel.lastUp) {
            pVel.scaleSpeed(pScaleFactor)
        }
        else {
            pVel.reset(!pVel.lastUp)
        }
        paddle.translate(0, -pVel.speed * dt)
    }
    else {
        pVel.reset(pVel.lastUp)
    }
}
activateCanvas()