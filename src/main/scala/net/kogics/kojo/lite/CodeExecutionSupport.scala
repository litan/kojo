/*
 * Copyright (C) 2009 Lalit Pant <pant.lalit@gmail.com>
 *
 * The contents of this file are subject to the GNU General Public License
 * Version 3 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.gnu.org/copyleft/gpl.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 */
package net.kogics.kojo
package lite

import java.awt.CardLayout
import java.awt.Color
import java.awt.Cursor
import java.awt.Dimension
import java.awt.Event
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.io.File
import java.io.OutputStream
import java.io.PrintStream
import java.io.Writer
import java.util.concurrent.CountDownLatch
import java.util.logging.Logger
import javax.swing.JButton
import javax.swing.JEditorPane
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTextArea
import javax.swing.JToolBar
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.event.HyperlinkListener
import net.kogics.kojo.core.CodingMode
import net.kogics.kojo.core.D3Mode
import net.kogics.kojo.core.InitedSingleton
import net.kogics.kojo.core.MwMode
import net.kogics.kojo.core.RunContext
import net.kogics.kojo.core.StagingMode
import net.kogics.kojo.core.TwMode
import net.kogics.kojo.history.CommandHistory
import net.kogics.kojo.lite.canvas.SpriteCanvas
import net.kogics.kojo.livecoding.InteractiveManipulator
import net.kogics.kojo.livecoding.ManipulationContext
import net.kogics.kojo.util.RichFile.enrichFile
import util.Utils
import javax.swing.event.HyperlinkEvent

object CodeExecutionSupport extends InitedSingleton[CodeExecutionSupport] {
  def initedInstance(codePane: JTextArea, ctx: KojoCtx) = synchronized {
    instanceInit()
    val ret = instance()
    ret.kojoCtx = ctx
    ret.setCodePane(codePane)
    ret
  }

  protected def newInstance = new CodeExecutionSupport()
}

class CodeExecutionSupport private extends core.CodeCompletionSupport with ManipulationContext {
  val Log = Logger.getLogger(getClass.getName);
  val (toolbar, runButton, compileButton, stopButton, hNextButton, hPrevButton,
    clearSButton, clearButton, cexButton) = makeToolbar()
  val outputWindow = new JTextArea
  val errorWindow = new JEditorPane
  errorWindow.setContentType("text/html")

  val outPanel = new JPanel(new CardLayout)
  outPanel.add(new JScrollPane(outputWindow), "Output")
  outPanel.add(new JScrollPane(errorWindow), "Error")

  outputWindow.setEditable(false)
  errorWindow.setEditable(false)
  System.setOut(new PrintStream(new WriterOutputStream(new OutputWindowWriter)))
  doWelcome()

  val commandHistory = CommandHistory.instance
  val historyManager = new HistoryManager
  hPrevButton.setEnabled(commandHistory.hasPrevious)

  val tCanvas = SpriteCanvas.instance
  tCanvas.outputFn = showOutput _
  val storyTeller = story.StoryTeller.instance
  storyTeller.outputFn = showOutput _

  val fuguePlayer = music.FuguePlayer.instance
  val mp3player = music.KMp3.instance

  @volatile var pendingCommands = false
  @volatile var runMonitor: RunMonitor = new NoOpRunMonitor()
  @volatile var codePane: JTextArea = _
  @volatile var kojoCtx: KojoCtx = _
  @volatile var startingUp = true

  val codeRunner = makeCodeRunner()

  val statusStrip = new StatusStrip()

  val promptMarkColor = new Color(0x2fa600)
  val promptColor = new Color(0x883300)
  val codeColor = new Color(0x009b00)
  val outputColor = new Color(32, 32, 32)

  @volatile var showCode = false
  @volatile var verboseOutput = false
  @volatile var retainCode = false
  val OutputDelimiter = "---\n"
  @volatile var lastOutput = ""

  setSpriteListener()

  class OutputWindowWriter extends Writer {
    override def write(s: String) {
      showOutput(s)
    }

    def write(cbuf: Array[Char], off: Int, len: Int) {
      write(new String(cbuf, off, len))
    }

    def close() {}
    def flush() {}
  }

  class WriterOutputStream(writer: Writer) extends OutputStream {

    private val buf = new Array[Byte](1)

    override def close() {
      writer.close()
    }

    override def flush() {
      writer.flush()
    }

    override def write(b: Array[Byte]) {
      writer.write(new String(b))
    }

