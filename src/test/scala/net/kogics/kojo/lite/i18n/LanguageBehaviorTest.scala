package net.kogics.kojo.lite.i18n

import java.awt.Font

import net.kogics.kojo.core.{CompletionInfo, MemberKind}
import net.kogics.kojo.lite.KojoCompletionProvider
import net.kogics.kojo.xscala.{CodeCompletionUtils, CodeTemplates, Help}
import org.junit.runner.RunWith
import org.scalatest.{BeforeAndAfter, FunSuite, Matchers}
import org.scalatest.junit.JUnitRunner

/** Observable behavior captured before extracting the language hooks. */
@RunWith(classOf[JUnitRunner])
class LanguageBehaviorTest extends FunSuite with Matchers with BeforeAndAfter {
  test("default font hooks preserve the existing family, style and fractional size") {
    val font = new Font(Font.MONOSPACED, Font.BOLD | Font.ITALIC, 12)
    for (code <- List("en", "sv", "de", "tr", "xx"); size <- List(12.0f, 15.5f)) {
      val behavior = LanguageBehavior.forLanguage(code)
      behavior.customUiFont should be(None)
      behavior.editorLinePadding should be(0)
      behavior.editorFont(font, size) should be(font.deriveFont(size))
    }
  }

  test("default behavior is identity and Turkish owner rules require the full package prefix") {
    for (language <- List("en", "sv", "de", "xx")) {
      val behavior = LanguageBehavior.forLanguage(language)
      behavior.updateResult("val res0: String = null") should be("val res0: String = null")
      behavior.updateTypes("Option[Int]") should be("Option[Int]")
      behavior.recognizesOwner("net.kogics.kojo.lite.i18n.tr.Eşlem") should be(false)
      behavior.allowsPlainNameFallback(null) should be(false)
    }
    val turkish = LanguageBehavior.forLanguage("tr")
    turkish.recognizesOwner("net.kogics.kojo.lite.i18n.tr.Eşlem") should be(true)
    for (owner <- List(null, "Eşlem", "net.kogics.kojo.lite.i18n.tr", "net.kogics.kojo.lite.i18n.trick.Owner")) {
      turkish.recognizesOwner(owner) should be(false)
      turkish.allowsPlainNameFallback(owner) should be(false)
    }
  }

  private def inTurkish[A](body: => A): A = LanguageTestSupport.withData(LanguageTestSupport.turkishData)(body)
  private def output(s: String) = LanguageBehavior.forLanguage("tr").updateResult(s)
  private def typeInfo(s: String) = LanguageBehavior.forLanguage("tr").updateTypes(s)
  private lazy val provider = new KojoCompletionProvider(null, tr.TurkishLanguageBehavior)

  after {
    Help.activateTw()
    CodeCompletionUtils.activateTw()
  }

  test("Turkish result and type presentation remain separate") { inTurkish {
    output("val res0: List[Int] = List(1, 2)") should be("dez sonuç0: Dizin[Sayı] = Dizin(1, 2)")
    output("java.lang.NullPointerException") should be("BoşGöstergeHatası")
    output("None Some(1) Range 1 to 3") should be("Hiçbiri Biri(1) Aralık(1, 2, 3)")
    typeInfo("Option[Int] => String") should be("Belki[Sayı] => Yazı")
    typeInfo("val res0: Int") should be("val res0: Sayı")
    // Preserve existing broad replacements, including replacements in user text.
    output("String") should be("Yazı")
  }}

  test("localized keyword and code templates retain their placeholders") { inTurkish {
    CodeCompletionUtils.Keywords should contain("val")
    CodeCompletionUtils.Keywords should contain("dez")
    CodeCompletionUtils.keywordTemplate("dez") should be("dez ${değişmezinAdı} = ${değer}")
    CodeTemplates("dez_türlü") should be("dez ${değişmezinAdı}: ${TürAdı} = ${değer}${cursor}\n")
    CodeCompletionUtils.keywordTemplate("while") should be("while (${condition}) {\n    ${cursor}\n}")
  }}

  test("Turkish keyword help overrides English entries only in turtle mode") { inTurkish {
    Help.activateTw()
    for ((word, translation) <- Seq("val" -> "dez", "def" -> "tanım", "if" -> "eğer", "for" -> "için",
        "class" -> "sınıf", "var" -> "den")) {
      Help(word) should be(s"Türkçesi '$translation' olan anahtar sözcük: '$word'.")
    }
    Help("enum") should be(null)
    Help("unknown-topic") should be(null)
    Help.activateVn()
    Help("val") should be(null)
    Help("class") should be(null)
  }}

  test("completion translates display fields while preserving insertion and fallback text") { inTurkish {
    val simple = provider.proposal(0, "Int", provider.TYPE, null)
    simple.getInputText should be("Sayı")
    simple.getReplacementText should be(null)
    simple.getInsertionInfo(new javax.swing.JTextArea(), false).getTextToInsert should be("Int")
    val info = CompletionInfo(MemberKind.Def, "sample", "", "example.Unknown", 1, false,
      List(List("x")), List(List("Int")), "String", "")
    val completion = provider.proposal2(0, info)
    completion.getInputText should be("sample(x: Sayı): Yazı")
    completion.getReplacementText should be(null)
    completion.getSummary should be(info.fullCompletion)
    completion.getInsertionInfo(new javax.swing.JTextArea(), false).getTextToInsert should be("sample(x)")
  }}

  test("Turkish collection owners use qualified help first, then plain-name help and templates") { inTurkish {
    val owner = "net.kogics.kojo.lite.i18n.tr.SeqMethodsInTurkish.SeqYöntemleri"
    val info = CompletionInfo(MemberKind.Def, "katla", "", owner, 1, false, Nil, Nil, "Int", "")
    val uiLanguage = System.getProperty("user.language")
    Help.addContent(uiLanguage, Map("katla" -> "plain help"))
    CodeCompletionUtils.addTemplates(uiLanguage, Map("katla" -> "katla(${seed})"))
    val plain = provider.proposal2(0, info)
    plain.getSummary should be("plain help")
    plain.getInsertionInfo(new javax.swing.JTextArea(), false).getTextToInsert should be("katla(seed)")
    Help.addContent(uiLanguage, Map("SeqYöntemleri.katla" -> "qualified help"))
    CodeCompletionUtils.addTemplates(uiLanguage, Map("SeqYöntemleri.katla" -> "katla(${qualified})"))
    provider.proposal2(0, info).getSummary should be("qualified help")
    provider.proposal2(0, info).getInsertionInfo(new javax.swing.JTextArea(), false).getTextToInsert should be("katla(qualified)")
    provider.proposal2(0, info.copy(owner = "example.SeqYöntemleri")).getSummary should be(info.fullCompletion)
    LanguageTestSupport.withData(LanguageEditorData()) {
      val englishProvider = new KojoCompletionProvider(null, LanguageBehavior.Default)
      englishProvider.proposal2(0, info).getSummary should be(info.fullCompletion)
      englishProvider.proposal2(0, info).getInsertionInfo(new javax.swing.JTextArea(), false).getTextToInsert should be("katla")
    }
  }}
}
