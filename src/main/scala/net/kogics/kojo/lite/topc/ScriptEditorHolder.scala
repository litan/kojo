package net.kogics.kojo.lite.topc

import javax.swing.JComponent
import bibliothek.gui.dock.common.DefaultSingleCDockable
import java.awt.Color

class ScriptEditorHolder(se: JComponent) extends DefaultSingleCDockable("SE", "Script Editor", se) {
  se.setBackground(Color.white)
}