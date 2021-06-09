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
import javax.{swing => s}
import javax.swing.{SwingConstants => sc}
import javax.swing.{BorderFactory => bf}
import java.awt.Font
import java.awt.{event => e}

object arayuz {

  case class BölmeÇizgisi(renk: Renk = yok, saydamMı: İkil = yanlış) extends s.JSeparator {
    if (renk != yok) {
      setBackground(renk)
    }
    setOpaque(saydamMı)
  }

  /* ../../../widget/swingwrappers.scala
   Also has traits: PreferredMax Focusable
   */
  type Parça = s.JComponent
  type Sıra = w.RowPanel
  type Satır = Sıra
  type Sütun = w.ColPanel
  type Yazıgirdisi[T] = w.TextField[T]
  type Yazıalanı = w.TextArea
  type Tanıt = w.Label
  type Düğme = w.Button
  type Açkapa = w.ToggleButton
  type Salındıraç[T] = w.DropDown[T]
  type Kaydıraç = w.Slider
  // type BölmeÇizgisi = s.JSeparator
  val Sıra = w.RowPanel
  val Satır = Sıra
  val Sütun = w.ColPanel
  val Yazıgirdisi = w.TextField
  val Yazıalanı = w.TextArea
  val Tanıt = w.Label
  val Düğme = w.Button
  val Açkapa = w.ToggleButton
  val Salındıraç = w.DropDown
  val Kaydıraç = w.Slider

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

  /* todo: how does this work? E.g.
   *      new Tanıt("This is so and so") {
   *          setFont(Font("Sans Serif", 60))
   *          setHorizontalAlignment(SwingConstants.CENTER)
   *      }
   * Seem to be methods defined in the class Tanıt (Label)
   */

  object olay {
    type TuşUyarlayıcısı = e.KeyAdapter
    type TuşaBasmaOlayı = e.KeyEvent
  }
}
