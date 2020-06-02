package net.kogics.kojo
package kmath

import scala.language.implicitConversions

import org.apache.commons.math3.stat.StatUtils
import org.apache.commons.math3.util.ArithmeticUtils

import net.kogics.kojo.core.Point

object Kmath {
  implicit def seqToArrD(seq: Seq[Double]): Array[Double] = seq.toArray
  implicit def seqToArrI(seq: Seq[Int]): Array[Double] = seq.map { _.toDouble }.toArray

  def mean(nums: Array[Double]) = StatUtils.mean(nums)
  def variance(nums: Array[Double]) = StatUtils.variance(nums)
  def variance(nums: Array[Double], mean: Double) = StatUtils.variance(nums, mean)

  def constrain(value: Double, min: Double, max: Double) = util.Math.constrain(value, min, max)
  def map(value: Double, start1: Double, stop1: Double, start2: Double, stop2: Double) = util.Math.map(value, start1, stop1, start2, stop2)
  def lerp(start: Double, stop: Double, amt: Double) = util.Math.lerp(start, stop, amt)

  def distance(x1: Double, y1: Double, x2: Double, y2: Double): Double =
    math.sqrt(math.pow(x2 - x1, 2) + math.pow(y2 - y1, 2))
  def distance(p1: Point, p2: Point): Double = distance(p1.x, p1.y, p2.x, p2.y)

  def angle(x1: Double, y1: Double, x2: Double, y2: Double): Double = math.atan2(y2 - y1, x2 - x1).toDegrees
  def angle(p1: Point, p2: Point): Double = angle(p1.x, p1.y, p2.x, p2.y)

  def gcd(p: Int, q: Int) = ArithmeticUtils.gcd(p, q)
  def gcd(p: Long, q: Long) = ArithmeticUtils.gcd(p, q)
  def hcf(p: Int, q: Int) = ArithmeticUtils.gcd(p, q)
  def hcf(p: Long, q: Long) = ArithmeticUtils.gcd(p, q)
  def lcm(p: Int, q: Int) = ArithmeticUtils.lcm(p, q)
  def lcm(p: Long, q: Long) = ArithmeticUtils.lcm(p, q)

  private lazy val log2_e = math.log(2)
  def log2(n: Double) = math.log(n) / log2_e
}
