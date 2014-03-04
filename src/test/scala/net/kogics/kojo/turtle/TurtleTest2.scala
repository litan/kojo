package net.kogics.kojo.turtle

import org.junit.runner.RunWith
import org.scalacheck.Prop.forAll
import org.scalacheck.Prop.propBoolean
import org.scalatest.FunSuite
import org.scalatest.Matchers
import org.scalatest.prop.Checkers
import org.scalatest.junit.JUnitRunner
import net.kogics.kojo.lite.NoOpKojoCtx
import net.kogics.kojo.lite.canvas.SpriteCanvas
import net.kogics.kojo.util.Utils
import org.scalacheck.Gen
import org.scalatest.BeforeAndAfter

@RunWith(classOf[JUnitRunner])
class TurtleTest2 extends FunSuite with Matchers with BeforeAndAfter {
  val kojoCtx = new NoOpKojoCtx
  val spriteCanvas = new SpriteCanvas(kojoCtx)
  //  val spriteCanvas = new NoOpSCanvas
  val turtle = new Turtle(spriteCanvas, "/images/turtle32.png", 0, 0)
  def gen = Gen.choose(-1000.0, 1000.0)

  before {
    turtle.init()
    turtle.setAnimationDelay(0)
  }
  
  test("arc1") {
    turtle.setPosition(100, 50)
    turtle.arc(100, 45)
    val pos = turtle.position
    pos.x shouldBe 100 * math.cos(45.toRadians) +- 0.001
    pos.y shouldBe (50 + 100 * math.sin(45.toRadians)) +- 0.001
  }

  test("arcs") {
    val prop = forAll(gen, gen, gen) { (x: Double, y: Double, a: Double) =>
      (a > 20 && a < 1000 && a != 0) ==> {
        turtle.setPosition(x, y)
        turtle.setHeading(90)
        turtle.arc(100, a)
        val pos = turtle.position
        Utils.doublesEqual(x - (100 - 100 * math.cos(a.toDouble.toRadians)), pos.x, 0.001) &&
          Utils.doublesEqual(y + 100 * math.sin(a.toDouble.toRadians), pos.y, 0.001)
      }
    }
    Checkers.check(prop)
  }
}