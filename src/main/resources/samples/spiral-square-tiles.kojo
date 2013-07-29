// Contributed by Mike Brown
// The plane can be tiled with a grid of squares. The only other regular 
// polygons that can do so are hexagons and equilateral triangles
// (and the latter only work if half of them are oriented "upside-down").
// 
// This script shows how the turtle can cover the plane with square tiles
// by winding around an initial square in a continuous spiral. A grid of
// squares would be boring, so I've drawn a simple equilateral triangle
// pattern inside each tile.
//  
// Why not design a more interesting pattern? Make your own pattern
// a similar size to the square, and remember when drawing a tile that
// the turtle starts and ends in the bottom-left corner, facing north!
// (You can be more creative if you don't draw the square outlines.)


clear()

// Length of the sides of the squares we're tiling
val tileSide: Double = 50 

// How many times should the spiral loop around the initial square?
val loops = 3 

// How fast do you want the turtle to move? 1000 means normal speed,
// 500 means double-speed, 10 means hundred times faster than normal
// and use 0 if you want the turtle to draw instantly
val speed = 400

// In your own tile pattern, remember to start and end in the bottom-left,
// facing north, and that the tile's dimensions are tileSide x tileSide.

def drawTile {
    penDown //only when drawing the tile patterns should the turtle pen be down
    
    // draw outline of square
    setPenColor(red)
    repeat(4){
        forward(tileSide)
        right()
    }
    // draw equilateral triangle pattern inside tile
    setPenColor(blue)
    repeat(4){
        right(60)
        forward(tileSide)
        left(120)
        forward(tileSide)
        right(150)
    }
    penUp //don't leave a line when moving to the next square
}


// Each time the spiral winds around, it draws four sides ("arms") around 
// the existing tiles. To draw an arm we need to know how many squares long
// it should be.  We will draw each tile, starting and finishing in the 
// bottom-left corner of its square, and move into position for the next tile.
// Finally the turtle turns to face the direction of the next arm.
//
// To draw each tile consistently we must start and end facing north. So it's
// helpful to know the orientation of each arm, measured by the angle the turtle
// has to turn through to face north.

def drawArm(tiles: Int, orientation: Double) {
    repeat(tiles) {
        left(orientation) // now facing north
        drawTile //starts and finishes in bottom-left of tile's square
        right(orientation) //now facing in direction of arm again
        forward(tileSide) //moves to bottom-left of next square
        }
    right(90) // arms wrap around clockwise, so turn right to start next arm
}

// We wind each loop of the spiral clockwise just by drawing its four arms.
// Arms have different lengths, and orientations change by 90 degrees.
// We use the the number of squares in the shortest (first) arm to specify
// how large to draw the loop.
def windSpiral(shortArm: Int) {
    //initial position is ready to start tile above what will be bottom-left
    //head north along left side (unusually short as bottom-left is in 4th arm)
    //finish in position to start the top-left tile
    drawArm(shortArm, 0) 
    //draw top-left tile, head east along top side
    //finish in position to start top-right tile
    drawArm(shortArm + 1, 90)
    //draw top-right tile, head south along right side
    //finish in position to start bottom-right tile
    drawArm(shortArm + 1, 180)
    //draw bottom-right tile, head west along bottom side
    //draw all bottom row tiles, including the bottom-left (so unusually long)
    //finish in position to the left of the bottom-left tile, facing north, so
    //in correct position and orientation for the northward arm of next loop
    drawArm(shortArm + 2, 270)
}

//with all the definitions complete, let's start tiling!

setAnimationDelay(speed) // speed up turtle
penUp() // only when drawing the tiles should the pen be down


drawTile // draw the initial tile

//move to bottom-left of square to the left of the initial one, face north, so 
//in correct position and orientation for the northward arm of the 1st loop
left()
forward(tileSide)
right()

// Now wind the spiral around the initial square!
// with each winding, the size of the short arm increases by 2
for (i <- 1 to 2*loops by 2) { //as loops have shortest side 1, 3, 5, 7, etc
    windSpiral(i)
}