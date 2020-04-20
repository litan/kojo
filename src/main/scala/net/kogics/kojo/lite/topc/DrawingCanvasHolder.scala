package net.kogics.kojo.lite.topc

import net.kogics.kojo.core.KojoCtx
import net.kogics.kojo.lite.Theme
import net.kogics.kojo.lite.canvas.SpriteCanvas
import net.kogics.kojo.util.Utils

class DrawingCanvasHolder(val dc: SpriteCanvas, ctx: KojoCtx)
  extends BaseHolder("DC", Utils.loadString("CTL_SCanvasTopComponent"), dc) {
  dc.setBackground(Theme.currentTheme.canvasBg)
  def activate(): Unit = {
    toFront()
    dc.activate()
  }

  // called when only the canvas and not the holder needs to be activate (e.g when Fullscreen-ing)
  def activateCanvas(): Unit = {
    dc.activate()
  }
}