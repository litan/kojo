package net.kogics.kojo.picture

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.Matchers
import org.scalatest.junit.JUnitRunner

import net.kogics.kojo.lite.NoOpKojoCtx
import net.kogics.kojo.lite.canvas.SpriteCanvas
import net.kogics.kojo.util.Vector2D

@RunWith(classOf[JUnitRunner])
class PictureBounceTest extends FunSuite with Matchers {
  val rg = new java.util.Random
  val kojoCtx = new NoOpKojoCtx
  implicit val spriteCanvas = new SpriteCanvas(kojoCtx)
  def picline(x1: Double, y1: Double, x2: Double, y2: Double) = Pic { t =>
    import t._
    jumpTo(x1, y1)
    moveTo(x2, y2)
  }

  test("bounce 1") {
    val obj = picline(0, 0, 100, 0)
    val pic = trans(50, -20) -> circle(20)
    obj.draw(); pic.draw();
    val v = Vector2D(0, 100)
    bouncePicVectorOffPic(pic, v, obj, rg) shouldBe Vector2D(0, -100)
  }
}