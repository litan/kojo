package net.kogics.kojo.util

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.ShouldMatchersForJUnit
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class TerminalAnsiCodesTest extends FunSuite with ShouldMatchersForJUnit {
  import TerminalAnsiCodes._
  
  test ("test1") {
    val input = s"$ESC[32mGreen$ESC[0mNormal"
    val expected = Seq(("Green", AnsiGreen), ("Normal", NormalColor))
    val actual = TerminalAnsiCodes.parse(input)
    actual should be(expected)
  }
  
  test ("test2") {
    val input = s"$ESC[31mRed$ESC[32mGreen"
    val expected = Seq(("Red", AnsiRed), ("Green", AnsiGreen))
    val actual = TerminalAnsiCodes.parse(input)
    actual should be(expected)
  }
  
  test ("test3") {
    val input = s"$ESC[33mUnknown$ESC[32mGreen"
    val expected = Seq(("Unknown", NormalColor), ("Green", AnsiGreen))
    val actual = TerminalAnsiCodes.parse(input)
    actual should be(expected)
  }
  
  test ("test4") {
    val input = s"$ESC[32mGreen"
    val expected = Seq(("Green", AnsiGreen))
    val actual = TerminalAnsiCodes.parse(input)
    actual should be(expected)
  }
  
}