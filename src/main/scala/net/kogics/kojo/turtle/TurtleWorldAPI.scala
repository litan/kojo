package net.kogics.kojo
package turtle

import java.awt.Paint
import net.kogics.kojo.core.Point
import net.kogics.kojo.core.Style
import net.kogics.kojo.core.TurtleMover
import net.kogics.kojo.core.Voice
import net.kogics.kojo.util.UserCommand

class TurtleWorldAPI(turtle0: => core.Turtle) extends TurtleMover {
    def forward(): Unit = forward(25)
    override def forward(n: Double) = turtle0.forward(n)
    UserCommand("forward", List("numSteps"), "Moves the turtle forward a given number of steps.")

    def back(): Unit = back(25)
    override def back(n: Double) = turtle0.back(n)
    UserCommand("back", List("numSteps"), "Moves the turtle back a given number of steps.")

    override def home(): Unit = turtle0.home()
    UserCommand("home", Nil, "Moves the turtle to its original location, and makes it point north.")

    override def jumpTo(p: Point): Unit = turtle0.jumpTo(p.x, p.y)
    override def jumpTo(x: Double, y: Double) = turtle0.jumpTo(x, y)
    UserCommand.addCompletion("jumpTo", List("x", "y"))

    override def setPosition(p: Point): Unit = turtle0.jumpTo(p)
    override def setPosition(x: Double, y: Double) = turtle0.jumpTo(x, y)
    UserCommand("setPosition", List("x", "y"), "Sends the turtle to the point (x, y) without drawing a line. The turtle's heading is not changed.")

    override def position: Point = turtle0.position
    UserCommand.addSynopsis("position - Queries the turtle's position.")

    def moveTo() = println("Please provide the coordinates of the point that the turtle should move to - e.g. moveTo(100, 100)")
    override def moveTo(x: Double, y: Double) = turtle0.moveTo(x, y)
    override def moveTo(p: Point): Unit = turtle0.moveTo(p.x, p.y)
    UserCommand("moveTo", List("x", "y"), "Turns the turtle towards (x, y) and moves the turtle to that point. ")

    def turn() = println("Please provide the angle to turn in degrees - e.g. turn(45)")
    override def turn(angle: Double) = turtle0.turn(angle)
    UserCommand("turn", List("angle"), "Turns the turtle through a specified angle. Angles are positive for counter-clockwise turns.")

    override def right(): Unit = turtle0.right()
    UserCommand("right", Nil, "Turns the turtle 90 degrees right (clockwise).")
    override def right(angle: Double): Unit = turtle0.right(angle)
    UserCommand("right", List("angle"), "Turns the turtle angle degrees right (clockwise).")

    override def left(): Unit = turtle0.left()
    UserCommand("left", Nil, "Turns the turtle 90 degrees left (counter-clockwise).")
    override def left(angle: Double): Unit = turtle0.left(angle)
    UserCommand("left", List("angle"), "Turns the turtle angle degrees left (counter-clockwise). ")

    def towards() = println("Please provide the coordinates of the point that the turtle should turn towards - e.g. towards(100, 100)")
    override def towards(p: Point): Unit = turtle0.towards(p.x, p.y)
    override def towards(x: Double, y: Double) = turtle0.towards(x, y)
    UserCommand("towards", List("x", "y"), "Turns the turtle towards the point (x, y).")

    override def setHeading(angle: Double) = turtle0.setHeading(angle)
    UserCommand("setHeading", List("angle"), "Sets the turtle's heading to angle (0 is towards the right side of the screen ('east'), 90 is up ('north')).")

    override def heading: Double = turtle0.heading
    UserCommand.addSynopsis("heading - Queries the turtle's heading (0 is towards the right side of the screen ('east'), 90 is up ('north')).")
    UserCommand.addSynopsisSeparator()

    override def penDown() = turtle0.penDown()
    UserCommand("penDown", Nil, "Makes the turtle draw lines as it moves (the default setting). ")

    override def penUp() = turtle0.penUp()
    UserCommand("penUp", Nil, "Makes the turtle not draw lines as it moves.")

    def setPenColor() = println("Please provide the color of the pen that the turtle should draw with - e.g setPenColor(blue)")
    override def setPenColor(color: Paint) = turtle0.setPenColor(color)
    UserCommand("setPenColor", List("color"), "Specifies the color of the pen that the turtle draws with.")

    def setFillColor() = println("Please provide the fill color for the areas drawn by the turtle - e.g setFillColor(yellow)")
    override def setFillColor(color: Paint) = turtle0.setFillColor(color)
    UserCommand("setFillColor", List("color"), "Specifies the fill color of the figures drawn by the turtle.")
    UserCommand.addSynopsisSeparator()

    def setPenThickness() = println("Please provide the thickness of the pen that the turtle should draw with - e.g setPenThickness(1)")
    override def setPenThickness(t: Double) = turtle0.setPenThickness(t)
    UserCommand("setPenThickness", List("thickness"), "Specifies the width of the pen that the turtle draws with.")

    override def setPenFontSize(n: Int) = turtle0.setPenFontSize(n)
    UserCommand("setPenFontSize", List("n"), "Specifies the font size of the pen that the turtle writes with.")

    override def saveStyle() = turtle0.saveStyle()
    UserCommand.addCompletion("saveStyle", Nil)

    override def restoreStyle() = turtle0.restoreStyle()
    UserCommand.addCompletion("restoreStyle", Nil)

    override def savePosHe() = turtle0.savePosHe()
    UserCommand("savePosHe", Nil, "Saves the turtle's current position and heading")

    override def restorePosHe() = turtle0.restorePosHe()
    UserCommand("restorePosHe", Nil, "Restores the turtle's current position and heading")

    override def beamsOn() = turtle0.beamsOn()
    UserCommand("beamsOn", Nil, "Shows crossbeams centered on the turtle - to help with more precise navigation.")

    override def beamsOff() = turtle0.beamsOff()
    UserCommand("beamsOff", Nil, "Hides the turtle crossbeams.")

    override def invisible() = turtle0.invisible()
    UserCommand("invisible", Nil, "Hides the turtle.")

    override def visible() = turtle0.visible()
    UserCommand("visible", Nil, "Makes the hidden turtle visible again.")
    UserCommand.addSynopsisSeparator()

    override def write(obj: Any): Unit = turtle0.write(obj)
    override def write(text: String) = turtle0.write(text)
    UserCommand("write", List("obj"), "Makes the turtle write the specified object as a string at its current location.")

    override def setAnimationDelay(d: Long) = turtle0.setAnimationDelay(d)
    UserCommand("setAnimationDelay", List("delay"), "Sets the turtle's speed. The specified delay is the amount of time (in milliseconds) taken by the turtle to move through a distance of one hundred steps.")

    override def animationDelay = turtle0.animationDelay
    UserCommand.addSynopsis("animationDelay - Queries the turtle's delay setting.")
    UserCommand.addSynopsisSeparator()

    override def playSound(voice: Voice) = turtle0.playSound(voice)
    UserCommand("playSound", List("voice"), "Makes the turtle play the specified melody, rhythm, or score.")

    override def style: Style = turtle0.style

    override def arc(r: Double, a: Int) = turtle0.arc(r, a)
    override def circle(r: Double) = turtle0.circle(r)
}
