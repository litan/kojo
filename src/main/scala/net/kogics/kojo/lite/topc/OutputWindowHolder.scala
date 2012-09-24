package net.kogics.kojo
package lite
package topc

import java.awt.Color
import java.awt.Font
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.JCheckBoxMenuItem
import javax.swing.JEditorPane
import javax.swing.JPanel
import javax.swing.JPopupMenu
import javax.swing.JTextArea
import net.kogics.kojo.lite.KojoCtx
import net.kogics.kojo.util.Utils
import javax.swing.event.PopupMenuListener
import javax.swing.event.PopupMenuEvent
import javax.swing.JMenuItem

class OutputWindowHolder(val ow: JTextArea, val ew: JEditorPane, val oPanel: JPanel, ctx: core.KojoCtx)
  extends BaseHolder("OW", "Output Pane", oPanel) {

  ow.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13))
  ow.setForeground(new Color(32, 32, 32))
  ow.setLineWrap(true)
  ow.setWrapStyleWord(true)

  val popup = new JPopupMenu {
    val verboseOutput = new JCheckBoxMenuItem(Utils.loadString("S_ShowVerboseOutput"))
    verboseOutput.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent) {
        if (verboseOutput.isSelected) {
          ctx.showVerboseOutput()
        }
        else {
          ctx.hideVerboseOutput()
        }
      }
    })
    add(verboseOutput)

    val showCode = new JCheckBoxMenuItem(Utils.loadString("S_ShowScriptInOutput"))
    showCode.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent) {
        if (showCode.isSelected) {
          ctx.showScriptInOutput()
        }
        else {
          ctx.hideScriptInOutput()
        }
      }
    })
    add(showCode)

    addSeparator()

    val clearItem = new JMenuItem(Utils.loadString("S_Clear"))
    clearItem.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent) {
        ctx.clearOutput()
      }
    })
    add(clearItem)

    addPopupMenuListener(new PopupMenuListener {
      def popupMenuWillBecomeVisible(e: PopupMenuEvent) {
        verboseOutput.setState(ctx.isVerboseOutput)
        showCode.setState(ctx.isSriptShownInOutput)
      }
      def popupMenuWillBecomeInvisible(e: PopupMenuEvent) {}
      def popupMenuCanceled(e: PopupMenuEvent) {}
    })
  }

  ow.setComponentPopupMenu(popup)
  ew.setComponentPopupMenu(popup)
  // should be called in swing thread
  def scrollToEnd() {
    ow.setCaretPosition(ow.getDocument.getLength)
  }
}