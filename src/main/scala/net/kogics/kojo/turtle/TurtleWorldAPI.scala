package net.kogics.kojo
package turtle

import java.awt.Font
import java.awt.Paint

import net.kogics.kojo.core.Point
import net.kogics.kojo.core.Style
import net.kogics.kojo.core.TurtleMover
import net.kogics.kojo.core.Voice
import net.kogics.kojo.util.UserCommand

class TurtleWorldAPI(turtle0: => core.Turtle) extends TurtleMover {
    def forward(): Unit = forward(25)
    def forward(n: Double) = turtle0.forward(n)
    UserCommand("forward", List("numSteps"), "Moves the turtle forward a given number of steps.")

    def hop(): Unit = hop(25)
    UserCommand("hop", List("numSteps"), "Moves the turtle forward a given number of steps without drawing a line.")

    // all commands that have a UserCommand entry but no implementation come in via
    // RichTurtleCommands or TurtleMover
    def back(): Unit = back(25)
    UserCommand("back", List("numSteps"), "Moves the turtle back a given number of steps.")

    UserCommand("home", Nil, "Moves the turtle to its original location, and makes it point north.")

    def jumpTo(x: Double, y: Double) = turtle0.jumpTo(x, y)
    UserCommand.addCompletion("jumpTo", List("x", "y"))

    UserCommand("setPosition", List("x", "y"), "Sends the turtle to the point (x, y) without drawing a line. The turtle's heading is not changed.")

    def position: Point = turtle0.position
    UserCommand.addSynopsis("position - Queries the turtle's position.")

    def moveTo(x: Double, y: Double) = turtle0.moveTo(x, y)
    UserCommand("moveTo", List("x", "y"), "Turns the turtle towards (x, y) and moves the turtle to that point. ")

    def turn(angle: Double) = turtle0.turn(angle)
    UserCommand("turn", List("angle"), "Turns the turtle through a specified angle. Angles are positive for counter-clockwise turns.")

    UserCommand("right", Nil, "Turns the turtle 90 degrees right (clockwise).")
    UserCommand("right", List("angle"), "Turns the turtle angle degrees right (clockwise).")

    UserCommand("left", Nil, "Turns the turtle 90 degrees left (counter-clockwise).")
    UserCommand("left", List("angle"), "Turns the turtle angle degrees left (counter-clockwise). ")

    def towards(x: Double, y: Double) = turtle0.towards(x, y)
    UserCommand("towards", List("x", "y"), "Turns the turtle towards the point (x, y).")

    UserCommand("setHeading", List("angle"), "Sets the turtle's heading to angle (0 is towards the right side of the screen ('east'), 90 is up ('north')).")

    def heading: Double = turtle0.heading
    UserCommand.addSynopsis("heading - Queries the turtle's heading (0 is towards the right side of the screen ('east'), 90 is up ('north')).")
    UserCommand.addSynopsisSeparator()

    def penDown() = turtle0.penDown()
    UserCommand("penDown", Nil, "Makes the turtle draw lines as it moves (the default setting). ")

    def penUp() = turtle0.penUp()
    UserCommand("penUp", Nil, "Makes the turtle not draw lines as it moves.")

    def setPenColor(color: Paint) = turtle0.setPenColor(color)
    UserCommand("setPenColor", List("color"), "Specifies the color of the pen that the turtle draws with.")

    def setFillColor(color: Paint) = turtle0.setFillColor(color)
    UserCommand("setFillColor", List("color"), "Specifies the fill color of the figures drawn by the turtle.")
    UserCommand.addSynopsisSeparator()

    def setPenThickness(t: Double) = turtle0.setPenThickness(t)
    UserCommand("setPenThickness", List("thickness"), "Specifies the width of the pen that the turtle draws with.")

    def setPenFontSize(n: Int) = turtle0.setPenFontSize(n)
    UserCommand("setPenFontSize", List("n"), "Specifies the font size of the pen that the turtle writes with.")

    def setPenFont(font: Font) = turtle0.setPenFont(font)
    UserCommand("setPenFont", List("font"), "Specifies the font of the pen that the turtle writes with.")

    def saveStyle() = turtle0.saveStyle()
    UserCommand.addCompletion("saveStyle", Nil)

    def restoreStyle() = turtle0.restoreStyle()
    UserCommand.addCompletion("restoreStyle", Nil)

    def savePosHe() = turtle0.savePosHe()
    UserCommand("savePosHe", Nil, "Saves the turtle's current position and heading")

    def restorePosHe() = turtle0.restorePosHe()
    UserCommand("restorePosHe", Nil, "Restores the turtle's current position and heading")

    def beamsOn() = turtle0.beamsOn()
    UserCommand("beamsOn", Nil, "Shows crossbeams centered on the turtle - to help with more precise navigation.")

    def beamsOff() = turtle0.beamsOff()
    UserCommand("beamsOff", Nil, "Hides the turtle crossbeams.")

    def invisible() = turtle0.invisible()
    UserCommand("invisible", Nil, "Hides the turtle.")

    def visible() = turtle0.visible()
    UserCommand("visible", Nil, "Makes the hidden turtle visible again.")
    UserCommand.addSynopsisSeparator()

    def write(text: String) = turtle0.write(text)
    UserCommand("write", List("obj"), "Makes the turtle write the specified object as a string at its current location.")

    def setAnimationDelay(d: Long) = turtle0.setAnimationDelay(d)
    UserCommand("setAnimationDelay", List("delay"), "Sets the turtle's speed. The specified delay is the amount of time (in milliseconds) taken by the turtle to move through a distance of one hundred steps.")

    def animationDelay = turtle0.animationDelay
    UserCommand.addSynopsis("animationDelay - Queries the turtle's delay setting.")
    UserCommand.addSynopsisSeparator()

    def playSound(voice: Voice) = turtle0.playSound(voice)
    UserCommand("playSound", List("voice"), "Makes the turtle play the specified melody, rhythm, or score.")

    def style: Style = turtle0.style

    def arc(r: Double, a: Int) = turtle0.arc(r, a)
    def setCostume(costumeFile: String) = turtle0.setCostume(costumeFile)
    UserCommand("setCostume", List("costumeFile"), "Changes the costume (i.e. image) associated with the turtle to the image in the specified file.")

    def setCostumes(costumeFiles: Vector[String]) = turtle0.setCostumes(costumeFiles)
    def nextCostume() = turtle0.nextCostume()
    def scaleCostume(factor: Double) = turtle0.scaleCostume(factor)
    def changePosition(x: Double, y: Double) = turtle0.changePosition(x, y)
    def area = turtle0.area
    def perimeter = turtle0.perimeter
    
    // need to make this a class to get it to show up reliably in completions
    class Costume {
      val car = "/media/costumes/car.png"
      val pencil = "/media/costumes/pencil.png"
      val bat1 = "/media/costumes/bat1-a.png"
      val bat2 = "/media/costumes/bat1-b.png"
      val womanWaving = "/media/costumes/womanwaving.png"
    }
    
    class Background {
      val trainTrack = "/media/backgrounds/train-tracks3.gif"
    }

    class Sound {
      val medieval1 = "/media/music-loops/Medieval1.mp3"
    }
}
