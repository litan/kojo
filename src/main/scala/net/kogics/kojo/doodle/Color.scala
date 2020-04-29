// Borrowed from: https://github.com/underscoreio/doodle
package net.kogics.kojo.doodle

import java.awt.GradientPaint
import java.awt.LinearGradientPaint
import java.awt.MultipleGradientPaint
import java.awt.RadialGradientPaint
import java.awt.TexturePaint
import java.awt.geom.Rectangle2D
import java.awt.{Color => AwtColor}

import scala.collection.mutable.ArrayBuffer

import org.hsluv.HUSLColorConverter

import net.kogics.kojo.syntax.angle._
import net.kogics.kojo.syntax.normalized._
import net.kogics.kojo.syntax.uByte._
import net.kogics.kojo.util.Utils

sealed abstract class Color extends Product with Serializable {

  // Accessors ----------------------------------------------

  def red: UnsignedByte =
    this.toRGBA.r

  def green: UnsignedByte =
    this.toRGBA.g

  def blue: UnsignedByte =
    this.toRGBA.b

  def hue: Angle =
    this.toHSLA.h

  def saturation: Normalized =
    this.toHSLA.s

  def lightness: Normalized =
    this.toHSLA.l

  def alpha: Normalized =
    this match {
      case RGBA(_, _, _, a) => a
      case HSLA(_, _, _, a) => a
    }

  // Color manipulation ------------------------------------

  /** Copies this color, changing the hue to the given value*/
  def hue(angle: Double): Color =
    this.toHSLA.copy(h = angle.degrees)

  /** Copies this color, changing the saturation to the given value*/
  def saturation(s: Double): Color =
    this.toHSLA.copy(s = s.normalized)

  /** Copies this color, changing the lightness to the given value*/
  def lightness(l: Double): Color =
    this.toHSLA.copy(l = l.normalized)

  /** Copies this color, changing the alpha to the given value*/
  def alpha(a: Double): Color =
    this.toHSLA.copy(a = a.normalized)

  /** Rotate hue by the given angle */
  def spin(angle: Double) = {
    val original = this.toHSLA
    original.copy(h = original.h + angle.degrees)
  }

  /** Rotate hue by the given amount relative to a full turn */
  def spinBy(turnFactor: Double) = {
    val original = this.toHSLA
    original.copy(h = original.h + Angle.turns(turnFactor))
  }

  /**
   * Saturate the color by the given amount. This is an absolute
   * amount, not an amount relative to the Color's current
   * saturation. Saturation is clipped at Normalized.MaxValue
   */
  def saturate(saturation: Double) = {
    val original = this.toHSLA
    original.copy(s = Normalized.clip(original.s + saturation.normalized))
  }

  /**
   * Desaturate the color by the given amount. This is an absolute
   * amount, not an amount relative to the Color's current
   * saturation. Saturation is clipped at Normalized.MaxValue
   */
  def desaturate(desaturation: Double) = {
    val original = this.toHSLA
    original.copy(s = Normalized.clip(original.s - desaturation.normalized))
  }

  /**
   * Lighten the color by the given amount. This is an absolute
   * amount, not an amount relative to the Color's current
   * lightness. Lightness is clipped at Normalized.MaxValue
   */
  def lighten(lightness: Double) = {
    val original = this.toHSLA
    original.copy(l = Normalized.clip(original.l + lightness.normalized))
  }

  /**
   * Darken the color by the given amount. This is an absolute
   * amount, not an amount relative to the Color's current
   * lightness. Lightness is clipped at Normalized.MaxValue
   */
  def darken(darkness: Double) = {
    val original = this.toHSLA
    original.copy(l = Normalized.clip(original.l - darkness.normalized))
  }

  /** Increase the alpha channel by the given amount. */
  def fadeIn(opacity: Double) = {
    val original = this.toHSLA
    original.copy(a = Normalized.clip(original.a + opacity.normalized))
  }

