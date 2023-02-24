package net.kogics.kojo.lite

import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseWheelEvent
import java.awt.BorderLayout
import java.awt.CardLayout
import java.awt.Color
import java.awt.Component
import java.awt.Font
import java.awt.Insets
import java.awt.Toolkit
import javax.swing.event.HyperlinkEvent
import javax.swing.event.HyperlinkListener
import javax.swing.event.PopupMenuEvent
import javax.swing.event.PopupMenuListener
import javax.swing.text.DefaultEditorKit
import javax.swing.text.StyleConstants
import javax.swing.text.StyleContext
import javax.swing.AbstractAction
import javax.swing.BorderFactory
import javax.swing.BoxLayout
import javax.swing.JCheckBoxMenuItem
import javax.swing.JEditorPane
import javax.swing.JLabel
import javax.swing.JMenuItem
import javax.swing.JPanel
import javax.swing.JPopupMenu
import javax.swing.JScrollPane
import javax.swing.JTextField
import javax.swing.JTextPane
import javax.swing.KeyStroke
import javax.swing.UIDefaults

import net.kogics.kojo.util.NoOpPainter
import net.kogics.kojo.util.TerminalAnsiCodes
import net.kogics.kojo.util.Utils

class OutputPane(execSupport: CodeExecutionSupport) extends JPanel {
  lazy val codePane = execSupport.codePane
  val kojoCtx = execSupport.kojoCtx

  val DefaultOutputColor = Theme.currentTheme.outputPaneFg
  val DefaultOutputBackground = Theme.currentTheme.outputPaneBg
  val DefaultOutputFontSize = kojoCtx.baseFontSize + kojoCtx.screenDpiFontDelta
  var outputColor = DefaultOutputColor
  var fontSize = DefaultOutputFontSize
  val baseStyle = StyleContext.getDefaultStyleContext.getStyle(StyleContext.DEFAULT_STYLE)

  val OutputDelimiter = "---\n"
  @volatile var lastOutput = ""

  @volatile var errText = ""
  @volatile var errOffset = 0
  @volatile var errCount = 0

  val outLayout = new CardLayout
  setLayout(outLayout)
  val outoutPanel = new JPanel(new BorderLayout)
  val outputWindow = new JTextPane
  outputWindow.setForeground(DefaultOutputColor)
  outputWindow.setBackground(DefaultOutputBackground)
  outputWindow.setEditable(false)
  val outoutSp = new JScrollPane(outputWindow)
  outoutSp.setBorder(BorderFactory.createEmptyBorder())

  outoutPanel.add(outoutSp, BorderLayout.CENTER)
  var readInputPanel: JPanel = new JPanel
  add(outoutPanel, "Output")

  val errorWindow = new JEditorPane
  errorWindow.setContentType("text/html")
  errorWindow.setEditable(false)
  add(new JScrollPane(errorWindow), "Error")
  setOutputFontSize(fontSize)

  errorWindow.addHyperlinkListener(new HyperlinkListener {
    val linkRegex = """(?i)http://error/(\d+)""".r
    def hyperlinkUpdate(e: HyperlinkEvent): Unit = {
      if (e.getEventType == HyperlinkEvent.EventType.ACTIVATED) {
        e.getURL.toString match {
          case linkRegex(offset) =>
            codePane.select(offset.toInt, offset.toInt + 1)
            kojoCtx.activateScriptEditor()
          case _ =>
        }
      }
    }
  })

  val tdef = new UIDefaults();
  try {
    tdef.put("TextPane[Enabled].backgroundPainter", new NoOpPainter);
    outputWindow.putClientProperty("Nimbus.Overrides", tdef);
  }
  catch {
    case t: Throwable =>
      println("Not running on an Oracle JVM. Output Pane background colors will not work.")
  }

