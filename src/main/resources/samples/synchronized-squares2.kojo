// This is an alternative version of the synchronized squares sample
// This uses turtle.act instead of runInBackground to make the turtles run concurrently 
cleari()

// a new command to make squares
// t - the turtle that draws the square
// n - the size of the square
// delay - the turtle's animation delay; this controls the synchronization effect
def square(t: Turtle, n: Int, delay: Int) {
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

// the code that you provide to act runs in a different thread
// that allows this turtle to run concurrently with other turtles
t1.act { self => // we give this turtle the name 'self' within act {...}
    square(self, 100, 100)    
}

t2.act { self => 
    square(self, 50, 200)
}

t3.act { self => 
    square(self, 50, 200)
}

t4.act { self => 
    square(self, 50, 200)
}

t5.act { self => 
    square(self, 50, 200)
}
