// In this program, we're trying to make a synchronized drawing with multiple turtles
cleari()

// a new command to make squares
// t - the turtle that draws the square
// n - the size of the square
// delay - the turtle's animation delay; this controls the synchronization effect
// we use runInBackground below to make the turtles run together
def square(t: Turtle, n: Int, delay: Int) = runInBackground {
    t.setAnimationDelay(delay)
    repeat(4) {
        t.forward(n)
        t.right()
    }
}
val t1 = newTurtle(0, 0)
val t2 = newTurtle(-200, 100)
val t3 = newTurtle(250, 100)
val t4 = newTurtle(250, -50)
val t5 = newTurtle(-200, -50)

square(t1, 100, 100)
square(t2, 50, 200)
square(t3, 50, 200)
square(t4, 50, 200)
square(t5, 50, 200)
