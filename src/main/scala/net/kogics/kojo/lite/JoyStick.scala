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
}
