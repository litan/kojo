package net.kogics.kojo.lite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FunSuite, Matchers}
import net.kogics.kojo.core.{CompletionInfo, MemberKind, TwMode}
import net.kogics.kojo.lite.i18n.{LanguageBehavior, LanguageEditorData, LanguageTestSupport}
import net.kogics.kojo.xscala.{CodeCompletionUtils, CodeTemplates, Help}
import org.fife.ui.autocomplete.{Completion, TemplateCompletion}
import org.fife.ui.rsyntaxtextarea.{CodeTemplateManager, RSyntaxDocument, RSyntaxTextArea, TokenTypes}
import org.fife.ui.rsyntaxtextarea.templates.StaticCodeTemplate
import org.jmock.{Expectations, Mockery}
import org.jmock.Expectations.returnValue
import org.jmock.lib.legacy.ClassImposteriser
import javax.swing.{JTextArea, SwingUtilities}
import scala.jdk.CollectionConverters._

@RunWith(classOf[JUnitRunner])
class KojoCompletionProviderTest extends FunSuite with Matchers {

  // Option B: shared instance for the whole suite
  private lazy val provider = new KojoCompletionProvider(null)

  test("provider queries shared keyword/snippet data and retains its injected behavior") {
    val context = new Mockery
    context.setImposteriser(ClassImposteriser.INSTANCE)
    val execution = context.mock(classOf[CodeExecutionSupport])
    val kojoContext = context.mock(classOf[net.kogics.kojo.core.KojoCtx])
    context.checking(new Expectations {
      allowing(execution).kojoCtx; will(returnValue(kojoContext))
      allowing(kojoContext).showAppWaitCursor()
      allowing(kojoContext).hideAppWaitCursor()
      allowing(execution).interpreterNameCompletions(Some("invented")); will(returnValue((Nil, 8)))
      allowing(execution).compilerCompletions(8, null, Some("invented")); will(returnValue((Nil, 8)))
      allowing(execution).keywordCompletions(Some("invented")); will(returnValue((List("invented"), 8)))
    })
    val behavior = new LanguageBehavior {
      override def updateTypes(text: String): String = text.replace("Int", "PresentedInt")
    }
    val p = new KojoCompletionProvider(execution, behavior)
    val data = LanguageEditorData(
      List("invented"), Map("invented" -> "invented(${saved})"),
      Map("invented_block" -> "invented_block(${cursor})\n", "test" -> "wrong"),
      Map(TwMode -> Map("invented" -> "keyword help"))
    )
    LanguageTestSupport.withData(data) {
      try {
        Help.activateTw()
        p.proposal(0, "Int", p.TYPE, null).getInputText should be("PresentedInt")
        p.completionPrefix = Some("invented")
        val editor = new JTextArea("invented")
        editor.setCaretPosition(8)
        val completions = p.complete(editor).asScala.map(_.asInstanceOf[TemplateCompletion])
        val keyword = completions.find(_.getInputText == "invented").get
        keyword.getSummary should be("keyword help")
        keyword.getInsertionInfo(new JTextArea(), false).getTextToInsert should be("invented(saved)")
        completions.find(_.getInputText == "invented_block").get.getSummary should be("invented_block(|)<br/>")
        val proposals = collection.mutable.ArrayBuffer.empty[Completion]
        p.addTemplateProposals(proposals, "test", 0)
        proposals.head.asInstanceOf[TemplateCompletion].getInsertionInfo(new JTextArea(), false)
          .getTextToInsert should include("testName")
      }
      finally Help.activateTw()
    }
    context.assertIsSatisfied()
  }

  test("RSTA expands base-winning and localized snippets and initially highlights both documents") {
    val data = LanguageEditorData(List("invented"), snippets = Map("test" -> "wrong", "invented_block" -> "before${cursor}after\n"))
    LanguageTestSupport.withData(data) {
      SwingUtilities.invokeAndWait(new Runnable {
        def run(): Unit = {
          val manager = new CodeTemplateManager
          CodeTemplates.templates.keys.foreach { name =>
            manager.addTemplate(new StaticCodeTemplate(name, CodeTemplates.beforeCursor(name), CodeTemplates.afterCursor(name)))
          }
          for ((trigger, expected) <- List("test" -> "testName", "invented_block" -> "beforeafter")) {
            val editor = new RSyntaxTextArea(trigger)
            editor.setCaretPosition(trigger.length)
            manager.getTemplate(editor).invoke(editor)
            editor.getText should include(expected)
          }
          for (_ <- 1 to 2) {
            val editor = new RSyntaxTextArea
            val lexer = new net.kogics.kojo.lexer.ScalariformTokenMaker
            editor.getDocument.asInstanceOf[RSyntaxDocument].setSyntaxStyle(lexer)
            lexer.lexDoc("invented x = 1")
            editor.setText("invented x = 1")
            editor.getDocument.asInstanceOf[RSyntaxDocument].getTokenListForLine(0).getType should be(TokenTypes.RESERVED_WORD)
          }
        }
      })
    }
  }

