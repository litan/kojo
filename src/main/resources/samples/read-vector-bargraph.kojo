cleari()
clearOutput()
setOutputBackground(black)
setOutputTextColor(gray)
var vec = Vector[Int]()
println("Let's create a vector, and make a Bar Graph out of its elements. Enter data below.")
val n = readInt("How many elements in your vector?")
setOutputTextColor(yellow)
for (i <- 1 to n) {
    val e = readInt(s"Enter element #$i (between 0 and 10)")
    vec = vec :+ e
}
setOutputTextColor(green)
println(s"Input Vector: $vec")
println("See Drawing Canvas for Bar Graph of elements (scaled by 10)")

def bar(n: Int) = PicShape.rect(n*10, 30)
val bars = vec.map { n => bar(n) }
axesOn()
gridOn()
val barGraph = HPics(bars).withGap(5)
draw(barGraph)
activateEditor()
