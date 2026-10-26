/*
 * Copyright (C) 2026
 *   Bulent Basaran <ben@scala.org> https://github.com/bulent2k2
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
package net.kogics.kojo.lite.i18n

import net.kogics.kojo.core.TwMode
import net.kogics.kojo.lite.LanguageRegistry
import net.kogics.kojo.xscala.{CodeCompletionUtils, CodeTemplates, Help}
import org.junit.runner.RunWith
import org.scalatest.{FunSuite, Matchers}
import org.scalatest.junit.JUnitRunner
import LanguageTestSupport.withData

@RunWith(classOf[JUnitRunner])
class LanguageEditorDataTest extends FunSuite with Matchers {
  test("empty editor data leaves English defaults available") {
    withData(LanguageEditorDataRegistry.Empty) {
      LanguageEditorDataRegistry.current.keywords should be(Nil)
      LanguageEditorDataRegistry.current.keywordSet.contains("dez") should be(false)
      CodeCompletionUtils.Keywords should contain("val")
      CodeCompletionUtils.keywordTemplate("while") should startWith("while (")
      CodeTemplates.templates should contain key "test"
    }
  }

  test("preInit registers static Turkish and Swedish data without builtins") {
    val previous = LanguageEditorDataRegistry.current
    try {
      TurkishInit.preInit()
      val turkish = LanguageEditorDataRegistry.current
      turkish.keywordSet should contain("dez")
      turkish.keywordSet should not contain "val"
      turkish.keywordHelp(TwMode)("val") should include("dez")
      TurkishInit.preInit()
      LanguageEditorDataRegistry.current should be(turkish)
      SvInit.preInit()
      val swedish = LanguageEditorDataRegistry.current
      swedish.keywordSet should contain("värde")
      swedish.keywordSet should not contain "dez"
      swedish.keywordTemplates should be(empty)
      swedish.keywordHelp should be(empty)
    }
    finally LanguageEditorDataRegistry.register(previous)
  }

  test("base templates and snippets win collisions in lookup and cached enumeration") {
    val data = LanguageEditorData(
      List("invented"),
      Map("while" -> "wrong", "invented" -> "invented ${value}"),
      Map("test" -> "wrong", "invented_block" -> "invented ${cursor}\n")
    )
    withData(data) {
      CodeCompletionUtils.keywordTemplate("while") should be(CodeCompletionUtils.EnglishKeywordTemplates("while"))
      CodeCompletionUtils.KeywordTemplates("while") should be(CodeCompletionUtils.keywordTemplate("while"))
      CodeCompletionUtils.keywordTemplate("invented") should be("invented ${value}")
      CodeCompletionUtils.keywordTemplate("missing") should be(null)
      CodeTemplates("test") should be(CodeTemplates.BaseTemplates("test"))
      CodeTemplates.templates("test") should be(CodeTemplates("test"))
      CodeTemplates("invented_block") should be("invented ${cursor}\n")
      CodeTemplates.asString("invented_block") should be("invented |<br/>")
      CodeCompletionUtils.Keywords.head should be("invented")
      LanguageEditorDataRegistry.current.keywordSet.contains("invented") should be(true)
      val keywords = CodeCompletionUtils.Keywords
      val templates = CodeCompletionUtils.KeywordTemplates
      val snippets = CodeTemplates.templates
      for (_ <- 1 to 3) {
        (CodeCompletionUtils.Keywords eq keywords) should be(true)
        (CodeCompletionUtils.KeywordTemplates eq templates) should be(true)
        (CodeTemplates.templates eq snippets) should be(true)
      }
    }
  }

  test("registration replaces a whole record and restores shared state after failure") {
    val original = LanguageEditorDataRegistry.current
    intercept[IllegalStateException] {
      withData(LanguageEditorData(List("old"))) {
        withData(LanguageEditorData(List("new"))) {
          LanguageEditorDataRegistry.current.keywordSet.contains("new") should be(true)
          LanguageEditorDataRegistry.current.keywordSet.contains("old") should be(false)
        }
        LanguageEditorDataRegistry.current.keywordSet.contains("old") should be(true)
        throw new IllegalStateException("test failure")
      }
    }
    (LanguageEditorDataRegistry.current eq original) should be(true)
  }

  test("synthetic editor data is independent of compiler capabilities and survives mode clears") {
    val data = LanguageEditorData(List("invented"), keywordHelp = Map(TwMode -> Map("val" -> "keyword help")))
    LanguageRegistry.forLanguage("synthetic").alternativeToolchain should be(None)
    withData(data) {
      try {
        CodeCompletionUtils.activateTw()
        Help("val") should be("keyword help")
        CodeCompletionUtils.activateVn()
        LanguageEditorDataRegistry.current.keywordSet.contains("invented") should be(true)
        Help("val") should be(null)
        CodeCompletionUtils.activateTw()
        Help("val") should be("keyword help")
      }
      finally CodeCompletionUtils.activateTw()
    }
  }

  test("derived collections initialize safely on concurrent first access") {
    import scala.concurrent.{Await, ExecutionContext, Future}
    import scala.concurrent.duration._
    implicit val ec: ExecutionContext = ExecutionContext.global
    val data = LanguageEditorData(List("concurrent"), snippets = Map("concurrent" -> "${cursor}\n"))
    val results = Await.result(Future.sequence((1 to 16).map { _ => Future {
      (data.allKeywords, data.allKeywordTemplates, data.allSnippets, data.keywordSet)
    }}), 10.seconds)
    results.foreach { case (keywords, templates, snippets, keywordSet) =>
      (keywords eq data.allKeywords) should be(true)
      (templates eq data.allKeywordTemplates) should be(true)
      (snippets eq data.allSnippets) should be(true)
      (keywordSet eq data.keywordSet) should be(true)
    }
  }
}
