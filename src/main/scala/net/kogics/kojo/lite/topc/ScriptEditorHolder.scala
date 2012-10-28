package net.kogics.kojo.lite.topc

import java.awt.BorderLayout
import java.awt.Color
import java.awt.Point
import java.awt.Toolkit
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

import org.fife.rsta.ui.search.AbstractFindReplaceDialog
import org.fife.rsta.ui.search.ReplaceDialog
import org.fife.ui.autocomplete.AutoCompletion
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.RSyntaxTextAreaEditorKit
import org.fife.ui.rsyntaxtextarea.RSyntaxTextAreaEditorKit.DecreaseFontSizeAction
import org.fife.ui.rsyntaxtextarea.RSyntaxTextAreaEditorKit.IncreaseFontSizeAction
import org.fife.ui.rsyntaxtextarea.Style
import org.fife.ui.rsyntaxtextarea.SyntaxConstants
import org.fife.ui.rsyntaxtextarea.TokenTypes
import org.fife.ui.rsyntaxtextarea.templates.StaticCodeTemplate
import org.fife.ui.rtextarea.RTextScrollPane
import org.fife.ui.rtextarea.SearchEngine

import net.kogics.kojo.action.ChooseColor
import net.kogics.kojo.codingmode.SwitchMode
import net.kogics.kojo.lite.CodeExecutionSupport
import net.kogics.kojo.lite.KojoCompletionProvider
import net.kogics.kojo.livecoding.IpmProvider
import net.kogics.kojo.util.Utils
import net.kogics.kojo.xscala.CodeTemplates

import javax.swing.AbstractAction
import javax.swing.JCheckBoxMenuItem
import javax.swing.JFrame
import javax.swing.JMenuItem
import javax.swing.JPanel
import javax.swing.JPopupMenu
import javax.swing.KeyStroke
import javax.swing.event.PopupMenuEvent
import javax.swing.event.PopupMenuListener
import scalariform.formatter.ScalaFormatter
import scalariform.formatter.preferences.AlignParameters
import scalariform.formatter.preferences.AlignSingleLineCaseStatements
import scalariform.formatter.preferences.CompactControlReadability
import scalariform.formatter.preferences.FormatXml
import scalariform.formatter.preferences.FormattingPreferences
import scalariform.formatter.preferences.IndentSpaces
import scalariform.formatter.preferences.PreserveDanglingCloseParenthesis

class ScriptEditorHolder(val se: JPanel, codePane: RSyntaxTextArea, codeSupport: CodeExecutionSupport, frame: JFrame) extends BaseHolder("SE", "Script Editor", se) {

  codeSupport.toolbar.setOpaque(true)
  codeSupport.toolbar.setBackground(new Color(230, 230, 230))

