// A Game of Pong
// Player on right uses Up/Down Arrow keys to control paddle
// Player on left uses A/Z keys to control paddle
// Press 'Esc' to quit
toggleFullScreenCanvas()
cleari()
drawStage(Color(0, 0, 250, 20))

val glevel = 1
val PaddleH = 100
val PaddleW = 25
val BallR = 15
val Height = canvasBounds.height
val Width = canvasBounds.width
val PaddleSpeed = 5
val BallSpeed = 5

def paddle = penColor(darkGray) * fillColor(red) -> PicShape.rect(PaddleH, PaddleW)
def vline = penColor(darkGray) -> PicShape.vline(Height)
def ball = penColor(lightGray) * penWidth(1) * fillColor(Color(0, 230, 0)) -> PicShape.circle(BallR)
def levelFactor = math.pow(1.1, glevel)

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

case class World(
    ballVel: Vector2D,
    level: Level,
    paddleInfo: Map[Picture, PaddleS],
    scores: Map[Picture, Score])

var world: World = _

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

onAnimationStart {
    val ballVel = Vector2D(BallSpeed * levelFactor, 3)
    world = World(
        ballVel,
        Level(glevel, ballVel),
        Map(
            paddle1 -> PaddleS(PaddleSpeed * levelFactor, true),
            paddle2 -> PaddleS(PaddleSpeed * levelFactor, true)
        ),
        Map(
            paddle1 -> Score(0, true),
            paddle2 -> Score(0, false)
        )
    )

    draw(world.scores(paddle1).pScore)
    draw(world.scores(paddle2).pScore)
    draw(world.level.pLevel)
}

gameBall.react { self =>
    self.transv(world.ballVel)
    if (self.collision(paddles).isDefined) {
        world = world.copy(
            ballVel = Vector2D(-world.ballVel.x, world.ballVel.y)
        )
    }
    else if (self.collision(topbot).isDefined) {
        world = world.copy(
            ballVel = Vector2D(world.ballVel.x, -world.ballVel.y)
        )
    }
    else if (self.collidesWith(leftGutter)) {
        self.setPosition(0, 0)
        world.scores(paddle2).pScore.erase()
        world = world.copy(
            ballVel = Vector2D(-world.level.vel.x.abs, world.level.vel.y),
            scores = world.scores + (paddle2 -> world.scores(paddle2).incrScore)
        )
        draw(world.scores(paddle2).pScore)
    }
    else if (self.collidesWith(rightGutter)) {
        self.setPosition(0, 0)
        world.scores(paddle1).pScore.erase()
        world = world.copy(
            ballVel = Vector2D(world.level.vel.x.abs, world.level.vel.y),
            scores = world.scores + (paddle1 -> world.scores(paddle1).incrScore)
        )
        draw(world.scores(paddle1).pScore)
    }
    else {
        world = world.copy(
            ballVel = (world.ballVel * 1.001).limit(10)
        )
    }
}

def paddleBehavior(paddle: Picture, upkey: Int, downkey: Int) {
    val pstate = world.paddleInfo(paddle)
    if (isKeyPressed(upkey) && !paddle.collidesWith(stageTop)) {
        paddle.translate(0, pstate.speed)
        if (pstate.lastUp) {
            world = world.copy(
                paddleInfo = world.paddleInfo + (paddle -> pstate.incrSpeed(0.1))
            )
        }
        else {
            world = world.copy(
                paddleInfo = world.paddleInfo + (paddle -> PaddleS(PaddleSpeed, true))
            )
        }
    }
    if (isKeyPressed(downkey) && !paddle.collidesWith(stageBot)) {
        paddle.translate(0, -pstate.speed)
        if (!pstate.lastUp) {
            world = world.copy(
                paddleInfo = world.paddleInfo + (paddle -> pstate.incrSpeed(0.1))
            )
        }
        else {
            world = world.copy(
                paddleInfo = world.paddleInfo + (paddle -> PaddleS(PaddleSpeed, false))
            )
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
        case Kc.VK_ESCAPE => stopAnimations()
        case _            =>
    }
}
activateCanvas()
