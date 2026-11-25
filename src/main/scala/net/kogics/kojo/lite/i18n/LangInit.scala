package net.kogics.kojo.lite.i18n

import java.awt.Font
import java.util.prefs.Preferences
import javax.swing.plaf.FontUIResource
import javax.swing.JMenu
import javax.swing.UIManager

import net.kogics.kojo.lite.CoreBuiltins

object LangInit {
  // Captured after locale setup in init; changing the preference requires a restart.
  lazy val language: String = System.getProperty("user.language", "en")

  def init(prefs: Preferences) = {
    val userLanguage = prefs.get("user.language", System.getProperty("user.language"))
    if (userLanguage != null && userLanguage.trim != "") {
      val oldLocale = java.util.Locale.getDefault
      java.util.Locale.setDefault(new java.util.Locale(userLanguage, oldLocale.getCountry, oldLocale.getVariant))
      System.setProperty("user.language", userLanguage)
    }
    language match {
      case "hi" => HindiInit.preInit()
      case "sv" => SvInit.preInit()
      case "tr" => TurkishInit.preInit()
      case _    =>
    }
    LanguageBehavior.current
    userLanguage
  }

  // Retained for callers that explicitly need the Hindi font.
  lazy val fontForHindi: Font = hi.HindiLanguageBehavior.font

  def lookAndFeelReady(): Unit = {
    LanguageBehavior.current.customUiFont.foreach { font =>
      UIManager.getLookAndFeelDefaults.put("defaultFont", new FontUIResource(font))
    }
  }

  def menuReady(m: JMenu): Unit = {
    LanguageBehavior.current.customUiFont.foreach(m.setFont)
  }

  def initPhase2(b: CoreBuiltins): Unit = {
    System.getProperty("user.language") match {
      case "sv" =>
        SvInit.init(b)
      case "pl" =>
        PlInit.init(b)
      case "nl" =>
        NlInit.init(b)
      case "de" =>
        DeInit.init(b)
      case "ru" =>
        RussianInit.init(b)
      case "it" =>
        ItInit.init(b)
      case "hr" =>
        hrInit.init(b)
      case "tr" =>
        TurkishInit.init(b)
      case "es" =>
        SpanishInit.init(b)
      case "hi" =>
        HindiInit.init(b)
      case _ =>
    }
  }

  def apply(): Unit = {
    val prefs = Preferences.userRoot().node("Kojolite-Prefs")
    init(prefs)
  }
}
