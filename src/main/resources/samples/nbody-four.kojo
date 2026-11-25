// Simulation journey, step 9/12: the same simulation, with the Vec and Body types.
// Four bodies: two pairs of twin stars. The colors are translucent, so where the
// orbits cross you get new colors. Set brush to 0 to see just the bodies, or make
// it bigger for fatter orbits.
val (pos, size, initSpeed, gravity, sample) = (50.0, 5.0, 1.0, 100.0, 4)
val brush = 6            // paints the orbits. 0 = off
val alpha = 50
val softening = size * 3 // closest-approach limit: gravity stops growing below this

val red = Color(255, 0, 0, alpha)
val green = Color(0, 255, 0, alpha)
val blue = Color(0, 110, 255, alpha)
val orange = Color(255, 255, 0, alpha) // yellow, really

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

def jitter = random(10) // a small nudge, so the twins are not exact copies
val bodies = Seq(
  Body(1.0, Point(pos + jitter, pos + jitter), Vec(initSpeed, 0.0), Look(size, red)),
  Body(1.0, Point(-pos + jitter, -pos + jitter), Vec(-initSpeed, 0.0), Look(size, blue)),
  Body(1.0, Point(-pos + jitter, pos + jitter), Vec(0.0, 0.0), Look(size, green)),
  Body(1.0, Point(pos + jitter, -pos + jitter), Vec(0.0, 0.0), Look(size, orange)),
)

def dv(p1: Point, p2: Point, mass2: Double) = { // change in velocity, i.e. acceleration
  val v = Vec(p2.x - p1.x, p2.y - p1.y)
  val d = math.max(softening, v.length)
  v.scaleBy(gravity * mass2 / (d * d * v.awayFromZero))
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