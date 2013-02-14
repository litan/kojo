package net.kogics.kojo
package lite
package topc

import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent
import java.awt.event.InputEvent
import java.awt.event.KeyEvent

import javax.swing.AbstractAction
import javax.swing.JCheckBoxMenuItem
import javax.swing.JMenuItem
import javax.swing.JPopupMenu
import javax.swing.KeyStroke
import javax.swing.UIDefaults
import javax.swing.event.PopupMenuEvent
import javax.swing.event.PopupMenuListener

import net.kogics.kojo.action.FullScreenOutputAction
import net.kogics.kojo.action.FullScreenSupport
import net.kogics.kojo.lite.CodeExecutionSupport
import net.kogics.kojo.util.NoOpPainter
import net.kogics.kojo.util.Utils

class OutputWindowHolder(val outputPane: OutputPane)
  extends BaseHolder("OW", Utils.loadString("CTL_OutputTopComponent"), outputPane) {

  def scrollToEnd() = outputPane.scrollToEnd()
}