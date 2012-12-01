// This sample contains many different shapes based on L-Systems.
// To play with the different shapes, tweak the following vals near the 
// bottom of the script:
// * drawing - to select what to draw
// * gens - to select how many generations to run the L-System
// You can also play with the zoom command at the bottom of the script to 
// scale or translate the drawing.

// See http://lalitpant.blogspot.in/2012/05/playing-with-l-systems-in-kojo.html for more information.


case class LSystem(axiom: String, angle: Double, len: Int = 100, sf: Double = 0.6)(rules: PartialFunction[Char, String]) {
    var currVal = axiom
    var currGen = 0
    def evolve() {
        currGen += 1
        currVal = currVal.map { c =>
            if (rules.isDefinedAt(c)) rules(c) else c
        }.mkString.replaceAll("""\|""" , currGen.toString)
    }
    
    def draw() {
        def isDigit(c: Char) = Character.isDigit(c)
        val genNum = new StringBuilder
        def maybeDrawBar() {
            if (genNum.size != 0) {
                val n = genNum.toString.toInt
                genNum.clear()
                forward(len * math.pow(sf, n))
            }
        }
        currVal.foreach { c => 
            if (!isDigit(c)) {
                maybeDrawBar()
            }
            
            c match {
                case 'F' => forward(len)
                case 'f' => forward(len)
                case 'G' => penUp(); forward(len); penDown()
                case '[' => savePosHe()
                case ']' => restorePosHe()
                case '+' => right(angle)
                case '-' => left(angle)
                case n if isDigit(n) => genNum.append(n)
                case _ => 
            }
        }
        maybeDrawBar()
    }
}

val bigh = LSystem("[G]--G", 90, 100, 0.65) {
    case 'G' => "|[+G][-G]"
}

val bentBigh = LSystem("[G]--G", 80, 100, 0.65) {
    case 'G' => "|[+G][-G]"
}

val bush2 = LSystem("G", 20) {
    case 'G' => "|[+G]|[-G]+G"
}

val tree2 = LSystem("G", 8, 100, 0.35) {
    case 'G' => "|[+++++G][-------G]-|[++++G][------G]-|[+++G][-----G]-|G"
}

val carpet = LSystem("F-F-F-F", 90, 1) {
    case 'F'=> "F[F]-F+F[--F]+F-F"
}

val koch_wp4 = LSystem("F", 90, 10) {
    case 'F'=> "F-F+F+F-F"
} 

val sierp_wp6 = LSystem("F", 60, 2) {
    case 'F' => "f+F+f"
    case 'f' => "F-f-F"
} 

val dragon_wp7 = LSystem("FX", 90, 20) {
    case 'X' => "X+YF"
    case 'Y' => "FX-Y"
}  

val fplant_wp8 = LSystem("X", 25, 4) {
    case 'X'=> "F-[[X]+X]+F[+FX]-X"
    case 'F' => "FF"
}

val drawing = fplant_wp8

clear()
invisible()
setAnimationDelay(0)
setPenThickness(1)
setPenColor(Color(20,20,20)) 
val gens = 6
repeat (gens) {
    drawing.evolve()
}
// pre drawing adjustments to make the drawing look right in the canvas
// right(10)
zoom(0.5, 0, 250)
setBackground(Color(255, 170, 29))

drawing.draw()

