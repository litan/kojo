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
import javax.swing.event.DocumentEvent
import javax.swing.JTextArea

@RunWith(classOf[JUnitRunner])
class ScalariformTokenMakerTest2 extends FunSuite with ShouldMatchersForJUnit {

  def fixture = (new ScalariformTokenMaker, new JTextArea)

  test("test single line val") {
    val (lexer, pane) = fixture
    val code = "val x "
    val code2 = "= 10"
    lexer.lexDoc(code)

    val doc = pane.getDocument
    doc.insertString(0, code + code2, null)

    lexer.docListener.insertUpdate(new DocumentEvent {
      def getOffset = code.length
      def getLength = code2.length
      def getDocument = pane.getDocument
      def getChange(x$1: javax.swing.text.Element): javax.swing.event.DocumentEvent.ElementChange = ???
      def getType() = DocumentEvent.EventType.INSERT
    })

    val segment = new Segment()
    doc.getText(0, doc.getLength, segment)

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
    val (lexer, pane) = fixture
    val line1 = "val x = 10\n"
    val line2 = "val y = 20"
    lexer.lexDoc(line1)

    val doc = pane.getDocument
    doc.insertString(0, line1+line2, null)

    lexer.docListener.insertUpdate(new DocumentEvent {
      def getOffset = line1.length
      def getLength = line2.length
      def getDocument = pane.getDocument
      def getChange(x$1: javax.swing.text.Element): javax.swing.event.DocumentEvent.ElementChange = ???
      def getType() = DocumentEvent.EventType.INSERT
    })

    val segment = new Segment()
    doc.getText(line1.length, line2.length, segment)

    val expectedTokens = List(
      Token(VAL, "val", 11, "val"),
      Token(WS, " ", 14, " "),
      Token(VARID, "y", 15, "y"),
      Token(WS, " ", 16, " "),
      Token(EQUALS, "=", 17, "="),
      Token(WS, " ", 18, " "),
      Token(INTEGER_LITERAL, "20", 19, "20")
    )

    lexer.tokensForLine(segment, line1.length) should be(expectedTokens)
  }

  test("test multi line string - line 1") {
    val (lexer, pane) = fixture
    val line1 = <a>val x = """</a>.text
    val line2 = <a>
abc
def</a>.text

//    lexer.lexDoc(line2)

    val doc = pane.getDocument
    doc.insertString(0, line1+line2, null)

    lexer.docListener.insertUpdate(new DocumentEvent {
      def getOffset = 0
      def getLength = line1.length
      def getDocument = pane.getDocument
      def getChange(x$1: javax.swing.text.Element): javax.swing.event.DocumentEvent.ElementChange = ???
      def getType() = DocumentEvent.EventType.INSERT
    })
    
    val segment = new Segment()
    doc.getText(0, line1.length, segment)
    
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
    val (lexer, pane) = fixture
    val line1 = <a>val x = """</a>.text
    val line23 = <a>
abc
def</a>.text

    lexer.lexDoc(line1)

    val doc = pane.getDocument
    doc.insertString(0, line1+line23, null)

    lexer.docListener.insertUpdate(new DocumentEvent {
      def getOffset = line1.length
      def getLength = line23.length
      def getDocument = pane.getDocument
      def getChange(x$1: javax.swing.text.Element): javax.swing.event.DocumentEvent.ElementChange = ???
      def getType() = DocumentEvent.EventType.INSERT
    })
    
    val segment = new Segment()
    doc.getText(line1.length+1, 3, segment)
    
    val expectedTokens = List(
      Token(STRING_LITERAL, "abc", 12, "abc")
    )

    lexer.tokensForLine(segment, line1.length+1) should be(expectedTokens)
  }
}
