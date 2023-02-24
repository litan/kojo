package net.kogics.kojo.lite.topc

import bibliothek.gui.dock.common.event.CDockableStateListener
import bibliothek.gui.dock.common.event.CFocusListener
import bibliothek.gui.dock.common.intern.CDockable
import bibliothek.gui.dock.common.mode.ExtendedMode
import net.kogics.kojo.history.HistoryPanel
import net.kogics.kojo.util.Utils

class HistoryHolder(val hw: HistoryPanel) extends BaseHolder("HW", Utils.loadString("CTL_HistoryTopComponent"), hw) {

  addFocusListener(new CFocusListener {
    override def focusGained(dockable: CDockable): Unit = {
      hw.searchField.requestFocusInWindow()
    }

    override def focusLost(dockable: CDockable): Unit = {}
  })

  addCDockableStateListener(new CDockableStateListener {
    def visibilityChanged(cDockable: CDockable): Unit = {}

    def extendedModeChanged(cDockable: CDockable, extendedMode: ExtendedMode): Unit = {
      if (extendedMode == ExtendedMode.MINIMIZED) {
        hw.clearSearch()
      }
    }
  })
}
