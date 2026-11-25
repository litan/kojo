// Simulation journey, step 6/12: out into space.
// The dance of two twin stars, trapped by gravity forever. No walls any more;
// the only thing changing their speed is the pull they put on each other.
// pos:        where the stars start: one at x,y = 40,40, the other at -40,-40
// gConstant:  the gravitational pull (and so the stars' turning) scales with this
// initSpeed:  how fast they move apart to begin with, horizontally
// sample:     the simulation redraws the stars about forty times a second to make
//             them look like they are moving. We use this sampling period when we
//             trace the orbits: we draw only one move in every 13.
val (pos, size, initSpeed, gConstant, sample) = (40, 5, 1.1, 200, 13)
// The first star is red, the second blue -- no politics intended :-)
val (s1, s2) = (penColor(red) * fillColor(red), penColor(blue) * fillColor(blue))
// c1 is the circle standing for the first body, c2 for the second
val (c1, c2) = (s1 -> Picture.circle(size), s2 -> Picture.circle(size))
cleari() // clear the canvas and hide the turtle
draw(trans(pos, pos) -> c1, trans(-pos, -pos) -> c2) // move both stars to their starting points and draw them
// the bodies' speed: delta x and delta y, how far each body shifts on every step of the loop
var (dx1, dy1, dx2, dy2) = (initSpeed, 0.0, -initSpeed, 0.0)
def angle(p1: Point, p2: Point): Double = math.atan(math.abs((p1.y - p2.y) / (p1.x - p2.x)))
def distance(p1: Point, p2: Point): Double = math.sqrt(math.pow(p1.x - p2.x, 2) + math.pow(p1.y - p2.y, 2))
// the vertical and horizontal parts of the diagonal
def vertical(diagonal: Double, angle: Double) = diagonal * math.sin(angle)
def horizontal(diagonal: Double, angle: Double) = diagonal * math.cos(angle)
// The pull falls off with the square of the distance between the two bodies.
// Their masses are the same, so we leave them out
def force(distance: Double) = gConstant / math.pow(math.max(0.01, distance), 2)
var step = 0 // count the animation steps, so we can sample them while tracing the orbits
animate { // this runs the block below 40 times a second. Call each run a step.
    val (p1, p2) = (c1.position, c2.position)
    step += 1
    if (step % sample == 1) for ((p, s) <- Seq((p1, s1), (p2, s2))) { draw(trans(p.x, p.y) * s -> Picture.circle(0.6)) }
    val (d, a) = (distance(p1, p2), angle(p1, p2))
    val f = force(d)
    val (fx, fy) = (horizontal(f, a), vertical(f, a))
    if (p1.x > p2.x) { dx1 -= fx; dx2 += fx } else { dx1 += fx; dx2 -= fx }
    if (p1.y > p2.y) { dy1 -= fy; dy2 += fy } else { dy1 += fy; dy2 -= fy }
    c1.setPosition(p1.x + dx1, p1.y + dy1)
    c2.setPosition(p2.x + dx2, p2.y + dy2)
}
