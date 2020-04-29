// #include /robosim/robot.kojo
// #include /robosim/environment1.kojo

// Included code can be seen here:
// https://github.com/litan/kojo/tree/master/src/main/resources/robosim

cleari()
drawStage(ColorMaker.khaki)
draw(allRooms) // allRooms comes from the included environment

// turn on grid if you want to tweak room placement
//showAxes()
//showGrid()

// Zoom out with a zoom factor of less than 1 if the simulation area does not fit in your canvas
// zoom(1.0)

val robot = Robot(-400, -240, allRooms)
robot.show()

repeatWhile(true) {
    loop()
}

def loop() {
    val d = robot.distanceToObstacle

    // after getting close to an obstacle, the robot stops and sweeps 90 degrees to left and right,
    // determines the clearest path, and then moves forward along this path.

    val sweepAngle = 90
    if (d <= 6 || robot.collidesWith(allRooms)) {
        var maxdTime = 0.0
        var maxd = 0.0
        val totalTurnTime = sweepAngle / robot.aVelocity * 1000
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
