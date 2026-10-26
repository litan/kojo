package net.kogics.kojo.lexer

import javax.swing.text.Segment

import org.fife.ui.rsyntaxtextarea.TokenTypes

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.Matchers
import org.scalatest.junit.JUnitRunner

import net.kogics.kojo.lite.i18n.{LanguageEditorData, LanguageTestSupport}

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

@RunWith(classOf[JUnitRunner])
class ScalariformTokenMakerTest extends FunSuite with Matchers {

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

  // The RSTA token type getTokenList assigns to the given lexeme on this line.
  private def rstaTypeOf(lexeme: String, code: String): Int = {
    lexer.lexDoc(code)
    val codeChars = new Array[Char](code.length)
    code.getChars(0, code.length, codeChars, 0)
    val segment = new Segment(codeChars, 0, code.length)
    var t = lexer.getTokenList(segment, TokenTypes.NULL, 0)
    var tpe = TokenTypes.NULL
    while (t != null && t.getType != TokenTypes.NULL) {
      if (t.getLexeme == lexeme) tpe = t.getType
      t = t.getNextToken
    }
    tpe
  }

  // A localized keyword (`dez` = Turkish `val`) is highlighted as a reserved
  // word when — and only when — its language is active. This exercises the
  // language-agnostic highlighting path (getTokenList -> registered keyword set),
  // which maps the identifier stock scalariform produces to RESERVED_WORD; it
  // does NOT need a keyword-patched scalariform on the classpath.
  test("a localized keyword is a reserved word only when its language is active") {
    val code = "dez x = 10"
    LanguageTestSupport.withData(LanguageEditorData()) {
      rstaTypeOf("dez", code) should be(TokenTypes.IDENTIFIER)
    }
    LanguageTestSupport.withData(LanguageTestSupport.turkishData) {
      rstaTypeOf("dez", code) should be(TokenTypes.RESERVED_WORD)
    }
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
