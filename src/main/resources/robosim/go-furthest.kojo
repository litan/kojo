// #include /robosim/robot.kojo

cleari()

// turn on grid if you want to tweak room placement
//showAxes()
//showGrid()

// Zoom out with a zoom factor of less than 1 if the simulation area does not fit in your canvas
zoom(1.0)

drawStage(ColorMaker.khaki)
def room(w: Double, h: Double) = {
    val c = ColorMaker.darkOliveGreen
    fillColor(c) * penColor(c) -> Picture.rectangle(w, h)
}
val roomWest = room(50, 500)
val roomNorth = room(800, 50)
val roomEast = room(50, 500)
val roomSouth = room(800, 50)
val room1 = room(50, 150)
val room1m = room(50, 150)
val room2 = room(200, 100)
val room3 = room(100, 200)
val room4 = room(200, 50)
val room4m = room(200, 50)
val room5 = room(50, 400)
val room6 = room(100, 150)
val room6m = room(100, 150)
val room7 = room(250, 100)

roomWest.setPosition(-450, -250)
roomNorth.setPosition(-400, 250)
roomEast.setPosition(400, -250)
roomSouth.setPosition(-400, -300)
room1.setPosition(-350, -250)
room1m.setPosition(-350, 100)
room2.setPosition(-400, -50)
room3.setPosition(-150, -100)
room4.setPosition(-250, -200)
room4m.setPosition(-250, 150)
room5.setPosition(50, -200)
room6.setPosition(150, 100)
room6m.setPosition(150, -250)
room7.setPosition(100, -50)

val allRooms = picStack(roomWest, roomNorth, roomEast, roomSouth, room1, room2,
    room3, room4, room1m, room4m, room5, room6, room6m, room7)

val robot = Robot(-400, -240, allRooms)

draw(allRooms)
robot.show()

def loop() {
    val d = robot.distanceToObstacle
    if (d <= 6 || robot.collidesWith(allRooms)) {
        var maxdTime = 0.0
        var maxd = 0.0
        val totalTurnTime = robot.sweepAngle / robot.aVelocity * 1000
        val chunks = 10
        val turnTime = totalTurnTime / chunks
        var goRight = false
        repeatFor(1 to chunks) { n =>
            robot.left(turnTime)
            val d = robot.distanceToObstacle
            if (d > maxd) {
                maxd = d
                maxdTime = n * turnTime
            }
        }

        robot.right(totalTurnTime)

        repeatFor(1 to chunks) { n =>
            robot.right(turnTime)
            val d = robot.distanceToObstacle
            if (d > maxd) {
                goRight = true
                maxd = d
                maxdTime = n * turnTime
            }
        }
        if (goRight) {
            robot.left(totalTurnTime - maxdTime)
        }
        else {
            robot.left(totalTurnTime)
            robot.left(maxdTime)
        }
        val fd = math.min(40, maxd)
        robot.forward(fd / robot.lVelocity * 1000)
    }
    else {
        robot.forward(5 / robot.lVelocity * 1000)
    }
}

repeatWhile(true) {
    loop()
}
