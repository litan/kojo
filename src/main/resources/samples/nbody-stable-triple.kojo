// Simulation journey, step 10/12: the same code, a different start.
// A stable triple. Real triple star systems are usually built like this: a close
// pair, plus a third body going round both of them from far away. The only
// difference from the chaotic one (see the Three Bodies sample) is the starting
// values -- the code is the same. The speeds here are circular-orbit speeds: the
// pair spins around itself while the third circles them like a planet. Measured
// over 60000 steps: the orbit is periodic.
val (pairRadius, farRadius, size, gravity, sample) = (20.0, 150.0, 5.0, 100.0, 4)
val (pairMass, farMass) = (1.0, 0.2)
val brush = 0.5          // orbit thickness. 0 draws nothing
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
zoomXY(0.8, 0.8, 0, 0) // so the third body's orbit fits on the canvas

// Circular-orbit speeds. The pair: each star sits pairRadius from their common
// centre, gravity is gravity*mass/(2*radius)², centripetal acceleration speed²/radius.
val pairSpeed = math.sqrt(gravity * pairMass / (4 * pairRadius))
// The third one: treat the pair as a single body of 2*mass, so speed = sqrt(gravity*2*mass/distance)
val farSpeed = math.sqrt(gravity * 2 * pairMass / farRadius)
// Give the pair an opposite kick so the total momentum of the system stays zero,
// otherwise the whole picture slowly drifts to the right.
val recoil = farMass * farSpeed / (2 * pairMass)

val bodies = Seq(
  Body(pairMass, Point(pairRadius, 0), Vec(-recoil, pairSpeed), Look(size, red)),
  Body(pairMass, Point(-pairRadius, 0), Vec(-recoil, -pairSpeed), Look(size, blue)),
  Body(farMass, Point(0, -farRadius), Vec(farSpeed, 0), Look(size * 0.7, green)),
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