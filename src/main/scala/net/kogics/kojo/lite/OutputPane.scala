package net.kogics.kojo.lite

import javax.swing.JPanel
import java.awt.CardLayout
import javax.swing.JScrollPane
import java.awt.BorderLayout
import javax.swing.BorderFactory
import javax.swing.JTextPane
import javax.swing.JEditorPane
import java.awt.Color
import javax.swing.JTextField
import javax.swing.BoxLayout
import javax.swing.JLabel
import java.awt.Component
import net.kogics.kojo.util.TerminalAnsiCodes
import javax.swing.text.StyleConstants
import javax.swing.text.StyleContext
import net.kogics.kojo.util.Utils
import java.awt.Font
import javax.swing.event.HyperlinkListener
import javax.swing.event.HyperlinkEvent
import javax.swing.JTextArea
import javax.swing.JCheckBoxMenuItem
import javax.swing.JPopupMenu
import javax.swing.AbstractAction
import net.kogics.kojo.action.FullScreenOutputAction
import javax.swing.KeyStroke
import javax.swing.UIDefaults
import javax.swing.JMenuItem
import java.awt.event.ActionEvent
import net.kogics.kojo.util.NoOpPainter
import javax.swing.event.PopupMenuListener
import java.awt.event.KeyEvent
import java.awt.event.ActionListener
import javax.swing.event.PopupMenuEvent
import java.awt.event.InputEvent
import java.awt.event.FocusAdapter
import net.kogics.kojo.action.FullScreenSupport
import java.awt.event.FocusEvent

class OutputPane(codeSupport: CodeExecutionSupport) extends JPanel {
  lazy val codePane = codeSupport.codePane
  val kojoCtx = codeSupport.kojoCtx
  
  val DefaultOutputColor = new Color(32, 32, 32)
  val DefaultOutputFontSize = 13
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
  outputWindow.setForeground(new Color(32, 32, 32))
  outputWindow.setBackground(Color.white)
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
    def hyperlinkUpdate(e: HyperlinkEvent) {
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

  def resetErrInfo() {
    errText = ""
    errOffset = 0
    errCount = 0
    Utils.runInSwingThread {
      errorWindow.setText("")
      outLayout.show(this, "Output")
    }
  }

  def appendOutput(s: String, color: Color) {
    if (TerminalAnsiCodes.isColoredString(s)) {
      TerminalAnsiCodes.parse(s) foreach { cstr =>
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

  def appendError(s: String, offset: Option[Int] = None) {
    errText += xml.Unparsed(s)
    if (offset.isDefined) {
      // errCount is used only for 'Check Script' case
      errCount += 1
      if (errCount == 1) {
        errOffset = offset.get
      }
    }

    def errorLink = "http://error/" + errOffset

    def errorLocation =
      <div style="margin:5px;font-size:large;">
      { if (errCount > 1) { <a href={ errorLink }>Locate first error in script</a> } else if (errCount == 1) { <a href={ errorLink }>Locate error in script</a> } else { <span style="color:blue;">Use the 'Check Script' button for better error recovery.</span> } }
      </div>

    val errMsg =
      <body style="">
        <h2>There's a problem in your script!</h2> 
        { errorLocation }
        <div style="color:red;margin:5px;font-size:large;">
          <pre>{ errText }</pre>
        </div>
        { if (errCount > 2) errorLocation }
      </body>

    errorWindow.setText(errMsg.toString)
    errorWindow.setCaretPosition(0)
    outLayout.show(this, "Error")
    // For the case where a warning is sent to the regular Output window
    Utils.schedule(0.9) { outLayout.show(this, "Error") }
  }
  
  def showOutput(outText: String) {
    Utils.runInSwingThread {
      showOutputHelper(outText, outputColor)
    }
    lastOutput = outText
  }

  def showOutput(outText: String, color: Color) {
    Utils.runInSwingThread {
      showOutputHelper(outText, color)
    }
    lastOutput = outText
  }

  def showOutputHelper(outText: String, color: Color): Unit = {
    appendOutput(outText, color)
    codeSupport.enableClearButton()
  }

  def showErrorMsg(errMsg: String) {
    Utils.runInSwingThread {
      appendError(errMsg)
      codeSupport.enableClearButton()
    }
    //    lastOutput = errMsg
  }

  def showErrorText(errText: String) {
    Utils.runInSwingThread {
      appendError(errText)
      codeSupport.enableClearButton()
    }
    //    lastOutput = errText
  }

  def showSmartErrorText(errText: String, line: Int, column: Int, offset: Int) {
    Utils.runInSwingThread {
      appendError(errText, Some(offset))
      codeSupport.enableClearButton()
    }
    //    lastOutput = errText
  }
  
  def clear() {
    outputColor = DefaultOutputColor
    setOutputFontSize(DefaultOutputFontSize)
    outputWindow.setBackground(Color.white)
    outputWindow.setText("")
    errorWindow.setText("")
    outoutPanel.remove(readInputPanel)
    outoutPanel.revalidate()
  }
  
  def clearLastOutput() {
    lastOutput = ""
  }

  def createReadInputPanel(prompt: String) = {
    readInputPanel = new JPanel()
    readInputPanel.setLayout(new BoxLayout(readInputPanel, BoxLayout.Y_AXIS))
    val label = new JLabel(" %s" format (prompt))
    label.setAlignmentX(Component.LEFT_ALIGNMENT)
    val inputField = new JTextField
    inputField.setAlignmentX(Component.LEFT_ALIGNMENT)
    readInputPanel.add(label)
    readInputPanel.add(inputField)
    outoutPanel.add(readInputPanel, BorderLayout.SOUTH)
    outoutPanel.revalidate()
    inputField
  }

  def removeInputPanel() {
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

  def increaseOutputFontSize() {
    setOutputFontSize(fontSize + 1)
  }

  def decreaseOutputFontSize() {
    setOutputFontSize(fontSize - 1)
  }

  def maybeOutputDelimiter() {
    if (lastOutput.length > 0 && !lastOutput.endsWith(OutputDelimiter))
      showOutput(OutputDelimiter, codeSupport.promptColor)
  }
  
//  val ow = codeSupport.outputWindow
//  val ew = codeSupport.errorWindow
//  val oPanel = codeSupport.outPanel

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
      override def actionPerformed(e: ActionEvent) {
        if (verboseOutput.isSelected) {
          kojoCtx.showVerboseOutput()
        }
        else {
          kojoCtx.hideVerboseOutput()
        }
      }
    })
    add(verboseOutput)

    val showCode = new JCheckBoxMenuItem(Utils.loadString("S_ShowScriptInOutput"))
    showCode.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent) {
        if (showCode.isSelected) {
          kojoCtx.showScriptInOutput()
        }
        else {
          kojoCtx.hideScriptInOutput()
        }
      }
    })
    add(showCode)

