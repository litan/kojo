package net.kogics.kojo.lite

import java.awt.Frame
import java.awt.GridLayout
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.io.File
import java.util.logging.FileHandler
import java.util.logging.Level
import java.util.logging.Logger
import java.util.logging.SimpleFormatter

import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.UIManager
import javax.swing.WindowConstants

import scala.collection.convert.WrapAsScala.propertiesAsScalaMap

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea

import net.kogics.kojo.d3.Canvas3D
import net.kogics.kojo.history.HistoryPanel
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
import bibliothek.gui.dock.common.theme.ThemeMap

object Main extends AppMenu {
  System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tc, %3$s] %4$s: %5$s%n")

  @volatile var codePane: RSyntaxTextArea = _
  @volatile var scriptEditorH: ScriptEditorHolder = _
  @volatile var frame: JFrame = _
  @volatile var splash: SplashScreen = _
  @volatile var codeSupport: CodeExecutionSupport = _
  @volatile var kojoCtx: KojoCtx = _

  def main(args: Array[String]): Unit = {
    realMain(args)
  }

  def setupLogging() {
    val userHome = System.getProperty("user.home")
    val logDir = new File(s"$userHome/.kojo/lite/log/")
    if (!logDir.exists()) {
      logDir.mkdirs()
    }
    val logPath = List(userHome, ".kojo", "lite", "log", "kojo0.log").mkString(File.separator)
    System.err.println(s"Logging has been redirected to: $logPath")
    val rootLogger = Logger.getLogger("")
    val logHandler = new FileHandler("%h/.kojo/lite/log/kojo%g.log", 1 * 1024 * 1024, 6, false)
    logHandler.setFormatter(new SimpleFormatter())
    logHandler.setLevel(Level.INFO)
    rootLogger.removeHandler(rootLogger.getHandlers()(0))
    rootLogger.setLevel(Level.INFO)
    rootLogger.addHandler(logHandler)
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

  def loadAndRunResource(res: String) = {
    try {
      codePane.setText("")
      val code = Utils.loadResource(res)
      codePane.setText(Utils.stripCR(code))
      codePane.setCaretPosition(0)
      codePane.setCaretPosition(0)
      Builtins.instance.stClickRunButton
    }
    catch {
      case t: Throwable => codePane.append("// Problem loading code: %s" format (t.getMessage))
    }
    scriptEditorH.activate()
  }

  def realMain(args: Array[String]): Unit = {
    System.setSecurityManager(null)
    kojoCtx = new KojoCtx // context needs to be created right up front to set user language
    Utils.runInSwingThreadAndWait {
      splash = new SplashScreen()
    }

    setupLogging()
    val Log = Logger.getLogger("Main")

    runMultiInstancehandler()

    Utils.schedule(0.3) {
      import javax.swing.UIManager
      // using println here clobbers output redirection in CodeExecutionSupport
      // System.out.println("Desktop Session is: " + System.getenv("DESKTOP_SESSION"))
      val xx = UIManager.getInstalledLookAndFeels.find { _.getName == "Nimbus" }.foreach { nim =>
        UIManager.setLookAndFeel(nim.getClassName)
      }

      frame = new JFrame(Utils.loadString("S_Kojo"))
      frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE)
      val control = new CControl(frame)
      val themes = control.getThemes()
      themes.select(ThemeMap.KEY_ECLIPSE_THEME)
      frame.setLayout(new GridLayout(1, 1))
      frame.add(control.getContentArea)

      SpriteCanvas.initedInstance(kojoCtx)
      StoryTeller.initedInstance(kojoCtx)
      GeoGebraCanvas.initedInstance(kojoCtx)
      Canvas3D.initedInstance(kojoCtx)

      codePane = new RSyntaxTextArea(5, 80)
      codeSupport = CodeExecutionSupport.initedInstance(codePane, kojoCtx)

      kojoCtx.frame = frame
      kojoCtx.codeSupport = codeSupport
      kojoCtx.control = control
      kojoCtx.storyTeller = StoryTeller.instance

      val drawingCanvasH = new DrawingCanvasHolder(SpriteCanvas.instance, kojoCtx)
      scriptEditorH = new ScriptEditorHolder(new JPanel(), codePane, codeSupport, frame)
      val outputHolder = new OutputWindowHolder(codeSupport, kojoCtx)
      val storyHolder = new StoryTellerHolder(StoryTeller.instance)
      val mwHolder = new MathworldHolder(GeoGebraCanvas.instance, kojoCtx)
      val d3Holder = new D3CanvasHolder(Canvas3D.instance, kojoCtx)
      val historyHolder = new HistoryHolder(new HistoryPanel(codeSupport))

      kojoCtx.topcs = TopCs(drawingCanvasH, outputHolder, scriptEditorH, storyHolder, mwHolder, d3Holder, historyHolder)
      kojoCtx.switchToDefaultPerspective()

      frame.setJMenuBar(menuBar)

      splash.close()

      frame.addWindowListener(new WindowAdapter {
        override def windowClosing(e: WindowEvent) {
          appExit()
        }
      })
      frame.setIconImage(Utils.loadImage("/images/kojo48.png"))
      frame.setExtendedState(Frame.MAXIMIZED_BOTH)
      frame.pack()

      //      The following (basically setBounds) triggers the menu offset bug on Gnome 3 
      //      val screenSize = Toolkit.getDefaultToolkit.getScreenSize
      //      frame.setBounds(50, 50, screenSize.width - 100, screenSize.height - 100)

      frame.setVisible(true)

      if (args.length == 1) {
        loadAndRunUrl(args(0), true)
      }
      else {
        Utils.schedule(1) { scriptEditorH.activate() }
        Utils.schedule(2) { scriptEditorH.activate() }
      }
    }

    // Do startup logging after scheduling the GUI stuff
    Log.info(s"Kojo version: ${Versions.KojoVersion}")
    Log.info(s"Java version: ${Versions.JavaVersion}. Scala version: ${Versions.ScalaVersion}")
    val sysProps =
      System.getProperties.toList.sorted.foldLeft(new StringBuilder) { case (sb, kv) => sb append s"\n${kv._1} = ${kv._2}" }
    Log.info(s"System Properties:${sysProps}\n\n")
  }

  def runMultiInstancehandler() {
    MultiInstanceHandler.run()
  }
}
