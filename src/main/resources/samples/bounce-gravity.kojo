// Simulation journey, step 4/12: gravity.
// The vertical speed is not constant any more: every step it drops by gravity.
// Hitting the ground flips the sign, so the ball bounces back to exactly the same
// height -- no energy lost. The next step adds friction and energy loss.
cleari
gridOn
axesOn
val (gravity, radius) = (0.2, 9.0)
val ball = Picture.circle(radius)
val (cb, d) = (canvasBounds, 2 * radius)
val (start, groundLevel, left, right) = (-cb.y - d, cb.y + d, cb.x + d, -cb.x - d)
draw(trans(left, start) * fillColor(yellow) -> ball)
var (dx, dy) = (1.2, 0.0)
animate {
    val p = ball.position
    dy = if (p.y > groundLevel) dy - gravity else -dy
    if (p.x < left || p.x > right) dx *= -1
    ball.setPosition(p.x + dx, p.y + dy)
}
