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
    var d = robot.distanceToObstacle

    // after getting close to an obstacle, the robot stops and finds the nearest path (to left or right)
    // that looks clear before moving forward again.

    if (d < 6) {
        val goRight = randomBoolean
        repeatUntil(d > 20) {
            if (goRight) {
                robot.right(100)
            }
            else {
                robot.left(100)
            }
            d = robot.distanceToObstacle
        }
    }
    else {
        robot.forward(50)
    }
}
