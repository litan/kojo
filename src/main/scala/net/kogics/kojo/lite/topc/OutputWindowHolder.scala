package net.kogics.kojo.lite
package topc

import java.awt.Color
import java.awt.Font
import bibliothek.gui.dock.common.DefaultSingleCDockable
import javax.swing.JComponent
import javax.swing.JScrollPane
import javax.swing.JTextArea
import sun.swing.SwingUtilities2

class OutputWindowHolder(val ow: JTextArea) extends BaseHolder("OW", "Output Pane", new JScrollPane(ow)) {

  ow.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13))
  ow.setForeground(new Color(32, 32, 32))
  ow.setLineWrap(true)
  ow.setWrapStyleWord(true)

  // should be called in swing thread
  def scrollToEnd() {
    ow.setCaretPosition(ow.getDocument.getLength)
  }
}