def border(t: Turtle, a: Double) = runInBackground {
    t.setAnimationDelay(200)
    t.setPenColor(black)
    t.right()
    t.forward(1200)
    repeat(15){
        t.setFillColor(red)
        t.turn(a)
        t.forward(40)
        t.turn(a)
        t.forward(40)
        t.turn(a)

        t.setFillColor(blue)
        t.turn(a)
        t.forward(40)
        t.turn(a)
        t.forward(40)
        t.turn(a)
    }
    t.invisible()
}

def flower(t:Turtle, c:Color) = runInBackground {
    t.setAnimationDelay(400)
    t.setPenColor(black)
    t.setFillColor(c)
    repeat(4){
        t.right()
        repeat(90){
            t.turn(-2)
            t.forward(2)
        }
    }
    t.invisible()
}

clear()
// when drawing with multiple turtles running in the background
// don't ask the default turtle to do any work
// just hide it
// see TurtleMania sample for reasons
invisible()

val t1=newTurtle(-600,-150)
val t2=newTurtle(-600, 150)

border(t1,120)
border(t2,-120)

val centerT = newTurtle(0, 0)
runInBackground {
    import centerT._
    jumpTo(-50,100)
    setAnimationDelay(200)
    setPenColor(black)
    setFillColor(green)
    repeat(6){
        turn(-120)
        repeat(90){
            turn(-2)
            forward(2)
        }
    }
    invisible()
}

val t3=newTurtle(-300,100)
val t4=newTurtle(-400,0)
val t5=newTurtle(-500,100)
val t6=newTurtle(-600,0)

val t7=newTurtle(200,100)
val t8=newTurtle(300,0)
val t9=newTurtle(400,100)
val t10=newTurtle(500,0)

flower(t3, orange)
flower(t4, yellow)
flower(t5, red)
flower(t6, purple)

flower(t7, orange)
flower(t8, yellow)
flower(t9, red)
flower(t10, purple)