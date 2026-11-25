// Simulation journey, step 7/12: add a third body and you get chaos.
// Three bodies, chaos. Triple star systems do exist, but a real one usually has
// two stars close together and a third far away in a wide orbit (see Stable
// Triple). Otherwise you get what you see here: there is no closed-form solution
// for three bodies, and the tiniest change in the starting values gives you a
// completely different picture after a while.
val (pos, speed, size, gravity, sample) = (50.0, 1.0, 5.0, 100.0, 4)
val brush = 0.2          // orbit thickness. 0 draws nothing
val softening = size * 3 // closest-approach limit: gravity stops growing below this

case class Look(size: Double, color: Color) { val transform = penColor(color) * fillColor(color) }
case class Vec(var x: Double, var y: Double) { // can stand for a position, a velocity or a force
  def add(v2: Vec) = { x += v2.x; y += v2.y; this }
  def scaleBy(c: Double) = { x *= c; y *= c; this }
  def lengthSquared = x * x + y * y
  def length = math.sqrt(lengthSquared)
  def awayFromZero = math.max(0.0001, length) // keep the division safe
}
case class Body(mass: Double, startPos: Point, vel: Vec, look: Look) {
  val p = trans(startPos.x, startPos.y) * look.transform -> Picture.circle(look.size)
  draw(p)
}
clear(); invisible()

def jitter = random(10) // a small nudge, so the three are not perfectly symmetric
val bodies = Seq(
  Body(1.0, Point(pos + jitter, pos + jitter), Vec(speed, 0.0), Look(size, red)),    // flung to the right
  Body(1.0, Point(-pos + jitter, -pos + jitter), Vec(-speed, 0.0), Look(size, blue)), // to the left
  Body(1.0, Point(-pos + jitter, pos + jitter), Vec(0.0, 0.0), Look(size, green)),    // at rest to begin with
)

def dv(p1: Point, p2: Point, mass2: Double) = { // change in velocity, i.e. acceleration
  val v = Vec(p2.x - p1.x, p2.y - p1.y)
  val d = math.max(softening, v.length)
  v.scaleBy(gravity * mass2 / (d * d * v.awayFromZero)) // size gravity*mass/d², direction a unit vector
}

var frame = 1
animate {
  if (brush > 0 && frame % sample == 1)
    for (b <- bodies) draw(trans(b.p.position.x, b.p.position.y) * b.look.transform -> Picture.circle(brush))
  frame += 1
  for (b <- bodies) // first every velocity is worked out from the same snapshot...
    for (other <- bodies if other != b)
      b.vel.add(dv(b.p.position, other.p.position, other.mass))
  for (b <- bodies) // ...then everybody moves together
    b.p.setPosition(b.p.position.x + b.vel.x, b.p.position.y + b.vel.y)
}