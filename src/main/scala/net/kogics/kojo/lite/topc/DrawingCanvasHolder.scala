package net.kogics.kojo.lite.topc

import net.kogics.kojo.core.KojoCtx
import net.kogics.kojo.lite.canvas.SpriteCanvas
import net.kogics.kojo.util.Utils

import bibliothek.gui.dock.common.event.CFocusListener
import bibliothek.gui.dock.common.intern.CDockable

class DrawingCanvasHolder(val dc: SpriteCanvas, ctx: KojoCtx)
  extends BaseHolder("DC", Utils.loadString("CTL_SCanvasTopComponent"), dc) {

  this.addFocusListener(new CFocusListener {
    override def focusGained(dockable: CDockable): Unit = {
      ctx.drawingCanvasActivated()
    }

    override def focusLost(dockable: CDockable): Unit = {
    }
  })

  def activate(): Unit = {
    toFront()
    dc.activate()
  }
  
  // called when only the canvas and not the holder needs to be activate (e.g when Fullscreen-ing)
  def activateCanvas(): Unit = {
    dc.activate()
    ctx.drawingCanvasActivated()
  }
}