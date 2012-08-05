package net.kogics.kojo.lite.topc

import javax.swing.JComponent
import bibliothek.gui.dock.common.DefaultSingleCDockable
import java.awt.Color
import javax.swing.JTextArea
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import javax.swing.JMenuItem
import org.fife.ui.rsyntaxtextarea.SyntaxConstants
import javax.swing.JPopupMenu
import net.kogics.kojo.lite.KojoCompletionProvider
import org.fife.ui.autocomplete.AutoCompletion
import net.kogics.kojo.CodeExecutionSupport
import java.awt.BorderLayout
import javax.swing.JPanel
import org.fife.ui.rtextarea.RTextScrollPane
import net.kogics.kojo.codingmode.SwitchMode
import javax.swing.JCheckBoxMenuItem
import net.kogics.kojo.util.Utils
import javax.swing.event.PopupMenuListener
import javax.swing.event.PopupMenuEvent

class ScriptEditorHolder(val se: JPanel, codePane: RSyntaxTextArea, codeSupport: CodeExecutionSupport) extends DefaultSingleCDockable("SE", "Script Editor", se) {
  se.setBackground(Color.white)
  codeSupport.toolbar.setOpaque(true)
  codeSupport.toolbar.setBackground(new Color(230, 230, 230))

  codePane.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SCALA)
  codePane.setAntiAliasingEnabled(true)

  val provider = new KojoCompletionProvider(codeSupport)
  val ac = new AutoCompletion(provider)
  ac.install(codePane)
  ac.setParameterAssistanceEnabled(true)
  ac.setAutoActivationEnabled(true)

  val sp = new RTextScrollPane(codePane)
  se.setLayout(new BorderLayout)
  se.add(codeSupport.toolbar, BorderLayout.NORTH)
  se.add(sp, BorderLayout.CENTER)
  se.add(codeSupport.statusStrip, BorderLayout.EAST)

  val popup = codePane.getPopupMenu
  popup.add(new JPopupMenu.Separator, 2)

  val switcher = new SwitchMode()
  val twCb = new JCheckBoxMenuItem(switcher)
  twCb.setText(Utils.loadString("S_TurtleMode"))
  twCb.setToolTipText(Utils.loadString("S_TurtleModeTT"))
  twCb.setActionCommand("Tw")
  popup.add(twCb, 3)

  val stagingCb = new JCheckBoxMenuItem(switcher)
  stagingCb.setText(Utils.loadString("S_StagingMode"))
  stagingCb.setToolTipText(Utils.loadString("S_StagingModeTT"))
  stagingCb.setActionCommand("Staging")
  popup.add(stagingCb, 4)

  val mwCb = new JCheckBoxMenuItem(switcher)
  mwCb.setText(Utils.loadString("S_MwMode"))
  mwCb.setToolTipText(Utils.loadString("S_MwModeTT"))
  mwCb.setActionCommand("Mw")
  popup.add(mwCb, 5)

  popup.addPopupMenuListener(new PopupMenuListener {
    def popupMenuWillBecomeVisible(e: PopupMenuEvent) {
      switcher.updateCb(twCb)
      switcher.updateCb(stagingCb)
      switcher.updateCb(mwCb)
    }
    def popupMenuWillBecomeInvisible(e: PopupMenuEvent) {}

    def popupMenuCanceled(e: PopupMenuEvent) {}
  })

  def activate() {
    toFront()
    codePane.requestFocusInWindow()
  }
}