  val popup = new JPopupMenu {
    val verboseOutput = new JCheckBoxMenuItem(Utils.loadString("S_ShowVerboseOutput"))
    verboseOutput.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        if (verboseOutput.isSelected) {
          kojoCtx.showVerboseOutput()
        }
        else {
          kojoCtx.hideVerboseOutput()
        }
      }
    })
    verboseOutput.setEnabled(false)
    add(verboseOutput)

    val showCode = new JCheckBoxMenuItem(Utils.loadString("S_ShowScriptInOutput"))
    showCode.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        if (showCode.isSelected) {
          kojoCtx.showScriptInOutput()
        }
        else {
          kojoCtx.hideScriptInOutput()
        }
      }
    })
    showCode.setEnabled(false)
    add(showCode)

    addSeparator()

    val increaseFontSizeAction = new AbstractAction(Utils.loadString("S_IncreaseFontSize")) {
      override def actionPerformed(e: ActionEvent): Unit = {
        increaseOutputFontSize()
      }
    }
    val incrFontSizeItem = new JMenuItem(increaseFontSizeAction)
    val inputMap = outputWindow.getInputMap
    val am = outputWindow.getActionMap
    val controlNumPlus = KeyStroke.getKeyStroke(KeyEvent.VK_ADD, InputEvent.CTRL_MASK)
    val controlPlus = KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, InputEvent.CTRL_MASK)
    val controlShiftPlus =
      KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK)
    inputMap.put(controlNumPlus, "increase-font-size")
    inputMap.put(controlPlus, "increase-font-size")
    inputMap.put(controlShiftPlus, "increase-font-size")
    am.put("increase-font-size", increaseFontSizeAction)
    incrFontSizeItem.setAccelerator(controlPlus)
    add(incrFontSizeItem)

    val decreaseFontSizeAction = new AbstractAction(Utils.loadString("S_DecreaseFontSize")) {
      override def actionPerformed(e: ActionEvent): Unit = {
        decreaseOutputFontSize()
      }
    }
    val decrFontSizeItem = new JMenuItem(decreaseFontSizeAction)
    val controlNumMinus = KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, InputEvent.CTRL_MASK)
    val controlMinus = KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, InputEvent.CTRL_MASK)
    val controlShiftMinus =
      KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK)
    inputMap.put(controlNumMinus, "decrease-font-size")
    inputMap.put(controlMinus, "decrease-font-size")
    inputMap.put(controlShiftMinus, "decrease-font-size")
    am.put("decrease-font-size", decreaseFontSizeAction)
    decrFontSizeItem.setAccelerator(controlMinus)
    add(decrFontSizeItem)

    inputMap.put(
      KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit.getMenuShortcutKeyMask),
      DefaultEditorKit.copyAction
    );

    errorWindow.addFocusListener(new FocusAdapter {
      override def focusGained(e: FocusEvent): Unit = {
        incrFontSizeItem.setEnabled(false)
        decrFontSizeItem.setEnabled(false)

      }
    })

    outputWindow.addFocusListener(new FocusAdapter {
      override def focusGained(e: FocusEvent): Unit = {
        incrFontSizeItem.setEnabled(true)
        decrFontSizeItem.setEnabled(true)

      }
    })

    val mListener = new MouseAdapter {
      override def mouseWheelMoved(e: MouseWheelEvent): Unit = {
        if (e.isControlDown) {
          val delta = e.getWheelRotation
          val action = if (delta < 0) increaseFontSizeAction else decreaseFontSizeAction
          action.actionPerformed(null)
        }
        else {
          outoutSp.getMouseWheelListeners.foreach { _.mouseWheelMoved(e) }
        }
      }
    }
    outputWindow.addMouseWheelListener(mListener)

    addSeparator()

    val clearItem = new JMenuItem(Utils.loadString("S_Clear"))
    clearItem.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent): Unit = {
        kojoCtx.clearOutput()
      }
    })
    add(clearItem)

    addSeparator()

    val fsOutputAction = kojoCtx.fullScreenOutputAction()
    val fullScreenItem: JCheckBoxMenuItem = new JCheckBoxMenuItem(fsOutputAction)
    add(fullScreenItem)

    addPopupMenuListener(new PopupMenuListener {
      def popupMenuWillBecomeVisible(e: PopupMenuEvent): Unit = {
        verboseOutput.setState(kojoCtx.isVerboseOutput)
        showCode.setState(kojoCtx.isSriptShownInOutput)
        kojoCtx.updateMenuItem(fullScreenItem, fsOutputAction)
      }
      def popupMenuWillBecomeInvisible(e: PopupMenuEvent): Unit = {}
      def popupMenuCanceled(e: PopupMenuEvent): Unit = {}
    })
  }

  outputWindow.setComponentPopupMenu(popup)
  errorWindow.setComponentPopupMenu(popup)

  def resetErrInfo(): Unit = {
    errText = ""
    errOffset = 0
    errCount = 0
    Utils.runLaterInSwingThread {
      errorWindow.setText("")
      outLayout.show(this, "Output")
    }
  }

  def appendOutput(s: String, color: Color): Unit = {
    if (TerminalAnsiCodes.isColoredString(s)) {
      TerminalAnsiCodes.parse(s).foreach { cstr =>
        appendOutput(cstr._1, cstr._2)
      }
    }
    else {
      val doc = outputWindow.getStyledDocument()
      var colorStyle = doc.getStyle(color.getRGB().toString)
      if (colorStyle == null) {
        colorStyle = doc.addStyle(color.getRGB().toString, baseStyle)
        StyleConstants.setForeground(colorStyle, color)
      }

      doc.insertString(doc.getLength, s, colorStyle)
      outputWindow.setCaretPosition(doc.getLength)
      outLayout.show(this, "Output")
    }
  }

  def appendError(s: String, offset: Option[Int] = None): Unit = {
    errText += s
    if (offset.isDefined) {
      // errCount is used only for 'Check Script' case
      errCount += 1
      if (errCount == 1) {
        errOffset = offset.get
      }
    }

    def errorLink = "http://error/" + errOffset

    def errorLocation =
      <div style="margin:5px;">
        {
        val key = if (errCount == 1) "S_OutputLocateError" else "S_OutputLocateFirstError"
        if (errCount >= 1) { <a href={errorLink}>{Utils.loadString(key)}</a> }
        else { <span style="color:blue;">{Utils.loadString("S_OutputCheckScriptButton")}</span> }
      }
      </div>

    val fontSize =
      if (kojoCtx.screenDpiFontDelta > 0)
        "large"
      else
        "large"
    val bg = Theme.currentTheme.errorPaneBg
    val errMsg =
      <body style={s"font-size:$fontSize;background-color:$bg"}>
        <div style="margin:5px;">
          <strong>{Utils.loadString("S_OutputScriptProblem")}</strong>
        </div>
        {errorLocation}
        <div style={s"color:${Theme.currentTheme.errorPaneFg};margin:5px;"}>
          <pre>{errText}</pre>
        </div>
        {if (errCount > 2) errorLocation}
      </body>

    errorWindow.setText(errMsg.toString)
    errorWindow.setCaretPosition(0)
    outLayout.show(this, "Error")
    // For the case where a warning is sent to the regular Output window
    Utils.schedule(0.9) { outLayout.show(this, "Error") }
  }

  def showOutput(outText: String): Unit = {
    Utils.runLaterInSwingThread {
      showOutputHelper(outText, outputColor)
    }
    lastOutput = outText
  }

  def showOutput(outText: String, color: Color): Unit = {
    Utils.runLaterInSwingThread {
      showOutputHelper(outText, color)
    }
    lastOutput = outText
  }

  def showOutputHelper(outText: String, color: Color): Unit = {
    appendOutput(outText, color)
    execSupport.enableClearButton()
  }

  def showError(errMsg: String): Unit = {
    Utils.runLaterInSwingThread {
      appendError(errMsg)
      execSupport.enableClearButton()
    }
    //    lastOutput = errMsg
  }

  def showException(errText: String): Unit = {
    Utils.runLaterInSwingThread {
      appendError(errText)
      execSupport.enableClearButton()
    }
    //    lastOutput = errText
  }

  def showSmartError(errText: String, line: Int, column: Int, offset: Int): Unit = {
    Utils.runLaterInSwingThread {
      appendError(errText, Some(offset))
      execSupport.enableClearButton()
    }
    //    lastOutput = errText
  }

  def clear(): Unit = {
    outputColor = DefaultOutputColor
    setOutputFontSize(DefaultOutputFontSize)
    outputWindow.setBackground(DefaultOutputBackground)
    outputWindow.setText("")
    errorWindow.setText("")
    outoutPanel.remove(readInputPanel)
    outoutPanel.revalidate()
  }

  def clearLastOutput(): Unit = {
    lastOutput = ""
  }

  def createReadInputPanel(prompt: String) = {
    //    outoutPanel.remove(readInputPanel)
    readInputPanel = new JPanel()
    readInputPanel.setLayout(new BoxLayout(readInputPanel, BoxLayout.Y_AXIS))
    val label = new JLabel(" %s".format(prompt))
    label.setAlignmentX(Component.LEFT_ALIGNMENT)
    val lfont = label.getFont()
    label.setFont(new Font(lfont.getName, Font.BOLD, lfont.getSize + 2))
    label.setBorder(BorderFactory.createEmptyBorder(6, 1, 3, 1))
    val inputField = new JTextField
    inputField.setAlignmentX(Component.LEFT_ALIGNMENT)
    val ifont = inputField.getFont()
    inputField.setFont(new Font(ifont.getName, ifont.getStyle, ifont.getSize + 2))
    inputField.setMargin(new Insets(3, 1, 3, 1))
    readInputPanel.add(label)
    readInputPanel.add(inputField)
    outoutPanel.add(readInputPanel, BorderLayout.SOUTH)
    outoutPanel.revalidate()
    inputField
  }

  def removeInputPanel(): Unit = {
    outoutPanel.remove(readInputPanel)
    outoutPanel.revalidate()
  }

  def setOutputBackground(color: Color) = Utils.runInSwingThread {
    // works after nimbus painter hack
    outputWindow.setBackground(color)

    // problem with code below:
    // works only for previous text
    // does not fill out the whole background
    //    val background = new SimpleAttributeSet()
    //    StyleConstants.setBackground(background, color)
    //    val doc = outputWindow.getStyledDocument
    //    doc.setCharacterAttributes(0, doc.getLength, background, false)
  }

  def setOutputForeground(color: Color) = Utils.runInSwingThread {
    outputColor = color
  }

  def setOutputFontSize(size: Int) = Utils.runInSwingThread {
    fontSize = size
    outputWindow.setFont(new Font(Font.MONOSPACED, Font.PLAIN, size))
  }

  def increaseOutputFontSize(): Unit = {
    setOutputFontSize(fontSize + 1)
  }

  def decreaseOutputFontSize(): Unit = {
    setOutputFontSize(fontSize - 1)
  }

  def maybeOutputDelimiter(): Unit = {
    if (lastOutput.length > 0 && !lastOutput.endsWith(OutputDelimiter))
      showOutput(OutputDelimiter, execSupport.promptColor)
  }

  // should be called in swing thread
  def scrollToEnd(): Unit = {
    //    outputWindow.setCaretPosition(outputWindow.getDocument.getLength)
    val vsb = outoutSp.getVerticalScrollBar
    vsb.setValue(vsb.getMaximum)
  }
}
