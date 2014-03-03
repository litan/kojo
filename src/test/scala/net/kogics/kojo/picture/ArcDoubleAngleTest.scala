package net.kogics.kojo.picture

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.Matchers
import net.kogics.kojo.lite.NoOpKojoCtx
import net.kogics.kojo.lite.canvas.SpriteCanvas

import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ArcDoubleAngleTest extends FunSuite with Matchers {
  val kojoCtx = new NoOpKojoCtx
  implicit val spriteCanvas = new SpriteCanvas(kojoCtx)

  test("touching") {
    val arc1 = arc(100, 45.6)
    val line = Pic { t =>
      import t._
      moveTo(100 * math.cos(45.6.toRadians), 100 * math.sin(45.6.toRadians))
    } //> line: net.kogics.kojo.picture.Pic0 = Picture with Id: 197773
    arc1.draw(); line.draw()
    arc1.collidesWith(line) shouldBe true    
  }

  test("not touching") {
    val arc1 = arc(100, 45.59)
    val line = Pic { t =>
      import t._
      moveTo(100 * math.cos(45.6.toRadians), 100 * math.sin(45.6.toRadians))
    } //> line: net.kogics.kojo.picture.Pic0 = Picture with Id: 197773
    arc1.draw(); line.draw()
    arc1.collidesWith(line) shouldBe false    
  }

  test("overlapping") {
    val arc1 = arc(100, 45.7)
    val line = Pic { t =>
      import t._
      moveTo(100 * math.cos(45.6.toRadians), 100 * math.sin(45.6.toRadians))
    } //> line: net.kogics.kojo.picture.Pic0 = Picture with Id: 197773
    arc1.draw(); line.draw()
    arc1.collidesWith(line) shouldBe true    
  }

}