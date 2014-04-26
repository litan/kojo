cleari()
val t1 = newTurtle(-500, 0)
val t2 = newTurtle(500, 0)

// the code that you provide to act runs in a different thread
// that allows this turtle to run concurrently with other turtles
t1.act { self => // we give this turtle the name 'self' within act {...}
    self.setPenColor(Color(0, 0, 255, 120))
    self.setPenThickness(4)
    repeatWhile(true) {
      self.towards(mousePosition)
      self.forward(2)
    } 
}

t2.act { self => 
    self.setPenColor(Color(0, 255, 0, 120))
    repeatWhile(true) {
      self.towards(t1)
      self.forward(1)
    }
}
