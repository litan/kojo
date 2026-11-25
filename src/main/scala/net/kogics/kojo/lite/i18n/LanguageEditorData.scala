package net.kogics.kojo.lite.i18n

import net.kogics.kojo.core.CodingMode
import net.kogics.kojo.xscala.{CodeCompletionUtils, CodeTemplates}

/** Immutable startup contributions. Derived enumeration tables are shared by all consumers. */
final case class LanguageEditorData(
    keywords: List[String] = Nil,
    keywordTemplates: Map[String, String] = Map.empty,
    snippets: Map[String, String] = Map.empty,
    keywordHelp: Map[CodingMode, Map[String, String]] = Map.empty
) {
  lazy val keywordSet: Set[String] = keywords.toSet
  lazy val allKeywords: List[String] = keywords ++ CodeCompletionUtils.EnglishKeywords
  lazy val allKeywordTemplates: Map[String, String] = keywordTemplates ++ CodeCompletionUtils.EnglishKeywordTemplates
  lazy val allSnippets: Map[String, String] = snippets ++ CodeTemplates.BaseTemplates
}

object LanguageEditorDataRegistry {
  val Empty: LanguageEditorData = LanguageEditorData()
  @volatile private var activeData: LanguageEditorData = Empty

  def register(data: LanguageEditorData): Unit = activeData = data
  def current: LanguageEditorData = activeData
}
