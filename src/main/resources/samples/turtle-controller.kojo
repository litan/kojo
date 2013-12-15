// Run this program to get basic control over the turtle via some buttons
// Useful for smaller kids

// Change these value to tweak the behavior of the buttons
val fdStep = 50
val fdStep2 = 10
val rtStep = 90
val rtStep2 =10
val bgColor = white
val sBgColor = "white"
// End tweak region

clear()
clearOutput()
val width = canvasBounds.width
val height = canvasBounds.height

setBackground(bgColor)
setPenColor(purple)

def action(code: String) {
    interpret(code); println(code)
}

val cmd = Map(
    "forward1" -> s"forward($fdStep)",
    "forward2" -> s"forward($fdStep2)",
    "hop1" -> s"hop($fdStep)",
    "hop2" -> s"hop($fdStep2)",
    "right1" -> s"right($rtStep)",
    "right2" -> s"right($rtStep2)",
    "left1" -> s"left($rtStep)",
    "left2" -> s"left($rtStep2)"
)

def eraseCmds(n: Int) =
    s"saveStyle(); setPenColor($sBgColor); setPenThickness(4); back($n); restoreStyle()"

def button(forcmd: String) = PicShape.button(cmd(forcmd)) { action(cmd(forcmd)) }

val panel = trans(-width / 2, -height / 2) -> VPics(
    HPics(
        button("left2"),
        button("forward2"),
        button("right2"),
        button("hop2"),
        PicShape.button(s"erase($fdStep2)") { action(eraseCmds(fdStep2)) }
    ),
    HPics(
        button("left1"),
        button("forward1"),
        button("right1"),
        button("hop1"),
        PicShape.button(s"erase($fdStep)") { action(eraseCmds(fdStep)) }
    )
)

draw(panel)
println("// Paste the generated program below into the script editor")
println("// and run it -- to reproduce your drawing")
println("clear()")
