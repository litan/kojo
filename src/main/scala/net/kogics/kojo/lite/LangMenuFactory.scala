package net.kogics.kojo.lite

import javax.swing.{JMenu, JOptionPane, JCheckBoxMenuItem, ImageIcon}
import net.kogics.kojo.core
import java.awt.event.{ActionEvent, ActionListener}
import net.kogics.kojo.util.Utils

/**
 * Creates the Language menu.
 *
 * @author Eric Zoerner <a href="mailto:eric.zoerner@gmail.com">eric.zoerner@gmail.com</a>
 */
object LangMenuFactory {

  val supportedLanguages = List("en", "sv", "fr", "pl" , "nl"/*, "hi","it"*/)

  def createLangMenu()(implicit kojoCtx: core.KojoCtx) = {
    var langMenus: Seq[JCheckBoxMenuItem] = Vector()
    val langHandler = new ActionListener {
      override def actionPerformed(e: ActionEvent) {
        val lang = e.getActionCommand
        kojoCtx.userLanguage = lang
        langMenus foreach {
          mi =>
            if (mi.getActionCommand != lang) {
              mi.setSelected(false)
            }
        }
        JOptionPane.showMessageDialog(kojoCtx.frame,
                                      Utils.loadString("S_LangChanged").format(e.getSource.asInstanceOf[JCheckBoxMenuItem].getText),
                                      Utils.loadString("S_LangChange"),
                                      JOptionPane.INFORMATION_MESSAGE)
      }
    }

    def langMenuItem(langCode: String) = {
      val langName = langNames(langCode)
      val mitem = new JCheckBoxMenuItem(langName)
      mitem.addActionListener(langHandler)
      mitem.setActionCommand(langCode)
//      mitem.setIcon(langIcon(langCode))
      if (kojoCtx.userLanguage == langCode) {
        mitem.setSelected(true)
      }
      langMenus :+= mitem
      mitem
    }

    val langMenu = new JMenu(Utils.loadString("S_Language"))
    langMenu.setMnemonic('L')
    langMenu.setIcon(Utils.loadIcon("/images/generic-flag.png"))
    supportedLanguages.foreach {lang ⇒ langMenu.add(langMenuItem(lang))}
    langMenu
  }

  private val langNames = Map (
    "en" → "English",
    "sv" → "Svenska (Swedish)",
    "fr" → "Français (French)",
    "pl" → "Polski (Polish)",
    "nl" → "Nederlands (Dutch)",
    "it" → "Italiano (Italian)",
    "hi" → "हिंदी (Hindi)"
  )

  /** If the language code is not in this map, then the country defaults to same code as the language. */
  private val langToCountry = Map(
    "en" → "us",
    "hi" → "in"
  )

  private def langIcon(langCode: String): ImageIcon = {
    val countryCode = langToCountry.get(langCode).getOrElse(langCode)
    Utils.loadIcon(s"/geogebra/gui/menubar/images/$countryCode.png")
  }
}