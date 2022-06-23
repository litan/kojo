/*
 * Copyright (C) 2021
 *   Bulent Basaran <ben@scala.org> https://github.com/bulent2k2
 *   Lalit Pant <pant.lalit@gmail.com>
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
package net.kogics.kojo.lite.i18n.tr

// translating user interface widgets

import net.kogics.kojo.{widget => w}
import net.kogics.kojo.util.Read
import javax.{swing => s}
import javax.swing.{SwingConstants => sc}
import javax.swing.{BorderFactory => bf}
import javax.swing.border.Border
import java.awt.{Font, Color}
import java.awt.{event => e}

object arayuz {

  type Yazıyüzü = Font
  type Çerçeve = Border

  /* ../../../widget/swingwrappers.scala
   Also has traits: PreferredMax Focusable
   */
  case class BölmeÇizgisi(renk: Renk = yok, saydamMı: İkil = yanlış) extends s.JSeparator {
    if (renk != yok) {
      setBackground(renk)
    }
    setOpaque(saydamMı)
  }
  type Parça = s.JComponent
  class Sıra(parçalar: Parça*) extends w.RowPanel(parçalar: _*) {
    def artalanıKur(renk: Renk) = setBackground(renk)
    def önalanıKur(renk: Renk) = setForeground(renk)
    def çerçeveyiKur(ç: Çerçeve) = setBorder(ç)
    def sakla = setVisible(false)
    def göster = setVisible(true)
  }
  type Satır = Sıra
  class Sütun(parçalar: Parça*) extends w.ColPanel(parçalar: _*) {
    def artalanıKur(renk: Renk) = setBackground(renk)
    def önalanıKur(renk: Renk) = setForeground(renk)
    def çerçeveyiKur(ç: Çerçeve) = setBorder(ç)
    def sakla = setVisible(false)
    def göster = setVisible(true)
  }
  class Yazıgirdisi[T](varsayılan: T)(implicit okur: Read[T]) extends w.TextField[T](varsayılan)(okur) {
    def değeri = value
    def yazıYüzünüKur(yy: Yazıyüzü) = setFont(yy)
    def sütunSayısınıKur(sayı: Sayı) = setColumns(sayı)
    def yatayDüzeniKur(düzen: Sayı) = setHorizontalAlignment(düzen)
    def artalanıKur(renk: Renk) = setBackground(renk)
    def önalanıKur(renk: Renk) = setForeground(renk)
    def çerçeveyiKur(ç: Çerçeve) = setBorder(ç)
    def girdiOdağıOl() = takeFocus()
    def girdiDinleyiciEkle(d: olay.TuşUyarlayıcısı) = addKeyListener(d)
    def yazıyıAl = getText
    def yazıyıKur(y: Yazı) = setText(y)
    def sakla = setVisible(false)
    def göster = setVisible(true)
  }
  class Yazıalanı(varsayılan: Yazı) extends w.TextArea(varsayılan) {
    def değeri = value
    def girdiOdağıOl() = takeFocus()
    def artalanıKur(renk: Renk) = setBackground(renk)
    def önalanıKur(renk: Renk) = setForeground(renk)
    def çerçeveyiKur(ç: Çerçeve) = setBorder(ç)
    def sakla = setVisible(false)
    def göster = setVisible(true)
  }

  class Tanıt(tanıt: Yazı) extends w.Label(tanıt) {
    def yazıYüzünüKur(yy: Yazıyüzü) = setFont(yy)
    def yatayDüzeniKur(düzen: Sayı) = setHorizontalAlignment(düzen)
    def dikeyDüzeniKur(düzen: Sayı) = setVerticalAlignment(düzen)
    def çerçeveyiKur(ç: Çerçeve) = setBorder(ç)
    def önalanıKur(renk: Renk) = setForeground(renk)
    def artalanıKur(renk: Renk) = setBackground(renk)
    def sakla = setVisible(false)
    def göster = setVisible(true)
  }
  class Düğme(tanıt: Yazı)(davranış: => Birim) extends w.Button(tanıt)(davranış) {
    def yazıYüzünüKur(yy: Yazıyüzü) = setFont(yy)
    def artalanıKur(renk: Renk) = setBackground(renk)
    def önalanıKur(renk: Renk) = setForeground(renk)
    def çerçeveyiKur(ç: Çerçeve) = setBorder(ç)
    def sakla = setVisible(false)
    def göster = setVisible(true)
  }
  class Açkapa(tanıt: Yazı)(davranış: İkil => Birim) extends w.ToggleButton(tanıt)(davranış) {
    def yazıYüzünüKur(yy: Yazıyüzü) = setFont(yy)
    def artalanıKur(renk: Renk) = setBackground(renk)
    def önalanıKur(renk: Renk) = setForeground(renk)
    def çerçeveyiKur(ç: Çerçeve) = setBorder(ç)
    def sakla = setVisible(false)
    def göster = setVisible(true)
  }
  class Salındıraç[T](ilkSeçenekler: T*)(implicit okur: Read[T]) extends w.DropDown[T](ilkSeçenekler: _*)(okur) {
    def değeri = value
    def seçilince(davran: T => Birim) = onSelection(davran)
    def seçenekleriKur(seçenekler: T*) = setOptions(seçenekler: _*)
    def yazıYüzünüKur(yy: Yazıyüzü) = setFont(yy)
    def artalanıKur(renk: Renk) = setBackground(renk)
    def önalanıKur(renk: Renk) = setForeground(renk)
    def çerçeveyiKur(ç: Çerçeve) = setBorder(ç)
    def sakla = setVisible(false)
    def göster = setVisible(true)
  }
  class Kaydıraç(enUfak: Sayı, enİri: Sayı, ilkDeğer: Sayı, boşluk: Sayı) extends w.Slider(enUfak, enİri, ilkDeğer, boşluk) {
    def değeri = value
    def artalanıKur(renk: Renk) = setBackground(renk)
    def önalanıKur(renk: Renk) = setForeground(renk)
    def çerçeveyiKur(ç: Çerçeve) = setBorder(ç)
    def sakla = setVisible(false)
    def göster = setVisible(true)
  }

  object değişmez {
    // https://docs.oracle.com/javase/7/docs/api/javax/swing/SwingConstants.html
    val (merkez, taban, tavan, sol, sağ,
      doğu, batı, kuzey, güney, kuzeydoğu, kuzeybatı, güneydoğu, güneybatı,
      yatay, dikey, önceki, sonraki, önder, izler) =
      (sc.CENTER, sc.BOTTOM, sc.TOP, sc.LEFT, sc.RIGHT,
        sc.EAST, sc.WEST, sc.NORTH, sc.SOUTH, sc.NORTH_EAST, sc.NORTH_WEST, sc.SOUTH_EAST, sc.SOUTH_WEST,
        sc.HORIZONTAL, sc.VERTICAL, sc.PREVIOUS, sc.NEXT, sc.LEADING, sc.TRAILING)
  }

  object çerçeveci {
    def çizgiKenar(r: Renk) = bf.createLineBorder(r)
    def boşKenar() = bf.createEmptyBorder()
  }

  object olay {
    type TuşUyarlayıcısı = e.KeyAdapter
    type TuşaBasmaOlayı = e.KeyEvent
  }

  trait SwingWidgetMethodsInTurkish {
    implicit class TuşaBasmaOlayıYöntemleri(a: olay.TuşaBasmaOlayı) {
      def tuşKodu = a.getKeyCode
      def tuşHarfi = a.getKeyChar
      def tüket() = a.consume()
    }
  }

}
