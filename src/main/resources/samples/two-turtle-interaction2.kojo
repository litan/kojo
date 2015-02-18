// This is an alternative version of the Interacting Turtles sample
// This uses turtle.react instead of turtle.act to make the turtles run concurrently 
cleari()

val t1 = newTurtle(-500, 0)
t1.setPenColor(Color(0, 0, 255, 120))
t1.setPenThickness(4)

val t2 = newTurtle(500, 0)
t2.setPenColor(Color(0, 255, 0, 120))

// the code that you provide to react runs in the GUI thread
// Your code gets called around 30 times per second
// Don't spend too long in your code, or you'll make the GUI sluggish
// In the worst case, you might lock the GUI (making a Kojo restart necessary)
t1.react { self =>
  self.towards(mousePosition)
  self.forward(2)
}

t2.react { self =>
  self.towards(t1)
  self.forward(1)
}