  test("owner recognition and plain-name fallback are independent and empty templates suppress fallback") {
    val info = CompletionInfo(MemberKind.Def, "example", "", "sample.Owner", 1, false, Nil, Nil, "Int", "")
    val language = System.getProperty("user.language")
    val savedHelp = Help.langContent.toMap
    val savedTemplates = CodeCompletionUtils.langTemplates.toMap
    try {
      Help.addContent(language, Map("example" -> "plain help"))
      CodeCompletionUtils.addTemplates(language, Map("example" -> "example(${plain})"))
      val recognizes = new LanguageBehavior {
        override def recognizesOwner(owner: String): Boolean = owner == "sample.Owner"
      }
      val recognized = new KojoCompletionProvider(null, recognizes).proposal2(0, info)
      recognized.getSummary should be(info.fullCompletion)
      recognized.getInsertionInfo(new JTextArea(), false).getTextToInsert should be("example")
      val fallback = new LanguageBehavior {
        override def recognizesOwner(owner: String): Boolean = owner == "sample.Owner"
        override def allowsPlainNameFallback(owner: String): Boolean = owner == "sample.Owner"
      }
      val p = new KojoCompletionProvider(null, fallback)
      p.proposal2(0, info).getSummary should be("plain help")
      CodeCompletionUtils.addTemplates(language, Map("Owner.example" -> ""))
      p.proposal2(0, info).getInsertionInfo(new JTextArea(), false).getTextToInsert should be("example")
      p.proposal2(0, info.copy(owner = null)).getSummary should be(info.copy(owner = null).fullCompletion)
    }
    finally {
      Help.langContent.clear(); Help.langContent ++= savedHelp
      CodeCompletionUtils.langTemplates.clear(); CodeCompletionUtils.langTemplates ++= savedTemplates
    }
  }

  test("rstaTemplate escapes $ not followed by { and appends ${cursor} when no interpolation is present") {
    provider.rstaTemplate("$conforms") shouldBe "$$conforms${cursor}"
  }

  test("rstaTemplate does not append ${cursor} when an interpolation ${...} is already present") {
    provider.rstaTemplate("forward(${n})") shouldBe "forward(${n})"
  }

  test("rstaTemplate escapes plain $ but preserves $ that starts a ${...} interpolation") {
    provider.rstaTemplate("$forward(${n})") shouldBe "$$forward(${n})"
  }

  test("rstaTemplate escapes plain $ but preserves $ that starts a ${...} interpolation, #2") {
    provider.rstaTemplate("for$ard(${n})") shouldBe "for$$ard(${n})"
  }

  test("rstaTemplate doubles a trailing $ and appends ${cursor} when no interpolation is present") {
    provider.rstaTemplate("$") shouldBe "$$${cursor}"
  }

  test("rstaTemplate leaves non-$ strings unchanged except for appending ${cursor}") {
    provider.rstaTemplate("clear") shouldBe "clear${cursor}"
  }

  test("completion kind icons are available at 16 by 16 pixels") {
    val icons = Seq(
      provider.kindIcon(provider.INTERPRETER_NAME),
      provider.kindIcon(provider.VALUE),
      provider.kindIcon(provider.VARIABLE),
      provider.kindIcon(provider.CLASS),
      provider.kindIcon(provider.TRAIT),
      provider.kindIcon(provider.TYPE),
      provider.kindIcon(provider.OBJECT),
      provider.kindIcon(provider.PACKAGE),
      provider.kindIcon(provider.PACKAGE_OBJECT)
    )

    icons.foreach { icon =>
      icon.getIconWidth shouldBe 16
      icon.getIconHeight shouldBe 16
    }
  }
}
