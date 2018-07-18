package net.kogics.kojo.picture

import java.awt.Paint

import net.kogics.kojo.core.Picture

trait PicDrawingDsl {
  def pic: Picture
  def draw() {
    pic.draw()
  }
  def outlined(color: Paint): PicDrawingDsl
  def withWidth(th: Double): PicDrawingDsl
  def filled(color: Paint): PicDrawingDsl
  def translated(x: Double, y: Double): PicDrawingDsl
  def rotated(angle: Double): PicDrawingDsl
  //  def rotatedXY(angle: Double, x: Double, y: Double): PicDrawingDsl = {
  //    translated(-x, -y) rotated (angle) translated (x, y)
  //  }
  def rotatedXY(angle: Double, x: Double, y: Double): PicDrawingDsl
  def scaled(f: Double): PicDrawingDsl = scaledXY(f, f)
  def scaledXY(fx: Double, fy: Double = 0): PicDrawingDsl
  def on(other: PicDrawingDsl): PicDrawingDsl
  def on2(other: PicDrawingDsl): PicDrawingDsl
  def under(other: PicDrawingDsl): PicDrawingDsl
  def under2(other: PicDrawingDsl): PicDrawingDsl
  def above(other: PicDrawingDsl): PicDrawingDsl
  def above2(other: PicDrawingDsl): PicDrawingDsl
  def below(other: PicDrawingDsl): PicDrawingDsl
  def below2(other: PicDrawingDsl): PicDrawingDsl
  def beside(other: PicDrawingDsl): PicDrawingDsl
  def beside2(other: PicDrawingDsl): PicDrawingDsl
  def inRow(n: Int): PicDrawingDsl = {
    assert(n > 0, "Can't repeat less than 1 times")
    if (n == 1) this else this beside this.inRow(n - 1)
  }
  def inCol(n: Int): PicDrawingDsl = {
    assert(n > 0, "Can't repeat less than 1 times")
    if (n == 1) this else this below this.inRow(n - 1)
  }
  def opaced(f: Double): PicDrawingDsl
  def brightened(f: Double): PicDrawingDsl
  def hueued(f: Double): PicDrawingDsl
  def withAxes(): PicDrawingDsl
  def at(x: Double, y: Double): PicDrawingDsl
  def atRt(r: Double, theta: Double) = at(r * math.cos(theta.toRadians), r * math.sin(theta.toRadians))
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
  def deDup(ps: List[Picture]): List[Picture] = {
    ps map getPic
  }
}

case class DslImpl(pic: Picture) extends PicDrawingDsl {
  import PicCache.getPic
  val drawnMsg = "Picture has already been drawn; drawing function '%s 'is not available."
  def outlined(color: Paint): PicDrawingDsl = {
    pic.checkDraw(drawnMsg format "outlined")
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
  def rotatedXY(angle: Double, x: Double, y: Double): PicDrawingDsl = {
    pic.checkDraw(drawnMsg format "rotatedXY")
    DslImpl(Rotp(angle, x, y)(getPic(pic)))
  }
  def scaledXY(fx: Double, fy: Double = 0): PicDrawingDsl = {
    val fy1 = if (fy == 0) fx else fy
    pic.checkDraw(drawnMsg format "scaled")
    DslImpl(ScaleXY(fx, fy1)(getPic(pic)))
  }
  def on(other: PicDrawingDsl): PicDrawingDsl = {
    pic.checkDraw(drawnMsg format "on")
    DslImpl(GPics2(getPic(other.pic), getPic(pic)))
  }
  def on2(other: PicDrawingDsl): PicDrawingDsl = {
    pic.checkDraw(drawnMsg format "on2")
    DslImpl(GPics(getPic(other.pic), getPic(pic)))
  }
  def under(other: PicDrawingDsl): PicDrawingDsl = {
    pic.checkDraw(drawnMsg format "under")
    DslImpl(GPics2(getPic(pic), getPic(other.pic)))
  }
  def under2(other: PicDrawingDsl): PicDrawingDsl = {
    pic.checkDraw(drawnMsg format "under2")
    DslImpl(GPics(getPic(pic), getPic(other.pic)))
  }
  def above(other: PicDrawingDsl): PicDrawingDsl = {
    pic.checkDraw(drawnMsg format "above")
    DslImpl(VPics2(getPic(other.pic), getPic(pic)))
  }
  def above2(other: PicDrawingDsl): PicDrawingDsl = {
    pic.checkDraw(drawnMsg format "above2")
    DslImpl(VPics(getPic(other.pic), getPic(pic)))
  }
  def below(other: PicDrawingDsl): PicDrawingDsl = {
    pic.checkDraw(drawnMsg format "below")
    DslImpl(VPics2(getPic(pic), getPic(other.pic)))
  }
  def below2(other: PicDrawingDsl): PicDrawingDsl = {
    pic.checkDraw(drawnMsg format "below2")
    DslImpl(VPics(getPic(pic), getPic(other.pic)))
  }
  def beside(other: PicDrawingDsl): PicDrawingDsl = {
    pic.checkDraw(drawnMsg format "beside")
    DslImpl(HPics2(getPic(pic), getPic(other.pic)))
  }
  def beside2(other: PicDrawingDsl): PicDrawingDsl = {
    pic.checkDraw(drawnMsg format "beside2")
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
  def at(x: Double, y: Double): PicDrawingDsl = {
    pic.checkDraw(drawnMsg format "at")
    DslImpl(Position(x, y)(getPic(pic)))
  }
}