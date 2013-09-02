cleari()

// a new command to make squares
// t - the turtle that draws the square
// n - the size of the square
// delay - the turtle's animation delay; this provides the synchronization effect
def square(t: Turtle, n: Int, delay: Int) {
    t.setAnimationDelay(delay)
    repeat(4) {
        t.forward(n)
        t.right()
    }
}

def eye(t: Turtle, n: Int, delay: Int) {
    square(t, n, delay)
    t.penUp()
    t.forward(n / 4)
    t.right()
    t.forward(n / 4)
    t.left()
    t.penDown()
    t.setFillColor(darkGray)
    square(t, n / 2, delay)
}

val face = newTurtle(-100, -100)
val eye1 = newTurtle(-75, 25)
val eye2 = newTurtle(25, 25)
val mouth = newTurtle(-50, -50)
val nose = newTurtle(0, -25)
val hair = newTurtle(-110, 100)
val body = newTurtle(25, -125)
val legs = newTurtle(0, -150)

// the code that you provide to act runs in a different thread
// that allows this turtle to run concurrently with other turtles
face.act { self => // we give this turtle the name 'self' within act {...}
    self.setFillColor(red)
    square(self, 200, 200)
    self.invisible()
}

eye1.act { self =>
    self.setFillColor(yellow)
    eye(self, 50, 800)
    self.invisible()
}

eye2.act { self =>
    self.setFillColor(yellow)
    eye(self, 50, 800)
    self.invisible()
}

mouth.act { self =>
    self.setAnimationDelay(2000)
    self.setPenColor(yellow)
    self.setPenThickness(14)
    self.right()
    self.forward(100)
    self.invisible()
}

nose.act { self =>
    self.setAnimationDelay(4000)
    self.setPenColor(yellow)
    self.setPenThickness(20)
    self.forward(50)
    self.invisible()
}

hair.act { self =>
    self.setAnimationDelay(200)
    self.right()
    self.setPenColor(black)
    self.setPenThickness(30)
    self.forward(220)
    self.left()
    repeat(10) {
        self.forward(25)
        self.back(25)
        self.left()
        self.forward(22)
        self.right()
    }
    self.forward(25)
    self.invisible()
}

body.act { self => 
    self.setFillColor(yellow)
    self.circle(25)
    self.invisible()
}

legs.act { self => 
    self.setAnimationDelay(3000)
    self.setPenColor(black)
    self.setPenThickness(30)
    self.right(180)
    self.savePosHe()
    self.right(30)
    self.forward(30)
    self.restorePosHe()
    self.left(30)
    self.forward(30)
    self.invisible()
}