  /** Decrease the alpha channel by the given amount. */
  def fadeOut(opacity: Double) = {
    val original = this.toHSLA
    original.copy(a = Normalized.clip(original.a - opacity.normalized))
  }

  /**
   * Saturate the color by the given *relative* amount. For example, calling
   * `aColor.saturateBy(0.1.normalized` increases the saturation by 10% of the
   * current saturation.
   */
  def saturateBy(saturation: Double) = {
    val original = this.toHSLA
    original.copy(s = Normalized.clip(original.s.get * (1 + saturation)))
  }

  /**
   * Desaturate the color by the given *relative* amount. For example, calling
   * `aColor.desaturateBy(0.1.normalized` decreases the saturation by 10% of the
   * current saturation.
   */
  def desaturateBy(desaturation: Double) = {
    val original = this.toHSLA
    original.copy(s = Normalized.clip(original.s.get * (1 - desaturation)))
  }

  /**
   * Lighten the color by the given *relative* amount. For example, calling
   * `aColor.lightenBy(0.1.normalized` increases the lightness by 10% of the
   * current lightness.
   */
  def lightenBy(lightness: Double) = {
    val original = this.toHSLA
    original.copy(l = Normalized.clip(original.l.get * (1 + lightness)))
  }

  /**
   * Darken the color by the given *relative* amount. For example, calling
   * `aColor.darkenBy(0.1.normalized` decreases the lightness by 10% of the
   * current lightness.
   */
  def darkenBy(darkness: Double) = {
    val original = this.toHSLA
    original.copy(l = Normalized.clip(original.l.get * (1 - darkness)))
  }

  /** Increase the alpha channel by the given relative amount. */
  def fadeInBy(opacity: Double) = {
    val original = this.toHSLA
    original.copy(a = Normalized.clip(original.a.get * (1 + opacity)))
  }

  /** Decrease the alpha channel by the given relative amount. */
  def fadeOutBy(opacity: Double) = {
    val original = this.toHSLA
    original.copy(a = Normalized.clip(original.a.get * (1 - opacity)))
  }

  // Other -------------------------------------------------

  /** True if this is approximately equal to that */
  def ~=(that: Color): Boolean =
    (this.toRGBA, that.toRGBA) match {
      case (RGBA(r1, g1, b1, a1), RGBA(r2, g2, b2, a2)) =>
        Math.abs(r1 - r2) < 2 &&
          Math.abs(g1 - g2) < 2 &&
          Math.abs(b1 - b2) < 2 &&
          Math.abs(a1 - a2) < 0.1
    }

  def toCanvas: String =
    this match {
      case RGBA(r, g, b, a) =>
        s"rgba(${r.toCanvas}, ${g.toCanvas}, ${b.toCanvas}, ${a.toCanvas})"
      case HSLA(h, s, l, a) =>
        s"hsla(${h.toCanvas}, ${s.toPercentage}, ${l.toPercentage}, ${a.toCanvas})"
    }

  def toRGBDouble: Double =
    this match {
      case RGBA(r, g, b, a) =>
        r.get << 16 | g.get << 8 | b.get
      case HSLA(h, s, l, a) =>
        toRGBA.toRGBDouble
    }

  def toAwt: AwtColor =
    this match {
      case RGBA(r, g, b, a) =>
        val rgba = (a.get * 255).toInt << 24 | r.get << 16 | g.get << 8 | b.get
        new AwtColor(rgba, true)
      case HSLA(h, s, l, a) =>
        toRGBA.toAwt
    }

