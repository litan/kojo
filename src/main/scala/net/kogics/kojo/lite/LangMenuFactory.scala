/*
 * Copyright (C) 2014 Eric Zoerner <eric.zoerner@gmail.com>
 * Copyright (C) 2012 Lalit Pant <pant.lalit@gmail.com>
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
package net.kogics.kojo.lite

import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.ImageIcon
import javax.swing.JCheckBoxMenuItem
import javax.swing.JMenu
import javax.swing.JOptionPane

import net.kogics.kojo.core
import net.kogics.kojo.util.Utils

/** Creates the Language menu.
  *
  * @author
  *   Eric Zoerner <a href="mailto:eric.zoerner@gmail.com">eric.zoerner@gmail.com</a>
  * @author
  *   Christoph Knabe http://public.beuth-hochschule.de/~knabe/
  */
object LangMenuFactory {

  val supportedLanguages = List("en", "sv", "fr", "pl", "nl", "eo", /*"hi", */ "de", "ru", "it", "hr", "tr", "es")

  def createLangMenu()(implicit kojoCtx: core.KojoCtx) = {
    var langMenus: Seq[JCheckBoxMenuItem] = Vector()
    val langHandler = new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        val lang = e.getActionCommand
        kojoCtx.userLanguage = lang
        langMenus.foreach { mi =>
          if (mi.getActionCommand != lang) {
            mi.setSelected(false)
          }
        }
        JOptionPane.showMessageDialog(
          kojoCtx.frame,
          Utils.loadString("S_LangChanged").format(e.getSource.asInstanceOf[JCheckBoxMenuItem].getText),
          Utils.loadString("S_LangChange"),
          JOptionPane.INFORMATION_MESSAGE
        )
      }
    }

    def langMenuItem(langCode: String) = {
      val langName = langNames(langCode)
      val mitem = new JCheckBoxMenuItem(langName)
      mitem.addActionListener(langHandler)
      mitem.setActionCommand(langCode)
      // mitem.setIcon(langIcon(langCode))
      if (kojoCtx.userLanguage == langCode) {
        mitem.setSelected(true)
      }
      langMenus :+= mitem
      mitem
    }

    val langMenu = new JMenu(Utils.loadString("S_Language"))
    langMenu.setMnemonic('L')
    langMenu.setIcon(
      Utils.loadIcon("/images/generic-flag.png")
      // langIcon(kojoCtx.userLanguage)
    )
    supportedLanguages.foreach { lang => langMenu.add(langMenuItem(lang)) }
    langMenu
  }

  private val langNames = Map(
    "en" -> "English",
    "sv" -> "Svenska (Swedish)",
    "fr" -> "Français (French)",
    "pl" -> "Polski (Polish)",
    "nl" -> "Nederlands (Dutch)",
    "eo" -> "Esperanto (international)",
    "it" -> "Italiano (Italian)",
    "hi" -> "हिंदी (Hindi)",
    "de" -> "Deutsch (German)",
    "ru" -> "Русский (Russian)",
    "hr" -> "Hrvatski (Croatian)",
    "tr" -> "Türkçe (Turkish)",
    "es" -> "Español (Spanish)"
  )

  /** If the language code is not in this map, then the country defaults to same code as the language. */
  private val langToCountry = Map(
    "en" -> "us",
    "hi" -> "in"
  )
}