    override def write(b: Array[Byte], off: Int, len: Int) {
      writer.write(new String(b, off, len))
    }

    def write(b: Int) {
      synchronized {
        buf(0) = b.toByte
        write(buf)
      }
    }
  }

  def setCodePane(cp: JTextArea) {
    codePane = cp;
    addCodePaneHandlers()
    statusStrip.linkToPane()
    codePane.getDocument.addDocumentListener(new DocumentListener {
      def insertUpdate(e: DocumentEvent) {
        // runButton.setEnabled(true)
        clearSButton.setEnabled(true)
        // cexButton.setEnabled(true)

      }
      def removeUpdate(e: DocumentEvent) {
        if (codePane.getDocument.getLength == 0) {
          // runButton.setEnabled(false) // interferes with enabling/disabling of run button with interpreter start/stop
          clearSButton.setEnabled(false)
          // cexButton.setEnabled(false) // makes the icon look horrible
        }
      }
      def changedUpdate(e: DocumentEvent) {}
    })
  }

  def enableRunButton(enable: Boolean) {
    runButton.setEnabled(enable)
    compileButton.setEnabled(enable)
  }

  def doWelcome() = {
    val msg = """Welcome to Kojo\u2248Ray, the online version of Kojo!
    |* To use code completion and see online help ->  Press Ctrl+Space within the Script Editor
    |* To interactively manipulate program output ->  Click on numbers and colors within the Script Editor
    |* To Pan or Zoom the Drawing Canvas          ->  Drag the left mouse button or Roll the mouse wheel
    |  * To reset Pan and Zoom levels             ->  Use the Drawing Canvas context menu
    |""".stripMargin

    showOutput(msg)
  }

  def makeToolbar() = {
    val RunScript = "RunScript"
    val CompileScript = "CompileScript"
    val StopScript = "StopScript"
    val HistoryNext = "HistoryNext"
    val HistoryPrev = "HistoryPrev"
    val ClearEditor = "ClearEditor"
    val ClearOutput = "ClearOutput"
    val UploadCommand = "UploadCommand"

    val actionListener = new ActionListener {
      def actionPerformed(e: ActionEvent) = e.getActionCommand match {
        case RunScript =>
          if ((e.getModifiers & Event.CTRL_MASK) == Event.CTRL_MASK) {
            compileRunCode()
          }
          else {
            runCode()
          }
        case CompileScript =>
          if ((e.getModifiers & Event.CTRL_MASK) == Event.CTRL_MASK) {
            parseCode(false)
          }
          else if ((e.getModifiers & Event.SHIFT_MASK) == Event.SHIFT_MASK) {
            parseCode(true)
          }
          else {
            compileCode()
          }
        case StopScript =>
          stopScript()
        case HistoryNext =>
          loadCodeFromHistoryNext()
        case HistoryPrev =>
          loadCodeFromHistoryPrev()
        case ClearEditor =>
          closeFileAndClrEditorIgnoringCancel()
        case ClearOutput =>
          clrOutput()
        case UploadCommand =>
          upload()
      }
    }

    def makeNavigationButton(imageFile: String, actionCommand: String,
                             toolTipText: String, altText: String): JButton = {
      val button = new JButton()
      button.setActionCommand(actionCommand)
      button.setToolTipText(toolTipText)
      button.addActionListener(actionListener)
      button.setIcon(Utils.loadIcon(imageFile, altText))
      // button.setMnemonic(KeyEvent.VK_ENTER)
      button;
    }

    val toolbar = new JToolBar
    toolbar.setPreferredSize(new Dimension(100, 24))

    val runButton = makeNavigationButton("/images/run24.png", RunScript, Utils.loadString("S_RunScript"), "Run the Code")
    val compileButton = makeNavigationButton("/images/check.png", CompileScript, Utils.loadString("S_CheckScript"), "Check the Code")
    val stopButton = makeNavigationButton("/images/stop24.png", StopScript, Utils.loadString("S_StopScript"), "Stop the Code")
    val hNextButton = makeNavigationButton("/images/history-next.png", HistoryNext, Utils.loadString("S_HistNext"), "Next in History")
    val hPrevButton = makeNavigationButton("/images/history-prev.png", HistoryPrev, Utils.loadString("S_HistPrev"), "Prev in History")
    val clearSButton = makeNavigationButton("/images/clears.png", ClearEditor, Utils.loadString("S_ClearEditorT"), "Clear the Editor and Close Open File")
    val clearButton = makeNavigationButton("/images/clear24.png", ClearOutput, Utils.loadString("S_ClearOutput"), "Clear the Output")
    val cexButton = makeNavigationButton("/images/upload.png", UploadCommand, Utils.loadString("S_Upload"), "Upload")

    toolbar.add(runButton)

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

    (toolbar, runButton, compileButton, stopButton, hNextButton, hPrevButton, clearSButton, clearButton, cexButton)
  }

