// Contributed by Mike Brown
// The plane can be tiled with a "honeycomb" of hexagons. The only other
// regular polygons that can do so are squares and equilateral triangles
// (and the latter only work if half of them are oriented "upside-down").
// 
// This script shows how the turtle can cover the plane with hexagonal tiles
// by winding around an initial hexagon in a continuous spiral. Two different
// but interlocking tile designs are alternated on successive loops of the
// spiral, and the colors used are gradually altered.
//  
// Why not try redesigning the tyle types, or create a new one and try adding
// it to the pattern? (There are some hints in the comments.) Make your design
// a similar size to the hexagon, and remember when drawing a tile that
// the turtle starts and ends at the bottom-left vertex, facing north!

clear()
import scala.math._ //gives access to square root and trig functions

// Length of the sides of the hexagons we're tiling
val tileSide: Double = 40 

// How many times should the spiral loop around the initial hexagon?
val loops = 4

// How fast do you want the turtle to move? 1000 means normal speed,
// 500 means double-speed, 10 means hundred times faster than normal
// and use 0 if you want the turtle to draw instantly
val speed = 100

// Initial ink colors and tyle type to use
// These will be changed later so use var not val
var firstColor = color(235, 0, 20)
var secondColor = color(20, 0, 235)
var tyleType = 1

// In your own tile pattern, remember to start and end in the bottom-left,
// facing north, and all sides of the hexagon are tileSide long.
// Here two types of tile are defined:

def drawTileType1{    
    //draw equilateral triangles
    setPenColor(firstColor)
    penDown
    left(30) //facing direction of bottom-left side
    repeat(6){
        //for each side of the hexagon, draw an equilateral triangle
        repeat(3){
            forward(tileSide)
            right(120)
        }
        //join midpoints of the two sides of the triangle other than the base
        //this creates a smaller equilateral triangle in the middle of the tile
        right(60)
        repeat(3){
            forward(tileSide/2)
            left(60)
        }
        right(180)
    }
    penUp
    right(30) //facing north again
    penUp     
       
    // draw pentagon pattern
    setPenColor(secondColor)
    penDown
    left(30) //facing direction of bottom-left side
    repeat(6){
        repeat(5){ //draw a pentagon using this side of the hexagon as a base
            forward(tileSide)
            right(72)
        }
        forward(tileSide) //move on to next side of hexagon
        right(60)
    }
    penUp
    right(30) //face north again
    
}

def drawTileType2{
    // Draw the circle inscribing the hexagon.
    // Centered at at center of tile, radius tileSide*sqrt(3)/2
    // Must first move to be tangent to circle, with center on the left.
    // (Another way to visualize this: the turtle must be lie on the end of
    // a radius of the circle at right angles to the turtle's heading, and
    // the turtle's heading must move it clockwise around the circle.)
    right()
    forward(tileSide/2) //to be tangent to circle, with center on left
    setPenColor(secondColor)
    penDown
    circle(tileSide*sqrt(3.0)/2)
    penUp
    back(tileSide/2)
    left() //now back in bottom-left vertex facing north
     
    // draw intersecting square pattern inside tile
    setPenColor(firstColor)
    penDown
    repeat(6){   
        forward(tileSide)
        right()
        forward(tileSide)
        right()
        forward(tileSide)
        right(120)
    }
    penUp
    
    // Draw circle centered at center of tile, radius tileSide/2
    // this is the circumcircle of the the smaller hexagon formed by
    // the intersecting square pattern. Draw it last as otherwise covered over!
    right(30) //oriented towards center of the hexagon
    forward(tileSide/2) //gets half way to the center
    right(90)//to be tangent to circle with center on the left
    setPenColor(secondColor)
    penDown
    circle(tileSide/2)
    penUp
    left(90)
    back(tileSide/2)
    left(30) //now back in bottom-left vertex facing north    
}

// drawTile(i) should draw a tile with design i
// We can use matching to make sure the right tile drawing is called.
// If you add a 3rd or 4th design what will you have to change here?
def drawTile(tyleType: Int) = tyleType match {
    case 1 => drawTileType1
    case 2 => drawTileType2
}
    
// Each time the spiral winds around, it draws six sides ("arms") around 
// the existing tiles. To draw an arm we need to know how many tiles long
// it should be.  We will draw each tile, starting and finishing in the 
// bottom-left vertex of its hexagon, and move into position for the next tile.
// Finally the turtle turns to face the direction of the next arm.
//
// To draw each tile consistently we must start and end facing north. So it's
// helpful to know the orientation of each arm, measured by the angle the turtle
// has to turn through to face north.
// 
// Arms in different windings will use a different tyle type.

