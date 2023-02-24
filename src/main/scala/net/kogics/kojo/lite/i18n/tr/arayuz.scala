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

import java.awt.{ event => e }
import java.awt.Color
import java.awt.Font
import javax.{ swing => s }
import javax.swing.{ BorderFactory => bf }
import javax.swing.{ SwingConstants => sc }
import javax.swing.border.Border

import net.kogics.kojo.{ widget => w }
import net.kogics.kojo.util.Read

object arayuz {

  type Yazıyüzü = Font
  type Çerçeve = Border
  type Parça = s.JComponent

  /* ../../../widget/swingwrappers.scala
   Also has traits: PreferredMax Focusable
   */
  case class BölmeÇizgisi(renk: Renk = yok, saydamMı: İkil = yanlış) extends s.JSeparator {
    if (renk != yok) {
      setBackground(renk)
    }
    setOpaque(saydamMı)
  }

  class Sıra(parçalar: Parça*) extends w.RowPanel(parçalar: _*) {
    def artalanıKur(renk: Renk) = setBackground(renk)
    def önalanıKur(renk: Renk) = setForeground(renk)
    def çerçeveyiKur(ç: Çerçeve) = setBorder(ç)
    def sakla = setVisible(false)
    def göster = setVisible(true)
  }
  object Sıra {
    def apply(parçalar: Parça*) = new Sıra(parçalar: _*)
  }
  type Satır = Sıra
  val Satır = Sıra

  class Sütun(parçalar: Parça*) extends w.ColPanel(parçalar: _*) {
    def artalanıKur(renk: Renk) = setBackground(renk)
    def önalanıKur(renk: Renk) = setForeground(renk)
    def çerçeveyiKur(ç: Çerçeve) = setBorder(ç)
    def sakla = setVisible(false)
    def göster = setVisible(true)
  }
  object Sütun {
    def apply(parçalar: Parça*) = new Sütun(parçalar: _*)
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
    def odakDinleyiciEkle(d: olay.OdakDinleyicisi) = addFocusListener(d)
    def yazıyıAl = getText
    def yazıyıKur(y: Yazı) = setText(y)
    def sakla = setVisible(false)
    def göster = setVisible(true)
  }
  object Yazıgirdisi {
    def apply[T](varsayılan: T)(implicit okur: Read[T]) = new Yazıgirdisi(varsayılan)(okur)
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
  object Yazıalanı {
    def apply(varsayılan: Yazı) = new Yazıalanı(varsayılan)
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
  object Tanıt {
    def apply(tanıt: Yazı) = new Tanıt(tanıt)
  }

  class Düğme(tanıt: Yazı)(davranış: => Birim) extends w.Button(tanıt)(davranış) {
    def yazıYüzünüKur(yy: Yazıyüzü) = setFont(yy)
    def artalanıKur(renk: Renk) = setBackground(renk)
    def önalanıKur(renk: Renk) = setForeground(renk)
    def çerçeveyiKur(ç: Çerçeve) = setBorder(ç)
    def görünürlüğüKur(seçim: İkil) = setVisible(seçim)
    def sakla = setVisible(false)
    def göster = setVisible(true)
    def etkinliğiKur(seçim: İkil) = setEnabled(seçim)
    def pencereİçindekiOdağıİste() = requestFocusInWindow()
  }
  object Düğme {
    def apply(tanıt: Yazı)(davranış: => Birim) = new Düğme(tanıt)(davranış)
  }

  class Açkapa(tanıt: Yazı)(davranış: İkil => Birim) extends w.ToggleButton(tanıt)(davranış) {
    def yazıYüzünüKur(yy: Yazıyüzü) = setFont(yy)
    def artalanıKur(renk: Renk) = setBackground(renk)
    def önalanıKur(renk: Renk) = setForeground(renk)
    def çerçeveyiKur(ç: Çerçeve) = setBorder(ç)
    def sakla = setVisible(false)
    def göster = setVisible(true)
  }
  object Açkapa {
    def apply(tanıt: Yazı)(davranış: İkil => Birim) = new Açkapa(tanıt)(davranış)
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
  object Salındıraç {
    def apply[T](ilkSeçenekler: T*)(implicit okur: Read[T]) = new Salındıraç(ilkSeçenekler: _*)(okur)
  }

  class Kaydıraç(enUfak: Sayı, enİri: Sayı, ilkDeğer: Sayı, boşluk: Sayı)
      extends w.Slider(enUfak, enİri, ilkDeğer, boşluk) {
    def değeri = value
    def artalanıKur(renk: Renk) = setBackground(renk)
    def önalanıKur(renk: Renk) = setForeground(renk)
    def çerçeveyiKur(ç: Çerçeve) = setBorder(ç)
    def sakla = setVisible(false)
    def göster = setVisible(true)
  }
  object Kaydıraç {
    def apply(enUfak: Sayı, enİri: Sayı, ilkDeğer: Sayı, boşluk: Sayı) = new Kaydıraç(enUfak, enİri, ilkDeğer, boşluk)
  }

  object değişmez {
    // https://docs.oracle.com/javase/7/docs/api/javax/swing/SwingConstants.html
    val (
      merkez,
      taban,
      tavan,
      sol,
      sağ,
      doğu,
      batı,
      kuzey,
      güney,
      kuzeydoğu,
      kuzeybatı,
      güneydoğu,
      güneybatı,
      yatay,
      dikey,
      önceki,
      sonraki,
      önder,
      izler
    ) =
      (
        sc.CENTER,
        sc.BOTTOM,
        sc.TOP,
        sc.LEFT,
        sc.RIGHT,
        sc.EAST,
        sc.WEST,
        sc.NORTH,
        sc.SOUTH,
        sc.NORTH_EAST,
        sc.NORTH_WEST,
        sc.SOUTH_EAST,
        sc.SOUTH_WEST,
        sc.HORIZONTAL,
        sc.VERTICAL,
        sc.PREVIOUS,
        sc.NEXT,
        sc.LEADING,
        sc.TRAILING
      )
  }

  object çerçeveci {
    def çizgiKenar(r: Renk) = bf.createLineBorder(r)
    def boşKenar() = bf.createEmptyBorder()
  }

  object olay {
    type TuşUyarlayıcısı = e.KeyAdapter
    type TuşaBasmaOlayı = e.KeyEvent
    type OdakUyarlayıcısı = e.FocusAdapter
    type OdakOlayı = e.FocusEvent
    type OdakDinleyicisi = e.FocusListener
  }

  trait SwingWidgetMethodsInTurkish {
    implicit class TuşaBasmaOlayıYöntemleri(a: olay.TuşaBasmaOlayı) {
      def tuşKodu = a.getKeyCode
      def tuşHarfi = a.getKeyChar
      def tüket() = a.consume()
    }
  }

}
