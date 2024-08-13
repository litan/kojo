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

import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.Color
import java.awt.Cursor
import java.io.OutputStream
import java.io.PrintStream
import java.io.Writer
import java.util.logging.Logger
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.text.Utilities
import javax.swing.JButton
import javax.swing.JTextArea

import net.kogics.kojo.core.CodingMode
import net.kogics.kojo.core.Interpreter
import net.kogics.kojo.core.RunContext
import net.kogics.kojo.core.TwMode
import net.kogics.kojo.core.VanillaMode
import net.kogics.kojo.history.CommandHistory
import net.kogics.kojo.lite.canvas.SpriteCanvas
import net.kogics.kojo.lite.i18n.LangInit
import net.kogics.kojo.lite.trace.MethodEvent
import net.kogics.kojo.livecoding.InteractiveManipulator
import net.kogics.kojo.livecoding.ManipulationContext
import net.kogics.kojo.turtle.TurtleWorldAPI
import net.kogics.kojo.util.FutureResult
import util.Utils

object CodeExecutionSupport {

  /** Contains the goal a user wants to reach, and the action he has to take for this. */
  private case class Line(goal: String, action: Option[String])

  /** The End-of-Line String used to separate lines of the welcome message */
  val EOL = "\n"
  val goalActionSeparator = "→" // instead of -> in order to save one position. Christoph 2017-02-24

  /** Composes a welcome message from the head and a tabulated arrangement of the instructions. Each instruction should
    * have the format "goal -> action". The instructions are splitted at the first occurrence of "->" and then
    * tabulated. The "->" are replaced by the unicode arrow \u2192 "→" in order to save one character position. In the
    * end all "→" are one below another when using a monospace font.
    */
  def makeTabulatedWelcomeMessage(head: String, instructions: List[String]): String = {
    require(head != null, "head != null")
    require(instructions != null, "instructions != null")
    val instructionsSplitted = instructions.map(_.split("->", 2))
    val instructionsTrimmed: List[Line] = for (instr <- instructionsSplitted) yield {
      val action = if (instr.length > 1) Some(instr(1).trim) else None
      Line(instr(0).trim, action)
    }
    val maxGoalLine = (Line("", None) :: instructionsTrimmed).maxBy(line => line.goal.length)
    val maxGoalLen = maxGoalLine.goal.length
    val sb = new StringBuilder(head)
    sb.append(EOL)
    for (instr <- instructionsTrimmed) {
      sb.append("* ")
      sb.append(instr.goal)
      instr.action match {
        case Some(a) =>
          sb.append(" " * (maxGoalLen - instr.goal.length))
          sb.append(" ")
          sb.append(goalActionSeparator)
          sb.append(" ")
          sb.append(a)
        case None => // Nothing to append
      }
      sb.append(EOL)
    }
    sb.toString
  }

}

