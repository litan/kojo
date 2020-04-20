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
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent

import javax.swing.JButton
import javax.swing.JDialog
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JTabbedPane
import javax.swing.JTextField

import net.kogics.kojo.util.Utils
import net.kogics.kojo.widget.ColPanel
import net.kogics.kojo.widget.DropDown
import net.kogics.kojo.widget.Label
import net.kogics.kojo.widget.RowPanel
import net.kogics.kojo.widget.TextField

class SettingsWindow(owner: JFrame) extends JDialog(owner) {

  val theme = Utils.appProperty("theme").getOrElse("light")
  val fontIncrease = Utils.appProperty("font.increase").getOrElse("0")
  val dpi = Utils.appProperty("export.image.dpi").getOrElse("")
  val inches = Utils.appProperty("export.image.inches").getOrElse("")
  val dimension = Utils.appProperty("export.image.dimension").getOrElse("height")

  def filler(n: Int) = Label(" " * n)

  val themeDd = DropDown("light", "dark")
  themeDd.setSelectedItem(theme)

  val fontDd = DropDown((-10 to 10).reverse: _*)
  fontDd.setSelectedItem(fontIncrease)

  val dpiTf = TextField(dpi)
  dpiTf.setColumns(3)

  def handleInchesDdSelection(item: String): Unit = PaperSize.fromString(item) match {
    case Some(ps) =>
      aspectTf.setText(ps.name)
      dimensionDd.setSelectedItem("height")
      dimensionDd.setEnabled(false)
      adjustCanvasBtn.requestFocusInWindow()
    case None =>
  }

  val inchesDd = DropDown(PaperSize.allSizes: _*)
  inchesDd.setEditable(true)
  val inchesDdEditor = {
    try {
      inchesDd.getEditor.getEditorComponent.asInstanceOf[JTextField]
    }
    catch {
      case t: Throwable =>
        println(t.getMessage)
        new JTextField()
    }
  }
  inchesDdEditor.setColumns(4)
  inchesDd.setSelectedItem(inches)
  inchesDd.onSelection { item =>
    handleInchesDdSelection(item)
  }
  inchesDdEditor.addKeyListener(new KeyAdapter {
    override def keyTyped(e: KeyEvent): Unit = {
      aspectTf.setText("")
      dimensionDd.setEnabled(true)
    }
  })
  val dimensionDd = DropDown("height", "width")
  dimensionDd.setSelectedItem(dimension)

  val r1 = RowPanel(filler(10), Label(Utils.loadString("S_UITheme")), filler(3), themeDd)
  val r2 = RowPanel(filler(10), Label(Utils.loadString("S_UIThemeHelp")), filler(10))
  val r3 = RowPanel(filler(10), Label(Utils.loadString("S_FontDelta")), filler(3), fontDd)
  val r4 = RowPanel(filler(10), Label(Utils.loadString("S_FontDeltaHelp")), filler(10))
  val r5 = RowPanel(filler(10), Label(Utils.loadString("S_SettingsRestart")))

  val r6 = RowPanel(filler(7), Label(Utils.loadString("S_ImageExport")))
  val r7 = RowPanel(
    filler(10),
    Label(Utils.loadString("S_DPI")), dpiTf, filler(3),
    Label(Utils.loadString("S_Dimension")), dimensionDd, filler(3),
    Label(Utils.loadString("S_Inches")), inchesDd, filler(3)
  )

  def changeModality(modal: Boolean): Unit = {
    setVisible(false)
    setModal(modal)
    setVisible(true)
  }

  def setCurrentAspectRatio(): Unit = {
    val cb = Builtins.instance.canvasBounds
    val r = cb.width / cb.height
    currentAspectTf.setText(f"$r%2.3f")
  }

