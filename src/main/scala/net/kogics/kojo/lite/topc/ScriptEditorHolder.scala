package net.kogics.kojo.lite.topc

import net.kogics.kojo.lite.ScriptEditor
import net.kogics.kojo.util.Utils

class ScriptEditorHolder(val se: ScriptEditor)
  extends BaseHolder("SE", Utils.loadString("CTL_CodeEditorTopComponent"), se) {

  def activate() {
    toFront()
    se.activate()
  }
}