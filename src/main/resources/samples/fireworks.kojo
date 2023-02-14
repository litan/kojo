cleari()
originBottomLeft()
setBackground(black)

setNoteInstrument(Instrument.ACOUSTIC_BASS)
playNote(50, 150)
playNote(45, 200)

val cb = canvasBounds
val gravity = Vector2D(0, -0.1)

class Particle(x0: Double, y0: Double, hu: Double, seed: Boolean) {
    var location = Vector2D(x0, y0)
    private var velocity =
        if (seed) Vector2D(0, random(5, 12))
        else randomExplosionVector
    private var acceleration = Vector2D(0, 0)
    private var lifespan = 255.0
    private var exploded = false

    private val pic = Picture.circle(1)

    def randomExplosionVector: Vector2D = {
        val rv1 = Vector2D(randomNormalDouble, randomNormalDouble)
        val r1 = randomDouble(2, 4)
        rv1 * r1
    }

    def applyForce(force: Vector2D) {
        acceleration += force
    }

    def shouldExplode: Boolean = !exploded && lifespan <= 0

    def checkExplode() {
        if (seed && velocity.y < 0) {
            lifespan = 0
        }
    }

    def isDead: Boolean = {
        if (seed) exploded else lifespan <= 0
    }

    def explode() {
        // note, duration, volume
        playNote(15, 30, 127)
        exploded = true
    }

    def step() {
        velocity += acceleration
        location += velocity
        if (!seed) {
            lifespan -= 5
            velocity *= 0.95
        }
        acceleration *= 0
        checkExplode()
    }

    def view() {
        if (isDead) {
            pic.erase()
        }
        else if (pic.isDrawn) {
            val clr = cm.hsla(hu, 1, 0.5, lifespan / 255)
            pic.setPenColor(clr)
            pic.setFillColor(clr)
            if (seed) {
                pic.setPenThickness(3)
            }
            else {
                pic.setPenThickness(2)
            }
            pic.setPosition(location.x, location.y)
        }
        else {
            pic.draw()
        }
    }
}

class Firework() {
    private val hu = random(360)
    private val firework = new Particle(random(cwidth), 0, hu, true)
    private val particles = ArrayBuffer.empty[Particle]

    def done: Boolean = {
        if (firework.isDead & particles.isEmpty) true else false
    }

    def step() {
        particles.filterInPlace(p => !p.isDead)

        if (!firework.isDead) {
            firework.applyForce(gravity)
            firework.step()

            if (firework.shouldExplode) {
                repeat(100) {
                    particles.append(
                        new Particle(firework.location.x, firework.location.y, hu, false)
                    )
                }
                firework.explode()
            }
        }

        repeatFor(particles) { p =>
            p.applyForce(gravity)
            p.step()
        }
    }

    def view() {
        firework.view()
        repeatFor(particles) { p =>
            p.view()
        }
    }

    def dead: Boolean = {
        if (particles.isEmpty) true else false
    }
}

val fireworks = ArrayBuffer.empty[Firework]

def updateState() {
    fireworks.filterInPlace(f => !f.done)
    if (randomDouble(1) < 0.08) {
        fireworks.append(new Firework())
    }
    repeatFor(fireworks) { f =>
        f.step()
    }
}

def viewState() {
    repeatFor(fireworks) { f =>
        f.view()
    }
}

animate {
    updateState()
    viewState()
}

// Inspired  by
// https://github.com/CodingTrain/Coding-Challenges/tree/main/027_FireWorks/Processing/CC_027_FireWorks_2D

// For more details, check out:
// https://github.com/litan/kojo-examples/tree/main/fireworks
