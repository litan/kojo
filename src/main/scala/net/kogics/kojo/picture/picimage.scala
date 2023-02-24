/*
 * Copyright (C) 2014 Lalit Pant <pant.lalit@gmail.com>
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

package net.kogics.kojo.picture

import java.awt.image.BufferedImage
import java.awt.image.BufferedImageOp
import java.awt.AlphaComposite
import java.awt.Color
import java.awt.GradientPaint
import java.awt.RenderingHints

import scala.collection.mutable.ArrayBuffer

import com.jhlabs.image.GaussianFilter
import com.jhlabs.image.LightFilter
import com.jhlabs.image.LightFilter.Light
import com.jhlabs.image.NoiseFilter
import com.jhlabs.image.WeaveFilter
import com.vividsolutions.jts.geom.Coordinate
import edu.umd.cs.piccolo.nodes.PImage
import edu.umd.cs.piccolo.util.PPaintContext
import edu.umd.cs.piccolo.PNode
import net.kogics.kojo.core.Picture
import net.kogics.kojo.core.SCanvas
import net.kogics.kojo.picture.PicCache.freshPic
import net.kogics.kojo.util.Utils

trait ImageOp {
  def filter(img: BufferedImage): BufferedImage
}

class FadeImageOp(n: Int) extends ImageOp {
  def filter(img: BufferedImage): BufferedImage = {
    val width = img.getWidth(null)
    val height = img.getHeight(null)
    val img2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    val g2 = img2.createGraphics()
    // draw initial image into new image
    g2.drawImage(img, 0, 0, null)
    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_IN))
    g2.setPaint(
      new GradientPaint(0, 0, new Color(0.0f, 0.0f, 0.0f, 1.0f), 0, n.toFloat, new Color(0.0f, 0.0f, 0.0f, 0.0f))
    )
    g2.fillRect(0, 0, width.toInt, n);
    g2.setPaint(new Color(0.0f, 0.0f, 0.0f, 0.0f))
    g2.fillRect(0, n, width.toInt, height - n);
    g2.dispose()
    img2
  }
}

class BlurImageOp(n: Int) extends ImageOp {
  def filter(img: BufferedImage): BufferedImage = {
    val fltr = new GaussianFilter(n.toFloat)
    fltr.filter(img, null)
  }
}

class PointLightImageOp(x: Double, y: Double, direction: Double, elevation: Double, distance: Double) extends ImageOp {
  def filter(img: BufferedImage): BufferedImage = {
    val fltr = new LightFilter
    val light = PointLight(x, y, direction, elevation, distance)
    fltr.setBumpShape(4)
    fltr.removeLight(fltr.getLights().get(0).asInstanceOf[Light])
    fltr.addLight(light)
    fltr.filter(img, null)
  }
}

class SpotLightImageOp(x: Double, y: Double, direction: Double, elevation: Double, distance: Double) extends ImageOp {
  def filter(img: BufferedImage): BufferedImage = {
    val fltr = new LightFilter
    val light = SpotLight(x, y, direction, elevation, distance)
    fltr.setBumpShape(4)
    fltr.removeLight(fltr.getLights().get(0).asInstanceOf[Light])
    fltr.addLight(light)
    fltr.filter(img, null)
  }
}

class LightsImageOp(lights: Light*) extends ImageOp {
  def filter(img: BufferedImage): BufferedImage = {
    val fltr = new LightFilter
    fltr.removeLight(fltr.getLights().get(0).asInstanceOf[Light])
    lights.foreach { fltr.addLight }
    fltr.filter(img, null)
  }
}

class NoiseImageOp(amount: Int, density: Double) extends ImageOp {
  def filter(img: BufferedImage): BufferedImage = {
    val fltr = new NoiseFilter()
    fltr.setAmount(amount)
    fltr.setDensity(density.toFloat)
    fltr.filter(img, null)
  }
}

class WeaveImageOp(xWidth: Double, xGap: Double, yWidth: Double, yGap: Double) extends ImageOp {
  def filter(img: BufferedImage): BufferedImage = {
    val fltr = new WeaveFilter()
    fltr.setXWidth(xWidth.toFloat)
    fltr.setXGap(xGap.toFloat)
    fltr.setYWidth(yWidth.toFloat)
    fltr.setYGap(yGap.toFloat)
    fltr.setUseImageColors(true)
    fltr.setRoundThreads(false)
    fltr.setShadeCrossings(true)
    fltr.filter(img, null)
  }
}

class SomeEffectImageOp(name0: Symbol, props: Tuple2[Symbol, Any]*) extends ImageOp {
  val name = name0.name
  def filter(img: BufferedImage): BufferedImage = {
    val cls = Class.forName(s"com.jhlabs.image.${name.head.toUpper}${name.tail}Filter")
    val fltr = cls.newInstance().asInstanceOf[BufferedImageOp]
    props.foreach { pv =>
      val prop0 = pv._1.name
      val prop = s"${prop0.head.toUpper}${prop0.tail}"
      val value = pv._2
      val valueClass = value match {
        case i: Int   => 1.getClass
        case f: Float => 1f.getClass
      }
      val method = cls.getMethod(s"set$prop", valueClass)
      val wrappedValue = value match {
        case i: Int   => java.lang.Integer.valueOf(i)
        case f: Float => java.lang.Float.valueOf(f)
      }
      method.invoke(fltr, wrappedValue)
    }
    fltr.filter(img, null)
  }
}

class ApplyFilterImageOp(filter: BufferedImageOp) extends ImageOp {
  override def filter(img: BufferedImage) = filter.filter(img, null)
}

trait EffectablePicture extends Picture {
  def fade(n: Int): Unit
  def blur(n: Int): Unit
  def pointLight(x: Double, y: Double, direction: Double, elevation: Double, distance: Double): Unit
  def spotLight(x: Double, y: Double, direction: Double, elevation: Double, distance: Double): Unit
  def lights(lights: Light*): Unit
  def noise(amount: Int, density: Double): Unit
  def weave(xWidth: Double, xGap: Double, yWidth: Double, yGap: Double): Unit
  def effect(name: Symbol, props: Tuple2[Symbol, Any]*): Unit
  def applyFilter(filter: BufferedImageOp): Unit
}

class EffectableImagePic(pic: Picture)(implicit val canvas: SCanvas)
    extends Picture
    with CorePicOps
    with CorePicOps2
    with TNodeCacher
    with RedrawStopper
    with NonVectorPicOps
    with EffectablePicture {
  @volatile var effects = Vector.empty[ImageOp]

  def makeTnode: edu.umd.cs.piccolo.PNode = Utils.runInSwingThreadAndPause {
    val node = new PNode
    node.setVisible(false)
    picLayer.addChild(node)
    node
  }

  override def initGeom(): com.vividsolutions.jts.geom.Geometry = {
    val cab = new ArrayBuffer[Coordinate]
    val b = tnode.getFullBounds
    cab += newCoordinate(b.x, b.y)
    cab += newCoordinate(b.x, b.y + b.height)
    cab += newCoordinate(b.x + b.width, b.y + b.height)
    cab += newCoordinate(b.x + b.width, b.y)
    cab += newCoordinate(b.x, b.y)
    pgTransform.getInverse.transform(Gf.createLineString(cab.toArray))
  }

  def pimage(img: BufferedImage) = {
    val inode: PImage = new PImage(img) {
      lazy val picWithEffects: BufferedImage = effects.foldLeft(img) { (imgt, op) => op.filter(imgt) }
      override def paint(paintContext: PPaintContext): Unit = {
        val finalImg = picWithEffects
        val g3 = paintContext.getGraphics()
        if (paintContext.getScale == 1.0) {
          g3.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR)
        }
        g3.drawImage(finalImg, 0, 0, null)
      }
    }
    inode
  }

  override def realDraw() = {
    pic.draw()
    Utils.runInSwingThread {
      picLayer.removeChild(pic.tnode)
      tnode.addChild(pimage(pic.toImage))
      tnode.translate(pic.bounds.x, pic.bounds.y)
//      if (!pic.isInstanceOf[ImagePic]) {
//        tnode.translate(-1, 0)
//      }
      tnode.setVisible(true)
      tnode.repaint()
    }
  }

  def copy: net.kogics.kojo.core.Picture = new EffectableImagePic(pic.copy)
  override def toString() = s"EffectableImagePic (Id: ${System.identityHashCode(this)}) -> ${pic.toString}"

  def fade(n: Int): Unit = {
    effects = new FadeImageOp(n) +: effects
  }
  def blur(n: Int): Unit = {
    effects = new BlurImageOp(n) +: effects
  }
  def pointLight(x: Double, y: Double, direction: Double, elevation: Double, distance: Double): Unit = {
    effects = new PointLightImageOp(x, y, direction, elevation, distance) +: effects
  }
  def spotLight(x: Double, y: Double, direction: Double, elevation: Double, distance: Double): Unit = {
    effects = new SpotLightImageOp(x, y, direction, elevation, distance) +: effects
  }
  def lights(lights: Light*): Unit = {
    effects = new LightsImageOp(lights: _*) +: effects
  }
  def noise(amount: Int, density: Double): Unit = {
    effects = new NoiseImageOp(amount, density) +: effects
  }
  def weave(xWidth: Double, xGap: Double, yWidth: Double, yGap: Double): Unit = {
    effects = new WeaveImageOp(xWidth, xGap, yWidth, yGap) +: effects
  }
  def effect(name: Symbol, props: Tuple2[Symbol, Any]*): Unit = {
    effects = new SomeEffectImageOp(name, props: _*) +: effects
  }
  def applyFilter(filter: BufferedImageOp): Unit = {
    effects = new ApplyFilterImageOp(filter) +: effects
  }
}

abstract class EffectableTransformer(pic: EffectablePicture) extends EffectablePicture with Transformer {
  val tpic = freshPic(pic).asInstanceOf[EffectablePicture]
  def fade(n: Int) = tpic.fade(n)
  def blur(n: Int) = tpic.blur(n)
  def pointLight(x: Double, y: Double, direction: Double, elevation: Double, distance: Double) =
    tpic.pointLight(x, y, direction, elevation, distance)
  def spotLight(x: Double, y: Double, direction: Double, elevation: Double, distance: Double) =
    tpic.spotLight(x, y, direction, elevation, distance)
  def lights(lights: Light*) = tpic.lights(lights: _*)
  def noise(amount: Int, density: Double) = tpic.noise(amount, density)
  def weave(xWidth: Double, xGap: Double, yWidth: Double, yGap: Double) = tpic.weave(xWidth, xGap, yWidth, yGap)
  def effect(name: Symbol, props: Tuple2[Symbol, Any]*) = tpic.effect(name, props: _*)
  def applyFilter(filter: BufferedImageOp) = tpic.applyFilter(filter)
}

case class Fade(n: Int)(pic: EffectablePicture) extends EffectableTransformer(pic) {
  def draw(): Unit = {
    tpic.fade(n)
    tpic.draw()
  }
  def copy = Fade(n)(pic.copy.asInstanceOf[EffectablePicture])
  override def toString() = s"Fade($n) (Id: ${System.identityHashCode(this)}) -> ${tpic.toString}"
}

case class Blur(n: Int)(pic: EffectablePicture) extends EffectableTransformer(pic) {
  def draw(): Unit = {
    tpic.blur(n)
    tpic.draw()
  }
  def copy = Blur(n)(pic.copy.asInstanceOf[EffectablePicture])
  override def toString() = s"Blur($n) (Id: ${System.identityHashCode(this)}) -> ${tpic.toString}"
}

case class PointLightEffect(x: Double, y: Double, direction: Double, elevation: Double, distance: Double)(
    pic: EffectablePicture
) extends EffectableTransformer(pic) {
  def draw(): Unit = {
    tpic.pointLight(x, y, direction, elevation, distance)
    tpic.draw()
  }
  def copy = PointLightEffect(x, y, direction, elevation, distance)(pic.copy.asInstanceOf[EffectablePicture])
  override def toString() =
    s"PointLightEffect($x, $y, $direction, $elevation, $distance) (Id: ${System.identityHashCode(this)}) -> ${tpic.toString}"
}

case class SpotLightEffect(x: Double, y: Double, direction: Double, elevation: Double, distance: Double)(
    pic: EffectablePicture
) extends EffectableTransformer(pic) {
  def draw(): Unit = {
    tpic.spotLight(x, y, direction, elevation, distance)
    tpic.draw()
  }
  def copy = SpotLightEffect(x, y, direction, elevation, distance)(pic.copy.asInstanceOf[EffectablePicture])
  override def toString() =
    s"SpotLightEffect($x, $y, $direction, $elevation, $distance) (Id: ${System.identityHashCode(this)}) -> ${tpic.toString}"
}

case class Lights(nlights: Light*)(pic: EffectablePicture) extends EffectableTransformer(pic) {
  def draw(): Unit = {
    tpic.lights(nlights: _*)
    tpic.draw()
  }
  def copy = Lights(nlights: _*)(pic.copy.asInstanceOf[EffectablePicture])
  override def toString() = s"Lights($nlights) (Id: ${System.identityHashCode(this)}) -> ${tpic.toString}"
}

case class Noise(amount: Int, density: Double)(pic: EffectablePicture) extends EffectableTransformer(pic) {
  def draw(): Unit = {
    tpic.noise(amount, density)
    tpic.draw()
  }
  def copy = Noise(amount, density)(pic.copy.asInstanceOf[EffectablePicture])
  override def toString() = s"Noise($amount, $density) (Id: ${System.identityHashCode(this)}) -> ${tpic.toString}"
}

case class Weave(xWidth: Double, xGap: Double, yWidth: Double, yGap: Double)(pic: EffectablePicture)
    extends EffectableTransformer(pic) {
  def draw(): Unit = {
    tpic.weave(xWidth, xGap, yWidth, yGap)
    tpic.draw()
  }
  def copy = Weave(xWidth, xGap, yWidth, yGap)(pic.copy.asInstanceOf[EffectablePicture])
  override def toString() =
    s"Weave($xWidth, $xGap, $yWidth, $yGap) (Id: ${System.identityHashCode(this)}) -> ${tpic.toString}"
}

case class SomeEffect(name: Symbol, props: Tuple2[Symbol, Any]*)(pic: EffectablePicture)
    extends EffectableTransformer(pic) {
  def draw(): Unit = {
    tpic.effect(name, props: _*)
    tpic.draw()
  }
  def copy = SomeEffect(name, props: _*)(pic.copy.asInstanceOf[EffectablePicture])
  override def toString() = s"Effect($name, $props) (Id: ${System.identityHashCode(this)}) -> ${tpic.toString}"
}

case class ApplyFilter(filter: BufferedImageOp)(pic: EffectablePicture) extends EffectableTransformer(pic) {
  def draw(): Unit = {
    tpic.applyFilter(filter)
    tpic.draw()
  }
  def copy = ApplyFilter(filter)(pic.copy.asInstanceOf[EffectablePicture])
  override def toString() = s"ApplyFilter($filter) (Id: ${System.identityHashCode(this)}) -> ${tpic.toString}"
}

abstract class ComposableImageEffect extends ComposableTransformer

case class Fadec(n: Int) extends ComposableImageEffect {
  def apply(p: Picture) = Fade(n)(epic(p))
}

case class Blurc(n: Int) extends ComposableImageEffect {
  def apply(p: Picture) = Blur(n)(epic(p))
}

case class PointLightc(x: Double, y: Double, direction: Double, elevation: Double, distance: Double)
    extends ComposableImageEffect {
  def apply(p: Picture) = PointLightEffect(x, y, direction, elevation, distance)(epic(p))
}

case class SpotLightc(x: Double, y: Double, direction: Double, elevation: Double, distance: Double)
    extends ComposableImageEffect {
  def apply(p: Picture) = SpotLightEffect(x, y, direction, elevation, distance)(epic(p))
}

case class Lightsc(lights: Light*) extends ComposableImageEffect {
  def apply(p: Picture) = Lights(lights: _*)(epic(p))
}

case class Noisec(amount: Int, density: Double) extends ComposableImageEffect {
  def apply(p: Picture) = Noise(amount, density)(epic(p))
}

case class Weavec(xWidth: Double, xGap: Double, yWidth: Double, yGap: Double) extends ComposableImageEffect {
  def apply(p: Picture) = Weave(xWidth, xGap, yWidth, yGap)(epic(p))
}

case class SomeEffectc(name: Symbol, props: Tuple2[Symbol, Any]*) extends ComposableImageEffect {
  def apply(p: Picture) = SomeEffect(name, props: _*)(epic(p))
}

case class ApplyFilterc(filter: BufferedImageOp) extends ComposableImageEffect {
  def apply(p: Picture) = ApplyFilter(filter)(epic(p))
}