  codePane.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SCALA)
  codePane.setAntiAliasingEnabled(true)
  codePane.setBracketMatchingEnabled(true)
  codePane.setMatchedBracketBGColor(new Color(247, 247, 247))
  codePane.setMatchedBracketBorderColor(new Color(192, 192, 192))
  codePane.setAnimateBracketMatching(false)
  codePane.setCloseCurlyBraces(false)
  codePane.setTabsEmulated(true)
  codePane.setTabSize(4)
  //  codePane.setCodeFoldingEnabled(true)
  codePane.getSyntaxScheme.setStyle(TokenTypes.SEPARATOR, new Style(Color.blue))

  val inputMap = codePane.getInputMap()
  inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_CLOSE_BRACKET, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), RSyntaxTextAreaEditorKit.rstaGoToMatchingBracketAction);

  new IncreaseFontSizeAction().actionPerformedImpl(null, codePane)

  RSyntaxTextArea.setTemplatesEnabled(true)
  val ctm = RSyntaxTextArea.getCodeTemplateManager()
  //  ctm.setInsertTrigger(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0))

  CodeTemplates.templates.foreach { kv =>
    val name = kv._1; val value = kv._2
    val ct = new StaticCodeTemplate(name, CodeTemplates.beforeCursor(name), CodeTemplates.afterCursor(name))
    ctm.addTemplate(ct)
  }

  val provider = new KojoCompletionProvider(codeSupport)
  val ac = new AutoCompletion(provider)
  ac.setParameterAssistanceEnabled(true)
  ac.setAutoActivationEnabled(true)
  ac.setShowDescWindow(true)
  ac.install(codePane)

  val sp = new RTextScrollPane(codePane)
  //  sp.setFoldIndicatorEnabled(true)
  se.setLayout(new BorderLayout)
  se.add(codeSupport.toolbar, BorderLayout.NORTH)
  se.add(sp, BorderLayout.CENTER)
  se.add(codeSupport.statusStrip, BorderLayout.EAST)

  var idx = 2
  val popup = codePane.getPopupMenu
  popup.add(new JPopupMenu.Separator, 2)
  idx += 1

  val switcher = new SwitchMode()
  val twCb = new JCheckBoxMenuItem(switcher)
  twCb.setText(Utils.loadString("S_TurtleMode"))
  twCb.setToolTipText(Utils.loadString("S_TurtleModeTT"))
  twCb.setActionCommand("Tw")
  popup.add(twCb, 3)
  idx += 1

  val stagingCb = new JCheckBoxMenuItem(switcher)
  stagingCb.setText(Utils.loadString("S_StagingMode"))
  stagingCb.setToolTipText(Utils.loadString("S_StagingModeTT"))
  stagingCb.setActionCommand("Staging")
  popup.add(stagingCb, 4)
  idx += 1

  val mwCb = new JCheckBoxMenuItem(switcher)
  mwCb.setText(Utils.loadString("S_MwMode"))
  mwCb.setToolTipText(Utils.loadString("S_MwModeTT"))
  mwCb.setActionCommand("Mw")
  popup.add(mwCb, 5)
  idx += 1

  val d3Cb = new JCheckBoxMenuItem(switcher)
  d3Cb.setText(Utils.loadString("S_D3Mode"))
  d3Cb.setToolTipText(Utils.loadString("S_D3ModeTT"))
  d3Cb.setActionCommand("D3")
  popup.add(d3Cb, 6)
  idx += 1

  popup.add(new JPopupMenu.Separator, idx)
  idx += 1

  val markOccurancesAction = new AbstractAction("Mark Occurances") {
    def actionPerformed(ev: ActionEvent) {
      if (markOccurancesItem.isSelected()) {
        codePane.setMarkOccurrences(true)
      }
      else {
        codePane.setMarkOccurrences(false)
      }
    }
  }
  val markOccurancesItem: JCheckBoxMenuItem = new JCheckBoxMenuItem(markOccurancesAction)
  popup.add(markOccurancesItem, idx)
  idx += 1

  val formatAction = new AbstractAction("Format Source") {
    import scalariform.formatter.preferences._

    def actionPerformed(ev: ActionEvent) {
      val pos = codePane.getCaretPosition()
      codePane.setText(ScalaFormatter.format(
        codePane.getText,
        new FormattingPreferences(
          Map(
            IndentSpaces -> 4,
            CompactControlReadability -> true,
            AlignParameters -> true,
            AlignSingleLineCaseStatements -> true,
            PreserveDanglingCloseParenthesis -> true,
            FormatXml -> false
          )
        )
      ))
      codePane.setCaretPosition(pos)
    }
  }

  val formatItem = new JMenuItem(formatAction)
  val csf = KeyStroke.getKeyStroke("control shift F")
  inputMap.put(csf, "format-src")
  val am = codePane.getActionMap()
  am.put("format-src", formatAction)
  formatItem.setAccelerator(csf)
  popup.add(formatItem, idx)
  idx += 1

  val findReplaceAction = new AbstractAction("Find/Replace") {
    lazy val dialog: ReplaceDialog = new ReplaceDialog(frame, listener)
    lazy val listener = new ActionListener {
      def actionPerformed(ev: ActionEvent) {
        ev.getActionCommand match {
          case AbstractFindReplaceDialog.ACTION_FIND =>
            val found = SearchEngine.find(codePane, dialog.getSearchContext());
          case AbstractFindReplaceDialog.ACTION_REPLACE =>
            val found = SearchEngine.replace(codePane, dialog.getSearchContext());
          case AbstractFindReplaceDialog.ACTION_REPLACE_ALL =>
            val found = SearchEngine.replaceAll(codePane, dialog.getSearchContext());
          case _ =>
        }
      }
    }
    def actionPerformed(ev: ActionEvent) {
      dialog.setTitle("Find / Replace")
      dialog.setVisible(true)
    }
  }
  val findReplaceItem = new JMenuItem(findReplaceAction)
  val cf = KeyStroke.getKeyStroke("control F")
  inputMap.put(cf, "find-replace")
  am.put("find-replace", findReplaceAction)
  findReplaceItem.setAccelerator(cf)
  popup.add(findReplaceItem, idx)
  idx += 1

  val chooseColorItem = new JMenuItem(new ChooseColor(codeSupport.kojoCtx))
  popup.add(chooseColorItem, idx)
  idx += 1

  popup.add(new JPopupMenu.Separator, idx)
  idx += 1

  val increaseFontSizeAction = new IncreaseFontSizeAction()
  val increaseFontItem = new JMenuItem(increaseFontSizeAction)
  increaseFontItem.setText("Increase Font Size")
  val controlPlus = KeyStroke.getKeyStroke(KeyEvent.VK_ADD, InputEvent.CTRL_MASK)
  inputMap.put(controlPlus, "increase-font-size")
  am.put("increase-font-size", increaseFontSizeAction)
  increaseFontSizeAction.setAccelerator(controlPlus)
  popup.add(increaseFontItem, idx)
  idx += 1

  val decreaseFontSizeAction = new DecreaseFontSizeAction()
  val decreaseFontItem = new JMenuItem(decreaseFontSizeAction)
  decreaseFontItem.setText("Decrease Font Size")
  val controlMinus = KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, InputEvent.CTRL_MASK)
  inputMap.put(controlMinus, "decrease-font-size")
  am.put("decrease-font-size", decreaseFontSizeAction)
  decreaseFontSizeAction.setAccelerator(controlMinus)
  popup.add(decreaseFontItem, idx)
  idx += 1

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

  val ipmProvider = new IpmProvider(codeSupport)
  codePane.addMouseListener(new MouseAdapter {
    override def mouseClicked(e: MouseEvent) {
      try {
        val pt = new Point(e.getX(), e.getY());
        val offset = codePane.viewToModel(pt);
        if (ipmProvider.isHyperlinkPoint(codePane, offset)) {
          // ipmProvider.getHyperlinkSpan(codePane, offset)
          ipmProvider.performClickAction(codePane, offset)
        }
        else {
          codeSupport.imanip.foreach { _ close () }
        }
      }
      catch {
        case t: Throwable => println("IPM Problem: " + t.getMessage)
      }
    }
  })
}