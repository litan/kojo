package net.kogics.kojo.lite.i18n

import net.kogics.kojo.util.Utils
import java.util.prefs.Preferences
import net.kogics.kojo.lite.CoreBuiltins

object LangInit {
  def init(prefs: Preferences) = {
    val userLanguage = prefs.get("user.language", System.getProperty("user.language"))
    if (userLanguage != null && userLanguage.trim != "") {
      val oldLocale = java.util.Locale.getDefault
      java.util.Locale.setDefault(new java.util.Locale(userLanguage, oldLocale.getCountry, oldLocale.getVariant))
      System.setProperty("user.language", userLanguage)
    }

    userLanguage
  }

  def initPhase2(b: CoreBuiltins) {
    System.getProperty("user.language") match {
      case "sv" =>
        val ct = SvInit.init(b)
      case "pl" =>
        val ct = PlInit.init(b)
      case "nl" =>
        val nl = NlInit.init(b)
      case _ =>
    }
  }

  def apply() {
    val prefs = Preferences.userRoot().node("Kojolite-Prefs")
    init(prefs)
  }
}