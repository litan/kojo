package net.kogics.kojo
package turtle

import java.awt.Paint

import net.kogics.kojo.core.Point
import net.kogics.kojo.core.Style
import net.kogics.kojo.core.TurtleMover
import net.kogics.kojo.core.Voice

class TurtleWorldAPI(turtle0: core.Turtle) extends TurtleMover {
  def forward(): Unit = forward(25)
  override def forward(n: Double) = turtle0.forward(n)

  def back(): Unit = back(25)
  override def back(n: Double) = turtle0.back(n)

  override def home(): Unit = turtle0.home()

  override def jumpTo(p: Point): Unit = turtle0.jumpTo(p.x, p.y)
  override def jumpTo(x: Double, y: Double) = turtle0.jumpTo(x, y)

  override def setPosition(p: Point): Unit = turtle0.jumpTo(p)
  override def setPosition(x: Double, y: Double) = turtle0.jumpTo(x, y)

  override def position: Point = turtle0.position

  def moveTo() = println("Please provide the coordinates of the point that the turtle should move to - e.g. moveTo(100, 100)")
  override def moveTo(x: Double, y: Double) = turtle0.moveTo(x, y)
  override def moveTo(p: Point): Unit = turtle0.moveTo(p.x, p.y)

  def turn() = println("Please provide the angle to turn in degrees - e.g. turn(45)")
  override def turn(angle: Double) = turtle0.turn(angle)

  override def right(): Unit = turtle0.right()
  override def right(angle: Double): Unit = turtle0.right(angle)

  override def left(): Unit = turtle0.left()
  override def left(angle: Double): Unit = turtle0.left(angle)

  def towards() = println("Please provide the coordinates of the point that the turtle should turn towards - e.g. towards(100, 100)")
  override def towards(p: Point): Unit = turtle0.towards(p.x, p.y)
  override def towards(x: Double, y: Double) = turtle0.towards(x, y)

  override def setHeading(angle: Double) = turtle0.setHeading(angle)

  override def heading: Double = turtle0.heading

  override def penDown() = turtle0.penDown()

  override def penUp() = turtle0.penUp()

  def setPenColor() = println("Please provide the color of the pen that the turtle should draw with - e.g setPenColor(blue)")
  override def setPenColor(color: Paint) = turtle0.setPenColor(color)

  def setFillColor() = println("Please provide the fill color for the areas drawn by the turtle - e.g setFillColor(yellow)")
  override def setFillColor(color: Paint) = turtle0.setFillColor(color)

  def setPenThickness() = println("Please provide the thickness of the pen that the turtle should draw with - e.g setPenThickness(1)")
  override def setPenThickness(t: Double) = turtle0.setPenThickness(t)

  override def setPenFontSize(n: Int) = turtle0.setPenFontSize(n)

  override def saveStyle() = turtle0.saveStyle()

  override def restoreStyle() = turtle0.restoreStyle()

  override def savePosHe() = turtle0.savePosHe()

  override def restorePosHe() = turtle0.restorePosHe()

  override def beamsOn() = turtle0.beamsOn()

  override def beamsOff() = turtle0.beamsOff()

  override def invisible() = turtle0.invisible()

  override def visible() = turtle0.visible()

  override def write(obj: Any): Unit = turtle0.write(obj)
  override def write(text: String) = turtle0.write(text)

  override def setAnimationDelay(d: Long) = turtle0.setAnimationDelay(d)

  override def animationDelay = turtle0.animationDelay

  override def playSound(voice: Voice) = turtle0.playSound(voice)

  override def cleart() = turtle0.cleart()
  //
  //  def cleari() = { clear(); invisible() }
  override def style: Style = turtle0.style

  override def arc(r: Double, a: Int) = turtle0.arc(r, a)
  override def circle(r: Double) = turtle0.circle(r)
}
