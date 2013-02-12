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

import javax.swing.AbstractAction
import javax.swing.Action
import javax.swing.JCheckBoxMenuItem
import javax.swing.JFrame
import javax.swing.JMenuItem
import javax.swing.JPanel
import javax.swing.JPopupMenu
import javax.swing.KeyStroke
import javax.swing.event.PopupMenuEvent
import javax.swing.event.PopupMenuListener

import org.fife.rsta.ui.search.AbstractFindReplaceDialog
import org.fife.rsta.ui.search.ReplaceDialog
import org.fife.ui.autocomplete.AutoCompletion
import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.RSyntaxTextAreaEditorKit
import org.fife.ui.rsyntaxtextarea.RSyntaxTextAreaEditorKit.DecreaseFontSizeAction
import org.fife.ui.rsyntaxtextarea.RSyntaxTextAreaEditorKit.IncreaseFontSizeAction
import org.fife.ui.rsyntaxtextarea.Style
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory
import org.fife.ui.rsyntaxtextarea.TokenTypes
import org.fife.ui.rsyntaxtextarea.folding.CurlyFoldParser
import org.fife.ui.rsyntaxtextarea.folding.FoldParserManager
import org.fife.ui.rsyntaxtextarea.templates.StaticCodeTemplate
import org.fife.ui.rtextarea.IconGroup
import org.fife.ui.rtextarea.RTextArea
import org.fife.ui.rtextarea.RTextScrollPane
import org.fife.ui.rtextarea.SearchEngine

import net.kogics.kojo.action.ChooseColor
import net.kogics.kojo.codingmode.SwitchMode
import net.kogics.kojo.lite.CodeExecutionSupport
import net.kogics.kojo.lite.KojoCompletionProvider
import net.kogics.kojo.livecoding.IpmProvider
import net.kogics.kojo.util.Utils
import net.kogics.kojo.xscala.CodeTemplates

import scalariform.formatter.ScalaFormatter
import scalariform.formatter.preferences.AlignParameters
import scalariform.formatter.preferences.AlignSingleLineCaseStatements
import scalariform.formatter.preferences.CompactControlReadability
import scalariform.formatter.preferences.FormatXml
import scalariform.formatter.preferences.FormattingPreferences
import scalariform.formatter.preferences.IndentSpaces
import scalariform.formatter.preferences.PreserveDanglingCloseParenthesis