  def makeCodeRunner(): core.CodeRunner = {
    new core.ProxyCodeRunner(makeRealCodeRunner _)
  }

  def isSingleLine(code: String): Boolean = {
    //    val n = code.count {c => c == '\n'}
    //    if (n > 1) false else true

    val len = code.length
    var idx = 0
    var count = 0
    while (idx < len) {
      if (code.charAt(idx) == '\n') {
        count += 1
        if (count > 1) {
          return false
        }
      }
      idx += 1
    }
    if (count == 0) {
      return true
    }
    else {
      if (code.charAt(len - 1) == '\n') {
        return true
      }
      else {
        return false
      }
    }
  }

  def makeRealCodeRunner: core.CodeRunner = {
    val codeRunner = new xscala.ScalaCodeRunner(new RunContext {

      @volatile var suppressInterpOutput = false

      def onInterpreterInit() = {
        showOutput(" " * 38 + "_____\n\n")
        lastOutput = ""
        startingUp = false
      }

      def onInterpreterStart(code: String) {
        resetErrInfo()
        if (verboseOutput || isSingleLine(code)) {
          suppressInterpOutput = false
        }
        else {
          suppressInterpOutput = true
        }

        showNormalCursor()
        enableRunButton(false)
        stopButton.setEnabled(true)
        runMonitor.onRunStart()
      }

      def onCompileStart() {
        resetErrInfo()
        showNormalCursor()
        enableRunButton(false)
      }

      def onRunError() {
        interpreterDone()
        onError()
      }

      def onCompileError() {
        compileDone()
        onError()
      }

      private def onError() {
        Utils.runInSwingThread {
          statusStrip.onError()
        }
        // just in case this was a story
        // bad coupling here!
        storyTeller.storyAborted()
      }

      def onCompileSuccess() {
        compileDone()
        Utils.runInSwingThread {
          statusStrip.onSuccess()
        }
      }

      private def compileDone() {
        codePane.requestFocusInWindow
        enableRunButton(true)
        Utils.schedule(0.2) {
          kojoCtx.scrollOutputToEnd()
        }
      }

      def onRunSuccess() = {
        interpreterDone()
        Utils.runInSwingThread {
          statusStrip.onSuccess()
        }
      }

      def onRunInterpError() = {
        showErrorMsg("Kojo is unable to process your script. Please modify your code and try again.\n")
        showOutput("More information about the problem - can be viewed - by clicking the red icon at the bottom right of the Kojo screen.\n")
        onRunError()
      }

      def onInternalCompilerError() = {
        showErrorMsg("Kojo is unable to process your script. Please modify your code and try again.\n")
        showOutput("More information about the problem - can be viewed - by clicking the red icon at the bottom right of the Kojo screen.\n")
        onCompileError()
      }

      def kprintln(outText: String) {
        showOutput(outText)
        runMonitor.reportOutput(outText)
      }

      def reportOutput(outText: String) {
        if (suppressInterpOutput) {
          return
        }

        kprintln(outText)
      }

      def reportErrorMsg(errMsg: String) {
        showErrorMsg(errMsg)
        runMonitor.reportOutput(errMsg)
      }

      def reportErrorText(errText: String) {
        showErrorText(errText)
        runMonitor.reportOutput(errText)
      }

      def reportSmartErrorText(errText: String, line: Int, column: Int, offset: Int) {
        showSmartErrorText(errText, line, column, offset)
        runMonitor.reportOutput(errText)
      }

      private def interpreterDone() = {
        Utils.runInSwingThread {
          enableRunButton(true)
          if (!pendingCommands) {
            stopButton.setEnabled(false)
          }

          Utils.schedule(0.2) {
            kojoCtx.scrollOutputToEnd()
          }
        }
        runMonitor.onRunEnd()
      }

      def showScriptInOutput() { showCode = true }
      def hideScriptInOutput() { showCode = false }
      def showVerboseOutput() { verboseOutput = true }
      def hideVerboseOutput() { verboseOutput = false }
      def retainSingleLineCode() { retainCode = true }
      def clearSingleLineCode() { retainCode = false }
      def readInput(prompt: String): String = CodeExecutionSupport.this.readInput(prompt)

      def clearOutput() = clrOutput()

      def setScript(code: String) {
        Utils.runInSwingThreadAndWait {
          closeFileAndClrEditor()
          codePane.setText(code)
          codePane.setCaretPosition(0)
        }
      }

      def stopAnimation() {
        CodeExecutionSupport.this.stopAnimation()
      }
    }, tCanvas)
    codeRunner
  }

