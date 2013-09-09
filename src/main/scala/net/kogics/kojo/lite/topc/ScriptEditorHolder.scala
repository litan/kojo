package net.kogics.kojo.lite.topc

import net.kogics.kojo.lite.ScriptEditor
import net.kogics.kojo.util.Utils

class ScriptEditorHolder(val se: ScriptEditor)
  extends BaseHolder("SE", Utils.loadString("CTL_CodeEditorTopComponent"), se) {
  val title = getTitleText
  val scratch = Utils.loadString("S_Scratch")
  
  setNoFileTitle()

  def activate() {
    toFront()
    se.activate()
  }

  def fileOpened(fileName: String) {
    setTitleText("%s - %s" format (title, fileName))
  }

  def fileModified(fileName: String) {
    setTitleText("%s - %s*" format (title, fileName))
  }

  def fileSaved(fileName: String) {
    setTitleText("%s - %s" format (title, fileName))
  }

  def fileClosed(fileName: String) {
    setNoFileTitle()
  }

  def setNoFileTitle() {
    if (se.kojoCtx.subKojo) {
      setTitleText("%s ( %s )" format (title, scratch))
    }
    else {
      setTitleText(title)
    }
  }
}