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
import javax.swing.KeyStroke
import java.awt.event.KeyEvent
import java.awt.event.InputEvent
import javax.swing.AbstractAction

class OutputWindowHolder(val ow: JTextArea, val ew: JEditorPane, val oPanel: JPanel, ctx: core.KojoCtx)
  extends BaseHolder("OW", Utils.loadString("CTL_OutputTopComponent"), oPanel) {

  var fontSize = 13
  def updateFont() {
    ow.setFont(new Font(Font.MONOSPACED, Font.PLAIN, fontSize))
  }
  updateFont()
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

    
    val increaseFontSizeAction = new AbstractAction(Utils.loadString("S_IncreaseFontSize")) {
      override def actionPerformed(e: ActionEvent) {
        fontSize += 1
        updateFont()
      }
    }
    val incrFontSizeItem = new JMenuItem(increaseFontSizeAction)
    val inputMap = ow.getInputMap
    val am = ow.getActionMap
    val controlPlus = KeyStroke.getKeyStroke(KeyEvent.VK_ADD, InputEvent.CTRL_MASK)
    inputMap.put(controlPlus, "increase-font-size")
    am.put("increase-font-size", increaseFontSizeAction)
    incrFontSizeItem.setAccelerator(controlPlus)
    add(incrFontSizeItem)

    val decreaseFontSizeAction = new AbstractAction(Utils.loadString("S_DecreaseFontSize")) {
      override def actionPerformed(e: ActionEvent) {
        fontSize -= 1
        updateFont()
      }
    }
    val decrFontSizeItem = new JMenuItem(decreaseFontSizeAction)
    val controlMinus = KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, InputEvent.CTRL_MASK)
    inputMap.put(controlMinus, "decrease-font-size")
    am.put("decrease-font-size", decreaseFontSizeAction)
    decrFontSizeItem.setAccelerator(controlMinus)
    add(decrFontSizeItem)

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