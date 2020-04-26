// A simple lunar lander game that demonstrates the following:
// Simple game mechanics with gravity, thrust, collisions, etc
// Usage of functions; and classes with fields and methods
// Usage of simple math to center game objects

cleari()
val cb = canvasBounds
drawStage(ColorMaker.hsl(240, 0.20, 0.16))

def xCenteredPosition(picWidth: Double) = {
    cb.x + (cb.width - picWidth) / 2
}

class Lander {
    val bodyWidth = 40; val bodyHeight = 70
    val thrusterWidth = 20; val thrusterHeight = 35
    val body = fillColor(red) -> Picture.rectangle(bodyWidth, bodyHeight)
    body.setPosition(xCenteredPosition(bodyWidth), cb.y + cb.height - bodyHeight - 10)
    val thruster = fillColor(orange) -> Picture.rectangle(thrusterWidth, thrusterHeight)
    setThrusterPosition()

    val gravity = Vector2D(0, -0.1)
    var velocity = Vector2D(0, 0)
    val zeroThrust = Vector2D(0, 0)
    val upThrust = new Vector2D(0, 1)
    var thrust = zeroThrust

    def setThrusterPosition() {
        thruster.setPosition(
            body.position.x + (bodyWidth - thrusterWidth) / 2,
            body.position.y - (thrusterHeight - 15)
        )
    }

    def draw() {
        body.draw()
        thruster.draw()
        thruster.invisible()
    }

    def step() {
        if (isKeyPressed(Kc.VK_UP)) {
            inThrust()
        }
        else {
            noThrust()
        }
        velocity = velocity + gravity
        velocity = velocity + thrust

        body.translate(velocity)
        setThrusterPosition()

        if (body.collidesWith(stageBorder)) {
            velocity = bouncePicVectorOffStage(body, velocity)
        }
    }

    def inThrust() {
        thrust = upThrust
        thruster.visible()
    }

    def noThrust() {
        thrust = zeroThrust
        thruster.invisible()
    }
}

class Moon {
    val pic = Picture {
        setPenColor(cm.lightBlue)
        setFillColor(cm.darkGray)
        right(45)
        right(90, 500)
    }

    // width of the moon is around 710 pixels
    pic.setPosition(xCenteredPosition(710), cb.y)

    def draw() {
        pic.draw()
    }

    def check(l: Lander) {
        if (l.body.collidesWith(pic)) {
            if (l.velocity.y.abs > 3) {
                drawCenteredMessage("You Lose", red, 39)
            }
            else {
                drawCenteredMessage("You Win", green, 30)
            }
            stopAnimation()
        }
    }

}

val l = new Lander()
l.draw()

val m = new Moon()
m.draw()

animate {
    l.step()
    m.check(l)
}
activateCanvas()