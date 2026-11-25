// Simulation journey, step 5/12: friction and energy loss.
// Three new coefficients: slowing down in the air, slowing down on the ground,
// and the energy lost on every bounce. Once the vertical speed gets small enough
// the ball stops bouncing and starts rolling.
cleari
gridOn
axesOn
val (radius, gravity, bounceLoss) = (9, 0.2, 0.79)
val (airFriction, groundFriction) = (0.999, 0.99)
val ball = Picture.circle(radius)
val (cb, r) = (canvasBounds, radius)
val (start, groundLevel, left, right) = (cb.getMaxY - r, cb.y + r, cb.x + r, cb.getMaxX - r)
draw(trans(left, start) * fillColor(yellow) -> ball)
var (dx, dy) = (3.0, 0.0)
animate {
    ball.setPosition(ball.position.x + dx, ball.position.y + dy)
    dx *= airFriction
    dy = if (ball.position.y > groundLevel) {
        dy - gravity
    } else {
        ball.setPosition(ball.position.x, groundLevel)
        if (dy.abs < 0.9) {
            dx *= groundFriction
            0
        } else {
            -dy * bounceLoss
        }
    }
    if (ball.position.x < left || ball.position.x > right) {
        dx *= -1
    }
}
