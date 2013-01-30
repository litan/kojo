toggleFullScreenOutput()
clearOutput()
setOutputBackground(black)
setOutputTextColor(gray)
println("Let's create a vector. Enter data below.")
var vec = Vector[Int]()
val n = readInt("How many elements in your vector?")
setOutputTextColor(yellow)
for (i <- 1 to n) {
    val e = readInt(s"Enter element $i")
    vec = vec :+ e
}
setOutputTextColor(green)
println(s"Input Vector: $vec")
println(f"The mean of the elements of the Vector is: ${vec.sum.toDouble / vec.size}%.2f")