class CodeExecutionSupport(
    TSCanvas: DrawingCanvasAPI,
    Tw: TurtleWorldAPI,
    Staging: staging.API,
    storyTeller: story.StoryTeller,
    mp3player: music.KMp3,
    fuguePlayer: music.FuguePlayer,
    tCanvas: SpriteCanvas,
    scriptEditor0: => ScriptEditor,
    val kojoCtx: core.KojoCtx
) extends core.CodeExecutionSupport
    with core.CodeCompletionSupport
    with ManipulationContext {
  // the script editor that gets passed in is not yet inited (the last remaining circular dependency!)
  // we access it via a lazy val
  // and then import the stuff inside it,
  // which we could not do if we used a regular var that was inited in phase 2
  lazy val scriptEditor = scriptEditor0
  import scriptEditor._
  import CodeExecutionSupport._

  var clearButton = new JButton

  val Log = Logger.getLogger("CodeExecutionSupport")
  val promptColor = new Color(178, 66, 0)
  val codeColor = new Color(0x009b00)
  val WorksheetMarker = " //> "

  System.setOut(new PrintStream(new WriterOutputStream(new OutputWindowWriter)))
  System.setErr(new PrintStream(new WriterOutputStream(new OutputWindowColoredWriter(Color.red))))
  val outputPane = new OutputPane(this)
  doWelcome()

  val commandHistory = CommandHistory(kojoCtx)
  val historyManager = new HistoryManager

  tCanvas.outputFn = showOutput _
  storyTeller.outputFn = showOutput _

  @volatile var pendingCommands = false
  @volatile var codePane: JTextArea = _
  @volatile var codePane1: JTextArea = _
  @volatile var codePane2: JTextArea = _
  @volatile var startingUp = true

  val codeRunner = makeCodeRunner
  val builtins = new Builtins(
    TSCanvas,
    Tw,
    Staging,
    storyTeller,
    mp3player,
    fuguePlayer,
    kojoCtx,
    codeRunner
  )

  @volatile var showCode = false
  @volatile var verboseOutput = false
  setActivityListener()

  def initPhase2(se: ScriptEditor): Unit = {
    codeRunner.start()
    clearButton = se.clearButton
    setCodePane(se.codePane)
    codePane1 = se.codePane
    codePane2 = se.codePane2
    hPrevButton.setEnabled(commandHistory.hasPrevious)
  }

  class OutputWindowWriter extends Writer {
    override def write(s: String): Unit = {
      showOutput(s)
    }

    def write(cbuf: Array[Char], off: Int, len: Int): Unit = {
      write(new String(cbuf, off, len))
    }

    def close(): Unit = {}
    def flush(): Unit = {}
  }

  class OutputWindowColoredWriter(c: Color) extends OutputWindowWriter {
    override def write(s: String): Unit = {
      Log.severe("stderr> " + s)
      showOutput(s, c)
    }
  }

  class WriterOutputStream(writer: Writer) extends OutputStream {

    private val buf = new Array[Byte](1)

    override def close(): Unit = {
      writer.close()
    }

    override def flush(): Unit = {
      writer.flush()
    }

    override def write(b: Array[Byte]): Unit = {
      writer.write(new String(b))
    }

    override def write(b: Array[Byte], off: Int, len: Int): Unit = {
      writer.write(new String(b, off, len))
    }

    def write(b: Int): Unit = {
      synchronized {
        buf(0) = b.toByte
        write(buf)
      }
    }
  }

  def setCodePane(cp: JTextArea): Unit = {
    codePane = cp
    codePane.getDocument.addDocumentListener(new DocumentListener {
      def insertUpdate(e: DocumentEvent): Unit = {
        clearSButton.setEnabled(true)
        if (hasOpenFile) {
          kojoCtx.fileModified()
        }
      }
      def removeUpdate(e: DocumentEvent): Unit = {
        if (codePane.getDocument.getLength == 0) {
          clearSButton.setEnabled(false)
        }
        if (hasOpenFile) {
          kojoCtx.fileModified()
        }
      }
      def changedUpdate(e: DocumentEvent): Unit = {}
    })
  }

  def enableRunButton(enable: Boolean): Unit = {
    runButton.setEnabled(enable)
    runWorksheetButton.setEnabled(enable)
    traceButton.setEnabled(enable)
    compileButton.setEnabled(enable)
  }

  def doWelcome(): Unit = {
    if (kojoCtx.subKojo) {
      showOutput(Utils.loadString("S_OutputScratchpadWelcome") + "\n")
      showOutput(Utils.loadString("S_OutputScratchpadHistoryNotSave") + "\n", Color.red)
    }
    else {
      val head = Utils.loadString("S_OutputWelcome").format(Versions.KojoVersion)
      val instructions = List(
        Utils.loadString("S_OutputVisualPalette"),
        Utils.loadString("S_OutputHelp"),
        Utils.loadString("S_OutputManipulate"),
        Utils.loadString("S_OutputContext"),
        Utils.loadString("S_OutputCanvasZoom"),
        Utils.loadString("S_ThemeChange")
      )
      showOutput(makeTabulatedWelcomeMessage(head, instructions))
    }
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

  def setScript(code: String, caretAtEnd: Boolean): Unit = {
    Utils.runInSwingThreadAndWait {
      closeFileAndClrEditor()
      codePane.setText(code)
      if (caretAtEnd) {
        codePane.setCaretPosition(code.length)
      }
      else {
        codePane.setCaretPosition(0)
      }
    }
  }

  def insertCodeInline(code: String) = smartInsertCode(code, false)
  def insertCodeBlock(code: String) = smartInsertCode(code, true)

  private def smartInsertCode(code: String, block: Boolean) = Utils.runInSwingThread {
    def lineAt(offset: Int) = {
      val lineStart = Utilities.getRowStart(codePane, offset)
      val lineEnd = Utilities.getRowEnd(codePane, offset)
      codePane.getDocument.getText(lineStart, lineEnd - lineStart)
    }

    def blankLine(offset: Int) = lineAt(offset).trim == ""

    var dot = codePane.getCaretPosition
    val cOffset = code.indexOf("${c}")
    if (cOffset == -1) {
      if (block) {
        if (blankLine(dot)) {
          codePane.insert("%s\n".format(code), dot)
        }
        else {
          codePane.insert("\n", Utilities.getRowEnd(codePane, dot))
          dot = Utilities.getRowEnd(codePane, dot) + 1
          codePane.insert("%s".format(code), dot)
          // minimize chance of dot moving to previous line because of upcoming format action
          dot = Utilities.getRowEnd(codePane, dot)
        }
        scriptEditor.formatAction.actionPerformed(null)
        val currLine = lineAt(dot)
        val leadingSpaces = currLine.segmentLength { _ == ' ' }
        codePane.setCaretPosition(Utilities.getRowEnd(codePane, dot) + 1 + leadingSpaces)
      }
      else {
        codePane.insert("%s ".format(code), dot)
      }
    }
    else {
      if (block) {
        if (blankLine(dot)) {
          codePane.insert("%s\n".format(code.replace("${c}", "")), dot)
        }
        else {
          codePane.insert("\n", Utilities.getRowEnd(codePane, dot))
          dot = Utilities.getRowEnd(codePane, dot) + 1
          codePane.insert("%s".format(code.replace("${c}", "")), dot)
          // minimize chance of dot moving to previous line because of upcoming format action
          dot = Utilities.getRowEnd(codePane, dot)
        }
        scriptEditor.formatAction.actionPerformed(null)
        val currLine = lineAt(dot)
        val leadingSpaces = currLine.segmentLength { _ == ' ' }
        codePane.setCaretPosition(Utilities.getRowStart(codePane, dot) + leadingSpaces + cOffset)
      }
      else {
        codePane.insert("%s ".format(code.replace("${c}", "")), dot)
        codePane.setCaretPosition(dot + cOffset)
      }
    }
    activateEditor()
  }

  lazy val runContext = new RunContext {

    @volatile var suppressInterpOutput = false

    def astStopPhase = kojoCtx.astStopPhase

    def initInterp(interp: Interpreter): Unit = {
      codingMode match {
        case TwMode =>
          interp.bind("predef", "net.kogics.kojo.lite.CodeExecutionSupport", CodeExecutionSupport.this)
          interp.interpret("val builtins = predef.builtins")
          interp.interpret("import builtins._")
          interp.interpret("import net.kogics.kojo.util.ScalatestHelper.{test, ignore}")
          interp.interpret("import org.scalatest.Matchers")
        case VanillaMode =>
      }
    }

    val compilerPrefix = """ {
  val builtins = net.kogics.kojo.lite.Builtins.instance
  import builtins._
  import net.kogics.kojo.util.ScalatestHelper.{test, ignore}
  import org.scalatest.Matchers
  class UserCode {
"""

    def onInterpreterInit() = {
      showOutput(" " * 38 + "_____\n\n")
      outputPane.clearLastOutput()
      startingUp = false
      LangInit.initPhase2(builtins)
    }

    def onInterpreterStart(code: String): Unit = {
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
      runStart()
    }

    def onCompileStart(): Unit = {
      resetErrInfo()
      showNormalCursor()
      enableRunButton(false)
    }

    def onRunError(): Unit = {
      runDone()
      onError()
    }

    def onCompileError(): Unit = {
      compileDone()
      onError()
    }

    private def onError(): Unit = {
      Utils.runLaterInSwingThread {
        statusStrip.onError()
      }
      // just in case this was a story
      // bad coupling here!
      storyTeller.storyAborted()
    }

    def onCompileSuccess(): Unit = {
      compileDone()
      Utils.runInSwingThread {
        statusStrip.onSuccess()
      }
    }

    private def compileDone(): Unit = {
      codePane.requestFocusInWindow
      enableRunButton(true)
      //        Utils.schedule(0.2) {
      //          kojoCtx.scrollOutputToEnd()
      //        }
    }

    def onRunSuccess() = {
      runDone()
      Utils.runLaterInSwingThread {
        statusStrip.onSuccess()
      }
      Utils.schedule(0.2) {
        kojoCtx.scrollOutputToEnd()
      }
    }

    private def showInternalErrorMsg(): Unit = {
      showError(Utils.loadString("S_ErrorProcessScript") + "\n")
      val logDir = Utils.locateLogDir().getPath
      showOutput(Utils.loadString(getClass, "S_OutputLogFileHint", logDir) + "\n")
    }

    def onRunInterpError() = {
      showInternalErrorMsg()
      onRunError()
    }

    def onInternalCompilerError() = {
      showInternalErrorMsg()
      onCompileError()
    }

    def reportOutput(outText: String): Unit = {
      if (suppressInterpOutput) {
        return
      }

      showOutput(outText)
    }

    def reportError(errMsg: String): Unit = {
      showError(errMsg)
    }

    def reportException(errText: String): Unit = {
      showException(errText)
    }

    def reportSmartError(errText: String, line: Int, column: Int, offset: Int): Unit = {
      showSmartError(errText, line, column, offset)
    }

    private def runStart(): Unit = {
      tCanvas.onRunStart()
      builtins.onRunStart()
    }

    private def runDone() = {
      tCanvas.onRunDone()
      builtins.onRunDone()
      Utils.runLaterInSwingThread {
        enableRunButton(true)
        if (!pendingCommands) {
          stopButton.setEnabled(false)
        }
      }
    }

    def reportWorksheetOutput(result: String, lineNum: Int): Unit = {
      appendToCodePaneLine(lineNum, result.trim.replaceAll("\r?\n", " | "))
    }

    private def appendToCodePaneLine(lineNum: Int, result: String) = Utils.runInSwingThread {
      val insertPos = getVisibleLineEndOffset(lineNum + selectionOffset)
      val dot = codePane.getCaretPosition
      val selStart = codePane.getSelectionStart()
      val selEnd = codePane.getSelectionEnd()
      codePane.insert(WorksheetMarker + result, insertPos)
      if (dot == insertPos) {
        if (selStart == selEnd) {
          codePane.setCaretPosition(dot)
        }
        else {
          codePane.setSelectionStart(selStart)
          codePane.setSelectionEnd(selEnd)
        }
      }
    }

    def isStoryRunning = storyTeller.currStory.isDefined

    def baseDir: String = kojoCtx.baseDir
  }

  def makeCodeRunner: core.CodeRunner = {
    val codeRunner = AppMode.currentMode.scalaCodeRunner(runContext)
    codeRunner
  }

  def isRunningEnabled = runButton.isEnabled

  def setActivityListener(): Unit = {
    kojoCtx.setActivityListener(new core.AbstractSpriteListener {
      def interpreterDone = runButton.isEnabled
      override def hasPendingCommands: Unit = {
        pendingCommands = true
        stopButton.setEnabled(true)
      }
      override def pendingCommandsDone: Unit = {
        pendingCommands = false
        if (interpreterDone && Utils.noMonitoredThreads) stopButton.setEnabled(false)
      }
    })
  }

  def upload(): Unit = {
    val dlg = new codex.CodeExchangeForm(kojoCtx, true)
    Utils.closeOnEsc(dlg)
    dlg.setCanvas(tCanvas)
    dlg.setCode(codePane.getText())
    dlg.centerScreen()
  }

  def clrOutput(): Unit = {
    Utils.runInSwingThread {
      outputPane.clear()
      clearButton.setEnabled(false)
      codePane.requestFocusInWindow
    }
    outputPane.clearLastOutput()
  }

  def enableClearButton() = if (!clearButton.isEnabled) clearButton.setEnabled(true)

  @volatile var inputBeingRead = false

  def removeInputPanel(): Unit = {
    inputBeingRead = false
    outputPane.removeInputPanel()
  }

  def readInput(prompt: String): String = {
    if (Utils.inSwingThread) {
      throw new RuntimeException("Input can't be read from the GUI thread")
    }
    else {
      if (!inputBeingRead) {
        inputBeingRead = true
        val input = new FutureResult[String]
        Utils.runInSwingThread {
          val inputField = outputPane.createReadInputPanel(prompt)
          kojoCtx.activateOutputPane()
          Utils.schedule(0.1) {
            inputField.requestFocusInWindow();
            kojoCtx.scrollOutputToEnd()
          }
          Utils.schedule(0.9) {
            inputField.requestFocusInWindow()
          }
          inputField.addActionListener(new ActionListener {
            def actionPerformed(e: ActionEvent): Unit = {
              //          println("%s: %s" format (prompt, inputField.getText))
              input.set(inputField.getText)
              outputPane.removeInputPanel()
            }
          })
        }
        val ret = input.get
        inputBeingRead = false
        ret
      }
      else {
        throw new RuntimeException("Input reads cannot overlap")
      }
    }
  }

  def showOutput(outText: String) = outputPane.showOutput(outText)
  def showOutput(outText: String, color: Color) = outputPane.showOutput(outText, color)
  def resetErrInfo() = outputPane.resetErrInfo()
  def showError(errMsg: String) = outputPane.showError(errMsg)
  def showException(errText: String) = outputPane.showException(errText)
  def showSmartError(errText: String, line: Int, column: Int, offset: Int) =
    outputPane.showSmartError(errText, line, column, offset)

  def showWaitCursor(): Unit = {
    val wc = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
    codePane.setCursor(wc)
    tCanvas.setCursor(wc)
  }

  def showNormalCursor(): Unit = {
    val nc = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
    codePane.setCursor(nc)
    tCanvas.setCursor(nc)
  }

  def stopScript(): Unit = {
    if (traceRunning) {
      stopTraceScript()
    }
    else {
      stopInterpreter()
      stopActivity()
      removeInputPanel()
    }
  }

  def stopInterpreter() = Utils.runInSwingThread {
    codeRunner.interruptInterpreter()
  }

  def stopActivity() = Utils.runInSwingThread {
    Utils.stopMonitoredThreads()
    tCanvas.stop()
    fuguePlayer.stopMusic()
    fuguePlayer.stopBgMusic()
    mp3player.stopMp3()
    mp3player.stopMp3Loop()
    builtins.stopNotePlayer()
  }

  def invalidCode(code: String): Boolean = {
    if (code == null || code.trim.length == 0) true
    else false
  }

  def parseCode(browseAst: Boolean) = compileParseCode(false, browseAst)
  def compileCode() = compileParseCode(true, false)

  private def compileParseCode(check: Boolean, browseAst: Boolean): Unit = {
    val code = codePane.getText()

    if (invalidCode(code)) {
      return
    }

    statusStrip.onDocChange()
    enableRunButton(false)
    showWaitCursor()

    val cleanCode = cleanWsOutput(CodeToRun(code, None))
    if (check) {
      codeRunner.compileCode(cleanCode)
    }
    else {
      codeRunner.parseCode(cleanCode, browseAst)
    }
  }

  def isStory(code: String) = {
    code.indexOf("stPlayStory") != -1
  }

  lazy val worksheetPragma = "#" + Utils.loadBundleString("S_WorksheetPragma")
  lazy val execPragma = "#" + Utils.loadBundleString("S_ExecPragma")

  def isWorksheet(code: String) = {
    code.indexOf(worksheetPragma) != -1
  }

  def isToBeExeced(code: String) = {
    code.indexOf(execPragma) != -1
  }

  def runIpmCode(code: String): Unit = {
    stopScript()
    codeRunner.compileRunCode(code) // codeRunner.runCode(cleanWsOutput(code))
  }

  @volatile var traceRunning = false
  lazy val traceListener = new trace.TraceListener {
    val tracingGUI = new trace.TracingGUI(scriptEditor, kojoCtx)

    def onStart(): Unit = {
      resetErrInfo()
      enableRunButton(false)
      stopButton.setEnabled(true)
      traceRunning = true
      tracingGUI.reset()
    }

    def onMethodEnter(me: MethodEvent): Unit = {
      tracingGUI.addEnterEvent(me)
    }

    def onMethodExit(me: MethodEvent): Unit = {
      tracingGUI.addExitEvent(me)
    }

    def onEnd(): Unit = {
      activateEditor()
      enableRunButton(true)
      stopButton.setEnabled(false)
      traceRunning = false
    }
  }

  lazy val tracer = new trace.Tracing(builtins, traceListener, runContext)

  def traceCode(): Unit = {
    val code = codeToRun.code
    if (invalidCode(code)) {
      return
    }
    historyManager.codeRun(code)
    tracer.trace(code)
  }

  def stopTraceScript(): Unit = {
    tracer.stop()
  }

  case class CodeToRun(code: String, selection: Option[(Int, Int)])

  def codeToRun: CodeToRun = {
    val code = codePane.getText
    val selectedCode = codePane.getSelectedText
    if (selectedCode == null) {
      CodeToRun(code, None)
    }
    else {
      CodeToRun(selectedCode, Some(codePane.getSelectionStart, codePane.getSelectionEnd))
    }
  }

  def runCode(): Unit = Utils.runInSwingThread {
    runCode(codeToRun)
  }

  def compileRunCode(): Unit = Utils.runInSwingThread {
    compileRunCode(codeToRun)
  }

  def runWorksheet(): Unit = Utils.runInSwingThread {
    val code2run = codeToRun
    if (invalidCode(code2run.code)) {
      commandHistory.ensureLastEntryVisible()
      return
    }

    stopScript()
    runWorksheet(code2run)
  }

  // code/worksheet running needs to happen on the Swing thread
  def runCode(code: String): Unit = runCode(CodeToRun(code, None))

  private def runCode(code2run: CodeToRun): Unit = {
    if (invalidCode(code2run.code)) {
      commandHistory.ensureLastEntryVisible()
      return
    }

    stopScript()
    if (isWorksheet(code2run.code)) {
      runWorksheet(code2run)
    }
    else {
      val code = cleanWsOutput(code2run)
      preProcessCode(code)
      if (isStory(code)) {
        codeRunner.compileRunCode(code)
      }
      else {
        codeRunner.runCode(code)
      }
    }
  }

  def compileRunCode(code: String): Unit = compileRunCode(CodeToRun(code, None))

  private def compileRunCode(code2run: CodeToRun): Unit = {
    if (invalidCode(code2run.code)) {
      commandHistory.ensureLastEntryVisible()
      return
    }

    stopScript()
    if (isWorksheet(code2run.code)) {
      runWorksheet(code2run)
    }
    else {
      val code = cleanWsOutput(code2run)
      preProcessCode(code)
      if (code2run.selection.isEmpty) {
        if (isToBeExeced(code)) {
          codeRunner.compileExecCode(code)
        }
        else {
          codeRunner.compileRunCode(code)
        }
      }
      else {
        codeRunner.runCode(code)
      }
    }
  }

  def runWorksheet(code2run0: CodeToRun): Unit = {
    val code2run = extendSelection(code2run0)
    val cleanCode = removeWorksheetOutput(code2run.code)
    setWorksheetScript(cleanCode, code2run.selection)
    preProcessCode(cleanCode)
    codeRunner.runWorksheet(cleanCode)
  }

  // impure function!
  def extendSelection(code2run: CodeToRun) = {
    code2run.selection
      .map {
        case (selStart, selEnd) =>
          val selStartLine = codePane.getLineOfOffset(selStart)
          val selEndLine = codePane.getLineOfOffset(selEnd)
          val selStartLineStart = codePane.getLineStartOffset(selStartLine)
          val selStartLineEnd = getVisibleLineEndOffset(selStartLine)
          val selEndLineStart = codePane.getLineStartOffset(selEndLine)
          val selEndLineEnd = getVisibleLineEndOffset(selEndLine)
          val newSelStart = if (selStartLineEnd == selStart) selStart else selStartLineStart
          val newSelEnd = if (selEndLineStart == selEnd) selEnd else selEndLineEnd
          codePane.setSelectionStart(newSelStart)
          codePane.setSelectionEnd(newSelEnd)
          CodeToRun(codePane.getSelectedText, Some(newSelStart, newSelEnd))
      }
      .getOrElse(code2run)
  }

  // Impure function!
  def cleanWsOutput(code2run: CodeToRun) = {
    val code = code2run.code
    if (code.contains(WorksheetMarker)) {
      val cleanCode = removeWorksheetOutput(code)
      setWorksheetScript(cleanCode, code2run.selection)
      cleanCode
    }
    else {
      code
    }
  }

  def removeWorksheetOutput(code: String) = code.replaceAll(s"${WorksheetMarker}.*", "")

  private def getVisibleLineEndOffset(line: Int) = {
    def newLineBefore(pos: Int) = codePane.getText(pos - 1, 1) == "\n"
    val lineStart = codePane.getLineStartOffset(line)
    val lineEnd = codePane.getLineEndOffset(line)
    if (newLineBefore(lineEnd) && lineStart != lineEnd) lineEnd - 1 else lineEnd
  }

  var selectionOffset = 0

  def setWorksheetScript(code: String, selection: Option[(Int, Int)]) = Utils.runInSwingThread {
    val dot = codePane.getCaretPosition
    val line = codePane.getLineOfOffset(dot)
    val offsetInLine = dot - codePane.getLineStartOffset(line)
    if (selection.isDefined) {
      val selStart = selection.get._1
      selectionOffset = codePane.getLineOfOffset(selStart)
      codePane.replaceRange(code, selStart, selection.get._2)
      codePane.setSelectionStart(selStart)
      codePane.setSelectionEnd(selStart + code.length)
    }
    else {
      selectionOffset = 0
      codePane.setText(code)
      try {
        val lineStart = codePane.getLineStartOffset(line)
        val lineEnd = getVisibleLineEndOffset(line)
        val newLineSize = lineEnd - lineStart
        codePane.setCaretPosition(lineStart + math.min(offsetInLine, newLineSize))
      }
      catch {
        case t: Throwable =>
          println(s"Problem placing Carent: $t.getMessage")
          codePane.setCaretPosition(0)
      }
    }
  }

  def preProcessCode(code: String): Unit = {
    // now that we use the proxy code runner, disable the run button right away and change
    // the cursor so that the user gets some feedback the first time he runs something
    // - relevant if the proxy is still loading the real runner
    enableRunButton(false)
    showWaitCursor()

    if (isStory(code)) {
      // a story
      activateTw()
      storyTeller.storyComing()
    }

    if (showCode) {
      showOutput("\n>>>\n", promptColor)
      showOutput(code, codeColor)
      showOutput("\n<<<\n", promptColor)
    }
    else {
      outputPane.maybeOutputDelimiter()
    }
    historyManager.codeRun(code)
  }

  def codeFragment(caretOffset: Int) = {
    val cpt = codePane.getText
    if (caretOffset > cpt.length) ""
    else cpt.substring(0, caretOffset)
  }
  def varCompletions(prefix: Option[String]) = codeRunner.varCompletions(prefix)
  def keywordCompletions(prefix: Option[String]) = codeRunner.keywordCompletions(prefix)
  def memberCompletions(caretOffset: Int, objid: String, prefix: Option[String]) = {
    val codeAndOffset = completionCodeAndOffset(caretOffset)
    codeRunner.memberCompletions(codeAndOffset._1, codeAndOffset._2, objid, prefix)
  }
  def objidAndPrefix(caretOffset: Int): (Option[String], Option[String]) =
    xscala.CodeCompletionUtils.findIdentifier(codeFragment(caretOffset))
  def typeAt(caretOffset: Int) = {
    val codeAndOffset = completionCodeAndOffset(caretOffset)
    codeRunner.typeAt(codeAndOffset._1, codeAndOffset._2)
  }

  private def completionCodeAndOffset(caretOffset: Int): (String, Int) = {
    if (codePane == codePane1) {
      (codePane.getText, caretOffset)
    }
    else {
      val cp1text = codePane1.getText
      (s"${cp1text}\n${codePane.getText}", caretOffset + cp1text.length + 1)
    }
  }

  var imanip: Option[InteractiveManipulator] = None
  def addManipulator(im: InteractiveManipulator): Unit = {
    imanip = Some(im)
  }
  def removeManipulator(im: InteractiveManipulator): Unit = {
    imanip = None
  }

  def loadCodeFromHistoryPrev() = historyManager.historyMoveBack
  def loadCodeFromHistoryNext() = historyManager.historyMoveForward
  def loadCodeFromHistory(historyIdx: Int) = historyManager.setCode(historyIdx)

  class HistoryManager {

    def historyMoveBack: Unit = {
      // depend on history listener mechanism to move back
      val prevCode = commandHistory.previous
      hPrevButton.setEnabled(commandHistory.hasPrevious)
      hNextButton.setEnabled(true)
      //      commandHistory.ensureLastEntryVisible()
    }

    def historyMoveForward: Unit = {
      // depend on history listener mechanism to move forward
      val nextCode = commandHistory.next
      if (!nextCode.isDefined) {
        hNextButton.setEnabled(false)
      }
      hPrevButton.setEnabled(true)
      //      commandHistory.ensureLastEntryVisible()
    }

    def updateButtons(historyIdx: Int): Unit = {
      if (commandHistory.size > 0 && historyIdx != 0)
        hPrevButton.setEnabled(true)
      else
        hPrevButton.setEnabled(false)

      if (historyIdx < commandHistory.size)
        hNextButton.setEnabled(true)
      else
        hNextButton.setEnabled(false)
    }

    def setCode(historyIdx: Int): Unit = {
      updateButtons(historyIdx)
      val codeAtIdx = commandHistory.toPosition(historyIdx)

      if (codeAtIdx.isDefined) {
        codePane.setText(codeAtIdx.get)
        codePane.setCaretPosition(0)
      }

      //        codePane.requestFocusInWindow
    }

    def codeRunError() = {}

    def codeRun(code: String): Unit = {
      val tcode = code.trim()
      commandHistory.add(code, openedFile.map(f => "%s (%s)".format(f.getName, f.getParent)))
    }
  }

  def activateEditor() = kojoCtx.activateScriptEditor()

  @volatile var codingMode: CodingMode = AppMode.currentMode.defaultCodingMode

  def activateTw(): Unit = {
    if (codingMode != TwMode) {
      codingMode = TwMode
      codeRunner.activateTw()
    }
  }

  def activateVn(): Unit = {
    if (codingMode != VanillaMode) {
      codingMode = VanillaMode
      codeRunner.activateVn()
    }
  }

  def isTwActive = codingMode == TwMode
  def isVnActive = codingMode == VanillaMode

  def knownColors = kojoCtx.knownColors
  def knownColor(name: String): Color = kojoCtx.knownColor(name)
  def knownColors2 = kojoCtx.knownColors2
  def knownColor2(name: String) = kojoCtx.knownColor2(name)
  def frame = kojoCtx.frame
}
