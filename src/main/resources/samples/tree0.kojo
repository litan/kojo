def tree(distance: Double) {
    if (distance > 4) {
        setPenThickness(distance/7)
        setPenColor(color(distance.toInt, math.abs(255-distance*3).toInt, 125))
        forward(distance)
        right(25)
        tree(distance*0.8-2)
        left(45)
        tree(distance-10)
        right(20)
        back(distance)
    }
}

clear()
invisible()
setAnimationDelay(10)
penUp()
back(200)
penDown()
tree(90)