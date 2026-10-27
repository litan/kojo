package net.kogics.kojo.lite

import java.awt.{Font, Rectangle, RenderingHints}
import java.awt.font.TextLayout
import java.awt.image.BufferedImage
import java.lang.reflect.InvocationTargetException
import javax.swing.{JFrame, SwingUtilities}
import net.kogics.kojo.lite.i18n.{LangInit, LanguageBehavior}
import net.kogics.kojo.util.Utils
import org.fife.ui.rsyntaxtextarea.{RSyntaxTextArea, RSyntaxTextAreaEditorKit, SyntaxConstants, TokenTypes}
import org.fife.ui.rtextarea.RTextArea
import org.jmock.{Expectations, Mockery}
import org.jmock.Expectations.returnValue
import org.jmock.lib.legacy.ClassImposteriser
import org.junit.runner.RunWith
import org.scalatest.{FunSuite, Matchers}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class HindiEditorFontTest extends FunSuite with Matchers {
  private def onEdt(body: => Unit): Unit = {
    try SwingUtilities.invokeAndWait(new Runnable {
      def run(): Unit = body
    })
    catch { case e: InvocationTargetException => throw e.getCause }
  }

  private def withEditor(language: String)(body: ScriptEditor => Unit): Unit =
    withEditor(language, LanguageBehavior.forLanguage(language))(body)

  private def withEditor(language: String, behavior: LanguageBehavior)(body: ScriptEditor => Unit): Unit = {
    val previousContext = Utils.kojoCtx
    val manager = RSyntaxTextArea.getCodeTemplateManager()
    val savedTemplates = manager.getTemplates
    val templatesEnabled = RSyntaxTextArea.getTemplatesEnabled()
    val iconGroup = RTextArea.getIconGroup()
    val context = new Mockery
    context.setImposteriser(ClassImposteriser.INSTANCE)
    val execution = context.mock(classOf[CodeExecutionSupport])
    val kojoContext = new NoOpKojoCtx {
      override def userLanguage: String = language
    }
    context.checking(new Expectations {
      allowing(execution).kojoCtx; will(returnValue(kojoContext))
      allowing(execution).imanip; will(returnValue(None))
    })
    val frame = new JFrame
    try {
      val editor = new ScriptEditor(execution, frame, behavior)
      editor.showInterpreterPane()
      frame.setContentPane(editor)
      frame.setUndecorated(true)
      frame.setFocusableWindowState(false)
      frame.setLocation(-10000, -10000)
      frame.setSize(820, 420)
      frame.setVisible(true)
      body(editor)
    }
    finally {
      frame.dispose()
      Utils.kojoCtx = previousContext
      manager.replaceTemplates(savedTemplates)
      RSyntaxTextArea.setTemplatesEnabled(templatesEnabled)
      if (iconGroup != null) RTextArea.setIconGroup(iconGroup)
    }
    context.assertIsSatisfied()
  }

  test("default and Turkish editor fonts and line metrics match the original initialization") {
    onEdt {
      for (language <- List("en", "hi");
          behavior <- List(LanguageBehavior.Default, LanguageBehavior.forLanguage("tr"))) {
        withEditor(language, behavior) { editor =>
          val expected = new RSyntaxTextArea(5, 80)
          expected.setSyntaxEditingStyle(editor.codePane.getSyntaxEditingStyle)
          Theme.currentTheme.loadEditorTheme.apply(expected)
          expected.setFont(expected.getFont.deriveFont(editor.getFont.getSize2D))
          val increase = new RSyntaxTextAreaEditorKit.IncreaseFontSizeAction
          for (_ <- 1 to 2) increase.actionPerformedImpl(null, expected)
          editor.codePanes.foreach { pane =>
            pane.getFont should be(expected.getFont)
            pane.getLineHeight should be(expected.getLineHeight)
            pane.getMaxAscent should be(expected.getMaxAscent)
            for (kind <- List(TokenTypes.IDENTIFIER, TokenTypes.RESERVED_WORD,
                TokenTypes.COMMENT_EOL, TokenTypes.LITERAL_STRING_DOUBLE_QUOTE)) {
              pane.getFontForTokenType(kind) should be(expected.getFontForTokenType(kind))
            }
          }
        }
      }
    }
  }

  test("an injected behavior controls fonts and padding independently of the context language") {
    onEdt {
      val selected = LanguageBehavior.current
      val requestedSizes = collection.mutable.ListBuffer.empty[Float]
      val customFont = new Font(Font.SERIF, Font.ITALIC, 20).deriveFont(20.5f)
      val behavior = new LanguageBehavior {
        override def editorFont(baseFont: Font, baseSize: Float): Font = {
          requestedSizes += baseSize
          customFont
        }
        override def editorLinePadding: Int = 3
      }
      withEditor("hi", behavior) { editor =>
        requestedSizes.toList should be(List.fill(2)(editor.getFont.getSize2D))
        val expected = new RSyntaxTextArea(5, 80)
        expected.setSyntaxEditingStyle(editor.codePane.getSyntaxEditingStyle)
        Theme.currentTheme.loadEditorTheme.apply(expected)
        expected.setFont(customFont)
        val increase = new RSyntaxTextAreaEditorKit.IncreaseFontSizeAction
        for (_ <- 1 to 2) increase.actionPerformedImpl(null, expected)
        editor.codePanes.foreach { pane =>
          pane.getFont should be(expected.getFont)
          pane.getLineHeight should be(expected.getLineHeight + 3)
          pane.getMaxAscent should be(expected.getMaxAscent)
        }
      }
      withEditor("en", LanguageBehavior.forLanguage("hi")) { editor =>
        editor.codePanes.foreach(checkMatras)
      }
      (LanguageBehavior.current eq selected) should be(true)
    }
  }

  private def checkMatras(pane: RSyntaxTextArea): Unit = {
    val image = new BufferedImage(800, 260, BufferedImage.TYPE_INT_ARGB)
    val graphics = image.createGraphics()
    try {
      graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
      for (kind <- List(TokenTypes.IDENTIFIER, TokenTypes.RESERVED_WORD,
          TokenTypes.COMMENT_EOL, TokenTypes.LITERAL_STRING_DOUBLE_QUOTE)) {
        val font = pane.getFontForTokenType(kind)
        font.getFamily should be(LangInit.fontForHindi.getFamily)
        font.canDisplayUpTo("किं कीं कुं कूँ कृं हैं हूँ val") should be(-1)
        val layout = new TextLayout("किं कीं कुं कूँ कृं हैं हूँ", font, graphics.getFontRenderContext)
        val ink = layout.getPixelBounds(null, 0, 0)
        val ascent = pane.getMaxAscent
        val lineHeight = pane.getLineHeight
        assert(-ink.y <= ascent && ink.getMaxY <= lineHeight - ascent,
          s"font=$font, ink=$ink, ascent=$ascent, lineHeight=$lineHeight")
      }
    }
    finally graphics.dispose()
  }

  test("Hindi fonts give both editors room for matras before and after zooming") {
    onEdt {
      var romanSize = 0.0f
      withEditor("en") { editor => romanSize = editor.codePane.getFont.getSize2D }
      withEditor("hi") { editor =>
        editor.codePanes.foreach { pane =>
          pane.getFont.getSize2D should be > romanSize
          checkMatras(pane)
        }
        for (_ <- 1 to 4) {
          editor.increaseFontSizeAction.actionPerformed(null)
          editor.codePanes.foreach(checkMatras)
        }
        for (_ <- 1 to 4) {
          editor.decreaseFontSizeAction.actionPerformed(null)
          editor.codePanes.foreach(checkMatras)
        }
      }
    }
  }

  private def paint(pane: RSyntaxTextArea, image: BufferedImage, clip: Rectangle): Unit = {
    val graphics = image.createGraphics()
    try {
      graphics.setClip(clip)
      pane.paint(graphics)
    }
    finally graphics.dispose()
  }

  test("adding a matra repaints completely within its line in both coloring modes") {
    onEdt {
      withEditor("hi") { editor =>
        for (pane <- editor.codePanes;
            style <- List(editor.SYNTAX_STYLE_SCALA2, SyntaxConstants.SYNTAX_STYLE_SCALA);
            size <- List(pane.getFont.getSize2D, 17.0f, 21.0f);
            matra <- List("िं", "ूँ", "ृ")) {
          pane.setSyntaxEditingStyle(style)
          pane.setFont(pane.getFont.deriveFont(size))
          pane.setHighlightCurrentLine(false)
          pane.setAnimateBracketMatching(false)
          pane.getCaret.setVisible(false)
          pane.setSize(720, 180)
          pane.setText("मान ऊपर = 1\nचर क = 2\nपरिभाषा नीचे() = 3\n")
          val complete = new Rectangle(0, 0, pane.getWidth, pane.getHeight)
          val partial = new BufferedImage(pane.getWidth, pane.getHeight, BufferedImage.TYPE_INT_ARGB)
          paint(pane, partial, complete)
          val line = pane.modelToView(pane.getLineStartOffset(1))
          val dirtyLine = new Rectangle(0, line.y, pane.getWidth, pane.getLineHeight)
          pane.getDocument.insertString(pane.getText.indexOf("क =") + 1, matra, null)
          paint(pane, partial, dirtyLine)
          val full = new BufferedImage(pane.getWidth, pane.getHeight, BufferedImage.TYPE_INT_ARGB)
          paint(pane, full, complete)
          val differences = (for {
            y <- 0 until pane.getHeight
            x <- 0 until pane.getWidth
            if partial.getRGB(x, y) != full.getRGB(x, y)
          } yield (x, y)).size
          withClue(s"style=$style, size=$size, matra=$matra: ") {
            differences should be(0)
          }
        }
      }
    }
  }
}