class ScriptEditorHolder(val se: JPanel, codePane: RSyntaxTextArea, codeSupport: CodeExecutionSupport, frame: JFrame)
  extends BaseHolder("SE", Utils.loadString("CTL_CodeEditorTopComponent"), se) {

  codeSupport.toolbar.setOpaque(true)
  codeSupport.toolbar.setBackground(new Color(230, 230, 230))

  val SYNTAX_STYLE_SCALA2 = "text/scala2"
  val tFactory = TokenMakerFactory.getDefaultInstance.asInstanceOf[AbstractTokenMakerFactory]
  tFactory.putMapping(SYNTAX_STYLE_SCALA2, "net.kogics.kojo.lexer.ScalariformTokenMaker")
  FoldParserManager.get.addFoldParserMapping(SYNTAX_STYLE_SCALA2, new CurlyFoldParser)
  TokenMakerFactory.setDefaultInstance(tFactory)
  codePane.setSyntaxEditingStyle(SYNTAX_STYLE_SCALA2)
  //  codePane.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SCALA)
  codePane.setAntiAliasingEnabled(true)
  codePane.setBracketMatchingEnabled(true)
  codePane.setMatchedBracketBGColor(new Color(247, 247, 247))
  codePane.setMatchedBracketBorderColor(new Color(192, 192, 192))
  codePane.setAnimateBracketMatching(false)
  codePane.setCloseCurlyBraces(false)
  codePane.setTabsEmulated(true)
  codePane.setTabSize(4)
  codePane.setCodeFoldingEnabled(true)
  codePane.getSyntaxScheme.setStyle(TokenTypes.SEPARATOR, new Style(Color.blue))
  codePane.getSyntaxScheme.setStyle(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, new Style(new Color(200, 50, 0)))
  codePane.getSyntaxScheme.setStyle(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE, new Style(new Color(200, 50, 0)))
//  val commentFont = codePane.getSyntaxScheme.getStyle(TokenTypes.COMMENT_MULTILINE).font
//  codePane.getSyntaxScheme.setStyle(TokenTypes.COMMENT_MULTILINE, new Style(new Color(10, 110, 10), null, commentFont))
  codePane.setSelectionColor(new Color(125, 150, 255))
  codePane.setMarkOccurrencesColor(new Color(150, 175, 200))

  val inputMap = codePane.getInputMap()
  inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_CLOSE_BRACKET, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), RSyntaxTextAreaEditorKit.rstaGoToMatchingBracketAction);

  val increaseFontSizeAction = new IncreaseFontSizeAction()
  for (i <- 1 to 2) { increaseFontSizeAction.actionPerformedImpl(null, codePane) }

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

  RTextArea.setIconGroup(new IconGroup("KojoIcons", "images/extra/"))

  var idx = 0
  val popup = codePane.getPopupMenu

  val switcher = new SwitchMode()
  val twCb = new JCheckBoxMenuItem(switcher)
  twCb.setText(Utils.loadString("S_TurtleMode"))
  twCb.setToolTipText(Utils.loadString("S_TurtleModeTT"))
  twCb.setActionCommand("Tw")
  popup.add(twCb, idx)
  idx += 1

  val stagingCb = new JCheckBoxMenuItem(switcher)
  stagingCb.setText(Utils.loadString("S_StagingMode"))
  stagingCb.setToolTipText(Utils.loadString("S_StagingModeTT"))
  stagingCb.setActionCommand("Staging")
  popup.add(stagingCb, idx)
  idx += 1

  val mwCb = new JCheckBoxMenuItem(switcher)
  mwCb.setText(Utils.loadString("S_MwMode"))
  mwCb.setToolTipText(Utils.loadString("S_MwModeTT"))
  mwCb.setActionCommand("Mw")
  popup.add(mwCb, idx)
  idx += 1

  val d3Cb = new JCheckBoxMenuItem(switcher)
  d3Cb.setText(Utils.loadString("S_D3Mode"))
  d3Cb.setToolTipText(Utils.loadString("S_D3ModeTT"))
  d3Cb.setActionCommand("D3")
  popup.add(d3Cb, idx)
  idx += 1

  popup.add(new JPopupMenu.Separator, idx)
  idx += 1

  val formatAction = new AbstractAction(Utils.loadString("S_FormatSource")) {
    import scalariform.formatter.preferences._

    def actionPerformed(ev: ActionEvent) {
      val pos = codePane.getCaretPosition()
      try {
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
        try {
          codePane.setCaretPosition(pos)
        }
        catch {
          case badPos: IllegalArgumentException =>
            codePane.setCaretPosition(codePane.getText().length())
        }
      }
      catch {
        case t: Throwable =>
          println("Unable to format: " + t.getMessage())
      }
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

  val findReplaceAction = new AbstractAction(Utils.loadString("S_FindReplace"), Utils.loadIcon("/images/extra/find.gif")) {
    lazy val dialog: ReplaceDialog = new ReplaceDialog(frame, listener) {
      override def setVisible(visible: Boolean) {
        if (!visible) {
          codePane.clearMarkAllHighlights()
        }
        super.setVisible(visible)
      }
    }
    lazy val listener = new ActionListener {
      def actionPerformed(ev: ActionEvent) {
        val searchContext = dialog.getSearchContext
        def markAllIf() {
          if (searchContext.getMarkAll) {
            codePane.markAll(searchContext.getSearchFor, searchContext.getMatchCase(),
              searchContext.getWholeWord, searchContext.isRegularExpression())
          }
        }
        def find() {
          var found = SearchEngine.find(codePane, searchContext)
          if (!found) {
            val oldDot = codePane.getCaret().getDot
            codePane.getCaret().setDot(0)
            found = SearchEngine.find(codePane, searchContext)
            if (!found) {
              codePane.getCaret().setDot(oldDot)
            }
          }
        }

        ev.getActionCommand match {
          case AbstractFindReplaceDialog.ACTION_FIND =>
            markAllIf()
            find()

          case AbstractFindReplaceDialog.ACTION_REPLACE =>
            markAllIf()
            val replaced = SearchEngine.replace(codePane, searchContext);
            if (replaced) {
              find()
            }
          case AbstractFindReplaceDialog.ACTION_REPLACE_ALL =>
            SearchEngine.replaceAll(codePane, searchContext);
          case _ =>
        }
      }
    }
    def actionPerformed(ev: ActionEvent) {
      Option(codePane.getSelectedText) foreach { st =>
        dialog.setSearchString(st)
        codePane.setSelectionEnd(codePane.getSelectionStart)
      }
      dialog.setTitle(Utils.loadString("S_FindReplace"))
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

  val typeAtAction = new AbstractAction(Utils.loadString("S_ShowType")) {
    def actionPerformed(ev: ActionEvent) {
      println(codeSupport.typeAt(codePane.getCaretPosition()))
    }
  }
  val typeAtItem = new JMenuItem(typeAtAction)
  popup.add(typeAtItem, idx)
  idx += 1

  val markOccurancesAction = new AbstractAction(Utils.loadString("S_MarkOccurances")) {
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

  popup.add(new JPopupMenu.Separator, idx)
  idx += 1

  val increaseFontItem = new JMenuItem(increaseFontSizeAction)
  increaseFontItem.setText(Utils.loadString("S_IncreaseFontSize"))
  val controlPlus = KeyStroke.getKeyStroke(KeyEvent.VK_ADD, InputEvent.CTRL_MASK)
  inputMap.put(controlPlus, "increase-font-size")
  am.put("increase-font-size", increaseFontSizeAction)
  increaseFontSizeAction.setAccelerator(controlPlus)
  popup.add(increaseFontItem, idx)
  idx += 1

  val decreaseFontSizeAction = new DecreaseFontSizeAction()
  val decreaseFontItem = new JMenuItem(decreaseFontSizeAction)
  decreaseFontItem.setText(Utils.loadString("S_DecreaseFontSize"))
  val controlMinus = KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, InputEvent.CTRL_MASK)
  inputMap.put(controlMinus, "decrease-font-size")
  am.put("decrease-font-size", decreaseFontSizeAction)
  decreaseFontSizeAction.setAccelerator(controlMinus)
  popup.add(decreaseFontItem, idx)
  idx += 1

  popup.add(new JPopupMenu.Separator, idx)
  idx += 1

  val ctrlL = KeyStroke.getKeyStroke("control L")
  val clearAction = new AbstractAction(Utils.loadString("S_ClearEditor"), Utils.loadIcon("/images/clears.png")) {
    putValue(Action.ACCELERATOR_KEY, ctrlL)
    def actionPerformed(ev: ActionEvent) {
      codeSupport.closeFileAndClrEditorIgnoringCancel()
    }
  }
  val clearItem = new JMenuItem(clearAction)
  inputMap.put(ctrlL, "clear-editor")
  am.put("clear-editor", clearAction)
  popup.add(clearItem, idx)
  idx += 1

  popup.add(new JPopupMenu.Separator, idx)
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
    Utils.schedule(0.3) { codePane.requestFocusInWindow() }
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