/*
 * Copyright (C) 2012 Lalit Pant <pant.lalit@gmail.com>
 *
 * The contents of this file are subject to the GNU General Public License
 * Version 3 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.gnu.org/copyleft/gpl.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 */

package net.kogics.kojo.lexer

import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.text.Segment

import scala.collection.mutable.ArrayBuffer

import org.fife.ui.rsyntaxtextarea.AbstractTokenMaker
import org.fife.ui.rsyntaxtextarea.Token
import org.fife.ui.rsyntaxtextarea.TokenMap
import org.fife.ui.rsyntaxtextarea.TokenTypes
import scalariform.lexer.{ Token => SfToken }
import scalariform.lexer.ScalaLexer
import scalariform.lexer.TokenType
import scalariform.lexer.Tokens
import scalariform.ScalaVersions

class ScalariformTokenMaker extends AbstractTokenMaker {

  def rawTokenise(s: String) = {
    ScalaLexer.createRawLexer(s, true, ScalaVersions.DEFAULT_VERSION).toArray
  }

  var docTokens = rawTokenise("")
  var debugOn = false
  var timingOn = false
  var prevRemove = false
  val docListener = makeDocListener

  override def getWordsToHighlight: TokenMap = wordsToHighlight
  override def getCurlyBracesDenoteCodeBlocks(n: Int) = true

  override def getMarkOccurrencesOfTokenType(tpe: Int) =
    tpe == TokenTypes.IDENTIFIER || tpe == TokenTypes.FUNCTION

  override def getShouldIndentNextLineAfter(t: Token) = {
    if (t != null) {
      if (t.length == 1) {
        val ch = t.charAt(0)
        ch == '{' || ch == '(' || ch == '='
      }
      else if (t.length == 2) {
        t.charAt(0) == '=' && t.charAt(1) == '>'
      }
      else {
        false
      }
    }
    else {
      false
    }
  }

  override def getLineCommentStartAndEnd(n: Int) = Array("//", null)

  override def getLastTokenTypeOnLine(text: Segment, initialTokenType: Int) = {
    TokenTypes.NULL
  }

  override def getTokenList(segment: Segment, initialTokenType: Int, docOffset: Int): Token = {
    def addRstaToken(t: SfToken): Unit = {
      debug("  %s".format(t))
      // offset of token within its segment = offset in doc - offset of segment within doc
      val segStartOffset = t.offset - (docOffset - segment.offset)
      val segEndOffset = segStartOffset + t.length - 1
      val docStartOffset = t.offset
      addToken(segment.array, segStartOffset, segEndOffset, convertTokenType(t.tokenType), docStartOffset)
    }

    debug(
      "\n---\nGetting tokens for Line. Doc Offset: %d, Seg Offset: %d, Length: %d".format(
        docOffset,
        segment.offset,
        segment.length
      )
    )
    debug("Line Text:" + segment.toString)

    resetTokenList()
    tokensForLine(segment, docOffset).foreach { addRstaToken }

    firstToken
  }

  def tokensForLine(segment: Segment, segmentOffset: Int) = {
    import collection.IndexedSeq
    def isLastMultiline(ts: IndexedSeq[SfToken]) = ts match {
      case IndexedSeq() => false
      case ts2 =>
        val t = ts2.last
        if (t.rawText.contains("\n")) true else false
    }

    def splitLastInactive(t: SfToken): Option[SfToken] = {
      val delta = segmentOffset - t.offset
      val upper = if (t.length > delta + segment.count) delta + segment.count else t.length
      val text = t.rawText.slice(delta, upper)
      if (text.length > 0)
        Some(SfToken(t.tokenType, text, t.offset + delta, text))
      else
        None
    }

    def splitLastActive(t: SfToken): Option[SfToken] = {
      val trim = segmentOffset + segment.count - t.offset
      val text = t.rawText.slice(0, trim)
      if (text.length > 0)
        Some(SfToken(t.tokenType, text, t.offset, text))
      else
        None
    }

    val (preInactive, rest) = docTokens.span { t => t.offset < segmentOffset }
    val (active, postInactive) = rest.span { t => t.offset <= segmentOffset + segment.length }

    val lineTokens = new ArrayBuffer[SfToken]

    if (isLastMultiline(preInactive)) {
      splitLastInactive(preInactive.last).foreach { lineTokens += _ }
    }

    if (isLastMultiline(active)) {
      val (active3, active4) = active.splitAt(active.length - 1)
      active3.foreach { lineTokens += _ }
      splitLastActive(active4(0)).foreach { lineTokens += _ }
    }
    else {
      active.foreach { lineTokens += _ }
    }

    if (lineTokens.isEmpty) {
      lineTokens += SfToken(Tokens.EOF, "", segmentOffset, "")
    }

    lineTokens.toArray
  }

