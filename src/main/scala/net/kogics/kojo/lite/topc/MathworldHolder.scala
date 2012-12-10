package net.kogics.kojo.lite.topc

import net.kogics.kojo.core.KojoCtx
import net.kogics.kojo.util.Utils

import bibliothek.gui.dock.common.event.CFocusListener
import bibliothek.gui.dock.common.intern.CDockable
import javax.swing.JComponent

class MathworldHolder(val mw: JComponent, ctx: KojoCtx)
  extends BaseHolder("MW", Utils.loadString("CTL_GeoGebraTopComponent"), null.asInstanceOf[JComponent]) {

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

