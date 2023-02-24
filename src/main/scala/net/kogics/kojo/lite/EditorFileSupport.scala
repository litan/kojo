package net.kogics.kojo.lite

import java.io.File
import javax.swing.JOptionPane

import net.kogics.kojo.util.RichFile
import net.kogics.kojo.util.Utils

trait EditorFileSupport { self: ScriptEditor =>
  var openedFile: Option[File] = None
  private var fileData: String = _
  val kojoCtx = execSupport.kojoCtx

  private def saveFileData(d: String): Unit = {
    fileData = execSupport.removeWorksheetOutput(d)
  }
  private def fileChanged = fileData != execSupport.removeWorksheetOutput(codePane.getText)

  def hasOpenFile = openedFile.isDefined

  def openFileWithoutClose(file: java.io.File): Unit = {
    import RichFile._
    val script = Utils.stripCR(file.readAsString)
    codePane.setText(script)
    codePane.setCaretPosition(0)
    openedFile = Some(file)
    kojoCtx.fileOpened(file)
    saveFileData(script)
  }

  def openFile(file: java.io.File): Unit = {
    try {
      closeFileIfOpen()
      openFileWithoutClose(file)
    }
    catch {
      case e: RuntimeException =>
    }
  }

  def closeFileIfOpen(): Unit = {
    if (openedFile.isDefined) {
      if (fileChanged) {
        val doSave = JOptionPane.showConfirmDialog(
          kojoCtx.frame,
          Utils.loadString("S_FileChanged").format(openedFile.get.getName, openedFile.get.getName)
        )
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

  def closeFileAndClrEditorIgnoringCancel(): Unit = {
    try {
      closeFileAndClrEditor()
    }
    catch {
      case e: RuntimeException => // ignore user cancel
    }
  }

  def closeFileAndClrEditor(): Unit = {
    closeFileIfOpen() // can throw runtime exception if user cancels
    this.codePane.setText(null)
    clearSButton.setEnabled(false)
    codePane.requestFocusInWindow
  }

  def saveFile(): Unit = {
    saveTo(openedFile.get)
    kojoCtx.fileSaved()
  }

  import java.io.File
  private def saveTo(file: File): Unit = {
    import RichFile._
    val script = codePane.getText()
    file.write(script)
    saveFileData(script)
  }

  def saveAs(file: java.io.File): Unit = {
    if (file.exists) {
      val doSave = JOptionPane.showConfirmDialog(kojoCtx.frame, Utils.loadString("S_FileExists").format(file.getName))
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

  def closing(): Unit = {
    if (openedFile.isDefined) {
      closeFileIfOpen()
    }
    else {
      if (kojoCtx.subKojo && codePane.getText.size > 0) {
        val doSave = JOptionPane.showOptionDialog(
          kojoCtx.frame,
          Utils.loadString("S_Unsaved_Code"),
          Utils.loadString("S_Exit_Scratchpad"),
          JOptionPane.YES_NO_OPTION,
          JOptionPane.WARNING_MESSAGE,
          null,
          null,
          null
        )
        if (doSave == JOptionPane.NO_OPTION || doSave == JOptionPane.CLOSED_OPTION) {
          throw new RuntimeException("Veto Shutdown")
        }
      }
    }
  }

}
