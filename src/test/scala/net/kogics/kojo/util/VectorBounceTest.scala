package net.kogics.kojo.util

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.Matchers
import org.scalatest.junit.JUnitRunner
import com.vividsolutions.jts.math.Vector2D

@RunWith(classOf[JUnitRunner])
class VectorBounceTest extends FunSuite with Matchers {

  test("bounce 1") {
    val v1 = Vector2D(100, 0)
    val v2 = Vector2D(0, 100)
    v2.bounceOff(v1) shouldBe Vector2D(0, -100)
  }

  test("bounce 2") {
    val v1 = Vector2D(100, 0)
    val v2 = Vector2D(0, -100)
    v2.bounceOff(v1) shouldBe Vector2D(0, 100)
  }

  test("bounce 3") {
    val v1 = Vector2D(0, 100)
    val v2 = Vector2D(100, 0)
    v2.bounceOff(v1) shouldBe Vector2D(-100, 0)
  }

  test("bounce 4") {
    val v1 = Vector2D(0, 100)
    val v2 = Vector2D(-100, 0)
    v2.bounceOff(v1) shouldBe Vector2D(100, 0)
  }

  test("bounce 5") {
    val v1 = Vector2D(50, 50)
    val v2 = Vector2D(-50, 0)
    v2.bounceOff(v1) shouldBe Vector2D(0, -50)
  }

  test("bounce 6") {
    val v1 = Vector2D(50, 50)
    val v2 = Vector2D(0, -50)
    v2.bounceOff(v1) shouldBe Vector2D(-50, 0)
  }

  test("bounce 7") {
    val v1 = Vector2D(50, 50)
    val v2 = Vector2D(50, 0)
    v2.bounceOff(v1) shouldBe Vector2D(0, 50)
  }

  test("bounce 8") {
    val v1 = Vector2D(50, 50)
    val v2 = Vector2D(0, 50)
    v2.bounceOff(v1) shouldBe Vector2D(50, 0)
  }

  test("bounce 9") {
    val v1 = Vector2D(50, 50)
    val v2 = Vector2D(50, 50)
    v2.bounceOff(v1) shouldBe Vector2D(50, 50)
  }
}