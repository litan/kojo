package net.kogics.kojo.lite

import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Event
import java.awt.Font
import java.awt.Point
import java.awt.Toolkit
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent
import java.awt.event.InputEvent
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseWheelEvent

import javax.swing.AbstractAction
import javax.swing.Action
import javax.swing.JButton
import javax.swing.JCheckBoxMenuItem
import javax.swing.JFrame
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem
import javax.swing.JPanel
import javax.swing.JPopupMenu
import javax.swing.JToolBar
import javax.swing.KeyStroke
import javax.swing.event.CaretEvent
import javax.swing.event.CaretListener
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.event.PopupMenuEvent
import javax.swing.event.PopupMenuListener
import javax.swing.text.Utilities

import org.fife.rsta.ui.CollapsibleSectionPanel
import org.fife.rsta.ui.search.ReplaceToolBar
import org.fife.rsta.ui.search.SearchEvent
import org.fife.rsta.ui.search.SearchListener
import org.fife.ui.autocomplete.AutoCompletion
import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.RSyntaxTextAreaEditorKit
import org.fife.ui.rsyntaxtextarea.RSyntaxTextAreaEditorKit.DecreaseFontSizeAction
import org.fife.ui.rsyntaxtextarea.RSyntaxTextAreaEditorKit.IncreaseFontSizeAction
import org.fife.ui.rsyntaxtextarea.SyntaxConstants
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory
import org.fife.ui.rsyntaxtextarea.folding.CurlyFoldParser
import org.fife.ui.rsyntaxtextarea.folding.FoldParserManager
import org.fife.ui.rsyntaxtextarea.templates.StaticCodeTemplate
import org.fife.ui.rtextarea.IconGroup
import org.fife.ui.rtextarea.RTextArea
import org.fife.ui.rtextarea.RTextScrollPane
import org.fife.ui.rtextarea.SearchEngine

import net.kogics.kojo.action.ChooseColor
import net.kogics.kojo.codingmode.SwitchMode
import net.kogics.kojo.core.TwMode
import net.kogics.kojo.core.VanillaMode
import net.kogics.kojo.livecoding.IpmProvider
import net.kogics.kojo.util.Utils
import net.kogics.kojo.xscala.CodeTemplates

import scalariform.formatter.ScalaFormatter
import scalariform.formatter.preferences._

class ScriptEditor(val execSupport: CodeExecutionSupport, frame: JFrame) extends JPanel with EditorFileSupport {

  var tabSize = 4
  val codePane = new RSyntaxTextArea(5, 80)
  val codePane2 = new RSyntaxTextArea(5, 80)
  val codePanes = List(codePane, codePane2)
  val statusStrip = new StatusStrip()
  val (toolbar, runButton, runWorksheetButton, traceButton, compileButton, stopButton, hNextButton, hPrevButton,
    clearSButton, clearButton, cexButton) = makeToolbar()

