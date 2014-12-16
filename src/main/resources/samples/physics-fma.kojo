// Shows the use of Newton's second law to calculate the motion of two balls
cleari()
val wall = fillColor(brown) * penColor(noColor) * trans(-45, -50) -> PicShape.rect(200, 20)
val ball1 = fillColor(green) * penColor(noColor) -> PicShape.circle(25)
val ball2 = fillColor(blue) * penColor(noColor) * trans(0, 100) -> PicShape.circle(25)

draw(wall, ball1, ball2)

val force1 = 100 // Newtons
val force2 = 100 // Newtons

val mass1 = 5 // kg
val mass2 = 10 //kg 

val acc1 = force1 / mass1 // m/s/s
val acc2 = force2 / mass2 // m/s/s

val t0 = epochTime // time, in seconds, since a reference time

animate {
    // s = 1/2 * a * t^2
    val t = epochTime - t0 // time, in seconds, since the start of the program
    val s1 = 0.5 * acc1 * t * t // distance travelled by ball1, in meters
    val s2 = 0.5 * acc2 * t * t

    ball1.setPosition(s1, ball1.position.y) // use pixels instead of meters
    ball2.setPosition(s2, ball2.position.y)
}
