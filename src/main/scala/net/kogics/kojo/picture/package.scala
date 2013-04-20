package net.kogics.kojo

import java.awt.Color
import java.awt.Paint
import java.awt.event.KeyEvent

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
  def stroke(color: Color) = Strokec(color)
  def strokeWidth(w: Double) = StrokeWidthc(w)
  def deco(painter: Painter) = Decoc(painter)

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

  def circle(r: Double)(implicit canvas: SCanvas) = Pic { t =>
    import t._
    penUp()
    right()
    forward(r)
    left()
    penDown()
    t.circle(r)
  }

  def arc(r: Double, angle: Int)(implicit canvas: SCanvas) = Pic { t =>
    import t._
    penUp()
    right()
    forward(r)
    left()
    penDown()
    t.arc(r, angle)
  }

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
      for (i <- 1 to 50) {
        forward(2 * math.Pi * r / 360 * 0.2)
        left(0.2)
      }
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
    val p = opac(-0.5) -> GPics(stroke(Color.black) -> prot(180), opac(-0.5) * stroke(Color.blue) -> arc(r / 4, 180))
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