  val SYNTAX_STYLE_SCALA2 = "text/scala2"
  val tFactory = TokenMakerFactory.getDefaultInstance.asInstanceOf[AbstractTokenMakerFactory]
  tFactory.putMapping(SYNTAX_STYLE_SCALA2, "net.kogics.kojo.lexer.ScalariformTokenMaker")
  FoldParserManager.get.addFoldParserMapping(SYNTAX_STYLE_SCALA2, new CurlyFoldParser)
  val defaultSyntaxRich = AppMode.currentMode.richSyntaxHighlighting
  if (defaultSyntaxRich) {
    codePanes.foreach(_.setSyntaxEditingStyle(SYNTAX_STYLE_SCALA2))
  }
  else {
    codePanes.foreach(_.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SCALA))
  }
  codePanes.foreach(_.setAntiAliasingEnabled(true))
  codePanes.foreach(_.setTabsEmulated(true))
  codePanes.foreach(_.setTabSize(tabSize))
  codePanes.foreach(_.setCodeFoldingEnabled(true))

  codePanes.foreach(Theme.currentTheme.loadEditorTheme.apply(_))

  val inputMap = codePane.getInputMap
  val inputMap2 = codePane2.getInputMap
  val inputMaps = List(inputMap, inputMap2)
  inputMaps.foreach {
    _.put(
      KeyStroke.getKeyStroke(KeyEvent.VK_CLOSE_BRACKET, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()),
      RSyntaxTextAreaEditorKit.rstaGoToMatchingBracketAction
    )
  }

  val realIncreaseFontSizeAction = new IncreaseFontSizeAction()
  val increaseFontSizeAction = new AbstractAction() {
    override def actionPerformed(e: ActionEvent) = {
      codePanes.foreach(realIncreaseFontSizeAction.actionPerformedImpl(null, _))
    }
  }
  Utils.safeProcessSilent {
    for (i <- 1 to 3 + kojoCtx.screenDpiFontDelta) { increaseFontSizeAction.actionPerformed(null) }
  }

  RSyntaxTextArea.setTemplatesEnabled(true)
  val ctm = RSyntaxTextArea.getCodeTemplateManager()
  //  ctm.setInsertTrigger(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0))

  CodeTemplates.templates.foreach { kv =>
    val name = kv._1; val value = kv._2
    val ct = new StaticCodeTemplate(name, CodeTemplates.beforeCursor(name), CodeTemplates.afterCursor(name))
    ctm.addTemplate(ct)
  }

  val provider = new KojoCompletionProvider(execSupport)
  codePanes.foreach { pane =>
    val ac = new AutoCompletion(provider)
    ac.setParameterAssistanceEnabled(true)
    ac.setAutoActivationEnabled(true)
    ac.setShowDescWindow(true)
    ac.install(pane)
  }

  val sp = new RTextScrollPane(codePane)
  //  sp.setFoldIndicatorEnabled(true)
  setLayout(new BorderLayout)
  add(toolbar, BorderLayout.NORTH)
  val csp = new CollapsibleSectionPanel
  csp.add(sp)
  add(csp, BorderLayout.CENTER)
  add(statusStrip, BorderLayout.EAST)
  //  val interpComponent = codePane2
  val interpComponent = new RTextScrollPane(codePane2)
  add(interpComponent, BorderLayout.SOUTH)
  hideInterpreterPane()

  RTextArea.setIconGroup(new IconGroup("KojoIcons", "images/extra/"))

  var idx = 0
  val popup = codePane.getPopupMenu

  val modeMenu = new JMenu(Utils.loadString("S_Mode"))
  kojoCtx.menuReady(modeMenu)

  val switcher = new SwitchMode(execSupport)
  val twCb = new JCheckBoxMenuItem(switcher)
  twCb.setText(Utils.loadString("S_TurtleMode"))
  twCb.setToolTipText(Utils.loadString("S_TurtleModeTT"))
  twCb.setActionCommand(TwMode.code)
  modeMenu.add(twCb)

  val vnCb = new JCheckBoxMenuItem(switcher)
  vnCb.setText(Utils.loadString("S_VanillaMode"))
  vnCb.setToolTipText(Utils.loadString("S_VanillaModeTT"))
  vnCb.setActionCommand(VanillaMode.code)
  modeMenu.add(vnCb)

  val syntaxColoringAction = new AbstractAction {
    def actionPerformed(e: ActionEvent): Unit = {
      e.getActionCommand match {
        case "Fast" =>
          codePane.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SCALA)
          fastColoringCb.setSelected(true)
          richColoringCb.setSelected(false)
        case "Rich" =>
          codePane.setSyntaxEditingStyle(SYNTAX_STYLE_SCALA2)
          val pos = codePane.getCaretPosition
          val doc = codePane.getDocument
          doc.insertString(doc.getLength, " ", null)
          doc.remove(doc.getLength - 1, 1)
          codePane.setCaretPosition(pos)
          fastColoringCb.setSelected(false)
          richColoringCb.setSelected(true)
      }
    }
  }

  val syntaxColoringMenu = new JMenu(Utils.loadString("S_SyntaxColoring"))
  kojoCtx.menuReady(syntaxColoringMenu)

  val richColoringCb: JCheckBoxMenuItem = new JCheckBoxMenuItem(syntaxColoringAction)
  richColoringCb.setText(Utils.loadString("S_ColoringRich"))
  richColoringCb.setActionCommand("Rich")
  richColoringCb.setSelected(defaultSyntaxRich)
  syntaxColoringMenu.add(richColoringCb)

  val fastColoringCb: JCheckBoxMenuItem = new JCheckBoxMenuItem(syntaxColoringAction)
  fastColoringCb.setText(Utils.loadString("S_ColoringFast"))
  fastColoringCb.setActionCommand("Fast")
  fastColoringCb.setSelected(!defaultSyntaxRich)
  syntaxColoringMenu.add(fastColoringCb)

  def makeFormatPrefs(ts: Int) = new FormattingPreferences(
    Map(
      IndentSpaces -> ts,
      CompactControlReadability -> true,
      AlignParameters -> true,
      AlignSingleLineCaseStatements -> true,
      DanglingCloseParenthesis -> Preserve,
      FormatXml -> false,
      SingleCasePatternOnNewline -> false
    )
  )

  var formatPrefs = makeFormatPrefs(tabSize)

  val formatAction = new AbstractAction(Utils.loadString("S_FormatSource")) {
    def actionPerformed(ev: ActionEvent): Unit = {
      val caretLine = codePane.getCaretLineNumber
      val posInLine = codePane.getCaretOffsetFromLineStart
      try {
        codePane.setText(ScalaFormatter.format(
          codePane.getText,
          formatPrefs
        ))
        try {
          val lineStart = codePane.getLineStartOffset(caretLine)
          val lineEnd = codePane.getLineEndOffset(caretLine)
          val lineLen = math.max(lineEnd - lineStart - 1, 0)
          val pos = lineStart + math.min(posInLine, lineLen)
          codePane.setCaretPosition(pos)
        }
        catch {
          case _: Throwable =>
            codePane.setCaretPosition(codePane.getText.length)
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
  val am = codePane.getActionMap
  val am2 = codePane2.getActionMap
  val actionMaps = List(am, am2)
  am.put("format-src", formatAction)
  formatItem.setAccelerator(csf)
  popup.add(formatItem, idx)
  idx += 1

  val findReplaceAction = new AbstractAction(Utils.loadString("S_FindReplace"), Utils.loadIcon("/images/extra/find.gif")) {

    //    lazy val dialog: ReplaceDialog = new ReplaceDialog(frame, listener) {
    //      setTitle(Utils.loadString("S_FindReplace"))
    //      override def setVisible(visible: Boolean) {
    //        val searchContext = getSearchContext
    //        if (!visible) {
    //          // dialog closing; get rid of marks, if any
    //          val oldMarkAll = searchContext.getMarkAll
    //          searchContext.setMarkAll(false)
    //          val oldDot = codePane.getCaret.getDot
    //          SearchEngine.find(codePane, searchContext)
    //          codePane.getCaret.setDot(oldDot)
    //          searchContext.setMarkAll(oldMarkAll)
    //        }
    //        super.setVisible(visible)
    //      }
    //    }

    lazy val listener = new SearchListener {
      def getSelectedText = codePane.getSelectedText

      def searchEvent(ev: SearchEvent): Unit = {
        val searchContext = toolbar.getSearchContext
        def find(): Unit = {
          var found = SearchEngine.find(codePane, searchContext)
          if (found.getCount == 0) {
            val oldDot = codePane.getCaret.getDot
            codePane.getCaret.setDot(0)
            found = SearchEngine.find(codePane, searchContext)
            if (found.getCount == 0) {
              codePane.getCaret.setDot(oldDot)
            }
          }
        }

        ev.getType match {
          case SearchEvent.Type.MARK_ALL =>
            SearchEngine.markAll(codePane, searchContext)
          case SearchEvent.Type.FIND =>
            find()
          case SearchEvent.Type.REPLACE =>
            val result = SearchEngine.replace(codePane, searchContext)
            val nextFindRange = result.getMatchRange
            if (nextFindRange.getEndOffset == nextFindRange.getStartOffset) {
              // nothing found; try from beginning of document
              find()
            }
          case SearchEvent.Type.REPLACE_ALL =>
            SearchEngine.replaceAll(codePane, searchContext);
          case _ =>
        }
      }
    }

    lazy val toolbar: ReplaceToolBar = new ReplaceToolBar(listener) {
      override def addNotify() = {
        val searchContext = getSearchContext
        searchContext.setMarkAll(false)
        val sel = codePane.getSelectedText
        if (sel != null) {
          searchContext.setSearchFor(sel)
          searchContext.setReplaceWith("")
        }
        super.addNotify()
      }

      override def removeNotify(): Unit = {
        val searchContext = getSearchContext
        // toolbar closing; get rid of marks, if any
        searchContext.setMarkAll(false)
        SearchEngine.markAll(codePane, searchContext)
        super.removeNotify()
      }
    }

    var toolbarAdded = false
    def actionPerformed(ev: ActionEvent): Unit = {
      //      dialog.setVisible(true)
      if (!toolbarAdded) {
        csp.addBottomComponent(toolbar)
        toolbarAdded = true
      }
      csp.showBottomComponent(toolbar)
    }
  }
  val findReplaceItem = new JMenuItem(findReplaceAction)
  val cf = KeyStroke.getKeyStroke("control F")
  inputMap.put(cf, "find-replace")
  am.put("find-replace", findReplaceAction)
  findReplaceItem.setAccelerator(cf)
  popup.add(findReplaceItem, idx)
  idx += 1

  val chooseColorItem = new JMenuItem(new ChooseColor(execSupport))
  popup.add(chooseColorItem, idx)
  idx += 1

  popup.add(new JPopupMenu.Separator, idx)
  idx += 1

  val typeAtAction = new AbstractAction(Utils.loadString("S_ShowType")) {
    def actionPerformed(ev: ActionEvent): Unit = {
      val codePane = execSupport.codePane
      val offset = codePane.getCaretPosition
      val typeAt = execSupport.typeAt(offset)
      val wordStart = Utilities.getWordStart(codePane, offset)
      val wordEnd = Utilities.getWordEnd(codePane, offset)
      val word0 = codePane.getDocument.getText(wordStart, wordEnd - wordStart)
      val delta = offset - wordStart
      val words = word0.split(raw"\.")
      val wordsWithCumIdx = words.foldLeft(Vector(("", 0))) { (l: Vector[(String, Int)], w: String) =>
        l :+ (w, w.size + l.last._2 + 1)
      }
      wordsWithCumIdx.find { case (w, idx) => idx > delta } match {
        case Some(wordIdx) =>
          val word = wordIdx._1
          word.find(c => !Character.isJavaIdentifierStart(c) && !Character.isJavaIdentifierPart(c)) match {
            case Some(_) => println(s"[type around] $word : $typeAt")
            case None    => println(s"[type] $word : $typeAt")
          }
        case None => println(s"[type at cursor] $typeAt")
      }
    }
  }
  val typeAtItem = new JMenuItem(typeAtAction)
  val controlT = KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK)
  inputMaps.foreach(_.put(controlT, "type-at"))
  actionMaps.foreach(_.put("type-at", typeAtAction))
  typeAtItem.setAccelerator(controlT)
  popup.add(typeAtItem, idx)
  idx += 1

  val markOccurancesAction = new AbstractAction(Utils.loadString("S_MarkOccurances")) {
    def actionPerformed(ev: ActionEvent): Unit = {
      if (markOccurancesItem.isSelected()) {
        codePane.setMarkOccurrences(true)
      }
      else {
        codePane.setMarkOccurrences(false)
      }
    }
  }
  val markOccurancesItem: JCheckBoxMenuItem = new JCheckBoxMenuItem(markOccurancesAction)

  val markOccurancesKeyboardAction = new AbstractAction() {
    def actionPerformed(ev: ActionEvent): Unit = {
      if (markOccurancesItem.isSelected()) {
        markOccurancesItem.setSelected(false)
      }
      else {
        markOccurancesItem.setSelected(true)
      }
      markOccurancesAction.actionPerformed(ev)
    }
  }
  val controlShiftM = KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK)
  inputMap.put(controlShiftM, "mark-occurances")
  am.put("mark-occurances", markOccurancesKeyboardAction)
  markOccurancesItem.setAccelerator(controlShiftM)
  popup.add(markOccurancesItem, idx)
  idx += 1

  popup.add(new JPopupMenu.Separator, idx)
  idx += 1

  val increaseFontItem = new JMenuItem(increaseFontSizeAction)
  increaseFontItem.setText(Utils.loadString("S_IncreaseFontSize"))
  val controlNumPlus = KeyStroke.getKeyStroke(KeyEvent.VK_ADD, InputEvent.CTRL_MASK)
  val controlPlus = KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, InputEvent.CTRL_MASK)
  val controlShiftPlus = KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK)
  inputMap.put(controlNumPlus, "increase-font-size")
  inputMap.put(controlPlus, "increase-font-size")
  inputMap.put(controlShiftPlus, "increase-font-size")
  am.put("increase-font-size", increaseFontSizeAction)
  increaseFontItem.setAccelerator(controlPlus)
  popup.add(increaseFontItem, idx)
  idx += 1

  val realDecreaseFontSizeAction = new DecreaseFontSizeAction()
  val decreaseFontSizeAction = new AbstractAction() {
    override def actionPerformed(e: ActionEvent) = {
      codePanes.foreach(realDecreaseFontSizeAction.actionPerformedImpl(null, _))
    }
  }

  val decreaseFontItem = new JMenuItem(decreaseFontSizeAction)
  decreaseFontItem.setText(Utils.loadString("S_DecreaseFontSize"))
  val controlNumMinus = KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, InputEvent.CTRL_MASK)
  val controlMinus = KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, InputEvent.CTRL_MASK)
  val controlShiftMinus = KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK)
  inputMap.put(controlNumMinus, "decrease-font-size")
  inputMap.put(controlMinus, "decrease-font-size")
  inputMap.put(controlShiftMinus, "decrease-font-size")
  am.put("decrease-font-size", decreaseFontSizeAction)
  decreaseFontItem.setAccelerator(controlMinus)
  popup.add(decreaseFontItem, idx)
  idx += 1

  popup.add(new JPopupMenu.Separator, idx)
  idx += 1

  val ctrlL = KeyStroke.getKeyStroke("control L")
  val clearAction = new AbstractAction(Utils.loadString("S_ClearEditor"), Utils.loadIcon("/images/24/clears.png")) {
    putValue(Action.ACCELERATOR_KEY, ctrlL)
    def actionPerformed(ev: ActionEvent): Unit = {
      closeFileAndClrEditorIgnoringCancel()
    }
  }
  val clearItem = new JMenuItem(clearAction)
  inputMap.put(ctrlL, "clear-editor")
  am.put("clear-editor", clearAction)
  popup.add(clearItem, idx)
  idx += 1

  var savedMenu: Option[JMenuBar] = None
  val toggleMenuAction = new AbstractAction(Utils.loadString("S_ToggleMenubar")) {
    def actionPerformed(ev: ActionEvent): Unit = {
      if (savedMenu.isEmpty) {
        savedMenu = Some(frame.getJMenuBar)
        frame.setJMenuBar(null)
        frame.getRootPane.revalidate()
        toggleMenuItem.setSelected(false)
      }
      else {
        frame.setJMenuBar(savedMenu.get)
        frame.getRootPane.revalidate()
        savedMenu = None
        toggleMenuItem.setSelected(true)
      }
    }
  }
  val toggleMenuItem: JCheckBoxMenuItem = new JCheckBoxMenuItem(toggleMenuAction)
  toggleMenuItem.setSelected(true)
  popup.add(toggleMenuItem, idx)
  idx += 1

  var interpPaneVisible = false
  val toggleInterpPaneAction = new AbstractAction(Utils.loadString("S_InterpPaneVisible")) {
    def actionPerformed(ev: ActionEvent): Unit = {
      if (interpPaneVisible) {
        interpPaneVisible = false
        hideInterpreterPane()
        toggleInterpPaneItem.setSelected(false)
      }
      else {
        interpPaneVisible = true
        showInterpreterPane()
        toggleInterpPaneItem.setSelected(true)
      }
    }
  }
  val toggleInterpPaneItem: JCheckBoxMenuItem = new JCheckBoxMenuItem(toggleInterpPaneAction)
  toggleInterpPaneItem.setSelected(false)

  val controlI = KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_DOWN_MASK)
  inputMaps.foreach(_.put(controlI, "toggle-interpreter-pane"))
  actionMaps.foreach(_.put("toggle-interpreter-pane", toggleInterpPaneAction))
  toggleInterpPaneItem.setAccelerator(controlI)
  popup.add(toggleInterpPaneItem, idx)
  idx += 1

  val toggleInterpPaneFocusAction = new AbstractAction() {
    def actionPerformed(ev: ActionEvent): Unit = {
      if (codePane.hasFocus) {
        codePane2.requestFocusInWindow()
      }
      else {
        codePane.requestFocusInWindow()
      }
    }
  }
  val controlShiftI = KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK)
  inputMaps.foreach(_.put(controlShiftI, "toggle-interpreter-pane-focus"))
  actionMaps.foreach(_.put("toggle-interpreter-pane-focus", toggleInterpPaneFocusAction))

  val resetInterpAction = new AbstractAction(Utils.loadString("S_ResetInterpreter")) {
    def actionPerformed(ev: ActionEvent): Unit = {
      execSupport.codeRunner.resetInterpUI()
    }
  }
  val resetInterpItem = new JMenuItem(resetInterpAction)
  popup.add(resetInterpItem, idx)
  idx += 1

  popup.add(new JPopupMenu.Separator, idx)
  idx += 1

  popup.add(syntaxColoringMenu, idx)
  idx += 1

  popup.add(modeMenu, idx)
  idx += 1

  popup.add(new JPopupMenu.Separator, idx)
  idx += 1

  popup.addPopupMenuListener(new PopupMenuListener {
    def popupMenuWillBecomeVisible(e: PopupMenuEvent): Unit = {
      switcher.updateCb(twCb)
      switcher.updateCb(vnCb)
    }
    def popupMenuWillBecomeInvisible(e: PopupMenuEvent): Unit = {}

    def popupMenuCanceled(e: PopupMenuEvent): Unit = {}
  })

  val focusRequestDelay = 0.3
  def activate(): Unit = {
    Utils.schedule(focusRequestDelay) { codePane.requestFocusInWindow() }
  }

  def showInterpreterPane(): Unit = {
    interpComponent.setVisible(true)
    revalidate()
    Utils.schedule(focusRequestDelay) { codePane2.requestFocusInWindow() }
  }

  def hideInterpreterPane(): Unit = {
    interpComponent.setVisible(false)
    revalidate()
    Utils.schedule(focusRequestDelay) { codePane.requestFocusInWindow() }
  }

  val ipmProvider = new IpmProvider(execSupport)
  addCodePaneListeners()

  private def compileRunOrInterpretCode(): Unit = {
    if (interpComponent.isVisible) {
      execSupport.runCode()
    }
    else {
      execSupport.compileRunCode()
    }
  }

  def makeToolbar() = {
    val RunScript = "RunScript"
    val RunWorksheet = "RunWorksheet"
    val TraceScript = "TraceScript"
    val CompileScript = "CompileScript"
    val StopScript = "StopScript"
    val HistoryNext = "HistoryNext"
    val HistoryPrev = "HistoryPrev"
    val ClearEditor = "ClearEditor"
    val ClearOutput = "ClearOutput"
    val UploadCommand = "UploadCommand"

    val actionListener = new ActionListener {
      def actionPerformed(e: ActionEvent): Unit = {
        activateCodePane1()
        e.getActionCommand match {
          case RunScript =>
            if ((e.getModifiers & Event.CTRL_MASK) == Event.CTRL_MASK) {
              execSupport.runCode()
            }
            else {
              compileRunOrInterpretCode()
            }
            codePane.requestFocusInWindow()
          case RunWorksheet =>
            execSupport.runWorksheet()
            codePane.requestFocusInWindow()
          case TraceScript =>
            execSupport.traceCode()
            codePane.requestFocusInWindow()
          case CompileScript =>
            if ((e.getModifiers & Event.CTRL_MASK) == Event.CTRL_MASK) {
              execSupport.parseCode(false)
            }
            else if ((e.getModifiers & Event.SHIFT_MASK) == Event.SHIFT_MASK) {
              execSupport.parseCode(true)
            }
            else {
              execSupport.compileCode()
            }
          case StopScript =>
            execSupport.stopScript()
            codePane.requestFocusInWindow()
          case HistoryNext =>
            execSupport.loadCodeFromHistoryNext()
            codePane.requestFocusInWindow()
          case HistoryPrev =>
            execSupport.loadCodeFromHistoryPrev()
            codePane.requestFocusInWindow()
          case ClearEditor =>
            closeFileAndClrEditorIgnoringCancel()
          case ClearOutput =>
            execSupport.clrOutput()
          case UploadCommand =>
            execSupport.upload()
        }
      }
    }

    def makeNavigationButton(imageFile: String, actionCommand: String,
                             toolTipText: String): JButton = {
      val button = new JButton()
      button.setActionCommand(actionCommand)
      button.setToolTipText(toolTipText)
      button.addActionListener(actionListener)
      button.setIcon(Utils.loadIcon(imageFile))
      // button.setMnemonic(KeyEvent.VK_ENTER)
      button.setBorderPainted(false)
      button
    }

    val toolbar = new JToolBar
    toolbar.setFloatable(false)
    toolbar.setOpaque(true)
    Theme.currentTheme.toolbarBg.foreach { c => toolbar.setBackground(c) }

    val imageFolder = kojoCtx.screenDpiFontDelta match {
      case n if n < 6 => 24
      case _          => 36
    }
    toolbar.setPreferredSize(new Dimension(0, imageFolder + imageFolder / 6))

    import Theme.currentTheme.checkPng
    import Theme.currentTheme.clearOwPng
    import Theme.currentTheme.clearSePng
    import Theme.currentTheme.runPng
    import Theme.currentTheme.runtPng
    import Theme.currentTheme.runwPng
    import Theme.currentTheme.stopPng

    val runButton = makeNavigationButton(s"/images/$imageFolder/$runPng", RunScript, Utils.loadString("S_RunScript"))
    val runWorksheetButton = makeNavigationButton(s"/images/$imageFolder/$runwPng", RunWorksheet, Utils.loadString("S_RunWorksheet"))
    val traceButton = makeNavigationButton(s"/images/$imageFolder/$runtPng", TraceScript, Utils.loadString("S_TraceScript"))
    val compileButton = makeNavigationButton(s"/images/$imageFolder/$checkPng", CompileScript, Utils.loadString("S_CheckScript"))
    val stopButton = makeNavigationButton(s"/images/$imageFolder/$stopPng", StopScript, Utils.loadString("S_StopScript"))
    val hNextButton = makeNavigationButton(s"/images/$imageFolder/history-next.png", HistoryNext, Utils.loadString("S_HistNext"))
    val hPrevButton = makeNavigationButton(s"/images/$imageFolder/history-prev.png", HistoryPrev, Utils.loadString("S_HistPrev"))
    val clearSButton = makeNavigationButton(s"/images/$imageFolder/$clearSePng", ClearEditor, Utils.loadString("S_ClearEditorT"))
    val clearButton = makeNavigationButton(s"/images/$imageFolder/$clearOwPng", ClearOutput, Utils.loadString("S_ClearOutput"))
    val cexButton = makeNavigationButton(s"/images/$imageFolder/upload.png", UploadCommand, Utils.loadString("S_Upload"))

    toolbar.add(runButton)
    toolbar.add(runWorksheetButton)
    toolbar.add(traceButton)

    stopButton.setEnabled(false)
    toolbar.add(stopButton)

    toolbar.add(compileButton)

    toolbar.add(hPrevButton)

    hNextButton.setEnabled(false)
    toolbar.add(hNextButton)

    clearSButton.setEnabled(false)
    toolbar.add(clearSButton)

    toolbar.addSeparator()

    toolbar.add(cexButton)

    clearButton.setEnabled(false)
    toolbar.add(clearButton)

    (toolbar, runButton, runWorksheetButton, traceButton, compileButton, stopButton, hNextButton, hPrevButton, clearSButton, clearButton, cexButton)
  }

  def addCodePaneListeners(): Unit = {
    statusStrip.linkToPane()
    codePane.addKeyListener(new KeyAdapter {
      override def keyPressed(evt: KeyEvent): Unit = {
        evt.getKeyCode match {
          case KeyEvent.VK_ENTER =>
            if (evt.isControlDown && (execSupport.isRunningEnabled || evt.isShiftDown)) {
              compileRunOrInterpretCode()
              evt.consume
            }
            else if (evt.isShiftDown && execSupport.isRunningEnabled) {
              execSupport.runWorksheet()
              evt.consume
            }
            else if (evt.isAltDown && execSupport.isRunningEnabled) {
              execSupport.traceCode()
              evt.consume
            }
          case KeyEvent.VK_Q =>
            if (evt.isControlDown) {
              execSupport.stopScript()
              evt.consume
            }
          case KeyEvent.VK_UP =>
            if ((evt.isControlDown || evt.isMetaDown) && hPrevButton.isEnabled) {
              execSupport.loadCodeFromHistoryPrev()
              evt.consume
            }
          case KeyEvent.VK_DOWN =>
            if (evt.isControlDown || evt.isMetaDown) {
              if (hNextButton.isEnabled) {
                execSupport.loadCodeFromHistoryNext()
              }
              // consume event in any case, to prevent scrolling when there is no 'next' history
              evt.consume
            }
          case KeyEvent.VK_ESCAPE =>
            execSupport.imanip.foreach { _.close() }

          case _ => // do nothing special
        }
      }

    })
    codePane.addCaretListener(new CaretListener {
      def caretUpdate(e: CaretEvent): Unit = {
        Utils.safeProcessSilent {
          val dot = e.getDot()
          val line = codePane.getLineOfOffset(dot)
          val lineStart = codePane.getLineStartOffset(line)
          val col = dot - lineStart
          kojoCtx.showStatusCaretPos(line + 1, col + 1)
        }
      }
    })
    kojoCtx.showStatusCaretPos(1, 1)

    val mouseListener = new MouseAdapter {
      override def mouseClicked(e: MouseEvent): Unit = {
        execSupport.imanip.foreach { _.close() }
        if (e.isControlDown || e.isMetaDown) {
          try {
            val pt = new Point(e.getX(), e.getY());
            val offset = codePane.viewToModel(pt);
            if (ipmProvider.isHyperlinkPoint(codePane, offset)) {
              // ipmProvider.getHyperlinkSpan(codePane, offset)
              ipmProvider.performClickAction(codePane, offset)
            }
          }
          catch {
            case t: Throwable => println("IPM Problem: " + t.getMessage)
          }
        }
      }

      override def mouseWheelMoved(e: MouseWheelEvent): Unit = {
        if (e.isControlDown) {
          val delta = e.getWheelRotation
          val action = if (delta < 0) increaseFontSizeAction else decreaseFontSizeAction
          action.actionPerformed(null)
        }
        else {
          sp.getMouseWheelListeners foreach { _.mouseWheelMoved(e) }
        }
      }
    }
    codePane.addMouseListener(mouseListener)
    codePane.addMouseWheelListener(mouseListener)

    codePane2.addKeyListener(new KeyAdapter {
      override def keyPressed(evt: KeyEvent): Unit = {
        evt.getKeyCode match {
          case KeyEvent.VK_ENTER =>
            if (evt.isControlDown && (execSupport.isRunningEnabled || evt.isShiftDown)) {
              execSupport.runCode()
              evt.consume
            }
          case KeyEvent.VK_UP =>
            if ((evt.isControlDown || evt.isMetaDown) && hPrevButton.isEnabled) {
              execSupport.loadCodeFromHistoryPrev()
              evt.consume
            }
          case KeyEvent.VK_DOWN =>
            if (evt.isControlDown || evt.isMetaDown) {
              if (hNextButton.isEnabled) {
                execSupport.loadCodeFromHistoryNext()
              }
              // consume event in any case, to prevent scrolling when there is no 'next' history
              evt.consume
            }
          case _ => // do nothing special
        }
      }
    })

    codePane.addFocusListener(new FocusAdapter {
      override def focusGained(e: FocusEvent) = {
        activateCodePane1()
      }
    })
    codePane2.addFocusListener(new FocusAdapter {
      override def focusGained(e: FocusEvent) = {
        activateCodePane2()
      }
    })
  }

  private def activateCodePane1(): Unit = {
    execSupport.codePane = codePane
    execSupport.kojoCtx.hideScriptInOutput()
    execSupport.kojoCtx.hideVerboseOutput()
  }

  private def activateCodePane2(): Unit = {
    execSupport.codePane = codePane2
    execSupport.kojoCtx.showScriptInOutput()
    execSupport.kojoCtx.showVerboseOutput()
  }

  def setTabSize(ts: Int) = Utils.runInSwingThread {
    tabSize = ts
    codePanes.foreach(_.setTabSize(tabSize))
    formatPrefs = makeFormatPrefs(tabSize)
  }

  def setFont(name: String) = Utils.runInSwingThread {
    val currFont = codePane.getFont()
    codePane.setFont(new Font(name, currFont.getStyle, currFont.getSize))
  }

  def markTraceLine(line: Int): Unit = {
    try {
      val lineStartOffset = codePane.getLineStartOffset(line - 1)
      Utils.scrollToOffset(lineStartOffset, codePane)
      codePane.select(lineStartOffset, codePane.getLineEndOffset(line - 1))
    }
    catch {
      // In case the user changes the contents of the script editor so that it is out of sync with the current trace
      case t: Throwable =>
        val cp = codePane.getCaretPosition
        codePane.select(cp, cp)
    }
  }

  class StatusStrip extends JPanel {
    val ErrorColor = Theme.currentTheme.errorColor
    val SuccessColor = Theme.currentTheme.successColor
    val NeutralColor = Theme.currentTheme.neutralColor
    val StripWidth = 6

    setBackground(NeutralColor)
    setPreferredSize(new Dimension(StripWidth, 10))

    def linkToPane(): Unit = {
      codePane.getDocument.addDocumentListener(new DocumentListener {
        def insertUpdate(e: DocumentEvent) = onDocChange()
        def removeUpdate(e: DocumentEvent) = onDocChange()
        def changedUpdate(e: DocumentEvent): Unit = {}
      })
    }

    def onSuccess(): Unit = {
      setBackground(SuccessColor)
    }

    def onError(): Unit = {
      setBackground(ErrorColor)
    }

    def onDocChange(): Unit = {
      if (execSupport.imanip.isEmpty) {
        if (getBackground != NeutralColor) setBackground(NeutralColor)
      }
      else {
        if (!execSupport.imanip.get.inSliderChange) {
          execSupport.imanip.get.close()
        }
      }
    }
  }
}