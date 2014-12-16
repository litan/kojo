package net.kogics.kojo.lite.topc

import javax.swing.JComponent
import bibliothek.gui.dock.common.event.CFocusListener
import bibliothek.gui.dock.common.intern.CDockable
import net.kogics.kojo.history.HistoryPanel
import net.kogics.kojo.util.Utils

class HistoryHolder(val hw: HistoryPanel)
  extends BaseHolder("HW", Utils.loadString("CTL_HistoryTopComponent"), hw) {
 
  addFocusListener(new CFocusListener {
    override def focusGained(dockable: CDockable) {
      hw.searchField.requestFocusInWindow()
    }

    override def focusLost(dockable: CDockable) {
    }
  })
}
