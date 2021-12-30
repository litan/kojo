package net.kogics.kojo.kmath

import net.kogics.kojo.util.Easing

trait KEasing {
  def ease(start: Double, end: Double, step: Double, duration: Double): Double
}

object KEasing {
  val Linear = new KEasing {
    override def ease(start: Double, end: Double, step: Double, duration: Double): Double =
      Easing.LINEAR.ease(step.toFloat, start.toFloat, (end - start).toFloat, duration.toFloat)
  }

  val QuadIn = new KEasing {
    override def ease(start: Double, end: Double, step: Double, duration: Double): Double =
      Easing.QUAD_IN.ease(step.toFloat, start.toFloat, (end - start).toFloat, duration.toFloat)
  }

  val QuadOut = new KEasing {
    override def ease(start: Double, end: Double, step: Double, duration: Double): Double =
      Easing.QUAD_OUT.ease(step.toFloat, start.toFloat, (end - start).toFloat, duration.toFloat)
  }

  val QuadInOut = new KEasing {
    override def ease(start: Double, end: Double, step: Double, duration: Double): Double =
      Easing.QUAD_IN_OUT.ease(step.toFloat, start.toFloat, (end - start).toFloat, duration.toFloat)
  }
}
