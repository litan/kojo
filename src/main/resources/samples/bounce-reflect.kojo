// Simulation journey, step 2/12: reflection.
// Instead of stopping we bounce: the step sizes (dx, dy) are variables now, and
// we flip their sign when we hit a wall. The loop never ends.
cleari
gridOn
axesOn
val r = 10
val ball = Picture.circle(r)
ball.draw
ball.setPosition(-150, -100)
var dx = 2
var dy = 1
animate {
    ball.setPosition(ball.position.x + dx, ball.position.y + dy)
    if (ball.position.x >= 200 || ball.position.x <= -200)
        dx = -dx
    if (ball.position.y >= 100 || ball.position.y <= -100)
        dy = -dy
}
