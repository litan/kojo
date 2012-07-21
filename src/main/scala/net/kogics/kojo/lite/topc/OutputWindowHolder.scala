package net.kogics.kojo.lite.topc

import javax.swing.JComponent
import bibliothek.gui.dock.common.DefaultSingleCDockable
import java.awt.Color

class OutputWindowHolder(ow: JComponent) extends DefaultSingleCDockable("OW", "Output Pane", ow) {
  ow.setBackground(Color.white)
}