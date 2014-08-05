package net.kogics.kojo.kmath

import org.junit.runner.RunWith
import org.scalacheck.Prop.forAll
import org.scalacheck.Prop.propBoolean
import org.scalatest.FunSuite
import org.scalatest.Matchers
import org.scalatest.prop.Checkers
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class RationalTest extends FunSuite with Matchers with Rationals {

  test("addition") {
    r"2/3" + r"3/4" shouldBe r"17/12"
  }

  test("subtraction") {
    r"2/3" - r"3/4" shouldBe r"-1/12"
  }

  test("multiplication") {
    r"3/5" * r"4/6" shouldBe r"12/30"
  }

  test("division") {
    r"3/5" / r"4/6" shouldBe r"18/20"
  }

  test("commutative law") {
    val prop = forAll { (n1: Int, d1: Int, n2: Int, d2: Int) =>
      (d1 != 0 && d2 != 0) ==> {
        n1.r / d1 + n2.r / d2 == n2.r / d2 + n1.r / d1
      }
    }
    Checkers.check(prop)
  }

  test("associative law") {
    val prop = forAll { (n1: Int, d1: Int, n2: Int, d2: Int, n3: Int, d3: Int) =>
      (d1 != 0 && d2 != 0 && d3 != 0) ==> {
        (n1.r / d1 + n2.r / d2) + n3.r / d3 == n1.r / d1 + (n2.r / d2 + n3.r / d3)
      }
    }
    Checkers.check(prop)
  }

  test("distributive law") {
    val prop = forAll { (n1: Int, d1: Int, n2: Int, d2: Int, n3: Int, d3: Int) =>
      (d1 != 0 && d2 != 0 && d3 != 0) ==> {
        n1.r / d1 * (n2.r / d2 + n3.r / d3) == n1.r / d1 * n3.r / d3 + n1.r / d1 * n2.r / d2
      }
    }
    Checkers.check(prop)
  }
}