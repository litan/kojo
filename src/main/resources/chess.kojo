// Moves the turtle to the square on the board specified in square (parameter).
// Only moves the turtle to squares in row 1, otherwise places it next to it and asks the user to choose a starting point in row 1

def startingPoint(square: String) {
    var x = 0.0
    var y = 12.5
    if (square == "A1") {
        x = -162.5
    }
    else if (square == "B1") {
        x = -162.5 + 25
    }
    else if (square == "C1") {
        x = -162.5 + 25 * 2
    }
    else if (square == "D1") {
        x = -162.5 + 25 * 3
    }
    else if (square == "E1") {
        x = -162.5 + 25 * 4
    }
    else if (square == "F1") {
        x = -162.5 + 25 * 5
    }
    else if (square == "G1") {
        x = -162.5 + 25 * 6
    }
    else if (square == "H1") {
        x = -162.5 + 25 * 7
    }
    else {
        x = 40
        y = 15

    }
    penUp()
    setAnimationDelay(0)
    moveTo(x, y)
    setHeading(90)
    penDown()
    if (x == 40) {
        write("Choose at starting point in row 1")
    }
    setAnimationDelay(1000)
}
// prints the board starting at (0,0)
def board() {
    var size = 25
    var filled = 0
    def square(side: Int, shouldFill: Int) {
        if (shouldFill % 2 == 0) {
            setFillColor(black)
        }
        else {
            setFillColor(noColor)
        }

        repeat(4) {
            forward(side)
            right
        }
    }
    def ladder(side: Int, fill: Int) {
        var fill2 = fill
        repeat(8) {
            square(side, fill2)
            fill2 = fill2 + 1
            forward(side)

        }
    }

    savePosHe()
    penUp()
    moveTo(0, 0)
    penDown()
    setHeading(90)
    setPenColor(black)
    setAnimationDelay(0)

    repeat(8) {
        ladder(size, filled)
        filled = filled + 1
        setFillColor(noColor)
        forward(-8 * size)
        left
        penUp()
        forward(size)
        penDown()
        right
    }
    setAnimationDelay(1000)
    setFillColor(noColor)
    setPenColor(red)
    restorePosHe()

}
// prints out the notation for the board
def notation() {
    savePosHe()
    setPenColor(red)
    setPenFontSize(20)
    setAnimationDelay(0)
    var i = 8
    var jump = 0
    repeat(8) {
        penUp()
        moveTo(-190, 200 - 25 * jump)
        penDown()
        setHeading(90)
        write(i.toString)
        penUp()
        moveTo(29, 200 - 25 * jump)
        penDown()
        setHeading(90)
        write(i.toString)
        jump = jump + 1
        i = i - 1
    }
    var bok = Vector("A", "B", "C", "D", "E", "F", "G", "H")
    i = 0
    jump = 0
    repeat(8) {
        penUp()
        moveTo(-167 + 25 * jump, 225)
        penDown()
        setHeading(90)
        write(bok(i))
        penUp()
        moveTo(-167 + 25 * jump, 0)
        penDown()
        setHeading(90)
        write(bok(i))
        jump = jump + 1
        i = i + 1
    }
    restorePosHe()
    setPenFontSize(5)
    setAnimationDelay(1000)
}
def backward() {
    forward(-25)
}
def diagonalNortheast() {
    right(45)
    forward(35)
    left(45)
}
def diagonalNorthwest() {
    left(45)
    forward(35)
    right(45)
}
def diagonalSoutheast() {
    right(135)
    forward(35)
    left(135)
}
def diagonalSouthwest() {
    left(135)
    forward(35)
    right(135)
}
