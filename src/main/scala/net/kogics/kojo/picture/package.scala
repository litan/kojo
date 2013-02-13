package net.kogics.kojo

import java.awt.Color
import java.awt.Paint
import net.kogics.kojo.picture.AxesOnc
import net.kogics.kojo.picture.FlipXc
import net.kogics.kojo.picture.FlipYc
import core.Picture
import net.kogics.kojo.xscala.Builtins
import net.kogics.kojo.picture.Spinc
import net.kogics.kojo.picture.Reflectc
import net.kogics.kojo.core.SCanvas
import java.awt.event.KeyEvent
import net.kogics.kojo.util.Vector2D

package object picture {
  type Painter = core.Painter
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
  
  def protractor(camScale: Double) = {
    val r = 90 / camScale
    def num(n: Int) = Pic { t =>
      import t._
      setPenFontSize(10)
      write(n)
    }
    def cross = Pic { t =>
      import t._
      def line() {
        forward(r/20)
        penUp()
        back(r/10)
        penDown()
        forward(r/20)
      }
      line()
      right()
      line()
    }
    def line = Pic { t =>
      import t._
      right()
      penUp()
      forward(r/4)
      penDown()
      forward(3*r/4)
    }
    def slice = Pic { t =>
      import t._
      right()
      penUp()
      forward(r/4)
      penDown()
      forward(3*r/4)
      left()
      for (i <- 1 to 50) {
        forward(2 * math.Pi * r / 360 * 0.2)
        left(0.2)
      }
    }
    def prot(n: Int): Picture = {
      def angletext(n: Int) = if (n >= 90) {
        trans(1.05*r, 8*r/100) -> num(180-n)
      }
      else {
        trans(1.25*r, -8*r/100) * rot(180) -> num(180-n)
      }
    
      if (n == 0) {
        GPics(
          line,
          angletext(n),
          cross
        )
      }
      else {
        GPics(
          slice,
          angletext(n),
          rot(10) -> prot(n-10)
        )
      }
    }
    val p = opac(-0.5) * stroke(Color.black) -> prot(180)
    var (oldx, oldy) = (0.0, 0.0)
    p.onMouseDrag { (x, y) =>
      if (staging.Inputs.isKeyPressed(KeyEvent.VK_SHIFT)) {
        val pos = p.position
        val v1 = Vector2D(oldx - pos.x, oldy - pos.y)
        val v2 = Vector2D(x - pos.x, y - pos.y)
        val angle2 = v1.angleTo(v2)
        p.rotate(angle2)
      }
      else {
        p.setPosition(x, y)
      }
      oldx = x
      oldy = y
    }
    p.onMouseClick { (x, y) =>
      oldx = x
      oldy = y
    }
    p
  }
}
