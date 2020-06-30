package net.kogics.kojo.kmath

import java.math.BigDecimal
import java.math.BigInteger

import scala.language.implicitConversions

import org.apache.commons.math3.fraction.BigFraction
import org.apache.commons.math3.fraction.BigFractionFormat

trait Rationals {
  implicit def itor(n: Int) = Rational(new BigFraction(n))
  implicit def ltor(n: Long) = Rational(new BigFraction(n))
  implicit def dtor(n: Double) = {
    val ret = Rational(new BigFraction(n, 1E-9, Int.MaxValue))
    if (n != 0 && ret == 0) Rational(new BigFraction(n)) else ret
  }
  implicit def ftor(n: Float) = Rational(new BigFraction(n))

  implicit class RationalMaker(val sc: StringContext) {
    val bff = new BigFractionFormat
    def r(args: Any*): Rational = {
      require(args.isEmpty && sc.parts.size == 1, """Write rationals as "m/n" """)
      val ratString = sc.parts(0)
      try {
        bff.parse(ratString)
      }
      catch {
        case _: Throwable => ratString.toDouble
      }
    }
  }

  implicit class Rational(val rep: BigFraction) {
    def r = this // allow easy conversion of scala numbers to rationals
    def +(other: Rational): Rational = rep.add(other.rep)
    def -(other: Rational): Rational = rep.subtract(other.rep)
    def *(other: Rational): Rational = rep.multiply(other.rep)
    def /(other: Rational): Rational = rep.divide(other.rep)
    def **(n: Long): Rational = rep.pow(n)
    def **(n: Double): Double = rep.pow(n)
    def **(n: Rational): Double = rep.pow(n.toDouble)
    def toNumeric(scale: Int = 5) = rep.bigDecimalValue(scale, BigDecimal.ROUND_HALF_UP)
    def toDouble = rep.doubleValue
    private def i2bi(n: Int): BigInteger = BigInteger.valueOf(n)
    override def toString() = rep.toString.replace(" ", "")
    //    def ==(other: Rational): Boolean = equals(other)
    override def equals(other: Any) = other match {
      case r: Rational => rep == r.rep
      case i: Int      => rep == itor(i).rep
      case l: Long     => rep == ltor(l).rep
      case d: Double   => rep == dtor(d).rep
      case f: Float    => rep == ftor(f).rep
    }
    def toMixedString() = {
      val n = rep.getNumerator
      val d = rep.getDenominator
      if (n.compareTo(d) > 0) {
        val rem = n.mod(d)
        if (rem.compareTo(i2bi(0)) == 0)
          "%s".format(n.divide(d))
        else
          "%s  %s/%s".format(n.divide(d), rem, d)
      }
      else {
        "%s/%s".format(n, d)
      }
    }
  }
}


