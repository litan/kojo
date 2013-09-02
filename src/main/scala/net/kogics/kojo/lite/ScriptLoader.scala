package net.kogics.kojo.lite

import net.kogics.kojo.util.Utils

import net.kogics.kojo.lite.Main

trait ScriptLoader { self: Main.type =>
  def _loadUrl(url: String)(postfn: => Unit = {}) {
    codePane.setText("// Loading code from URL: %s ...\n" format (url))
    Utils.runAsyncMonitored {
      try {
        val code = Utils.readUrl(url)
        Utils.runInSwingThread {
          codePane.setText(Utils.stripCR(code))
          codePane.setCaretPosition(0)
          postfn
        }
      }
      catch {
        case t: Throwable => codePane.append("// Problem loading code: %s" format (t.getMessage))
      }
      scriptEditorHolder.activate()
    }
  }

  def loadUrl(url: String) = _loadUrl(url) {}

  def loadAndRunUrl(url: String, onStartup: Boolean = false) = _loadUrl(url) {
    if (!url.startsWith("http://www.kogics.net/public/kojolite/samples/") && codePane.getText.toLowerCase.contains("file")) {
      codePane.insert("// Loaded code from URL: %s\n// ** Not running it automatically ** because it references files.\n// Look carefully at the code before running it.\n\n" format (url), 0)
      codePane.setCaretPosition(0)
    }
    else {
      val msg2 = if (onStartup) "\n// Please wait, this might take a few seconds as Kojo starts up..." else ""
      codePane.insert("// Running code loaded from URL: %s%s\n\n" format (url, msg2), 0)
      codePane.setCaretPosition(0)
      execSupport.runCode()
    }
  }

  def loadAndRunResource(res: String) = {
    try {
      codePane.setText("")
      val code = Utils.loadResource(res)
      codePane.setText(Utils.stripCR(code))
      codePane.setCaretPosition(0)
      execSupport.runCode()
    }
    catch {
      case t: Throwable => codePane.append("// Problem loading/running code: %s" format (t.getMessage))
    }
    scriptEditorHolder.activate()
  }

  // No Editor Load version
  def loadAndRunResourceNEL(res: String) = {
    try {
      val code = Utils.loadResource(res)
      execSupport.runCode(code)
    }
    catch {
      case t: Throwable => println("// Problem loading/running code: %s" format (t.getMessage))
    }
    scriptEditorHolder.activate()
  }
}