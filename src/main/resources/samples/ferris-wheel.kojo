def car(t: Turtle, c: Color, a: Double) = runInBackground {
    t.setPenColor(c)
    t.setFillColor(c)
    t.turn(a)
    t.forward(150)
    repeat(3){
        t.right()
        t.forward(50)
    }
    t.left()
    t.forward(100)
}

clear()
// when drawing with multiple turtles running in the background
// don't ask the default turtle to do any work
// just hide it
// see TurtleMania sample for reasons
invisible()

val t0 = newTurtle(0,0)  
val t1 = newTurtle(0,0)
val t2 = newTurtle(0,0)
val t3 = newTurtle(0,0)
val t4 = newTurtle(0,0)
val t5 = newTurtle(0,0)
val t6 = newTurtle(0,0)
val t7 = newTurtle(0,0)
val t8 = newTurtle(0,0)
val t9 = newTurtle(0,0)
val t10 = newTurtle(0,0)
val t11 = newTurtle(0,0)

car(t0, red,0)
car(t1, yellow,30)
car(t2, blue,60)
car(t3, green,90)
car(t4, orange,120)
car(t5, purple,150)
car(t6, red,180)
car(t7, yellow,210)
car(t8, blue,240)
car(t9, green,270)
car(t10, orange,300)
car(t11, purple,330)