  def isRunningEnabled = runButton.isEnabled

  def addCodePaneHandlers() {
    codePane.addKeyListener(new KeyAdapter {
      override def keyPressed(evt: KeyEvent) {
        imanip.foreach { _ close () }
        evt.getKeyCode match {
          case KeyEvent.VK_ENTER =>
            if (evt.isControlDown && (isRunningEnabled || evt.isShiftDown)) {
              runCode()
              evt.consume
            }
          case KeyEvent.VK_UP =>
            if (evt.isControlDown && hPrevButton.isEnabled) {
              loadCodeFromHistoryPrev()
              evt.consume
            }
          case KeyEvent.VK_DOWN =>
            if (evt.isControlDown && hNextButton.isEnabled) {
              loadCodeFromHistoryNext()
              evt.consume
            }
          case _ => // do nothing special
        }
      }

    })
  }

  def setSpriteListener() {
    tCanvas.setTurtleListener(new core.AbstractSpriteListener {
      def interpreterDone = runButton.isEnabled
      override def hasPendingCommands {
        pendingCommands = true
        stopButton.setEnabled(true)
      }
      override def pendingCommandsDone {
        pendingCommands = false
        if (interpreterDone) stopButton.setEnabled(false)
      }
    })
  }

  def upload() {
    val dlg = new codex.CodeExchangeForm(kojoCtx, true)
    dlg.setCanvas(tCanvas)
    dlg.setCode(Utils.stripCR(codePane.getText()))
    dlg.centerScreen()
  }

  def locateError(errorText0: String) {
    // TODO Kojo Lite
  }

  def clrOutput() {
    Utils.runInSwingThread {
      outputWindow.setText("")
      clearButton.setEnabled(false)
      codePane.requestFocusInWindow
    }
    lastOutput = ""
  }

  def enableClearButton() = if (!clearButton.isEnabled) clearButton.setEnabled(true)

  def readInput(prompt: String): String = {
    throw new UnsupportedOperationException("Input reading is not available in KojoLite.")
  }

  def appendOutput(s: String) {
    outputWindow.append(s)
    outputWindow.setCaretPosition(outputWindow.getDocument.getLength)
    val cl = outPanel.getLayout().asInstanceOf[CardLayout]
    cl.show(outPanel, "Output")
  }

