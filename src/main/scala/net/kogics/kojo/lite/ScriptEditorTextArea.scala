package net.kogics.kojo.lite

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea

private[lite] class ScriptEditorTextArea(rows: Int, columns: Int, hindi: Boolean)
    extends RSyntaxTextArea(rows, columns) {
  // At some font sizes (e.g. 17 pt), Noto's lower matras extend one pixel beyond
  // RSyntaxTextArea's line metrics. Include that pixel in layout and repainting.
  override def getLineHeight: Int = super.getLineHeight + (if (hindi) 1 else 0)
}
