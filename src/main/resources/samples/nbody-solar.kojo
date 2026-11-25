// Simulation journey, step 11/12: one dominant central mass.
// A sketch of a solar system: one heavy star in the middle and four small bodies
// around it. When one mass dominates you get tidy orbits instead of three-body
// chaos -- compare with the Three Bodies sample: almost the same code, different masses.
// Sun(red), comet1(green), Earth(blue), comet2(purple), Mars(orange)
val sample = 11 // 1 and up; how densely we trace the orbits

case class Look(size: Double, color: Color) { val transform = penColor(color) * fillColor(color) }
case class Vec(var x: Double, var y: Double) { // can stand for a position, a velocity or a force
  def add(v2: Vec) = { x += v2.x; y += v2.y; this }
  def scaleBy(c: Double) = { x *= c; y *= c; this }
  def lengthSquared = x * x + y * y
  def length = math.sqrt(lengthSquared)
  // Here one mass dominates in the middle and the bodies never get very close to
  // each other, so instead of softening it is enough to keep the division safe.
  def lengthCubed = math.max(0.0001, length * lengthSquared)
}
case class Body(mass: Double, startPos: Point, vel: Vec, look: Look) {
  val p = trans(startPos.x, startPos.y) * look.transform -> Picture.circle(look.size)
  draw(p)
}

clear(); invisible()

val (pos, size, initSpeed) = (40.0, 3.0, 2.0)
val bodies = Seq(
  Body(1000, Point(0, 0), Vec(0, 0), Look(6 * size, red)),
  Body(2.0, Point(pos, 2 * pos), Vec(initSpeed, -1.5 * initSpeed), Look(2 * size, green)),
  Body(3.0, Point(4 * pos, 2 * pos), Vec(initSpeed / 2, -initSpeed), Look(2 * size, blue)),
  Body(5.0, Point(-6 * pos, 2 * pos), Vec(-initSpeed / 3, initSpeed), Look(3 * size, purple)),
  Body(6.0, Point(0, -7 * pos), Vec(-initSpeed, 0), Look(3 * size, orange)),
)

def dv(p1: Point, p2: Point, mass2: Double) = { // change in velocity, i.e. acceleration
  val v = Vec(p2.x - p1.x, p2.y - p1.y)
  v.scaleBy(mass2 / v.lengthCubed)
}

var frame = 0
animate {
  if (frame % sample == 0)
    for (b <- bodies) draw(trans(b.p.position.x, b.p.position.y) * b.look.transform -> Picture.circle(2))
  frame += 1
  for (b <- bodies) {
    for (other <- bodies if other != b)
      b.vel.add(dv(b.p.position, other.p.position, other.mass))
    b.p.setPosition(b.p.position.x + b.vel.x, b.p.position.y + b.vel.y)
  }
}
zoomXY(0.3, 0.3, pos, -pos)
