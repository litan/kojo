package net.kogics.kojo.lite.topc

import net.kogics.kojo.lite.OutputPane
import net.kogics.kojo.util.Utils

class OutputWindowHolder(val outputPane: OutputPane)
  extends BaseHolder("OW", Utils.loadString("CTL_OutputTopComponent"), outputPane) {

  def scrollToEnd() = outputPane.scrollToEnd()
}