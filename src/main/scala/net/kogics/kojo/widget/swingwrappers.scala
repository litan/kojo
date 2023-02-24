package net.kogics.kojo.widget

import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.HierarchyEvent
import java.awt.event.HierarchyListener
import java.awt.Color
import java.awt.FlowLayout
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.DefaultComboBoxModel
import javax.swing.JButton
import javax.swing.JComboBox
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JSlider
import javax.swing.JTextArea
import javax.swing.JTextField
import javax.swing.JToggleButton

import net.kogics.kojo.lite.Theme
import net.kogics.kojo.util.Constants
import net.kogics.kojo.util.Read
import net.kogics.kojo.util.Utils

//def borderWithMargin(m: Int) = {
//    import javax.swing.border._
//    import javax.swing.BorderFactory
//    val outsideBorder = BorderFactory.createLineBorder(color(128, 128, 128))
//    val insideBorder = new EmptyBorder(m, m, m, m)
//    new CompoundBorder(outsideBorder, insideBorder)
//}

trait PreferredMax { self: JComponent =>
  override def getMaximumSize = getPreferredSize
}

trait Focusable { self: JComponent =>
  def takeFocus() = {
    if (isShowing) Utils.runLaterInSwingThread {
      requestFocusInWindow()
    }
    else {
      lazy val hl: HierarchyListener = new HierarchyListener {
        def hierarchyChanged(e: HierarchyEvent): Unit = {
          if ((e.getChangeFlags & HierarchyEvent.SHOWING_CHANGED) != 0 && isShowing) {
            Utils.schedule(0.1) { requestFocusInWindow() }
            Utils.schedule(0.9) { requestFocusInWindow() }
            removeHierarchyListener(hl)
          }
        }
      }
      addHierarchyListener(hl)
    }
  }
}

object RowPanel {
  def horizontalGlue = Box.createHorizontalGlue().asInstanceOf[JComponent]
  def horizontalGap(width: Int) = Box.createHorizontalStrut(width).asInstanceOf[JComponent]
}

case class RowPanel(comps: JComponent*) extends JPanel {
  setBackground(Theme.currentTheme.defaultBg)
  setLayout(new FlowLayout(FlowLayout.LEFT))
  comps.foreach { add(_) }
}

object ColPanel {
  def verticalGlue = {
    Box.createVerticalGlue().asInstanceOf[JComponent]
  }
  def verticalGap(height: Int) = Box.createVerticalStrut(height).asInstanceOf[JComponent]
}

case class ColPanel(comps: JComponent*) extends JPanel with PreferredMax {
  setBackground(Theme.currentTheme.defaultBg)
  setLayout(new BoxLayout(this, BoxLayout.Y_AXIS))
  comps.foreach { add(_) }
}

case class TextField[T](default: T)(implicit reader: Read[T]) extends JTextField(6) with Focusable {
  setText(default.toString)
  def value = reader.read(getText)
  def value_=(t: T): Unit = { setValue(t) }
  def setValue(t: T): Unit = { setText(t.toString) }
  def onEnter(oe: T => Unit): Unit = {
    addActionListener(new ActionListener {
      def actionPerformed(e: ActionEvent): Unit = {
        oe(value)
      }
    })
  }
}
case class TextArea(default: String) extends JTextArea with Focusable {
  setText(default)
  def value = getText
}
case class Label(label: String) extends JLabel(label) {
  override def setText(t: String): Unit = {
    super.setText(t)
    getParent match {
      case parent: JComponent =>
        if (isShowing) {
          parent.revalidate()
        }
      case _ =>
    }
  }
}
case class Button(label: String)(al: => Unit) extends JButton(label) {
  addActionListener(new ActionListener {
    def actionPerformed(e: ActionEvent): Unit = {
      al
    }
  })
}
case class ToggleButton(label: String)(al: Boolean => Unit) extends JToggleButton(label) {
  addActionListener(new ActionListener {
    def actionPerformed(e: ActionEvent): Unit = {
      al(isSelected)
    }
  })
}

case class DropDown[T](origOptions: T*)(implicit reader: Read[T]) extends JComboBox[String] {
  setEditable(false)
  setOptions(origOptions: _*)
  var options = origOptions
  def value: T = reader.read(getSelectedItem.asInstanceOf[String])
  def onSelection(os: T => Unit): Unit = {
    addActionListener(new ActionListener {
      def actionPerformed(e: ActionEvent): Unit = {
        if (Constants.DropDownCanvasPadding != getSelectedItem) {
          os(value)
        }
      }
    })
  }
  def setOptions(noptions: T*): Unit = {
    var inCanvas = false
    if (getItemAt(getItemCount - 1) == Constants.DropDownCanvasPadding) {
      inCanvas = true
    }
    options = noptions
    setModel(new DefaultComboBoxModel(options.map(_.toString).toArray))
    if (inCanvas) {
      addItem(Constants.DropDownCanvasPadding)
    }
  }
}

case class Slider(min: Int, max: Int, curr: Int, spacing: Int) extends JSlider {
  setMinimum(min)
  setMaximum(max)
  setValue(curr)
  setMajorTickSpacing(spacing)
  setPaintTicks(true)
  setPaintLabels(true)
  setSnapToTicks(true)
  def value = getValue
}
