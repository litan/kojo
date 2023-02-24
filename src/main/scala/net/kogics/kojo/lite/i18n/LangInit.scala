package net.kogics.kojo.lite.i18n

import java.awt.Font
import java.awt.GraphicsEnvironment
import java.util.prefs.Preferences
import javax.swing.plaf.FontUIResource
import javax.swing.JMenu
import javax.swing.UIManager

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

  lazy val fontForHindi = {
    val font = Font.createFont(Font.TRUETYPE_FONT, getClass.getResourceAsStream("/i18n/gargi.ttf"))
    GraphicsEnvironment.getLocalGraphicsEnvironment.registerFont(font)
    val defaultSize = UIManager.getLookAndFeelDefaults.get("defaultFont").asInstanceOf[Font].getSize
    font.deriveFont(Font.PLAIN, defaultSize + 4.0f)
  }

  def lookAndFeelReady(): Unit = {
    System.getProperty("user.language") match {
      case "hi" =>
        val defaults = UIManager.getLookAndFeelDefaults
        val keys = Seq("defaultFont")
        keys.foreach { defaults.put(_, new FontUIResource(fontForHindi)) }
      case _ =>
    }
  }

  def menuReady(m: JMenu): Unit = {
    System.getProperty("user.language") match {
      case "hi" =>
        m.setFont(fontForHindi)
      case _ =>
    }
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
      case _ =>
    }
  }

  def apply(): Unit = {
    val prefs = Preferences.userRoot().node("Kojolite-Prefs")
    init(prefs)
  }
}
