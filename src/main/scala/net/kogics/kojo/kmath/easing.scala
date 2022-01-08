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

  val CubicIn = new KEasing {
    override def ease(start: Double, end: Double, step: Double, duration: Double): Double =
      Easing.CUBIC_IN.ease(step.toFloat, start.toFloat, (end - start).toFloat, duration.toFloat)
  }

  val CubicOut = new KEasing {
    override def ease(start: Double, end: Double, step: Double, duration: Double): Double =
      Easing.CUBIC_OUT.ease(step.toFloat, start.toFloat, (end - start).toFloat, duration.toFloat)
  }

  val CubicInOut = new KEasing {
    override def ease(start: Double, end: Double, step: Double, duration: Double): Double =
      Easing.CUBIC_IN_OUT.ease(step.toFloat, start.toFloat, (end - start).toFloat, duration.toFloat)
  }

  val QuarticIn = new KEasing {
    override def ease(start: Double, end: Double, step: Double, duration: Double): Double =
      Easing.QUARTIC_IN.ease(step.toFloat, start.toFloat, (end - start).toFloat, duration.toFloat)
  }

  val QuarticOut = new KEasing {
    override def ease(start: Double, end: Double, step: Double, duration: Double): Double =
      Easing.QUARTIC_OUT.ease(step.toFloat, start.toFloat, (end - start).toFloat, duration.toFloat)
  }

  val QuarticInOut = new KEasing {
    override def ease(start: Double, end: Double, step: Double, duration: Double): Double =
      Easing.QUARTIC_IN_OUT.ease(step.toFloat, start.toFloat, (end - start).toFloat, duration.toFloat)
  }

  val QuinticIn = new KEasing {
    override def ease(start: Double, end: Double, step: Double, duration: Double): Double =
      Easing.QUINTIC_IN.ease(step.toFloat, start.toFloat, (end - start).toFloat, duration.toFloat)
  }

  val QuinticOut = new KEasing {
    override def ease(start: Double, end: Double, step: Double, duration: Double): Double =
      Easing.QUINTIC_OUT.ease(step.toFloat, start.toFloat, (end - start).toFloat, duration.toFloat)
  }

  val QuinticInOut = new KEasing {
    override def ease(start: Double, end: Double, step: Double, duration: Double): Double =
      Easing.QUINTIC_IN_OUT.ease(step.toFloat, start.toFloat, (end - start).toFloat, duration.toFloat)
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

  val SineIn = new KEasing {
    override def ease(start: Double, end: Double, step: Double, duration: Double): Double =
      Easing.SINE_IN.ease(step.toFloat, start.toFloat, (end - start).toFloat, duration.toFloat)
  }

  val SineOut = new KEasing {
    override def ease(start: Double, end: Double, step: Double, duration: Double): Double =
      Easing.SINE_OUT.ease(step.toFloat, start.toFloat, (end - start).toFloat, duration.toFloat)
  }

  val SineInOut = new KEasing {
    override def ease(start: Double, end: Double, step: Double, duration: Double): Double =
      Easing.SINE_IN_OUT.ease(step.toFloat, start.toFloat, (end - start).toFloat, duration.toFloat)
  }

  val CircIn = new KEasing {
    override def ease(start: Double, end: Double, step: Double, duration: Double): Double =
      Easing.CIRC_IN.ease(step.toFloat, start.toFloat, (end - start).toFloat, duration.toFloat)
  }

  val CircOut = new KEasing {
    override def ease(start: Double, end: Double, step: Double, duration: Double): Double =
      Easing.CIRC_OUT.ease(step.toFloat, start.toFloat, (end - start).toFloat, duration.toFloat)
  }

  val CircInOut = new KEasing {
    override def ease(start: Double, end: Double, step: Double, duration: Double): Double =
      Easing.CIRC_IN_OUT.ease(step.toFloat, start.toFloat, (end - start).toFloat, duration.toFloat)
  }

  val ExpoIn = new KEasing {
    override def ease(start: Double, end: Double, step: Double, duration: Double): Double =
      Easing.EXPO_IN.ease(step.toFloat, start.toFloat, (end - start).toFloat, duration.toFloat)
  }

  val ExpoOut = new KEasing {
    override def ease(start: Double, end: Double, step: Double, duration: Double): Double =
      Easing.EXPO_OUT.ease(step.toFloat, start.toFloat, (end - start).toFloat, duration.toFloat)
  }

  val ExpoInOut = new KEasing {
    override def ease(start: Double, end: Double, step: Double, duration: Double): Double =
      Easing.EXPO_IN_OUT.ease(step.toFloat, start.toFloat, (end - start).toFloat, duration.toFloat)
  }
}