def drawArm(tiles: Int, orientation: Double, tyleType: Integer) {
    repeat(tiles) {
        left(orientation) // now facing north
        drawTile(tyleType) //starts and finishes in bottom-left of tile's hexagon
        right(orientation) //now facing in direction of arm again
        // Trigonometry tells us the turtle should move in the arm's direction
        // in a straight line of distance square root 3 times the tile side.
        // So we could use:  forward(tileSide * 1.73205081)
        // But that path doesn't follow any of the sides of the hexagons in
        // our grid, so to show the grid more clearly let's get to the
        // bottom-left vertex of the next tile by traversing sides only.
        // For arms at a bearing of 0, 120 or 240 degrees the side most closely
        // matching the arm's direction is 30 degrees to the left, for the other
        // arms it is 30 degrees to the right. We can use division remainder:
        // orientation%120-30 is -30 or 30 for the two groups of arms.
        right(orientation%120-30) //align with nearest side
        forward(tileSide) //move to next vertex
        left(2*(orientation%120-30)) //align with next side
        forward(tileSide) //move to destination vertex
        right(orientation%120-30) //realign with arm direction
        
        }
    right(60) // arms wrap around clockwise, so turn right to start next arm
}

// We wind each loop of the spiral clockwise by selecting a tyle type to use
// and drawing the loop's six arms Arms have different lengths, and orientations
// change by 60 degrees.  We use the the number of tiles in the shortest (first)
// arm to specify how large to draw the loop.
def windSpiral(shortArm: Int) {
    // The tyle design used can be changed between loops. The initial tile was
    // type 1. The first loop around it should be type 2. Hence, even loops
    // should be type 2 and odd loops type 1.
    // 
    // A remainder function can tell us which type's turn it is! shortArm takes
    // values 0, 1, 2, 3... on successive loops so shortArm%2 is 0 on first and
    // other odd loops, 1 on even loops. We could use the if expession:
    // tyleType = if (shortArm%2==0) 2 else 1
    // 
    // But in case you want to add more tile designs, let's use match instead.
    // If you add a third design, you'll have to use divisor 3 and will have
    // three possible remainders (0, 1 and 2) to deal with.
    
    tyleType = (shortArm%2) match {
      case 0 => 2
      case 1 => 1
    }
    
    // Initially the turtle is ready to draw the tile immediately above the
    // lowest tile of the left side (which is in the 6th arm, so the 1st arm is
    // unusually short). Continue north along the left side, finishing in
    // position to start the highest tile of the left side. (On the very first
    // loop this is just the initial position, so the arm has length 0).
    drawArm(shortArm, 0, tyleType)
    // draw top tile of left side (bottom-left tile of upper-left side)
    // head on bearing of 060 degrees along upper-left side, finish in position
    // to start top tile
    drawArm(shortArm + 1, 60, tyleType)
    // draw top tile then head on bearing of 120 degrees along upper-right side.
    // finish in position to start bottom-right tile of upper-right side.
    drawArm(shortArm + 1, 120, tyleType) 
    // draw top tile of right side (bottom-right of upper-right side)
    // head south along right side
    // finish in position to start bottom tile of right side
    drawArm(shortArm + 1, 180, tyleType)
    //draw bottom tile of right side (top-right tile of lower-right side)
    //head on bearing of 240 degrees along lower-right side
    //finish in position to start the bottom tile
    drawArm(shortArm + 1, 240, tyleType)
    // Draw bottom tile and head on bearing of 300 degrees along bottom-left 
    // side, drawing all tiles on this side including the upper-left (the
    // bottom tile of left side) - hence this side is unusually long.
    // Finish in the bottom-left vertex of the tile to the upper-left of the
    // final tile drawn, facing north - this is the starting position for the
    // first arm of the next loop of the spiral.
    drawArm(shortArm + 2, 300, tyleType)
}

//with all the definitions complete, let's start tiling!

setAnimationDelay(speed) // speed up turtle
penUp() // only when drawing the tiles should the pen be down

// draw the initial tile, finish in bottom-left vertex facing north
drawTile(tyleType)

//move to what will become bottom-left vertex of hexagon to the upper-left of
//the initial tile, and face north: in correct position and orientation for
//the first (northbound) arm of the spiral. Note that in the first winding
//around the initial hexagon, the northbound arm has length zero, so this tile
//actually gets drawn as the first tile of the second arm!
left(30)
forward(tileSide)
left(60)
forward(tileSide)
right()

// Now wind the spiral around the initial hexagon!
// with each winding, the size of the short arm increases by 1
for (i <- 0 to loops - 1) { //as loops have shortest arms 0, 1, 2, etc
    // We can gradually alter the colors each loop.
    // The colors will be (127, 128, 0) and (0, 128, 127) at final loop.
    if (loops != 0) { //prevents division by 0 errors
        firstColor = color(235 - 128/loops*(i+1), 128/loops*(i+1), 20)
        secondColor = color(20, 128/loops*(i+1), 255 - 128/loops*(i+1))
    }
    windSpiral(i)
}