  def toHSLA: HSLA =
    this match {
      case RGBA(r, g, b, a) =>
        val rNormalized = r.toNormalized
        val gNormalized = g.toNormalized
        val bNormalized = b.toNormalized
        val cMax = rNormalized max gNormalized max bNormalized
        val cMin = rNormalized min gNormalized min bNormalized
        val delta = cMax - cMin

        val unnormalizedHue =
          if (cMax == rNormalized)
            60 * (((gNormalized - bNormalized) / delta))
          else if (cMax == gNormalized)
            60 * (((bNormalized - rNormalized) / delta) + 2)
          else
            60 * (((rNormalized - gNormalized) / delta) + 4)
        val hue = unnormalizedHue.degrees

        val lightness = Normalized.clip((cMax + cMin) / 2)

        val saturation =
          if (delta == 0.0)
            Normalized.MinValue
          else
            Normalized.clip(delta / (1 - Math.abs(2 * lightness.get - 1)))

        HSLA(hue, saturation, lightness, a)

      case HSLA(h, s, l, a) => HSLA(h, s, l, a)
    }

  def toRGBA: RGBA =
    this match {
      case RGBA(r, g, b, a) => RGBA(r, g, b, a)
      case HSLA(h, s, l, a) =>
        s.get match {
          case 0 =>
            val lightness = l.toUnsignedByte
            RGBA(lightness, lightness, lightness, a)
          case s =>
            def hueToRgb(p: Double, q: Double, t: Normalized): Normalized = {
              Normalized.wrap(t.get match {
                case t if t < 1.0 / 6.0 => p + (q - p) * 6 * t
                case t if t < 0.5       => q
                case t if t < 2.0 / 3.0 => p + (q - p) * (2.0 / 3.0 - t) * 6
                case t                  => p
              })
            }

            val lightness = l.get
            val q =
              if (lightness < 0.5)
                lightness * (1 + s)
              else
                lightness + s - (lightness * s)
            val p = 2 * lightness - q
            val r = hueToRgb(p, q, Normalized.wrap((h + 120.degrees).toTurns))
            val g = hueToRgb(p, q, Normalized.wrap(h.toTurns))
            val b = hueToRgb(p, q, Normalized.wrap((h - 120.degrees).toTurns))

            RGBA(r.toUnsignedByte, g.toUnsignedByte, b.toUnsignedByte, a)
        }
    }
}
final case class RGBA(r: UnsignedByte, g: UnsignedByte, b: UnsignedByte, a: Normalized) extends Color
final case class HSLA(h: Angle, s: Normalized, l: Normalized, a: Normalized) extends Color

object Color extends CommonColors {
  private def rgba(r: UnsignedByte, g: UnsignedByte, b: UnsignedByte, a: Normalized): Color =
    RGBA(r, g, b, a)

  // used by color constants
  private[doodle] def rgb(r: UnsignedByte, g: UnsignedByte, b: UnsignedByte): Color =
    rgba(r, g, b, 1.0.normalized)

  def rgba(red: Int, green: Int, blue: Int, opacity: Int): Color =
    rgba(red.uByte, green.uByte, blue.uByte, (opacity / 255.0).normalized)

  def rgb(red: Int, green: Int, blue: Int): Color =
    rgba(red, green, blue, 255)

  def hsla(hueAngle: Double, saturationFraction: Double, lightnessFraction: Double, opacityFraction: Double): Color =
    HSLA(Angle.degrees(hueAngle), saturationFraction.normalized, lightnessFraction.normalized, opacityFraction.normalized)

  def hsl(hueAngle: Double, saturationFraction: Double, lightnessFraction: Double): Color =
    hsla(hueAngle, saturationFraction, lightnessFraction, 1.0)

  def linearGradient(x1: Double, y1: Double, c1: AwtColor, x2: Double, y2: Double, c2: AwtColor, cyclic: Boolean = false): GradientPaint =
    new GradientPaint(x1.toFloat, y1.toFloat, c1, x2.toFloat, y2.toFloat, c2, cyclic)

