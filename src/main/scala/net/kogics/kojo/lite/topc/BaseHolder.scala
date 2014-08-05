package net.kogics.kojo.lite.topc

import javax.swing.JComponent
import bibliothek.gui.dock.common.DefaultSingleCDockable
import java.awt.Color
import bibliothek.gui.dock.common.mode.ExtendedMode
import bibliothek.gui.dock.common.CLocation

class BaseHolder(id: String, title: String, component: JComponent) extends DefaultSingleCDockable(id, title, component) {
  if (component != null) {
    component.setBackground(Color.white)
  }
  setDefaultLocation(ExtendedMode.MINIMIZED, CLocation.base.minimalWest)
}