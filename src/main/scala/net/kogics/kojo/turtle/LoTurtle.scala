package net.kogics.kojo
package turtle

import java.awt.Color

import scala.language.dynamics
import scala.language.postfixOps

import net.kogics.kojo.kmath.Kmath

class LoTurtle(val t: core.Turtle) extends Dynamic {
  lazy val cmdsu = collection.mutable.HashMap.empty[Symbol, Unit => LoTurtle]
  lazy val cmdsd = collection.mutable.HashMap.empty[Symbol, Double => LoTurtle]
  lazy val cmdsdd = collection.mutable.HashMap.empty[Symbol, (Double, Double) => LoTurtle]
  lazy val cmdsddd = collection.mutable.HashMap.empty[Symbol, (Double, Double, Double) => LoTurtle]
  var lastCmd = Symbol("")

  def fd(n: Double) = { t.forward(n); this }
  def bk(n: Double) = { t.back(n); this }
  def rt(a: Double) = { t.right(a); this }
  def rt(a: Double, r: Double) = { t.right(a, r); this }
  def lt(a: Double) = { t.left(a); this }
  def lt(a: Double, r: Double) = { t.left(a, r); this }
  def hp(n: Double) = { t.hop(n); this }
  def pc(c: Color) = { t.setPenColor(c); this }
  def fc(c: Color) = { t.setFillColor(c); this }
  def pt(n: Int) = { t.setPenThickness(n); this }
  def jb(x: Double, y: Double) = { this hp x rt 90 hp y lt 90; this }
  def jt(x: Double, y: Double) = { t.setPosition(x, y); this }
  def delay(n: Int) = { t.setAnimationDelay(n); this }
  def speed(n: Double) = {
    delay(math.max(math.min(Kmath.map(n, 1, 10, 1000, 0), 1000), 0).toInt)
  }

  def cmds(code: => LoTurtle) {
    cmdsu.put(lastCmd, { x: Unit => code })
  }
  def cmds(code: Double => LoTurtle) {
    cmdsd.put(lastCmd, code)
  }
  def cmds(code: (Double, Double) => LoTurtle) {
    cmdsdd.put(lastCmd, code)
  }

  def cmds(code: (Double, Double, Double) => LoTurtle) {
    cmdsddd.put(lastCmd, code)
  }

  def teach(name: Symbol) = {
    lastCmd = name
    this
  }

  //  def selectDynamic(name: String) = {
  //    cmdsu(Symbol(name)).apply(())
  //  }

  def applyDynamic(name: String)(args: Double*): LoTurtle = {
    try {
      args.size match {
        case 0 => cmdsu(Symbol(name)).apply(())
        case 1 => cmdsd(Symbol(name))(args(0))
        case 2 => cmdsdd(Symbol(name))(args(0), args(1))
        case 3 => cmdsddd(Symbol(name))(args(0), args(1), args(2))
        case _ => this
      }
    }
    catch {
      case t: NoSuchElementException =>
        throw new RuntimeException(s"Invalid command $name with ${args.length} input(s). Check command name and input(s).")
    }
  }
}