  def radialMultipleGradient(x: Double, y: Double, radius: Double, distribution: collection.Seq[Double], colors: collection.Seq[AwtColor], cyclic: Boolean = false) = {
    val cycleMode = if (cyclic) MultipleGradientPaint.CycleMethod.REFLECT else MultipleGradientPaint.CycleMethod.NO_CYCLE
    val floatD = ArrayBuffer.empty[Float]; distribution.foreach { n => floatD.append(n.toFloat) }
    new RadialGradientPaint(x.toFloat, y.toFloat, radius.toFloat, floatD.toArray, colors.toArray, cycleMode)
  }

  def radialGradient(cx: Double, cy: Double, c1: AwtColor, radius: Double, c2: AwtColor, cyclic: Boolean = false) =
    radialMultipleGradient(cx, cy, radius, Seq(0, 1), Seq(c1, c2), cyclic)

  def linearMultipleGradient(x1: Double, y1: Double, x2: Double, y2: Double, distribution: collection.Seq[Double], colors: collection.Seq[AwtColor], cyclic: Boolean = false) = {
    val cycleMode = if (cyclic) MultipleGradientPaint.CycleMethod.REFLECT else MultipleGradientPaint.CycleMethod.NO_CYCLE
    val floatD = ArrayBuffer.empty[Float]; distribution.foreach { n => floatD.append(n.toFloat) }
    new LinearGradientPaint(x1.toFloat, y1.toFloat, x2.toFloat, y2.toFloat, floatD.toArray, colors.toArray, cycleMode)
  }

  def texture(file: String, x: Double, y: Double) = {
    val img = Utils.loadBufImage(file)
    new TexturePaint(img, new Rectangle2D.Double(x, y, img.getWidth, -img.getHeight))
  }

  def hex(rgbHex: Int) = new AwtColor(rgbHex, false)
  def hexa(argbHex: Int) = new AwtColor(argbHex, true)

  def hsluv(hueAngle: Double, saturationFraction: Double, lightnessFraction: Double): Color = {
    val rgbs = HUSLColorConverter.hsluvToRgb(Array(hueAngle, saturationFraction * 100, lightnessFraction * 100))
    rgb((rgbs(0) * 255).toInt, (rgbs(1) * 255).toInt, (rgbs(2) * 255).toInt)
  }
  def hsluva(hueAngle: Double, saturationFraction: Double, lightnessFraction: Double, opacityFraction: Double): Color = {
    val rgbs = HUSLColorConverter.hsluvToRgb(Array(hueAngle, saturationFraction * 100, lightnessFraction * 100))
    rgba((rgbs(0) * 255).toInt, (rgbs(1) * 255).toInt, (rgbs(2) * 255).toInt, (opacityFraction * 255).toInt)
  }

  private def hsbToHsl(h: Double, s: Double, v: Double) = {
    val hh = h
    var ll = (2 - s) * v
    val den = if (ll <= 1) ll else 2 - ll
    val ss = s * v / den
    ll /= 2
    (hh, ss, ll)
  }

  def hsb(hueAngle: Double, saturationFraction: Double, brightnessFraction: Double) = {
    val hslparts = hsbToHsl(hueAngle, saturationFraction, brightnessFraction)
    hsl(hslparts._1, hslparts._2, hslparts._3)
  }

  def hsba(hueAngle: Double, saturationFraction: Double, brightnessFraction: Double, opacityFraction: Double) = {
    val hslparts = hsbToHsl(hueAngle, saturationFraction, brightnessFraction)
    hsla(hslparts._1, hslparts._2, hslparts._3, opacityFraction)
  }

  def hsbuv(hueAngle: Double, saturationFraction: Double, brightnessFraction: Double) = {
    val hslparts = hsbToHsl(hueAngle, saturationFraction, brightnessFraction)
    hsluv(hslparts._1, hslparts._2, hslparts._3)
  }

  def hsbuva(hueAngle: Double, saturationFraction: Double, brightnessFraction: Double, opacityFraction: Double) = {
    val hslparts = hsbToHsl(hueAngle, saturationFraction, brightnessFraction)
    hsluva(hslparts._1, hslparts._2, hslparts._3, opacityFraction)
  }
}