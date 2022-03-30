// Moves the turtle to the square on the board specified in square (parameter). 
// Only moves the turtle to squares in row 1, otherwise places it next to it and asks the user to choose a starting point in row 1
def startingPoint(square:String){ 
  var x = 40.0
  var y = 15.0
  if (square.startsWith("A") || square.startsWith("a")){
    x= -162.5
  }
  else if (square.startsWith("B") || square.startsWith("b")){
    x= -162.5+25
  }
  else if (square.startsWith("C") || square.startsWith("c")){
    x= -162.5+25*2
  }
  else if (square.startsWith("D") || square.startsWith("d")){
    x= -162.5+25*3
  }
  else if (square.startsWith("E") || square.startsWith("e")){
    x= -162.5+25*4
  }
  else if (square.startsWith("F") || square.startsWith("f")){
    x= -162.5+25*5
  }
  else if (square.startsWith("G") || square.startsWith("g")){
    x= -162.5+25*6
  }
  else if (square.startsWith("H") || square.startsWith("h")){
    x= -162.5+25*7
  }

  if (square.endsWith("1")){
    y= 12.5
  }
  else if (square.endsWith("2")){
    y= 12.5+25
  }
  else if (square.endsWith("3")){
    y= 12.5+25*2
  }
  else if (square.endsWith("4")){
    y= 12.5+25*3
  }
  else if (square.endsWith("5")){
    y= 12.5+25*4
  }
  else if (square.endsWith("6")){
    y= 12.5+25*5
  }
  else if (square.endsWith("7")){
    y= 12.5+25*6
  }
  else if (square.endsWith("8")){
    y= 12.5+25*7
  }
  penUp()
  setAnimationDelay(0)
  moveTo(x,y)
  setHeading(90)
  
  if(x==40 || y==15){
    x=40
    y=15
    moveTo(x,y)
    penDown()
    setHeading(90)
    write("Choose a correct starting point")
  }
  penDown()
  setAnimationDelay(1000)
}
// prints the board starting at (0,0)
def board(){ 
var size = 25
var filled = 1
def square(side:Int, shouldFill:Int) {
  if(shouldFill%2==0){
      setFillColor(black)
    }
  else{
    setFillColor(noColor)
  }
  
  repeat(4){
    forward(side)
    right
  }
}
def ladder(side:Int, fill:Int) {
  var fill2 = fill
  repeat(8){
    square(side, fill2)
    fill2 = fill2 +1
    forward(side)
    
  }
}

  savePosHe()
  penUp()
  setAnimationDelay(0)
  moveTo(0,0)
  penDown()
  setHeading(90)
  setPenColor(black)
  
  
  repeat(8){
    ladder(size, filled)
    filled = filled +1
    setFillColor(noColor)
    forward(-8*size)
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
def notation(){ 
  savePosHe()
  setPenColor(red)
  setPenFontSize(20)
  setAnimationDelay(0)
  var i = 8
  var jump = 0
  repeat(8){
    penUp()
    moveTo(-190, 200 -25*jump)
    penDown()
    setHeading(90)
    write(i.toString)
    penUp()
    moveTo(29, 200 -25*jump)
    penDown()
    setHeading(90)
    write(i.toString)
    jump = jump + 1
    i = i-1
  }
  var bok = Vector("A", "B", "C", "D", "E", "F", "G", "H" ) 
  i = 0
  jump = 0
  repeat(8){
    penUp()
    moveTo(-167  +25*jump, 225)
    penDown()
    setHeading(90)
    write(bok(i))
    penUp()
    moveTo(-167  +25*jump, 0)
    penDown()
    setHeading(90)
    write(bok(i))
    jump = jump + 1
    i = i+1
  }
  restorePosHe()
  setPenFontSize(5)
  setAnimationDelay(1000)
}

// moves the turle backward step steps
def backward(step: Int = 25){
  forward(-step)
}
def diagonalForwardRight(){
  right(45)
  forward(35)
  left(45)
}
def diagonalForwardLeft(){
  left(45)
  forward(35)
  right(45)
}
def diagonalBackwardLeft(){
  right(45)
  forward(-35)
  left(45)
}
def diagonalBackwardRight(){
  left(45)
  forward(-35)
  right(45)
}

// Swedish commands
def bräde(){
  board()
}

def startpunkt(ruta:String){
  startingPoint(ruta)
}

// notation är samma

def bak(steg: Int =25){
  forward(-steg)
}

def diagonalFramHöger(){
  diagonalForwardRight()
}
 def diagonalFramVänster(){
   diagonalForwardLeft()
 }

 def diagonalBakHöger(){
   diagonalBackwardRight()
 }

 def diagonalBakVänster(){
   diagonalBackwardLeft()
 }
