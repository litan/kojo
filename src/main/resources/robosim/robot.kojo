case class Robot(x0: Int, y: Int, roomWalls: Picture) {
    val rc = ColorMaker.darkMagenta
    val width = 20
    val height = 30
    val x = x0 + (50 - width) / 2
    val distanceSensor = trans(width / 2, height) * fillColor(rc.lighten(0.5)) *
        penColor(rc) -> Picture.circle(5)
    val soundWave = trans(width / 2, height) * fillColor(ColorMaker.lightBlue) *
        penColor(ColorMaker.lightSlateGray) * penWidth(1) -> Picture.circle(width / 2)
    val robotBody = fillColor(rc) * penColor(rc) -> Picture.rectangle(width, height)
    val lVelocity = 200.0 // pixels per sec
    val aVelocity = 180.0 // degrees per sec
    //    distanceSensor.axesOn()

    robotBody.setPosition(x, y)
    distanceSensor.setPosition(x, y)
    soundWave.setPosition(x, y)

    def distanceToObstacle = {
        soundWave.visible()
        var d = 0
        while (!soundWave.collidesWith(roomWalls)) {
            soundWave.translate(0, 3)
            d += 3
        }
        soundWave.translate(0, -d)
        soundWave.invisible()
        d
    }

    def show() {
        draw(robotBody, distanceSensor, soundWave)
        soundWave.invisible()
    }

    def forward(ms: Double) {
        // go forward for ms millis
        val d = lVelocity * ms / 1000
        val chunks = 10
        repeat(chunks) {
            robotBody.translate(0, d / chunks)
            distanceSensor.translate(0, d / chunks)
            soundWave.translate(0, d / chunks)
            pause(ms / chunks / 1000)
        }
    }

    def left(ms: Double) {
        val angle = aVelocity * ms / 1000
        val chunks = 30
        repeat(chunks) {
            robotBody.rotateAboutPoint(angle / chunks, width / 2, height / 2)
            distanceSensor.rotateAboutPoint(angle / chunks, 0, -height / 2)
            soundWave.rotateAboutPoint(angle / chunks, 0, -height / 2)
            pause(ms / chunks / 1000)
        }
    }

    def right(ms: Double) {
        val angle = aVelocity * ms / 1000
        val chunks = 30
        repeat(chunks) {
            robotBody.rotateAboutPoint(-angle / chunks, width / 2, height / 2)
            distanceSensor.rotateAboutPoint(-angle / chunks, 0, -height / 2)
            soundWave.rotateAboutPoint(-angle / chunks, 0, -height / 2)
            pause(ms / chunks / 1000)
        }
    }

    def collidesWith(other: Picture) = {
        robotBody.collidesWith(other) || distanceSensor.collidesWith(other)
    }
}
