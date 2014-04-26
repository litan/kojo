package net.kogics.kojo.kmath

import scala.language.implicitConversions

import org.apache.commons.math.stat.StatUtils

object Kmath {
  implicit def seqToArrD(seq: Seq[Double]): Array[Double] = seq.toArray
  implicit def seqToArrI(seq: Seq[Int]): Array[Double] = seq.map { _.toDouble }.toArray

  def mean(nums: Array[Double]) = StatUtils.mean(nums)
  def variance(nums: Array[Double]) = StatUtils.variance(nums)
  def variance(nums: Array[Double], mean: Double) = StatUtils.variance(nums, mean)
}


