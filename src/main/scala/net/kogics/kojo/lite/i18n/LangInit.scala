package net.kogics.kojo.lite.i18n

import net.kogics.kojo.util.Utils
import java.util.prefs.Preferences

object LangInit {
  def init(prefs: Preferences) = {
    val userLanguage = prefs.get("user.language", System.getProperty("user.language"))
    if (userLanguage != null && userLanguage.trim != "") {
      java.util.Locale.setDefault(new java.util.Locale(userLanguage))
      System.setProperty("user.language", userLanguage)
    }

    System.getProperty("user.language") match {
      case "sv" =>
        System.out.println("Swedish Init")
        val ct = SvInit.codeTemplates
      case _ =>
    }
    userLanguage
  }
  
  def apply() {
    val prefs = Preferences.userRoot().node("Kojolite-Prefs")
    init(prefs)
  }
}