  // Hooks called by RSTA on doc change
  override def onInsert(e: DocumentEvent) = docListener.insertUpdate(e)
  override def onRemove(e: DocumentEvent) = docListener.removeUpdate(e)

  def makeDocListener = new DocumentListener {
    import collection.IndexedSeq
    def insertUpdate(e: DocumentEvent): Unit = {
      def changeInToken(offset: Int, len: Int, t: SfToken) = scalariform.utils.Range(offset, len).intersects(t.range)

      val (offset, len) = (e.getOffset, e.getLength)
      val doc = e.getDocument
      val insertPrefix = doc.getText(offset, 1)

      val fullScanNeeded = {
        if (prevRemove) {
          prevRemove = false
          true
        }
        else if (insertPrefix == "\n" || insertPrefix == "\r") {
          true
        }
        else {
          false
        }
      }

      if (fullScanNeeded) {
        lexDoc(doc.getText(0, doc.getLength))
      }
      else {
        updateDiagnosticFlags()
        val t0 = System.currentTimeMillis()
        val preInactive = docTokens.takeWhile(!changeInToken(offset, 1, _))
        val active = docTokens.slice(preInactive.length, docTokens.length).takeWhile(changeInToken(offset, 1, _))
        val postInactive = docTokens.slice(preInactive.length + active.length, docTokens.length)
        val newPostInactive = postInactive.map { t => t.copy(offset = t.offset + len) }

        def wsWithNl(x: SfToken) = x.tokenType == Tokens.WS && x.rawText.contains("\n")

        @annotation.tailrec
        def seekPrevLineBreak(ts: IndexedSeq[SfToken], dropped: Int): (Int, Int) = ts match {
          case IndexedSeq() => (0, dropped)
          case x +: xs =>
            if (wsWithNl(x)) (x.offset, dropped) else seekPrevLineBreak(xs, dropped + 1)
        }

        @annotation.tailrec
        def seekNextLineBreak(ts: IndexedSeq[SfToken], dropped: Int): (Int, Int) = ts match {
          case IndexedSeq() => (doc.getLength, dropped)
          case x +: xs =>
            if (wsWithNl(x)) (x.offset, dropped) else seekNextLineBreak(xs, dropped + 1)
        }

        val (lower, dropped) = seekPrevLineBreak(preInactive.reverse, 1)
        val (upper, udropped) = seekNextLineBreak(newPostInactive, 0)
        val flen = upper - lower
        val docFragment = doc.getText(lower, flen)
        val newActive = rawTokenise(docFragment).map { t => t.copy(offset = t.offset + lower) }
        docTokens = preInactive.slice(0, preInactive.length - dropped) ++
          newActive.slice(0, newActive.length - 1) ++
          newPostInactive.slice(udropped, newPostInactive.length)
        showTiming(t0, "Incr")
      }
    }

    def removeUpdate(e: DocumentEvent): Unit = {
      val doc = e.getDocument.getText(0, e.getDocument.getLength)
      lexDoc(doc)
      prevRemove = true
    }

    def changedUpdate(e: DocumentEvent): Unit = {
      val doc = e.getDocument.getText(0, e.getDocument.getLength)
      lexDoc(doc)
    }
  }

