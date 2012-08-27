package net.kogics.kojo.lite
package topc

import java.awt.Color
import java.awt.Font

import javax.swing.JEditorPane
import javax.swing.JPanel
import javax.swing.JTextArea

class OutputWindowHolder(val ow: JTextArea, val ew: JEditorPane, val oPanel: JPanel)
    extends BaseHolder("OW", "Output Pane", oPanel) {

  ow.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13))
  ow.setForeground(new Color(32, 32, 32))
  ow.setLineWrap(true)
  ow.setWrapStyleWord(true)

  // should be called in swing thread
  def scrollToEnd() {
    ow.setCaretPosition(ow.getDocument.getLength)
  }
}