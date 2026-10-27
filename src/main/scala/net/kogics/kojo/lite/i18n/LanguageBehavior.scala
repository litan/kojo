package net.kogics.kojo.lite.i18n

import java.awt.Font

/** Language-specific presentation and completion rules, selected before UI construction. */
trait LanguageBehavior {
  def updateResult(text: String): String = text
  def updateTypes(text: String): String = text
  def recognizesOwner(owner: String): Boolean = false
  def allowsPlainNameFallback(owner: String): Boolean = false
  def customUiFont: Option[Font] = None
  def editorFont(baseFont: Font, baseSize: Float): Font = baseFont.deriveFont(baseSize)
  def editorLinePadding: Int = 0
}

object LanguageBehavior {
  object Default extends LanguageBehavior

  def forLanguage(code: String): LanguageBehavior = code match {
    case "hi" => hi.HindiLanguageBehavior
    case "tr" => tr.TurkishLanguageBehavior
    case _    => Default
  }

  lazy val current: LanguageBehavior = forLanguage(LangInit.language)
}
