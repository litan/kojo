package net.kogics.kojo.syntax

import net.kogics.kojo.doodle.{ Angle, Normalized, UnsignedByte }
import normalized._
import uByte._
import angle._

object Builtins {
  val Color = net.kogics.kojo.doodle.Color
  implicit def int2ubyte(n: Int): UnsignedByte = n.uByte
  implicit def double2norm(n: Double): Normalized = n.normalized
  implicit def int2norm(n: Int): Normalized = n.normalized
  implicit def double2angle(n: Double): Angle = n.degrees
  implicit def int2angle(n: Int): Angle = n.degrees
}
