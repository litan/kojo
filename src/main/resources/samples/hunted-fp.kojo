cleari()
drawStage(cm.white)
val cb = canvasBounds

// game model/state

case class Player(x: Double, y: Double, w: Double, h: Double)
case class Hunter(x: Double, y: Double, w: Double, h: Double, vel: Vector2D)

case class Model(
    player:   Player,
    hunters:  Seq[Hunter],
    gameOver: Boolean
)

// possible events/messages that can update the model
trait Msg
case object Tick extends Msg
case object MoveLeft extends Msg
case object MoveRight extends Msg
case object MoveUp extends Msg
case object MoveDown extends Msg
case object DontMove extends Msg

val nh = 20
def init: Model =
    Model(
        Player(cb.x + cb.width / 2, cb.y + 20, 40, 40),
        (1 to nh).map { n =>
            Hunter(
                cb.x + cb.width / (nh + 2) * n,
                cb.y + randomDouble(100, cb.height - 200),
                40, 40,
                Vector2D(random(1, 4), random(1, 4))
            )
        },
        false
    )

val speed = 5
val cd = new net.kogics.kojo.gaming.CollisionDetector()
def update(m: Model, msg: Msg): Model = msg match {
    case MoveLeft =>
        val player = m.player
        m.copy(player = player.copy(x = player.x - speed))
    case MoveRight =>
        val player = m.player
        m.copy(player = player.copy(x = player.x + speed))
    case MoveUp =>
        val player = m.player
        m.copy(player = player.copy(y = player.y + speed))
    case MoveDown =>
        val player = m.player
        m.copy(player = player.copy(y = player.y - speed))
    case DontMove => m
    case Tick =>
        val newm = m.copy(hunters =
            m.hunters.map { h =>
                val newx = h.x + h.vel.x
                val newy = h.y + h.vel.y
                val vx = if (cd.collidesWithHorizontalEdge(newx, h.w))
                    h.vel.x * -1 else h.vel.x
                val vy = if (cd.collidesWithVerticalEdge(newy, h.h))
                    h.vel.y * -1 else h.vel.y
                h.copy(x = newx, y = newy, vel = Vector2D(vx, vy))
            })

        val p = m.player
        val gameOver =
            cd.collidesWithEdge(p.x, p.y, p.w, p.h) ||
                newm.hunters.exists { h =>
                    cd.collidesWith(p.x, p.y, p.w, p.h, h.x, h.y, h.w, h.h)
                }
        newm.copy(gameOver = gameOver)
}

def playerPic(p: Player): Picture = {
    val base = Picture.rectangle(p.w, p.h)
    return base.thatsFilledWith(cm.yellow).thatsStrokeColored(black).thatsTranslated(p.x, p.y)
}

def hunterPic(h: Hunter): Picture = {
    val base = Picture.rectangle(h.w, h.h)
    return base.thatsFilledWith(cm.lightBlue).thatsStrokeColored(black).thatsTranslated(h.x, h.y)
}

def view(m: Model): Picture = {
    val viewPics =
        m.hunters.map { h =>
            hunterPic(h)
        }.appended(playerPic(m.player))

    if (m.gameOver) {
        picStack(viewPics.appended(Picture.text("Game Over", 40)))
    }
    else
        picStack(viewPics)
}

val tickSub = Subscriptions.onAnimationFrame {
    Tick
}

val keyDownSub = Subscriptions.onKeyDown { keyCode =>
    keyCode match {
        case Kc.VK_LEFT  => MoveLeft
        case Kc.VK_RIGHT => MoveRight
        case Kc.VK_UP    => MoveUp
        case Kc.VK_DOWN  => MoveDown
        case _           => DontMove
    }
}

def subscriptions(m: Model) = {
    if (m.gameOver) Seq()
    else Seq(tickSub, keyDownSub)
}

runGame(init, update, view, subscriptions)
activateCanvas()
