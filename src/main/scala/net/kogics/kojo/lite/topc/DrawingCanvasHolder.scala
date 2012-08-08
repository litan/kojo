package net.kogics.kojo.lite.topc

import javax.swing.JComponent
import bibliothek.gui.dock.common.DefaultSingleCDockable
import java.awt.Color
import bibliothek.gui.dock.common.event.CFocusListener
import bibliothek.gui.dock.common.intern.CDockable
import net.kogics.kojo.core.KojoCtx

class DrawingCanvasHolder(val dc: JComponent, ctx: KojoCtx) extends DefaultSingleCDockable("DC", "Drawing Canvas", dc) {
  dc.setBackground(Color.white)

  this.addFocusListener(new CFocusListener {
    override def focusGained(dockable: CDockable) {
      ctx.drawingCanvasActivated()
    }

    override def focusLost(dockable: CDockable) {
    }
  })
}