// A Physics Kinematics animation

toggleFullScreenCanvas()
cleari()
axesOn()
gridOn()
zoom(0.75, 500, 300)

val a = 20.0 // initail acceleration
val u = 10.0 // initial velocity

val car = newTurtle(0, 0, Costume.car)

val splot = newTurtle(0, 0, Costume.pencil)
splot.setPenColor(blue)

val vplot = newTurtle(0, u, Costume.pencil)
vplot.setPenColor(green)

val t0 = epochTime // time, in seconds, since a reference time
val time = newTurtle(100, -50)

def showTime(t: String) {
    time.clear()
    time.invisible()
    time.setPenColor(blue)
    time.write(t)
}

animate {
    val t = epochTime - t0 // time, in seconds, since the start of the program
    // determine the car's velocity and accelaration based on the equations of Kinematics 
    val v = u + a * t 
    val s = u * t + 0.5 * a * t * t 
    car.setPosition(s, 10)
    splot.moveTo(t * 50, s)
    vplot.moveTo(t * 50, v)
    showTime(f"Time: $t%.1f seconds")
}

onKeyPress { k =>
    k match {
      case Kc.VK_ESCAPE => stopAnimation()
      case _ =>
    }
}
