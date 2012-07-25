package net.kogics.kojo.lite
package topc

import javax.swing.JComponent
import bibliothek.gui.dock.common.DefaultSingleCDockable
import java.awt.Color
import javax.swing.JTextArea
import javax.swing.JScrollPane
import javax.swing.JScrollBar
import net.kogics.kojo.util.Utils

class OutputWindowHolder(val ow: JTextArea) extends DefaultSingleCDockable("OW", "Output Pane", new JScrollPane(ow)) {
  ow.setBackground(Color.white)

  // should be called in swing thread
  def scrollToEnd() {
    ow.setCaretPosition(ow.getDocument.getLength)
  }
}