  def lexDoc(doc: String): Unit = {
    updateDiagnosticFlags()
    val t0 = System.currentTimeMillis()
    docTokens = rawTokenise(doc)
    docTokens = docTokens.slice(0, docTokens.length - 1)
    showTiming(t0, "Full")
  }

  def convertTokenType(sfType: TokenType): Int = {
    if (Tokens.KEYWORDS.contains(sfType)) {
      TokenTypes.RESERVED_WORD
    }
    else if (Tokens.COMMENTS.contains(sfType)) {
      TokenTypes.COMMENT_MULTILINE
    }
    else {
      sfType match {
        case Tokens.WS                     => TokenTypes.WHITESPACE
        case Tokens.CHARACTER_LITERAL      => TokenTypes.LITERAL_CHAR
        case Tokens.INTEGER_LITERAL        => TokenTypes.LITERAL_NUMBER_DECIMAL_INT
        case Tokens.FLOATING_POINT_LITERAL => TokenTypes.LITERAL_NUMBER_FLOAT
        case Tokens.STRING_LITERAL         => TokenTypes.LITERAL_STRING_DOUBLE_QUOTE
        case Tokens.STRING_PART            => TokenTypes.LITERAL_STRING_DOUBLE_QUOTE
        case Tokens.SYMBOL_LITERAL         => TokenTypes.LITERAL_STRING_DOUBLE_QUOTE
        case Tokens.TRUE                   => TokenTypes.LITERAL_BOOLEAN
        case Tokens.FALSE                  => TokenTypes.LITERAL_BOOLEAN
        case Tokens.NULL                   => TokenTypes.LITERAL_CHAR
        case Tokens.EOF                    => TokenTypes.WHITESPACE
        case Tokens.LBRACE                 => TokenTypes.SEPARATOR
        case Tokens.RBRACE                 => TokenTypes.SEPARATOR
        case Tokens.LBRACKET               => TokenTypes.SEPARATOR
        case Tokens.RBRACKET               => TokenTypes.SEPARATOR
        case Tokens.LPAREN                 => TokenTypes.SEPARATOR
        case Tokens.RPAREN                 => TokenTypes.SEPARATOR

        case Tokens.XML_START_OPEN             => TokenTypes.MARKUP_TAG_DELIMITER
        case Tokens.XML_EMPTY_CLOSE            => TokenTypes.MARKUP_TAG_DELIMITER
        case Tokens.XML_TAG_CLOSE              => TokenTypes.MARKUP_TAG_DELIMITER
        case Tokens.XML_END_OPEN               => TokenTypes.MARKUP_TAG_DELIMITER
        case Tokens.XML_WHITESPACE             => TokenTypes.WHITESPACE
        case Tokens.XML_ATTR_EQ                => TokenTypes.MARKUP_TAG_ATTRIBUTE
        case Tokens.XML_ATTR_VALUE             => TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE
        case Tokens.XML_NAME                   => TokenTypes.MARKUP_TAG_NAME
        case Tokens.XML_PCDATA                 => TokenTypes.IDENTIFIER
        case Tokens.XML_COMMENT                => TokenTypes.COMMENT_MARKUP
        case Tokens.XML_CDATA                  => TokenTypes.MARKUP_CDATA
        case Tokens.XML_UNPARSED               => TokenTypes.MARKUP_CDATA
        case Tokens.XML_PROCESSING_INSTRUCTION => TokenTypes.MARKUP_PROCESSING_INSTRUCTION

        case _ => TokenTypes.IDENTIFIER
      }
    }
  }

  def debug(msg: => String): Unit = {
    if (debugOn) {
      println(msg)
    }
  }

  def showTiming(t0: Long, mod: String): Unit = {
    if (timingOn) {
      println("Doc lexing [%s] took: %2.4f secs".format(mod, (System.currentTimeMillis - t0) / 1000.0))
    }
  }

  def updateDiagnosticFlags(): Unit = {
    debugOn = if (System.getProperty("kojo.lexer.debug") == "true") true else false
    timingOn = if (System.getProperty("kojo.lexer.timing") == "true") true else false
  }
}
