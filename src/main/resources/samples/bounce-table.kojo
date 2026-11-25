// Simulation journey, step 3/12: the billiard table.
// The walls are no longer numbers buried in the code: we draw the table and work
// the bounce limits out from its edges. The ball's radius counts too, so that it
// stops at the cushion instead of sinking into it.
cleari
gridOn
axesOn
val r = 10
val ball = Picture.circle(r)
ball.draw
val (leftEdge, rightEdge) = (-200, 200); val width = rightEdge - leftEdge
val (bottomEdge, topEdge) = (-100, 100); val height = topEdge - bottomEdge
draw(penColor(black) * trans(leftEdge, bottomEdge) ->
    Picture.rectangle(width, height))
ball.setPosition(leftEdge + 20, bottomEdge + 30)
var (dx, dy) = (2, 4)
animate {
    ball.setPosition(ball.position.x + dx, ball.position.y + dy)
    if (ball.position.x >= rightEdge - r ||
        ball.position.x <= leftEdge + r) {
        dx = -dx
    }
    if (ball.position.y >= topEdge - r ||
        ball.position.y <= bottomEdge + r) {
        dy = -dy
    }
}
