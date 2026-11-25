package net.kogics.kojo.core

import java.text.DecimalFormatSymbols
import java.util.Locale

import org.junit.runner.RunWith
import org.scalatest.{FunSuite, Matchers}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PointTest extends FunSuite with Matchers {
  test("point text uses the formatting locale's digits and decimal separator") {
    val previousLocale = Locale.getDefault(Locale.Category.FORMAT)
    val locales = List(Locale.US, new Locale("sv", "SE"), new Locale("tr", "TR"),
      new Locale("hi", "IN"), Locale.GERMANY)
    val examples = List(
      Point(40, 0) -> "Point(40.00 , 0.00)",
      Point(-12.346, 98.761) -> "Point(-12.35 , 98.76)",
      Point(-0.0, 0.0) -> "Point(-0.00 , 0.00)"
    )
    try {
      for (locale <- locales) {
        Locale.setDefault(Locale.Category.FORMAT, locale)
        val symbols = DecimalFormatSymbols.getInstance(locale)
        for ((point, expected) <- examples) {
          val localizedExpected = expected.map {
            case '.' => symbols.getDecimalSeparator
            case c if c >= '0' && c <= '9' => (symbols.getZeroDigit + c - '0').toChar
            case c => c
          }
          withClue(s"locale=$locale: ") { point.toString should be(localizedExpected) }
        }
      }
    }
    finally Locale.setDefault(Locale.Category.FORMAT, previousLocale)
  }
}