  @volatile var errText = ""
  @volatile var errOffset = 0
  @volatile var errCount = 0

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
  }

  def appendError(s: String, offset: Option[Int] = None) {
    errText += xml.Unparsed(s)
    if (offset.isDefined) {
      errCount += 1
      if (errCount == 1) {
        errOffset = offset.get
      }
    }

    def errorLink = "http://error/" + errOffset

    val errMsg =
      <body style="">
        <h2>There's a problem in your script!</h2>
        <div style="color:red;margin:5px;font-size:large;">
          <pre>{ errText }</pre>
        </div>
        { if (errCount > 1) { <div style="margin:5px;font-size:large;">
        	<a href={ errorLink }>Locate first error in script</a>
          </div> } else if (errCount == 1) { <div style="margin:5px;font-size:large;">
        	<a href={ errorLink }>Locate error in script</a>
          </div> } else { <div style="margin:5px;font-size:large;">
        	  Use the 'Check Script' button for better error recovery.
            </div> } }
      </body>

    errorWindow.setText(errMsg.toString)
    errorWindow.setCaretPosition(errorWindow.getDocument.getLength)
    val cl = outPanel.getLayout().asInstanceOf[CardLayout]
    cl.show(outPanel, "Error")
  }

  def showOutput(outText: String): Unit = showOutput(outText, outputColor)

  def showOutput(outText: String, color: Color): Unit = {
    Utils.runInSwingThread {
      appendOutput(outText)
      enableClearButton()
    }
    lastOutput = outText
  }

  def showErrorMsg(errMsg: String) {
    Utils.runInSwingThread {
      appendError(errMsg)
      enableClearButton()
    }
    lastOutput = errMsg
  }

  def showErrorText(errText: String) {
    Utils.runInSwingThread {
      appendError(errText)
      enableClearButton()
    }
    lastOutput = errText
  }

  def showSmartErrorText(errText: String, line: Int, column: Int, offset: Int) {
    Utils.runInSwingThread {
      appendError(errText, Some(offset))
      enableClearButton()
    }
    lastOutput = errText
  }

  def showWaitCursor() {
    val wc = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
    codePane.setCursor(wc)
    tCanvas.setCursor(wc)
  }

  def showNormalCursor() {
    val nc = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
    codePane.setCursor(nc);
    tCanvas.setCursor(nc)
  }

  def stopScript() {
    stopInterpreter()
    stopAnimation()
  }

  def stopInterpreter() {
    codeRunner.interruptInterpreter()
  }

  def stopAnimation() {
    Utils.stopMonitoredThreads()
    tCanvas.stop()
    fuguePlayer.stopMusic()
    fuguePlayer.stopBgMusic()
    mp3player.stopMp3()
    mp3player.stopMp3Loop()
  }

  def invalidCode(code: String): Boolean = {
    if (code == null || code.trim.length == 0) true
    else false
  }

  def parseCode(browseAst: Boolean) {
    val code = codePane.getText()

    if (invalidCode(code)) {
      return
    }

    statusStrip.onDocChange()
    enableRunButton(false)
    showWaitCursor()

    codeRunner.parseCode(code, browseAst)
  }

  def compileCode() {
    val code = codePane.getText()

    if (invalidCode(code)) {
      return
    }

    statusStrip.onDocChange()
    enableRunButton(false)
    showWaitCursor()

    codeRunner.compileCode(code)
  }

  def isStory(code: String) = {
    code.indexOf("stPlayStory") != -1
  }

  def compileRunCode() {
    preProcessCode() map { codeToRun => codeRunner.compileRunCode(codeToRun) }
  }

  // For IPM
  def runCode(code: String) = codeRunner.runCode(code)

  def runCode() {
    // Runs on swing thread
    preProcessCode() map { codeToRun =>
      historyManager.codeRun(codeToRun)
      if (isStory(codeToRun)) {
        codeRunner.compileRunCode(codeToRun)
      }
      else {
        codeRunner.runCode(codeToRun)
      }
    }
  }

  def preProcessCode(): Option[String] = {
    val code = codePane.getText()

    if (invalidCode(code)) {
      return None
    }

    // now that we use the proxy code runner, disable the run button right away and change
    // the cursor so that the user gets some feedback the first time he runs something
    // - relevant if the proxy is still loading the real runner
    enableRunButton(false)
    showWaitCursor()

    val selStart = codePane.getSelectionStart
    val selEnd = codePane.getSelectionEnd
    val caretPos = codePane.getCaretPosition

    val selectedCode = codePane.getSelectedText
    val codeToRun = if (selectedCode == null) code else selectedCode

    if (isStory(codeToRun)) {
      // a story
      activateTw()
      storyTeller.storyComing()
    }

    if (showCode) {
      showOutput("\n>>>\n", promptColor)
      showOutput(codeToRun, codeColor)
      showOutput("\n<<<\n", promptColor)
    }
    else {
      maybeOutputDelimiter()
    }
    Some(codeToRun)
  }

  def maybeOutputDelimiter() {
    if (lastOutput.length > 0 && !lastOutput.endsWith(OutputDelimiter))
      showOutput(OutputDelimiter, promptColor)
  }

  def codeFragment(caretOffset: Int) = {
    val cpt = codePane.getText
    if (caretOffset > cpt.length) ""
    else Utils.stripCR(cpt).substring(0, caretOffset)
  }
  def varCompletions(prefix: Option[String]) = codeRunner.varCompletions(prefix)
  def keywordCompletions(prefix: Option[String]) = codeRunner.keywordCompletions(prefix)
  def memberCompletions(caretOffset: Int, objid: String, prefix: Option[String]) = codeRunner.memberCompletions(Utils.stripCR(codePane.getText), caretOffset, objid, prefix)
  def objidAndPrefix(caretOffset: Int): (Option[String], Option[String]) = xscala.CodeCompletionUtils.findIdentifier(codeFragment(caretOffset))

  var openedFile: Option[File] = None
  var fileData: String = _
  def saveFileData(d: String) {
    fileData = Utils.stripCR(d)
  }
  def fileChanged = fileData != Utils.stripCR(codePane.getText)

  def hasOpenFile = openedFile.isDefined

  def openFileWithoutClose(file: java.io.File) {
    openedFile = Some(file)
    import util.RichFile._
    val script = file.readAsString
    codePane.setText(script)
    codePane.setCaretPosition(0)
    kojoCtx.fileOpened(file)
    saveFileData(script)
  }

  def openFile(file: java.io.File) {
    try {
      closeFileIfOpen()
      openFileWithoutClose(file)
    }
    catch {
      case e: RuntimeException =>
    }
  }

  def closeFileIfOpen() {
    if (openedFile.isDefined) {
      if (fileChanged) {
        val doSave = JOptionPane.showConfirmDialog(
          null,
          Utils.loadString("S_FileChanged") format (openedFile.get.getName, openedFile.get.getName))
        if (doSave == JOptionPane.CANCEL_OPTION || doSave == JOptionPane.CLOSED_OPTION) {
          throw new RuntimeException("Cancel File Close")
        }
        if (doSave == JOptionPane.YES_OPTION) {
          saveFile()
        }
      }
      openedFile = None
      kojoCtx.fileClosed()
    }
  }

  def closeFileAndClrEditorIgnoringCancel() {
    try {
      closeFileAndClrEditor()
    }
    catch {
      case e: RuntimeException => // ignore user cancel
    }
  }

  def closeFileAndClrEditor() {
    closeFileIfOpen() // can throw runtime exception if user cancels
    this.codePane.setText(null)
    clearSButton.setEnabled(false)
    codePane.requestFocusInWindow
  }

  def saveFile() {
    saveTo(openedFile.get)
  }

  import java.io.File
  private def saveTo(file: File) {
    import util.RichFile._
    val script = codePane.getText()
    file.write(script)
    saveFileData(script)
  }

  def saveAs(file: java.io.File) {
    if (file.exists) {
      val doSave = JOptionPane.showConfirmDialog(
        null,
        Utils.loadString("S_FileExists") format (file.getName))
      if (doSave == JOptionPane.CANCEL_OPTION || doSave == JOptionPane.CLOSED_OPTION) {
        throw new RuntimeException("Cancel File SaveAs")
      }
      else if (doSave == JOptionPane.NO_OPTION) {
        throw new IllegalArgumentException("Redo 'Save As' to select new file")
      }
      else if (doSave == JOptionPane.YES_OPTION) {
        saveTo(file)
      }
    }
    else {
      saveTo(file)
    }
  }

  def closing() {
    if (openedFile.isDefined) {
      closeFileIfOpen()
    }
    //    else {
    //      if (codePane.getText.size > 0) {
    //        val doSave = JOptionPane.showConfirmDialog(
    //          kojoCtx.frame,
    //          "You have unsaved work. Do you want to save your script to a file?")
    //        if (doSave == JOptionPane.CANCEL_OPTION || doSave == JOptionPane.CLOSED_OPTION) {
    //          throw new RuntimeException("Veto Shutdown")
    //        }
    //        else if (doSave == JOptionPane.YES_OPTION) {
    //          kojoCtx.saveAsFile()
    //        }
    //      }
    //    }
  }

  var imanip: Option[InteractiveManipulator] = None
  def addManipulator(im: InteractiveManipulator) {
    imanip = Some(im)
  }
  def removeManipulator(im: InteractiveManipulator) {
    imanip = None
  }

  def loadCodeFromHistoryPrev() = historyManager.historyMoveBack
  def loadCodeFromHistoryNext() = historyManager.historyMoveForward
  def loadCodeFromHistory(historyIdx: Int) = historyManager.setCode(historyIdx)

  class HistoryManager {

    def historyMoveBack {
      // depend on history listener mechanism to move back
      val prevCode = commandHistory.previous
      hPrevButton.setEnabled(commandHistory.hasPrevious)
      hNextButton.setEnabled(true)
      commandHistory.ensureLastEntryVisible()
    }

    def historyMoveForward {
      // depend on history listener mechanism to move forward
      val nextCode = commandHistory.next
      if (!nextCode.isDefined) {
        hNextButton.setEnabled(false)
      }
      hPrevButton.setEnabled(true)
      commandHistory.ensureLastEntryVisible()
    }

    def updateButtons(historyIdx: Int) {
      if (commandHistory.size > 0 && historyIdx != 0)
        hPrevButton.setEnabled(true)
      else
        hPrevButton.setEnabled(false)

      if (historyIdx < commandHistory.size)
        hNextButton.setEnabled(true)
      else
        hNextButton.setEnabled(false)
    }

    def setCode(historyIdx: Int) {
      updateButtons(historyIdx)
      val codeAtIdx = commandHistory.toPosition(historyIdx)

      if (codeAtIdx.isDefined) {
        codePane.setText(codeAtIdx.get)
        codePane.setCaretPosition(0)
      }

      //        codePane.requestFocusInWindow
    }

    def codeRunError() = {
    }

    def codeRun(code: String) {
      val tcode = code.trim()
      commandHistory.add(code, openedFile.map(f => "%s (%s)" format (f.getName, f.getParent)))
      //      updateButtons(commandHistory.hIndex)

      //      if (commandHistory.hIndex == prevIndex + 1) {
      //        // the last entry within history was selected
      //        commandHistory.ensureLastEntryVisible()
      //      } else {
      //        commandHistory.ensureVisible(prevIndex)
      //      }
    }
  }

  def runCodeWithOutputCapture(): String = {
    runMonitor = new OutputCapturingRunner()
    val ret = runMonitor.asInstanceOf[OutputCapturingRunner].go()
    runMonitor = new NoOpRunMonitor()
    ret
  }

  def activateEditor() {
    Utils.schedule(0.3) {
      codePane.requestFocusInWindow();
    }
  }

  @volatile var codingMode: CodingMode = TwMode // default mode for interp

  def activateTw() {
    if (codingMode != TwMode) {
      codingMode = TwMode
      codeRunner.activateTw()
    }
  }

  def activateStaging() {
    if (codingMode != StagingMode) {
      codingMode = StagingMode
      codeRunner.activateStaging()
    }
  }

  def activateMw() {
    if (codingMode != MwMode) {
      codingMode = MwMode
      codeRunner.activateMw()
    }
  }

  def activateD3() {
    if (codingMode != D3Mode) {
      codingMode = D3Mode
      codeRunner.activateD3()
    }
  }

  def isTwActive = codingMode == TwMode
  def isStagingActive = codingMode == StagingMode
  def isMwActive = codingMode == MwMode
  def isD3Active = codingMode == D3Mode

  def knownColors = kojoCtx.knownColors

  class OutputCapturingRunner extends RunMonitor {
    val outputx: StringBuilder = new StringBuilder()
    val latch = new CountDownLatch(1)

    def reportOutput(outText: String) = captureOutput(outText)
    def onRunStart() {}
    def onRunEnd() = latch.countDown()

    def go(): String = {
      runCode()
      latch.await()
      outputx.toString
    }

    def captureOutput(output: String) {
      outputx.append(output)
    }
  }

  class StatusStrip extends JPanel {
    val ErrorColor = new Color(0xff1a1a) // reddish
    val SuccessColor = new Color(0x33ff33) // greenish
    val NeutralColor = new Color(0xf0f0f0) // very light gray
    val StripWidth = 6

    setBackground(NeutralColor)
    setPreferredSize(new Dimension(StripWidth, 10))

    def linkToPane() {
      codePane.getDocument.addDocumentListener(new DocumentListener {
        def insertUpdate(e: DocumentEvent) = onDocChange()
        def removeUpdate(e: DocumentEvent) = onDocChange()
        def changedUpdate(e: DocumentEvent) {}
      })
    }

    def onSuccess() {
      setBackground(SuccessColor)
    }

    def onError() {
      setBackground(ErrorColor)
    }

    def onDocChange() {
      if (imanip.isEmpty) {
        if (getBackground != NeutralColor) setBackground(NeutralColor)
      }
      else {
        if (!imanip.get.inSliderChange) {
          imanip.get.close()
        }
      }
    }
  }
}

trait RunMonitor {
  def reportOutput(outText: String)
  def onRunStart()
  def onRunEnd()
}

class NoOpRunMonitor extends RunMonitor {
  def reportOutput(outText: String) {}
  def onRunStart() {}
  def onRunEnd() {}
}
