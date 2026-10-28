package net.kogics.kojo.lite.i18n

import java.awt.{Font, GraphicsEnvironment}

/** Language-specific presentation and completion rules, selected before UI construction. */
trait LanguageBehavior {
  def updateResult(text: String): String = text
  def updateTypes(text: String): String = text
  def recognizesOwner(owner: String): Boolean = false
  def allowsPlainNameFallback(owner: String): Boolean = false
  def customUiFont: Option[Font] = None
  def editorFont(baseFont: Font, baseSize: Float): Font = {
    val preferredFont = LanguageBehavior.preferredEditorFontFamily match {
      case Some(family) => new Font(family, baseFont.getStyle, baseFont.getSize)
      case None         => baseFont
    }
    preferredFont.deriveFont(baseSize)
  }
  def editorBottomPadding: Int = 0
  def editorTopPadding: Int = 0
}

object LanguageBehavior {
  private lazy val preferredEditorFontFamily = GraphicsEnvironment
    .getLocalGraphicsEnvironment
    .getAvailableFontFamilyNames
    .find(_.equalsIgnoreCase("Consolas"))

  object Default extends LanguageBehavior

  def forLanguage(code: String): LanguageBehavior = code match {
    case "hi" => hi.HindiLanguageBehavior
    case "tr" => tr.TurkishLanguageBehavior
    case _    => Default
  }

  lazy val current: LanguageBehavior = forLanguage(LangInit.language)
}