  val adjustCanvasBtn = new JButton(Utils.loadString("S_AdjustCanvas"))
  adjustCanvasBtn.addActionListener(new ActionListener {
    def actionPerformed(e: ActionEvent): Unit = {
      changeModality(false)
      PaperSize.fromString(aspectTf.value) match {
        case Some(ps) =>
          ps match {
            case A4 =>
              Builtins.instance.setDrawingCanvasToA4()
            case A4Landscape =>
              Builtins.instance.setDrawingCanvasToA4Landscape()
            case A3 =>
              Builtins.instance.setDrawingCanvasToA3()
            case A3Landscape =>
              Builtins.instance.setDrawingCanvasToA3Landscape()
            case A2 =>
              Builtins.instance.setDrawingCanvasToA2()
            case A2Landscape =>
              Builtins.instance.setDrawingCanvasToA2Landscape()
            case A1 =>
              Builtins.instance.setDrawingCanvasToA1()
            case A1Landscape =>
              Builtins.instance.setDrawingCanvasToA1Landscape()
            case A0 =>
              Builtins.instance.setDrawingCanvasToA0()
            case A0Landscape =>
              Builtins.instance.setDrawingCanvasToA0Landscape()
          }
          Utils.schedule(0.3) {
            setCurrentAspectRatio()
          }
        case None =>
          try {
            val r = aspectTf.value.toDouble
            Builtins.instance.setDrawingCanvasAspectRatio(r)
            Utils.schedule(0.3) {
              setCurrentAspectRatio()
            }
          }
          catch {
            case throwable: Throwable =>
          }
      }
    }
  })
  val aspectTf = TextField("")
  aspectTf.setColumns(8)
  handleInchesDdSelection(inchesDd.value)
  val currentAspectTf = Label("")
  setCurrentAspectRatio()

  val r8 = RowPanel(filler(10), Label(Utils.loadString("S_CanvasAspectRatio")), aspectTf, adjustCanvasBtn, filler(3))
  val r9 = RowPanel(filler(10), Label(Utils.loadString("S_CanvasCurrentAspectRatio")), currentAspectTf, filler(3))

  val okCancel = new JPanel
  val ok = new JButton(Utils.loadString("S_OK"))
  ok.addActionListener(new ActionListener {
    def actionPerformed(ev: ActionEvent): Unit = {
      val newFontIncrease = fontDd.value.toString
      val newTheme = themeDd.value
      var newInches = {
        val v = inchesDd.value
        PaperSize.fromString(v) match {
          case Some(ps) => ps.name
          case None =>
            try {
              assert(v.toDouble > 0)
              v
            }
            catch {
              case throwable: Throwable => ""
            }
        }
      }
      val newDpi = if (newInches.trim == "") "" else {
        val v = dpiTf.value
        try {
          assert(v.toInt > 0)
          v
        }
        catch {
          case throwable: Throwable =>
            newInches = ""
            ""
        }
      }
      val newDimension = dimensionDd.value
      val m = Map(
        "theme" -> newTheme,
        "font.increase" -> newFontIncrease,
        "export.image.dpi" -> newDpi,
        "export.image.inches" -> newInches,
        "export.image.dimension" -> newDimension
      )
      Utils.updateAppProperties(m)
      setVisible(false)
    }
  })
  val cancel = new JButton(Utils.loadString("S_Cancel"))
  cancel.addActionListener(new ActionListener {
    def actionPerformed(ev: ActionEvent): Unit = {
      setVisible(false)
    }
  })
  okCancel.add(ok)
  okCancel.add(cancel)

  val d1 = ColPanel(filler(1), r1, r2, r3, r4, r5, filler(1))
  val d2 = ColPanel(filler(1), r6, r7, r9, r8, ColPanel.verticalGlue, ColPanel.verticalGlue, filler(1))

  setTitle(Utils.loadString("S_Settings"))
  setModal(true)
  getRootPane.setDefaultButton(ok)
  val tabbedPane = new JTabbedPane()
  tabbedPane.add(Utils.loadString("S_System"), d1)
  tabbedPane.add(Utils.loadString("S_Export"), d2)
  //  tabbedPane.addChangeListener((e: ChangeEvent) => {
  //    changeModality(true)
  //  })

  val d = ColPanel(filler(1), tabbedPane, okCancel)
  getContentPane.add(d)
  pack()
  setLocationRelativeTo(owner)
  Utils.closeOnEsc(this)
}
