package net.kogics.kojo
package lite
package topc

import java.awt.Color
import java.awt.Font
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent
import java.awt.event.InputEvent
import java.awt.event.KeyEvent

import javax.swing.AbstractAction
import javax.swing.JCheckBoxMenuItem
import javax.swing.JEditorPane
import javax.swing.JMenuItem
import javax.swing.JPanel
import javax.swing.JPopupMenu
import javax.swing.JTextPane
import javax.swing.KeyStroke
import javax.swing.UIDefaults
import javax.swing.event.PopupMenuEvent
import javax.swing.event.PopupMenuListener

import net.kogics.kojo.action.FullScreenOutputAction
import net.kogics.kojo.action.FullScreenSupport
import net.kogics.kojo.util.NoOpPainter
import net.kogics.kojo.util.Utils

class OutputWindowHolder(val ow: JTextPane, val ew: JEditorPane, val oPanel: JPanel, ctx: core.KojoCtx)
  extends BaseHolder("OW", Utils.loadString("CTL_OutputTopComponent"), oPanel) {

  val tdef = new UIDefaults();
  tdef.put("TextPane[Enabled].backgroundPainter", new NoOpPainter);
  ow.putClientProperty("Nimbus.Overrides", tdef);

  var fontSize = 13
  def updateFont() {
    ow.setFont(new Font(Font.MONOSPACED, Font.PLAIN, fontSize))
  }
  updateFont()
  ow.setForeground(new Color(32, 32, 32))
  ow.setBackground(Color.white)

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

    ew.addFocusListener(new FocusAdapter {
      override def focusGained(e: FocusEvent) {
        incrFontSizeItem.setEnabled(false)
        decrFontSizeItem.setEnabled(false)

      }
    })

    ow.addFocusListener(new FocusAdapter {
      override def focusGained(e: FocusEvent) {
        incrFontSizeItem.setEnabled(true)
        decrFontSizeItem.setEnabled(true)

      }
    })

    addSeparator()

    val clearItem = new JMenuItem(Utils.loadString("S_Clear"))
    clearItem.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent) {
        ctx.clearOutput()
      }
    })
    add(clearItem)

    addSeparator()

    val fsOutputAction = FullScreenOutputAction(ctx)
    val fullScreenItem: JCheckBoxMenuItem = new JCheckBoxMenuItem(fsOutputAction)
    add(fullScreenItem)

    addPopupMenuListener(new PopupMenuListener {
      def popupMenuWillBecomeVisible(e: PopupMenuEvent) {
        verboseOutput.setState(ctx.isVerboseOutput)
        showCode.setState(ctx.isSriptShownInOutput)
        FullScreenSupport.updateMenuItem(fullScreenItem, fsOutputAction)
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