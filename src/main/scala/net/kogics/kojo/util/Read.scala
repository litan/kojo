package net.kogics.kojo.util

trait Read[T] {
  def read(s: String): T
  def typeName: String
}

object Read {
  implicit object StringRead extends Read[String] {
    def read(s: String) = s
    def typeName = "String"
  }

  implicit object DoubleRead extends Read[Double] {
    def read(n: String) = n.toDouble
    def typeName = "Double"
  }

  implicit object IntRead extends Read[Int] {
    def read(n: String) = n.toInt
    def typeName = "Int"
  }
}
