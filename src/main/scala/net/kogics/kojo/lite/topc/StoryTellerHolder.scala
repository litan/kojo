package net.kogics.kojo.lite.topc

import javax.swing.JComponent
import bibliothek.gui.dock.common.DefaultSingleCDockable
import java.awt.Color
import net.kogics.kojo.util.Utils

class StoryTellerHolder(val st: JComponent)
  extends BaseHolder("ST", Utils.loadString("CTL_StoryTellerTopComponent"), st) 