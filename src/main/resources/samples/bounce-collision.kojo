// Simulation journey, step 12/12: real billiards.
// We are back at the table from step 3, but now we have Body and Vec: the balls
// bounce off each other as well as off the walls. When two balls of equal mass
// collide they SWAP the velocity components along the line joining their centres;
// the component at right angles to that line does not change at all. That is the
// whole collision rule.
val (r, friction) = (12.0, 0.9995)
val (leftEdge, rightEdge) = (-200.0, 200.0)
val (bottomEdge, topEdge) = (-100.0, 100.0)

case class Look(size: Double, color: Color) { val transform = penColor(color) * fillColor(color) }
case class Vec(var x: Double, var y: Double) {
  def add(v2: Vec) = { x += v2.x; y += v2.y; this }
  def scaleBy(c: Double) = { x *= c; y *= c; this }
  def minus(v2: Vec) = Vec(x - v2.x, y - v2.y) // a new Vec: unlike add, this leaves us alone
  def dot(v2: Vec) = x * v2.x + y * v2.y       // the dot product
  def length = math.sqrt(x * x + y * y)
}
case class Body(mass: Double, startPos: Point, vel: Vec, look: Look) {
  val p = trans(startPos.x, startPos.y) * look.transform -> Picture.circle(look.size)
  draw(p)
}

clear(); invisible()
draw(penColor(black) * trans(leftEdge, bottomEdge) ->
    Picture.rectangle(rightEdge - leftEdge, topEdge - bottomEdge))

// The cue ball comes in from the left, the other four are racked up across the table
val bodies = Seq(
  Body(1.0, Point(-150, 0), Vec(4.0, 0.3), Look(r, gray)),
  Body(1.0, Point(60, 0), Vec(0.0, 0.0), Look(r, red)),
  Body(1.0, Point(84, 13), Vec(0.0, 0.0), Look(r, blue)),
  Body(1.0, Point(84, -13), Vec(0.0, 0.0), Look(r, green)),
  Body(1.0, Point(108, 0), Vec(0.0, 0.0), Look(r, purple)),
)

// Collide two balls if they are touching AND moving towards each other. The masses
// are equal here, so the swap is simple; otherwise we would have to share the
// components out in proportion to the masses.
def collide(a: Body, b: Body): Unit = {
  val (pa, pb) = (a.p.position, b.p.position)
  val gap = Vec(pb.x - pa.x, pb.y - pa.y)
  val d = gap.length
  if (d > 0 && d < 2 * r) {
    val unit = Vec(gap.x / d, gap.y / d) // the line joining the centres
    val closing = a.vel.minus(b.vel).dot(unit)
    if (closing > 0) { // leave them alone if they are moving apart, or they stick together
      a.vel.add(Vec(-closing * unit.x, -closing * unit.y))
      b.vel.add(Vec(closing * unit.x, closing * unit.y))
    }
    val push = (2 * r - d) / 2 // undo the overlap
    a.p.setPosition(pa.x - unit.x * push, pa.y - unit.y * push)
    b.p.setPosition(pb.x + unit.x * push, pb.y + unit.y * push)
  }
}

animate {
  for (b <- bodies) {
    b.vel.scaleBy(friction) // the friction from step 5: the balls do come to rest
    b.p.setPosition(b.p.position.x + b.vel.x, b.p.position.y + b.vel.y)
    val k = b.p.position // bounce only when heading INTO the wall; "did it touch" makes balls stick
    if ((k.x <= leftEdge + r && b.vel.x < 0) || (k.x >= rightEdge - r && b.vel.x > 0))
        b.vel.x = -b.vel.x
    if ((k.y <= bottomEdge + r && b.vel.y < 0) || (k.y >= topEdge - r && b.vel.y > 0))
        b.vel.y = -b.vel.y
  }
  for (i <- bodies.indices; j <- bodies.indices if j > i)
    collide(bodies(i), bodies(j))
}
