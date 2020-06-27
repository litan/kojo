package net.kogics.kojo.lite

class JoyStick(radius: Double)(builtins: Builtins) {
  import builtins._
  val opacity = 0.6
  val perimeter = Picture.circle(radius)
  perimeter.setFillColor(cm.khaki)
  perimeter.setPenColor(cm.black)
  perimeter.setPenThickness(4)
  perimeter.setOpacity(opacity)

  val control = Picture.circle(radius / 2)
  control.setPenColor(noColor)
  control.setFillColor(cm.black)
  control.setOpacity(opacity)

  val origin = Picture.circle(radius / 5)
  origin.setPenColor(noColor)
  origin.setFillColor(cm.black)
  origin.setOpacity(opacity)

  val zeroVec = Vector2D(0, 0)
  private var currentVec = zeroVec
  perimeter.onMouseDrag { (x, y) =>
    val op = origin.position
    val dx = x - op.x
    val dy = y - op.y
    val vec = Vector2D(dx, dy).limit(radius / 2)
    control.setPosition(op.x + vec.x, op.y + vec.y)
    currentVec = vec
  }
  perimeter.onMouseRelease { (x, y) =>
    val op = origin.position
    control.setPosition(op.x, op.y)
    currentVec = zeroVec
  }
  control.forwardInputTo(perimeter)

  def draw(): Unit = {
    perimeter.draw()
    control.draw()
  }

  def setPostiion(x: Double, y: Double): Unit = {
    perimeter.setPosition(x, y)
    control.setPosition(x, y)
    origin.setPosition(x, y)
  }

  def currentVector = currentVec

  def movePlayer(player: Picture, scaleVelocity: Double = 1, directionConstraint: net.kogics.kojo.util.Vector2D = null): Unit = {
    import builtins.TSCanvas._
    val vel = if (directionConstraint == null)
      currentVector * scaleVelocity else currentVector.project(directionConstraint) * scaleVelocity
    player.offset(vel)

    def handlePossibleCollision(stagePart: Picture, vel: Vector2D): Unit = {
      if (player.collidesWith(stagePart)) {
        val StageRight = stageRight; val StageLeft = stageLeft; val StageTop = stageTop; val StageBot = stageBot
        val (stagePart2, stagePart3, velPart) = stagePart match {
          case StageRight => (stageTop, stageBot, Vector2D(0, vel.y))
          case StageLeft  => (stageTop, stageBot, Vector2D(0, vel.y))
          case StageTop   => (stageLeft, stageRight, Vector2D(vel.x, 0))
          case StageBot   => (stageLeft, stageRight, Vector2D(vel.x, 0))
        }

        val vel2 = -vel.normalize
        while (player.collidesWith(stagePart)) {
          player.offset(vel2)
        }
        player.offset(velPart)
        repeatFor(Seq(stagePart2, stagePart3)) { stagePartX =>
          if (player.collidesWith(stagePartX)) {
            val vel2 = -velPart.normalize
            while (player.collidesWith(stagePartX)) {
              player.offset(vel2)
            }
          }
        }
      }
    }
    handlePossibleCollision(stageRight, vel)
    handlePossibleCollision(stageLeft, vel)
    handlePossibleCollision(stageTop, vel)
    handlePossibleCollision(stageBot, vel)
  }
}
