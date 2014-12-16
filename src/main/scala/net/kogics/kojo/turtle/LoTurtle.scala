package net.kogics.kojo
package turtle

import scala.language.dynamics
import scala.language.postfixOps
import java.awt.Color

class LoTurtle(val t: core.Turtle) extends Dynamic {
  lazy val cmds = collection.mutable.HashMap.empty[Symbol, Unit => Unit]
  lazy val cmdsd = collection.mutable.HashMap.empty[Symbol, Double => Unit]
  lazy val cmdsdd = collection.mutable.HashMap.empty[Symbol, (Double, Double) => Unit]
  var lastCmd = Symbol("")

  def fd(n: Double) = t.forward(n)
  def rt(a: Double) = t.right(a)
  def lt(a: Double) = t.left(a)
  def hp(n: Double) = t.hop(n)
  def pc(c: Color) = t.setPenColor(c)
  def fc(c: Color) = t.setFillColor(c)
  def pt(n: Int) = t.setPenThickness(n)
  def delay(n: Int) = t.setAnimationDelay(n)

  def cmds(code: => Unit) {
    cmds.put(lastCmd, { x: Unit => code })
  }
  def cmds(code: Double => Unit) {
    cmdsd.put(lastCmd, code)
  }
  def cmds(code: (Double, Double) => Unit) {
    cmdsdd.put(lastCmd, code)
  }

  def teach(name: Symbol) = {
    lastCmd = name
    this
  }

  def selectDynamic(name: String) {
    cmds(Symbol(name)).apply(())
  }

  def applyDynamic(name: String)(arg1: Double, arg2: Double = Double.NaN) {
    if (arg2.isNaN) {
      cmdsd(Symbol(name))(arg1)
    }
    else {
      cmdsdd(Symbol(name))(arg1, arg2)
    }
  }
}