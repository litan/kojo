// How this code grew
// ==================
// 1- First we moved a ball with an animate loop
// 2- Then we added the ability to bounce
// 3- Then we drew the billiard table too
// 4- Then we added gravity, so the ball would bounce instead of drift
// 5- Then friction, and the energy lost on every bounce
// 6- Then gravity between two bodies out in space
// 7- Then three bodies
// 8- Then four; by now 12 separate variables and five helpers
// 9- Then the Vec and Body classes: the same simulation, a third of the code
// 10- Then the same code with different starting values: a stable triple
// 11- Then a solar system with one dominant central mass
// 12- And back to the table: now the balls bounce off each other too
//
// The twelve samples under Samples > Simulation, in exactly this order.
//
// Step 1/12: the simplest version. Draw a circle, nudge it a little every step.
cleari
gridOn
axesOn
val r = 10 // radius
val ball = Picture.circle(r) // the ball's picture
ball.draw // put it on the canvas
ball.setPosition(-200, -100) // start from the bottom left corner
// animate starts a loop that keeps the ball moving:
animate {
    // the commands in this block run about 40 times a second
    ball.setPosition(ball.position.x + 2, ball.position.y + 1)
    if (ball.position.x >= 200) // far enough -- stop the loop
        stopAnimation
}
