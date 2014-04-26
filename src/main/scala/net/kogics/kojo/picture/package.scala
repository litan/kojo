/*
 * Copyright (C) 2011 Lalit Pant <pant.lalit@gmail.com>
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

import java.awt.Color
import java.awt.Image
import java.awt.Paint
import java.awt.event.KeyEvent

import javax.swing.JComponent

import com.jhlabs.image.LightFilter
import com.jhlabs.image.LightFilter.Light
import com.vividsolutions.jts.geom.GeometryFactory

import net.kogics.kojo.core.Cm
import net.kogics.kojo.core.Inch
import net.kogics.kojo.core.Pixel
import net.kogics.kojo.core.SCanvas
import net.kogics.kojo.util.Utils
import net.kogics.kojo.util.Vector2D

import core.Picture

package object picture {
  type Painter = core.Painter
  val Gf = new GeometryFactory
  def rot(angle: Double) = Rotc(angle)
  def rotp(angle: Double, x: Double, y: Double) = Rotpc(angle, x, y)
  def scale(factor: Double) = Scalec(factor)
  def scale(x: Double, y: Double) = ScaleXYc(x, y)
  def opac(f: Double) = Opacc(f)
  def hue(f: Double) = Huec(f)
  def sat(f: Double) = Satc(f)
  def brit(f: Double) = Britc(f)
  def trans(x: Double, y: Double) = Transc(x, y)
  def offset(x: Double, y: Double) = Offsetc(x, y)
  val flipX = FlipXc
  val flipY = FlipYc
  val axesOn = AxesOnc
  def fill(color: Paint) = Fillc(color)
  def stroke(color: Paint) = Strokec(color)
  def strokeWidth(w: Double) = StrokeWidthc(w)
  def deco(painter: Painter) = Decoc(painter)
  def fade(n: Int) = Fadec(n)
  def blur(n: Int) = Blurc(n)
  def pointLight(x: Double, y: Double, direction: Double, elevation: Double, distance: Double) = 
    PointLightc(x, y, direction, elevation, distance)
  def spotLight(x: Double, y: Double, direction: Double, elevation: Double, distance: Double) = 
    SpotLightc(x, y, direction, elevation, distance)
  def lights(lights: Light*) = Lightsc(lights: _*)
  def noise(amount: Int, density: Double) = Noisec(amount, density)
  def weave(xWidth: Double, xGap: Double, yWidth: Double, yGap: Double) = Weavec(xWidth, xGap, yWidth, yGap)
  def effect(name: Symbol, props: Tuple2[Symbol, Any]*) = SomeEffectc(name, props: _*)
  
  def PointLight(x: Double, y: Double, direction: Double, elevation: Double, distance: Double) = {
    val fltr = new LightFilter
    val light = new fltr.PointLight
    light.setCentreX(x.toFloat)
    light.setCentreY(y.toFloat)
    light.setAzimuth(direction.toRadians.toFloat)
    light.setElevation(elevation.toRadians.toFloat)
    light.setDistance(distance.toFloat)
    light.setConeAngle(30.toRadians)
    light.setFocus(0.5f)
    light
  }
  def SpotLight(x: Double, y: Double, direction: Double, elevation: Double, distance: Double) = {
    val fltr = new LightFilter
    val light = new fltr.SpotLight
    light.setCentreX(x.toFloat)
    light.setCentreY(y.toFloat)
    light.setAzimuth(direction.toRadians.toFloat)
    light.setElevation(elevation.toRadians.toFloat)
    light.setDistance(distance.toFloat)
    light.setConeAngle(30.toRadians)
    light.setFocus(0.5f)
    light
  }

  def spin(n: Int) = Spinc(n)
  def reflect(n: Int) = Reflectc(n)
  def row(p: Picture, n: Int) = {
    val lb = collection.mutable.ListBuffer[Picture]()
    for (i <- 1 to n) {
      lb += p.copy
    }
    HPics(lb.toList)
  }
  def col(p: => Picture, n: Int) = {
    val lb = collection.mutable.ListBuffer[Picture]()
    for (i <- 1 to n) {
      lb += p.copy
    }
    VPics(lb.toList)
  }

  def text(s0: Any, fontSize: Int = 15)(implicit canvas: SCanvas) = Pic { t =>
    import t._
    val s = s0.toString
    setPenFontSize(fontSize)
    write(s)
  }

  def rect(h: Double, w: Double)(implicit canvas: SCanvas) = Pic { t =>
    Utils.trect(h, w, t)
  }

  def vline(l: Double)(implicit canvas: SCanvas) = Pic { t =>
    import t._
    forward(l)
  }

  def hline(l: Double)(implicit canvas: SCanvas) = Pic { t =>
    import t._
    right()
    forward(l)
  }

  def circle(r: Double)(implicit canvas: SCanvas) = new CirclePic(r)

  def arc(r: Double, angle: Double)(implicit canvas: SCanvas) = new ArcPic(r, angle)

  def image(file: String)(implicit canvas: SCanvas) = new FileImagePic(file)

  def image(img: Image)(implicit canvas: SCanvas) = new ImagePic(img)

  def widget(swingComponent: JComponent)(implicit canvas: SCanvas) = new SwingPic(swingComponent)
  
  def effectablePic(pic: Picture)(implicit canvas: SCanvas) = new EffectableImagePic(pic)

  def protractor(camScale: Double)(implicit canvas: SCanvas) = {
    val r = 90 / camScale

    def line = Pic { t =>
      import t._
      right()
      penUp()
      forward(r / 4)
      penDown()
      forward(3 * r / 4)
    }

    def slice = Pic { t =>
      import t._
      right()
      penUp()
      forward(r / 4)
      penDown()
      forward(3 * r / 4)
      left()
    }

    def prot(n: Int): Picture = {
      def angletext(n: Int) = if (n >= 90) {
        trans(1.05 * r, 8 * r / 100) -> text(180 - n, 10)
      }
      else {
        trans(1.25 * r, -8 * r / 100) * rot(180) -> text(180 - n, 10)
      }

      if (n == 0) {
        GPics(
          line,
          angletext(n),
          cross(r / 10)
        )
      }
      else {
        GPics(
          slice,
          angletext(n),
          rot(10) -> prot(n - 10)
        )
      }
    }
    val p = opac(-0.5) -> GPics(
      stroke(Color.black) -> prot(180),
      fill(new Color(0, 0, 0, 0)) * stroke(Color.black) -> arc(r, 180),
      opac(-0.5) * stroke(Color.blue) -> arc(r / 4, 180)
    )
    addMouseHandlers(p)
    p
  }

  def ruler(camScale: Double)(implicit canvas: SCanvas) = {

    val bigTick = canvas.unitLen match {
      case Pixel => 18
      case Cm    => 18.0 / camScale
      case Inch  => 18.0 / camScale
    }
    val tickSpacing = canvas.unitLen match {
      case Pixel => 10
      case Cm    => 0.2
      case Inch  => 0.1
    }

    val sectionLen = tickSpacing * 5.0

    val scaleLen = canvas.unitLen match {
      case Pixel => 500
      case Cm    => 15
      case Inch  => 6
    }

    def section = Pic { t =>
      import t._
      forward(bigTick)
      for (i <- 1 to 5) {
        right()
        forward(tickSpacing)
        right()
        forward(bigTick / 3)
        hop(-bigTick / 3)
        right(180)
      }
    }

    def scale(len: Int) = {
      def sectionLabel(n: Double): String = {
        val labelnum = len - n + sectionLen
        canvas.unitLen match {
          case Pixel => labelnum.toInt.toString
          case Cm    => labelnum.toInt.toString
          case Inch  => labelnum.toString
        }
      }

      def sectionWithLabel(n: Double) =
        GPics(section, trans(4 * sectionLen / 5, 0) -> text(sectionLabel(n), 10))

      def side(n: Double): Picture = {
        if (n == sectionLen) {
          section
        }
        else {
          GPics(sectionWithLabel(n), trans(sectionLen, 0) -> side(n - sectionLen))
        }
      }

      GPics(
        trans(0, -bigTick) * opac(-0.5) * stroke(Color.black) -> GPics(
          side(len),
          trans(0, -sectionLen) * rot(180) * trans(-len, 0) -> side(len),
          trans(0, -sectionLen - bigTick) -> vline(sectionLen + 2 * bigTick),
          trans(len, -sectionLen - bigTick) -> vline(sectionLen + 2 * bigTick),
          trans(0, -sectionLen) * opac(-0.5) * stroke(Color.blue) -> rect(sectionLen, len),
          trans(0, -sectionLen - bigTick) * opac(-0.9) * stroke(Color.blue) -> rect(sectionLen + 2 * bigTick, len),
          trans(0, bigTick) * strokeWidth(1 / camScale) -> cross(bigTick / 2)
        )
      )
    }

    val p = scale(scaleLen)
    addMouseHandlers(p)
    p
  }

  private def cross(l: Double)(implicit canvas: SCanvas) = stroke(Color.blue) -> Pic { t =>
    import t._
    def line() {
      forward(l / 2)
      penUp()
      back(l)
      penDown()
      forward(l / 2)
    }
    line()
    right()
    line()
  }

  private def addMouseHandlers(p: Picture) {
    var (oldx, oldy) = (0.0, 0.0)
    var pressDelta = (0.0, 0.0)
    p.onMouseDrag { (x, y) =>
      if (staging.Inputs.isKeyPressed(KeyEvent.VK_SHIFT)) {
        val pos = p.position
        val v1 = Vector2D(oldx - pos.x, oldy - pos.y)
        val v2 = Vector2D(x - pos.x, y - pos.y)
        val angle2 = v1.angleTo(v2)
        p.rotate(angle2)
      }
      else {
        p.setPosition(x - pressDelta._1, y - pressDelta._2)
      }
      oldx = x
      oldy = y
    }
    p.onMousePress { (x, y) =>
      oldx = x
      oldy = y
      val pos = p.position
      pressDelta = (x - pos.x, y - pos.y)
    }

  }
}
