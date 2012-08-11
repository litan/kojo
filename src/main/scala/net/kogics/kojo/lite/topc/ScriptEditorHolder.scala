package net.kogics.kojo.lite.topc

import java.awt.BorderLayout
import java.awt.Color
import org.fife.ui.autocomplete.AutoCompletion
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.SyntaxConstants
import org.fife.ui.rtextarea.RTextScrollPane
import bibliothek.gui.dock.common.DefaultSingleCDockable
import javax.swing.event.PopupMenuEvent
import javax.swing.event.PopupMenuListener
import javax.swing.JCheckBoxMenuItem
import javax.swing.JPanel
import javax.swing.JPopupMenu
import net.kogics.kojo.codingmode.SwitchMode
import net.kogics.kojo.lite.KojoCompletionProvider
import net.kogics.kojo.util.Utils
import net.kogics.kojo.lite.CodeExecutionSupport
import org.fife.ui.rsyntaxtextarea.RSyntaxTextAreaEditorKit.IncreaseFontSizeAction
import org.fife.ui.rsyntaxtextarea.TokenTypes
import org.fife.ui.rsyntaxtextarea.Style

class ScriptEditorHolder(val se: JPanel, codePane: RSyntaxTextArea, codeSupport: CodeExecutionSupport) extends BaseHolder("SE", "Script Editor", se) {

  codeSupport.toolbar.setOpaque(true)
  codeSupport.toolbar.setBackground(new Color(230, 230, 230))

  codePane.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SCALA)
  codePane.setAntiAliasingEnabled(true)
  codePane.setAnimateBracketMatching(false)
  codePane.setCloseCurlyBraces(true)
  codePane.setTabsEmulated(true)
  codePane.setTabSize(4)
//  codePane.setMarkOccurrences(true)
  codePane.getSyntaxScheme.setStyle(TokenTypes.SEPARATOR, new Style(Color.blue))
  new IncreaseFontSizeAction().actionPerformedImpl(null, codePane)

  val provider = new KojoCompletionProvider(codeSupport)
  val ac = new AutoCompletion(provider)
  ac.setParameterAssistanceEnabled(true)
  ac.setAutoActivationEnabled(true)
  ac.setShowDescWindow(true)
  ac.install(codePane)

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

  val d3Cb = new JCheckBoxMenuItem(switcher)
  d3Cb.setText(Utils.loadString("S_D3Mode"))
  d3Cb.setToolTipText(Utils.loadString("S_D3ModeTT"))
  d3Cb.setActionCommand("D3")
  popup.add(d3Cb, 6)

  popup.addPopupMenuListener(new PopupMenuListener {
    def popupMenuWillBecomeVisible(e: PopupMenuEvent) {
      switcher.updateCb(twCb)
      switcher.updateCb(stagingCb)
      switcher.updateCb(mwCb)
      switcher.updateCb(d3Cb)
    }
    def popupMenuWillBecomeInvisible(e: PopupMenuEvent) {}

    def popupMenuCanceled(e: PopupMenuEvent) {}
  })

  def activate() {
    toFront()
    codePane.requestFocusInWindow()
  }
}