/*
 * Copyright (C) 2018 Lalit Pant <pant.lalit@gmail.com>
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

import javax.swing.JButton
import javax.swing.JDialog
import javax.swing.JFrame
import javax.swing.JPanel

import net.kogics.kojo.util.Utils
import net.kogics.kojo.widget.ColPanel
import net.kogics.kojo.widget.DropDown
import net.kogics.kojo.widget.Label
import net.kogics.kojo.widget.RowPanel

class SettingsWindow(owner: JFrame) extends JDialog(owner) {

  val theme = Utils.appProperty("theme").getOrElse("light")
  val fontIncrease = Utils.appProperty("font.increase").getOrElse("0")

  def filler(n: Int) = Label(" " * n)

  val themeDd = DropDown("light", "dark")
  themeDd.setSelectedItem(theme)

  val fontDd = DropDown((-10 to 10).reverse: _*)
  fontDd.setSelectedItem(fontIncrease)

  val r1 = RowPanel(filler(10), Label("UI Theme:"), filler(3), themeDd)
  val r2 = RowPanel(filler(10), Label(<html>&nbsp;&nbsp;&nbsp;<em>Choose a light or dark background
    for Kojo.</em></html>.toString()), filler(10))
  val r3 = RowPanel(filler(10), Label("Font Adjustment:"), filler(3), fontDd)
  val r4 = RowPanel(filler(10), Label(<html>&nbsp;&nbsp;&nbsp;<em>Adjust Kojo font-size. Positive numbers increase<br/>
    &nbsp;&nbsp;&nbsp;the default font size; negative numbers decrease it.</em></html>.toString()), filler(10))
  val r5 = RowPanel(filler(10), Label(<html><em>After making changes, restart Kojo to activate new settings.</em></html>.toString()))

  val okCancel = new JPanel
  val ok = new JButton(Utils.loadString("S_OK"))
  ok.addActionListener(new ActionListener {
    def actionPerformed(ev: ActionEvent): Unit = {
      val newFontIncrease = fontDd.value.toString
      val newTheme = themeDd.value
      val m = Map(
        "theme" -> newTheme,
        "font.increase" -> newFontIncrease
      )
      Utils.updateAppProperties(m)
      setVisible(false)
    }
  })
  val cancel = new JButton(Utils.loadString("S_Cancel"))
  cancel.addActionListener(new ActionListener {
    def actionPerformed(ev: ActionEvent) {
      setVisible(false)
    }
  })
  okCancel.add(ok)
  okCancel.add(cancel)

  val d = ColPanel(filler(1), r1, r2, r3, r4, filler(1), r5, okCancel)

  setTitle("Settings")
  setModal(true)
  getRootPane.setDefaultButton(ok)
  getContentPane.add(d)
  setBounds(300, 300, 450, 400)
  pack()
}
