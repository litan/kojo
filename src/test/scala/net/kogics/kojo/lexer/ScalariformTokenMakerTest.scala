package net.kogics.kojo.lexer

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.ShouldMatchersForJUnit
import javax.swing.text.Segment
import scalariform.lexer.Token
import scalariform.lexer.Tokens.EQUALS
import scalariform.lexer.Tokens.INTEGER_LITERAL
import scalariform.lexer.Tokens.STRING_LITERAL
import scalariform.lexer.Tokens.VAL
import scalariform.lexer.Tokens.VARID
import scalariform.lexer.Tokens.WS
import scalariform.lexer.Tokens.XML_NAME
import scalariform.lexer.Tokens.XML_START_OPEN
import scalariform.lexer.Tokens.XML_TAG_CLOSE
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ScalariformTokenMakerTest extends FunSuite with ShouldMatchersForJUnit {

  val lexer = new ScalariformTokenMaker

  test("test single line val") {
    val code = "val x = 10"
    lexer.lexDoc(code)

    val codeChars = new Array[Char](code.length)
    code.getChars(0, code.length - 1, codeChars, 0)
    val segment = new Segment(codeChars, 0, code.length)

    val expectedTokens = List(
      Token(VAL, "val", 0, "val"),
      Token(WS, " ", 3, " "),
      Token(VARID, "x", 4, "x"),
      Token(WS, " ", 5, " "),
      Token(EQUALS, "=", 6, "="),
      Token(WS, " ", 7, " "),
      Token(INTEGER_LITERAL, "10", 8, "10")
    )

    lexer.tokensForLine(segment, 0) should be(expectedTokens)
  }

  test("test two line vals - line 2") {
    val line1 = "val x = 10"
    val line2 = "val y = 20"
    val code = """val x = 10
val y = 20
"""
    lexer.lexDoc(code)

    val codeChars = new Array[Char](code.length)
    code.getChars(0, code.length - 1, codeChars, 0)
    val segment = new Segment(codeChars, line1.length + 1, line2.length)

    val expectedTokens = List(
      Token(VAL, "val", 11, "val"),
      Token(WS, " ", 14, " "),
      Token(VARID, "y", 15, "y"),
      Token(WS, " ", 16, " "),
      Token(EQUALS, "=", 17, "="),
      Token(WS, " ", 18, " "),
      Token(INTEGER_LITERAL, "20", 19, "20")
    )

    lexer.tokensForLine(segment, line1.length + 1) should be(expectedTokens)
  }

  test("test multi line string - line 1") {
    val line1 = <a>val x = """</a>.text
    val code = <a>val x = """
abc
def</a>.text

    lexer.lexDoc(code)

    val codeChars = new Array[Char](code.length)
    code.getChars(0, code.length - 1, codeChars, 0)
    val segment = new Segment(codeChars, 0, line1.length)

    val expectedTokens = List(
      Token(VAL, "val", 0, "val"),
      Token(WS, " ", 3, " "),
      Token(VARID, "x", 4, "x"),
      Token(WS, " ", 5, " "),
      Token(EQUALS, "=", 6, "="),
      Token(WS, " ", 7, " "),
      Token(STRING_LITERAL, "\"\"\"", 8, "\"\"\"")
    )

    lexer.tokensForLine(segment, 0) should be(expectedTokens)
  }

  test("test multi line string - line 2") {
    val line1 = <a>val x = """</a>.text
    val line2 = "abc"
    val code = <a>val x = """
abc
def</a>.text

    lexer.lexDoc(code)

    val codeChars = new Array[Char](code.length)
    code.getChars(0, code.length - 1, codeChars, 0)
    val segment = new Segment(codeChars, line1.length + 1, line2.length)

    val expectedTokens = List(
      Token(STRING_LITERAL, "abc", 12, "abc")
    )

    //    println(lexer.tokensForLine(segment, 12))

    lexer.tokensForLine(segment, line1.length + 1) should be(expectedTokens)
  }

  test("test xml bug 1") {
    val line1 = "val x = "
    val line2 = " <level1>"
    val code = """val x = 
 <level1>
</level1>     
"""

    lexer.lexDoc(code)

    val codeChars = new Array[Char](code.length)
    code.getChars(0, code.length - 1, codeChars, 0)
    val segment = new Segment(codeChars, line1.length + 1, line2.length)

    val expectedTokens = List(
      Token(WS, " ", 9, " "),
      Token(XML_START_OPEN, "<", 10, "<"),
      Token(XML_NAME, "level1", 11, "level1"),
      Token(XML_TAG_CLOSE, ">", 17, ">")
    )

    lexer.tokensForLine(segment, line1.length + 1) should be(expectedTokens)
  }
}