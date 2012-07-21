package net.kogics.kojo.lite.topc

import javax.swing.JComponent
import bibliothek.gui.dock.common.DefaultSingleCDockable
import java.awt.Color

class StoryTellerHolder(val st: JComponent) extends DefaultSingleCDockable("ST", "StoryTeller", st) {
  st.setBackground(Color.white)
}