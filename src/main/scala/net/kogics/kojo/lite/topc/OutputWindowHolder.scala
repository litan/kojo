package net.kogics.kojo.lite
package topc

import java.awt.Color
import java.awt.Font
import bibliothek.gui.dock.common.DefaultSingleCDockable
import javax.swing.JComponent
import javax.swing.JScrollPane
import javax.swing.JTextArea
import sun.swing.SwingUtilities2

class OutputWindowHolder(val ow: JTextArea) extends DefaultSingleCDockable("OW", "Output Pane", new JScrollPane(ow)) {
  ow.setBackground(Color.white)
  ow.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12))

  // should be called in swing thread
  def scrollToEnd() {
    ow.setCaretPosition(ow.getDocument.getLength)
  }
}