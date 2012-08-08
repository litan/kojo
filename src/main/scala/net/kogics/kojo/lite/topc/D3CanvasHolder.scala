package net.kogics.kojo.lite.topc

import javax.swing.JComponent
import bibliothek.gui.dock.common.DefaultSingleCDockable
import java.awt.Color

class D3CanvasHolder(val d3: JComponent) extends DefaultSingleCDockable("D3", "3D Canvas", d3) {
	d3.setBackground(Color.white)
}

