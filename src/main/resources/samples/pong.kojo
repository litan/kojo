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
val PaddleSpeed = 5
val pScaleFactor = 1.01
val BallSpeed = 5
val bScaleFactor = 1.001

def paddle = penColor(darkGray) * fillColor(red) -> Picture.rectangle(PaddleW, PaddleH)
def vline = penColor(white) -> Picture.vline(Height)
def ball = penColor(cm.rgb(0, 230, 0)) * fillColor(cm.rgb(0, 230, 0)) -> Picture.circle(BallR)

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
    val pScore = Picture.textu(score, 20)
    pScore.translate(xt, Height / 2 - 10)
    pScore.setPenColor(cm.lightSteelBlue)
    def incrScore() {
        score += 1
        pScore.update(score)
    }
}

val topbot = Seq(stageTop, stageBot)
val paddle1 = trans(-Width / 2, 0) -> paddle
val paddle2 = trans(Width / 2 - PaddleW, 0) -> paddle
val centerLine = trans(0, -Height / 2) -> vline
val leftGutter = trans(-Width / 2 + PaddleW, -Height / 2) -> vline
val rightGutter = trans(Width / 2 - PaddleW, -Height / 2) -> vline
val gutters = Seq(leftGutter, rightGutter)
val paddles = Seq(paddle1, paddle2)
val gameBall = ball

draw(paddle1, paddle2, centerLine, leftGutter, rightGutter, gameBall)

val ballVel = Vector2D(BallSpeed, 3)
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
    gameBall.translate(currBallVel)

    if (gameBall.collision(paddles).isDefined) {
        currBallVel = Vector2D(-currBallVel.x, currBallVel.y)
    }
    else if (gameBall.collision(topbot).isDefined) {
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
        currBallVel = (currBallVel * bScaleFactor).limit(11)
    }
    paddleBehavior(paddle1, Kc.VK_A, Kc.VK_Z)
    paddleBehavior(paddle2, Kc.VK_UP, Kc.VK_DOWN)
}

def paddleBehavior(paddle: Picture, upkey: Int, downkey: Int) {
    val pVel = paddleVelocity(paddle)
    if (isKeyPressed(upkey) && !paddle.collidesWith(stageTop)) {
        if (pVel.lastUp) {
            pVel.scaleSpeed(pScaleFactor)
        }
        else {
            pVel.reset(!pVel.lastUp)
        }
        paddle.translate(0, pVel.speed)
    }
    else if (isKeyPressed(downkey) && !paddle.collidesWith(stageBot)) {
        if (!pVel.lastUp) {
            pVel.scaleSpeed(pScaleFactor)
        }
        else {
            pVel.reset(!pVel.lastUp)
        }
        paddle.translate(0, -pVel.speed)
    }
    else {
        pVel.reset(pVel.lastUp)
    }
}
activateCanvas()