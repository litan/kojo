// Simulation journey, step 8/12: a fourth body, still without types.
// The same four bodies, but without the types: a separate variable for every
// body, a separate var for every velocity component, and five helpers that split
// the force up by angle. Read it side by side with the Four Bodies sample: same
// simulation, same result. The types do not ADD anything, they TAKE AWAY -- 12
// variables become one sequence, five helpers become one dv, and adding a fifth
// body is one line there and four blocks here.
val (pos, size, initSpeed, gravity, sample) = (50, 5, 1.0, 100, 4)
val brush = 6 // paints the orbits. 0 = off
val alpha = 50
val red = Color(255, 0, 0, alpha)
val green = Color(0, 255, 0, alpha)
val blue = Color(0, 110, 255, alpha)
val orange = Color(255, 255, 0, alpha) // yellow, really
val (c1, c2, c3, c4) = (red, blue, green, orange)
val b1 = Picture.circle(size).withPenColor(c1).withFillColor(c1)
val b2 = Picture.circle(size).withPenColor(c2).withFillColor(c2)
val b3 = Picture.circle(size).withPenColor(c3).withFillColor(c3)
val b4 = Picture.circle(size).withPenColor(c4).withFillColor(c4)
clear(); invisible()
draw(b1.withTranslation(pos + random(10), pos + random(10)),
     b2.withTranslation(-pos + random(10), -pos + random(10)),
     b3.withTranslation(-pos + random(10), pos + random(10)),
     b4.withTranslation(pos + random(10), -pos + random(10)))
var (dx1, dy1, dx2, dy2, dx3, dy3, dx4, dy4) = (initSpeed, 0.0, -initSpeed, 0.0, 0.0, 0.0, 0.0, 0.0)
def angle(p1: Point, p2: Point): Double = math.atan(math.abs((p1.y - p2.y) / (p1.x - p2.x)))
def distance(p1: Point, p2: Point): Double = math.sqrt(math.pow(p1.x - p2.x, 2) + math.pow(p1.y - p2.y, 2))
def vertical(diagonal: Double, angle: Double) = diagonal * math.sin(angle)
def horizontal(diagonal: Double, angle: Double) = diagonal * math.cos(angle)
def force(distance: Double) = gravity / math.pow(math.max(size * 3, distance), 2)
def dv(p1: Point, p2: Point) = {
  val a = angle(p1, p2)
  val f = force(distance(p1, p2))
  val (fx, fy) = (horizontal(f, a), vertical(f, a))
  (if (p1.x > p2.x) -fx else fx,
   if (p1.y > p2.y) -fy else fy)
}
var frame = 1
animate {
  val (p1, p2, p3, p4) = (b1.position, b2.position, b3.position, b4.position)
  if (brush > 0 && frame % sample == 1) {
    for ((p, c) <- Seq((p1, c1), (p2, c2), (p3, c3), (p4, c4)))
      draw(Picture.circle(brush).withPenColor(c).withFillColor(c).withTranslation(p.x, p.y))
  }
  frame += 1
  for (p <- Seq(p2, p3, p4)) { val (fx, fy) = dv(p1, p); dx1 += fx; dy1 += fy }
  for (p <- Seq(p3, p4, p1)) { val (fx, fy) = dv(p2, p); dx2 += fx; dy2 += fy }
  for (p <- Seq(p4, p1, p2)) { val (fx, fy) = dv(p3, p); dx3 += fx; dy3 += fy }
  for (p <- Seq(p1, p2, p3)) { val (fx, fy) = dv(p4, p); dx4 += fx; dy4 += fy }
  b1.setPosition(p1.x + dx1, p1.y + dy1)
  b2.setPosition(p2.x + dx2, p2.y + dy2)
  b3.setPosition(p3.x + dx3, p3.y + dy3)
  b4.setPosition(p4.x + dx4, p4.y + dy4)
}
