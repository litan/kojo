package net.kogics.kojo
package lite

import java.awt.{ Color => JColor }
import java.awt.{ Font => JFont }
import java.awt.image.BufferedImage
import java.awt.GraphicsEnvironment
import java.awt.Paint
import java.net.URL
import java.util.concurrent.CountDownLatch

import scala.language.implicitConversions
import scala.util.Random

import io.github.jdiemke.triangulation.Triangle2D
import net.kogics.kojo.core.Rectangle
import net.kogics.kojo.core.TSCanvasFeatures
import net.kogics.kojo.kmath.Rationals
import net.kogics.kojo.picture.PicCache
import net.kogics.kojo.turtle.LoTurtle
import net.kogics.kojo.util.PerlinNoiseImproved
import net.kogics.kojo.util.PerlinNoiseProcessing
import net.kogics.kojo.util.Utils

trait CoreBuiltins extends Rationals {
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
  val sRandom = new Random(Random)

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
  val darkGrayClassic = JColor.darkGray
  val magenta = JColor.magenta
  val cyan = JColor.cyan

  val BoldFont = JFont.BOLD
  val PlainFont = JFont.PLAIN
  val ItalicFont = JFont.ITALIC

  val C = staging.KColor

  val ColorMaker = doodle.Color
  val cm = doodle.Color
  implicit def rc2c(rc: doodle.Color): Color = rc.toAwt
  implicit def c2rc(c: Color): doodle.Color = Utils.awtColorToDoodleColor(c)
  implicit def rcSeq2cSeq(rcs: collection.Seq[doodle.Color]): collection.Seq[Color] = rcs.map(rc2c)

  //  val Color = staging.KColor
  val noColor = C.noColor

  val Kc = new staging.KeyCodes
  val MouseButton = new staging.MouseButtons
  val CapJoin = new staging.CapJoin

  val kmath = net.kogics.kojo.kmath.Kmath
  val mathx = kmath
  val easing = net.kogics.kojo.kmath.KEasing

  val slow = core.Slow
  val medium = core.Medium
  val fast = core.Fast
  val superFast = core.SuperFast

  type MMap[K, V] = collection.mutable.Map[K, V]
  type MSet[V] = collection.mutable.Set[V]
  type MSeq[V] = collection.mutable.Seq[V]

  val HashMap = collection.mutable.HashMap
  type HashMap[K, V] = collection.mutable.HashMap[K, V]

  val HashSet = collection.mutable.HashSet
  type HashSet[V] = collection.mutable.HashSet[V]

  val ArrayBuffer = collection.mutable.ArrayBuffer
  type ArrayBuffer[V] = collection.mutable.ArrayBuffer[V]

  def epochTimeMillis = System.currentTimeMillis()
  def epochTime = System.currentTimeMillis() / 1000.0

  def round(n: Number, digits: Int = 0): Double = {
    val factor = math.pow(10, digits).toDouble
    math.round(n.doubleValue * factor).toLong / factor
  }
  def lruCache[A, B](maxEntries: Int): collection.mutable.Map[A, B] = {
    import scala.jdk.CollectionConverters._
    new java.util.LinkedHashMap[A, B]() {
      override def removeEldestEntry(eldest: java.util.Map.Entry[A, B]) = size > maxEntries
    }.asScala
  }

  def setRandomSeed(seed: Long): Unit = { Random.setSeed(seed) }
  def random(upperBound: Int) = Random.nextInt(upperBound)
  def random(lowerBound: Int, upperBound: Int): Int = {
    if (lowerBound >= upperBound) lowerBound
    else
      lowerBound + random(upperBound - lowerBound)
  }
  def randomDouble(upperBound: Double): Double = {
    if ((upperBound == 0) || (upperBound != upperBound)) 0
    else
      Random.nextDouble * upperBound
  }
  def randomDouble(lowerBound: Double, upperBound: Double): Double = {
    if (lowerBound >= upperBound) lowerBound
    else
      lowerBound + randomDouble(upperBound - lowerBound)
  }

