package net.kogics.kojo.lite

import java.awt.{Color, GraphicsEnvironment}
import java.io.{ByteArrayOutputStream, PrintStream}
import javax.swing.{JTextArea, SwingUtilities}

import net.kogics.kojo.lite.i18n.LanguageBehavior
import org.jmock.{Expectations, Mockery}
import org.jmock.Expectations.{any, returnValue}
import org.jmock.lib.legacy.ClassImposteriser
import org.junit.runner.RunWith
import org.scalatest.{FunSuite, Matchers}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class LanguageBehaviorInjectionTest extends FunSuite with Matchers {
  private class Fixture {
    val context = new Mockery
    context.setImposteriser(ClassImposteriser.INSTANCE)
    val execution = context.mock(classOf[CodeExecutionSupport])
    val kojoContext = context.mock(classOf[net.kogics.kojo.core.KojoCtx])
    context.checking(new Expectations {
      allowing(execution).kojoCtx; will(returnValue(kojoContext))
      allowing(kojoContext).baseFontSize; will(returnValue(14))
      allowing(kojoContext).screenDpiFontDelta; will(returnValue(0))
      allowing(kojoContext).fullScreenOutputAction(); will(returnValue(null))
      allowing(execution).enableClearButton()
    })
  }

  test("output and error presentation use an injected behavior without changing the selected behavior") {
    assume(!GraphicsEnvironment.isHeadless, "OutputPane requires a graphical toolkit")
    val fixture = new Fixture
    val selected = LanguageBehavior.current
    val behavior = new LanguageBehavior {
      override def updateResult(text: String): String = text.replace("source", "presented")
    }
    SwingUtilities.invokeAndWait(new Runnable {
      def run(): Unit = {
        val pane = new OutputPane(fixture.execution, behavior)
        pane.showOutputHelper("source", Color.black)
        pane.outputWindow.getText should be("presented")
        pane.appendError("source")
        pane.errorWindow.getDocument.getText(0, pane.errorWindow.getDocument.getLength) should include("presented")
        val defaultPane = new OutputPane(fixture.execution)
        defaultPane.showOutputHelper("source", Color.black)
        defaultPane.outputWindow.getText should be(selected.updateResult("source"))
      }
    })
    (LanguageBehavior.current eq selected) should be(true)
    fixture.context.assertIsSatisfied()
  }

  test("the editor uses its injected behavior for Show Type and passes it to its completion provider") {
    assume(!GraphicsEnvironment.isHeadless, "ScriptEditor requires a graphical toolkit")
    val fixture = new Fixture
    val source = new JTextArea("x")
    source.setCaretPosition(1)
    fixture.context.checking(new Expectations {
      allowing(fixture.execution).codePane; will(returnValue(source))
      allowing(fixture.execution).typeAt(1); will(returnValue("Int"))
      allowing(fixture.execution).imanip; will(returnValue(None))
      allowing(fixture.kojoContext).menuReady(`with`(any(classOf[javax.swing.JMenu])))
      allowing(fixture.kojoContext).showStatusCaretPos(1, 1)
    })
    val behavior = new LanguageBehavior {
      override def updateTypes(text: String): String = text.replace("Int", "PresentedInt")
    }
    SwingUtilities.invokeAndWait(new Runnable {
      def run(): Unit = {
        val manager = org.fife.ui.rsyntaxtextarea.RSyntaxTextArea.getCodeTemplateManager()
        val savedTemplates = manager.getTemplates
        val templatesEnabled = org.fife.ui.rsyntaxtextarea.RSyntaxTextArea.getTemplatesEnabled()
        val iconGroup = org.fife.ui.rtextarea.RTextArea.getIconGroup()
        try {
          val editor = new ScriptEditor(fixture.execution, null, behavior)
          editor.provider.proposal(0, "Int", editor.provider.TYPE, null).getInputText should be("PresentedInt")
          val output = new ByteArrayOutputStream
          val stream = new PrintStream(output, true, "UTF-8")
          try Console.withOut(stream) { editor.typeAtAction.actionPerformed(null) }
          finally stream.close()
          output.toString("UTF-8") should include("PresentedInt")
        }
        finally {
          manager.replaceTemplates(savedTemplates)
          org.fife.ui.rsyntaxtextarea.RSyntaxTextArea.setTemplatesEnabled(templatesEnabled)
          if (iconGroup != null) org.fife.ui.rtextarea.RTextArea.setIconGroup(iconGroup)
        }
      }
    })
    fixture.context.assertIsSatisfied()
  }
}
