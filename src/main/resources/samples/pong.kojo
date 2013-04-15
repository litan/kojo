// A Game of Pong
// Player on right uses Up/Down Arrow keys to control paddle
// Player on left uses A/Z keys to control paddle
// 'Space' to start; 'P' to bring up Game Panel; 'Esc' to quit

toggleFullScreenCanvas()
cleari()
setRefreshRate(40)
val PaddleH = 100
val PaddleW = 25
val BallR = 15
val Height = canvasBounds.height
val Width = canvasBounds.width
val PaddleSpeed = 9
val BallSpeed = 9

def paddle = penColor(darkGray) * fillColor(red) -> PicShape.rect(PaddleH, PaddleW)
def vline = penColor(darkGray) -> PicShape.vline(Height)
def ball = penColor(lightGray) * penWidth(1) * fillColor(Color(0, 230, 0)) -> PicShape.circle(BallR)

val paddle1 = trans(-Width / 2, 0) -> paddle
val paddle2 = trans(Width / 2 - PaddleW, 0) -> paddle
val centerLine = trans(0, -Height / 2) -> vline
val leftGutter = trans(-Width / 2 + PaddleW, -Height / 2) -> vline
val rightGutter = trans(Width / 2 - PaddleW, -Height / 2) -> vline
val gameBall = ball

drawStage(Color(0, 0, 250, 20))
draw(paddle1, paddle2, centerLine, leftGutter, rightGutter, gameBall)

case class PaddleS(speed: Double, lastUp: Boolean) { outer =>
    def incrSpeed(i: Double) = copy(speed = outer.speed + i)
    def scaleSpeed(f: Double) = copy(speed = outer.speed * f)
}

case class Score(score: Int, left: Boolean) { outer =>
    val xt = if (left) -50 else 50
    val pScore = trans(xt, Height / 2 - 10) * penColor(black) -> PicShape.text(score, 20)
    def incrScore = copy(score = outer.score + 1)
}

case class Level(num: Int, vel: Vector2D) {
    val pLevel = trans(-60, -Height / 2 + 40) * penColor(black) -> PicShape.text(s"Level: $num", 20)
}

var running = false
var ballVel = Vector2D(BallSpeed, 3)
var level = Level(1, ballVel)

var paddleInfo = Map(
    paddle1 -> PaddleS(PaddleSpeed, true),
    paddle2 -> PaddleS(PaddleSpeed, true)
)

var scores = Map(
    paddle1 -> Score(0, true),
    paddle2 -> Score(0, false)
)

def onStart() {
    running = true
    gPanel.invisible()
}
def onPause() {
    running = false
}
def onStop() {
    stopAnimation()
    gPanel.invisible()
}
def onLevelUp() {
    ballVel *= 1.1
    level.pLevel.erase()
    level = Level(level.num + 1, ballVel)
    draw(level.pLevel)
    paddleInfo += paddle1 -> paddleInfo(paddle1).scaleSpeed(1.1)
    paddleInfo += paddle2 -> paddleInfo(paddle2).scaleSpeed(1.1)
}
def onLevelDown() {
    ballVel *= 0.9
    level.pLevel.erase()
    level = Level(level.num - 1, ballVel)
    draw(level.pLevel)
    paddleInfo += paddle1 -> paddleInfo(paddle1).scaleSpeed(0.9)
    paddleInfo += paddle2 -> paddleInfo(paddle2).scaleSpeed(0.9)
}

val gPanel = Gaming.gamePanel(
    onStart,
    onPause,
    onStop,
    onLevelUp,
    onLevelDown
)

draw(scores(paddle1).pScore)
draw(scores(paddle2).pScore)
draw(level.pLevel)
draw(gPanel)

val gutters = Seq(leftGutter, rightGutter)
val topbot = Seq(stageTop, stageBot)
val paddles = Seq(paddle1, paddle2)

gameBall.react { self => 
    if (running) {
        self.transv(ballVel)
        if (self.collision(paddles).isDefined) {
            ballVel = Vector2D(-ballVel.x, ballVel.y)
        }
        else if (self.collision(topbot).isDefined) {
            ballVel = Vector2D(ballVel.x, -ballVel.y)
        }
        else if (self.collidesWith(leftGutter)) {
            self.setPosition(0, 0)
            ballVel = Vector2D(-level.vel.x.abs, level.vel.y)
            scores(paddle1).pScore.erase()
            scores += paddle1 -> scores(paddle1).incrScore
            draw(scores(paddle1).pScore)
        }
        else if (self.collidesWith(rightGutter)) {
            self.setPosition(0, 0)
            ballVel = Vector2D(level.vel.x.abs, level.vel.y)
            scores(paddle2).pScore.erase()
            scores += paddle2 -> scores(paddle2).incrScore
            draw(scores(paddle2).pScore)
        }
        else {
            ballVel *= 1.001
        }
    }
}

def paddleBehavior(paddle: Picture, upkey: Int, downkey: Int) {
    if (running) {
        val pstate = paddleInfo(paddle)
        if (isKeyPressed(upkey) && !paddle.collidesWith(stageTop)) {
            paddle.translate(0, pstate.speed)
            if (pstate.lastUp) {
                paddleInfo += paddle -> pstate.incrSpeed(0.1)
            }
            else {
                paddleInfo += paddle -> PaddleS(PaddleSpeed, true)
            }
        }
        if (isKeyPressed(downkey) && !paddle.collidesWith(stageBot)) {
            paddle.translate(0, -pstate.speed)
            if (!pstate.lastUp) {
                paddleInfo += paddle -> pstate.incrSpeed(0.1)
            }
            else {
                paddleInfo += paddle -> PaddleS(PaddleSpeed, false)
            }
        }
    }
}

paddle1.react { self =>
    paddleBehavior(self, Kc.VK_A, Kc.VK_Z)
}

paddle2.react { self =>
    paddleBehavior(self, Kc.VK_UP, Kc.VK_DOWN)
}

onKeyPress { k =>
    k match {
        case Kc.VK_P      =>
            gPanel.visible(); onPause()
        case Kc.VK_O      => gPanel.invisible()
        case Kc.VK_SPACE => onStart()
        case Kc.VK_ESCAPE => onStop()
        case _            =>
    }
}
