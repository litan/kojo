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

import java.awt.event.KeyEvent
import java.awt.geom.GeneralPath
import java.awt.image.BufferedImageOp
import java.awt.Color
import java.awt.Font
import java.awt.Image
import java.awt.Paint
import java.net.URL
import java.util.Random
import javax.swing.JComponent

import scala.swing.Graphics2D

import com.jhlabs.image.LightFilter
import com.jhlabs.image.LightFilter.Light
import com.vividsolutions.jts.geom.Coordinate
import com.vividsolutions.jts.geom.GeometryFactory
import com.vividsolutions.jts.geom.PrecisionModel
import core.Picture
import net.kogics.kojo.core.Cm
import net.kogics.kojo.core.Inch
import net.kogics.kojo.core.Pixel
import net.kogics.kojo.core.SCanvas
import net.kogics.kojo.picture.PicCache.freshPic
import net.kogics.kojo.util.Utils
import net.kogics.kojo.util.Vector2D

package object picture {
  type Painter = core.Painter
  val Gf = new GeometryFactory
  def rot(angle: Double) = Rotc(angle)
  def rotp(angle: Double, x: Double, y: Double) = Rotpc(angle, x, y)
  def scale(factor: Double) = Scalec(factor)
  def scalep(factor: Double, x: Double, y: Double) = PreDrawTransformc { pic =>
    pic.scaleAboutPoint(factor, x, y)
  }
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

  private[picture] def picLocalBounds(pic: Picture): Unit = Utils.runInSwingThread {
    import edu.umd.cs.piccolo.nodes.PPath
    val tnode = pic.tnode
    val b = tnode.getUnionOfChildrenBounds(null)
    b.add(tnode.getBoundsReference)
    val bRect = PPath.createRectangle(b.x.toFloat, b.y.toFloat, b.width.toFloat, b.height.toFloat)
    bRect.setPaint(null)
    tnode.addChild(bRect)
    tnode.repaint()
  }

  def bounds = PostDrawTransformc { pic =>
    picLocalBounds(pic)
  }
  def fill(color: Paint) = Fillc(color)
  def stroke(color: Paint) = Strokec(color)
  def strokeWidth(w: Double) = StrokeWidthc(w)
  def fade(n: Int) = Fadec(n)
  def blur(n: Int) = Blurc(n)
  def pointLight(x: Double, y: Double, direction: Double, elevation: Double, distance: Double) =
    PointLightc(x, y, direction, elevation, distance)
  def spotLight(x: Double, y: Double, direction: Double, elevation: Double, distance: Double) =
    SpotLightc(x, y, direction, elevation, distance)
  def distantLight(direction: Double, elevation: Double): LightFilter = {
    val lightFilter = new LightFilter()
    lightFilter.getLights.clear()
    val light = new lightFilter.DistantLight()
    light.setAzimuth(direction.toFloat.toRadians)
    light.setElevation(elevation.toFloat.toRadians)
    lightFilter.addLight(light)
    lightFilter
  }
  def lights(lights: Light*) = Lightsc(lights: _*)
  def noise(amount: Int, density: Double) = Noisec(amount, density)
  def weave(xWidth: Double, xGap: Double, yWidth: Double, yGap: Double) = Weavec(xWidth, xGap, yWidth, yGap)
  def effect(name: Symbol, props: Tuple2[Symbol, Any]*) = SomeEffectc(name, props: _*)
  def applyFilter(filter: BufferedImageOp) = ApplyFilterc(filter)

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

  def text(s0: Any, fontSize: Int, color: Color)(implicit canvas: SCanvas): TextPic =
    new TextPic(s0.toString, fontSize, color)
  def text(s0: Any, font: Font, color: Color)(implicit canvas: SCanvas): TextPic = {
    val ret = text(s0, 15, color)
    ret.setPenFont(font)
    ret
  }

  def rect(h: Double, w: Double)(implicit canvas: SCanvas) = Pic { t =>
    Utils.trect(h, w, t)
  }

  def vline(length: Double)(implicit canvas: SCanvas) = line(0, length)

  def hline(length: Double)(implicit canvas: SCanvas) = line(length, 0)

  def circle(r: Double)(implicit canvas: SCanvas) = new CirclePic(r)

  def ellipse(rx: Double, ry: Double)(implicit canvas: SCanvas) = new EllipsePic(rx, ry)

  def rect2(w: Double, h: Double)(implicit canvas: SCanvas) = new RectanglePic(w, h)

  def line(x: Double, y: Double)(implicit canvas: SCanvas) = new LinePic(x, y)

  def fromPath(path: => GeneralPath)(implicit canvas: SCanvas) = new PathPic(path)

  def arc(r: Double, angle: Double)(implicit canvas: SCanvas) = new ArcPic(r, angle)

  def fromJava2d(w: Double, h: Double, fn: Graphics2D => Unit)(implicit canvas: SCanvas) =
    new Java2DPic(w, h, fn)

  def fromJava2dDynamic(w: Double, h: Double, scaleOutFactor: Double, fn: Graphics2D => Unit, stopCheck: => Boolean)(
      implicit canvas: SCanvas
  ) =
    new Java2DPic(w * scaleOutFactor, h * scaleOutFactor, fn) {
      override def draw(): Unit = {
        super.draw()
        scale(1 / scaleOutFactor)
        canvas.animate {
          update()
          if (stopCheck) {
            canvas.stopAnimation()
          }
        }
      }
    }

  def image(file: String, envelope: Option[Picture])(implicit canvas: SCanvas) = new FileImagePic(file, envelope)
  def image(url: URL, envelope: Option[Picture])(implicit canvas: SCanvas) = new UrlImagePic(url, envelope)

  def image(img: Image, envelope: Option[Picture])(implicit canvas: SCanvas) = new ImagePic(img, envelope)

  def widget(swingComponent: JComponent)(implicit canvas: SCanvas) = new SwingPic(swingComponent)

  def effectablePic(pic: Picture)(implicit canvas: SCanvas) = new EffectableImagePic(pic)

  def protractor(camScale: Double)(implicit canvas: SCanvas) = {
    val r = 90 / camScale

    def line = Pic { t =>
      import t._
      right()
      forward(r)
    }

    def line2 = Pic { t =>
      import t._
      right()
      penUp()
      forward(r)
      penDown()
      forward(0.6 * r)
    }

    def slice = Pic { t =>
      import t._
      right()
      forward(r)
      left()
    }

    def prot(n: Int): Picture = {
      def angletext(n: Int) = stroke(Color.black) -> GPics(
        trans(1.25 * r, -6 * r / 100) * rot(180) -> text(180 - n, 10),
        trans(1.35 * r, 6 * r / 100) -> text(n, 10)
      )

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
      stroke(Color.gray) -> prot(180),
      fill(new Color(0, 0, 0, 0)) * stroke(Color.black) -> arc(r, 180),
      fill(new Color(0, 0, 0, 0)) * stroke(Color.gray) -> arc(1.3 * r, 180),
      fill(new Color(0, 0, 0, 0)) * stroke(Color.black) -> arc(1.6 * r, 180),
      opac(-0.5) * stroke(Color.blue) -> arc(r / 4, 180),
      fill(new Color(0, 0, 0, 0)) * stroke(Color.lightGray) -> line2,
      fill(new Color(0, 0, 0, 0)) * stroke(Color.lightGray) * rot(180) -> line2
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
    def line(): Unit = {
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

  private def addMouseHandlers(p: Picture): Unit = {
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
  lazy val pmodel = new PrecisionModel(14)
  def newCoordinate(x: Double, y: Double) = {
    val coord = new Coordinate(x, y)
    pmodel.makePrecise(coord)
    coord
  }

  def bounceVecOffStage(v: Vector2D, p: Picture)(implicit canvas: SCanvas): Vector2D = {
    import canvas._

    val topCollides = p.collidesWith(stageTop)
    val leftCollides = p.collidesWith(stageLeft)
    val botCollides = p.collidesWith(stageBot)
    val rightCollides = p.collidesWith(stageRight)

    val c = v.magnitude / math.sqrt(2)
    if (topCollides && leftCollides)
      Vector2D(c, -c)
    else if (topCollides && rightCollides)
      Vector2D(-c, -c)
    else if (botCollides && leftCollides)
      Vector2D(c, c)
    else if (botCollides && rightCollides)
      Vector2D(-c, c)
    else if (topCollides)
      Vector2D(v.x, -v.y)
    else if (botCollides)
      Vector2D(v.x, -v.y)
    else if (leftCollides)
      Vector2D(-v.x, v.y)
    else if (rightCollides)
      Vector2D(-v.x, v.y)
    else
      v
  }

  def bouncePicVectorOffPic(pic: Picture, vel: Vector2D, obstacle: Picture, rg: Random): Vector2D = {
    // returns points on the obstacle that contain the given collision coordinate
    def obstacleCollPoints(c: Coordinate): Option[Array[Coordinate]] = {
      obstacle.picGeom.getCoordinates.sliding(2).find { cs =>
        val xcheck =
          if (cs(0).x > cs(1).x)
            cs(0).x >= c.x && c.x >= cs(1).x
          else
            cs(0).x <= c.x && c.x <= cs(1).x

        val ycheck =
          if (cs(0).y > cs(1).y)
            cs(0).y >= c.y && c.y >= cs(1).y
          else
            cs(0).y <= c.y && c.y <= cs(1).y
        xcheck && ycheck
      }
    }
    // returns vector for obstacle boundary segment that contains the collision point
    def obstacleCollVector(c: Coordinate) = makeVectorFromCollPoints(obstacleCollPoints(c))

    // creates a vector out of two (collision) points
    def makeVectorFromCollPoints(cps: Option[Array[Coordinate]]) = cps match {
      case Some(cs) =>
        Vector2D(cs(0).x - cs(1).x, cs(0).y - cs(1).y)
      case None =>
        println("Warning: unable to determine collision vector; generating random vector")
        Vector2D(rg.nextDouble, rg.nextDouble)
    }

    def collisionVector = {
      val pt = obstacle.intersection(pic)
      val iCoords = pt.getCoordinates

      if (iCoords.length == 0) {
        Vector2D(rg.nextDouble, rg.nextDouble).normalize
      }
      else {
        if (iCoords.length == 1) {
          val cv1 = obstacleCollVector(iCoords(0))
          cv1.normalize
        }
        else {
          val c1 = iCoords(0)
          val c2 = iCoords(iCoords.length - 1)
          makeVectorFromCollPoints(Some(Array(c1, c2))).normalize
        }
      }
    }
    def pullbackCollision() = {
      val velNorm = vel.normalize
      val v2 = velNorm.rotate(180)
      val velMag = vel.magnitude
      var pulled = 0
      while (pic.collidesWith(obstacle) && pulled < velMag) {
        pic.offset(v2)
        pulled += 1
      }
      pic.offset(velNorm)
    }

    pullbackCollision()
    val cv = collisionVector
    vel.bounceOff(cv)
  }

  protected[picture] def epic(p: Picture) = p match {
    case ep: EffectablePicture => ep
    case _                     => new EffectableImagePic(freshPic(p))(p.canvas)
  }
}
