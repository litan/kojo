package net.kogics.kojo.lite

import java.awt.Frame
import java.awt.GridLayout
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.UIManager
import javax.swing.WindowConstants
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import net.kogics.kojo.d3.Canvas3D
import net.kogics.kojo.lite.canvas.SpriteCanvas
import net.kogics.kojo.lite.topc.D3CanvasHolder
import net.kogics.kojo.lite.topc.DrawingCanvasHolder
import net.kogics.kojo.lite.topc.HistoryHolder
import net.kogics.kojo.lite.topc.MathworldHolder
import net.kogics.kojo.lite.topc.OutputWindowHolder
import net.kogics.kojo.lite.topc.ScriptEditorHolder
import net.kogics.kojo.lite.topc.StoryTellerHolder
import net.kogics.kojo.mathworld.GeoGebraCanvas
import net.kogics.kojo.story.StoryTeller
import net.kogics.kojo.util.Utils
import net.kogics.kojo.xscala.Builtins
import bibliothek.gui.dock.common.CControl
import bibliothek.gui.dock.common.CGrid
import bibliothek.gui.dock.common.mode.ExtendedMode
import bibliothek.gui.dock.common.theme.ThemeMap

object Main extends AppMenu {

  @volatile var codePane: RSyntaxTextArea = _
  @volatile var scriptEditorH: ScriptEditorHolder = _
  @volatile var frame: JFrame = _
  @volatile var splash: SplashScreen = _
  @volatile var codeSupport: CodeExecutionSupport = _
  @volatile var kojoCtx: KojoCtx = _

  def main(args: Array[String]): Unit = {
    realMain(args)
  }

  def appExit() {
    try {
      codeSupport.closing()
      frame.dispose()
      System.exit(0)
    }
    catch {
      case rte: RuntimeException =>
    }
  }

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
      scriptEditorH.activate()
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
      Builtins.instance.stClickRunButton
    }
  }

  def realMain(args: Array[String]): Unit = {
    System.setSecurityManager(null)
    runMultiInstancehandler()

    Utils.runInSwingThread {
      splash = new SplashScreen()
    }

    Utils.schedule(0.3) {
      import javax.swing.UIManager

      val xx = UIManager.getInstalledLookAndFeels.find { _.getName == "Nimbus" }.foreach { nim =>
        UIManager.setLookAndFeel(nim.getClassName)
      }

      frame = new JFrame("Kojo  \u2248Ray")
      frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE)
      val control = new CControl(frame)
      val themes = control.getThemes()
      themes.select(ThemeMap.KEY_ECLIPSE_THEME)
      frame.setLayout(new GridLayout(1, 1))
      frame.add(control.getContentArea)

      kojoCtx = new KojoCtx
      SpriteCanvas.initedInstance(kojoCtx)
      StoryTeller.initedInstance(kojoCtx)
      GeoGebraCanvas.initedInstance(kojoCtx)
      Canvas3D.initedInstance(kojoCtx)

      codePane = new RSyntaxTextArea(5, 80)
      codeSupport = CodeExecutionSupport.initedInstance(codePane, kojoCtx)
      val drawingCanvasH = new DrawingCanvasHolder(SpriteCanvas.instance, kojoCtx)
      scriptEditorH = new ScriptEditorHolder(new JPanel(), codePane, codeSupport, frame)
      val outputHolder = new OutputWindowHolder(codeSupport.outputWindow, codeSupport.errorWindow, codeSupport.outPanel, kojoCtx)
      val storyHolder = new StoryTellerHolder(StoryTeller.instance)
      val mwHolder = new MathworldHolder(GeoGebraCanvas.instance, kojoCtx)
      val d3Holder = new D3CanvasHolder(Canvas3D.instance, kojoCtx)
      val historyHolder = new HistoryHolder(new JPanel(), kojoCtx, codeSupport)

      kojoCtx.topcs = TopCs(drawingCanvasH, outputHolder, scriptEditorH, storyHolder, mwHolder, d3Holder)
      kojoCtx.frame = frame
      kojoCtx.codeSupport = codeSupport

      val grid = new CGrid(control)
      grid.add(1, 0, 4, 2, d3Holder)
      grid.add(1, 0, 4, 2, mwHolder)
      grid.add(1, 0, 4, 2, drawingCanvasH)
      grid.add(1, 2, 2.5, 1, scriptEditorH)
      grid.add(3.5, 2, 1.5, 1, outputHolder)
      grid.add(0, 0, 2, 4, historyHolder)
      grid.add(0, 0, 1, 4, storyHolder)
      control.getContentArea.deploy(grid)

      historyHolder.setExtendedMode(ExtendedMode.MINIMIZED)
      storyHolder.setExtendedMode(ExtendedMode.MINIMIZED)

      frame.setJMenuBar(menuBar)

      splash.close()

      frame.setBounds(100, 100, 600, 500)
      frame.addWindowListener(new WindowAdapter {
        override def windowClosing(e: WindowEvent) {
          appExit()
        }
      })
      //      frame.pack()
      frame.setIconImage(Utils.loadImage("/images/kojo48.png"))
      frame.setVisible(true)
      frame.setExtendedState(Frame.MAXIMIZED_BOTH)

      if (args.length == 1) {
        loadAndRunUrl(args(0), true)
      }
      else {
        Utils.schedule(1) {
          scriptEditorH.activate()
        }
      }
    }
  }

  def runMultiInstancehandler() {
    MultiInstanceHandler.run()
  }
}