    addSeparator()

    val increaseFontSizeAction = new AbstractAction(Utils.loadString("S_IncreaseFontSize")) {
      override def actionPerformed(e: ActionEvent) {
        codeSupport.increaseOutputFontSize()
      }
    }
    val incrFontSizeItem = new JMenuItem(increaseFontSizeAction)
    val inputMap = outputWindow.getInputMap
    val am = outputWindow.getActionMap
    val controlPlus = KeyStroke.getKeyStroke(KeyEvent.VK_ADD, InputEvent.CTRL_MASK)
    inputMap.put(controlPlus, "increase-font-size")
    am.put("increase-font-size", increaseFontSizeAction)
    incrFontSizeItem.setAccelerator(controlPlus)
    add(incrFontSizeItem)

    val decreaseFontSizeAction = new AbstractAction(Utils.loadString("S_DecreaseFontSize")) {
      override def actionPerformed(e: ActionEvent) {
        codeSupport.decreaseOutputFontSize()
      }
    }
    val decrFontSizeItem = new JMenuItem(decreaseFontSizeAction)
    val controlMinus = KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, InputEvent.CTRL_MASK)
    inputMap.put(controlMinus, "decrease-font-size")
    am.put("decrease-font-size", decreaseFontSizeAction)
    decrFontSizeItem.setAccelerator(controlMinus)
    add(decrFontSizeItem)

    errorWindow.addFocusListener(new FocusAdapter {
      override def focusGained(e: FocusEvent) {
        incrFontSizeItem.setEnabled(false)
        decrFontSizeItem.setEnabled(false)

      }
    })

    outputWindow.addFocusListener(new FocusAdapter {
      override def focusGained(e: FocusEvent) {
        incrFontSizeItem.setEnabled(true)
        decrFontSizeItem.setEnabled(true)

      }
    })

    addSeparator()

    val clearItem = new JMenuItem(Utils.loadString("S_Clear"))
    clearItem.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent) {
        kojoCtx.clearOutput()
      }
    })
    add(clearItem)

    addSeparator()

    val fsOutputAction = FullScreenOutputAction(kojoCtx)
    val fullScreenItem: JCheckBoxMenuItem = new JCheckBoxMenuItem(fsOutputAction)
    add(fullScreenItem)

    addPopupMenuListener(new PopupMenuListener {
      def popupMenuWillBecomeVisible(e: PopupMenuEvent) {
        verboseOutput.setState(kojoCtx.isVerboseOutput)
        showCode.setState(kojoCtx.isSriptShownInOutput)
        FullScreenSupport.updateMenuItem(fullScreenItem, fsOutputAction)
      }
      def popupMenuWillBecomeInvisible(e: PopupMenuEvent) {}
      def popupMenuCanceled(e: PopupMenuEvent) {}
    })
  }

  outputWindow.setComponentPopupMenu(popup)
  errorWindow.setComponentPopupMenu(popup)
  // should be called in swing thread
  def scrollToEnd() {
    outputWindow.setCaretPosition(outputWindow.getDocument.getLength)
  }
  
}