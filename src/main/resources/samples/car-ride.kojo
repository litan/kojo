// Use the four arrow keys to avoid the blue cars
// You gain energy every second, and lose energy for every collision
// You lose if your energy drops below zero, or you hit the edges of the screen
// You win if you stay alive for a minute
switchToDefault2Perspective()
cleari()
drawStage(black)
// setRefreshRate(50)

val cb = canvasBounds
val carHeight = 100
val markerHeight = 80
// The collision polygon for the (very similarly sized) car images car1.png and car2.png
val carE = trans(2, 14) -> Picture {
    repeat(2) {
        forward(70); right(45); forward(20); right(45)
        forward(18); right(45); forward(20); right(45)
    }
}
def car(img: String) = Picture.image(img, carE)

val cars = collection.mutable.Map.empty[Picture, Vector2D]
val carSpeed = 3
val pResponse = 3
var pVel = Vector2D(0, 0)
var disabledTime = 0L

val bplayer = newMp3Player
val cplayer = newMp3Player

def createCar() {
    val c = trans(player.position.x + randomNormalDouble * cb.width / 10, cb.y + cb.height) ->
        car("/media/car-ride/car2.png")
    draw(c)
    cars += c -> Vector2D(0, -carSpeed)
}
val markers = collection.mutable.Set.empty[Picture]
def createMarker() {
    val mwidth = 20
    val m = fillColor(white) * penColor(white) *
        trans(cb.x + cb.width / 2 - mwidth / 2, cb.y + cb.height) -> Picture.rect(markerHeight, mwidth)
    draw(m)
    markers += m
}

val player = car("/media/car-ride/car1.png")
draw(player)
drawAndHide(carE)

timer(800) {
    createMarker()
    createCar()
}

animate {
    player.moveToFront()
    val enabled = epochTimeMillis - disabledTime > 300
    if (enabled) {
        if (isKeyPressed(Kc.VK_LEFT)) {
            pVel = Vector2D(-pResponse, 0)
            player.translate(pVel)
        }
        if (isKeyPressed(Kc.VK_RIGHT)) {
            pVel = Vector2D(pResponse, 0)
            player.translate(pVel)
        }
        if (isKeyPressed(Kc.VK_UP)) {
            pVel = Vector2D(0, pResponse)
            player.translate(pVel)
            if (!isMp3Playing) {
                playMp3Sound("/media/car-ride/car-accel.mp3")
            }
        }
        else {
            stopMp3()
        }
        if (isKeyPressed(Kc.VK_DOWN)) {
            pVel = Vector2D(0, -pResponse)
            player.translate(pVel)
            if (!bplayer.isMp3Playing) {
                bplayer.playMp3Sound("/media/car-ride/car-brake.mp3")
            }
        }
        else {
            bplayer.stopMp3()
        }
    }
    else {
        player.translate(pVel)
    }

    if (player.collidesWith(stageLeft) || player.collidesWith(stageRight)) {
        cplayer.playMp3Sound("/media/car-ride/car-crash.mp3")
        player.setOpacity(0.5)
        drawCenteredMessage("You Crashed!", red, 30)
        stopAnimation()
    }
    else if (player.collidesWith(stageTop)) {
        pVel = Vector2D(0, -pResponse)
        player.translate(pVel * 2)
        disabledTime = epochTimeMillis
    }
    else if (player.collidesWith(stageBot)) {
        pVel = Vector2D(0, pResponse)
        player.translate(pVel * 2)
        disabledTime = epochTimeMillis
    }

    cars.foreach { cv =>
        val (c, vel) = cv
        c.moveToFront()
        if (player.collidesWith(c)) {
            cplayer.playMp3Sound("/media/car-ride/car-crash.mp3")
            pVel = bouncePicVectorOffPic(player, pVel - vel, c) / 2
            player.translate(pVel * 3)
            c.translate(-pVel * 3)
            disabledTime = epochTimeMillis
            updateEnergyCrash()
        }
        else {
            val newVel = Vector2D(vel.x + randomDouble(1) / 2 - 0.25, vel.y)
            cars += c -> newVel
            c.translate(newVel)
        }
        if (c.position.y + carHeight < cb.y) {
            c.erase()
            cars -= c
        }
    }
    markers.foreach { m =>
        m.translate(0, -carSpeed * 2)
        if (m.position.y + markerHeight < cb.y) {
            m.erase()
            markers -= m
        }
    }
}

var energyLevel = 0
def energyText = s"Energy: $energyLevel"
val energyLabel = Picture.textu(energyText, 20, ColorMaker.aquamarine)
energyLabel.translate(cb.x + 10, cb.y + cb.height - 10)
def updateEnergyTick() {
    energyLevel += 2
    energyLabel.update(energyText)
}
def updateEnergyCrash() {
    energyLevel -= 10
    energyLabel.update(energyText)
    if (energyLevel < 0) {
        drawCenteredMessage("You're out of energy! You Lose", red, 30)
        stopAnimation()
    }
}

def manageGameScore() {
    var gameTime = 0
    val timeLabel = Picture.textu(gameTime, 20, ColorMaker.azure)
    timeLabel.translate(cb.x + 10, cb.y + 50)
    draw(timeLabel)
    draw(energyLabel)
    timeLabel.forwardInputTo(stageArea)

    timer(1000) {
        gameTime += 1
        timeLabel.update(gameTime)
        updateEnergyTick()

        if (gameTime == 60) {
            drawCenteredMessage("Time up! You Win", green, 30)
            stopAnimation()
        }
    }
}

manageGameScore()
playMp3Loop("/media/car-ride/car-move.mp3")
activateCanvas()

// Car images, via google images, from http://motor-kid.com/race-cars-top-view.html
// and www.carinfopic.com
// Car sounds from http://soundbible.com