  def randomNormalDouble: Double = Random.nextGaussian()
  def randomNormalDouble(mean: Double, stdDev: Double): Double = randomNormalDouble * stdDev + mean
  def randomBoolean = Random.nextBoolean
  def randomInt = Random.nextInt
  def randomLong = Random.nextLong
  def randomFrom[T](seq: collection.Seq[T]) = seq(random(seq.length))
  def randomFrom[T](seq: collection.Seq[T], weights: Seq[Int]): T = randomFrom(seq, weights.map(_.toDouble))
  def randomFrom[T](seq: collection.Seq[T], weights: collection.mutable.Seq[Int]): T =
    randomFrom(seq, weights.map(_.toDouble))
  def randomFrom[T](seq: collection.Seq[T], weights: collection.Seq[Double]): T = {
    val sum = weights.sum
    val probabilities = if (Utils.doublesEqual(sum, 1.0, 1e-3)) {
      weights
    }
    else {
      weights.map { w => w / sum }
    }

    // sourced from:
    // https://stackoverflow.com/questions/24869304/scala-how-can-i-generate-numbers-according-to-an-expected-distribution
    val p = Random.nextDouble
    val it = seq.zip(probabilities).iterator
    var accum = 0.0
    while (it.hasNext) {
      val (item, itemProb) = it.next()
      accum += itemProb
      if (accum >= p)
        return item // return so that we don't have to search through the whole distribution
    }
    sys.error("this should never happen") // needed so it will compile
  }

  def randomColor = Color(random(256), random(256), random(256))
  def randomTransparentColor = Color(random(256), random(256), random(256), 100 + random(156))
  def shuffle[T](seq: collection.Seq[T]): collection.Seq[T] = sRandom.shuffle(seq)

  def initRandomGenerator(): Unit = {
    initRandomGenerator(System.currentTimeMillis())
  }

  def initRandomGenerator(seed: Long): Unit = {
    println(s"Random seed set to: ${seed}L")
    setRandomSeed(seed)
  }
  lazy val perlin = new PerlinNoiseProcessing()
  lazy val perlin2 = new PerlinNoiseImproved()
  def triangulate(points: collection.Seq[Point]): collection.Seq[Triangle2D] = {
    import scala.jdk.CollectionConverters._
    import io.github.jdiemke.triangulation.DelaunayTriangulator
    import io.github.jdiemke.triangulation.Vector2D

    val triangulator = new DelaunayTriangulator(points.map { p => new Vector2D(p.x, p.y) }.asJava)
    triangulator.triangulate()
    val tr = triangulator.getTriangles
    tr.asScala
  }

  @deprecated("Use Color instead", "2.7")
  def color(r: Int, g: Int, b: Int) = new Color(r, g, b)
  def Color(r: Int, g: Int, b: Int) = new Color(r, g, b, 255)
  def Color(r: Int, g: Int, b: Int, a: Int) = new Color(r, g, b, a)
  def Color(rgbHex: Int, hasAlpha: Boolean = false) = new Color(rgbHex, hasAlpha)
  def ColorG(x1: Double, y1: Double, c1: Color, x2: Double, y2: Double, c2: Color, cyclic: Boolean = false) =
    cm.linearGradient(x1, y1, c1, x2, y2, c2, cyclic)
  def ColorRadialG(
      x: Double,
      y: Double,
      radius: Double,
      distribution: collection.Seq[Double],
      colors: collection.Seq[Color],
      cyclic: Boolean = false
  ) =
    cm.radialMultipleGradient(x, y, radius, distribution, colors, cyclic)
  def ColorLinearG(
      x1: Double,
      y1: Double,
      x2: Double,
      y2: Double,
      distribution: collection.Seq[Double],
      colors: collection.Seq[Color],
      cyclic: Boolean = false
  ) =
    cm.linearMultipleGradient(x1, y1, x2, y2, distribution, colors, cyclic)
  def ColorHSB(h: Double, s: Double, b: Double) =
    java.awt.Color.getHSBColor((h / 360).toFloat, (s / 100).toFloat, (b / 100).toFloat)
  def pause(seconds: Double) = pauseMillis((seconds * 1000).toLong)
  def pauseMillis(milliSeconds: Long) = Thread.sleep(milliSeconds)

