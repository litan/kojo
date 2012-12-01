package net.kogics.kojo.lite.topc

import javax.swing.JComponent
import bibliothek.gui.dock.common.event.CFocusListener
import bibliothek.gui.dock.common.intern.CDockable
import net.kogics.kojo.history.HistoryPanel

class HistoryHolder(val hw: HistoryPanel) extends BaseHolder("HW", "History Pane", hw) {
  addFocusListener(new CFocusListener {
    override def focusGained(dockable: CDockable) {
      hw.searchField.requestFocusInWindow()
    }

    override def focusLost(dockable: CDockable) {
    }
  })
}
