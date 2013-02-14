/*
 * Copyright (C) 2010 Peter Lewerin <peter.lewerin@tele2.se>
 *
 * The contents of this file are subject to the GNU General Public License
 * Version 3 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.gnu.org/copyleft/gpl.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 */

package net.kogics.kojo
package staging

import util.Math

import javax.swing._

import core._
import Impl.API

object ColorMaker {
  def apply(mode: GRAY)  = new GrayColorMaker(mode)
  def apply(mode: GRAYA) = new GrayAlphaColorMaker(mode)
  def apply(mode: RGB)   = new RgbColorMaker(mode)
  def apply(mode: RGBA)  = new RgbAlphaColorMaker(mode)
  def apply(mode: HSB)   = new HsbColorMaker(mode)

  def color(s: String) = s match {
    case ColorName(cc) => cc
    case "none"        => null
    case z             => java.awt.Color.decode(s)
  }
}

class ColorMaker {
  type Color = java.awt.Color
}

class GrayColorMaker(mode: GRAY) extends ColorMaker {
  def apply(v: Int) = {
    val vv = API.norm(v, 0, mode.v).toFloat
    new Color(vv, vv, vv)
  }
  def apply(v: Double) = {
    val vv = v.toFloat
    new Color(vv, vv, vv)
  }
}

class GrayAlphaColorMaker(mode: GRAYA) extends ColorMaker {
  def apply(v: Int, a: Int) = {
    val vv = API.norm(v, 0, mode.v).toFloat
    val aa = API.norm(a, 0, mode.a).toFloat
    new Color(vv, vv, vv, aa)
  }
  def apply(v: Double, a: Double) = {
    val vv = v.toFloat
    new Color(vv, vv, vv, a.toFloat)
  }
}

class RgbColorMaker(mode: RGB) extends ColorMaker {
  def apply(r: Int, g: Int, b: Int) = {
    val rr = API.norm(r, 0, mode.r).toFloat
    val gg = API.norm(g, 0, mode.g).toFloat
    val bb = API.norm(b, 0, mode.b).toFloat
    new Color(rr, gg, bb)
  }
  def apply(r: Double, g: Double, b: Double) = {
    new Color(r.toFloat, g.toFloat, b.toFloat)
  }
}

class RgbAlphaColorMaker(mode: RGBA) extends ColorMaker {
  def apply(r: Int, g: Int, b: Int, a: Int) = {
    val rr = API.norm(r, 0, mode.r).toFloat
    val gg = API.norm(g, 0, mode.g).toFloat
    val bb = API.norm(b, 0, mode.b).toFloat
    val aa = API.norm(a, 0, mode.a).toFloat
    new Color(rr, gg, bb, aa)
  }
  def apply(r: Double, g: Double, b: Double, a: Double) = {
    new Color(r.toFloat, g.toFloat, b.toFloat, a.toFloat)
  }
}

class HsbColorMaker(mode: HSB) extends ColorMaker {
  def apply(h: Int, s: Int, b: Int) = {
    val hh = API.norm(h, 0, mode.h).toFloat
    val ss = API.norm(s, 0, mode.s).toFloat
    val bb = API.norm(b, 0, mode.b).toFloat
    java.awt.Color.getHSBColor(hh, ss, bb)
  }
  def apply(h: Double, s: Double, b: Double) = {
    val hh = h.toFloat
    val ss = s.toFloat
    val bb = b.toFloat
    java.awt.Color.getHSBColor(hh, ss, bb)
  }
}

class RichColor (val c: java.awt.Color) {
  type Color = java.awt.Color
  def alpha = c.getAlpha
  def red = c.getRed
  def blue = c.getBlue
  def green = c.getGreen
  private def hsb =
    java.awt.Color.RGBtoHSB(c.getRed, c.getBlue, c.getGreen, null)
  def hue = {
    val h = math.floor(255 * (1 - this.hsb(0))) + 1
    if (h > 255) 0 else h.toInt
  }
  def saturation = (this.hsb(1) * 255).toInt
  def brightness = (this.hsb(2) * 255).toInt
  // TODO blendColor
}
object RichColor {
  def apply(c: java.awt.Color) = new RichColor(c)

  def lerpColor(from: RichColor, to: RichColor, amt: Double) = {
    require(amt >= 0d && amt <= 1d)
    new java.awt.Color(
      Math.lerp(from.red, to.red, amt).round.toInt,
      Math.lerp(from.green, to.green, amt).round.toInt,
      Math.lerp(from.blue, to.blue, amt).round.toInt
    )
  }
}