  def clearOutput(): Unit
  def readln(prompt: String): String
  def setBackground(c: Paint): Unit
  def Font(name: String, size: Int) = new Font(name, JFont.PLAIN, size)
  def Font(name: String, size: Int, style: Int) = new Font(name, style, size)
  def textExtent(text: String, fontSize: Int, fontName: String = null) = Utils.runInSwingThreadAndWait {
    val tnode = Utils.textNode(text, 0, 0, TSCanvas.camScale, fontSize, if (fontName == null) None else Some(fontName))
    val b = tnode.getFullBounds
    new Rectangle(new Point(b.x, b.y), new Point(b.x + b.width, b.y + b.height))
  }
  def countDownLatch(n: Int) = new CountDownLatch(n)
  def homeDir = Utils.homeDir
  def currentDir = Utils.currentDir
  def installDir = Utils.installDir
  def availableFontNames = GraphicsEnvironment.getLocalGraphicsEnvironment.getAvailableFontFamilyNames.toList

  type Picture = core.Picture
  type HPics = picture.HPics
  val HPics = picture.HPics
  type HPics2 = picture.HPics2
  val HPics2 = picture.HPics2
  type VPics = picture.VPics
  val VPics = picture.VPics
  type VPics2 = picture.VPics2
  val VPics2 = picture.VPics2
  type GPics = picture.GPics
  val GPics = picture.GPics
  type GPics2 = picture.GPics2
  val GPics2 = picture.GPics2
  type BatchPics = picture.BatchPics
  val BatchPics = picture.BatchPics
  type PicScreen = picture.PicScreen
  val trans = picture.trans _
  val rot = picture.rot _
  def id = picture.trans(0, 0)
  def scale(f: Double) = picture.scale(f)
  def scale(xf: Double, yf: Double) = picture.scale(xf, yf)
  def scalep(f: Double, x: Double, y: Double) = picture.scalep(f, x, y)
  def draw(pictures: Picture*): Unit = draw(pictures)
  protected[lite] def checkForLargeDrawing(): Unit = {
    if (PicCache.size > 60000) {
      println("There are too many pics in your drawing, and trying to draw them might freeze Kojo.")
      println("If you still want to go ahead with this, use the pic.draw() method.")
      println("Or use Picture.fromSketch(...).")
      assert(false, "Too many pics to draw - Kojo might freeze.")
    }
  }
  def draw(pictures: collection.Seq[Picture]): Unit = {
    checkForLargeDrawing()
    PicCache.freshPics(pictures).foreach { pic =>
      pic.invisible()
      pic.draw()
      pic.visible()
    }
  }
  type Image = java.awt.Image
  def image(height: Int, width: Int) = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
  def setImagePixel(image: BufferedImage, x: Int, y: Int, c: Color) = image.setRGB(x, image.getHeight - 1 - y, c.getRGB)
  def getImagePixel(image: BufferedImage, x: Int, y: Int) = Color(image.getRGB(x, image.getHeight - 1 - y), true)
  def image(fileName: String): BufferedImage = Utils.loadBufImage(fileName)
  def image(url: URL): BufferedImage = Utils.loadUrlImage(url)

  // For younger kids
  def clr() = { TSCanvas.clear(); TSCanvas.turtle0.invisible() }
  def nt: LoTurtle = nt(0, 0)
  def nt(x: Double = 0, y: Double = 0): LoTurtle = {
    nt(TSCanvas.newTurtle(x, y, "/images/blue-turtle32.png"))
  }
  def nt(t: core.Turtle): LoTurtle = new LoTurtle(t)
  def rpt(n: Int)(code: => LoTurtle): LoTurtle = {
    var i = 0
    var ret: LoTurtle = null
    while (i < n) {
      ret = code
      i += 1
    }
    ret
  }
}
