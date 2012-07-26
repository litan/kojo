package net.kogics.kojo.lite.topc

import javax.swing.JComponent
import bibliothek.gui.dock.common.DefaultSingleCDockable
import java.awt.Color
import javax.swing.JTextArea

class ScriptEditorHolder(val se: JComponent, codePane: JTextArea) extends DefaultSingleCDockable("SE", "Script Editor", se) {
  se.setBackground(Color.white)
  
  def activate() {
    toFront()
    codePane.requestFocusInWindow()
  }
}