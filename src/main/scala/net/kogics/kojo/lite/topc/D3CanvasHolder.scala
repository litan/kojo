package net.kogics.kojo.lite.topc

import javax.swing.JComponent
import bibliothek.gui.dock.common.DefaultSingleCDockable
import java.awt.Color
import bibliothek.gui.dock.common.event.CDockableStateListener
import bibliothek.gui.dock.common.event.CDockableAdapter
import bibliothek.gui.dock.common.intern.CDockable
import bibliothek.gui.dock.common.mode.ExtendedMode
import bibliothek.gui.dock.common.event.CFocusListener
import net.kogics.kojo.core.KojoCtx
import net.kogics.kojo.util.Utils

class D3CanvasHolder(val d3: JComponent, ctx: KojoCtx)
  extends BaseHolder("D3", Utils.loadString("CTL_D3CanvasTopComponent"), null.asInstanceOf[JComponent]) {

  var added = false

  this.addFocusListener(new CFocusListener {
    override def focusGained(dockable: CDockable) {
      ctx.d3Activated()
      if (!added) {
        add(d3)
        added = true
      }
    }

    override def focusLost(dockable: CDockable) {
      if (!dockable.isShowing) {
        remove(d3)
        added = false
      }
    }
  })

  def otherPaneActivated() {
    if (added) {
      remove(d3)
      added = false
    }
  }
}

