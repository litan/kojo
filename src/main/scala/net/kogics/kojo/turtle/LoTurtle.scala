package net.kogics.kojo
package turtle

import java.awt.Color

import scala.language.dynamics
import scala.language.postfixOps

import net.kogics.kojo.kmath.Kmath

class LoTurtle(val t: core.Turtle) extends Dynamic {
  lazy val cmdsu = collection.mutable.HashMap.empty[Symbol, Unit => LoTurtle]
  lazy val cmdsd = collection.mutable.HashMap.empty[Symbol, Any => LoTurtle]
  lazy val cmdsdd = collection.mutable.HashMap.empty[Symbol, (Any, Any) => LoTurtle]
  lazy val cmdsddd = collection.mutable.HashMap.empty[Symbol, (Any, Any, Any) => LoTurtle]
  var lastCmd = Symbol("")

  def toD(n: Any): Double = n match {
    case i: Int    => i.toDouble
    case d: Double => d
    case _         => n.asInstanceOf[Double]
  }
  def delay(n: Int) = { t.setAnimationDelay(n); this }

  def fd(n: Any) = { t.forward(toD(n)); this }
  def bk(n: Any) = { t.back(toD(n)); this }
  def rt(a: Any) = { t.right(toD(a)); this }
  def rt(a: Any, r: Any) = { t.right(toD(a), toD(r)); this }
  def lt(a: Any) = { t.left(toD(a)); this }
  def lt(a: Any, r: Any) = { t.left(toD(a), toD(r)); this }
  def hp(n: Any) = { t.hop(toD(n)); this }
  def pc(c: Any) = { t.setPenColor(c.asInstanceOf[Color]); this }
  def fc(c: Any) = { t.setFillColor(c.asInstanceOf[Color]); this }
  def pt(n: Any) = { t.setPenThickness(n.asInstanceOf[Int]); this }
  def jb(x: Any, y: Any) = { this hp x rt 90 hp y lt 90; this }
  def jt(x: Any, y: Any) = { t.setPosition(toD(x), toD(y)); this }
  def speed(n: Any) = {
    delay(math.max(math.min(Kmath.map(toD(n), 1, 10, 1000, 0), 1000), 0).toInt)
  }

  def cmds(code: => LoTurtle) {
    cmdsu.put(lastCmd, { x: Unit => code })
  }
  def cmds(code: Any => LoTurtle) {
    cmdsd.put(lastCmd, code)
  }
  def cmds(code: (Any, Any) => LoTurtle) {
    cmdsdd.put(lastCmd, code)
  }

  def cmds(code: (Any, Any, Any) => LoTurtle) {
    cmdsddd.put(lastCmd, code)
  }

  def teach(name: Symbol) = {
    lastCmd = name
    this
  }

  //  def selectDynamic(name: String) = {
  //    cmdsu(Symbol(name)).apply(())
  //  }

  def applyDynamic(name: String)(args: Any*): LoTurtle = {
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