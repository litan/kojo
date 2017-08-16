package net.kogics.kojo.picture

import java.awt.Paint

import net.kogics.kojo.core.Picture

trait PicDrawingDsl {
  def pic: Picture
  def draw() {
    pic.draw()
  }
  def colored(color: Paint): PicDrawingDsl
  def withWidth(th: Double): PicDrawingDsl
  def filled(color: Paint): PicDrawingDsl
  def translated(x: Double, y: Double): PicDrawingDsl
  def rotated(angle: Double): PicDrawingDsl
  def scaled(fx: Double, fy: Double = 0): PicDrawingDsl
  def on(other: PicDrawingDsl): PicDrawingDsl
  def under(other: PicDrawingDsl): PicDrawingDsl
  def above(other: PicDrawingDsl): PicDrawingDsl
  def below(other: PicDrawingDsl): PicDrawingDsl
  def beside(other: PicDrawingDsl): PicDrawingDsl
  def opaced(f: Double): PicDrawingDsl
  def brightened(f: Double): PicDrawingDsl
  def hueued(f: Double): PicDrawingDsl
  def withAxes(): PicDrawingDsl
}

object PicCache {
  var hits = 0
  var misses = 0
  val seen = collection.mutable.HashSet.empty[Picture]
  def clear() {
    seen.clear()
    hits = 0
    misses = 0
  }
  def getPic(pic: Picture): Picture = {
    if (seen.contains(pic)) {
      val ret = pic.copy
      seen.add(ret)
      hits += 1
      ret
    }
    else {
      seen.add(pic)
      misses += 1
      pic
    }
  }
}

case class DslImpl(pic: Picture) extends PicDrawingDsl {
  import PicCache.getPic
  val drawnMsg = "Picture has already been drawn; drawing function '%s 'is not available."
  def colored(color: Paint): PicDrawingDsl = {
    pic.checkDraw(drawnMsg format "colored")
    DslImpl(Stroke(color)(getPic(pic)))
  }
  def withWidth(th: Double): PicDrawingDsl = {
    pic.checkDraw(drawnMsg format "withWidth")
    DslImpl(StrokeWidth(th)(getPic(pic)))
  }
  def filled(color: Paint): PicDrawingDsl = {
    pic.checkDraw(drawnMsg format "filled")
    DslImpl(Fill(color)(getPic(pic)))
  }
  def translated(x: Double, y: Double): PicDrawingDsl = {
    pic.checkDraw(drawnMsg format "translated")
    DslImpl(Trans(x, y)(getPic(pic)))
  }
  def rotated(angle: Double): PicDrawingDsl = {
    pic.checkDraw(drawnMsg format "rotated")
    DslImpl(Rot(angle)(getPic(pic)))
  }
  def scaled(fx: Double, fy: Double = 0): PicDrawingDsl = {
    val fy1 = if (fy == 0) fx else fy
    pic.checkDraw(drawnMsg format "scaled")
    DslImpl(ScaleXY(fx, fy1)(getPic(pic)))
  }
  def on(other: PicDrawingDsl): PicDrawingDsl = {
    pic.checkDraw(drawnMsg format "on")
    DslImpl(GPics(getPic(other.pic), getPic(pic)))
  }
  def under(other: PicDrawingDsl): PicDrawingDsl = {
    pic.checkDraw(drawnMsg format "under")
    DslImpl(GPics(getPic(pic), getPic(other.pic)))
  }
  def above(other: PicDrawingDsl): PicDrawingDsl = {
    pic.checkDraw(drawnMsg format "above")
    DslImpl(VPics(getPic(other.pic), getPic(pic)))
  }
  def below(other: PicDrawingDsl): PicDrawingDsl = {
    pic.checkDraw(drawnMsg format "below")
    DslImpl(VPics(getPic(pic), getPic(other.pic)))
  }
  def beside(other: PicDrawingDsl): PicDrawingDsl = {
    pic.checkDraw(drawnMsg format "beside")
    DslImpl(HPics(getPic(pic), getPic(other.pic)))
  }
  def opaced(f: Double): PicDrawingDsl = {
    pic.checkDraw(drawnMsg format "opaced")
    DslImpl(Opac(f)(getPic(pic)))
  }
  def brightened(f: Double): PicDrawingDsl = {
    pic.checkDraw(drawnMsg format "brightened")
    DslImpl(Brit(f)(getPic(pic)))
  }
  def hueued(f: Double): PicDrawingDsl = {
    pic.checkDraw(drawnMsg format "hueued")
    DslImpl(Hue(f)(getPic(pic)))
  }
  def withAxes(): PicDrawingDsl = {
    pic.checkDraw(drawnMsg format "withAxes")
    DslImpl(AxesOn(getPic(pic)))
  }

}