package net.kogics.kojo.lite

import java.awt.FlowLayout
import java.awt.Font
import javax.swing.JLabel
import javax.swing.JPanel
import java.awt.Dimension

class StatusBar extends JPanel {
  setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0))
  setPreferredSize(new Dimension(0, 16))
  val statusText = new JLabel
  statusText.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11))
  add(statusText)

  def showText(text: String) {
    statusText.setText(text)
  }
}