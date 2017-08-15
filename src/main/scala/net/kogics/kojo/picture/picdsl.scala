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
  def on(other: PicDrawingDsl): PicDrawingDsl
  def below(other: PicDrawingDsl): PicDrawingDsl
}

case class DslImpl(pic: Picture) extends PicDrawingDsl {
  val drawnMsg = "Picture has already been drawn; drawing function '%s 'is not available."
  def colored(color: Paint): PicDrawingDsl = {
    pic.checkDraw(drawnMsg format "colored")
    DslImpl(Stroke(color)(pic))
  }
  def withWidth(th: Double): PicDrawingDsl = {
    pic.checkDraw(drawnMsg format "withWidth")
    DslImpl(StrokeWidth(th)(pic))
  }
  def filled(color: Paint): PicDrawingDsl = {
    pic.checkDraw(drawnMsg format "filled")
    DslImpl(Fill(color)(pic))
  }
  def translated(x: Double, y: Double): PicDrawingDsl = {
    pic.checkDraw(drawnMsg format "translated")
    DslImpl(Trans(x, y)(pic))
  }
  def on(other: PicDrawingDsl): PicDrawingDsl = {
    pic.checkDraw(drawnMsg format "on")
    DslImpl(GPics(other.pic, pic))
  }
  def below(other: PicDrawingDsl): PicDrawingDsl = {
    pic.checkDraw(drawnMsg format "below")
    DslImpl(GPics(pic, other.pic))
  }
}