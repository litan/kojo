package net.kogics.kojo.lite

import java.awt.BorderLayout
import java.awt.Color
import java.awt.Font

import javax.swing.BorderFactory
import javax.swing.JLabel
import javax.swing.JPanel

class StatusBar extends JPanel {
  setLayout(new BorderLayout())
  val statusText = new JLabel
  statusText.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11))
  statusText.setBorder(BorderFactory.createLineBorder(Color.lightGray))
  add(statusText, BorderLayout.CENTER)

  val lineCol = new JLabel
  lineCol.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12))
  lineCol.setBorder(BorderFactory.createLineBorder(Color.lightGray))
  add(lineCol, BorderLayout.EAST)

  def showText(text: String) {
    statusText.setText(text)
  }

  def showCaretPos(line: Int, col: Int) {
    lineCol.setText(s"${" " * 12}$line | $col${" " * 12}")
  }
}