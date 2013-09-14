package net.kogics.kojo
package lite

import java.awt.{ Color => JColor }
import java.awt.{ Font => JFont }
import java.awt.GradientPaint
import java.awt.Paint
import java.util.concurrent.CountDownLatch

import scala.language.implicitConversions

import net.kogics.kojo.core.Rectangle
import net.kogics.kojo.core.TSCanvasFeatures
import net.kogics.kojo.util.Utils

trait CoreBuiltins {
  import scala.language.implicitConversions
  def TSCanvas: TSCanvasFeatures

  type Turtle = core.Turtle
  type Color = java.awt.Color
  type Font = java.awt.Font
  type Point = core.Point
  val Point = core.Point
  type PolyLine = kgeom.PolyLine
  val PolyLine = kgeom.PolyLine
  type Point2D = java.awt.geom.Point2D.Double
  def Point2D(x: Double, y: Double) = new java.awt.geom.Point2D.Double(x, y)

  val Random = new java.util.Random

  val blue = JColor.blue
  val red = JColor.red
  val yellow = JColor.yellow
  val green = JColor.green
  val orange = JColor.orange
  val purple = new Color(0x740f73)
  val pink = JColor.pink
  val brown = new Color(0x583a0b)
  val black = JColor.black
  val white = JColor.white
  val gray = JColor.gray
  val lightGray = JColor.lightGray
  val darkGray = JColor.darkGray
  val magenta = JColor.magenta
  val cyan = JColor.cyan

  val BoldFont = JFont.BOLD
  val PlainFont = JFont.PLAIN
  val ItalicFont = JFont.ITALIC

  val C = staging.KColor
  //  val Color = staging.KColor
  val noColor = C.noColor

  val Kc = new staging.KeyCodes

  implicit def seqToArrD(seq: Seq[Double]): Array[Double] = seq.toArray
  implicit def seqToArrI(seq: Seq[Int]): Array[Double] = seq.map { _.toDouble }.toArray
  val Kmath = new Kmath

  def epochTimeMillis = System.currentTimeMillis()
  def epochTime = System.currentTimeMillis() / 1000.0

  def random(upperBound: Int) = Random.nextInt(upperBound)
  def randomDouble(upperBound: Int) = Random.nextDouble * upperBound

  def color(r: Int, g: Int, b: Int) = new Color(r, g, b)
  def Color(r: Int, g: Int, b: Int) = new Color(r, g, b, 255)
  def Color(r: Int, g: Int, b: Int, a: Int) = new Color(r, g, b, a)
  def ColorG(x1: Double, y1: Double, c1: Color, x2: Double, y2: Double, c2: Color, cyclic: Boolean = false) = {
    new GradientPaint(x1.toFloat, y1.toFloat, c1, x2.toFloat, y2.toFloat, c2, cyclic)
  }
  def ColorHSB(h: Double, s: Double, b: Double) = java.awt.Color.getHSBColor((h / 360).toFloat, (s / 100).toFloat, (b / 100).toFloat)
  def pause(secs: Double) = Thread.sleep((secs * 1000).toLong)

  def clearOutput()
  def readln(prompt: String): String
  def setBackground(c: Paint)
  def Font(name: String, size: Int) = new Font(name, JFont.PLAIN, size)
  def Font(name: String, size: Int, style: Int) = new Font(name, style, size)
  def textExtent(text: String, fontSize: Int) = Utils.runInSwingThreadAndWait {
    val tnode = Utils.textNode(text, 0, 0, TSCanvas.camScale, fontSize)
    val b = tnode.getFullBounds
    new Rectangle(new Point(b.x, b.y), new Point(b.x + b.width, b.y + b.height))
  }
  def countDownLatch(n: Int) = new CountDownLatch(n)
  def homeDir = Utils.homeDir
  def currentDir = Utils.currentDir
  def installDir = Utils.installDir

  type Picture = core.Picture
  type HPics = picture.HPics
  val HPics = picture.HPics
  type VPics = picture.VPics
  val VPics = picture.VPics
  type GPics = picture.GPics
  val GPics = picture.GPics
  val trans = picture.trans _
  val rot = picture.rot _
  def scale(f: Double) = picture.scale(f)
  def scale(x: Double, y: Double) = picture.scale(x, y)
  def draw(pictures: Picture*) = pictures.foreach { _ draw () }
}
