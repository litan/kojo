package net.kogics.kojo.lite.topc

import javax.swing.JComponent
import bibliothek.gui.dock.common.DefaultSingleCDockable
import java.awt.Color
import bibliothek.gui.dock.common.event.CFocusListener
import bibliothek.gui.dock.common.intern.CDockable
import net.kogics.kojo.core.KojoCtx

class MathworldHolder(val mw: JComponent, ctx: KojoCtx) extends BaseHolder("MW", "Mathworld", null.asInstanceOf[JComponent]) {

  var added = false

  this.addFocusListener(new CFocusListener {
    override def focusGained(dockable: CDockable) {
      ctx.mwActivated()
      if (!added) {
        add(mw)
        added = true
      }
    }

    override def focusLost(dockable: CDockable) {
      if (!dockable.isShowing) {
        remove(mw)
        added = false
      }
    }
  })

  def otherPaneActivated() {
    if (added) {
      remove(mw)
      added = false
    }
  }
}

