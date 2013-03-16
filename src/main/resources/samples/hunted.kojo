// sample game - with characters made out of tangram pieces.
toggleFullScreenCanvas()
val len = 4
val d = math.sqrt(2 * len * len)
val d2 = d / 2
val d4 = d / 4

def p1 = Picture {
    forward(len)
    right(135)
    forward(d2)
    right()
    forward(d2)
}

def p2 = p1

def p3 = Picture {
    right()
    forward(len / 2)
    left(135)
    forward(d4)
    left()
    forward(d4)
}

def p4 = p3

def p6 = Picture {
    repeat(4) {
        forward(d4)
        right()
    }
}

def p5 = Picture {
    right()
    forward(len / 2)
    left()
    forward(len / 2)
    left(135)
    forward(d2)
}

def p7 = Picture {
    right()
    forward(len / 2)
    left(45)
    forward(d4)
    left(135)
    forward(len / 2)
    left(45)
    forward(d4)
}

val tgreen = Color(0, 255, 0, 150)

def guy = GPics(
    rot(-120) -> p3,
    rot(150) * trans(0, -3.5) -> p1,
    flipY * rot(120) * trans(1.5, 0) -> p7,
    rot(150) * trans(-1, -4.5) -> p5,
    rot(-165) * trans(-4.47, -3.9) -> p4,
    rot(150) * trans(1, -6.5) -> p2,
    trans(-1.75, 5.4) * rotp(30, d4, 0) -> p6
)

def border(size: Double) = Picture {
    forward(size)
}

val lostMsg = trans(-20, 0) -> Picture {
    write("You Lost!")
}

val wonMsg = trans(-20, 0) -> Picture {
    write("You Won!")
}

clearWithUL(Cm)
val cb = canvasBounds
val xmax = cb.x.abs
val ymax = cb.y.abs
val goodguy = fillColor(yellow) * trans(xmax / 3, 2) * scale(0.3) -> guy
val badguy = fillColor(black) * scale(0.3) -> guy
val badguy2 = fillColor(black) * trans(-xmax / 2, 0) * scale(0.3) -> guy
val badguy3 = fillColor(black) * trans(2 * xmax / 3, 0) * scale(0.3) -> guy
val badguy4 = fillColor(black) * trans(-xmax / 2, ymax / 2) * scale(0.3) -> guy
val badguy5 = fillColor(black) * trans(2 * xmax / 3, ymax / 2) * scale(0.3) -> guy

playMp3Loop("/media/music-loops/Cave.mp3")
invisible()
draw(goodguy, badguy, badguy2, badguy3, badguy4, badguy5)
drawAndHide(lostMsg, wonMsg)
drawStage(Color(150, 150, 255))

val bf = 2
val sf = 1.5
val speed = 0.4

val vec = Vector2D(0, speed)
val vec2 = Vector2D(speed, 0)
val vec3 = Vector2D(-speed, 0)
val vec4 = vec2
val vec5 = vec3

var velocities = Map(
    badguy -> vec,
    badguy2 -> vec2,
    badguy3 -> vec3,
    badguy4 -> vec4,
    badguy5 -> vec5
)

def badBehavior(self: Picture) {
    var newv = velocities(self).rotate(randomDouble(10) - 5)
    self.transv(newv)
    if (self.collidesWith(stage)) {
        newv = bounceVecOffStage(newv, self)
        self.transv(newv)
    }
    velocities += self -> newv
}

badguy.react(badBehavior)
badguy2.react(badBehavior)
badguy3.react(badBehavior)
badguy4.react(badBehavior)
badguy5.react(badBehavior)

goodguy.react { self =>
    if (isKeyPressed(Kc.VK_RIGHT)) {
        self.translate(speed * sf, 0)
    }
    if (isKeyPressed(Kc.VK_LEFT)) {
        self.translate(-speed * sf, 0)
    }
    if (isKeyPressed(Kc.VK_UP)) {
        self.translate(0, speed * sf)
    }
    if (isKeyPressed(Kc.VK_DOWN)) {
        self.translate(0, -speed * sf)
    }
}

def time = System.currentTimeMillis
val startTime = time

val others = List(badguy, badguy2, badguy3, badguy4, badguy5)

goodguy.react { self =>
    if (self.collision(others).isDefined) {
        stopAnimation()
        self.setFillColor(brown)
        lostMsg.setPosition(0, 0)
        lostMsg.visible()
    }

    // an example of playing some game event music
    if (goodguy.collidesWith(stage)) {
        if (!isMp3Playing) {
            playMp3("/media/music-loops/DrumBeats.mp3")
        }
    }
    else {
        stopMp3()
    }

    if (time - startTime > 60 * 1000) {
        stopAnimation()
        wonMsg.setPosition(0, 0)
        wonMsg.visible()
    }
}

onKeyPress { k =>
    k match {
        case Kc.VK_ESCAPE =>
            stopAnimation()
        case _            =>
    }
}
