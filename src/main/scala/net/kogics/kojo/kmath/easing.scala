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

  val ElasticIn = new KEasing {
    override def ease(start: Double, end: Double, step: Double, duration: Double): Double =
      Easing.ELASTIC_IN.ease(step.toFloat, start.toFloat, (end - start).toFloat, duration.toFloat)
  }

  val ElasticOut = new KEasing {
    override def ease(start: Double, end: Double, step: Double, duration: Double): Double =
      Easing.ELASTIC_OUT.ease(step.toFloat, start.toFloat, (end - start).toFloat, duration.toFloat)
  }

  val ElasticInOut = new KEasing {
    override def ease(start: Double, end: Double, step: Double, duration: Double): Double =
      Easing.ELASTIC_IN_OUT.ease(step.toFloat, start.toFloat, (end - start).toFloat, duration.toFloat)
  }

  val BounceIn = new KEasing {
    override def ease(start: Double, end: Double, step: Double, duration: Double): Double =
      Easing.BOUNCE_IN.ease(step.toFloat, start.toFloat, (end - start).toFloat, duration.toFloat)
  }

  val BounceOut = new KEasing {
    override def ease(start: Double, end: Double, step: Double, duration: Double): Double =
      Easing.BOUNCE_OUT.ease(step.toFloat, start.toFloat, (end - start).toFloat, duration.toFloat)
  }

  val BounceInOut = new KEasing {
    override def ease(start: Double, end: Double, step: Double, duration: Double): Double =
      Easing.BOUNCE_IN_OUT.ease(step.toFloat, start.toFloat, (end - start).toFloat, duration.toFloat)
  }
}
