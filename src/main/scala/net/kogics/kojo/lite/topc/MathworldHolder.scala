package net.kogics.kojo.lite.topc

import javax.swing.JComponent
import bibliothek.gui.dock.common.DefaultSingleCDockable
import java.awt.Color

class MathworldHolder(val mw: JComponent) extends DefaultSingleCDockable("MW", "Mathworld", mw) {
	mw.setBackground(Color.white)
}

