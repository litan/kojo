package net.kogics.kojo.kmath

import java.math.BigDecimal
import java.math.BigInteger

import scala.language.implicitConversions

import org.apache.commons.math.fraction.BigFraction
import org.apache.commons.math.fraction.BigFractionFormat
import org.apache.commons.math.stat.StatUtils

object Kmath {
  implicit def seqToArrD(seq: Seq[Double]): Array[Double] = seq.toArray
  implicit def seqToArrI(seq: Seq[Int]): Array[Double] = seq.map { _.toDouble }.toArray

  def mean(nums: Array[Double]) = StatUtils.mean(nums)
  def variance(nums: Array[Double]) = StatUtils.variance(nums)
  def variance(nums: Array[Double], mean: Double) = StatUtils.variance(nums, mean)

  implicit class Rational(val rep: BigFraction) {
    def +(other: Rational): Rational = rep.add(other.rep)
    def -(other: Rational): Rational = rep.subtract(other.rep)
    def *(other: Rational): Rational = rep.multiply(other.rep)
    def /(other: Rational): Rational = rep.divide(other.rep)
    def **(n: Long): Rational = rep.pow(n)
    def **(n: Double): Double = rep.pow(n)
    def **(n: Rational): Double = rep.pow(n.toDouble)
    def toNumeric(scale: Int = 5) = rep.bigDecimalValue(scale, BigDecimal.ROUND_HALF_UP)
    def toDouble() = rep.doubleValue
    private def i2bi(n: Int): BigInteger = BigInteger.valueOf(n)
    override def toString() = rep.toString()
    def toMixedString() = {
      val n = rep.getNumerator
      val d = rep.getDenominator
      if (n.compareTo(d) > 0) {
        val rem = n.mod(d)
        if (rem.compareTo(i2bi(0)) == 0)
          "%s" format (n.divide(d))
        else
          "%s  %s/%s" format (n.divide(d), rem, d)
      }
      else {
        "%s/%s" format (n, d)
      }
    }
  }
}


