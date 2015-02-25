package net.kogics.kojo.lite.i18n

import java.awt.Font
import java.util.prefs.Preferences

import javax.swing.JMenu
import javax.swing.UIManager
import javax.swing.plaf.FontUIResource

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

  lazy val fontForHindi = Font.createFont(Font.TRUETYPE_FONT, getClass.getResourceAsStream("/i18n/gargi.ttf")).deriveFont(Font.PLAIN, 16f)
  //  lazy val fontForHindi = new Font(Font.SANS_SERIF, Font.PLAIN, 18)
  def lookAndFeelReady() {
    System.getProperty("user.language") match {
      case "hi" =>
        val defaults = UIManager.getLookAndFeelDefaults
        val keys = Seq("defaultFont")
        keys.foreach { defaults.put(_, new FontUIResource(fontForHindi)) }
      case _ =>
    }
  }

  def menuReady(m: JMenu) {
    System.getProperty("user.language") match {
      case "hi" =>
        m.setFont(fontForHindi)
      case _ =>
    }
  }

  def initPhase2(b: CoreBuiltins) {
    System.getProperty("user.language") match {
      case "sv" =>
        val ct = SvInit.init(b)
      case "pl" =>
        val ct = PlInit.init(b)
      case "nl" =>
        val nl = NlInit.init(b)
      case "de" =>
        val de = DeInit.init(b)
      case _ =>
    }
  }

  def apply() {
    val prefs = Preferences.userRoot().node("Kojolite-Prefs")
    init(prefs)
  }
}