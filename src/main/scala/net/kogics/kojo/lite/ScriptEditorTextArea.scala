package net.kogics.kojo.lite

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea

private[lite] class ScriptEditorTextArea(rows: Int, columns: Int, linePadding: Int)
    extends RSyntaxTextArea(rows, columns) {
  // Include the language's extra glyph space in both layout and repainting.
  override def getLineHeight: Int = super.getLineHeight + linePadding
}
