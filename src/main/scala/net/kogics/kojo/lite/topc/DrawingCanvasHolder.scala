package net.kogics.kojo.lite.topc

import javax.swing.JComponent
import bibliothek.gui.dock.common.DefaultSingleCDockable
import java.awt.Color

class DrawingCanvasHolder(val dc: JComponent) extends DefaultSingleCDockable("DC", "Drawing Canvas", dc) {
	dc.setBackground(Color.white)
}